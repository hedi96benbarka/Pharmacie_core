package com.csys.pharmacie.vente.quittance.factory;

import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.vente.quittance.domain.FactureDR;
import com.csys.pharmacie.vente.quittance.dto.ListDemandeRecuperationDto;
import com.csys.pharmacie.vente.quittance.dto.LitDTO;
import java.util.stream.Collectors;

public class ListDemandeRecuperationDtoAssembler {
    
    public static ListDemandeRecuperationDto assembler(FactureDR facturedr, DepotDTO depot, LitDTO lit) {
        ListDemandeRecuperationDto listDemandeRecuperationDto = new ListDemandeRecuperationDto();
        listDemandeRecuperationDto.setNumBon(facturedr.getNumbon());
        listDemandeRecuperationDto.setDate(facturedr.getDatbon());
        listDemandeRecuperationDto.setNumDoss(facturedr.getNumdoss());
        listDemandeRecuperationDto.setNumChambre(facturedr.getNumCha());
        listDemandeRecuperationDto.setNomPatient(facturedr.getRaisoc());
        listDemandeRecuperationDto.setValide(facturedr.getEtatbon());
        listDemandeRecuperationDto.setUserCreate(facturedr.getCodvend());
        listDemandeRecuperationDto.setDateNaissance(facturedr.getDateNaissance());
        listDemandeRecuperationDto.setSexe(facturedr.getSexe());
        listDemandeRecuperationDto.setUser(facturedr.getCodAnnul());
        listDemandeRecuperationDto.setDateSup(facturedr.getDatAnnul());
        listDemandeRecuperationDto.setSatisfaction(facturedr.getSatisf());
        listDemandeRecuperationDto.setLit(lit);
        listDemandeRecuperationDto.setDesignationDepotSec(depot.getDesignationSec());
        listDemandeRecuperationDto.setNumQuittance(facturedr.getFactures().stream().map(x -> {
            return x.getNumbon();
        }).collect(Collectors.toList()));
        return listDemandeRecuperationDto;
    }
    
}
