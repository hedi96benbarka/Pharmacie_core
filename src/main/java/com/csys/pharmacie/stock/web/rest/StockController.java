/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.web.rest;

import com.csys.pharmacie.stock.dto.DepstoDTO;
import com.csys.pharmacie.achat.service.MvtStoBAService;
import com.csys.pharmacie.achat.service.ReceivingService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.csys.pharmacie.stock.repository.ArticleInDepProjection;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.inventaire.dto.EtatEcartInventaire;
import com.csys.pharmacie.inventaire.dto.TypeEnvoieEtatEnum;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.dto.ArticleStockProjection;
import com.csys.pharmacie.stock.dto.DepstoEditionValeurStockDTO;
import com.csys.pharmacie.stock.dto.IsArticleQteAvailable;
import com.csys.pharmacie.stock.dto.ListDepstoDTOWrapper;
import com.csys.pharmacie.stock.factory.DepstoFactory;
import com.csys.pharmacie.stock.repository.InvDepstoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Farouk
 */
@RestController
public class StockController {

    private final Logger log = LoggerFactory.getLogger(ReceivingService.class);
    @Autowired
    MvtStoBAService shit;

    @Autowired
    private StockService stockService;


    @Autowired
    private InvDepstoService invDepstoService;

    
    @Deprecated
    @RequestMapping(value = "/stock/{codDep}/Articles", method = RequestMethod.GET)
    public @ResponseBody
    List<ArticleInDepProjection> findArticlesInDep(@PathVariable("codDep") Integer codDep) {
        return stockService.findArticlesInDep(codDep);
    }

    @GetMapping(value = "/stock/{article}/is-stored")
    public @ResponseBody
    Boolean findNombreArticleDepotFrsProchPerime(@PathVariable("article") Integer articleID) {
        return stockService.articleExistsWithQuantity(articleID);
    }

    @Deprecated
    @ApiOperation(value = "This methode will soon be deprecated due to performance issues and is replaced with another one under /api/v2/stock ")
    @GetMapping("/stock")
    public @ResponseBody
    List<DepstoDTO> findAll(
            @RequestParam(required = false, value = "article-ids") List<Integer> articleIDs,
            @RequestParam(required = false, value = "catgorie-depot") CategorieDepotEnum categDep,
            @RequestParam(required = false, value = "depot-id") Integer codeDepot,
            @RequestParam("include-null-quantity") boolean includeNullqty,
            @RequestParam("detailed") boolean detailed,
            @RequestParam(required = false, value = "with-selling-price") boolean withSellingPrice,
            @ApiParam("param needed when config apply marginal to medication is true: when not pharmacie externe then selling price for public will be remplaced by selling price with marge ")
            @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne
    ) {

        return stockService.findAll(codeDepot, categDep, articleIDs, includeNullqty, detailed, withSellingPrice, pharmacieExterne);

    }

    @GetMapping("/api/v2/stock")
    public @ResponseBody
    List<DepstoDTO> findAll(
            @RequestParam(required = false, value = "q")
            @ApiParam(value = "The  articles that have the designation ,desginaton of categorieAritlce or  codesaisie like this parameter will be returned. The minium length to start querying is 3. If null or length < 2 it will only return  100 articles") String q,
            @RequestParam(required = true, value = "catgorie-depot") List<CategorieDepotEnum> categDep,
            @RequestParam(required = false, value = "depot-id") Integer codeDepot,
            @RequestParam("include-null-quantity") boolean includeNullqty,
            @ApiParam("If False it will return depstos grouped by codart and unit.")
            @RequestParam("detailed") boolean detailed,
            @RequestParam(required = false, value = "with-selling-price") boolean withSellingPrice,
            @ApiParam("In case of non medical items, passing this value as true will return only the items that belongs to the current user. Else case, this param will be forced to take the value false")
            @RequestParam(required = false, value = "only-my-articles") boolean onlyMyArticles,
            @ApiParam("param needed when config apply marginal to medication is true: when not pharmacie externe then selling price for public will be remplaced by selling price with marge ")
            @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne) {

        if ((q == null || q.length() < 3) && !onlyMyArticles) {
            return stockService.findTop100(codeDepot, categDep, includeNullqty, detailed, withSellingPrice, pharmacieExterne);
        } else {
            return stockService.findAll(codeDepot, categDep, q, includeNullqty, detailed, withSellingPrice, onlyMyArticles, pharmacieExterne);
        }
    }

    @GetMapping("/api/stock/perime")
    public @ResponseBody
    List<DepstoDTO> findAllPerime(
            @RequestParam(required = true, value = "catgorie-depot") List<CategorieDepotEnum> categDep,
            @RequestParam(required = false, value = "depot-id") Integer codeDepot,
            @RequestParam("include-null-quantity") boolean includeNullqty,
            @ApiParam("If False it will return depstos grouped by codart and unit.")
            @RequestParam("detailed") boolean detailed,
            @RequestParam(required = false, value = "with-selling-price") boolean withSellingPrice,
            @RequestParam(required = false, value = "include-non-moved-article") boolean nonMoved) {
        return stockService.findAllPerime(codeDepot, categDep, includeNullqty, detailed, withSellingPrice, nonMoved);

    }

    @ApiOperation(value = "Search stock by article Id and depot ID(optional)")
    @PostMapping("/stock/searches")
    public @ResponseBody
    List<DepstoDTO> searchArticles(
            @RequestBody List<Integer> articleIDs,
            @RequestParam(required = false, value = "depot-id") Integer codeDepot,
            @RequestParam(required = false, value = "detailed", defaultValue = "false") boolean detailed,
            @ApiParam("If true, all informations relevant to the article will not be provided ( designation, codeSaisi, unitDesignation, etc).Only ids and quantity will be returned.")
            @RequestParam(required = false, value = "use-minimum-data", defaultValue = "false") boolean useMinimumData,
            @ApiParam(value = "If \"true\" the api will return only the current user's articles")
            @RequestParam(name = "only-my-articles", required = false) Boolean onlyMyArticles,
            @ApiParam("param needed when config apply marginal to medication is true: when not pharmacie externe then selling price for public will be remplaced by selling price with marge ")
            @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne) {

        return stockService.findQuantiteOfArticles(articleIDs, codeDepot, detailed, useMinimumData, onlyMyArticles, pharmacieExterne);

    }

    @ApiOperation(value = "Search stock (sum quantity not in inventory and quantity in inventory before open it) by article Id and depot ID(optional)")
    @PostMapping("/stock/groupped-by-unite/searches")
    public @ResponseBody
    List<ArticleStockProjection> searchArticlesGroupped(
            @RequestParam(required = false, value = "catgorieDepot") CategorieDepotEnum categDepot,
            @RequestParam(required = false, value = "codeDepot") Integer codeDepot,
            @RequestBody List<Integer> articleIDs) {

        return stockService.findAllGrouppedByByCodeArticleAndCodeUnite(categDepot, codeDepot, articleIDs);

    }

    @GetMapping("/stock/{qr-code:.+}")
    public @ResponseBody
    DepstoDTO SearchOne(
            @PathVariable("qr-code") String QRCode,
            @ApiParam("Only set it when looking for an article with category UU")
            @RequestParam(name = "depot-id", required = false) Integer depotID,
            @ApiParam("param needed when config apply marginal to medication is true: when not pharmacie externe then selling price for public will be remplaced by selling price with marge ")
            @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne) {

        return stockService.findByQrCodeAndUnity(QRCode, depotID, pharmacieExterne);
    }

    @ApiOperation(value = "Check availabilty of articles in a depot based on the demanded quantity of each article")
    @PostMapping("/stock/check-disponibility-by-quantites")
    public List<IsArticleQteAvailable> isDisponible(@RequestBody
            @Valid List<DepstoDTO> depstos,
            @ApiParam("Where to check if the demanded quantites are available")
            @RequestParam(required = false, name = "depot-id") Integer depotId
    ) {
        List<IsArticleQteAvailable> result = stockService.areDemandedDepstosAvailable(depstos, depotId);
        log.debug("result areDemandedDepstosAvailable : {}", result.toString());
        return result;
    }

    @PostMapping("/stock/findDepstoByCodeIn")
    public List<DepstoDTO> findDepstoByCodeIn(@RequestBody List<Integer> ids
    ) {
        return invDepstoService.findDepstoByCodeIn(ids);
    }

    @GetMapping("/stock/findDepstoInv")
    public @ResponseBody
    List<DepstoDTO> findDepstoInv(
            @RequestParam(required = false, value = "catgorie-depot") CategorieDepotEnum categDep,
            @RequestParam(required = false, value = "depot-id") Integer codeDepot,
            @RequestParam("include-null-quantity") boolean includeNullqty,
            @RequestParam("detailed") boolean detailed,
            @RequestParam(required = false, value = "categ-art") Integer categ_art,
            @RequestParam(required = false, value = "codArt") Integer codArt,
            @RequestParam(required = false, value = "uniteArt") Integer uniteArt
    ) {

        return invDepstoService.findDepstoInvByCodDepCathegDep(codeDepot, categDep, includeNullqty, detailed, categ_art, codArt, uniteArt, null);

    }

    @GetMapping("/stock/findDepstoInv2")
    public @ResponseBody
    ResponseEntity<List<DepstoDTO>> findDepstoInv2(
            @RequestParam(required = false, value = "catgorie-depot") CategorieDepotEnum categDep,
            @RequestParam(required = false, value = "depot-id") Integer codeDepot,
            @RequestParam("include-null-quantity") boolean includeNullqty,
            @RequestParam("detailed") boolean detailed,
            @RequestParam(required = false, value = "categ-art") Integer categ_art,
            @RequestParam(required = false, value = "codArt") Integer codArt,
            @RequestParam(required = false, value = "uniteArt") Integer uniteArt
    ) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                .body(invDepstoService.findDepstoInvByCodDepCathegDep(codeDepot, categDep, includeNullqty, detailed, categ_art, codArt, uniteArt, null));

    }

    @GetMapping("/stock/etatEcartAvantVldGlob")
    List<EtatEcartInventaire> etatEcartAvantVldGlobByCodDepCathegDepCodInv(
            @RequestParam("depot-id") Integer coddep,
            @RequestParam("catgorie-depot") CategorieDepotEnum categ_depot,
            @RequestParam("code-Inventaire") Integer codeInventaire,
            @RequestParam("optionImpression") TypeEnvoieEtatEnum optionImpression
    ) {
        return invDepstoService.etatEcartAvantVldGlobByCodDepCathegDepCodInv(coddep, categ_depot, codeInventaire, optionImpression,null);
    }

    @RequestMapping(value = "/stock/saisieInv", method = RequestMethod.POST)
    public @ResponseBody
    Boolean saisieInv(@Valid
            @RequestBody ListDepstoDTOWrapper depstoDTO, Integer coddep
    ) {
        return invDepstoService.saisieInv(depstoDTO.getList(), coddep);

    }

    @PostMapping("/findDepstoByCodeIn")
    public ResponseEntity<List<DepstoDTO>> createDemandePec(@RequestBody List<Integer> ids, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to get DepstoDTO : {}", ids);

        List<DepstoDTO> result = invDepstoService.findDepstoByCodeIn(ids);
        return ResponseEntity.created(new URI("/api/findDepstoByCodeIn/" + result)).body(result);
    }

    @PostMapping("/stock/depots/Articles")
    public DepstoDTO saveDepsto(@RequestParam("idArticle") Integer idArticle,
            @RequestParam("categ_depot") CategorieDepotEnum categ_depot,
            @RequestParam("coddep") Integer coddep,
            @RequestParam("codeUnite") Integer codeUnite
    ) {
        return invDepstoService.saveDepsto(idArticle, categ_depot, coddep, codeUnite);

    }

    @RequestMapping(value = "/stock/validerInventaire", method = RequestMethod.POST)
    @ResponseBody
    public Boolean validerInventaire(@RequestParam(name = "codInventaire", required = true) Integer codeInventaire) {
        return invDepstoService.validerInventaire(codeInventaire);

    }

    @RequestMapping(value = "/stock/annulation-inventaire", method = RequestMethod.PUT)
    @ResponseBody
    public Boolean annulationInventaire(@RequestParam(name = "codeInventaire", required = true) Integer codeInventaire) {
        return invDepstoService.annulationInventaire(codeInventaire);

    }

    @GetMapping("/stock/etatEcartApresVldGlob")
    List<EtatEcartInventaire> etatEcartApresVldGlob(
            @RequestParam("code-Inventaire") Integer codeInventaire,
            @RequestParam("optionImpression") TypeEnvoieEtatEnum optionImpression
    ) {
        return invDepstoService.etatEcartApresVldGlobByCodDepCathegDepCodInv(codeInventaire, optionImpression,null,null);
    }

    @GetMapping("/stock/importerInventaire")
    boolean importation(
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ,
            @RequestParam("coddep") Integer coddep
    ) {
        return invDepstoService.importation(categ, coddep);
    }

    @GetMapping("/stock/etatEcartStock")
    public @ResponseBody
    List<DepstoEditionValeurStockDTO> findSuiviEcartStock(
            @RequestParam(required = true, value = "catgorie-depot") CategorieDepotEnum categDep,
            @RequestParam(required = true, value = "depot-id") Integer codeDepot
    ) {

        List<DepstoEditionValeurStockDTO> resultEcartStock = stockService.findEcartStock(categDep, codeDepot);
        return resultEcartStock;

    }

    @PostMapping("/stock/articleInDepWithQte")
    public @ResponseBody
    List<DepstoDTO> articleInDepWithQte(
            @RequestBody
            @Valid Collection<Integer> codArticles,
            @RequestParam(required = true, value = "qte-greater-than") BigDecimal gt
    ) {

        List<Depsto> result = stockService.findByCodartInAndQteGreaterThan(codArticles, gt);
        List<DepstoDTO> resultDTO = DepstoFactory.DepstoTodepstoDTOs(result);
        return resultDTO;

    }

    @GetMapping("/stock/configs/expiration-date-inteval")
    public String getexpirationDateInteval() {
        return stockService.getINTERVAL();
    }

    @ApiOperation("this method import excel which contains actual stock after that it maps this data with stock from inventory")
    @PostMapping("/stock/mapping-actual-stock-with-inventory")
    public @ResponseBody
    List<DepstoDTO> mappingActualStockWithInventory(
            @RequestParam Integer codeDepot,
            @RequestBody MultipartFile masterDataFile
    ) throws IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        log.debug("REST request to mapping Actual Stock With Inventory : {}");

        List<DepstoDTO> result = stockService.mappingActualStockWithInventory(codeDepot, masterDataFile);
        return result;
    }

    @PutMapping("/stock/updateDatePer")
    public void updateDatePerDepsto(@Valid
            @RequestBody List<DepstoDTO> depstoDTOs) throws MethodArgumentNotValidException {
        stockService.updateDatePerDepsto(depstoDTOs);
    }

    @ApiOperation("this method reset unit price in depsto from purchase price item table")
    @PutMapping("/stock/reset-unit-price")
    public Boolean updatePrixAchatDepstoPourDemarrage(@RequestParam(required = true, value = "categorie-depot") CategorieDepotEnum categDepot
    ) {
        return stockService.updatePrixAchatDepstoPourDemarrage(categDepot);
    }

    @ApiOperation("this method reset unit price in depsto from purchase price item table by articleID's")
    @PutMapping("/stock/reset-unit-price/articles")
    public Boolean updatePrixAchatDepstoPourDemarrageByArticleIn(@RequestParam(required = true, value = "categorie-depot") CategorieDepotEnum categDepot,
            @RequestBody List<Integer> articleIDs
    ) {
        return stockService.updatePrixAchatDepstoPourDemarrageByArticleIn(categDepot, articleIDs);
    }
//    @ApiOperation("this method reset unit price in depsto from purchase price item table")
//    @PutMapping("/stock/update-by-pmp")
//    public Boolean updateDepstoWithPMPAfterGoLive(@RequestParam(required = true, value = "categorie-depot") CategorieDepotEnum categDepot) {
//        return stockService.updateDepstoWithPMPAfterGoLive(categDepot);
//    }
//
//    @ApiOperation("this method reset unit price in depsto from purchase price item table")
//    @PutMapping("/stock/reset-unit-price-apres-demarrage")
//    public Boolean updatePrixAchatDepstoPourDemarrageApresDemarrage(@RequestParam(required = true, value = "categorie-depot") CategorieDepotEnum categDepot,@RequestParam(required = false, value = "article-ids") List<Integer> articleIDs) {
//        return stockService.updatePrixAchatDepstoPourDemarrage(categDepot,articleIDs);
//    }

    @ApiOperation(value = "return stock(sum qte and qte0 and stkrel(if withQteReelInInventory = true)) for items that have their ids in the given list articleIds ")
    @PostMapping("/stock/item-total-quantity")
    public List<IsArticleQteAvailable> findTotalQuantitiesInStockByItemIdsIn(
            @RequestParam(name = "detailedDepot", defaultValue = "false", required = false) Boolean detailedDepot,
            @RequestParam(name= "withQteSaisiInInventory" , defaultValue = "false", required = false) Boolean withQteSaisiInInventory,
            @RequestBody @Valid List<Integer> articleIds) {
        log.debug(" rest request to findTotalQuantitiesInStockByItemIdsIn ");
        List<IsArticleQteAvailable> result = stockService.findTotalQuantitiesInStockByItemIdsIn(articleIds, detailedDepot, withQteSaisiInInventory);
        return result;
    }
    
    
    @ApiOperation(value = "return true if exist quantity reel in stock for item in inventory")
    @GetMapping("/stock/inventory-item-has-reel-quantity")
    public Boolean existInventoryRealQuantitiesInStockByItemId(
            @RequestParam(required = true, value = "codeArticle") Integer codeArticle) {
        log.debug(" rest request to existInventoryRealQuantitiesInStockByItemId ");
        return stockService.existsByCodartAndAInventorierTrueAndAndStkrelGreaterThan(codeArticle);
    }
}
