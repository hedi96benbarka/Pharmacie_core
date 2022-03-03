package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.DetailMvtStoBA;
import com.csys.pharmacie.achat.dto.DetailMvtStoBADTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DetailMvtStoBAFactory {
  public static DetailMvtStoBADTO detailmvtstobaToDetailMvtStoBADTO(DetailMvtStoBA detailmvtstoba) {
    DetailMvtStoBADTO detailmvtstobaDTO=new DetailMvtStoBADTO();
    detailmvtstobaDTO.setPk(detailmvtstoba.getPk());
    detailmvtstobaDTO.setQuantiteDisponible(detailmvtstoba.getQuantiteDisponible());
    detailmvtstobaDTO.setQuantite_retourne(detailmvtstoba.getQuantite_retourne());
    detailmvtstobaDTO.setNumbonDepsto(detailmvtstoba.getNumbonDepsto());
    detailmvtstobaDTO.setLotinter(detailmvtstoba.getLotinter());
    detailmvtstobaDTO.setDatPer(detailmvtstoba.getDatPer());
    detailmvtstobaDTO.setPriuni(detailmvtstoba.getPriuni());
    detailmvtstobaDTO.setUnite(detailmvtstoba.getUnite());
    detailmvtstobaDTO.setDatSYS(detailmvtstoba.getDatSYS());
    detailmvtstobaDTO.setStkRel(detailmvtstoba.getStkRel());
//    detailmvtstobaDTO.setDepsto(detailmvtstoba.getDepsto());
    return detailmvtstobaDTO;
  }
    
  public static DetailMvtStoBA detailmvtstobaDTOToDetailMvtStoBA(DetailMvtStoBADTO detailmvtstobaDTO) {
    DetailMvtStoBA detailmvtstoba=new DetailMvtStoBA();
    detailmvtstoba.setPk(detailmvtstobaDTO.getPk());
    detailmvtstoba.setQuantiteDisponible(detailmvtstobaDTO.getQuantiteDisponible());
    detailmvtstoba.setQuantite_retourne(detailmvtstobaDTO.getQuantite_retourne());
    detailmvtstoba.setNumbonDepsto(detailmvtstobaDTO.getNumbonDepsto());
    detailmvtstoba.setLotinter(detailmvtstobaDTO.getLotinter());
    detailmvtstoba.setDatPer(detailmvtstobaDTO.getDatPer());
    detailmvtstoba.setPriuni(detailmvtstobaDTO.getPriuni());
    detailmvtstoba.setUnite(detailmvtstobaDTO.getUnite());
    detailmvtstoba.setDatSYS(detailmvtstobaDTO.getDatSYS());
    detailmvtstoba.setStkRel(detailmvtstobaDTO.getStkRel());
//    detailmvtstoba.setDepsto(detailmvtstobaDTO.getDepsto());
    return detailmvtstoba;
}

  public static Collection<DetailMvtStoBADTO> detailmvtstobaToDetailMvtStoBADTOs(Collection<DetailMvtStoBA> detailmvtstobas) {
    List<DetailMvtStoBADTO> detailmvtstobasDTO=new ArrayList<>();
    detailmvtstobas.forEach(x -> {
      detailmvtstobasDTO.add(detailmvtstobaToDetailMvtStoBADTO(x));
    } );
    return detailmvtstobasDTO;
  }
}

