package com.csys.pharmacie.vente.avoir.factory;

import com.csys.pharmacie.helper.BaseTVAFactory;
import com.csys.pharmacie.vente.avoir.domain.FactureAV;
import com.csys.pharmacie.vente.avoir.dto.FactureAVDTO;
import com.csys.pharmacie.vente.avoir.dto.FactureAVEditionDTO;
import com.csys.pharmacie.vente.quittance.dto.FactureDTO;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FactureAVFactory {

    @Autowired
    BaseTVAFactory baseTVAFactory;
    @Autowired
    MvtStoAVFactory mvtStoAVFactory;

    public FactureAVEditionDTO factureavToFactureAVEditionDTO(FactureAV factureav) {
        FactureAVEditionDTO factureavDTO = new FactureAVEditionDTO();
        factureavDTO.setMntbon(factureav.getMntbon());
        factureavDTO.setMemop(factureav.getMemop());
        factureavDTO.setNumdoss(factureav.getNumdoss());
        factureavDTO.setCodeDepot(factureav.getCoddep());
        factureavDTO.setBasetvaFactureAVCollection(baseTVAFactory.listeEntitiesToListDTos(factureav.getBasetvaFactureAVCollection()));
        factureavDTO.setMvtStoAVCollection(mvtStoAVFactory.mvtstoavToMvtStoAVDTOs(factureav.getMvtStoAVCollection()));
        factureavDTO.setNumbon(factureav.getNumbon());
        factureavDTO.setCodvend(factureav.getCodvend());
        factureavDTO.setDatbon(Date.from(factureav.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        factureavDTO.setDatesys(factureav.getDatesys());
        factureavDTO.setHeuresys(factureav.getHeuresys());
        factureavDTO.setTypbon(factureav.getTypbon());
        factureavDTO.setNumaffiche(factureav.getNumaffiche());
        factureavDTO.setCategDepot(factureav.getCategDepot());
        factureavDTO.setImprimer(factureav.isImprimer());
        factureavDTO.setPartiePEC(factureav.getPartiePEC());
        factureavDTO.setPartiePatient(factureav.getPartiePatient());
        return factureavDTO;
    }

    public FactureAVDTO factureavToFactureAVDTO(FactureAV factureav) {
        FactureAVDTO factureavDTO = new FactureAVDTO();
        factureavDTO.setMntbon(factureav.getMntbon());
        factureavDTO.setMemop(factureav.getMemop());
        factureavDTO.setNumdoss(factureav.getNumdoss());
        factureavDTO.setCodeDepot(factureav.getCoddep());
        factureavDTO.setBaseTvaFactureList(baseTVAFactory.listeEntitiesToListDTos(factureav.getBasetvaFactureAVCollection()));
        factureavDTO.setMvtstoCollection(mvtStoAVFactory.mvtstoavToMvtStoAVDTOs(factureav.getMvtStoAVCollection()));
        factureavDTO.setNumbon(factureav.getNumbon());
        factureavDTO.setCodvend(factureav.getCodvend());
        factureavDTO.setDatbon(factureav.getDatbon());
        factureavDTO.setDatesys(factureav.getDatesys());
        factureavDTO.setHeuresys(factureav.getHeuresys());
        factureavDTO.setTypbon(factureav.getTypbon());
        factureavDTO.setNumaffiche(factureav.getNumaffiche());
        factureavDTO.setCategDepot(factureav.getCategDepot());
        factureavDTO.setImprimer(factureav.isImprimer());
        factureavDTO.setPartiePEC(factureav.getPartiePEC());
        factureavDTO.setPartiePatient(factureav.getPartiePatient());
        factureavDTO.setNumQuittanceCorrespondant(factureav.getNumQuittanceCorrespondant());
        factureavDTO.setNumbonComplementaire(factureav.getNumbonComplementaire());
        factureavDTO.setAvoirPhPostReg(factureav.getAvoirPhPostReg());
        factureavDTO.setMontantRemise(factureav.getMontantRemise());
        return factureavDTO;
    }

    public FactureAVDTO factureavToFactureAVDTOLazy(FactureAV factureav) {
        FactureAVDTO factureavDTO = new FactureAVDTO();
        factureavDTO.setMntbon(factureav.getMntbon());
        factureavDTO.setMemop(factureav.getMemop());
        factureavDTO.setNumdoss(factureav.getNumdoss());
        factureavDTO.setCodeDepot(factureav.getCoddep());
        factureavDTO.setNumbon(factureav.getNumbon());
        factureavDTO.setCodvend(factureav.getCodvend());
        factureavDTO.setDatbon(factureav.getDatbon());
        factureavDTO.setDatesys(factureav.getDatesys());
        factureavDTO.setHeuresys(factureav.getHeuresys());
        factureavDTO.setTypbon(factureav.getTypbon());
        factureavDTO.setNumaffiche(factureav.getNumaffiche());
        factureavDTO.setCategDepot(factureav.getCategDepot());
        factureavDTO.setImprimer(factureav.isImprimer());
        factureavDTO.setPartiePEC(factureav.getPartiePEC());
        factureavDTO.setPartiePatient(factureav.getPartiePatient());
        factureavDTO.setNumQuittanceCorrespondant(factureav.getNumQuittanceCorrespondant());
        factureavDTO.setNumbonComplementaire(factureav.getNumbonComplementaire());
        factureavDTO.setAvoirPhPostReg(factureav.getAvoirPhPostReg());
        factureavDTO.setMontantRemise(factureav.getMontantRemise());
        return factureavDTO;
    }

    public FactureAV factureavDTOToFactureAV(FactureAVDTO factureavDTO) {
        FactureAV factureav = new FactureAV();
        factureav.setMntbon(factureavDTO.getMntbon());
        factureav.setMemop(factureavDTO.getMemop());
        factureav.setNumdoss(factureavDTO.getNumdoss());
        factureav.setImprimer(factureavDTO.getImprimer());
        factureav.setCoddep(factureavDTO.getCodeDepot());
        factureav.setNumbon(factureavDTO.getNumbon());
        factureav.setCodvend(factureavDTO.getCodvend());
        factureav.setDatbon(factureavDTO.getDatbon());
        factureav.setDatesys(factureavDTO.getDatesys());
        factureav.setHeuresys(factureavDTO.getHeuresys());
        factureav.setTypbon(factureavDTO.getTypbon());
        factureav.setNumaffiche(factureavDTO.getNumaffiche());
        factureav.setCategDepot(factureavDTO.getCategDepot());
        factureav.setPartiePEC(factureavDTO.getPartiePEC());
        factureav.setPartiePatient(factureavDTO.getPartiePatient());
        return factureav;
    }

    public List<FactureAVDTO> factureavToFactureAVDTOs(Collection<FactureAV> factureavs) {
        List<FactureAVDTO> factureavsDTO = new ArrayList<>();
        factureavs.forEach(x -> {
            factureavsDTO.add(factureavToFactureAVDTO(x));
        });
        return factureavsDTO;
    }

    public List<FactureAVDTO> factureavToFactureAVDTOLazys(Collection<FactureAV> factureavs) {
        List<FactureAVDTO> factureavsDTO = new ArrayList<>();
        factureavs.forEach(x -> {
            factureavsDTO.add(factureavToFactureAVDTOLazy(x));
        });
        return factureavsDTO;
    }

    public FactureDTO factureAVToFactureDTO(FactureAV facture) {
        FactureDTO factureDTO = new FactureDTO();
        factureDTO.setMntbon(facture.getMntbon());
        factureDTO.setMemop(facture.getMemop());
        factureDTO.setNumdoss(facture.getNumdoss());
        factureDTO.setCodeDepot(facture.getCoddep());
        factureDTO.setNumbon(facture.getNumbon());
        factureDTO.setCodvend(facture.getCodvend());
        factureDTO.setDatbon(facture.getDatbon());
        factureDTO.setDatesys(facture.getDatesys());
        factureDTO.setHeuresys(facture.getHeuresys());
        factureDTO.setTypbon(facture.getTypbon());
        factureDTO.setNumaffiche(facture.getNumaffiche());
        factureDTO.setCategDepot(facture.getCategDepot());
        factureDTO.setPartiePEC(facture.getPartiePEC());
        factureDTO.setPartiePatient(facture.getPartiePatient());
        factureDTO.setNumbonComplementaire(facture.getNumbonComplementaire());
        factureDTO.setBaseTvaFactureList(baseTVAFactory.listeEntitiesToListDTos(facture.getBasetvaFactureAVCollection()));
        factureDTO.setMvtstoCollection(mvtStoAVFactory.mvtstoAVToMvtstoDTOs(facture.getMvtStoAVCollection()));
        return factureDTO;
    }

    public List<FactureDTO> factureAVToFactureDTOs(List<FactureAV> factures) {
        List<FactureDTO> facturesDTO = new ArrayList<>();
        factures.forEach(x -> {
            facturesDTO.add(factureAVToFactureDTO(x));
        });
        return facturesDTO;
    }
}
