/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import com.csys.pharmacie.helper.MvtSto;
import java.io.Serializable;
import java.math.BigDecimal;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Hamdi
 */
@Entity
@Table(name = "MvtStoPR")
@NamedEntityGraph(name = "MvtStoPR.FacturePR",
        attributeNodes = {
            @NamedAttributeNode("facturePR")
        })
public class MvtStoPR extends MvtSto implements Serializable {

    @Column(name = "qtecom")
    private BigDecimal qtecom;

    @Column(name = "code_emplacement")
    private Integer codeEmplacement;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mvtStoPR")
    private List<DetailMvtStoPR> detailMvtStoPRList;

    @JoinColumn(name = "numbon", referencedColumnName = "numbon")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private FacturePR facturePR;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.getCode());
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
        final MvtStoPR other = (MvtStoPR) obj;
        if (!Objects.equals(this.qtecom, other.qtecom)) {
            return false;
        }
        if (!Objects.equals(this.detailMvtStoPRList, other.detailMvtStoPRList)) {
            return false;
        }
        return true;
    }

    public MvtStoPR() {
    }

    public FacturePR getFacturePR() {
        return facturePR;
    }

    public void setFacturePR(FacturePR facturePR) {
        this.facturePR = facturePR;
    }

    public List<DetailMvtStoPR> getDetailMvtStoPRList() {
        return detailMvtStoPRList;
    }

    public void setDetailMvtStoPRList(List<DetailMvtStoPR> detailMvtStoPRList) {
        this.detailMvtStoPRList = detailMvtStoPRList;
    }

    public BigDecimal getQtecom() {
        return qtecom;
    }

    public void setQtecom(BigDecimal qtecom) {
        this.qtecom = qtecom;
    }

    @Override
    public String toString() {
        return "MvtStoPR{" + "qtecom=" + qtecom + " codart=" + this.getCodart() + " unite=" + this.getUnite() + " designation=" + this.getDesart() + " datPer=" + this.getDatPer()
                + " lotInter=" + this.getLotinter() + "numbon=" + this.getNumbon()
                + '}';
    }

    public Integer getCodeEmplacement() {
        return codeEmplacement;
    }

    public void setCodeEmplacement(Integer codeEmplacement) {
        this.codeEmplacement = codeEmplacement;
    }

}
