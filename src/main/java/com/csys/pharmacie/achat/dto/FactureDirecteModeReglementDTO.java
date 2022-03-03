package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.achat.domain.FactureDirecte;
import com.csys.pharmacie.achat.domain.FactureDirecteModeReglementPK;
import java.lang.Integer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FactureDirecteModeReglementDTO {

    
    private String numBon;

    @NotNull
    private Integer codeReglement;

    private String designationReglement;

    @NotNull
    private Integer codeMotifPaiement;

    private String designationMotifPaiement;

    private Integer delaiPaiement;

    private Integer delaiValeurPaiement;

    private Integer pourcentage;

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public Integer getCodeReglement() {
        return codeReglement;
    }

    public void setCodeReglement(Integer codeReglement) {
        this.codeReglement = codeReglement;
    }

    public String getDesignationReglement() {
        return designationReglement;
    }

    public void setDesignationReglement(String designationReglement) {
        this.designationReglement = designationReglement;
    }

    public Integer getCodeMotifPaiement() {
        return codeMotifPaiement;
    }

    public void setCodeMotifPaiement(Integer codeMotifPaiement) {
        this.codeMotifPaiement = codeMotifPaiement;
    }

    public String getDesignationMotifPaiement() {
        return designationMotifPaiement;
    }

    public void setDesignationMotifPaiement(String designationMotifPaiement) {
        this.designationMotifPaiement = designationMotifPaiement;
    }

    public Integer getDelaiPaiement() {
        return delaiPaiement;
    }

    public void setDelaiPaiement(Integer delaiPaiement) {
        this.delaiPaiement = delaiPaiement;
    }

    public Integer getDelaiValeurPaiement() {
        return delaiValeurPaiement;
    }

    public void setDelaiValeurPaiement(Integer delaiValeurPaiement) {
        this.delaiValeurPaiement = delaiValeurPaiement;
    }

    public Integer getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Integer pourcentage) {
        this.pourcentage = pourcentage;
    }



    
}
