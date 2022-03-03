/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
public class ImmobilisationDTO {

    @Id
    private Integer code;
    @NotNull
    @Size(min = 1, max = 150)
    private String designation;

    @NotNull
    @Size(min = 1, max = 150)

    private String designationSec;

    @NotNull
    private boolean actif;

    @NotNull
    @Size(
            min = 1,
            max = 50
    )
    private String codeSaisi;

    private BigDecimal quantite;

    private BigDecimal valeurAchat;

    private BigDecimal valeurProratat;

    private BigDecimal autreFrais;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebutAmortissement;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFinAmortissement;

    private BigDecimal amortissementAnterieur;

    private BigDecimal valeurAmortissementAnterieur;

    private BigDecimal tauxAmortissement;

    private String codeEmplacement;

    private Integer numeroFacture;

    private Integer codeTva;

    private String numeroReception;

    @NotNull
    private String codeArticle;
    private String codeSaisiArticle;

    private Boolean amortie;

    @Size(
            min = 0,
            max = 1
    )
    private String saisie;

    private Boolean existe;

    private Boolean cession;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCession;

    private BigDecimal quantiteInventaire;

    @Size(
            min = 0,
            max = 50
    )
    private String numeroSerie;
    private Boolean detaille;
    private Boolean genererImmobilisation;
    private Boolean ettiquetable;
    private Boolean avecNumeroSerie;
    private String codeFournisseur;
    private String familleArticle;

    private BigDecimal tauxIfrs;

    private BigDecimal tauxAmortFiscale1;

    private BigDecimal tauxAmortFiscale2;
    
    private LocalDate dateFacture;
    
    private String numeroFactureFournisseur;

    public ImmobilisationDTO() {
    }

    public ImmobilisationDTO(BigDecimal quantite, BigDecimal valeurAchat, Integer numeroFacture, String numeroReception, String codeArticle) {
        this.quantite = quantite;
        this.valeurAchat = valeurAchat;
        this.numeroFacture = numeroFacture;
        this.numeroReception = numeroReception;
        this.codeArticle = codeArticle;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignationSec() {
        return designationSec;
    }

    public void setDesignationSec(String designationSec) {
        this.designationSec = designationSec;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getValeurAchat() {
        return valeurAchat;
    }

    public void setValeurAchat(BigDecimal valeurAchat) {
        this.valeurAchat = valeurAchat;
    }

    public BigDecimal getValeurProratat() {
        return valeurProratat;
    }

    public void setValeurProratat(BigDecimal valeurProratat) {
        this.valeurProratat = valeurProratat;
    }

    public BigDecimal getAutreFrais() {
        return autreFrais;
    }

    public void setAutreFrais(BigDecimal autreFrais) {
        this.autreFrais = autreFrais;
    }

    public Date getDateDebutAmortissement() {
        return dateDebutAmortissement;
    }

    public void setDateDebutAmortissement(Date dateDebutAmortissement) {
        this.dateDebutAmortissement = dateDebutAmortissement;
    }

    public Date getDateFinAmortissement() {
        return dateFinAmortissement;
    }

    public void setDateFinAmortissement(Date dateFinAmortissement) {
        this.dateFinAmortissement = dateFinAmortissement;
    }

    public BigDecimal getAmortissementAnterieur() {
        return amortissementAnterieur;
    }

    public void setAmortissementAnterieur(BigDecimal amortissementAnterieur) {
        this.amortissementAnterieur = amortissementAnterieur;
    }

    public BigDecimal getValeurAmortissementAnterieur() {
        return valeurAmortissementAnterieur;
    }

    public void setValeurAmortissementAnterieur(BigDecimal valeurAmortissementAnterieur) {
        this.valeurAmortissementAnterieur = valeurAmortissementAnterieur;
    }

    public BigDecimal getTauxAmortissement() {
        return tauxAmortissement;
    }

    public void setTauxAmortissement(BigDecimal tauxAmortissement) {
        this.tauxAmortissement = tauxAmortissement;
    }

    public String getCodeEmplacement() {
        return codeEmplacement;
    }

    public void setCodeEmplacement(String codeEmplacement) {
        this.codeEmplacement = codeEmplacement;
    }

    public Integer getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(Integer numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public String getNumeroReception() {
        return numeroReception;
    }

    public void setNumeroReception(String numeroReception) {
        this.numeroReception = numeroReception;
    }

    public String getCodeSaisiArticle() {
        return codeSaisiArticle;
    }

    public void setCodeSaisiArticle(String codeSaisiArticle) {
        this.codeSaisiArticle = codeSaisiArticle;
    }

    public Boolean getAmortie() {
        return amortie;
    }

    public void setAmortie(Boolean amortie) {
        this.amortie = amortie;
    }

    public String getSaisie() {
        return saisie;
    }

    public void setSaisie(String saisie) {
        this.saisie = saisie;
    }

    public Boolean getExiste() {
        return existe;
    }

    public void setExiste(Boolean existe) {
        this.existe = existe;
    }

    public Boolean getCession() {
        return cession;
    }

    public void setCession(Boolean cession) {
        this.cession = cession;
    }

    public Date getDateCession() {
        return dateCession;
    }

    public void setDateCession(Date dateCession) {
        this.dateCession = dateCession;
    }

    public BigDecimal getQuantiteInventaire() {
        return quantiteInventaire;
    }

    public void setQuantiteInventaire(BigDecimal quantiteInventaire) {
        this.quantiteInventaire = quantiteInventaire;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Boolean getDetaille() {
        return detaille;
    }

    public void setDetaille(Boolean detaille) {
        this.detaille = detaille;
    }

    public Boolean getGenererImmobilisation() {
        return genererImmobilisation;
    }

    public void setGenererImmobilisation(Boolean genererImmobilisation) {
        this.genererImmobilisation = genererImmobilisation;
    }

    public Boolean getEttiquetable() {
        return ettiquetable;
    }

    public void setEttiquetable(Boolean ettiquetable) {
        this.ettiquetable = ettiquetable;
    }

    public Boolean getAvecNumeroSerie() {
        return avecNumeroSerie;
    }

    public void setAvecNumeroSerie(Boolean avecNumeroSerie) {
        this.avecNumeroSerie = avecNumeroSerie;
    }

    public String getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(String codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getFamilleArticle() {
        return familleArticle;
    }

    public void setFamilleArticle(String familleArticle) {
        this.familleArticle = familleArticle;
    }

    public BigDecimal getTauxIfrs() {
        return tauxIfrs;
    }

    public void setTauxIfrs(BigDecimal tauxIfrs) {
        this.tauxIfrs = tauxIfrs;
    }

    public BigDecimal getTauxAmortFiscale1() {
        return tauxAmortFiscale1;
    }

    public void setTauxAmortFiscale1(BigDecimal tauxAmortFiscale1) {
        this.tauxAmortFiscale1 = tauxAmortFiscale1;
    }

    public BigDecimal getTauxAmortFiscale2() {
        return tauxAmortFiscale2;
    }

    public void setTauxAmortFiscale2(BigDecimal tauxAmortFiscale2) {
        this.tauxAmortFiscale2 = tauxAmortFiscale2;
    }

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public String getNumeroFactureFournisseur() {
        return numeroFactureFournisseur;
    }

    public void setNumeroFactureFournisseur(String numeroFactureFournisseur) {
        this.numeroFactureFournisseur = numeroFactureFournisseur;
    }

    @Override
    public String toString() {
        return "ImmobilisationDTO{" + "code=" + code + ", designation=" + designation + ", designationSec=" + designationSec + ", codeSaisi=" + codeSaisi + ", dateDebutAmortissement=" + dateDebutAmortissement + ", dateFinAmortissement=" + dateFinAmortissement + ", tauxAmortissement=" + tauxAmortissement + ", codeEmplacement=" + codeEmplacement + ", codeArticle=" + codeArticle + ", codeSaisiArticle=" + codeSaisiArticle + ", numeroSerie=" + numeroSerie + '}';
    }

}
