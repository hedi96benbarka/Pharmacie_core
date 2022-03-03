///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.csys.pharmacie.article.controller;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.csys.exception.webhandler.RestPreconditions;
//import com.csys.pharmacie.achat.domain.ProposeVO;
//import com.csys.pharmacie.article.dto.ArticleDTO;
//
//import com.csys.pharmacie.article.dto.CodDesArtProjection;
//import com.csys.pharmacie.article.dto.CodDesProjection;
//import com.csys.pharmacie.article.dto.FamArtProjection;
//import com.csys.pharmacie.article.dto.FamilleDto;
//import com.csys.pharmacie.article.dto.MargesDto;
//import com.csys.pharmacie.article.dto.PalierDto;
//import com.csys.pharmacie.article.dto.PalierProjection;
//import com.csys.pharmacie.article.dto.ParametrageArticleDto;
//import com.csys.pharmacie.article.dto.ParametrageFamArtProjection;
//import com.csys.pharmacie.article.entity.FamArtMere;
//import com.csys.pharmacie.article.entity.Stockph;
//import com.csys.pharmacie.article.repository.ArticleService;
//import com.csys.pharmacie.stock.dto.LotInterDatPerQteProjection;
//import com.csys.pharmacie.stock.entity.Depsto;
//import com.csys.pharmacie.stock.repository.StockService;
//import com.google.common.base.Preconditions;
//
//import helper.CodeDesDto;
//
///**
// *
// * @author Farouk
// */
//@RestController
//@RequestMapping("/Article")
//
//public class ArticleController {
//
//    @Autowired
//    private ArticleService articleService;
//
//    @Autowired
//    private StockService stockService;
//
//    @RequestMapping(value = "/PalierMarge", method = RequestMethod.GET)
//    public @ResponseBody
//    List<PalierDto> findPalierMargebycodfam(@RequestParam String codfam) {
//        return articleService.findPalierMargebycodfam(codfam);
//    }
//
//    @RequestMapping(value = "/PalierMarge", method = RequestMethod.POST)
//    public @ResponseBody
//    Boolean addPalierMargebycodfam(@RequestBody MargesDto dto) {
//        return articleService.addPalierMargebycodfam(dto);
//    }
//
//    @RequestMapping(value = "/PalierMarge", method = RequestMethod.PUT)
//    public @ResponseBody
//    Boolean updatePalierMargebycodfam(@RequestBody MargesDto dto) {
//        return articleService.updatePalierMargebycodfam(dto);
//    }
//
//    @RequestMapping(value = "/Unite", method = RequestMethod.POST)
//    public Boolean AddUnite(@RequestBody CodeDesDto dto) {
//        return articleService.addUnite(dto);
//    }
//
//    @RequestMapping(value = "/Unite", method = RequestMethod.PUT)
//    public Boolean UpdateUnite(@RequestBody CodeDesDto dto) {
//        return articleService.updateUnite(dto);
//    }
//
//    @RequestMapping(value = "/Unite", method = RequestMethod.GET)
//    public @ResponseBody
//    List<CodeDesDto> findAllUnite() {
//        return articleService.findAllUnite();
//    }
//
//    @RequestMapping(value = "/Famartmere", method = RequestMethod.POST)
//    public Boolean AddFamArtMere(@RequestBody CodeDesDto famille) {
//        return articleService.AddFamArtMere(famille);
//
//    }
//
//    @RequestMapping(value = "/Famartmere", method = RequestMethod.PUT)
//    public Boolean UpdateFamArtMere(@RequestBody CodeDesDto famille) {
//        return articleService.UpdateFamArtMere(famille);
//
//    }
//
//    @RequestMapping(value = "/like/{param}", method = RequestMethod.GET)
//    public @ResponseBody
//    List<CodDesProjection> findTop30ContainingParam(@PathVariable("param") String param,@RequestParam("stup") boolean stup) {
//        param = "%"+param+"%";
//    	List<CodDesProjection> f = articleService.getStockrepository()
//                .findFirst30ByStupAndCodartContainsOrDesartContains(stup,param);
//        RestPreconditions.checkFound(f);
//        return f;
//    }
//
//    @RequestMapping(value = "/like", method = RequestMethod.GET)
//    public @ResponseBody
//    List<CodDesProjection> findTop50ContainingParam(@RequestParam("stup") boolean stup) {
//        List<CodDesProjection> f = articleService.getStockrepository().findFirst10ByAndStup(stup);
//        return f;
//    }
//
//    @RequestMapping(value = "/findbyfamart/{famille}", method = RequestMethod.GET)
//    public @ResponseBody
//    List<ArticleDTO> findByFam(@PathVariable("famille") String fam,@RequestParam("stup") boolean stup) {
//        List<ArticleDTO> f = articleService.findbyfamart(fam,stup);
//        return f;
//    }
//
//    @RequestMapping(value = "{codArt}/LotInDep/{codDep}", method = RequestMethod.GET)
//    public @ResponseBody
//    List<LotInterDatPerQteProjection> findAllLotsOfArticleInDep(
//            @PathVariable("codArt") String codArt, @PathVariable("codDep") String codDep) {
//        return stockService.findAllLotsOfArticleInDep(codArt, codDep);
//    }
//
//    @RequestMapping(value = "/Famart", method = RequestMethod.GET)
//    public @ResponseBody
//    List<FamArtProjection> findAllFamart() {
//        return articleService.findAllFamart();
//
//    }
//
//    @RequestMapping(value = "/Famartmere", method = RequestMethod.GET)
//    public @ResponseBody
//    List<CodeDesDto> findAllFamArtMere() {
//        return articleService.findAllFamArtMere();
//
//    }
//
//    @RequestMapping(value = "/ParametrageFamart", method = RequestMethod.GET)
//    public @ResponseBody
//    List<ParametrageFamArtProjection> findAllFamartOrderByDesFam() {
//        return articleService.findAllFamartOrderByDesFam();
//
//    }
//
//    @RequestMapping(value = "/ParametrageFamart", method = RequestMethod.POST)
//    public @ResponseBody
//    Boolean AddFamart(@RequestBody FamilleDto famart) {
//        return articleService.AddFamArt(famart);
//
//    }
//
//    @RequestMapping(value = "/ParametrageFamart", method = RequestMethod.PUT)
//    public @ResponseBody
//    Boolean UpdateFamart(@RequestBody FamilleDto famart) {
//        return articleService.UpdateFamArt(famart);
//
//    }
//
//    @RequestMapping(value = "/findCodDesArtbyFamart/{famille}", method = RequestMethod.GET)
//    public @ResponseBody
//    List<CodDesArtProjection> findCodDesArtByFamArt(@PathVariable("famille") String fam,@RequestParam("stup") boolean stup) {
//        List<CodDesArtProjection> f = articleService.findCodDesArtByFamArt(fam,stup);
//        return f;
//    }
//
//    @RequestMapping(value = "/findCodDesArtbyFamart", method = RequestMethod.POST)
//    public @ResponseBody
//    List<CodDesArtProjection> findCodDesArtByFamArt(@RequestBody List<String> fams,@RequestParam("stup") boolean stup) {
//        Preconditions.checkArgument(!fams.isEmpty(), "Liste de famille vide");
//        List<CodDesArtProjection> f = articleService.findCodDesArtByFamArts(fams,stup);
//        return f;
//    }
//
//    @RequestMapping(value = "/getNewCodeByFamart", method = RequestMethod.GET)
//    public @ResponseBody
//    String findMaxCodart(@RequestParam("famart") String fam) {
//        return articleService.findMaxCodart(fam);
//
//    }
//
//    @RequestMapping(value = "/{codart}", method = RequestMethod.GET)
//    public @ResponseBody
//    ParametrageArticleDto findArticle(@PathVariable("codart") String codart) {
//        ParametrageArticleDto dto = articleService.findArticle(codart);
//        return dto;
//
//    }
//
//    @RequestMapping(method = RequestMethod.POST)
//    public @ResponseBody
//    Boolean ajoutArticle(@RequestBody ParametrageArticleDto dto) {
//
//        return articleService.ajoutArticle(dto);
//
//    }
//
//    @RequestMapping(method = RequestMethod.PUT)
//    public @ResponseBody
//    Boolean updateArticle(@RequestBody ParametrageArticleDto dto) {
//
//        return articleService.updateArticle(dto);
//
//    }
//
//    @RequestMapping(value = "/getMargeByfamartAndPrix", method = RequestMethod.GET)
//    public @ResponseBody
//    BigDecimal findArticle(@RequestParam("codfam") String codfam, @RequestParam("prix") BigDecimal prix) {
//
//        return articleService.getMargeByFamArtAndPrix(codfam, prix);
//
//    }
//
//    @RequestMapping(value = "/findArticle", method = RequestMethod.GET)
//    public @ResponseBody
//    List<ArticleDTO> findArticle(@RequestParam(value = "codfam", required = false, defaultValue = "") String codfam, @RequestParam(value = "rech", required = false, defaultValue = "") String rech, Boolean rupture, Boolean actif,@RequestParam("stup") boolean stup) {
//
//        return articleService.findArticles(codfam, rech, actif, rupture,stup);
//
//    }
//
//    @RequestMapping(method = RequestMethod.GET)
//    public @ResponseBody
//    List<ArticleDTO> findAllArticle(@RequestParam("stup") boolean stup) {
//
//        return articleService.findAllArticles(stup);
//
//    }
//    
//    @RequestMapping(value = "{codart}/isExist",method = RequestMethod.GET)
//    public @ResponseBody
//    boolean isExist(@PathVariable("codart") String codart) {
//
//        return articleService.isExist(codart);
//
//    }
//
//    @RequestMapping(value = "{codart}/isReceptionee", method = RequestMethod.GET)
//    public @ResponseBody
//   boolean isReceptionee(@PathVariable("codart") String codart) {
//         return articleService.isRecptionee(codart);
//    }
//    @RequestMapping(value = "/NombreArticlesEnRuptures", method = RequestMethod.GET)
//    public @ResponseBody
//    Integer findNombreArticlesEnRupture(@RequestParam("stup") boolean stup) {
//         return articleService.findNombreArticlesEnRupture(stup);
//    }
//
//}
