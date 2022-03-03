/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "receiving_details")
public class ReceivingDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code")
    private Integer code;
//    @EmbeddedId
//    protected ReceivingDetailsPK receivingDetailsPK;
      @Basic(optional = false)
    @NotNull
    @Column(name = "codart", nullable = false)
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "desart", nullable = false, length = 255)
    private String desart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "desart_sec", nullable = false, length = 255)
    private String desartSec;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "code_saisi", nullable = false, length = 50)
    private String codeSaisi;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite", nullable = false, precision = 18, scale = 3)
    private BigDecimal quantite;
//    @MapsId("receiving")
    @JoinColumn(name = "receiving", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private Receiving receiving;
    @Size(min = 0, max = 50)
    @Column(name = "lot_inter")
    private String lotInter;
    @Column(name = "date_per")
    private LocalDate datePer;
    @Column(name = "quantite_receiving")
    private BigDecimal quantiteReceiving;
    @Column(name = "quantite_valide")
    private BigDecimal quantiteValide;
    @Column(name = "prix_unitaire")
    private BigDecimal prixUnitaire;
    @Column(name = "tautva")
    private BigDecimal tautva;
    @Column(name = "codtva")
    private Integer codtva;
    @Column(name = "memo")
    private String memo;
    @Column(name = "delai_livraison")
    private Integer delaiLivraison;
    @Column(name = "remise")
    private BigDecimal remise;

    @Column(name = "appel_offre")
    private Boolean appelOffre;
    @Column(name = "is_capitalize")
    private Boolean isCapitalize;
       @Column(name = "quantite_gratuite")
    private Integer quantiteGratuite;
           @Column(name = "is_free")
    private Boolean isFree;
    @Column(name = "quantity_from_commande")
    private Boolean quantityFromComannde;
    public ReceivingDetails() {
    }

//    public ReceivingDetails(ReceivingDetailsPK receivingDetailsPK) {
//        this.receivingDetailsPK = receivingDetailsPK;
//    }
//
//    public ReceivingDetails(ReceivingDetailsPK receivingDetailsPK, String desart, String desartSec, String codeSaisi, BigDecimal quantite) {
//        this.receivingDetailsPK = receivingDetailsPK;
//        this.desart = desart;
//        this.desartSec = desartSec;
//        this.codeSaisi = codeSaisi;
//        this.quantite = quantite;
//    }
//
//    public ReceivingDetails(int codart, int receiving) {
//        this.receivingDetailsPK = new ReceivingDetailsPK(codart, receiving);
//    }
//
//    public ReceivingDetailsPK getReceivingDetailsPK() {
//        return receivingDetailsPK;
//    }
//
//    public void setReceivingDetailsPK(ReceivingDetailsPK receivingDetailsPK) {
//        this.receivingDetailsPK = receivingDetailsPK;
//    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesartSec() {
        return desartSec;
    }

    public void setDesartSec(String desartSec) {
        this.desartSec = desartSec;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public Receiving getReceiving1() {
        return receiving;
    }

    public void setReceiving1(Receiving receiving1) {
        this.receiving = receiving1;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public LocalDate getDatePer() {
        return datePer;
    }

    public void setDatePer(LocalDate datePer) {
        this.datePer = datePer;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (receivingDetailsPK != null ? receivingDetailsPK.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof ReceivingDetails)) {
//            return false;
//        }
//        ReceivingDetails other = (ReceivingDetails) object;
//        if ((this.receivingDetailsPK == null && other.receivingDetailsPK != null) || (this.receivingDetailsPK != null && !this.receivingDetailsPK.equals(other.receivingDetailsPK))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "com.csys.pharmacie.achat.domain.ReceivingDetails[ receivingDetailsPK=" + receivingDetailsPK + " ]";
//    }

    public BigDecimal getQuantiteReceiving() {
        return quantiteReceiving;
    }

    public void setQuantiteReceiving(BigDecimal quantiteReceiving) {
        this.quantiteReceiving = quantiteReceiving;
    }

  

    public BigDecimal getQuantiteValide() {
        return quantiteValide;
    }

    public void setQuantiteValide(BigDecimal quantiteValide) {
        this.quantiteValide = quantiteValide;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

 

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getDelaiLivraison() {
        return delaiLivraison;
    }

    public void setDelaiLivraison(Integer delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public Boolean getAppelOffre() {
        return appelOffre;
    }

    public void setAppelOffre(Boolean appelOffre) {
        this.appelOffre = appelOffre;
    }

    public Boolean getIsCapitalize() {
        return isCapitalize;
    }

    public void setIsCapitalize(Boolean isCapitalize) {
        this.isCapitalize = isCapitalize;
    }

    public Integer getQuantiteGratuite() {
        return quantiteGratuite;
    }

    public void setQuantiteGratuite(Integer quantiteGratuite) {
        this.quantiteGratuite = quantiteGratuite;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public Boolean getQuantityFromComannde() {
        return quantityFromComannde;
    }

    public void setQuantityFromComannde(Boolean quantityFromComannde) {
        this.quantityFromComannde = quantityFromComannde;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

 

}
