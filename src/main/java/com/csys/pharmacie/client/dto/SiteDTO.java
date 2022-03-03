package com.csys.pharmacie.client.dto;




import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class SiteDTO {

    @NotNull
    private Integer code;

    @Size(  min = 0, max = 200)
    private String designationAr;

    @Size( min = 0, max = 200)
    private String designation;


    @Size(min = 0,max = 200)
    private String ipAdress;
    
    private Integer codeCompany;

    private Boolean save;

    private Boolean ping;
    
    private Boolean health;
    
    private Boolean isHead;
        
    private Boolean actif;
   
    
    private String typeSite;

    public Integer getCodeCompany() {
        return codeCompany;
    }

    public void setCodeCompany(Integer codeCompany) {
        this.codeCompany = codeCompany;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSave() {
        return save;
    }

    public void setSave(Boolean save) {
        this.save = save;
    }

    public Boolean getPing() {
        return ping;
    }

    public void setPing(Boolean ping) {
        this.ping = ping;
    }

    public String getDesignationAr() {
        return designationAr;
    }

    public void setDesignationAr(String designationAr) {
        this.designationAr = designationAr;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }


    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public Boolean getHealth() {
        return health;
    }

    public void setHealth(Boolean health) {
        this.health = health;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
    

    public Boolean getIsHead() {
        return isHead;
    }

    public void setIsHead(Boolean isHead) {
        this.isHead = isHead;
    }

   

 

    public String getTypeSite() {
        return typeSite;
    }

    public void setTypeSite(String typeSite) {
        this.typeSite = typeSite;
    }
      @Override
    public String toString() {
        return "SiteDTO{" + "code=" + code + ", designationAr=" + designationAr + ", ipAdress=" + ipAdress + ", codeCompany=" + codeCompany + ", save=" + save + ", ping=" + ping + ", health=" + health + ", isHead=" + isHead + ", actif=" + actif +  ", typeSite=" + typeSite + '}';
    }    
}
