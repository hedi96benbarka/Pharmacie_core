package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.ReceivingCommande;
import com.csys.pharmacie.achat.domain.ReceivingCommandePK;
import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
import com.csys.pharmacie.achat.dto.ReceivingCommandeDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReceivingCommandeFactory {
  public static ReceivingCommandeDTO receivingcommandeToReceivingCommandeDTO(ReceivingCommande receivingcommande) {
    ReceivingCommandeDTO receivingcommandeDTO=new ReceivingCommandeDTO();
    CommandeAchatDTO commandeAchatDTO=new CommandeAchatDTO();
    commandeAchatDTO.setCode(receivingcommande.getReceivingCommandePK().getCommandeParamAchat());
    receivingcommandeDTO.setCommandeAchat(commandeAchatDTO);
    receivingcommandeDTO.setReceiving(ReceivingFactory.receivingToReceivingDTO(receivingcommande.getReceiving()));
    receivingcommandeDTO.setReceivingID(receivingcommande.getReceivingCommandePK().getReciveing());
    return receivingcommandeDTO;
  }

  public static ReceivingCommande receivingcommandeDTOToReceivingCommande(ReceivingCommandeDTO receivingcommandeDTO) {
    ReceivingCommande receivingcommande=new ReceivingCommande();
    ReceivingCommandePK receivingcommandePK=new ReceivingCommandePK();
    receivingcommandePK.setReciveing(receivingcommandeDTO.getReceivingID());
    receivingcommandePK.setCommandeParamAchat(receivingcommandeDTO.getCommandeAchat().getCode());
    receivingcommande.setReceivingCommandePK(receivingcommandePK);
    return receivingcommande;
  }

  public static Collection<ReceivingCommandeDTO> receivingcommandeToReceivingCommandeDTOs(Collection<ReceivingCommande> receivingcommandes) {
    List<ReceivingCommandeDTO> receivingcommandesDTO=new ArrayList<>();
    receivingcommandes.forEach(x -> {
      receivingcommandesDTO.add(receivingcommandeToReceivingCommandeDTO(x));
    } );
    return receivingcommandesDTO;
  }
}

