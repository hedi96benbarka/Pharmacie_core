/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.MvtSto;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "detail_facture_directe")
public class DetailFactureDirecte extends MvtSto implements Serializable {

//    private static final long serialVersionUID = 1L;
//    @Id
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "code")
//    private Long code;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "codart")
//    private long codart;
//    @Size(max = 50)
//    @Column(name = "lot_inter")
//    private String lotInter;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "priuni")
//    private BigDecimal priuni;
//    @Column(name = "DatPer")
//    @Temporal(TemporalType.DATE)
//    private Date datPer;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "code_unite")
//    private long codeUnite;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 10)
//    @Column(name = "categ_depot")
//    private String categDepot;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 50)
//    @Column(name = "code_saisi")
//    private String codeSaisi;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 255)
//    @Column(name = "desart")
//    private String desart;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 255)
//    @Column(name = "desart_sec")
//    private String desartSec;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "quantite")
//    private BigDecimal quantite;
    @Column(name = "codtva")
    private Integer codtva;
    @Column(name = "tautva")
    private BigDecimal tautva;
    @JoinColumn(name = "numbon", referencedColumnName = "numbon")
    @ManyToOne(optional = false)
    @NotNull
    private FactureDirecte factureDirecte;

    public DetailFactureDirecte() {
    }

//    public DetailFactureDirecte(Long code) {
//        this.code = code;
//    }
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

    public FactureDirecte getFactureDirecte() {
        return factureDirecte;
    }

    public void setFactureDirecte(FactureDirecte factureDirecte) {
        this.factureDirecte = factureDirecte;
    }

//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (code != null ? code.hashCode() : 0);
//        return hash;
//    }
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof DetailFactureDirecte)) {
//            return false;
//        }
//        DetailFactureDirecte other = (DetailFactureDirecte) object;
//        if ((this.code() == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
//            return false;
//        }
//        return true;
//    }
    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.DetailFactureDirecte[ code=" + getCode() + " ]";
    }

}
