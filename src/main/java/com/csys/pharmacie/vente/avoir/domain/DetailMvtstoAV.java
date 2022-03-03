/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.domain;

import com.csys.pharmacie.stock.domain.Depsto;
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
@Table(name = "detail_mvtstoAV")
@Audited
@AuditTable("detail_mvtstoAV_AUD")
public class DetailMvtstoAV implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code", nullable = false)
    private Long code;
    @Basic(optional = false)
    @Column(name = "code_detail_mvtsto")
    private long codeDetailMvtsto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "numordreMvtstoAV", nullable = false, length = 6)
    private String numordreMvtstoAV;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbonMvtstoAV", nullable = false, length = 20)
    private String numbonMvtstoAV;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codart", nullable = false)
    private long codart;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "qte", nullable = false, precision = 18, scale = 3)
    private BigDecimal qte;

    @Column(name = "code_tva")
    private Integer codeTva;

    @Column(name = "taux_tva")
    private BigDecimal tauxTva;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "PU", nullable = false, precision = 18, scale = 5)
    private BigDecimal pu;
    
    @JoinColumn(name = "code_depsto", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private Depsto depsto;
    @JoinColumns({
        @JoinColumn(name = "codart", referencedColumnName = "codart", insertable = false, updatable = false)
        , @JoinColumn(name = "numbonMvtstoAV", referencedColumnName = "numbon", insertable = false, updatable = false)
        , @JoinColumn(name = "numordreMvtstoAV", referencedColumnName = "numordre", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private MvtStoAV mvtStoAV;

    public DetailMvtstoAV() {
    }

    public DetailMvtstoAV(Long code) {
        this.code = code;
    }

    public DetailMvtstoAV(Long code, String numordreMvtstoAA, String numBonMvtstoAA, long codart, BigDecimal qte) {
        this.code = code;
        this.numordreMvtstoAV = numordreMvtstoAA;
        this.numbonMvtstoAV = numBonMvtstoAA;
        this.codart = codart;
        this.qte = qte;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public long getCodeDetailMvtsto() {
        return codeDetailMvtsto;
    }

    public void setCodeDetailMvtsto(long codeDetailMvtsto) {
        this.codeDetailMvtsto = codeDetailMvtsto;
    }

    public String getNumordreMvtstoAV() {
        return numordreMvtstoAV;
    }

    public void setNumordreMvtstoAV(String numordreMvtstoAV) {
        this.numordreMvtstoAV = numordreMvtstoAV;
    }

    public String getNumbonMvtstoAV() {
        return numbonMvtstoAV;
    }

    public void setNumbonMvtstoAV(String numbonMvtstoAV) {
        this.numbonMvtstoAV = numbonMvtstoAV;
    }

    public long getCodart() {
        return codart;
    }

    public void setCodart(long codart) {
        this.codart = codart;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public Depsto getDepsto() {
        return depsto;
    }

    public void setDepsto(Depsto depsto) {
        this.depsto = depsto;
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
        if (!(object instanceof DetailMvtstoAV)) {
            return false;
        }
        DetailMvtstoAV other = (DetailMvtstoAV) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.vente.avoir.domain.DepstomvtstoAA[ code=" + code + " ]";
    }

}
