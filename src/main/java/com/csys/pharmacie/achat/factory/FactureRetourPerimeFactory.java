package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.BaseTvaFactureRetourPerime;
import com.csys.pharmacie.achat.domain.FactureRetourPerime;
import com.csys.pharmacie.achat.dto.BaseTvaFactureRetourPerimeDTO;
import com.csys.pharmacie.achat.dto.FactureRetourPerimeDTO;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class FactureRetourPerimeFactory {

    public static FactureRetourPerimeDTO factureRetourPerimeToFactureRetourPerimeDTO(FactureRetourPerime factureRetourPerime) {
        FactureRetourPerimeDTO factureRetourPerimeDTO = new FactureRetourPerimeDTO();
        factureRetourPerimeDTO.setNumbon(factureRetourPerime.getNumbon());
        factureRetourPerimeDTO.setCodeVend(factureRetourPerime.getCodvend());
        factureRetourPerimeDTO.setDateBon(factureRetourPerime.getDatbon());
        factureRetourPerimeDTO.setTypeBon(factureRetourPerime.getTypbon());
        factureRetourPerimeDTO.setNumAffiche(factureRetourPerime.getNumaffiche());
        factureRetourPerimeDTO.setCategDepot(factureRetourPerime.getCategDepot());
        factureRetourPerimeDTO.setMontantTTC(factureRetourPerime.getMontantTTC());
        factureRetourPerimeDTO.setCodeFournisseur(factureRetourPerime.getCodeFournisseur());
        factureRetourPerimeDTO.setDateFournisseur(factureRetourPerime.getDateFournisseur());
        factureRetourPerimeDTO.setReferenceFournisseur(factureRetourPerime.getReferenceFournisseur());
        factureRetourPerimeDTO.setUserAnnule(factureRetourPerime.getUserAnnule());
        factureRetourPerimeDTO.setDateAnnule(factureRetourPerime.getDateAnnule());
        factureRetourPerimeDTO.setIntegrer(factureRetourPerime.getIntegrer());
        
        //pour edition
         Instant instant = factureRetourPerime.getDateFournisseur().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            factureRetourPerimeDTO.setDateFournisseurEdition(Date.from(instant));
            
            factureRetourPerimeDTO.setDateBonEdition(Date.from(factureRetourPerime.getDatbon().atZone(ZoneId.systemDefault())
                    .toInstant()));

        Collection<BaseTvaFactureRetourPerimeDTO> baseTvaFactureRetourPerimeDtos = new ArrayList<>();
        factureRetourPerime.getBaseTvaFactureRetourPerime().forEach(x -> {
            BaseTvaFactureRetourPerimeDTO baseTvaFactureRetourPerimeDto = BaseTvaFactureRetourPerimeFactory.baseTvaFactureRetourPerimeToBaseTvaFactureRetourPerimeDTO(x);
            baseTvaFactureRetourPerimeDtos.add(baseTvaFactureRetourPerimeDto);
        });
        factureRetourPerimeDTO.setBaseTvaFactureRetourPerime(baseTvaFactureRetourPerimeDtos);

        return factureRetourPerimeDTO;
    }

    public static FactureRetourPerime factureRetourPerimeDTOToFactureRetourPerime(FactureRetourPerimeDTO factureRetourPerimeDTO) {
        FactureRetourPerime factureRetourPerime = new FactureRetourPerime();
        factureRetourPerime.setCodvend(factureRetourPerimeDTO.getCodeVend());
        factureRetourPerime.setDatbon(factureRetourPerimeDTO.getDateBon());
        factureRetourPerime.setNumaffiche(factureRetourPerimeDTO.getNumAffiche());
        factureRetourPerime.setCategDepot(factureRetourPerimeDTO.getCategDepot());
        factureRetourPerime.setMontantTTC(factureRetourPerimeDTO.getMontantTTC());
        factureRetourPerime.setCodeFournisseur(factureRetourPerimeDTO.getCodeFournisseur());
        factureRetourPerime.setDateFournisseur(factureRetourPerimeDTO.getDateFournisseur());
        factureRetourPerime.setReferenceFournisseur(factureRetourPerimeDTO.getReferenceFournisseur());
        factureRetourPerime.setUserAnnule(factureRetourPerimeDTO.getUserAnnule());
        factureRetourPerime.setDateAnnule(factureRetourPerimeDTO.getDateAnnule());
        factureRetourPerime.setIntegrer(factureRetourPerimeDTO.getIntegrer());

        Collection<BaseTvaFactureRetourPerime> baseTvaFactureRetourPerimes = new ArrayList<>();
        factureRetourPerimeDTO.getBaseTvaFactureRetourPerime().forEach(x -> {
            BaseTvaFactureRetourPerime baseTvaFactureRetourPerime = BaseTvaFactureRetourPerimeFactory.baseTvaFactureRetourPerimeDTOToBaseTvaFactureRetourPerime(x);
            baseTvaFactureRetourPerime.setFactureRetourPerime(factureRetourPerime);
            baseTvaFactureRetourPerimes.add(baseTvaFactureRetourPerime);
        });

        factureRetourPerime.setBaseTvaFactureRetourPerime(baseTvaFactureRetourPerimes);
        return factureRetourPerime;
    }

    public static Collection<FactureRetourPerimeDTO> factureretourperimeToFactureRetourPerimeDTOs(Collection<FactureRetourPerime> factureRetourPerimes) {
        List<FactureRetourPerimeDTO> factureRetourPerimesDTO = new ArrayList<>();
        factureRetourPerimes.forEach(x -> {
            factureRetourPerimesDTO.add(factureRetourPerimeToFactureRetourPerimeDTO(x));
        });
        return factureRetourPerimesDTO;
    }

    public static FactureRetourPerimeDTO lazyFactureRetourPerimeToFactureRetourPerimeDTO(FactureRetourPerime factureRetourPerime) {
        FactureRetourPerimeDTO factureRetourPerimeDTO = new FactureRetourPerimeDTO();
        factureRetourPerimeDTO.setCodeVend(factureRetourPerime.getCodvend());
        factureRetourPerimeDTO.setDateBon(factureRetourPerime.getDatbon());
        factureRetourPerimeDTO.setNumAffiche(factureRetourPerime.getNumaffiche());
        factureRetourPerimeDTO.setMontantTTC(factureRetourPerime.getMontantTTC());
        factureRetourPerimeDTO.setCodeFournisseur(factureRetourPerime.getCodeFournisseur());
        factureRetourPerimeDTO.setDateFournisseur(factureRetourPerime.getDateFournisseur());
        factureRetourPerimeDTO.setReferenceFournisseur(factureRetourPerime.getReferenceFournisseur());
        factureRetourPerimeDTO.setUserAnnule(factureRetourPerime.getUserAnnule());
        factureRetourPerimeDTO.setDateAnnule(factureRetourPerime.getDateAnnule());
        factureRetourPerimeDTO.setIntegrer(factureRetourPerime.getIntegrer());
        factureRetourPerimeDTO.setCategDepot(factureRetourPerime.getCategDepot());
        return factureRetourPerimeDTO;
    }

    public static Collection<FactureRetourPerimeDTO> lazyFactureRetourPerimeToFactureRetourPerimeDTOs(Collection<FactureRetourPerime> factureRetourPerimes) {
        List<FactureRetourPerimeDTO> factureRetourPerimesDTO = new ArrayList<>();
        factureRetourPerimes.forEach(x -> {
            factureRetourPerimesDTO.add(lazyFactureRetourPerimeToFactureRetourPerimeDTO(x));
        });
        return factureRetourPerimesDTO;
    }

}
