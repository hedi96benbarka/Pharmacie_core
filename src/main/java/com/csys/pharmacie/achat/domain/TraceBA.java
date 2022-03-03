/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author farouk
 */
@Entity
@Table(name = "TraceBA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TraceBA.findAll", query = "SELECT t FROM TraceBA t"),
//    @NamedQuery(name = "TraceBA.findDistinctByNumBon", query = "SELECT t FROM TraceBA t WHERE t.traceBAPK.codArt IN (SELECT DISTINCT t.traceBAPK.codArt FROM TraceBA t WHERE t.traceBAPK.dateSys >= :datbond  and t.traceBAPK.dateSys <= :datbonf order by t.traceBAPK.dateSys desc)"),
    @NamedQuery(name = "TraceBA.findNumBCByNumBon", query = "SELECT t.traceBAPK.numBc FROM TraceBA t WHERE t.traceBAPK.numBon = :numBon"),
    @NamedQuery(name = "TraceBA.findByNumBon", query = "SELECT t FROM TraceBA t WHERE t.traceBAPK.numBon = :numBon"),
    @NamedQuery(name = "TraceBA.findByCodArt", query = "SELECT t FROM TraceBA t WHERE t.traceBAPK.codArt = :codArt"),
    @NamedQuery(name = "TraceBA.findByQuantite", query = "SELECT t FROM TraceBA t WHERE t.quantite = :quantite"),
    @NamedQuery(name = "TraceBA.findByPriUni", query = "SELECT t FROM TraceBA t WHERE t.priUni = :priUni"),
    @NamedQuery(name = "TraceBA.findByNumBc", query = "SELECT t FROM TraceBA t WHERE t.traceBAPK.numBc = :numBc"),
    @NamedQuery(name = "TraceBA.findByCodFrs", query = "SELECT t FROM TraceBA t WHERE t.codFrs = :codFrs"),
    @NamedQuery(name = "TraceBA.findByUserCre", query = "SELECT t FROM TraceBA t WHERE t.userCre = :userCre"),
    @NamedQuery(name = "TraceBA.findByUserDel", query = "SELECT t FROM TraceBA t WHERE t.userDel = :userDel"),
    @NamedQuery(name = "TraceBA.findByDateSys", query = "SELECT t FROM TraceBA t WHERE t.traceBAPK.dateSys = :dateSys"),
    @NamedQuery(name = "TraceBA.findByHeureSys", query = "SELECT t FROM TraceBA t WHERE t.heureSys = :heureSys"),
    @NamedQuery(name = "TraceBA.findByDateAnnul", query = "SELECT t FROM TraceBA t WHERE t.dateAnnul = :dateAnnul"),
    @NamedQuery(name = "TraceBA.findByHeureAnnul", query = "SELECT t FROM TraceBA t WHERE t.heureAnnul = :heureAnnul"),
    @NamedQuery(name = "TraceBA.findByAutomatique", query = "SELECT t FROM TraceBA t WHERE t.automatique = :automatique"),
    @NamedQuery(name = "TraceBA.findByNumFe", query = "SELECT t FROM TraceBA t WHERE t.numFe = :numFe"),
    @NamedQuery(name = "TraceBA.findByRefFrs", query = "SELECT t FROM TraceBA t WHERE t.refFrs = :refFrs"),
    @NamedQuery(name = "TraceBA.findByDatFrs", query = "SELECT t FROM TraceBA t WHERE t.datFrs = :datFrs"),
    @NamedQuery(name = "TraceBA.findByMntBon", query = "SELECT t FROM TraceBA t WHERE t.mntBon = :mntBon")})
public class TraceBA implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TraceBAPK traceBAPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Quantite")
    private BigDecimal quantite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PriUni")
    private BigDecimal priUni;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CodFrs")
    private String codFrs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "UserCre")
    private String userCre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "UserDel")
    private String userDel;
    @Column(name = "HeureSys")
    @Temporal(TemporalType.TIMESTAMP)
    private Date heureSys;
    @Column(name = "DateAnnul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAnnul;
    @Column(name = "HeureAnnul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date heureAnnul;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Automatique")
    private boolean automatique;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "NumFe")
    private String numFe;
    @Size(max = 20)
    @Column(name = "RefFrs")
    private String refFrs;
    @Column(name = "datFrs")
    @Temporal(TemporalType.DATE)
    private Date datFrs;
    @Column(name = "mntBon")
    private BigDecimal mntBon;
    
    public TraceBA() {
    }

    public TraceBA(TraceBAPK traceBAPK) {
        this.traceBAPK = traceBAPK;
    }

    public TraceBA(TraceBAPK traceBAPK, BigDecimal quantite, BigDecimal priUni, String codFrs, String userCre, String userDel, boolean automatique, String numFe) {
        this.traceBAPK = traceBAPK;
        this.quantite = quantite;
        this.priUni = priUni;
        this.codFrs = codFrs;
        this.userCre = userCre;
        this.userDel = userDel;
        this.automatique = automatique;
        this.numFe = numFe;
    }

  

    public TraceBAPK getTraceBAPK() {
        return traceBAPK;
    }

    public void setTraceBAPK(TraceBAPK traceBAPK) {
        this.traceBAPK = traceBAPK;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPriUni() {
        return priUni;
    }

    public void setPriUni(BigDecimal priUni) {
        this.priUni = priUni;
    }

    public String getCodFrs() {
        return codFrs;
    }

    public void setCodFrs(String codFrs) {
        this.codFrs = codFrs;
    }

    public String getUserCre() {
        return userCre;
    }

    public void setUserCre(String userCre) {
        this.userCre = userCre;
    }

    public String getUserDel() {
        return userDel;
    }

    public void setUserDel(String userDel) {
        this.userDel = userDel;
    }

    public Date getHeureSys() {
        return heureSys;
    }

    public void setHeureSys(Date heureSys) {
        this.heureSys = heureSys;
    }

    public Date getDateAnnul() {
        return dateAnnul;
    }

    public void setDateAnnul(Date dateAnnul) {
        this.dateAnnul = dateAnnul;
    }

    public Date getHeureAnnul() {
        return heureAnnul;
    }

    public void setHeureAnnul(Date heureAnnul) {
        this.heureAnnul = heureAnnul;
    }

    public boolean getAutomatique() {
        return automatique;
    }

    public void setAutomatique(boolean automatique) {
        this.automatique = automatique;
    }

    public String getNumFe() {
        return numFe;
    }

    public void setNumFe(String numFe) {
        this.numFe = numFe;
    }

    public String getRefFrs() {
        return refFrs;
    }

    public void setRefFrs(String refFrs) {
        this.refFrs = refFrs;
    }

    public Date getDatFrs() {
        return datFrs;
    }

    public void setDatFrs(Date datFrs) {
        this.datFrs = datFrs;
    }

    public BigDecimal getMntBon() {
        return mntBon;
    }

    public void setMntBon(BigDecimal mntBon) {
        this.mntBon = mntBon;
    }

 @Override
    public int hashCode() {
        int hash = 0;
        hash += (traceBAPK != null ? traceBAPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TraceBA)) {
            return false;
        }
        TraceBA other = (TraceBA) object;
        if ((this.traceBAPK == null && other.traceBAPK != null) || (this.traceBAPK != null && !this.traceBAPK.equals(other.traceBAPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.achat.model.pharmacie.TraceBA[ traceBAPK=" + traceBAPK + " ]";
    }
    
}
