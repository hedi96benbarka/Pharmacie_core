///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.csys.pharmacie.inventaire.domain;
//
//import com.csys.pharmacie.helper.CategorieDepotEnum;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//import javax.persistence.Basic;
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//
///**
// *
// * @author Administrateur
// */
//@Entity 
//@Table(name = "MvtStoINV")
//public class MvtStoINV implements Serializable {
//
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "unite")
//    private int unite;
//
//    private static final long serialVersionUID = 1L;
//    @Id
//    @Basic(optional = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "Num")
//    private Long num;
//    @Basic(optional = false)
//    @NotNull
//
//    @Column(name = "codart")
//    private Integer codart;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 20)
//    @Column(name = "numbon")
//    private String numbon;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "codInventaire")
//    private int codInventaire;
//    @Column(name = "coddep")
//    private Integer coddep;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 2)
//    @Column(name = "typbon")
//    private String typbon;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 255)
//    @Column(name = "desart")
//    private String desart;
//    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "quantite")
//    private BigDecimal quantite;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "priuni")
//    private BigDecimal priuni;
//    @Column(name = "DatPer")
//    private LocalDate datPer;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "HeureSysteme")
//    private LocalDateTime heureSysteme;
//    @Size(max = 50)
//    @Column(name = "Lot_Inter")
//    private String lotInter;
//    @Size(max = 50)
//    @Column(name = "Numero_AMC")
//    private String numeroAMC;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 255)
//    @Column(name = "desart_sec")
//    private String desartSec;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "categ_depot")
//    private CategorieDepotEnum categDepot;
//    @Column(name = "is_prix_reference")
//    private Boolean isPrixReference;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 50)
//    @Column(name = "code_saisi")
//    private String codeSaisi;
//
//    @ManyToOne
//
//    @JoinColumn(name = "codInventaire", referencedColumnName = "code" , insertable = false, updatable = false)
//
//    private Inventaire inventaire;
//    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mvtStoINV")
//    private List<DepStoHist> depStoHist;
//    public MvtStoINV() {
//    }
//
//    public MvtStoINV(Long num) {
//        this.num = num;
//    }
//
//    public MvtStoINV(Long num, Integer codart, String numbon, int codInventaire, String typbon, String desart, BigDecimal quantite, BigDecimal priuni, LocalDateTime heureSysteme, String desartSec, CategorieDepotEnum categDepot, String codeSaisi) {
//        this.num = num;
//        this.codart = codart;
//        this.numbon = numbon;
//        this.codInventaire = codInventaire;
//        this.typbon = typbon;
//        this.desart = desart;
//        this.quantite = quantite;
//        this.priuni = priuni;
//        this.heureSysteme = heureSysteme;
//        this.desartSec = desartSec;
//        this.categDepot = categDepot;
//        this.codeSaisi = codeSaisi;
//    }
//
//    public Inventaire getInventaire() {
//        return inventaire;
//    }
//
//    public void setInventaire(Inventaire inventaire) {
//        this.inventaire = inventaire;
//    }
//
//    public Long getNum() {
//        return num;
//    }
//
//    public void setNum(Long num) {
//        this.num = num;
//    }
//
//    public Integer getCodart() {
//        return codart;
//    }
//
//    public void setCodart(Integer codart) {
//        this.codart = codart;
//    }
//
//    public String getNumbon() {
//        return numbon;
//    }
//
//    public void setNumbon(String numbon) {
//        this.numbon = numbon;
//    }
//
//    public int getCodInventaire() {
//        return codInventaire;
//    }
//
//    public void setCodInventaire(int codInventaire) {
//        this.codInventaire = codInventaire;
//    }
//
//    public Integer getCoddep() {
//        return coddep;
//    }
//
//    public void setCoddep(Integer coddep) {
//        this.coddep = coddep;
//    }
//
//    public String getTypbon() {
//        return typbon;
//    }
//
//    public void setTypbon(String typbon) {
//        this.typbon = typbon;
//    }
//
//    public String getDesart() {
//        return desart;
//    }
//
//    public void setDesart(String desart) {
//        this.desart = desart;
//    }
//
//    public BigDecimal getQuantite() {
//        return quantite;
//    }
//
//    public void setQuantite(BigDecimal quantite) {
//        this.quantite = quantite;
//    }
//
//    public BigDecimal getPriuni() {
//        return priuni;
//    }
//
//    public void setPriuni(BigDecimal priuni) {
//        this.priuni = priuni;
//    }
//
//    public LocalDate getDatPer() {
//        return datPer;
//    }
//
//    public void setDatPer(LocalDate datPer) {
//        this.datPer = datPer;
//    }
//
//    public LocalDateTime getHeureSysteme() {
//        return heureSysteme;
//    }
//
//    public void setHeureSysteme(LocalDateTime heureSysteme) {
//        this.heureSysteme = heureSysteme;
//    }
//
//    public String getLotInter() {
//        return lotInter;
//    }
//
//    public void setLotInter(String lotInter) {
//        this.lotInter = lotInter;
//    }
//
//    public String getNumeroAMC() {
//        return numeroAMC;
//    }
//
//    public void setNumeroAMC(String numeroAMC) {
//        this.numeroAMC = numeroAMC;
//    }
//
//    public String getDesartSec() {
//        return desartSec;
//    }
//
//    public void setDesartSec(String desartSec) {
//        this.desartSec = desartSec;
//    }
//
//    public CategorieDepotEnum getCategDepot() {
//        return categDepot;
//    }
//
//    public void setCategDepot(CategorieDepotEnum categDepot) {
//        this.categDepot = categDepot;
//    }
//
//    public Boolean getIsPrixReference() {
//        return isPrixReference;
//    }
//
//    public void setIsPrixReference(Boolean isPrixReference) {
//        this.isPrixReference = isPrixReference;
//    }
//
//    public String getCodeSaisi() {
//        return codeSaisi;
//    }
//
//    public void setCodeSaisi(String codeSaisi) {
//        this.codeSaisi = codeSaisi;
//    }
//
//    public List<DepStoHist> getDepStoHist() {
//        return depStoHist;
//    }
//
//    public void setDepStoHist(List<DepStoHist> depStoHist) {
//        this.depStoHist = depStoHist;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (num != null ? num.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof MvtStoINV)) {
//            return false;
//        }
//        MvtStoINV other = (MvtStoINV) object;
//        if ((this.num == null && other.num != null) || (this.num != null && !this.num.equals(other.num))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "com.csys.pharmacie.achat.domain.MvtStoINV[ num=" + num + " ]";
//    }
//
//    public int getUnite() {
//        return unite;
//    }
//
//    public void setUnite(int unite) {
//        this.unite = unite;
//    }
//
//}
