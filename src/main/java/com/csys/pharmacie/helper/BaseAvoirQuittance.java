/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Administrateur
 */
public class BaseAvoirQuittance {

    @Size(min = 0, max = 12)
    private String numdoss;
    @NotNull
    private Integer codeDepot;
    @NotEmpty
    private List<BaseTVADTO> baseTvaFactureList;

    @NotNull
    @Size(min = 1, max = 20)
    private String numbon;

    private BigDecimal mntbon;

    private TypeBonEnum typbon;

    private CategorieDepotEnum categDepot;

    private Integer codePrestation;

    private Integer codeOperation;
    
    private BigDecimal mntmpl;
    
    private Boolean avoirPhPostReg;
    
    private BigDecimal montantRemise;

    private Integer codeCostCenterAnalytique;

    public String getNumdoss() {
        return numdoss;
    }

    public void setNumdoss(String numdoss) {
        this.numdoss = numdoss;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    public List<BaseTVADTO> getBaseTvaFactureList() {
        return baseTvaFactureList;
    }

    public void setBaseTvaFactureList(List<BaseTVADTO> baseTvaFactureList) {
        this.baseTvaFactureList = baseTvaFactureList;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
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

    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

    public Integer getCodePrestation() {
        return codePrestation;
    }

    public void setCodePrestation(Integer codePrestation) {
        this.codePrestation = codePrestation;
    }

    public BigDecimal getMntmpl() {
        return mntmpl;
    }

    public void setMntmpl(BigDecimal mntmpl) {
        this.mntmpl = mntmpl;
    }

    public Boolean getAvoirPhPostReg() {
        return avoirPhPostReg;
    }

    public void setAvoirPhPostReg(Boolean avoirPhPostReg) {
        this.avoirPhPostReg = avoirPhPostReg;
    }

    public BigDecimal getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(BigDecimal montantRemise) {
        this.montantRemise = montantRemise;
    }

    public Integer getCodeOperation() {
        return codeOperation;
    }

    public void setCodeOperation(Integer codeOperation) {
        this.codeOperation = codeOperation;
    }

    public Integer getCodeCostCenterAnalytique() {
        return codeCostCenterAnalytique;
    }

    public void setCodeCostCenterAnalytique(Integer codeCostCenterAnalytique) {
        this.codeCostCenterAnalytique = codeCostCenterAnalytique;
    }
}
