package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ReceivingEditionDTO {

    private Integer code;
    private String numbon;
    private String numaffiche;
    private Date dateCreate;
    private String userCreate;
    private Date dateValidate;
    private String userValidate;
    private Date dateAnnule;
    private String userAnnule;
    private String fournisseur;
    private String designationFournisseur;
    private String numAfficheBonRecep;
    private List<String> numBonCommandes;
    private FournisseurDTO fournisseurDTO;
    private CategorieDepotEnum categDepot;
    private List<Integer> codesCommandeAchat;
    private Set<CommandeAchatDTO> CommandeAchatList;
    private List<ReceivingDetailsDTO> ReceivingDetailsList;
    private BonRecepDTO bonRecep;
    private String memo;
    private String userMemo;
    private Date dateMemo;
    private Boolean imprime;
    private String userImprime;
    private Date dateImprime;
     private Date dateValidateReceiving;
    private String userValidateReceiving;
    private String designationSite;
    public ReceivingEditionDTO() {
    }

    public ReceivingEditionDTO(Integer code, String numbon, Date dateCreate, String userCreate, Date dateValidate, String userValidate, Date dateAnnule, String userAnnule, String fournisseur, String designationFournisseur, String numAfficheBonRecep, List<String> numBonCommandes, FournisseurDTO fournisseurDTO, CategorieDepotEnum categDepot, List<Integer> codesCommandeAchat, Set<CommandeAchatDTO> CommandeAchatList, List<ReceivingDetailsDTO> ReceivingDetailsList, BonRecepDTO bonRecep) {
        this.code = code;
        this.numbon = numbon;
        this.dateCreate = dateCreate;
        this.userCreate = userCreate;
        this.dateValidate = dateValidate;
        this.userValidate = userValidate;
        this.dateAnnule = dateAnnule;
        this.userAnnule = userAnnule;
        this.fournisseur = fournisseur;
        this.designationFournisseur = designationFournisseur;
        this.numAfficheBonRecep = numAfficheBonRecep;
        this.numBonCommandes = numBonCommandes;
        this.fournisseurDTO = fournisseurDTO;
        this.categDepot = categDepot;
        this.codesCommandeAchat = codesCommandeAchat;
        this.CommandeAchatList = CommandeAchatList;
        this.ReceivingDetailsList = ReceivingDetailsList;
        this.bonRecep = bonRecep;
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

    public Date getDateValidate() {
        return dateValidate;
    }

    public void setDateValidate(Date dateValidate) {
        this.dateValidate = dateValidate;
    }

    public String getUserValidate() {
        return userValidate;
    }

    public void setUserValidate(String userValidate) {
        this.userValidate = userValidate;
    }

    public Date getDateAnnule() {
        return dateAnnule;
    }

    public void setDateAnnule(Date dateAnnule) {
        this.dateAnnule = dateAnnule;
    }

    public String getUserAnnule() {
        return userAnnule;
    }

    public void setUserAnnule(String userAnnule) {
        this.userAnnule = userAnnule;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getDesignationFournisseur() {
        return designationFournisseur;
    }

    public void setDesignationFournisseur(String designationFournisseur) {
        this.designationFournisseur = designationFournisseur;
    }

    public String getNumAfficheBonRecep() {
        return numAfficheBonRecep;
    }

    public void setNumAfficheBonRecep(String numAfficheBonRecep) {
        this.numAfficheBonRecep = numAfficheBonRecep;
    }

    public List<String> getNumBonCommandes() {
        return numBonCommandes;
    }

    public void setNumBonCommandes(List<String> numBonCommandes) {
        this.numBonCommandes = numBonCommandes;
    }

    public FournisseurDTO getFournisseurDTO() {
        return fournisseurDTO;
    }

    public void setFournisseurDTO(FournisseurDTO fournisseurDTO) {
        this.fournisseurDTO = fournisseurDTO;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public List<Integer> getCodesCommandeAchat() {
        return codesCommandeAchat;
    }

    public void setCodesCommandeAchat(List<Integer> codesCommandeAchat) {
        this.codesCommandeAchat = codesCommandeAchat;
    }

    public Set<CommandeAchatDTO> getCommandeAchatList() {
        return CommandeAchatList;
    }

    public void setCommandeAchatList(Set<CommandeAchatDTO> CommandeAchatList) {
        this.CommandeAchatList = CommandeAchatList;
    }

    public List<ReceivingDetailsDTO> getReceivingDetailsList() {
        return ReceivingDetailsList;
    }

    public void setReceivingDetailsList(List<ReceivingDetailsDTO> ReceivingDetailsList) {
        this.ReceivingDetailsList = ReceivingDetailsList;
    }

    public BonRecepDTO getBonRecep() {
        return bonRecep;
    }

    public void setBonRecep(BonRecepDTO bonRecep) {
        this.bonRecep = bonRecep;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getUserMemo() {
        return userMemo;
    }

    public void setUserMemo(String userMemo) {
        this.userMemo = userMemo;
    }

    public Date getDateMemo() {
        return dateMemo;
    }

    public void setDateMemo(Date dateMemo) {
        this.dateMemo = dateMemo;
    }

    public Boolean getImprime() {
        return imprime;
    }

    public void setImprime(Boolean imprime) {
        this.imprime = imprime;
    }

    public String getUserImprime() {
        return userImprime;
    }

    public void setUserImprime(String userImprime) {
        this.userImprime = userImprime;
    }

    public Date getDateImprime() {
        return dateImprime;
    }

    public void setDateImprime(Date dateImprime) {
        this.dateImprime = dateImprime;
    }

    public Date getDateValidateReceiving() {
        return dateValidateReceiving;
    }

    public void setDateValidateReceiving(Date dateValidateReceiving) {
        this.dateValidateReceiving = dateValidateReceiving;
    }

    public String getUserValidateReceiving() {
        return userValidateReceiving;
    }

    public void setUserValidateReceiving(String userValidateReceiving) {
        this.userValidateReceiving = userValidateReceiving;
    }

    public String getDesignationSite() {
        return designationSite;
    }

    public void setDesignationSite(String designationSite) {
        this.designationSite = designationSite;
    }

}
