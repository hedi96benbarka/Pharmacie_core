/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "mvtstoDR")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "MvtstoDR.factureDR",
                      attributeNodes = {
                          @NamedAttributeNode("factureDR")
                      })
})
@Audited
@AuditTable("mvtstoDR_AUD")
public class MvtstoDR implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MvtstoDRPK mvtstoDRPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "coddep", nullable = false)
    private long coddep;
    @Size(max = 25)
    @Column(name = "lot", length = 25)
    private String lot;
    @Size(max = 2)
    @Column(name = "typbon", length = 2)
    private String typbon;
    @Column(name = "datbon")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datbon;
    @Size(max = 10)
    @Column(name = "numfacbl", length = 10)
    private String numfacbl;
    @Column(name = "datefacbl")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datefacbl;
    @Size(max = 255)
    @Column(name = "desart")
    private String desart;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite", precision = 18, scale = 3)
    private BigDecimal quantite;
    @Column(name = "priuni", precision = 18, scale = 5)
    private BigDecimal priuni;
    @Column(name = "remise", precision = 18, scale = 5)
    private BigDecimal remise;
    @Column(name = "montht", precision = 18, scale = 3)
    private BigDecimal montht;
    @Column(name = "tautva", precision = 18, scale = 3)
    private BigDecimal tautva;
    @Column(name = "fodart")
    private Boolean fodart;
    @Column(name = "homologue")
    private Boolean homologue;
    @Size(max = 40)
    @Column(name = "deptr", length = 40)
    private String deptr;
    @Column(name = "priach", precision = 18, scale = 3)
    private BigDecimal priach;
    @Column(name = "marge", precision = 18, scale = 3)
    private BigDecimal marge;
    @Column(name = "nbrpce")
    private Short nbrpce;
    @Size(max = 2500)
    @Column(name = "memoart", length = 2500)
    private String memoart;
    @Column(name = "ancien_qte", precision = 18, scale = 3)
    private BigDecimal ancienQte;
    @Column(name = "ancien_Valfod", precision = 18, scale = 3)
    private BigDecimal ancienValfod;
    @Column(name = "ancien_Valrem", precision = 18, scale = 3)
    private BigDecimal ancienValrem;
    @Size(max = 20)
    @Column(name = "numaffiche", length = 20)
    private String numaffiche;
    @Column(name = "solde", precision = 18, scale = 3)
    private BigDecimal solde;
    @Column(name = "codtva")
    private Integer codtva;
    @Size(max = 1)
    @Column(name = "typmvt", length = 1)
    private String typmvt;
    @Column(name = "qteben", precision = 18, scale = 3)
    private BigDecimal qteben;
    @Column(name = "prixben", precision = 18, scale = 3)
    private BigDecimal prixben;
    @Column(name = "pvp", precision = 18, scale = 3)
    private BigDecimal pvp;
    @Column(name = "majoration", precision = 18, scale = 3)
    private BigDecimal majoration;
    @Size(max = 8)
    @Column(name = "codvend", length = 8)
    private String codvend;
    @Size(max = 50)
    @Column(name = "gest", length = 50)
    private String gest;
    @Column(name = "DatArr")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datArr;
    @Column(name = "QteArr", precision = 18, scale = 3)
    private BigDecimal qteArr;
    @Size(max = 20)
    @Column(name = "NumCha", length = 20)
    private String numCha;
    @Size(max = 20)
    @Column(name = "Utilis", length = 20)
    private String utilis;
    @Size(max = 1)
    @Column(name = "satisfait", length = 1)
    private String satisfait;
    @Column(name = "qtecom", precision = 18, scale = 3)
    private BigDecimal qtecom;
    @Size(max = 20)
    @Column(name = "numdoss", length = 20)
    private String numdoss;
    @Size(max = 60)
    @Column(name = "medtrait", length = 60)
    private String medtrait;
    @Size(max = 1)
    @Column(name = "Etat", length = 1)
    private String etat;
    @Column(name = "datinv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datinv;
    @Size(max = 5)
    @Column(name = "NumVir", length = 5)
    private String numVir;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "etat_dep", nullable = false, length = 1)
    private String etatDep;
    @Column(name = "datinvdep")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datinvdep;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PriAchApresProrata", nullable = false, precision = 18, scale = 3)
    private BigDecimal priAchApresProrata;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Prix_Majore", nullable = false, precision = 18, scale = 3)
    private BigDecimal prixMajore;
    @Column(name = "DatPer")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datPer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Modifier", nullable = false)
    private Character modifier;
    @Column(name = "Poids", precision = 18, scale = 3)
    private BigDecimal poids;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HeureSysteme", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date heureSysteme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Tx_Majoration", nullable = false, precision = 6, scale = 3)
    private BigDecimal txMajoration;
    @Column(name = "QtePans", precision = 18, scale = 3)
    private BigDecimal qtePans;
    @Column(name = "QteAnest", precision = 18, scale = 3)
    private BigDecimal qteAnest;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 8)
    @Column(name = "NumBon_Depot", nullable = true, length = 8)
    private String numBonDepot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 0, max = 20)
    @Column(name = "reference", nullable = true, length = 20)
    private String reference;
    @Column(name = "QteCom_Sauv", precision = 18, scale = 3)
    private BigDecimal qteComSauv;
    @Size(max = 5)
    @Column(name = "CodTvaAch", length = 5)
    private String codTvaAch;
    @Column(name = "TauTvaAch", precision = 18, scale = 3)
    private BigDecimal tauTvaAch;
    @Column(name = "unite")
    private Integer unite;
//    @Size(max = 10)
//    @Column(name = "categ_depot", length = 10)
//    private String categDepot;
    @Size(max = 255)
    @Column(name = "desart_sec")
    private String desartSec;
    @Size(max = 50)
    @Column(name = "code_saisi", length = 50)
    private String codeSaisi;
    @Column(name = "dateSysteme")
    @Temporal(TemporalType.DATE)
    private Date dateSysteme;
    @Column(name = "codeUnite")
    private Integer codeUnite;
    @Column(name = "perissable")
    private Boolean perissable;
    @Basic(optional = false)
    @NotNull
    @Column(name = "a_remplacer")
    private boolean aRemplacer;
    @JoinColumn(name = "numbon", referencedColumnName = "numbon", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FactureDR factureDR;

    public MvtstoDR() {
    }

    public MvtstoDR(MvtstoDRPK mvtstoDRPK) {
        this.mvtstoDRPK = mvtstoDRPK;
    }

    public MvtstoDR(MvtstoDRPK mvtstoDRPK, long coddep, String etatDep, BigDecimal priAchApresProrata, BigDecimal prixMajore, Character modifier, Date heureSysteme, BigDecimal txMajoration, String numBonDepot, String reference) {
        this.mvtstoDRPK = mvtstoDRPK;
        this.coddep = coddep;
        this.etatDep = etatDep;
        this.priAchApresProrata = priAchApresProrata;
        this.prixMajore = prixMajore;
        this.modifier = modifier;
        this.heureSysteme = heureSysteme;
        this.txMajoration = txMajoration;
        this.numBonDepot = numBonDepot;
        this.reference = reference;
    }

    public Boolean getPerissable() {
        return perissable;
    }

    public void setPerissable(Boolean perissable) {
        this.perissable = perissable;
    }

    public MvtstoDR(Integer codart, String numbon, String numordre) {
        this.mvtstoDRPK = new MvtstoDRPK(codart, numbon, numordre);
    }

    public MvtstoDRPK getMvtstoDRPK() {
        return mvtstoDRPK;
    }

    public void setMvtstoDRPK(MvtstoDRPK mvtstoDRPK) {
        this.mvtstoDRPK = mvtstoDRPK;
    }

    public long getCoddep() {
        return coddep;
    }

    public void setCoddep(long coddep) {
        this.coddep = coddep;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getTypbon() {
        return typbon;
    }

    public void setTypbon(String typbon) {
        this.typbon = typbon;
    }

    public Date getDatbon() {
        return datbon;
    }

    public void setDatbon(Date datbon) {
        this.datbon = datbon;
    }

    public String getNumfacbl() {
        return numfacbl;
    }

    public void setNumfacbl(String numfacbl) {
        this.numfacbl = numfacbl;
    }

    public Date getDatefacbl() {
        return datefacbl;
    }

    public void setDatefacbl(Date datefacbl) {
        this.datefacbl = datefacbl;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
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
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    public Boolean getFodart() {
        return fodart;
    }

    public void setFodart(Boolean fodart) {
        this.fodart = fodart;
    }

    public Boolean getHomologue() {
        return homologue;
    }

    public void setHomologue(Boolean homologue) {
        this.homologue = homologue;
    }

    public String getDeptr() {
        return deptr;
    }

    public void setDeptr(String deptr) {
        this.deptr = deptr;
    }

    public BigDecimal getPriach() {
        return priach;
    }

    public void setPriach(BigDecimal priach) {
        this.priach = priach;
    }

    public BigDecimal getMarge() {
        return marge;
    }

    public void setMarge(BigDecimal marge) {
        this.marge = marge;
    }

    public Short getNbrpce() {
        return nbrpce;
    }

    public void setNbrpce(Short nbrpce) {
        this.nbrpce = nbrpce;
    }

    public String getMemoart() {
        return memoart;
    }

    public void setMemoart(String memoart) {
        this.memoart = memoart;
    }

    public BigDecimal getAncienQte() {
        return ancienQte;
    }

    public void setAncienQte(BigDecimal ancienQte) {
        this.ancienQte = ancienQte;
    }

    public BigDecimal getAncienValfod() {
        return ancienValfod;
    }

    public void setAncienValfod(BigDecimal ancienValfod) {
        this.ancienValfod = ancienValfod;
    }

    public BigDecimal getAncienValrem() {
        return ancienValrem;
    }

    public void setAncienValrem(BigDecimal ancienValrem) {
        this.ancienValrem = ancienValrem;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

    public String getTypmvt() {
        return typmvt;
    }

    public void setTypmvt(String typmvt) {
        this.typmvt = typmvt;
    }

    public BigDecimal getQteben() {
        return qteben;
    }

    public void setQteben(BigDecimal qteben) {
        this.qteben = qteben;
    }

    public BigDecimal getPrixben() {
        return prixben;
    }

    public void setPrixben(BigDecimal prixben) {
        this.prixben = prixben;
    }

    public BigDecimal getPvp() {
        return pvp;
    }

    public void setPvp(BigDecimal pvp) {
        this.pvp = pvp;
    }

    public BigDecimal getMajoration() {
        return majoration;
    }

    public void setMajoration(BigDecimal majoration) {
        this.majoration = majoration;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public String getGest() {
        return gest;
    }

    public void setGest(String gest) {
        this.gest = gest;
    }

    public Date getDatArr() {
        return datArr;
    }

    public void setDatArr(Date datArr) {
        this.datArr = datArr;
    }

    public BigDecimal getQteArr() {
        return qteArr;
    }

    public void setQteArr(BigDecimal qteArr) {
        this.qteArr = qteArr;
    }

    public String getNumCha() {
        return numCha;
    }

    public void setNumCha(String numCha) {
        this.numCha = numCha;
    }

    public String getUtilis() {
        return utilis;
    }

    public void setUtilis(String utilis) {
        this.utilis = utilis;
    }

    public String getSatisfait() {
        return satisfait;
    }

    public void setSatisfait(String satisfait) {
        this.satisfait = satisfait;
    }

    public BigDecimal getQtecom() {
        return qtecom;
    }

    public void setQtecom(BigDecimal qtecom) {
        this.qtecom = qtecom;
    }

    public String getNumdoss() {
        return numdoss;
    }

    public void setNumdoss(String numdoss) {
        this.numdoss = numdoss;
    }

    public String getMedtrait() {
        return medtrait;
    }

    public void setMedtrait(String medtrait) {
        this.medtrait = medtrait;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Date getDatinv() {
        return datinv;
    }

    public void setDatinv(Date datinv) {
        this.datinv = datinv;
    }

    public String getNumVir() {
        return numVir;
    }

    public void setNumVir(String numVir) {
        this.numVir = numVir;
    }

    public String getEtatDep() {
        return etatDep;
    }

    public void setEtatDep(String etatDep) {
        this.etatDep = etatDep;
    }

    public Date getDatinvdep() {
        return datinvdep;
    }

    public void setDatinvdep(Date datinvdep) {
        this.datinvdep = datinvdep;
    }

    public BigDecimal getPriAchApresProrata() {
        return priAchApresProrata;
    }

    public void setPriAchApresProrata(BigDecimal priAchApresProrata) {
        this.priAchApresProrata = priAchApresProrata;
    }

    public BigDecimal getPrixMajore() {
        return prixMajore;
    }

    public void setPrixMajore(BigDecimal prixMajore) {
        this.prixMajore = prixMajore;
    }

    public Date getDatPer() {
        return datPer;
    }

    public void setDatPer(Date datPer) {
        this.datPer = datPer;
    }

    public Character getModifier() {
        return modifier;
    }

    public void setModifier(Character modifier) {
        this.modifier = modifier;
    }

    public BigDecimal getPoids() {
        return poids;
    }

    public void setPoids(BigDecimal poids) {
        this.poids = poids;
    }

    public Date getHeureSysteme() {
        return heureSysteme;
    }

    public void setHeureSysteme(Date heureSysteme) {
        this.heureSysteme = heureSysteme;
    }

    public BigDecimal getTxMajoration() {
        return txMajoration;
    }

    public void setTxMajoration(BigDecimal txMajoration) {
        this.txMajoration = txMajoration;
    }

    public BigDecimal getQtePans() {
        return qtePans;
    }

    public void setQtePans(BigDecimal qtePans) {
        this.qtePans = qtePans;
    }

    public BigDecimal getQteAnest() {
        return qteAnest;
    }

    public void setQteAnest(BigDecimal qteAnest) {
        this.qteAnest = qteAnest;
    }

    public String getNumBonDepot() {
        return numBonDepot;
    }

    public void setNumBonDepot(String numBonDepot) {
        this.numBonDepot = numBonDepot;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getQteComSauv() {
        return qteComSauv;
    }

    public void setQteComSauv(BigDecimal qteComSauv) {
        this.qteComSauv = qteComSauv;
    }

    public String getCodTvaAch() {
        return codTvaAch;
    }

    public void setCodTvaAch(String codTvaAch) {
        this.codTvaAch = codTvaAch;
    }

    public BigDecimal getTauTvaAch() {
        return tauTvaAch;
    }

    public void setTauTvaAch(BigDecimal tauTvaAch) {
        this.tauTvaAch = tauTvaAch;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }
//
//    public String getCategDepot() {
//        return categDepot;
//    }
//
//    public void setCategDepot(String categDepot) {
//        this.categDepot = categDepot;
//    }

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

    public Date getDateSysteme() {
        return dateSysteme;
    }

    public void setDateSysteme(Date dateSysteme) {
        this.dateSysteme = dateSysteme;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public boolean isaRemplacer() {
        return aRemplacer;
    }

    public void setaRemplacer(boolean aRemplacer) {
        this.aRemplacer = aRemplacer;
    }

    public FactureDR getFactureDR() {
        return factureDR;
    }

    public void setFactureDR(FactureDR factureDR) {
        this.factureDR = factureDR;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mvtstoDRPK != null ? mvtstoDRPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MvtstoDR)) {
            return false;
        }
        MvtstoDR other = (MvtstoDR) object;
        if ((this.mvtstoDRPK == null && other.mvtstoDRPK != null) || (this.mvtstoDRPK != null && !this.mvtstoDRPK.equals(other.mvtstoDRPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MvtstoDR{" + "coddep=" + coddep + ", numdoss=" + numdoss + ", unite=" + unite + '}';
    }



  
}
