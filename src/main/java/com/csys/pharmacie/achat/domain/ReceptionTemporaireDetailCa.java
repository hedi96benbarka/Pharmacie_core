/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author Administrateur
 */
@Entity
@Audited
@Table(name = "reception_temporaire_detail_ca")
@AuditTable("reception_temporaire_detail_ca_AUD")
public class ReceptionTemporaireDetailCa implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReceptionTemporaireDetailCaPK receptionTemporaireDetailCaPK;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_receptione")
    private BigDecimal quantiteReceptione;
    
    @Column(name = "quantite_gratuite")
    private BigDecimal quantiteGratuite;
    
    @MapsId("receptionTemporaire")
    @JoinColumn(name = "receptionTemporaire", referencedColumnName = "numbon")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReceptionTemporaire receptionTemporaire;

    public ReceptionTemporaireDetailCa() {
    }

    public ReceptionTemporaireDetailCa(BigDecimal quantiteReceptione, BigDecimal quantiteGratuite) {
        this.quantiteReceptione = quantiteReceptione;
        this.quantiteGratuite = quantiteGratuite;
    }

    
        public ReceptionTemporaireDetailCa(String recep, Integer commandeAchat, Integer article, BigDecimal quantiteReceptione, BigDecimal quantiteGratuite) {
        this.receptionTemporaireDetailCaPK = new ReceptionTemporaireDetailCaPK(recep, commandeAchat, article);
        this.quantiteReceptione = quantiteReceptione;
        this.quantiteGratuite = quantiteGratuite;
    }
    
    public ReceptionTemporaireDetailCa(ReceptionTemporaireDetailCaPK receptionTemporaireDetailCaPK, BigDecimal quantiteReceptione, BigDecimal quantiteGratuite, ReceptionTemporaire receptionTemporaire1) {
        this.receptionTemporaireDetailCaPK = receptionTemporaireDetailCaPK;
        this.quantiteReceptione = quantiteReceptione;
        this.quantiteGratuite = quantiteGratuite;
        this.receptionTemporaire = receptionTemporaire1;
    }

    public ReceptionTemporaireDetailCa(ReceptionTemporaireDetailCaPK receptionTemporaireDetailCaPK) {
        this.receptionTemporaireDetailCaPK = receptionTemporaireDetailCaPK;
    }

    public ReceptionTemporaireDetailCa(ReceptionTemporaireDetailCaPK receptionTemporaireDetailCaPK, BigDecimal quantiteReceptione) {
        this.receptionTemporaireDetailCaPK = receptionTemporaireDetailCaPK;
        this.quantiteReceptione = quantiteReceptione;
    }

    public ReceptionTemporaireDetailCa(String receptionTemporaire, int commandeAchat, int article) {
        this.receptionTemporaireDetailCaPK = new ReceptionTemporaireDetailCaPK(receptionTemporaire, commandeAchat, article);
    }

    public ReceptionTemporaireDetailCaPK getReceptionTemporaireDetailCaPK() {
        return receptionTemporaireDetailCaPK;
    }

    public void setReceptionTemporaireDetailCaPK(ReceptionTemporaireDetailCaPK receptionTemporaireDetailCaPK) {
        this.receptionTemporaireDetailCaPK = receptionTemporaireDetailCaPK;
    }

    public BigDecimal getQuantiteReceptione() {
        return quantiteReceptione;
    }

    public void setQuantiteReceptione(BigDecimal quantiteReceptione) {
        this.quantiteReceptione = quantiteReceptione;
    }

    public BigDecimal getQuantiteGratuite() {
        return quantiteGratuite;
    }

    public void setQuantiteGratuite(BigDecimal quantiteGratuite) {
        this.quantiteGratuite = quantiteGratuite;
    }

    public ReceptionTemporaire getReceptionTemporaire1() {
        return receptionTemporaire;
    }

    public void setReceptionTemporaire1(ReceptionTemporaire receptionTemporaire1) {
        this.receptionTemporaire= receptionTemporaire1;
    }

 

  
    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCa[ receptionTemporaireDetailCaPK=" + receptionTemporaireDetailCaPK + " ]";
    }
   
}
