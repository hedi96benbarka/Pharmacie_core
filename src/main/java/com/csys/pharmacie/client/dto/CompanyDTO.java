package com.csys.pharmacie.client.dto;



import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CompanyDTO {

    @NotNull
    private Integer code;

    @Size( min = 0,max = 200)
    private String designationAr;

    @Size( min = 0,max = 200)
    private String designation;


    @Size( min = 0,max = 200)
    private String ipAdress;

    @NotNull
    private boolean actif;

    private Boolean save;

    private Boolean ping;
    
    private Boolean health;
    
  


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesignationAr() {
        return designationAr;
    }

    public void setDesignationAr(String designationAr) {
        this.designationAr = designationAr;
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

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Boolean getHealth() {
        return health;
    }

    public void setHealth(Boolean health) {
        this.health = health;
    }

   

}
