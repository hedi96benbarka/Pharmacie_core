package com.csys.pharmacie.vente.avoir.factory;

import com.csys.pharmacie.vente.avoir.domain.MvtstoMvtstoAV;
import com.csys.pharmacie.vente.avoir.dto.MvtstomvtstoAVDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MvtstomvtstomvtstoAVFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    public static MvtstomvtstoAVDTO mvtstomvtstoAVToMvtstomvtstomvtstoAVDTO(MvtstoMvtstoAV mvtstomvtstoAV) {
        MvtstomvtstoAVDTO mvtstomvtstoAVDTO = new MvtstomvtstoAVDTO();
        mvtstomvtstoAVDTO.setCodart(mvtstomvtstoAV.getMvtsto().getMvtstoPK().getCodart());
        mvtstomvtstoAVDTO.setLotInter(mvtstomvtstoAV.getMvtsto().getLotInter());
        mvtstomvtstoAVDTO.setPriuni(mvtstomvtstoAV.getMvtsto().getPriuni());
        mvtstomvtstoAVDTO.setMontht(mvtstomvtstoAV.getMvtsto().getMontht());
        mvtstomvtstoAVDTO.setDatPer(mvtstomvtstoAV.getMvtsto().getDatPer());
        mvtstomvtstoAVDTO.setUnityCode(mvtstomvtstoAV.getMvtsto().getUnite());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstomvtstoAVDTO.setDesart(mvtstomvtstoAV.getMvtsto().getDesArtSec());
            mvtstomvtstoAVDTO.setDesArtSec(mvtstomvtstoAV.getMvtsto().getDesart());
        } else {
            mvtstomvtstoAVDTO.setDesart(mvtstomvtstoAV.getMvtsto().getDesart());
            mvtstomvtstoAVDTO.setDesArtSec(mvtstomvtstoAV.getMvtsto().getDesArtSec());
        }

        mvtstomvtstoAVDTO.setCodeSaisi(mvtstomvtstoAV.getMvtsto().getCodeSaisi());
        mvtstomvtstoAVDTO.setQuantite(mvtstomvtstoAV.getQte());
        return mvtstomvtstoAVDTO;
    }

    public static List<MvtstomvtstoAVDTO> mvtstomvtstoAVToMvtstomvtstomvtstoAVAVDTOs(List<MvtstoMvtstoAV> mvtstomvtstoAVs) {
        List<MvtstomvtstoAVDTO> mvtstomvtstoAVsDTO = new ArrayList<>();
        mvtstomvtstoAVs.forEach(x -> {
            mvtstomvtstoAVsDTO.add(mvtstomvtstoAVToMvtstomvtstomvtstoAVDTO(x));
        });
        return mvtstomvtstoAVsDTO;
    }

}
