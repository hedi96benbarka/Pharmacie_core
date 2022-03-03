package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FactureRetourPerimeDTO {

    private String numbon;
    private BigDecimal montantTTC;

    @NotNull
    @Size(min = 1, max = 50)
    private String codeFournisseur;
    
    @NotNull
    private LocalDate dateFournisseur;
    
    @NotNull
    @Size(min = 1, max = 50)
    private String referenceFournisseur;
    
    @Size(min = 0, max = 20)
    private String codeVend;

    @NotNull
    private LocalDateTime dateBon;

    private TypeBonEnum typeBon;

    @Size(min = 0, max = 16)
    private String numAffiche;

    private CategorieDepotEnum categDepot;

    @Size(min = 0, max = 50)
    private String userAnnule;

    private LocalDateTime dateAnnule;

    private Boolean integrer;

    private Collection<BaseTvaFactureRetourPerimeDTO> baseTvaFactureRetourPerime;

    private FournisseurDTO fournisseur;
    
    private List<RetourPerimeDTO> retourPerimeRelative; 
    
    private List<MvtstoRetourPerimeDTO> detailRetourPerimeRelative;


    // c'est la liste concaténé des num Bons de retour perime necessaire pour les editions   
    @JsonIgnore
    private String listeBonsRetourPerime;
    
//    attribut necessaire avec les rpt :cristal report ne supporte pas le type LocalDateTime 
    @JsonIgnore
    private Date dateBonEdition;
    
      @JsonIgnore
    private Date dateFournisseurEdition;
    
    
       public BigDecimal calcul(FactureRetourPerimeDTO factureRetourPerimeDTO) {
        this.montantTTC = this.baseTvaFactureRetourPerime.stream().map(item -> item.getMontantTva().add(item.getBaseTva()))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b)).setScale(2, RoundingMode.HALF_UP);
        return this.montantTTC;
    }
        
    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

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

    public Boolean getIntegrer() {
        return integrer;
    }

    public void setIntegrer(Boolean integrer) {
        this.integrer = integrer;
    }

    public Collection<BaseTvaFactureRetourPerimeDTO> getBaseTvaFactureRetourPerime() {
        return baseTvaFactureRetourPerime;
    }

    public void setBaseTvaFactureRetourPerime(Collection<BaseTvaFactureRetourPerimeDTO> baseTvaFactureRetourPerime) {
        this.baseTvaFactureRetourPerime = baseTvaFactureRetourPerime;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getCodeVend() {
        return codeVend;
    }

    public void setCodeVend(String codeVend) {
        this.codeVend = codeVend;
    }

    public LocalDateTime getDateBon() {
        return dateBon;
    }

    public void setDateBon(LocalDateTime dateBon) {
        this.dateBon = dateBon;
    }

    public TypeBonEnum getTypeBon() {
        return typeBon;
    }

    public void setTypeBon(TypeBonEnum typeBon) {
        this.typeBon = typeBon;
    }

    public String getNumAffiche() {
        return numAffiche;
    }

    public void setNumAffiche(String numAffiche) {
        this.numAffiche = numAffiche;
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

    public List<RetourPerimeDTO> getRetourPerimeRelative() {
        return retourPerimeRelative;
    }

    public void setRetourPerimeRelative(List<RetourPerimeDTO> retourPerimeRelative) {
        this.retourPerimeRelative = retourPerimeRelative;
    }

    public List<MvtstoRetourPerimeDTO> getDetailRetourPerimeRelative() {
        return detailRetourPerimeRelative;
    }

    public void setDetailRetourPerimeRelative(List<MvtstoRetourPerimeDTO> detailRetourPerimeRelative) {
        this.detailRetourPerimeRelative = detailRetourPerimeRelative;
    }



    public String getListeBonsRetourPerime() {
        return listeBonsRetourPerime;
    }

    public void setListeBonsRetourPerime(String listeBonsRetourPerime) {
        this.listeBonsRetourPerime = listeBonsRetourPerime;
    }

    public Date getDateBonEdition() {
        return dateBonEdition;
    }

    public void setDateBonEdition(Date dateBonEdition) {
        this.dateBonEdition = dateBonEdition;
    }

    public Date getDateFournisseurEdition() {
        return dateFournisseurEdition;
    }

    public void setDateFournisseurEdition(Date dateFournisseurEdition) {
        this.dateFournisseurEdition = dateFournisseurEdition;
    }

    
}
