package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DetailReceptionTemporaireDTO {

    private BigDecimal qteCom;

    private BigDecimal remise;

    private BigDecimal montht;

    @NotNull
    @Size(min = 1, max = 2)
    private String typeBon;

    private LocalDate dateBon;

    @NotNull
    private BigDecimal tauTVA;

    @NotNull
    private Integer codeTva;

    private Boolean isPrixRef;

    private BigDecimal sellingPrice;

    private BigDecimal baseTva;

    private BigDecimal pmpPrecedant;

    private BigDecimal quantitePrecedante;

    private Integer oldCodtva;

    private BigDecimal oldTautva;

    private Integer codeEmplacement;

    @NotNull
    private CategorieDepotEnum categDepot;

    @NotNull
    @Size(min = 0, max = 255)
    private String desart;

    @NotNull
    @Size(min = 0, max = 255)
    private String desartSec;

    @NotNull
    @Size(min = 0, max = 50)
    private String codeSaisi;

    @NotNull
    private BigDecimal quantite;

    private Integer code;

    private Integer refArt;

    private String numbon;

    @Size(min = 1, max = 50)
    private String lotInter;

    private LocalDate datPer;

    @NotNull
    private BigDecimal priuni;

    private Integer codeUnite;
    private String unitDesignation;

    private Integer codeCommande;
private boolean free;
    public BigDecimal getQteCom() {
        return qteCom;
    }

    public void setQteCom(BigDecimal qteCom) {
        this.qteCom = qteCom;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
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

    public String getTypeBon() {
        return typeBon;
    }

    public void setTypeBon(String typeBon) {
        this.typeBon = typeBon;
    }

    public LocalDate getDateBon() {
        return dateBon;
    }

    public void setDateBon(LocalDate dateBon) {
        this.dateBon = dateBon;
    }

    public BigDecimal getTauTVA() {
        return tauTVA;
    }

    public void setTauTVA(BigDecimal tauTVA) {
        this.tauTVA = tauTVA;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public Boolean getIsPrixRef() {
        return isPrixRef;
    }

    public void setIsPrixRef(Boolean isPrixRef) {
        this.isPrixRef = isPrixRef;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getBaseTva() {
        return baseTva;
    }

    public void setBaseTva(BigDecimal baseTva) {
        this.baseTva = baseTva;
    }

    public BigDecimal getPmpPrecedant() {
        return pmpPrecedant;
    }

    public void setPmpPrecedant(BigDecimal pmpPrecedant) {
        this.pmpPrecedant = pmpPrecedant;
    }

    public BigDecimal getQuantitePrecedante() {
        return quantitePrecedante;
    }

    public void setQuantitePrecedante(BigDecimal quantitePrecedante) {
        this.quantitePrecedante = quantitePrecedante;
    }

    public Integer getOldCodtva() {
        return oldCodtva;
    }

    public void setOldCodtva(Integer oldCodtva) {
        this.oldCodtva = oldCodtva;
    }

    public BigDecimal getOldTautva() {
        return oldTautva;
    }

    public void setOldTautva(BigDecimal oldTautva) {
        this.oldTautva = oldTautva;
    }

    public Integer getCodeEmplacement() {
        return codeEmplacement;
    }

    public void setCodeEmplacement(Integer codeEmplacement) {
        this.codeEmplacement = codeEmplacement;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getRefArt() {
        return refArt;
    }

    public void setRefArt(Integer refArt) {
        this.refArt = refArt;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
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

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public Integer getUnite() {
        return codeUnite;
    }

    public void setUnite(Integer unite) {
        this.codeUnite = unite;
    }

    public String getUnitDesignation() {
        return unitDesignation;
    }

    public void setUnitDesignation(String unitDesignation) {
        this.unitDesignation = unitDesignation;
    }

    public Integer getCodeCommande() {
        return codeCommande;
    }

    public void setCodeCommande(Integer codeCommande) {
        this.codeCommande = codeCommande;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    
    
    @Override
    public String toString() {
        return "DetailReceptionTemporaireDTO{" + "qteCom=" + qteCom + ", isPrixRef=" + isPrixRef + ", desartSec=" + desartSec + ", codeSaisi=" + codeSaisi + ", quantite=" + quantite + ", refArt=" + refArt + ", numbon=" + numbon + ", priuni=" + priuni + '}';
    }

}
