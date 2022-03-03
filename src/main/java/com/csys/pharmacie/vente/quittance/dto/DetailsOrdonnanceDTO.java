package com.csys.pharmacie.vente.quittance.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DetailsOrdonnanceDTO {

    private Long id;

    @NotNull
    @Size(
            min = 1,
            max = 9
    )
    private String code;

    @NotNull
    @Size(
            min = 1,
            max = 1073741823
    )
    private String designation;

    private String voieAdministration;

    private String qte;

    private String unite;

    private String date_debut;

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    private String frequence;

    private Integer daysNbr;

    @NotNull
    @Size(
            min = 1,
            max = 1073741823
    )
    private String posologie;

    private String posologieAr;

    private String note;

    @NotNull
    @Size(
            min = 1,
            max = 1073741823
    )
    private String user;

    @Size(
            min = 0,
            max = 250
    )
    private String per;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getVoieAdministration() {
        return voieAdministration;
    }

    public void setVoieAdministration(String voieAdministration) {
        this.voieAdministration = voieAdministration;
    }

    public String getQte() {
        return qte;
    }

    public void setQte(String qte) {
        this.qte = qte;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public Integer getDaysNbr() {
        return daysNbr;
    }

    public void setDaysNbr(Integer daysNbr) {
        this.daysNbr = daysNbr;
    }

    public String getPosologie() {
        return posologie;
    }

    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }

    public String getPosologieAr() {
        return posologieAr;
    }

    public void setPosologieAr(String posologieAr) {
        this.posologieAr = posologieAr;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

}
