/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
@Entity

@Table(name = "ConsomationReels")
@NamedQueries({
    @NamedQuery(name = "ConsomationReels.findAll", query = "SELECT c FROM ConsomationReels c")})
public class ConsomationReels implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 257)
    @Column(name = "id")
    private String id;
    @Size(max = 2)
    @Column(name = "typbon")
    private String typbon;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codart")
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbon")
    private String numbon;
    @Size(max = 20)
    @Column(name = "numaffiche")
    private String numaffiche;
    @Column(name = "codTvaAch")
    private Integer codTvaAch;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "tauTvaAch")
    private BigDecimal tauTvaAch;
    @Basic(optional = false)
    @NotNull
    @Column(name = "priach")
    private BigDecimal priach;
    @Size(max = 255)
    @Column(name = "desart")
    private String desart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "desArtSec")
    private String desArtSec;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codeSaisi")
    private String codeSaisi;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite")
    private BigDecimal quantite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "coddep")
    private int coddep;
    @Size(max = 50)
    @Column(name = "designationDepot")
    private String designationDepot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "designationDepotSec")
    private String designationDepotSec;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codeUnite")
    private Integer codeUnite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "designationUnite")
    private String designationUnite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "designationUniteSec")
    private String designationUniteSec;
    @Column(name = "date")
    private LocalDateTime date;
    @Size(max = 10)
    @Column(name = "categ_depot")
    private String categDepot;

    public ConsomationReels() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypbon() {
        return typbon;
    }

    public void setTypbon(String typbon) {
        this.typbon = typbon;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public Integer getCodTvaAch() {
        return codTvaAch;
    }

    public void setCodTvaAch(Integer codTvaAch) {
        this.codTvaAch = codTvaAch;
    }

    public BigDecimal getTauTvaAch() {
        return tauTvaAch;
    }

    public void setTauTvaAch(BigDecimal tauTvaAch) {
        this.tauTvaAch = tauTvaAch;
    }

    public BigDecimal getPriach() {
        return priach;
    }

    public void setPriach(BigDecimal priach) {
        this.priach = priach;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesArtSec() {
        return desArtSec;
    }

    public void setDesArtSec(String desArtSec) {
        this.desArtSec = desArtSec;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public int getCoddep() {
        return coddep;
    }

    public void setCoddep(int coddep) {
        this.coddep = coddep;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public String getDesignationDepotSec() {
        return designationDepotSec;
    }

    public void setDesignationDepotSec(String designationDepotSec) {
        this.designationDepotSec = designationDepotSec;
    }

    public int getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    public String getDesignationUniteSec() {
        return designationUniteSec;
    }

    public void setDesignationUniteSec(String designationUniteSec) {
        this.designationUniteSec = designationUniteSec;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(String categDepot) {
        this.categDepot = categDepot;
    }
    
}
