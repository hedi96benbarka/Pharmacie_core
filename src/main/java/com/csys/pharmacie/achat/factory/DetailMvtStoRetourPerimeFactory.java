package com.csys.pharmacie.achat.factory;


import com.csys.pharmacie.achat.domain.DetailMvtStoRetourPerime;
import com.csys.pharmacie.achat.dto.DetailMvtStoRetourPerimeDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DetailMvtStoRetourPerimeFactory {
  public static DetailMvtStoRetourPerimeDTO detailMvtstoretourperimeToDetailMvtStoRetourPerimeDTO(DetailMvtStoRetourPerime detail_mvtstoretourperime) {
    DetailMvtStoRetourPerimeDTO detail_mvtstoretourperimeDTO=new DetailMvtStoRetourPerimeDTO();

    detail_mvtstoretourperimeDTO.setMvtstoRetour_perime(detail_mvtstoretourperime.getMvtstoRetourPerime());
    detail_mvtstoretourperimeDTO.setCode(detail_mvtstoretourperime.getCode());
    detail_mvtstoretourperimeDTO.setCodeMvtSto(detail_mvtstoretourperime.getCodeMvtSto());
    detail_mvtstoretourperimeDTO.setCodeDepsto(detail_mvtstoretourperime.getCodeDepsto());
    detail_mvtstoretourperimeDTO.setQuantiteDisponible(detail_mvtstoretourperime.getQuantiteDisponible());
    detail_mvtstoretourperimeDTO.setQuantitePrelevee(detail_mvtstoretourperime.getQuantitePrelevee());
    detail_mvtstoretourperimeDTO.setNumbon(detail_mvtstoretourperime.getNumbon());
    detail_mvtstoretourperimeDTO.setLotinter(detail_mvtstoretourperime.getLotinter());
    detail_mvtstoretourperimeDTO.setDatPer(detail_mvtstoretourperime.getDatPer());
    detail_mvtstoretourperimeDTO.setPriuni(detail_mvtstoretourperime.getPriuni());
    detail_mvtstoretourperimeDTO.setUnite(detail_mvtstoretourperime.getUnite());
    detail_mvtstoretourperimeDTO.setDatSYS(detail_mvtstoretourperime.getDatSYS());
    detail_mvtstoretourperimeDTO.setStkRel(detail_mvtstoretourperime.getStkRel());
    return detail_mvtstoretourperimeDTO;
  }

  public static DetailMvtStoRetourPerime detailMvtstoretourperimeDTOToDetailMvtStoRetourPerime(DetailMvtStoRetourPerimeDTO detail_mvtstoretourperimeDTO) {
    DetailMvtStoRetourPerime detail_mvtstoretourperime=new DetailMvtStoRetourPerime();

    detail_mvtstoretourperime.setMvtstoRetourPerime(detail_mvtstoretourperimeDTO.getMvtstoRetour_perime());
    detail_mvtstoretourperime.setCode(detail_mvtstoretourperimeDTO.getCode());
    detail_mvtstoretourperime.setCodeMvtSto(detail_mvtstoretourperimeDTO.getCodeMvtSto());
    detail_mvtstoretourperime.setCodeDepsto(detail_mvtstoretourperimeDTO.getCodeDepsto());
    detail_mvtstoretourperime.setQuantiteDisponible(detail_mvtstoretourperimeDTO.getQuantiteDisponible());
    detail_mvtstoretourperime.setQuantitePrelevee(detail_mvtstoretourperimeDTO.getQuantitePrelevee());
    detail_mvtstoretourperime.setNumbon(detail_mvtstoretourperimeDTO.getNumbon());
    detail_mvtstoretourperime.setLotinter(detail_mvtstoretourperimeDTO.getLotinter());
    detail_mvtstoretourperime.setDatPer(detail_mvtstoretourperimeDTO.getDatPer());
    detail_mvtstoretourperime.setPriuni(detail_mvtstoretourperimeDTO.getPriuni());
    detail_mvtstoretourperime.setUnite(detail_mvtstoretourperimeDTO.getUnite());
    detail_mvtstoretourperime.setDatSYS(detail_mvtstoretourperimeDTO.getDatSYS());
    detail_mvtstoretourperime.setStkRel(detail_mvtstoretourperimeDTO.getStkRel());
    return detail_mvtstoretourperime;
  }

  public static Collection<DetailMvtStoRetourPerimeDTO> detail_mvtstoretourperimeToDetail_MvtStoRetourPerimeDTOs(Collection<DetailMvtStoRetourPerime> detail_mvtstoretourperimes) {
    List<DetailMvtStoRetourPerimeDTO> detail_mvtstoretourperimesDTO=new ArrayList<>();
    detail_mvtstoretourperimes.forEach(x -> {
      detail_mvtstoretourperimesDTO.add(detailMvtstoretourperimeToDetailMvtStoRetourPerimeDTO(x));
    } );
    return detail_mvtstoretourperimesDTO;
  }
}

