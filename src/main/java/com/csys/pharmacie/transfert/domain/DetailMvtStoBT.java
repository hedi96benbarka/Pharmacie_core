/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import com.csys.pharmacie.helper.DetailMvtSto;
import com.csys.pharmacie.stock.domain.Depsto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity
@Audited
@Table(name = "detail_MvtStoBT")
@AuditTable("detail_MvtStoBT_AUD")

public class DetailMvtStoBT extends DetailMvtSto {

    private static final long serialVersionUID = 1L;

//    @JoinColumn(name = "code_depsto", referencedColumnName = "code", insertable = false, updatable = false)
//    @ManyToOne(optional = true)
//
//    private Depsto depsto;

    @JoinColumn(name = "code_mvtsto", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private MvtStoBT mvtStoBT;

    public DetailMvtStoBT() {
    }

    public DetailMvtStoBT(Integer codemvt, Depsto depsto, BigDecimal quantiteDisponible, BigDecimal quantitePrelevee, String numbon, String lotinter, LocalDate datPer, BigDecimal priuni, Integer unite, LocalDateTime datSYS, BigDecimal stkRel) {
        super(codemvt, depsto, quantiteDisponible, quantitePrelevee, numbon, lotinter, datPer, priuni, unite, datSYS, stkRel);
    }

//    public Depsto getDepsto() {
//        return depsto;
//    }
//
//    public void setDepsto(Depsto depsto) {
//        this.depsto = depsto;
//    }

    public MvtStoBT getMvtStoBT() {
        return mvtStoBT;
    }

    public void setMvtStoBT(MvtStoBT mvtStoBT) {
        this.mvtStoBT = mvtStoBT;
    }

    }
