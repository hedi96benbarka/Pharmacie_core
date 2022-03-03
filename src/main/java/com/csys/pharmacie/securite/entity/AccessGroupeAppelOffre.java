/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.securite.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ERRAYHAN
 */
@Entity
@Table(name = "Access_Groupe_Appel_Offre")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccessGroupeAppelOffre.findAll", query = "SELECT a FROM AccessGroupeAppelOffre a"),
    @NamedQuery(name = "AccessGroupeAppelOffre.findById", query = "SELECT a FROM AccessGroupeAppelOffre a WHERE a.id = :id"),
    @NamedQuery(name = "AccessGroupeAppelOffre.findgroupeByUsername", query = "SELECT a FROM AccessGroupeAppelOffre a WHERE a.username = :username"),
    @NamedQuery(name = "AccessGroupeAppelOffre.findByUsername", query = "SELECT a.groupe FROM AccessGroupeAppelOffre a WHERE a.username = :username and a.type = :type"),
    @NamedQuery(name = "AccessGroupeAppelOffre.findByGroupe", query = "SELECT a FROM AccessGroupeAppelOffre a WHERE a.groupe = :groupe"),
    @NamedQuery(name = "AccessGroupeAppelOffre.findByType", query = "SELECT a FROM AccessGroupeAppelOffre a WHERE a.type = :type"),
    @NamedQuery(name = "AccessGroupeAppelOffre.findByUserCreate", query = "SELECT a FROM AccessGroupeAppelOffre a WHERE a.userCreate = :userCreate"),
    @NamedQuery(name = "AccessGroupeAppelOffre.findByDatecre", query = "SELECT a FROM AccessGroupeAppelOffre a WHERE a.datecre = :datecre")})
public class AccessGroupeAppelOffre implements Serializable {

    @Column(name = "Date_cre")
    @Temporal(TemporalType.DATE)
    private Date datecre;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Username")
    private String username;
//     @JoinColumn(name = "Groupe", referencedColumnName = "codegroup")
//    @ManyToOne(optional = false)
//    private GroupeApp groupe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Groupe")
    private long groupe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Type")
    private int type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "User_Create")
    private String userCreate;

    public AccessGroupeAppelOffre() {
    }

    public AccessGroupeAppelOffre(Integer id) {
        this.id = id;
    }

    public long getGroupe() {
        return groupe;
    }

    public void setGroupe(long groupe) {
        this.groupe = groupe;
    }

    public AccessGroupeAppelOffre(Integer id, String username, long groupe, int type, String userCreate) {
        this.id = id;
        this.username = username;
        this.type = type;
        this.userCreate = userCreate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Date getDatecre() {
        return datecre;
    }

    public void setDatecre(Date datecre) {
        this.datecre = datecre;
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
        if (!(object instanceof AccessGroupeAppelOffre)) {
            return false;
        }
        AccessGroupeAppelOffre other = (AccessGroupeAppelOffre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.achat.model.AccessGroupeAppelOffre[ id=" + id + " ]";
    }

//    public Date getDatecre() {
//        return datecre;
//    }
//
//    public void setDatecre(Date datecre) {
//        this.datecre = datecre;
//    }

}
