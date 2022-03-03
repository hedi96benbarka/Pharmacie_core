/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.domain;

import com.csys.pharmacie.helper.BaseTva;
import java.io.Serializable;
import javax.persistence.Entity;
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
@Table(name = "base_tva_FactureAV")
@NamedQueries({
    @NamedQuery(name = "BasetvaFactureAV.findAll", query = "SELECT b FROM BasetvaFactureAV b")})
@Audited
@AuditTable("base_tva_FactureAV_AUD")
@AuditOverride(forClass = BaseTva.class, isAudited = true)
public class BasetvaFactureAV extends BaseTva implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @JoinColumn(name = "factureAV", referencedColumnName = "numbon", nullable = false)
    @ManyToOne(optional = false)
    private FactureAV factureAV;

    public BasetvaFactureAV() {
    }

    public FactureAV getFactureAV() {
        return factureAV;
    }

    public void setFactureAV(FactureAV factureAV) {
        this.factureAV = factureAV;
    }
    
}
