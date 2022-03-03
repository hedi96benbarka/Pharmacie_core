package com.csys.pharmacie.vente.quittance.dto;

import java.math.BigDecimal;

public class DetailsReglementDTO {

    private String codeAdmission;

    private BigDecimal mntBen;

    private Long codeReglement;

    private Integer codePrestation;

    private Integer codeIntervenant;

    private Long codeDetailsAdmission;

    public String getCodeAdmission() {
        return codeAdmission;
    }

    public int getCodePrestation() {
        return codePrestation;
    }

    public void setCodePrestation(Integer codePrestation) {
        this.codePrestation = codePrestation;
    }

    public int getCodeIntervenant() {
        return codeIntervenant;
    }

    public void setCodeIntervenant(Integer codeIntervenant) {
        this.codeIntervenant = codeIntervenant;
    }

    public long getCodeDetailsAdmission() {
        return codeDetailsAdmission;
    }

    public void setCodeDetailsAdmission(Long codeDetailsAdmission) {
        this.codeDetailsAdmission = codeDetailsAdmission;
    }

    public void setCodeAdmission(String codeAdmission) {
        this.codeAdmission = codeAdmission;
    }

    public BigDecimal getMntBen() {
        return mntBen;
    }

    public void setMntBen(BigDecimal mntBen) {
        this.mntBen = mntBen;
    }

    public Long getCodeReglement() {
        return codeReglement;
    }

    public void setCodeReglement(Long codeReglement) {
        this.codeReglement = codeReglement;
    }

}
