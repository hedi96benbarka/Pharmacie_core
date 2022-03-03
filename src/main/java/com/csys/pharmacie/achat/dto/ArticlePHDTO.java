package com.csys.pharmacie.achat.dto;

import java.util.List;
public class ArticlePHDTO extends ArticleDTO {
    
    private boolean imported; 
     
    private List<ArticleUniteDTO> articleUnites;

    public List<ArticleUniteDTO> getArticleUnites() {
        return articleUnites;
    }

    public void setArticleUnites(List<ArticleUniteDTO> articleUnites) {
        this.articleUnites = articleUnites;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

    public ArticlePHDTO() {
    }

    public ArticlePHDTO(List<ArticleUniteDTO> articleUnites, Integer code) {
        super(code);
        this.articleUnites = articleUnites;
    }

    
}
