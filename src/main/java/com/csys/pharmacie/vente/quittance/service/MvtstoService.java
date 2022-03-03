package com.csys.pharmacie.vente.quittance.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.achat.dto.ArticleUuDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import static com.csys.pharmacie.helper.CategorieDepotEnum.PH;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.MouvementConsomation;
import com.csys.pharmacie.helper.QtePrixMouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.stock.dto.DepstoDTO;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import com.csys.pharmacie.vente.quittance.domain.QMvtsto;
import com.csys.pharmacie.vente.quittance.dto.AdmissionDemandePECDTO;
import com.csys.pharmacie.vente.quittance.dto.MvtstoDTO;
import com.csys.pharmacie.vente.quittance.factory.MvtstoFactory;
import com.csys.pharmacie.vente.quittance.repository.DetailMvtstoRepository;
import com.csys.pharmacie.vente.quittance.repository.MvtstoRepository;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Mvtsto.
 */
@Service
@Transactional
public class MvtstoService {

    private final Logger log = LoggerFactory.getLogger(MvtstoService.class);

    private final MvtstoRepository mvtstoRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final MvtstoFactory mvtstoFactory;
    private final ReceptionServiceClient receptionServiceClient;
    private final StockService stockService;
    private final DetailMvtstoRepository detailMvtstoRepository;

    public MvtstoService(MvtstoRepository mvtstoRepository, ParamAchatServiceClient paramAchatServiceClient, MvtstoFactory mvtstoFactory, ReceptionServiceClient receptionServiceClient, StockService stockService, DetailMvtstoRepository detailMvtstoRepository) {
        this.mvtstoRepository = mvtstoRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.mvtstoFactory = mvtstoFactory;
        this.receptionServiceClient = receptionServiceClient;
        this.stockService = stockService;
        this.detailMvtstoRepository = detailMvtstoRepository;
    }

    /**
     * Get all the factures by code in.
     *
     * @param coddep
     * @param numdoss
     * @param fromDate
     * @param toDate
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<MvtstoDTO> findByCoddepAndNumdossAndDatbon(Integer coddep, List<String> numdoss, LocalDateTime fromDate,
            LocalDateTime toDate) {
        List<Mvtsto> mvtstos = new ArrayList<>();
        Integer numberOfChunks = (int) Math.ceil((double) numdoss.size() / 2000);
        for (int i = 0; i < numberOfChunks; i++) {
            List<String> codesChunk = numdoss.subList(i * 2000, Math.min(i * 2000 + 2000, numdoss.size()));
            QMvtsto _mvtsto = QMvtsto.mvtsto;
            WhereClauseBuilder builder = new WhereClauseBuilder()
                    .and(_mvtsto.facture().numdoss.in(codesChunk))
                    .and(_mvtsto.facture().coddep.eq(coddep))
                    .and(_mvtsto.facture().datbon.goe(fromDate))
                    .and(_mvtsto.facture().datbon.loe(toDate))
                    .and(_mvtsto.facture().codAnnul.isNull())
                    .and(_mvtsto.qteben.goe(BigDecimal.ONE));
            mvtstos.addAll(mvtstoRepository.findAll(builder));
        }
        List<MvtstoDTO> result = MvtstoFactory.mvtstoToMvtstoDTOs(mvtstos);
        log.debug("list MvtstoDTOs {}", result);
        List<Integer> codeUnites = result.stream().map(MvtstoDTO::getUnityCode).distinct().collect(Collectors.toList());
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        List<Integer> codeArticles = result.stream().map(MvtstoDTO::getCodart).distinct().collect(Collectors.toList());
        List<ArticleDTO> listArticles = paramAchatServiceClient.articleFindbyListCode(codeArticles);
        result.forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
            Optional<ArticleDTO> article = listArticles.stream().filter(x -> x.getCode().equals(mvtstoDTO.getCodart())).findFirst();
            if (article.isPresent()) {
                mvtstoDTO.setIsImported(article.get().isIsImported());
            }

        });
        return result;
    }

    /**
     * Get all the factures by code in.
     *
     * @param coddep
     * @param categ
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<AdmissionDemandePECDTO> findDossierByCoddep(Integer coddep, CategorieDepotEnum categ) {
        log.debug(" to get Client by code depot {} {}", coddep, categ);
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionNonCloture();
        List<String> codeAdmissions = clients.stream()
                .map(AdmissionDemandePECDTO::getCode).distinct().collect(Collectors.toList());
        QMvtsto _mvtsto = QMvtsto.mvtsto;
        List<Mvtsto> mvtstos = new ArrayList<>();
        Integer numberOfChunks = (int) Math.ceil((double) codeAdmissions.size() / 2000);
        for (int i = 0; i < numberOfChunks; i++) {
            List<String> codesChunk = codeAdmissions.subList(i * 2000, Math.min(i * 2000 + 2000, codeAdmissions.size()));
            WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.facture().categDepot.eq(categ))
                    .and(_mvtsto.facture().coddep.eq(coddep))
                    .and(_mvtsto.qteben.gt(BigDecimal.ZERO))
                    .and(_mvtsto.facture().panier.eq(Boolean.FALSE))
                    .and(_mvtsto.facture().numbonRecept.isNull())
                    .and(_mvtsto.facture().numdoss.in(codesChunk));
            mvtstos.addAll(mvtstoRepository.findAll(builder));
        }
        List<String> codeAdmissionsMvtsto = mvtstos.stream().map(Mvtsto::getFacture).collect(Collectors.toSet())
                .stream().map(Facture::getNumdoss).distinct().collect(Collectors.toList());
        clients = clients.stream().filter(x -> {
            return codeAdmissionsMvtsto.contains(x.getCode());
        }).collect(Collectors.toList());
        return clients;
    }

    /**
     * Get details facture.
     *
     * @param categ
     * @param Numdoss
     * @param coddep
     * @param search
     * @param grouped
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<MvtstoDTO> findDetailsAvoir(CategorieDepotEnum categ, String Numdoss, Integer coddep, String search, Boolean grouped) {
        log.debug("Request to get Facture: {}", Numdoss);
        QMvtsto _mvtsto = QMvtsto.mvtsto;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.facture().categDepot.eq(categ))
                .and(_mvtsto.facture().numdoss.eq(Numdoss))
                .and(_mvtsto.facture().panier.ne(Boolean.TRUE))
                .and(_mvtsto.facture().coddep.eq(coddep))
                .and(_mvtsto.facture().codAnnul.isNull())
                .and(_mvtsto.qteben.gt(BigDecimal.ZERO))
                .optionalAnd(search, () -> _mvtsto.codeSaisi.like("%" + search + "%").or(_mvtsto.desart.like("%" + search + "%")).or(_mvtsto.desArtSec.like("%" + search + "%")));

        List<Mvtsto> mvtstos = (List<Mvtsto>) mvtstoRepository.findAll(builder);
        log.debug("list mvtstos {}", mvtstos);
        List<MvtstoDTO> result = MvtstoFactory.mvtstoToMvtstoDTOs(mvtstos);
        log.debug("list MvtstoDTOs {}", result);
        List<Integer> codeUnites = new ArrayList<>();
        result.forEach(x -> {
            codeUnites.add(x.getUnityCode());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        result.forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });
        if (grouped) {
            List<MvtstoDTO> listMvtstoDTOs = new ArrayList<>();
            result.stream().collect(groupingBy(MvtstoDTO::getCodart,
                    groupingBy(MvtstoDTO::getUnityCode,
                            Collectors.reducing(new MvtstoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), (x, mvtstoDTO) -> {
                                mvtstoDTO.setQuantite(mvtstoDTO.getQuantite().add(x.getQuantite()));
                                mvtstoDTO.setQteRestante(mvtstoDTO.getQteRestante().add(x.getQteRestante()));
                                mvtstoDTO.setMontht(mvtstoDTO.getMontht().add(x.getMontht()));
                                return mvtstoDTO;
                            })))).forEach((k, v) -> {
                listMvtstoDTOs.addAll(v.values());
            });
            return listMvtstoDTOs;

        } else {
            List<MvtstoDTO> listMvtstoDTOs = new ArrayList<>();
            List<MvtstoDTO> listMvtstoDTOWithDatPers = result.stream().filter(x -> {
                return x.getLotInter() != null || x.getDatPer() != null;
            }).collect(Collectors.toList());
            listMvtstoDTOs.addAll(listMvtstoDTOWithDatPers);
            List<MvtstoDTO> listMvtstoDTOWithoutDatPers = result.stream().filter(x -> {
                return x.getLotInter() == null && x.getDatPer() == null;
            }).collect(Collectors.toList());
            listMvtstoDTOWithoutDatPers.stream().collect(groupingBy(MvtstoDTO::getCodart,
                    groupingBy(MvtstoDTO::getUnityCode,
                            Collectors.reducing(new MvtstoDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), (x, mvtstoDTO) -> {
                                mvtstoDTO.setQuantite(mvtstoDTO.getQuantite().add(x.getQuantite()));
                                mvtstoDTO.setQteRestante(mvtstoDTO.getQteRestante().add(x.getQteRestante()));
                                mvtstoDTO.setMontht(mvtstoDTO.getMontht().add(x.getMontht()));
                                return mvtstoDTO;
                            })))).forEach((k, v) -> {
                listMvtstoDTOs.addAll(v.values());
            });
            return listMvtstoDTOs;
        }
    }

    /**
     * Get details facture.
     *
     * @param categ
     * @param Numdoss
     * @param coddep
     * @param fromDate
     * @param toDate
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<MvtstoDTO> findDetailsQuittance(List<CategorieDepotEnum> categ, String Numdoss, Integer coddep, LocalDateTime fromDate,
            LocalDateTime toDate, List<Boolean> isPanier, Boolean deleted) {
        log.debug("Request to get Facture: {}", Numdoss);
        checkBusinessLogique(!categ.contains(CategorieDepotEnum.EC), "article-economat");
        QMvtsto _mvtsto = QMvtsto.mvtsto;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.facture().categDepot.in(categ))
                .optionalAnd(Numdoss, () -> _mvtsto.facture().numdoss.eq(Numdoss))
                .optionalAnd(coddep, () -> _mvtsto.facture().coddep.eq(coddep))
                .optionalAnd(fromDate, () -> _mvtsto.facture().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.facture().datbon.loe(toDate))
                .optionalAnd(isPanier, () -> _mvtsto.facture().panier.in(isPanier))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _mvtsto.facture().codAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _mvtsto.facture().codAnnul.isNull());

        List<MvtstoDTO> result = MvtstoFactory.mvtstoToMvtstoDTOs((List<Mvtsto>) mvtstoRepository.findAll(builder));
        List<Integer> codeUnites = new ArrayList<>();
        result.forEach(x -> {
            codeUnites.add(x.getUnityCode());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        result.forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });

        return result;
    }

    /**
     * Get details facture.
     *
     * @param numBon
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<MvtstoDTO> findDetails(String numBon) {
        List<MvtstoDTO> result = MvtstoFactory.mvtstoToMvtstoDTOs(mvtstoRepository.findByMvtstoPK_Numbon(numBon));
        List<Integer> codeUnites = new ArrayList<>();
        result.forEach(x -> {
            codeUnites.add(x.getUnityCode());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        result.forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });
        return result;
    }

    @Transactional(readOnly = true)
    public List<MvtstoDTO> searchMouvements(String[] quittancesIDs, List<CategorieDepotEnum> categDepot, String codeAdmission) {
        if (categDepot != null) {
            checkBusinessLogique(!categDepot.contains(CategorieDepotEnum.EC), "article-economat");
        }
        QMvtsto _mvtsto = QMvtsto.mvtsto;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(categDepot, () -> _mvtsto.facture().categDepot.in(categDepot))
                .optionalAnd(quittancesIDs, () -> _mvtsto.facture().numbon.in(quittancesIDs))
                .optionalAnd(codeAdmission, () -> _mvtsto.facture().numdoss.eq(codeAdmission));

        List<MvtstoDTO> result = MvtstoFactory.mvtstoToMvtstoDTOs((List<Mvtsto>) mvtstoRepository.findAll(builder));
        List<Integer> codeUnites = new ArrayList<>();
        result.forEach(x -> {
            codeUnites.add(x.getUnityCode());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        result.forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });

        return result;
    }

    @Transactional(readOnly = true)
    public Collection<MvtstoDTO> searchDetailsOfQuittances(List<String> quittancesIDs, CategorieDepotEnum categDepot, Boolean onlyMyArticles) {

        List<Mvtsto> mvtstos = new ArrayList<>();
        Integer numberOfChunks = (int) Math.ceil((double) quittancesIDs.size() / 2000);
        CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
        for (int i = 0; i < numberOfChunks; i++) {
            List<String> codesChunk = quittancesIDs.subList(i * 2000, Math.min(i * 2000 + 2000, quittancesIDs.size()));
            completableFuture[i] = mvtstoRepository.findByMvtstoPK_NumbonIn(codesChunk).whenComplete((mvtStos, exception) -> {
                mvtstos.addAll(mvtStos);
            });
        }
        CompletableFuture.allOf(completableFuture).join();
        Integer codDep = mvtstos.get(0).getFacture().getCoddep();
        Collection<MvtstoDTO> result;
        if (categDepot.equals(PH)) {
            result = searchDetailsOfQuittancesPH(mvtstos, codDep);
        } else {
            result = searchDetailsOfQuittancesUU(mvtstos, codDep);
        }
        
        if (onlyMyArticles != null && Boolean.TRUE.equals(onlyMyArticles)) {
            List<Integer> articleIds = result.stream().map(item -> item.getCodart()).collect(toList());
            List<ArticleDTO> articleDTOs = paramAchatServiceClient.articleFindbyListCode(articleIds, onlyMyArticles);
            List<Integer> onlyMyArticleIds = articleDTOs.stream().map(art -> art.getCode()).collect(Collectors.toList());
              log.debug("articleIds after filtre:{}", onlyMyArticleIds);
              result = result.stream()
                      .filter(mvt ->  onlyMyArticleIds.contains(mvt.getCodart()))
                      .collect(Collectors.toList());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Integer> findCodartByMvtstoPK_NumbonIn(List<String> quittancesIDs) {
        return mvtstoRepository.findCodartByMvtstoPK_NumbonIn(quittancesIDs);
    }

    /**
     *
     * @param mvtstos
     * @param depotID
     * @return List of details of quittances
     */
    public Collection<MvtstoDTO> searchDetailsOfQuittancesPH(List<Mvtsto> mvtstos, Integer depotID) {
        List<Integer> articlesID = mvtstos.stream().map(item -> item.getMvtstoPK().getCodart()).collect(toList());
//        List<ArticlePHDTO> articlesPH = paramAchatServiceClient.articlePHFindbyListCode(articlesID);
        List<ArticlePHDTO> articlesPH = paramAchatServiceClient.articlePHFindbyListCode(articlesID);

        List<DepstoDTO> depstos = stockService.findQuantiteOfArticles(articlesID, depotID, false, true, null,null);

//        Collection<MvtstoDTO> result = mvtstos.stream()
//                .map(item -> MvtstoFactory.mvtstoToMvtstoDTO(item))
//                .collect(groupingBy(MvtstoDTO::getCodart,
//                        Collectors.reducing(new MvtstoDTO(BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
//                            ArticlePHDTO matchedArticle = articlesPH.stream().filter(art -> art.getCode().equals(b.getCodart())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));
////   
//
//                            BigDecimal nbrePiece = matchedArticle.getArticleUnites().stream()
//                                    .filter(elt -> elt.getCodeUnite().equals(b.getUnityCode()))
//                                    .findFirst()
//                                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(b.getCodart().toString())))
//                                    .getNbPiece();
//
//                            b.setQuantite(b.getQuantite().divide(nbrePiece, 0, RoundingMode.UP));
//                            b.setQuantite(a.getQuantite().add(b.getQuantite()));
//                            b.setQteRestante(b.getQteRestante().divide(nbrePiece, 3, RoundingMode.UP));  // by remaining qte we mean the qte after preforming a return ( avoir ) 
//                            b.setQteRestante(a.getQteRestante().add(b.getQteRestante()));
//                            b.setUnityDesignation(matchedArticle.getDesignationUnite());
//                            b.setUnityCode(matchedArticle.getCodeUnite());
//                            b.setPriach(matchedArticle.getPrixAchat());
//                            b.setPerissable(matchedArticle.getPerissable());
//
//                            BigDecimal qteInStore = depstos.stream()
//                                    .filter(depsto -> depsto.getArticleID().equals(b.getCodart()) && depsto.getUnityCode().equals(matchedArticle.getCodeUnite()))
//                                    .findFirst()
//                                    .orElse(new DepstoDTO(BigDecimal.ZERO))
//                                    .getQuantity();
//                            b.setQuantityInStore(qteInStore);
//                            return b;
//                        })
//                )).values();
//
//        return result;
        ////////       
        Collection<MvtstoDTO> result = mvtstos.stream()
                .map(item -> MvtstoFactory.mvtstoToMvtstoDTO(item))
                .collect(groupingBy(MvtstoDTO::getCodart,
                        Collectors.collectingAndThen(Collectors.reducing(new MvtstoDTO(BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
                            ArticlePHDTO matchedArticle = articlesPH.stream().filter(art -> art.getCode().equals(b.getCodart())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));
//   

                            BigDecimal nbrePiece = matchedArticle.getArticleUnites().stream()
                                    .filter(elt -> elt.getCodeUnite().equals(b.getUnityCode()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(b.getCodart().toString())))
                                    .getNbPiece();

                            b.setQuantite(b.getQuantite().divide(nbrePiece, 3, RoundingMode.UP));
                            b.setQteRestante(b.getQteRestante().divide(nbrePiece, 3, RoundingMode.UP));
                            b.setQuantite(a.getQuantite().add(b.getQuantite()));
                            b.setQteRestante(a.getQteRestante().add(b.getQteRestante()));
                            b.setUnityDesignation(matchedArticle.getDesignationUnite());
                            b.setUnityCode(matchedArticle.getCodeUnite());
                            b.setPriach(matchedArticle.getPrixAchat());
                            b.setPerissable(matchedArticle.getPerissable());

                            BigDecimal qteInStore = depstos.stream()
                                    .filter(depsto -> depsto.getArticleID().equals(b.getCodart()) && depsto.getUnityCode().equals(matchedArticle.getCodeUnite()))
                                    .findFirst()
                                    .orElse(new DepstoDTO(BigDecimal.ZERO))
                                    .getQuantity();
                            b.setQuantityInStore(qteInStore);
                            return b;
                        }), mvtstoDTO -> {
                            log.debug(" quantite et quantite restante du codart {} sont avant arrondissement {},{} ", mvtstoDTO.getCodart(), mvtstoDTO.getQuantite(), mvtstoDTO.getQteRestante());
                            mvtstoDTO.setQuantiteRestanteWithoutRounding(mvtstoDTO.getQteRestante());
                            mvtstoDTO.setQuantite(mvtstoDTO.getQuantite().setScale(0, RoundingMode.CEILING));
                            mvtstoDTO.setQteRestante(mvtstoDTO.getQteRestante().setScale(0, RoundingMode.CEILING));
                            log.debug(" quantite et quantite restante du codart {} sont {},{} ", mvtstoDTO.getCodart(), mvtstoDTO.getQuantite(), mvtstoDTO.getQteRestante());
                            return mvtstoDTO;
                        })
                )).values();

        return result;

    }

    public Collection<MvtstoDTO> searchDetailsOfQuittancesUU(List<Mvtsto> mvtstos, Integer depotID) {

        List<Integer> articlesID = mvtstos.stream().map(item -> item.getMvtstoPK().getCodart()).collect(toList());

        List<ArticleUuDTO> articlesUU = paramAchatServiceClient.articleUUFindbyListCode(articlesID);
        List<DepstoDTO> depstos = stockService.findQuantiteOfArticles(articlesID, depotID, false, true, null,null);
        Map<Integer, MvtstoDTO> result = mvtstos.stream()
                .map(item -> {
                    MvtstoDTO mvtstoDTO = MvtstoFactory.mvtstoToMvtstoDTO(item);

                    ArticleUuDTO matchedArticle = articlesUU.stream().filter(art -> art.getCode().equals(mvtstoDTO.getCodart())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));
                    mvtstoDTO.setPriach(matchedArticle.getPrixAchat());
                    mvtstoDTO.setUnityDesignation(matchedArticle.getDesignationUnite());
                    mvtstoDTO.setPerissable(matchedArticle.getPerissable());
                    BigDecimal qteInStore = depstos.stream()
                            .filter(depsto -> depsto.getArticleID().equals(mvtstoDTO.getCodart()) && depsto.getUnityCode().equals(matchedArticle.getCodeUnite()))
                            .findFirst()
                            .orElse(new DepstoDTO(BigDecimal.ZERO))
                            .getQuantity();
                    mvtstoDTO.setQuantityInStore(qteInStore);
                    return mvtstoDTO;
                })
                .collect(Collectors.groupingBy(item -> item.getCodart(),
                        Collectors.reducing(new MvtstoDTO(BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {

                            b.setQuantite(a.getQuantite().add(b.getQuantite()));
                            b.setQteRestante(a.getQteRestante().add(b.getQteRestante()));
                            return b;
                        })));

        return result.values();
    }

    @Transactional(readOnly = true)
    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
        QMvtsto _mvtsto = QMvtsto.mvtsto;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.facture().categDepot.eq(categ))
                .optionalAnd(codart, () -> _mvtsto.mvtstoPK().codart.eq(codart))
                .optionalAnd(coddep, () -> _mvtsto.facture().coddep.eq(coddep))
                .optionalAnd(fromDate, () -> _mvtsto.facture().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.facture().datbon.loe(toDate))
                .and(_mvtsto.facture().codAnnul.isNull());
        List<Mvtsto> list = (List<Mvtsto>) mvtstoRepository.findAll(builder);
        List<String> codeAdmissions = new ArrayList<>();
        List<Integer> codeUnites = new ArrayList<>();
        list.forEach(mvtsto -> {
            codeAdmissions.add(mvtsto.getFacture().getNumdoss());
            codeUnites.add(mvtsto.getUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        List<Mouvement> mouvements = new ArrayList<>();
        list.forEach(mvtsto -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mvtsto.getUnite())).findFirst().orElse(null);
            AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(mvtsto.getFacture().getNumdoss())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            com.csys.util.Preconditions.checkBusinessLogique(client != null, "missing-client");
            mouvements.addAll(mvtstoFactory.toMouvement(mvtsto, unite, client, typeDate));
        });
        return mouvements;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return mvtstoRepository.findTotalMouvement(coddep, codart, toDate);
        } else {
            return mvtstoRepository.findTotalMouvement(coddep, codart, fromDate, toDate);
        }
    }

    @Transactional(readOnly = true)
    public List<MouvementConsomation> findConsomationReel(CategorieDepotEnum categ, List<Integer> coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        QMvtsto _mvtsto = QMvtsto.mvtsto;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.facture().categDepot.eq(categ))
                .optionalAnd(coddep, () -> _mvtsto.facture().coddep.in(coddep))
                .optionalAnd(fromDate, () -> _mvtsto.facture().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.facture().datbon.loe(toDate))
                .and(_mvtsto.facture().codAnnul.isNull());
        List<Mvtsto> list = (List<Mvtsto>) mvtstoRepository.findAll(builder);
        List<Integer> codeUnites = new ArrayList<>();
        List<Integer> codeDepots = new ArrayList<>();
        list.forEach(mvtsto -> {
            codeDepots.add(mvtsto.getFacture().getCoddep());
            codeUnites.add(mvtsto.getUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites.stream().distinct().collect(Collectors.toList()));
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        List<MouvementConsomation> mouvements = new ArrayList<>();
        list.forEach(mvtsto -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mvtsto.getUnite())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            DepotDTO depot = listDepot.stream().filter(x -> x.getCode().equals(mvtsto.getFacture().getCoddep())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(depot != null, "missing-depot");
            mouvements.add(mvtstoFactory.toMouvement(mvtsto, unite, depot));
        });
        return mouvements;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(List<Integer> codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        List<TotalMouvement> lists = new ArrayList<>();
        if (codart != null && codart.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codart.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {

                List<Integer> codesChunk = codart.subList(i * 2000, Math.min(i * 2000 + 2000, codart.size()));
                if (fromDate == null) {
                    completableFuture[i] = mvtstoRepository.findTotalMouvement(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = mvtstoRepository.findTotalMouvement(coddep, codesChunk, fromDate, toDate).whenComplete((articles, exception) -> {
                        lists.addAll(articles);
                    });
                }
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (coddep != null) {
            if (fromDate == null) {
                return mvtstoRepository.findTotalMouvement(coddep, toDate);
            } else {
                return mvtstoRepository.findTotalMouvement(coddep, fromDate, toDate);
            }

        } else {
            if (fromDate == null) {
                return mvtstoRepository.findTotalMouvement(toDate);
            } else {
                return mvtstoRepository.findTotalMouvement(fromDate, toDate);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<QtePrixMouvement> findQuantitePrixMouvementByCodartIn(List<Integer> coddep, LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categ, List<Integer> articleIds) {

        List<QtePrixMouvement> result = new ArrayList();
        Integer numberOfChunks = (int) Math.ceil((double) articleIds.size() / 2000);
        for (int i = 0; i < numberOfChunks; i++) {
            List<Integer> articleIdChunk = articleIds.subList(i * 2000, Math.min(i * 2000 + 2000, articleIds.size()));

            if (coddep != null) {
                List<QtePrixMouvement> qtePrixMouvements = detailMvtstoRepository.findQuantitePrixMouvementAndCodartIn(coddep, fromDate, toDate, categ, articleIdChunk);
                qtePrixMouvements.addAll(mvtstoRepository.findQuantitePrixMouvementAndCodartIn(coddep, fromDate, toDate, categ, articleIdChunk));
                result.addAll(qtePrixMouvements);
                log.debug("result.size est {}",result.size());
            } else {
                List<QtePrixMouvement> qtePrixMouvements = detailMvtstoRepository.findQuantitePrixMouvementAndCodartIn(fromDate, toDate, categ, articleIdChunk);
                qtePrixMouvements.addAll(mvtstoRepository.findQuantitePrixMouvementAndCodartIn(fromDate, toDate, categ, articleIdChunk));
                result.addAll(qtePrixMouvements);
            }
        }
        return  result;
    }

}