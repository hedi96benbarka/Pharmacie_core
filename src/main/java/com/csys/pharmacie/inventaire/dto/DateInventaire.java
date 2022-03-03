/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.dto;

import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author Administrateur
 */
public class DateInventaire {
    private Integer categorieArticle;
    private Date heureSysteme;
    private Integer coddep;

    public DateInventaire(Integer categorieArticle, Date heureSysteme, Integer coddep) {
        this.categorieArticle = categorieArticle;
        this.heureSysteme = heureSysteme;
        this.coddep = coddep;
    }

    
    
    public DateInventaire(Integer categorieArticle, Date heureSysteme) {
        this.categorieArticle = categorieArticle;
        this.heureSysteme = heureSysteme;
    }

    public Integer getCategorieArticle() {
        return categorieArticle;
    }

    public void setCategorieArticle(Integer categorieArticle) {
        this.categorieArticle = categorieArticle;
    }

    public Date getHeureSysteme() {
        return heureSysteme;
    }

    public void setHeureSysteme(Date heureSysteme) {
        this.heureSysteme = heureSysteme;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

}
