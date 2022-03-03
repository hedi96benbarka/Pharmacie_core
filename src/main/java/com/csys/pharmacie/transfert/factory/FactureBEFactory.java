package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.achat.dto.DemandeRedressementDTO;
import com.csys.pharmacie.helper.BaseTVAFactory;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.transfert.domain.FactureBE;
import com.csys.pharmacie.transfert.dto.FactureBEDTO;
import com.csys.pharmacie.transfert.dto.FactureBEEditionDTO;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class FactureBEFactory {

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
        facturebeDTO.setDatbon(facturebe.getDatbon());

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
