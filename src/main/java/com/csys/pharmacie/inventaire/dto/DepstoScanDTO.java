package com.csys.pharmacie.inventaire.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DepstoScanDTO {

    private Long num;

    @NotNull
    private Integer codart;

    @NotNull
    private Integer unite;

    private Integer coddep;

    @NotNull
    private CategorieDepotEnum categDepot;

    @Size(
            min = 1,
            max = 255
    )
    private String desart;

    @Size(
            min = 1,
            max = 255
    )
    private String desartSec;

    @NotNull
    private BigDecimal quantite;

    private LocalDate datPer;
    
    private Date datPerEdition;

    @Size(
            min = 0,
            max = 50
    )
    private String lotInter;

    @Size(
            min = 1,
            max = 50
    )
    private String codeSaisi;

    private boolean defectueux;

    private String uniteDesignation;

    private boolean importer;

    private boolean inventerier;
    private Integer categArt;
    
    private Integer inventaire ; 

    public Integer getInventaire() {
        return inventaire;
    }

    public void setInventaire(Integer inventaire) {
        this.inventaire = inventaire;
    }
    
    

    public Integer getCategArt() {
        return categArt;
    }

    public void setCategArt(Integer categArt) {
        this.categArt = categArt;
    }

    private LocalDateTime heureSysteme;

    @NotNull
    @Size(
            min = 1,
            max = 50
    )
    private String adresseMac;

    @NotNull
    @Size(
            min = 1,
            max = 20
    )
    private String userName;

    public String getUniteDesignation() {
        return uniteDesignation;
    }

    public void setUniteDesignation(String uniteDesignation) {
        this.uniteDesignation = uniteDesignation;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
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

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public boolean getDefectueux() {
        return defectueux;
    }

    public void setDefectueux(boolean defectueux) {
        this.defectueux = defectueux;
    }

    public boolean getImporter() {
        return importer;
    }

    public void setImporter(boolean importer) {
        this.importer = importer;
    }

    public boolean getInventerier() {
        return inventerier;
    }

    public void setInventerier(boolean inventerier) {
        this.inventerier = inventerier;
    }

    public LocalDateTime getHeureSysteme() {
        return heureSysteme;
    }

    public void setHeureSysteme(LocalDateTime heureSysteme) {
        this.heureSysteme = heureSysteme;
    }

    public String getAdresseMac() {
        return adresseMac;
    }

    public void setAdresseMac(String adresseMac) {
        this.adresseMac = adresseMac;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
       public Date getDatPerReturningDate(){
        Date date = Date.from(datPer.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    public Date getDatPerEdition() {
        return datPerEdition;
    }

    public void setDatPerEdition(Date datPerEdition) {
        this.datPerEdition = datPerEdition;
    }
       
    @Override
    public String toString() {
        return "DepstoScanDTO{" + "codart=" + codart + ", unite=" + unite + ", coddep=" + coddep + ", categDepot=" + categDepot + ", desart=" + desart + ", desartSec=" + desartSec + ", quantite=" + quantite + ", datPer=" + datPer + ", lotInter=" + lotInter + ", codeSaisi=" + codeSaisi + ", uniteDesignation=" + uniteDesignation + ", categArt=" + categArt + ", userName=" + userName + '}';
    }
       
       
}
