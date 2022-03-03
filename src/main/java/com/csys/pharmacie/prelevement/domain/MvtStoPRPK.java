/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Hamdi
 */
@Embeddable
public class MvtStoPRPK implements Serializable  {
    
    
     private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "codart")
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "numbon")
    private String numbon;
         @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 17)
    @Column(name = "lot")
    private String lot; 
         @NotNull
      @Column(name = "DatPer")
    private LocalDate datPer;
  

    public MvtStoPRPK() {
    }

    public MvtStoPRPK(Integer codart, String numbon, String lot, LocalDate datPer) {
        this.codart = codart;
        this.numbon = numbon;
        this.lot = lot;
        this.datPer = datPer;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
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


   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codart != null ? codart.hashCode() : 0);
        hash += (numbon != null ? numbon.hashCode() : 0);
        hash += (lot != null ? lot.hashCode() : 0);
          hash += (datPer != null ? datPer.hashCode() : 0);
        return hash;
    }

     @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MvtStoPRPK)) {
            return false;
        }
        MvtStoPRPK other = (MvtStoPRPK) object;
        if ((this.codart == null && other.codart != null) || (this.codart != null && !this.codart.equals(other.codart))) {
            return false;
        }
        if ((this.numbon == null && other.numbon != null) || (this.numbon != null && !this.numbon.equals(other.numbon))) {
            return false;
        }
        if ((this.lot == null && other.lot != null) || (this.lot != null && !this.lot.equals(other.lot))) {
            return false;
        }
        
         if ((this.datPer == null && other.datPer != null) || (this.datPer != null && !this.datPer.equals(other.datPer))) {
            return false;
        }
        
        
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.prelevement.domain.MvtStoPRPK{" + "codart=" + codart + ", numbon=" + numbon + ", lot=" + lot + ", datPer=" + datPer +'}';
    }
    
    
    
    
}
