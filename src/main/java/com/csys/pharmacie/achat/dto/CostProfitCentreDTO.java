package com.csys.pharmacie.achat.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CostProfitCentreDTO {

   private Integer code;

   @NotNull
   @Size(
           min = 1,
           max = 20
   )
   private String codeSaisie;

   @NotNull
   @Size(
           min = 1,
           max = 200
   )
   private String designationAr;

   @NotNull
   @Size(
           min = 1,
           max = 200
   )
   private String designation;

   public Integer getCode() {
       return code;
   }

   public void setCode(Integer code) {
       this.code = code;
   }

   public String getCodeSaisie() {
       return codeSaisie;
   }

   public void setCodeSaisie(String codeSaisie) {
       this.codeSaisie = codeSaisie;
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
}