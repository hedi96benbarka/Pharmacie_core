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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Farouk
 *
 * This entity holds the details of purchase orders (CA) used to form a "Bon de
 * reception"
 * <\p> For example if we have a "Bon de reception " that is made from 3
 * purchase orders that each contains 3 purchase details, we will find 9 entites
 * of this type with each one containing the id of the "bon de reception",the id
 * of the detail of the purchase order and the quantitie that we used from this
 * detail.
 */
@Audited
@Entity
@Table(name = "reception_detail_ca")
@AuditTable("reception_detail_ca_AUD")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "reception_detail_ca.reception",
                      attributeNodes = {
                          @NamedAttributeNode("reception")
                      })

})
public class ReceptionDetailCA implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReceptionDetailCAPK pk;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_receptione")
    private BigDecimal quantiteReceptione;
    
      @Column(name = "quantite_gratuite")
    private BigDecimal quantiteGratuite;

    @MapsId("reception")
    @JoinColumn(name = "reception", referencedColumnName = "numbon")
    @ManyToOne( fetch = FetchType.LAZY)
    private FactureBA reception;

    public ReceptionDetailCA() {
    }

    public ReceptionDetailCA(BigDecimal quantiteReceptione) {
        this.quantiteReceptione = quantiteReceptione;
    }
    
    

    public ReceptionDetailCA(String recep, Integer commandeAchat, Integer article, BigDecimal quantiteReceptione) {
        this.pk = new ReceptionDetailCAPK(recep, commandeAchat, article);
        this.quantiteReceptione = quantiteReceptione;
    }

    public ReceptionDetailCA(String recep, Integer commandeAchat, Integer article, BigDecimal quantiteReceptione, BigDecimal quantiteGratuite) {
        this.pk = new ReceptionDetailCAPK(recep, commandeAchat, article);
        this.quantiteReceptione = quantiteReceptione;
        this.quantiteGratuite = quantiteGratuite;
    }


    
    public ReceptionDetailCA(ReceptionDetailCAPK receptionDetailCaPK) {
        this.pk = receptionDetailCaPK;
    }

    public ReceptionDetailCA(ReceptionDetailCAPK receptionDetailCaPK, BigDecimal quantiteReceptione) {
        this.pk = receptionDetailCaPK;
        this.quantiteReceptione = quantiteReceptione;
    }

    public ReceptionDetailCA(FactureBA reception, Integer commandeAchat, Integer article, BigDecimal quantiteReceptione, BigDecimal quantiteGratuite) {
        this.pk = new ReceptionDetailCAPK(reception.getNumbon(), commandeAchat, article);
        this.reception = reception;
        this.quantiteReceptione = quantiteReceptione;
        this.quantiteGratuite = quantiteGratuite;
    }

    public ReceptionDetailCA(String reception, Integer commandeAchat, Integer article) {
        this.pk = new ReceptionDetailCAPK(reception, commandeAchat, article);
    }

    public ReceptionDetailCAPK getPk() {
        return pk;
    }

    public void setPk(ReceptionDetailCAPK pk) {
        this.pk = pk;
    }

    public BigDecimal getQuantiteReceptione() {
        return quantiteReceptione;
    }

    public void setQuantiteReceptione(BigDecimal quantiteReceptione) {
        this.quantiteReceptione = quantiteReceptione;
    }

    public FactureBA getReception() {
        return reception;
    }

    public void setReception(FactureBA reception) {
        this.reception = reception;
    }

    public BigDecimal getQuantiteGratuite() {
        return quantiteGratuite;
    }

    public void setQuantiteGratuite(BigDecimal quantiteGratuite) {
        this.quantiteGratuite = quantiteGratuite;
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
        if (!(object instanceof ReceptionDetailCA)) {
            return false;
        }
        ReceptionDetailCA other = (ReceptionDetailCA) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReceptionDetailCA{" + "pk=" + pk + ", quantiteReceptione=" + quantiteReceptione + ", quantiteGratuite=" + quantiteGratuite + ", reception=" + reception + '}';
    }

   

}
