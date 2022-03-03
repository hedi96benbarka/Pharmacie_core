/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.DetailMvtSto;
import com.csys.pharmacie.stock.domain.Depsto;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "detail_MvtStoAF")

public class DetailMvtStoAF extends DetailMvtSto implements Serializable {

    @JoinColumn(name = "code_depsto", referencedColumnName = "code", insertable = false, updatable = false)
    @OneToOne
    private Depsto depsto;

    @JoinColumn(name = "code_mvtsto", referencedColumnName = "code")
    @ManyToOne
    private MvtstoAF codeMvtstoAF;

    public DetailMvtStoAF() {
    }

    public DetailMvtStoAF(Depsto depsto, MvtstoAF codeMvtstoAF) {
        this.depsto = depsto;
        this.codeMvtstoAF = codeMvtstoAF;
    }

    public Depsto getDepsto() {
        return depsto;
    }

    public void setDepsto(Depsto depsto) {
        this.depsto = depsto;
    }

    public MvtstoAF getCodeMvtstoAF() {
        return codeMvtstoAF;
    }

    public void setCodeMvtstoAF(MvtstoAF codeMvtstoAF) {
        this.codeMvtstoAF = codeMvtstoAF;
    }




}
