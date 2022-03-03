/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MvtstoDRDTO {

    @NotNull
    private long coddep;
    @NotNull
    private String numordre;
    @NotNull
    private Integer codart;
    private String lot;
    private String typbon;
    private Date datbon;
    private String numfacbl;
    private Date datefacbl;
    private BigDecimal quantite;
    private String desart;

    private BigDecimal priuni;

    private BigDecimal remise;

    private BigDecimal montht;

    private BigDecimal tautva;

    private Boolean fodart;

    private Boolean homologue;

    private String deptr;

    private BigDecimal priach;

    private BigDecimal marge;

    private Short nbrpce;

    private String memoart;

    private BigDecimal ancienQte;

    private BigDecimal ancienValfod;

    private BigDecimal ancienValrem;

    private String numaffiche;

    private Integer codtva;

    private String typmvt;

    private BigDecimal qteben;

    private BigDecimal prixben;

    private BigDecimal pvp;

    private BigDecimal majoration;

    private String codvend;

    private String gest;

    private Date datArr;

    private BigDecimal qteArr;

    private String numCha;

    private String utilis;

    private String satisfait;

    private BigDecimal qtecom;

    private String numdoss;

    private String medtrait;

    private String etat;

    private Date datinv;

    private String numVir;

    private String etatDep;

    private Date datinvdep;
  
    private BigDecimal priAchApresProrata;

    private BigDecimal prixMajore;

    private Date datPer;
  
    private Character modifier;

    private BigDecimal poids;
   
    private Date heureSysteme;
    @NotNull
    private BigDecimal txMajoration;

    private BigDecimal qtePans;

    private BigDecimal qteAnest;

    private String numBonDepot;
  
    private String reference;

    private BigDecimal qteComSauv;

    private String codTvaAch;

    private BigDecimal tauTvaAch;

    private Integer unite;

    private String desartSec;

    private String codeSaisi;

    private Date dateSysteme;

    private Integer codeUnite;
    
    private String designationUnite;

    private Boolean perissable;
    @NotNull
    private boolean aRemplacer;
    private BigDecimal solde;

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

    public Boolean getPerissable() {
        return perissable;
    }

    public void setPerissable(Boolean perissable) {
        this.perissable = perissable;
    }

    public boolean isaRemplacer() {
        return aRemplacer;
    }

    public void setaRemplacer(boolean aRemplacer) {
        this.aRemplacer = aRemplacer;
    }

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    
}
