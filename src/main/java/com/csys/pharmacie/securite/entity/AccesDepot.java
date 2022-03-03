/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.securite.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ERRAYHAN
 */
@Entity
@Table(name = "acces_depot")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccesDepot.findAll", query = "SELECT a FROM AccesDepot a"),
    @NamedQuery(name = "AccesDepot.findById", query = "SELECT a FROM AccesDepot a WHERE a.id = :id"),
    @NamedQuery(name = "AccesDepot.findByUserName", query = "SELECT a FROM AccesDepot a WHERE a.userName = :userName"),
    @NamedQuery(name = "AccesDepot.findByDescription", query = "SELECT a FROM AccesDepot a WHERE a.description = :description"),
    @NamedQuery(name = "AccesDepot.findByCodeMedecinInfirmier", query = "SELECT a FROM AccesDepot a WHERE a.codeMedecinInfirmier = :codeMedecinInfirmier"),
    @NamedQuery(name = "AccesDepot.findByEtage", query = "SELECT a FROM AccesDepot a WHERE a.etage = :etage"),
    @NamedQuery(name = "AccesDepot.findByDesdep", query = "SELECT a FROM AccesDepot a WHERE a.desdep = :desdep")})
public class AccesDepot implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 12)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "UserName")
    private String userName;
    @Size(max = 60)
    @Column(name = "Description")
    private String description;
    @Size(max = 10)
    @Column(name = "Code_Medecin_Infirmier")
    private String codeMedecinInfirmier;
    @Size(max = 2)
    @Column(name = "Etage")
    private String etage;
    @Size(max = 40)
    @Column(name = "desdep")
    private String desdep;

    public AccesDepot() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeMedecinInfirmier() {
        return codeMedecinInfirmier;
    }

    public void setCodeMedecinInfirmier(String codeMedecinInfirmier) {
        this.codeMedecinInfirmier = codeMedecinInfirmier;
    }

    public String getEtage() {
        return etage;
    }

    public void setEtage(String etage) {
        this.etage = etage;
    }

    public String getDesdep() {
        return desdep;
    }

    public void setDesdep(String desdep) {
        this.desdep = desdep;
    }
    
}
