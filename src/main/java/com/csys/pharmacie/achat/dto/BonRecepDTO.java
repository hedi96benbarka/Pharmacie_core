/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.EnumCrudMethod;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.util.List;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author Farouk
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class BonRecepDTO extends FactureBADTO {

    private String userAnnule;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateAnnule;

//    @NotNull
//    @NotEmpty
//    private List<Integer> purchaseOrdersCodes;
//
//    /* This field is only serialized because we only need it to wrap the purchase orders informations and send it to the client*/
//    private Set<CommandeAchatDTO> purchaseOrders;
    @NotNull
    private Integer receivingCode;

    private String receivingNumBon;

    private String receivingNumaffiche;
    @NotNull
    private List<UUID> attachedFilesIDs;

    private String receivingMemo;

    private Boolean automatique;

    private Boolean isReturned;

//    private Boolean fournisseurExonere;
    private String numAfficheFactureBonReception;

    private List<BaseTvaReceptionDTO> listeBaseTvaReceptionDTO;

    private String numAfficheRecetionTemporaire;
    
    private EnumCrudMethod action;
    
    private Integer codeSite;
    
    private String numbonOringin;
    
    private String designationSite;

    public List<UUID> getAttachedFilesIDs() {
        return attachedFilesIDs;
    }

    public void setAttachedFilesIDs(List<UUID> attachedFilesIDs) {
        this.attachedFilesIDs = attachedFilesIDs;
    }

//    public List<Integer> getPurchaseOrdersCodes() {
//        return purchaseOrdersCodes;
//    }
//
//    public void setPurchaseOrdersCodes(List<Integer> purchaseOrdersCodes) {
//        this.purchaseOrdersCodes = purchaseOrdersCodes;
//    }
//
//    public Set<CommandeAchatDTO> getPurchaseOrders() {
//        return purchaseOrders;
//    }
//
//    public void setPurchaseOrders(Set<CommandeAchatDTO> purchaseOrders) {
//        this.purchaseOrders = purchaseOrders;
//    }
    public String getUserAnnule() {
        return userAnnule;
    }

    public void setUserAnnule(String userAnnule) {
        this.userAnnule = userAnnule;
    }

    public LocalDateTime getDateAnnule() {
        return dateAnnule;
    }

    public void setDateAnnule(LocalDateTime dateAnnule) {
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

    public Boolean getAutomatique() {
        return automatique;
    }

    public void setAutomatique(Boolean automatique) {
        this.automatique = automatique;
    }

    public Boolean getIsReturned() {
        return isReturned;
    }

    public void setIsReturned(Boolean isReturned) {
        this.isReturned = isReturned;
    }

    public List<BaseTvaReceptionDTO> getListeBaseTvaReceptionDTO() {
        return listeBaseTvaReceptionDTO;
    }

    public void setListeBaseTvaReceptionDTO(List<BaseTvaReceptionDTO> listeBaseTvaReceptionDTO) {
        this.listeBaseTvaReceptionDTO = listeBaseTvaReceptionDTO;
    }

//    public Boolean getFournisseurExonere() {
//        return fournisseurExonere;
//    }
//
//    public void setFournisseurExonere(Boolean fournisseur_exonere) {
//        this.fournisseurExonere = fournisseur_exonere;
//    }
    public String getNumAfficheFactureBonReception() {
        return numAfficheFactureBonReception;
    }

    public void setNumAfficheFactureBonReception(String numAfficheFactureBonReception) {
        this.numAfficheFactureBonReception = numAfficheFactureBonReception;
    }

    public String getNumAfficheRecetionTemporaire() {
        return numAfficheRecetionTemporaire;
    }

    public void setNumAfficheRecetionTemporaire(String numAfficheRecetionTemporaire) {
        this.numAfficheRecetionTemporaire = numAfficheRecetionTemporaire;
    }

    public EnumCrudMethod getAction() {
        return action;
    }

    public void setAction(EnumCrudMethod action) {
        this.action = action;
    }

    public Integer getCodeSite() {
        return codeSite;
    }

    public void setCodeSite(Integer codeSite) {
        this.codeSite = codeSite;
    }

    public String getNumbonOringin() {
        return numbonOringin;
    }

    public void setNumbonOringin(String numbonOringin) {
        this.numbonOringin = numbonOringin;
    }

    public String getDesignationSite() {
        return designationSite;
    }

    public void setDesignationSite(String designationSite) {
        this.designationSite = designationSite;
    }

}
