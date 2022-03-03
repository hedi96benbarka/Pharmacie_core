/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.repository;

import com.csys.exception.IllegalBusinessLogiqueException;

import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.stock.factory.DepstoDTOAssembler;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticleIMMODTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.achat.dto.CategorieArticleDTO;
import com.csys.pharmacie.achat.dto.CategorieDepotDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.service.ImmobilisationService;
import com.csys.pharmacie.client.dto.ImmobilisationDTO;
import com.csys.pharmacie.client.dto.ListeImmobilisationDTOWrapper;

import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import static com.csys.pharmacie.helper.CategorieDepotEnum.PH;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.inventaire.domain.DepStoHist;
import com.csys.pharmacie.inventaire.domain.DetailInventaire;
import com.csys.pharmacie.inventaire.domain.Inventaire;
import com.csys.pharmacie.inventaire.dto.DepstoScanDTO;
import com.csys.pharmacie.inventaire.dto.EtatEcartInventaire;
import com.csys.pharmacie.inventaire.dto.InventaireDTO;
import com.csys.pharmacie.inventaire.dto.TypeEnvoieEtatEnum;
import com.csys.pharmacie.inventaire.repository.DepStoHistRepository;
import com.csys.pharmacie.inventaire.repository.DepstoScanRepository;
import com.csys.pharmacie.inventaire.repository.DetailInventaireRepository;
import com.csys.pharmacie.inventaire.repository.InventaireRepository;
import com.csys.pharmacie.inventaire.service.DepstoScanService;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.dto.DepstoDTO;
import com.csys.pharmacie.stock.dto.DepstoDetailArticleDTO;
import com.csys.pharmacie.stock.factory.DepstoFactory;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import static java.util.Comparator.comparing;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Mahmoud
 */
@Service("InvDepstoService")
public class InvDepstoService {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    private final Logger log = LoggerFactory.getLogger(StockService.class);

    @Autowired
    private DepstoRepository depstorepository;
    private final ParamAchatServiceClient paramAchatServiceClient;

    @Autowired
    private InventaireRepository inventaireRepository;
    @Autowired
    private StockService stockService;
    @Autowired
    private DetailInventaireRepository detailInventaireRepository;
    @Autowired
    private InventaireService inventaireService;
    @Autowired
    private DepStoHistRepository depStoHistRepository;
    @Autowired
    private DepstoScanRepository depstoScanRepository;
    @Autowired
    private DepstoScanService depstoScanService;
    @Autowired
    private ImmobilisationService immobilisationService;

    @Value("${lang.secondary}")
    private String langage;

    public InvDepstoService(ParamAchatServiceClient paramAchatServiceClient) {
        this.paramAchatServiceClient = paramAchatServiceClient;
    }

    public List<DepstoDTO> findDepstoByCodeIn(List<Integer> ids) {

        List<DepstoDTO> depStoDTO = new ArrayList<>();
        List<Depsto> Depsto = depstorepository.findByCodeIn(ids);
        Preconditions.checkBusinessLogique(Depsto != null, "stock inexistant");
        for (Depsto elem : Depsto) {
            depStoDTO.add(DepstoDTOAssembler.assemblePH(elem, null));
        }
        return depStoDTO;
    }

    public DepstoDTO saveDepsto(Integer idArticle, CategorieDepotEnum categ_depot, Integer coddep, Integer codeUnite) {

        List<Integer> idList = new ArrayList<>();
        idList.add(idArticle);
        List<ArticleDTO> listeArticle = paramAchatServiceClient.articleFindbyListCodeWithLanguageFR(idList);
        Preconditions.checkBusinessLogique(listeArticle.size() > 0, "Article inexistant");

        listeArticle.stream().filter(item -> Boolean.TRUE.equals(item.getStopped())).findAny().ifPresent(item -> {
            throw new IllegalBusinessLogiqueException("item.stopped", new Throwable(item.getCodeSaisi()));
        });
        
        UniteDTO uniteDTO = paramAchatServiceClient.findUniteByCode(codeUnite);
        Preconditions.checkBusinessLogique(uniteDTO != null, "missing-unit", codeUnite.toString());
        
        List<Depsto> depstos = depstorepository.findByCategDepotAndCodartAndUniteAndCoddep(categ_depot, idArticle, codeUnite, coddep);
        Preconditions.checkBusinessLogique(depstos.isEmpty(), "article.unite.depot.existant");

        Depsto depSto = new Depsto();
        depSto.setCategDepot(categ_depot);
        depSto.setDatesys(LocalDateTime.now());
        depSto.setCoddep(coddep);
        depSto.setCodart(listeArticle.get(0).getCode());
        depSto.setNumBonOrigin("AJART001");
        depSto.setNumBon("AJART001");
        depSto.setLotInter("*");
        depSto.setDatPer(LocalDate.now().plusYears(2));
        depSto.setQte(BigDecimal.ZERO);
        depSto.setPu(BigDecimal.ZERO);
        depSto.setUnite(Integer.valueOf(codeUnite.toString().substring(0, 3)));
        depSto.setTauxTva(listeArticle.get(0).getValeurTvaAch());
        depSto.setCodeTva(listeArticle.get(0).getCodeTvaAch());
        Depsto saveDepSto = depstorepository.save(depSto);

        return DepstoDTOAssembler.assemble(saveDepSto, new ArticleDTO());

    }

    public List<EtatEcartInventaire> etatEcartAvantVldGlobByCodDepCathegDepCodInv(Integer coddep, CategorieDepotEnum categ_depot, Integer codeInventaire, TypeEnvoieEtatEnum optionImpression, Boolean withPrincipalUnity) {

        List<EtatEcartInventaire> listeEtatEcartInventaire = new ArrayList<>();

        //Controle Si l'inventaire et encore ouverte ou nn
        Inventaire inventaire = inventaireRepository.findOne(codeInventaire);
        Preconditions.checkBusinessLogique(inventaire != null, "inventaire.depot-inexistant");
        Preconditions.checkBusinessLogique(inventaire.getDateCloture() == null, "inventaire.depot-ferme");
        //Importer la liste de tous les articles dans ce depot et cathégorie article
        List<DepstoDTO> depStoDTO;
        depStoDTO = stockService.findAll(coddep, categ_depot, null, true, true, false, null);
        Set<Integer> articleIdsSet = depStoDTO.stream().map(DepstoDTO::getArticleID).collect(Collectors.toSet());
        List<Integer> articleIds = new ArrayList<>(articleIdsSet);

        // la liste des cathégorie article pour la ccathegorie ouverte dans l'inventaire 
        List<DetailInventaire> lisCategorieArticle = detailInventaireRepository.findByDetailInventairePK_Inventaire(inventaire.getCode());

        //liste des cathegrie dans liste of string
        List<String> listeCathegArticle = lisCategorieArticle.stream()
                .map(d -> d.getDetailInventairePK().getCategorieArticle().toString())
                .collect(Collectors.toList());
        //filtrer les article qui appartient a la liste des cathégorie article
        depStoDTO = depStoDTO.stream().
                filter(d -> listeCathegArticle.contains(d.getCategorieArticle())).
                collect(Collectors.toList());

        //Mettre la valeur du stock reel dans StkRel2 ----- valeur stock theorique dans stkRel3
        depStoDTO.forEach(d -> {
            if (d.getQuantiteReel() == null) {
                d.setQuantiteReel(BigDecimal.ZERO);
            }
            if (d.getQuantiteTheorique() == null) {
                d.setQuantiteTheorique(BigDecimal.ZERO);
            }
            d.setSommeValReel(d.getQuantiteReel().multiply(d.getPrixAchat()));
            d.setSommeValReelTTC(Helper.applyTaxesToPrice(d.getSommeValReel(), d.getTauxTva()));
            d.setSommeValtheorique(d.getQuantiteTheorique().multiply(d.getPrixAchat()));
            d.setSommeValtheoriqueTTC(Helper.applyTaxesToPrice(d.getSommeValtheorique(), d.getTauxTva()));
        });

        /**
         * elimminer les doublant dans une autre liste
         */
        List<DepstoDTO> simplifiedResult = new ArrayList();
        depStoDTO.stream()
                .collect(groupingBy(DepstoDTO::getArticleID,
                        groupingBy(DepstoDTO::getUnityCode,
                                Collectors.reducing(new DepstoDTO(BigDecimal.ZERO), (a, b) -> {
                                    if (b.getQuantiteReel() == null) {
                                        b.setQuantiteReel(BigDecimal.ZERO);
                                    }
                                    if (a.getQuantiteReel() == null) {
                                        a.setQuantiteReel(BigDecimal.ZERO);
                                    }

                                    if (b.getQuantiteTheorique() == null) {
                                        b.setQuantiteTheorique(BigDecimal.ZERO);
                                    }
                                    if (a.getQuantiteTheorique() == null) {
                                        a.setQuantiteTheorique(BigDecimal.ZERO);
                                    }

                                    if (a.getSommeValReel() == null) {
                                        a.setSommeValReel(BigDecimal.ZERO);
                                    }

                                    if (b.getSommeValReel() == null) {
                                        b.setSommeValReel(BigDecimal.ZERO);
                                    }

                                    if (a.getSommeValtheorique() == null) {
                                        a.setSommeValtheorique(BigDecimal.ZERO);
                                    }

                                    if (b.getSommeValtheorique() == null) {
                                        b.setSommeValtheorique(BigDecimal.ZERO);
                                    }

                                    if (a.getSommeValReel() == null) {
                                        a.setSommeValReel(BigDecimal.ZERO);
                                    }

                                    if (b.getSommeValReel() == null) {
                                        b.setSommeValReel(BigDecimal.ZERO);
                                    }
                                    if (a.getSommeValReelTTC() == null) {
                                        a.setSommeValReelTTC(BigDecimal.ZERO);
                                    }

                                    if (b.getSommeValReelTTC() == null) {
                                        b.setSommeValReel(BigDecimal.ZERO);
                                    }
                                    if (a.getSommeValtheorique() == null) {
                                        a.setSommeValtheorique(BigDecimal.ZERO);
                                    }

                                    if (b.getSommeValtheorique() == null) {
                                        b.setSommeValtheorique(BigDecimal.ZERO);
                                    }
                                    if (a.getSommeValtheoriqueTTC() == null) {
                                        a.setSommeValtheoriqueTTC(BigDecimal.ZERO);
                                    }

                                    if (b.getSommeValtheoriqueTTC() == null) {
                                        b.setSommeValtheoriqueTTC(BigDecimal.ZERO);
                                    }

                                    if (b.getLotInter() == null) {
                                        b.setLotInter("");
                                    }

                                    if (categ_depot == CategorieDepotEnum.EC) {
                                        b.setPerissable(false);
                                    }
                                    b.setQuantiteReel(b.getQuantiteReel().add(a.getQuantiteReel()));
                                    //prix reel dans achat
                                    b.setSommeValReel(b.getSommeValReel().add(a.getSommeValReel()));
                                    b.setSommeValtheorique(b.getSommeValtheorique().add(a.getSommeValtheorique()));
                                    b.setSommeValReelTTC(b.getSommeValReelTTC().add(a.getSommeValReelTTC()));
                                    b.setSommeValtheoriqueTTC(b.getSommeValtheoriqueTTC().add(a.getSommeValtheoriqueTTC()));
                                    b.setQuantiteTheorique(b.getQuantiteTheorique().add(a.getQuantiteTheorique()));
                                    b.setQuantity(BigDecimal.ZERO);

                                    return b;
                                }))))
                .forEach((k, v) -> {
                    simplifiedResult.addAll(v.values());
                });

        for (DepstoDTO listeDepstoDTO : simplifiedResult) {

            EtatEcartInventaire etatEcartInventaire = new EtatEcartInventaire();
            etatEcartInventaire.setCodart(listeDepstoDTO.getArticleID());
            etatEcartInventaire.setCoddep(coddep);
            etatEcartInventaire.setCodeCathegDepot(categ_depot);
            etatEcartInventaire.setCodeInventaire(inventaire.getDepot());

            CategorieDepotDTO categorieDepotDTO = paramAchatServiceClient.findCategorieDepot(listeDepstoDTO.getCategorieDepot().toString());

            etatEcartInventaire.setDesCathegorieDepot(categorieDepotDTO.getDesignation());
            etatEcartInventaire.setDesignation(listeDepstoDTO.getDesignation());
            etatEcartInventaire.setDesignationDepot(paramAchatServiceClient.findDepotByCode(coddep).getDesignation());

            etatEcartInventaire.setQuantiteReel(listeDepstoDTO.getQuantiteReel());
            etatEcartInventaire.setQuantiteTheorique(listeDepstoDTO.getQuantiteTheorique());
            etatEcartInventaire.setUnityCode(listeDepstoDTO.getUnityCode());
            etatEcartInventaire.setUnityDesignation(listeDepstoDTO.getUnityDesignation());
            etatEcartInventaire.setCodeSaisie(inventaire.getCodeSaisie());
            etatEcartInventaire.setCodeCategorieArticle(listeDepstoDTO.getCategorieArticle());
            etatEcartInventaire.setValeurReel(listeDepstoDTO.getSommeValReel());
            etatEcartInventaire.setValeurTheorique(listeDepstoDTO.getSommeValtheorique());
            etatEcartInventaire.setValeurReelTTC(listeDepstoDTO.getSommeValReelTTC());
            etatEcartInventaire.setValeurTheoriqueTTC(listeDepstoDTO.getSommeValtheoriqueTTC());
            etatEcartInventaire.setCodeSaisiArticle(listeDepstoDTO.getCodeSaisiArticle());
            Integer codeCateg = Integer.parseInt(listeDepstoDTO.getCategorieArticle());
            //etatEcartInventaire.setCategorieArticle(paramAchatServiceClient.findNodesByCategorieArticleParent(Integer.parseInt(listeDepstoDTO.getCategorieArticle())));
            List<CategorieArticleDTO> listeCategArticle = paramAchatServiceClient.findNodesByCategorieArticleParent(Integer.parseInt(listeDepstoDTO.getCategorieArticle()));
            listeCategArticle.forEach(d -> {
                if (d.getCode().equals(codeCateg)) {
                    etatEcartInventaire.setDesignationCategorieArt(d.getDesignation());
                    if (langage.equals(LocaleContextHolder.getLocale().getLanguage())) {
                        etatEcartInventaire.setCategorieArticle(d.getDesignationSec());
                    } else {
                        etatEcartInventaire.setCategorieArticle(d.getDesignation());
                    }
                }
            });

            if ((optionImpression == TypeEnvoieEtatEnum.AE || optionImpression == TypeEnvoieEtatEnum.ALL) && etatEcartInventaire.getQuantiteReel().compareTo(etatEcartInventaire.getQuantiteTheorique()) != 0) {
                listeEtatEcartInventaire.add(etatEcartInventaire);
            }
            if ((optionImpression == TypeEnvoieEtatEnum.SE || optionImpression == TypeEnvoieEtatEnum.ALL) && etatEcartInventaire.getQuantiteReel().compareTo(etatEcartInventaire.getQuantiteTheorique()) == 0) {
                listeEtatEcartInventaire.add(etatEcartInventaire);
            }
        }
        //Ordonner la liste des ecart selon la designation
        Collections.sort(listeEtatEcartInventaire, (d1, d2) -> d1.getDesignation().toUpperCase().compareToIgnoreCase(d2.getDesignation().toUpperCase()));
        if (CategorieDepotEnum.PH.equals(categ_depot)) {
            if (Boolean.TRUE.equals(withPrincipalUnity)) {
                List<ArticlePHDTO> listArticlePHForMap = paramAchatServiceClient.articlePHFindbyListCode(articleIds);
                List<EtatEcartInventaire> listeEtatEcartInventaireWithPrincipalUnity = listeEtatEcartInventaire.stream().map(etatEcart -> {

                    ArticlePHDTO matchedArticled = listArticlePHForMap.stream()
                            .filter(art -> art.getCode().equals(etatEcart.getCodart())).findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));
                    ArticleUniteDTO matchedUnite = matchedArticled.getArticleUnites().stream()
                            .filter(unity -> unity.getCodeUnite().equals(etatEcart.getUnityCode()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unity"));
                    BigDecimal qteInPrincipaleUnit = etatEcart.getQuantiteReel().divide(matchedUnite.getNbPiece(), 2, RoundingMode.HALF_UP);
                    etatEcart.setQuantiteReel(qteInPrincipaleUnit);
                    BigDecimal qteTheoriqueInPrincipaleUnit = etatEcart.getQuantiteTheorique().divide(matchedUnite.getNbPiece(), 2, RoundingMode.HALF_UP);
                    etatEcart.setQuantiteTheorique(qteTheoriqueInPrincipaleUnit);
                    etatEcart.setUnityCode(matchedArticled.getCodeUnite());
                    etatEcart.setUnityDesignation(matchedArticled.getDesignationUnite());
                    return etatEcart;
                }).collect(Collectors.groupingBy(item -> item.getCodart(),
                        Collectors.reducing(new EtatEcartInventaire(BigDecimal.ZERO), (a, b) -> {
                            b.setQuantiteReel(a.getQuantiteReel().add(b.getQuantiteReel()));
                            b.setQuantiteTheorique(a.getQuantiteTheorique().add(b.getQuantiteTheorique()));
                            b.setValeurReel(a.getValeurReel().add(b.getValeurReel()));
                            b.setValeurReelTTC(a.getValeurReelTTC().add(b.getValeurReelTTC()));
                            b.setValeurTheorique(a.getValeurTheorique().add(b.getValeurTheorique()));
                            b.setValeurTheoriqueTTC(a.getValeurTheoriqueTTC().add(b.getValeurTheoriqueTTC()));
                            return b;
                        })))
                        .values().stream().collect(toList());
                return listeEtatEcartInventaireWithPrincipalUnity;
            }
        }

        return listeEtatEcartInventaire;
    }
//Android
    //http://172.20.102.26/pharmacie-core/stock/findDepstoInv?catgorie-depot=UU&depot-id=504&include-null-quantity=true&detailed=true&codArt=32842&uniteArt=137

    /**
     *
     * @param coddep
     * @param categ_depot
     * @param includeNullQty
     * @param detailed
     * @param categ_art
     * @param codArticle
     * @param uniteArticle
     * @param listeArticle
     * @return
     */
    public List<DepstoDTO> findDepstoInvByCodDepCathegDep(Integer coddep, CategorieDepotEnum categ_depot, boolean includeNullQty, boolean detailed, Integer categ_art,
            Integer codArticle, Integer uniteArticle, Set<Integer> listeArticle) {

        //Liste des controle sur le depot
        List<InventaireDTO> listInventaireDto = inventaireService.findListInventaire(coddep, categ_depot, true, false);
        List<Integer> t = new ArrayList<>();

        //Controle si cathegorie article est vide (cas pour ANDROID) 
        List<String> listeCathegArticle = null;
        if (categ_art != null) {
            //Si oui on verifier si cette catégorie et ouverte pour cet inventaire
            listInventaireDto.forEach(d -> {

                List<Integer> h = d.getDetailInventaireCollection().stream()
                        .filter(i -> {
                            return i.getDetailInventairePK().getCategorieArticle().equals(categ_art);
                        })
                        .map(k -> k.getDetailInventairePK().getCategorieArticle())
                        .collect(Collectors.toList());
                t.addAll(h);
            });
            Preconditions.checkBusinessLogique(!t.isEmpty(), "inventaire.categorie.closed");

        } else {
            //si no on extraire la liste des cathégorie_article ouverte pour ce depot
            List<Integer> listeCodeInventaire = listInventaireDto.stream()
                    .map(d -> d.getCode())
                    .collect(Collectors.toList());
            List<DetailInventaire> lisCategorieArticle = detailInventaireRepository.findByDetailInventairePK_InventaireIn(listeCodeInventaire);
            //liste des cathegrie dans liste of string
            listeCathegArticle = lisCategorieArticle.stream()
                    .map(d -> d.getDetailInventairePK().getCategorieArticle().toString())
                    .collect(Collectors.toList());
        }

        List<Integer> listeSeulArticle = null;
        //Importer un seul article en cas ou on a besoin d'un seul article 
        List<ArticleDTO> articleDto = null;
        boolean test = false;
        if (codArticle != null && uniteArticle != null && listeArticle == null) {
            listeSeulArticle = new ArrayList<>();
            listeSeulArticle.add(codArticle);
            //Controle pour savoir si un article va etre inventerier mais n'esxiste pas dans le depot (--> on ajoute un article dans un dépôt ) 
            if (categ_art == null) {
                articleDto
                        = //                        categ_depot.equals(CategorieDepotEnum.IMMO)?
                        //                       (List<ArticleDTO>) paramAchatServiceClient.articleIMMOFindbyListCode(listeSeulArticle)
                        //                        :
                        (List) paramAchatServiceClient.articleFindbyListCodeWithLanguageFR(listeSeulArticle);
                for (ArticleDTO article : articleDto) {
                    Preconditions.checkBusinessLogique(article.getActif(), "return.add.article-inactif");
                    if (listeCathegArticle.contains(article.getCategorieArticle().getCode().toString())) {
                        test = true;
                    }
                }
            }
        } else if (categ_art != null && listeArticle == null) { // en cas ou on a besoin de plusieur article pour la saisie
            List<ArticleDTO> listeSeulArticles = (List) paramAchatServiceClient.articleByCategArt(categ_art);
            listeSeulArticle = listeSeulArticles.stream()
                    .map(q -> q.getCode())
                    .collect(Collectors.toList());
            Preconditions.checkBusinessLogique(!listeSeulArticle.isEmpty(), "inventaire.inventaire-vide", "inventaire.inventaire-vide");
        } else if (listeArticle != null) { // pour le cas de l'importation
            listeSeulArticle = new ArrayList<>();
            listeSeulArticle.addAll(listeArticle);
        }

        List<DepstoDTO> depStoDTO;
        List<DepstoDTO> depStoDTOs = new ArrayList<>();
        depStoDTO = stockService.findAll(coddep, categ_depot, listeSeulArticle, true, true, false, null);
        //Pour le cas ou article appartient a l'inventaire mais n'existr pas dans depsto
        if (depStoDTO.isEmpty() && test == true) {
            saveDepsto(articleDto.get(0).getCode(), categ_depot, coddep, uniteArticle);
            depStoDTO = stockService.findAll(coddep, categ_depot, listeSeulArticle, true, true, false, null);
        } else if (test == true) {
            boolean test2 = false;
            for (DepstoDTO depPar : depStoDTO) {
                if (depPar.getArticleID().equals(codArticle) && depPar.getUnityCode().equals(uniteArticle)) {
                    test2 = true;
                }
            }
            if (test2 == false) {
                saveDepsto(articleDto.get(0).getCode(), categ_depot, coddep, uniteArticle);
                depStoDTO = stockService.findAllFr(coddep, categ_depot, listeSeulArticle, true, true, false);
            }
        }

        //Filtrer tous les articles dans le depot en cas ou categArt is null 
        if (listeCathegArticle != null) {
            for (DepstoDTO depstoDTOpar : depStoDTO) {
                if (listeCathegArticle.contains(depstoDTOpar.getCategorieArticle())) {
                    depStoDTOs.add(depstoDTOpar);
                }
            }
            depStoDTO.clear();
            depStoDTO.addAll(depStoDTOs);
        }

        //Controle si on veux retourne un seul article donnée
        if (codArticle != null && uniteArticle != null) {
            depStoDTO = depStoDTO.stream().
                    filter(d -> d.getArticleID().equals(codArticle) && d.getUnityCode().equals(uniteArticle)).
                    collect(Collectors.toList());
            Preconditions.checkBusinessLogique(!depStoDTO.isEmpty(), "inventaire.article-Ferme");
        }
        if (categ_art != null) {
            depStoDTO = depStoDTO.stream().
                    filter(d -> {
                        return d.getCategorieArticle().equals(categ_art.toString());

                    }).
                    collect(Collectors.toList());
        }
        //Controller s'il existe des article a invetorier
        Preconditions.checkBusinessLogique(!depStoDTO.isEmpty(), "inventaire.inventaire-vide", "empty");

        /**
         * elimminer les doublant dans une autre liste
         */
        List<DepstoDTO> simplifiedResult = new ArrayList();
        depStoDTO.stream()
                .collect(groupingBy(DepstoDTO::getArticleID,
                        groupingBy(DepstoDTO::getUnityCode,
                                Collectors.reducing(new DepstoDTO(BigDecimal.ZERO), (a, b) -> {
                                    if (b.getQuantiteReel() == null) {
                                        b.setQuantiteReel(BigDecimal.ZERO);
                                    }
                                    if (a.getQuantiteReel() == null) {
                                        a.setQuantiteReel(BigDecimal.ZERO);
                                    }

                                    if (b.getLotInter() == null) {
                                        b.setLotInter("");
                                    }

                                    b.setQuantity(BigDecimal.ZERO);

                                    return b;
                                }))))
                .forEach((k, v) -> {
                    simplifiedResult.addAll(v.values());
                });

        /**
         * Ajouter detailArticle pour chaque article qui a une quantité reel et
         * non perissable
         */
        for (DepstoDTO sList : simplifiedResult) {
            List<DepstoDetailArticleDTO> result = new ArrayList<>();
            BigDecimal qteReel = BigDecimal.ZERO;
            for (DepstoDTO dList : depStoDTO) {
                if (dList.getArticleID().equals(sList.getArticleID()) && dList.getUnityCode().equals(sList.getUnityCode()) && dList.getQuantiteReel() != null
                        && dList.getQuantiteReel().compareTo(BigDecimal.ZERO) != 0) {
                    qteReel = qteReel.add(dList.getQuantiteReel());

                    if ((!CategorieDepotEnum.IMMO.equals(categ_depot) && dList.getPerissable() == true)
                            || (CategorieDepotEnum.IMMO.equals(categ_depot) && Boolean.TRUE.equals(dList.getDetaille()) && Boolean.TRUE.equals(dList.getAvecNumeroSerie()))) {
                        DepstoDetailArticleDTO detailArticle = new DepstoDetailArticleDTO();
                        detailArticle.setDatPer(dList.getPreemptionDate());
                        detailArticle.setLotInter(dList.getLotInter());
                        detailArticle.setQte(dList.getQuantiteReel());
                        result.add(detailArticle);
                    }
                }

            }

            //Eliminer les doublant dans le detailArticle par datePeremption et LotInter
            if (!CategorieDepotEnum.IMMO.equals(categ_depot)) {
                List<DepstoDetailArticleDTO> listDepstoDetailArticleDTOGroupper = new ArrayList();
                result.stream()
                        .collect(groupingBy(DepstoDetailArticleDTO::getDatPer,
                                groupingBy(DepstoDetailArticleDTO::getLotInter,
                                        Collectors.reducing(new DepstoDetailArticleDTO(BigDecimal.ZERO), (a, b) -> {
                                            if (b.getQte() == null) {
                                                b.setQte(BigDecimal.ZERO);
                                            }
                                            if (a.getQte() == null) {
                                                a.setQte(BigDecimal.ZERO);
                                            }
                                            b.setQte(b.getQte().add(a.getQte()));

                                            return b;
                                        }))))
                        .forEach((k, v) -> {
                            listDepstoDetailArticleDTOGroupper.addAll(v.values());
                        });

                sList.setDepstoDetailArticle(listDepstoDetailArticleDTOGroupper);
                sList.setQuantiteReel(qteReel);
            } else {
                /**
                 * categorie depot IMMO *
                 */
                //Eliminer les doublant dans le detailArticle par datePeremption et LotInter
                List<DepstoDetailArticleDTO> listDepstosArticleIMMOGrouppedByNumSerie = new ArrayList();
                result.stream()
                        .collect(groupingBy(DepstoDetailArticleDTO::getLotInter,
                                Collectors.reducing(new DepstoDetailArticleDTO(BigDecimal.ZERO), (a, b) -> {
                                    if (b.getQte() == null) {
                                        b.setQte(BigDecimal.ZERO);
                                    }
                                    if (a.getQte() == null) {
                                        a.setQte(BigDecimal.ZERO);
                                    }
                                    b.setQte(b.getQte().add(a.getQte()));

                                    return b;
                                })))
                        .forEach((k, v) -> {
                            listDepstosArticleIMMOGrouppedByNumSerie.add(v);
                        });

                sList.setDepstoDetailArticle(listDepstosArticleIMMOGrouppedByNumSerie);
                sList.setQuantiteReel(qteReel);
                /**
                 * ***
                 */
            }

            if (codArticle != null) {
                BigDecimal qteDejSaisie = depstoScanRepository.sumQuaniteByCodartAndUnite(sList.getArticleID(), sList.getUnityCode(), categ_depot, sList.getCodeDepot());
                sList.setQteDejaSaisieInv(qteDejSaisie.intValue());
            }
        }
        //Ordonner la liste des articles selon la designation
        Collections.sort(simplifiedResult, (d1, d2) -> d1.getDesignation().toUpperCase().compareToIgnoreCase(d2.getDesignation().toUpperCase()));
        simplifiedResult.sort(Comparator.comparing(DepstoDTO::getDesignation).thenComparing(DepstoDTO::getCodeSaisiArticle).thenComparing(DepstoDTO::getUnityCode));

        return simplifiedResult;

    }

    @Transactional
    public Boolean importation(CategorieDepotEnum categ_depot, Integer coddep) {
        //Liste des controle sur le depot
        List<InventaireDTO> listInventaireDto = inventaireService.findListInventaire(coddep, categ_depot, true, false);

        //extraire la liste des cathégorie_article ouverte pour ce depot
        List<Integer> listeCodeInventaire = listInventaireDto.stream()
                .map(d -> d.getCode())
                .collect(Collectors.toList());
        List<DetailInventaire> lisCategorieArticle = detailInventaireRepository.findByDetailInventairePK_InventaireIn(listeCodeInventaire);
        com.csys.util.Preconditions.checkBusinessLogique(!lisCategorieArticle.isEmpty(), "inventaire.exist-pas-ouvert");
        //liste des cathegrie dans liste of string
        List<Integer> listeCathegArticle = lisCategorieArticle.stream()
                .map(d -> d.getDetailInventairePK().getCategorieArticle())
                .collect(Collectors.toList());
        //Liste des articles a importer 
        List<DepstoScanDTO> listeDepstoScanDTo = depstoScanService.findByCodartAndUniteAndCategDepotAndCoddepAndInventerier(null, null, categ_depot, coddep, false, null, false);

        //Liste article a importer aprés le filtre avec les catégorie article ouverte dans l'inventaire
        listeDepstoScanDTo = listeDepstoScanDTo.stream()
                .filter(item -> listeCathegArticle.contains(item.getCategArt()))
                .collect(Collectors.toList());

//        Map<Integer, Set<Integer>> mapDepstoScan = listeDepstoScanDTo.stream().collect(Collectors.groupingBy(
//                DepstoScanDTO::getCategArt, HashMap::new, mapping(DepstoScanDTO::getCodart, toSet())));
        Map<Integer, List<DepstoScanDTO>> mapDepstoScanDTO = listeDepstoScanDTo.stream().collect(groupingBy(
                DepstoScanDTO::getCategArt));

        log.debug("grouped depstoToscan {}", mapDepstoScanDTO);

        mapDepstoScanDTO.forEach((k, v) -> {
            //liste des article
            Set<Integer> listeArticle = v.stream()
                    .map(d -> d.getCodart())
                    .collect(Collectors.toSet());
            //Importer la liste des articles déja saisie dans l'inventaire
            List<DepstoDTO> listeDepstoDTO = this.findDepstoInvByCodDepCathegDep(coddep, categ_depot, true, true, k, null, null, listeArticle);

            //Parcourir la liste des article déja dans l'inventaire , mm si la quantité = 0 (on importe tous les article demandé)
            for (DepstoDTO depstoDTO : listeDepstoDTO) {

                List<DepstoDetailArticleDTO> result = depstoDTO.getDepstoDetailArticle();
                BigDecimal qteReel = depstoDTO.getQuantiteReel();

                //On verifie pour chaque article s'il y a des quantité saisie dans depsto_scan (si oui on les ajoute dans la liste importe de l'inventaire ) 
                for (DepstoScanDTO depstoScanDTO : v) {

                    if (depstoScanDTO.getCodart().equals(depstoDTO.getArticleID()) && depstoScanDTO.getUnite().equals(depstoDTO.getUnityCode())) {// && depstoScanDTO.getDatPer().isAfter(LocalDate.now())) {

                        qteReel = qteReel.add(depstoScanDTO.getQuantite());

                        DepstoDetailArticleDTO detailArticle = new DepstoDetailArticleDTO();
                        detailArticle.setDatPer(depstoScanDTO.getDatPer());
                        detailArticle.setLotInter(depstoScanDTO.getLotInter());
                        detailArticle.setQte(depstoScanDTO.getQuantite());
                        result.add(detailArticle);

                    }

                }
                depstoDTO.setDepstoDetailArticle(result);
                depstoDTO.setQuantiteReel(qteReel);
                depstoDTO.setIsInInventory(Boolean.TRUE);
            }

            this.saisieInv(listeDepstoDTO, coddep);
            depstoScanRepository.updatePerimer(listeArticle, categ_depot, coddep);
            depstoScanRepository.updateImporter(listeArticle, categ_depot, coddep);
        });

        return true;
    }

    @Transactional
    public Boolean saisieInv(List<DepstoDTO> depstoDTO, Integer coddep) {
        log.debug("SaiseInventaire***********");

        Preconditions.checkBusinessLogique(!depstoDTO.isEmpty(), "inventaire.verifier");
        //une ligne pour connaitre le depot et cathégorie depot 
        DepstoDTO firstDTO = depstoDTO.get(0);
        CategorieDepotEnum categDepot = firstDTO.getCategorieDepot();
        Integer categArticle = Integer.parseInt(firstDTO.getCategorieArticle());

        //Liste des controle sur le depot
        List<InventaireDTO> listInventaireDto = inventaireService.findListInventaire(coddep, categDepot, true, false);

        List<Integer> t = new ArrayList<>();
        listInventaireDto.forEach(d -> {

            List<Integer> h = d.getDetailInventaireCollection().stream().filter(i -> {
                return i.getDetailInventairePK().getCategorieArticle().equals(categArticle);
            })
                    .collect(Collectors.toList()).stream().map(k -> k.getDetailInventairePK().getCategorieArticle()).collect(Collectors.toList());
            t.addAll(h);
        });

        Preconditions.checkBusinessLogique(!t.isEmpty(), "inventaire.categorie.closed");

        String codeSaisiInventaire = listInventaireDto.stream()
                .filter(inv -> {
                    Optional<Integer> inventaireId = inv.getDetailInventaireCollection().stream()
                            .filter(d -> d.getDetailInventairePK().getCategorieArticle().equals(categArticle))
                            .map(d -> d.getDetailInventairePK().getInventaire())
                            .findFirst();
                    return inventaireId.isPresent() && inv.getCode().equals(inventaireId.get());
                })
                .map(inv -> inv.getCodeSaisie()).findAny().orElse(null);

        //Recuperer la liste des id d'article 
        List<Integer> listeArticleIds = depstoDTO.stream()
                .map(d -> Integer.parseInt(d.getArticleID().toString() + d.getUnityCode().toString()))
                .collect(Collectors.toList());

        List<Integer> listeArticleId = depstoDTO.stream()
                .map(d -> d.getArticleID())
                .collect(Collectors.toList());

        //Suppression des ancien ligne crée dans la saisie s'il existe
        depstorepository.deleteByCategDepotAndCoddepAndCodArt(categDepot, coddep, listeArticleIds);

        List<ArticleDTO> listeArticle;
        if (categDepot.equals(PH)) {
            listeArticle = new ArrayList(paramAchatServiceClient.articlePHFindbyListCode(listeArticleId));
        } else {
            listeArticle = paramAchatServiceClient.articleFindbyListCodeWithLanguageFR(listeArticleId);
        }
        listeArticle.stream().filter(item -> Boolean.TRUE.equals(item.getStopped())).findAny().ifPresent(item -> {
            throw new IllegalBusinessLogiqueException("item.stopped", new Throwable(item.getCodeSaisi()));
        });

        List<Depsto> listeDepstoTousArticleReel = new ArrayList<>();
        //Parcourir la liste des articles reçus et les filtrer selon la quantité et les mettre dans depsto
        for (DepstoDTO depstoDto : depstoDTO) {

            if (!depstoDto.getDepstoDetailArticle().isEmpty() && depstoDto.getQuantiteReel().compareTo(BigDecimal.ZERO) != 0) {
                //Traitement PH,UU,EC avec les articles périssables
                for (DepstoDetailArticleDTO dListe : depstoDto.getDepstoDetailArticle()) {

                    Depsto depSto = DepstoFactory.depstoDTOToDepsto(depstoDto);
                    depSto.setNumBon(codeSaisiInventaire);
                    depSto.setNumBonOrigin(codeSaisiInventaire);
                    depSto.setCoddep(coddep);
                    depSto.setLotInter(dListe.getLotInter());
                    depSto.setDatPer(dListe.getDatPer());
                    depSto.setStkrel(dListe.getQte());
                    depSto.setQte(BigDecimal.ZERO);
                    depSto.setPu(BigDecimal.ZERO);
                    depSto.setTauxTva(depstoDto.getTauxTva());
                    depSto.setCodeTva(depstoDto.getCodeTva());
                    depSto.setUserInv(SecurityContextHolder.getContext().getAuthentication().getName());
                    depSto.setaInventorier(Boolean.TRUE);
                    listeDepstoTousArticleReel.add(depSto);
                }
            } else if (depstoDto.getQuantiteReel().compareTo(BigDecimal.ZERO) != 0) {
                //Pour les articles non périssables
                Depsto depSto = DepstoFactory.depstoDTOToDepsto(depstoDto);
                depSto.setCoddep(coddep);
                depSto.setNumBon(codeSaisiInventaire);
                depSto.setNumBonOrigin(codeSaisiInventaire);
                depSto.setLotInter("***");
                depSto.setQte(BigDecimal.ZERO);
                depSto.setPu(BigDecimal.ZERO);
                depSto.setTauxTva(depstoDto.getTauxTva());
                depSto.setCodeTva(depstoDto.getCodeTva());
                depSto.setUserInv(SecurityContextHolder.getContext().getAuthentication().getName());
                depSto.setaInventorier(Boolean.TRUE);
                listeDepstoTousArticleReel.add(depSto);

            }

        }

        //Inserer un simple ligne avec la quantité et le PMP pour un article EC
        //Importer la liste de tous les articles du depsto  avec la quantité théorique > 0(qte0) et les article recupéré du webService 
        List<Depsto> listeDepstoTousArticleTheorique = depstorepository.findByCategDepotAndCoddepAndCodArt(categDepot, coddep, listeArticleId);
        listeDepstoTousArticleTheorique.forEach(d -> d.setStkdep0(d.getQte0()));

        //Liste des article a traitté pour ne pas faire le traitement plusieur fois en cas de redandance de l'article dans la liste
        Map<Integer, Set<Integer>> mapArticleATraiter = new HashMap<>();
        //Partcorir la liste des article reçue du web pour les comparer avec les articles existe dans la base

        for (Depsto depsto : listeDepstoTousArticleReel) {
            ArticleDTO article = listeArticle.stream().filter(item -> item.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            Preconditions.checkBusinessLogique(article != null, "missing-article", depsto.getCodart().toString());
            //Liste théorique pour chaque article 
            List<Depsto> listeDepstoPourArticleTheorique = listeDepstoTousArticleTheorique.stream()
                    .filter(item -> item.getCodart().equals(depsto.getCodart()) && item.getUnite().equals(depsto.getUnite()))
                    .collect(Collectors.toList());

            //Cas ou il n'existe pas aucun article avec quantite theorique
            if (listeDepstoPourArticleTheorique.isEmpty()) {
                List<Integer> articleListe = new ArrayList<>();
                articleListe.add(depsto.getCodart());
                if (Boolean.TRUE.equals(listInventaireDto.get(0).getIsDemarrage())) {
                    depsto.setTauxTva(depsto.getTauxTva());
                    depsto.setCodeTva(depsto.getCodeTva());
                } else {
                    depsto.setTauxTva(article.getValeurTvaAch());
                    depsto.setCodeTva(article.getCodeTvaAch());
                }
                if (categDepot.equals(PH)) { // ou cas ou on n'utilise pas unite principale pour PH
                    ArticlePHDTO articlePH = (ArticlePHDTO) article;
                    for (ArticleUniteDTO articleUnite : articlePH.getArticleUnites()) {
                        if (articleUnite.getCodeUnite().equals(depsto.getUnite())) {
                            depsto.setPu(articlePH.getPrixAchat().divide(articleUnite.getNbPiece(), 2, RoundingMode.HALF_UP));
                        }
                    }
                } else { // On cas ou on utilise l'unite principale
                    depsto.setPu(article.getPrixAchat());
                }
                depstorepository.save(depsto);

            } //en cas ou il existe des ligne dans la table depsto
            else {
                //Verifier si on a deja traiter cette article
                if (!(mapArticleATraiter.containsKey(depsto.getCodart()) && mapArticleATraiter.get(depsto.getCodart()).contains(depsto.getUnite()))) {
                    List<Depsto> listeDepstoPourArticleReel = listeDepstoTousArticleReel.stream()
                            .filter(item -> item.getCodart().equals(depsto.getCodart()) && item.getUnite().equals(depsto.getUnite()))
                            .collect(Collectors.toList());

                    //Ordonner la liste théorique selon PU desc
                    Collections.sort(listeDepstoPourArticleTheorique, Comparator.comparing(Depsto::getPu).thenComparing(Depsto::getTauxTva).reversed());
                    List<Depsto> listeDepstoFinal = new ArrayList();
                    Depsto depstoFinal;
                    BigDecimal qtePhysiqueReste;
                    for (Depsto depstoReel : listeDepstoPourArticleReel) {

                        qtePhysiqueReste = depstoReel.getStkrel();
                        Iterator it = listeDepstoPourArticleTheorique.iterator();
                        while (it.hasNext() && qtePhysiqueReste.compareTo(BigDecimal.ZERO) != 0) {
                            Depsto depstoTheorique = (Depsto) it.next();

                            if (depstoTheorique.getStkdep0().compareTo(BigDecimal.ZERO) != 0) {

                                //Creation d'un nouv depsto Pour l'enregistrement
                                depstoFinal = new Depsto(coddep, depstoTheorique.getCodart(), BigDecimal.ZERO, BigDecimal.ZERO,
                                        LocalDateTime.now(), codeSaisiInventaire, depstoReel.getUnite(), categDepot);
                                depstoFinal.setNumBonOrigin(codeSaisiInventaire);
                                depstoFinal.setNumBonBeforeInventaire(depstoReel.getNumBonOrigin());

                                if (Boolean.TRUE.equals(listInventaireDto.get(0).getIsDemarrage())) {
                                    depstoFinal.setTauxTva(depsto.getTauxTva());
                                    depstoFinal.setCodeTva(depsto.getCodeTva());
                                } else {
                                    depstoFinal.setTauxTva(depstoTheorique.getTauxTva());
                                    depstoFinal.setCodeTva(depstoTheorique.getCodeTva());
                                }
                                depstoFinal.setMemo(depstoTheorique.getMemo());
                                if (qtePhysiqueReste.subtract(depstoTheorique.getStkdep0()).intValue() >= 0) {

                                    depstoFinal.setLotInter(depstoReel.getLotInter());
                                    depstoFinal.setPu(depstoTheorique.getPu());
                                    depstoFinal.setDatPer(depstoReel.getDatPer());
                                    depstoFinal.setStkrel(depstoTheorique.getStkdep0());
                                    qtePhysiqueReste = qtePhysiqueReste.subtract(depstoTheorique.getStkdep0());
                                    depstoTheorique.setStkdep0(BigDecimal.ZERO);
                                    depstoFinal.setaInventorier(Boolean.TRUE);
                                    listeDepstoFinal.add(depstoFinal);

                                } else {
                                    depstoFinal.setLotInter(depstoReel.getLotInter());
                                    depstoFinal.setPu(depstoTheorique.getPu());
                                    depstoFinal.setDatPer(depstoReel.getDatPer());
                                    depstoFinal.setStkrel(qtePhysiqueReste);
                                    depstoTheorique.setStkdep0(depstoTheorique.getStkdep0().subtract(qtePhysiqueReste));
                                    qtePhysiqueReste = BigDecimal.ZERO;
                                    depstoFinal.setaInventorier(Boolean.TRUE);
                                    listeDepstoFinal.add(depstoFinal);
                                }
                            }
                        }//Fin Parcour ListeThéorique 

                        if (qtePhysiqueReste.compareTo(BigDecimal.ZERO) != 0) {
                            //Creation d'un nouv depsto pour la quantite restante et 
                            //la donne un maximum de prix
                            depstoFinal = new Depsto(coddep, depstoReel.getCodart(), BigDecimal.ZERO, BigDecimal.ZERO,
                                    LocalDateTime.now(), codeSaisiInventaire, depstoReel.getUnite(), categDepot);

                            depstoFinal.setNumBonOrigin(codeSaisiInventaire);
                            depstoFinal.setNumBonBeforeInventaire(depstoReel.getNumBonOrigin());
                            //get le max prix de la liste théorique
                            BigDecimal maxPu = listeDepstoPourArticleTheorique.stream().
                                    min((l1, l2) -> l2.getPu().compareTo(l1.getPu())).get().getPu();

                            depstoFinal.setPu(maxPu);
                            depstoFinal.setDatPer(depstoReel.getDatPer());
                            depstoFinal.setStkrel(qtePhysiqueReste);
                            depstoFinal.setLotInter(depstoReel.getLotInter());
                            if (Boolean.TRUE.equals(listInventaireDto.get(0).getIsDemarrage())) {
                                depstoFinal.setTauxTva(depsto.getTauxTva());
                                depstoFinal.setCodeTva(depsto.getCodeTva());
                            } else {
                                depstoFinal.setTauxTva(article.getValeurTvaAch());
                                depstoFinal.setCodeTva(article.getCodeTvaAch());
                            }
                            qtePhysiqueReste = BigDecimal.ZERO;
                            depstoFinal.setaInventorier(Boolean.TRUE);
                            listeDepstoFinal.add(depstoFinal);
                        }

                    }
                    depstorepository.save(listeDepstoFinal);
                }
            }
            //Inserer les articles avec leurs unité pour verifier s'il existe dans le parcoure des articles réel 
            Set<Integer> uniteTemp;
            uniteTemp = mapArticleATraiter.get(depsto.getCodart());
            if (uniteTemp == null) {
                uniteTemp = new HashSet<Integer>(Arrays.asList(depsto.getUnite()));
            }
            uniteTemp.add(depsto.getUnite());
            mapArticleATraiter.put(depsto.getCodart(), uniteTemp);
        }
        return true;
    }

    @Transactional
    public Boolean validerInventaire(Integer codeInventaire) {

        //Controle Si l'inventaire est encore ouverte ou nn
        Inventaire inventaire = inventaireRepository.findOne(codeInventaire);
        Preconditions.checkBusinessLogique(inventaire != null, "inventaire.depot-inexistant");
        Preconditions.checkBusinessLogique(inventaire.getDateCloture() == null, "inventaire.depot-ferme");
        Preconditions.checkBusinessLogique(inventaire.getDateAnnule() == null, "inventaire-depot-annuler");
        Preconditions.checkBusinessLogique(depstoScanRepository.findByCodInvAndImporterAndInventerier(codeInventaire, false, false).isEmpty(), "inventaire.depot-importer");

        // la liste des catégories articles pour cette inventaire 
        List<DetailInventaire> detailInventaire = detailInventaireRepository.findByDetailInventairePK_Inventaire(inventaire.getCode());

        // la liste des catégories articles pour cette inventaire 
        List<Integer> listeCategArticle = detailInventaire.stream()
                .map(d -> d.getDetailInventairePK().getCategorieArticle())
                .collect(Collectors.toList());

        List<Integer> articleIds = new ArrayList();
        List<ArticleDTO> articleDTOs = !CategorieDepotEnum.IMMO.equals(inventaire.getCategorieDepot())
                ? paramAchatServiceClient.articleFindByListCategorieArticlesIds(listeCategArticle)
                : new ArrayList();
        List<ArticleIMMODTO> articleIMMODTOs = CategorieDepotEnum.IMMO.equals(inventaire.getCategorieDepot())
                ? paramAchatServiceClient.articleIMMOFindByListCategorieArticlesIds(listeCategArticle)
                : new ArrayList();

        if (CategorieDepotEnum.IMMO.equals(inventaire.getCategorieDepot())) {
            Preconditions.checkBusinessLogique(articleIMMODTOs != null, "error-parametrage-achat-core");
            articleIds = articleIMMODTOs.stream().map(item -> item.getCode()).collect(Collectors.toList());
        } else {
            Preconditions.checkBusinessLogique(articleDTOs != null, "error-parametrage-achat-core");
            articleIds = articleDTOs.stream().map(item -> item.getCode()).collect(Collectors.toList());
        }

        /**
         * effacer du depsto lignes inserer pour déclencher l'inventaire de
         * démarrage before construnct deptohist
         */
        if (Boolean.TRUE.equals(inventaire.getIsDemarrage())) {
            depstorepository.deleteByCategDepotAndCoddepAndNumBonOrigineIsDemarrage(inventaire.getCategorieDepot(), inventaire.getDepot());
        }

        Integer[] codeArticlesInventerier = articleIds.toArray(new Integer[articleIds.size()]);
        //Decompser la liste des articles en des parties de 2000 id pour l'accés a la base de données
        List<Depsto> depstoInInventory = new ArrayList<>();
        if (codeArticlesInventerier != null && codeArticlesInventerier.length > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codeArticlesInventerier.length / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> codesChunk = Arrays.asList(codeArticlesInventerier).subList(i * 2000, Math.min(i * 2000 + 2000, codeArticlesInventerier.length));
                depstoInInventory.addAll(depstorepository.findByCategDepotAndCoddepAndCodartIn(inventaire.getCategorieDepot(), inventaire.getDepot(), codesChunk));
            }
        }

        if (CategorieDepotEnum.IMMO.equals(inventaire.getCategorieDepot())) {
            List<DepstoDTO> depstoDTOIMMOs = DepstoDTOAssembler.assembleListIMMO(depstoInInventory, articleIMMODTOs);

            List<DepstoDTO> articleIMMODTOsWithNumSerieDuplicated = new ArrayList();

            depstoDTOIMMOs.stream()
                    .filter(d -> {
                        return Boolean.TRUE.equals(d.getDetaille()) && Boolean.TRUE.equals(d.getAvecNumeroSerie());
                    })
                    .collect(groupingBy(DepstoDTO::getArticleID,
                            //                            groupingBy(DepstoDTO::getUnityCode,
                            groupingBy(DepstoDTO::getLotInter,
                                    Collectors.reducing(new DepstoDTO(BigDecimal.ZERO), (a, b) -> {
                                        if (b.getQuantiteReel() == null) {
                                            b.setQuantiteReel(BigDecimal.ZERO);
                                        }
                                        if (a.getQuantiteReel() == null) {
                                            a.setQuantiteReel(BigDecimal.ZERO);
                                        }
                                        b.setQuantiteReel(b.getQuantiteReel().add(a.getQuantiteReel()));

                                        return b;
                                    }))))
                    .forEach((k, v) -> {
                        articleIMMODTOsWithNumSerieDuplicated.addAll(v.values());
                    });
            articleIMMODTOsWithNumSerieDuplicated.stream()
                    .filter(d -> d.getQuantiteReel().compareTo(BigDecimal.ONE) > 0)
                    .findFirst().ifPresent(item -> {
                        throw new IllegalBusinessLogiqueException("inventaire.article.immo.duplicated", new Throwable(item.getCodeSaisiArticle()));
                    });

        }

        //insert depstoHist 
        List<Integer> depstoIdsInIventory = depstoInInventory.stream()
                .filter(d -> d.getStkrel() == null || d.getStkrel().compareTo(BigDecimal.ZERO) == 0)
                .map(d -> d.getCode()).collect(Collectors.toList());

        Integer[] depstoIdsInIventoryArray = depstoIdsInIventory.toArray(new Integer[depstoIdsInIventory.size()]);

        if (depstoIdsInIventoryArray != null && depstoIdsInIventoryArray.length > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) depstoIdsInIventoryArray.length / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> codesChunk = Arrays.asList(depstoIdsInIventoryArray).subList(i * 2000, Math.min(i * 2000 + 2000, depstoIdsInIventoryArray.length));
                depStoHistRepository.insertDepstoHistWhenValidateInventory(codeInventaire, inventaire.getCategorieDepot().categ(), codesChunk);
                depstorepository.updateDepstoAInventorierToValidateInventoryByCodeIn(codesChunk);
            }
        }

        //update quantity and a_inventorier depsto if stkrel > 0 with @Query native
        List<Integer> depstoIdsToUpdateQuantity = depstoInInventory.stream()
                .filter(d -> d.getStkrel() != null && d.getStkrel().compareTo(BigDecimal.ZERO) != 0)
                .map(d -> d.getCode()).collect(Collectors.toList());
        Integer[] depstoIdsToUpdateQuantityArray = depstoIdsToUpdateQuantity.toArray(new Integer[depstoIdsToUpdateQuantity.size()]);

        if (depstoIdsToUpdateQuantityArray != null && depstoIdsToUpdateQuantityArray.length > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) depstoIdsToUpdateQuantityArray.length / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> codesChunk = Arrays.asList(depstoIdsToUpdateQuantityArray).subList(i * 2000, Math.min(i * 2000 + 2000, depstoIdsToUpdateQuantityArray.length));
                depStoHistRepository.insertDepstoHistWhenValidateInventory(codeInventaire, inventaire.getCategorieDepot().categ(), codesChunk);
                depstorepository.updateDepstoQuantityToValidateInventoryByCodeIn(codesChunk);
            }
        }

        //Remplir depstoHist
//        List<DepStoHist> listDepstoHist = new ArrayList<>();
//        depstoInInventory.forEach(d -> {
//            DepStoHist depstoHist = new DepStoHist();
//            depstoHist.setCodeDepsto(d.getCode());
//            depstoHist.setNumBonOriginDepsto(d.getNumBonOrigin());
//            depstoHist.setNumBonDepsto(d.getNumBon());
//            depstoHist.setInventaire(inventaire);
//            depstoHist.setCodeArticle(d.getCodart());
//
//            if (CategorieDepotEnum.IMMO.equals(inventaire.getCategorieDepot())) {
//                ArticleIMMODTO articleIMMODTO = articleIMMODTOs.stream().filter(item -> item.getCode().equals(d.getCodart())).findFirst().get();
//                depstoHist.setCodeSaisie(articleIMMODTO.getCodeSaisi());
//                depstoHist.setArticleDesignation(articleIMMODTO.getDesignation());
//                depstoHist.setArticledesignationAr(articleIMMODTO.getDesignationSec());
//                depstoHist.setCodeCategorieArticle(articleIMMODTO.getCategorieArticle().getCode());
//               
//            } else {
//                ArticleDTO articleDTO = articleDTOs.stream().filter(item -> item.getCode().equals(d.getCodart())).findFirst().get();
//                depstoHist.setCodeSaisie(articleDTO.getCodeSaisi());
//                depstoHist.setArticleDesignation(articleDTO.getDesignation());
//                depstoHist.setArticledesignationAr(articleDTO.getDesignationSec());
//                depstoHist.setCodeCategorieArticle(articleDTO.getCategorieArticle().getCode());
//            }
//
//            if (d.getStkrel() == null) {
//                depstoHist.setStkDep(BigDecimal.ZERO);
//                depstoHist.setPuTotReel(BigDecimal.ZERO);
//            } else {
//                depstoHist.setStkDep(d.getStkrel());
//                depstoHist.setPuTotReel(d.getStkrel().multiply(d.getPu()));
//            }
//            if (d.getQte0() == null) {
//                depstoHist.setQte0(BigDecimal.ZERO);
//                depstoHist.setPuTotTheorique(BigDecimal.ZERO);
//            } else {
//                depstoHist.setQte0(d.getQte0());
//                depstoHist.setPuTotTheorique(d.getQte0().multiply(d.getPu()));
//            }
//            depstoHist.setPu(d.getPu());
//            if (d.getLotInter() != null) {
//                depstoHist.setLot(d.getLotInter());
//            } else {
//                depstoHist.setLot("");
//            }
//            depstoHist.setCodeUnite(d.getUnite());
//            depstoHist.setDatPer(d.getDatPer());
//            depstoHist.setCodeTva(d.getCodeTva());
//            depstoHist.setTauxTva(d.getTauxTva());
//
//            listDepstoHist.add(depstoHist);
//            /*update depsto*/
//            if (d.getStkrel() != null && d.getStkrel().compareTo(BigDecimal.ZERO) != 0) {
//                d.setQte(d.getStkrel());
//                d.setStkdep(d.getStkrel());
//                d.setStkrel(BigDecimal.ZERO);
//            }
//            d.setaInventorier(Boolean.FALSE);
//        });
        inventaire.setDateCloture(new Date());
        inventaire.setUserCloture(SecurityContextHolder.getContext().getAuthentication().getName());

        //generer immobilisations 
        List<ImmobilisationDTO> immobilisations = new ArrayList();
        depstoInInventory.forEach(d -> {
            if (CategorieDepotEnum.IMMO.equals(inventaire.getCategorieDepot())) {
                ArticleIMMODTO articleIMMODTO = articleIMMODTOs.stream().filter(item -> item.getCode().equals(d.getCodart())).findFirst().get();
                if (articleIMMODTO.getGenererImmobilisation()) {
                    immobilisations.add(genererImmoDTO(d, articleIMMODTO, inventaire));
                }
            }
        });
        if (CategorieDepotEnum.IMMO.equals(inventaire.getCategorieDepot()) && !immobilisations.isEmpty()) {
            ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper = new ListeImmobilisationDTOWrapper();
            listeImmobilisationDTOWrapper.setImmobilisation(immobilisations);
            ListeImmobilisationDTOWrapper listeImmo = immobilisationService.saveImmo(listeImmobilisationDTOWrapper);
            Preconditions.checkBusinessLogique(listeImmo != null, "error-saving-immo");
        }

        //  depStoHistRepository.save(listDepstoHist);
        depstoScanRepository.updateCloture(listeCategArticle, inventaire.getCategorieDepot(), inventaire.getDepot());
        return true;
    }

    public ImmobilisationDTO genererImmoDTO(Depsto depsto, ArticleIMMODTO articleIMMO, Inventaire inventaire) {

        ImmobilisationDTO immoDTO = new ImmobilisationDTO();
        immoDTO.setDateFacture(LocalDate.now());
        immoDTO.setNumeroReception(inventaire.getCodeSaisie());

        immoDTO.setQuantite(depsto.getStkrel());
        immoDTO.setValeurAchat(depsto.getPu());
        immoDTO.setCodeArticle(articleIMMO.getCodeSaisi());

        immoDTO.setTauxAmortissement(articleIMMO.getTauxAmortissement());
        immoDTO.setAvecNumeroSerie(articleIMMO.getAvecNumeroSerie());
        immoDTO.setEttiquetable(articleIMMO.getEtiquettable());
        immoDTO.setGenererImmobilisation(articleIMMO.getGenererImmobilisation());

        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            immoDTO.setDesignation(articleIMMO.getDesignationSec());
            immoDTO.setDesignationSec(articleIMMO.getDesignation());

        } else {
            immoDTO.setDesignation(articleIMMO.getDesignation());
            immoDTO.setDesignationSec(articleIMMO.getDesignationSec());
        }

        immoDTO.setCodeSaisiArticle(articleIMMO.getCodeSaisi());
        immoDTO.setExiste(Boolean.TRUE);
        immoDTO.setValeurProratat(BigDecimal.ZERO);
        immoDTO.setAutreFrais(BigDecimal.ZERO);
        immoDTO.setValeurAmortissementAnterieur(BigDecimal.ZERO);
        immoDTO.setTauxAmortissement(articleIMMO.getTauxAmortissement());
        immoDTO.setCodeTva(articleIMMO.getCodeTvaAch());
        //si art detaille et avecNumSerie
        immoDTO.setNumeroSerie(depsto.getLotInter());
        immoDTO.setDetaille(articleIMMO.getDetaille());
        immoDTO.setFamilleArticle(articleIMMO.getCategorieArticle().getCodeSaisi());
        immoDTO.setTauxIfrs(articleIMMO.getTauxIfrs());
        immoDTO.setTauxAmortFiscale1(articleIMMO.getTauxAmortFiscale1());
        immoDTO.setTauxAmortFiscale2(articleIMMO.getTauxAmortFiscale2());

        return immoDTO;
    }

    @Transactional
    public Boolean annulationInventaire(Integer codeInventaire) {

        //Controle Si l'inventaire est encore ouverte ou nn
        Inventaire inventaire = inventaireRepository.findOne(codeInventaire);
        Preconditions.checkBusinessLogique(inventaire != null, "inventaire.depot-inexistant");
        Preconditions.checkBusinessLogique(inventaire.getDateAnnule() == null, "inventaire-depot-annuler");
        Preconditions.checkBusinessLogique(inventaire.getDateCloture() == null, "inventaire.depot-ferme");

        Preconditions.checkBusinessLogique(depstoScanRepository.findByCodInv(codeInventaire).isEmpty(), "inventaire-scan-exist");

        /**
         * effacer du depsto lignes inserer pour déclencher l'inventaire de
         * démarrage before construnct deptohist
         */
        if (Boolean.TRUE.equals(inventaire.getIsDemarrage())) {
            depstorepository.deleteByCategDepotAndCoddepAndNumBonOrigineIsDemarrage(inventaire.getCategorieDepot(), inventaire.getDepot());
        }

        // la liste des catégories articles pour cette inventaire 
        List<DetailInventaire> detailInventaire = detailInventaireRepository.findByDetailInventairePK_Inventaire(inventaire.getCode());

        // la liste des catégories articles pour cette inventaire 
        List<Integer> listeCategArticle = detailInventaire.stream()
                .map(d -> d.getDetailInventairePK().getCategorieArticle())
                .collect(Collectors.toList());

        List<ArticleDTO> articleDTOs = paramAchatServiceClient.articleFindByListCategorieArticlesIds(listeCategArticle);
        Preconditions.checkBusinessLogique(articleDTOs != null, "error-parametrage-achat-core");
        List<Integer> articleIds = articleDTOs.stream().map(item -> item.getCode()).collect(Collectors.toList());

        Integer[] codes = articleIds.toArray(new Integer[articleIds.size()]);
        //Decompser la liste des articles en des parties de 2000 id pour l'accés a la base de données
        List<Depsto> depsto = new ArrayList<>();
        if (codes != null && codes.length > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codes.length / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> codesChunk = Arrays.asList(codes).subList(i * 2000, Math.min(i * 2000 + 2000, codes.length));
                depsto.addAll(depstorepository.findByCategDepotAndCoddepAndCodartIn(inventaire.getCategorieDepot(), inventaire.getDepot(), codesChunk));
            }
        }

        depsto.stream().map(d -> {
            Preconditions.checkBusinessLogique(d.getStkrel() == null || d.getStkrel().compareTo(BigDecimal.ZERO) == 0, "inventaire-saisi-exist");
            Preconditions.checkBusinessLogique(d.getQte() == null || d.getQte().compareTo(BigDecimal.ZERO) == 0, "inventaire-qte-problem");
            d.setQte(d.getQte0());
            d.setQte0(BigDecimal.ZERO);
            d.setaInventorier(Boolean.FALSE);
            return d;
        }).collect(Collectors.toList());

        inventaire.setDateAnnule(new Date());
        inventaire.setUserAnnule(SecurityContextHolder.getContext().getAuthentication().getName());

        return true;
    }

    public List<EtatEcartInventaire> etatEcartApresVldGlobByCodDepCathegDepCodInv(Integer codeInventaire, TypeEnvoieEtatEnum optionImpression, CategorieDepotEnum categDepot, Boolean withPrincipalUnity) {
//        CategorieDepotEnum categ = CategorieDepotEnum.PH;
        List<EtatEcartInventaire> listeEtatEcartInventaire = new ArrayList<>();

        //Controle Si l'inventaire et encore ouverte ou nn
        List<DepStoHist> listeDepstoHist = depStoHistRepository.findByInventaire_Code(codeInventaire);

        List<ArticleDTO> articles;
        List<ArticlePHDTO> listArticlePH = new ArrayList();

        if (!listeDepstoHist.isEmpty()) {
            //Controle Si l'inventaire et encore ouverte ou nn
            Inventaire inventaire = listeDepstoHist.get(0).getInventaire();
            Preconditions.checkBusinessLogique(inventaire != null, "inventaire.depot-inexistant");
            Preconditions.checkBusinessLogique(inventaire.getDateCloture() != null, "invt.init.inventaire-ouvert");
            List<CategorieDepotDTO> listCategorieDepotDTOs = paramAchatServiceClient.findAllCategorieDepot();
            DepotDTO depot = paramAchatServiceClient.findDepotByCode(inventaire.getDepot());

            List<Integer> articleIds = listeDepstoHist.stream().map(DepStoHist::getCodeArticle).collect(toList());
            if (CategorieDepotEnum.PH.equals(categDepot)) {
                listArticlePH = paramAchatServiceClient.articlePHFindbyListCode(articleIds);
                articles = listArticlePH.stream().map(articlePH -> (ArticleDTO) articlePH).collect(toList());
            } else {
                articles = paramAchatServiceClient.articleFindbyListCodeWithLanguageFR(articleIds);
            }
            Set<Integer> uniteIds = listeDepstoHist.stream().map(d -> d.getCodeUnite()).collect(Collectors.toSet());
            List<UniteDTO> listUniteDTOs = paramAchatServiceClient.findUnitsByCodes(new ArrayList<>(uniteIds));

            for (DepStoHist depstoHist : listeDepstoHist) {

                EtatEcartInventaire etatEcartInventaire = new EtatEcartInventaire();

                etatEcartInventaire.setCodart(depstoHist.getCodeArticle());
                etatEcartInventaire.setCoddep(inventaire.getDepot());
                etatEcartInventaire.setCodeCathegDepot(inventaire.getCategorieDepot());
                etatEcartInventaire.setCodeInventaire(inventaire.getCode());

                CategorieDepotDTO categorieDepotDTO = listCategorieDepotDTOs.stream()
                        .filter(item -> item.getCode().equalsIgnoreCase(inventaire.getCategorieDepot().toString()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("print-inventory.missing-categorie-depot", new Throwable(inventaire.getCategorieDepot().toString())));

                UniteDTO unite = listUniteDTOs.stream().filter(x -> x.getCode().equals(depstoHist.getCodeUnite())).findFirst().orElse(null);
                Preconditions.checkBusinessLogique(unite != null, "missing-unity");
                etatEcartInventaire.setUnityDesignation(unite.getDesignation());

                etatEcartInventaire.setDesignationDepot(depot.getDesignation());
                etatEcartInventaire.setDesignationDepot(depot.getDesignation());

                etatEcartInventaire.setDesCathegorieDepot(categorieDepotDTO.getDesignation());
                etatEcartInventaire.setQuantiteReel(depstoHist.getStkDep());
                etatEcartInventaire.setQuantiteTheorique(depstoHist.getQte0());
                etatEcartInventaire.setUnityCode(depstoHist.getCodeUnite());
                etatEcartInventaire.setValeurReel(depstoHist.getPuTotReel());
                etatEcartInventaire.setValeurTheorique(depstoHist.getPuTotTheorique());
                etatEcartInventaire.setValeurReelTTC(Helper.applyTaxesToPrice(depstoHist.getPuTotReel(), depstoHist.getTauxTva()));
                etatEcartInventaire.setValeurTheoriqueTTC(Helper.applyTaxesToPrice(depstoHist.getPuTotTheorique(), depstoHist.getTauxTva()));
                etatEcartInventaire.setCodeSaisie(inventaire.getCodeSaisie());
                etatEcartInventaire.setCodeCategorieArticle(depstoHist.getCodeCategorieArticle().toString());

                ArticleDTO matchedArticle = articles.stream()
                        .filter(item -> item.getCode().equals(depstoHist.getCodeArticle()))
                        .findFirst()
                        .orElseThrow(() -> {
                            log.error("print-inventory.missing-article {}", depstoHist.getCodeArticle().toString());
                            return new IllegalBusinessLogiqueException("print-inventory.missing-article", new Throwable(depstoHist.getCodeArticle().toString()));
                        });

                etatEcartInventaire.setCodeSaisiArticle(matchedArticle.getCodeSaisi());
                etatEcartInventaire.setDesignation(matchedArticle.getDesignation());
                etatEcartInventaire.setCategorieArticle(matchedArticle.getCategorieArticle().getDesignation());
                etatEcartInventaire.setDesignationCategorieArt(matchedArticle.getCategorieArticle().getDesignation());

                listeEtatEcartInventaire.add(etatEcartInventaire);

            }
        }
//         /**
//         * elimminer les doublant dans une autre liste
//         */ 
        List<EtatEcartInventaire> simplifiedResult = new ArrayList();
        listeEtatEcartInventaire.stream()
                .collect(groupingBy(EtatEcartInventaire::getCodart,
                        groupingBy(EtatEcartInventaire::getUnityCode,
                                Collectors.reducing(new EtatEcartInventaire(BigDecimal.ZERO), (a, b) -> {

                                    b.setQuantiteTheorique(b.getQuantiteTheorique().add(a.getQuantiteTheorique()));
                                    b.setQuantiteReel(b.getQuantiteReel().add(a.getQuantiteReel()));
                                    b.setValeurReel(b.getValeurReel().add(a.getValeurReel()));
                                    b.setValeurTheorique(b.getValeurTheorique().add(a.getValeurTheorique()));
                                    b.setValeurReelTTC(b.getValeurReelTTC().add(a.getValeurReelTTC()));
                                    b.setValeurTheoriqueTTC(b.getValeurTheoriqueTTC().add(a.getValeurTheoriqueTTC()));
                                    return b;
                                }))))
                .forEach((k, v) -> {
                    simplifiedResult.addAll(v.values());
                });

        //Remplir la liste final selon l'option de l'impression 
        List<EtatEcartInventaire> listeEtatEcartInventaireFinal = new ArrayList<>();
        for (EtatEcartInventaire etatEcartInventaire : simplifiedResult) {
            if ((optionImpression == TypeEnvoieEtatEnum.AE || optionImpression == TypeEnvoieEtatEnum.ALL) && etatEcartInventaire.getQuantiteReel().compareTo(etatEcartInventaire.getQuantiteTheorique()) != 0) {
                listeEtatEcartInventaireFinal.add(etatEcartInventaire);
            }
            if ((optionImpression == TypeEnvoieEtatEnum.SE || optionImpression == TypeEnvoieEtatEnum.ALL) && etatEcartInventaire.getQuantiteReel().compareTo(etatEcartInventaire.getQuantiteTheorique()) == 0) {
                listeEtatEcartInventaireFinal.add(etatEcartInventaire);
            }
        }
        //Ordonner la liste des ecart selon la designation

        Collections.sort(listeEtatEcartInventaireFinal,
                (d1, d2) -> d1.getDesignation().toUpperCase().compareToIgnoreCase(d2.getDesignation().toUpperCase()));
//        simplifiedResult.sort(Comparator.comparing(EtatEcartInventaire::getDesignation).thenComparing(EtatEcartInventaire::getCodeSaisie).thenComparing(EtatEcartInventaire::getUnityCode));

        if (CategorieDepotEnum.PH.equals(categDepot)) {
            if (Boolean.TRUE.equals(withPrincipalUnity)) {
                List<ArticlePHDTO> listArticlePHForMap = listArticlePH;
                List<EtatEcartInventaire> listeEtatEcartInventaireFinalWithPrincipalUnity = listeEtatEcartInventaireFinal.stream().map(etatEcart -> {

                    ArticlePHDTO matchedArticled = listArticlePHForMap.stream()
                            .filter(art -> art.getCode().equals(etatEcart.getCodart())).findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));
                    ArticleUniteDTO matchedUnite = matchedArticled.getArticleUnites().stream()
                            .filter(unity -> unity.getCodeUnite().equals(etatEcart.getUnityCode()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unity"));
                    BigDecimal qteInPrincipaleUnit = etatEcart.getQuantiteReel().divide(matchedUnite.getNbPiece(), 2, RoundingMode.HALF_UP);
                    etatEcart.setQuantiteReel(qteInPrincipaleUnit);
                    BigDecimal qteTheoriqueInPrincipaleUnit = etatEcart.getQuantiteTheorique().divide(matchedUnite.getNbPiece(), 2, RoundingMode.HALF_UP);
                    etatEcart.setQuantiteTheorique(qteTheoriqueInPrincipaleUnit);
                    etatEcart.setUnityCode(matchedArticled.getCodeUnite());
                    etatEcart.setUnityDesignation(matchedArticled.getDesignationUnite());
                    return etatEcart;
                }).collect(Collectors.groupingBy(item -> item.getCodart(),
                        Collectors.reducing(new EtatEcartInventaire(BigDecimal.ZERO), (a, b) -> {
                            b.setQuantiteReel(a.getQuantiteReel().add(b.getQuantiteReel()));
                            b.setQuantiteTheorique(a.getQuantiteTheorique().add(b.getQuantiteTheorique()));
                            b.setValeurReel(a.getValeurReel().add(b.getValeurReel()));
                            b.setValeurReelTTC(a.getValeurReelTTC().add(b.getValeurReelTTC()));
                            b.setValeurTheorique(a.getValeurTheorique().add(b.getValeurTheorique()));
                            b.setValeurTheoriqueTTC(a.getValeurTheoriqueTTC().add(b.getValeurTheoriqueTTC()));
                            return b;
                        })))
                        .values().stream().collect(toList());
                return listeEtatEcartInventaireFinalWithPrincipalUnity;
            }
        }
        return listeEtatEcartInventaireFinal;
    }

    public List<EtatEcartInventaire> etatEcartApresVldGlobByCodDepCathegDepCodInvDetail(Integer codeInventaire, TypeEnvoieEtatEnum optionImpression, CategorieDepotEnum categDepot, Boolean withPrincipalUnity) {

        List<EtatEcartInventaire> listeEtatEcartInventaire = new ArrayList<>();

        //Importer la liste de tous les articles dans ce depot et cathégorie article
        List<DepStoHist> listeDepstoHist = depStoHistRepository.findByInventaire_Code(codeInventaire);
        List<Integer> articleIds = listeDepstoHist.stream().map(DepStoHist::getCodeArticle).collect(toList());

        if (!listeDepstoHist.isEmpty()) {
            //Controle Si l'inventaire et encore ouverte ou nn
            Inventaire inventaire = listeDepstoHist.get(0).getInventaire();
            Preconditions.checkBusinessLogique(inventaire != null, "inventaire.depot-inexistant");
            Preconditions.checkBusinessLogique(inventaire.getDateCloture() != null, "invt.init.inventaire-ouvert");
            CategorieDepotDTO categorieDepotDTO = paramAchatServiceClient.findCategorieDepot(inventaire.getCategorieDepot().toString());
            DepotDTO depot = paramAchatServiceClient.findDepotByCode(inventaire.getDepot());
            List<Integer> articleIDs = listeDepstoHist.stream().map(DepStoHist::getCodeArticle).collect(toList());
            List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCodeWithLanguageFR(articleIDs);

            Set<Integer> uniteIds = listeDepstoHist.stream().map(d -> d.getCodeUnite()).collect(Collectors.toSet());
            List<UniteDTO> listUniteDTOs = paramAchatServiceClient.findUnitsByCodes(new ArrayList<>(uniteIds));

            for (DepStoHist liste : listeDepstoHist) {

                EtatEcartInventaire etatEcartInventaire = new EtatEcartInventaire();

                etatEcartInventaire.setCodart(liste.getCodeArticle());
                etatEcartInventaire.setCoddep(inventaire.getDepot());
                etatEcartInventaire.setCodeCathegDepot(inventaire.getCategorieDepot());
                etatEcartInventaire.setCodeInventaire(inventaire.getCode());

                UniteDTO unite = listUniteDTOs.stream().filter(x -> x.getCode().equals(liste.getCodeUnite())).findFirst().orElse(null);
                Preconditions.checkBusinessLogique(unite != null, "missing-unity");
                etatEcartInventaire.setUnityDesignation(unite.getDesignation());

                etatEcartInventaire.setDesignationDepot(depot.getDesignation());

                etatEcartInventaire.setDesCathegorieDepot(categorieDepotDTO.getDesignation());
                etatEcartInventaire.setQuantiteReel(liste.getStkDep());
                etatEcartInventaire.setQuantiteTheorique(liste.getQte0());
                etatEcartInventaire.setUnityCode(liste.getCodeUnite());
                etatEcartInventaire.setValeurReel(liste.getPuTotReel());
                etatEcartInventaire.setValeurReelTTC(Helper.applyTaxesToPrice(liste.getPuTotReel(), liste.getTauxTva()));
                if (etatEcartInventaire.getValeurReel().compareTo(BigDecimal.ZERO) != 0) {
                    etatEcartInventaire.setPriUniReel(liste.getPu());
                    etatEcartInventaire.setPriUniReelTTC(Helper.applyTaxesToPrice(liste.getPu(), liste.getTauxTva()));
                } else {
                    etatEcartInventaire.setPriUniReel(BigDecimal.ZERO);
                    etatEcartInventaire.setPriUniReel(BigDecimal.ZERO);
                }
                etatEcartInventaire.setValeurTheorique(liste.getPuTotTheorique());
                etatEcartInventaire.setValeurTheoriqueTTC(Helper.applyTaxesToPrice(liste.getPuTotTheorique(), liste.getTauxTva()));
                if (etatEcartInventaire.getValeurTheorique().compareTo(BigDecimal.ZERO) != 0) {
                    etatEcartInventaire.setPriUniTheorique(liste.getPu());
                    etatEcartInventaire.setPriUniTheoriqueTTC(Helper.applyTaxesToPrice(liste.getPu(), liste.getTauxTva()));
                } else {
                    etatEcartInventaire.setPriUniTheorique(BigDecimal.ZERO);
                    etatEcartInventaire.setPriUniTheoriqueTTC(BigDecimal.ZERO);
                }

                etatEcartInventaire.setCodeSaisie(inventaire.getCodeSaisie());
                etatEcartInventaire.setCodeCategorieArticle(liste.getCodeCategorieArticle().toString());
                ArticleDTO article = articles.stream().filter(item -> liste.getCodeArticle().equals(item.getCode())).findFirst().get();

                etatEcartInventaire.setCodeSaisiArticle(article.getCodeSaisi());
                etatEcartInventaire.setDesignation(article.getDesignation());

                //etatEcartInventaire.setCategorieArticle(paramAchatServiceClient.findNodesByCategorieArticleParent(Integer.parseInt(listeDepstoDTO.getCategorieArticle())));
                etatEcartInventaire.setCategorieArticle(article.getCategorieArticle().getDesignation());
                etatEcartInventaire.setDesignationCategorieArt(article.getCategorieArticle().getDesignation());

                if (etatEcartInventaire.getQuantiteReel().compareTo(BigDecimal.ZERO) != 0 || etatEcartInventaire.getQuantiteTheorique().compareTo(BigDecimal.ZERO) != 0) {
                    if ((optionImpression == TypeEnvoieEtatEnum.AE || optionImpression == TypeEnvoieEtatEnum.ALL) && etatEcartInventaire.getQuantiteReel().compareTo(etatEcartInventaire.getQuantiteTheorique()) != 0) {
                        listeEtatEcartInventaire.add(etatEcartInventaire);
                    }
                    if ((optionImpression == TypeEnvoieEtatEnum.SE || optionImpression == TypeEnvoieEtatEnum.ALL) && etatEcartInventaire.getQuantiteReel().compareTo(etatEcartInventaire.getQuantiteTheorique()) == 0) {
                        listeEtatEcartInventaire.add(etatEcartInventaire);
                    }
                }
            }

            listeEtatEcartInventaire.sort(Comparator.comparing(EtatEcartInventaire::getDesignation).thenComparing(EtatEcartInventaire::getCodeSaisie).thenComparing(EtatEcartInventaire::getUnityCode).thenComparing(comparing(EtatEcartInventaire::getPriUniReel).reversed()).thenComparing(comparing(EtatEcartInventaire::getPriUniTheorique).reversed()));
        }
        if (CategorieDepotEnum.PH.equals(categDepot)) {
            if (Boolean.TRUE.equals(withPrincipalUnity)) {
                List<ArticlePHDTO> listArticlePHForMap = paramAchatServiceClient.articlePHFindbyListCode(articleIds);
                List<EtatEcartInventaire> listeEtatEcartInventaireFinalWithPrincipalUnity = listeEtatEcartInventaire.stream().map(etatEcart -> {

                    ArticlePHDTO matchedArticled = listArticlePHForMap.stream()
                            .filter(art -> art.getCode().equals(etatEcart.getCodart())).findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));
                    ArticleUniteDTO matchedUnite = matchedArticled.getArticleUnites().stream()
                            .filter(unity -> unity.getCodeUnite().equals(etatEcart.getUnityCode()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unity"));
                    BigDecimal qteInPrincipaleUnit = etatEcart.getQuantiteReel().divide(matchedUnite.getNbPiece(), 2, RoundingMode.HALF_UP);
                    etatEcart.setQuantiteReel(qteInPrincipaleUnit);
                    BigDecimal qteTheoriqueInPrincipaleUnit = etatEcart.getQuantiteTheorique().divide(matchedUnite.getNbPiece(), 2, RoundingMode.HALF_UP);
                    etatEcart.setQuantiteTheorique(qteTheoriqueInPrincipaleUnit);
                    etatEcart.setUnityCode(matchedArticled.getCodeUnite());
                    etatEcart.setUnityDesignation(matchedArticled.getDesignationUnite());
                    return etatEcart;
                }).collect(Collectors.groupingBy(item -> item.getCodart(),
                        Collectors.reducing(new EtatEcartInventaire(BigDecimal.ZERO), (a, b) -> {
                            b.setQuantiteReel(a.getQuantiteReel().add(b.getQuantiteReel()));
                            b.setQuantiteTheorique(a.getQuantiteTheorique().add(b.getQuantiteTheorique()));
                            b.setValeurReel(a.getValeurReel().add(b.getValeurReel()));
                            b.setValeurReelTTC(a.getValeurReelTTC().add(b.getValeurReelTTC()));
                            b.setValeurTheorique(a.getValeurTheorique().add(b.getValeurTheorique()));
                            b.setValeurTheoriqueTTC(a.getValeurTheoriqueTTC().add(b.getValeurTheoriqueTTC()));
                            return b;
                        })))
                        .values().stream().collect(toList());
                return listeEtatEcartInventaireFinalWithPrincipalUnity;
            }
        }
        return listeEtatEcartInventaire;
    }
}
