package com.csys.pharmacie.stock.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DecoupageDTO {

    @Size(min = 1, max = 20)
    private String numbon;

    private LocalDateTime datbon;
    private String memop;
    @NotNull
    private CategorieDepotEnum categDepot;
    private String numaffiche;

    @Size(min = 1, max = 50)
    private String codvend;

    private TypeBonEnum typbon;

    @NotNull
    private Integer coddep;
    private String CodeSaisiDepot;

    private String designationDepot;
    private boolean auto;

    @NotNull
    @Valid
    private List<DetailDecoupageDTO> details;

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public LocalDateTime getDatbon() {
        return datbon;
    }

    public void setDatbon(LocalDateTime datbon) {
        this.datbon = datbon;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public TypeBonEnum getTypbon() {
        return typbon;
    }

    public void setTypbon(TypeBonEnum typbon) {
        this.typbon = typbon;
    }

    public List<DetailDecoupageDTO> getDetails() {
        return details;
    }

    public void setDetails(List<DetailDecoupageDTO> details) {
        this.details = details;
    }

    public String getCodeSaisiDepot() {
        return CodeSaisiDepot;
    }

    public void setCodeSaisiDepot(String CodeSaisiDepot) {
        this.CodeSaisiDepot = CodeSaisiDepot;
    }

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

}
