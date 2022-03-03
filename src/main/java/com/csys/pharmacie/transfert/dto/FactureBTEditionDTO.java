package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.SatisfactionEnum;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Size;

public class FactureBTEditionDTO {

    private BigDecimal mntbon;

    @Size(min = 0, max = 140)
    private String memop;

    private Integer sourceID;

    private Integer destinationID;

    private Boolean interdpot;

    @Size(min = 1, max = 20)
    private String code;

    @Size(min = 0, max = 20)
    private String userCreate;

    private String numaffiche;

    private Date dateCreate;

    private CategorieDepotEnum categDepot;

    private String sourceDesignation;

    private String destinationDesignation;

    private String details;

    private boolean avoirTransfert;

    private SatisfactionEnum satisf;

    private List<MvtstoBTEditionDTO> listMvtsto;

    private Boolean perime;

    private Boolean valide;

    private String userValidate;

    private Date dateValidate;

    public Integer getSourceID() {
        return sourceID;
    }

    public void setSourceID(Integer sourceID) {
        this.sourceID = sourceID;
    }

    public Integer getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(Integer destinationID) {
        this.destinationID = destinationID;
    }

    public Boolean getInterdpot() {
        return interdpot;
    }

    public void setInterdpot(Boolean interdpot) {
        this.interdpot = interdpot;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getSourceDesignation() {
        return sourceDesignation;
    }

    public void setSourceDesignation(String sourceDesignation) {
        this.sourceDesignation = sourceDesignation;
    }

    public String getDestinationDesignation() {
        return destinationDesignation;
    }

    public void setDestinationDesignation(String destinationDesignation) {
        this.destinationDesignation = destinationDesignation;
    }

    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public boolean isInterdpot() {
        return interdpot;
    }

    public void setInterdpot(boolean interdpot) {
        this.interdpot = interdpot;
    }

    public boolean isAvoirTransfert() {
        return avoirTransfert;
    }

    public void setAvoirTransfert(boolean avoirTransfert) {
        this.avoirTransfert = avoirTransfert;
    }

    public SatisfactionEnum getSatisf() {
        return satisf;
    }

    public void setSatisf(SatisfactionEnum satisf) {
        this.satisf = satisf;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<MvtstoBTEditionDTO> getListMvtsto() {
        return listMvtsto;
    }

    public void setListMvtsto(List<MvtstoBTEditionDTO> listMvtsto) {
        this.listMvtsto = listMvtsto;
    }

    public Boolean getPerime() {
        return perime;
    }

    public void setPerime(Boolean perime) {
        this.perime = perime;
    }

    public Boolean getValide() {
        return valide;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }

    public String getUserValidate() {
        return userValidate;
    }

    public void setUserValidate(String userValidate) {
        this.userValidate = userValidate;
    }

    public Date getDateValidate() {
        return dateValidate;
    }

    public void setDateValidate(Date dateValidate) {
        this.dateValidate = dateValidate;
    }

    
    @Override
    public String toString() {
        return "FactureBTEditionDTO{" + "mntbon=" + mntbon + ", memop=" + memop + ", sourceID=" + sourceID + ", destinationID=" + destinationID + ", interdpot=" + interdpot + ", code=" + code + ", userCreate=" + userCreate + ", numaffiche=" + numaffiche + ", dateCreate=" + dateCreate + ", categDepot=" + categDepot + ", sourceDesignation=" + sourceDesignation + ", destinationDesignation=" + destinationDesignation + ", details=" + details + ", avoirTransfert=" + avoirTransfert + ", satisf=" + satisf + ", listMvtsto=" + listMvtsto + ", perime=" + perime + '}';
    }

}
