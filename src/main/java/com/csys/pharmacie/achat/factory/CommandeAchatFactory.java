package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
import com.csys.pharmacie.achat.dto.CommandeAchatModeReglementDTO;
import com.csys.pharmacie.achat.dto.DetailCommandeAchatDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommandeAchatFactory {

    static String CODE_MOTIF_PAIEMEMT;

    @Value("${default-code-motif-paiement-ca}")

    public  void setCodeMotifPaiement(String db) {
        CODE_MOTIF_PAIEMEMT = db;
    }
    public static CommandeAchatDTO commandeachatToCommandeAchatDTO(FactureBA bonRecep, FournisseurDTO fourniss) {
        CommandeAchatDTO commandeachatDTO = new CommandeAchatDTO();
        commandeachatDTO.setCategorieDepot(bonRecep.getCategDepot());
        commandeachatDTO.setMemo(bonRecep.getMemop());
        commandeachatDTO.setSatisfaction("ST");
        commandeachatDTO.setImprimer(false);
        commandeachatDTO.setManuel(true);
        commandeachatDTO.setStockable(true);
        commandeachatDTO.setFournisseur(fourniss);
        commandeachatDTO.setLocalReception("");
        commandeachatDTO.setIsService(false);
        commandeachatDTO.setCodeSite(bonRecep.getCodeSite());
        List<DetailCommandeAchatDTO> detailCommandeAchatCollectionDtos = new ArrayList<>();

        for (MvtStoBA x : bonRecep.getDetailFactureBACollection()) {
            DetailCommandeAchatDTO detailcommandeachatDTO = new DetailCommandeAchatDTO();
            detailcommandeachatDTO.setCodart(x.getPk().getCodart());
            detailcommandeachatDTO.setQuantite(x.getQuantite());
            detailcommandeachatDTO.setCodtva(x.getCodtva());
            detailcommandeachatDTO.setTautva(x.getTautva());
            detailcommandeachatDTO.setMemo("");
            detailcommandeachatDTO.setPrixUnitaire(x.getPriuni());
            detailcommandeachatDTO.setRemise(BigDecimal.ZERO);
            detailcommandeachatDTO.setCodeSaisi(x.getCodeSaisi());
            detailcommandeachatDTO.setDesignation(x.getDesart());
            detailcommandeachatDTO.setDesignationSec(x.getDesArtSec());
            detailcommandeachatDTO.setDelaiLivraison(fourniss.getDelaiLivraison());
            detailcommandeachatDTO.setQuantiteGratuite(BigDecimal.ZERO);
            detailCommandeAchatCollectionDtos.add(detailcommandeachatDTO);
        }
        commandeachatDTO.setDetailCommandeAchatCollection(detailCommandeAchatCollectionDtos);

        List<CommandeAchatModeReglementDTO> modeReglementList = new ArrayList<>();
        modeReglementList.add(new CommandeAchatModeReglementDTO(fourniss.getCodeModeReglement(), 100, Integer.valueOf(CODE_MOTIF_PAIEMEMT), fourniss.getDelaiPaiement(), fourniss.getDelaiPaiement()));
        commandeachatDTO.setModeReglementList(modeReglementList);

        return commandeachatDTO;
    }

}
