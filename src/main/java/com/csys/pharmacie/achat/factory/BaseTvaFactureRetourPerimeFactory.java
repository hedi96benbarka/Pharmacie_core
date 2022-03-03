package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.BaseTvaFactureRetourPerime;
import com.csys.pharmacie.achat.dto.BaseTvaFactureRetourPerimeDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseTvaFactureRetourPerimeFactory {

    public static BaseTvaFactureRetourPerimeDTO baseTvaFactureRetourPerimeToBaseTvaFactureRetourPerimeDTO(BaseTvaFactureRetourPerime basetvafactureretourperime) {
        BaseTvaFactureRetourPerimeDTO basetvafactureretourperimeDTO = new BaseTvaFactureRetourPerimeDTO();
        basetvafactureretourperimeDTO.setCode(basetvafactureretourperime.getCode());
        basetvafactureretourperimeDTO.setCodeTva(basetvafactureretourperime.getCodeTva());
        basetvafactureretourperimeDTO.setTauxTva(basetvafactureretourperime.getTauxTva());
        basetvafactureretourperimeDTO.setBaseTva(basetvafactureretourperime.getBaseTva());
        basetvafactureretourperimeDTO.setMontantTva(basetvafactureretourperime.getMontantTva());
        return basetvafactureretourperimeDTO;
    }

    public static BaseTvaFactureRetourPerime baseTvaFactureRetourPerimeDTOToBaseTvaFactureRetourPerime(BaseTvaFactureRetourPerimeDTO baseTvaFactureRetourPerimeDTO) {
        BaseTvaFactureRetourPerime baseTvaFactureRetourPerime = new BaseTvaFactureRetourPerime();
        baseTvaFactureRetourPerime.setCode(baseTvaFactureRetourPerimeDTO.getCode());
        baseTvaFactureRetourPerime.setCodeTva(baseTvaFactureRetourPerimeDTO.getCodeTva());
        baseTvaFactureRetourPerime.setTauxTva(baseTvaFactureRetourPerimeDTO.getTauxTva());
        baseTvaFactureRetourPerime.setBaseTva(baseTvaFactureRetourPerimeDTO.getBaseTva());
        baseTvaFactureRetourPerime.setMontantTva(baseTvaFactureRetourPerimeDTO.getMontantTva());
        return baseTvaFactureRetourPerime;
    }

    public static Collection<BaseTvaFactureRetourPerimeDTO> basetvafactureretourperimeToBaseTvaFactureRetourPerimeDTOs(Collection<BaseTvaFactureRetourPerime> basetvafactureretourperimes) {
        List<BaseTvaFactureRetourPerimeDTO> basetvafactureretourperimesDTO = new ArrayList<>();
        basetvafactureretourperimes.forEach(x -> {
                basetvafactureretourperimesDTO.add(baseTvaFactureRetourPerimeToBaseTvaFactureRetourPerimeDTO(x));
        });
        return basetvafactureretourperimesDTO;
    }
}
