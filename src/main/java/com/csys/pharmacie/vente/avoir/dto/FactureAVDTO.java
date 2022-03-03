package com.csys.pharmacie.vente.avoir.dto;

import com.csys.pharmacie.helper.BaseAvoirQuittance;
import com.csys.pharmacie.vente.quittance.dto.AdmissionDemandePECDTO;
import com.csys.pharmacie.vente.quittance.dto.FactureDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FactureAVDTO extends BaseAvoirQuittance {

    @Size(
            min = 0,
            max = 140
    )
    private String memop;

    @NotNull
    private boolean imprimer;

    private List<MvtStoAVDTO> mvtstoCollection;

    private List<FactureDTO> listFacture;

    @Size(
            min = 0,
            max = 20
    )
    private String codvend;

    private LocalDateTime datbon;

    private LocalDate datesys;

    private LocalTime heuresys;

    @Size(
            min = 0,
            max = 16
    )
    private String numaffiche;

    private AdmissionDemandePECDTO client;

    private String designationDepot;    
    
    private BigDecimal partiePatient;
    
    private String numQuittanceCorrespondant;
    
    private String numbonComplementaire;
    
    private BigDecimal partiePEC;
    

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public boolean getImprimer() {
        return imprimer;
    }

    public void setImprimer(boolean imprimer) {
        this.imprimer = imprimer;
    }

    public List<MvtStoAVDTO> getMvtstoCollection() {
        return mvtstoCollection;
    }

    public void setMvtstoCollection(List<MvtStoAVDTO> mvtStoAVCollection) {
        this.mvtstoCollection = mvtStoAVCollection;
    }

    public List<FactureDTO> getListFacture() {
        return listFacture;
    }

    public void setListFacture(List<FactureDTO> listFacture) {
        this.listFacture = listFacture;
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

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public BigDecimal getPartiePatient() {
        return partiePatient;
    }

    public void setPartiePatient(BigDecimal partiePatient) {
        this.partiePatient = partiePatient;
    }

    public String getNumQuittanceCorrespondant() {
        return numQuittanceCorrespondant;
    }

    public void setNumQuittanceCorrespondant(String numQuittanceCorrespondant) {
        this.numQuittanceCorrespondant = numQuittanceCorrespondant;
    }

    public BigDecimal getPartiePEC() {
        return partiePEC;
    }

    public void setPartiePEC(BigDecimal partiePEC) {
        this.partiePEC = partiePEC;
    }

    public String getNumbonComplementaire() {
        return numbonComplementaire;
    }

    public void setNumbonComplementaire(String numbonComplementaire) {
        this.numbonComplementaire = numbonComplementaire;
    }

    }
