package com.csys.pharmacie.inventaire.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

public class InventaireDTO {

    @NotNull
    private Integer code;

    @NotNull
    private Integer depot;

    private CategorieDepotEnum categorieDepot;

    private Date dateOuverture;

    private String userOuverture;

    private Date dateCloture;

    private String userCloture;

    private String codeSaisie;

    private String categArtDesignation;

    private String categArtDesignationSec;

    private String userAnnule;

    private Date dateAnnule;
    
    private Boolean isDemarrage;

    public String getCodeSaisie() {

        return codeSaisie;
    }

    public String getCategArtDesignation() {
        return categArtDesignation;
    }

    public void setCategArtDesignation(String categArtDesignation) {
        this.categArtDesignation = categArtDesignation;
    }

    public String getCategArtDesignationSec() {
        return categArtDesignationSec;
    }

    public void setCategArtDesignationSec(String categArtDesignationSec) {
        this.categArtDesignationSec = categArtDesignationSec;
    }

    public void setCodeSaisie(String codeSaisie) {
        this.codeSaisie = codeSaisie;
    }

    private Integer categorieArticleParent;

    private List<DetailInventaireDTO> detailInventaireCollection;

    public Integer getCategorieArticleParent() {
        return categorieArticleParent;
    }

    public void setCategorieArticleParent(Integer categorieArticleParent) {
        this.categorieArticleParent = categorieArticleParent;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getDepot() {
        return depot;
    }

    public void setDepot(Integer depot) {
        this.depot = depot;
    }

    public CategorieDepotEnum getCategorieDepot() {
        return categorieDepot;
    }

    public void setCategorieDepot(CategorieDepotEnum categorieDepot) {
        this.categorieDepot = categorieDepot;
    }

    public Date getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Date dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public String getUserOuverture() {
        return userOuverture;
    }

    public void setUserOuverture(String userOuverture) {
        this.userOuverture = userOuverture;
    }

    public Date getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(Date dateCloture) {
        this.dateCloture = dateCloture;
    }

    public String getUserCloture() {
        return userCloture;
    }

    public void setUserCloture(String userCloture) {
        this.userCloture = userCloture;
    }

    public String getUserAnnule() {
        return userAnnule;
    }

    public void setUserAnnule(String userAnnule) {
        this.userAnnule = userAnnule;
    }

    public Date getDateAnnule() {
        return dateAnnule;
    }

    public void setDateAnnule(Date dateAnnule) {
        this.dateAnnule = dateAnnule;
    }
    
    public List<DetailInventaireDTO> getDetailInventaireCollection() {
        return detailInventaireCollection;
    }

    public void setDetailInventaireCollection(List<DetailInventaireDTO> detailInventaireCollection) {
        this.detailInventaireCollection = detailInventaireCollection;
    }

    public Boolean getIsDemarrage() {
        return isDemarrage;
    }

    public void setIsDemarrage(Boolean isDemarrage) {
        this.isDemarrage = isDemarrage;
    }

    @Override
    public String toString() {
        return "InventaireDTO{" + "code=" + code + ", depot=" + depot + '}';
    }
    
    
}
