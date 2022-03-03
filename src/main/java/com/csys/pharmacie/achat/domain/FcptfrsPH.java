package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author farouk
 */
@Entity
@Table(name = "fcptfrs")
@NamedQueries({
    @NamedQuery(name = "FcptfrsPH.findAll", query = "SELECT f FROM FcptfrsPH f")
    ,
    @NamedQuery(name = "FcptfrsPH.findByNumOpr", query = "SELECT f FROM FcptfrsPH f WHERE f.numOpr = :numOpr")
    ,
    @NamedQuery(name = "FcptfrsPH.findByDateOpr", query = "SELECT f FROM FcptfrsPH f WHERE f.dateOpr = :dateOpr")
    ,
    @NamedQuery(name = "FcptfrsPH.findByLibOpr", query = "SELECT f FROM FcptfrsPH f WHERE f.libOpr = :libOpr")
    ,
    @NamedQuery(name = "FcptfrsPH.findByCodFrs", query = "SELECT f FROM FcptfrsPH f WHERE f.codFrs = :codFrs")
    ,
    @NamedQuery(name = "FcptfrsPH.findByNumReg", query = "SELECT f FROM FcptfrsPH f WHERE f.numReg = :numReg")
    ,
    @NamedQuery(name = "FcptfrsPH.findByNumRegAff", query = "SELECT f FROM FcptfrsPH f WHERE f.numRegAff = :numRegAff")
    ,
    @NamedQuery(name = "FcptfrsPH.findByNumBon", query = "SELECT f FROM FcptfrsPH f WHERE f.numBon = :numBon")
    ,
    @NamedQuery(name = "FcptfrsPH.findByNumBonAff", query = "SELECT f FROM FcptfrsPH f WHERE f.numBonAff = :numBonAff")
    ,
    @NamedQuery(name = "FcptfrsPH.findByTypBon", query = "SELECT f FROM FcptfrsPH f WHERE f.typBon = :typBon")
    ,
    @NamedQuery(name = "FcptfrsPH.findByDebit", query = "SELECT f FROM FcptfrsPH f WHERE f.debit = :debit")
    ,
    @NamedQuery(name = "FcptfrsPH.findByCredit", query = "SELECT f FROM FcptfrsPH f WHERE f.credit = :credit")
    ,
    @NamedQuery(name = "FcptfrsPH.findBySens", query = "SELECT f FROM FcptfrsPH f WHERE f.sens = :sens")
    ,
    @NamedQuery(name = "FcptfrsPH.findByReste", query = "SELECT f FROM FcptfrsPH f WHERE f.reste = :reste")
    ,
    @NamedQuery(name = "FcptfrsPH.findBySolde", query = "SELECT f FROM FcptfrsPH f WHERE f.solde = :solde")
    ,
    @NamedQuery(name = "FcptfrsPH.findByEtat", query = "SELECT f FROM FcptfrsPH f WHERE f.etat = :etat")
    ,
    @NamedQuery(name = "FcptfrsPH.findByCodsup", query = "SELECT f FROM FcptfrsPH f WHERE f.codsup = :codsup")
    ,
    @NamedQuery(name = "FcptfrsPH.findByRetenu", query = "SELECT f FROM FcptfrsPH f WHERE f.retenu = :retenu")
    ,
    @NamedQuery(name = "FcptfrsPH.findByNumfacture", query = "SELECT f FROM FcptfrsPH f WHERE f.numfacture = :numfacture")
    ,
    @NamedQuery(name = "FcptfrsPH.findByNumFac", query = "SELECT f FROM FcptfrsPH f WHERE f.numFac = :numFac")
    ,
    @NamedQuery(name = "FcptfrsPH.findByMntOP", query = "SELECT f FROM FcptfrsPH f WHERE f.mntOP = :mntOP")
    ,
    @NamedQuery(name = "FcptfrsPH.findByRetenuOP", query = "SELECT f FROM FcptfrsPH f WHERE f.retenuOP = :retenuOP")
    ,
    @NamedQuery(name = "FcptfrsPH.findByDeclar", query = "SELECT f FROM FcptfrsPH f WHERE f.declar = :declar")})
public class FcptfrsPH implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Declar")
    private boolean declar;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "NumOpr")
    private Long numOpr;
    @Column(name = "DateOpr")
    private LocalDateTime dateOpr;
    @Size(max = 160)
    @Column(name = "LibOpr")
    private String libOpr;
    @Size(max = 10)
    @Column(name = "CodFrs")
    private String codFrs;
    @Size(max = 10)
    @Column(name = "NumReg")
    private String numReg;

    @Column(name = "NumRegAff")
    private String numRegAff;
    @Size(max = 20)
    @Column(name = "NumBon")
    private String numBon;

    @Column(name = "NumBonAff")
    private String numBonAff;
    @Size(max = 5)
    @Column(name = "TypBon")
    private String typBon;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Debit")
    private BigDecimal debit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Credit")
    private BigDecimal credit;
    @Size(max = 1)
    @Column(name = "Sens")
    private String sens;
    @Column(name = "Reste")
    private BigDecimal reste;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Solde")
    private BigDecimal solde;
    @Size(max = 1)
    @Column(name = "etat")
    private String etat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "codsup")
    private String codsup;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "retenu")
    private String retenu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numfacture")
    private String numfacture;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numFac")
    private String numFac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Mnt_OP")
    private BigDecimal mntOP;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Retenu_OP")
    private BigDecimal retenuOP;

    @Basic(optional = false)
    @NotNull
    @Column(name = "montant_ht")
    private BigDecimal montantHT;

    @Basic(optional = false)
    @NotNull
    @Column(name = "montant_tva")
    private BigDecimal montantTVA;

    @Column(name = "code_devise")
    private String codeDevise;

    @Column(name = "taux_devise")
    private BigDecimal tauxDevise;

    @Column(name = "montant_en_devise")
    private BigDecimal montantDevise;

    public FcptfrsPH() {
    }

    public FcptfrsPH(Long numOpr) {
        this.numOpr = numOpr;
    }

    public FcptfrsPH(Long numOpr, BigDecimal debit, BigDecimal credit, BigDecimal solde, String codsup, String retenu, String numfacture, String numFac, BigDecimal mntOP, BigDecimal retenuOP, boolean declar) {
        this.numOpr = numOpr;
        this.debit = debit;
        this.credit = credit;
        this.solde = solde;
        this.codsup = codsup;
        this.retenu = retenu;
        this.numfacture = numfacture;
        this.numFac = numFac;
        this.mntOP = mntOP;
        this.retenuOP = retenuOP;
        this.declar = declar;
    }

    public Long getNumOpr() {
        return numOpr;
    }

    public void setNumOpr(Long numOpr) {
        this.numOpr = numOpr;
    }

    public LocalDateTime getDateOpr() {
        return dateOpr;
    }

    public void setDateOpr(LocalDateTime dateOpr) {
        this.dateOpr = dateOpr;
    }

    public String getLibOpr() {
        return libOpr;
    }

    public void setLibOpr(String libOpr) {
        this.libOpr = libOpr;
    }

    public String getCodFrs() {
        return codFrs;
    }

    public void setCodFrs(String codFrs) {
        this.codFrs = codFrs;
    }

    public String getNumReg() {
        return numReg;
    }

    public void setNumReg(String numReg) {
        this.numReg = numReg;
    }

    public String getNumRegAff() {
        return numRegAff;
    }

    public void setNumRegAff(String numRegAff) {
        this.numRegAff = numRegAff;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public String getNumBonAff() {
        return numBonAff;
    }

    public void setNumBonAff(String numBonAff) {
        this.numBonAff = numBonAff;
    }

    public String getTypBon() {
        return typBon;
    }

    public void setTypBon(String typBon) {
        this.typBon = typBon;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getSens() {
        return sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public BigDecimal getReste() {
        return reste;
    }

    public void setReste(BigDecimal reste) {
        this.reste = reste;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getCodsup() {
        return codsup;
    }

    public void setCodsup(String codsup) {
        this.codsup = codsup;
    }

    public String getRetenu() {
        return retenu;
    }

    public void setRetenu(String retenu) {
        this.retenu = retenu;
    }

    public String getNumfacture() {
        return numfacture;
    }

    public void setNumfacture(String numfacture) {
        this.numfacture = numfacture;
    }

    public String getNumFac() {
        return numFac;
    }

    public void setNumFac(String numFac) {
        this.numFac = numFac;
    }

    public BigDecimal getMntOP() {
        return mntOP;
    }

    public void setMntOP(BigDecimal mntOP) {
        this.mntOP = mntOP;
    }

    public BigDecimal getRetenuOP() {
        return retenuOP;
    }

    public void setRetenuOP(BigDecimal retenuOP) {
        this.retenuOP = retenuOP;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numOpr != null ? numOpr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FcptfrsPH)) {
            return false;
        }
        FcptfrsPH other = (FcptfrsPH) object;
        if ((this.numOpr == null && other.numOpr != null) || (this.numOpr != null && !this.numOpr.equals(other.numOpr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.achat.model.pharmacie.FcptfrsPH[ numOpr=" + numOpr + " ]";
    }

    public boolean getDeclar() {
        return declar;
    }

    public void setDeclar(boolean declar) {
        this.declar = declar;
    }

    public void setSolde(String CodFrs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public BigDecimal getMontantHT() {
        return montantHT;
    }

    public void setMontantHT(BigDecimal montantHT) {
        this.montantHT = montantHT;
    }

    public BigDecimal getMontantTVA() {
        return montantTVA;
    }

    public void setMontantTVA(BigDecimal montantTVA) {
        this.montantTVA = montantTVA;
    }

    public String getCodeDevise() {
        return codeDevise;
    }

    public void setCodeDevise(String codeDevise) {
        this.codeDevise = codeDevise;
    }

    public BigDecimal getTauxDevise() {
        return tauxDevise;
    }

    public void setTauxDevise(BigDecimal tauxDevise) {
        this.tauxDevise = tauxDevise;
    }

    public BigDecimal getMontantDevise() {
        return montantDevise;
    }

    public void setMontantDevise(BigDecimal montantDevise) {
        this.montantDevise = montantDevise;
    }

}
