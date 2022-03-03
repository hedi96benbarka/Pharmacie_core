/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
@Embeddable
public class AjustementRetourFournisseurPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbon")
    private String numbon;
   
    @NotNull
    @Column(name = "codart")
    private Integer codart;
   
    @NotNull
    @Column(name = "code_unite")
    private Integer codeUnite;
   
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "num_retour")
    private String numRetour;

    public AjustementRetourFournisseurPK() {
    }

    public AjustementRetourFournisseurPK(String numbon, Integer codart, Integer codeUnite, String numRetour) {
        this.numbon = numbon;
        this.codart = codart;
        this.codeUnite = codeUnite;
        this.numRetour = numRetour;
    }



    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getNumRetour() {
        return numRetour;
    }

    public void setNumRetour(String numRetour) {
        this.numRetour = numRetour;
    }


    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.AjustementRetourFournisseurPK[ numbon=" + numbon + ", codart=" + codart + ", codeUnite=" + codeUnite + ", numRetour=" + numRetour + " ]";
    }
    
}
