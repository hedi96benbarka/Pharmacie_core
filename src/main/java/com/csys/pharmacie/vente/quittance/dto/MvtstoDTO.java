package com.csys.pharmacie.vente.quittance.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class MvtstoDTO {

    @NotNull
    private Integer codart;

    @NotNull
    private String numbon;

    private String numaffiche;

    private LocalDateTime datbon;

    private String numordre;

    @NotNull
    @Size(min = 1, max = 5)
    private Integer codTvaAch;

    @NotNull
    private BigDecimal tauTvaAch;

    @NotNull
    @Size(min = 1, max = 17)
    private String lotInter;

    @Size(min = 0, max = 2)
    private TypeBonEnum typbon;

    @NotNull
    private BigDecimal priuni;

    private BigDecimal montht;

    private BigDecimal tautva;

    private BigDecimal priach;

    @Size(min = 0, max = 500)
    private String memoart;
    
    private LocalDate datPer;

    private LocalTime heureSysteme;

    @NotNull
    private CategorieDepotEnum categDepot;

    @NotNull
    @Size(min = 0, max = 255)
    private String desart;

    @NotNull
    @Size(min = 0, max = 255)
    private String desArtSec;

    @NotNull
    @Size(min = 0, max = 50)
    private String codeSaisi;

    @NotNull
    private BigDecimal quantite;

    @NotNull
    private BigDecimal qteRestante;

    private BigDecimal remise;

    private BigDecimal majoration;

    private String codvend;

    @NotNull
    private Integer unityCode;

    private String unityDesignation;

    private Boolean perissable;

    private Integer codtva;

    private BigDecimal ajustement;

    private BigDecimal prixBrute;

    private BigDecimal quantityInStore;

    private BigDecimal tauxCouverture;

    private Boolean isImported;

    private String numdoss;
    
    private Integer codeSociete;
    
    private String remiseConventionnellePharmacie;
    private BigDecimal quantiteRestanteWithoutRounding;

    public MvtstoDTO() {
    }

    public MvtstoDTO(Integer codart, BigDecimal quantite) {
        this.codart = codart;
        this.quantite = quantite;
    }

    public MvtstoDTO(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public MvtstoDTO(BigDecimal quantite, BigDecimal qteRestante) {
        this.quantite = quantite;
        this.qteRestante = qteRestante;
    }

    public MvtstoDTO(BigDecimal montht, BigDecimal quantite, BigDecimal qteRestante) {
        this.montht = montht;
        this.quantite = quantite;
        this.qteRestante = qteRestante;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    public Integer getCodTvaAch() {
        return codTvaAch;
    }

    public void setCodTvaAch(Integer codTvaAch) {
        this.codTvaAch = codTvaAch;
    }

    public BigDecimal getTauTvaAch() {
        return tauTvaAch;
    }

    public void setTauTvaAch(BigDecimal tauTvaAch) {
        this.tauTvaAch = tauTvaAch;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public TypeBonEnum getTypbon() {
        return typbon;
    }

    public void setTypbon(TypeBonEnum typbon) {
        this.typbon = typbon;
    }

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public BigDecimal getMontht() {
        return montht;
    }

    public void setMontht(BigDecimal montht) {
        this.montht = montht;
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    public BigDecimal getPriach() {
        return priach;
    }

    public void setPriach(BigDecimal priach) {
        this.priach = priach;
    }

    public String getMemoart() {
        return memoart;
    }

    public void setMemoart(String memoart) {
        this.memoart = memoart;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public LocalTime getHeureSysteme() {
        return heureSysteme;
    }

    public void setHeureSysteme(LocalTime heureSysteme) {
        this.heureSysteme = heureSysteme;
    }

    public Integer getUnityCode() {
        return unityCode;
    }

    public void setUnityCode(Integer UnityCode) {
        this.unityCode = UnityCode;
    }

    public String getUnityDesignation() {
        return unityDesignation;
    }

    public void setUnityDesignation(String UnityDesignation) {
        this.unityDesignation = UnityDesignation;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesArtSec() {
        return desArtSec;
    }

    public void setDesArtSec(String desArtSec) {
        this.desArtSec = desArtSec;
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

    public Boolean getPerissable() {
        return perissable;
    }

    public void setPerissable(Boolean perissable) {
        this.perissable = perissable;
    }

    public BigDecimal getQteRestante() {
        return qteRestante;
    }

    public void setQteRestante(BigDecimal qteRestante) {
        this.qteRestante = qteRestante;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getMajoration() {
        return majoration;
    }

    public void setMajoration(BigDecimal majoration) {
        this.majoration = majoration;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public LocalDateTime getDatbon() {
        return datbon;
    }

    public void setDatbon(LocalDateTime datbon) {
        this.datbon = datbon;
    }

    public BigDecimal getAjustement() {
        return ajustement;
    }

    public void setAjustement(BigDecimal ajustement) {
        this.ajustement = ajustement;
    }

    public BigDecimal getPrixBrute() {
        return prixBrute;
    }

    public void setPrixBrute(BigDecimal prixBrute) {
        this.prixBrute = prixBrute;
    }

    public BigDecimal getQuantityInStore() {
        return quantityInStore;
    }

    public void setQuantityInStore(BigDecimal quantityInStore) {
        this.quantityInStore = quantityInStore;
    }

    public BigDecimal getTauxCouverture() {
        return tauxCouverture;
    }

    public void setTauxCouverture(BigDecimal tauxCouverture) {
        this.tauxCouverture = tauxCouverture;
    }

    public Boolean getIsImported() {
        return isImported;
    }

    public void setIsImported(Boolean isImported) {
        this.isImported = isImported;
    }

    public String getNumdoss() {
        return numdoss;
    }

    public void setNumdoss(String numdoss) {
        this.numdoss = numdoss;
    }

    public Integer getCodeSociete() {
        return codeSociete;
    }

    public void setCodeSociete(Integer codeSociete) {
        this.codeSociete = codeSociete;
    }

    public String getRemiseConventionnellePharmacie() {
        return remiseConventionnellePharmacie;
    }

    public void setRemiseConventionnellePharmacie(String remiseConventionnellePharmacie) {
        this.remiseConventionnellePharmacie = remiseConventionnellePharmacie;
    }

    public BigDecimal getQuantiteRestanteWithoutRounding() {
        return quantiteRestanteWithoutRounding;
    }

    public void setQuantiteRestanteWithoutRounding(BigDecimal quantiteRestanteWithoutRounding) {
        this.quantiteRestanteWithoutRounding = quantiteRestanteWithoutRounding;
    }

 

    @Override
    public String toString() {
        return "MvtstoDTO{" + "codart=" + codart + ", datbon=" + datbon + ", categDepot=" + categDepot + ", codeSaisi=" + codeSaisi + '}';
    }

  

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MvtstoDTO other = (MvtstoDTO) obj;
        if (!Objects.equals(this.numbon, other.numbon)) {
            return false;
        }
        if (!Objects.equals(this.lotInter, other.lotInter)) {
            return false;
        }
        if (!Objects.equals(this.codart, other.codart)) {
            return false;
        }
        if (!Objects.equals(this.datPer, other.datPer)) {
            return false;
        }
        return true;
    }

}
