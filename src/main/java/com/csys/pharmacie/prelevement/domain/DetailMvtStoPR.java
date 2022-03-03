/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.helper.DetailMvtSto;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Hamdi
 */
@Entity
@Table(name = "detail_MvtStoPR")
public class DetailMvtStoPR extends DetailMvtSto {

     @Column(name = "qtecom")
    private BigDecimal qtecom;
    
    @JoinColumn(name = "code_mvtsto", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private MvtStoPR mvtStoPR;

    @JoinColumn(name = "code_depsto", referencedColumnName = "code", insertable = false, updatable = false)
    @OneToOne
    private Depsto depsto;

    public MvtStoPR getMvtStoPR() {
        return mvtStoPR;
    }

    public void setMvtStoPR(MvtStoPR mvtStoPR) {
        this.mvtStoPR = mvtStoPR;
    }

    public Depsto getDepsto() {
        return depsto;
    }

    public void setDepsto(Depsto depsto) {
        this.depsto = depsto;
    }

    public BigDecimal getQtecom() {
        return qtecom;
    }

    public void setQtecom(BigDecimal qtecom) {
        this.qtecom = qtecom;
    }




    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DetailMvtStoPR other = (DetailMvtStoPR) obj;
        if (!Objects.equals(this.qtecom, other.qtecom)) {
            return false;
        }
        if (!Objects.equals(this.mvtStoPR, other.mvtStoPR)) {
            return false;
        }
        return true;
    }

}
