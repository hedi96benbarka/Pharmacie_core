/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.domain;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "inventaire")
public class Inventaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codeSaisie")
    private String codeSaisie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "depot")
    private Integer depot;
    @Basic(optional = false)
    @NotNull
    @Column(name = "categorie_depot")
    @Enumerated(EnumType.STRING)
    private CategorieDepotEnum categorieDepot;
    @Column(name = "dateOuverture")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOuverture;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "userOuverture")
    private String userOuverture;
    @Column(name = "dateCloture")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCloture;
    @Size(max = 20)
    @Column(name = "userCloture")
    private String userCloture;
    @NotNull
    @Column(name = "categorie_article_Parent")
    private Integer categorieArticleParent;
    @Column(name = "demarrage")
    private Boolean isDemarrage;
    @Size(max = 50)
    @Column(name = "user_annule")
    private String userAnnule;
    @Column(name = "date_annule")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAnnule;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventaire1")
    private List<DetailInventaire> detailInventaireCollection;

    @OneToMany(mappedBy = "inventaire")
    private List<DepStoHist> depstoHist;

    public List<DepStoHist> getDepstoHist() {
        return depstoHist;
    }

    public void setDepstoHist(List<DepStoHist> depstoHist) {
        this.depstoHist = depstoHist;
    }

    @PrePersist
    public void prePersist() {

        this.dateOuverture = new Date();
        this.userOuverture = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Inventaire() {
    }

    public Inventaire(Integer code) {
        this.code = code;
    }

    public String getCodeSaisie() {
        return codeSaisie;
    }

    public void setCodeSaisie(String codeSaisie) {
        this.codeSaisie = codeSaisie;
    }

    public Integer getCategorieArticleParent() {
        return categorieArticleParent;
    }

    public void setCategorieArticleParent(Integer categorieArticleParent) {
        this.categorieArticleParent = categorieArticleParent;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getDepot() {
        return depot;
    }

    public void setDepot(Integer depot) {
        this.depot = depot;
    }

    public CategorieDepotEnum getCategorieDepot() {
        return categorieDepot;
    }

    public void setCategorieDepot(CategorieDepotEnum categorieDepot) {
        this.categorieDepot = categorieDepot;
    }

    public Date getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Date dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public String getUserOuverture() {
        return userOuverture;
    }

    public void setUserOuverture(String userOuverture) {
        this.userOuverture = userOuverture;
    }

    public Date getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(Date dateCloture) {
        this.dateCloture = dateCloture;
    }

    public String getUserCloture() {
        return userCloture;
    }

    public void setUserCloture(String userCloture) {
        this.userCloture = userCloture;
    }

    public List<DetailInventaire> getDetailInventaireCollection() {
        return detailInventaireCollection;
    }

    public void setDetailInventaireCollection(List<DetailInventaire> detailInventaireCollection) {
        this.detailInventaireCollection = detailInventaireCollection;
    }

    public Boolean getIsDemarrage() {
        return isDemarrage;
    }

    public void setIsDemarrage(Boolean isDemarrage) {
        this.isDemarrage = isDemarrage;
    }

    public String getUserAnnule() {
        return userAnnule;
    }

    public void setUserAnnule(String userAnnule) {
        this.userAnnule = userAnnule;
    }

    public Date getDateAnnule() {
        return dateAnnule;
    }

    public void setDateAnnule(Date dateAnnule) {
        this.dateAnnule = dateAnnule;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventaire)) {
            return false;
        }
        Inventaire other = (Inventaire) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Inventaire{" + "code=" + code + ", codeSaisie=" + codeSaisie + '}';
    }

}
