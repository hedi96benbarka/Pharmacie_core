/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.dto;

import com.csys.pharmacie.stock.domain.*;
import com.csys.pharmacie.helper.ClassificationArticleEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
public class ConsommationReelleForRopDTO  {


    private Integer code;
    private String id;
    private String typbon;
    private Integer codart;
    private String numbon;
    private String numaffiche;
    private Integer codTvaAch;
    private BigDecimal tauTvaAch;
    private BigDecimal priach;
    private String desart;
    private String desArtSec;
    private String codeSaisi;
    private BigDecimal quantite;
    private Integer coddep;
    private String designationDepot;
    private String designationDepotSec;
    private Integer codeUnite;
    private String designationUnite;
    private String designationUniteSec;
    private LocalDateTime date;
    private String categDepot;
    
    private BigDecimal valeur;
    private ClassificationArticleEnum classificationArticle;

    public ConsommationReelleForRopDTO() {
    }

    public ConsommationReelleForRopDTO(Integer code) {
        this.code = code;
    }

    public ConsommationReelleForRopDTO(BigDecimal quantite, BigDecimal valeur) {
        this.quantite = quantite;
        this.valeur = valeur;
    }

    public ConsommationReelleForRopDTO(Integer code, Integer codart, String numbon, String desArtSec, String codeSaisi, Integer coddep, String designationDepotSec, Integer codeUnite, String designationUnite, String designationUniteSec) {
        this.code = code;
        this.codart = codart;
        this.numbon = numbon;
        this.desArtSec = desArtSec;
        this.codeSaisi = codeSaisi;
        this.coddep = coddep;
        this.designationDepotSec = designationDepotSec;
        this.codeUnite = codeUnite;
        this.designationUnite = designationUnite;
        this.designationUniteSec = designationUniteSec;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypbon() {
        return typbon;
    }

    public void setTypbon(String typbon) {
        this.typbon = typbon;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public Integer getCodTvaAch() {
        return codTvaAch;
    }

    public void setCodTvaAch(Integer codTvaAch) {
        this.codTvaAch = codTvaAch;
    }

    public BigDecimal getTauTvaAch() {
        return tauTvaAch;
    }

    public void setTauTvaAch(BigDecimal tauTvaAch) {
        this.tauTvaAch = tauTvaAch;
    }

    public BigDecimal getPriach() {
        return priach;
    }

    public void setPriach(BigDecimal priach) {
        this.priach = priach;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesArtSec() {
        return desArtSec;
    }

    public void setDesArtSec(String desArtSec) {
        this.desArtSec = desArtSec;
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

    public String getDesignationDepotSec() {
        return designationDepotSec;
    }

    public void setDesignationDepotSec(String designationDepotSec) {
        this.designationDepotSec = designationDepotSec;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    public String getDesignationUniteSec() {
        return designationUniteSec;
    }

    public void setDesignationUniteSec(String designationUniteSec) {
        this.designationUniteSec = designationUniteSec;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(String categDepot) {
        this.categDepot = categDepot;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }


    public ClassificationArticleEnum getClassificationArticle() {
        return classificationArticle;
    }

    public void setClassificationArticle(ClassificationArticleEnum classificationArticle) {
        this.classificationArticle = classificationArticle;
    }

    

   

   

}
