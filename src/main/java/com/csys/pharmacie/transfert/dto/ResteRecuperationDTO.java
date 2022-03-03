package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.transfert.domain.ResteRecuperationPK;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ResteRecuperationDTO {

    @NotNull
    private Integer codart;

    @Size(min = 0, max = 255)
    private String desart;

    @Size(min = 0, max = 255)
    private String desArtSec;

    @Size(min = 0, max = 50)
    private String codeSaisi;

    @NotNull
    private BigDecimal quantite;

    @NotNull
    private Integer unityCode;

    private String unityDesignation;

    private Boolean perissable;

    @NotNull
    private Integer depotID;

    private BigDecimal priach;

    private BigDecimal quantityInStore;
    
    private CategorieDepotEnum categDepot;

    public ResteRecuperationDTO(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public ResteRecuperationDTO(BigDecimal quantite, BigDecimal quantityInStore) {
        this.quantite = quantite;
        this.quantityInStore = quantityInStore;
    }

    public ResteRecuperationDTO() {
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesArtSec() {
        return desArtSec;
    }

    public void setDesArtSec(String desArtSec) {
        this.desArtSec = desArtSec;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public Integer getUnityCode() {
        return unityCode;
    }

    public void setUnityCode(Integer unityCode) {
        this.unityCode = unityCode;
    }

    public String getUnityDesignation() {
        return unityDesignation;
    }

    public void setUnityDesignation(String unityDesignation) {
        this.unityDesignation = unityDesignation;
    }

    public Boolean getPerissable() {
        return perissable;
    }

    public void setPerissable(Boolean perissable) {
        this.perissable = perissable;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public Integer getDepotID() {
        return depotID;
    }

    public void setDepotID(Integer depotID) {
        this.depotID = depotID;
    }

    public BigDecimal getPriach() {
        return priach;
    }

    public void setPriach(BigDecimal priach) {
        this.priach = priach;
    }

    public BigDecimal getQuantityInStore() {
        return quantityInStore;
    }

    public void setQuantityInStore(BigDecimal quantityInStore) {
        this.quantityInStore = quantityInStore;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

}
