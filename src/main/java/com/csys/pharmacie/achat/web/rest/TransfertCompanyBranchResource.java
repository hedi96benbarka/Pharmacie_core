package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.DetailTransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.service.TransfertCompanyBranchService;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransfertCompanyBranchResource {

    private final TransfertCompanyBranchService transfertcompanybranchService;

    private final Logger log = LoggerFactory.getLogger(TransfertCompanyBranchResource.class);

    public TransfertCompanyBranchResource(TransfertCompanyBranchService transfertcompanybranchService) {
        this.transfertcompanybranchService = transfertcompanybranchService;
    }

    @Deprecated
    @ApiOperation(" do not use this method it s only for test and debugging use")
    @PostMapping("/transfer-company-branches")
    public ResponseEntity<TransfertCompanyBranchDTO> createTransfertCompanyBranch(@Valid @RequestBody TransfertCompanyBranchDTO transfertcompanybranchDTO, BindingResult bindingResult) throws MethodArgumentNotValidException, URISyntaxException {
        log.debug("REST request to save TransfertCompanyBranch : {}", transfertcompanybranchDTO);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        TransfertCompanyBranchDTO result = transfertcompanybranchService.save(transfertcompanybranchDTO);
        return ResponseEntity.created(new URI("/api/transfer-company-branches/" + result.getNumBon())).body(result);
    }

    @PutMapping("/transfer-company-branches/replicate")
    public ResponseEntity<TransfertCompanyBranchDTO> updateTransfertCompanyBranch(String numBon) throws MethodArgumentNotValidException {
        log.debug("Request to replicate  TransfertCompanyBranch for branch: {}", numBon);

        TransfertCompanyBranchDTO result = transfertcompanybranchService.rereplicateTransfertWhenErrorReplication(numBon);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/transfer-company-branches/{id}")
    public ResponseEntity<TransfertCompanyBranchDTO> getTransfertCompanyBranch(@PathVariable String id) {
        log.debug("Request to get TransfertCompanyBranch: {}", id);
        TransfertCompanyBranchDTO dto = transfertcompanybranchService.findOne(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/transfer-company-branches/{id}/details")
    public Collection<DetailTransfertCompanyBranchDTO> getDetailsTransfertCompanyBranch(@PathVariable String id) {
        log.debug("Request to get DetailsTransfertCompanyBranch: {}", id);
        TransfertCompanyBranchDTO dto = transfertcompanybranchService.findOne(id);
        return dto.getDetailTransfertCompanyBranchDTOs();
    }

    @GetMapping("/transfer-company-branches")
    public Collection<TransfertCompanyBranchDTO> getAllTransfertCompanyBranchs(
            @RequestParam(name = "categoryDepot", required = false) CategorieDepotEnum categoryDepot,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "supplierId", required = false) String supplierId,
            @RequestParam(name = "depotId", required = false) Integer depotId,
            @RequestParam(name = "itemId", required = false) Integer codeArticle,
            @RequestParam(name = "isTransferCompanyBranch", required = false) Boolean isTransferCompanyBranch,
            @RequestParam(name = "isReturnTransferCompanyBranch", required = false) Boolean isReturnTransferCompanyBranch,
            @ApiParam(value = "only used when context is company to return list of return which relative reception is invoiced or not ")
            @RequestParam(name = "receptionRelativeFacturee", required = false) Boolean receptionRelativeFacturee,
            @RequestParam(name = "returnedToSupplier", required = false) Boolean returnedToSupplier,
            @RequestParam(name = "codeSite", required = false) Integer codeSite) {
        log.debug("Request to get all  TransfertCompanyBranchs : {}");

        TransfertCompanyBranch queriedTransfert = new TransfertCompanyBranch();
        queriedTransfert.setCodeFournisseur(supplierId);
        queriedTransfert.setCodeDepot(depotId);
        TypeBonEnum typeBon = null;
        if (Boolean.TRUE.equals(isTransferCompanyBranch)) {
            typeBon = TypeBonEnum.TCB;
        } else if (Boolean.TRUE.equals(isReturnTransferCompanyBranch)) {
            typeBon = TypeBonEnum.TBC;
        }
        queriedTransfert.setTypeBon(typeBon);
        queriedTransfert.setCategDepot(categoryDepot);
        queriedTransfert.setCodeFournisseur(supplierId);
        queriedTransfert.setCodeDepot(depotId);
        return transfertcompanybranchService.findAll(queriedTransfert, fromDate, toDate, codeArticle, receptionRelativeFacturee, returnedToSupplier, codeSite);
    }

    @ApiOperation("")
    @PostMapping("/transfer-company-branches/return")
    public ResponseEntity<TransfertCompanyBranchDTO> createTransferBranchToCompany(@Valid
            @RequestBody TransfertCompanyBranchDTO transfertcompanybranchDTO, BindingResult bindingResult) throws MethodArgumentNotValidException, URISyntaxException {
        log.debug("REST request to save TransfertCompanyBranch : {}", transfertcompanybranchDTO);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        TransfertCompanyBranchDTO result = transfertcompanybranchService.ajoutTransfertBranchToCompanyPourRetour(transfertcompanybranchDTO);
        return ResponseEntity.created(new URI("/api/transfer-company-branches/return/" + result.getNumBon())).body(result);
    }
//    @ApiOperation("")
//    @PostMapping("/transfer-company-branches/regenarete-immobilisations")
//    public ResponseEntity<TransfertCompanyBranchDTO> regerenerateImmobilisation(String numBon)    {
//        log.debug("REST request to regenerate Immos from  TransfertCompanyBranch : {}", numBon);
//       
//        TransfertCompanyBranchDTO result = transfertcompanybranchService.ajoutTransfertBranchToCompanyPourRetour(transfertcompanybranchDTO);
//        return ResponseEntity.created(new URI("/api/transfer-company-branches/return/" + result.getNumBon())).body(result);
//    }
//
//  @DeleteMapping("/transfertcompanybranchs/{id}")
//  public ResponseEntity<Void> deleteTransfertCompanyBranch(@PathVariable String id) {
//    log.debug("Request to delete TransfertCompanyBranch: {}",id);
//    transfertcompanybranchService.delete(id);
//    return ResponseEntity.ok().build();
//  }
}
