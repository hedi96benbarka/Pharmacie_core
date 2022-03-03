package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.transfert.domain.FactureBT;
import com.csys.pharmacie.transfert.domain.TransfertDetailDTRPK;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransfertDetailDTRDTO {

    private Integer codedetailDTR;

    private String codeTransfert;

    @NotNull
    private Integer codeDTR;

    private BigDecimal qteTransferred;

    private FactureBT reception;

    public Integer getCodeDTR() {
        return codeDTR;
    }

    public void setCodeDTR(Integer codeDTR) {
        this.codeDTR = codeDTR;
    }

    public BigDecimal getQteTransferred() {
        return qteTransferred;
    }

    public void setQteTransferred(BigDecimal qteTransferred) {
        this.qteTransferred = qteTransferred;
    }

    public FactureBT getReception() {
        return reception;
    }

    public void setReception(FactureBT reception) {
        this.reception = reception;
    }

    public TransfertDetailDTRDTO(Integer codeDTR, BigDecimal qteTransferred, FactureBT reception) {
        this.codeDTR = codeDTR;
        this.qteTransferred = qteTransferred;
        this.reception = reception;
    }

    public TransfertDetailDTRDTO(Integer codedetailDTR, BigDecimal qteTransferred, Integer codeDTR) {
        this.codedetailDTR = codedetailDTR;
        this.qteTransferred = qteTransferred;
        this.codeDTR = codeDTR;
    }

    public Integer getCodedetailDTR() {
        return codedetailDTR;
    }

    public void setCodedetailDTR(Integer codedetailDTR) {
        this.codedetailDTR = codedetailDTR;
    }

    public String getCodeTransfert() {
        return codeTransfert;
    }

    public void setCodeTransfert(String codeTransfert) {
        this.codeTransfert = codeTransfert;
    }

    public TransfertDetailDTRDTO() {
    }

}
