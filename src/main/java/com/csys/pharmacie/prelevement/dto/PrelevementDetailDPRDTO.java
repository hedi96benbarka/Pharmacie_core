package com.csys.pharmacie.prelevement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrelevementDetailDPRDTO {

    private Integer codedetailDPR;

    private String codePrelevment;

    private BigDecimal quantite_prelevee;
    private Integer codeDPR;

    public PrelevementDetailDPRDTO() {
    }

    public Integer getCodedetailDPR() {
        return codedetailDPR;
    }

    public void setCodedetailDPR(Integer codedetailDPR) {
        this.codedetailDPR = codedetailDPR;
    }

    public String getCodePrelevment() {
        return codePrelevment;
    }

    public void setCodePrelevment(String codePrelevment) {
        this.codePrelevment = codePrelevment;
    }

    public PrelevementDetailDPRDTO(Integer codedetailDPR, BigDecimal quantite_prelevee, Integer codeDPR) {
        this.codedetailDPR = codedetailDPR;
        this.quantite_prelevee = quantite_prelevee;
        this.codeDPR = codeDPR;
    }

    public BigDecimal getQuantite_prelevee() {
        return quantite_prelevee;
    }

    public void setQuantite_prelevee(BigDecimal quantite_prelevee) {
        this.quantite_prelevee = quantite_prelevee;
    }

    public Integer getCodeDPR() {
        return codeDPR;
    }

    public void setCodeDPR(Integer codeDPR) {
        this.codeDPR = codeDPR;
    }

}
