package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.BaseBonDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

public class FactureBADTO extends BaseBonDTO<MvtstoBADTO> {

    @NotNull
    private BigDecimal mntBon;

    @NotNull
    private BigDecimal valrem;

    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateFrs;

    private String designationDepot;

    @NotNull
    private Integer coddep;

    @NotNull
    @Valid
    private FournisseurDTO fournisseur;

    private String codeFournisseu;
    @NotNull
    @Size(max = 140)
    private String memop;
    @NotNull
    private String refFrs;

    private Boolean fournisseurExonere;

    private Integer maxDelaiPaiement;





    @Valid
    @NotNull
    private List<BaseTvaReceptionDTO> basesTVA;

//    private String numAfficheRecetionTemporaire;
    @NotNull
    @NotEmpty
    private List<Integer> purchaseOrdersCodes;

    /* This field is only serialized because we only need it to wrap the purchase orders informations and send it to the client*/
    private Set<CommandeAchatDTO> purchaseOrders;

    private Date datebonEdition;

    private Date dateFournisseurEdition;
    private Integer codeSite;
    private String designationSite;

    public BigDecimal getValrem() {
        return valrem;
    }

    public void setValrem(BigDecimal valrem) {
        this.valrem = valrem;
    }

    public BigDecimal getMntBon() {
        return mntBon;
    }

    public void setMntBon(BigDecimal mntBon) {
        this.mntBon = mntBon;
    }

    public LocalDate getDateFrs() {
        return dateFrs;
    }

    public void setDateFrs(LocalDate dateFrs) {
        this.dateFrs = dateFrs;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public String getRefFrs() {
        return refFrs;
    }

    public void setRefFrs(String refFrs) {
        this.refFrs = refFrs;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public String getCodeFournisseu() {
        return codeFournisseu;
    }

    public void setCodeFournisseu(String codeFournisseu) {
        this.codeFournisseu = codeFournisseu;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public List<BaseTvaReceptionDTO> getBasesTVA() {
        return basesTVA;
    }

    public void setBasesTVA(List<BaseTvaReceptionDTO> basesTVA) {
        this.basesTVA = basesTVA;
    }

    public Boolean getFournisseurExonere() {
        return fournisseurExonere;
    }

    public void setFournisseurExonere(Boolean fournisseurExonere) {
        this.fournisseurExonere = fournisseurExonere;
    }

    public Integer getMaxDelaiPaiement() {
        return maxDelaiPaiement;
    }

    public void setMaxDelaiPaiement(Integer maxDelaiPaiement) {
        this.maxDelaiPaiement = maxDelaiPaiement;
    }

//    public String getNumAfficheRecetionTemporaire() {
//        return numAfficheRecetionTemporaire;
//    }
//
//    public void setNumAfficheRecetionTemporaire(String numAfficheRecetionTemporaire) {
//        this.numAfficheRecetionTemporaire = numAfficheRecetionTemporaire;
//    }
    public List<Integer> getPurchaseOrdersCodes() {
        return purchaseOrdersCodes;
    }

    public void setPurchaseOrdersCodes(List<Integer> purchaseOrdersCodes) {
        this.purchaseOrdersCodes = purchaseOrdersCodes;
    }

    public Set<CommandeAchatDTO> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Set<CommandeAchatDTO> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    public Date getDatebonEdition() {
        return datebonEdition;
    }

    public void setDatebonEdition(Date datebonEdition) {
        this.datebonEdition = datebonEdition;
    }

    public Date getDateFournisseurEdition() {
        return dateFournisseurEdition;
    }

    public void setDateFournisseurEdition(Date dateFournisseurEdition) {
        this.dateFournisseurEdition = dateFournisseurEdition;
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
