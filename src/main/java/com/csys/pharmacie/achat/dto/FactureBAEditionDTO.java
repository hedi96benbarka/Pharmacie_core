package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.BaseBonDTO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

public class FactureBAEditionDTO extends BaseBonDTO<DetailEditionDTO> {

    @NotNull
    private BigDecimal mntBon;

    @NotNull
    private BigDecimal valrem;

    @NotNull
    private Date dateFrs;

    private Date datebonEdition;

    private String designationDepot;

    @NotNull
    private Integer coddep;

    @NotNull
    @Valid
    private FournisseurDTO fournisseur;
    private String codeFournisseu;
    @NotNull
    private String memop;
    @NotNull
    private String refFrs;
    @Valid
    @NotNull
    private List<BaseTvaReceptionDTO> basesTVA;
    private Boolean fournisseurExonere;

    @NotNull
    @NotEmpty
    private List<Integer> purchaseOrdersCodes;

    /* This field is only serialized because we only need it to wrap the purchase orders informations and send it to the client*/
    private Set<CommandeAchatDTO> purchaseOrders;
    
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

    public Date getDateFrs() {
        return dateFrs;
    }

    public void setDateFrs(Date dateFrs) {
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

    public List<BaseTvaReceptionDTO> getBasesTVA() {
        return basesTVA;
    }

    public void setBasesTVA(List<BaseTvaReceptionDTO> basesTVA) {
        this.basesTVA = basesTVA;
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

    public Date getDatebonEdition() {
        return datebonEdition;
    }

    public void setDatebonEdition(Date datebonEdition) {
        this.datebonEdition = datebonEdition;
    }

    public Boolean getFournisseurExonere() {
        return fournisseurExonere;
    }

    public void setFournisseurExonere(Boolean fournisseurExonere) {
        this.fournisseurExonere = fournisseurExonere;
    }

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

}