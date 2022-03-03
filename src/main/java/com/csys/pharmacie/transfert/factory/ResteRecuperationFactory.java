package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.transfert.domain.ResteRecuperation;
import com.csys.pharmacie.transfert.domain.ResteRecuperationPK;
import com.csys.pharmacie.transfert.dto.ResteRecuperationDTO;
import java.util.ArrayList;
import java.util.List;

public class ResteRecuperationFactory {

    public static ResteRecuperationDTO resterecuperationToResteRecuperationDTO(ResteRecuperation resterecuperation) {
        ResteRecuperationDTO resterecuperationDTO = new ResteRecuperationDTO();
        resterecuperationDTO.setCodart(resterecuperation.getPk().getCodeArticle());
        resterecuperationDTO.setDepotID(resterecuperation.getPk().getCodeDepot());
        resterecuperationDTO.setUnityCode(resterecuperation.getCodeUnite());
        resterecuperationDTO.setQuantite(resterecuperation.getQuantite());
        return resterecuperationDTO;
    }

    public static ResteRecuperation resterecuperationDTOToResteRecuperation(ResteRecuperationDTO resterecuperationDTO) {
        ResteRecuperation resterecuperation = new ResteRecuperation();
        resterecuperation.setPk(new ResteRecuperationPK(resterecuperationDTO.getCodart(), resterecuperationDTO.getDepotID()));
        resterecuperation.setCodeUnite(resterecuperationDTO.getUnityCode());
        resterecuperation.setQuantite(resterecuperationDTO.getQuantite()); 
        return resterecuperation;
    }

    public static List<ResteRecuperationDTO> resterecuperationToResteRecuperationDTOs(List<ResteRecuperation> resterecuperations) {
        List<ResteRecuperationDTO> resterecuperationsDTO = new ArrayList<>();
        resterecuperations.forEach(x -> {
            resterecuperationsDTO.add(resterecuperationToResteRecuperationDTO(x));
        });
        return resterecuperationsDTO;
    }

    public static List<ResteRecuperation> resteRecuperationDTOsToResteRecuperations(List<ResteRecuperationDTO> resterecuperationDTOs) {
        List<ResteRecuperation> resterecuperations = new ArrayList<>();
        resterecuperationDTOs.forEach(x -> {
            resterecuperations.add(resterecuperationDTOToResteRecuperation(x));
        });
        return resterecuperations;
    }
}
