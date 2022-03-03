package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.BaseTvaReception;
import com.csys.pharmacie.achat.domain.BaseTvaReceptionTemporaire;
import com.csys.pharmacie.achat.dto.BaseTvaReceptionTemporaireDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseTvaReceptionTemporaireFactory {
  public static BaseTvaReceptionTemporaireDTO basetvareceptiontemporaireToBaseTvaReceptionTemporaireDTO(BaseTvaReceptionTemporaire basetvareceptiontemporaire) {
    BaseTvaReceptionTemporaireDTO basetvareceptiontemporaireDTO=new BaseTvaReceptionTemporaireDTO();
    basetvareceptiontemporaireDTO.setCodeTva(basetvareceptiontemporaire.getCodeTva());
    basetvareceptiontemporaireDTO.setTauxTva(basetvareceptiontemporaire.getTauxTva());
    basetvareceptiontemporaireDTO.setBaseTva(basetvareceptiontemporaire.getBaseTva());
    basetvareceptiontemporaireDTO.setMontantTva(basetvareceptiontemporaire.getMontantTva());
    basetvareceptiontemporaireDTO.setCode(basetvareceptiontemporaire.getCode());
    basetvareceptiontemporaireDTO.setMontantTvaGratuite(basetvareceptiontemporaire.getMntTvaGrtauite());
    basetvareceptiontemporaireDTO.setBaseTvaGratuite(basetvareceptiontemporaire.getBaseTvaGratuite());
    basetvareceptiontemporaireDTO.setReception(basetvareceptiontemporaire.getReception());
    return basetvareceptiontemporaireDTO;
  }

  public static BaseTvaReceptionTemporaire basetvareceptiontemporaireDTOToBaseTvaReceptionTemporaire(BaseTvaReceptionTemporaireDTO basetvareceptiontemporaireDTO) {
    BaseTvaReceptionTemporaire basetvareceptiontemporaire=new BaseTvaReceptionTemporaire();
    basetvareceptiontemporaire.setCodeTva(basetvareceptiontemporaireDTO.getCodeTva());
    basetvareceptiontemporaire.setTauxTva(basetvareceptiontemporaireDTO.getTauxTva());
    basetvareceptiontemporaire.setBaseTva(basetvareceptiontemporaireDTO.getBaseTva());
    basetvareceptiontemporaire.setMontantTva(basetvareceptiontemporaireDTO.getMontantTva());
    basetvareceptiontemporaire.setMntTvaGrtauite(basetvareceptiontemporaireDTO.getMontantTvaGratuite());
    basetvareceptiontemporaire.setBaseTvaGratuite(basetvareceptiontemporaireDTO.getBaseTvaGratuite());
    basetvareceptiontemporaire.setReception(basetvareceptiontemporaireDTO.getReception());
    return basetvareceptiontemporaire;
  }

  public static Collection<BaseTvaReceptionTemporaireDTO> basetvareceptiontemporaireToBaseTvaReceptionTemporaireDTOs(Collection<BaseTvaReceptionTemporaire> basetvareceptiontemporaires) {
    List<BaseTvaReceptionTemporaireDTO> basetvareceptiontemporairesDTO=new ArrayList<>();
    basetvareceptiontemporaires.forEach(x -> {
      basetvareceptiontemporairesDTO.add(basetvareceptiontemporaireToBaseTvaReceptionTemporaireDTO(x));
    } );
    return basetvareceptiontemporairesDTO;
  }
  
    public static BaseTvaReception basetvareceptiontemporaireDTOToBaseTvaReception(BaseTvaReceptionTemporaireDTO basetvareceptiontemporaireDTO) {
    BaseTvaReception BaseTvaReception=new BaseTvaReception();
    BaseTvaReception.setCodeTva(basetvareceptiontemporaireDTO.getCodeTva());
    BaseTvaReception.setTauxTva(basetvareceptiontemporaireDTO.getTauxTva());
    BaseTvaReception.setBaseTva(basetvareceptiontemporaireDTO.getBaseTva());
    BaseTvaReception.setMontantTva(basetvareceptiontemporaireDTO.getMontantTva());
    BaseTvaReception.setMontantTvaGratuite(basetvareceptiontemporaireDTO.getMontantTvaGratuite());
    BaseTvaReception.setBaseTvaGratuite(basetvareceptiontemporaireDTO.getBaseTvaGratuite());
    return BaseTvaReception;
  }
}

