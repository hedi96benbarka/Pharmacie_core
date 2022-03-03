/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.service;

import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.repository.DecoupageRepository;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.vente.quittance.dto.MvtQuittanceDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Administrateur
 */
public class FactureServiceTest {

    FactureService factureService;

    @Mock
    ParamService paramService;

    @Mock
    DecoupageRepository decoupageRepository;

    @Mock
    StockService stockService;

    List<Depsto> depstos;

    ArticlePHDTO article;

    MvtQuittanceDTO mvtsto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        factureService = new FactureService(null, null, null, null, null, stockService, null, null, paramService, null, null, null, null, decoupageRepository, null,null,null,null,null);
        // --------- Article Configuration. One article is composed of 12 pills(unit 1) ==> 2 tablets(unit 2) ==> 1 Box(unit 3)
        article = new ArticlePHDTO();
        article.setCode(1);
        ArticleUniteDTO unitePill = new ArticleUniteDTO();
        unitePill.setUnityDesignation("Pill");
        unitePill.setCodeUnite(1);
        unitePill.setNbPiece(new BigDecimal(12));

        ArticleUniteDTO uniteTablet = new ArticleUniteDTO();
        uniteTablet.setUnityDesignation("Tablet");
        uniteTablet.setCodeUnite(2);
        uniteTablet.setNbPiece(new BigDecimal(2));

        ArticleUniteDTO uniteBox = new ArticleUniteDTO();
        uniteBox.setUnityDesignation("Box");
        uniteBox.setCodeUnite(3);
        uniteBox.setNbPiece(new BigDecimal(1));

        article.setArticleUnites(Arrays.asList(uniteBox, unitePill, uniteTablet));

        // ------ End article configuration
        // ------ Setting the storage  40 pills , 0 tablets, 5 Boxes 
        Depsto depstoPill = new Depsto();
        depstoPill.setCodart(1);
        depstoPill.setQte(new BigDecimal(40));
        depstoPill.setUnite(1);
        depstoPill.setPu(new BigDecimal(3));
        depstoPill.setCoddep(1);

        Depsto depstoTablet = new Depsto();
        depstoTablet.setCodart(1);
        depstoTablet.setQte(new BigDecimal(2));
        depstoTablet.setUnite(2);
        depstoTablet.setPu(new BigDecimal(18));
        depstoTablet.setCoddep(1);

        Depsto depstoBox = new Depsto();
        depstoBox.setCodart(1);
        depstoBox.setQte(new BigDecimal(5));
        depstoBox.setUnite(3);
        depstoBox.setPu(new BigDecimal(36));
        depstoBox.setCoddep(1);

        depstos = new ArrayList(Arrays.asList(depstoPill, depstoBox, depstoTablet));

        // ------ Setting the demanded article: we are demanding 60 pills
        mvtsto = new MvtQuittanceDTO();
        mvtsto.setCodart(1);
        mvtsto.setQuantite(new BigDecimal(60));
        mvtsto.setUnite(1);
        mvtsto.setArticle(article);
    }

//    @Test
//    public void createDecoupageWhenQteAvailableShouldCreateDecoupage() {
//
//        // Setting expected Storage: we will have afthe the decoupage 64 pills,  1 tablet  ,3 Boxes
//        Depsto expectedDepsto1 = new Depsto();
//        expectedDepsto1.setCodart(1);
//        expectedDepsto1.setUnite(1);
//        expectedDepsto1.setQte(new BigDecimal(12));
//        expectedDepsto1.setPu(new BigDecimal("3.000")); // les pu issues du decoupage sont arrondis à 3 chiffres 
//
//        Depsto expectedDepsto2 = new Depsto();
//        expectedDepsto2.setCodart(1);
//        expectedDepsto2.setUnite(1);
//        expectedDepsto2.setQte(new BigDecimal(12));
//        expectedDepsto2.setPu(new BigDecimal("3.000"));// les pu issues du decoupage sont arrondis à 3 chiffres 
//
//        Depsto expectedDepsto3 = new Depsto();
//        expectedDepsto3.setCodart(1);
//        expectedDepsto3.setUnite(1);
//        expectedDepsto3.setQte(new BigDecimal(40));
//        expectedDepsto3.setPu(new BigDecimal(3));
//
//        Depsto expectedDepsto4 = new Depsto();
//        expectedDepsto4.setCodart(1);
//        expectedDepsto4.setUnite(3);
//        expectedDepsto4.setQte(new BigDecimal(4));
//        expectedDepsto4.setPu(new BigDecimal(36));
//
//        // Setting expected preformed deoupages
//        Decoupage decoupage1 = new Decoupage();
//
//        decoupage1.setNumbon("DC00001");
//        decoupage1.setAuto(true);
//        decoupage1.setTypbon(TypeBonEnum.DC);
//
//        DetailDecoupage detailDecoupage1_1 = new DetailDecoupage();
//        detailDecoupage1_1.setCodart(1);
//        detailDecoupage1_1.setUniteOrigine(2);
//        detailDecoupage1_1.setQuantite(new BigDecimal(2));
//        detailDecoupage1_1.setUniteFinal(1);
//        detailDecoupage1_1.setQuantiteObtenue(new BigDecimal(12));
//        detailDecoupage1_1.setDecoupage(decoupage1);
//        detailDecoupage1_1.setCategDepot(CategorieDepotEnum.PH);
//
//        DetailDecoupage detailDecoupage1_2 = new DetailDecoupage();
//        detailDecoupage1_2.setCodart(1);
//        detailDecoupage1_2.setUniteOrigine(3);
//        detailDecoupage1_2.setQuantite(new BigDecimal(1));
//        detailDecoupage1_2.setUniteFinal(2);
//        detailDecoupage1_2.setQuantiteObtenue(new BigDecimal(2));
//        detailDecoupage1_2.setDecoupage(decoupage1);
//        detailDecoupage1_2.setCategDepot(CategorieDepotEnum.PH);
//
//        decoupage1.setDetailDecoupageList(Arrays.asList(detailDecoupage1_1, detailDecoupage1_2));
//        Decoupage decoupage2 = new Decoupage();
//
//        decoupage2.setNumbon("DC00002");
//        decoupage2.setAuto(true);
//        decoupage2.setTypbon(TypeBonEnum.DC);
//
//        DetailDecoupage detailDecoupage2_1 = new DetailDecoupage();
//        detailDecoupage2_1.setCodart(1);
//        detailDecoupage2_1.setUniteOrigine(2);
//        detailDecoupage2_1.setQuantite(new BigDecimal(2));
//        detailDecoupage2_1.setUniteFinal(1);
//        detailDecoupage2_1.setQuantiteObtenue(new BigDecimal(12));
//        detailDecoupage2_1.setDecoupage(decoupage2);
//        detailDecoupage2_1.setCategDepot(CategorieDepotEnum.PH);
//
//        decoupage2.setDetailDecoupageList(Arrays.asList(detailDecoupage2_1));
//        ArgumentCaptor<Decoupage> decoupages = ArgumentCaptor.forClass(Decoupage.class);
//        given(paramService.findcompteurbycode(CategorieDepotEnum.PH, TypeBonEnum.DC)).willReturn(new CompteurPharmacie(CategorieDepotEnum.PH, TypeBonEnum.DC, "DC", "00001", 5));
//        given(stockService.saveDepsto((Depsto) anyObject())).willReturn(true);
//        given(decoupageRepository.save(decoupages.capture())).willReturn(null);
//        factureService.createDecoupage(depstos, 40, mvtsto, article);
//
//        assertThat(depstos.stream().filter(item -> item.getQte().compareTo(BigDecimal.ZERO) > 0).collect(toList())).usingElementComparatorOnFields("codart", "qte", "unite", "pu").containsExactlyInAnyOrder(expectedDepsto1, expectedDepsto2, expectedDepsto3, expectedDepsto4);
////
//        assertThat(decoupages.getAllValues()).usingElementComparatorOnFields("numbon", "auto", "typbon").containsExactlyInAnyOrder(decoupage1, decoupage2);
//
////        assertThat(decoupages.getAllValues()).extracting("detailDecoupageList").usingElementComparatorOnFields("codart", "uniteOrigine", "uniteFinal", "quantiteObtenue").containsExactlyInAnyOrder(detailDecoupage1_1, detailDecoupage1_2, detailDecoupage2_1);
//    }

}
