package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.dto.ReceptionTemporaireDTO;
import com.csys.pharmacie.achat.service.FactureBAService;
import com.csys.pharmacie.achat.service.ReceptionTemporaireService;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReceptionTemporaireResource {

    private static final String ENTITY_NAME = "receptiontemporaire";

    private final ReceptionTemporaireService receptiontemporaireService;
    private final FactureBAService factureBAService;

    private final Logger log = LoggerFactory.getLogger(ReceptionTemporaireService.class);

    public ReceptionTemporaireResource(ReceptionTemporaireService receptiontemporaireService, FactureBAService factureBAService) {
        this.receptiontemporaireService = receptiontemporaireService;
        this.factureBAService = factureBAService;
    }

    @PostMapping("/reception-temporaires")
    public ResponseEntity<ReceptionTemporaireDTO> createReceptionTemporaire(@Valid @RequestBody ReceptionTemporaireDTO receptiontemporaireDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save ReceptionTemporaire : {}", receptiontemporaireDTO);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        log.debug("receptiontemporaireDTO.getNumbon() estv {}", receptiontemporaireDTO.getNumbon());
        if (receptiontemporaireDTO.getNumbon() != null) {
            bindingResult.addError(new FieldError("ReceptionTemporaireDTO", "numbon", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        ReceptionTemporaireDTO result = receptiontemporaireService.save(receptiontemporaireDTO);
        return ResponseEntity.created(new URI("/api/receptiontemporaires/" + result.getNumbon())).body(result);
    }

//    @PutMapping("/reception-temporaires")
//    public ResponseEntity<ReceptionTemporaireDTO> updateReceptionTemporaire(@Valid @RequestBody ReceptionTemporaireDTO receptiontemporaireDTO, BindingResult bindingResult) throws MethodArgumentNotValidException {
//        log.debug("Request to update ReceptionTemporaire: {}", receptiontemporaireDTO);
//        if (bindingResult.hasErrors()) {
//            throw new MethodArgumentNotValidException(null, bindingResult);
//        }
//        if (receptiontemporaireDTO.getNumbon() == null || receptiontemporaireDTO.getNumbon().isEmpty()) {
//            bindingResult.addError(new FieldError("ReceptionTemporaireDTO", "numbon", "PUT method does not accepte " + ENTITY_NAME + " with code"));
//            throw new MethodArgumentNotValidException(null, bindingResult);
//        }
//        ReceptionTemporaireDTO result = receptiontemporaireService.update(receptiontemporaireDTO);
//        return ResponseEntity.ok().body(result);
//    }
    @GetMapping("/receptionTemporaire/{id}")
    public ResponseEntity<ReceptionTemporaireDTO> getReceptionTemporaire(@PathVariable String id) {
        log.debug("Request to get ReceptionTemporaire: {}", id);
        ReceptionTemporaireDTO dto = receptiontemporaireService.findOne(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/reception-temporaires")
    public Collection<ReceptionTemporaireDTO> getAllReceptionTemporaires( 
            @RequestParam(name = "categDepot", required = false) CategorieDepotEnum categDepot,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "deleted", required = false) Boolean deleted,
            @RequestParam(name = "codeFournisseur", required = false) String codeFournisseur) {
          log.debug("request to get all reception temporaire");
        return receptiontemporaireService.findAll(categDepot, fromDate, toDate, deleted, codeFournisseur);
    }
       @GetMapping("/reception-temporaires-Not-Validated")
    public List<ReceptionTemporaireDTO> getReceptionTemporairesNotValidated(
            @RequestParam(name = "categDepot", required = false) CategorieDepotEnum categDepot,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "deleted", required = false) Boolean deleted,
            @RequestParam(name = "codeFournisseur", required = false) String codeFournisseur) {
        log.debug("Request to get all  ReceptionTemporaires : {}");
        return receptiontemporaireService.findAllReceptionTempraireNotValidated(categDepot, fromDate, toDate, deleted, codeFournisseur);
    }
    /*
    @DeleteMapping("/reception-temporaires/{id}")
    public ResponseEntity<Void> deleteReceptionTemporaire(@PathVariable String id) {
        log.debug("Request to delete ReceptionTemporaire: {}", id);
        receptiontemporaireService.delete(id);
        return ResponseEntity.ok().build();
    }*/
    
    
    
    
}



