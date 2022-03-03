package com.csys.pharmacie.vente.quittance.factory;

import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.vente.quittance.domain.FactureDR;
import com.csys.pharmacie.vente.quittance.dto.FactureDREditionDTO;
import java.time.ZoneId;
import java.util.Date;

public class FactureDREditionDTOFactory {

    public static FactureDREditionDTO EntityToFactureDREditionDTO(FactureDR facturedr, DepotDTO depot) {
        FactureDREditionDTO factureDREditionDTO = new FactureDREditionDTO();
        factureDREditionDTO.setNumBon(facturedr.getNumbon());
        factureDREditionDTO.setNumAffiche(facturedr.getNumaffiche());
        factureDREditionDTO.setDate(Date.from(facturedr.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        factureDREditionDTO.setNumDoss(facturedr.getNumdoss());
        factureDREditionDTO.setNumChambre(facturedr.getNumCha());
        factureDREditionDTO.setNomPatient(facturedr.getRaisoc());
        factureDREditionDTO.setDepotDesignation(depot.getDesignation());
        factureDREditionDTO.setDepotCode(depot.getCode());
        factureDREditionDTO.setImprimer(facturedr.isImprimer());
        factureDREditionDTO.setDemandes(MvtstoDRFactory.mvtstodrToDemandeArticlesDtos(facturedr));
        return factureDREditionDTO;
    }

}
