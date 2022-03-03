package com.csys.pharmacie.vente.avoir.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.vente.quittance.dto.ReglementDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Avoir {

    @Size(
            min = 1,
            max = 20
    )
    private String numbon;

    private TypeBonEnum typbon;
    @Size(
            min = 0,
            max = 140
    )
    private String memop;

    @NotNull
    @Size(
            min = 0,
            max = 12
    )
    private String numdoss;

    @NotNull
    private Integer coddep;
    
    @Valid
    @Size(min = 1)
    private List<MvtAvoir> mvtAvoir;

    @NotNull
    private CategorieDepotEnum categDepot;

    private List<ReglementDTO> reglementDTOs;

    private BigDecimal mntbon;

    private String numFacture;
    
    private Boolean avoirPhPostReg;
    
    private BigDecimal montantRemise;

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public String getNumdoss() {
        return numdoss;
    }

    public void setNumdoss(String numdoss) {
        this.numdoss = numdoss;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public List<MvtAvoir> getMvtAvoir() {
        return mvtAvoir;
    }

    public void setMvtAvoir(List<MvtAvoir> MvtAvoir) {
        this.mvtAvoir = MvtAvoir;
    }

    public TypeBonEnum getTypbon() {
        return typbon;
    }

    public void setTypbon(TypeBonEnum typbon) {
        this.typbon = typbon;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public List<ReglementDTO> getReglementDTOs() {
        return reglementDTOs;
    }

    public void setReglementDTOs(List<ReglementDTO> reglementDTOs) {
        this.reglementDTOs = reglementDTOs;
    }

    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

    public String getNumFacture() {
        return numFacture;
    }

    public void setNumFacture(String numFacture) {
        this.numFacture = numFacture;
    }

    public Boolean getAvoirPhPostReg() {
        return avoirPhPostReg;
    }

    public void setAvoirPhPostReg(Boolean avoirPhPostReg) {
        this.avoirPhPostReg = avoirPhPostReg;
    }

    public BigDecimal getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(BigDecimal montantRemise) {
        this.montantRemise = montantRemise;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.typbon);
        hash = 67 * hash + Objects.hashCode(this.memop);
        hash = 67 * hash + Objects.hashCode(this.numdoss);
        hash = 67 * hash + Objects.hashCode(this.coddep);
        hash = 67 * hash + Objects.hashCode(this.mvtAvoir);
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
        final Avoir other = (Avoir) obj;
        if (!Objects.equals(this.memop, other.memop)) {
            return false;
        }
        if (!Objects.equals(this.numdoss, other.numdoss)) {
            return false;
        }
        if (this.typbon != other.typbon) {
            return false;
        }
        if (!Objects.equals(this.coddep, other.coddep)) {
            return false;
        }
        if (!Objects.equals(this.mvtAvoir, other.mvtAvoir)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QuittanceDTO{" + "numbon=" + numbon + ", typbon=" + typbon + ", memop=" + memop + ", numdoss=" + numdoss + ", coddep=" + coddep + ", MvtQuittance=" + mvtAvoir + '}';
    }

}
