package com.csys.pharmacie.stock.dto;

import java.math.BigDecimal;

public class MouvementStockEditionDTO {

    private Integer coddep;
    private String designationDepot;
    private String codeSaisiDepot;

    private Integer codeArticle;
    private String codeSaisiArticle;
    private String designationArticle;
    private String designationCategorieArticle;

    private Integer codeUnite;
    private String designationUnite;

    private String typbon;
    private BigDecimal valeur;
    private BigDecimal quantite;

    private BigDecimal stkDep = BigDecimal.ZERO;
    private BigDecimal recep = BigDecimal.ZERO;
    private BigDecimal receptionAnnulee = BigDecimal.ZERO;
    private BigDecimal retourFournisseur = BigDecimal.ZERO;
    private BigDecimal ajustementRetourFournisseurE = BigDecimal.ZERO;
    private BigDecimal ajustementRetourFournisseurS = BigDecimal.ZERO;
    private BigDecimal avoirFournisseur = BigDecimal.ZERO;
    private BigDecimal ajustementAvoirFournisseurE = BigDecimal.ZERO;
    private BigDecimal ajustementAvoirFournisseurS = BigDecimal.ZERO;
    private BigDecimal transent = BigDecimal.ZERO;
    private BigDecimal transf = BigDecimal.ZERO;
    private BigDecimal transfertEA = BigDecimal.ZERO;
    private BigDecimal transfertSA = BigDecimal.ZERO;
    private BigDecimal retourPerime = BigDecimal.ZERO;
    private BigDecimal ajustementRetourPerimeE = BigDecimal.ZERO;
    private BigDecimal ajustementRetourPerimeS = BigDecimal.ZERO;
    private BigDecimal redressE = BigDecimal.ZERO;
    private BigDecimal redressS = BigDecimal.ZERO;
    private BigDecimal bonPrelevement = BigDecimal.ZERO;
    private BigDecimal bonPrelevement1 = BigDecimal.ZERO;
    private BigDecimal bonPrelevementAnnulee = BigDecimal.ZERO;
    private BigDecimal bonRetourPrelevement = BigDecimal.ZERO;
    private BigDecimal decoupE = BigDecimal.ZERO;
    private BigDecimal decoupS = BigDecimal.ZERO;
    private BigDecimal quittance = BigDecimal.ZERO;
    private BigDecimal quittanceAnnulee = BigDecimal.ZERO;
    private BigDecimal avoir = BigDecimal.ZERO;
    private BigDecimal invenE = BigDecimal.ZERO;
    private BigDecimal invenS = BigDecimal.ZERO;
    private BigDecimal transfertCB = BigDecimal.ZERO;
    private BigDecimal transfertBC = BigDecimal.ZERO;

    private BigDecimal quantiteStkDep = BigDecimal.ZERO;
    private BigDecimal quantiteRecep = BigDecimal.ZERO;
    private BigDecimal quantiteReceptionAnnulee = BigDecimal.ZERO;
    private BigDecimal quantiteRetourFournisseur = BigDecimal.ZERO;
    private BigDecimal quantiteAjustementRetourFournisseurE = BigDecimal.ZERO;
    private BigDecimal quantiteAjustementRetourFournisseurS = BigDecimal.ZERO;
    private BigDecimal quantiteAvoirFournisseur = BigDecimal.ZERO;
    private BigDecimal quantiteAjustementAvoirFournisseurE = BigDecimal.ZERO;
    private BigDecimal quantiteAjustementAvoirFournisseurS = BigDecimal.ZERO;
    private BigDecimal quantiteTransent = BigDecimal.ZERO;
    private BigDecimal quantiteTransf = BigDecimal.ZERO;
    private BigDecimal quantiteTransfertEA = BigDecimal.ZERO;
    private BigDecimal quantiteTransfertSA = BigDecimal.ZERO;
    private BigDecimal quantiteRetourPerime = BigDecimal.ZERO;
    private BigDecimal quantiteAjustementRetourPerimeE = BigDecimal.ZERO;
    private BigDecimal quantiteAjustementRetourPerimeS = BigDecimal.ZERO;
    private BigDecimal quantiteRedressE = BigDecimal.ZERO;
    private BigDecimal quantiteRedressS = BigDecimal.ZERO;
    private BigDecimal quantiteBonPrelevement = BigDecimal.ZERO;
    private BigDecimal quantiteBonPrelevement1 = BigDecimal.ZERO;
    private BigDecimal quantiteBonPrelevementAnnulee = BigDecimal.ZERO;
    private BigDecimal quantiteBonRetourPrelevement = BigDecimal.ZERO;
    private BigDecimal quantiteDecoupE = BigDecimal.ZERO;
    private BigDecimal quantiteDecoupS = BigDecimal.ZERO;
    private BigDecimal quantiteQuittance = BigDecimal.ZERO;
    private BigDecimal quantiteQuittanceAnnulee = BigDecimal.ZERO;
    private BigDecimal quantiteAvoir = BigDecimal.ZERO;
    private BigDecimal quantiteInvenE = BigDecimal.ZERO;
    private BigDecimal quantiteInvenS = BigDecimal.ZERO;
    private BigDecimal quantiteTransfertCB = BigDecimal.ZERO;
    private BigDecimal quantiteTransfertBC = BigDecimal.ZERO;

    public MouvementStockEditionDTO() {

    }

    public MouvementStockEditionDTO(Integer coddep, String typbon, BigDecimal valeur) {
        this.coddep = coddep;
        this.typbon = typbon;
        this.valeur = valeur;
    }

    public MouvementStockEditionDTO(Integer coddep, Integer codeArticle, Integer codeUnite, String typbon, BigDecimal quantite, BigDecimal valeur) {
        this.coddep = coddep;
        this.codeArticle = codeArticle;
        this.codeUnite = codeUnite;
        this.typbon = typbon;
        this.quantite = quantite;
        this.valeur = valeur;
    }

    public MouvementStockEditionDTO(Integer coddep, Integer codeArticle, String codeSaisiArticle, String designationArticle, Integer codeUnite, String typbon, BigDecimal quantite, BigDecimal valeur) {
        this.coddep = coddep;
        this.codeArticle = codeArticle;
        this.codeSaisiArticle = codeSaisiArticle;
        this.designationArticle = designationArticle;
        this.codeUnite = codeUnite;
        this.typbon = typbon;
        this.quantite = quantite;
        this.valeur = valeur;
    }

    public MouvementStockEditionDTO(Integer coddep, BigDecimal valeur) {
        this.coddep = coddep;
        this.valeur = valeur;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public String getCodeSaisiDepot() {
        return codeSaisiDepot;
    }

    public void setCodeSaisiDepot(String codeSaisiDepot) {
        this.codeSaisiDepot = codeSaisiDepot;
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getCodeSaisiArticle() {
        return codeSaisiArticle;
    }

    public void setCodeSaisiArticle(String codeSaisiArticle) {
        this.codeSaisiArticle = codeSaisiArticle;
    }

    public String getDesignationArticle() {
        return designationArticle;
    }

    public void setDesignationArticle(String designationArticle) {
        this.designationArticle = designationArticle;
    }

    public String getDesignationCategorieArticle() {
        return designationCategorieArticle;
    }

    public void setDesignationCategorieArticle(String designationCategorieArticle) {
        this.designationCategorieArticle = designationCategorieArticle;
    }

    public BigDecimal getStkDep() {
        return stkDep;
    }

    public void setStkDep(BigDecimal stkDep) {
        this.stkDep = stkDep;
    }

    public BigDecimal getTransf() {
        return transf;
    }

    public void setTransf(BigDecimal transf) {
        this.transf = transf;
    }

    public BigDecimal getRedressS() {
        return redressS;
    }

    public void setRedressS(BigDecimal redressS) {
        this.redressS = redressS;
    }

    public BigDecimal getRecep() {
        return recep;
    }

    public void setRecep(BigDecimal recep) {
        this.recep = recep;
    }

    public BigDecimal getAvoir() {
        return avoir;
    }

    public void setAvoir(BigDecimal avoir) {
        this.avoir = avoir;
    }

    public BigDecimal getRedressE() {
        return redressE;
    }

    public void setRedressE(BigDecimal redressE) {
        this.redressE = redressE;
    }

    public BigDecimal getDecoupS() {
        return decoupS;
    }

    public void setDecoupS(BigDecimal decoupS) {
        this.decoupS = decoupS;
    }

    public BigDecimal getDecoupE() {
        return decoupE;
    }

    public void setDecoupE(BigDecimal decoupE) {
        this.decoupE = decoupE;
    }

    public BigDecimal getTransent() {
        return transent;
    }

    public void setTransent(BigDecimal transent) {
        this.transent = transent;
    }

    public BigDecimal getRetourPerime() {
        return retourPerime;
    }

    public void setRetourPerime(BigDecimal retourPerime) {
        this.retourPerime = retourPerime;
    }

    public BigDecimal getBonPrelevement() {
        return bonPrelevement;
    }

    public void setBonPrelevement(BigDecimal bonPrelevement) {
        this.bonPrelevement = bonPrelevement;
    }

    public BigDecimal getInvenE() {
        return invenE;
    }

    public void setInvenE(BigDecimal invenE) {
        this.invenE = invenE;
    }

    public BigDecimal getInvenS() {
        return invenS;
    }

    public void setInvenS(BigDecimal invenS) {
        this.invenS = invenS;
    }

    public BigDecimal getBonRetourPrelevement() {
        return bonRetourPrelevement;
    }

    public void setBonRetourPrelevement(BigDecimal bonRetourPrelevement) {
        this.bonRetourPrelevement = bonRetourPrelevement;
    }

    public BigDecimal getAjustementRetourFournisseurE() {
        return ajustementRetourFournisseurE;
    }

    public void setAjustementRetourFournisseurE(BigDecimal ajustementRetourFournisseurE) {
        this.ajustementRetourFournisseurE = ajustementRetourFournisseurE;
    }

    public BigDecimal getAjustementRetourPerimeE() {
        return ajustementRetourPerimeE;
    }

    public void setAjustementRetourPerimeE(BigDecimal ajustementRetourPerimeE) {
        this.ajustementRetourPerimeE = ajustementRetourPerimeE;
    }

    public BigDecimal getAjustementRetourFournisseurS() {
        return ajustementRetourFournisseurS;
    }

    public void setAjustementRetourFournisseurS(BigDecimal ajustementRetourFournisseurS) {
        this.ajustementRetourFournisseurS = ajustementRetourFournisseurS;
    }

    public BigDecimal getAjustementRetourPerimeS() {
        return ajustementRetourPerimeS;
    }

    public void setAjustementRetourPerimeS(BigDecimal ajustementRetourPerimeS) {
        this.ajustementRetourPerimeS = ajustementRetourPerimeS;
    }

    public String getTypbon() {
        return typbon;
    }

    public void setTypbon(String typbon) {
        this.typbon = typbon;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public BigDecimal getReceptionAnnulee() {
        return receptionAnnulee;
    }

    public void setReceptionAnnulee(BigDecimal receptionAnnulee) {
        this.receptionAnnulee = receptionAnnulee;
    }

    public BigDecimal getRetourFournisseur() {
        return retourFournisseur;
    }

    public void setRetourFournisseur(BigDecimal retourFournisseur) {
        this.retourFournisseur = retourFournisseur;
    }

    public BigDecimal getBonPrelevementAnnulee() {
        return bonPrelevementAnnulee;
    }

    public void setBonPrelevementAnnulee(BigDecimal bonPrelevementAnnulee) {
        this.bonPrelevementAnnulee = bonPrelevementAnnulee;
    }

    public BigDecimal getBonPrelevement1() {
        return bonPrelevement1;
    }

    public void setBonPrelevement1(BigDecimal bonPrelevement1) {
        this.bonPrelevement1 = bonPrelevement1;
    }

    public BigDecimal getQuittance() {
        return quittance;
    }

    public void setQuittance(BigDecimal quittance) {
        this.quittance = quittance;
    }

    public BigDecimal getQuittanceAnnulee() {
        return quittanceAnnulee;
    }

    public void setQuittanceAnnulee(BigDecimal quittanceAnnulee) {
        this.quittanceAnnulee = quittanceAnnulee;
    }

    public BigDecimal getAvoirFournisseur() {
        return avoirFournisseur;
    }

    public void setAvoirFournisseur(BigDecimal avoirFournisseur) {
        this.avoirFournisseur = avoirFournisseur;
    }

    public BigDecimal getAjustementAvoirFournisseurE() {
        return ajustementAvoirFournisseurE;
    }

    public void setAjustementAvoirFournisseurE(BigDecimal ajustementAvoirFournisseurE) {
        this.ajustementAvoirFournisseurE = ajustementAvoirFournisseurE;
    }

    public BigDecimal getAjustementAvoirFournisseurS() {
        return ajustementAvoirFournisseurS;
    }

    public void setAjustementAvoirFournisseurS(BigDecimal ajustementAvoirFournisseurS) {
        this.ajustementAvoirFournisseurS = ajustementAvoirFournisseurS;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getQuantiteStkDep() {
        return quantiteStkDep;
    }

    public void setQuantiteStkDep(BigDecimal quantiteStkDep) {
        this.quantiteStkDep = quantiteStkDep;
    }

    public BigDecimal getQuantiteRecep() {
        return quantiteRecep;
    }

    public void setQuantiteRecep(BigDecimal quantiteRecep) {
        this.quantiteRecep = quantiteRecep;
    }

    public BigDecimal getQuantiteReceptionAnnulee() {
        return quantiteReceptionAnnulee;
    }

    public void setQuantiteReceptionAnnulee(BigDecimal quantiteReceptionAnnulee) {
        this.quantiteReceptionAnnulee = quantiteReceptionAnnulee;
    }

    public BigDecimal getQuantiteRetourFournisseur() {
        return quantiteRetourFournisseur;
    }

    public void setQuantiteRetourFournisseur(BigDecimal quantiteRetourFournisseur) {
        this.quantiteRetourFournisseur = quantiteRetourFournisseur;
    }

    public BigDecimal getQuantiteAjustementRetourFournisseurE() {
        return quantiteAjustementRetourFournisseurE;
    }

    public void setQuantiteAjustementRetourFournisseurE(BigDecimal quantiteAjustementRetourFournisseurE) {
        this.quantiteAjustementRetourFournisseurE = quantiteAjustementRetourFournisseurE;
    }

    public BigDecimal getQuantiteAjustementRetourFournisseurS() {
        return quantiteAjustementRetourFournisseurS;
    }

    public void setQuantiteAjustementRetourFournisseurS(BigDecimal quantiteAjustementRetourFournisseurS) {
        this.quantiteAjustementRetourFournisseurS = quantiteAjustementRetourFournisseurS;
    }

    public BigDecimal getQuantiteAvoirFournisseur() {
        return quantiteAvoirFournisseur;
    }

    public void setQuantiteAvoirFournisseur(BigDecimal quantiteAvoirFournisseur) {
        this.quantiteAvoirFournisseur = quantiteAvoirFournisseur;
    }

    public BigDecimal getQuantiteAjustementAvoirFournisseurE() {
        return quantiteAjustementAvoirFournisseurE;
    }

    public void setQuantiteAjustementAvoirFournisseurE(BigDecimal quantiteAjustementAvoirFournisseurE) {
        this.quantiteAjustementAvoirFournisseurE = quantiteAjustementAvoirFournisseurE;
    }

    public BigDecimal getQuantiteAjustementAvoirFournisseurS() {
        return quantiteAjustementAvoirFournisseurS;
    }

    public void setQuantiteAjustementAvoirFournisseurS(BigDecimal quantiteAjustementAvoirFournisseurS) {
        this.quantiteAjustementAvoirFournisseurS = quantiteAjustementAvoirFournisseurS;
    }

    public BigDecimal getQuantiteTransent() {
        return quantiteTransent;
    }

    public void setQuantiteTransent(BigDecimal quantiteTransent) {
        this.quantiteTransent = quantiteTransent;
    }

    public BigDecimal getQuantiteTransf() {
        return quantiteTransf;
    }

    public void setQuantiteTransf(BigDecimal quantiteTransf) {
        this.quantiteTransf = quantiteTransf;
    }

    public BigDecimal getQuantiteRetourPerime() {
        return quantiteRetourPerime;
    }

    public void setQuantiteRetourPerime(BigDecimal quantiteRetourPerime) {
        this.quantiteRetourPerime = quantiteRetourPerime;
    }

    public BigDecimal getQuantiteAjustementRetourPerimeE() {
        return quantiteAjustementRetourPerimeE;
    }

    public void setQuantiteAjustementRetourPerimeE(BigDecimal quantiteAjustementRetourPerimeE) {
        this.quantiteAjustementRetourPerimeE = quantiteAjustementRetourPerimeE;
    }

    public BigDecimal getQuantiteAjustementRetourPerimeS() {
        return quantiteAjustementRetourPerimeS;
    }

    public void setQuantiteAjustementRetourPerimeS(BigDecimal quantiteAjustementRetourPerimeS) {
        this.quantiteAjustementRetourPerimeS = quantiteAjustementRetourPerimeS;
    }

    public BigDecimal getQuantiteRedressE() {
        return quantiteRedressE;
    }

    public void setQuantiteRedressE(BigDecimal quantiteRedressE) {
        this.quantiteRedressE = quantiteRedressE;
    }

    public BigDecimal getQuantiteRedressS() {
        return quantiteRedressS;
    }

    public void setQuantiteRedressS(BigDecimal quantiteRedressS) {
        this.quantiteRedressS = quantiteRedressS;
    }

    public BigDecimal getQuantiteBonPrelevement() {
        return quantiteBonPrelevement;
    }

    public void setQuantiteBonPrelevement(BigDecimal quantiteBonPrelevement) {
        this.quantiteBonPrelevement = quantiteBonPrelevement;
    }

    public BigDecimal getQuantiteBonPrelevement1() {
        return quantiteBonPrelevement1;
    }

    public void setQuantiteBonPrelevement1(BigDecimal quantiteBonPrelevement1) {
        this.quantiteBonPrelevement1 = quantiteBonPrelevement1;
    }

    public BigDecimal getQuantiteBonPrelevementAnnulee() {
        return quantiteBonPrelevementAnnulee;
    }

    public void setQuantiteBonPrelevementAnnulee(BigDecimal quantiteBonPrelevementAnnulee) {
        this.quantiteBonPrelevementAnnulee = quantiteBonPrelevementAnnulee;
    }

    public BigDecimal getQuantiteBonRetourPrelevement() {
        return quantiteBonRetourPrelevement;
    }

    public void setQuantiteBonRetourPrelevement(BigDecimal quantiteBonRetourPrelevement) {
        this.quantiteBonRetourPrelevement = quantiteBonRetourPrelevement;
    }

    public BigDecimal getQuantiteDecoupE() {
        return quantiteDecoupE;
    }

    public void setQuantiteDecoupE(BigDecimal quantiteDecoupE) {
        this.quantiteDecoupE = quantiteDecoupE;
    }

    public BigDecimal getQuantiteDecoupS() {
        return quantiteDecoupS;
    }

    public void setQuantiteDecoupS(BigDecimal quantiteDecoupS) {
        this.quantiteDecoupS = quantiteDecoupS;
    }

    public BigDecimal getQuantiteQuittance() {
        return quantiteQuittance;
    }

    public void setQuantiteQuittance(BigDecimal quantiteQuittance) {
        this.quantiteQuittance = quantiteQuittance;
    }

    public BigDecimal getQuantiteQuittanceAnnulee() {
        return quantiteQuittanceAnnulee;
    }

    public void setQuantiteQuittanceAnnulee(BigDecimal quantiteQuittanceAnnulee) {
        this.quantiteQuittanceAnnulee = quantiteQuittanceAnnulee;
    }

    public BigDecimal getQuantiteAvoir() {
        return quantiteAvoir;
    }

    public void setQuantiteAvoir(BigDecimal quantiteAvoir) {
        this.quantiteAvoir = quantiteAvoir;
    }

    public BigDecimal getQuantiteInvenE() {
        return quantiteInvenE;
    }

    public void setQuantiteInvenE(BigDecimal quantiteInvenE) {
        this.quantiteInvenE = quantiteInvenE;
    }

    public BigDecimal getQuantiteInvenS() {
        return quantiteInvenS;
    }

    public void setQuantiteInvenS(BigDecimal quantiteInvenS) {
        this.quantiteInvenS = quantiteInvenS;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    public BigDecimal getTransfertEA() {
        return transfertEA;
    }

    public void setTransfertEA(BigDecimal transfertEA) {
        this.transfertEA = transfertEA;
    }

    public BigDecimal getTransfertSA() {
        return transfertSA;
    }

    public void setTransfertSA(BigDecimal transfertSA) {
        this.transfertSA = transfertSA;
    }

    public BigDecimal getQuantiteTransfertEA() {
        return quantiteTransfertEA;
    }

    public void setQuantiteTransfertEA(BigDecimal quantiteTransfertEA) {
        this.quantiteTransfertEA = quantiteTransfertEA;
    }

    public BigDecimal getQuantiteTransfertSA() {
        return quantiteTransfertSA;
    }

    public void setQuantiteTransfertSA(BigDecimal quantiteTransfertSA) {
        this.quantiteTransfertSA = quantiteTransfertSA;
    }

    public BigDecimal getTransfertCB() {
        return transfertCB;
    }

    public void setTransfertCB(BigDecimal transfertCB) {
        this.transfertCB = transfertCB;
    }

    public BigDecimal getTransfertBC() {
        return transfertBC;
    }

    public void setTransfertBC(BigDecimal transfertBC) {
        this.transfertBC = transfertBC;
    }

    public BigDecimal getQuantiteTransfertCB() {
        return quantiteTransfertCB;
    }

    public void setQuantiteTransfertCB(BigDecimal quantiteTransfertCB) {
        this.quantiteTransfertCB = quantiteTransfertCB;
    }

    public BigDecimal getQuantiteTransfertBC() {
        return quantiteTransfertBC;
    }

    public void setQuantiteTransfertBC(BigDecimal quantiteTransfertBC) {
        this.quantiteTransfertBC = quantiteTransfertBC;
    }
   
    @Override
    public String toString() {
        return "MouvementStockEditionDTO{" + "coddep=" + coddep + ", designationDepot=" + designationDepot + ", codeSaisiDepot=" + codeSaisiDepot + ", codeArticle=" + codeArticle + ", codeSaisiArticle=" + codeSaisiArticle + ", designationArticle=" + designationArticle + ", designationCategorieArticle=" + designationCategorieArticle + ", codeUnite=" + codeUnite + ", designationUnite=" + designationUnite + ", typbon=" + typbon + ", valeur=" + valeur + ", quantite=" + quantite + ", stkDep=" + stkDep + ", recep=" + recep + ", receptionAnnulee=" + receptionAnnulee + ", retourFournisseur=" + retourFournisseur + ", ajustementRetourFournisseurE=" + ajustementRetourFournisseurE + ", ajustementRetourFournisseurS=" + ajustementRetourFournisseurS + ", avoirFournisseur=" + avoirFournisseur + ", ajustementAvoirFournisseurE=" + ajustementAvoirFournisseurE + ", ajustementAvoirFournisseurS=" + ajustementAvoirFournisseurS + ", transent=" + transent + ", transf=" + transf + ", transfertEA=" + transfertEA + ", transfertSA=" + transfertSA + ", retourPerime=" + retourPerime + ", ajustementRetourPerimeE=" + ajustementRetourPerimeE + ", ajustementRetourPerimeS=" + ajustementRetourPerimeS + ", redressE=" + redressE + ", redressS=" + redressS + ", bonPrelevement=" + bonPrelevement + ", bonPrelevement1=" + bonPrelevement1 + ", bonPrelevementAnnulee=" + bonPrelevementAnnulee + ", bonRetourPrelevement=" + bonRetourPrelevement + ", decoupE=" + decoupE + ", decoupS=" + decoupS + ", quittance=" + quittance + ", quittanceAnnulee=" + quittanceAnnulee + ", avoir=" + avoir + ", invenE=" + invenE + ", invenS=" + invenS + ", quantiteStkDep=" + quantiteStkDep + ", quantiteRecep=" + quantiteRecep + ", quantiteReceptionAnnulee=" + quantiteReceptionAnnulee + ", quantiteRetourFournisseur=" + quantiteRetourFournisseur + ", quantiteAjustementRetourFournisseurE=" + quantiteAjustementRetourFournisseurE + ", quantiteAjustementRetourFournisseurS=" + quantiteAjustementRetourFournisseurS + ", quantiteAvoirFournisseur=" + quantiteAvoirFournisseur + ", quantiteAjustementAvoirFournisseurE=" + quantiteAjustementAvoirFournisseurE + ", quantiteAjustementAvoirFournisseurS=" + quantiteAjustementAvoirFournisseurS + ", quantiteTransent=" + quantiteTransent + ", quantiteTransf=" + quantiteTransf + ", quantiteTransfertEA=" + quantiteTransfertEA + ", quantiteTransfertSA=" + quantiteTransfertSA + ", quantiteRetourPerime=" + quantiteRetourPerime + ", quantiteAjustementRetourPerimeE=" + quantiteAjustementRetourPerimeE + ", quantiteAjustementRetourPerimeS=" + quantiteAjustementRetourPerimeS + ", quantiteRedressE=" + quantiteRedressE + ", quantiteRedressS=" + quantiteRedressS + ", quantiteBonPrelevement=" + quantiteBonPrelevement + ", quantiteBonPrelevement1=" + quantiteBonPrelevement1 + ", quantiteBonPrelevementAnnulee=" + quantiteBonPrelevementAnnulee + ", quantiteBonRetourPrelevement=" + quantiteBonRetourPrelevement + ", quantiteDecoupE=" + quantiteDecoupE + ", quantiteDecoupS=" + quantiteDecoupS + ", quantiteQuittance=" + quantiteQuittance + ", quantiteQuittanceAnnulee=" + quantiteQuittanceAnnulee + ", quantiteAvoir=" + quantiteAvoir + ", quantiteInvenE=" + quantiteInvenE + ", quantiteInvenS=" + quantiteInvenS + '}';
    }

}
