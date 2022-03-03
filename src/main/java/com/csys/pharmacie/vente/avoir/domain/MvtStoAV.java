/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.csys.pharmacie.helper.BaseDetailBon;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Farouk
 */
@Entity
@Table(name = "MvtStoAV")
@Audited
@AuditTable("MvtStoAV_AUD")
@AuditOverride(forClass = BaseDetailBon.class, isAudited = true)
public class MvtStoAV extends BaseDetailBon implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MvtStoAVPK mvtStoAVPK;
    @Enumerated(EnumType.STRING)
    @Column(name = "typbon")
    private TypeBonEnum typbon;
    @Basic(optional = false)
    @NotNull
    @Column(name = "priuni")
    private BigDecimal priuni;
    @Column(name = "montht")
    private BigDecimal montht;
    @Column(name = "tautva")
    private BigDecimal tautva;
    @Column(name = "priach")
    private BigDecimal priach;
    @Size(max = 500)
    @Column(name = "memoart")
    private String memoart;
    @Column(name = "codtva")
    private Integer codtva;
    @Column(name = "dateSysteme")
    private LocalDate dateSysteme;
    @Column(name = "HeureSysteme")
    private LocalTime heureSysteme;
    @Column(name = "remise")
    private BigDecimal remise;
    @Column(name = "majoration")
    private BigDecimal majoration;
    @Column(name = "ajustement")
    private BigDecimal ajustement;
    @Column(name = "prixBrute")
    private BigDecimal prixBrute;
    @Column(name = "tauxCouverture")
    private BigDecimal tauxCouverture;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unite")
    private int unite;
    @Basic(optional = false)
    @Size(min = 0, max = 50)
    @Column(name = "Lot_Inter")
    private String lotInter;
    @Column(name = "DatPer")
    private LocalDate datPer;
    @JoinColumn(name = "numbon", referencedColumnName = "numbon", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FactureAV factureAV;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mvtStoAV")
    private List<DetailMvtstoAV> detailMvtstoAVCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mvtStoAV")
    private List<MvtstoMvtstoAV> mvtstoMvtstoAVCollection;

    @PrePersist
    public void prePersist() {
        this.heureSysteme = LocalTime.now();
        this.dateSysteme = LocalDate.now();
    }

    public FactureAV getFactureAV() {
        return factureAV;
    }

    public void setFactureAV(FactureAV factureBA) {
        this.factureAV = factureBA;
    }

    public MvtStoAV() {
    }

    public MvtStoAV(MvtStoAVPK mvtStoAVPK) {
        this.mvtStoAVPK = mvtStoAVPK;
    }

    public MvtStoAVPK getMvtStoAVPK() {
        return mvtStoAVPK;
    }

    public void setMvtStoAVPK(MvtStoAVPK mvtStoAVPK) {
        this.mvtStoAVPK = mvtStoAVPK;
    }

    public TypeBonEnum getTypbon() {
        return typbon;
    }

    public void setTypbon(TypeBonEnum typbon) {
        this.typbon = typbon;
    }

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public BigDecimal getMontht() {
        return montht;
    }

    public void setMontht(BigDecimal montht) {
        this.montht = montht;
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    public BigDecimal getPriach() {
        return priach;
    }

    public void setPriach(BigDecimal priach) {
        this.priach = priach;
    }

    public String getMemoart() {
        return memoart;
    }

    public void setMemoart(String memoart) {
        this.memoart = memoart;
    }

    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

    public LocalDate getDateSysteme() {
        return dateSysteme;
    }

    public void setDateSysteme(LocalDate dateSysteme) {
        this.dateSysteme = dateSysteme;
    }

    public LocalTime getHeureSysteme() {
        return heureSysteme;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getAjustement() {
        return ajustement;
    }

    public void setAjustement(BigDecimal ajustement) {
        this.ajustement = ajustement;
    }

    public BigDecimal getPrixBrute() {
        return prixBrute;
    }

    public void setPrixBrute(BigDecimal prixBrute) {
        this.prixBrute = prixBrute;
    }

    public BigDecimal getTauxCouverture() {
        return tauxCouverture;
    }

    public void setTauxCouverture(BigDecimal tauxCouverture) {
        this.tauxCouverture = tauxCouverture;
    }

    public BigDecimal getMajoration() {
        return majoration;
    }

    public void setMajoration(BigDecimal majoration) {
        this.majoration = majoration;
    }

    public void setHeureSysteme(LocalTime heureSysteme) {
        this.heureSysteme = heureSysteme;
    }

    public int getUnite() {
        return unite;
    }

    public void setUnite(int unite) {
        this.unite = unite;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public List<DetailMvtstoAV> getDetailMvtstoAVCollection() {
        return detailMvtstoAVCollection;
    }

    public void setDetailMvtstoAVCollection(List<DetailMvtstoAV> detailMvtstoAVCollection) {
        this.detailMvtstoAVCollection = detailMvtstoAVCollection;
    }

    public List<MvtstoMvtstoAV> getMvtstoMvtstoAVCollection() {
        return mvtstoMvtstoAVCollection;
    }

    public void setMvtstoMvtstoAVCollection(List<MvtstoMvtstoAV> mvtstoMvtstoAVCollection) {
        this.mvtstoMvtstoAVCollection = mvtstoMvtstoAVCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mvtStoAVPK != null ? mvtStoAVPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MvtStoAV)) {
            return false;
        }
        MvtStoAV other = (MvtStoAV) object;
        if ((this.mvtStoAVPK == null && other.mvtStoAVPK != null) || (this.mvtStoAVPK != null && !this.mvtStoAVPK.equals(other.mvtStoAVPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MvtStoAV{" + "mvtStoAVPK=" + mvtStoAVPK + ", tautva=" + tautva + ", codtva=" + codtva + '}';
    }

}
