package com.csys.pharmacie.vente.avoir.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MvtStoAVDTO {

    @NotNull
    private Integer codart;

    @NotNull
    private String numbon;

    private String numaffiche;

    private LocalDateTime datbon;

    private String numordre;

    private TypeBonEnum typbon;

    @NotNull
    private BigDecimal priuni;

    private BigDecimal montht;

    private BigDecimal tautva;

    private Integer codtva;

    private BigDecimal priach;

    @Size(
            min = 0,
            max = 500
    )
    private String memoart;

    private LocalDate dateSysteme;

    private LocalTime heureSysteme;

    @NotNull
    private Integer unityCode;

    private String unityDesignation;

    @NotNull
    private CategorieDepotEnum categDepot;

    @NotNull
    @Size(
            min = 0,
            max = 255
    )
    private String desart;

    @NotNull
    @Size(
            min = 0,
            max = 255
    )
    private String desArtSec;

    @NotNull
    @Size(
            min = 0,
            max = 50
    )
    private String codeSaisi;

    @NotNull
    private BigDecimal quantite;

    private String codvend;

    private BigDecimal ajustement;

    private BigDecimal prixBrute;

    private BigDecimal tauxCouverture;

    @Size(min = 1, max = 50)
    private String lot;

    private LocalDate datPer;

    private Date datPerDate;
    
    private Integer codeSociete;
    
    private String remiseConventionnellePharmacie;

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public LocalDateTime getDatbon() {
        return datbon;
    }

    public void setDatbon(LocalDateTime datbon) {
        this.datbon = datbon;
    }

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
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

    public void setHeureSysteme(LocalTime heureSysteme) {
        this.heureSysteme = heureSysteme;
    }

    public Integer getUnityCode() {
        return unityCode;
    }

    public void setUnityCode(Integer unityCode) {
        this.unityCode = unityCode;
    }

    public String getUnityDesignation() {
        return unityDesignation;
    }

    public void setUnityDesignation(String unityDesignation) {
        this.unityDesignation = unityDesignation;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesArtSec() {
        return desArtSec;
    }

    public void setDesArtSec(String desArtSec) {
        this.desArtSec = desArtSec;
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

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
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

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public Date getDatPerDate() {
        return datPerDate;
    }

    public void setDatPerDate(Date datPerDate) {
        this.datPerDate = datPerDate;
    }

    public Integer getCodeSociete() {
        return codeSociete;
    }

    public void setCodeSociete(Integer codeSociete) {
        this.codeSociete = codeSociete;
    }

    public String getRemiseConventionnellePharmacie() {
        return remiseConventionnellePharmacie;
    }

    public void setRemiseConventionnellePharmacie(String remiseConventionnellePharmacie) {
        this.remiseConventionnellePharmacie = remiseConventionnellePharmacie;
    }

    }
