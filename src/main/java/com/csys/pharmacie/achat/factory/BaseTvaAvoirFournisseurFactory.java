package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.BaseTvaAvoirFournisseur;
import com.csys.pharmacie.achat.dto.BaseTvaAvoirFournisseurDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseTvaAvoirFournisseurFactory {
  public static BaseTvaAvoirFournisseurDTO basetvaavoirfournisseurToBaseTvaAvoirFournisseurDTO(BaseTvaAvoirFournisseur basetvaavoirfournisseur) {
    BaseTvaAvoirFournisseurDTO basetvaavoirfournisseurDTO=new BaseTvaAvoirFournisseurDTO();
    basetvaavoirfournisseurDTO.setMntTvaGrtauite(basetvaavoirfournisseur.getMntTvaGrtauite());
    basetvaavoirfournisseurDTO.setBaseTvaGratuite(basetvaavoirfournisseur.getBaseTvaGratuite());
    basetvaavoirfournisseurDTO.setCode(basetvaavoirfournisseur.getCode());
    basetvaavoirfournisseurDTO.setCodeTva(basetvaavoirfournisseur.getCodeTva());
    basetvaavoirfournisseurDTO.setTauxTva(basetvaavoirfournisseur.getTauxTva());
    basetvaavoirfournisseurDTO.setBaseTva(basetvaavoirfournisseur.getBaseTva());
    basetvaavoirfournisseurDTO.setMontantTva(basetvaavoirfournisseur.getMontantTva());
    return basetvaavoirfournisseurDTO;
  }

  public static BaseTvaAvoirFournisseur basetvaavoirfournisseurDTOToBaseTvaAvoirFournisseur(BaseTvaAvoirFournisseurDTO basetvaavoirfournisseurDTO) {
    BaseTvaAvoirFournisseur basetvaavoirfournisseur=new BaseTvaAvoirFournisseur();
    basetvaavoirfournisseur.setMntTvaGrtauite(basetvaavoirfournisseurDTO.getMntTvaGrtauite());
    basetvaavoirfournisseur.setBaseTvaGratuite(basetvaavoirfournisseurDTO.getBaseTvaGratuite());
    basetvaavoirfournisseur.setCode(basetvaavoirfournisseurDTO.getCode());
    basetvaavoirfournisseur.setCodeTva(basetvaavoirfournisseurDTO.getCodeTva());
    basetvaavoirfournisseur.setTauxTva(basetvaavoirfournisseurDTO.getTauxTva());
    basetvaavoirfournisseur.setBaseTva(basetvaavoirfournisseurDTO.getBaseTva());
    basetvaavoirfournisseur.setMontantTva(basetvaavoirfournisseurDTO.getMontantTva());
    return basetvaavoirfournisseur;
  }

  public static List<BaseTvaAvoirFournisseurDTO> basetvaavoirfournisseurToBaseTvaAvoirFournisseurDTOs(List<BaseTvaAvoirFournisseur> basetvaavoirfournisseurs) {
    List<BaseTvaAvoirFournisseurDTO> basetvaavoirfournisseursDTO=new ArrayList<>();
    basetvaavoirfournisseurs.forEach(x -> {
      basetvaavoirfournisseursDTO.add(basetvaavoirfournisseurToBaseTvaAvoirFournisseurDTO(x));
    } );
    return basetvaavoirfournisseursDTO;
  }
}

