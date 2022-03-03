package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCa;
import com.csys.pharmacie.achat.dto.ReceptionTemporaireDetailCaDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReceptionTemporaireDetailCaFactory {
  public static ReceptionTemporaireDetailCaDTO receptiontemporairedetailcaToReceptionTemporaireDetailCaDTO(ReceptionTemporaireDetailCa receptiontemporairedetailca) {
    ReceptionTemporaireDetailCaDTO receptiontemporairedetailcaDTO=new ReceptionTemporaireDetailCaDTO();
    receptiontemporairedetailcaDTO.setReceptionTemporaireDetailCaPK(receptiontemporairedetailca.getReceptionTemporaireDetailCaPK());
    receptiontemporairedetailcaDTO.setQuantiteReceptione(receptiontemporairedetailca.getQuantiteReceptione());
    receptiontemporairedetailcaDTO.setQuantiteGratuite(receptiontemporairedetailca.getQuantiteGratuite());
    receptiontemporairedetailcaDTO.setReceptionTemporaire1(receptiontemporairedetailca.getReceptionTemporaire1());
    return receptiontemporairedetailcaDTO;
  }

  public static ReceptionTemporaireDetailCa receptiontemporairedetailcaDTOToReceptionTemporaireDetailCa(ReceptionTemporaireDetailCaDTO receptiontemporairedetailcaDTO) {
    ReceptionTemporaireDetailCa receptiontemporairedetailca=new ReceptionTemporaireDetailCa();
    receptiontemporairedetailca.setReceptionTemporaireDetailCaPK(receptiontemporairedetailcaDTO.getReceptionTemporaireDetailCaPK());
    receptiontemporairedetailca.setQuantiteReceptione(receptiontemporairedetailcaDTO.getQuantiteReceptione());
    receptiontemporairedetailca.setQuantiteGratuite(receptiontemporairedetailcaDTO.getQuantiteGratuite());
    receptiontemporairedetailca.setReceptionTemporaire1(receptiontemporairedetailcaDTO.getReceptionTemporaire1());
    return receptiontemporairedetailca;
  }

  public static Collection<ReceptionTemporaireDetailCaDTO> receptiontemporairedetailcaToReceptionTemporaireDetailCaDTOs(Collection<ReceptionTemporaireDetailCa> receptiontemporairedetailcas) {
    List<ReceptionTemporaireDetailCaDTO> receptiontemporairedetailcasDTO=new ArrayList<>();
    receptiontemporairedetailcas.forEach(x -> {
      receptiontemporairedetailcasDTO.add(receptiontemporairedetailcaToReceptionTemporaireDetailCaDTO(x));
    } );
    return receptiontemporairedetailcasDTO;
  }
}

