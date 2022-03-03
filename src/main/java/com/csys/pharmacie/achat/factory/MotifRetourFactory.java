package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.MotifRetour;
import com.csys.pharmacie.achat.dto.MotifRetourDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MotifRetourFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    public static MotifRetourDTO motifretourToMotifRetourDTO(MotifRetour motifretour) {
        MotifRetourDTO motifretourDTO = new MotifRetourDTO();
        motifretourDTO.setId(motifretour.getId());

        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            motifretourDTO.setDescription(motifretour.getDescriptionSec());
            motifretourDTO.setDescriptionSec(motifretour.getDescription());
        } else {
            motifretourDTO.setDescription(motifretour.getDescription());
            motifretourDTO.setDescriptionSec(motifretour.getDescriptionSec());
        }

        return motifretourDTO;
    }

    public static MotifRetour motifretourDTOToMotifRetour(MotifRetourDTO motifretourDTO) {
        MotifRetour motifretour = new MotifRetour();
        motifretour.setId(motifretourDTO.getId());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            motifretour.setDescription(motifretourDTO.getDescriptionSec());
            motifretour.setDescriptionSec(motifretourDTO.getDescription());
        }else {
             motifretour.setDescription(motifretourDTO.getDescription());
            motifretour.setDescriptionSec(motifretourDTO.getDescriptionSec());
        }
        return motifretour;
    }

    public static Collection<MotifRetourDTO> motifretourToMotifRetourDTOs(Collection<MotifRetour> motifretours) {
        List<MotifRetourDTO> motifretoursDTO = new ArrayList<>();
        motifretours.forEach(x -> {
            motifretoursDTO.add(motifretourToMotifRetourDTO(x));
        });
        return motifretoursDTO;
    }
}
