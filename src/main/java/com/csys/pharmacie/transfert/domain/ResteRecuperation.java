/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "reste_recuperation")
@Audited
@AuditTable("reste_recuperation_aud")
@NamedQueries({
    @NamedQuery(name = "ResteRecuperation.findAll", query = "SELECT r FROM ResteRecuperation r")})
public class ResteRecuperation implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ResteRecuperationPK pk;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_unite")
    private int codeUnite;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite")
    private BigDecimal quantite;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "categ_depot")
    private CategorieDepotEnum categorieDepot;

    public ResteRecuperation() {
    }

    public ResteRecuperation(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public ResteRecuperation(ResteRecuperationPK resteRecuperationPK) {
        this.pk = resteRecuperationPK;
    }

    public ResteRecuperation(ResteRecuperationPK resteRecuperationPK, Integer codeUnite, BigDecimal quantite) {
        this.pk = resteRecuperationPK;
        this.codeUnite = codeUnite;
        this.quantite = quantite;
    }

    public ResteRecuperation(Integer codeArticle, Integer codeDepot) {
        this.pk = new ResteRecuperationPK(codeArticle, codeDepot);
    }

    public ResteRecuperationPK getPk() {
        return pk;
    }

    public void setPk(ResteRecuperationPK pk) {
        this.pk = pk;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public CategorieDepotEnum getCategorieDepot() {
        return categorieDepot;
    }

    public void setCategorieDepot(CategorieDepotEnum categorieDepot) {
        this.categorieDepot = categorieDepot;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pk != null ? pk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResteRecuperation)) {
            return false;
        }
        ResteRecuperation other = (ResteRecuperation) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.transfert.domain.ResteRecuperation[ resteRecuperationPK=" + pk + " ]";
    }

}
