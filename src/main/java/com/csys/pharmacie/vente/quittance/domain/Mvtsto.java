/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.domain;

import com.csys.pharmacie.helper.BaseDetailBon;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "mvtsto")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "Mvtsto.facture",
                      attributeNodes = {
                          @NamedAttributeNode("facture")
                      })

})
@Audited
@AuditTable("mvtsto_AUD")
@AuditOverride(forClass = BaseDetailBon.class, isAudited = true)
public class Mvtsto extends BaseDetailBon implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MvtstoPK mvtstoPK;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "Lot_Inter")
    private String lotInter;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CodTvaAch")
    private Integer codTvaAch;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TauTvaAch")
    private BigDecimal tauTvaAch;
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
    @Column(name = "DatPer")
    private LocalDate datPer;
    @Column(name = "dateSysteme")
    private LocalDate dateSysteme;
    @Column(name = "HeureSysteme")
    private LocalTime heureSysteme;
    @NotNull
    @Column(name = "qteben")
    private BigDecimal qteben;
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
    private Integer unite;
    @JoinColumn(name = "numbon", referencedColumnName = "numbon", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Facture facture;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mvtsto")
    private List<DetailMvtsto> detailMvtstoCollection;

    @PrePersist
    public void prePersist() {
        this.heureSysteme = LocalTime.now();
        this.dateSysteme = LocalDate.now();
    }

    public Mvtsto() {
    }

    public Mvtsto(MvtstoPK mvtstoPK) {
        this.mvtstoPK = mvtstoPK;
    }

    public MvtstoPK getMvtstoPK() {
        return mvtstoPK;
    }

    public void setMvtstoPK(MvtstoPK mvtstoPK) {
        this.mvtstoPK = mvtstoPK;
    }

    public Integer getCodTvaAch() {
        return codTvaAch;
    }

    public void setCodTvaAch(Integer codTvaAch) {
        this.codTvaAch = codTvaAch;
    }

    public BigDecimal getTauTvaAch() {
        return tauTvaAch;
    }

    public void setTauTvaAch(BigDecimal tauTvaAch) {
        this.tauTvaAch = tauTvaAch;
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

    public LocalDate getDateSysteme() {
        return dateSysteme;
    }

    public void setDateSysteme(LocalDate dateSysteme) {
        this.dateSysteme = dateSysteme;
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

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public LocalTime getHeureSysteme() {
        return heureSysteme;
    }

    public void setHeureSysteme(LocalTime heureSysteme) {
        this.heureSysteme = heureSysteme;
    }

    public BigDecimal getQteben() {
        return qteben;
    }

    public void setQteben(BigDecimal qteben) {
        this.qteben = qteben;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getMajoration() {
        return majoration;
    }

    public void setMajoration(BigDecimal majoration) {
        this.majoration = majoration;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public BigDecimal getAjustement() {
        return ajustement;
    }

    public void setAjustement(BigDecimal ajustement) {
        this.ajustement = ajustement;
    }

    public BigDecimal getTauxCouverture() {
        return tauxCouverture;
    }

    public void setTauxCouverture(BigDecimal tauxCouverture) {
        this.tauxCouverture = tauxCouverture;
    }

    public BigDecimal getPrixBrute() {
        return prixBrute;
    }

    public void setPrixBrute(BigDecimal prixBrute) {
        this.prixBrute = prixBrute;
    }

    public List<DetailMvtsto> getDetailMvtstoCollection() {
        return detailMvtstoCollection;
    }

    public void setDetailMvtstoCollection(List<DetailMvtsto> detailMvtstoCollection) {
        this.detailMvtstoCollection = detailMvtstoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.mvtstoPK);
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
        final Mvtsto other = (Mvtsto) obj;
        if (!Objects.equals(this.mvtstoPK, other.mvtstoPK)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Mvtsto{" + "mvtstoPK=" + mvtstoPK + ", qteben=" + qteben + ", unite=" + unite + '}';
    }

}
