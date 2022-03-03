package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.RetourPerime;
import com.csys.pharmacie.achat.dto.BonEditionDTO;
import com.csys.pharmacie.achat.dto.ReceptionEditionDTO;
import com.csys.pharmacie.achat.dto.RetourPerimeDTO;
import com.csys.pharmacie.helper.BaseTVAFactory;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class RetourPerimeFactory {

    public static RetourPerimeDTO retourPerimeToRetourPerimeDTO(RetourPerime factureretourPerime) {
        RetourPerimeDTO factureretourPerimeDTO = lazyRetourPerimeToRetourPerimeDTO(factureretourPerime);
        factureretourPerimeDTO.setDetailFactureRPCollection(MvtstoRetourPerimeFactory.mvtstoRetourPerimesToMvtstoRetourPerimeDTOs(factureretourPerime.getDetailFactureRPCollection()));
        factureretourPerimeDTO.setBasesTVA(BaseTVAFactory.listeEntitiesToListDTos(factureretourPerime.getBaseTvaRetourPerime()));
        return factureretourPerimeDTO;
    }

    public static RetourPerimeDTO lazyRetourPerimeToRetourPerimeDTO(RetourPerime factureretourPerime) {
        RetourPerimeDTO factureretourPerimeDTO = new RetourPerimeDTO();
        factureretourPerimeDTO.setMntbon(factureretourPerime.getMntbon());
        factureretourPerimeDTO.setMemop(factureretourPerime.getMemop());
        factureretourPerimeDTO.setCodFrs(factureretourPerime.getCodFrs());
        factureretourPerimeDTO.setMotifRetourDTO(MotifRetourFactory.motifretourToMotifRetourDTO(factureretourPerime.getMotifRetour()));
        factureretourPerimeDTO.setNumbon(factureretourPerime.getNumbon());
        factureretourPerimeDTO.setCodvend(factureretourPerime.getCodvend());
        factureretourPerimeDTO.setDatbon(factureretourPerime.getDatbon());
        factureretourPerimeDTO.setCoddep(factureretourPerime.getCoddep());
        factureretourPerimeDTO.setRefFrs(factureretourPerime.getRefFrs());
        factureretourPerimeDTO.setDatRefFrs(factureretourPerime.getDatRefFrs());
        factureretourPerimeDTO.setTypbon(factureretourPerime.getTypbon());
        factureretourPerimeDTO.setNumaffiche(factureretourPerime.getNumaffiche());
        factureretourPerimeDTO.setCategDepot(factureretourPerime.getCategDepot());

        return factureretourPerimeDTO;
    }

    public static RetourPerime retourPerimeDTOToRetourPerime(RetourPerimeDTO factureretourPerimeDTO) {
        RetourPerime factureretourPerime = new RetourPerime();
        factureretourPerime.setMntbon(factureretourPerimeDTO.getMntbon());
        factureretourPerime.setMemop(factureretourPerimeDTO.getMemop());
        factureretourPerime.setCodFrs(factureretourPerimeDTO.getCodFrs());
        factureretourPerime.setMotifRetour(MotifRetourFactory.motifretourDTOToMotifRetour(factureretourPerimeDTO.getMotifRetourDTO()));

        factureretourPerime.setNumbon(factureretourPerimeDTO.getNumbon());
        factureretourPerime.setCodvend(factureretourPerimeDTO.getCodvend());
        factureretourPerime.setDatbon(factureretourPerimeDTO.getDatbon());
        factureretourPerime.setRefFrs(factureretourPerimeDTO.getRefFrs());
        factureretourPerime.setDatRefFrs(factureretourPerimeDTO.getDatRefFrs());

        factureretourPerime.setTypbon(factureretourPerimeDTO.getTypbon());
        factureretourPerime.setNumaffiche(factureretourPerimeDTO.getNumaffiche());
        factureretourPerime.setCategDepot(factureretourPerimeDTO.getCategDepot());
        factureretourPerime.setCoddep(factureretourPerimeDTO.getCoddep());
        return factureretourPerime;
    }

    public static Collection<RetourPerimeDTO> retourPerimeToRetourPerimeDTOs(Collection<RetourPerime> factureretour_perimes) {
        List<RetourPerimeDTO> factureretour_perimesDTO = new ArrayList<>();
        factureretour_perimes.forEach(x -> {
            factureretour_perimesDTO.add(lazyRetourPerimeToRetourPerimeDTO(x));
        });
        return factureretour_perimesDTO;
    }

    public static BonEditionDTO lazyRetourPerimeToBonEditionDTO(RetourPerime factureretourPerime) {
        BonEditionDTO bonEditionDTO = new BonEditionDTO();
        bonEditionDTO.setMntBon(factureretourPerime.getMntbon());
        bonEditionDTO.setMemop(factureretourPerime.getMemop());
        bonEditionDTO.setNumafficheFactureBonReception("");
        bonEditionDTO.setNumbonFactureBonReception("");
        bonEditionDTO.setCodeFournisseu(factureretourPerime.getCodFrs());
        bonEditionDTO.setNumbon(factureretourPerime.getNumbon());
        bonEditionDTO.setCodvend(factureretourPerime.getCodvend());
        bonEditionDTO.setDatebonEdition(Date.from(factureretourPerime.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        bonEditionDTO.setCoddep(factureretourPerime.getCoddep());
        bonEditionDTO.setRefFrs(factureretourPerime.getRefFrs());
        bonEditionDTO.setDateFrs(Date.from(factureretourPerime.getDatRefFrs().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        bonEditionDTO.setTypbon(factureretourPerime.getTypbon().type());
        bonEditionDTO.setNumaffiche(factureretourPerime.getNumaffiche());
        bonEditionDTO.setCategDepot(factureretourPerime.getCategDepot());
        bonEditionDTO.setDetails(MvtstoRetourPerimeFactory.toEditionDTO(factureretourPerime.getDetailFactureRPCollection()));

        return bonEditionDTO;
    }
    
    public static ReceptionEditionDTO retourPerimeToReceptionEditionDTO(RetourPerime retourPerime) {
        if (retourPerime == null) {
            return null;
        }
        ReceptionEditionDTO bonRecepDTO = new ReceptionEditionDTO();
        bonRecepDTO.setCodvend(retourPerime.getCodvend());
        bonRecepDTO.setNumbon(retourPerime.getNumbon());
        bonRecepDTO.setDatbon(Date.from(retourPerime.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        bonRecepDTO.setNumaffiche(retourPerime.getNumaffiche());
        bonRecepDTO.setMntBon(retourPerime.getMntbon());
        bonRecepDTO.setDateFrs(Date.from(retourPerime.getDatRefFrs().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        bonRecepDTO.setCodeFournisseu(retourPerime.getCodFrs());
        bonRecepDTO.setRefFrs(retourPerime.getRefFrs());
        bonRecepDTO.setTypbon(retourPerime.getTypbon().type());
        if (retourPerime.getCodAnnul() == null) {
            bonRecepDTO.setAnnule(Boolean.FALSE);

        } else {
            bonRecepDTO.setAnnule(Boolean.TRUE);
        }
        /*automatique is null for retourPerime*/
        bonRecepDTO.setAutomatique(Boolean.FALSE);
        return bonRecepDTO;
    }

    public static List<BonEditionDTO> retourPerimeToBonEditionDTOs(Collection<RetourPerime> factureretour_perimes) {
        List<BonEditionDTO> factureretour_perimesDTO = new ArrayList<>();
        factureretour_perimes.forEach(x -> {
            factureretour_perimesDTO.add(lazyRetourPerimeToBonEditionDTO(x));
        });
        return factureretour_perimesDTO;
    }
}
