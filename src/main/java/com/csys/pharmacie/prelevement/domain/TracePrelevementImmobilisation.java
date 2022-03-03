/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "trace_prelevement_immobilisation")

public class TracePrelevementImmobilisation implements Serializable {

    @EmbeddedId
    protected TracePrelevementImmobilisationPK tracePrelevementImmobilisationPK;

    public TracePrelevementImmobilisation(TracePrelevementImmobilisationPK tracePrelevementImmobilisationPK) {
        this.tracePrelevementImmobilisationPK = tracePrelevementImmobilisationPK;
    }

    public TracePrelevementImmobilisation() {
    }

}
