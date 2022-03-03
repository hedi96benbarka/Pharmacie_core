package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.ArticleFournisseur;
import com.csys.pharmacie.achat.dto.ArticleFournisseurDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArticleFournisseurFactory {
  public static ArticleFournisseurDTO articlefournisseurToArticleFournisseurDTO(ArticleFournisseur articlefournisseur) {
    ArticleFournisseurDTO articlefournisseurDTO=new ArticleFournisseurDTO();
    articlefournisseurDTO.setArticleFournisseurPK(articlefournisseur.getArticleFournisseurPK());
    articlefournisseurDTO.setMaxPrixAchat(articlefournisseur.getMaxPrixAchat());
    articlefournisseurDTO.setMinPrixAchat(articlefournisseur.getMinPrixAchat());
    articlefournisseurDTO.setPrixAchat(articlefournisseur.getPrixAchat());
    articlefournisseurDTO.setDelaiLivraison(articlefournisseur.getDelaiLivraison());
    articlefournisseurDTO.setCodeArtFrs(articlefournisseur.getCodeArtFrs());
    return articlefournisseurDTO;
  }

  public static ArticleFournisseur articlefournisseurDTOToArticleFournisseur(ArticleFournisseurDTO articlefournisseurDTO) {
    ArticleFournisseur articlefournisseur=new ArticleFournisseur();
    articlefournisseur.setArticleFournisseurPK(articlefournisseurDTO.getArticleFournisseurPK());
    articlefournisseur.setMaxPrixAchat(articlefournisseurDTO.getMaxPrixAchat());
    articlefournisseur.setMinPrixAchat(articlefournisseurDTO.getMinPrixAchat());
    articlefournisseur.setPrixAchat(articlefournisseurDTO.getPrixAchat());
    articlefournisseur.setDelaiLivraison(articlefournisseurDTO.getDelaiLivraison());
    articlefournisseur.setCodeArtFrs(articlefournisseurDTO.getCodeArtFrs());
    return articlefournisseur;
  }

  public static Collection<ArticleFournisseurDTO> articlefournisseurToArticleFournisseurDTOs(Collection<ArticleFournisseur> articlefournisseurs) {
    List<ArticleFournisseurDTO> articlefournisseursDTO=new ArrayList<>();
    articlefournisseurs.forEach(x -> {
      articlefournisseursDTO.add(articlefournisseurToArticleFournisseurDTO(x));
    } );
    return articlefournisseursDTO;
  }
}

