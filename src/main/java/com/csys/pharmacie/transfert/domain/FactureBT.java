/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import com.csys.pharmacie.helper.BaseBon;
import com.csys.pharmacie.helper.SatisfactionEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
/**
 *
 * @author Farouk
 */
@Entity
@NamedEntityGraphs({
    @NamedEntityGraph(name = "FactureBT.DetailTransfertDTR",
            attributeNodes = {
                @NamedAttributeNode("DetailTransfertDTR")
            }),
    @NamedEntityGraph(name = "FactureBT.BTFE",
            attributeNodes = {
                @NamedAttributeNode("BTFE")
            })
})
@Audited
@Table(name = "FactureBT")
@AuditTable("FactureBT_AUD")
public class FactureBT extends BaseBon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "mntbon")
    private BigDecimal mntbon;
    @Size(max = 140)
    @Column(name = "memop")
    private String memop;

    @Column(name = "coddep")
    private Integer coddep;

    @Column(name = "deptr")
    private Integer deptr;

    @NotNull
    @Column(name = "interdepot")
    private boolean interdepot;//1 tahwil bayn makhazan

    @NotNull
    @Column(name = "avoir_transfert")
    private boolean avoirTransfert;//1 ==> irja3 tahwil

    @Column(name = "Automatique")
    private Boolean automatique;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "Satisf")
    private SatisfactionEnum satisf;

    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "CodAnnul")
    private String codAnnul;
    @Column(name = "DatAnnul")
    private LocalDateTime datAnnul;

    @Column(name = "perime")
    private Boolean perime;

    @Column(name = "valide")
    private Boolean valide;

    @Column(name = "conforme")
    private Boolean conforme;

    @Column(name = "user_validate")
    private String userValidate;
    @Column(name = "date_validate")
    private LocalDateTime dateValidate;
    
    
    @Column(name = "memo_validate")
    private String memoValidate;

    @Column(name = "avoir_suite_validation")
    private Boolean avoirSuiteValidation;
 
   
    @Column(name = "hashCode")
    private byte[] hashCode;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "numbon")
    private List<MvtStoBT> detailFactureBTCollection;
   
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "factureBT")
    private List<Btfe> BTFE;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numBT")
    private List<Btbt> numBTReturn;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "factureBT")
    private List<DemandeRecup> DemandeRecup;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "facturebt")
    private List<TransfertDetailDTR> DetailTransfertDTR;
    
       @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "factureBT")
     private List<Btav> BTAV;
 

    public List<Btfe> getBTFE() {
        return BTFE;
    }

    public void setBTFE(List<Btfe> BTFE) {
        this.BTFE = BTFE;
    }

    public List<MvtStoBT> getDetailFactureBTCollection() {
        return detailFactureBTCollection;
    }

    public void setDetailFactureBTCollection(List<MvtStoBT> detailFactureBTCollection) {
        this.detailFactureBTCollection = detailFactureBTCollection;
    }

    public FactureBT() {
    }

    public FactureBT(boolean interdepot) {
        this.interdepot = interdepot;
    }

    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public boolean getInterdepot() {
        return interdepot;
    }

    public void setInterdepot(boolean interdepot) {
        this.interdepot = interdepot;
    }

    public Integer getDeptr() {
        return deptr;
    }

    public boolean isInterdepot() {
        return interdepot;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public boolean isAvoirTransfert() {
        return avoirTransfert;
    }

    public void setAvoirTransfert(boolean avoirTransfert) {
        this.avoirTransfert = avoirTransfert;
    }

    public Boolean getAutomatique() {
        return automatique;
    }

    public void setAutomatique(Boolean automatique) {
        this.automatique = automatique;
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

    public void setDeptr(Integer deptr) {
        this.deptr = deptr;
    }

    public List<DemandeRecup> getDemandeRecup() {
        return DemandeRecup;
    }

    public void setDemandeRecup(List<DemandeRecup> DemandeRecup) {
        this.DemandeRecup = DemandeRecup;
    }

    public List<Btbt> getNumBTReturn() {
        return numBTReturn;
    }

    public void setNumBTReturn(List<Btbt> numBTReturn) {
        this.numBTReturn = numBTReturn;
    }

    public SatisfactionEnum getSatisf() {
        return satisf;
    }

    public void setSatisf(SatisfactionEnum satisf) {
        this.satisf = satisf;
    }

    public Boolean getPerime() {
        return perime;
    }

    public void setPerime(Boolean perime) {
        this.perime = perime;
    }

    public Boolean getValide() {
        return valide;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }

    public Boolean getConforme() {
        return conforme;
    }

    public void setConforme(Boolean conforme) {
        this.conforme = conforme;
    }

    public String getUserValidate() {
        return userValidate;
    }

    public void setUserValidate(String userValidate) {
        this.userValidate = userValidate;
    }

    public LocalDateTime getDateValidate() {
        return dateValidate;
    }

    public void setDateValidate(LocalDateTime dateValidate) {
        this.dateValidate = dateValidate;
    }

    public String getMemoValidate() {
        return memoValidate;
    }

    public void setMemoValidate(String memoValidate) {
        this.memoValidate = memoValidate;
    }

    public Boolean getAvoirSuiteValidation() {
        return avoirSuiteValidation;
    }

    public void setAvoirSuiteValidation(Boolean avoirSuiteValidation) {
        this.avoirSuiteValidation = avoirSuiteValidation;
    }

    public List<Btav> getBTAV() {
        return BTAV;
    }

    public void setBTAV(List<Btav> BTAV) {
        this.BTAV = BTAV;
    }

    public byte[] getHashCode() {
        return hashCode;
    }

    public void setHashCode(byte[] hashCode) {
        this.hashCode = hashCode;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getNumbon() != null ? getNumbon().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactureBT)) {
            return false;
        }
        FactureBT other = (FactureBT) object;
        if ((this.getNumbon() == null && other.getNumbon() != null) || (this.getNumbon() != null && !this.getNumbon().equals(other.getNumbon()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.transfert.domain.FactureBT[ numbon=" + getNumbon() + " ]";
    }

    public List<TransfertDetailDTR> getDetailTransfertDTR() {
        return DetailTransfertDTR;
    }

    public void setDetailTransfertDTR(List<TransfertDetailDTR> DetailTransfertDTR) {
        this.DetailTransfertDTR = DetailTransfertDTR;
    }

    }
