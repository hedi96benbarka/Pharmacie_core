package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.dto.FcptfrsPHdto;
import com.csys.pharmacie.achat.dto.ListeFcptFrsPHDTOWrapper;
import com.csys.pharmacie.achat.service.FcptFrsPHService;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing FcptfrsPH.
 */
@RestController
@RequestMapping("/api")
public class FcptfrsPHResource {

    private static final String ENTITY_NAME = "fcptfrsph";

    private final FcptFrsPHService fcptfrsphService;

    private final Logger log = LoggerFactory.getLogger(FcptfrsPHResource.class);

    public FcptfrsPHResource(FcptFrsPHService fcptfrsphService) {
        this.fcptfrsphService = fcptfrsphService;
    }

    /**
     * POST /fcptfrsphs : Create a new fcptfrsph.
     *
     * @param fcptfrsphDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new fcptfrsph, or with status 400 (Bad Request) if the fcptfrsph has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/fcptfrsphs")
    public ResponseEntity<FcptfrsPHdto> createFcptfrsPH(@Valid @RequestBody FcptfrsPHdto fcptfrsphDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save FcptfrsPH : {}", fcptfrsphDTO);
        if (fcptfrsphDTO.getNumOpr() != null) {
            bindingResult.addError(new FieldError("FcptfrsPHDTO", "numOpr", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        FcptfrsPHdto result = fcptfrsphService.save(fcptfrsphDTO);
        return ResponseEntity.created(new URI("/api/fcptfrsphs/" + result.getNumOpr())).body(result);
    }

    @PostMapping("/fcptfrsphs/liste")
    public ResponseEntity<ListeFcptFrsPHDTOWrapper> createListeFcptfrsPHOnAvance(@Valid @RequestBody ListeFcptFrsPHDTOWrapper listeFcptFrsPHDTOWrapper, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save ListeFcptFrsPHDTOWrapper : {}", listeFcptFrsPHDTOWrapper);

        ListeFcptFrsPHDTOWrapper result = fcptfrsphService.saveListeListeFcptFrsPHOnAvanceFournisseur(listeFcptFrsPHDTOWrapper);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/fcptfrsphs/deleteByNumOprIn")
    public Boolean deleteFcptfrsPHByNumOPr(@RequestBody(required = true) Long[] numOprs){
    Boolean   result=  fcptfrsphService.deleteByNumOprIn(numOprs);
    return result; 

}

}
