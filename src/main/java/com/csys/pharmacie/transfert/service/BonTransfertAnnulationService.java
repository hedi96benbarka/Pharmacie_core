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
import com.csys.pharmacie.helper.SatisfactionEnum;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.transfert.domain.FactureBT;

import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.service.StockService;

import com.csys.pharmacie.transfert.domain.Btbt;
import com.csys.pharmacie.transfert.domain.DetailMvtStoBT;
import com.csys.pharmacie.transfert.domain.MvtStoBT;
import com.csys.pharmacie.transfert.domain.QFactureBT;
import com.csys.pharmacie.transfert.dto.BonTransferAnnulationDTO;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;
import com.csys.pharmacie.transfert.dto.MvtstoBTDTO;
import com.csys.pharmacie.transfert.factory.BonTransfertAnnulationFactory;
import com.csys.pharmacie.transfert.factory.FactureBTFactory;
import com.csys.pharmacie.transfert.factory.MvtstoBTFactory;
import com.csys.pharmacie.transfert.repository.FactureBTRepository;
import com.csys.pharmacie.vente.quittance.service.FactureService;
import com.csys.util.Preconditions;
import edu.emory.mathcs.backport.java.util.Arrays;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
@Service("BonTransfertAnnulationService")
public class BonTransfertAnnulationService {

    private final Logger log = LoggerFactory.getLogger(BonTransfertAnnulationService.class);
    private static String validationTransfertAuto;
    @Value("${validation-transfert-auto}")
    public void setValidation(String validation) {
        validationTransfertAuto = validation;
    }

    @Autowired
    private FactureBTRepository factureBTRepository;

    @Autowired
    private BonTransfertService bonTransfertService;

    @Autowired
    private ParamService paramService;

    @Autowired
    private InventaireService inventaireService;

    @Autowired
    private StockService stockService;

    @Autowired
    private FactureService factureService;

    @Autowired
    private ParamAchatServiceClient paramAchatServiceClient;

    @Transactional
    public BonTransferAnnulationDTO addBonTransferAnnulation(BonTransferAnnulationDTO dto) {
        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(dto.getDestinationID());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + depotd.getCode() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getCode() + "] est un dépot fournisseur");

        DepotDTO depots = paramAchatServiceClient.findDepotByCode(dto.getSourceID());
        Preconditions.checkBusinessLogique(!depots.getDesignation().equals("depot.deleted"), "Depot [" + depots.getCode() + "] introuvable");
        Preconditions.checkBusinessLogique(!depots.getDepotFrs(), "Dépot [" + depots.getCode() + "] est un dépot fournisseur");
        if (!Boolean.TRUE.equals(dto.getAvoirSuiteValidation())) {
            Boolean existeTransfertNonValide = factureBTRepository.existsByCoddepAndDeptrAndValideFalseAndCodAnnulIsNull(dto.getSourceID(), dto.getDestinationID());
            Preconditions.checkBusinessLogique(!existeTransfertNonValide, "existe.transfert.non.validé");
        }

        FactureBT bonTransferAnnulation = FactureBTFactory.factureBTDTOToFactureBT(dto);

        bonTransferAnnulation.setAvoirSuiteValidation(dto.getAvoirSuiteValidation());

        String numbon = paramService.getcompteur(dto.getCategDepot(), TypeBonEnum.BT);
        bonTransferAnnulation.setNumbon(numbon);
        bonTransferAnnulation.setInterdepot(true);

        if (Boolean.TRUE.equals(bonTransferAnnulation.getAvoirSuiteValidation())) {
            bonTransferAnnulation.setValide(Boolean.TRUE);
            bonTransferAnnulation.setConforme(Boolean.TRUE);
        } else {
            bonTransferAnnulation.setValide(Boolean.FALSE);
        }

        List<Btbt> listeRelatives = dto.getRelativesFactureBTs().stream().map(item -> {
            return new Btbt(bonTransferAnnulation, item);
        }).collect(toList());
        log.debug("liste relative BTs  {}", listeRelatives);
        bonTransferAnnulation.setNumBTReturn(listeRelatives);
        bonTransferAnnulation.setSatisf(SatisfactionEnum.RECOVRED);
        // check for inventory
        Integer[] articlesIDs = dto.getDetails().stream().map(MvtstoBTDTO::getArticleID).toArray(Integer[]::new);
        List<ArticleDTO> articles = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(dto.getCategDepot(), articlesIDs);
        List<Integer> categArticleIDs = articles.stream().map(item -> item.getCategorieArticle().getCode()).collect(toList());
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(categArticleIDs, dto.getDestinationID());
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = articles.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }

        List<Depsto> depstos = stockService.findByCodartInAndCoddepAndQteGreaterThan(Arrays.asList(articlesIDs), bonTransferAnnulation.getCoddep(), BigDecimal.ZERO);
//        log.debug("liste of depstos to be processed  {}", depstos);
        List<Depsto> newDepstos = new ArrayList(); //the new stock to be created as a result of this transfert.
        List<MvtStoBT> detailsFactureBT = new ArrayList();
        String numordre = "0001";
        for (MvtstoBTDTO mvtstoBTDTO : dto.getDetails()) {
            MvtStoBT detailFacture = MvtstoBTFactory.mvtstoBTDTOToMvtstoBT(mvtstoBTDTO);
            detailFacture.setFactureBT(bonTransferAnnulation);

            detailFacture.setCodart(mvtstoBTDTO.getArticleID());
            detailFacture.setNumbon(numbon);
            detailFacture.setNumordre(numordre);
//            detailFacture.setQteben(BigDecimal.ZERO);

            numordre = Helper.IncrementString(numordre, 4);
            detailsFactureBT.add(detailFacture);
            List<DetailMvtSto> detailMvtSto = stockService.GestionDetailFacture(depstos, detailFacture, DetailMvtStoBT.class, false);

            List<DetailMvtStoBT> detailMvtStoBTs = new ArrayList<>();
            detailMvtSto.forEach(item -> {
                DetailMvtStoBT detailMvtStoBT = (DetailMvtStoBT) item;
                detailMvtStoBT.setMvtStoBT(detailFacture);
                detailMvtStoBTs.add(detailMvtStoBT);

                Depsto newDepsto = new Depsto(detailMvtStoBT);
                newDepsto.setCoddep(dto.getDestinationID());
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
        }
        bonTransferAnnulation.setDetailFactureBTCollection(detailsFactureBT);
        if (validationTransfertAuto.equals("true")) {
            bonTransferAnnulation.setConforme(Boolean.TRUE);
            bonTransferAnnulation.setValide(Boolean.TRUE);
            bonTransferAnnulation.setUserValidate(SecurityContextHolder.getContext().getAuthentication().getName());
            bonTransferAnnulation.setDateValidate(LocalDateTime.now());
        }
        if (dto.getRelativesFactureBTs().size() > 0) {
            List<FactureBT> factureBTs = factureBTRepository.findByNumbonIn(dto.getRelativesFactureBTs());
            List<MvtStoBT> listeMvtstoBT = new ArrayList<>();
            for (FactureBT factureBT : factureBTs) {
                if (!Boolean.TRUE.equals(factureBT.isAvoirTransfert())) {
                    Preconditions.checkBusinessLogique(!factureBT.getSatisf().equals(SatisfactionEnum.RECOVRED), "factureDR.SatisfaitTotalement");
                }
                if (!Boolean.TRUE.equals(dto.getAvoirSuiteValidation())) {
                    Preconditions.checkBusinessLogique(Boolean.TRUE.equals(factureBT.getValide()), "transfert-non-valide", factureBT.getNumaffiche());
                }
                listeMvtstoBT.addAll(factureBT.getDetailFactureBTCollection());
            }

            // Update remaining quantities of each detail of the relative transfers 
            for (MvtstoBTDTO mvt : dto.getDetails()) {
                List<MvtStoBT> listeMvtstoBTByCodArt = listeMvtstoBT.stream().filter(item -> item.getCodart().equals(mvt.getArticleID())
                        && item.getQteben().compareTo(BigDecimal.ZERO) > 0
                        && item.getUnite().equals(mvt.getUnityID()))
                        .collect(Collectors.toList());
                BigDecimal qte = mvt.getQuantity();
                Integer availableQteMvtStoBT = listeMvtstoBTByCodArt.stream()
                        .map(filtredItem -> filtredItem.getQteben().intValue())
                        .collect(Collectors.summingInt(Integer::new));
                Preconditions.checkBusinessLogique(availableQteMvtStoBT >= qte.intValue(), "return.add.insuffisant-qte-demande", mvt.getArticleID().toString());
                for (MvtStoBT mvtstoBT : listeMvtstoBTByCodArt) {
                    BigDecimal qteToRmvFromMvtstoBT = qte.min(mvtstoBT.getQteben());
                    if (qteToRmvFromMvtstoBT.compareTo(BigDecimal.ZERO) > 0) {
                        mvtstoBT.setQteben(mvtstoBT.getQteben().subtract(qteToRmvFromMvtstoBT));
                        qte = qte.subtract(qteToRmvFromMvtstoBT);
                    }
                    if (qte.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                }
            }

            // Update satisfaction of relative transfers 
            for (FactureBT factureBT : factureBTs) {
                if (!Boolean.TRUE.equals(factureBT.isAvoirTransfert())) {
                    Integer satisfaitTotal = factureBT.getDetailFactureBTCollection().stream()
                            .filter(item -> item.getQteben().compareTo(BigDecimal.ZERO) == 0)
                            .collect(Collectors.toList()).size();
                    Integer satisfaitPartiel = factureBT.getDetailFactureBTCollection().stream()
                            .filter(item -> item.getQteben().compareTo(BigDecimal.ZERO) > 0 && item.getQuantite().compareTo(item.getQteben()) > 0)
                            .collect(Collectors.toList()).size();
                    if (satisfaitTotal == factureBT.getDetailFactureBTCollection().size()) {
                        factureBT.setSatisf(SatisfactionEnum.RECOVRED);
                    }
                    if (satisfaitPartiel > 0) {
                        factureBT.setSatisf(SatisfactionEnum.PARTIALLY_RECOVRED);
                    }
                    if (satisfaitTotal != factureBT.getDetailFactureBTCollection().size() && satisfaitTotal > 0) {
                        factureBT.setSatisf(SatisfactionEnum.PARTIALLY_RECOVRED);
                    }
                }
            }
            factureBTRepository.save(factureBTs);
        }

        stockService.saveDepsto(newDepstos);

        factureBTRepository.save(bonTransferAnnulation);
        paramService.updateCompteurPharmacie(bonTransferAnnulation.getCategDepot(), TypeBonEnum.BT);
        return BonTransfertAnnulationFactory.factureBTToFactureBTDTO(bonTransferAnnulation);
    }

    @Transactional(readOnly = true)
    public BonTransferAnnulationDTO findOne(String numBon) {
        log.debug("Request to get Facture: {}", numBon);
        FactureBT facture = factureBTRepository.findOne(numBon);
        BonTransferAnnulationDTO dto = BonTransfertAnnulationFactory.factureBTToFactureBTDTO(facture);
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

        List<String> codeFactureBTs = new ArrayList<>();
        facture.getNumBTReturn().forEach(x -> {
            codeFactureBTs.add(x.getNumBTReturn());
        });
        List<FactureBTDTO> listFactureBTs = bonTransfertService.findByNumbonIn(codeFactureBTs);
        dto.setFactureBTs(listFactureBTs);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<BonTransferAnnulationDTO> findAll(CategorieDepotEnum categ, LocalDateTime fromDate,
            LocalDateTime toDate, Integer depotID_src, Integer depotID_des, Boolean interdepot, Boolean avoirTransfert, Boolean deleted, Boolean validated, Boolean conforme) throws ParseException {
        log.debug("Request to get All FactureBTs");
        QFactureBT _FactureBT = QFactureBT.factureBT;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_FactureBT.typbon.eq(TypeBonEnum.BT))
                .optionalAnd(categ, () -> _FactureBT.categDepot.eq(categ))
                .optionalAnd(fromDate, () -> _FactureBT.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _FactureBT.datbon.loe(toDate))
                .optionalAnd(interdepot, () -> _FactureBT.interdepot.eq(interdepot))
                .optionalAnd(depotID_src, () -> _FactureBT.coddep.eq(depotID_src))
                .optionalAnd(depotID_des, () -> _FactureBT.deptr.eq(depotID_des))
                .optionalAnd(avoirTransfert, () -> _FactureBT.avoirTransfert.eq(interdepot))
                .optionalAnd(validated, () -> _FactureBT.valide.eq(validated))
                .optionalAnd(conforme, () -> _FactureBT.conforme.eq(conforme))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _FactureBT.datAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _FactureBT.datAnnul.isNull());
        List<FactureBT> listFactureBTs = (List<FactureBT>) factureBTRepository.findAll(builder);

        List<Integer> codeDepots = new ArrayList<>();
        listFactureBTs.forEach(x -> {
            codeDepots.add(x.getCoddep());
            codeDepots.add(x.getDeptr());
        });
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        List<BonTransferAnnulationDTO> listFactureBTDTO = listFactureBTs.stream().map(item -> {
            BonTransferAnnulationDTO dto = BonTransfertAnnulationFactory.factureBTToFactureBTDTO(item);
            listDepot.stream().filter(x -> x.getCode().equals(item.getCoddep()) || x.getCode().equals(item.getDeptr())).forEach(filtredItem -> {
                if (filtredItem.getCode().equals(item.getCoddep())) {
                    dto.setSourceDesignation(filtredItem.getDesignation());
                    dto.setSourceCodeSaisi(filtredItem.getCodeSaisi());
                } else {
                    dto.setDestinationDesignation(filtredItem.getDesignation());
                    dto.setDestinationCodeSaisi(filtredItem.getCodeSaisi());
                }
            });

            List<String> codeFactureBTs = new ArrayList<>();
            item.getNumBTReturn().forEach(x -> {
                codeFactureBTs.add(x.getNumBTReturn());
            });
            List<FactureBTDTO> listeFactureBTs = bonTransfertService.findByNumbonIn(codeFactureBTs);
            dto.setFactureBTs(listeFactureBTs);

            return dto;
        }).collect(toList());
        return listFactureBTDTO;
    }

}
