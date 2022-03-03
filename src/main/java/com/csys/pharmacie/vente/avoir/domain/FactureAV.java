/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.domain;

import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.helper.BaseBon;
import com.csys.pharmacie.vente.quittance.dto.SocieteDTO;
import com.mysema.commons.lang.Pair;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Farouk
 */
@Entity
@Table(name = "FactureAV")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "FactureAV.mvtStoAVCollection",
            attributeNodes = {
                @NamedAttributeNode("mvtStoAVCollection")
            })
})
@Audited
@AuditTable("FactureAV_AUD")
@AuditOverride(forClass = BaseBon.class, isAudited = true)
public class FactureAV extends BaseBon implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "mntbon")
    private BigDecimal mntbon;
    @Size(max = 140)
    @Column(name = "memop")
    private String memop;
    @Size(max = 12)
    @Column(name = "numdoss")
    private String numdoss;
    @Basic(optional = false)
    @NotNull
    @Column(name = "imprimer")
    private boolean imprimer;
    @Column(name = "coddep")
    private Integer coddep;
    @Column(name = "partiePatient", precision = 18, scale = 3)
    private BigDecimal partiePatient;

    @Size(max = 20)
    @Column(name = "numQuittanceCorrespondant")
    private String numQuittanceCorrespondant;

    @Column(name = "integrer")
    private Boolean integrer;
    
    @Column(name = "num_Vir")
    private String codeIntegration;
    
    @Column(name = "avoirPhPostReg")
    private Boolean avoirPhPostReg;

    @Size(max = 20)
    @Column(name = "numbonComplementaire")
    private String numbonComplementaire;

    @Column(name = "montantRemise")
    private BigDecimal montantRemise;

    @Column(name = "codeSociete")
    private Integer codeSociete;

    @Column(name = "remise_conventionnelle_pharmacie")
    private String remiseConventionnellePharmacie;

    @Column(name = "code_cost_center")
    private Integer codeCostCenter;

    @Column(name = "partiePEC", precision = 18, scale = 3)
    private BigDecimal partiePEC;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureAV")
    private List<MvtStoAV> mvtStoAVCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureAV", orphanRemoval = true)
    private List<BasetvaFactureAV> basetvaFactureAVCollection;

    @NotNull
    @Column(name = "hashCode")
    private byte[] hashCode;

    public void calcul(List<TvaDTO> listTVA, Boolean pharmacieExterne) {

        List<BasetvaFactureAV> listeBaseTVA = new ArrayList<BasetvaFactureAV>();
        Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVA = mvtStoAVCollection.stream()
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getPriuni().multiply(art.getQuantite()), BigDecimal::add)));
        BigDecimal montantHT = BigDecimal.ZERO;
        BigDecimal montantTva = BigDecimal.ZERO;

        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseTVA.entrySet()) {
            BasetvaFactureAV base = new BasetvaFactureAV();
            base.setBaseTva(entry.getValue());
            base.setCodeTva(entry.getKey().getFirst());
            base.setTauxTva(entry.getKey().getSecond());
            base.setMontantTva(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)));
            base.setFactureAV(this);
            listeBaseTVA.add(base);
            montantHT = montantHT.add(entry.getValue());
            montantTva = montantTva.add(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)));
        }
        for (TvaDTO tva : listTVA) {
            if (!listeBaseTVA.stream().anyMatch(t -> t.getCodeTva().equals(tva.getCode()))) {
                BasetvaFactureAV base = new BasetvaFactureAV();
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(tva.getCode());
                base.setTauxTva(tva.getValeur());
                base.setMontantTva(BigDecimal.ZERO);
                base.setFactureAV(this);
                listeBaseTVA.add(base);
            }
        }
        if (this.getBasetvaFactureAVCollection() != null) {
            this.getBasetvaFactureAVCollection().clear();
            this.getBasetvaFactureAVCollection().addAll(listeBaseTVA);
        } else {
            this.setBasetvaFactureAVCollection(listeBaseTVA);
        }
        if (this.getCodeSociete() == null || !pharmacieExterne) {
            this.partiePatient = mvtStoAVCollection.stream().map(art -> {
                return art.getMontht().multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                        .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
            }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(2, RoundingMode.HALF_UP);
            this.mntbon = montantHT.add(montantTva).setScale(7, RoundingMode.HALF_UP);
            this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient);

        } else {
            if ("C".equals(this.getRemiseConventionnellePharmacie())) {
                this.partiePatient = mvtStoAVCollection.stream().map(art -> {
                    return art.getMontht().multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(7, RoundingMode.HALF_UP);
                this.mntbon = montantHT.add(montantTva).setScale(7, RoundingMode.HALF_UP);
                this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient);
            } else {
                this.partiePatient = mvtStoAVCollection.stream().map(art -> {
                    return art.getPrixBrute().multiply((BigDecimal.valueOf(100).subtract(art.getAjustement())).divide(BigDecimal.valueOf(100))).multiply(art.getQuantite()).multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(7, RoundingMode.HALF_UP);
                this.mntbon = montantHT.add(montantTva).setScale(7, RoundingMode.HALF_UP);
                this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient);
            }
        }

    }

    public void calculPartiePatientPartiePEC(SocieteDTO societe, Boolean pharmacieExterne) {
        if (societe == null || !pharmacieExterne) {
            this.partiePatient = mvtStoAVCollection.stream().map(art -> {
                return art.getMontht().multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                        .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
            }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(2, RoundingMode.HALF_UP);
            this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient);

        } else {
            if ("C".equals(societe.getRemiseConventionnellePharmacie())) {
                this.partiePatient = mvtStoAVCollection.stream().map(art -> {
                    return art.getMontht().multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(2, RoundingMode.HALF_UP);
                this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient);
            } else {
                this.partiePatient = mvtStoAVCollection.stream().map(art -> {
                    return art.getPrixBrute().multiply((BigDecimal.valueOf(100).subtract(art.getAjustement())).divide(BigDecimal.valueOf(100))).multiply(art.getQuantite()).multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(2, RoundingMode.HALF_UP);
                this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient);
            }
        }

    }

    public FactureAV() {
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

    public boolean isImprimer() {
        return imprimer;
    }

    public void setImprimer(boolean imprimer) {
        this.imprimer = imprimer;
    }

    public BigDecimal getPartiePatient() {
        return partiePatient;
    }

    public void setPartiePatient(BigDecimal partiePatient) {
        this.partiePatient = partiePatient;
    }

    public String getNumQuittanceCorrespondant() {
        return numQuittanceCorrespondant;
    }

    public void setNumQuittanceCorrespondant(String numQuittanceCorrespondant) {
        this.numQuittanceCorrespondant = numQuittanceCorrespondant;
    }

    public BigDecimal getPartiePEC() {
        return partiePEC;
    }

    public void setPartiePEC(BigDecimal partiePEC) {
        this.partiePEC = partiePEC;
    }

    public String getNumbonComplementaire() {
        return numbonComplementaire;
    }

    public void setNumbonComplementaire(String numbonComplementaire) {
        this.numbonComplementaire = numbonComplementaire;
    }

    public BigDecimal getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(BigDecimal montantRemise) {
        this.montantRemise = montantRemise;
    }

    public Boolean getIntegrer() {
        return integrer;
    }

    public void setIntegrer(Boolean integrer) {
        this.integrer = integrer;
    }

    public List<MvtStoAV> getMvtStoAVCollection() {
        return mvtStoAVCollection;
    }

    public void setMvtStoAVCollection(List<MvtStoAV> mvtStoAVCollection) {
        this.mvtStoAVCollection = mvtStoAVCollection;
    }

    public List<BasetvaFactureAV> getBasetvaFactureAVCollection() {
        return basetvaFactureAVCollection;
    }

    public void setBasetvaFactureAVCollection(List<BasetvaFactureAV> basetvaFactureAVCollection) {
        this.basetvaFactureAVCollection = basetvaFactureAVCollection;
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

    public Boolean getAvoirPhPostReg() {
        return avoirPhPostReg;
    }

    public void setAvoirPhPostReg(Boolean avoirPhPostReg) {
        this.avoirPhPostReg = avoirPhPostReg;
    }

    public Integer getCodeCostCenter() {
        return codeCostCenter;
    }

    public void setCodeCostCenter(Integer codeCostCenter) {
        this.codeCostCenter = codeCostCenter;
    }

    public byte[] getHashCode() {
        return hashCode;
    }

    public void setHashCode(byte[] hashCode) {
        this.hashCode = hashCode;
    }

    public String getCodeIntegration() {
        return codeIntegration;
    }

    public void setCodeIntegration(String codeIntegration) {
        this.codeIntegration = codeIntegration;
    }

    @Override
    public String toString() {
        return "FactureAV{" + "mntbon=" + mntbon + ", memop=" + memop + ", numdoss=" + numdoss + ", imprimer=" + imprimer + ", coddep=" + coddep + ", mvtStoAVCollection=" + mvtStoAVCollection + ", basetvaFactureAVCollection=" + basetvaFactureAVCollection + '}';
    }

}
