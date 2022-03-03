package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MvtstoBTEditionDTO {

    @NotNull
    private Integer articleID;
   
    private String numbon;
    
    private String numordre;

    @NotNull
    @Size(min = 1, max = 17)
    private String lotInter;

    @NotNull
    private BigDecimal unityPrice;

    private Date preemptionDate;

    @NotNull
    private Integer unityID;

    private String unityDesignation;

    @NotNull
    private CategorieDepotEnum categDepot;

    @NotNull
    @Size(min = 0, max = 255)
    private String designation;

    @NotNull
    @Size(min = 0, max = 255)
    private String secondDesignation;

    @NotNull
    @Size(min = 0, max = 50)
    private String codeSaisi;

    @NotNull
    private BigDecimal quantity;
    
     private BigDecimal quantiteRecue;
    
    private BigDecimal quantiteDefectueuse;

    public Integer getArticleID() {
        return articleID;
    }

    public void setArticleID(Integer articleID) {
        this.articleID = articleID;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public BigDecimal getUnityPrice() {
        return unityPrice;
    }

    public void setUnityPrice(BigDecimal unityPrice) {
        this.unityPrice = unityPrice;
    }

    public Date getPreemptionDate() {
        return preemptionDate;
    }

    public void setPreemptionDate(Date preemptionDate) {
        this.preemptionDate = preemptionDate;
    }

    public Integer getUnityID() {
        return unityID;
    }

    public void setUnityID(Integer unityID) {
        this.unityID = unityID;
    }

    public String getUnityDesignation() {
        return unityDesignation;
    }

    public void setUnityDesignation(String unityDesignation) {
        this.unityDesignation = unityDesignation;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSecondDesignation() {
        return secondDesignation;
    }

    public void setSecondDesignation(String secondDesignation) {
        this.secondDesignation = secondDesignation;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantiteRecue() {
        return quantiteRecue;
    }

    public void setQuantiteRecue(BigDecimal quantiteRecue) {
        this.quantiteRecue = quantiteRecue;
    }

    public BigDecimal getQuantiteDefectueuse() {
        return quantiteDefectueuse;
    }

    public void setQuantiteDefectueuse(BigDecimal quantiteDefectueuse) {
        this.quantiteDefectueuse = quantiteDefectueuse;
    }

    
    
    @Override
    public String toString() {
        return "MvtstoBTEditionDTO{" + "articleID=" + articleID + ", numbon=" + numbon + ", numordre=" + numordre + ", lotInter=" + lotInter + ", unityPrice=" + unityPrice + ", preemptionDate=" + preemptionDate + ", unityID=" + unityID + ", unityDesignation=" + unityDesignation + ", categDepot=" + categDepot + ", designation=" + designation + ", secondDesignation=" + secondDesignation + ", codeSaisi=" + codeSaisi + ", quantity=" + quantity + '}';
    }
    
    
}
