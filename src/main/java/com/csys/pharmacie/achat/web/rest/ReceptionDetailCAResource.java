package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
import com.csys.util.RestPreconditions;
import com.csys.pharmacie.achat.domain.ReceptionDetailCAPK;
import com.csys.pharmacie.achat.dto.ReceptionDetailCADTO;
import com.csys.pharmacie.achat.service.ReceptionDetailCAService;
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
public class ReceptionDetailCAResource {

    private static final String ENTITY_NAME = "receptiondetailca";

    private final ReceptionDetailCAService receptiondetailcaService;

    private final Logger log = LoggerFactory.getLogger(ReceptionDetailCAService.class);

    public ReceptionDetailCAResource(ReceptionDetailCAService receptiondetailcaService) {
        this.receptiondetailcaService = receptiondetailcaService;
    }

    /**
     * POST /receptiondetailcas : Create a new receptiondetailca.
     *
     * @param receptiondetailcaDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new receptiondetailca, or with status 400 (Bad Request) if the
     * receptiondetailca has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
//    @PostMapping("/receptiondetailcas")
//    public ResponseEntity<ReceptionDetailCADTO> createReceptionDetailCA(@Valid @RequestBody ReceptionDetailCADTO receptiondetailcaDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
//        log.debug("REST request to save ReceptionDetailCA : {}", receptiondetailcaDTO);
//        if (receptiondetailcaDTO.getReceptionDetailCaPK() != null) {
//            bindingResult.addError(new FieldError("ReceptionDetailCADTO", "receptionDetailCaPK", "POST method does not accepte " + ENTITY_NAME + " with code"));
//            throw new MethodArgumentNotValidException(null, bindingResult);
//        }
//        if (bindingResult.hasErrors()) {
//            throw new MethodArgumentNotValidException(null, bindingResult);
//        }
//        ReceptionDetailCADTO result = receptiondetailcaService.save(receptiondetailcaDTO);
//        return ResponseEntity.created(new URI("/api/receptiondetailcas/" + result.getReceptionDetailCaPK())).body(result);
//    }
    /**
     * PUT /receptiondetailcas : Updates an existing receptiondetailca.
     *
     * @param id
     * @param receptiondetailcaDTO the receptiondetailca to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * receptiondetailca, or with status 400 (Bad Request) if the
     * receptiondetailca is not valid, or with status 500 (Internal Server
     * Error) if the receptiondetailca couldn't be updated
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
//    @PutMapping("/receptiondetailcas/{id}")
//    public ResponseEntity<ReceptionDetailCADTO> updateReceptionDetailCA(@PathVariable ReceptionDetailCAPK id, @Valid @RequestBody ReceptionDetailCADTO receptiondetailcaDTO) throws MethodArgumentNotValidException {
//        log.debug("Request to update ReceptionDetailCA: {}", id);
//        receptiondetailcaDTO.setReceptionDetailCaPK(id);
//        ReceptionDetailCADTO result = receptiondetailcaService.update(receptiondetailcaDTO);
//        return ResponseEntity.ok().body(result);
//    }
    /**
     * GET /receptiondetailcas/{id} : get the "id" receptiondetailca.
     *
     * @param id the id of the receptiondetailca to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * receptiondetailca, or with status 404 (Not Found)
     */
    @GetMapping("/received-purchase-order-details/{id}")
    public List<ReceptionDetailCADTO> getReceptionDetailCA(@PathVariable Integer id) {
        log.debug("Request to get ReceptionDetailCA: {}", id);
        return receptiondetailcaService.findRecivedDetailCAByCommandeAchat(id);

    }

    @ApiOperation("search recived articles by code of purchase order and article code")
    @PostMapping("/received-purchase-order-details")
    public List<ReceptionDetailCADTO> getReceptionDetailCA(@ApiParam("Only set the purchase order id and the aticle id that you are looking for.")
    @RequestBody List<ReceptionDetailCADTO> id) {
        log.debug("Request to get ReceptionDetailCA: {}", id);
        return receptiondetailcaService.findRecivedDetailCAByCAsAndArticleIds(id);

    }

    /**
     * GET /receptiondetailcas : get all the receptiondetailcas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of
     * receptiondetailcas in body
     */
    @GetMapping("/received-purchase-order-details")
    public Collection<ReceptionDetailCADTO> getAllReceptionDetailCAs() {
        log.debug("Request to get all  ReceptionDetailCAs : {}");
        return receptiondetailcaService.findAll();
    }

    /**
     * DELETE /receptiondetailcas/{id} : delete the "id" receptiondetailca.
     *
     * @param id the id of the receptiondetailca to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/received-purchase-order-details/{id}")
    public ResponseEntity<Void> deleteReceptionDetailCA(@PathVariable ReceptionDetailCAPK id) {
        log.debug("Request to delete ReceptionDetailCA: {}", id);
        receptiondetailcaService.delete(id);
        return ResponseEntity.ok().build();
    }
}
