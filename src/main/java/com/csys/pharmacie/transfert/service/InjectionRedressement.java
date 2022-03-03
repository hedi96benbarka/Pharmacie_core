/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.service;

import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.transfert.domain.FactureBE;
import com.csys.pharmacie.transfert.domain.MvtStoBE;
import com.csys.pharmacie.transfert.domain.TableInjectionRedressement;
import com.csys.pharmacie.transfert.dto.FactureBEDTO;
import com.csys.pharmacie.transfert.factory.FactureBEFactory;
import com.csys.pharmacie.transfert.repository.FactureBERepository;
import com.csys.pharmacie.transfert.repository.TableInjectionRedressementRepository;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DELL
 */
@Service
@Transactional
public class InjectionRedressement {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    private final Logger log = LoggerFactory.getLogger(FactureBEService.class);

    private final FactureBERepository facturebeRepository;

    private final ParamService paramService;
    private final StockService stockService;

    private final ParamAchatServiceClient paramAchatServiceClient;

//    private final PricingService pricingService;

    private final TableInjectionRedressementRepository tableInjectionRedressementRepository;

    public InjectionRedressement(FactureBERepository facturebeRepository, ParamService paramService, StockService stockService, ParamAchatServiceClient paramAchatServiceClient, TableInjectionRedressementRepository tableInjectionRedressementRepository) {
        this.facturebeRepository = facturebeRepository;
        this.paramService = paramService;
        this.stockService = stockService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.tableInjectionRedressementRepository = tableInjectionRedressementRepository;
    }

  
    @Transactional
    public List<FactureBEDTO> addBonRedressement() throws ParseException {

        List<FactureBEDTO> resultDTOs = new ArrayList<>();
        List<TableInjectionRedressement> tableInjectionRedressements
                = tableInjectionRedressementRepository.findAll();
        Map<CategorieDepotEnum, List<TableInjectionRedressement>> groupByCategorieDepotEnum
                = tableInjectionRedressements.stream().collect(Collectors.groupingBy(x -> x.getCategDepot()));
        for (Map.Entry<CategorieDepotEnum, List<TableInjectionRedressement>> entry : groupByCategorieDepotEnum.entrySet()) {
            CategorieDepotEnum categDepot = entry.getKey();
            List<TableInjectionRedressement> listByCategorieDepotEnum = entry.getValue();
            Map<Integer, List<TableInjectionRedressement>> groupByCoddep
                    = listByCategorieDepotEnum.stream().collect(Collectors.groupingBy(x -> x.getCoddep()));
            for (Map.Entry<Integer, List<TableInjectionRedressement>> entryByCoddep : groupByCoddep.entrySet()) {
                Integer coddep = entryByCoddep.getKey();
                List<TableInjectionRedressement> listByCoddep = entryByCoddep.getValue();
                log.debug("Request to save FactureBE: {}", listByCoddep);
                DepotDTO depotd = paramAchatServiceClient.findDepotByCode(coddep);
                checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + depotd.getDesignation() + "] introuvable");
                checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getDesignation() + "] est un dépot fournisseur");

                FactureBE facturebe = new FactureBE();
                facturebe.setMemop("");
                facturebe.setCoddep(coddep);
                facturebe.setTypbon(TypeBonEnum.BE);
                facturebe.setCategDepot(categDepot);

                String numbon = paramService.getcompteur(categDepot, TypeBonEnum.BE);
                facturebe.setNumbon(numbon);
//                facturebe.s(motifRedressementService.findMotifRedressement(1));
                List<MvtStoBE> detailsFactureBE = new ArrayList();

                List<Depsto> newStock = new ArrayList();
                List<MvtStoBE> articlesToRecalculatePMP = new ArrayList();

                String numordre1 = "0001";
                for (TableInjectionRedressement mvtStoDTO : listByCoddep) {
                    MvtStoBE mvtstobe = new MvtStoBE();

                    mvtstobe.setLotinter(mvtStoDTO.getLotInter());
                    mvtstobe.setPriuni(mvtStoDTO.getPrixPmp().setScale(3));
                    mvtstobe.setDatPer(mvtStoDTO.getDatPer());
                    mvtstobe.setCategDepot(mvtStoDTO.getCategDepot());
                    if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
                        mvtstobe.setDesart(mvtStoDTO.getDesArtSec());
                        mvtstobe.setDesArtSec(mvtStoDTO.getDesart());

                    } else {
                        mvtstobe.setDesart(mvtStoDTO.getDesart());
                        mvtstobe.setDesArtSec(mvtStoDTO.getDesArtSec());
                    }
                    mvtstobe.setCodtva(mvtStoDTO.getCodtva());
                    mvtstobe.setTautva(mvtStoDTO.getTautva());

                    mvtstobe.setCodeSaisi(mvtStoDTO.getCodeSaisi());
                    mvtstobe.setQuantite(mvtStoDTO.getQuantite());
                    mvtstobe.setUnite(mvtStoDTO.getCodeUnite());
                    mvtstobe.setCodart(mvtStoDTO.getCode());
                    mvtstobe.setNumbon(numbon);
                    mvtstobe.setNumordre(numordre1);
                    mvtstobe.setFactureBE(facturebe); //se
                    detailsFactureBE.add(mvtstobe);
                    Depsto depsto = new Depsto(mvtstobe);
                    newStock.add(depsto);
                    Helper.IncrementString(numordre1, 4);
                }

                BigDecimal totaleMnt = detailsFactureBE.stream().map(item -> item.getPriuni().multiply(item.getQuantite())).reduce(BigDecimal.ZERO, (p, q) -> p.add(q));
                facturebe.setMntbon(totaleMnt.abs());
                facturebe.setDetailFactureBECollection(detailsFactureBE);

                List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
//                facturebe.calcul(listTvas);

                facturebe = facturebeRepository.save(facturebe);
                stockService.saveDepsto(newStock);
//                pricingService.updatePMPOnRedressement(articlesToRecalculatePMP);
                paramService.updateCompteurPharmacie(categDepot, TypeBonEnum.BE);
                FactureBEDTO resultDTO = FactureBEFactory.facturebeToFactureBEDTO(facturebe,false);
                resultDTOs.add(resultDTO);
            }

        }
        return resultDTOs;
    }

}
