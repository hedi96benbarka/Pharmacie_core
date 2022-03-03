package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.BaseTVADTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;

import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RetourPerimeDTO {

    @NotNull
    private BigDecimal mntbon;

    @Size(min = 0, max = 140)
    private String memop;

    @NotNull
    private String codFrs;

    private FournisseurDTO fournisseur;

    @NotNull
    private MotifRetourDTO motifRetourDTO;

    @Valid
    @NotNull
    private List<MvtstoRetourPerimeDTO> detailFactureRPCollection;

    private String numbon;

    private String codvend;

    private LocalDateTime datbon;

    private String designationDepot;

    @NotNull
    private Integer coddep;

    private TypeBonEnum typbon;

    private String numaffiche;

    @NotNull
    private CategorieDepotEnum categDepot;

    @NotNull
    private String refFrs;

    @NotNull
    private LocalDate datRefFrs;

    
    private List<BaseTVADTO> basesTVA;

    public List<BaseTVADTO> getBasesTVA() {
        return basesTVA;
    }

    public void setBasesTVA(List<BaseTVADTO> basesTVA) {
        this.basesTVA = basesTVA;
    }
    
    
    

    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public String getCodFrs() {
        return codFrs;
    }

    public void setCodFrs(String codFrs) {
        this.codFrs = codFrs;
    }

    public MotifRetourDTO getMotifRetourDTO() {
        return motifRetourDTO;
    }

    public void setMotifRetourDTO(MotifRetourDTO motifRetourDTO) {
        this.motifRetourDTO = motifRetourDTO;
    }

    public List<MvtstoRetourPerimeDTO> getDetailFactureRPCollection() {
        return detailFactureRPCollection;
    }

    public void setDetailFactureRPCollection(List<MvtstoRetourPerimeDTO> detailFactureRPCollection) {
        this.detailFactureRPCollection = detailFactureRPCollection;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public LocalDateTime getDatbon() {
        return datbon;
    }

    public void setDatbon(LocalDateTime datbon) {
        this.datbon = datbon;
    }

    public TypeBonEnum getTypbon() {
        return typbon;
    }

    public void setTypbon(TypeBonEnum typbon) {
        this.typbon = typbon;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public String getRefFrs() {
        return refFrs;
    }

    public void setRefFrs(String refFrs) {
        this.refFrs = refFrs;
    }

    public LocalDate getDatRefFrs() {
        return datRefFrs;
    }

    public void setDatRefFrs(LocalDate datRefFrs) {
        this.datRefFrs = datRefFrs;
    }

    @Override
    public String toString() {
        return "RetourPerimeDTO{" + "detailFactureRPCollection=" + detailFactureRPCollection + '}';
    }

}
