package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.DetailFactureDirecte;
import com.csys.pharmacie.achat.dto.DetailFactureDirecteDTO;
import static com.csys.pharmacie.helper.CategorieDepotEnum.EC;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class DetailFactureDirecteFactory {

    private static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    public static DetailFactureDirecteDTO detailfacturedirecteToDetailFactureDirecteDTO(DetailFactureDirecte detailfacturedirecte) {
        DetailFactureDirecteDTO detailfacturedirecteDTO = new DetailFactureDirecteDTO();
        detailfacturedirecteDTO.setCodtva(detailfacturedirecte.getCodtva());
        detailfacturedirecteDTO.setTautva(detailfacturedirecte.getTautva());
        detailfacturedirecteDTO.setCategDepot(EC);
        detailfacturedirecteDTO.setCodeSaisi(detailfacturedirecte.getCodeSaisi());
        detailfacturedirecteDTO.setQuantite(detailfacturedirecte.getQuantite());
        detailfacturedirecteDTO.setCode(detailfacturedirecte.getCode());
        detailfacturedirecteDTO.setCodart(detailfacturedirecte.getCodart());
        detailfacturedirecteDTO.setNumbon(detailfacturedirecte.getNumbon());
        detailfacturedirecteDTO.setPriuni(detailfacturedirecte.getPriuni());
        detailfacturedirecteDTO.setUnite(detailfacturedirecte.getUnite());
      

        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            detailfacturedirecteDTO.setDesart(detailfacturedirecte.getDesArtSec());
            detailfacturedirecteDTO.setDesArtSec(detailfacturedirecte.getDesart());
        } else {
            detailfacturedirecteDTO.setDesart(detailfacturedirecte.getDesart());
            detailfacturedirecteDTO.setDesArtSec(detailfacturedirecte.getDesArtSec());
        }

        return detailfacturedirecteDTO;
    }

    public static DetailFactureDirecte detailfacturedirecteDTOToDetailFactureDirecte(DetailFactureDirecteDTO detailfacturedirecteDTO) {
        DetailFactureDirecte detailfacturedirecte = new DetailFactureDirecte();
        detailfacturedirecte.setCodtva(detailfacturedirecteDTO.getCodtva());
        detailfacturedirecte.setTautva(detailfacturedirecteDTO.getTautva());
        detailfacturedirecte.setCategDepot(EC);
        detailfacturedirecte.setCodeSaisi(detailfacturedirecteDTO.getCodeSaisi());
        detailfacturedirecte.setQuantite(detailfacturedirecteDTO.getQuantite());
        detailfacturedirecte.setCode(detailfacturedirecteDTO.getCode());
        detailfacturedirecte.setCodart(detailfacturedirecteDTO.getCodart());
        detailfacturedirecte.setNumbon(detailfacturedirecteDTO.getNumbon());
        detailfacturedirecte.setPriuni(detailfacturedirecteDTO.getPriuni());
        detailfacturedirecte.setUnite(detailfacturedirecteDTO.getUnite());

        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            detailfacturedirecte.setDesart(detailfacturedirecteDTO.getDesArtSec());
            detailfacturedirecte.setDesArtSec(detailfacturedirecteDTO.getDesart());
        } else {
            detailfacturedirecte.setDesart(detailfacturedirecteDTO.getDesart());
            detailfacturedirecte.setDesArtSec(detailfacturedirecteDTO.getDesArtSec());
        }

        return detailfacturedirecte;
    }

    public static Collection<DetailFactureDirecteDTO> detailfacturedirecteToDetailFactureDirecteDTOs(Collection<DetailFactureDirecte> detailfacturedirectes) {
        List<DetailFactureDirecteDTO> detailfacturedirectesDTO = new ArrayList<>();
        detailfacturedirectes.forEach(x -> {
            detailfacturedirectesDTO.add(detailfacturedirecteToDetailFactureDirecteDTO(x));
        });
        return detailfacturedirectesDTO;
    }
}
