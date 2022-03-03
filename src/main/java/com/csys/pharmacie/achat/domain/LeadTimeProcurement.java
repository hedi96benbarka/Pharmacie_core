/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "leadTimeProcurement")
public class LeadTimeProcurement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 510)
    @Column(name = "id")
    private String id;
    @Column(name = "difference_da_reception")
    private Integer differenceDaReception;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_article")
    private int codeArticle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "categorie_article")
    private String categorieArticle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_unite")
    private int codeUnite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_da")
    private int codeDa;
    @Column(name = "date_validate_da")
    @Temporal(TemporalType.DATE)
    private Date dateValidateDa;
    @Size(max = 14)
    @Column(name = "num_affiche_reception")
    private String numAfficheReception;
    @Column(name = "date_bon_reception")
    @Temporal(TemporalType.DATE)
    private Date dateBonReception;

    public LeadTimeProcurement() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDifferenceDaReception() {
        return differenceDaReception;
    }

    public void setDifferenceDaReception(Integer differenceDaReception) {
        this.differenceDaReception = differenceDaReception;
    }

    public int getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(int codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getCategorieArticle() {
        return categorieArticle;
    }

    public void setCategorieArticle(String categorieArticle) {
        this.categorieArticle = categorieArticle;
    }

    public int getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(int codeUnite) {
        this.codeUnite = codeUnite;
    }

    public int getCodeDa() {
        return codeDa;
    }

    public void setCodeDa(int codeDa) {
        this.codeDa = codeDa;
    }

    public Date getDateValidateDa() {
        return dateValidateDa;
    }

    public void setDateValidateDa(Date dateValidateDa) {
        this.dateValidateDa = dateValidateDa;
    }

    public String getNumAfficheReception() {
        return numAfficheReception;
    }

    public void setNumAfficheReception(String numAfficheReception) {
        this.numAfficheReception = numAfficheReception;
    }

    public Date getDateBonReception() {
        return dateBonReception;
    }

    public void setDateBonReception(Date dateBonReception) {
        this.dateBonReception = dateBonReception;
    }
    
}
