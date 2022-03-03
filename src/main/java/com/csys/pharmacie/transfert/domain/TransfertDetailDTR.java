/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author bassatine
 */
@Entity
@Audited
@Table(name = "Transfert_DetailDTR ")
@AuditTable("Transfert_DetailDTR_AUD")
public class TransfertDetailDTR implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TransfertDetailDTRPK pk;

    @Basic(optional = false)
    @NotNull
    @Column(name = "code_DTR")
    private Integer codeDTR;

    @Column(name = "quantite_transfert")
    private BigDecimal quantiteTransferred;

    @MapsId("reception")
    @JoinColumn(name = " code_transfert", referencedColumnName = "numbon")
    @ManyToOne(fetch = FetchType.LAZY)
    private FactureBT facturebt;

    public TransfertDetailDTRPK getPk() {
        return pk;
    }

    public void setPk(TransfertDetailDTRPK pk) {
        this.pk = pk;
    }

    public Integer getCodeDTR() {
        return codeDTR;
    }

    public void setCodeDTR(Integer codeDTR) {
        this.codeDTR = codeDTR;
    }

    public BigDecimal getQuantiteTransferred() {
        return quantiteTransferred;
    }

    public void setQuantiteTransferred(BigDecimal quantiteTransferred) {
        this.quantiteTransferred = quantiteTransferred;
    }

    public FactureBT getFacturebt() {
        return facturebt;
    }

    public void setFacturebt(FactureBT facturebt) {
        this.facturebt = facturebt;
    }

  

    public TransfertDetailDTR(TransfertDetailDTRPK pk, Integer codeDTR, BigDecimal quantiteTransferred, FactureBT reception) {
        this.pk = pk;
        this.codeDTR = codeDTR;
        this.quantiteTransferred = quantiteTransferred;
        this.facturebt = reception;
    }

    public TransfertDetailDTR() {
    }

    @Override
    public String toString() {
        return "TransfertDetailDTR{" + "pk=" + pk + ", codeDTR=" + codeDTR + ", quantiteTransferred=" + quantiteTransferred + ", reception=" + facturebt + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.pk);
        hash = 59 * hash + Objects.hashCode(this.codeDTR);
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
        final TransfertDetailDTR other = (TransfertDetailDTR) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        if (!Objects.equals(this.codeDTR, other.codeDTR)) {
            return false;
        }
        return true;
    }

}
