package com.csys.pharmacie.prelevement.dto;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

public class RetourPrelevementDTO {

    private String numbon;

    private String codvend;

    private LocalDateTime datbon;

//attribut necessaire avec les rpt :cristal report ne supporte pas le type LocalDateTime 
    @JsonIgnore
    private Date datebonEdition;

    private LocalDate datesys;

    private LocalTime heuresys;

    private TypeBonEnum typbon;

    private String numaffiche;

    private CategorieDepotEnum categDepot;

    private BigDecimal montantFac;

    private BigDecimal mntbon;

    @Size(min = 0, max = 140)
    private String remarque;

    private Integer coddepartSrc;

    private String designationDepartSrc;

    private Integer coddepDesti;

    private String designationDepotDest;
    private String codeSaisiDepotDest;
    private String codeSaisiDepotSrc;

    @Valid
    @NotNull
    private List<DetailRetourPrelevementDTO> detailRetourPrelevementDTO;

    private String dateDebut;

    private String dateFin;

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

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public BigDecimal getMontantFac() {
        return montantFac;
    }

    public void setMontantFac(BigDecimal montantFac) {
        this.montantFac = montantFac;
    }

    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Integer getCoddepartSrc() {
        return coddepartSrc;
    }

    public void setCoddepartSrc(Integer coddepartSrc) {
        this.coddepartSrc = coddepartSrc;
    }

    public Integer getCoddepDesti() {
        return coddepDesti;
    }

    public void setCoddepDesti(Integer coddepDesti) {
        this.coddepDesti = coddepDesti;
    }

    public String getDesignationDepartSrc() {
        return designationDepartSrc;
    }

    public void setDesignationDepartSrc(String designationDepartSrc) {
        this.designationDepartSrc = designationDepartSrc;
    }

    public String getDesignationDepotDest() {
        return designationDepotDest;
    }

    public void setDesignationDepotDest(String designationDepotDest) {
        this.designationDepotDest = designationDepotDest;
    }

    public List<DetailRetourPrelevementDTO> getDetailRetourPrelevementDTO() {
        return detailRetourPrelevementDTO;
    }

    public void setDetailRetourPrelevementDTO(List<DetailRetourPrelevementDTO> detailRetourPrelevementDTO) {
        this.detailRetourPrelevementDTO = detailRetourPrelevementDTO;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }



    public Date getDatebonEdition() {
        return datebonEdition;
    }

    public void setDatebonEdition(Date datebonEdition) {
        this.datebonEdition = datebonEdition;
    }

    public String getCodeSaisiDepotDest() {
        return codeSaisiDepotDest;
    }

    public void setCodeSaisiDepotDest(String codeSaisiDepotDest) {
        this.codeSaisiDepotDest = codeSaisiDepotDest;
    }

    public String getCodeSaisiDepotSrc() {
        return codeSaisiDepotSrc;
    }

    public void setCodeSaisiDepotSrc(String codeSaisiDepotSrc) {
        this.codeSaisiDepotSrc = codeSaisiDepotSrc;
    }

}
