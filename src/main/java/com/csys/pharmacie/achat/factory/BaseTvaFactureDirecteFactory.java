package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.BaseTvaFactureDirecte;
import com.csys.pharmacie.achat.dto.BaseTvaFactureDirecteDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseTvaFactureDirecteFactory {
  public static BaseTvaFactureDirecteDTO basetvafacturedirecteToBaseTvaFactureDirecteDTO(BaseTvaFactureDirecte basetvafacturedirecte) {
    BaseTvaFactureDirecteDTO basetvafacturedirecteDTO=new BaseTvaFactureDirecteDTO();
    basetvafacturedirecteDTO.setCodeTva(basetvafacturedirecte.getCodeTva());
    basetvafacturedirecteDTO.setTauxTva(basetvafacturedirecte.getTauxTva());
    basetvafacturedirecteDTO.setBaseTva(basetvafacturedirecte.getBaseTva());
    basetvafacturedirecteDTO.setMontantTva(basetvafacturedirecte.getMontantTva());
    basetvafacturedirecteDTO.setCode(basetvafacturedirecte.getCode()); 
    return basetvafacturedirecteDTO;
  }

  public static BaseTvaFactureDirecte basetvafacturedirecteDTOToBaseTvaFactureDirecte(BaseTvaFactureDirecteDTO basetvafacturedirecteDTO) {
    BaseTvaFactureDirecte basetvafacturedirecte=new BaseTvaFactureDirecte();
    basetvafacturedirecte.setCodeTva(basetvafacturedirecteDTO.getCodeTva());
    basetvafacturedirecte.setTauxTva(basetvafacturedirecteDTO.getTauxTva());
    basetvafacturedirecte.setBaseTva(basetvafacturedirecteDTO.getBaseTva());
    basetvafacturedirecte.setMontantTva(basetvafacturedirecteDTO.getMontantTva());
    basetvafacturedirecte.setCode(basetvafacturedirecteDTO.getCode()); 
    return basetvafacturedirecte;
  }

  public static Collection<BaseTvaFactureDirecteDTO> basetvafacturedirecteToBaseTvaFactureDirecteDTOs(Collection<BaseTvaFactureDirecte> basetvafacturedirectes) {
    List<BaseTvaFactureDirecteDTO> basetvafacturedirectesDTO=new ArrayList<>();
    basetvafacturedirectes.forEach(x -> {
      basetvafacturedirectesDTO.add(basetvafacturedirecteToBaseTvaFactureDirecteDTO(x));
    } );
    return basetvafacturedirectesDTO;
  }
}

