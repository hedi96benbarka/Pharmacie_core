package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.AjustementRetourPerime;
import com.csys.pharmacie.achat.dto.AjustementRetourPerimeDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AjustementRetourPerimeFactory {
  public static AjustementRetourPerimeDTO ajustementretourperimeToAjustementRetourPerimeDTO(AjustementRetourPerime ajustementretourperime) {
    AjustementRetourPerimeDTO ajustementretourperimeDTO=new AjustementRetourPerimeDTO();
//    ajustementretourperimeDTO.setAjustementRetourPerimePK(ajustementretourperime.getAjustementRetourPerimePK());
    ajustementretourperimeDTO.setCodeDepot(ajustementretourperime.getCodeDepot());
    ajustementretourperimeDTO.setDiffMntHt(ajustementretourperime.getDiffMntHt());
    ajustementretourperimeDTO.setDiffMntTtc(ajustementretourperime.getDiffMntTtc());
    ajustementretourperimeDTO.setRetourPerime(ajustementretourperime.getRetourPerime());
    return ajustementretourperimeDTO;
  }

  public static AjustementRetourPerime ajustementretourperimeDTOToAjustementRetourPerime(AjustementRetourPerimeDTO ajustementretourperimeDTO) {
    AjustementRetourPerime ajustementretourperime=new AjustementRetourPerime();
//    ajustementretourperime.setAjustementRetourPerimePK(ajustementretourperimeDTO.getAjustementRetourPerimePK());
    ajustementretourperime.setCodeDepot(ajustementretourperimeDTO.getCodeDepot());
    ajustementretourperime.setDiffMntHt(ajustementretourperimeDTO.getDiffMntHt());
    ajustementretourperime.setDiffMntTtc(ajustementretourperimeDTO.getDiffMntTtc());
    ajustementretourperime.setRetourPerime(ajustementretourperimeDTO.getRetourPerime());
    return ajustementretourperime;
  }

  public static Collection<AjustementRetourPerimeDTO> ajustementretourperimeToAjustementRetourPerimeDTOs(Collection<AjustementRetourPerime> ajustementretourperimes) {
    List<AjustementRetourPerimeDTO> ajustementretourperimesDTO=new ArrayList<>();
    ajustementretourperimes.forEach(x -> {
      ajustementretourperimesDTO.add(ajustementretourperimeToAjustementRetourPerimeDTO(x));
    } );
    return ajustementretourperimesDTO;
  }
}

