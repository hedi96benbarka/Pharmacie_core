/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import com.sun.jersey.core.util.Base64;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

//import com.sun.jersey.core.util.Base64;
public class CliniqueDto {

    private Integer code;
    private String logo;
    private String nomClinique;
    private String nomCliniqueAr;
    private String imagePath;
    private Blob logoClinique;
        private Integer codeSite;
    private Integer codeCompany;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNomClinique() {
        return nomClinique;
    }

    public void setNomClinique(String nomClinique) {
        this.nomClinique = nomClinique;
    }

    public String getNomCliniqueAr() {
        return nomCliniqueAr;
    }

    public void setNomCliniqueAr(String nomCliniqueAr) {
        this.nomCliniqueAr = nomCliniqueAr;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setLogoClinique() throws SerialException, SQLException {
        this.logoClinique = new SerialBlob(Base64.decode(logo));
    }

    public Blob getLogoClinique() throws SerialException, SQLException {
        return logoClinique;
    }

    public Integer getCodeSite() {
        return codeSite;
    }

    public void setCodeSite(Integer codeSite) {
        this.codeSite = codeSite;
    }

    public Integer getCodeCompany() {
        return codeCompany;
    }

    public void setCodeCompany(Integer codeCompany) {
        this.codeCompany = codeCompany;
    }

    @Override
    public String toString() {
        return "CliniqueDto{" + "code=" + code + ", logo=" + logo + ", nomClinique=" + nomClinique + ", nomCliniqueAr=" + nomCliniqueAr + ", imagePath=" + imagePath + '}';
    }

}
