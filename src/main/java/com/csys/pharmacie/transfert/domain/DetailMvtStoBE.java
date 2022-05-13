/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import com.csys.pharmacie.helper.DetailMvtSto;
import com.csys.pharmacie.stock.domain.Depsto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "detail_MvtStoBE")

public class DetailMvtStoBE extends DetailMvtSto {

    @JoinColumn(name = "code_depsto", referencedColumnName = "code", insertable = false, updatable = false)
    @OneToOne
    private Depsto depsto;
//    @JoinColumns({
//        @JoinColumn(name = "numbon", referencedColumnName = "numbon", insertable = false, updatable = false),
//        @JoinColumn(name = "code_article", referencedColumnName = "codart", insertable = false, updatable = false),
//        @JoinColumn(name = "numorder", referencedColumnName = "numordre", insertable = false, updatable = false)})
//    @ManyToOne(optional = false)
    @JoinColumn(name = "code_mvtsto", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private MvtStoBE mvtStoBE;

    public DetailMvtStoBE() {       
    }        

    public DetailMvtStoBE(Depsto depsto, MvtStoBE mvtStoBE) {
        this.depsto = depsto;
        this.mvtStoBE = mvtStoBE;
    }

    public Depsto getDepsto() {
        return depsto;
    }

    public void setDepsto(Depsto depsto) {
        this.depsto = depsto;
    }

    public MvtStoBE getMvtStoBE() {
        return mvtStoBE;
    }

    public void setMvtStoBE(MvtStoBE mvtStoBT) {
        this.mvtStoBE = mvtStoBT;
    }

    @Override
    public String toString() {
        return "DetailMvtStoBE{" +
                "depsto=" + depsto +
                '}';
    }
}
