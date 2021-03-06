package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.FactureDirecteCostCenter;
import com.csys.pharmacie.achat.domain.FactureDirecteCostCenterPK;
import com.csys.pharmacie.achat.dto.FactureDirecteCostCenterDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactureDirecteCostCenterFactory {
   // private final Logger log = LoggerFactory.getLogger(FactureDirecteCostCenterFactory.class);

    public static FactureDirecteCostCenterDTO facturedirectecostcenterToFactureDirecteCostCenterDTO(FactureDirecteCostCenter facturedirectecostcenter) {
        if (facturedirectecostcenter == null) {
            return null;
        } else {
            FactureDirecteCostCenterDTO facturedirectecostcenterDTO = new FactureDirecteCostCenterDTO();
            facturedirectecostcenterDTO.setCodeCostCenter(facturedirectecostcenter.getPk().getCodeCostCenter());
            facturedirectecostcenterDTO.setValueTTC(facturedirectecostcenter.getMontantTTC());
            facturedirectecostcenterDTO.setNumeroFactureDirecte(facturedirectecostcenter.getFactureDirecte().getNumbon());
            facturedirectecostcenterDTO.setNumeroAfficheFactureDirecte(facturedirectecostcenter.getFactureDirecte().getNumaffiche());
            //System.out.println("iciiiiiiiiiii  "+(facturedirectecostcenterDTO.getValueTTC().divide(facturedirectecostcenter.getFactureDirecte().getMontant(),17,BigDecimal.ROUND_HALF_UP)).toString());
            facturedirectecostcenterDTO.setProportionCostCenter(facturedirectecostcenterDTO.getValueTTC().divide(facturedirectecostcenter.getFactureDirecte().getMontant(),17,BigDecimal.ROUND_HALF_UP));
            return facturedirectecostcenterDTO;
        }
    }

    public static FactureDirecteCostCenter facturedirectecostcenterDTOToFactureDirecteCostCenter(FactureDirecteCostCenterDTO facturedirectecostcenterDTO) {
        FactureDirecteCostCenter facturedirectecostcenter = new FactureDirecteCostCenter();
        FactureDirecteCostCenterPK pk = new FactureDirecteCostCenterPK();
        pk.setCodeCostCenter(facturedirectecostcenterDTO.getCodeCostCenter());
        pk.setNumeroFactureDirecte(facturedirectecostcenterDTO.getNumeroFactureDirecte());
        facturedirectecostcenter.setPk(pk);
        facturedirectecostcenter.setMontantTTC(facturedirectecostcenterDTO.getValueTTC());

        return facturedirectecostcenter;
    }

    public static Collection<FactureDirecteCostCenterDTO> facturedirectecostcenterToFactureDirecteCostCenterDTOs(Collection<FactureDirecteCostCenter> facturedirectecostcenters) {
        List<FactureDirecteCostCenterDTO> facturedirectecostcentersDTO = new ArrayList<>();
        facturedirectecostcenters.forEach(x -> {
            facturedirectecostcentersDTO.add(facturedirectecostcenterToFactureDirecteCostCenterDTO(x));
        });
        return facturedirectecostcentersDTO;
    }
}
