package com.csys.pharmacie.prelevement.factory;

import com.csys.pharmacie.prelevement.domain.DetailRetourPrelevement;
import com.csys.pharmacie.prelevement.domain.RetourPrelevement;
import com.csys.pharmacie.prelevement.dto.RetourPrelevementDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class RetourPrelevementFactory {

    public static RetourPrelevementDTO retourPrelevementToRetourPrelevementDTO(RetourPrelevement retourprelevement) {
        RetourPrelevementDTO retourprelevementDTO = new RetourPrelevementDTO();
        retourprelevementDTO.setNumbon(retourprelevement.getNumbon());
        retourprelevementDTO.setCodvend(retourprelevement.getCodvend());
        retourprelevementDTO.setDatbon(retourprelevement.getDatbon());
        Instant instant = retourprelevementDTO.getDatbon().atZone(ZoneId.systemDefault()).toInstant();
        retourprelevementDTO.setDatebonEdition(Date.from(instant));

        retourprelevementDTO.setDatesys(retourprelevement.getDatesys());
        retourprelevementDTO.setHeuresys(retourprelevement.getHeuresys());
        retourprelevementDTO.setTypbon(retourprelevement.getTypbon());
        retourprelevementDTO.setNumaffiche(retourprelevement.getNumaffiche());
        retourprelevementDTO.setCategDepot(retourprelevement.getCategDepot());
        retourprelevementDTO.setMontantFac(retourprelevement.getMontantFac());
        retourprelevementDTO.setMntbon(retourprelevement.getMntbon());
        retourprelevementDTO.setRemarque(retourprelevement.getRemarque());
        retourprelevementDTO.setCoddepartSrc(retourprelevement.getCoddepartSrc());
        retourprelevementDTO.setCoddepDesti(retourprelevement.getCoddepDesti());
        retourprelevementDTO.setDetailRetourPrelevementDTO(DetailRetourPrelevementFactory.detailretourprelevementToDetailRetourPrelevementDTOs(retourprelevement.getLiseDetailRetourPrelevement()));
        return retourprelevementDTO;
    }

    public static RetourPrelevementDTO retourPrelevementToRetourPrelevementDTOLazy(RetourPrelevement retourprelevement) {
        RetourPrelevementDTO retourprelevementDTO = new RetourPrelevementDTO();
        retourprelevementDTO.setNumbon(retourprelevement.getNumbon());
        retourprelevementDTO.setCodvend(retourprelevement.getCodvend());
        retourprelevementDTO.setDatbon(retourprelevement.getDatbon());
        retourprelevementDTO.setDatesys(retourprelevement.getDatesys());
        retourprelevementDTO.setHeuresys(retourprelevement.getHeuresys());
        retourprelevementDTO.setTypbon(retourprelevement.getTypbon());
        retourprelevementDTO.setNumaffiche(retourprelevement.getNumaffiche());
        retourprelevementDTO.setCategDepot(retourprelevement.getCategDepot());
        retourprelevementDTO.setMontantFac(retourprelevement.getMontantFac());
        retourprelevementDTO.setMntbon(retourprelevement.getMntbon());
        retourprelevementDTO.setRemarque(retourprelevement.getRemarque());
        retourprelevementDTO.setCoddepartSrc(retourprelevement.getCoddepartSrc());
        retourprelevementDTO.setCoddepDesti(retourprelevement.getCoddepDesti());
//        retourprelevementDTO.setDetailRetourPrelevementDTO(DetailRetourPrelevementFactory.detailretourprelevementToDetailRetourPrelevementDTOs(retourprelevement.getLiseDetailRetourPrelevement()));
        return retourprelevementDTO;
    }

    public static RetourPrelevement retourPrelevementDTOToRetourPrelevementLazy(RetourPrelevementDTO retourprelevementDTO) {
        RetourPrelevement retourprelevement = new RetourPrelevement();
        retourprelevement.setNumbon(retourprelevementDTO.getNumbon());
        retourprelevement.setCodvend(retourprelevementDTO.getCodvend());
        retourprelevement.setDatbon(retourprelevementDTO.getDatbon());
        retourprelevement.setDatesys(retourprelevementDTO.getDatesys());
        retourprelevement.setHeuresys(retourprelevementDTO.getHeuresys());
        retourprelevement.setTypbon(retourprelevementDTO.getTypbon());
        retourprelevement.setNumaffiche(retourprelevementDTO.getNumaffiche());
        retourprelevement.setCategDepot(retourprelevementDTO.getCategDepot());
        retourprelevement.setMontantFac(retourprelevementDTO.getMontantFac());
        retourprelevement.setMntbon(retourprelevementDTO.getMntbon());
        retourprelevement.setRemarque(retourprelevementDTO.getRemarque());
        retourprelevement.setCoddepartSrc(retourprelevementDTO.getCoddepartSrc());
        retourprelevement.setCoddepDesti(retourprelevementDTO.getCoddepDesti());
        return retourprelevement;
    }

    public static RetourPrelevement retourPrelevementDTOToRetourPrelevement(RetourPrelevementDTO retourprelevementDTO) {
        RetourPrelevement retourprelevement = new RetourPrelevement();
        retourprelevement.setNumbon(retourprelevementDTO.getNumbon());
        retourprelevement.setCodvend(retourprelevementDTO.getCodvend());
        retourprelevement.setDatbon(retourprelevementDTO.getDatbon());
        retourprelevement.setDatesys(retourprelevementDTO.getDatesys());
        retourprelevement.setHeuresys(retourprelevementDTO.getHeuresys());
        retourprelevement.setTypbon(retourprelevementDTO.getTypbon());
        retourprelevement.setNumaffiche(retourprelevementDTO.getNumaffiche());
        retourprelevement.setCategDepot(retourprelevementDTO.getCategDepot());
        retourprelevement.setMontantFac(retourprelevementDTO.getMontantFac());
        retourprelevement.setMntbon(retourprelevementDTO.getMntbon());
        retourprelevement.setRemarque(retourprelevementDTO.getRemarque());
        retourprelevement.setCoddepartSrc(retourprelevementDTO.getCoddepartSrc());
        retourprelevement.setCoddepDesti(retourprelevementDTO.getCoddepDesti());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateDebut = LocalDateTime.parse(retourprelevementDTO.getDateDebut(), formatter);
        LocalDateTime dateFin = LocalDateTime.parse(retourprelevementDTO.getDateFin(), formatter);

        retourprelevement.setDateDebut(dateDebut);
        retourprelevement.setDateFin(dateFin);

        List<DetailRetourPrelevement> detailretourprelevements = new ArrayList<>();
        
        retourprelevementDTO.getDetailRetourPrelevementDTO().forEach(x -> {
            DetailRetourPrelevement detail = DetailRetourPrelevementFactory.detailretourprelevementDTOToDetailRetourPrelevement(x);
            detail.setRetourPrelevement(retourprelevement);
            detailretourprelevements.add(detail);
        });


    retourprelevement.setLiseDetailRetourPrelevement (detailretourprelevements);
    return retourprelevement ;
}

public static List<RetourPrelevementDTO> retourPrelevementToRetourPrelevementDTOs(List<RetourPrelevement> retourprelevements) {
        List<RetourPrelevementDTO> retourprelevementsDTO = new ArrayList<>();
        retourprelevements.forEach(x -> {
            retourprelevementsDTO.add(retourPrelevementToRetourPrelevementDTO(x));
        });
        return retourprelevementsDTO;
    }
}
