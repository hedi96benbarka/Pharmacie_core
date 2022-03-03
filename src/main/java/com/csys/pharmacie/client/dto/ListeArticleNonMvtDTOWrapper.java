/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.dto;

import java.util.List;

/**
 *
 * @author DELL
 */
public class ListeArticleNonMvtDTOWrapper {

    private List<ArticleNonMvtDTO> listeArticleNonMvtDTO;

    public ListeArticleNonMvtDTOWrapper(List<ArticleNonMvtDTO> listeArticleNonMvtDTO) {
        this.listeArticleNonMvtDTO = listeArticleNonMvtDTO;
    }

    public ListeArticleNonMvtDTOWrapper() {
    }

    public List<ArticleNonMvtDTO> getListeArticleNonMvtDTO() {
        return listeArticleNonMvtDTO;
    }

    public void setListeArticleNonMvtDTO(List<ArticleNonMvtDTO> listeArticleNonMvtDTO) {
        this.listeArticleNonMvtDTO = listeArticleNonMvtDTO;
    }

    @Override
    public String toString() {
        return "{" + listeArticleNonMvtDTO + '}';
    }

}
