package com.csys.pharmacie.vente.web.rest;

import com.csys.pharmacie.achat.web.rest.*;
import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
import com.csys.util.RestPreconditions;
import com.csys.pharmacie.achat.domain.ReceptionDetailCAPK;
import com.csys.pharmacie.achat.dto.ReceptionDetailCADTO;
import com.csys.pharmacie.achat.service.ReceptionDetailCAService;
import com.csys.pharmacie.vente.dto.PMPArticleDTO;
import com.csys.pharmacie.vente.dto.PrixReferenceArticleDTO;
import com.csys.pharmacie.vente.service.PricingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.lang.String;
import java.lang.Void;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing ReceptionDetailCA.
 */
@RestController
@RequestMapping("/api")
public class PricingResource {

    private static final String ENTITY_NAME = "price";

    private final PricingService pricingService;

    private final Logger log = LoggerFactory.getLogger(ReceptionDetailCAService.class);

    public PricingResource(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    /**
     * POST /prices : Create a new price.
     *
     * @param priceDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new price, or with status 400 (Bad Request) if the price has already an
     * ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
//    @PostMapping("/prices")
//    public ResponseEntity<ReceptionDetailCADTO> createReceptionDetailCA(@Valid @RequestBody ReceptionDetailCADTO priceDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
//        log.debug("REST request to save ReceptionDetailCA : {}", priceDTO);
//        if (priceDTO.getReceptionDetailCaPK() != null) {
//            bindingResult.addError(new FieldError("ReceptionDetailCADTO", "receptionDetailCaPK", "POST method does not accepte " + ENTITY_NAME + " with code"));
//            throw new MethodArgumentNotValidException(null, bindingResult);
//        }
//        if (bindingResult.hasErrors()) {
//            throw new MethodArgumentNotValidException(null, bindingResult);
//        }
//        ReceptionDetailCADTO result = priceService.save(priceDTO);
//        return ResponseEntity.created(new URI("/api/prices/" + result.getReceptionDetailCaPK())).body(result);
//    }
    /**
     * PUT /prices : Updates an existing price.
     *
     * @param id
     * @param priceDTO the price to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * price, or with status 400 (Bad Request) if the price is not valid, or
     * with status 500 (Internal Server Error) if the price couldn't be updated
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
//    @PutMapping("/prices/{id}")
//    public ResponseEntity<ReceptionDetailCADTO> updateReceptionDetailCA(@PathVariable ReceptionDetailCAPK id, @Valid @RequestBody ReceptionDetailCADTO priceDTO) throws MethodArgumentNotValidException {
//        log.debug("Request to update ReceptionDetailCA: {}", id);
//        priceDTO.setReceptionDetailCaPK(id);
//        ReceptionDetailCADTO result = priceService.update(priceDTO);
//        return ResponseEntity.ok().body(result);
//    }
    /**
     * GET /reference-price/{id} : get the reference-price by article id.
     *
     * @param id the article id
     * @return the ResponseEntity with status 200 (OK) and with body of price,
     * or with status 404 (Not Found)
     */
    @GetMapping("/reference-price/{id}")
    public PrixReferenceArticleDTO findReferencePrice(@PathVariable Integer id) {
        log.debug("Request to get ReceptionDetailCA: {}", id);
        return pricingService.findReferencePriceByArticle(id);

    }

    /**
     * POST /reference-price : search the reference prices by articles ids.
     *
     * @param codes
     * @return the ResponseEntity with status 200 (OK) and with body of price,
     * or with status 404 (Not Found)
     */
    @ApiOperation(value = "getting list of price reference for items UU having codes in the list")
    @PostMapping("/reference-price")
    public List<PrixReferenceArticleDTO> searchReferencePrice(
            @ApiParam(value = "List of codes of the articles. It must not be null. If empty it will return all the reference prices ")
            @RequestBody(required = false) Integer[] codes
    ) {

        return pricingService.findReferencePricesByArticleIn(codes);

    }

    @ApiOperation(value = "save price reference for item UU")
    @PostMapping("/reference-price/item")
    public PrixReferenceArticleDTO addReferencePrice(
            @RequestBody PrixReferenceArticleDTO prixReferenceArticleDTO) {
        return pricingService.save(prixReferenceArticleDTO);

    }
    @ApiOperation(value = "save pmp for item UU")
    @PostMapping("/pmp/item")
    public PMPArticleDTO addPMP(
            @RequestBody PMPArticleDTO pmpDTO) {
        return pricingService.savePMP(pmpDTO);

    }

    @PutMapping("/reference-price")
    public PrixReferenceArticleDTO updateReferencePrice(
            @RequestBody PrixReferenceArticleDTO prixReferenceArticleDTO) {
        log.debug("REST request to update PrixReferenceArticleDTO : {}", prixReferenceArticleDTO);
        return pricingService.save(prixReferenceArticleDTO);

    }
    
     @PutMapping("/reference-price/batch")
    public Boolean updateListReferencePrice(
            @RequestBody List<PrixReferenceArticleDTO> listePrixReferenceArticleDTO) {
        log.debug("REST request to update liste PrixReferenceArticleDTO : {}", listePrixReferenceArticleDTO);
        return pricingService.updateListReferencePrice(listePrixReferenceArticleDTO);

    }
   
        @PutMapping("/pmp")
    public PMPArticleDTO updatePMP(
            @RequestBody PMPArticleDTO pMPArticleDTO) {
        log.debug("REST request to update pMPArticleDTO : {}", pMPArticleDTO);
        return pricingService.savePMP(pMPArticleDTO);

    }

    /**
     * GET /pmp/{id} : get the pmp by article id
     *
     * @param id
     * @return the ResponseEntity with status 200 (OK) and the list of prices in
     * body
     */
    @GetMapping("/pmp/{id}")
    public PMPArticleDTO findPMP(@PathVariable Integer id) {
        log.debug("Request to get all  ReceptionDetailCAs : {}");
        return pricingService.findPMPByArticle(id);
    }

    /**
     * POST /pmp : search the pmps by articles ids
     *
     * @param codes
     * @return the ResponseEntity with status 200 (OK) and the list of prices in
     * body
     */
    @PostMapping("/pmp")
    public List<PMPArticleDTO> searchPMP(
            @ApiParam(value = "List of codes of the articles. It must not be null. If empty it will return all the pmps ")
            @RequestBody(required = false) Integer[] codes) {
        log.debug("Request to get all  ReceptionDetailCAs : {}");
        return pricingService.findPMPsByArticleIn(codes);
    }
    
//    /**
//     * DELETE /prices/{id} : delete the "id" price.
//     *
//     * @param id the id of the price to delete
//     * @return the ResponseEntity with status 200 (OK)
//     */
//    @DeleteMapping("/received-purchase-order-details/{id}")
//    public ResponseEntity<Void> deleteReceptionDetailCA(@PathVariable ReceptionDetailCAPK id) {
//        log.debug("Request to delete ReceptionDetailCA: {}", id);
//        priceService.delete(id);
//        return ResponseEntity.ok().build();
//    }
}
