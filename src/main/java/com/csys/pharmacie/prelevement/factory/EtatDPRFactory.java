package com.csys.pharmacie.prelevement.factory;

import com.csys.pharmacie.prelevement.domain.EtatDPR;
import com.csys.pharmacie.prelevement.dto.EtatDPRDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EtatDPRFactory {
  public static EtatDPRDTO etatdprToEtatDPRDTO(EtatDPR etatdpr) {
    EtatDPRDTO etatdprDTO=new EtatDPRDTO();
    etatdprDTO.setCodedpr(etatdpr.getCodedpr());
    etatdprDTO.setEtat(etatdpr.getEtat());
    return etatdprDTO;
  }

  public static EtatDPR etatdprDTOToEtatDPR(EtatDPRDTO etatdprDTO) {
    EtatDPR etatdpr=new EtatDPR();
    etatdpr.setCodedpr(etatdprDTO.getCodedpr());
    etatdpr.setEtat(etatdprDTO.getEtat());
    return etatdpr;
  }

  public static List<EtatDPRDTO> etatdprToEtatDPRDTOs(List<EtatDPR> etatdprs) {
    List<EtatDPRDTO> etatdprsDTO=new ArrayList<>();
    etatdprs.forEach(x -> {
      etatdprsDTO.add(etatdprToEtatDPRDTO(x));
    } );
    return etatdprsDTO;
  }
}

