package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.BaseTvaFactureDirecte;
import com.csys.pharmacie.achat.domain.DetailFactureDirecte;
import com.csys.pharmacie.achat.domain.FactureDirecte;
import com.csys.pharmacie.achat.domain.FactureDirecteCostCenter;
import com.csys.pharmacie.achat.domain.FactureDirecteModeReglement;
import com.csys.pharmacie.achat.dto.BaseTvaFactureDirecteDTO;
import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
import com.csys.pharmacie.achat.dto.DetailFactureDirecteDTO;
import com.csys.pharmacie.achat.dto.FactureDirecteCostCenterDTO;
import com.csys.pharmacie.achat.dto.FactureDirecteDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FactureDirecteFactory {

    public static FactureDirecteDTO facturedirecteToFactureDirecteDTO(FactureDirecte facturedirecte, Boolean withoutModeReglement) {
        FactureDirecteDTO facturedirecteDTO = new FactureDirecteDTO();
        facturedirecteDTO.setCodeFournisseur(facturedirecte.getCodeFournisseur());
        facturedirecteDTO.setDateFournisseur(facturedirecte.getDateFournisseur());

        facturedirecteDTO.setReferenceFournisseur(facturedirecte.getReferenceFournisseur());
        facturedirecteDTO.setMontant(facturedirecte.getMontant());

        Set<DetailFactureDirecteDTO> detailFactureDirecteCollectionDtos = new HashSet<>();
        facturedirecte.getDetailFactureDirecteCollection().forEach(x -> {
            DetailFactureDirecteDTO detailfacturedirecteDto = DetailFactureDirecteFactory.detailfacturedirecteToDetailFactureDirecteDTO(x);
            detailFactureDirecteCollectionDtos.add(detailfacturedirecteDto);
        });
        facturedirecteDTO.setDetailFactureDirecteCollection(detailFactureDirecteCollectionDtos);

        List<FactureDirecteCostCenterDTO> costCenters = new ArrayList<>();
        facturedirecte.getCostCenters().forEach(x -> {
            FactureDirecteCostCenterDTO costCenter = FactureDirecteCostCenterFactory.facturedirectecostcenterToFactureDirecteCostCenterDTO(x);
            costCenters.add(costCenter);
        });
        facturedirecteDTO.setCostCenters(costCenters);

        facturedirecteDTO.setNumbon(facturedirecte.getNumbon());
        facturedirecteDTO.setCodvend(facturedirecte.getCodvend());
        facturedirecteDTO.setDatbon(facturedirecte.getDatbon());
        facturedirecteDTO.setDatesys(facturedirecte.getDatesys());
        facturedirecteDTO.setHeuresys(facturedirecte.getHeuresys());
        facturedirecteDTO.setTypbon(facturedirecte.getTypbon());
        facturedirecteDTO.setNumaffiche(facturedirecte.getNumaffiche());
        facturedirecteDTO.setCategDepot(facturedirecte.getCategDepot());
        facturedirecteDTO.setObservation(facturedirecte.getObservation());
        facturedirecteDTO.setUserAnnule(facturedirecte.getUserAnnule());
        facturedirecteDTO.setDateAnnule(facturedirecte.getDateAnnule());
        facturedirecteDTO.setIntegrer(facturedirecte.getIntegrer());
        facturedirecteDTO.setCodeDevise(facturedirecte.getCodeDevise());
        facturedirecteDTO.setTauxDevise(facturedirecte.getTauxDevise());
        facturedirecteDTO.setMontantDevise(facturedirecte.getMontantDevise());
        facturedirecteDTO.setDateBonEdition(java.util.Date.from(facturedirecte.getDatbon().atZone(ZoneId.systemDefault())
                .toInstant()));
        if (facturedirecte.getDateAnnule() != null) {
            facturedirecteDTO.setDateAnnuleEdition(java.util.Date.from(facturedirecte.getDateAnnule().atZone(ZoneId.systemDefault())
                    .toInstant()));
        }
        Instant instant = facturedirecte.getDateFournisseur().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        facturedirecteDTO.setDateFournisseurEdition(Date.from(instant));

        if (Boolean.FALSE.equals(withoutModeReglement) && facturedirecte.getFactureDirecteModeReglement() != null && !facturedirecte.getFactureDirecteModeReglement().isEmpty()) {
            facturedirecteDTO.setModeReglementList(FactureDirecteModeReglementFactory.facturedirectemodereglementsToFactureDirecteModeReglementDTOs(facturedirecte.getFactureDirecteModeReglement()));
        } else {
            facturedirecteDTO.setModeReglementList(new ArrayList<>());
        }
        return facturedirecteDTO;
    }

    public static FactureDirecte facturedirecteDTOToFactureDirecte(FactureDirecteDTO facturedirecteDTO) {
        FactureDirecte facturedirecte = new FactureDirecte();
        facturedirecte.setCodeFournisseur(facturedirecteDTO.getCodeFournisseur());
        facturedirecte.setDateFournisseur(facturedirecteDTO.getDateFournisseur());
        facturedirecte.setReferenceFournisseur(facturedirecteDTO.getReferenceFournisseur());
        facturedirecte.setMontant(facturedirecteDTO.getMontant());
        facturedirecte.setNumbon(facturedirecteDTO.getNumbon());
        Set<DetailFactureDirecte> detailFactureDirecteCollections = new HashSet<>();
        facturedirecteDTO.getDetailFactureDirecteCollection().forEach(x -> {
            DetailFactureDirecte detailfacturedirecte = DetailFactureDirecteFactory.detailfacturedirecteDTOToDetailFactureDirecte(x);
            detailfacturedirecte.setFactureDirecte(facturedirecte);
            detailFactureDirecteCollections.add(detailfacturedirecte);
        });
        facturedirecte.setDetailFactureDirecteCollection(detailFactureDirecteCollections);

        Collection<FactureDirecteCostCenter> costCenters = new ArrayList<>();
        facturedirecteDTO.getCostCenters().forEach(x -> {
            x.setNumeroFactureDirecte(facturedirecteDTO.getNumbon());
            FactureDirecteCostCenter costCenter = FactureDirecteCostCenterFactory.facturedirectecostcenterDTOToFactureDirecteCostCenter(x);
            costCenter.setFactureDirecte(facturedirecte);
            costCenters.add(costCenter);
        });

        facturedirecte.setCostCenters(costCenters);
        facturedirecte.setCodvend(facturedirecteDTO.getCodvend());
        facturedirecte.setDatbon(facturedirecteDTO.getDatbon());
        facturedirecte.setDatesys(facturedirecteDTO.getDatesys());
        facturedirecte.setHeuresys(facturedirecteDTO.getHeuresys());
        facturedirecte.setTypbon(facturedirecteDTO.getTypbon());
        facturedirecte.setNumaffiche(facturedirecteDTO.getNumaffiche());
        facturedirecte.setCategDepot(facturedirecteDTO.getCategDepot());
        facturedirecte.setUserAnnule(facturedirecteDTO.getUserAnnule());
        facturedirecte.setDateAnnule(facturedirecteDTO.getDateAnnule());
        facturedirecte.setObservation(facturedirecteDTO.getObservation());
        facturedirecte.setIntegrer(facturedirecteDTO.getIntegrer());
        facturedirecte.setCodeDevise(facturedirecteDTO.getCodeDevise());
        facturedirecte.setTauxDevise(facturedirecteDTO.getTauxDevise());
        facturedirecte.setMontantDevise(facturedirecteDTO.getMontantDevise());
        Collection<FactureDirecteModeReglement> factureDirecteModeReglements = new ArrayList<>();
        if (facturedirecteDTO.getModeReglementList() != null && !facturedirecteDTO.getModeReglementList().isEmpty()) {
            factureDirecteModeReglements = FactureDirecteModeReglementFactory.factureDirecteModeReglementDTOsToFactureDirecteModeReglements(facturedirecteDTO.getModeReglementList(), facturedirecte);
        }

        if (facturedirecte.getFactureDirecteModeReglement() != null) {
            facturedirecte.getFactureDirecteModeReglement().clear();
            facturedirecte.getFactureDirecteModeReglement().addAll(factureDirecteModeReglements);
        } else {
            facturedirecte.setFactureDirecteModeReglement(factureDirecteModeReglements);
        }

        return facturedirecte;
    }

    public static Collection<FactureDirecteDTO> facturedirecteToFactureDirecteDTOs(Collection<FactureDirecte> facturedirectes) {
        List<FactureDirecteDTO> facturedirectesDTO = new ArrayList<>();
        facturedirectes.forEach(x -> {
            facturedirectesDTO.add(facturedirecteToFactureDirecteDTO(x, Boolean.FALSE));
        });
        return facturedirectesDTO;
    }

    public static FactureDirecteDTO lazyfacturedirecteToFactureDirecteDTO(FactureDirecte facturedirecte) {
        FactureDirecteDTO facturedirecteDTO = new FactureDirecteDTO();
        facturedirecteDTO.setCodeFournisseur(facturedirecte.getCodeFournisseur());
        facturedirecteDTO.setDateFournisseur(facturedirecte.getDateFournisseur());
        facturedirecteDTO.setReferenceFournisseur(facturedirecte.getReferenceFournisseur());
        facturedirecteDTO.setMontant(facturedirecte.getMontant());
        facturedirecteDTO.setNumbon(facturedirecte.getNumbon());
        facturedirecteDTO.setCodvend(facturedirecte.getCodvend());
        facturedirecteDTO.setDatbon(facturedirecte.getDatbon());
        facturedirecteDTO.setDatesys(facturedirecte.getDatesys());
        facturedirecteDTO.setHeuresys(facturedirecte.getHeuresys());
        facturedirecteDTO.setUserAnnule(facturedirecte.getUserAnnule());
        facturedirecteDTO.setDateAnnule(facturedirecte.getDateAnnule());
        facturedirecteDTO.setTypbon(facturedirecte.getTypbon());
        facturedirecteDTO.setNumaffiche(facturedirecte.getNumaffiche());
        facturedirecteDTO.setCategDepot(facturedirecte.getCategDepot());
        facturedirecteDTO.setObservation(facturedirecte.getObservation());
        facturedirecteDTO.setIntegrer(facturedirecte.getIntegrer());
        facturedirecteDTO.setCodeDevise(facturedirecte.getCodeDevise());
        facturedirecteDTO.setTauxDevise(facturedirecte.getTauxDevise());
        facturedirecteDTO.setMontantDevise(facturedirecte.getMontantDevise());
        facturedirecteDTO.setCodeCommandeAchat(facturedirecte.getCodeCommandeAchat());
        facturedirecteDTO.setDateBonEdition(java.util.Date.from(facturedirecte.getDatbon().atZone(ZoneId.systemDefault())
                .toInstant()));
        if (facturedirecte.getDateAnnule() != null) {
            facturedirecteDTO.setDateAnnuleEdition(java.util.Date.from(facturedirecte.getDateAnnule().atZone(ZoneId.systemDefault())
                    .toInstant()));
        }
            Instant instant = facturedirecte.getDateFournisseur().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            facturedirecteDTO.setDateFournisseurEdition(Date.from(instant));

            if (facturedirecte.getFactureDirecteModeReglement() != null && !facturedirecte.getFactureDirecteModeReglement().isEmpty()) {
                facturedirecteDTO.setModeReglementList(FactureDirecteModeReglementFactory.facturedirectemodereglementsToFactureDirecteModeReglementDTOs(facturedirecte.getFactureDirecteModeReglement()));
            } else {
                facturedirecteDTO.setModeReglementList(new ArrayList<>());
            }

            return facturedirecteDTO;
        }

    

    public static Collection<FactureDirecteDTO> lazyfacturedirecteToFactureDirecteDTOs(Collection<FactureDirecte> facturedirectes) {
        List<FactureDirecteDTO> facturedirectesDTO = new ArrayList<>();
        facturedirectes.forEach(x -> {
            facturedirectesDTO.add(lazyfacturedirecteToFactureDirecteDTO(x));
        });
        return facturedirectesDTO;
    }

}
