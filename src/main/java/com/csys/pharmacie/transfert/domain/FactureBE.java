/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import com.csys.pharmacie.helper.BaseBon;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Farouk
 */
@Entity
@Table(name = "FactureBE")
public class FactureBE extends BaseBon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "mntbon")
    private BigDecimal mntbon;
    @Size(max = 250)
    @Column(name = "memop")
    private String memop;

    @Column(name = "coddep")
    private Integer coddep;

    @Column(name = "demande_redressement")
    private Integer numeroDemande;

    @NotNull
    @Column(name = "Motif")
    private Integer codeMotifRedressement;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numbon")
    private List<MvtStoBE> detailFactureBECollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureBE", orphanRemoval = true)
    private List<BaseTvaRedressement> baseTvaRedressement;

    public List<MvtStoBE> getDetailFactureBECollection() {
        return detailFactureBECollection;
    }

    public void setDetailFactureBECollection(List<MvtStoBE> detailFactureBECollection) {
        this.detailFactureBECollection = detailFactureBECollection;
    }

    public Integer getCodeMotifRedressement() {
        return codeMotifRedressement;
    }

    public void setCodeMotifRedressement(Integer codeMotifRedressement) {
        this.codeMotifRedressement = codeMotifRedressement;
    }

    public FactureBE() {
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

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public Integer getNumeroDemande() {
        return numeroDemande;
    }

    public void setNumeroDemande(Integer numeroDemande) {
        this.numeroDemande = numeroDemande;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getNumbon() != null ? getNumbon().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactureBE)) {
            return false;
        }
        FactureBE other = (FactureBE) object;
        if ((this.getNumbon() == null && other.getNumbon() != null) || (this.getNumbon() != null && !this.getNumbon().equals(other.getNumbon()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.transfert.entity.FactureBE[ numbon=" + getNumbon() + " ]";
    }

    public List<BaseTvaRedressement> getBaseTvaRedressement() {
        return baseTvaRedressement;
    }

    public void setBaseTvaRedressement(List<BaseTvaRedressement> baseTvaRedressement) {
        this.baseTvaRedressement = baseTvaRedressement;
    }

//    public void calcul(List<TvaDTO> listTVA) {
//
//        List<BaseTvaRedressement> listeBaseTVA = new ArrayList<BaseTvaRedressement>();
//
////        List<MvtStoBE> TEST = detailFactureBECollection.stream().filter(item-> item.getQuantite().intValue()>0).collect(Collectors.toList());
////        List<MvtStoBE> TEST1=details.stream().filter(item-> item.getQuantite().intValue()<0).collect(Collectors.toList());
////        TEST1.forEach(item->{TEST.add(item);});
////        detailFactureBECollection
//        Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVAForItemsWithPositifQuantity = detailFactureBECollection.stream()
//                .filter(mvtstoBE-> mvtstoBE.getQuantite().compareTo(BigDecimal.ZERO)==1)
//                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
//                        art -> art.getPriuni().multiply(art.getQuantite()), BigDecimal::add)));
//                Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVAForItemsWithNegatifQuantity = detailFactureBECollection.stream()
//                  .filter(mvtstoBE-> mvtstoBE.getQuantite().compareTo(BigDecimal.ZERO)<0)       
//                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
//                        art -> art.getPriuni().multiply(art.getQuantite()), BigDecimal::add)));
//        BigDecimal montantHT = BigDecimal.ZERO;
//        BigDecimal montantTva = BigDecimal.ZERO;
//      
//        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseTVA.entrySet()) {
//            BaseTvaRedressement base = new BaseTvaRedressement();
//            base.setBaseTva(entry.getValue());
//            base.setCodeTva(entry.getKey().getFirst());
//            base.setTauxTva(entry.getKey().getSecond());
//            base.setMontantTva(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)));
//            base.setFactureBE(this);
//            listeBaseTVA.add(base);
//            montantHT = montantHT.add(entry.getValue());
//            montantTva = montantTva.add(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)));
//}
//        for (TvaDTO tva : listTVA) {
//            if (!listeBaseTVA.stream().anyMatch(t -> t.getCodeTva().equals(tva.getCode()))) {
//                BaseTvaRedressement base = new BaseTvaRedressement();
//                base.setBaseTva(BigDecimal.ZERO);
//                base.setCodeTva(tva.getCode());
//                base.setTauxTva(tva.getValeur());
//                base.setMontantTva(BigDecimal.ZERO);
//                base.setFactureBE(this);
//                listeBaseTVA.add(base);
//            }
//        }
//
//        this.setBaseTvaRedressement(listeBaseTVA);
//        this.mntbon = montantHT.add(montantTva);
//
//    }
}
