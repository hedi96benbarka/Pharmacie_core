package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.dto.DetailReceptionTemporaireDTO;
import com.csys.pharmacie.achat.service.DetailReceptionTemporaireService;
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

@RestController
@RequestMapping("/api")
public class DetailReceptionTemporaireResource {

    private static final String ENTITY_NAME = "detailreceptiontemporaire";

    private final DetailReceptionTemporaireService detailreceptiontemporaireService;

    private final Logger log = LoggerFactory.getLogger(DetailReceptionTemporaireService.class);

    public DetailReceptionTemporaireResource(DetailReceptionTemporaireService detailreceptiontemporaireService) {
        this.detailreceptiontemporaireService = detailreceptiontemporaireService;
    }

    @PostMapping("/detail-reception-temporaires")
    public ResponseEntity<DetailReceptionTemporaireDTO> createDetailReceptionTemporaire(@Valid @RequestBody DetailReceptionTemporaireDTO detailreceptiontemporaireDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save DetailReceptionTemporaire : {}", detailreceptiontemporaireDTO);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (detailreceptiontemporaireDTO.getCode() != null) {
            bindingResult.addError(new FieldError("DetailReceptionTemporaireDTO", "code", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        DetailReceptionTemporaireDTO result = detailreceptiontemporaireService.save(detailreceptiontemporaireDTO);
        return ResponseEntity.created(new URI("/api/detailreceptiontemporaires/" + result.getCode())).body(result);
    }

    @PutMapping("/detail-reception-temporaires")
    public ResponseEntity<DetailReceptionTemporaireDTO> updateDetailReceptionTemporaire(@Valid @RequestBody DetailReceptionTemporaireDTO detailreceptiontemporaireDTO, BindingResult bindingResult) throws MethodArgumentNotValidException {
        log.debug("Request to update DetailReceptionTemporaire: {}", detailreceptiontemporaireDTO);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (detailreceptiontemporaireDTO.getCode() == null) {
            bindingResult.addError(new FieldError("DetailReceptionTemporaireDTO", "code", "PUT method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        DetailReceptionTemporaireDTO result = detailreceptiontemporaireService.update(detailreceptiontemporaireDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/detail-reception-temporaires/{id}")
    public ResponseEntity<DetailReceptionTemporaireDTO> getDetailReceptionTemporaire(@PathVariable Integer id) {
        log.debug("Request to get DetailReceptionTemporaire: {}", id);
        DetailReceptionTemporaireDTO dto = detailreceptiontemporaireService.findOne(id);
        return ResponseEntity.ok().body(dto);
    }

    public Collection<DetailReceptionTemporaireDTO> getAllDetailReceptionTemporaires() {
        log.debug("Request to get all  DetailReceptionTemporaires : {}");
        return detailreceptiontemporaireService.findAll();
    }

    @DeleteMapping("/detail-reception-temporaires/{id}")
    public ResponseEntity<Void> deleteDetailReceptionTemporaire(@PathVariable Integer id) {
        log.debug("Request to delete DetailReceptionTemporaire: {}", id);
        detailreceptiontemporaireService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    
       @GetMapping("/reception/{numBon}/details")
    public List<DetailReceptionTemporaireDTO> getDetailReceptionTemporaire(@PathVariable String numBon) {
        log.debug("Request to get DetailReceptionTemporaire: {}", numBon);
       List<DetailReceptionTemporaireDTO> dto = detailreceptiontemporaireService.findByNumBon(numBon);
        return dto;
    }
}
