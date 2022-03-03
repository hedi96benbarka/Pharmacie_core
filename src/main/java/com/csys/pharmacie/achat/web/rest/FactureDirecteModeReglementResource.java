package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.domain.FactureDirecteModeReglementPK;
import com.csys.pharmacie.achat.dto.FactureDirecteModeReglementDTO;
import com.csys.pharmacie.achat.dto.ListeFactureDirecteModeReglementDTOWrapper;
import com.csys.pharmacie.achat.service.FactureDirecteModeReglementService;
import com.csys.util.RestPreconditions;
import java.lang.String;
import java.lang.Void;
import java.net.URI;
import java.net.URISyntaxException;
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
 * REST controller for managing FactureDirecteModeReglement.
 */
@RestController
@RequestMapping("/api")
public class FactureDirecteModeReglementResource {

    private static final String ENTITY_NAME = "facturedirectemodereglement";

    private final FactureDirecteModeReglementService factureDirecteModeReglementService;

    private final Logger log = LoggerFactory.getLogger(FactureDirecteModeReglementService.class);

    public FactureDirecteModeReglementResource(FactureDirecteModeReglementService factureDirecteModeReglementService) {
        this.factureDirecteModeReglementService = factureDirecteModeReglementService;
    }

    /**
     * POST /facture-directe-mode-reglements : Create a new
     * facturedirectemodereglement.
     *
     * @param listeFactureDirecteModeReglementDTOWrapper
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new facturedirectemodereglement, or with status 400 (Bad Request) if the
     * facturedirectemodereglement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/facture-directe-mode-reglements")
    public ListeFactureDirecteModeReglementDTOWrapper createFactureDirecteModeReglement(@Valid @RequestBody ListeFactureDirecteModeReglementDTOWrapper listeFactureDirecteModeReglementDTOWrapper, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save FactureDirecteModeReglement : {}", listeFactureDirecteModeReglementDTOWrapper);

        Collection<FactureDirecteModeReglementDTO> listeFactureDirecteModeReglementDTOs = listeFactureDirecteModeReglementDTOWrapper.getModeReglementList();
        listeFactureDirecteModeReglementDTOs.forEach(facturedirectemodereglementDTO -> {
            if (facturedirectemodereglementDTO.getNumBon() == null || facturedirectemodereglementDTO.getCodeMotifPaiement() == null && facturedirectemodereglementDTO.getCodeReglement() == null) {
                bindingResult.addError(new FieldError("FactureDirecteModeReglementDTO", "factureDirecteModeReglementPK", "POST method does not accepte " + ENTITY_NAME + " with numBon or codeMotifPaiementor or codeReglement null"));
            }
        });

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        ListeFactureDirecteModeReglementDTOWrapper result = factureDirecteModeReglementService.save(listeFactureDirecteModeReglementDTOWrapper);
        return result;
    }

    /**
     * GET /facture-directe-mode-reglements/{id} : get the "id"
     * facturedirectemodereglement.
     *
     * @param id the id of the facturedirectemodereglement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * facturedirectemodereglement, or with status 404 (Not Found)
     */
    @GetMapping("/facture-directe-mode-reglements/{id}")
    public ResponseEntity<FactureDirecteModeReglementDTO> getFactureDirecteModeReglement(@PathVariable FactureDirecteModeReglementPK id) {
        log.debug("Request to get FactureDirecteModeReglement: {}", id);
        FactureDirecteModeReglementDTO dto = factureDirecteModeReglementService.findOne(id);
        RestPreconditions.checkFound(dto, "facturedirectemodereglement.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /facture-directe-mode-reglements : get all the
     * facture-directe-mode-reglements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of
     * facture-directe-mode-reglements in body
     */
    @GetMapping("/facture-directe-mode-reglements")
    public Collection<FactureDirecteModeReglementDTO> getAllFactureDirecteModeReglements() {
        log.debug("Request to get all  FactureDirecteModeReglements : {}");
        return factureDirecteModeReglementService.findAll();
    }

    /**
     * DELETE /facture-directe-mode-reglements/{id} : delete the "id"
     * facturedirectemodereglement.
     *
     * @param id the id of the facturedirectemodereglement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/facture-directe-mode-reglements/{id}")
    public ResponseEntity<Void> deleteFactureDirecteModeReglement(@PathVariable FactureDirecteModeReglementPK id) {
        log.debug("Request to delete FactureDirecteModeReglement: {}", id);
        factureDirecteModeReglementService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/facture-directe-mode-reglements/searches")
    public Collection<FactureDirecteModeReglementDTO> findFactureDirecteModeReglementByNumBonsIn(@Valid @RequestBody List<Integer> numBons)
            throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to search CommandeAchatModeReglement : {}", numBons);

        return factureDirecteModeReglementService.findFactureDirecteModeReglementByNumBonsIn(numBons);
    }
}
