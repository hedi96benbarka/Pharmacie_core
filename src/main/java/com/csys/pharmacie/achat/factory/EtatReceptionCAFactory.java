package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.EtatReceptionCA;
import com.csys.pharmacie.achat.dto.EtatReceptionCADTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EtatReceptionCAFactory {

    public static EtatReceptionCADTO etatreceptioncaToEtatReceptionCADTO(EtatReceptionCA etatreceptionca) {
        if (etatreceptionca == null) {
            return null;
        }
 EtatReceptionCADTO etatreceptioncaDTO = new EtatReceptionCADTO();
        etatreceptioncaDTO.setCommandeAchat(etatreceptionca.getCommandeAchat());
        etatreceptioncaDTO.setEtatReception(etatreceptionca.getEtatReception());
        return etatreceptioncaDTO;
    }

    public static EtatReceptionCA etatreceptioncaDTOToEtatReceptionCA(EtatReceptionCADTO etatreceptioncaDTO) {
        EtatReceptionCA etatreceptionca = new EtatReceptionCA();
        etatreceptionca.setCommandeAchat(etatreceptioncaDTO.getCommandeAchat());
        etatreceptionca.setEtatReception(etatreceptioncaDTO.getEtatReception());
        return etatreceptionca;
    }

    public static List<EtatReceptionCADTO> etatreceptioncaToEtatReceptionCADTOs(Collection<EtatReceptionCA> etatreceptioncas) {
        List<EtatReceptionCADTO> etatreceptioncasDTO = new ArrayList<>();
        etatreceptioncas.forEach(x -> {
            etatreceptioncasDTO.add(etatreceptioncaToEtatReceptionCADTO(x));
        });
        return etatreceptioncasDTO;
    }
}
