/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;


import com.csys.pharmacie.helper.MethodeTraitementdeStockEnum;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ERRAYHAN
 */
public class DepotDTO {

    @NotNull
    private boolean bTDuParDatePer;

    @Size(min = 1, max = 30)
    private String userOuv;

    private Date dateHeureOuv;

    private boolean inventaireAvecSCANPAL;

    @NotNull
    @Size(min = 1, max = 10)
    private MethodeTraitementdeStockEnum methodeTraitementdeStock;

    @NotNull
    private boolean ouvrenouvex;

    @NotNull
    private boolean valoriser;

    @NotNull
    private Character vldCge1;

    @NotNull
    private Character vldCge2;

    @NotNull
    private boolean stup;

    @NotNull
    private boolean aControler;

    @NotNull
    private boolean orthopedie;

    @NotNull
    private boolean depotFrs;

    @NotNull
    @Size(min = 1, max = 20)
    private String codFrs;

    @NotNull
    @Size(min = 1, max = 3)
    private Integer code;

    @Size(max = 40)
    private String designation;

    @Size(max = 40)
    private String designationSec;

    private MaquetteDto maquette;
    
    private String codeSaisi;
    
    private Boolean perime;
    
    private List<NatureDepotDTO> natureDepot;
    
     private Integer codeCostCenterDeparement;

    private Boolean isCapitalize;
    public DepotDTO(Integer coddep) {
        this.code = coddep;
    }

    public DepotDTO() {
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    
    
    public boolean isbTDuParDatePer() {
        return bTDuParDatePer;
    }

    public void setbTDuParDatePer(boolean bTDuParDatePer) {
        this.bTDuParDatePer = bTDuParDatePer;
    }

    public boolean isaControler() {
        return aControler;
    }

    public void setaControler(boolean aControler) {
        this.aControler = aControler;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignationSec() {
        return designationSec;
    }

    public void setDesignationSec(String designationSec) {
        this.designationSec = designationSec;
    }

    public String getDesdep() {
        return designation;
    }

    public void setDesdep(String desdep) {
        this.designation = desdep;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof DepotDTO)) {
            return false;
        }
        DepotDTO other = (DepotDTO) object;
        if ((this.code == null && other.code != null)
                || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.achat.model.pharmacie.Depot[ coddep=" + code + " ]";
    }

    public boolean getOuvrenouvex() {
        return ouvrenouvex;
    }

    public void setOuvrenouvex(boolean ouvrenouvex) {
        this.ouvrenouvex = ouvrenouvex;
    }

    public boolean getValoriser() {
        return valoriser;
    }

    public void setValoriser(boolean valoriser) {
        this.valoriser = valoriser;
    }

    public Character getVldCge1() {
        return vldCge1;
    }

    public void setVldCge1(Character vldCge1) {
        this.vldCge1 = vldCge1;
    }

    public Character getVldCge2() {
        return vldCge2;
    }

    public void setVldCge2(Character vldCge2) {
        this.vldCge2 = vldCge2;
    }

    public boolean getStup() {
        return stup;
    }

    public void setStup(boolean stup) {
        this.stup = stup;
    }

    public boolean getAControler() {
        return aControler;
    }

    public void setAControler(boolean aControler) {
        this.aControler = aControler;
    }

    public boolean getOrthopedie() {
        return orthopedie;
    }

    public void setOrthopedie(boolean orthopedie) {
        this.orthopedie = orthopedie;
    }

    public boolean getDepotFrs() {
        return depotFrs;
    }

    public void setDepotFrs(boolean depotFrs) {
        this.depotFrs = depotFrs;
    }

    public String getCodFrs() {
        return codFrs;
    }

    public void setCodFrs(String codFrs) {
        this.codFrs = codFrs;
    }

    public String getUserOuv() {
        return userOuv;
    }

    public void setUserOuv(String userOuv) {
        this.userOuv = userOuv;
    }

    public Date getDateHeureOuv() {
        return dateHeureOuv;
    }

    public void setDateHeureOuv(Date dateHeureOuv) {
        this.dateHeureOuv = dateHeureOuv;
    }

    public boolean getInventaireAvecSCANPAL() {
        return inventaireAvecSCANPAL;
    }

    public void setInventaireAvecSCANPAL(boolean inventaireAvecSCANPAL) {
        this.inventaireAvecSCANPAL = inventaireAvecSCANPAL;
    }

    public MethodeTraitementdeStockEnum getMethodeTraitementdeStock() {
        return methodeTraitementdeStock;
    }

    public void setMethodeTraitementdeStock(MethodeTraitementdeStockEnum methodeTraitementdeStock) {
        this.methodeTraitementdeStock = methodeTraitementdeStock;
    }

    public boolean getBTDuParDatePer() {
        return bTDuParDatePer;
    }

    public void setBTDuParDatePer(boolean bTDuParDatePer) {
        this.bTDuParDatePer = bTDuParDatePer;
    }

    public MaquetteDto getMaquette() {
        return maquette;
    }

    public void setMaquette(MaquetteDto maquette) {
        this.maquette = maquette;
    }

    public List<NatureDepotDTO> getNatureDepot() {
        return natureDepot;
    }

    public void setNatureDepot(List<NatureDepotDTO> natureDepot) {
        this.natureDepot = natureDepot;
    }

    public Boolean getPerime() {
        return perime;
    }

    public void setPerime(Boolean perime) {
        this.perime = perime;
    }

    public Integer getCodeCostCenterDeparement() {
        return codeCostCenterDeparement;
    }

    public void setCodeCostCenterDeparement(Integer codeCostCenterDeparement) {
        this.codeCostCenterDeparement = codeCostCenterDeparement;
    }

    public Boolean getIsCapitalize() {
        return isCapitalize;
    }

    public void setIsCapitalize(Boolean isCapitalize) {
        this.isCapitalize = isCapitalize;
    }
    

}
