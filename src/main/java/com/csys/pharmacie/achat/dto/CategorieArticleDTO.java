package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

public class CategorieArticleDTO extends BaseDTO {

    @NotNull
    private boolean affecter;
    private String prefix;
    private String suffix;
    @NotNull
    private String categorieDepotCode;
    private BigDecimal marge;
    private List<CategorieArticleDTO> nodes;
    private Integer parent;
    private boolean margeAppTous;
    private String listeCategorieArticle;
    private Integer codeMaquetteCategorieArticle;
    private String codeSaisi;

    public CategorieArticleDTO() {
    }

    public CategorieArticleDTO(Integer code, String designation) {
        super(code, designation);
    }

    public Boolean hasMargeFixe() {
        return (marge != null);

    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isAffecter() {
        return affecter;
    }

    public void setAffecter(boolean affecter) {
        this.affecter = affecter;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public List<CategorieArticleDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<CategorieArticleDTO> children) {
        this.nodes = children;
    }

    public String getCategorieDepotCode() {
        return categorieDepotCode;
    }

    public void setCategorieDepotCode(String categorieDepotCode) {
        this.categorieDepotCode = categorieDepotCode;
    }

    public BigDecimal getMarge() {
        return marge;
    }

    public void setMarge(BigDecimal marge) {
        this.marge = marge;
    }

    public String getText() {
        return getDesignation();
    }

    public boolean isMargeAppTous() {
        return margeAppTous;
    }

    public void setMargeAppTous(boolean margeAppTous) {
        this.margeAppTous = margeAppTous;
    }

    public String getListeCategorieArticle() {
        return listeCategorieArticle;
    }

    public void setListeCategorieArticle(String listeCategorieArticle) {
        this.listeCategorieArticle = listeCategorieArticle;
    }

    public Integer getCodeMaquetteCategorieArticle() {
        return codeMaquetteCategorieArticle;
    }

    public void setCodeMaquetteCategorieArticle(Integer codeMaquetteCategorieArticle) {
        this.codeMaquetteCategorieArticle = codeMaquetteCategorieArticle;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    @Override
    public String toString() {
        return "CategorieArticleDTO{" + "categorieDepotCode=" + categorieDepotCode + ", parent=" + parent + ", listeCategorieArticle=" + listeCategorieArticle + '}';
    }

}
