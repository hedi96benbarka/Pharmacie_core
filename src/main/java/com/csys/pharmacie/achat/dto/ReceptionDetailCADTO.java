package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReceptionDetailCADTO {

    @NotNull @Size(min = 1, max = 10)
    private String reception;
    @NotNull
    private Integer purchaseOrder;
    @NotNull
    private Integer article;
    @NotNull
    private BigDecimal receivedQuantity;
    
    private BigDecimal freeReceivedQuantity;

    

    public ReceptionDetailCADTO(Integer purchaseOrder, Integer article, BigDecimal receivedQuantity) {
        this.purchaseOrder = purchaseOrder;
        this.article = article;
        this.receivedQuantity = receivedQuantity;
    }

    public ReceptionDetailCADTO(Integer purchaseOrder, Integer article, BigDecimal receivedQuantity, BigDecimal freeReceivedQuantity) {
        this.purchaseOrder = purchaseOrder;
        this.article = article;
        this.receivedQuantity = receivedQuantity;
        this.freeReceivedQuantity = freeReceivedQuantity;
    }
 

    
    
    public ReceptionDetailCADTO() {
    }

    public BigDecimal getFreeReceivedQuantity() {
        return freeReceivedQuantity;
    }

    public void setFreeReceivedQuantity(BigDecimal freeReceivedQuantity) {
        this.freeReceivedQuantity = freeReceivedQuantity;
    }

 
    public Integer getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(Integer purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public BigDecimal getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(BigDecimal receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public String getReception() {
        return reception;
    }

    public void setReception(String reception) {
        this.reception = reception;
    }

    public Integer getArticle() {
        return article;
    }

    public void setArticle(Integer article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return "ReceptionDetailCADTO{" + "reception=" + reception + ", purchaseOrder=" + purchaseOrder + ", article=" + article + ", receivedQuantity=" + receivedQuantity + ", freeReceivedQuantity=" + freeReceivedQuantity + '}';
    }

}
