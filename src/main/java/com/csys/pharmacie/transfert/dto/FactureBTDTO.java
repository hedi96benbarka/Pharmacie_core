package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.SatisfactionEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FactureBTDTO {

    private BigDecimal mntbon;

    @Size(min = 0, max = 140)
    private String memop;
    @NotNull
    private Integer sourceID;
    @NotNull
    private Integer destinationID;

    private Boolean interdpot;

    @Valid
    @NotNull
    private List<MvtstoBTDTO> details;

    @Size(min = 1, max = 20)
    private String code;

    @Size(min = 0, max = 20)
    private String userCreate;

    private String numaffiche;

    private LocalDateTime dateCreate;
    @Enumerated(EnumType.STRING)
    private CategorieDepotEnum categDepot;

    private String sourceDesignation;

    private String destinationDesignation;

    private String sourceCodeSaisi;

    private String destinationCodeSaisi;

    @NotNull
    private boolean avoirTransfert;

    private SatisfactionEnum satisf;

    private String userAnnule;

    private LocalDateTime dateAnnule;

    private Boolean automatique;

    @NotNull
    @Valid
    private List<String> relativesBons;

    private Boolean perime;

    private Boolean valide;

    private Boolean conforme;

    private String userValidate;

    private LocalDateTime dateValidate;

    private String memoValidate;

    public FactureBTDTO(Integer sourceID, Integer destinationID) {
        this.sourceID = sourceID;
        this.destinationID = destinationID;
    }

    public FactureBTDTO() {
    }

    

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

    public List<MvtstoBTDTO> getDetails() {
        return details;
    }

    public void setDetails(List<MvtstoBTDTO> details) {
        this.details = details;
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

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
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

    public List<String> getRelativesBons() {
        return relativesBons;
    }

    public void setRelativesBons(List<String> relativesBons) {
        this.relativesBons = relativesBons;
    }

    public String getSourceCodeSaisi() {
        return sourceCodeSaisi;
    }

    public void setSourceCodeSaisi(String sourceCodeSaisi) {
        this.sourceCodeSaisi = sourceCodeSaisi;
    }

    public String getDestinationCodeSaisi() {
        return destinationCodeSaisi;
    }

    public void setDestinationCodeSaisi(String destinationCodeSaisi) {
        this.destinationCodeSaisi = destinationCodeSaisi;
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

    public String getUserAnnule() {
        return userAnnule;
    }

    public void setUserAnnule(String userAnnule) {
        this.userAnnule = userAnnule;
    }

    public LocalDateTime getDateAnnule() {
        return dateAnnule;
    }

    public void setDateAnnule(LocalDateTime dateAnnule) {
        this.dateAnnule = dateAnnule;
    }

    public Boolean getAutomatique() {
        return automatique;
    }

    public void setAutomatique(Boolean automatique) {
        this.automatique = automatique;
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

    public Boolean getConforme() {
        return conforme;
    }

    public void setConforme(Boolean conforme) {
        this.conforme = conforme;
    }

    public String getUserValidate() {
        return userValidate;
    }

    public void setUserValidate(String userValidate) {
        this.userValidate = userValidate;
    }

    public LocalDateTime getDateValidate() {
        return dateValidate;
    }

    public void setDateValidate(LocalDateTime dateValidate) {
        this.dateValidate = dateValidate;
    }

    public String getMemoValidate() {
        return memoValidate;
    }

    public void setMemoValidate(String memoValidate) {
        this.memoValidate = memoValidate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final FactureBTDTO other = (FactureBTDTO) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

}
