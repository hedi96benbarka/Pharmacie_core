/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
public class BonEditionDTO extends FactureBAEditionDTO {

   

    private String userAnnule;

    private Date dateAnnule;


    @NotNull
    private Integer receivingCode;

    private String receivingNumBon;

    private String receivingNumaffiche;

    private String receivingMemo;
    
    private String numbonFactureBonReception;
    
    private String numafficheFactureBonReception;

   
   

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

   

    public Integer getReceivingCode() {
        return receivingCode;
    }

    public void setReceivingCode(Integer receivingCode) {
        this.receivingCode = receivingCode;
    }

    public String getReceivingNumBon() {
        return receivingNumBon;
    }

    public void setReceivingNumBon(String receivingNumBon) {
        this.receivingNumBon = receivingNumBon;
    }

    public String getReceivingNumaffiche() {
        return receivingNumaffiche;
    }

    public void setReceivingNumaffiche(String receivingNumaffiche) {
        this.receivingNumaffiche = receivingNumaffiche;
    }

    public String getReceivingMemo() {
        return receivingMemo;
    }

    public void setReceivingMemo(String receivingMemo) {
        this.receivingMemo = receivingMemo;
    }

    public String getNumbonFactureBonReception() {
        return numbonFactureBonReception;
    }

    public void setNumbonFactureBonReception(String numbonFactureBonReception) {
        this.numbonFactureBonReception = numbonFactureBonReception;
    }

    public String getNumafficheFactureBonReception() {
        return numafficheFactureBonReception;
    }

    public void setNumafficheFactureBonReception(String numafficheFactureBonReception) {
        this.numafficheFactureBonReception = numafficheFactureBonReception;
    }

    @Override
    public String toString() {
        return "BonEditionDTO{" + "userAnnule=" + userAnnule + ", dateAnnule=" + dateAnnule + ", receivingCode=" + receivingCode + ", receivingNumBon=" + receivingNumBon + ", receivingNumaffiche=" + receivingNumaffiche + ", receivingMemo=" + receivingMemo + ", numbonFactureBonReception=" + numbonFactureBonReception + ", numafficheFactureBonReception=" + numafficheFactureBonReception + '}';
    }

}