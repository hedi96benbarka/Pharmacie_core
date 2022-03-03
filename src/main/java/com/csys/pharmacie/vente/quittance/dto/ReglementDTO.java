package com.csys.pharmacie.vente.quittance.dto;

import com.csys.pharmacie.helper.EnumTypeReglementPatient;
import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReglementDTO {

    private Long code;

    private String codeReglement;

    @Size(min = 0, max = 50)
    private String caissier;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datReg;

    private BigDecimal mntReg;

    @Size(min = 0, max = 300)
    private String designation;

    private BigDecimal mntTropMoinsPercu;

    @NotNull
    private Boolean pec;

    private AdmissionDTO admission;

    private Integer codeNatureAdmission;

    private ModeReglementDTO modeReglement;

    @Size(min = 0, max = 50)
    private String reference;

    @Temporal(TemporalType.TIMESTAMP)
    private Date echeance;

    private String titulaire;

    private BigDecimal mntRecu;

    private Integer imprimer;

    private boolean deleted;

    private BanqueDTO banque;

    private List<DetailsReglementDTO> detailsReglementCollection;

    private Long codeReglementCorrespondant;

    private Integer codeMotifAvoir;

    private String motifAvoir;

    private Integer codeTypeSession;

    private EnumTypeReglementPatient typeReglement;

    public Integer getCodeTypeSession() {
        return codeTypeSession;
    }

    public void setCodeTypeSession(Integer codeTypeSession) {
        this.codeTypeSession = codeTypeSession;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getCodeReglement() {
        return codeReglement;
    }

    public void setCodeReglement(String codeReglement) {
        this.codeReglement = codeReglement;
    }

    public String getCaissier() {
        return caissier;
    }

    public void setCaissier(String caissier) {
        this.caissier = caissier;
    }

    public Date getDatReg() {
        return datReg;
    }

    public void setDatReg(Date datReg) {
        this.datReg = datReg;
    }

    public BigDecimal getMntReg() {
        return mntReg;
    }

    public void setMntReg(BigDecimal mntReg) {
        this.mntReg = mntReg;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getMntTropMoinsPercu() {
        return mntTropMoinsPercu;
    }

    public void setMntTropMoinsPercu(BigDecimal mntTropMoinsPercu) {
        this.mntTropMoinsPercu = mntTropMoinsPercu;
    }

    public Boolean getPec() {
        return pec;
    }

    public void setPec(Boolean pec) {
        this.pec = pec;
    }

    public AdmissionDTO getAdmission() {
        return admission;
    }

    public void setAdmission(AdmissionDTO admission) {
        this.admission = admission;
    }

    public Integer getCodeNatureAdmission() {
        return codeNatureAdmission;
    }

    public void setCodeNatureAdmission(Integer codeNatureAdmission) {
        this.codeNatureAdmission = codeNatureAdmission;
    }

    public ModeReglementDTO getModeReglement() {
        return modeReglement;
    }

    public void setModeReglement(ModeReglementDTO modeReglement) {
        this.modeReglement = modeReglement;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getEcheance() {
        return echeance;
    }

    public void setEcheance(Date echeance) {
        this.echeance = echeance;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public void setTitulaire(String titulaire) {
        this.titulaire = titulaire;
    }

    public BigDecimal getMntRecu() {
        return mntRecu;
    }

    public void setMntRecu(BigDecimal mntRecu) {
        this.mntRecu = mntRecu;
    }

    public Integer getImprimer() {
        return imprimer;
    }

    public void setImprimer(Integer imprimer) {
        this.imprimer = imprimer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public BanqueDTO getBanque() {
        return banque;
    }

    public void setBanque(BanqueDTO banque) {
        this.banque = banque;
    }

    public List<DetailsReglementDTO> getDetailsReglementCollection() {
        return detailsReglementCollection;
    }

    public void setDetailsReglementCollection(List<DetailsReglementDTO> detailsReglementCollection) {
        this.detailsReglementCollection = detailsReglementCollection;
    }

    public Long getCodeReglementCorrespondant() {
        return codeReglementCorrespondant;
    }

    public void setCodeReglementCorrespondant(Long codeReglementCorrespondant) {
        this.codeReglementCorrespondant = codeReglementCorrespondant;
    }

    public Integer getCodeMotifAvoir() {
        return codeMotifAvoir;
    }

    public void setCodeMotifAvoir(Integer codeMotifAvoir) {
        this.codeMotifAvoir = codeMotifAvoir;
    }

    public String getMotifAvoir() {
        return motifAvoir;
    }

    public void setMotifAvoir(String motifAvoir) {
        this.motifAvoir = motifAvoir;
    }

    public EnumTypeReglementPatient getTypeReglement() {
        return typeReglement;
    }

    public void setTypeReglement(EnumTypeReglementPatient typeReglement) {
        this.typeReglement = typeReglement;
    }
    
    

    @Override
    public String toString() {
        return "ReglementDTO{" + "code=" + code + ", codeReglement=" + codeReglement + ", caissier=" + caissier + ", datReg=" + datReg + ", mntReg=" + mntReg + ", designation=" + designation + ", mntTropMoinsPercu=" + mntTropMoinsPercu + ", pec=" + pec + ", admission=" + admission + ", codeNatureAdmission=" + codeNatureAdmission + ", modeReglement=" + modeReglement + ", reference=" + reference + ", echeance=" + echeance + ", titulaire=" + titulaire + ", mntRecu=" + mntRecu + ", imprimer=" + imprimer + ", deleted=" + deleted + ", banque=" + banque + ", detailsReglementCollection=" + detailsReglementCollection + ", codeReglementCorrespondant=" + codeReglementCorrespondant + ", codeMotifAvoir=" + codeMotifAvoir + ", motifAvoir=" + motifAvoir + '}';
    }

}
