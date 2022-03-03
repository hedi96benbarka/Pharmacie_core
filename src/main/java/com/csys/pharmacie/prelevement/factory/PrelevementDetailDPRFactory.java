package com.csys.pharmacie.prelevement.factory;

import com.csys.pharmacie.prelevement.domain.PrelevementDetailDPR;
import com.csys.pharmacie.prelevement.domain.PrelevementDetailDPRPK;
import com.csys.pharmacie.prelevement.dto.PrelevementDetailDPRDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrelevementDetailDPRFactory {

    public static PrelevementDetailDPRDTO prelevementdetaildprToPrelevementDetailDPRDTO(PrelevementDetailDPR prelevementdetaildpr) {
        PrelevementDetailDPRDTO prelevementdetaildprDTO = new PrelevementDetailDPRDTO();

        prelevementdetaildprDTO.setCodedetailDPR(prelevementdetaildpr.getPk().getCodedetailDPR());
        prelevementdetaildprDTO.setCodePrelevment(prelevementdetaildpr.getPk().getCodePrelevment());

        prelevementdetaildprDTO.setQuantite_prelevee(prelevementdetaildpr.getQuantite_prelevee());
        prelevementdetaildprDTO.setCodeDPR(prelevementdetaildpr.getCodeDPR());
        return prelevementdetaildprDTO;
    }

    public static PrelevementDetailDPR prelevementdetaildprDTOToPrelevementDetailDPR(PrelevementDetailDPRDTO prelevementdetaildprDTO) {
        PrelevementDetailDPR prelevementdetaildpr = new PrelevementDetailDPR();
        PrelevementDetailDPRPK pk = new PrelevementDetailDPRPK(prelevementdetaildprDTO.getCodedetailDPR(), prelevementdetaildprDTO.getCodePrelevment());
        prelevementdetaildpr.setPk(pk);

        prelevementdetaildpr.setQuantite_prelevee(prelevementdetaildprDTO.getQuantite_prelevee());
        prelevementdetaildpr.setCodeDPR(prelevementdetaildprDTO.getCodeDPR());
        return prelevementdetaildpr;
    }

    public static Collection<PrelevementDetailDPRDTO> prelevementdetaildprToPrelevementDetailDPRDTOs(Collection<PrelevementDetailDPR> prelevementdetaildprs) {
        List<PrelevementDetailDPRDTO> prelevementdetaildprsDTO = new ArrayList<>();
        prelevementdetaildprs.forEach(x -> {
            prelevementdetaildprsDTO.add(prelevementdetaildprToPrelevementDetailDPRDTO(x));
        });
        return prelevementdetaildprsDTO;
    }
}
