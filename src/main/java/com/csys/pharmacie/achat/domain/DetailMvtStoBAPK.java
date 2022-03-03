/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author bassatine
 */
@Embeddable
public class DetailMvtStoBAPK implements Serializable {
    private static final long serialVersionUID = 1L;
    
       @Column(name = "numbon" )
    private String numbon;
      
    @Column(name="codart" )   
    private Integer codart ;
    @Column(name="numordre")
    private String numordre ;
    
      @Column(name = "code_depsto" )
    private Integer codeDepsto;

    public DetailMvtStoBAPK() {              
    }

    public DetailMvtStoBAPK(String numbon, Integer codart, String numordre, Integer codeDepsto) {
        this.numbon = numbon;
        this.codart = codart;
        this.numordre = numordre;
        this.codeDepsto = codeDepsto;
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

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    public Integer getCodeDepsto() {
        return codeDepsto;
    }

    public void setCodeDepsto(Integer codeDepsto) {
        this.codeDepsto = codeDepsto;
    }

    public DetailMvtStoBAPK(String numbon, String numordre, Integer codeDepsto) {
        this.numbon = numbon;
        this.numordre = numordre;
        this.codeDepsto = codeDepsto;
    }

    @Override
    public String toString() {
        return "DetailMvtStoBAPK{" + "numbon=" + numbon + ", codart=" + codart + ", numordre=" + numordre + ", codeDepsto=" + codeDepsto + '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }
      
      
      
    
}
