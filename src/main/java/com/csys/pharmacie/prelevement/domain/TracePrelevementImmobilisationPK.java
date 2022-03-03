/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
public class TracePrelevementImmobilisationPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "code_mvtstopr")
    private Integer codeMvtstoPR;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_immobilisation")
    private Integer codeImmobilisation;

    public TracePrelevementImmobilisationPK() {
    }

    public TracePrelevementImmobilisationPK(Integer codeMvtstoPR, Integer codeImmobilisation) {
        this.codeMvtstoPR = codeMvtstoPR;
        this.codeImmobilisation = codeImmobilisation;
    }

    public Integer getCodeMvtstoPR() {
        return codeMvtstoPR;
    }

    public void setCodeMvtstoPR(Integer codeMvtstoPR) {
        this.codeMvtstoPR = codeMvtstoPR;
    }

    public Integer getCodeImmobilisation() {
        return codeImmobilisation;
    }

    public void setCodeImmobilisation(Integer codeImmobilisation) {
        this.codeImmobilisation = codeImmobilisation;
    }

    
    
    
}
