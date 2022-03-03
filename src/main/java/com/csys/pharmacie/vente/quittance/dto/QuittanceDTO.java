package com.csys.pharmacie.vente.quittance.dto;

import com.csys.pharmacie.helper.TypeBonEnum;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QuittanceDTO {

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

    @Size(
            min = 0,
            max = 12
    )
    private String numdoss;

    @NotNull
    private Integer coddep;

    private BigDecimal mntbon;

    @Valid
    @Size(min = 1)
    private List<MvtQuittanceDTO> mvtQuittance;

    private FactureDRDTO FactureDr;

    private List<ReglementDTO> reglementDTOs;

    private Integer codePrestation;
    
    private Long codeDemande;

    private String codeFournisseur;

    private BigDecimal partiePatient;

    private BigDecimal partiePEC;

    private BigDecimal quantitePrestation;

    private Boolean appliquerExoneration;
    
    private Long codeDetailsAdmission;
    
    private Long idOrdonnance;
    
    private Integer codeOperation;

    public void calcul(SocieteDTO societe, Boolean pharmacieExterne) {

        if (societe == null || !pharmacieExterne) {
            this.partiePatient = mvtQuittance.stream().map(art -> {
                return art.getPrixVente().multiply(art.getQuantite()).multiply((BigDecimal.valueOf(100).subtract(art.getAjustement())).divide(BigDecimal.valueOf(100)))
                        .multiply((BigDecimal.valueOf(100).subtract(art.getRemise())).divide(BigDecimal.valueOf(100)))
                        .multiply((BigDecimal.valueOf(100).add(art.getMajoration())).divide(BigDecimal.valueOf(100)))
                        .multiply(BigDecimal.ONE.add(art.getTauxTva().divide(BigDecimal.valueOf(100))))
                        .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
            }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(2, RoundingMode.HALF_UP);
            this.mntbon = mvtQuittance.stream().map(art -> {
                return art.getPrixVente().multiply(art.getQuantite()).multiply((BigDecimal.valueOf(100).subtract(art.getAjustement())).divide(BigDecimal.valueOf(100)))
                        .multiply((BigDecimal.valueOf(100).subtract(art.getRemise())).divide(BigDecimal.valueOf(100)))
                        .multiply((BigDecimal.valueOf(100).add(art.getMajoration())).divide(BigDecimal.valueOf(100)))
                        .multiply(BigDecimal.ONE.add(art.getTauxTva().divide(BigDecimal.valueOf(100))));
            }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(7, RoundingMode.HALF_UP);
            this.partiePEC = this.mntbon.subtract(this.partiePatient);

        } else {
            if ("C".equals(societe.getRemiseConventionnellePharmacie())) {
                this.partiePatient = mvtQuittance.stream().map(art -> {
                    return art.getPrixVente().multiply(art.getQuantite()).multiply((BigDecimal.valueOf(100).subtract(art.getAjustement())).divide(BigDecimal.valueOf(100)))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getRemise())).divide(BigDecimal.valueOf(100)))
                            .multiply((BigDecimal.valueOf(100).add(art.getMajoration())).divide(BigDecimal.valueOf(100)))
                            .multiply(BigDecimal.ONE.add(art.getTauxTva().divide(BigDecimal.valueOf(100))))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(2, RoundingMode.HALF_UP);
                this.mntbon = mvtQuittance.stream().map(art -> {
                    return art.getPrixVente().multiply(art.getQuantite()).multiply((BigDecimal.valueOf(100).subtract(art.getAjustement())).divide(BigDecimal.valueOf(100)))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getRemise())).divide(BigDecimal.valueOf(100)))
                            .multiply((BigDecimal.valueOf(100).add(art.getMajoration())).divide(BigDecimal.valueOf(100)))
                            .multiply(BigDecimal.ONE.add(art.getTauxTva().divide(BigDecimal.valueOf(100))));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(7, RoundingMode.HALF_UP);
                this.partiePEC = this.mntbon.subtract(this.partiePatient);
            } else {
                /*partiePatient sans remise sans majoration*/
                this.partiePatient = mvtQuittance.stream().map(art -> {
                    return art.getPrixVente().multiply(art.getQuantite()).multiply((BigDecimal.valueOf(100).subtract(art.getAjustement())).divide(BigDecimal.valueOf(100)))
                            .multiply(BigDecimal.ONE.add(art.getTauxTva().divide(BigDecimal.valueOf(100))))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(2, RoundingMode.HALF_UP);
                this.mntbon = mvtQuittance.stream().map(art -> {
                    return art.getPrixVente().multiply(art.getQuantite()).multiply((BigDecimal.valueOf(100).subtract(art.getAjustement())).divide(BigDecimal.valueOf(100)))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getRemise())).divide(BigDecimal.valueOf(100)))
                            .multiply((BigDecimal.valueOf(100).add(art.getMajoration())).divide(BigDecimal.valueOf(100)))
                            .multiply(BigDecimal.ONE.add(art.getTauxTva().divide(BigDecimal.valueOf(100))));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(7, RoundingMode.HALF_UP);
                this.partiePEC = this.mntbon.subtract(this.partiePatient);
            }
        }
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

    public List<MvtQuittanceDTO> getMvtQuittance() {
        return mvtQuittance;
    }

    public void setMvtQuittance(List<MvtQuittanceDTO> mvtQuittance) {
        this.mvtQuittance = mvtQuittance;
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

    public FactureDRDTO getFactureDr() {
        return FactureDr;
    }

    public void setFactureDr(FactureDRDTO FactureDr) {
        this.FactureDr = FactureDr;
    }

    public List<ReglementDTO> getReglementDTOs() {
        return reglementDTOs;
    }

    public void setReglementDTOs(List<ReglementDTO> reglementDTOs) {
        this.reglementDTOs = reglementDTOs;
    }

    public Integer getCodePrestation() {
        return codePrestation;
    }

    public void setCodePrestation(Integer codePrestation) {
        this.codePrestation = codePrestation;
    }

    public String getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(String codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }

    public BigDecimal getPartiePatient() {
        return partiePatient;
    }

    public void setPartiePatient(BigDecimal partiePatient) {
        this.partiePatient = partiePatient;
    }

    public BigDecimal getPartiePEC() {
        return partiePEC;
    }

    public void setPartiePEC(BigDecimal partiePEC) {
        this.partiePEC = partiePEC;
    }

    public BigDecimal getQuantitePrestation() {
        return quantitePrestation;
    }

    public void setQuantitePrestation(BigDecimal quantitePrestation) {
        this.quantitePrestation = quantitePrestation;
    }

    public Boolean getAppliquerExoneration() {
        return appliquerExoneration;
    }

    public void setAppliquerExoneration(Boolean appliquerExoneration) {
        this.appliquerExoneration = appliquerExoneration;
    }

    public Long getCodeDemande() {
        return codeDemande;
    }

    public void setCodeDemande(Long codeDemande) {
        this.codeDemande = codeDemande;
    }

    public Long getCodeDetailsAdmission() {
        return codeDetailsAdmission;
    }

    public void setCodeDetailsAdmission(Long codeDetailsAdmission) {
        this.codeDetailsAdmission = codeDetailsAdmission;
    }

    public Long getIdOrdonnance() {
        return idOrdonnance;
    }

    public void setIdOrdonnance(Long idOrdonnance) {
        this.idOrdonnance = idOrdonnance;
    }

    public Integer getCodeOperation() {
        return codeOperation;
    }

    public void setCodeOperation(Integer codeOperation) {
        this.codeOperation = codeOperation;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.typbon);
        hash = 67 * hash + Objects.hashCode(this.memop);
        hash = 67 * hash + Objects.hashCode(this.numdoss);
        hash = 67 * hash + Objects.hashCode(this.coddep);
        hash = 67 * hash + Objects.hashCode(this.mvtQuittance);
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
        final QuittanceDTO other = (QuittanceDTO) obj;
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
        if (!Objects.equals(this.mvtQuittance, other.mvtQuittance)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QuittanceDTO{" + "numbon=" + numbon + ", typbon=" + typbon + ", memop=" + memop + ", numdoss=" + numdoss + ", coddep=" + coddep + ", mntbon=" + mntbon + ", MvtQuittance=" + mvtQuittance + ", FactureDr=" + FactureDr + ", reglementDTOs=" + reglementDTOs + ", codePrestation=" + codePrestation + ", codeFournisseur=" + codeFournisseur + ", partiePatient=" + partiePatient + ", partiePEC=" + partiePEC + '}';
    }

}
