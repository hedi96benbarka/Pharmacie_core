package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.LeadTimeProcurement;
import com.csys.pharmacie.achat.dto.LeadTimeProcurementDTO;
import com.csys.pharmacie.achat.factory.LeadTimeProcurementFactory;
import com.csys.pharmacie.achat.repository.LeadTimeProcurementRepository;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.stock.domain.ArticleReorderPoint;
import com.csys.pharmacie.stock.domain.QArticleReorderPoint;
import com.csys.pharmacie.stock.dto.ArticleReorderPointDTO;
import com.csys.pharmacie.stock.factory.ArticleReorderPointFactory;
import com.google.common.base.Preconditions;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LeadTimeProcurementService {

    private final Logger log = LoggerFactory.getLogger(LeadTimeProcurementService.class);

    private final LeadTimeProcurementRepository leadTimeProcurementRepository;

    public LeadTimeProcurementService(LeadTimeProcurementRepository leadtimeprocurementRepository) {
        this.leadTimeProcurementRepository = leadtimeprocurementRepository;
    }

    @Transactional(
            readOnly = true
    )
    public LeadTimeProcurementDTO findOne(String id) {
        log.debug("Request to get LeadTimeProcurement: {}", id);
        LeadTimeProcurement leadtimeprocurement = leadTimeProcurementRepository.findOne(id);
        Preconditions.checkArgument(leadtimeprocurement != null, "LeadTimeProcurement does not exist");
        LeadTimeProcurementDTO dto = LeadTimeProcurementFactory.leadtimeprocurementToLeadTimeProcurementDTO(leadtimeprocurement);
        return dto;
    }

    @Transactional(
            readOnly = true
    )
    public LeadTimeProcurement findLeadTimeProcurement(String id) {
        log.debug("Request to get LeadTimeProcurement: {}", id);
        LeadTimeProcurement leadtimeprocurement = leadTimeProcurementRepository.findOne(id);
        Preconditions.checkArgument(leadtimeprocurement != null, "LeadTimeProcurement does not exist");
        return leadtimeprocurement;
    }

//  @Transactional(
//      readOnly = true
//  )
//  public Collection<LeadTimeProcurementDTO> findAll() {
//    log.debug("Request to get All LeadTimeProcurements");
//    Collection<LeadTimeProcurement> result= leadTimeProcurementRepository.findAll();
//    return LeadTimeProcurementFactory.leadtimeprocurementToLeadTimeProcurementDTOs(result);
//  }
    @Transactional(
            readOnly = true
    )
    public Collection<LeadTimeProcurementDTO> findAll(Date fromDate, Date toDate, CategorieDepotEnum categDepot) {
        log.debug("Request to get All LeadTimeProcurements");
        toDate = new Date();
        Collection<LeadTimeProcurement> result = leadTimeProcurementRepository.findByDateValidateDaBetweenAndCategorieArticle(fromDate, toDate, categDepot.toString());
        return LeadTimeProcurementFactory.leadtimeprocurementToLeadTimeProcurementDTOs(result);
    }

    public List<LeadTimeProcurementDTO> findLeadTimeProcurementByArticleUnite(Date fromDate, Date toDate, CategorieDepotEnum categDepot) {
        log.debug("Request to get All LeadTimeProcurements");
        toDate = new Date();
        return leadTimeProcurementRepository.findLeadTimeProcurementByArticleUnite(fromDate, toDate, categDepot.toString());

    }
}
