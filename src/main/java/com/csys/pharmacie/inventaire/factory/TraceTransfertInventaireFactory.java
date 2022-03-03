package com.csys.pharmacie.inventaire.factory;

import com.csys.pharmacie.inventaire.domain.TraceTransfertInventaire;
import com.csys.pharmacie.inventaire.dto.TraceTransfertInventaireDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TraceTransfertInventaireFactory {
  public static TraceTransfertInventaireDTO tracetransfertinventaireToTraceTransfertInventaireDTO(TraceTransfertInventaire tracetransfertinventaire) {
    TraceTransfertInventaireDTO tracetransfertinventaireDTO=new TraceTransfertInventaireDTO();
    tracetransfertinventaireDTO.setCode(tracetransfertinventaire.getCode());
    tracetransfertinventaireDTO.setUser(tracetransfertinventaire.getUser());
    tracetransfertinventaireDTO.setDate(tracetransfertinventaire.getDate());
    return tracetransfertinventaireDTO;
  }

  public static TraceTransfertInventaire tracetransfertinventaireDTOToTraceTransfertInventaire(TraceTransfertInventaireDTO tracetransfertinventaireDTO) {
    TraceTransfertInventaire tracetransfertinventaire=new TraceTransfertInventaire();
    tracetransfertinventaire.setCode(tracetransfertinventaireDTO.getCode());
    tracetransfertinventaire.setUser(tracetransfertinventaireDTO.getUser());
    tracetransfertinventaire.setDate(tracetransfertinventaireDTO.getDate());
     return tracetransfertinventaire;
  }

  public static List<TraceTransfertInventaireDTO> traceTransfertInventaireToTraceTransfertInventaireDTOs(List<TraceTransfertInventaire> tracetransfertinventaires) {
    List<TraceTransfertInventaireDTO> tracetransfertinventairesDTO=new ArrayList<>();
    tracetransfertinventaires.forEach(x -> {
      tracetransfertinventairesDTO.add(tracetransfertinventaireToTraceTransfertInventaireDTO(x));
    } );
    return tracetransfertinventairesDTO;
  }
}

