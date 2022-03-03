//package com.csys.pharmacie.achat.web.rest;
//
//import com.csys.pharmacie.achat.dto.ReceivingDetailsDTO;
//import com.csys.pharmacie.achat.service.ReceivingDetailsService;
//import java.lang.String;
//import java.util.Collection;
//import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * REST controller for managing ReceivingDetails.
// */
//@RestController
//@RequestMapping("/api")
//public class ReceivingDetailsResource {
//
//    private static final String ENTITY_NAME = "receivingdetails";
//
//    private final ReceivingDetailsService receivingdetailsService;
//
//    private final Logger log = LoggerFactory.getLogger(ReceivingDetailsService.class);
//
//    public ReceivingDetailsResource(ReceivingDetailsService receivingdetailsService) {
//        this.receivingdetailsService = receivingdetailsService;
//    }
//
//    /**
//     * GET /receivingdetailss : get all the receivingdetailss.
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of
//     * receivingdetailss in body
//     */
//    @Deprecated
//    @GetMapping("/receivingdetailss")
//    public Collection<ReceivingDetailsDTO> getAllReceivingDetailss() {
//        log.debug("Request to get all  ReceivingDetailss : {}");
//        return receivingdetailsService.findAll();
//    }
//    
//
//}
