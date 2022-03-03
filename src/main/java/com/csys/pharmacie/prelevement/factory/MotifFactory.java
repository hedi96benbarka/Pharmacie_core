package com.csys.pharmacie.prelevement.factory;

import com.csys.pharmacie.prelevement.domain.FacturePR;
import com.csys.pharmacie.prelevement.domain.Motif;
import com.csys.pharmacie.prelevement.dto.FacturePRDTO;
import com.csys.pharmacie.prelevement.dto.MotifDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MotifFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    public static MotifDTO motifToMotifDTO(Motif motif) {

        MotifDTO motifDTO = new MotifDTO();
        motifDTO.setId(motif.getId());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {

            motifDTO.setDesignation(motif.getDesignation_sec());
            motifDTO.setDesignation_sec(motif.getDesignation());
        } else {
            motifDTO.setDesignation(motif.getDesignation());
            motifDTO.setDesignation_sec(motif.getDesignation_sec());
        }

        return motifDTO;
    }

    public static Motif motifDTOToMotif(MotifDTO motifDTO) {
        Motif motif = new Motif();
        motif.setId(motifDTO.getId());

        motif.setDesignation(motifDTO.getDesignation());
        motif.setDesignation_sec(motifDTO.getDesignation_sec());

        return motif;
    }

    public static Collection<MotifDTO> motifToMotifDTOs(Collection<Motif> motifs) {
        List<MotifDTO> motifsDTO = new ArrayList<>();
        motifs.forEach(x -> {
            motifsDTO.add(motifToMotifDTO(x));
        });
        return motifsDTO;
    }

    public static MotifDTO lazymotifToMotifDTO(Motif motif) {
        MotifDTO motifDTO = new MotifDTO();
        motifDTO.setId(motif.getId());
        motifDTO.setDesignation(motif.getDesignation());
        motifDTO.setDesignation_sec(motif.getDesignation_sec());
        return motifDTO;
    }

    public static Collection<MotifDTO> lazymotifToMotifDTOs(Collection<Motif> motifs) {
        List<MotifDTO> motifsDTO = new ArrayList<>();
        motifs.forEach(x -> {
            motifsDTO.add(lazymotifToMotifDTO(x));
        });
        return motifsDTO;
    }
}
