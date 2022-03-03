package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.dto.LeadTimeProcurementDTO;
import com.csys.pharmacie.achat.service.LeadTimeProcurementService;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LeadTimeProcurementResource {

    private static final String ENTITY_NAME = "leadtimeprocurement";

    private final LeadTimeProcurementService leadTimeProcurementService;

    private final Logger log = LoggerFactory.getLogger(LeadTimeProcurementService.class);

    public LeadTimeProcurementResource(LeadTimeProcurementService leadtimeprocurementService) {
        this.leadTimeProcurementService = leadtimeprocurementService;
    }

    @GetMapping("/lead-time-procurements/{id}")
    public ResponseEntity<LeadTimeProcurementDTO> getLeadTimeProcurement(@PathVariable String id) {
        log.debug("Request to get LeadTimeProcurement: {}", id);
        LeadTimeProcurementDTO dto = leadTimeProcurementService.findOne(id);
        return ResponseEntity.ok().body(dto);
    }

//    @GetMapping("/lead-time-procurements")
//    public Collection<LeadTimeProcurementDTO> getAllLeadTimeProcurements() {
//        log.debug("Request to get all  LeadTimeProcurements : {}");
//        return leadtimeprocurementService.findAll();
//    }
    @GetMapping("/lead-time-procurements/filter")
    public ResponseEntity<Collection<LeadTimeProcurementDTO>> findAll(
            @RequestParam(name = "categorieDepot", required = true) CategorieDepotEnum categorieDepot,
            @RequestParam(name = "fromDate", required = true) Date fromDate,
            @RequestParam(name = "toDate", required = true) Date toDate) {
        Collection<LeadTimeProcurementDTO> dto = leadTimeProcurementService.findAll(fromDate, toDate, categorieDepot);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/lead-time-procurements/filter-by-article-unite")
    public ResponseEntity<List<LeadTimeProcurementDTO>> findLeadTimeProcurementByArticleUnite(
            @RequestParam(name = "categorieDepot", required = true) CategorieDepotEnum categorieDepot,
            @RequestParam(name = "fromDate", required = false) String fromDateStr,
            @RequestParam(name = "toDate", required = false) String toDateStr) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fromDate = formatter.parse(fromDateStr);
        Date toDate = formatter.parse(toDateStr);
        List<LeadTimeProcurementDTO> dto = leadTimeProcurementService.findLeadTimeProcurementByArticleUnite(fromDate, toDate, categorieDepot);
        return ResponseEntity.ok().body(dto);
    }
}
