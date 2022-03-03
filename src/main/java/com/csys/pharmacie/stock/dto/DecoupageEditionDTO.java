/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.util.Date;
import java.util.List;

/**
 *
 * @author bassatine
 */
public class DecoupageEditionDTO {
       
  
    private String numbon;
    
    
    private boolean auto;
    
    private Date datbon;


    private CategorieDepotEnum categDepot;

    private String numaffiche ;
    
    private String codvend;

  
    private TypeBonEnum typbon;

 
    private Integer coddep;

    private String designationDepot;

    
    
    private List<DetailDecoupageEditionDTO> details;

   

    public DecoupageEditionDTO() {
    }

    public DecoupageEditionDTO(String numbon, Date datbon, CategorieDepotEnum categDepot, String codvend, TypeBonEnum typbon, Integer coddep, String designationDepot, List<DetailDecoupageEditionDTO> details) {
        this.numbon = numbon;
        this.datbon = datbon;
        this.categDepot = categDepot;
        this.codvend = codvend;
        this.typbon = typbon;
        this.coddep = coddep;
        this.designationDepot = designationDepot;
        this.details = details;
    }

    public List<DetailDecoupageEditionDTO> getDetails() {
        return details;
    }

    public void setDetails(List<DetailDecoupageEditionDTO> details) {
        this.details = details;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }
   public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public Date getDatbon() {
        return datbon;
    }

    public void setDatbon(Date datbon) {
        this.datbon = datbon;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public TypeBonEnum getTypbon() {
        return typbon;
    }

    public void setTypbon(TypeBonEnum typbon) {
        this.typbon = typbon;
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

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }
    
}
