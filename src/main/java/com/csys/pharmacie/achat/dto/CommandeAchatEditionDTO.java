/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

/**
 *
 * @author Administrateur
 */
import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public class CommandeAchatEditionDTO {

    private Integer codeReceiving;
    private Integer code;
    private String numbon;
    private CategorieDepotEnum categorieDepot;
    private String memo;
    private String satisfaction;
//    private Integer delaiPaiement;
    private boolean imprimer;
    private boolean manuel;
    private boolean stockable;
    private String userCreate;
    private Date dateCreate;
    private String fournisseur;
    private String userDelete;
    private Date dateDelete;
    private String userAnnul;
    private Date dateAnnul;
    
    private Collection<CommandeAchatModeReglementDTO> modeReglementList;

    public CommandeAchatEditionDTO() {
    }

    public CommandeAchatEditionDTO(Integer code, String numbon) {
        this.code = code;
        this.numbon = numbon;
    }

    public Integer getCodeReceiving() {
        return codeReceiving;
    }

    public void setCodeReceiving(Integer codeReceiving) {
        this.codeReceiving = codeReceiving;
    }

    public Date getDateDelete() {
        return dateDelete;
    }

    public String getUserAnnul() {
        return userAnnul;
    }

    public void setUserAnnul(String userAnnul) {
        this.userAnnul = userAnnul;
    }

    public Date getDateAnnul() {
        return dateAnnul;
    }

    public void setDateAnnul(Date dateAnnul) {
        this.dateAnnul = dateAnnul;
    }

    public void setDateDelete(Date dateDelete) {
        this.dateDelete = dateDelete;
    }

    public CategorieDepotEnum getCategorieDepot() {
        return categorieDepot;
    }

    public void setCategorieDepot(CategorieDepotEnum categorieDepot) {
        this.categorieDepot = categorieDepot;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

//    public Integer getDelaiPaiement() {
//        return delaiPaiement;
//    }
//
//    public void setDelaiPaiement(Integer delaiPaiement) {
//        this.delaiPaiement = delaiPaiement;
//    }

    public boolean getImprimer() {
        return imprimer;
    }

    public void setImprimer(boolean imprimer) {
        this.imprimer = imprimer;
    }

    public boolean getManuel() {
        return manuel;
    }

    public void setManuel(boolean manuel) {
        this.manuel = manuel;
    }

    public boolean getStockable() {
        return stockable;
    }

    public void setStockable(boolean stockable) {
        this.stockable = stockable;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getUserDelete() {
        return userDelete;
    }

    public void setUserDelete(String userDelete) {
        this.userDelete = userDelete;
    }
    public Collection<CommandeAchatModeReglementDTO> getModeReglementList() {
        return modeReglementList;
    }

    public void setModeReglementList(Collection<CommandeAchatModeReglementDTO> modeReglementList) {
        this.modeReglementList = modeReglementList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommandeAchatEditionDTO other = (CommandeAchatEditionDTO) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CommandeAchatEditionDTO{" + "codeReceiving=" + codeReceiving + ", code=" + code + ", numbon=" + numbon + ", categorieDepot=" + categorieDepot + ", memo=" + memo + ", satisfaction=" + satisfaction + ", imprimer=" + imprimer + ", manuel=" + manuel + ", stockable=" + stockable + ", userCreate=" + userCreate + ", dateCreate=" + dateCreate + ", fournisseur=" + fournisseur + ", userDelete=" + userDelete + ", dateDelete=" + dateDelete + ", userAnnul=" + userAnnul + ", dateAnnul=" + dateAnnul + '}';
    }

}
