/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.csys.pharmacie.helper.MvtSto;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Farouk
 */
@Entity
@Audited
@Table(name = "MvtStoBT")
@AuditTable("MvtStoBT_AUD")
@NamedEntityGraph(name = "MvtStoBT.allNodes",
                  attributeNodes = {
                      @NamedAttributeNode("factureBT")
                          ,@NamedAttributeNode("detailMvtStoBTList")
                  })
public class MvtStoBT extends MvtSto implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mvtStoBT")
    private List<DetailMvtStoBT> detailMvtStoBTList;
    @Size(min = 1, max = 6)
    @Column(name = "numordre")
    private String numordre;

    @Column(name = "qteben", precision = 18, scale = 3)
    private BigDecimal qteben;

    @Column(name = "codtva")
    private Integer codeTvaAchat;

    @Column(name = "tautva")
    private BigDecimal tauxTvaAchat;

    @Column(name = "quantite_recue")
    private BigDecimal quantiteRecue;

    @Column(name = "quantite_defectueuse")
    private BigDecimal quantiteDefectueuse;

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    @JoinColumn(name = "numbon", referencedColumnName = "numbon")
    @ManyToOne(optional = false)
    private FactureBT factureBT;

    public FactureBT getFactureBT() {
        return factureBT;
    }

    public void setFactureBT(FactureBT factureBT) {
        this.factureBT = factureBT;
    }

    public MvtStoBT() {
    }

    public MvtStoBT(List<DetailMvtStoBT> detailMvtStoBTList, FactureBT factureBT, BigDecimal quantite, Integer codart, String lotinter, LocalDate datPer, Integer unite) {
        super(quantite, codart, lotinter, datPer, unite);
        this.detailMvtStoBTList = detailMvtStoBTList;
        this.factureBT = factureBT;
    }



    public List<DetailMvtStoBT> getDetailMvtStoBTList() {
        return detailMvtStoBTList;
    }

    public void setDetailMvtStoBTList(List<DetailMvtStoBT> detailMvtStoBTList) {
        this.detailMvtStoBTList = detailMvtStoBTList;
    }

    public BigDecimal getQteben() {
        return qteben;
    }

    public void setQteben(BigDecimal qteben) {
        this.qteben = qteben;
    }

    public Integer getCodeTvaAchat() {
        return codeTvaAchat;
    }

    public void setCodeTvaAchat(Integer codeTvaAchat) {
        this.codeTvaAchat = codeTvaAchat;
    }

    public BigDecimal getTauxTvaAchat() {
        return tauxTvaAchat;
    }

    public void setTauxTvaAchat(BigDecimal tauxTvaAchat) {
        this.tauxTvaAchat = tauxTvaAchat;
    }

    public BigDecimal getQuantiteRecue() {
        return quantiteRecue;
    }

    public void setQuantiteRecue(BigDecimal quantiteRecue) {
        this.quantiteRecue = quantiteRecue;
    }

    public BigDecimal getQuantiteDefectueuse() {
        return quantiteDefectueuse;
    }

    public void setQuantiteDefectueuse(BigDecimal quantiteDefectueuse) {
        this.quantiteDefectueuse = quantiteDefectueuse;
    }

}
