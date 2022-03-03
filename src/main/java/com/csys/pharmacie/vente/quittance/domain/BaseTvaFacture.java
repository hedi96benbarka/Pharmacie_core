

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.domain;

import com.csys.pharmacie.helper.BaseTva;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "base_tva_facture")
@NamedQueries({
    @NamedQuery(name = "BaseTvaFacture.findAll", query = "SELECT b FROM BaseTvaFacture b")})
@Audited
@AuditTable("base_tva_facture_AUD")
@AuditOverride(forClass = BaseTva.class, isAudited = true)
public class BaseTvaFacture extends BaseTva implements Serializable {

    private static final long serialVersionUID = 1L;
    @JoinColumn(name = "facture", referencedColumnName = "numbon", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Facture facture;

    public BaseTvaFacture() {
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    

}
