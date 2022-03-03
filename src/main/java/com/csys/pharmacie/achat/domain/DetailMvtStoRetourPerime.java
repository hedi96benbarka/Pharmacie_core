/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.DetailMvtSto;
import com.csys.pharmacie.stock.domain.Depsto;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author bassatine
 */
@Entity
@Table(name = "detail_MvtStoRetour_perime")
public class DetailMvtStoRetourPerime extends DetailMvtSto {

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
    private MvtstoRetourPerime mvtstoRetourPerime;

    public Depsto getDepsto() {
        return depsto;
    }

    public DetailMvtStoRetourPerime(Integer code, BigDecimal quantitePrelevee, BigDecimal priuni, BigDecimal tauxTva) {
        super(code, quantitePrelevee, priuni, tauxTva);
    }

    public void setDepsto(Depsto depsto) {
        this.depsto = depsto;
    }

    public MvtstoRetourPerime getMvtstoRetourPerime() {
        return mvtstoRetourPerime;
    }

    public void setMvtstoRetourPerime(MvtstoRetourPerime mvtstoRetourPerime) {
        this.mvtstoRetourPerime = mvtstoRetourPerime;
    }

    public DetailMvtStoRetourPerime(Depsto depsto, MvtstoRetourPerime mvtstoRetour_perime) {
        this.depsto = depsto;
        this.mvtstoRetourPerime = mvtstoRetour_perime;
    }

    public DetailMvtStoRetourPerime() {
    }

}
