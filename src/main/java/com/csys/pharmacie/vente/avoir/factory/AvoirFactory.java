package com.csys.pharmacie.vente.avoir.factory;

import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.vente.avoir.domain.FactureAV;
import com.csys.pharmacie.vente.avoir.dto.Avoir;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import org.springframework.stereotype.Component;

@Component
public class AvoirFactory {

    public static FactureAV avoirToFactureAV(Avoir avoir) {
        FactureAV factureav = new FactureAV();
        factureav.setMemop(avoir.getMemop());
        factureav.setNumdoss(avoir.getNumdoss());
        factureav.setImprimer(Boolean.FALSE);
        factureav.setCoddep(avoir.getCoddep());
        factureav.setTypbon(TypeBonEnum.AV);
        factureav.setCategDepot(avoir.getCategDepot());
        factureav.setIntegrer(Boolean.FALSE);
        factureav.setCodeIntegration("");
        factureav.setAvoirPhPostReg(avoir.getAvoirPhPostReg());
        factureav.setMontantRemise(avoir.getMontantRemise());
        return factureav;
    }
    
    public static FactureAV factureToFactureAV(Facture facture) {
        FactureAV factureav = new FactureAV();
        factureav.setMemop(facture.getMemop());
        factureav.setNumdoss(facture.getNumdoss());
        factureav.setImprimer(Boolean.FALSE);
        factureav.setCoddep(facture.getCoddep());
        factureav.setTypbon(TypeBonEnum.AV);
        factureav.setCategDepot(facture.getCategDepot());
        factureav.setIntegrer(Boolean.FALSE);
        factureav.setCodeIntegration("");
        return factureav;
    }
}
