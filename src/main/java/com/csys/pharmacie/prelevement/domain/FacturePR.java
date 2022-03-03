/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import static com.crystaldecisions12.reports.common.RootCauseID.T;
import com.csys.pharmacie.helper.BaseBon;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import com.csys.pharmacie.prelevement.domain.MvtStoPR;
import javax.persistence.PrePersist;

/**
 *
 * @author Hamdi
 */
@Entity
@Table(name = "FacturePR")
@NamedEntityGraph(name = "FacturePR.motif",
                  attributeNodes = {
                      @NamedAttributeNode("motif")
                  })
public class FacturePR extends BaseBon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "CodAnnul", nullable = false, length = 10)
    private String codAnnul;
    @Column(name = "DatAnnul")
    private LocalDateTime datAnnul;

    @Size(max = 140)
    @Column(name = "remarque")
    private String remarque;

    @Column(name = "montant_fac")
    private BigDecimal mntFac;

    @Column(name = "coddep_src")
    private Integer coddepotSrc;

    @Column(name = "coddepart_desti")
    private Integer coddepartDest;

    @Column(name = "code_cost_center")
    private Integer codeCostCenter;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numbon")
    private List< MvtStoPR> detailFacturePRCollection;

    @ManyToOne
    @JoinColumn(name = "idMotif", referencedColumnName = "idMotif")
    private Motif motif;
    
    
    @Column(name = "integrer")
    private Boolean integrer;

    public List<MvtStoPR> getDetailFacturePRCollection() {
        return detailFacturePRCollection;
    }

    public void setDetailFacturePRCollection(List<MvtStoPR> detailFacturePRCollection) {
        this.detailFacturePRCollection = detailFacturePRCollection;
    }

    public FacturePR() {
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturePR)) {
            return false;
        }
        FacturePR other = (FacturePR) object;
        if ((this.getNumbon() == null && other.getNumbon() != null) || (this.getNumbon() != null && !this.getNumbon().equals(other.getNumbon()))) {
            return false;
        }
        return true;
    }

    public FacturePR(BigDecimal mntbon, String remarque, BigDecimal mntFac, Integer coddepotSrc, Integer coddepartDest, List<MvtStoPR> detailFacturePRCollection, Motif motif) {

        this.remarque = remarque;
        this.mntFac = mntFac;
        this.coddepotSrc = coddepotSrc;
        this.coddepartDest = coddepartDest;
        this.detailFacturePRCollection = detailFacturePRCollection;
        this.motif = motif;
    }

    public FacturePR(String remarque, BigDecimal mntFac, String numaffiche, Integer coddepotSrc, Integer coddepartDest) {
        this.remarque = remarque;
        this.mntFac = mntFac;

        this.coddepotSrc = coddepotSrc;
        this.coddepartDest = coddepartDest;
    }

    public String getRemarque() {
        return remarque;
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

    public void setCoddepotSrc(Integer coddepotSrc) {
        this.coddepotSrc = coddepotSrc;
    }

    public Integer getCoddepartDest() {
        return coddepartDest;
    }

    public void setCoddepartDest(Integer coddepartDest) {
        this.coddepartDest = coddepartDest;
    }

    public Motif getMotif() {
        return motif;
    }

    public void setMotif(Motif motif) {
        this.motif = motif;
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

    public Boolean getIntegrer() {
        return integrer;
    }

    public void setIntegrer(Boolean integrer) {
        this.integrer = integrer;
    }

    public Integer getCodeCostCenter() {
        return codeCostCenter;
    }

    public void setCodeCostCenter(Integer codeCostCenter) {
        this.codeCostCenter = codeCostCenter;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getNumbon() != null ? getNumbon().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.prelevement.domain.FacturePR[ numbon=" + getNumbon() + " ]";
    }

}
