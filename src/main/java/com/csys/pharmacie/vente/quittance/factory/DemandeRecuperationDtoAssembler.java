package com.csys.pharmacie.vente.quittance.factory;

import com.csys.pharmacie.vente.quittance.domain.FactureDR;
import com.csys.pharmacie.vente.quittance.dto.DemandeRecuperationDto;
import com.csys.pharmacie.vente.quittance.dto.PatientDto;

public class DemandeRecuperationDtoAssembler {

    public static DemandeRecuperationDto assembler(FactureDR facturedr) {
        DemandeRecuperationDto demandeRecuperationDto = new DemandeRecuperationDto();
        demandeRecuperationDto.setNumBon(facturedr.getNumbon());
        demandeRecuperationDto.setValide(facturedr.getEtatbon());
        demandeRecuperationDto.setDemandes(MvtstoDRFactory.mvtstodrToDemandeArticlesDtos(facturedr));
        demandeRecuperationDto.setSatisfaction(facturedr.getSatisf());
        PatientDto patient = new PatientDto();
        patient.setNom(facturedr.getRaisoc());
        patient.setPrenom(facturedr.getRaisoc());
        patient.setNumChambre(facturedr.getNumCha());
        patient.setDateNaissance(facturedr.getDateNaissance());
        demandeRecuperationDto.setPatient(patient);
        demandeRecuperationDto.setDateValidation(facturedr.getDatreffrs());
        demandeRecuperationDto.setValidePar(facturedr.getReffrs());
        demandeRecuperationDto.setCoddep(facturedr.getCoddep());
        return demandeRecuperationDto;
    }

}
