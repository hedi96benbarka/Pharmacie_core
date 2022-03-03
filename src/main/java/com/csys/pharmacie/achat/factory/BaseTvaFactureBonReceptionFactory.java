package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.BaseTvaFactureBonReception;
import com.csys.pharmacie.achat.dto.BaseTvaFactureBonReceptionDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseTvaFactureBonReceptionFactory {
  public static BaseTvaFactureBonReceptionDTO basetvafacturebonreceptionToBaseTvaFactureBonReceptionDTO(BaseTvaFactureBonReception basetvafacturebonreception) {
    BaseTvaFactureBonReceptionDTO basetvafacturebonreceptionDTO=new BaseTvaFactureBonReceptionDTO();
    basetvafacturebonreceptionDTO.setCodeTva(basetvafacturebonreception.getCodeTva());
    basetvafacturebonreceptionDTO.setTauxTva(basetvafacturebonreception.getTauxTva());
    basetvafacturebonreceptionDTO.setBaseTva(basetvafacturebonreception.getBaseTva());
    basetvafacturebonreceptionDTO.setMontantTva(basetvafacturebonreception.getMontantTva());
    basetvafacturebonreceptionDTO.setCode(basetvafacturebonreception.getCode());
    basetvafacturebonreceptionDTO.setBaseTvaGratuite(basetvafacturebonreception.getBaseTvaGratuite());
    basetvafacturebonreceptionDTO.setMontantTvaGratuite(basetvafacturebonreception.getMontantTvaGratuite());
     return basetvafacturebonreceptionDTO;
  }

  public static BaseTvaFactureBonReception basetvafacturebonreceptionDTOToBaseTvaFactureBonReception(BaseTvaFactureBonReceptionDTO basetvafacturebonreceptionDTO) {
    BaseTvaFactureBonReception basetvafacturebonreception=new BaseTvaFactureBonReception();
    basetvafacturebonreception.setCodeTva(basetvafacturebonreceptionDTO.getCodeTva());
    basetvafacturebonreception.setTauxTva(basetvafacturebonreceptionDTO.getTauxTva());
    basetvafacturebonreception.setBaseTva(basetvafacturebonreceptionDTO.getBaseTva());
    basetvafacturebonreception.setMontantTva(basetvafacturebonreceptionDTO.getMontantTva());
    basetvafacturebonreception.setCode(basetvafacturebonreceptionDTO.getCode());
    basetvafacturebonreception.setBaseTvaGratuite(basetvafacturebonreceptionDTO.getBaseTvaGratuite());
    basetvafacturebonreception.setMontantTvaGratuite(basetvafacturebonreceptionDTO.getMontantTvaGratuite());
     return basetvafacturebonreception;
  }

  public static Collection<BaseTvaFactureBonReceptionDTO> basetvafacturebonreceptionToBaseTvaFactureBonReceptionDTOs(Collection<BaseTvaFactureBonReception> basetvafacturebonreceptions) {
    List<BaseTvaFactureBonReceptionDTO> basetvafacturebonreceptionsDTO=new ArrayList<>();
    basetvafacturebonreceptions.forEach(x -> {
      basetvafacturebonreceptionsDTO.add(basetvafacturebonreceptionToBaseTvaFactureBonReceptionDTO(x));
    } );
    return basetvafacturebonreceptionsDTO;
  }
}

