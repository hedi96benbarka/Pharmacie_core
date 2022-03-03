package com.csys.pharmacie.securite.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class UserAction implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
@Id
    private String action;
    private String designation_action;
    private Boolean droit;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDesignation_action() {
        return designation_action;
    }

    public void setDesignation_action(String designation_action) {
        this.designation_action = designation_action;
    }

   

    public Boolean getDroit() {
        return droit;
    }

    public void setDroit(Boolean droit) {
        this.droit = droit;
    }

}
