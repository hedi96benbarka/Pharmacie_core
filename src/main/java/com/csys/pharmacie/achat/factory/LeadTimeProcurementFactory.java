package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.LeadTimeProcurement;
import com.csys.pharmacie.achat.dto.LeadTimeProcurementDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LeadTimeProcurementFactory {
  public static LeadTimeProcurementDTO leadtimeprocurementToLeadTimeProcurementDTO(LeadTimeProcurement leadtimeprocurement) {
    LeadTimeProcurementDTO leadtimeprocurementDTO=new LeadTimeProcurementDTO();
    leadtimeprocurementDTO.setId(leadtimeprocurement.getId());
    leadtimeprocurementDTO.setDifferenceDaReception(leadtimeprocurement.getDifferenceDaReception());
    leadtimeprocurementDTO.setCodeArticle(leadtimeprocurement.getCodeArticle());
    leadtimeprocurementDTO.setCategorieArticle(leadtimeprocurement.getCategorieArticle());
    leadtimeprocurementDTO.setCodeUnite(leadtimeprocurement.getCodeUnite());
    leadtimeprocurementDTO.setCodeDa(leadtimeprocurement.getCodeDa());
    leadtimeprocurementDTO.setDateValidateDa(leadtimeprocurement.getDateValidateDa());
    leadtimeprocurementDTO.setNumAfficheReception(leadtimeprocurement.getNumAfficheReception());
    leadtimeprocurementDTO.setDateBonReception(leadtimeprocurement.getDateBonReception());
    return leadtimeprocurementDTO;
  }

  public static LeadTimeProcurement leadtimeprocurementDTOToLeadTimeProcurement(LeadTimeProcurementDTO leadtimeprocurementDTO) {
    LeadTimeProcurement leadtimeprocurement=new LeadTimeProcurement();
    leadtimeprocurement.setId(leadtimeprocurementDTO.getId());
    leadtimeprocurement.setDifferenceDaReception(leadtimeprocurementDTO.getDifferenceDaReception());
    leadtimeprocurement.setCodeArticle(leadtimeprocurementDTO.getCodeArticle());
    leadtimeprocurement.setCategorieArticle(leadtimeprocurementDTO.getCategorieArticle());
    leadtimeprocurement.setCodeUnite(leadtimeprocurementDTO.getCodeUnite());
    leadtimeprocurement.setCodeDa(leadtimeprocurementDTO.getCodeDa());
    leadtimeprocurement.setDateValidateDa(leadtimeprocurementDTO.getDateValidateDa());
    leadtimeprocurement.setNumAfficheReception(leadtimeprocurementDTO.getNumAfficheReception());
    leadtimeprocurement.setDateBonReception(leadtimeprocurementDTO.getDateBonReception());
    return leadtimeprocurement;
  }

  public static Collection<LeadTimeProcurementDTO> leadtimeprocurementToLeadTimeProcurementDTOs(Collection<LeadTimeProcurement> leadtimeprocurements) {
    List<LeadTimeProcurementDTO> leadtimeprocurementsDTO=new ArrayList<>();
    leadtimeprocurements.forEach(x -> {
      leadtimeprocurementsDTO.add(leadtimeprocurementToLeadTimeProcurementDTO(x));
    } );
    return leadtimeprocurementsDTO;
  }
}

