/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.domain;

import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.helper.BaseBon;
import com.csys.pharmacie.helper.SatisfactionFactureEnum;
import com.csys.pharmacie.vente.quittance.dto.SocieteDTO;
import com.mysema.commons.lang.Pair;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "facture")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "Facture.mvtstoCollection",
            attributeNodes = {
                @NamedAttributeNode("mvtstoCollection")
            })

})
@NamedQueries({
    @NamedQuery(name = "Facture.findAll", query = "SELECT f FROM Facture f")})
@Audited
@AuditTable("facture_AUD")
@AuditOverride(forClass = BaseBon.class, isAudited = true)
public class Facture extends BaseBon implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Size(max = 20)
    @Column(name = "numpiece", length = 20)
    private String numpiece;
    @Column(name = "datepiece")
    private LocalDateTime datepiece;
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "Satisf")
    private SatisfactionFactureEnum satisf;
    
    @Column(name = "mntbon", precision = 18, scale = 3)
    private BigDecimal mntbon;
    @Size(max = 140)
    @Column(name = "memop", length = 140)
    private String memop;
    @Size(max = 12)
    @Column(name = "numdoss", length = 8)
    private String numdoss;
    @Basic(optional = false)
    @Size(min = 1, max = 30)
    @Column(name = "CodAnnul", nullable = false, length = 10)
    private String codAnnul;
    @Column(name = "DatAnnul")
    private LocalDateTime datAnnul;
    @Size(max = 10)
    @Column(name = "numfacbl")
    private String numfacbl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "etatbon")
    private boolean etatbon;
    @Size(max = 100)
    @Column(name = "raisoc")
    private String raisoc;
    @Column(name = "NumCha")
    private Integer numCha;
    
    @Column(name = "coddep")
    private Integer coddep;
    @Basic(optional = false)
    @Column(name = "imprimer")
    private boolean imprimer;
    @Column(name = "medecin")
    private Integer medecin;
    @Size(max = 10)
    @Column(name = "codfrs")
    private String codfrs;
    @Size(max = 255)
    @Column(name = "Reffrs")
    private String reffrs;
    @Size(max = 255)
    @Column(name = "ReffrsAr")
    private String reffrsAr;
    @Size(max = 20)
    @Column(name = "numbonRecept")
    private String numbonRecept;
    @Size(max = 20)
    @Column(name = "numbonTransfert")
    private String numbonTransfert;
    
    @Column(name = "panier")
    private Boolean panier;
    
    @Column(name = "CodePrestation")
    private Integer codePrestation;
    @Size(max = 20)
    @Column(name = "numbonPanier")
    private String numbonPanier;
    @Column(name = "partiePatient", precision = 18, scale = 3)
    private BigDecimal partiePatient;
    
    @Column(name = "partiePEC", precision = 18, scale = 3)
    private BigDecimal partiePEC;
    
    @Column(name = "quantitePrestation")
    private BigDecimal quantitePrestation;
    
    @Column(name = "integrer")
    private Boolean integrer;
    
    @Column(name = "num_Vir")
    private String codeIntegration;
    
    @Size(max = 20)
    @Column(name = "numbonComplementaire")
    private String numbonComplementaire;
    
    @Column(name = "codeDemande")
    private Long codeDemande;
    
    @Column(name = "codeDetailsAdmission")
    private Long codeDetailsAdmission;
    
    @Column(name = "idOrdonnance")
    private Long idOrdonnance;
    
    @Column(name = "codeSociete")
    private Integer codeSociete;
    
    @Column(name = "remise_conventionnelle_pharmacie")
    private String remiseConventionnellePharmacie;
    
    @Column(name = "code_cost_center")
    private Integer codeCostCenter;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facture", orphanRemoval = true)
    private List<BaseTvaFacture> baseTvaFactureList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facture")
    private List<Mvtsto> mvtstoCollection;
    
    @JoinColumn(name = "numpiece", referencedColumnName = "numbon", nullable = true, insertable = false, updatable = false)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private FactureDR factureDR;
    
    @NotNull
    @Column(name = "hashCode")
    private byte[] hashCode;
    
    @Column(name = "code_operation")
    private Integer codeOperation;
    
    @Column(name = "code_cost_center_analytique")
    private Integer codeCostCenterAnalytique;
    
    @PrePersist
    @Override
    public void prePersist() {
        this.setDatesys(LocalDate.now());
        this.setHeuresys(LocalTime.now());
        this.datbon = LocalDateTime.now(); 
        this.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());
        this.setNumaffiche(this.getNumbon().substring(2));
        this.setCodeIntegration("");
    }
    
    public void calcul(List<TvaDTO> listTVA, SocieteDTO societe, Boolean pharmacieExterne) {
        
        List<BaseTvaFacture> listeBaseTVA = new ArrayList<BaseTvaFacture>();
        Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVA = mvtstoCollection.stream()
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getPriuni().multiply(art.getQuantite()), BigDecimal::add)));
        BigDecimal montantHT = BigDecimal.ZERO;
        BigDecimal montantTva = BigDecimal.ZERO;
        
        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseTVA.entrySet()) {
            BaseTvaFacture base = new BaseTvaFacture();
            base.setBaseTva(entry.getValue());
            base.setCodeTva(entry.getKey().getFirst());
            base.setTauxTva(entry.getKey().getSecond());
            base.setMontantTva(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP));
            base.setFacture(this);
            listeBaseTVA.add(base);
            montantHT = montantHT.add(entry.getValue());
            montantTva = montantTva.add(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)));
        }
        for (TvaDTO tva : listTVA) {
            if (!listeBaseTVA.stream().anyMatch(t -> t.getCodeTva().equals(tva.getCode()))) {
                BaseTvaFacture base = new BaseTvaFacture();
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(tva.getCode());
                base.setTauxTva(tva.getValeur());
                base.setMontantTva(BigDecimal.ZERO);
                base.setFacture(this);
                listeBaseTVA.add(base);
            }
        }
        if (this.getBaseTvaFactureList() != null) {
            this.getBaseTvaFactureList().clear();
            this.getBaseTvaFactureList().addAll(listeBaseTVA);
        } else {
            this.setBaseTvaFactureList(listeBaseTVA);
        }
        if (societe == null || !pharmacieExterne) {
            this.partiePatient = mvtstoCollection.stream().map(art -> {
                return art.getMontht().multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                        .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
            }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(2, RoundingMode.HALF_UP);
            this.mntbon = montantHT.add(montantTva).setScale(7, RoundingMode.HALF_UP);
            this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient);
        } else {
            if ("C".equals(societe.getRemiseConventionnellePharmacie())) {
                this.partiePatient = mvtstoCollection.stream().map(art -> {
                    return art.getMontht().multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(7, RoundingMode.HALF_UP);
                this.mntbon = montantHT.add(montantTva).setScale(7, RoundingMode.HALF_UP);
                this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient.setScale(2, RoundingMode.HALF_UP));
            } else {
                this.partiePatient = mvtstoCollection.stream().map(art -> {
                    return art.getPrixBrute().multiply(art.getQuantite()).multiply((BigDecimal.valueOf(100).subtract(art.getAjustement())).divide(BigDecimal.valueOf(100)))
                            .multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(7, RoundingMode.HALF_UP);
                this.mntbon = montantHT.add(montantTva).setScale(7, RoundingMode.HALF_UP);
                this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient.setScale(2, RoundingMode.HALF_UP));
            }
        }
    }
    
    public void calculPartiePatientPartiePEC(SocieteDTO societe, Boolean pharmacieExterne) {
        
        if (societe == null || !pharmacieExterne) {
            this.partiePatient = mvtstoCollection.stream().map(art -> {
                return art.getMontht().multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                        .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
            }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(2, RoundingMode.HALF_UP);
            this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient);
        } else {
            if ("C".equals(societe.getRemiseConventionnellePharmacie())) {
                this.partiePatient = mvtstoCollection.stream().map(art -> {
                    return art.getMontht().multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(2, RoundingMode.HALF_UP);
                this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient.setScale(2, RoundingMode.HALF_UP));
            } else {
                this.partiePatient = mvtstoCollection.stream().map(art -> {
                    return art.getPrixBrute().multiply(art.getQuantite()).multiply((BigDecimal.valueOf(100).subtract(art.getAjustement())).divide(BigDecimal.valueOf(100)))
                            .multiply(art.getQuantite()).multiply(BigDecimal.ONE.add(art.getTautva().divide(BigDecimal.valueOf(100))))
                            .multiply((BigDecimal.valueOf(100).subtract(art.getTauxCouverture())).divide(BigDecimal.valueOf(100)));
                }).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)).setScale(2, RoundingMode.HALF_UP);
                this.partiePEC = this.mntbon.setScale(2, RoundingMode.HALF_UP).subtract(this.partiePatient.setScale(2, RoundingMode.HALF_UP));
            }
        }
    }
    
    public Facture() {
    }
    
    public String getNumpiece() {
        return numpiece;
    }
    
    public void setNumpiece(String numpiece) {
        this.numpiece = numpiece;
    }
    
    public LocalDateTime getDatepiece() {
        return datepiece;
    }
    
    public void setDatepiece(LocalDateTime datepiece) {
        this.datepiece = datepiece;
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
    
    public String getCodAnnul() {
        return codAnnul;
    }
    
    public void setCodAnnul(String codAnnul) {
        this.codAnnul = codAnnul;
    }
    
    public LocalDateTime getDatAnnul() {
        return datAnnul;
    }
    
    public void setDatAnnul(LocalDateTime datAnnul) {
        this.datAnnul = datAnnul;
    }
    
    public String getNumdoss() {
        return numdoss;
    }
    
    public void setNumdoss(String numdoss) {
        this.numdoss = numdoss;
    }
    
    public SatisfactionFactureEnum getSatisf() {
        return satisf;
    }
    
    public void setSatisf(SatisfactionFactureEnum satisf) {
        this.satisf = satisf;
    }
    
    public String getNumfacbl() {
        return numfacbl;
    }
    
    public void setNumfacbl(String numfacbl) {
        this.numfacbl = numfacbl;
    }
    
    public boolean isEtatbon() {
        return etatbon;
    }
    
    public void setEtatbon(boolean etatbon) {
        this.etatbon = etatbon;
    }
    
    public List<BaseTvaFacture> getBaseTvaFactureList() {
        return baseTvaFactureList;
    }
    
    public String getCodfrs() {
        return codfrs;
    }
    
    public void setCodfrs(String codfrs) {
        this.codfrs = codfrs;
    }
    
    public String getReffrs() {
        return reffrs;
    }
    
    public void setReffrs(String reffrs) {
        this.reffrs = reffrs;
    }
    
    public String getReffrsAr() {
        return reffrsAr;
    }
    
    public void setReffrsAr(String reffrsAr) {
        this.reffrsAr = reffrsAr;
    }
    
    public String getNumbonRecept() {
        return numbonRecept;
    }
    
    public void setNumbonRecept(String numbonRecept) {
        this.numbonRecept = numbonRecept;
    }
    
    public String getNumbonTransfert() {
        return numbonTransfert;
    }
    
    public void setNumbonTransfert(String numbonTransfert) {
        this.numbonTransfert = numbonTransfert;
    }
    
    public Boolean getPanier() {
        return panier;
    }
    
    public void setPanier(Boolean panier) {
        this.panier = panier;
    }
    
    public Integer getCodePrestation() {
        return codePrestation;
    }
    
    public void setCodePrestation(Integer codePrestation) {
        this.codePrestation = codePrestation;
    }
    
    public String getNumbonPanier() {
        return numbonPanier;
    }
    
    public void setNumbonPanier(String numbonPanier) {
        this.numbonPanier = numbonPanier;
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
    
    public void setBaseTvaFactureList(List<BaseTvaFacture> baseTvaFactureList) {
        this.baseTvaFactureList = baseTvaFactureList;
    }
    
    public List<Mvtsto> getMvtstoCollection() {
        return mvtstoCollection;
    }
    
    public void setMvtstoCollection(List<Mvtsto> mvtstoCollection) {
        this.mvtstoCollection = mvtstoCollection;
    }
    
    public String getRaisoc() {
        return raisoc;
    }
    
    public void setRaisoc(String raisoc) {
        this.raisoc = raisoc;
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
    
    public Integer getNumCha() {
        return numCha;
    }
    
    public void setNumCha(Integer numCha) {
        this.numCha = numCha;
    }
    
    public Integer getMedecin() {
        return medecin;
    }
    
    public void setMedecin(Integer medecin) {
        this.medecin = medecin;
    }
    
    public FactureDR getFactureDR() {
        return factureDR;
    }
    
    public void setFactureDR(FactureDR factureDR) {
        this.factureDR = factureDR;
    }
    
    public Boolean getIntegrer() {
        return integrer;
    }
    
    public void setIntegrer(Boolean integrer) {
        this.integrer = integrer;
    }
    
    public String getCodeIntegration() {
        return codeIntegration;
    }
    
    public void setCodeIntegration(String codeIntegration) {
        this.codeIntegration = codeIntegration;
    }    
    
    public String getNumbonComplementaire() {
        return numbonComplementaire;
    }
    
    public void setNumbonComplementaire(String numbonComplementaire) {
        this.numbonComplementaire = numbonComplementaire;
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
    
    public Integer getCodeOperation() {
        return codeOperation;
    }
    
    public void setCodeOperation(Integer codeOperation) {
        this.codeOperation = codeOperation;
    }
    
    public Integer getCodeCostCenterAnalytique() {
        return codeCostCenterAnalytique;
    }
    
    public void setCodeCostCenterAnalytique(Integer codeCostCenterAnalytique) {
        this.codeCostCenterAnalytique = codeCostCenterAnalytique;
    }
    
    @Override
    public String toString() {
        return "Facture{" + "numpiece=" + numpiece + ", datepiece=" + datepiece + ", satisf=" + satisf + ", mntbon=" + mntbon + ", memop=" + memop + ", numdoss=" + numdoss + ", codAnnul=" + codAnnul + ", datAnnul=" + datAnnul + ", numfacbl=" + numfacbl + ", etatbon=" + etatbon + ", raisoc=" + raisoc + ", numCha=" + numCha + ", coddep=" + coddep + ", imprimer=" + imprimer + ", medecin=" + medecin + ", baseTvaFactureList=" + baseTvaFactureList + ", mvtstoCollection=" + mvtstoCollection + ", factureDR=" + factureDR + '}';
    }
    
}
