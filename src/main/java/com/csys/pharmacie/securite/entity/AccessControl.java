/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.securite.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 * @author ERRAYHAN
 */
@Entity
@Table(name = "Access Control")
@NamedQueries({
    @NamedQuery(name = "AccessControl.authentification", query = "SELECT a FROM AccessControl a WHERE a.userName = :userName AND a.passWord = :passWord and a.actif=1 "),
    @NamedQuery(name = "AccessControl.findAll", query = "SELECT a FROM AccessControl a where  a.actif=1"),
    @NamedQuery(name = "AccessControl.findByUserName", query = "SELECT a FROM AccessControl a WHERE a.userName = :userName "),
    @NamedQuery(name = "AccessControl.findByDescription", query = "SELECT a FROM AccessControl a WHERE a.description = :description"),
    @NamedQuery(name = "AccessControl.findByPassWord", query = "SELECT a FROM AccessControl a WHERE a.passWord = :passWord"),
    @NamedQuery(name = "AccessControl.findByGrp", query = "SELECT a FROM AccessControl a WHERE a.grp = :grp")})
public class AccessControl implements Serializable {

//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "actif")
//    private boolean actif;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Actif")
    private boolean actif;

//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "actif")
//    private boolean actif;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "oldgroup")
    private String oldgroup;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "usermodif")
    private String usermodif;
    @Column(name = "date_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModif;
    @Size(max = 2147483647)
    @Column(name = "TraceNotif")
    private String traceNotif;
    @Size(max = 1)
    @Column(name = "Ch_Stat")
    private String chStat;
    @Column(name = "Date_Mod_Pwd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModPwd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CompteExpire")
    private boolean compteExpire;
    @Column(name = "dateExpirationCompte")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateExpirationCompte;
    @Column(name = "Dernier_Date_Cnx")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dernierDateCnx;
    @Basic(optional = false)
    @NotNull
    @Column(name = "expire")
    private boolean expire;
    @Column(name = "nbre_jours_expiration")
    private Integer nbreJoursExpiration;
    @Basic(optional = false)
    @NotNull
    @Column(name = "styleClaire")
    private boolean styleClaire;
    @Column(name = "Valid_Cpt_Rend")
    private Boolean validCptRend;
    @Column(name = "cptShowAllPatient")
    private Boolean cptShowAllPatient;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nature_user_DS")
    private String natureuserDS;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Valid_PH_Nuit")
    private boolean validPHNuit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "Code_Carte")
    private String codeCarte;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Matricule")
    private String matricule;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Old_Grp")
    private String oldGrp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Code_Carte_Minus")
    private String codeCarteMinus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Cledallas")
    private String cledallas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Autorise_av")
    private String autoriseav;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "MatriculeResp")
    private String matriculeResp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Autorise_securite")
    private boolean autorisesecurite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Mat")
    private String mat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Type_user")
    private String typeuser;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "caissier")
//    private boolean caissier;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Admin_Ev_Indesirable")
    private boolean adminEvIndesirable;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Secretaire")
    private boolean secretaire;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cptconsultActivityAll")
    private boolean cptconsultActivityAll;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "codePin")
    private String codePin;
   
    @Size(max = 10)
    @Column(name = "Code_Medecin_Infirmier")
    private String codeMedecinInfirmier;
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 1)
//    @Column(name = "Actif")
//    private String actif;
  

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "UserName")
    private String userName;
    @Size(max = 60)
    @Column(name = "Description")
    private String description;
    @Size(max = 15)
    @Column(name = "PassWord")
    private String passWord;
    @Size(max = 200)
    @Column(name = "Grp")
    private String grp;

    public AccessControl() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getGrp() {
        return grp;
    }

    public void setGrp(String grp) {
        this.grp = grp;
    }

//    public String getMatricule() {
//        return matricule;
//    }
//
//    public void setMatricule(String matricule) {
//        this.matricule = matricule;
//    }
//
//    public String getOldGrp() {
//        return oldGrp;
//    }
//
//    public void setOldGrp(String oldGrp) {
//        this.oldGrp = oldGrp;
//    }

    public String getCodeMedecinInfirmier() {
        return codeMedecinInfirmier;
    }

    public void setCodeMedecinInfirmier(String codeMedecinInfirmier) {
        this.codeMedecinInfirmier = codeMedecinInfirmier;
    }

//    public String getActif() {
//        return actif;
//    }
//
//    public void setActif(String actif) {
//        this.actif = actif;
//    }  

 

    public String getOldgroup() {
        return oldgroup;
    }

    public void setOldgroup(String oldgroup) {
        this.oldgroup = oldgroup;
    }

    public String getUsermodif() {
        return usermodif;
    }

    public void setUsermodif(String usermodif) {
        this.usermodif = usermodif;
    }

    public Date getDateModif() {
        return dateModif;
    }

    public void setDateModif(Date dateModif) {
        this.dateModif = dateModif;
    }

    public String getTraceNotif() {
        return traceNotif;
    }

    public void setTraceNotif(String traceNotif) {
        this.traceNotif = traceNotif;
    }

    public String getChStat() {
        return chStat;
    }

    public void setChStat(String chStat) {
        this.chStat = chStat;
    }

    public Date getDateModPwd() {
        return dateModPwd;
    }

    public void setDateModPwd(Date dateModPwd) {
        this.dateModPwd = dateModPwd;
    }

    public boolean getCompteExpire() {
        return compteExpire;
    }

    public void setCompteExpire(boolean compteExpire) {
        this.compteExpire = compteExpire;
    }

    public Date getDateExpirationCompte() {
        return dateExpirationCompte;
    }

    public void setDateExpirationCompte(Date dateExpirationCompte) {
        this.dateExpirationCompte = dateExpirationCompte;
    }

    public Date getDernierDateCnx() {
        return dernierDateCnx;
    }

    public void setDernierDateCnx(Date dernierDateCnx) {
        this.dernierDateCnx = dernierDateCnx;
    }

    public boolean getExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public Integer getNbreJoursExpiration() {
        return nbreJoursExpiration;
    }

    public void setNbreJoursExpiration(Integer nbreJoursExpiration) {
        this.nbreJoursExpiration = nbreJoursExpiration;
    }

    public boolean getStyleClaire() {
        return styleClaire;
    }

    public void setStyleClaire(boolean styleClaire) {
        this.styleClaire = styleClaire;
    }

    public Boolean getValidCptRend() {
        return validCptRend;
    }

    public void setValidCptRend(Boolean validCptRend) {
        this.validCptRend = validCptRend;
    }

    public Boolean getCptShowAllPatient() {
        return cptShowAllPatient;
    }

    public void setCptShowAllPatient(Boolean cptShowAllPatient) {
        this.cptShowAllPatient = cptShowAllPatient;
    }

    public String getNatureuserDS() {
        return natureuserDS;
    }

    public void setNatureuserDS(String natureuserDS) {
        this.natureuserDS = natureuserDS;
    }

    public boolean getValidPHNuit() {
        return validPHNuit;
    }

    public void setValidPHNuit(boolean validPHNuit) {
        this.validPHNuit = validPHNuit;
    }

    public String getCodeCarte() {
        return codeCarte;
    }

    public void setCodeCarte(String codeCarte) {
        this.codeCarte = codeCarte;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getOldGrp() {
        return oldGrp;
    }

    public void setOldGrp(String oldGrp) {
        this.oldGrp = oldGrp;
    }

    public String getCodeCarteMinus() {
        return codeCarteMinus;
    }

    public void setCodeCarteMinus(String codeCarteMinus) {
        this.codeCarteMinus = codeCarteMinus;
    }

    public String getCledallas() {
        return cledallas;
    }

    public void setCledallas(String cledallas) {
        this.cledallas = cledallas;
    }

    public String getAutoriseav() {
        return autoriseav;
    }

    public void setAutoriseav(String autoriseav) {
        this.autoriseav = autoriseav;
    }

    public String getMatriculeResp() {
        return matriculeResp;
    }

    public void setMatriculeResp(String matriculeResp) {
        this.matriculeResp = matriculeResp;
    }

    public boolean getAutorisesecurite() {
        return autorisesecurite;
    }

    public void setAutorisesecurite(boolean autorisesecurite) {
        this.autorisesecurite = autorisesecurite;
    }

    public String getMat() {
        return mat;
    }

    public void setMat(String mat) {
        this.mat = mat;
    }

    public String getTypeuser() {
        return typeuser;
    }

    public void setTypeuser(String typeuser) {
        this.typeuser = typeuser;
    }

//    public boolean getCaissier() {
//        return caissier;
//    }
//
//    public void setCaissier(boolean caissier) {
//        this.caissier = caissier;
//    }

    public boolean getAdminEvIndesirable() {
        return adminEvIndesirable;
    }

    public void setAdminEvIndesirable(boolean adminEvIndesirable) {
        this.adminEvIndesirable = adminEvIndesirable;
    }

    public boolean getSecretaire() {
        return secretaire;
    }

    public void setSecretaire(boolean secretaire) {
        this.secretaire = secretaire;
    }

    public boolean getCptconsultActivityAll() {
        return cptconsultActivityAll;
    }

    public void setCptconsultActivityAll(boolean cptconsultActivityAll) {
        this.cptconsultActivityAll = cptconsultActivityAll;
    }

    public String getCodePin() {
        return codePin;
    }

    public void setCodePin(String codePin) {
        this.codePin = codePin;
    }

    public boolean getActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

//    public boolean getActif() {
//        return actif;
//    }
//
//    public void setActif(boolean actif) {
//        this.actif = actif;
//    }

}
