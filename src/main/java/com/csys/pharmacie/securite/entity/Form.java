/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.securite.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "Form")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Form.findAll", query = "SELECT f FROM Form f"),
    @NamedQuery(name = "Form.findByModule", query = "SELECT f FROM Form f WHERE f.formPK.module = :module"),
    @NamedQuery(name = "Form.findByForm", query = "SELECT f FROM Form f WHERE f.formPK.form = :form"),
    @NamedQuery(name = "Form.findByNomForm", query = "SELECT f FROM Form f WHERE f.nomForm = :nomForm"),
    @NamedQuery(name = "Form.findByControl", query = "SELECT f FROM Form f WHERE f.formPK.control = :control"),
    @NamedQuery(name = "Form.findByNomControl", query = "SELECT f FROM Form f WHERE f.nomControl = :nomControl"),
    @NamedQuery(name = "Form.findByCodeMenu", query = "SELECT f FROM Form f WHERE f.codeMenu = :codeMenu")})
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FormPK formPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NomForm")
    private String nomForm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NomControl")
    private String nomControl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "code_menu")
    private String codeMenu;

    public Form() {
    }

    public Form(FormPK formPK) {
        this.formPK = formPK;
    }

    public Form(FormPK formPK, String nomForm, String nomControl, String codeMenu) {
        this.formPK = formPK;
        this.nomForm = nomForm;
        this.nomControl = nomControl;
        this.codeMenu = codeMenu;
    }

    public Form(String module, String form, String control) {
        this.formPK = new FormPK(module, form, control);
    }

    public FormPK getFormPK() {
        return formPK;
    }

    public void setFormPK(FormPK formPK) {
        this.formPK = formPK;
    }

    public String getNomForm() {
        return nomForm;
    }

    public void setNomForm(String nomForm) {
        this.nomForm = nomForm;
    }

    public String getNomControl() {
        return nomControl;
    }

    public void setNomControl(String nomControl) {
        this.nomControl = nomControl;
    }

    public String getCodeMenu() {
        return codeMenu;
    }

    public void setCodeMenu(String codeMenu) {
        this.codeMenu = codeMenu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (formPK != null ? formPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Form)) {
            return false;
        }
        Form other = (Form) object;
        if ((this.formPK == null && other.formPK != null) || (this.formPK != null && !this.formPK.equals(other.formPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.securite.entity.Form[ formPK=" + formPK + " ]";
    }
    
}
