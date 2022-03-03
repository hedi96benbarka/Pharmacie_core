package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommandeAchatDTO {

    private Integer code;
    private String numbon;

    @NotNull
    @Size(min = 1, max = 10)
    private CategorieDepotEnum categorieDepot;

    @Size(min = 0, max = 50)
    private String memo;

    private String satisfaction;

//    private Integer delaiPaiement;

    @NotNull
    private boolean imprimer;

    @NotNull
    private boolean manuel;

    private boolean stockable;

    private String userCreate;

    private Date dateCreate;

    private BigDecimal montantttc;

    private List<DetailCommandeAchatDTO> detailCommandeAchatCollection;

    private FournisseurDTO fournisseur;

    private String userDelete;
    private Date dateDelete;

    private String userAnnul;
    private Date dateAnnul;

    private boolean isService;
    
    private BigDecimal montantht;
    
    private BigDecimal montanttva;
    
     private BigDecimal montantRemise;
     
//    private ModeReglementDTO modeReglement;

    private String userValidate;
    private Date dateValidate;
        private String userArchive;
    private Date dateArchive;
      private Boolean valide;
          private Date dateDebutExenoration;
    private Date dateFinExenoration;

    @Size(max = 50)
    private String localReception;
//    private Integer delaiValeurPaiement;
    
    
    private Collection<CommandeAchatModeReglementDTO> modeReglementList;
        Integer codeSite;
    public CommandeAchatDTO() {
    }

    public CommandeAchatDTO(Integer code, String numbon) {
        this.code = code;
        this.numbon = numbon;
    }

    public Date getDateDelete() {
        return dateDelete;
    }

    public String getUserAnnul() {
        return userAnnul;
    }

    public void setUserAnnul(String userAnnul) {
        this.userAnnul = userAnnul;
    }

    public Date getDateAnnul() {
        return dateAnnul;
    }

    public void setDateAnnul(Date dateAnnul) {
        this.dateAnnul = dateAnnul;
    }

    public void setDateDelete(Date dateDelete) {
        this.dateDelete = dateDelete;
    }

    public CategorieDepotEnum getCategorieDepot() {
        return categorieDepot;
    }

    public void setCategorieDepot(CategorieDepotEnum categorieDepot) {
        this.categorieDepot = categorieDepot;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

//    public Integer getDelaiPaiement() {
//        return delaiPaiement;
//    }
//
//    public void setDelaiPaiement(Integer delaiPaiement) {
//        this.delaiPaiement = delaiPaiement;
//    }

    public boolean getImprimer() {
        return imprimer;
    }

    public void setImprimer(boolean imprimer) {
        this.imprimer = imprimer;
    }

    public boolean getManuel() {
        return manuel;
    }

    public void setManuel(boolean manuel) {
        this.manuel = manuel;
    }

    public boolean getStockable() {
        return stockable;
    }

    public void setStockable(boolean stockable) {
        this.stockable = stockable;
    }

    public Collection<DetailCommandeAchatDTO> getDetailCommandeAchatCollection() {
        return detailCommandeAchatCollection;
    }

  
    public void setDetailCommandeAchatCollection(List<DetailCommandeAchatDTO> detailCommandeAchatCollection) {
        this.detailCommandeAchatCollection = detailCommandeAchatCollection;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserDelete() {
        return userDelete;
    }

    public void setUserDelete(String userDelete) {
        this.userDelete = userDelete;
    }

    public BigDecimal getMontantttc() {
        return montantttc;
    }

    public void setMontantttc(BigDecimal montantttc) {
        this.montantttc = montantttc;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    public boolean isIsService() {
        return isService;
    }

    public void setIsService(boolean isService) {
        this.isService = isService;
    }

    public BigDecimal getMontantht() {
        return montantht;
    }

    public void setMontantht(BigDecimal montantht) {
        this.montantht = montantht;
    }

    public BigDecimal getMontanttva() {
        return montanttva;
    }

    public void setMontanttva(BigDecimal montanttva) {
        this.montanttva = montanttva;
    }

    public BigDecimal getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(BigDecimal montantRemise) {
        this.montantRemise = montantRemise;
    }

//    public ModeReglementDTO getModeReglement() {
//        return modeReglement;
//    }
//
//    public void setModeReglement(ModeReglementDTO modeReglement) {
//        this.modeReglement = modeReglement;
//    }

    public String getUserValidate() {
        return userValidate;
    }

    public void setUserValidate(String userValidate) {
        this.userValidate = userValidate;
    }

    public Date getDateValidate() {
        return dateValidate;
    }

    public void setDateValidate(Date dateValidate) {
        this.dateValidate = dateValidate;
    }

    public String getLocalReception() {
        return localReception;
    }

    public void setLocalReception(String localReception) {
        this.localReception = localReception;
    }

//    public Integer getDelaiValeurPaiement() {
//        return delaiValeurPaiement;
//    }
//
//    public void setDelaiValeurPaiement(Integer delaiValeurPaiement) {
//        this.delaiValeurPaiement = delaiValeurPaiement;
//    }

    public Collection<CommandeAchatModeReglementDTO> getModeReglementList() {
        return modeReglementList;
    }

    public void setModeReglementList(Collection<CommandeAchatModeReglementDTO> modeReglementList) {
        this.modeReglementList = modeReglementList;
    }

    public String getUserArchive() {
        return userArchive;
    }

    public void setUserArchive(String userArchive) {
        this.userArchive = userArchive;
    }

    public Date getDateArchive() {
        return dateArchive;
    }

    public void setDateArchive(Date dateArchive) {
        this.dateArchive = dateArchive;
    }

    public Boolean getValide() {
        return valide;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }

    public Integer getCodeSite() {
        return codeSite;
    }

    public void setCodeSite(Integer codeSite) {
        this.codeSite = codeSite;
    }

    public Date getDateDebutExenoration() {
        return dateDebutExenoration;
    }

    public void setDateDebutExenoration(Date dateDebutExenoration) {
        this.dateDebutExenoration = dateDebutExenoration;
    }

    public Date getDateFinExenoration() {
        return dateFinExenoration;
    }

    public void setDateFinExenoration(Date dateFinExenoration) {
        this.dateFinExenoration = dateFinExenoration;
    }

    
    @Override
    public String toString() {
        return "CommandeAchatDTO{" + "code=" + code + ", numbon=" + numbon + '}';
    }

    
    
    
}
