package com.csys.pharmacie.stock.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.stock.dto.ArticleReorderPointDTO;
import com.csys.pharmacie.stock.service.ArticleReorderPointService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ArticleReorderPointResource {

    private static final String ENTITY_NAME = "articlereorderpoint";

    private final ArticleReorderPointService articlereorderpointService;

    private final Logger log = LoggerFactory.getLogger(ArticleReorderPointService.class);

    public ArticleReorderPointResource(ArticleReorderPointService articlereorderpointService) {
        this.articlereorderpointService = articlereorderpointService;
    }

//    @PostMapping("/article-reorder-point")
//    public ResponseEntity<ArticleReorderPointDTO> createArticleReorderPoint(@Valid @RequestBody ArticleReorderPointDTO articlereorderpointDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
//        if (articlereorderpointDTO.getCode() != null) {
//            bindingResult.addError(new FieldError("ArticleReorderPointDTO", "code", "POST method does not accepte " + ENTITY_NAME + " with code"));
//            throw new MethodArgumentNotValidException(null, bindingResult);
//        }
//        if (bindingResult.hasErrors()) {
//            throw new MethodArgumentNotValidException(null, bindingResult);
//        }
//        ArticleReorderPointDTO result = articlereorderpointService.save(articlereorderpointDTO);
//        return ResponseEntity.created(new URI("/api/article-reorder-point/" + result.getCode())).body(result);
//    }
    @GetMapping("/article-reorder-point/filter")
    public ResponseEntity<Collection<ArticleReorderPointDTO>> findAllArticleReorderPoint(
            @RequestParam(name = "categorieDepot", required = true) CategorieDepotEnum categorieDepot,
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {

        Collection<ArticleReorderPointDTO> dto = articlereorderpointService.findAll(categorieDepot, fromDate, toDate);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/article-reorder-point/{id}")
    public ResponseEntity<ArticleReorderPointDTO> getArticleReorderPoint(@PathVariable Integer id) {
        log.debug("Request to get ArticleReorderPoint: {}", id);
        ArticleReorderPointDTO dto = articlereorderpointService.getArticleReorderPoint(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/article-reorder-point/last-one")
    public ResponseEntity<ArticleReorderPointDTO> getLastArticleReorderPoint(
            @RequestParam(name = "categorieDepot", required = true) CategorieDepotEnum categorieDepot) {
        log.debug("Request to get  LastArticleReorderPoint");
        ArticleReorderPointDTO dto = articlereorderpointService.getLastArticleReorderPoint(categorieDepot);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/article-reorder-point/{id}")
    public ResponseEntity<Void> deleteArticleReorderPoint(@PathVariable Integer id) {
        log.debug("Request to delete ArticleReorderPoint: {}", id);
        articlereorderpointService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("article-reorder-point/edition/{id}")
    public ResponseEntity<byte[]> editionArticleReorderPoint(@PathVariable Integer id)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {

        log.debug("Request to print ArticleReorderPoint  :");
        byte[] bytePdf = articlereorderpointService.editionArticleReorderPoint(id);

        HttpHeaders headers = new HttpHeaders();
        MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
        headers.setContentType(excelMediaType);
        return ResponseEntity.ok().headers(headers).body(bytePdf);

    }

}
