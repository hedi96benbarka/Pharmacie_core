package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
import com.csys.pharmacie.achat.domain.ReceptionDetailCAPK;
import com.csys.pharmacie.achat.dto.ReceptionDetailCADTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReceptionDetailCAFactory {

    public static ReceptionDetailCADTO receptiondetailcaToReceptionDetailCADTO(ReceptionDetailCA entity) {
        ReceptionDetailCADTO receptiondetailcaDTO = new ReceptionDetailCADTO();
        receptiondetailcaDTO.setArticle(entity.getPk().getArticle());
        receptiondetailcaDTO.setPurchaseOrder(entity.getPk().getCommandeAchat());
        receptiondetailcaDTO.setReception(entity.getPk().getReception());
        receptiondetailcaDTO.setReceivedQuantity(entity.getQuantiteReceptione());
        return receptiondetailcaDTO;
    }

    public static ReceptionDetailCA receptiondetailcaDTOToReceptionDetailCA(ReceptionDetailCADTO dto) {
        ReceptionDetailCA receptiondetailca = new ReceptionDetailCA();
        receptiondetailca.setPk(new ReceptionDetailCAPK(dto.getReception(), dto.getPurchaseOrder(), dto.getArticle()));
        receptiondetailca.setQuantiteReceptione(dto.getReceivedQuantity());
//        receptiondetailca.
        return receptiondetailca;
    }

    public static List<ReceptionDetailCADTO> receptiondetailcaToReceptionDetailCADTOs(Collection<ReceptionDetailCA> receptiondetailcas) {
        List<ReceptionDetailCADTO> receptiondetailcasDTO = new ArrayList<>();
        receptiondetailcas.forEach(x -> {
            receptiondetailcasDTO.add(receptiondetailcaToReceptionDetailCADTO(x));
        });
        return receptiondetailcasDTO;
    }
}
