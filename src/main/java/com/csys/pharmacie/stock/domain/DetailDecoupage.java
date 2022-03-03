/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import com.csys.pharmacie.helper.BaseDetailBon;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "detail_decoupage")
@NamedQueries({
    @NamedQuery(name = "DetailDecoupage.findAll", query = "SELECT d FROM DetailDecoupage d")})
public class DetailDecoupage extends BaseDetailBon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;

    @Basic(optional = false)
    @NotNull
    @Column(name = "codart")
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "code_decoupage", insertable = false, updatable = false)
    private String codeDecoupage;

    @Basic(optional = false)
    @NotNull
    @Column(name = "unite_origine")
    private Integer uniteOrigine;

    @Basic(optional = false)
    @NotNull
    @Column(name = "unite_final")
    private Integer uniteFinal;
    @Basic(optional = false)
    @Column(name = "date_peremption")
    private LocalDate datePeremption;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "lot_inter")
    private String lotInter;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_obtenue")
    private BigDecimal quantiteObtenue;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detailDecoupage")
    private List<DepstoDetailDecoupage> depstoDetailDecoupageList;

    @JoinColumn(name = "code_decoupage", referencedColumnName = "numbon")
    @ManyToOne(optional = false)
    private Decoupage decoupage;

    public DetailDecoupage() {
    }

    public Integer getUniteFinal() {
        return uniteFinal;
    }

    public void setUniteFinal(Integer uniteFinal) {
        this.uniteFinal = uniteFinal;
    }

    public BigDecimal getQuantiteObtenue() {
        return quantiteObtenue;
    }

    public void setQuantiteObtenue(BigDecimal quantiteObtenue) {
        this.quantiteObtenue = quantiteObtenue;
    }

    public List<DepstoDetailDecoupage> getDepstoDetailDecoupageList() {
        return depstoDetailDecoupageList;
    }

    public void setDepstoDetailDecoupageList(List<DepstoDetailDecoupage> depstoDetailDecoupageList) {
        this.depstoDetailDecoupageList = depstoDetailDecoupageList;
    }

    public Decoupage getDecoupage() {
        return decoupage;
    }

    public void setDecoupage(Decoupage decoupage) {
        this.decoupage = decoupage;
    }

    public LocalDate getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(LocalDate datePeremption) {
        this.datePeremption = datePeremption;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getCodeDecoupage() {
        return codeDecoupage;
    }

    public void setCodeDecoupage(String codeDecoupage) {
        this.codeDecoupage = codeDecoupage;
    }

    public Integer getUniteOrigine() {
        return uniteOrigine;
    }

    public void setUniteOrigine(Integer uniteOrigine) {
        this.uniteOrigine = uniteOrigine;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.code);
        return hash;
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
        final DetailDecoupage other = (DetailDecoupage) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "DetailDecoupage{" + "code=" + code + ", codart=" + codart + ", codeDecoupage=" + codeDecoupage + ", uniteOrigine=" + uniteOrigine + ", uniteFinal=" + uniteFinal + ", datePeremption=" + datePeremption + ", lotInter=" + lotInter + ", quantiteObtenue=" + quantiteObtenue + ", depstoDetailDecoupageList=" + depstoDetailDecoupageList +  '}';
//    }
    @Override
    public String toString() {
        return "DetailDecoupage{" + "codart=" + codart + ", uniteOrigine=" + uniteOrigine + ", uniteFinal=" + uniteFinal + ", quantiteObtenue=" + quantiteObtenue + ", quantite=" + super.getQuantite() + '}';
    }

}
