package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.helper.BaseTVADTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FactureBEDTO {

    private BigDecimal mntbon;

    @Size(min = 0, max = 250 )
    private String memop;

    private Integer coddep;

    private String CodeSaisiDepot;
    
    private String designationDepot;

    private Integer codeMotifRedressement;
    private String designationMotifRedressement;

    @Valid
    @NotNull
    private List<MvtStoBEDTO> details;

    @Size(min = 1, max = 20)
    private String numbon;

    @Size(min = 0, max = 20)
    private String codvend;

    private LocalDateTime datbon;

    private TypeBonEnum typbon;

    @Size(min = 0, max = 16)
    private String numaffiche;

    private CategorieDepotEnum categDepot;
    
    private List<BaseTVADTO> basesTVA;
    
    private Integer numeroDemande;
    private Date datbonEdition;

    public FactureBEDTO(Integer coddep, Integer codeMotifRedressement, List<MvtStoBEDTO> details, CategorieDepotEnum categDepot) {
        this.coddep = coddep;
        this.codeMotifRedressement = codeMotifRedressement;
        this.details = details;
        this.categDepot = categDepot;
    }

    public FactureBEDTO() {
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

    public Integer getCodeMotifRedressement() {
        return codeMotifRedressement;
    }

    public void setCodeMotifRedressement(Integer codeMotifRedressement) {
        this.codeMotifRedressement = codeMotifRedressement;
    }
 
    public List<MvtStoBEDTO> getDetails() {
        return details;
    }

    public void setDetails(List<MvtStoBEDTO> detailFactureBECollection) {
        this.details = detailFactureBECollection;
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

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public String getDesignationMotifRedressement() {
        return designationMotifRedressement;
    }

    public void setDesignationMotifRedressement(String designationMotifRedressement) {
        this.designationMotifRedressement = designationMotifRedressement;
    }

    public String getCodeSaisiDepot() {
        return CodeSaisiDepot;
    }

    public void setCodeSaisiDepot(String CodeSaisiDepot) {
        this.CodeSaisiDepot = CodeSaisiDepot;
    }

    public List<BaseTVADTO> getBasesTVA() {
        return basesTVA;
    }

    public void setBasesTVA(List<BaseTVADTO> basesTVA) {
        this.basesTVA = basesTVA;
    }

    public Integer getNumeroDemande() {
        return numeroDemande;
    }

    public void setNumeroDemande(Integer numeroDemande) {
        this.numeroDemande = numeroDemande;
    }

    public Date getDatbonEdition() {
        return datbonEdition;
    }

    public void setDatbonEdition(Date datbonEdition) {
        this.datbonEdition = datbonEdition;
    }
       
   @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.numbon);
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
        final FactureBEDTO other = (FactureBEDTO) obj;
        if (!Objects.equals(this.numbon, other.numbon)) {
            return false;
        }
        return true;
    }
}
