/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.io.Serializable;
import java.time.LocalDateTime;
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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "receiving")
@NamedEntityGraph(name = "Receiving.allNodes",
        attributeNodes = {
            @NamedAttributeNode("receivingCommandeList")})
public class Receiving implements Serializable {

    @Column(name = "memo", length = 2147483647)
    private String memo;
    @Size(max = 50)
    @Column(name = "user_memo", length = 50)
    private String userMemo;
    @Column(name = "date_memo")
    private LocalDateTime dateMemo;
    @Column(name = "imprime")
    private Boolean imprime;
    @Size(max = 50)
    @Column(name = "user_imprime", length = 50)
    private String userImprime;
    @Column(name = "date_imprime")
    private LocalDateTime dateImprime;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "code", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "numbon", nullable = false, length = 50)
    private String numbon;

    @Column(name = "numaffiche")
    private String numaffiche;

    @Basic(optional = false)
    @NotNull
    @Column(name = "date_create", nullable = false)
    private LocalDateTime dateCreate;
    @Size(max = 50)
    @Column(name = "user_create", length = 50)
    private String userCreate;
    @Column(name = "date_validate")
    private LocalDateTime dateValidate;
    @Size(max = 50)
    @Column(name = "user_validate", length = 50)
    private String userValidate;
    @Column(name = "date_annule")
    private LocalDateTime dateAnnule;
    @Size(max = 50)
    @Column(name = "user_annule", length = 50)
    private String userAnnule;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "fournisseur", nullable = false, length = 50)
    private String fournisseur;
    @Column(name = "date_validate_receiving")
    private LocalDateTime dateValidateReceiving;
    @Size(max = 50)
    @Column(name = "user_validate_receiving", length = 50)
    private String userValidateReceiving;
    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private CategorieDepotEnum categDepot;

    @Column(name = "code_site")
    Integer codeSite;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiving")
    private List<ReceivingCommande> receivingCommandeList;

//    @OneToOne(mappedBy = "receiving", fetch = FetchType.EAGER, optional = false)
//    private FactureBA factureBA;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiving")
    private List<ReceivingDetails> receivingDetailsList;

    public Receiving() {
    }

    public Receiving(Integer code) {
        this.code = code;
    }

    public Receiving(Integer code, String numbon, LocalDateTime dateCreate, String fournisseur, CategorieDepotEnum type) {
        this.code = code;
        this.numbon = numbon;
        this.dateCreate = dateCreate;
        this.fournisseur = fournisseur;
        this.categDepot = type;
    }

    @PrePersist
    public void prePersist() {
        this.dateCreate = LocalDateTime.now();
        this.userCreate = SecurityContextHolder.getContext().getAuthentication().getName();
        this.numaffiche = this.numbon.substring(2);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public LocalDateTime getDateValidate() {
        return dateValidate;
    }

    public void setDateValidate(LocalDateTime dateValidate) {
        this.dateValidate = dateValidate;
    }

    public String getUserValidate() {
        return userValidate;
    }

    public void setUserValidate(String userValidate) {
        this.userValidate = userValidate;
    }

    public LocalDateTime getDateAnnule() {
        return dateAnnule;
    }

    public void setDateAnnule(LocalDateTime dateAnnule) {
        this.dateAnnule = dateAnnule;
    }

    public String getUserAnnule() {
        return userAnnule;
    }

    public void setUserAnnule(String userAnnule) {
        this.userAnnule = userAnnule;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum type) {
        this.categDepot = type;
    }

    public List<ReceivingCommande> getReceivingCommandeList() {
        return receivingCommandeList;
    }

    public void setReceivingCommandeList(List<ReceivingCommande> receivingCommandeList) {
        this.receivingCommandeList = receivingCommandeList;
    }

//    public FactureBA getFactureBA() {
//        return factureBA;
//    }
//
//    public void setFactureBA(FactureBA factureBA) {
//        this.factureBA = factureBA;
//    }
    public List<ReceivingDetails> getReceivingDetailsList() {
        return receivingDetailsList;
    }

    public void setReceivingDetailsList(List<ReceivingDetails> receivingDetailsList) {
        this.receivingDetailsList = receivingDetailsList;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getUserMemo() {
        return userMemo;
    }

    public void setUserMemo(String userMemo) {
        this.userMemo = userMemo;
    }

    public LocalDateTime getDateMemo() {
        return dateMemo;
    }

    public void setDateMemo(LocalDateTime dateMemo) {
        this.dateMemo = dateMemo;
    }

    public Boolean getImprime() {
        return imprime;
    }

    public void setImprime(Boolean imprime) {
        this.imprime = imprime;
    }

    public String getUserImprime() {
        return userImprime;
    }

    public void setUserImprime(String userImprime) {
        this.userImprime = userImprime;
    }

    public LocalDateTime getDateImprime() {
        return dateImprime;
    }

    public void setDateImprime(LocalDateTime dateImprime) {
        this.dateImprime = dateImprime;
    }

    public LocalDateTime getDateValidateReceiving() {
        return dateValidateReceiving;
    }

    public void setDateValidateReceiving(LocalDateTime dateValidateReceiving) {
        this.dateValidateReceiving = dateValidateReceiving;
    }

    public String getUserValidateReceiving() {
        return userValidateReceiving;
    }

    public void setUserValidateReceiving(String userValidateReceiving) {
        this.userValidateReceiving = userValidateReceiving;
    }

    public Integer getCodeSite() {
        return codeSite;
    }

    public void setCodeSite(Integer codeSite) {
        this.codeSite = codeSite;
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
        if (!(object instanceof Receiving)) {
            return false;
        }
        Receiving other = (Receiving) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.Receiving[ code=" + code + " ]";
    }
}
