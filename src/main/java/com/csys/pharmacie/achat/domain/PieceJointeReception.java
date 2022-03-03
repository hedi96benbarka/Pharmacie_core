/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "piece_jointe_reception")
@NamedQueries({
    @NamedQuery(name = "PieceJointeReception.findAll", query = "SELECT p FROM PieceJointeReception p")})
public class PieceJointeReception implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_document")
    private String codeDocument;

    @NotNull
    @JoinColumn(referencedColumnName = "numbon", name = "numbon")
    @ManyToOne
    private FactureBA reception;

    public PieceJointeReception() {
    }

    public PieceJointeReception(String codeDocument, FactureBA reception) {
        this.codeDocument = codeDocument;
        this.reception = reception;
    }

    public PieceJointeReception(String code) {
        this.codeDocument = code;
    }

    public String getCodeDocument() {
        return codeDocument;
    }

    public void setCodeDocument(String codeDocument) {
        this.codeDocument = codeDocument;
    }

    public FactureBA getReception() {
        return reception;
    }

    public void setReception(FactureBA reception) {
        this.reception = reception;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeDocument != null ? codeDocument.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PieceJointeReception)) {
            return false;
        }
        PieceJointeReception other = (PieceJointeReception) object;
        if ((this.codeDocument == null && other.codeDocument != null) || (this.codeDocument != null && !this.codeDocument.equalsIgnoreCase(other.codeDocument))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PieceJointeReception{" + "codeDocument=" + codeDocument + ", reception=" + reception + '}';
    }

    

}
