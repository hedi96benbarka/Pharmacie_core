/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.DetailMvtSto;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.transfert.domain.FactureBT;

import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.transfert.domain.Btav;

import com.csys.pharmacie.transfert.domain.Btfe;
import com.csys.pharmacie.transfert.domain.DetailMvtStoBT;
import com.csys.pharmacie.transfert.domain.MvtStoBT;
import com.csys.pharmacie.transfert.domain.QFactureBT;
import com.csys.pharmacie.transfert.dto.BonTransfertRecupDTO;
import com.csys.pharmacie.transfert.dto.MvtstoBTDTO;
import com.csys.pharmacie.transfert.factory.BonTransfertRecupFactory;
import com.csys.pharmacie.transfert.factory.FactureBTFactory;
import com.csys.pharmacie.transfert.factory.MvtstoBTFactory;
import com.csys.pharmacie.transfert.repository.BonTransferRecupRepository;
import com.csys.pharmacie.transfert.repository.BtfeRepository;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import com.csys.pharmacie.vente.quittance.dto.FactureDTO;
import com.csys.pharmacie.vente.quittance.service.FactureService;
import com.csys.util.Preconditions;
import static com.csys.util.Preconditions.checkBusinessLogique;
import edu.emory.mathcs.backport.java.util.Arrays;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
/**
 *
 * @author Farouk
 */
@Service("BonTransfertRecupService")
public class BonTransfertRecupService {

    private final Logger log = LoggerFactory.getLogger(BonTransfertRecupService.class);
    private static String validationTransfertAuto;
     private static String clinicName;
    private   static long BLOCKING_DELAY_BETWEEN_TRANSFERT_RECUP;

    @Value("${validation-transfert-auto}")
    public void setValidation(String validation) {
        validationTransfertAuto = validation;
    }
        @Value("${clinic-name}")
    public void setClinicName(String clinic) {
        clinicName = clinic;
    }
        
    @Value("${blocking-delay-between-transfert-recup}")
    public void setBlockingDelayBetweenQuittances(long db) {
        BLOCKING_DELAY_BETWEEN_TRANSFERT_RECUP = db;
    }
    @Autowired
    private BonTransferRecupRepository bonTransferRecupRepository;

    @Autowired
    private ParamService paramService;

    @Autowired
    private InventaireService inventaireService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ResteRecuperationService resteRecuperationService;
    @Autowired
    private FactureService factureService;

    @Autowired
    private ParamAchatServiceClient paramAchatServiceClient;

    @Autowired
    private BtfeRepository btfeRepository;

    @Transactional
    public BonTransfertRecupDTO addBonTransferRecup(BonTransfertRecupDTO bonTransfertRecupDTO) throws NoSuchAlgorithmException {
        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(bonTransfertRecupDTO.getDestinationID());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + depotd.getDesignation() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getCode() + "] est un dépot fournisseur");

        DepotDTO depots = paramAchatServiceClient.findDepotByCode(bonTransfertRecupDTO.getSourceID());
        Preconditions.checkBusinessLogique(!depots.getDesignation().equals("depot.deleted"), "Depot [" + depots.getDesignation() + "] introuvable");
        Preconditions.checkBusinessLogique(!depots.getDepotFrs(), "Dépot [" + depots.getCode() + "] est un dépot fournisseur");

        Boolean existeTransfertNonValide = bonTransferRecupRepository.existsByCoddepAndDeptrAndValideFalseAndCodAnnulIsNull(bonTransfertRecupDTO.getSourceID(), bonTransfertRecupDTO.getDestinationID());
        Preconditions.checkBusinessLogique(!existeTransfertNonValide, "existe.transfert.non.validé");

        FactureBT bonTransferRecup = FactureBTFactory.factureBTDTOToFactureBT(bonTransfertRecupDTO);
        String numbon = paramService.getcompteur(bonTransfertRecupDTO.getCategDepot(), TypeBonEnum.BT);
        bonTransferRecup.setNumbon(numbon);
        bonTransferRecup.setValide(false);
//        List<String> recoverdQuittancesIDs = btfeRepository.findByNumFEIn(dto.getRelativesBons()).stream().map(Btfe::getNumFE).collect(toList());
        List<String> queriedQuittancesIDs = bonTransfertRecupDTO.getRelativesBons();
        List<Btfe> btFes = new ArrayList<>();
        

        //  quittance deja recuperé avant 
        Integer numberOfChunks = (int) Math.ceil((double) queriedQuittancesIDs.size() / 2000);
        CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
        for (int i = 0; i < numberOfChunks; i++) {
            List<String> codesChunk = queriedQuittancesIDs.subList(i * 2000, Math.min(i * 2000 + 2000, queriedQuittancesIDs.size()));
            completableFuture[i] = btfeRepository.findByNumFEIn(codesChunk).whenComplete((btfes, exception) -> {
                btFes.addAll(btfes);
            });
        }
        CompletableFuture.allOf(completableFuture).join();
        List<String> recoverdQuittancesIDs = btFes.stream().map(Btfe::getNumFE).collect(toList());
        if(clinicName.equalsIgnoreCase("hikma")){
        Preconditions.checkBusinessLogique(recoverdQuittancesIDs.isEmpty(), "transfer-recup.already-recoverd-quittances", recoverdQuittancesIDs.toString());}


        // check for inventory
        Integer[] articlesIDs = bonTransfertRecupDTO.getDetails().stream().map(MvtstoBTDTO::getArticleID).toArray(Integer[]::new);
        List<ArticleDTO> articles = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(bonTransfertRecupDTO.getCategDepot(), articlesIDs);
        List<Integer> categArticleIDs = articles.stream().map(item -> item.getCategorieArticle().getCode()).collect(toList());
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(categArticleIDs, bonTransfertRecupDTO.getDestinationID());
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = articles.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }

        List<Depsto> depstos = stockService.findByCodartInAndCoddepAndQteGreaterThan(Arrays.asList(articlesIDs), bonTransferRecup.getCoddep(), BigDecimal.ZERO);
//        log.debug("liste of depstos to be processed  {}", depstos);
        List<Depsto> newDepstos = new ArrayList(); //the new stock to be created as a result of this transfert.
        List<MvtStoBT> detailsFactureBT = new ArrayList();
        String numordre1 = "0001";
        BigDecimal totaleMnt = BigDecimal.ZERO;
        for (MvtstoBTDTO mvtstoBTDTO : bonTransfertRecupDTO.getDetails()) {

            MvtStoBT detailFacture = MvtstoBTFactory.mvtstoBTDTOToMvtstoBT(mvtstoBTDTO);
            detailFacture.setFactureBT(bonTransferRecup);

            detailFacture.setCodart(mvtstoBTDTO.getArticleID());
            detailFacture.setNumbon(numbon);
            detailFacture.setNumordre(numordre1);

            numordre1 = Helper.IncrementString(numordre1, 4);
            detailsFactureBT.add(detailFacture);
            List<DetailMvtSto> detailMvtSto = stockService.GestionDetailFacture(depstos, detailFacture, DetailMvtStoBT.class, false);

            List<DetailMvtStoBT> detailMvtStoBTs = new ArrayList<>();
            detailMvtSto.forEach(item -> {
                DetailMvtStoBT detailMvtStoBT = (DetailMvtStoBT) item;
                detailMvtStoBT.setMvtStoBT(detailFacture);
                detailMvtStoBTs.add(detailMvtStoBT);

                Depsto newDepsto = new Depsto(detailMvtStoBT);
                newDepsto.setCoddep(bonTransfertRecupDTO.getDestinationID());
                newDepsto.setCodart(detailFacture.getCodart());
                newDepsto.setCategDepot(detailFacture.getCategDepot());
                newDepsto.setCategDepot(detailFacture.getCategDepot());
                newDepsto.setNumBon(detailFacture.getNumbon());
                newDepsto.setNumBonOrigin(detailMvtStoBT.getNumBonOrigin());
                Optional<Depsto> matchedDepsto = depstos.stream().filter(dep -> dep.getCode().equals(detailMvtStoBT.getCodeDepsto())).findFirst();
                if (matchedDepsto.isPresent()) {
                    newDepsto.setMemo(matchedDepsto.get().getMemo());
                }
                newDepstos.add(newDepsto);

            });
            detailFacture.setDetailMvtStoBTList(detailMvtStoBTs);
            totaleMnt = totaleMnt.add(detailFacture.getPriuni().multiply(detailFacture.getQuantite()));
            log.debug("*** End of treating article {} ****", detailFacture);
        }
        List<Btfe> listeRelatives = bonTransfertRecupDTO.getRelativesBons().stream().map(item -> {
            return new Btfe(bonTransferRecup, item);
        }).collect(toList());
        bonTransferRecup.setBTFE(listeRelatives);

        bonTransferRecup.setDetailFactureBTCollection(detailsFactureBT);
        bonTransferRecup.setMntbon(totaleMnt);
        String codeHashage = bonTransferRecup.getMntbon().toString().concat(bonTransferRecup.getCoddep().toString());
        for(MvtStoBT mvtstoBt : bonTransferRecup.getDetailFactureBTCollection()){
             codeHashage = codeHashage.concat(mvtstoBt.getCodart().toString().concat(mvtstoBt.getQuantite().toString()));
        }
           byte[] hashCode = Helper.hashing(codeHashage);
           bonTransferRecup.setHashCode(hashCode);
        List<FactureBT> listFactureBTByHashCodeAndDatbonGreaterThan = bonTransferRecupRepository.findByHashCodeAndDatbonGreaterThan(hashCode, LocalDateTime.now().minusMinutes(BLOCKING_DELAY_BETWEEN_TRANSFERT_RECUP));
        Preconditions.checkBusinessLogique(listFactureBTByHashCodeAndDatbonGreaterThan.isEmpty(), "blockage.trasnfert.double");
        if (validationTransfertAuto.equals("true")) {
            bonTransferRecup.setConforme(Boolean.TRUE);
            bonTransferRecup.setValide(Boolean.TRUE);
            bonTransferRecup.setUserValidate(SecurityContextHolder.getContext().getAuthentication().getName());
            bonTransferRecup.setDateValidate(LocalDateTime.now());
        }
              if(clinicName.equalsIgnoreCase("hikma")){
        FactureBT lasteRecup=findLastTransfertRecupByCategorieDepotAndCodeDep(bonTransferRecup.getCategDepot(),bonTransfertRecupDTO.getDestinationID());
        log.debug("dernier recup {}",lasteRecup);
        List<String>matchingNumbonAvoirsRecup=resteRecuperationService.processRemainingQuantitiesOnRecovering(bonTransfertRecupDTO,recoverdQuittancesIDs,lasteRecup);
        if(!matchingNumbonAvoirsRecup.isEmpty()){
            log.debug("matching Numbon AvoirsRecup {}",matchingNumbonAvoirsRecup);
        List<Btav> listeAvoirs = matchingNumbonAvoirsRecup.stream().map(item -> {
            return new Btav(bonTransferRecup, item);
        }).collect(toList());
        bonTransferRecup.setBTAV(listeAvoirs);}}
        else{
        resteRecuperationService.processRemainingQuantitiesOnRecoveringWithoutAvoirs(bonTransfertRecupDTO,recoverdQuittancesIDs);} 
        stockService.saveDepsto(newDepstos);
        bonTransferRecupRepository.save(bonTransferRecup);
        paramService.updateCompteurPharmacie(bonTransferRecup.getCategDepot(), TypeBonEnum.BT);
        return BonTransfertRecupFactory.factureBTToFactureBTDTO(bonTransferRecup);
    }

    @Transactional(readOnly = true)
    public BonTransfertRecupDTO findOne(String numBon) {
        log.debug("Request to get Facture: {}", numBon);
        FactureBT facture = bonTransferRecupRepository.findOne(numBon);
        BonTransfertRecupDTO dto = BonTransfertRecupFactory.factureBTToFactureBTDTO(facture);
        dto.setDetails(MvtstoBTFactory.mvtstoBTToMvtstoBTDTOs(facture.getDetailFactureBTCollection()));
        List<Integer> codeDepots = new ArrayList<>();
        codeDepots.add(dto.getDestinationID());
        codeDepots.add(dto.getSourceID());
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots);
        listDepot.stream().forEach(item -> {
            if (item.getCode().equals(dto.getSourceID())) {
                dto.setSourceDesignation(item.getDesignation());
            } else {
                dto.setDestinationDesignation(item.getDesignation());
            }
        });
        List<Integer> codeUnites = new ArrayList<>();
        facture.getDetailFactureBTCollection().forEach(x -> {
            codeUnites.add(x.getUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        dto.getDetails().forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityID())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });

        List<String> codeQuittances = new ArrayList<>();
        facture.getBTFE().forEach(x -> {
            codeQuittances.add(x.getNumFE());
        });
        List<FactureDTO> listQuittances = factureService.findDTOsByNumbonIn(codeQuittances);
        dto.setListQuittances(listQuittances);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<BonTransfertRecupDTO> findAll(CategorieDepotEnum categ, LocalDateTime fromDate,
            LocalDateTime toDate, Integer depotID_src, Integer depotID_des, Boolean interdepot, Boolean avoirTransfert, Boolean deleted, Boolean validated, Boolean conforme) throws ParseException {
        log.debug("Request to get All FactureBTs");
        QFactureBT _FactureBT = QFactureBT.factureBT;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_FactureBT.typbon.eq(TypeBonEnum.BT))
                .optionalAnd(categ, () -> _FactureBT.categDepot.eq(categ))
                .optionalAnd(fromDate, () -> _FactureBT.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _FactureBT.datbon.loe(toDate))
                .optionalAnd(depotID_src, () -> _FactureBT.coddep.eq(depotID_src))
                .optionalAnd(depotID_des, () -> _FactureBT.deptr.eq(depotID_des))
                .optionalAnd(interdepot, () -> _FactureBT.interdepot.eq(interdepot))
                .optionalAnd(avoirTransfert, () -> _FactureBT.avoirTransfert.eq(avoirTransfert))
                .optionalAnd(validated, () -> _FactureBT.valide.eq(validated))
                .optionalAnd(conforme, () -> _FactureBT.conforme.eq(conforme))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _FactureBT.datAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _FactureBT.datAnnul.isNull());
        Set<FactureBT> listFactureBTs = new HashSet((List<FactureBT>) bonTransferRecupRepository.findAll(builder));
        List<Integer> codeDepots = new ArrayList<>();
        listFactureBTs.forEach(x -> {
            codeDepots.add(x.getCoddep());
            codeDepots.add(x.getDeptr());
        });
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        List<String> codeQuittances = listFactureBTs.stream().flatMap(facture -> facture.getBTFE().stream()).map(Btfe::getNumFE).collect(toList());

        List<Facture> listQuittances = factureService.findByNumbonIn(codeQuittances);
        List<BonTransfertRecupDTO> listFactureBTDTO = listFactureBTs.stream().map(item -> {
            BonTransfertRecupDTO dto = BonTransfertRecupFactory.factureBTToFactureBTDTO(item);
            listDepot.stream().filter(x -> x.getCode().equals(item.getCoddep()) || x.getCode().equals(item.getDeptr())).forEach(filtredItem -> {
                if (filtredItem.getCode().equals(item.getCoddep())) {
                    dto.setSourceDesignation(filtredItem.getDesignation());
                    dto.setSourceCodeSaisi(filtredItem.getCodeSaisi());
                } else {
                    dto.setDestinationDesignation(filtredItem.getDesignation());
                    dto.setDestinationCodeSaisi(filtredItem.getCodeSaisi());
                }
            });
            List<String> relativeQuittancesID = item.getBTFE().stream().map(Btfe::getNumFE).collect(toList());
            dto.setRelativesBons(listQuittances.stream().filter(quittance -> relativeQuittancesID.contains(quittance.getNumbon())).map(Facture::getNumaffiche).collect(toList()));
            return dto;
        }).collect(toList());
        return listFactureBTDTO;
    }
    @Transactional(readOnly = true)
    public FactureBT findLastTransfertRecupByCategorieDepotAndCodeDep(CategorieDepotEnum categorieDepot,Integer codeDep) {
        FactureBT lastTransfert = bonTransferRecupRepository.findTopByCategDepotAndCoddepOrderByDatbonDesc(categorieDepot.toString(),codeDep);

        return lastTransfert;

    }
}
