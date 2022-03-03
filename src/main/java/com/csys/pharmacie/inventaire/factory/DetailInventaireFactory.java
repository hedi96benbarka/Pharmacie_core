package com.csys.pharmacie.inventaire.factory;

import com.csys.pharmacie.inventaire.domain.DetailInventaire;
import com.csys.pharmacie.inventaire.dto.DetailInventaireDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DetailInventaireFactory {
  public static DetailInventaireDTO detailinventaireToDetailInventaireDTO(DetailInventaire detailinventaire) {
    DetailInventaireDTO detailinventaireDTO=new DetailInventaireDTO();
    detailinventaireDTO.setDetailInventairePK(detailinventaire.getDetailInventairePK());
    return detailinventaireDTO;
  }

  public static DetailInventaire detailinventaireDTOToDetailInventaire(DetailInventaireDTO detailinventaireDTO) {
    DetailInventaire detailinventaire=new DetailInventaire();
    detailinventaire.setDetailInventairePK(detailinventaireDTO.getDetailInventairePK());
    return detailinventaire;
  }

  public static Collection<DetailInventaireDTO> detailinventaireToDetailInventaireDTOs(Collection<DetailInventaire> detailinventaires) {
    List<DetailInventaireDTO> detailinventairesDTO=new ArrayList<>();
    detailinventaires.forEach(x -> {
      detailinventairesDTO.add(detailinventaireToDetailInventaireDTO(x));
    } );
    return detailinventairesDTO;
  }
}

