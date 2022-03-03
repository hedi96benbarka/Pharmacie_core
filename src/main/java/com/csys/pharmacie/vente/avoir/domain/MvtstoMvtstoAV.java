/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.domain;

import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "mvtsto_mvtstoAV")
@Audited
@AuditTable("mvtsto_mvtstoAV_AUD")
public class MvtstoMvtstoAV implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code", nullable = false)
    private Long code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "numordreMvtstoAV", nullable = false, length = 6)
    private String numordreMvtstoAV;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbonMvtstoAV", nullable = false, length = 20)
    private String numBonMvtstoAV;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codart", nullable = false)
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "numordreMvtsto", nullable = false, length = 6)
    private String numordreMvtsto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NumBonMvtsto", nullable = false, length = 20)
    private String numBonMvtsto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "qte", nullable = false, precision = 18, scale = 3)
    private BigDecimal qte;

    @JoinColumns({
        @JoinColumn(name = "codart", referencedColumnName = "codart", insertable = false, updatable = false)
        , @JoinColumn(name = "NumBonMvtsto", referencedColumnName = "numbon", insertable = false, updatable = false)
        , @JoinColumn(name = "numordreMvtsto", referencedColumnName = "numordre", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mvtsto mvtsto;

    @JoinColumns({
        @JoinColumn(name = "codart", referencedColumnName = "codart", insertable = false, updatable = false)
        , @JoinColumn(name = "numbonMvtstoAV", referencedColumnName = "numbon", insertable = false, updatable = false)
        , @JoinColumn(name = "numordreMvtstoAV", referencedColumnName = "numordre", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private MvtStoAV mvtStoAV;

    public MvtstoMvtstoAV() {
    }

    public MvtstoMvtstoAV(Long code) {
        this.code = code;
    }

    public MvtstoMvtstoAV(Long code, String numordreMvtstoAA, String numBonMvtstoAA, Integer codart, String numordreMvtsto, String numBonMvtsto, BigDecimal qte) {
        this.code = code;
        this.numordreMvtstoAV = numordreMvtstoAA;
        this.numBonMvtstoAV = numBonMvtstoAA;
        this.codart = codart;
        this.numordreMvtsto = numordreMvtsto;
        this.numBonMvtsto = numBonMvtsto;
        this.qte = qte;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getNumordreMvtstoAV() {
        return numordreMvtstoAV;
    }

    public void setNumordreMvtstoAV(String numordreMvtstoAV) {
        this.numordreMvtstoAV = numordreMvtstoAV;
    }

    public String getNumBonMvtstoAV() {
        return numBonMvtstoAV;
    }

    public void setNumBonMvtstoAV(String numBonMvtstoAV) {
        this.numBonMvtstoAV = numBonMvtstoAV;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getNumordreMvtsto() {
        return numordreMvtsto;
    }

    public void setNumordreMvtsto(String numordreMvtsto) {
        this.numordreMvtsto = numordreMvtsto;
    }

    public String getNumBonMvtsto() {
        return numBonMvtsto;
    }

    public void setNumBonMvtsto(String numBonMvtsto) {
        this.numBonMvtsto = numBonMvtsto;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public Mvtsto getMvtsto() {
        return mvtsto;
    }

    public void setMvtsto(Mvtsto mvtsto) {
        this.mvtsto = mvtsto;
    }

    public MvtStoAV getMvtStoAV() {
        return mvtStoAV;
    }

    public void setMvtStoAV(MvtStoAV mvtStoAV) {
        this.mvtStoAV = mvtStoAV;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MvtstoMvtstoAV)) {
            return false;
        }
        MvtstoMvtstoAV other = (MvtstoMvtstoAV) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.vente.avoir.domain.MvtstomvtstoAA[ code=" + code + " ]";
    }

}
