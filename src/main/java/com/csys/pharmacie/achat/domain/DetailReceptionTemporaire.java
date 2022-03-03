/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.MvtSto;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "detail_reception_temporaire")
//@Audited
//@AuditTable("detail_reception_temporaire_AUD")
public class DetailReceptionTemporaire extends MvtSto  implements Serializable {

    private static final long serialVersionUID = 1L;
 
  
    @Column(name = "qtecom")
    private BigDecimal quantiteCommande;
    @Column(name = "remise")
    private BigDecimal remise;
    @Column(name = "montht")
    private BigDecimal montht;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tautva")
    private BigDecimal tauxTva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codtva")
    private Integer codeTva;
    @Column(name = "is_prix_reference")
    private Boolean isPrixReference;
    @Column(name = "prix_vente")
    private BigDecimal sellingPrice;
    @Column(name = "base_tva")
    private BigDecimal baseTva;
    @Column(name = "pmp_precedant")
    private BigDecimal pmpPrecedant;
    @Column(name = "quantite_precedante")
    private BigDecimal quantitePrecedante;
    @Column(name = "old_codtva")
    private Integer oldCodtva;
    @Column(name = "old_tautva")
    private BigDecimal oldTautva;
    @Column(name = "code_emplacement")
    private Integer codeEmplacement;
    
    @JoinColumn(name = "numbon", referencedColumnName = "numbon")
    @ManyToOne
    private ReceptionTemporaire reception;

    public DetailReceptionTemporaire() {
    }


    public void setQtecom(BigDecimal qtecom) {
        this.quantiteCommande = qtecom;
    }
    
    public BigDecimal getQuantiteCommande() {
        return quantiteCommande ;
    }
    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getMontht() {
        return montht;
    }

    public void setMontht(BigDecimal montht) {
        this.montht = montht;
    }



    public BigDecimal getTautva() {
        return tauxTva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tauxTva = tautva;
    }
 public int getCodtva() {
        return codeTva;
    }
  public void setCodtva(int codtva) {
        this.codeTva = codtva;
    }
    public Boolean getIsPrixReference() {         
        return isPrixReference;
    }

    public void setIsPrixReference(Boolean isPrixReference) {
        this.isPrixReference = isPrixReference;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getBaseTva() {
        return baseTva;
    }

    public void setBaseTva(BigDecimal baseTva) {
        this.baseTva = baseTva;
    }

    public BigDecimal getPmpPrecedant() {
        return pmpPrecedant;
    }

    public void setPmpPrecedant(BigDecimal pmpPrecedant) {
        this.pmpPrecedant = pmpPrecedant;
    }

    public BigDecimal getQuantitePrecedante() {
        return quantitePrecedante;
    }

    public void setQuantitePrecedante(BigDecimal quantitePrecedante) {
        this.quantitePrecedante = quantitePrecedante;
    }

    public Integer getOldCodtva() {
        return oldCodtva;
    }

    public void setOldCodtva(Integer oldCodtva) {
        this.oldCodtva = oldCodtva;
    }

    public BigDecimal getOldTautva() {
        return oldTautva;
    }

    public void setOldTautva(BigDecimal oldTautva) {
        this.oldTautva = oldTautva;
    }

    public Integer getCodeEmplacement() {
        return codeEmplacement;
    }

    public void setCodeEmplacement(Integer codeEmplacement) {
        this.codeEmplacement = codeEmplacement;
    }

    public ReceptionTemporaire getReception() {
        return reception;
    }
    public void setReception(ReceptionTemporaire reception){
        this.reception=reception;}

    public void setQuantiteCommande(BigDecimal quantiteCommande) {
        this.quantiteCommande = quantiteCommande;
    }
    


    
}
