package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.achat.dto.DemandeRedressementDTO;
import com.csys.pharmacie.helper.BaseTVAFactory;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.transfert.domain.FactureBE;
import com.csys.pharmacie.transfert.domain.MvtStoBE;
import com.csys.pharmacie.transfert.dto.FactureBEDTO;
import com.csys.pharmacie.transfert.dto.FactureBEEditionDTO;
import com.csys.pharmacie.transfert.dto.MvtStoBEDTO;
import com.csys.pharmacie.transfert.service.BonTransfertRecupService;
import com.csys.pharmacie.transfert.service.FactureBEService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class FactureBEFactory {


    private static Logger log = LoggerFactory.getLogger(FactureBEFactory.class);

    public static FactureBEDTO facturebeToFactureBEDTO(FactureBE facturebe, Boolean edition) {
        if (facturebe == null) {
            return null;
        }
        FactureBEDTO facturebeDTO = new FactureBEDTO();
        facturebeDTO.setMntbon(facturebe.getMntbon());
        facturebeDTO.setMemop(facturebe.getMemop());
        facturebeDTO.setCoddep(facturebe.getCoddep());
        facturebeDTO.setNumbon(facturebe.getNumbon());
        facturebeDTO.setCodvend(facturebe.getCodvend());
       // facturebeDTO.setDatbon(facturebe.getDatbon());
       // facturebe.setDatbon(2019-10-10T13:24:18.078Z);
        LocalDateTime aDateTime = LocalDateTime.of(2019, Month.APRIL, 11, 19, 30, 40);


        String str = "2019-05-11 13:18:00.910";
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //facturebeDTO.setDatbon(LocalDateTime.parse(str));
        facturebeDTO.setDatbon(aDateTime);
        System.out.println("dateBon est "+aDateTime);

        facturebeDTO.setTypbon(facturebe.getTypbon());
        facturebeDTO.setNumaffiche(facturebe.getNumaffiche());
        facturebeDTO.setCategDepot(facturebe.getCategDepot());
        facturebeDTO.setNumeroDemande(facturebe.getNumeroDemande());
        facturebeDTO.setCodeMotifRedressement(facturebe.getCodeMotifRedressement());
        BaseTVAFactory a = new BaseTVAFactory();

        facturebeDTO.setBasesTVA(a.listeEntitiesToListDTos(facturebe.getBaseTvaRedressement()));
        facturebeDTO.setBasesTVA(a.listeEntitiesToListDTos(facturebe.getBaseTvaRedressement()));
        if (edition) {
            facturebeDTO.setDatbonEdition(Date.from(facturebe.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
           facturebeDTO.setDetails(MvtStoBEFactory.mvtstobeToMvtStoBEDTOs(facturebe.getDetailFactureBECollection()));

        }



        //bloc oumayma and hedi
//        List<MvtStoBEDTO> mvtStoBEDTOS= factureBEService.findByNumBon(facturebe.getNumbon());
//
//        facturebeDTO.setDetails(mvtStoBEDTOS);
        return facturebeDTO;
    }

    public static FactureBEEditionDTO facturebeToFactureBEEditionDTO(FactureBE facturebe, DemandeRedressementDTO demandeRedressementDTO) {
        if (facturebe == null) {
            return null;
        }
        FactureBEEditionDTO facturebeDTO = new FactureBEEditionDTO();
        facturebeDTO.setMntbon(facturebe.getMntbon());
        facturebeDTO.setMemop(facturebe.getMemop());
        facturebeDTO.setCoddep(facturebe.getCoddep());

        facturebeDTO.setNumbon(facturebe.getNumbon());
        facturebeDTO.setCodvend(facturebe.getCodvend());
        facturebeDTO.setDatbon(Date.from(facturebe.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));

        facturebeDTO.setTypbon(facturebe.getTypbon());
        facturebeDTO.setNumaffiche(facturebe.getNumaffiche());
        facturebeDTO.setCategDepot(facturebe.getCategDepot());
        facturebeDTO.setCodeDemande(facturebe.getNumeroDemande());
        facturebeDTO.setNumeroDemande(demandeRedressementDTO.getNumeroDemande());
        facturebeDTO.setCodeMotifRedressement(facturebe.getCodeMotifRedressement());
        return facturebeDTO;
    }

    public static FactureBE facturebeDTOToFactureBE(FactureBEDTO facturebeDTO) {

        FactureBE facturebe = new FactureBE();
        facturebe.setMntbon(facturebeDTO.getMntbon());
        facturebe.setMemop(facturebeDTO.getMemop());
        facturebe.setCoddep(facturebeDTO.getCoddep());
        facturebe.setNumbon(facturebeDTO.getNumbon());
        facturebe.setTypbon(TypeBonEnum.BE);
        facturebe.setNumaffiche(facturebeDTO.getNumaffiche());
        facturebe.setCategDepot(facturebeDTO.getCategDepot());
        facturebe.setNumeroDemande(facturebeDTO.getNumeroDemande());
        facturebe.setCodeMotifRedressement(facturebeDTO.getCodeMotifRedressement());
//        List<BaseTvaRedressement> listeBasesTVA = new ArrayList();

        return facturebe;
    }



    public static Collection<FactureBEDTO> facturebeToFactureBEDTOs(Collection<FactureBE> facturebes) {
        List<FactureBEDTO> facturebesDTO = new ArrayList<>();
        facturebes.forEach(x -> {
            facturebesDTO.add(facturebeToFactureBEDTO(x, false));
        });
        return facturebesDTO;
    }
}
