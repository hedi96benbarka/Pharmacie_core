//package com.csys.pharmacie.achat.service;
//
//import com.csys.pharmacie.achat.domain.ArticleFournisseur;
//import com.csys.pharmacie.achat.domain.ArticleFournisseurPK;
//import com.csys.pharmacie.achat.dto.ArticleFournisseurDTO;
//import com.csys.pharmacie.achat.factory.ArticleFournisseurFactory;
//import com.csys.pharmacie.achat.repository.ArticleFournisseurRepository;
//import com.google.common.base.Preconditions;
//import java.util.Collection;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Service Implementation for managing ArticleFournisseur.
// */
//@Service
//@Transactional
//public class ArticleFournisseurService {
//  private final Logger log = LoggerFactory.getLogger(ArticleFournisseurService.class);
//
//  private final ArticleFournisseurRepository articlefournisseurRepository;
//
//  public ArticleFournisseurService(ArticleFournisseurRepository articlefournisseurRepository) {
//    this.articlefournisseurRepository=articlefournisseurRepository;
//  }
//
//  /**
//   * Save a articlefournisseurDTO.
//   *
//   * @param articlefournisseurDTO
//   * @return the persisted entity
//   */
//  public ArticleFournisseurDTO save(ArticleFournisseurDTO articlefournisseurDTO) {
//    log.debug("Request to save ArticleFournisseur: {}",articlefournisseurDTO);
//    ArticleFournisseur articlefournisseur = ArticleFournisseurFactory.articlefournisseurDTOToArticleFournisseur(articlefournisseurDTO);
//    articlefournisseur = articlefournisseurRepository.save(articlefournisseur);
//    ArticleFournisseurDTO resultDTO = ArticleFournisseurFactory.articlefournisseurToArticleFournisseurDTO(articlefournisseur);
//    return resultDTO;
//  }
//
//  /**
//   * Update a articlefournisseurDTO.
//   *
//   * @param articlefournisseurDTO
//   * @return the updated entity
//   */
//  public ArticleFournisseurDTO update(ArticleFournisseurDTO articlefournisseurDTO) {
//    log.debug("Request to update ArticleFournisseur: {}",articlefournisseurDTO);
//    ArticleFournisseur inBase= articlefournisseurRepository.findOne(articlefournisseurDTO.getArticleFournisseurPK());
//    Preconditions.checkArgument(inBase != null, "articlefournisseur.NotFound");
//    ArticleFournisseur articlefournisseur = ArticleFournisseurFactory.articlefournisseurDTOToArticleFournisseur(articlefournisseurDTO);
//    articlefournisseur = articlefournisseurRepository.save(articlefournisseur);
//    ArticleFournisseurDTO resultDTO = ArticleFournisseurFactory.articlefournisseurToArticleFournisseurDTO(articlefournisseur);
//    return resultDTO;
//  }
//
//  /**
//   * Get one articlefournisseurDTO by id.
//   *
//   * @param id the id of the entity
//   * @return the entity DTO
//   */
//  @Transactional(
//      readOnly = true
//  )
//  public ArticleFournisseurDTO findOne(ArticleFournisseurPK id) {
//    log.debug("Request to get ArticleFournisseur: {}",id);
//    ArticleFournisseur articlefournisseur= articlefournisseurRepository.findOne(id);
//    ArticleFournisseurDTO dto = ArticleFournisseurFactory.articlefournisseurToArticleFournisseurDTO(articlefournisseur);
//    return dto;
//  }
//
//  /**
//   * Get one articlefournisseur by id.
//   *
//   * @param id the id of the entity
//   * @return the entity
//   */
//  @Transactional(
//      readOnly = true
//  )
//  public ArticleFournisseur findArticleFournisseur(ArticleFournisseurPK id) {
//    log.debug("Request to get ArticleFournisseur: {}",id);
//    ArticleFournisseur articlefournisseur= articlefournisseurRepository.findOne(id);
//    return articlefournisseur;
//  }
//
//  /**
//   * Get all the articlefournisseurs.
//   *
//   * @return the the list of entities
//   */
//  @Transactional(
//      readOnly = true
//  )
//  public Collection<ArticleFournisseurDTO> findAll() {
//    log.debug("Request to get All ArticleFournisseurs");
//    Collection<ArticleFournisseur> result= articlefournisseurRepository.findAll();
//    return ArticleFournisseurFactory.articlefournisseurToArticleFournisseurDTOs(result);
//  }
//
//  /**
//   * Delete articlefournisseur by id.
//   *
//   * @param id the id of the entity
//   */
//  public void delete(ArticleFournisseurPK id) {
//    log.debug("Request to delete ArticleFournisseur: {}",id);
//    articlefournisseurRepository.delete(id);
//  }
//}

