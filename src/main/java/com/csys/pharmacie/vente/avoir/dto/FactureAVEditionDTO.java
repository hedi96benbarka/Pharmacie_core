package com.csys.pharmacie.vente.avoir.dto;

import com.csys.pharmacie.vente.quittance.dto.AdmissionDemandePECDTO;
import com.csys.pharmacie.helper.BaseTVADTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.vente.quittance.dto.FactureDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FactureAVEditionDTO {

    private BigDecimal mntbon;

    @Size(
            min = 0,
            max = 140
    )
    private String memop;

    @Size(
            min = 0,
            max = 10
    )
    private String numdoss;

    @NotNull
    private boolean imprimer;

    @Size(
            min = 0,
            max = 2
    )
    private Integer codeDepot;

    private List<MvtStoAVDTO> mvtStoAVCollection;

    private List<FactureDTO> listFacture;

    private List<BaseTVADTO> basetvaFactureAVCollection;

    @NotNull
    @Size(
            min = 1,
            max = 20
    )
    private String numbon;

    @Size(
            min = 0,
            max = 20
    )
    private String codvend;

    private Date datbon;

    private LocalDate datesys;

    private LocalTime heuresys;

    private TypeBonEnum typbon;

    @Size(
            min = 0,
            max = 16
    )
    private String numaffiche;

    private CategorieDepotEnum categDepot;
    
    private AdmissionDemandePECDTO client;

    private String designationDepot;
    
    private BigDecimal partiePatient;
    
    
    private BigDecimal partiePEC;
    
    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public String getNumdoss() {
        return numdoss;
    }

    public void setNumdoss(String numdoss) {
        this.numdoss = numdoss;
    }

    public boolean getImprimer() {
        return imprimer;
    }

    public void setImprimer(boolean imprimer) {
        this.imprimer = imprimer;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    public List<MvtStoAVDTO> getMvtStoAVCollection() {
        return mvtStoAVCollection;
    }

    public void setMvtStoAVCollection(List<MvtStoAVDTO> mvtStoAVCollection) {
        this.mvtStoAVCollection = mvtStoAVCollection;
    }

    public List<FactureDTO> getListFacture() {
        return listFacture;
    }

    public void setListFacture(List<FactureDTO> listFacture) {
        this.listFacture = listFacture;
    }

    public List<BaseTVADTO> getBasetvaFactureAVCollection() {
        return basetvaFactureAVCollection;
    }

    public void setBasetvaFactureAVCollection(List<BaseTVADTO> basetvaFactureAVCollection) {
        this.basetvaFactureAVCollection = basetvaFactureAVCollection;
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

    public Date getDatbon() {
        return datbon;
    }

    public void setDatbon(Date datbon) {
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

    public BigDecimal getPartiePEC() {
        return partiePEC;
    }

    public void setPartiePEC(BigDecimal partiePEC) {
        this.partiePEC = partiePEC;
    }
    
}
