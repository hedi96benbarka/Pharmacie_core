package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.client.dto.DeviseDTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FactureBonReceptionDTO {

    @NotNull
    @Size(min = 0, max = 50)
    private String codeFournisseur;

    @NotNull

    private LocalDate dateFournisseur;

    @NotNull
    @Size(min = 1, max = 50)
    private String referenceFournisseur;

    private String codvend;

    private LocalDate datesys;

    private LocalTime heuresys;

    private String numaffiche;

    private String numbon;

    private BigDecimal montant;

    private LocalDateTime datBon;

// c est la liste concaténé des numBons des bons de receptions necessaire pour les editions   
    @JsonIgnore
    private String listeDesBons;

//    attribut necessaire avec les rpt :cristal report ne supporte pas le type LocalDateTime 
    @JsonIgnore
    private Date datebonEdition;

    @JsonIgnore
    private Date dateFournisseurEdition;

    private TypeBonEnum typbon;

    private CategorieDepotEnum categDepot;

    private BigDecimal remiseExep;

    private List<BonRecepDTO> BonReceptionCollection;
    private List<MvtstoBADTO> detailReceptionCollection;
    private LocalDateTime dateAnnule;
    private Integer maxDelaiPaiement;
    @Size(max = 50)
    private String userAnnule;

    private List<BaseTvaFactureBonReceptionDTO> baseTvaFactureBonReceptionCollection;
    private FournisseurDTO fournisseur;

    private String codeDevise;
    private BigDecimal tauxDevise;
    private BigDecimal montantDevise;
    private String designationDevise;
    private DeviseDTO devise;

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

    public TypeBonEnum getTypbon() {
        return typbon;
    }

    public void setTypbon(TypeBonEnum typbon) {
        this.typbon = typbon;
    }

    public String getReferenceFournisseur() {
        return referenceFournisseur;
    }

    public void setReferenceFournisseur(String referenceFournisseur) {
        this.referenceFournisseur = referenceFournisseur;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
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

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public BigDecimal getRemiseExep() {
        return remiseExep;
    }

    public void setRemiseExep(BigDecimal remiseExep) {
        this.remiseExep = remiseExep;
    }

    public List<BonRecepDTO> getBonReceptionCollection() {
        return BonReceptionCollection;
    }

    public void setBonReceptionCollection(List<BonRecepDTO> BonReceptionCollection) {
        this.BonReceptionCollection = BonReceptionCollection;
    }

    public List<BaseTvaFactureBonReceptionDTO> getBaseTvaFactureBonReceptionCollection() {
        return baseTvaFactureBonReceptionCollection;
    }

    public void setBaseTvaFactureBonReceptionCollection(List<BaseTvaFactureBonReceptionDTO> baseTvaFactureBonReceptionCollection) {
        this.baseTvaFactureBonReceptionCollection = baseTvaFactureBonReceptionCollection;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getDatBon() {
        return datBon;
    }

    public void setDatBon(LocalDateTime dateBon) {
        this.datBon = dateBon;
    }

    public Date getDatebonEdition() {
        return datebonEdition;
    }

    public void setDatebonEdition(Date datbon) {
        this.datebonEdition = datbon;
    }

    public String getListeDesBons() {
        return listeDesBons;
    }

    public void setListeDesBons(String listeDesBons) {
        this.listeDesBons = listeDesBons;
    }

    public Date getDateFournisseurEdition() {
        return dateFournisseurEdition;
    }

    public void setDateFournisseurEdition(Date dateFournisseurEdition) {
        this.dateFournisseurEdition = dateFournisseurEdition;
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

    public List<MvtstoBADTO> getDetailReceptionCollection() {
        return detailReceptionCollection;
    }

    public void setDetailReceptionCollection(List<MvtstoBADTO> detailReceptionCollection) {
        this.detailReceptionCollection = detailReceptionCollection;
    }

    public Integer getMaxDelaiPaiement() {
        return maxDelaiPaiement;
    }

    public void setMaxDelaiPaiement(Integer maxDelaiPaiement) {
        this.maxDelaiPaiement = maxDelaiPaiement;
    }

    public String getCodeDevise() {
        return codeDevise;
    }

    public void setCodeDevise(String codeDevise) {
        this.codeDevise = codeDevise;
    }

    public BigDecimal getTauxDevise() {
        return tauxDevise;
    }

    public void setTauxDevise(BigDecimal tauxDevise) {
        this.tauxDevise = tauxDevise;
    }

    public BigDecimal getMontantDevise() {
        return montantDevise;
    }

    public void setMontantDevise(BigDecimal montantDevise) {
        this.montantDevise = montantDevise;
    }

    public String getDesignationDevise() {
        return designationDevise;
    }

    public void setDesignationDevise(String designationDevise) {
        this.designationDevise = designationDevise;
    }

    public DeviseDTO getDevise() {
        return devise;
    }

    public void setDevise(DeviseDTO devise) {
        this.devise = devise;
    }



}
