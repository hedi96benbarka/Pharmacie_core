package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.client.dto.DeviseDTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.EnumCrudMethod;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.cglib.core.Local;

import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FactureDirecteDTO {

    @NotNull
    @Size(min = 0, max = 50)
    private String codeFournisseur;

    private String designationFournisseur;

    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateFournisseur;

    @NotNull
    @Size(min = 1, max = 50)
    private String referenceFournisseur;

    private BigDecimal montant;
    @NotNull
    private List<BaseTvaFactureDirecteDTO> baseTvaFactureDirecteCollection;

    @NotNull
    @Valid
    private Set<DetailFactureDirecteDTO> detailFactureDirecteCollection;

    private String numbon;

    private String codvend;

    @NotNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime datbon;
//
//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    private LocalDate datesys;

//    @JsonSerialize(using = LocalTimeSerializer.class)
//    @JsonDeserialize(using = LocalTimeDeserializer.class)
//    private LocalTime heuresys;

    private TypeBonEnum typbon;

    private String numaffiche;

    @Size(max = 300)
    private String observation;

    private CategorieDepotEnum categDepot;

    private FournisseurDTO fournisseur;
    private Boolean integrer;
    private DeviseDTO devise;
    private BigDecimal tauxDevise;
    private BigDecimal montantDevise;
    private String designationDevise;
    private String codeDevise;
    private Integer codeCommandeAchat;
    private CommandeAchatDTO commandeAchat;

    public String getCodeDevise() {
        return codeDevise;
    }

    public void setCodeDevise(String codeDevise) {
        this.codeDevise = codeDevise;
    }
    //@JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateBonEdition;
    @JsonIgnore
    private Date dateFournisseurEdition;
    //@JsonIgnore
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateAnnuleEdition;


    private EnumCrudMethod action;

    @Valid
    private List<FactureDirecteCostCenterDTO> costCenters;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateAnnule;

    @Size(min = 0, max = 50)
    private String userAnnule;

    Collection<FactureDirecteModeReglementDTO> modeReglementList;

    public String getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(String codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }

    public String getDesignationFournisseur() {
        return designationFournisseur;
    }

    public void setDesignationFournisseur(String designationFournisseur) {
        this.designationFournisseur = designationFournisseur;
    }

    public String getReferenceFournisseur() {
        return referenceFournisseur;
    }

    public void setReferenceFournisseur(String referenceFournisseur) {
        this.referenceFournisseur = referenceFournisseur;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDateFournisseur() {
        return dateFournisseur;
    }

    public void setDateFournisseur(LocalDate dateFournisseur) {
        this.dateFournisseur = dateFournisseur;
    }

    public List<BaseTvaFactureDirecteDTO> getBaseTvaFactureDirecteCollection() {
        return baseTvaFactureDirecteCollection;
    }

    public void setBaseTvaFactureDirecteCollection(List<BaseTvaFactureDirecteDTO> baseTvaFactureDirecteCollection) {
        this.baseTvaFactureDirecteCollection = baseTvaFactureDirecteCollection;
    }

    public Set<DetailFactureDirecteDTO> getDetailFactureDirecteCollection() {
        return detailFactureDirecteCollection;
    }

    public void setDetailFactureDirecteCollection(Set<DetailFactureDirecteDTO> detailFactureDirecteCollection) {
        this.detailFactureDirecteCollection = detailFactureDirecteCollection;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getDatbon() {
        return datbon;
    }

    public void setDatbon(LocalDateTime datbon) {
        this.datbon = datbon;
    }

    public EnumCrudMethod getAction() {
        return action;
    }

    public void setAction(EnumCrudMethod action) {
        this.action = action;
    }

    //    public LocalDate getDatesys() {
//        return datesys;
//    }
//
//    public void setDatesys(LocalDate datesys) {
//        this.datesys = datesys;
//    }
//
//    public LocalTime getHeuresys() {
//        return heuresys;
//    }
//
//    public void setHeuresys(LocalTime heuresys) {
//        this.heuresys = heuresys;
//    }

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

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    public LocalDateTime getDateBonEdition() {
        return dateBonEdition;
    }

    public void setDateBonEdition(LocalDateTime dateBonEdition) {
        this.dateBonEdition = dateBonEdition;
    }

    public Date getDateFournisseurEdition() {
        return dateFournisseurEdition;
    }

    public void setDateFournisseurEdition(Date dateFournisseurEdition) {
        this.dateFournisseurEdition = dateFournisseurEdition;
    }

    public List<FactureDirecteCostCenterDTO> getCostCenters() {
        return costCenters;
    }

    public void setCostCenters(List<FactureDirecteCostCenterDTO> costCenters) {
        this.costCenters = costCenters;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
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

    public Boolean getIntegrer() {
        return integrer;
    }

    public void setIntegrer(Boolean integrer) {
        this.integrer = integrer;
    }

    public Collection<FactureDirecteModeReglementDTO> getModeReglementList() {
        return modeReglementList;
    }

    public void setModeReglementList(Collection<FactureDirecteModeReglementDTO> modeReglementList) {
        this.modeReglementList = modeReglementList;
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

    public LocalDate getDateAnnuleEdition() {
        return dateAnnuleEdition;
    }

    public void setDateAnnuleEdition(LocalDate dateAnnuleEdition) {
        this.dateAnnuleEdition = dateAnnuleEdition;
    }

    public Integer getCodeCommandeAchat() {
        return codeCommandeAchat;
    }

    public void setCodeCommandeAchat(Integer codeCommandeAchat) {
        this.codeCommandeAchat = codeCommandeAchat;
    }

    public CommandeAchatDTO getCommandeAchat() {
        return commandeAchat;
    }

    public void setCommandeAchat(CommandeAchatDTO commandeAchat) {
        this.commandeAchat = commandeAchat;
    }

    @Override
    public String toString() {
        return "FactureDirecteDTO{" +
                "codeFournisseur='" + codeFournisseur + '\'' +
                ", designationFournisseur='" + designationFournisseur + '\'' +
                ", dateFournisseur=" + dateFournisseur +
                ", referenceFournisseur='" + referenceFournisseur + '\'' +
                ", montant=" + montant +
                ", baseTvaFactureDirecteCollection=" + baseTvaFactureDirecteCollection +
                ", detailFactureDirecteCollection=" + detailFactureDirecteCollection +
                ", numbon='" + numbon + '\'' +
                ", codvend='" + codvend + '\'' +
                ", datbon=" + datbon +
                ", typbon=" + typbon +
                ", numaffiche='" + numaffiche + '\'' +
                ", observation='" + observation + '\'' +
                ", categDepot=" + categDepot +
                ", fournisseur=" + fournisseur +
                ", integrer=" + integrer +
                ", devise=" + devise +
                ", tauxDevise=" + tauxDevise +
                ", montantDevise=" + montantDevise +
                ", designationDevise='" + designationDevise + '\'' +
                ", codeDevise='" + codeDevise + '\'' +
                ", codeCommandeAchat=" + codeCommandeAchat +
                ", commandeAchat=" + commandeAchat +
                ", dateBonEdition=" + dateBonEdition +
                ", dateFournisseurEdition=" + dateFournisseurEdition +
                ", dateAnnuleEdition=" + dateAnnuleEdition +
                ", action=" + action +
                ", costCenters=" + costCenters +
                ", dateAnnule=" + dateAnnule +
                ", userAnnule='" + userAnnule + '\'' +
                ", modeReglementList=" + modeReglementList +
                '}';
    }
}
