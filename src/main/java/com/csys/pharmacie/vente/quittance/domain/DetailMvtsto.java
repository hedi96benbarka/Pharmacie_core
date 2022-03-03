/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.domain;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.stock.domain.Depsto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "detail_mvtsto")
@Audited
@AuditTable("detail_mvtsto_AUD")
public class DetailMvtsto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code", nullable = false)
    private Long code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codart")
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbonMvtsto")
    private String numbonMvtsto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "numordreMvtsto")
    private String numordreMvtsto;

    @Basic(optional = false)
    @NotNull
    @Column(name = "coddep", nullable = false)
    private long coddep;
    @Size(max = 6)
    @Column(name = "numordre", length = 6)
    private String numordre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "qte", nullable = false, precision = 18, scale = 3)
    private BigDecimal qte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PU", nullable = false, precision = 18, scale = 5)
    private BigDecimal pu;
    @Column(name = "DatPer")
    private LocalDate datPer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "NumBon", nullable = false, length = 15)
    private String numBon;
    @Column(name = "datesys")
    private LocalDateTime datesys;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unite", nullable = false)
    private int unite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "categ_depot")
    @Enumerated(EnumType.STRING)
    private CategorieDepotEnum categDepot;
    @Size(max = 50)
    @Column(name = "Lot_Inter", length = 50)
    private String lotInter;
    @Column(name = "qteAvoir")
    private BigDecimal qteAvoir;

    @Column(name = "code_tva")
    private Integer codeTva;

    @Column(name = "taux_tva")
    private BigDecimal tauxTva;

    @JoinColumn(name = "code_depsto", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private Depsto depsto;
    @JoinColumns({
        @JoinColumn(name = "codart", referencedColumnName = "codart", insertable = false, updatable = false)
        , @JoinColumn(name = "numbonMvtsto", referencedColumnName = "numbon", insertable = false, updatable = false)
        , @JoinColumn(name = "numordreMvtsto", referencedColumnName = "numordre", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Mvtsto mvtsto;

    public DetailMvtsto() {
    }

    public DetailMvtsto(Long code) {
        this.code = code;
    }

    public DetailMvtsto(Depsto depsto, Mvtsto mvtsto) {
//        this.codeDepsto = depsto.getCode();
        this.depsto = depsto;
        this.coddep = depsto.getCoddep();
        this.lotInter = depsto.getLotInter();
        this.pu = depsto.getPu();
        this.datPer = depsto.getDatPer();
        this.numBon = depsto.getNumBon();
        this.datesys = depsto.getDatesys();
        this.unite = depsto.getUnite();
        this.categDepot = depsto.getCategDepot();
        this.numordre = depsto.getNumordre();
        this.qteAvoir = BigDecimal.ZERO;
        this.codart = mvtsto.getMvtstoPK().getCodart();
        this.numbonMvtsto = mvtsto.getMvtstoPK().getNumbon();
        this.numordreMvtsto = mvtsto.getMvtstoPK().getNumordre();
        this.tauxTva = depsto.getTauxTva();
        this.codeTva = depsto.getCodeTva();
    }

    public DetailMvtsto(Long code, long coddep, BigDecimal qte, BigDecimal pu, String numBon, int unite, CategorieDepotEnum categDepot) {
        this.code = code;
        this.coddep = coddep;
        this.qte = qte;
        this.pu = pu;
        this.numBon = numBon;
        this.unite = unite;
        this.categDepot = categDepot;
    }
    
      public DetailMvtsto(Long code, long coddep, BigDecimal qte, BigDecimal pu, String numBon, int unite, CategorieDepotEnum categDepot, Integer codart) {
        this.code = code;
        this.coddep = coddep;
        this.qte = qte;
        this.pu = pu;
        this.numBon = numBon;
        this.unite = unite;
        this.categDepot = categDepot;
        this.codart = codart;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getNumbonMvtsto() {
        return numbonMvtsto;
    }

    public void setNumbonMvtsto(String numbonMvtsto) {
        this.numbonMvtsto = numbonMvtsto;
    }

    public String getNumordreMvtsto() {
        return numordreMvtsto;
    }

    public void setNumordreMvtsto(String numordreMvtsto) {
        this.numordreMvtsto = numordreMvtsto;
    }

    public long getCoddep() {
        return coddep;
    }

    public void setCoddep(long coddep) {
        this.coddep = coddep;
    }

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public LocalDateTime getDatesys() {
        return datesys;
    }

    public void setDatesys(LocalDateTime datesys) {
        this.datesys = datesys;
    }

    public int getUnite() {
        return unite;
    }

    public void setUnite(int unite) {
        this.unite = unite;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public BigDecimal getQteAvoir() {
        return qteAvoir;
    }

    public void setQteAvoir(BigDecimal qteAvoir) {
        this.qteAvoir = qteAvoir;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public Depsto getDepsto() {
        return depsto;
    }

    public void setDepsto(Depsto depsto) {
        this.depsto = depsto;
    }

    public Mvtsto getMvtsto() {
        return mvtsto;
    }

    public void setMvtsto(Mvtsto mvtsto) {
        this.mvtsto = mvtsto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailMvtsto)) {
            return false;
        }
        DetailMvtsto other = (DetailMvtsto) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DetailMvtsto{" + "code=" + code + ", codart=" + codart + ", numBonFac=" + numbonMvtsto + ", numordreMvtsto=" + numordreMvtsto + ", coddep=" + coddep + ", numordre=" + numordre + ", qte=" + qte + ", pu=" + pu + ", datPer=" + datPer + ", numBon=" + numBon + ", datesys=" + datesys + ", unite=" + unite + ", categDepot=" + categDepot + ", lotInter=" + lotInter + ", qteAvoir=" + qteAvoir + ", depsto=" + depsto + ", mvtsto=" + mvtsto + '}';
    }

}
