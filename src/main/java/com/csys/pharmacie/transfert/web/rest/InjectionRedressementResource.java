package com.csys.pharmacie.transfert.web.rest;

import com.csys.pharmacie.transfert.dto.FactureBEDTO;
import com.csys.pharmacie.transfert.service.InjectionRedressement;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing MotifRedressement.
 */
@RestController
@RequestMapping("/injectionRedressement")
public class InjectionRedressementResource {

   @Autowired
    private InjectionRedressement injectionRedressement;


    
    @PostMapping
    ResponseEntity<List<FactureBEDTO>> addRedressement() throws MethodArgumentNotValidException, URISyntaxException, ParseException {
        List<FactureBEDTO> result = injectionRedressement.addBonRedressement();
        return ResponseEntity.created(new URI("/api/injectionRedressement/")).body(result);
    }
}

