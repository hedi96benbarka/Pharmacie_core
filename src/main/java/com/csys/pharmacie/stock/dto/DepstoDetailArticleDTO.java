/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author MAHMOUD
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepstoDetailArticleDTO {
    
    
    private String lotInter;
    private LocalDate datPer;
    private BigDecimal qte;

    public DepstoDetailArticleDTO(BigDecimal ZERO) {
     }

    public DepstoDetailArticleDTO() {
     }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    @Override
    public String toString() {
        return "DepstoDetailArticleDTO{" + "lotInter=" + lotInter + ", datPer=" + datPer + ", qte=" + qte + '}';
    }
    
    
    
    
}
