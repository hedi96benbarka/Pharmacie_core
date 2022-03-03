/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import com.csys.pharmacie.achat.domain.DetailTransfertCompanyBranch;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.MvtstoAF;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.achat.dto.CategorieArticleDTO;
import com.csys.pharmacie.achat.dto.DetailReceptionDTO;
import com.csys.pharmacie.achat.dto.DetailTransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.dto.MaquetteDto;
import com.csys.pharmacie.achat.dto.MargeDto;
import com.csys.pharmacie.achat.service.MvtStoBAService;
import com.csys.pharmacie.achat.service.TransfertCompanyBranchService;
import com.csys.pharmacie.client.dto.PalierMargeCategorieArticleDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.Action;
import static com.csys.pharmacie.helper.Action.ADD;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import static com.csys.pharmacie.helper.CategorieDepotEnum.UU;
import com.csys.pharmacie.helper.TypeBonEnum;
import static com.csys.pharmacie.helper.TypeBonEnum.BA;
import static com.csys.pharmacie.helper.TypeBonEnum.RT;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.transfert.domain.FactureBE;
import com.csys.pharmacie.transfert.domain.MvtStoBE;
import com.csys.pharmacie.transfert.service.MvtStoBEService;
import com.csys.pharmacie.vente.domain.PrixMoyPondereArticle;
import com.csys.pharmacie.vente.domain.PrixReferenceArticle;
import com.csys.pharmacie.vente.dto.PMPArticleDTO;
import com.csys.pharmacie.vente.dto.PrixReferenceArticleDTO;
import com.csys.pharmacie.vente.repository.PrixMoyPondereArticleRepository;
import com.csys.pharmacie.vente.repository.PrixReferenceArticleRepository;
import com.csys.util.Preconditions;
import static com.csys.util.Preconditions.checkBusinessLogique;
import edu.emory.mathcs.backport.java.util.Arrays;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Farouk
 */
@Service
@Transactional
public class PricingService {

    private final Logger log = LoggerFactory.getLogger(PricingService.class);

    private final StockService stockService;

    private final PrixMoyPondereArticleRepository prixMoyPondereArticleRepository;

    private final PrixReferenceArticleRepository prixReferenceArticleRepository;

    private final MvtStoBAService mvtStoBAService;

    private final ParamAchatServiceClient paramAchatServiceClient;

    private final MvtStoBEService mvtStoBEService;
    
    private final TransfertCompanyBranchService transfertCompanyBranchService;

    @Value("${prix-choisi-calcul-prix-vente-uu}")
    private String prixChoisiPourCalculPrixVenteUU;

    public void setPrixChoisiPourCalculPrixVenteUU(String prixChoisiPourCalculPrixVenteUU) {
        this.prixChoisiPourCalculPrixVenteUU = prixChoisiPourCalculPrixVenteUU;
    }

    public PricingService(StockService stockService, PrixMoyPondereArticleRepository prixMoyPondereArticleRepository, PrixReferenceArticleRepository prixReferenceArticleRepository, MvtStoBAService mvtStoBAService, ParamAchatServiceClient paramAchatServiceClient, MvtStoBEService mvtStoBEService, TransfertCompanyBranchService transfertCompanyBranchService) {
        this.stockService = stockService;
        this.prixMoyPondereArticleRepository = prixMoyPondereArticleRepository;
        this.prixReferenceArticleRepository = prixReferenceArticleRepository;
        this.mvtStoBAService = mvtStoBAService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.mvtStoBEService = mvtStoBEService;
        this.transfertCompanyBranchService = transfertCompanyBranchService;
       
    }

    @Transactional(readOnly = true)
    public PrixReferenceArticleDTO findReferencePriceByArticle(Integer codeArticle) {
        PrixReferenceArticle prixReferenceArticle = prixReferenceArticleRepository.findOne(codeArticle);
        PrixReferenceArticleDTO result = prixReferenceArticle != null ? new PrixReferenceArticleDTO(prixReferenceArticle.getPrixReference(), codeArticle) : new PrixReferenceArticleDTO(BigDecimal.ZERO, codeArticle);
        return result;
    }

    @Transactional(readOnly = true)
    public PMPArticleDTO findPMPByArticle(Integer codeArticle) {
        PrixMoyPondereArticle prixMoyPondereArticle = prixMoyPondereArticleRepository.findOne(codeArticle);
        PMPArticleDTO result = prixMoyPondereArticle != null ? new PMPArticleDTO(prixMoyPondereArticle.getPrixMoyPondere(), codeArticle) : new PMPArticleDTO(BigDecimal.ZERO, codeArticle);
        return result;
    }

    @Transactional(readOnly = true)
    public List<PMPArticleDTO> findPMPsByArticleIn(Integer[] codeArticles) {
        List<PrixMoyPondereArticle> listPrixMoyPondereArticle = new ArrayList();
        List<PMPArticleDTO> resultList = new ArrayList();
        if (codeArticles.length != 0) {

            Integer numberOfChunks = (int) Math.ceil((double) codeArticles.length / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> articleIDChunk = Arrays.asList(codeArticles).subList(i * 2000, Math.min(i * 2000 + 2000, codeArticles.length));
                listPrixMoyPondereArticle.addAll(prixMoyPondereArticleRepository.findByArticleIn(articleIDChunk.toArray(new Integer[articleIDChunk.size()])));
            }

            for (Integer codeArt : codeArticles) {
                PrixMoyPondereArticle filtredPrice = listPrixMoyPondereArticle.stream().filter(item -> item.getArticle().equals(codeArt)).findFirst().orElse(null);
                PMPArticleDTO result = filtredPrice != null ? new PMPArticleDTO(filtredPrice.getPrixMoyPondere(), codeArt) : new PMPArticleDTO(BigDecimal.ZERO, codeArt);
                resultList.add(result);
            }
        } else {
            listPrixMoyPondereArticle = prixMoyPondereArticleRepository.findAll();
            resultList = listPrixMoyPondereArticle.stream().map(item -> new PMPArticleDTO(item.getPrixMoyPondere(), item.getArticle())).collect(toList());
        }

        return resultList;
    }

    @Transactional(readOnly = true)
    public List<PrixReferenceArticleDTO> findReferencePricesByArticleIn(Integer[] codeArticles) {
        List<PrixReferenceArticle> listPrixReferenceArticle = new ArrayList();
        List<PrixReferenceArticleDTO> resultList = new ArrayList();
        if (codeArticles.length != 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codeArticles.length / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> articleIDChunk = Arrays.asList(codeArticles).subList(i * 2000, Math.min(i * 2000 + 2000, codeArticles.length));
                listPrixReferenceArticle.addAll(prixReferenceArticleRepository.findByArticleIn(articleIDChunk.toArray(new Integer[articleIDChunk.size()])));
            }

            for (Integer codeArt : codeArticles) {
                PrixReferenceArticle filtredPrice = listPrixReferenceArticle.stream().filter(item -> item.getArticle().equals(codeArt)).findFirst().orElse(null);
                PrixReferenceArticleDTO result = filtredPrice != null ? new PrixReferenceArticleDTO(filtredPrice.getPrixReference(), codeArt) : new PrixReferenceArticleDTO(BigDecimal.ZERO, codeArt);
                resultList.add(result);
            }
        } else {
            listPrixReferenceArticle = prixReferenceArticleRepository.findAll();
            resultList = listPrixReferenceArticle.stream().map(item -> new PrixReferenceArticleDTO(item.getPrixReference(), item.getArticle())).collect(toList());
        }

        return resultList;
    }

    /**
     * Update articles prices based on the reception or the return (bon du
     * retour).
     *
     * @param factureBA The reception or the return that will be used to
     * recalculate the new prices
     * @param categ_depot
     * @param action
     */
    public void updatePricesAfterReception(FactureBA factureBA, CategorieDepotEnum categ_depot, Action action) {
        Preconditions.checkBusinessLogique(BA.equals(factureBA.getTypbon()), "Invalid typBon. You must pass a reception ");
        if (action.equals(ADD)) {
            List<PrixMoyPondereArticle> listUpdatedPMP = updatePMPOnADDBA(factureBA);
            if (categ_depot.equals(UU)) {
                updateRefPriceOnADDBA(factureBA, listUpdatedPMP);
            }

        }
    }

    public void updatePricesAfterDeleteReception(FactureBA factureBA, List<MvtStoBA> listeToUpdateNotReceivedAfter, CategorieDepotEnum categ_depot) {

        //new Logic update pmp only if it is the last reception and not having a postif redressement 
        Set<Integer> codeArticlesNonReceptionnes = listeToUpdateNotReceivedAfter.stream().map(item -> item.getCodart()).distinct().collect(Collectors.toSet());
        Set<Integer> codeArticlesNonReceptionnesForRef = codeArticlesNonReceptionnes;
        List<MvtStoBE> listeMvtStoBEAfterReception = mvtStoBEService.findByCodeArticleInAndDatBonAfterAndQuantityGreaterThan(codeArticlesNonReceptionnes, factureBA.getDatbon(), BigDecimal.ZERO);
        Set<Integer> codeArticlesRedresses = listeMvtStoBEAfterReception.stream().map(item -> item.getCodart()).distinct().collect(Collectors.toSet());
        codeArticlesNonReceptionnes.removeIf(codeArticleRecep -> codeArticlesRedresses.contains(codeArticleRecep));

        log.debug("liste to update pmp est {}", listeToUpdateNotReceivedAfter);
//        List<MvtStoBA> listeToUpdatePmp=  listeToUpdateNotReceivedAfter.removeIf(mvt -> codeArticlesRedresses.contains(mvt.getCodart()));
        List<PrixMoyPondereArticle> listUpdatedPMP = updatePMPOnDeleteReception(listeToUpdateNotReceivedAfter, codeArticlesNonReceptionnes);
        if (CategorieDepotEnum.UU.equals(categ_depot)) {
            updateRefPriceOnDELETEBA(factureBA, codeArticlesNonReceptionnesForRef, listUpdatedPMP);
        }
    }

    private List<PrixMoyPondereArticle> updatePMPOnDeleteReception(List<MvtStoBA> listeMvtStoBAToUpdate, Set<Integer> codeArticlesReceptionnes) {
        List<PrixMoyPondereArticle> oldPmps = prixMoyPondereArticleRepository.findByArticleIn(codeArticlesReceptionnes.toArray(new Integer[codeArticlesReceptionnes.size()]));
        List<PrixMoyPondereArticle> newUpdatedPMP = new ArrayList();
        listeMvtStoBAToUpdate.forEach(mvtstoBa
                -> {
            PrixMoyPondereArticle matchingPMP = oldPmps
                    .stream()
                    .filter(item -> item.getArticle().equals(mvtstoBa.getCodart()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-pmp", new Throwable(mvtstoBa.getCodeSaisi())));
            matchingPMP.setPrixMoyPondere(mvtstoBa.getPmpPrecedent());
            newUpdatedPMP.add(matchingPMP);
        });
        return newUpdatedPMP;
    }

    private void updateRefPriceOnDELETEBA(FactureBA bonReception, Set<Integer> codeArticlesNonReceptionnesForRef, List<PrixMoyPondereArticle> listeUpdatedPMP) {
        log.debug("updating reference price on delete reception");
        List<Integer> codeArts = bonReception.getDetailFactureBACollection().stream().map(item -> item.getCodart()).collect(Collectors.toList());

        List<ArticleDTO> listeArticles = paramAchatServiceClient.articleFindbyListCode(codeArts);
        List<PrixReferenceArticle> listPrixReference = prixReferenceArticleRepository.findAll(codeArts);

        log.debug("le prix des reference est :{}", listPrixReference);
        List<PrixReferenceArticle> newListPrixReference = bonReception.getDetailFactureBACollection().stream()
                .map(detailFactBA -> {
                    PrixReferenceArticle prixReferenceArticle = listPrixReference.stream()
                            .filter(item -> detailFactBA.getPk().getCodart().equals(item.getArticle()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing.price.reference", new Throwable(detailFactBA.getPk().getCodart().toString())));
                    log.debug("the old reference price is {}", prixReferenceArticle);
                    log.debug("detailFactBA {}", detailFactBA.getIsPrixReference());
                    switch (prixChoisiPourCalculPrixVenteUU) {
                        case "prix-reference":
                            if (detailFactBA.getIsPrixReference() && detailFactBA.getPriuni().compareTo(prixReferenceArticle.getPrixReference()) == 0) {

                                BigDecimal newPrice = mvtStoBAService.findMaxPriuniByCodeArticleAndIsPrixRefAndNotReturnedAndNumBonNot(detailFactBA.getCodart(), Boolean.TRUE, bonReception.getNumbon());

                                log.debug("the new reference price is {}", newPrice);
                                if (newPrice == null) {
                                    ArticleDTO articleDTO = listeArticles
                                            .stream()
                                            .filter(item -> detailFactBA.getPk().getCodart().equals(item.getCode()))
                                            .findFirst()
                                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(detailFactBA.getPk().getCodart().toString())));
                                    newPrice = articleDTO.getAncienPrixAchat();
                                }
                                prixReferenceArticle.setPrixReference(newPrice);
                            }
                            break;
                        case "dernier-prix-achat":
                            if (detailFactBA.getPriuni().compareTo(BigDecimal.ZERO) > 0 && (codeArticlesNonReceptionnesForRef.contains(detailFactBA.getCodart()))) {
                                prixReferenceArticle.setPrixReference(detailFactBA.getAncienPrixAchat());
                                log.debug("prix reference suivant dernier-prix-achat :{}", prixReferenceArticle.getPrixReference());
                            }
                            break;
                        case "pmp":

                            Optional<PrixMoyPondereArticle> matchedUpdatedPmp = listeUpdatedPMP
                                    .stream()
                                    .filter(pmp -> pmp.getArticle().equals(detailFactBA.getCodart()))
                                    .findFirst();
                            if (matchedUpdatedPmp.isPresent()) {
                                prixReferenceArticle.setPrixReference(matchedUpdatedPmp.get().getPrixMoyPondere());
                                log.debug("prix reference suivant pmp :{}", prixReferenceArticle.getPrixReference());
                                break;
                            }
                    }
                    return prixReferenceArticle;
                })
                .collect(Collectors.toList());

        prixReferenceArticleRepository.save(newListPrixReference);
    }

    public void updatePricesAfterRedressement(FactureBE facturebe, CategorieDepotEnum categDepot) {
        List<PrixMoyPondereArticle> listUpdatedPmp = updatePMPOnRedressement(facturebe);
        log.debug("listUpdatePmp est {}", listUpdatedPmp);
        if (categDepot.equals(UU) && prixChoisiPourCalculPrixVenteUU.equalsIgnoreCase("pmp")) {
            log.debug("update reference prix when adding new referessement");
            updateRefPriceOnRederessement(listUpdatedPmp);
        }
    }

    private void updateRefPriceOnRederessement(List<PrixMoyPondereArticle> listeUpdatedPMP) {

        log.debug("updating reference price on add redressement");
        List<Integer> codeArts = listeUpdatedPMP.stream().map(item -> item.getArticle()).collect(Collectors.toList());
        List<PrixReferenceArticle> listPrixReference = prixReferenceArticleRepository.findAll(codeArts);
        List<PrixReferenceArticle> listUpdatedRefPrices = new ArrayList();
        listeUpdatedPMP
                .forEach(pmp -> {
                    log.debug("pmp est {}", pmp);
                    PrixReferenceArticle matchedPrixReferenceArticle = listPrixReference
                            .stream()
                            .filter(prixReference -> prixReference.getArticle().equals(pmp.getArticle()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing.price.reference", new Throwable(pmp.getArticle().toString())));
                    log.debug("matchedPrixReferenceArticle est {}", matchedPrixReferenceArticle);
                    matchedPrixReferenceArticle.setPrixReference(pmp.getPrixMoyPondere());
                    log.debug("prix reference suivant pmp :{}", matchedPrixReferenceArticle.getPrixReference());

                    listUpdatedRefPrices.add(matchedPrixReferenceArticle);

                });

        prixReferenceArticleRepository.save(listUpdatedRefPrices);
    }
    public void updatePricesAfterTransfertCompanyToBranch(TransfertCompanyBranch transfertCompanyBranch, CategorieDepotEnum categ_depot) {
        
        
            List<PrixMoyPondereArticle> listUpdatedPMP = updatePMPAfterTransfertCompanyToBranch(transfertCompanyBranch);
            if (categ_depot.equals(UU)) {
                updateRefPriceAfterTransfertCompanyToBranch(transfertCompanyBranch, listUpdatedPMP);
            }
      
    }
        public List<PrixMoyPondereArticle> updatePMPAfterTransfertCompanyToBranch(TransfertCompanyBranch transfertCompanyBranch) {
        log.debug("updating PMP on add reception");
        Set<Integer> codeArts = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()
                .stream().map(item -> item.getCodeArticle()).distinct().collect(Collectors.toSet());
        List<Depsto> depstos = stockService.findByCodartInAndQteGreaterThanZero(codeArts);
        List<PrixMoyPondereArticle> oldPmps = prixMoyPondereArticleRepository.findByArticleIn(codeArts.toArray(new Integer[codeArts.size()]));
        List<PrixMoyPondereArticle> newPrixMoyArtList = new ArrayList();

        List<ArticleUniteDTO> articlePHunits = transfertCompanyBranch.getCategDepot().equals(CategorieDepotEnum.PH) ? paramAchatServiceClient.articlePHFindbyListCode(codeArts).stream().flatMap(articlePH -> articlePH.getArticleUnites().stream()).collect(toList()) : new ArrayList();

        codeArts.forEach(codArt -> {

            BigDecimal b4ReceptionQte = depstos.stream()
                    .filter(item -> item.getCodart().equals(codArt) && !item.getNumBon().equals(transfertCompanyBranch.getNumBon()))
                    .collect(Collectors.reducing(BigDecimal.ZERO, item -> {
                        if (transfertCompanyBranch.getCategDepot().equals(CategorieDepotEnum.PH)) {
                            BigDecimal nbrePiece = articlePHunits.stream()
                                    .filter(elt -> elt.getCodeUnite().equals(item.getUnite()) && elt.getCodeArticle().equals(item.getCodart()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(item.getCodart().toString())))
                                    .getNbPiece();
                            return item.getQte().divide(nbrePiece, 5, RoundingMode.CEILING);
                        } else {
                            return item.getQte();
                        }
                    }, (a, b) -> a.add(b)));
            log.debug("b4ReceptionQte pour l artcile {} est {}", codArt, b4ReceptionQte);

//            BigDecimal b4ReceptionQte = depstos.stream()
//                    .filter(item -> item.getCodart().equals(codArt) && !item.getNumBon().equals(bonReception.getNumbon()))
//                    .collect(Collectors.reducing(BigDecimal.ZERO, item -> item.getQte(), (a, b) -> a.add(b)));
            PrixMoyPondereArticle oldPmp = oldPmps.stream().filter(item -> item.getArticle().equals(codArt)).findFirst()
                    .orElse(null);
            if (oldPmp == null) {
                DetailTransfertCompanyBranch matchingDetail = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()
                        .stream()
                        .filter(x -> x.getCodeArticle().equals(codArt) && x.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0)
                        .findFirst()
                        .orElse(null);

                oldPmp = matchingDetail == null
                        ? new PrixMoyPondereArticle(codArt, BigDecimal.ZERO, new Date())
                        : new PrixMoyPondereArticle(codArt, matchingDetail.getPrixUnitaire()
                                .multiply(BigDecimal.valueOf(100).subtract(matchingDetail.getRemise().divide(BigDecimal.valueOf(100)))), new Date());
            }
            BigDecimal oldPrixmoy = oldPmp.getPrixMoyPondere();
            log.debug(" le codart est {} et oldPMP est {}", codArt, oldPmp.getPrixMoyPondere());
            DetailTransfertCompanyBranch recepitionedMvtstoBAs = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()
                    .stream()
                    .filter(x -> x.getCodeArticle().equals(codArt))
                    .map(item
                            -> {
                        item.setPmpPrecedent(oldPrixmoy);
                        item.setQuantitePrecedante(b4ReceptionQte);
                        return new DetailTransfertCompanyBranch(item);
                    })
                    .collect(Collectors.reducing(new DetailTransfertCompanyBranch(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {

                        a.setQuantite(a.getQuantite().add(b.getQuantite()));
                        a.setMontantHt(a.getMontantHt().add(b.getMontantHt()));
                        return a;

                    }));

            BigDecimal sumPrixHt = (oldPmp.getPrixMoyPondere().multiply(b4ReceptionQte)).add(recepitionedMvtstoBAs.getMontantHt());
            log.debug("sumPrixHt pour l artcile {} est {}", codArt, sumPrixHt);
            BigDecimal sumQte = recepitionedMvtstoBAs.getQuantite().add(b4ReceptionQte);
            log.debug("sumQte pour l artcile {}  est {}", codArt, sumQte);
            BigDecimal pmp = sumPrixHt.divide(sumQte, 2, RoundingMode.HALF_EVEN);
            oldPmp.setPrixMoyPondere(pmp);
            log.debug("new calculated pmp du codart {} est {}", codArt, pmp);
            newPrixMoyArtList.add(oldPmp);
        });

        prixMoyPondereArticleRepository.save(newPrixMoyArtList);
        return newPrixMoyArtList;
    }
    
     private void updateRefPriceAfterTransfertCompanyToBranch(TransfertCompanyBranch transfertCompanyBranch, List<PrixMoyPondereArticle> listeUpdatedPMP) {

        log.debug("updating reference price on add reception");
        List<Integer> codeArts = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection().stream().map(item -> item.getCodeArticle()).collect(Collectors.toList());
        List<PrixReferenceArticle> listPrixReference = prixReferenceArticleRepository.findAll(codeArts);
        transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()
                .stream()
                .map((DetailTransfertCompanyBranch detailFactBA) -> {

                    PrixReferenceArticle matchedPrixReferenceArticle = listPrixReference.stream()
                            .filter(item -> detailFactBA.getCodeArticle().equals(item.getArticle()))
                            .findFirst()
                            .orElse(null);
                    if (matchedPrixReferenceArticle == null) {
                        matchedPrixReferenceArticle = new PrixReferenceArticle(detailFactBA.getCodeArticle(), detailFactBA.getPrixUnitaire().setScale(2, RoundingMode.HALF_UP));

                    };
                    switch (prixChoisiPourCalculPrixVenteUU) {

                        case "pmp":

                            PrixMoyPondereArticle matchedUpdatedPmp = listeUpdatedPMP
                                    .stream()
                                    .filter(pmp -> pmp.getArticle().equals(detailFactBA.getCodeArticle()))
                                    .findFirst()
                                    .orElseGet(() -> {
                                        PrixMoyPondereArticle pmp = new PrixMoyPondereArticle(detailFactBA.getCodeArticle(), detailFactBA.getPrixUnitaire(), new Date());
//                                        newPrixMoyArtList.add(pmp);
                                        return pmp;
                                    });
                            if (matchedUpdatedPmp.getPrixMoyPondere().compareTo(BigDecimal.ZERO) > 0) {
                                matchedPrixReferenceArticle.setPrixReference(matchedUpdatedPmp.getPrixMoyPondere());
                            }
                            log.debug("prix reference suivant pmp :{}", matchedPrixReferenceArticle.getPrixReference());
                            break;
                        case "prix-reference":
                            if (detailFactBA.getIsReferencePrice()) {
                                matchedPrixReferenceArticle.setPrixReference(detailFactBA.getPrixUnitaire().setScale(2, RoundingMode.HALF_UP));
                                log.debug("prix reference suivant reference checeked :{}", matchedPrixReferenceArticle.getPrixReference());
                            }
                            break;
                        case "dernier-prix-achat":
                            if (detailFactBA.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0) {
                                matchedPrixReferenceArticle.setPrixReference(detailFactBA.getPrixUnitaire().setScale(2, RoundingMode.HALF_UP));
                                log.debug("prix reference suivant dernier-prix-achat :{}", matchedPrixReferenceArticle.getPrixReference());
                            }
                            break;
                    }
                    prixReferenceArticleRepository.save(matchedPrixReferenceArticle);
                    return matchedPrixReferenceArticle;
                })
                .collect(Collectors.toList());

    }
    /**
     * Recalculate the "Prix reference" based on the new purchase and update it
     * on the DB.
     *
     * @param bonReception the new purchase
     */
    private void updateRefPriceOnADDBA(FactureBA bonReception, List<PrixMoyPondereArticle> listeUpdatedPMP) {

        log.debug("updating reference price on add reception");
        List<Integer> codeArts = bonReception.getDetailFactureBACollection().stream().map(item -> item.getCodart()).collect(Collectors.toList());
        List<PrixReferenceArticle> listPrixReference = prixReferenceArticleRepository.findAll(codeArts);
        bonReception.getDetailFactureBACollection()
                .stream()
                .map((MvtStoBA detailFactBA) -> {

                    PrixReferenceArticle matchedPrixReferenceArticle = listPrixReference.stream()
                            .filter(item -> detailFactBA.getPk().getCodart().equals(item.getArticle()))
                            .findFirst()
                            .orElse(null);
                    if (matchedPrixReferenceArticle == null) {
                        matchedPrixReferenceArticle = new PrixReferenceArticle(detailFactBA.getCodart(), detailFactBA.getPriuni().setScale(2, RoundingMode.HALF_UP));

                    };
                    switch (prixChoisiPourCalculPrixVenteUU) {

                        case "pmp":

                            PrixMoyPondereArticle matchedUpdatedPmp = listeUpdatedPMP
                                    .stream()
                                    .filter(pmp -> pmp.getArticle().equals(detailFactBA.getCodart()))
                                    .findFirst()
                                    .orElseGet(() -> {
                                        PrixMoyPondereArticle pmp = new PrixMoyPondereArticle(detailFactBA.getCodart(), detailFactBA.getPriuni(), new Date());
//                                        newPrixMoyArtList.add(pmp);
                                        return pmp;
                                    });
                            if (matchedUpdatedPmp.getPrixMoyPondere().compareTo(BigDecimal.ZERO) > 0) {
                                matchedPrixReferenceArticle.setPrixReference(matchedUpdatedPmp.getPrixMoyPondere());
                            }
                            log.debug("prix reference suivant pmp :{}", matchedPrixReferenceArticle.getPrixReference());
                            break;
                        case "prix-reference":
                            if (detailFactBA.getIsPrixReference()) {
                                matchedPrixReferenceArticle.setPrixReference(detailFactBA.getPriuni().setScale(2, RoundingMode.HALF_UP));
                                log.debug("prix reference suivant reference checeked :{}", matchedPrixReferenceArticle.getPrixReference());
                            }
                            break;
                        case "dernier-prix-achat":
                            if (detailFactBA.getPriuni().compareTo(BigDecimal.ZERO) > 0) {
                                matchedPrixReferenceArticle.setPrixReference(detailFactBA.getPriuni().setScale(2, RoundingMode.HALF_UP));
                                log.debug("prix reference suivant dernier-prix-achat :{}", matchedPrixReferenceArticle.getPrixReference());
                            }
                            break;
                    }
                    prixReferenceArticleRepository.save(matchedPrixReferenceArticle);
                    return matchedPrixReferenceArticle;
                })
                .collect(Collectors.toList());

    }

    /**
     * Recalculate the "Pmp (prix moy pondere" based on the new purchase and
     * update it on the DB.
     *
     * @param bonReception the new purchase
     * @return
     */
    public List<PrixMoyPondereArticle> updatePMPOnADDBA(FactureBA bonReception) {
        log.debug("updating PMP on add reception");
        Set<Integer> codeArts = bonReception.getDetailFactureBACollection().stream().map(item -> item.getCodart()).distinct().collect(Collectors.toSet());
        List<Depsto> depstos = stockService.findByCodartInAndQteGreaterThanZero(codeArts);
        List<PrixMoyPondereArticle> oldPmps = prixMoyPondereArticleRepository.findByArticleIn(codeArts.toArray(new Integer[codeArts.size()]));
        List<PrixMoyPondereArticle> newPrixMoyArtList = new ArrayList();

        List<ArticleUniteDTO> articlePHunits = bonReception.getCategDepot().equals(CategorieDepotEnum.PH) ? paramAchatServiceClient.articlePHFindbyListCode(codeArts).stream().flatMap(articlePH -> articlePH.getArticleUnites().stream()).collect(toList()) : new ArrayList();

        codeArts.forEach(codArt -> {

            BigDecimal b4ReceptionQte = depstos.stream()
                    .filter(item -> item.getCodart().equals(codArt) && !item.getNumBon().equals(bonReception.getNumbon()))
                    .collect(Collectors.reducing(BigDecimal.ZERO, item -> {
                        if (bonReception.getCategDepot().equals(CategorieDepotEnum.PH)) {
                            BigDecimal nbrePiece = articlePHunits.stream()
                                    .filter(elt -> elt.getCodeUnite().equals(item.getUnite()) && elt.getCodeArticle().equals(item.getCodart()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(item.getCodart().toString())))
                                    .getNbPiece();
                            return item.getQte().divide(nbrePiece, 5, RoundingMode.CEILING);
                        } else {
                            return item.getQte();
                        }
                    }, (a, b) -> a.add(b)));
            log.debug("b4ReceptionQte pour l artcile {} est {}", codArt, b4ReceptionQte);

//            BigDecimal b4ReceptionQte = depstos.stream()
//                    .filter(item -> item.getCodart().equals(codArt) && !item.getNumBon().equals(bonReception.getNumbon()))
//                    .collect(Collectors.reducing(BigDecimal.ZERO, item -> item.getQte(), (a, b) -> a.add(b)));
            PrixMoyPondereArticle oldPmp = oldPmps.stream().filter(item -> item.getArticle().equals(codArt)).findFirst()
                    .orElse(null);
            if (oldPmp == null) {
                MvtStoBA matchingMvtstoBa = bonReception.getDetailFactureBACollection()
                        .stream()
                        .filter(x -> x.getCodart().equals(codArt) && x.getPriuni().compareTo(BigDecimal.ZERO) > 0)
                        .findFirst()
                        .orElse(null);

                oldPmp = matchingMvtstoBa == null
                        ? new PrixMoyPondereArticle(codArt, BigDecimal.ZERO, new Date())
                        : new PrixMoyPondereArticle(codArt, matchingMvtstoBa.getPriuni()
                                .multiply(BigDecimal.valueOf(100).subtract(matchingMvtstoBa.getRemise().divide(BigDecimal.valueOf(100)))), new Date());
            }
            BigDecimal oldPrixmoy = oldPmp.getPrixMoyPondere();
            log.debug(" le codart est {} et oldPMP est {}", codArt, oldPmp.getPrixMoyPondere());
            MvtStoBA recepitionedMvtstoBAs = bonReception.getDetailFactureBACollection()
                    .stream()
                    .filter(x -> x.getCodart().equals(codArt))
                    .map(item
                            -> {
                        item.setPmpPrecedent(oldPrixmoy);
                        item.setQuantitePrecedante(b4ReceptionQte);
                        return new MvtStoBA(item);
                    })
                    .collect(Collectors.reducing(new MvtStoBA(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {

                        a.setQuantite(a.getQuantite().add(b.getQuantite()));
                        a.setMontht(a.getMontht().add(b.getMontht()));
                        return a;

                    }));

            BigDecimal sumPrixHt = (oldPmp.getPrixMoyPondere().multiply(b4ReceptionQte)).add(recepitionedMvtstoBAs.getMontht());
            log.debug("sumPrixHt pour l artcile {} est {}", codArt, sumPrixHt);
            BigDecimal sumQte = recepitionedMvtstoBAs.getQuantite().add(b4ReceptionQte);
            log.debug("sumQte pour l artcile {}  est {}", codArt, sumQte);
            BigDecimal pmp = sumPrixHt.divide(sumQte, 2, RoundingMode.HALF_EVEN);
            oldPmp.setPrixMoyPondere(pmp);
            log.debug("new calculated pmp du codart {} est {}", codArt, pmp);
            newPrixMoyArtList.add(oldPmp);
        });

        prixMoyPondereArticleRepository.save(newPrixMoyArtList);
        return newPrixMoyArtList;
    }

    public List<PrixMoyPondereArticle> updatePMPOnRedressement(FactureBE facturebe) {
        List<PrixMoyPondereArticle> newPrixMoyArtList = new ArrayList();
        if (!facturebe.getDetailFactureBECollection().isEmpty()) {

            log.debug("updating PMP on add redresssement");

            Set<Integer> codeArts = facturebe.getDetailFactureBECollection().stream().filter(item -> item.getQuantite().compareTo(BigDecimal.ZERO) > 0).map(item -> item.getCodart()).distinct().collect(Collectors.toSet());
            log.debug("les articles sont {}", codeArts);

            List<Depsto> depstos = stockService.findByCodartInAndQteGreaterThanZero(codeArts);
            List<PrixMoyPondereArticle> oldPmps = prixMoyPondereArticleRepository.findByArticleIn(codeArts.toArray(new Integer[codeArts.size()]));

            List<ArticleUniteDTO> articlePHunits = facturebe.getCategDepot().equals(CategorieDepotEnum.PH) ? paramAchatServiceClient.articlePHFindbyListCode(codeArts).stream().flatMap(articlePH -> articlePH.getArticleUnites().stream()).collect(toList()) : new ArrayList();

            codeArts.forEach(codArt -> {

                BigDecimal b4ReceptionQte = depstos.stream()
                        .filter(item -> item.getCodart().equals(codArt) && !item.getNumBon().equals(facturebe.getNumbon()))
                        .collect(Collectors.reducing(BigDecimal.ZERO, item -> {
                            if (facturebe.getCategDepot().equals(CategorieDepotEnum.PH)) {
                                BigDecimal nbrePiece = articlePHunits.stream()
                                        .filter(elt -> elt.getCodeUnite().equals(item.getUnite()) && elt.getCodeArticle().equals(item.getCodart()))
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(item.getCodart().toString())))
                                        .getNbPiece();
                                return item.getQte().divide(nbrePiece, 5, RoundingMode.CEILING);
                            } else {
                                return item.getQte();
                            }
                        }, (a, b) -> a.add(b)));

                log.debug("b4ReceptionQte pour l artcile {} est {}", codArt, b4ReceptionQte);

                PrixMoyPondereArticle oldPmp = oldPmps.stream().filter(item -> item.getArticle().equals(codArt)).findFirst().orElseGet(() -> {

                    MvtStoBE matchingMvtstoBe = facturebe.getDetailFactureBECollection()
                            .stream()
                            .filter(x -> x.getCodart().equals(codArt) && x.getPriuni().compareTo(BigDecimal.ZERO) > 0 && x.getQuantite().compareTo(BigDecimal.ZERO) > 0)
                            .findFirst()
                            .orElse(null);

                    PrixMoyPondereArticle pmp = matchingMvtstoBe == null ? new PrixMoyPondereArticle(codArt, BigDecimal.ZERO, new Date()) : new PrixMoyPondereArticle(codArt, matchingMvtstoBe.getPriuni(), new Date());
                    newPrixMoyArtList.add(pmp);
                    return pmp;
                });

                log.debug(" le codart est {} et oldPMP est {}", codArt, oldPmp.getPrixMoyPondere());

//                Depsto recepitionedDepsto = depstos.stream()
//                        .filter(item -> item.getCodart().equals(codArt) && item.getNumBon().equals(numBon))
//                        .collect(Collectors.reducing(new Depsto(BigDecimal.ZERO), (a, b) -> {
//                            a.setQte(a.getQte().add(b.getQte()));
//                            a.setPu(b.getPu());
//                            return a;
//                        }));
                MvtStoBE matchingMvtstoBEs = facturebe.getDetailFactureBECollection()
                        .stream()
                        .filter(x -> x.getCodart().equals(codArt) && x.getQuantite().compareTo(BigDecimal.ZERO) > 0)
                        .map(item
                                -> {
                            item.setPmpPrecedent(oldPmp.getPrixMoyPondere());
                            item.setQuantitePrecedante(b4ReceptionQte);

                            return new MvtStoBE(item);
                        })
                        .collect(Collectors.reducing(new MvtStoBE(BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
                            if (facturebe.getCategDepot().equals(CategorieDepotEnum.PH)) {
                                BigDecimal nbrePiece = articlePHunits.stream()
                                        .filter(elt -> elt.getCodeUnite().equals(b.getUnite()) && elt.getCodeArticle().equals(b.getCodart()))
                                        .findFirst()
                                        .get()
                                        .getNbPiece();
                                b.setPriuni(b.getPriuni().multiply(nbrePiece));
                                b.setQuantite(b.getQuantite().divide(nbrePiece, 5, RoundingMode.CEILING));
                            }
                            log.debug(" a quantite est {} et quantite b est {}", a.getQuantite(), b.getQuantite());
                            a.setQuantite(a.getQuantite().add(b.getQuantite()));
                            a.setPriuni(a.getPriuni().add(b.getPriuni().multiply(b.getQuantite())));
                            return a;

                        }));
                log.debug("la quantite de matchingMvtstoBEs EST {}et son priuni est{}  ", matchingMvtstoBEs.getQuantite(), matchingMvtstoBEs.getPriuni());
                BigDecimal sumPrixHt = (oldPmp.getPrixMoyPondere().multiply(b4ReceptionQte)).add(matchingMvtstoBEs.getPriuni());
                log.debug("sumPrixHt pour l artcile {} est {}", codArt, sumPrixHt);
                BigDecimal sumQte = matchingMvtstoBEs.getQuantite().add(b4ReceptionQte);
                log.debug("sumQte pour l artcile {}  est {}", codArt, sumQte);
                BigDecimal newPMP = sumPrixHt.divide(sumQte, 2, RoundingMode.HALF_EVEN);
                log.debug("new calculated pmp du codart {} est {}", codArt, newPMP);
                oldPmp.setPrixMoyPondere(newPMP);
                newPrixMoyArtList.add(oldPmp);

            });
            prixMoyPondereArticleRepository.save(newPrixMoyArtList);
        }
        return newPrixMoyArtList;
    }

    /**
     * Recalculate the "Prix reference" after deleteing the purchase and update
     * it on the DB.
     *
     * @param bonReception the new purchase
     */
    /**
     * Recalculate the "Pmp (prix moy pondere" based on the new purchase and
     * update it on the DB. only if it is the last purchase and there is no
     * rederessement on it .
     *
     * @param bonReception the new purchase
     */
    private void updatePMPOnDELETEBA(FactureBA bonReception) {
        log.debug("updating PMP on delete reception");
        List<Integer> codeArts = bonReception.getDetailFactureBACollection().stream().map(item -> item.getCodart()).distinct().collect(Collectors.toList());
        List<Depsto> listArticles = stockService.findByCodeArticlesAndNumBonNot(codeArts, bonReception.getNumbon());

        List<PrixMoyPondereArticle> newPrixMoyArtList = new ArrayList();
        codeArts.forEach(codArt -> {
            Depsto mergedDepsto = listArticles.stream()
                    .filter(item -> item.getCodart().equals(codArt))
                    .map(filtItem -> {
                        Depsto depst = new Depsto();
                        depst.setQte(filtItem.getQte());
                        depst.setPrixHT(filtItem.getQte().multiply(filtItem.getPu()));
                        return depst;
                    })
                    .reduce((a, b) -> {
                        a.setQte(a.getQte().add(b.getQte()));
                        a.setPrixHT(a.getPrixHT().add(b.getPrixHT()));
                        return a;
                    })
                    .orElse(null);
            BigDecimal pmp = BigDecimal.ZERO;
            if (mergedDepsto != null) {
                BigDecimal sumPrixHt = mergedDepsto.getPrixHT();
                BigDecimal sumQte = mergedDepsto.getQte();
                pmp = sumPrixHt.divide(sumQte, 2, RoundingMode.HALF_EVEN);
            }
            newPrixMoyArtList.add(new PrixMoyPondereArticle(codArt, pmp, new Date()));
        });
        prixMoyPondereArticleRepository.save(newPrixMoyArtList);

    }

    //logique changé plus de calcul de pmp apres les retours
//    public void updatePricesAfterAvoirFournisseur(AvoirFournisseur avoirFournisseur, CategorieDepotEnum categ_depot) {
//
//       List<PrixMoyPondereArticle>  listUpdatedPmp= updatePMPOnAddAvoirFournisseur(avoirFournisseur);
//
//    }
    /**
     * Update articles prices based on the reception or the return (bon du
     * retour).
     *
     * @param factureBA The reception or the return that will be used to
     * recalculate the new prices
     * @param listeDetailsReceptionCorrespondants
     * @param datBonReception
     */
    public void updatePricesAfterReturn(FactureBA factureBA, List<MvtStoBA> listeDetailsReceptionCorrespondants, LocalDateTime datBonReception) {
        Preconditions.checkBusinessLogique(RT.equals(factureBA.getTypbon()), "Invalid typBon. You must pass a return ");
//        List<PrixMoyPondereArticle> listeUpdatedPmp = updatePMPOnAddRT(factureBA); //plus de calcul de pmp pour les retour 

        if (factureBA.getCategDepot().equals(UU)) {
            updateRefPriceOnAddRT(factureBA, listeDetailsReceptionCorrespondants, datBonReception);
        }
    }

    /**
     * Recalculate the "Prix reference" based on the new purchase and update it
     * on the DB.
     *
     * @param bonRetour the new purchase
     *
     */
    private void updateRefPriceOnAddRT(FactureBA bonRetour, List<MvtStoBA> listeDetailsReceptionCorrespondants, LocalDateTime datBonReception) {
        log.debug("UPDATE prix ref on retour");

//on va travailler seulement dans le cas d un retour total
        List<DetailReceptionDTO> listeMvtstoBaRetournesTotalement = mvtStoBAService.returnListeArticlesRetournesTotalmenet(listeDetailsReceptionCorrespondants);

        Set<Integer> listeArticlesRetournesTotalement = listeMvtstoBaRetournesTotalement.stream().map(DetailReceptionDTO::getRefArt).collect(Collectors.toSet());
        log.debug("listeArticlesRetournesTotalement sont {} ", listeArticlesRetournesTotalement);
// on ne va pas prendre directement la liste des articles retournés totalmenet car il peut avoir un article dans la reception retourne totalmenet dans un retour precedant non existant dans ce retour
        Set<Integer> codeArts = bonRetour.getDetailFactureBACollection()
                .stream()
                .map(item -> item.getCodart())
                .filter(elt -> listeArticlesRetournesTotalement.contains(elt))
                .collect(Collectors.toSet());
        log.debug("list of articles dans le retour  retournes totalement   {}", codeArts);

        List<MvtStoBA> listeMvtstoBAReceptionnesApres = mvtStoBAService.findByPk_CodartInAndFactureBA_DatbonAfterAndTypeBon(codeArts, datBonReception, TypeBonEnum.BA);
        log.debug("listeDerniersMvtstoBA est {}", listeMvtstoBAReceptionnesApres);
        Set<Integer> codeArtsReceptionnesApres = listeMvtstoBAReceptionnesApres.stream().map(item -> item.getCodart()).distinct().collect(Collectors.toSet());
        codeArts.removeIf(codart -> codeArtsReceptionnesApres.contains(codart));

        log.debug(" liste Codart to update est {}", codeArts);

        List<PrixReferenceArticle> listPrixReference = prixReferenceArticleRepository.findAll(codeArts);
        List<ArticleDTO> listeArticles = paramAchatServiceClient.articleFindbyListCode(codeArts);

        List<PrixReferenceArticle> newListPrixReference = bonRetour.getDetailFactureBACollection()
                .stream()
                .filter(detailFactBA -> codeArts.contains(detailFactBA.getCodart()))
                .map(detailRetour -> {

                    PrixReferenceArticle matchedPrixReferenceArticle = listPrixReference.stream()
                            .filter(item -> detailRetour.getPk().getCodart().equals(item.getArticle()))
                            .findFirst()
                            .get();

                    log.debug("the old reference price is {}", matchedPrixReferenceArticle);
                    log.debug("the canceled reception detail is  {}", detailRetour);
                    log.debug("Should the canceled be used in calculating the reference price   {}", detailRetour.getIsPrixReference());
                    switch (prixChoisiPourCalculPrixVenteUU) {
                        case "prix-reference":
                            if (detailRetour.getIsPrixReference() && detailRetour.getPriuni().compareTo(matchedPrixReferenceArticle.getPrixReference()) == 0) {
                                BigDecimal newPrice = mvtStoBAService.findMaxPriuniByCodeArticleAndIsPrixRefAndNotReturned(detailRetour.getCodart(), Boolean.TRUE);
                                log.debug("the new reference price is {}", newPrice);
                                if (newPrice == null) {
                                    ArticleDTO articleDTO = listeArticles
                                            .stream()
                                            .filter(item -> detailRetour.getPk().getCodart().equals(item.getCode()))
                                            .findFirst()
                                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(detailRetour.getPk().getCodart().toString())));
                                    newPrice = articleDTO.getAncienPrixAchat();

                                }

                                matchedPrixReferenceArticle.setPrixReference(newPrice);
                            }
                            break;
                        case "dernier-prix-achat":
                            if (detailRetour.getPriuni().compareTo(BigDecimal.ZERO) > 0) {

                                DetailReceptionDTO matchingDetailReception = listeMvtstoBaRetournesTotalement
                                        .stream()
                                        .filter(elt -> elt.getRefArt().equals(detailRetour.getCodart()))
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-item -reception", new Throwable(detailRetour.getCodeSaisi())));

                                log.debug("matchingDetailReception est {}", matchingDetailReception);
                                matchedPrixReferenceArticle.setPrixReference(matchingDetailReception.getAncienPrixAchat().setScale(2, RoundingMode.HALF_UP));
                                log.debug("prix reference suivant dernier-prix-achat :{}", matchedPrixReferenceArticle.getPrixReference());
                            }
                            break;

                    }
                    return matchedPrixReferenceArticle;

                })
                .collect(Collectors.toList());

        prixReferenceArticleRepository.save(newListPrixReference);
    }

    /**
     * Recalculate the "Pmp (prix moy pondere" based on the new purchase and
     * update it on the DB.
     *
     * @param bonRetour the new purchase
     */
    private List<PrixMoyPondereArticle> updatePMPOnAddRT(FactureBA bonRetour) {
        log.debug("updatePMPOnAddRT");
        List<Integer> codeArts = bonRetour.getDetailFactureBACollection().stream().map(item -> item.getPk().getCodart()).distinct().collect(Collectors.toList());
        List<Depsto> depstos = stockService.findByCodartInAndQteGreaterThanZero(codeArts);
        List<PrixMoyPondereArticle> oldPmps = prixMoyPondereArticleRepository.findByArticleIn(codeArts.toArray(new Integer[codeArts.size()]));
        List<ArticleUniteDTO> articlePHunits = bonRetour.getCategDepot().equals(CategorieDepotEnum.PH) ? paramAchatServiceClient.articlePHFindbyListCode(codeArts).stream().flatMap(articlePH -> articlePH.getArticleUnites().stream()).collect(toList()) : new ArrayList();
        List<PrixMoyPondereArticle> listeUpdatedPmp = new ArrayList();
        codeArts.forEach(codeArt -> {

            log.debug("codart is {}", codeArt);
            BigDecimal qteAfterReturn = depstos.stream()
                    .filter(item -> item.getCodart().equals(codeArt))
                    .collect(Collectors.reducing(BigDecimal.ZERO, item -> {
                        if (bonRetour.getCategDepot().equals(CategorieDepotEnum.PH)) {
                            BigDecimal nbrePiece = articlePHunits.stream()
                                    .filter(elt -> elt.getCodeUnite().equals(item.getUnite()) && elt.getCodeArticle().equals(item.getCodart()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(item.getCodart().toString())))
                                    .getNbPiece();
                            return item.getQte().divide(nbrePiece, 5, RoundingMode.CEILING);
                        } else {
                            return item.getQte();
                        }
                    }, (a, b) -> a.add(b)));

            if (qteAfterReturn.compareTo(BigDecimal.ZERO) == 0) {
                return;
            }
            log.debug("qteAfterReturn pour l artcile {} est {}", codeArt, qteAfterReturn);
            PrixMoyPondereArticle oldPmp = oldPmps.stream().filter(item -> item.getArticle().equals(codeArt)).findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-pmp"));
            // log.debug("old pmp for article {} est {}", codeArt, oldPmp.getPrixMoyPondere());
            MvtStoBA returnedMvtsto = bonRetour.getDetailFactureBACollection()
                    .stream()
                    .filter(item -> item.getCodart().equals(codeArt))
                    .collect(Collectors.reducing(new MvtStoBA(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
                        a.setQuantite(a.getQuantite().add(b.getQuantite()));
                        a.setMontht(a.getMontht().add(b.getMontht()));
                        return a;
                    }));
            log.debug("returnedMvtsto qte pour l artcile {} est {}", codeArt, returnedMvtsto.getQuantite());
            // formule (pmp*qteB4Return - PU*QteReturned)/qteAfterReturn
            BigDecimal qteB4Return = qteAfterReturn.add(returnedMvtsto.getQuantite());
            bonRetour.getDetailFactureBACollection()
                    .stream()
                    .filter(item -> item.getCodart().equals(codeArt))
                    .forEach(item -> {
                        item.setPmpPrecedent(oldPmp.getPrixMoyPondere());
                        item.setQuantitePrecedante(qteB4Return);

                    });
            BigDecimal sumPrixHt = (oldPmp.getPrixMoyPondere().multiply(qteB4Return)).subtract(returnedMvtsto.getMontht());
            log.debug("sumPrixHt pour l artcile {} est {}", codeArt, sumPrixHt);
            BigDecimal pmp = sumPrixHt.divide(qteAfterReturn, 2, RoundingMode.HALF_EVEN);
            oldPmp.setPrixMoyPondere(pmp);
            log.debug("new calculated pmp du codart {} est {}", codeArt, pmp);
            listeUpdatedPmp.add(oldPmp);
        });
        return listeUpdatedPmp;
    }

    public BigDecimal resolveSellingPrice(MaquetteDto maquette, CategorieArticleDTO categorieArticleDTO, BigDecimal prixAchat) {
        log.debug("resolveSellingPrice categorieArticleDTO.getCodeMaquetteCategorieArticle() : {}", categorieArticleDTO.getCodeMaquetteCategorieArticle());
        BigDecimal marginalValue;
        if (categorieArticleDTO.getCodeMaquetteCategorieArticle() == null) {
            MargeDto palier = maquette.getMarges().stream()
                    .filter(elt -> {
                        log.debug(" {} < {} < {}   is  {}", elt.getDu(), prixAchat, elt.getAu(), (prixAchat.compareTo(elt.getDu()) >= 0) && (prixAchat.compareTo(elt.getAu()) <= 0));
                        return prixAchat.compareTo(elt.getDu()) >= 0 && prixAchat.compareTo(elt.getAu()) <= 0;
                    })
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(palier != null, "article-uu.calculate-price.missing-palier", prixAchat.toString());
            marginalValue = palier.getMarge().divide(BigDecimal.valueOf(100.0));
            prixAchat.setScale(2, RoundingMode.HALF_UP);
        } else {
            List<PalierMargeCategorieArticleDTO> palierMargeCategorieArticleDTOs = paramAchatServiceClient.palierMargeCategorieArticleById(categorieArticleDTO.getCodeMaquetteCategorieArticle());

            PalierMargeCategorieArticleDTO palierMargeCategorieArticleDTO = palierMargeCategorieArticleDTOs.stream()
                    .filter(elt -> {
                        log.debug(" palier marge categorie article {} ===>{}  ", elt.getDu(), elt.getAu());
                        return prixAchat.compareTo(elt.getDu()) >= 0 && prixAchat.compareTo(elt.getAu()) <= 0;
                    })
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(palierMargeCategorieArticleDTO != null, "article-uu.calculate-price.missing-palier-categorie-article", prixAchat.toString());

            marginalValue = palierMargeCategorieArticleDTO.getMarge().divide(BigDecimal.valueOf(100.0));
        }
        return prixAchat.multiply(marginalValue.add(BigDecimal.ONE)).setScale(2, RoundingMode.HALF_UP);
    }

    private List<PrixMoyPondereArticle> updatePMPOnAddAvoirFournisseur(AvoirFournisseur avoirFournisseur) {
        log.debug("updatePMP On Add avoirFournisseur");
        List<Integer> codeArts = avoirFournisseur.getMvtstoAFList().stream().map(item -> item.getCodart()).distinct().collect(Collectors.toList());
        List<Depsto> depstos = stockService.findByCodartInAndQteGreaterThanZero(codeArts);
        List<PrixMoyPondereArticle> oldPmps = prixMoyPondereArticleRepository.findByArticleIn(codeArts.toArray(new Integer[codeArts.size()]));
        List<ArticleUniteDTO> articlePHunits = avoirFournisseur.getCategDepot().equals(CategorieDepotEnum.PH) ? paramAchatServiceClient.articlePHFindbyListCode(codeArts).stream().flatMap(articlePH -> articlePH.getArticleUnites().stream()).collect(toList()) : new ArrayList();
        List<PrixMoyPondereArticle> listeUpdatedPmp = new ArrayList();
        codeArts.forEach(codeArt -> {

            log.debug("codart is {}", codeArt);
            BigDecimal qteAfterReturn = depstos.stream()
                    .filter(item -> item.getCodart().equals(codeArt))
                    .collect(Collectors.reducing(BigDecimal.ZERO, item -> {
                        if (avoirFournisseur.getCategDepot().equals(CategorieDepotEnum.PH)) {
                            BigDecimal nbrePiece = articlePHunits.stream()
                                    .filter(elt -> elt.getCodeUnite().equals(item.getUnite()) && elt.getCodeArticle().equals(item.getCodart()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(item.getCodart().toString())))
                                    .getNbPiece();
                            return item.getQte().divide(nbrePiece, 5, RoundingMode.CEILING);
                        } else {
                            return item.getQte();
                        }
                    }, (a, b) -> a.add(b)));

            if (qteAfterReturn.compareTo(BigDecimal.ZERO) == 0) {
                return;
            }
            log.debug("qteAfterReturn pour l artcile {} est {}", codeArt, qteAfterReturn);
            PrixMoyPondereArticle oldPmp = oldPmps.stream().filter(item -> item.getArticle().equals(codeArt)).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-pmp"));
            // log.debug("old pmp for article {} est {}", codeArt, oldPmp.getPrixMoyPondere());
            MvtstoAF returnedMvtsto = avoirFournisseur.getMvtstoAFList()
                    .stream()
                    .filter(item -> item.getCodart().equals(codeArt))
                    .collect(Collectors.reducing(new MvtstoAF(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
                        a.setQuantite(a.getQuantite().add(b.getQuantite()));
                        a.setMontht(a.getMontht().add(b.getMontht()));
                        return a;
                    }));
            log.debug("returnedMvtsto qte pour l artcile {} est {}", codeArt, returnedMvtsto.getQuantite());
            // formule (pmp*qteB4Return - PU*QteReturned)/qteAfterReturn
            BigDecimal qteB4Return = qteAfterReturn.add(returnedMvtsto.getQuantite());
            avoirFournisseur.getMvtstoAFList()
                    .stream()
                    .filter(item -> item.getCodart().equals(codeArt))
                    .forEach(item -> {
                        item.setPmpPrecedent(oldPmp.getPrixMoyPondere());
                        item.setQuantitePrecedante(qteB4Return);

                    });
            BigDecimal sumPrixHt = (oldPmp.getPrixMoyPondere().multiply(qteB4Return)).subtract(returnedMvtsto.getMontht());
            log.debug("sumPrixHt pour l artcile {} est {}", codeArt, sumPrixHt);
            BigDecimal pmp = sumPrixHt.divide(qteAfterReturn, 2, RoundingMode.HALF_EVEN);
            oldPmp.setPrixMoyPondere(pmp);
            log.debug("new calculated pmp du codart {} est {}", codeArt, pmp);
            listeUpdatedPmp.add(oldPmp);
        });
        return listeUpdatedPmp;
    }

    @Transactional
    public PrixReferenceArticleDTO save(PrixReferenceArticleDTO prixReferenceArticleDTO) {
        PrixReferenceArticle prixReferenceArticle = new PrixReferenceArticle(prixReferenceArticleDTO.getArticleID(), prixReferenceArticleDTO.getReferencePrice());
        prixReferenceArticleRepository.save(prixReferenceArticle);
        return new PrixReferenceArticleDTO(prixReferenceArticle.getPrixReference(), prixReferenceArticle.getArticle());
    }

    @Transactional
    public PMPArticleDTO savePMP(PMPArticleDTO pMPArticleDTO) {
        PrixMoyPondereArticle prixReferenceArticle = new PrixMoyPondereArticle(pMPArticleDTO.getArticleID(), pMPArticleDTO.getPMP());
        prixMoyPondereArticleRepository.save(prixReferenceArticle);
        return new PMPArticleDTO(prixReferenceArticle.getPrixMoyPondere(), prixReferenceArticle.getArticle());
    }

    @Transactional
    public PrixReferenceArticleDTO update(PrixReferenceArticleDTO prixReferenceArticleDTO) {
        PrixReferenceArticle prixReferenceArticleInBase = prixReferenceArticleRepository.findByArticle(prixReferenceArticleDTO.getArticleID());

        prixReferenceArticleInBase.setPrixReference(prixReferenceArticleDTO.getReferencePrice());
        prixReferenceArticleRepository.save(prixReferenceArticleInBase);
        return new PrixReferenceArticleDTO(prixReferenceArticleInBase.getPrixReference(), prixReferenceArticleInBase.getArticle());
    }

    @Transactional
    public Boolean updateListReferencePrice(List<PrixReferenceArticleDTO> listePrixReferenceArticleDTO) {
        List<PrixReferenceArticle> listeUpdateReferencePrice = new ArrayList();
        listePrixReferenceArticleDTO.forEach(elt -> {
            PrixReferenceArticle prixReferenceArticleInBase = prixReferenceArticleRepository.findByArticle(elt.getArticleID());
            if (prixReferenceArticleInBase != null) {
                prixReferenceArticleInBase.setPrixReference(elt.getReferencePrice());

            } else {
                prixReferenceArticleInBase = new PrixReferenceArticle(elt.getArticleID(), elt.getReferencePrice());
                listeUpdateReferencePrice.add(prixReferenceArticleInBase);
            }
        });
        prixReferenceArticleRepository.save(listeUpdateReferencePrice);
        return Boolean.TRUE;
    }

 

    public void updateReferencePriceAfterTransferBranchCompany(TransfertCompanyBranch transfertCompanyBranch, Collection<DetailTransfertCompanyBranch> listeDetailsTransfertCorrespondants, LocalDateTime dateBonTransfer) {
        log.debug("UPDATE prix ref on retour Transfert Company ");

        //on va travailler seulement dans le cas d un retour total
        List<DetailTransfertCompanyBranchDTO> listeDetailRetournesTotalement = transfertCompanyBranchService.returnListeArticlesRetournesTransfertToCompanyTotalmenet(listeDetailsTransfertCorrespondants);

        Set<Integer> listeArticlesRetournesTotalement = listeDetailRetournesTotalement.stream().map(DetailTransfertCompanyBranchDTO::getCodeArticle).collect(Collectors.toSet());
        log.debug("listeArticlesRetournesTotalement sont {} ", listeArticlesRetournesTotalement);
// on ne va pas prendre directement la liste des articles retournés totalmenet car il peut avoir un article dans la reception retourne totalmenet dans un retour precedant non existant dans ce retour
        Set<Integer> codeArts = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()
                .stream()
                .map(item -> item.getCodeArticle())
                .filter(elt -> listeArticlesRetournesTotalement.contains(elt))
                .collect(Collectors.toSet());
        log.debug("list of articles dans le retour  retournes totalement   {}", codeArts);

        List<MvtStoBA> listeMvtstoBAReceptionnesApres = mvtStoBAService.findByPk_CodartInAndFactureBA_DatbonAfterAndTypeBon(codeArts, dateBonTransfer, TypeBonEnum.BA);
        log.debug("listeDerniersMvtstoBA est {}", listeMvtstoBAReceptionnesApres);
        Set<Integer> codeArtsReceptionnesApres = listeMvtstoBAReceptionnesApres.stream().map(item -> item.getCodart()).distinct().collect(Collectors.toSet());
        codeArts.removeIf(codart -> codeArtsReceptionnesApres.contains(codart));

        log.debug(" liste Codart to update est {}", codeArts);

        List<PrixReferenceArticle> listPrixReference = prixReferenceArticleRepository.findAll(codeArts);
        List<ArticleDTO> listeArticles = paramAchatServiceClient.articleFindbyListCode(codeArts);

        List<PrixReferenceArticle> newListPrixReference = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()
                .stream()
                .filter(detailTransfert -> codeArts.contains(detailTransfert.getCodeArticle()))
                .map(detailRetour -> {

                    PrixReferenceArticle matchedPrixReferenceArticle = listPrixReference.stream()
                            .filter(item -> detailRetour.getCodeArticle().equals(item.getArticle()))
                            .findFirst()
                            .get();

                    log.debug("the old reference price is {}", matchedPrixReferenceArticle);
                    log.debug("the canceled reception detail is  {}", detailRetour);
                    log.debug("Should the canceled be used in calculating the reference price   {}", detailRetour.getIsReferencePrice());
                    switch (prixChoisiPourCalculPrixVenteUU) {
                        case "prix-reference":
                            if (detailRetour.getIsReferencePrice()&& detailRetour.getPrixUnitaire().compareTo(matchedPrixReferenceArticle.getPrixReference()) == 0) {
                                BigDecimal newPrice = mvtStoBAService.findMaxPriuniByCodeArticleAndIsPrixRefAndNotReturned(detailRetour.getCodeArticle(), Boolean.TRUE);
                                log.debug("the new reference price is {}", newPrice);
                                if (newPrice == null) {
                                    ArticleDTO articleDTO = listeArticles
                                            .stream()
                                            .filter(item -> detailRetour.getCodeArticle().equals(item.getCode()))
                                            .findFirst()
                                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(detailRetour.getCodeArticle().toString())));
                                    newPrice = articleDTO.getAncienPrixAchat();

                                }

                                matchedPrixReferenceArticle.setPrixReference(newPrice);
                            }
                            break;
                        case "dernier-prix-achat":
                            if (detailRetour.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0) {

                                DetailTransfertCompanyBranchDTO matchingDetailTransfer = listeDetailRetournesTotalement
                                        .stream()
                                        .filter(elt -> elt.getCodeArticle().equals(detailRetour.getCodeArticle()))
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-item -reception", new Throwable(detailRetour.getCodeSaisi())));

                                log.debug("matchingDetailReception est {}", matchingDetailTransfer);
                                matchedPrixReferenceArticle.setPrixReference(matchingDetailTransfer.getAncienPrixAchat().setScale(2, RoundingMode.HALF_UP));
                                log.debug("prix reference suivant dernier-prix-achat :{}", matchedPrixReferenceArticle.getPrixReference());
                            }
                            break;

                    }
                    return matchedPrixReferenceArticle;

                })
                .collect(Collectors.toList());

        prixReferenceArticleRepository.save(newListPrixReference);
    }

//    public List<PrixMoyPondereArticle> updatePMPOnADDTransferFromCompany(TransfertCompanyBranch transfertCompanyBranch) {
//        log.debug("updating PMP on add transfert from Company");
//        Set<Integer> codeArts = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection().stream().map(item -> item.getCodeArticle()).distinct().collect(Collectors.toSet());
//        List<Depsto> depstos = stockService.findByCodartInAndQteGreaterThanZero(codeArts);
//        List<PrixMoyPondereArticle> oldPmps = prixMoyPondereArticleRepository.findByArticleIn(codeArts.toArray(new Integer[codeArts.size()]));
//        List<PrixMoyPondereArticle> newPrixMoyArtList = new ArrayList();
//
//        List<ArticleUniteDTO> articlePHunits = transfertCompanyBranch.getCategDepot().equals(CategorieDepotEnum.PH) ? paramAchatServiceClient.articlePHFindbyListCode(codeArts).stream().flatMap(articlePH -> articlePH.getArticleUnites().stream()).collect(toList()) : new ArrayList();
//
//        codeArts.forEach(codArt -> {
//
//            BigDecimal b4ReceptionQte = depstos.stream()
//                    .filter(item -> item.getCodart().equals(codArt) && !item.getNumBon().equals(transfertCompanyBranch.getNumBon()))
//                    .collect(Collectors.reducing(BigDecimal.ZERO, item -> {
//                        if (transfertCompanyBranch.getCategDepot().equals(CategorieDepotEnum.PH)) {
//                            BigDecimal nbrePiece = articlePHunits.stream()
//                                    .filter(elt -> elt.getCodeUnite().equals(item.getUnite()) && elt.getCodeArticle().equals(item.getCodart()))
//                                    .findFirst()
//                                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(item.getCodart().toString())))
//                                    .getNbPiece();
//                            return item.getQte().divide(nbrePiece, 5, RoundingMode.CEILING);
//                        } else {
//                            return item.getQte();
//                        }
//                    }, (a, b) -> a.add(b)));
//            log.debug("b4ReceptionQte pour l artcile {} est {}", codArt, b4ReceptionQte);
//
////            BigDecimal b4ReceptionQte = depstos.stream()
////                    .filter(item -> item.getCodart().equals(codArt) && !item.getNumBon().equals(bonReception.getNumbon()))
////                    .collect(Collectors.reducing(BigDecimal.ZERO, item -> item.getQte(), (a, b) -> a.add(b)));
//            PrixMoyPondereArticle oldPmp = oldPmps.stream().filter(item -> item.getArticle().equals(codArt)).findFirst()
//                    .orElse(null);
//            if (oldPmp == null) {
//                DetailTransfertCompanyBranch matchingMvtstoBa = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()
//                        .stream()
//                        .filter(x -> x.getCodeArticle().equals(codArt) && x.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0)
//                        .findFirst()
//                        .orElse(null);
//
//                oldPmp = matchingMvtstoBa == null
//                        ? new PrixMoyPondereArticle(codArt, BigDecimal.ZERO, new Date())
//                        : new PrixMoyPondereArticle(codArt, matchingMvtstoBa.getPrixUnitaire()
//                                .multiply(BigDecimal.valueOf(100).subtract(matchingMvtstoBa.getRemise().divide(BigDecimal.valueOf(100)))), new Date());
//            }
//            BigDecimal oldPrixmoy = oldPmp.getPrixMoyPondere();
//            log.debug(" le codart est {} et oldPMP est {}", codArt, oldPmp.getPrixMoyPondere());
//            DetailTransfertCompanyBranch recepitionedMvtstoBAs = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()
//                    .stream()
//                    .filter(x -> x.getCodeArticle().equals(codArt))
//                    .map(item
//                            -> {
//                        item.setPmpPrecedent(oldPrixmoy);
//                        item.setQuantitePrecedante(b4ReceptionQte);
//                        return new DetailTransfertCompanyBranch(item);
//                    })
//                    .collect(Collectors.reducing(new DetailTransfertCompanyBranch(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
//
//                        a.setQuantite(a.getQuantite().add(b.getQuantite()));
//                        a.setMontantHt(a.getMontantHt().add(b.getMontantHt()));
//                        return a;
//
//                    }));
//
//            BigDecimal sumPrixHt = (oldPmp.getPrixMoyPondere().multiply(b4ReceptionQte)).add(recepitionedMvtstoBAs.getMontantHt());
//            log.debug("sumPrixHt pour l artcile {} est {}", codArt, sumPrixHt);
//            BigDecimal sumQte = recepitionedMvtstoBAs.getQuantite().add(b4ReceptionQte);
//            log.debug("sumQte pour l artcile {}  est {}", codArt, sumQte);
//            BigDecimal pmp = sumPrixHt.divide(sumQte, 2, RoundingMode.HALF_EVEN);
//            oldPmp.setPrixMoyPondere(pmp);
//            log.debug("new calculated pmp du codart {} est {}", codArt, pmp);
//            newPrixMoyArtList.add(oldPmp);
//        });
//
//        prixMoyPondereArticleRepository.save(newPrixMoyArtList);
//        return newPrixMoyArtList;
//    }
}
