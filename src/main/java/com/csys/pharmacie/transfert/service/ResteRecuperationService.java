package com.csys.pharmacie.transfert.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.SatisfactionEnum;
import com.csys.pharmacie.helper.SatisfactionFactureEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.dto.DepstoDTO;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.transfert.domain.Btfe;
import com.csys.pharmacie.transfert.domain.FactureBT;
import com.csys.pharmacie.transfert.domain.ResteRecuperation;
import com.csys.pharmacie.transfert.domain.ResteRecuperationPK;
import com.csys.pharmacie.transfert.dto.BonTransfertRecupDTO;
import com.csys.pharmacie.transfert.dto.MvtstoBTDTO;
import com.csys.pharmacie.transfert.dto.ResteRecuperationDTO;
import com.csys.pharmacie.transfert.factory.ResteRecuperationFactory;
import com.csys.pharmacie.transfert.repository.BtfeRepository;
import com.csys.pharmacie.transfert.repository.ResteRecuperationRepository;
import com.csys.pharmacie.vente.avoir.domain.FactureAV;
import com.csys.pharmacie.vente.avoir.domain.MvtStoAV;
import com.csys.pharmacie.vente.avoir.domain.MvtstoMvtstoAV;
import com.csys.pharmacie.vente.avoir.dto.FactureAVDTO;
import com.csys.pharmacie.vente.avoir.dto.MvtStoAVDTO;
import com.csys.pharmacie.vente.avoir.repository.MvtstomvtstoAVRepository;
import com.csys.pharmacie.vente.avoir.service.AvoirService;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import com.csys.pharmacie.vente.quittance.service.FactureService;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing ResteRecuperation.
 */
@Service
@Transactional
public class ResteRecuperationService {

    private final Logger log = LoggerFactory.getLogger(ResteRecuperationService.class);
     private static String clinicName;
          @Value("${clinic-name}")
    public void setClinicName(String clinic) {
        clinicName = clinic;
    }
    private final ResteRecuperationRepository resterecuperationRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final StockService stockService;
    private final FactureService factureService;
    private final ParamService paramService;
    private final BtfeRepository btfeRepository;
    private final BonTransfertInterDepotService bonTransfertInterDepotService;
    private final AvoirService avoirService;
    private final MvtstomvtstoAVRepository mvtstomvtstoAVRepository;

    public ResteRecuperationService(ResteRecuperationRepository resterecuperationRepository, ParamAchatServiceClient paramAchatServiceClient, @Lazy StockService stockService, FactureService factureService, ParamService paramService, BtfeRepository btfeRepository, BonTransfertInterDepotService bonTransfertInterDepotService, AvoirService avoirService, MvtstomvtstoAVRepository mvtstomvtstoAVRepository) {
        this.resterecuperationRepository = resterecuperationRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.stockService = stockService;
        this.factureService = factureService;
        this.paramService = paramService;
        this.btfeRepository = btfeRepository;
        this.bonTransfertInterDepotService = bonTransfertInterDepotService;
        this.avoirService = avoirService;
        this.mvtstomvtstoAVRepository = mvtstomvtstoAVRepository;
    }

    /**
     * Save a resterecuperationDTO.
     *
     * @param resterecuperationDTO
     * @return the persisted entity
     */
    public ResteRecuperationDTO save(ResteRecuperationDTO resterecuperationDTO) {
        log.debug("Request to save ResteRecuperation: {}", resterecuperationDTO);
        ResteRecuperation resterecuperation = ResteRecuperationFactory.resterecuperationDTOToResteRecuperation(resterecuperationDTO);
        resterecuperation = resterecuperationRepository.save(resterecuperation);
        ResteRecuperationDTO resultDTO = ResteRecuperationFactory.resterecuperationToResteRecuperationDTO(resterecuperation);
        return resultDTO;
    }

    /**
     * Save a resterecuperationDTO.
     *
     * @param recuperationsDTOs
     * @param categDepot
     * @return the persisted entity
     */
    public List<ResteRecuperationDTO> save(List<ResteRecuperationDTO> recuperationsDTOs, CategorieDepotEnum categDepot) {
        log.debug("Request to save ResteRecuperation: {}", recuperationsDTOs);

        Collection<ResteRecuperation> resterecuperations = recuperationsDTOs.stream()
                .map(item -> ResteRecuperationFactory.resterecuperationDTOToResteRecuperation(item))
                .collect(Collectors.groupingBy(item -> item.getPk().getCodeArticle(),
                        Collectors.reducing(new ResteRecuperation(BigDecimal.ZERO), (a, b) -> {

                            b.setQuantite(a.getQuantite().add(b.getQuantite()));
                            b.setCategorieDepot(categDepot);
                            return b;
                        }))).values();
        List<ResteRecuperation> result = resterecuperationRepository.save(resterecuperations);
        return ResteRecuperationFactory.resterecuperationToResteRecuperationDTOs(result);
    }

    @Transactional
    public List<String> processRemainingQuantitiesOnRecovering(BonTransfertRecupDTO factureBTDTO, List<String> recoverdQuittancesIDs, FactureBT lastRecup) {
//get quantities in quittannces if not recovered
//get quantities in restRecup
// merge both lists ( sum qte)
//search avoir after last transfert  

// calculate the new rest
// delete the old rest and replace it by the new rest
        List<Facture> quittances = factureService.findByNumbonIn(factureBTDTO.getRelativesBons());//get quittances
        List<ResteRecuperation> restRecup = resterecuperationRepository.findByPk_CodeDepotAndCategorieDepot(factureBTDTO.getDestinationID(), factureBTDTO.getCategDepot());//get resteRecup
        Set<Integer> listeCodesArticles = restRecup.stream().map(reste -> reste.getPk().getCodeArticle()).collect(Collectors.toSet());
        List<Mvtsto> listeMvtstos = quittances.stream().flatMap(facture -> facture.getMvtstoCollection().stream()).collect(Collectors.toList());
        log.debug("lastRecup est {}", lastRecup);

        List<FactureAV> listeAvoirs = avoirService.findAllAvoirs(Arrays.asList(factureBTDTO.getCategDepot()), lastRecup.getDatbon(), LocalDateTime.now(), factureBTDTO.getDestinationID());
        log.debug("avoirs sont  size {},listeAvoirs  {}",listeAvoirs.size(),listeAvoirs);
        List<MvtstoMvtstoAV> listeAvoirsRecup = new ArrayList();
          List<String>matchingNumbonAvoirsRecup= new ArrayList();
        if (!listeAvoirs.isEmpty()) {
            
            List<String> numBonAvoirs = listeAvoirs.stream()
                     .peek(elt->log.debug("elt.elt.getMvtstoCollection{}",elt.getMvtStoAVCollection()))
//                    .flatMap(avoir -> avoir.getMvtStoAVCollection().stream())
                    .map(x -> x.getNumbon())
//                            .peek(elt->log.debug(elt.getNumbon())))
                    .collect(Collectors.toList());

//            List<String> numBonMVTSTOav = listemvtstoAVApresResteRecup.stream().map(x -> x.getNumbon()).collect(Collectors.toList());
            List<MvtstoMvtstoAV> listeMvtstoMVtstoAV = mvtstomvtstoAVRepository.findByNumBonMvtstoAVIn(numBonAvoirs);
            List<String> numbonQuittances = listeMvtstoMVtstoAV.stream().map(elt -> elt.getNumBonMvtsto()).collect(Collectors.toList());

            //  quittance deja recuperé avant 
            
         List<Btfe> listeQuittanceAvoirRecup = new ArrayList<>();
        if (numbonQuittances.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) numbonQuittances.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {
                List<String> codesChunk = numbonQuittances.subList(i * 2000, Math.min(i * 2000 + 2000, numbonQuittances.size()));
                completableFuture[i] = btfeRepository.findByNumFEIn(codesChunk).whenComplete((btfes, exception) -> {
              
                    listeQuittanceAvoirRecup.addAll(btfes);
                });
            }
            CompletableFuture.allOf(completableFuture).join();
        }
            
          
            List<String> listeNumbonsQuittancesTraites = listeQuittanceAvoirRecup.stream().map(elt -> elt.getNumFE()).collect(Collectors.toList());
            //extraire les mvtstoav (avoirs )fait sulisteAvoirsQuittancesRecuperesr des quittances deja recuperes ( cad exitent dans la table btfe)
            List<Mvtsto> listeAvoirsQuittancesRecuperes = new ArrayList<>();

             listeAvoirsRecup = listeMvtstoMVtstoAV.stream().filter(elt -> listeNumbonsQuittancesTraites.contains(elt.getNumBonMvtsto())).collect(Collectors.toList());
// extraire code article from listeAvoirsRecup and add all liset listeCodesArticles
            List<Integer> codeAriclesInlisteAvoirsRecup = listeAvoirsRecup.stream().map(x -> x.getCodart()).collect(Collectors.toList());
            listeCodesArticles.addAll(codeAriclesInlisteAvoirsRecup);
        
        //extraire numbon from listeAvoirsRecup and save in table BTAV
            matchingNumbonAvoirsRecup=listeAvoirsRecup.stream().map(item->item.getNumBonMvtstoAV()).collect(Collectors.toList());
        }

        Set<Integer> listeCodesFromQuittances = listeMvtstos.stream().map(mvtsto -> mvtsto.getMvtstoPK().getCodart()).collect(Collectors.toSet());
        listeCodesArticles.addAll(listeCodesFromQuittances);    //get articles ids 
        List<ArticlePHDTO> listeArticlePHDTOs = new ArrayList();
        List<ArticleDTO> articles = new ArrayList();
        if (factureBTDTO.getCategDepot().equals(CategorieDepotEnum.PH)) {
            listeArticlePHDTOs = paramAchatServiceClient.articlePHFindbyListCode(listeCodesArticles);
        } else {
            articles = paramAchatServiceClient.articleFindbyListCode(listeCodesArticles);
        }

        List<ArticleUniteDTO> articlePHunits = (factureBTDTO.getCategDepot().equals(CategorieDepotEnum.PH)) ? listeArticlePHDTOs.stream()
                .flatMap(articlePH -> articlePH.getArticleUnites()
                .stream()).collect(toList()) : new ArrayList();
        //merge both lists ( sum qte)
        resterecuperationRepository.delete(restRecup);
        List<ResteRecuperation> newResteRecupList = new ArrayList();

        for (Integer codeArticle : listeCodesArticles) {
            log.debug("*********begin process remaining quantities du codart {}**********", codeArticle);
            BigDecimal mvtstosQuantityWithoutRound = listeMvtstos
                    .stream()
                    .filter(x -> x.getMvtstoPK().getCodart().equals(codeArticle) && (!recoverdQuittancesIDs.contains(x.getMvtstoPK().getNumbon())))
                    .collect(Collectors.reducing(BigDecimal.ZERO, mvtsto -> {

                        if (factureBTDTO.getCategDepot().equals(CategorieDepotEnum.PH)) {

                            BigDecimal nbrePiece = articlePHunits.stream()
                                    .filter(artUnite -> artUnite.getCodeUnite().equals(mvtsto.getUnite()) && artUnite.getCodeArticle().equals(mvtsto.getMvtstoPK().getCodart()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(mvtsto.getCodeSaisi())))
                                    .getNbPiece();
                            return mvtsto.getQteben().divide(nbrePiece, 3, RoundingMode.CEILING);
                        } else {
                            return mvtsto.getQteben();
                        }
                    }, (a, b) -> a.add(b)));//quantites de quittance without Rounding expl==>2,3 boite
//            BigDecimal mvtstosQuantityWithoutRound = mvtstosQuantityWithoutRound.setScale(0, RoundingMode.UP);//on ne peut pas recuperer 2,3 boite==>3boites

            log.debug("mvtstosQuantity  du codart {} est {}", codeArticle, mvtstosQuantityWithoutRound);

            //liste avoir 
            BigDecimal avoirQuantityWithoutRound = listeAvoirsRecup
                    .stream()
                    .filter(x -> x.getCodart().equals(codeArticle) && (!recoverdQuittancesIDs.contains(x.getNumBonMvtsto())))
                    .collect(Collectors.reducing(BigDecimal.ZERO, mvtsto -> {

                        if (factureBTDTO.getCategDepot().equals(CategorieDepotEnum.PH)) {

                            BigDecimal nbrePiece = articlePHunits.stream()
                                    .filter(artUnite -> artUnite.getCodeUnite().equals(mvtsto.getMvtsto().getUnite()) && artUnite.getCodeArticle().equals(mvtsto.getCodart()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(mvtsto.getMvtsto().getCodeSaisi())))
                                    .getNbPiece();
                            return mvtsto.getQte().divide(nbrePiece, 3, RoundingMode.CEILING);
                        } else {
                            return mvtsto.getQte();
                        }
                    }, (a, b) -> a.add(b)));//quantites de quittance without Rounding expl==>2,3 boite

            BigDecimal oldRecupQuantity = restRecup.stream().filter(x -> x.getPk().getCodeArticle().equals(codeArticle)).map(ResteRecuperation::getQuantite)
                    .collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));
            log.debug("oldRecupQuantity du codart {} est {}", codeArticle, oldRecupQuantity);
            BigDecimal newResteRecupQuantity = mvtstosQuantityWithoutRound.add(oldRecupQuantity);
//            log.debug("newResteRecupQuantity du codart {} est {}", codeArticle, newResteRecupQuantity);

            List<MvtstoBTDTO> recupered = factureBTDTO.getDetails()
                    .stream()
                    .filter(elt -> elt.getArticleID().equals(codeArticle)).collect(Collectors.toList());
//            log.debug("recupered sont {}", recupered);
            if (!recupered.isEmpty()) {
                BigDecimal transferedQuantity = recupered.stream().map(MvtstoBTDTO::getQuantity)
                        .collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));
                log.debug("transferedQuantity du codart {} est {}", codeArticle, transferedQuantity);
                if (!clinicName.equalsIgnoreCase("hikma") && transferedQuantity.compareTo(newResteRecupQuantity) > 0 && (newResteRecupQuantity.compareTo(BigDecimal.ZERO) > 0)) {
                    log.debug("new resteRecup {} et {}", String.valueOf(newResteRecupQuantity.intValue()), newResteRecupQuantity);
                    Preconditions.checkBusinessLogique(transferedQuantity.compareTo(newResteRecupQuantity) < 0, "transfer-recup.insuffusant_quantity", String.valueOf(newResteRecupQuantity.intValue()), recupered.get(0).getDesignation());
                }

                log.debug("***************************newResteRecupQuantity {}, test newResteRecupQuantity {},{}", newResteRecupQuantity, newResteRecupQuantity.compareTo(BigDecimal.ZERO), newResteRecupQuantity.compareTo(BigDecimal.ZERO) != 0);
               // Preconditions.checkBusinessLogique(newResteRecupQuantity.compareTo(BigDecimal.ZERO) != 0, "transfer-recup.already-recoverd-quittances", recupered.get(0).getDesignation());
                newResteRecupQuantity = newResteRecupQuantity.subtract(transferedQuantity);
            }
            log.debug(" ***************************newResteRecupQuantity du codart apres transfert {} est {}", codeArticle, newResteRecupQuantity);
            BigDecimal newResteRecupQuantityAfterAvoir = newResteRecupQuantity.subtract(avoirQuantityWithoutRound);
            log.debug("***************************newResteRecupQuantityAfterAvoir du codart apres transfert {} est {}", codeArticle, newResteRecupQuantityAfterAvoir);
            ArticleDTO matchingArticle;
            if (factureBTDTO.getCategDepot().equals(CategorieDepotEnum.PH)) {
                matchingArticle = listeArticlePHDTOs.stream().filter(elt -> codeArticle.equals(elt.getCode()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(codeArticle.toString())));
            } else {
                matchingArticle = articles.stream().filter(elt -> codeArticle.equals(elt.getCode()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(codeArticle.toString())));
            }
            ResteRecuperationPK pk = new ResteRecuperationPK(codeArticle, factureBTDTO.getDestinationID());
            ResteRecuperation newResteRecup = new ResteRecuperation(pk, matchingArticle.getCodeUnite(), newResteRecupQuantityAfterAvoir);
            newResteRecup.setCategorieDepot(factureBTDTO.getCategDepot());
//            if (newResteRecup.getQuantite().compareTo(BigDecimal.ZERO) != 0) {
                newResteRecupList.add(newResteRecup);
//            }
        }

//        resterecuperationRepository.delete(restRecup);
//       save(factureBTDTO.getRemainToRecover(), factureBTDTO.getCategDepot());
        List<ResteRecuperation> result = resterecuperationRepository.save(newResteRecupList);
        return matchingNumbonAvoirsRecup;
    }

//    /**
//     * Update a resterecuperationDTO.
//     *
//     * @param resterecuperationDTO
//     * @return the updated entity
//     */
//    public ResteRecuperationDTO update(ResteRecuperationDTO resterecuperationDTO) {
//        log.debug("Request to update ResteRecuperation: {}", resterecuperationDTO);
//        ResteRecuperation inBase = resterecuperationRepository.findOne(resterecuperationDTO.getResteRecuperationPK());
//        Preconditions.checkArgument(inBase != null, "resterecuperation.NotFound");
//        ResteRecuperation resterecuperation = ResteRecuperationFactory.resterecuperationDTOToResteRecuperation(resterecuperationDTO);
//        resterecuperation = resterecuperationRepository.save(resterecuperation);
//        ResteRecuperationDTO resultDTO = ResteRecuperationFactory.resterecuperationToResteRecuperationDTO(resterecuperation);
//        return resultDTO;
//    }
    /**
     * Get one resterecuperationDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public ResteRecuperationDTO findOne(ResteRecuperationPK id) {
        log.debug("Request to get ResteRecuperation: {}", id);
        ResteRecuperation resterecuperation = resterecuperationRepository.findOne(id);
        ResteRecuperationDTO dto = ResteRecuperationFactory.resterecuperationToResteRecuperationDTO(resterecuperation);
        return dto;
    }

    @Transactional(readOnly = true)
    public Collection<ResteRecuperationDTO> findByCodeDep(Integer depotID, CategorieDepotEnum categDepot, Boolean onlyMyArticles) {
        log.debug("Request to get ResteRecuperation: {}", depotID);
        List<ResteRecuperation> resteRecuperations = resterecuperationRepository.findByPk_CodeDepotAndCategorieDepot(depotID, categDepot);

        List<Integer> articlesID = resteRecuperations.stream().map(item -> item.getPk().getCodeArticle()).collect(toList());
        List<ArticleDTO> articleDTOs = new ArrayList();
        List<Integer> onlyMyArticlesID = new ArrayList();
        if (onlyMyArticles != null && Boolean.TRUE.equals(onlyMyArticles)) {
            articleDTOs = paramAchatServiceClient.articleFindbyListCode(articlesID, onlyMyArticles);
            onlyMyArticlesID = articleDTOs.stream().map(art -> art.getCode()).collect(Collectors.toList());
        }

        Collection<ResteRecuperationDTO> result = new ArrayList();
        log.debug("id reste recup : {}", onlyMyArticlesID);
        if (onlyMyArticles != null && Boolean.TRUE.equals(onlyMyArticles) && !onlyMyArticlesID.isEmpty() || !Boolean.TRUE.equals(onlyMyArticles)) {
            List<DepstoDTO> depstos = stockService.findQuantiteOfArticles(articlesID, depotID, false, true, null, null);
            List<ArticleDTO> articles = (onlyMyArticles != null && Boolean.TRUE.equals(onlyMyArticles))
                    ? articleDTOs : paramAchatServiceClient.articleFindbyListCode(articlesID);
            result = resteRecuperations.stream()
                    .filter(item
                            -> onlyMyArticles != null && Boolean.TRUE.equals(onlyMyArticles)
                    && articles.stream().map(art -> art.getCode()).collect(Collectors.toList()).contains(item.getPk().getCodeArticle())
                    || !Boolean.TRUE.equals(onlyMyArticles))
                    .map(item -> {
                        ResteRecuperationDTO restRecupDTO = ResteRecuperationFactory.resterecuperationToResteRecuperationDTO(item);
                        ArticleDTO matchedArticle = articles.stream().filter(art -> art.getCode().equals(restRecupDTO.getCodart())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));
                        restRecupDTO.setDesart(matchedArticle.getDesignation());
                        restRecupDTO.setDesArtSec(matchedArticle.getDesignationSec());
                        restRecupDTO.setPerissable(matchedArticle.getPerissable());
                        restRecupDTO.setPriach(matchedArticle.getPrixAchat());
                        restRecupDTO.setUnityDesignation(matchedArticle.getDesignationUnite());
                        restRecupDTO.setCodeSaisi(matchedArticle.getCodeSaisi());
                        restRecupDTO.setCategDepot(matchedArticle.getCategorieDepot());
                        BigDecimal qteInStore = depstos.stream()
                                .filter(depsto -> depsto.getArticleID().equals(restRecupDTO.getCodart()) && depsto.getUnityCode().equals(matchedArticle.getCodeUnite()))
                                .findFirst()
                                .orElse(new DepstoDTO(BigDecimal.ZERO))
                                .getQuantity();
                        restRecupDTO.setQuantityInStore(qteInStore);
                        return restRecupDTO;
                    })
                    .collect(Collectors.groupingBy(item -> item.getCodart(),
                            Collectors.reducing(new ResteRecuperationDTO(BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
                                a.setCodart(b.getCodart());
                                a.setDesart(b.getDesart());
                                a.setDesArtSec(b.getDesArtSec());
                                a.setUnityCode(b.getUnityCode());
                                a.setUnityDesignation(b.getUnityDesignation());
                                a.setQuantite(b.getQuantite().add(a.getQuantite()));
                                a.setQuantityInStore(b.getQuantityInStore().add(a.getQuantityInStore()));
                                a.setPriach(b.getPriach());
                                a.setPerissable(b.getPerissable());
                                a.setCodeSaisi(b.getCodeSaisi());
                                a.setCategDepot(b.getCategDepot());
                                return b;
                            })))
                    .values();
        }

        return result;

    }

    @Transactional(readOnly = true)
    public List<ResteRecuperation> findByCodeDepotAndCategorieDepot(Integer depotID, CategorieDepotEnum categDepot) {
        log.debug("Request to get ResteRecuperation: {}", depotID);
        return resterecuperationRepository.findByPk_CodeDepotAndCategorieDepot(depotID, categDepot);
    }

    public void delete(List<ResteRecuperation> toDeleteRestRecup) {
        resterecuperationRepository.delete(toDeleteRestRecup);
    }

//  /**
//   * Get one resterecuperation by id.
//   *
//   * @param id the id of the entity
//   * @return the entity
//   */
//  @Transactional(
//      readOnly = true
//  )
//  public ResteRecuperation findResteRecuperation(ResteRecuperationPK id) {
//    log.debug("Request to get ResteRecuperation: {}",id);
//    ResteRecuperation resterecuperation= resterecuperationRepository.findOne(id);
//    return resterecuperation;
//  }
//
//  /**
//   * Get all the resterecuperations.
//   *
//   * @return the the list of entities
//   */
//  @Transactional(
//      readOnly = true
//  )
//  public Collection<ResteRecuperationDTO> findAll() {
//    log.debug("Request to get All ResteRecuperations");
//    Collection<ResteRecuperation> result= resterecuperationRepository.findAll();
//    return ResteRecuperationFactory.resterecuperationToResteRecuperationDTOs(result);
//  }
//
//  /**
//   * Delete resterecuperation by id.
//   *
//   * @param id the id of the entity
//   */
//  public void delete(ResteRecuperationPK id) {
//    log.debug("Request to delete ResteRecuperation: {}",id);
//    resterecuperationRepository.delete(id);
//  }
    public void reinitialiserResteRecup(CategorieDepotEnum categ, Integer codeDepot) {

//        Inventaire inventaire = inventaireRepository.findOne(codeInventaire);
        // vider table reste recup 
        List<ResteRecuperation> toDeleteRestRecup = findByCodeDepotAndCategorieDepot(codeDepot, categ);
        resterecuperationRepository.delete(toDeleteRestRecup);
        //insertion dans BTFe des quittances non satisfaits 

        List<Facture> listQuittancesNotRecovered = factureService.findBySatisfaction(categ, null, null, Boolean.FALSE, SatisfactionFactureEnum.NOT_RECOVRED, codeDepot);
        log.debug("listQuittancesNotRecovered size est {}", listQuittancesNotRecovered.size());
        if (!listQuittancesNotRecovered.isEmpty()) {
            FactureBT transfertBTfe = new FactureBT();

            transfertBTfe.setCategDepot(categ);
            transfertBTfe.setCoddep(codeDepot);
            transfertBTfe.setDeptr(codeDepot);
            transfertBTfe.setAutomatique(Boolean.FALSE);
            transfertBTfe.setValide(Boolean.TRUE);
            transfertBTfe.setConforme(Boolean.TRUE);
            transfertBTfe.setSatisf(SatisfactionEnum.RECOVRED);
            String numbon = paramService.getcompteur(categ, TypeBonEnum.BT);
//         transfertBTfe.setDetails(new ArrayList());
//         transfertBTfe.setRelativesBons(new ArrayList());
            transfertBTfe.setNumbon(numbon);

            bonTransfertInterDepotService.save(transfertBTfe);
            paramService.updateCompteurPharmacie(categ, TypeBonEnum.BT);
            log.debug("transfertBTfeDTO est {}", transfertBTfe.getNumaffiche());
//         FactureBT transfert=FactureBTFactory.factureBTDTOToFactureBT(transfertBTfeDTO);
            log.debug("transfert est {}", transfertBTfe.getNumbon());
            List<Btfe> ListBtfeToSave = listQuittancesNotRecovered.stream().map(quittance -> {
                Btfe btfe = new Btfe();
                btfe.setNumFE(quittance.getNumbon());
                btfe.setFactureBT(transfertBTfe);
                return btfe;
            })
                    .collect(Collectors.toList());

            btfeRepository.save(ListBtfeToSave);
        }
    }
    
    @Transactional
    public void processRemainingQuantitiesOnRecoveringWithoutAvoirs(BonTransfertRecupDTO factureBTDTO, List<String> recoverdQuittancesIDs) {
 // traitement only for Egypte (on ne prend pas en consideration les avoirs aprés dernier transfert recup )
//get quantities in quittannces if not recovered
//get quantities in restRecup
// merge both lists ( sum qte)
// calculate the new rest
// delete the old rest and replace it by the new rest
        List<Facture> quittances = factureService.findByNumbonIn(factureBTDTO.getRelativesBons());//get quittances
        List<ResteRecuperation> restRecup = resterecuperationRepository.findByPk_CodeDepotAndCategorieDepot(factureBTDTO.getDestinationID(), factureBTDTO.getCategDepot());//get resteRecup
        Set<Integer> listeCodesArticles = restRecup.stream().map(reste -> reste.getPk().getCodeArticle()).collect(Collectors.toSet());
        List<Mvtsto> listeMvtstos = quittances.stream().flatMap(facture -> facture.getMvtstoCollection().stream()).collect(Collectors.toList());
        Set<Integer> listeCodesFromQuittances = listeMvtstos.stream().map(mvtsto -> mvtsto.getMvtstoPK().getCodart()).collect(Collectors.toSet());
        listeCodesArticles.addAll(listeCodesFromQuittances);    //get articles ids 
        List<ArticlePHDTO> listeArticlePHDTOs = new ArrayList();
        List<ArticleDTO> articles = new ArrayList();
        if (factureBTDTO.getCategDepot().equals(CategorieDepotEnum.PH)) {
            listeArticlePHDTOs = paramAchatServiceClient.articlePHFindbyListCode(listeCodesArticles);
        } else {
            articles = paramAchatServiceClient.articleFindbyListCode(listeCodesArticles);
        }

        List<ArticleUniteDTO> articlePHunits = (factureBTDTO.getCategDepot().equals(CategorieDepotEnum.PH)) ? listeArticlePHDTOs.stream()
                .flatMap(articlePH -> articlePH.getArticleUnites()
                .stream()).collect(toList()) : new ArrayList();
        //merge both lists ( sum qte)
        resterecuperationRepository.delete(restRecup);
        List<ResteRecuperation> newResteRecupList = new ArrayList();

        for (Integer codeArticle : listeCodesArticles) {
            log.debug("*********begin process remaining quantities du codart {}**********", codeArticle);
            BigDecimal mvtstosQuantityWithoutRound = listeMvtstos
                    .stream()
                    .filter(x -> x.getMvtstoPK().getCodart().equals(codeArticle) && (!recoverdQuittancesIDs.contains(x.getMvtstoPK().getNumbon())))
                    .collect(Collectors.reducing(BigDecimal.ZERO, mvtsto -> {

                        if (factureBTDTO.getCategDepot().equals(CategorieDepotEnum.PH)) {

                            BigDecimal nbrePiece = articlePHunits.stream()
                                    .filter(artUnite -> artUnite.getCodeUnite().equals(mvtsto.getUnite()) && artUnite.getCodeArticle().equals(mvtsto.getMvtstoPK().getCodart()))
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(mvtsto.getCodeSaisi())))
                                    .getNbPiece();
                            return mvtsto.getQteben().divide(nbrePiece, 3, RoundingMode.CEILING);
                        } else {
                            return mvtsto.getQteben();
                        }
                    }, (a, b) -> a.add(b)));//quantites de quittance without Rounding expl==>2,3 boite
//            BigDecimal mvtstosQuantityWithoutRound = mvtstosQuantityWithoutRound.setScale(0, RoundingMode.UP);//on ne peut pas recuperer 2,3 boite==>3boites

            log.debug("mvtstosQuantity  du codart {} est {}", codeArticle, mvtstosQuantityWithoutRound);

            BigDecimal oldRecupQuantity = restRecup.stream().filter(x -> x.getPk().getCodeArticle().equals(codeArticle)).map(ResteRecuperation::getQuantite)
                    .collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));
            log.debug("oldRecupQuantity du codart {} est {}", codeArticle, oldRecupQuantity);
            BigDecimal newResteRecupQuantity = mvtstosQuantityWithoutRound.add(oldRecupQuantity);
//            log.debug("newResteRecupQuantity du codart {} est {}", codeArticle, newResteRecupQuantity);

            List<MvtstoBTDTO> recupered = factureBTDTO.getDetails()
                    .stream()
                    .filter(elt -> elt.getArticleID().equals(codeArticle)).collect(Collectors.toList());
//            log.debug("recupered sont {}", recupered);
            if (!recupered.isEmpty()) {
                BigDecimal transferedQuantity = recupered.stream().map(MvtstoBTDTO::getQuantity)
                        .collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));
                log.debug("transferedQuantity du codart {} est {}", codeArticle, transferedQuantity);
                if (transferedQuantity.compareTo(newResteRecupQuantity) > 0 && (newResteRecupQuantity.compareTo(BigDecimal.ZERO) > 0)) {
                    log.debug("new resteRecup {} et {}", String.valueOf(newResteRecupQuantity.intValue()), newResteRecupQuantity);
                    Preconditions.checkBusinessLogique(transferedQuantity.compareTo(newResteRecupQuantity) < 0, "transfer-recup.insuffusant_quantity", String.valueOf(newResteRecupQuantity.intValue()), recupered.get(0).getDesignation());
                }


               log.debug("newResteRecupQuantity {}, test newResteRecupQuantity {},{}",newResteRecupQuantity, newResteRecupQuantity.compareTo(BigDecimal.ZERO), newResteRecupQuantity.compareTo(BigDecimal.ZERO) != 0);
                Preconditions.checkBusinessLogique(newResteRecupQuantity.compareTo(BigDecimal.ZERO) != 0, "transfer-recup.already-recoverd-quittances", recupered.get(0).getDesignation());
                newResteRecupQuantity = newResteRecupQuantity.subtract(transferedQuantity);
            }
            log.debug("newResteRecupQuantity du codart apres transfert {} est {}", codeArticle, newResteRecupQuantity);
            ArticleDTO matchingArticle;
            if (factureBTDTO.getCategDepot().equals(CategorieDepotEnum.PH)) {
                matchingArticle = listeArticlePHDTOs.stream().filter(elt -> codeArticle.equals(elt.getCode()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(codeArticle.toString())));
            } else {
                matchingArticle = articles.stream().filter(elt -> codeArticle.equals(elt.getCode()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(codeArticle.toString())));
            }
            ResteRecuperationPK pk = new ResteRecuperationPK(codeArticle, factureBTDTO.getDestinationID());
            ResteRecuperation newResteRecup = new ResteRecuperation(pk, matchingArticle.getCodeUnite(), newResteRecupQuantity);
            newResteRecup.setCategorieDepot(factureBTDTO.getCategDepot());
            if (newResteRecup.getQuantite().compareTo(BigDecimal.ZERO) > 0) {
                newResteRecupList.add(newResteRecup);
            }
        }

//        resterecuperationRepository.delete(restRecup);
//       save(factureBTDTO.getRemainToRecover(), factureBTDTO.getCategDepot());
        List<ResteRecuperation> result = resterecuperationRepository.save(newResteRecupList);
    }
}
