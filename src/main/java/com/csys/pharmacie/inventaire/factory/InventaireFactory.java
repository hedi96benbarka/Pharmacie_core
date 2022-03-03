package com.csys.pharmacie.inventaire.factory;

import com.csys.pharmacie.inventaire.domain.DetailInventaire;
import com.csys.pharmacie.inventaire.domain.Inventaire;
import com.csys.pharmacie.inventaire.dto.DetailInventaireDTO;
import com.csys.pharmacie.inventaire.dto.InventaireDTO;
import java.util.ArrayList;
import java.util.List;

public class InventaireFactory {
  public static InventaireDTO inventaireToInventaireDTO(Inventaire inventaire) {
    InventaireDTO inventaireDTO=new InventaireDTO();
    inventaireDTO.setCode(inventaire.getCode());
    inventaireDTO.setDepot(inventaire.getDepot());
    inventaireDTO.setCategorieDepot(inventaire.getCategorieDepot());
    inventaireDTO.setDateOuverture(inventaire.getDateOuverture());
    inventaireDTO.setUserOuverture(inventaire.getUserOuverture());
    inventaireDTO.setDateCloture(inventaire.getDateCloture());
    inventaireDTO.setUserCloture(inventaire.getUserCloture());
    inventaireDTO.setCategorieArticleParent(inventaire.getCategorieArticleParent());
    inventaireDTO.setCodeSaisie(inventaire.getCodeSaisie());
    inventaireDTO.setUserAnnule(inventaire.getUserAnnule());
    inventaireDTO.setDateAnnule(inventaire.getDateAnnule());
    inventaireDTO.setIsDemarrage(inventaire.getIsDemarrage());
    List<DetailInventaireDTO> detailInventaireCollectionDtos = new ArrayList<>();
    inventaire.getDetailInventaireCollection().forEach(x -> {
      DetailInventaireDTO detailinventaireDto = new DetailInventaireDTO();
      detailinventaireDto = DetailInventaireFactory.detailinventaireToDetailInventaireDTO(x);
      detailInventaireCollectionDtos.add(detailinventaireDto);
    } );
    if(inventaireDTO.getDetailInventaireCollection() !=null) {
      inventaireDTO.getDetailInventaireCollection().clear();
      inventaireDTO.getDetailInventaireCollection().addAll(detailInventaireCollectionDtos);
    }
    else {
      inventaireDTO.setDetailInventaireCollection(detailInventaireCollectionDtos);
    }
    return inventaireDTO;
  }

  public static Inventaire inventaireDTOToInventaire(InventaireDTO inventaireDTO) {
    Inventaire inventaire=new Inventaire();
    inventaire.setCode(inventaireDTO.getCode());
    inventaire.setDepot(inventaireDTO.getDepot());
    inventaire.setCategorieDepot(inventaireDTO.getCategorieDepot());
    inventaire.setDateOuverture(inventaireDTO.getDateOuverture());
    inventaire.setUserOuverture(inventaireDTO.getUserOuverture());
    inventaire.setDateCloture(inventaireDTO.getDateCloture());
    inventaire.setUserCloture(inventaireDTO.getUserCloture());
    inventaire.setCategorieArticleParent(inventaireDTO.getCategorieArticleParent());
    List<DetailInventaire> detailInventaireCollections = new ArrayList<>();
    inventaireDTO.getDetailInventaireCollection().forEach(x -> {
      DetailInventaire detailinventaire = new DetailInventaire();
      detailinventaire = DetailInventaireFactory.detailinventaireDTOToDetailInventaire(x);
      detailInventaireCollections.add(detailinventaire);
    } );
    if(inventaire.getDetailInventaireCollection() !=null) {
      inventaire.getDetailInventaireCollection().clear();
      inventaire.getDetailInventaireCollection().addAll(detailInventaireCollections);
    }
    else {
      inventaire.setDetailInventaireCollection(detailInventaireCollections);
    }
    return inventaire;
  }

  public static List<InventaireDTO> inventaireToInventaireDTOs(List<Inventaire> inventaires) {
    List<InventaireDTO> inventairesDTO=new ArrayList<>();
    inventaires.forEach(x -> {
      inventairesDTO.add(inventaireToInventaireDTO(x));
    } );
    return inventairesDTO;
  }

  public static InventaireDTO lazyinventaireToInventaireDTO(Inventaire inventaire) {
    InventaireDTO inventaireDTO=new InventaireDTO();
    inventaireDTO.setCode(inventaire.getCode());
    inventaireDTO.setDepot(inventaire.getDepot());
    inventaireDTO.setCategorieDepot(inventaire.getCategorieDepot());
    inventaireDTO.setDateOuverture(inventaire.getDateOuverture());
    inventaireDTO.setUserOuverture(inventaire.getUserOuverture());
    inventaireDTO.setDateCloture(inventaire.getDateCloture());
    inventaireDTO.setCategorieArticleParent(inventaire.getCategorieArticleParent());
    inventaireDTO.setUserCloture(inventaire.getUserCloture());
    inventaireDTO.setUserAnnule(inventaire.getUserAnnule());
    inventaireDTO.setDateAnnule(inventaire.getDateAnnule());
    return inventaireDTO;
  }

  public static List<InventaireDTO> lazyinventaireToInventaireDTOs(List<Inventaire> inventaires) {
    List<InventaireDTO> inventairesDTO=new ArrayList<>();
    inventaires.forEach(x -> {
      inventairesDTO.add(lazyinventaireToInventaireDTO(x));
    } );
    return inventairesDTO;
  }
}

