package com.csys.pharmacie.prelevement.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FacturePREditionDTO {

    private String numbon;
    private String remarque;

    private BigDecimal mntFac;

    private Integer coddepotSrc;
    
    private String depotDesignation;

    private Integer coddepartDesti;
    
    private String deparDestinationDesignation;

    private String numaffiche;

    private List<MvtStoPREditionDTO> details;

    private String listdpr;  

    private MotifDTO motif;

    private Integer motifID;

    private String codvend;

    private Date datbon;

    private TypeBonEnum typbon;

    private CategorieDepotEnum categDepot;

    public String getListdpr() {
        return listdpr;
    }

    public void setListdpr(String listdpr) {
        this.listdpr = listdpr;
    }
    
    

    public String getRemarque() {
        return remarque;
    }

    public String getDepotDesignation() {
        return depotDesignation;
    }

    public void setDepotDesignation(String depotDesignation) {
        this.depotDesignation = depotDesignation;
    }

    public String getDeparDestinationDesignation() {
        return deparDestinationDesignation;
    }

    public void setDeparDestinationDesignation(String deparDestinationDesignation) {
        this.deparDestinationDesignation = deparDestinationDesignation;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public BigDecimal getMntFac() {
        return mntFac;
    }

    public void setMntFac(BigDecimal mntFac) {
        this.mntFac = mntFac;
    }

    public Integer getCoddepotSrc() {
        return coddepotSrc;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public void setCoddepotSrc(Integer coddepotSrc) {
        this.coddepotSrc = coddepotSrc;
    }

    public Integer getCoddepartDesti() {
        return coddepartDesti;
    }

    public void setCoddepartDesti(Integer coddepartDesti) {
        this.coddepartDesti = coddepartDesti;
    }

    public List<MvtStoPREditionDTO> getDetails() {
        return details;
    }

    public void setDetails(List<MvtStoPREditionDTO> details) {
        this.details = details;
    }

   

    public Integer getMotifID() {
        return motifID;
    }

    public void setMotifID(Integer motif) {
        this.motifID = motif;
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

    public TypeBonEnum getTypbon() {
        return typbon;
    }

    public void setTypbon(TypeBonEnum typbon) {
        this.typbon = typbon;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public MotifDTO getMotif() {
        return motif;
    }

    public void setMotif(MotifDTO motif) {
        this.motif = motif;
    }

    public FacturePREditionDTO(String remarque, BigDecimal mntFac, Integer coddepotSrc, String depotDesignation, Integer coddepartDesti, String deparDestinationDesignation, String numaffiche, Integer motifID, String numbon, String codvend, Date datbon, CategorieDepotEnum categDepot) {
        this.remarque = remarque;
        this.mntFac = mntFac;
        this.coddepotSrc = coddepotSrc;
        this.depotDesignation = depotDesignation;
        this.coddepartDesti = coddepartDesti;
        this.deparDestinationDesignation = deparDestinationDesignation;
        this.numaffiche = numaffiche;
        this.motifID = motifID;
        this.numbon = numbon;
        this.codvend = codvend;
        this.datbon = datbon;
        this.categDepot = categDepot;
    }

    public FacturePREditionDTO() {
    }

}
