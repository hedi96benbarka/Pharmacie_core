package com.csys.pharmacie.transfert.web.rest;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.transfert.domain.ResteRecuperationPK;
import com.csys.pharmacie.transfert.dto.DemandeTransfRecupereeDTO;
import com.csys.pharmacie.transfert.dto.ResteRecuperationDTO;
import com.csys.pharmacie.transfert.service.DemandeTransfRecupereeService;
import com.csys.pharmacie.transfert.service.ResteRecuperationService;
import com.csys.pharmacie.vente.quittance.dto.MvtstoDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing ResteRecuperation.
 */
@RestController
@RequestMapping("/api")
public class DemandeTransfertRecupRessource {

    private static final String ENTITY_NAME = "DemandeTransfertRecup";

    private final DemandeTransfRecupereeService demandeTransfRecupereeService;

    private final Logger log = LoggerFactory.getLogger(ResteRecuperationService.class);

    public DemandeTransfertRecupRessource(DemandeTransfRecupereeService demandeTransfRecupereeService) {
        this.demandeTransfRecupereeService = demandeTransfRecupereeService;
    }

    @ApiOperation(value = "Search recoverd  transfer demands.")
    @PostMapping("/recoverd-demande-transfert/searches")
    public Collection<DemandeTransfRecupereeDTO> searchDetailsOfDemandes(@ApiParam("list of quittances ids") @RequestBody List<Integer> demandsIDs) {
        log.debug("REST request to search details of quittances  ");

        return demandeTransfRecupereeService.findByDemandeTransfIn(demandsIDs);

    }
}
