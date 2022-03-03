/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.transfert.domain.DetailMvtStoBT;
import com.csys.pharmacie.transfert.domain.MvtStoBE;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Farouk
 */
@Entity
@Table(name = "depsto")
@Audited
@AuditTable("depsto_AUD")
//@NamedQueries({
//    //    @NamedQuery(name = "Depsto.findQteArtInDep", query = "SELECT SUM(d.qte) FROM Depsto d WHERE d.codart.codart = :codart and d.coddep= :coddep")
//    //    ,
//   
//    @NamedQuery(name = "Depsto.findByDatPer", query = "SELECT d FROM Depsto d WHERE d.datPer = :datPer"),
//
//    @NamedQuery(name = "Depsto.findByLotsaisie", query = "SELECT d FROM Depsto d WHERE d.lotInter = :lotInter")})
public class Depsto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "coddep")
    private Integer coddep;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codart")
    private Integer codart;
//    @Size(max = 17)
//    @Column(name = "lot")
//    private String lot;
    @Size(max = 6)
    @Column(name = "numordre")
    private String numordre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "qte")
    private BigDecimal qte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PU")
    private BigDecimal pu;
    @Column(name = "stkdep")
    private BigDecimal stkdep;
    @Column(name = "stkrel")
    private BigDecimal stkrel;
//    @Column(name = "priinv")
//    private BigDecimal priinv;
    @Column(name = "qte0")
    private BigDecimal qte0;
    @Column(name = "stkdep0")
    private BigDecimal stkdep0;
//    @Column(name = "stkrel0")
//    private BigDecimal stkrel0;
    @Column(name = "DatPer")
    private LocalDate datPer;
    @Column(name = "datesys")
    private LocalDateTime datesys;
//    @Column(name = "acqte")
//    private BigDecimal acqte;
    @Column(name = "StkFix")
    private BigDecimal stkFix;
//    @Basic(optional = false)
//    @Column(name = "NUM")
//    private Long num;
//    @Column(name = "PACH")
//    private BigDecimal pach;
//    @Column(name = "Poids")
//    private BigDecimal poids;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NumBon")
    private String numBon;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @Column(name = "memo")
    private String memo;
    @Size(max = 50)
    @Column(name = "Reference")
    private String reference;
//    @Column(name = "Cocher1")
//    private Character cocher1;
//    @Column(name = "Cocher2")
//    private Character cocher2;
//    @Column(name = "Cocher3")
//    private Character cocher3;
//    @Size(max = 1)
//    @Column(name = "Exist")
//    private String exist;
//    @Column(name = "StkRel1")
//    private BigDecimal stkRel1;
//    @Column(name = "StkRel2")
//    private BigDecimal stkRel2;
//    @Column(name = "StkRel3")
//    private BigDecimal stkRel3;
    @Column(name = "A_Inventorier")
    private Boolean aInventorier;
//    @Size(max = 30)
//    @Column(name = "Lot_Frs")
//    private String lotFrs;
    @Size(max = 30)
    @Column(name = "Identifiant")
    private String identifiant;
//    @Size(max = 40)
//    @Column(name = "NumSerie")
//    private String numSerie;
    @Size(max = 50)
    @Column(name = "Lot_Inter")
    private String lotInter;
//    @Size(max = 50)
//    @Column(name = "Numero_AMC")
//    private String numeroAMC;
//    @Column(name = "StkRel_inter")
//    private BigDecimal stkRelinter;
//    @Column(name = "Defectueux")
//    private Boolean defectueux;
//    @Column(name = "Qte0_T")
//    private Long qte0T;
    @Size(max = 50)
    @Column(name = "UserInv")
    private String userInv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unite")
    private Integer unite;
    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "categ_depot")
    private CategorieDepotEnum categDepot;
    @Column(name = "code_emplacement")
    private Integer codeEmplacement;
    @Transient
    private BigDecimal prixHT;

    @Column(name = "code_tva")
    private Integer codeTva;

    @Column(name = "taux_tva")
    private BigDecimal tauxTva;

    @Size(min = 0, max = 20)
    @Column(name = "numbon_origin")
    private String numBonOrigin;
    
    @Size(min = 0, max = 20)
    @Column(name = "numbon_before_inventaire")
    private String numBonBeforeInventaire;
         @Column(name = "is_capitalize")
    private Boolean isCapitalize;
    @Version
    private Integer version;

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public LocalDateTime getDatesys() {
        return datesys;
    }

    public void setDatesys(LocalDateTime datesys) {
        this.datesys = datesys;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @PrePersist
    public void prePersist() {
        this.datesys = LocalDateTime.now();
    }

    public Depsto() {
    }

    public Depsto(BigDecimal qte, BigDecimal stkrel) {
        this.qte = qte;
        this.stkrel = stkrel;
    }

    public Depsto(BigDecimal qte) {
        this.qte = qte;
    }

// modifier
    public Depsto(MvtStoBE mvtStoBE) {
        this.coddep = mvtStoBE.getFactureBE().getCoddep();
        this.numBon = mvtStoBE.getNumbon();
        this.codart = mvtStoBE.getCodart();
        this.qte = mvtStoBE.getQuantite();
        this.pu = mvtStoBE.getPriuni();
        this.lotInter = mvtStoBE.getLotinter();
        this.datPer = mvtStoBE.getDatPer();
        this.unite = mvtStoBE.getUnite();
        this.categDepot = mvtStoBE.getCategDepot();
        this.codeTva = mvtStoBE.getCodtva();
        this.tauxTva = mvtStoBE.getTautva();
    }

    public Depsto(DetailDecoupage mvtStoBE) {
        this.coddep = mvtStoBE.getDecoupage().getCoddep();
        this.numBon = mvtStoBE.getCodeDecoupage();
        this.codart = mvtStoBE.getCodart();
        this.qte = mvtStoBE.getQuantiteObtenue();
        this.lotInter = mvtStoBE.getLotInter();
        this.datPer = mvtStoBE.getDatePeremption();
        this.unite = mvtStoBE.getUniteFinal();
        this.categDepot = mvtStoBE.getCategDepot();
    }

    public Depsto(Integer code, Integer codart, BigDecimal qte, BigDecimal pu) {
        this.code = code;
        this.codart = codart;
        this.qte = qte;
        this.pu = pu;
    }

    public Depsto(Integer code, Integer codart, BigDecimal qte, BigDecimal pu, Integer unite) {
        this.code = code;
        this.codart = codart;
        this.qte = qte;
        this.pu = pu;
        this.unite = unite;
    }

    public Depsto(Integer codart, LocalDate datPer, String lotInter) {
        this.codart = codart;
        this.datPer = datPer;
        this.lotInter = lotInter;
    }

    public Depsto(Depsto depsto) {

        this.coddep = depsto.coddep;
        this.codart = depsto.codart;
//        this.lot = depsto.lot;
        this.numordre = depsto.numordre;
        this.qte = depsto.qte;
        this.pu = depsto.pu;
        this.stkdep = depsto.stkdep;
        this.stkrel = depsto.stkrel;
//        this.priinv = depsto.priinv;
        this.qte0 = depsto.qte0;
        this.stkdep0 = depsto.stkdep0;
//        this.stkrel0 = depsto.stkrel0;
        this.datPer = depsto.datPer;
        this.datesys = depsto.datesys;
//        this.acqte = depsto.acqte;
        this.stkFix = depsto.stkFix;
//        this.num = depsto.num;
//        this.pach = depsto.pach;
//        this.poids = depsto.poids;
        this.numBon = depsto.numBon;
        this.reference = depsto.reference;
//        this.cocher1 = depsto.cocher1;
//        this.cocher2 = depsto.cocher2;
//        this.cocher3 = depsto.cocher3;
//        this.exist = depsto.exist;
//        this.stkRel1 = depsto.stkRel1;
//        this.stkRel2 = depsto.stkRel2;
//        this.stkRel3 = depsto.stkRel3;
        this.aInventorier = depsto.aInventorier;
//        this.lotFrs = depsto.lotFrs;
        this.identifiant = depsto.identifiant;
//        this.numSerie = depsto.numSerie;
        this.lotInter = depsto.lotInter;
       
//        this.stkRelinter = depsto.stkRelinter;
//        this.defectueux = depsto.defectueux;
//        this.qte0T = depsto.qte0T;
        this.userInv = depsto.userInv;
        this.unite = depsto.unite;
        this.categDepot = depsto.categDepot;
        this.prixHT = depsto.prixHT;
        this.tauxTva = depsto.tauxTva;
        this.codeTva = depsto.codeTva;
    }

    public Depsto(Integer coddep, Integer codart, BigDecimal qte, BigDecimal pu, BigDecimal stkrel, LocalDate datPer, LocalDateTime datesys, String lotInter, Integer unite, CategorieDepotEnum categDepot, String numBon) {
        this.coddep = coddep;
        this.codart = codart;
        this.qte = qte;
        this.pu = pu;
        this.stkrel = stkrel;
        this.datPer = datPer;
        this.datesys = datesys;
        this.lotInter = lotInter;
        this.unite = unite;
        this.numBon = numBon;
        this.categDepot = categDepot;
    }

    public Depsto(Integer coddep, Integer codart, BigDecimal qte, BigDecimal qte0, LocalDateTime datesys, String numBon, Integer unite, CategorieDepotEnum categDepot) {
        this.coddep = coddep;
        this.codart = codart;
        this.qte = qte;
        this.qte0 = qte0;
        this.datesys = datesys;
        this.numBon = numBon;
        this.unite = unite;
        this.categDepot = categDepot;
    }

    public Depsto(DetailMvtStoBT detailMvtStoBT) {
        this.qte = detailMvtStoBT.getQuantitePrelevee();
        this.pu = detailMvtStoBT.getPriuni();
        this.stkrel = detailMvtStoBT.getStkRel();
        this.datPer = detailMvtStoBT.getDatPer();
        this.datesys = detailMvtStoBT.getDatSYS();
        this.lotInter = detailMvtStoBT.getLotinter();
        this.unite = detailMvtStoBT.getUnite();
        this.codeTva = detailMvtStoBT.getCodeTva();
        this.tauxTva = detailMvtStoBT.getTauxTva();
    }

    public Depsto(Integer coddep, Integer codart, LocalDate datPer, String numBon, String lotInter, Integer unite, CategorieDepotEnum categDepot, Integer codeTva, BigDecimal tauxTva) {
        this.coddep = coddep;
        this.codart = codart;
        this.datPer = datPer;
        this.numBon = numBon;
        this.lotInter = lotInter;
        this.unite = unite;
        this.categDepot = categDepot;
        this.codeTva = codeTva;
        this.tauxTva = tauxTva;
    }
    
    
    public Depsto(Integer coddep, Integer codart, Integer unite, BigDecimal qte, BigDecimal qte0) {
        this.coddep = coddep;
        this.codart = codart;
        this.unite = unite;
        this.qte = qte;
        this.qte0 = qte0;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getaInventorier() {
        return aInventorier;
    }

    public void setaInventorier(Boolean aInventorier) {
        this.aInventorier = aInventorier;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public BigDecimal getPrixHT() {
        return prixHT;
    }

    public void setPrixHT(BigDecimal prixHT) {
        this.prixHT = prixHT;
    }

//    public Depsto(Long num) {
//        this.num = num;
//    }
//
//    public Long getNum() {
//        return num;
//    }
//
//    public void setNum(Long num) {
//        this.num = num;
//    }

//    public String getLot() {
//        return lot;
//    }
//
//    public void setLot(String lot) {
//        this.lot = lot;
//    }

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public BigDecimal getStkdep() {
        return stkdep;
    }

    public void setStkdep(BigDecimal stkdep) {
        this.stkdep = stkdep;
    }

    public BigDecimal getStkrel() {
        return stkrel;
    }

    public void setStkrel(BigDecimal stkrel) {
        this.stkrel = stkrel;
    }

//    public BigDecimal getPriinv() {
//        return priinv;
//    }
//
//    public void setPriinv(BigDecimal priinv) {
//        this.priinv = priinv;
//    }

    public BigDecimal getQte0() {
        return qte0;
    }

    public void setQte0(BigDecimal qte0) {
        this.qte0 = qte0;
    }

    public BigDecimal getStkdep0() {
        return stkdep0;
    }

    public void setStkdep0(BigDecimal stkdep0) {
        this.stkdep0 = stkdep0;
    }

//    public BigDecimal getStkrel0() {
//        return stkrel0;
//    }
//
//    public void setStkrel0(BigDecimal stkrel0) {
//        this.stkrel0 = stkrel0;
//    }

//    public BigDecimal getAcqte() {
//        return acqte;
//    }
//
//    public void setAcqte(BigDecimal acqte) {
//        this.acqte = acqte;
//    }
//
//  
//
//  
//
//    public String getExist() {
//        return exist;
//    }
//
//    public void setExist(String exist) {
//        this.exist = exist;
//    }
//
//    public BigDecimal getPach() {
//        return pach;
//    }
//
//    public void setPach(BigDecimal pach) {
//        this.pach = pach;
//    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

//    public String getLotFrs() {
//        return lotFrs;
//    }
//
//    public void setLotFrs(String lotFrs) {
//        this.lotFrs = lotFrs;
//    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

//    public String getNumSerie() {
//        return numSerie;
//    }
//
//    public void setNumSerie(String numSerie) {
//        this.numSerie = numSerie;
//    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

//    public String getNumeroAMC() {
//        return numeroAMC;
//    }
//
//    public void setNumeroAMC(String numeroAMC) {
//        this.numeroAMC = numeroAMC;
//    }

//    public BigDecimal getStkRelinter() {
//        return stkRelinter;
//    }
//
//    public void setStkRelinter(BigDecimal stkRelinter) {
//        this.stkRelinter = stkRelinter;
//    }

//    public Long getQte0T() {
//        return qte0T;
//    }
//
//    public void setQte0T(Long qte0T) {
//        this.qte0T = qte0T;
//    }

    public BigDecimal getStkFix() {
        return stkFix;
    }

    public void setStkFix(BigDecimal stkFix) {
        this.stkFix = stkFix;
    }

//    public BigDecimal getPoids() {
//        return poids;
//    }
//
//    public void setPoids(BigDecimal poids) {
//        this.poids = poids;
//    }
//
//    public Boolean getDefectueux() {
//        return defectueux;
//    }
//
//    public void setDefectueux(Boolean defectueux) {
//        this.defectueux = defectueux;
//    }

    public String getUserInv() {
        return userInv;
    }

    public void setUserInv(String userInv) {
        this.userInv = userInv;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public Integer getCodeEmplacement() {
        return codeEmplacement;
    }

    public void setCodeEmplacement(Integer codeEmplacement) {
        this.codeEmplacement = codeEmplacement;
    }

    public Integer getVersion() {
        return version;
    }

    public String getNumBonOrigin() {
        return numBonOrigin;
    }

    public void setNumBonOrigin(String numBonOrigin) {
        this.numBonOrigin = numBonOrigin;
    }

    public String getNumBonBeforeInventaire() {
        return numBonBeforeInventaire;
    }

    public void setNumBonBeforeInventaire(String numBonBeforeInventaire) {
        this.numBonBeforeInventaire = numBonBeforeInventaire;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsCapitalize() {
        return isCapitalize;
    }

    public void setIsCapitalize(Boolean isCapitalize) {
        this.isCapitalize = isCapitalize;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.code);
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
        final Depsto other = (Depsto) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "Depsto{" + "code=" + code + ", coddep=" + coddep + ", codart=" + codart + ", qte=" + qte + ", pu=" + pu + ", datPer=" + datPer + ", lotInter=" + lotInter + ", unite=" + unite + ", codeTva=" + codeTva + ", tauxTva=" + tauxTva +  '}';
//    }

    @Override
    public String toString() {
        return "Depsto{" + "code=" + code + ", coddep=" + coddep + ", codart=" + codart  + ", numordre=" + numordre + ", qte=" + qte + ", pu=" + pu + ", stkdep=" + stkdep + ", stkrel=" + stkrel  + ", qte0=" + qte0 + ", datPer=" + datPer + ", datesys=" + datesys + ", stkFix=" + stkFix + ", numBon=" + numBon + ", memo=" + memo + ", reference=" + reference + ", aInventorier=" + aInventorier  + ", identifiant=" + identifiant + ", lotInter=" + lotInter + ", userInv=" + userInv + ", unite=" + unite + ", categDepot=" + categDepot + ", codeEmplacement=" + codeEmplacement + ", prixHT=" + prixHT + ", codeTva=" + codeTva + ", tauxTva=" + tauxTva + ", numBonOrigin=" + numBonOrigin + ", numBonBeforeInventaire=" + numBonBeforeInventaire + ", isCapitalize=" + isCapitalize + ", version=" + version + '}';
    }


  

   
    


}
