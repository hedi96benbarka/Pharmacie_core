package com.csys.pharmacie.achat.dto;

public class ReceivingCommandeDTO {

    private CommandeAchatDTO commandeAchat;
    private ReceivingDTO receiving;

    private Integer receivingID;

    public CommandeAchatDTO getCommandeAchat() {
        return commandeAchat;
    }

    public void setCommandeAchat(CommandeAchatDTO commandeAchat) {
        this.commandeAchat = commandeAchat;
    }

    public Integer getReceivingID() {
        return receivingID;
    }

    public void setReceivingID(Integer receivingID) {
        this.receivingID = receivingID;
    }

    public ReceivingDTO getReceiving() {
        return receiving;
    }

    public void setReceiving(ReceivingDTO receiving) {
        this.receiving = receiving;
    }

}
