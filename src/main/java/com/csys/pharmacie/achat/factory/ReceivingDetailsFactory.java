package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.ReceivingDetails;
import com.csys.pharmacie.achat.dto.ReceivingDetailsDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ReceivingDetailsFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    public static ReceivingDetailsDTO receivingdetailsToReceivingDetailsDTO(ReceivingDetails receivingdetails) {
        ReceivingDetailsDTO receivingdetailsDTO = new ReceivingDetailsDTO();
        receivingdetailsDTO.setCodart(receivingdetails.getCodart());
        receivingdetailsDTO.setReceiving(receivingdetails.getReceiving1().getCode());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            receivingdetailsDTO.setDesart(receivingdetails.getDesartSec());
            receivingdetailsDTO.setDesartSec(receivingdetails.getDesart());
        } else {
            receivingdetailsDTO.setDesart(receivingdetails.getDesart());
            receivingdetailsDTO.setDesartSec(receivingdetails.getDesartSec());
        }
        receivingdetailsDTO.setCodeSaisi(receivingdetails.getCodeSaisi());
        receivingdetailsDTO.setQuantite(receivingdetails.getQuantite());
        receivingdetailsDTO.setLotInter(receivingdetails.getLotInter());
        receivingdetailsDTO.setQuantiteReceiving(receivingdetails.getQuantiteReceiving());
        receivingdetailsDTO.setQuantiteReceivingValide(receivingdetails.getQuantiteValide());
        receivingdetailsDTO.setPrixUnitaire(receivingdetails.getPrixUnitaire());
        receivingdetailsDTO.setCodtva(receivingdetails.getCodtva());
        receivingdetailsDTO.setTautva(receivingdetails.getTautva());
        receivingdetailsDTO.setDelaiLivraison(receivingdetails.getDelaiLivraison());
        receivingdetailsDTO.setMemo(receivingdetails.getMemo());
        receivingdetailsDTO.setRemise(receivingdetails.getRemise());
        receivingdetailsDTO.setDatePréemption(receivingdetails.getDatePer());
        receivingdetailsDTO.setTautva(receivingdetails.getTautva());
        receivingdetailsDTO.setIsCapitalize(receivingdetails.getIsCapitalize());
        receivingdetailsDTO.setQuantiteGratuite(receivingdetails.getQuantiteGratuite());
        receivingdetailsDTO.setFree(receivingdetails.getQuantiteGratuite() != null && receivingdetails.getQuantiteGratuite() > 0 ? Boolean.TRUE : receivingdetails.getIsFree());
        if (Boolean.FALSE.equals(receivingdetailsDTO.getFree())) {
            receivingdetailsDTO.setQuantiteGratuiteRestante(receivingdetails.getQuantiteValide());
        }
        receivingdetailsDTO.setQuantiteRestante(receivingdetails.getQuantiteValide());
        receivingdetailsDTO.setQuantityFromComannde(receivingdetails.getQuantityFromComannde());
        return receivingdetailsDTO;
    }

    public static ReceivingDetails receivingdetailsDTOToReceivingDetails(ReceivingDetailsDTO receivingdetailsDTO) {
        ReceivingDetails receivingdetails = new ReceivingDetails();
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            receivingdetails.setDesart(receivingdetailsDTO.getDesartSec());
            receivingdetails.setDesartSec(receivingdetailsDTO.getDesart());
        } else {
            receivingdetails.setDesart(receivingdetailsDTO.getDesart());
            receivingdetails.setDesartSec(receivingdetailsDTO.getDesartSec());
        }
        receivingdetails.setCodeSaisi(receivingdetailsDTO.getCodeSaisi());
        receivingdetails.setQuantite(receivingdetailsDTO.getQuantite());
        receivingdetails.setLotInter(receivingdetailsDTO.getLotInter());
        receivingdetails.setDatePer(receivingdetailsDTO.getDatePréemption());
        receivingdetails.setQuantiteReceiving(receivingdetailsDTO.getQuantiteReceiving());
        receivingdetails.setQuantiteValide(receivingdetailsDTO.getQuantiteReceivingValide());
        receivingdetails.setPrixUnitaire(receivingdetailsDTO.getPrixUnitaire());
        receivingdetails.setCodtva(receivingdetailsDTO.getCodtva());
        receivingdetails.setTautva(receivingdetailsDTO.getTautva());
        receivingdetails.setDelaiLivraison(receivingdetailsDTO.getDelaiLivraison());
        receivingdetails.setMemo(receivingdetailsDTO.getMemo());
        receivingdetails.setRemise(receivingdetailsDTO.getRemise());
        receivingdetails.setQuantiteGratuite(receivingdetailsDTO.getQuantiteGratuite());
        receivingdetails.setIsFree(receivingdetailsDTO.getFree());
        receivingdetails.setIsCapitalize(receivingdetailsDTO.getIsCapitalize());
        receivingdetails.setAppelOffre(receivingdetailsDTO.getIsAppelOffre());
        receivingdetails.setQuantityFromComannde(receivingdetailsDTO.getQuantityFromComannde());
        return receivingdetails;
    }

    public static List<ReceivingDetailsDTO> receivingdetailsToReceivingDetailsDTOs(List<ReceivingDetails> receivingdetailss) {
        List<ReceivingDetailsDTO> receivingdetailssDTO = new ArrayList<>();
        receivingdetailss.forEach(x -> {
            receivingdetailssDTO.add(receivingdetailsToReceivingDetailsDTO(x));
        });
        return receivingdetailssDTO;
    }

    public static List<ReceivingDetails> ReceivingDetailsDTOToreceivingdetailss(List<ReceivingDetailsDTO> receivingdetailsDTOs) {
        List<ReceivingDetails> receivingdetails = new ArrayList<>();
        receivingdetailsDTOs.forEach(x -> {
            receivingdetails.add(receivingdetailsDTOToReceivingDetails(x));
        });
        return receivingdetails;
    }
}
