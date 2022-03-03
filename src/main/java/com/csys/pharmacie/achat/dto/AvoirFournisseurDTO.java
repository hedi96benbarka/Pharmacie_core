package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AvoirFournisseurDTO {

    @NotNull
    @Size(min = 1, max = 50)
    private String codeFournisseur;

    private String designationFournisseur;

    private FournisseurDTO fournisseur;

    @NotNull

    private LocalDate dateFournisseur;

    @NotNull
    @Size(min = 1, max = 50)
    private String referenceFournisseur;

    @NotNull
    private Integer coddep;

    @NotNull
    private BigDecimal montantTTC;

    private String userAnnule;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAnnule;

    private String numFactureBonRecep;

    private LocalDateTime DateFactureBonRecep;

    private BigDecimal mntFactureBonRecep;

    private boolean integrer;

    private List<MvtstoAFDTO> mvtstoAFList;

    private List<BaseTvaAvoirFournisseurDTO> baseTvaAvoirFournisseurList;

    private String numbon;

    @Size(min = 0, max = 20)
    private String codvend;

    private LocalDateTime datbon;

    private LocalDate datesys;

    private LocalTime heuresys;

    private TypeBonEnum typbon;

    @Size(min = 0, max = 16)
    private String numaffiche;

    private CategorieDepotEnum categDepot;

    private String memo;

    private String designationDepot;

    @JsonIgnore
    private Date datebonEdition;

    @JsonIgnore
    private Date dateFournisseurEdition;

    private String numbonRetourTransfertCompanyBranch;

    private Integer codeSite;
    private String designationSite;

    public String getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(String codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }

    public LocalDate getDateFournisseur() {
        return dateFournisseur;
    }

    public void setDateFournisseur(LocalDate dateFournisseur) {
        this.dateFournisseur = dateFournisseur;
    }

    public String getReferenceFournisseur() {
        return referenceFournisseur;
    }

    public void setReferenceFournisseur(String referenceFournisseur) {
        this.referenceFournisseur = referenceFournisseur;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
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

    public boolean getIntegrer() {
        return integrer;
    }

    public void setIntegrer(boolean integrer) {
        this.integrer = integrer;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public LocalDateTime getDatbon() {
        return datbon;
    }

    public void setDatbon(LocalDateTime datbon) {
        this.datbon = datbon;
    }

    public LocalDate getDatesys() {
        return datesys;
    }

    public void setDatesys(LocalDate datesys) {
        this.datesys = datesys;
    }

    public LocalTime getHeuresys() {
        return heuresys;
    }

    public void setHeuresys(LocalTime heuresys) {
        this.heuresys = heuresys;
    }

    public TypeBonEnum getTypbon() {
        return typbon;
    }

    public void setTypbon(TypeBonEnum typbon) {
        this.typbon = typbon;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getNumFactureBonRecep() {
        return numFactureBonRecep;
    }

    public void setNumFactureBonRecep(String numFactureBonRecep) {
        this.numFactureBonRecep = numFactureBonRecep;
    }

    public List<MvtstoAFDTO> getMvtstoAFList() {
        return mvtstoAFList;
    }

    public void setMvtstoAFList(List<MvtstoAFDTO> mvtstoAFList) {
        this.mvtstoAFList = mvtstoAFList;
    }

    public String getDesignationFournisseur() {
        return designationFournisseur;
    }

    public void setDesignationFournisseur(String designationFournisseur) {
        this.designationFournisseur = designationFournisseur;
    }

    public List<BaseTvaAvoirFournisseurDTO> getBaseTvaAvoirFournisseurList() {
        return baseTvaAvoirFournisseurList;
    }

    public void setBaseTvaAvoirFournisseurList(List<BaseTvaAvoirFournisseurDTO> baseTvaAvoirFournisseurList) {
        this.baseTvaAvoirFournisseurList = baseTvaAvoirFournisseurList;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Date getDatebonEdition() {
        return datebonEdition;
    }

    public void setDatebonEdition(Date datebonEdition) {
        this.datebonEdition = datebonEdition;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDateTime getDateFactureBonRecep() {
        return DateFactureBonRecep;
    }

    public void setDateFactureBonRecep(LocalDateTime DateFactureBonRecep) {
        this.DateFactureBonRecep = DateFactureBonRecep;
    }

    public BigDecimal getMntFactureBonRecep() {
        return mntFactureBonRecep;
    }

    public void setMntFactureBonRecep(BigDecimal mntFactureBonRecep) {
        this.mntFactureBonRecep = mntFactureBonRecep;
    }

    public Date getDateFournisseurEdition() {
        return dateFournisseurEdition;
    }

    public void setDateFournisseurEdition(Date dateFournisseurEdition) {
        this.dateFournisseurEdition = dateFournisseurEdition;
    }

    public String getNumbonRetourTransfertCompanyBranch() {
        return numbonRetourTransfertCompanyBranch;
    }

    public void setNumbonRetourTransfertCompanyBranch(String numbonRetourTransfertCompanyBranch) {
        this.numbonRetourTransfertCompanyBranch = numbonRetourTransfertCompanyBranch;
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
