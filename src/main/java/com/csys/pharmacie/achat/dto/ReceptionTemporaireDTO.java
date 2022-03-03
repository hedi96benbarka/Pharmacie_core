package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReceptionTemporaireDTO extends BonRecepDTO    {

//    @NotNull
//    @NotEmpty
//    private List<Integer> purchaseOrdersCodes;
    @NotNull
    @Size(min = 1, max = 10)
    private String codfrs;

    private BigDecimal mntbon;

//    @Size(min = 0, max = 140)
//    private String memop;

    @Size(min = 1, max = 20)
    private String refFrs;

//    @NotNull
//    private LocalDate dateFrs;

    private Integer coddep;

    @Size(min = 0, max = 50)
    private String userAnnule;

    private String numaffiche;

    private CategorieDepotEnum categDepot;

    private LocalDateTime datAnnul;

    private BigInteger codePieceJointe;

    private Boolean fournisseurExonere;
      @Size(min = 1, max = 20)
    private String receivingNumaffiche;

//    private TypeBonEnum typbon;

    private LocalDate dateDebutExoneration;

    private LocalDate dateFinExoneration;
//    private int maxDelaiPaiement;

    @Size(min = 0, max = 20)
    private String numbon;

    @Size(min = 0, max = 20)
    private String codvend;

    private LocalDateTime datbon;

    private LocalDate datesys;

    private LocalTime heuresys;

    private Integer receivingCode;

    private List<DetailReceptionTemporaireDTO> detailReceptionTempraireDTO;
      
    private String  numBonFactureBa;
    private boolean isTemporaire;
     private boolean isValidated=false;
    
    @NotNull
    @Valid
    private FournisseurDTO fournisseur;
    private List<BaseTvaReceptionTemporaireDTO> basesTVA;
    public String getCodfrs() {
        return codfrs;
    }

    public void setCodfrs(String codfrs) {
        this.codfrs = codfrs;
    }

    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

//    public String getMemop() {
//        return memop;
//    }
//
//    public void setMemop(String memop) {
//        this.memop = memop;
//    }

    public String getReffrs() {
        return refFrs;
    }

    public void setReffrs(String reffrs) {
        this.refFrs = reffrs;
    }

//    public LocalDate getDateFrs() {
//        return dateFrs;
//    }
//
//    public void setDateFrs(LocalDate dateFrs) {
//        this.dateFrs = dateFrs;
//    }

 

    public String getCodAnnul() {
        return userAnnule;
    }

    public void setCodAnnul(String codAnnul) {
        this.userAnnule = codAnnul;
    }

    public LocalDateTime getDatAnnul() {
        return datAnnul;
    }

    public void setDatAnnul(LocalDateTime datAnnul) {
        this.datAnnul = datAnnul;
    }

    public BigInteger getCodePieceJointe() {
        return codePieceJointe;
    }

    public void setCodePieceJointe(BigInteger codePieceJointe) {
        this.codePieceJointe = codePieceJointe;
    }

  

    public LocalDate getDateDebutExoneration() {
        return dateDebutExoneration;
    }

    public void setDateDebutExoneration(LocalDate dateDebutExoneration) {
        this.dateDebutExoneration = dateDebutExoneration;
    }

    public LocalDate getDateFinExoneration() {
        return dateFinExoneration;
    }

    public void setDateFinExoneration(LocalDate dateFinExoneration) {
        this.dateFinExoneration = dateFinExoneration;
    }

   

    public LocalDate getDatesys() {
        return datesys;
    }

    public void setDatesys(LocalDate datesys) {
        this.datesys = datesys;
    }

    public LocalTime getHeuresys() {
        return heuresys;
    }

    public void setHeuresys(LocalTime heuresys) {
        this.heuresys = heuresys;
    }

//    public TypeBonEnum getTypbon() {
//        return typbon;
//    }
//
//    public void setTypbon(TypeBonEnum typbon) {
//        this.typbon = typbon;
//    }

   

  
    public List<DetailReceptionTemporaireDTO> getDetailReceptionTempraireDTO() {
        return detailReceptionTempraireDTO;
    }

    public void setDetailReceptionTempraireDTO(List<DetailReceptionTemporaireDTO> detailReceptionTempraireDTO) {
        this.detailReceptionTempraireDTO = detailReceptionTempraireDTO;
    }

    public String getReferenceFournisseur() {
        return refFrs;
    }

    public void setReferenceFournisseur(String referenceFournisseur) {
        this.refFrs = referenceFournisseur;
    }

//    public LocalDate getDateReferenceFournisseur() {
//        return dateFrs;
//    }
//
//    public void setDateReferenceFournisseur(LocalDate dateReferenceFournisseur) {
//        this.dateFrs = dateReferenceFournisseur;
//    }

  

    public boolean isIsTemporaire() {
        return isTemporaire;
    }

    public void setIsTemporaire(boolean isTemporaire) {
        this.isTemporaire = isTemporaire;
    }


    public String getNumBonFactureBa() {
        return numBonFactureBa;
    }

    public void setNumBonFactureBa(String numBonFactureBa) {
        this.numBonFactureBa = numBonFactureBa;
    }

  

    public List<BaseTvaReceptionTemporaireDTO> getReceptionBasesTVA() {
        return basesTVA;
    }

    public void setReceptionBasesTVA(List<BaseTvaReceptionTemporaireDTO> basesTVA) {
        this.basesTVA = basesTVA;
    }

    public boolean isIsValidated() {
        return isValidated;
    }

    public void setIsValidated(boolean isValidated) {
        this.isValidated = isValidated;
    }





}
