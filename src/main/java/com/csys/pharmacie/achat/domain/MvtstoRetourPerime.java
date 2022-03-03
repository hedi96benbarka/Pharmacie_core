/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.MvtSto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author bassatine
 */
@Entity
@Table(name = "MvtStoRetour_perime")
public class MvtstoRetourPerime extends MvtSto implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mvtstoRetourPerime")
    private List<DetailMvtStoRetourPerime> detailMvtStoList;

    @Size(min = 1, max = 6)
    @Column(name = "numordre")
    private String numordre;

    @NotNull
    @Column(name = "codtva")
    private Integer codtva;

    @NotNull
    @Column(name = "tautva")
    private BigDecimal tautva;

    @JoinColumn(name = "numbon", referencedColumnName = "numbon")
    @ManyToOne(optional = false)
    private RetourPerime factureRetourPerime;

    public MvtstoRetourPerime(BigDecimal tautva, BigDecimal quantite, BigDecimal priuni) {
        super(quantite, priuni);
        this.tautva = tautva;
    }

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    public List<DetailMvtStoRetourPerime> getDetailMvtStoList() {
        return detailMvtStoList;
    }

    public void setDetailMvtStoList(List<DetailMvtStoRetourPerime> detailMvtStoList) {
        this.detailMvtStoList = detailMvtStoList;
    }

    public RetourPerime getFactureRetourPerime() {
        return factureRetourPerime;
    }

    public void setFactureRetourPerime(RetourPerime factureRetourPerime) {
        this.factureRetourPerime = factureRetourPerime;
    }

    public MvtstoRetourPerime(List<DetailMvtStoRetourPerime> detailMvtStoList, String numordre, Integer codtva, BigDecimal tautva, RetourPerime FactureRetour_perime) {
        this.detailMvtStoList = detailMvtStoList;
        this.numordre = numordre;
        this.codtva = codtva;
        this.tautva = tautva;
        this.factureRetourPerime = FactureRetour_perime;
    }

    public MvtstoRetourPerime() {
    }

}
