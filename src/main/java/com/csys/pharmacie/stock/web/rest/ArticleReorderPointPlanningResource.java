package com.csys.pharmacie.stock.web.rest;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.stock.dto.ArticleReorderPointPlanningDTO;
import com.csys.pharmacie.stock.service.ArticleReorderPointPlanningService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
public class ArticleReorderPointPlanningResource {

    private static final String ENTITY_NAME = "articlereorderpointplanning";

    private final ArticleReorderPointPlanningService articleReorderPointPlanningService;

    private final Logger log = LoggerFactory.getLogger(ArticleReorderPointPlanningService.class);

    public ArticleReorderPointPlanningResource(ArticleReorderPointPlanningService articleReorderPointPlanningService) {
        this.articleReorderPointPlanningService = articleReorderPointPlanningService;
    }

    @PostMapping("/article-reorder-point-plannings")
    public ResponseEntity<ArticleReorderPointPlanningDTO> createArticleReorderPointPlanning(@Valid @RequestBody ArticleReorderPointPlanningDTO articleReorderPointPlanningDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save ArticleReorderPointPlanning : {}", articleReorderPointPlanningDTO);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (articleReorderPointPlanningDTO.getCode() != null) {
            bindingResult.addError(new FieldError("ArticleReorderPointPlanningDTO", "code", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        ArticleReorderPointPlanningDTO result = articleReorderPointPlanningService.save(articleReorderPointPlanningDTO);
        return ResponseEntity.created(new URI("/api/article-reorder-point-plannings/" + result.getCode())).body(result);
    }

    @PutMapping("/article-reorder-point-plannings/save-article-reorder-point-if-job-not-working/{id}")
    public void saveArticleReorderPointIfJobNotWorking(@PathVariable Integer id) {
        log.debug("REST request to saveArticleReorderPointIfJobNotWorking : {}", id);
        articleReorderPointPlanningService.saveArticleReorderPointIfJobNotWorking(id);
    }

    @PutMapping("/article-reorder-point-plannings/ltp-executed/{id}")
    public Boolean updateArticleReorderPointPlanning(@PathVariable Integer id) {
        log.debug("REST request to update tld to true : {}", id);
        return articleReorderPointPlanningService.updateLtpExecuted(id);
    }

    @GetMapping("/article-reorder-point-plannings/{id}")
    public ResponseEntity<ArticleReorderPointPlanningDTO> getArticleReorderPointPlaning(@PathVariable Integer id) {
        log.debug("Request to get ArticleReorderPoint: {}", id);
        ArticleReorderPointPlanningDTO dto = articleReorderPointPlanningService.findOne(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/article-reorder-point-plannings")
    public ResponseEntity<Collection<ArticleReorderPointPlanningDTO>> getAllArticleReorderPointPlaning(
            @RequestParam(name = "categorieDepot", required = true) CategorieDepotEnum categorieDepot,
            @RequestParam(name = "planned", required = false) Boolean planned,
            @RequestParam(name = "ltpExecuted", required = false) Boolean ltpExecuted,
            @RequestParam(name = "executed", required = false) Boolean executed) {

        Collection<ArticleReorderPointPlanningDTO> dto = articleReorderPointPlanningService.findAll(categorieDepot, planned, ltpExecuted, executed);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/article-reorder-point-plannings/excuteJobWithOrder1")
    public void excuteJobWithOrder1(@RequestParam(name = "categorieDepot", required = true) CategorieDepotEnum categorieDepot) {
        if (categorieDepot.equals(CategorieDepotEnum.PH)) {
            articleReorderPointPlanningService.excuteJobWithOrder1ForPHGroupped(categorieDepot);
        } else {
            articleReorderPointPlanningService.excuteJobWithOrder1Groupped(categorieDepot);
        }
    }

    @PutMapping("/article-reorder-point-plannings/excuteJobWithOrder2")
    public void excuteJobWithOrder2(@RequestParam(name = "categorieDepot", required = true) CategorieDepotEnum categorieDepot) {

        articleReorderPointPlanningService.excuteJobWithOrder2(categorieDepot);
    }

}
