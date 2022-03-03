/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "table_injection_redressement")
@NamedQueries({
    @NamedQuery(name = "TableInjectionRedressement.findAll", query = "SELECT t FROM TableInjectionRedressement t")})
public class TableInjectionRedressement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "code")
    private Integer code;
    @Size(max = 50)
    @Column(name = "code_saisi", length = 50)
    private String codeSaisi;
    @Enumerated(EnumType.STRING)
    @Column(name = "categDepot", length = 10)
    private CategorieDepotEnum categDepot;
    @Column(name = "codeUnite")
    private Integer codeUnite;
    @Size(max = 50)
    @Column(name = "designation_unite", length = 50)
    private String designationUnite;
    @Column(name = "datPer")
    private LocalDate datPer;
    @Size(max = 255)
    @Column(name = "desArtSec", length = 255)
    private String desArtSec;
    @Size(max = 255)
    @Column(name = "desart", length = 255)
    private String desart;
    @Size(max = 255)
    @Column(name = "lotInter", length = 255)
    private String lotInter;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "priuni", precision = 18, scale = 3)
    private BigDecimal priuni;
    @Column(name = "quantite", precision = 18, scale = 3)
    private BigDecimal quantite;
    @Column(name = "codtva")
    private Integer codtva;
    @Column(name = "tautva", precision = 18, scale = 3)
    private BigDecimal tautva;
    @Column(name = "prix_vente", precision = 18, scale = 3)
    private BigDecimal prixVente;
    @Column(name = "prix_achat", precision = 18, scale = 3)
    private BigDecimal prixAchat;
    @Column(name = "prix_reference", precision = 18, scale = 3)
    private BigDecimal prixReference;
    @Column(name = "prix_pmp", precision = 18, scale = 3)
    private BigDecimal prixPmp;
    @Column(name = "max_prix_achat", precision = 18, scale = 3)
    private BigDecimal maxPrixAchat;
    @Size(max = 255)
    @Column(name = "designation_depot", length = 255)
    private String designationDepot;
    @Column(name = "coddep")
    private Integer coddep;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;

    public TableInjectionRedressement() {
    }

    public TableInjectionRedressement(Integer id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public String getDesArtSec() {
        return desArtSec;
    }

    public void setDesArtSec(String desArtSec) {
        this.desArtSec = desArtSec;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
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

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public BigDecimal getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(BigDecimal prixAchat) {
        this.prixAchat = prixAchat;
    }

    public BigDecimal getPrixReference() {
        return prixReference;
    }

    public void setPrixReference(BigDecimal prixReference) {
        this.prixReference = prixReference;
    }

    public BigDecimal getPrixPmp() {
        return prixPmp;
    }

    public void setPrixPmp(BigDecimal prixPmp) {
        this.prixPmp = prixPmp;
    }

    public BigDecimal getMaxPrixAchat() {
        return maxPrixAchat;
    }

    public void setMaxPrixAchat(BigDecimal maxPrixAchat) {
        this.maxPrixAchat = maxPrixAchat;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TableInjectionRedressement)) {
            return false;
        }
        TableInjectionRedressement other = (TableInjectionRedressement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.transfert.domain.TableInjectionRedressement[ id=" + id + " ]";
    }

}
