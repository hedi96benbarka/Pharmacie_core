package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.FactureDirecte;
import com.csys.pharmacie.achat.domain.FactureDirecteModeReglement;
import com.csys.pharmacie.achat.domain.FactureDirecteModeReglementPK;
import com.csys.pharmacie.achat.dto.FactureDirecteModeReglementDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FactureDirecteModeReglementFactory {
  public static FactureDirecteModeReglementDTO factureDirecteModeReglementToFactureDirecteModeReglementDTO(FactureDirecteModeReglement factureDirecteModeReglement) {
    FactureDirecteModeReglementDTO factureDirecteModeReglementDTO=new FactureDirecteModeReglementDTO();
    factureDirecteModeReglementDTO.setNumBon(factureDirecteModeReglement.getFactureDirecteModeReglementPK().getNumBon());
    factureDirecteModeReglementDTO.setCodeReglement(factureDirecteModeReglement.getFactureDirecteModeReglementPK().getCodeReg());
    factureDirecteModeReglementDTO.setCodeMotifPaiement(factureDirecteModeReglement.getFactureDirecteModeReglementPK().getCodeMotifPaiement());
    factureDirecteModeReglementDTO.setDelaiPaiement(factureDirecteModeReglement.getDelaiPaiement());
    factureDirecteModeReglementDTO.setDelaiValeurPaiement(factureDirecteModeReglement.getDelaiValeurPaiement());
    factureDirecteModeReglementDTO.setPourcentage(factureDirecteModeReglement.getPourcentage());
    return factureDirecteModeReglementDTO;
  }

  public static FactureDirecteModeReglement factureDirecteModeReglementDTOToFactureDirecteModeReglement(FactureDirecteModeReglementDTO factureDirecteModeReglementDTO, FactureDirecte factureDirecte) {
    FactureDirecteModeReglement factureDirecteModeReglement=new FactureDirecteModeReglement();
    factureDirecteModeReglement.setFactureDirecteModeReglementPK(new FactureDirecteModeReglementPK(factureDirecte.getNumbon(), factureDirecteModeReglementDTO.getCodeReglement(), factureDirecteModeReglementDTO.getCodeMotifPaiement()));
    factureDirecteModeReglement.setDelaiPaiement(factureDirecteModeReglementDTO.getDelaiPaiement());
    factureDirecteModeReglement.setDelaiValeurPaiement(factureDirecteModeReglementDTO.getDelaiValeurPaiement());
    factureDirecteModeReglement.setPourcentage(factureDirecteModeReglementDTO.getPourcentage());
    factureDirecteModeReglement.setFactureDirecte(factureDirecte);
    return factureDirecteModeReglement;
  }

  public static Collection<FactureDirecteModeReglementDTO> facturedirectemodereglementsToFactureDirecteModeReglementDTOs(Collection<FactureDirecteModeReglement> facturedirectemodereglements) {
    List<FactureDirecteModeReglementDTO> facturedirectemodereglementsDTO=new ArrayList<>();
    facturedirectemodereglements.forEach(x -> {
      facturedirectemodereglementsDTO.add(factureDirecteModeReglementToFactureDirecteModeReglementDTO(x));
    } );
    return facturedirectemodereglementsDTO;
  }
  
    public static Collection<FactureDirecteModeReglement> factureDirecteModeReglementDTOsToFactureDirecteModeReglements(Collection<FactureDirecteModeReglementDTO> factureDirecteModeReglementDTOs,FactureDirecte factureDirecte){
    List<FactureDirecteModeReglement> factureDirecteModeReglements =  new ArrayList<>();
    factureDirecteModeReglementDTOs.forEach(x -> {
    factureDirecteModeReglements.add(factureDirecteModeReglementDTOToFactureDirecteModeReglement(x, factureDirecte));
    });
         return factureDirecteModeReglements;
    }
}

