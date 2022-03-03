package com.csys.pharmacie.prelevement.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.Size;

public class FacturePRDTO {

    @Size(min = 0, max = 140)
    private String remarque;

    private BigDecimal mntFac;

    private Integer coddepotSrc;
    private String depotDesignation;

    private Integer coddepartDesti;
    private String designationDepartDestination;

    private String numaffiche;

    private List<MvtStoPRDTO> details;

    private List<DemandePrDTO> demandeDPR;

    private MotifDTO motif;
    private String codAnnul;

    private LocalDateTime datAnnul;

    private Integer motifID;

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

    public List<DemandePrDTO> getDemandeDPR() {
        return demandeDPR;
    }

    public void setDemandeDPR(List<DemandePrDTO> demandeDPR) {
        this.demandeDPR = demandeDPR;
    }

    private LocalDateTime datbon;

    private LocalDate datesys;

    private LocalTime heuresys;

    private TypeBonEnum typbon;

    private CategorieDepotEnum categDepot;

    private String sourceCodeSaisi;

    private String destinationCodeSaisi;

    public String getRemarque() {
        return remarque;
    }

    public String getDepotDesignation() {
        return depotDesignation;
    }

    public void setDepotDesignation(String depotDesignation) {
        this.depotDesignation = depotDesignation;
    }

    public String getDesignationDepartDestination() {
        return designationDepartDestination;
    }

    public void setDesignationDepartDestination(String designationDepartDestination) {
        this.designationDepartDestination = designationDepartDestination;
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

    public List<MvtStoPRDTO> getDetails() {
        return details;
    }

    public void setDetails(List<MvtStoPRDTO> details) {
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

    public LocalDateTime getDatbon() {
        return datbon;
    }

    public void setDatbon(LocalDateTime datbon) {
        this.datbon = datbon;
    }

    @JsonIgnore
    public LocalDate getDatesys() {
        return datesys;
    }

    public void setDatesys(LocalDate datesys) {
        this.datesys = datesys;
    }

    @JsonIgnore
    public LocalTime getHeuresys() {
        return heuresys;
    }

    public void setHeuresys(LocalTime heuresys) {
        this.heuresys = heuresys;
    }

    @JsonIgnore
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

    public FacturePRDTO(String remarque, BigDecimal mntFac, Integer coddepotSrc, String depotDesignation, Integer coddepartDesti, String deparDestinationDesignation, String numaffiche, Integer motifID, String numbon, String codvend, LocalDateTime datbon, LocalDate datesys, LocalTime heuresys, CategorieDepotEnum categDepot) {
        this.remarque = remarque;
        this.mntFac = mntFac;
        this.coddepotSrc = coddepotSrc;
        this.depotDesignation = depotDesignation;
        this.coddepartDesti = coddepartDesti;
        this.designationDepartDestination = deparDestinationDesignation;
        this.numaffiche = numaffiche;
        this.motifID = motifID;
        this.numbon = numbon;
        this.codvend = codvend;
        this.datbon = datbon;
        this.datesys = datesys;
        this.heuresys = heuresys;
        this.categDepot = categDepot;
    }

    public FacturePRDTO() {
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

    public String getSourceCodeSaisi() {
        return sourceCodeSaisi;
    }

    public void setSourceCodeSaisi(String sourceCodeSaisi) {
        this.sourceCodeSaisi = sourceCodeSaisi;
    }

    public String getDestinationCodeSaisi() {
        return destinationCodeSaisi;
    }

    public void setDestinationCodeSaisi(String destinationCodeSaisi) {
        this.destinationCodeSaisi = destinationCodeSaisi;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FacturePRDTO other = (FacturePRDTO) obj;
        if (!Objects.equals(this.numbon, other.numbon)) {
            return false;
        }
        return true;
    }

}
