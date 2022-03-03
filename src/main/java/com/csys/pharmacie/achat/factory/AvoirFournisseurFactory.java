package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import com.csys.pharmacie.achat.dto.AvoirFournisseurDTO;
import com.csys.pharmacie.achat.dto.BonEditionDTO;
import com.csys.pharmacie.achat.dto.ReceptionEditionDTO;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AvoirFournisseurFactory {

    @Autowired
    MvtstoAFFactory mvtstoAFFactory;

    public  AvoirFournisseurDTO avoirFournisseurToAvoirFournisseurDTOLazy(AvoirFournisseur avoirfournisseur) {
        AvoirFournisseurDTO avoirfournisseurDTO = new AvoirFournisseurDTO();
        avoirfournisseurDTO.setCodeFournisseur(avoirfournisseur.getCodeFournisseur());
        avoirfournisseurDTO.setDateFournisseur(avoirfournisseur.getDateFournisseur());
        avoirfournisseurDTO.setReferenceFournisseur(avoirfournisseur.getReferenceFournisseur());
        avoirfournisseurDTO.setCoddep(avoirfournisseur.getCoddep());
        avoirfournisseurDTO.setMontantTTC(avoirfournisseur.getMontantTTC());
        avoirfournisseurDTO.setUserAnnule(avoirfournisseur.getUserAnnule());
        avoirfournisseurDTO.setDateAnnule(avoirfournisseur.getDateAnnule());
        avoirfournisseurDTO.setIntegrer(avoirfournisseur.isIntegrer());
        avoirfournisseurDTO.setNumbon(avoirfournisseur.getNumbon());
        avoirfournisseurDTO.setCodvend(avoirfournisseur.getCodvend());
        avoirfournisseurDTO.setDatbon(avoirfournisseur.getDatbon());
        avoirfournisseurDTO.setDatesys(avoirfournisseur.getDatesys());
        avoirfournisseurDTO.setHeuresys(avoirfournisseur.getHeuresys());
        avoirfournisseurDTO.setTypbon(avoirfournisseur.getTypbon());
        avoirfournisseurDTO.setNumaffiche(avoirfournisseur.getNumaffiche());
        avoirfournisseurDTO.setCategDepot(avoirfournisseur.getCategDepot());
        avoirfournisseurDTO.setMemo(avoirfournisseur.getMemo());
        avoirfournisseurDTO.setNumFactureBonRecep(avoirfournisseur.getNumFactureBonRecep());
        return avoirfournisseurDTO;
    }

    public  AvoirFournisseur avoirFournisseurDTOToAvoirFournisseur(AvoirFournisseurDTO avoirfournisseurDTO) {
        AvoirFournisseur avoirfournisseur = new AvoirFournisseur();
        avoirfournisseur.setCodeFournisseur(avoirfournisseurDTO.getCodeFournisseur());
        avoirfournisseur.setDateFournisseur(avoirfournisseurDTO.getDateFournisseur());
        avoirfournisseur.setReferenceFournisseur(avoirfournisseurDTO.getReferenceFournisseur());
        avoirfournisseur.setCoddep(avoirfournisseurDTO.getCoddep());
        avoirfournisseur.setMontantTTC(avoirfournisseurDTO.getMontantTTC());
        avoirfournisseur.setUserAnnule(avoirfournisseurDTO.getUserAnnule());
        avoirfournisseur.setDateAnnule(avoirfournisseurDTO.getDateAnnule());
        avoirfournisseur.setIntegrer(avoirfournisseurDTO.getIntegrer());
        avoirfournisseur.setNumbon(avoirfournisseurDTO.getNumbon());
        avoirfournisseur.setCodvend(avoirfournisseurDTO.getCodvend());
        avoirfournisseur.setDatbon(avoirfournisseurDTO.getDatbon());
        avoirfournisseur.setDatesys(avoirfournisseurDTO.getDatesys());
        avoirfournisseur.setHeuresys(avoirfournisseurDTO.getHeuresys());
        avoirfournisseur.setTypbon(avoirfournisseurDTO.getTypbon());
        avoirfournisseur.setNumaffiche(avoirfournisseurDTO.getNumaffiche());
        avoirfournisseur.setCategDepot(avoirfournisseurDTO.getCategDepot());
        avoirfournisseur.setMemo(avoirfournisseurDTO.getMemo());
        avoirfournisseur.setNumFactureBonRecep(avoirfournisseurDTO.getNumFactureBonRecep());
        return avoirfournisseur;
    }

    public  Collection<AvoirFournisseurDTO> avoirfournisseurToAvoirFournisseurDTOs(Collection<AvoirFournisseur> avoirfournisseurs) {
        List<AvoirFournisseurDTO> avoirfournisseursDTO = new ArrayList<>();
        avoirfournisseurs.forEach(x -> {
            avoirfournisseursDTO.add(avoirFournisseurToAvoirFournisseurDTOLazy(x));
        });
        return avoirfournisseursDTO;
    }

    public  AvoirFournisseurDTO avoirFournisseurToAvoirFournisseurDTO(AvoirFournisseur avoirfournisseur) {
        AvoirFournisseurDTO avoirfournisseurDTO = new AvoirFournisseurDTO();
        avoirfournisseurDTO.setCodeFournisseur(avoirfournisseur.getCodeFournisseur());
        avoirfournisseurDTO.setDateFournisseur(avoirfournisseur.getDateFournisseur());
        avoirfournisseurDTO.setReferenceFournisseur(avoirfournisseur.getReferenceFournisseur());
        avoirfournisseurDTO.setCoddep(avoirfournisseur.getCoddep());
        avoirfournisseurDTO.setMontantTTC(avoirfournisseur.getMontantTTC());
        avoirfournisseurDTO.setUserAnnule(avoirfournisseur.getUserAnnule());
        avoirfournisseurDTO.setDateAnnule(avoirfournisseur.getDateAnnule());
        avoirfournisseurDTO.setIntegrer(avoirfournisseur.isIntegrer());
        avoirfournisseurDTO.setNumbon(avoirfournisseur.getNumbon());
        avoirfournisseurDTO.setCodvend(avoirfournisseur.getCodvend());
        avoirfournisseurDTO.setDatbon(avoirfournisseur.getDatbon());
        avoirfournisseurDTO.setDatesys(avoirfournisseur.getDatesys());
        avoirfournisseurDTO.setHeuresys(avoirfournisseur.getHeuresys());
        avoirfournisseurDTO.setTypbon(avoirfournisseur.getTypbon());
        avoirfournisseurDTO.setNumaffiche(avoirfournisseur.getNumaffiche());
        avoirfournisseurDTO.setCategDepot(avoirfournisseur.getCategDepot());
        avoirfournisseurDTO.setMemo(avoirfournisseur.getMemo());
        avoirfournisseurDTO.setNumFactureBonRecep(avoirfournisseur.getNumFactureBonRecep());
        avoirfournisseurDTO.setMvtstoAFList(mvtstoAFFactory.mvtstoAFToMvtstoAFDTOs(avoirfournisseur.getMvtstoAFList()));
        avoirfournisseurDTO.setDatebonEdition(java.util.Date.from(avoirfournisseur.getDatbon().atZone(ZoneId.systemDefault())
                .toInstant()));
        avoirfournisseurDTO.setDateFournisseurEdition(Date.from(avoirfournisseurDTO.getDateFournisseur().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        avoirfournisseurDTO.setBaseTvaAvoirFournisseurList(BaseTvaAvoirFournisseurFactory.basetvaavoirfournisseurToBaseTvaAvoirFournisseurDTOs(avoirfournisseur.getBaseTvaAvoirFournisseurList()));

        return avoirfournisseurDTO;
    }

    public  ReceptionEditionDTO avoirFournisseurToReceptionEditionDTO(AvoirFournisseur avoirFournisseur) {
        if (avoirFournisseur == null) {
            return null;
        }
        ReceptionEditionDTO bonRecepDTO = new ReceptionEditionDTO();
        bonRecepDTO.setCodvend(avoirFournisseur.getCodvend());
        bonRecepDTO.setNumbon(avoirFournisseur.getNumbon());
        bonRecepDTO.setDatbon(Date.from(avoirFournisseur.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        bonRecepDTO.setNumaffiche(avoirFournisseur.getNumaffiche());
        bonRecepDTO.setMntBon(avoirFournisseur.getMontantTTC());
        bonRecepDTO.setDateFrs(Date.from(avoirFournisseur.getDateFournisseur().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        bonRecepDTO.setCodeFournisseu(avoirFournisseur.getCodeFournisseur());
        bonRecepDTO.setRefFrs(avoirFournisseur.getReferenceFournisseur());
        bonRecepDTO.setTypbon(avoirFournisseur.getTypbon().type());
        if (avoirFournisseur.getUserAnnule() == null) {
            bonRecepDTO.setAnnule(Boolean.FALSE);

        } else {
            bonRecepDTO.setAnnule(Boolean.TRUE);
        }
        /*automatique is null for avoirFournisseur*/
        bonRecepDTO.setAutomatique(Boolean.FALSE);
        return bonRecepDTO;
    }

    public BonEditionDTO lazyAvoirfournisseurToBonEditionDTO(AvoirFournisseur avoirFournisseur) {
        BonEditionDTO bonEditionDTO = new BonEditionDTO();
        bonEditionDTO.setMntBon(avoirFournisseur.getMontantTTC());
        bonEditionDTO.setMemop(avoirFournisseur.getMemo());
        bonEditionDTO.setNumafficheFactureBonReception("");
        bonEditionDTO.setNumbonFactureBonReception("");
        bonEditionDTO.setCodeFournisseu(avoirFournisseur.getCodeFournisseur());
        bonEditionDTO.setNumbon(avoirFournisseur.getNumbon());
        bonEditionDTO.setCodvend(avoirFournisseur.getCodvend());
        bonEditionDTO.setDatebonEdition(Date.from(avoirFournisseur.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        bonEditionDTO.setCoddep(avoirFournisseur.getCoddep());
        bonEditionDTO.setRefFrs(avoirFournisseur.getReferenceFournisseur());
        bonEditionDTO.setDateFrs(Date.from(avoirFournisseur.getDateFournisseur().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        bonEditionDTO.setTypbon(avoirFournisseur.getTypbon().type());
        bonEditionDTO.setNumaffiche(avoirFournisseur.getNumaffiche());
        bonEditionDTO.setCategDepot(avoirFournisseur.getCategDepot());
        bonEditionDTO.setDetails(mvtstoAFFactory.toEditionDTO(avoirFournisseur.getMvtstoAFList()));
        return bonEditionDTO;
    }
}
