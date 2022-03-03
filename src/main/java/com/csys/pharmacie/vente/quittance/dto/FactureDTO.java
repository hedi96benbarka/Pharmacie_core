package com.csys.pharmacie.vente.quittance.dto;

import com.csys.pharmacie.helper.BaseAvoirQuittance;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FactureDTO extends BaseAvoirQuittance {

    @Size(min = 0, max = 140)
    private String memop;

    @NotNull
    @Size(min = 1, max = 10)
    private String codAnnul;

    private LocalDateTime datAnnul;

    private String designationDepot;

    private List<MvtstoDTO> mvtstoCollection;

    @Size(min = 0, max = 20)
    private String codvend;

    private LocalDateTime datbon;

    private LocalDate datesys;

    private LocalTime heuresys;

    @Size(min = 0, max = 16)
    private String numaffiche;

    private String codfrs;

    private String reffrs;

    private String reffrsAr;

    private String numbonRecept;

    private Boolean panier;

    private Integer codePrestation;

    private Integer codePrestationPanier;
    
    private BigDecimal partiePatient;
    
    private BigDecimal partiePEC;

    private AdmissionDemandePECDTO client;

    private String numbonTransfert;
    
    private Integer commandeAchat;
    
    private String numbonComplementaire;
    
    private Long idOrdonnance;
    
    private List<FactureDTO> factureCorespondantes;
    
    private Integer codeOperation;

    private Integer codeCostCenterAnalytique;

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public String getCodAnnul() {
        return codAnnul;
    }

    public void setCodAnnul(String codAnnul) {
        this.codAnnul = codAnnul;
    }

    public LocalDateTime getDatAnnul() {
        return datAnnul;
    }

    public void setDatAnnul(LocalDateTime datAnnul) {
        this.datAnnul = datAnnul;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public List<MvtstoDTO> getMvtstoCollection() {
        return mvtstoCollection;
    }

    public void setMvtstoCollection(List<MvtstoDTO> mvtstoCollection) {
        this.mvtstoCollection = mvtstoCollection;
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

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public AdmissionDemandePECDTO getClient() {
        return client;
    }

    public void setClient(AdmissionDemandePECDTO client) {
        this.client = client;
    }

    public String getCodfrs() {
        return codfrs;
    }

    public void setCodfrs(String codfrs) {
        this.codfrs = codfrs;
    }

    public String getReffrs() {
        return reffrs;
    }

    public void setReffrs(String reffrs) {
        this.reffrs = reffrs;
    }

    public String getReffrsAr() {
        return reffrsAr;
    }

    public void setReffrsAr(String reffrsAr) {
        this.reffrsAr = reffrsAr;
    }

    public String getNumbonRecept() {
        return numbonRecept;
    }

    public void setNumbonRecept(String numbonRecept) {
        this.numbonRecept = numbonRecept;
    }

    public Boolean getPanier() {
        return panier;
    }

    public void setPanier(Boolean panier) {
        this.panier = panier;
    }

    public Integer getCodePrestation() {
        return codePrestation;
    }

    public void setCodePrestation(Integer codePrestation) {
        this.codePrestation = codePrestation;
    }

    public Integer getCodePrestationPanier() {
        return codePrestationPanier;
    }

    public void setCodePrestationPanier(Integer codePrestationPanier) {
        this.codePrestationPanier = codePrestationPanier;
    }

    public BigDecimal getPartiePatient() {
        return partiePatient;
    }

    public void setPartiePatient(BigDecimal partiePatient) {
        this.partiePatient = partiePatient;
    }

    public BigDecimal getPartiePEC() {
        return partiePEC;
    }

    public void setPartiePEC(BigDecimal partiePEC) {
        this.partiePEC = partiePEC;
    }

    public String getNumbonTransfert() {
        return numbonTransfert;
    }

    public void setNumbonTransfert(String numbonTransfert) {
        this.numbonTransfert = numbonTransfert;
    }

    public Integer getCommandeAchat() {
        return commandeAchat;
    }

    public void setCommandeAchat(Integer commandeAchat) {
        this.commandeAchat = commandeAchat;
    }

    public List<FactureDTO> getFactureCorespondantes() {
        return factureCorespondantes;
    }

    public void setFactureCorespondantes(List<FactureDTO> factureCorespondantes) {
        this.factureCorespondantes = factureCorespondantes;
    }

    public String getNumbonComplementaire() {
        return numbonComplementaire;
    }

    public void setNumbonComplementaire(String numbonComplementaire) {
        this.numbonComplementaire = numbonComplementaire;
    }

    public Long getIdOrdonnance() {
        return idOrdonnance;
    }

    public void setIdOrdonnance(Long idOrdonnance) {
        this.idOrdonnance = idOrdonnance;
    }

    public Integer getCodeOperation() {
        return codeOperation;
    }

    public void setCodeOperation(Integer codeOperation) {
        this.codeOperation = codeOperation;
    }

    @Override
    public Integer getCodeCostCenterAnalytique() {
        return codeCostCenterAnalytique;
    }

    @Override
    public void setCodeCostCenterAnalytique(Integer codeCostCenterAnalytique) {
        this.codeCostCenterAnalytique = codeCostCenterAnalytique;
    }

    @Override
    public String toString() {
        return "FactureDTO{" + "memop=" + memop + ", codAnnul=" + codAnnul + ", datAnnul=" + datAnnul + ", designationDepot=" + designationDepot + ", mvtstoCollection=" + mvtstoCollection + ", codvend=" + codvend + ", datbon=" + datbon + ", datesys=" + datesys + ", heuresys=" + heuresys + ", numaffiche=" + numaffiche + ", client=" + client + '}';
    }

}
