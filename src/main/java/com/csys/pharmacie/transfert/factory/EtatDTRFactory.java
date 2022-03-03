package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.transfert.domain.EtatDTR;
import com.csys.pharmacie.transfert.dto.EtatDTRDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EtatDTRFactory {
  public static EtatDTRDTO etatdtrToEtatDTRDTO(EtatDTR etatdtr) {
    EtatDTRDTO etatdtrDTO=new EtatDTRDTO();
    etatdtrDTO.setCodedtr(etatdtr.getCodedtr());
    etatdtrDTO.setEtat(etatdtr.getEtat());
    return etatdtrDTO;
  }

  public static EtatDTR etatdtrDTOToEtatDTR(EtatDTRDTO etatdtrDTO) {
    EtatDTR etatdtr=new EtatDTR();
    etatdtr.setCodedtr(etatdtrDTO.getCodedtr());
    etatdtr.setEtat(etatdtrDTO.getEtat());
    return etatdtr;
  }

  public static Collection<EtatDTRDTO> etatdtrToEtatDTRDTOs(Collection<EtatDTR> etatdtrs) {
    List<EtatDTRDTO> etatdtrsDTO=new ArrayList<>();
    etatdtrs.forEach(x -> {
      etatdtrsDTO.add(etatdtrToEtatDTRDTO(x));
    } );
    return etatdtrsDTO;
  }   
  
  
    public static List<EtatDTRDTO> etatdTrToEtatDTRDTOsLIST(List<EtatDTR> etatdtrs) {
    List<EtatDTRDTO> etatdtrsDTO=new ArrayList<>();
    etatdtrs.forEach(x -> {
      etatdtrsDTO.add(etatdtrToEtatDTRDTO(x));
    } );
    return etatdtrsDTO;
  }
  
  
}

