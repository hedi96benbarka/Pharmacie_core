package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReceivingDTO {

    private Integer code;

    @NotNull
    @Size(min = 1, max = 50)
    private String numbon;

    private String numaffiche;

    private LocalDateTime dateCreate;

    @Size(min = 0, max = 50)
    private String userCreate;

    private LocalDateTime dateValidate;

    @Size(min = 0, max = 50)
    private String userValidate;

    private LocalDateTime dateAnnule;

    @Size(min = 0, max = 50)
    private String userAnnule;

    @NotNull
    @Size(min = 0, max = 255)
    private String fournisseur;

    private String designationFournisseur;

    private String numAfficheBonRecep;
    private List<String> numBonCommandes;

    @NotNull
    @Valid
    private FournisseurDTO fournisseurDTO;

    @NotNull
    private CategorieDepotEnum categDepot;

    private List<Integer> codesCommandeAchat;

    @Size(min = 1)
    private Set<CommandeAchatDTO> CommandeAchatList;

    @NotNull
    @Valid
    private List<ReceivingDetailsDTO> ReceivingDetailsList;

    private BonRecepDTO bonRecep;

    private String memo;

    private String userMemo;

    private LocalDateTime dateMemo;

    private Boolean imprime;

    private String userImprime;

    private LocalDateTime dateImprime;
    private LocalDateTime dateValidateReceiving;
    @Size(min = 0, max = 50)
    private String userValidateReceiving;
    // numeroBonCommandes is added for edition receiving
    private String numeroBonCommandes;
    @JsonIgnore
    private Date dateCreateEdition;

    private Integer codeSite;
    private String designationSite;

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

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public LocalDateTime getDateValidate() {
        return dateValidate;
    }

    public void setDateValidate(LocalDateTime dateValidate) {
        this.dateValidate = dateValidate;
    }

    public String getUserValidate() {
        return userValidate;
    }

    public void setUserValidate(String userValidate) {
        this.userValidate = userValidate;
    }

    public LocalDateTime getDateAnnule() {
        return dateAnnule;
    }

    public void setDateAnnule(LocalDateTime dateAnnule) {
        this.dateAnnule = dateAnnule;
    }

    public String getUserAnnule() {
        return userAnnule;
    }

    public void setUserAnnule(String userAnnule) {
        this.userAnnule = userAnnule;
    }

    public FournisseurDTO getFournisseurDTO() {
        return fournisseurDTO;
    }

    public void setFournisseurDTO(FournisseurDTO fournisseurDTO) {
        this.fournisseurDTO = fournisseurDTO;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public Set<CommandeAchatDTO> getCommandeAchatList() {
        return CommandeAchatList;
    }

    public void setCommandeAchatList(Set<CommandeAchatDTO> CommandeAchatList) {
        this.CommandeAchatList = CommandeAchatList;
    }

    public List<Integer> getCodesCommandeAchat() {
        return codesCommandeAchat;
    }

    public void setCodesCommandeAchat(List<Integer> codesCommandeAchat) {
        this.codesCommandeAchat = codesCommandeAchat;
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

    public String getDesignationFournisseur() {
        return designationFournisseur;
    }

    public void setDesignationFournisseur(String designationFournisseur) {
        this.designationFournisseur = designationFournisseur;
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

    public LocalDateTime getDateMemo() {
        return dateMemo;
    }

    public void setDateMemo(LocalDateTime dateMemo) {
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

    public LocalDateTime getDateImprime() {
        return dateImprime;
    }

    public void setDateImprime(LocalDateTime dateImprime) {
        this.dateImprime = dateImprime;
    }

    public String getNumeroBonCommandes() {
        return numeroBonCommandes;
    }

    public void setNumeroBonCommandes(String numeroBonCommandes) {
        this.numeroBonCommandes = numeroBonCommandes;
    }

    public Date getDateCreateEdition() {
        return dateCreateEdition;
    }

    public void setDateCreateEdition(Date dateCreateEdition) {
        this.dateCreateEdition = dateCreateEdition;
    }

    public LocalDateTime getDateValidateReceiving() {
        return dateValidateReceiving;
    }

    public void setDateValidateReceiving(LocalDateTime dateValidateReceiving) {
        this.dateValidateReceiving = dateValidateReceiving;
    }

    public String getUserValidateReceiving() {
        return userValidateReceiving;
    }

    public void setUserValidateReceiving(String userValidateReceiving) {
        this.userValidateReceiving = userValidateReceiving;
    }

    public Integer getCodeSite() {
        return codeSite;
    }

    public void setCodeSite(Integer codeSite) {
        this.codeSite = codeSite;
    }

    public String getDesignationSite() {
        return designationSite;
    }

    public void setDesignationSite(String designationSite) {
        this.designationSite = designationSite;
    }

}
