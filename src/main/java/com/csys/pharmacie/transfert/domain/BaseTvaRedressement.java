/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import com.csys.pharmacie.helper.BaseTva;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author bassatine
 */
@Entity
@Table(name = "base_tva_redressement")
public class BaseTvaRedressement extends BaseTva implements Serializable {
     private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "redressement", referencedColumnName = "numbon")
    private FactureBE factureBE;

    public FactureBE getFactureBE() {
        return factureBE;
    }

    public void setFactureBE(FactureBE factureBE) {
        this.factureBE = factureBE;
    }

    


    @Override
    public String toString() {
        return "BaseTvaRedressement{" + "factureBE=" + factureBE + '}';
    }

 


    
    
}
