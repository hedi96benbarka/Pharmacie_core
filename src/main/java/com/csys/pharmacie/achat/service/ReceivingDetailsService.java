//package com.csys.pharmacie.achat.service;
//
//import com.csys.exception.IllegalBusinessLogiqueException;
//import com.csys.pharmacie.achat.domain.ReceivingDetails;
//import com.csys.pharmacie.achat.dto.ArticleDTO;
//import com.csys.pharmacie.achat.dto.ArticlePHDTO;
//import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
//import com.csys.pharmacie.achat.dto.ReceivingDetailsDTO;
//import com.csys.pharmacie.achat.factory.ReceivingDetailsFactory;
//import com.csys.pharmacie.achat.repository.ReceivingDetailsRepository;
//import com.csys.pharmacie.client.service.ParamAchatServiceClient;
//import com.csys.pharmacie.helper.CategorieDepotEnum;
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Service Implementation for managing ReceivingDetails.
// */
//@Service
//@Transactional
//public class ReceivingDetailsService {
//
//    private final Logger log = LoggerFactory.getLogger(ReceivingDetailsService.class);
//
//    private final ReceivingDetailsRepository receivingdetailsRepository;
//      private final ParamAchatServiceClient paramAchatServiceClient;
//
//    public ReceivingDetailsService(ReceivingDetailsRepository receivingdetailsRepository,ParamAchatServiceClient paramAchatServiceClient) {
//        this.receivingdetailsRepository = receivingdetailsRepository;
//        this.paramAchatServiceClient=paramAchatServiceClient;
//                
//    }
//
//    /**
//     * Get all the receivingdetailss.
//     *
//     * @return the the list of entities
//     */
//    @Transactional(
//            readOnly = true
//    )
//    public List<ReceivingDetailsDTO> findAll() {
//        log.debug("Request to get All ReceivingDetailss");
//        List<ReceivingDetails> result = receivingdetailsRepository.findAll();
//        return ReceivingDetailsFactory.receivingdetailsToReceivingDetailsDTOs(result);
//    }
//    
//
//}
