/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.Audited;

/**
 *
 * @author Farouk
 */
@MappedSuperclass
@Audited
public abstract class BaseDetailBon {

    @NotNull
    @Column(name = "categ_depot")
    @Enumerated(EnumType.STRING)
    private CategorieDepotEnum categDepot;
    @NotNull
    @Size(max = 255)
    @Column(name = "desart")
    private String desart;
    @NotNull
    @Size(max = 255)
    @Column(name = "desart_sec")
    private String desArtSec;
    @NotNull
    @Size(max = 50)
    @Column(name = "code_saisi")
    private String codeSaisi;
    @NotNull
    @Column(name = "quantite")
    private BigDecimal quantite;

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
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

    public BaseDetailBon(BaseDetailBon baseDetailBon) {
        this.categDepot = baseDetailBon.categDepot;
        this.desart = baseDetailBon.desart;
        this.desArtSec = baseDetailBon.desArtSec;
        this.codeSaisi = baseDetailBon.codeSaisi;
        this.quantite = baseDetailBon.quantite;
    }

    public BaseDetailBon() {
    }

    @Override
    public String toString() {
        return "BaseDetailBon{" + "desart=" + desart + ", desArtSec=" + desArtSec + ", codeSaisi=" + codeSaisi + ", quantite=" + quantite + '}';
    }

}
