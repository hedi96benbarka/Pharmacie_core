///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.csys.pharmacie.vente.service;
//
//import com.csys.pharmacie.achat.domain.FactureBA;
//import com.csys.pharmacie.achat.domain.MvtStoBA;
//import com.csys.pharmacie.achat.domain.MvtStoBAPK;
//import com.csys.pharmacie.achat.dto.ArticlePHDTO;
//import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
//import com.csys.pharmacie.achat.service.MvtStoBAService;
//import com.csys.pharmacie.achat.service.ParamAchatServiceClient;
//import com.csys.pharmacie.helper.Action;
//import com.csys.pharmacie.helper.CategorieDepotEnum;
//import com.csys.pharmacie.helper.TypeBonEnum;
//import com.csys.pharmacie.stock.domain.Depsto;
//import com.csys.pharmacie.stock.service.StockService;
//import com.csys.pharmacie.transfert.domain.FactureBE;
//import com.csys.pharmacie.transfert.domain.MvtStoBE;
//import com.csys.pharmacie.vente.domain.PrixMoyPondereArticle;
//import com.csys.pharmacie.vente.repository.PrixMoyPondereArticleRepository;
//import com.csys.pharmacie.vente.repository.PrixReferenceArticleRepository;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import static org.assertj.core.api.Assertions.assertThat;
//import org.hibernate.mapping.Collection;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Matchers.anyList;
//import static org.mockito.Matchers.anyObject;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
///**
// *
// * @author Farouk
// */
//public class PricingServiceTest {
//
//    @Mock
//    StockService stockService;
//    @Mock
//    PrixMoyPondereArticleRepository prixMoyPondereArticleRepository;
//    @Mock
//    PrixReferenceArticleRepository prixReferenceArticleRepository;
//    @Mock
//    PricingService pricingService;
//    @Mock
//    MvtStoBAService mvtStoBAService;
//    @Mock
//    ParamAchatServiceClient paramAchatServiceClient;
//    List<Depsto> listeDepstos = new ArrayList();
//    List<ArticleUniteDTO> articlePHunits = new ArrayList();
//    List<ArticleUniteDTO> articlePHunits2 = new ArrayList();
//    List<ArticlePHDTO> articlePHDTO = new ArrayList();
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        pricingService = new PricingService(stockService, prixMoyPondereArticleRepository, prixReferenceArticleRepository, mvtStoBAService, paramAchatServiceClient);
//
//        Depsto depsto1 = new Depsto(1, 1, new BigDecimal("10"), new BigDecimal("3.33"), 1);
//        depsto1.setNumBon("rc1");
//
//        Depsto depsto6 = new Depsto(6, 1, new BigDecimal("16"), new BigDecimal("1.5"), 2);
//        depsto6.setNumBon("rc1");
//
//        Depsto depsto4 = new Depsto(4, 2, new BigDecimal("9"), new BigDecimal("9.99"), 1);
//        depsto4.setNumBon("rc1");
//
//        Depsto depsto2 = new Depsto(2, 1, new BigDecimal("5"), new BigDecimal("15.00"), 1);
//        depsto2.setNumBon("rc2");
//
//        Depsto depsto3 = new Depsto(3, 1, new BigDecimal("3"), BigDecimal.ZERO, 1);
//        depsto3.setNumBon("rc2");
//
//        Depsto depsto5 = new Depsto(5, 3, new BigDecimal("3"), new BigDecimal("3.00"), 1);
//        depsto5.setNumBon("rc2");
//
//        listeDepstos.add(depsto1);
//        listeDepstos.add(depsto2);
//        listeDepstos.add(depsto3);
//        listeDepstos.add(depsto4);
//        listeDepstos.add(depsto5);
//        listeDepstos.add(depsto6);
//
//        ArticleUniteDTO articleUnite1 = new ArticleUniteDTO(1, 1, BigDecimal.ONE);
//        ArticleUniteDTO articleUnite2 = new ArticleUniteDTO(1, 2, new BigDecimal("4"));
//        articlePHunits.add(articleUnite1);
//        articlePHunits.add(articleUnite2);
//
//        ArticleUniteDTO articleUnite22 = new ArticleUniteDTO(2, 1, BigDecimal.ONE);
//        articlePHunits2.add(articleUnite22);
//        ArticlePHDTO ArticlePHDTO1 = new ArticlePHDTO(articlePHunits, 1);
//        ArticlePHDTO ArticlePHDTO2 = new ArticlePHDTO(articlePHunits2, 3);
//        articlePHDTO.add(ArticlePHDTO1);
//        articlePHDTO.add(ArticlePHDTO2);
//
//    }
//
////    @Test
////    public void updatePMPOnAddReceptShouldUpdatePMP() {
////        List<PrixMoyPondereArticle> oldPmps = Arrays.asList(new PrixMoyPondereArticle(1, BigDecimal.ONE));
////        FactureBA f = new FactureBA();
////        f.setCategDepot(CategorieDepotEnum.PH);
////        f.setNumbon("rc2");
////        f.setTypbon(TypeBonEnum.BA);
////
////        MvtStoBA detailf1 = new MvtStoBA(new MvtStoBAPK(), new BigDecimal("15.00"), new BigDecimal("5"), new BigDecimal("75.00"));
////        detailf1.getPk().setCodart(1);
////
////        MvtStoBA detailf2 = new MvtStoBA(new MvtStoBAPK(), BigDecimal.ZERO, new BigDecimal("3"), BigDecimal.ZERO);
////        detailf2.getPk().setCodart(1);
////
////        MvtStoBA detailf3 = new MvtStoBA(new MvtStoBAPK(), new BigDecimal("3.00"), new BigDecimal("3"), new BigDecimal("9.00"));
////        detailf3.getPk().setCodart(3);
////
////        List<MvtStoBA> listMvtStoBA = Arrays.asList(detailf1, detailf2, detailf3);
////
////        f.setDetailFactureBACollection(listMvtStoBA);
////
////        ArgumentCaptor<List> listPrixMoyPond = ArgumentCaptor.forClass(List.class);
////        given(paramAchatServiceClient.articlePHFindbyListCode(anyList())).willReturn(articlePHDTO);
////
////        given(stockService.findByCodartInAndQteGreaterThanZero(anyList())).willReturn(listeDepstos);
////        given(prixMoyPondereArticleRepository.findByArticleIn(anyObject())).willReturn(oldPmps);
////        given(prixMoyPondereArticleRepository.save(listPrixMoyPond.capture())).willReturn(anyList());
////        pricingService.updatePMPOnADDBA(f);
////        assertThat(listPrixMoyPond.getValue()).containsExactlyInAnyOrder(new PrixMoyPondereArticle(3, new BigDecimal("3.00")));
////        assertThat(oldPmps).contains(new PrixMoyPondereArticle(1, new BigDecimal("4.05")));
////
////    }
//
//    @Test
//    public void updatePMPOnAddFullReturnShouldUpdatePMP() {
//        List<PrixMoyPondereArticle> oldPmps = Arrays.asList(new PrixMoyPondereArticle(1, BigDecimal.ONE));
//        FactureBA f = new FactureBA();
//        f.setTypbon(TypeBonEnum.RT);
//        f.setNumbon("rc2");
//        MvtStoBA detailf1 = new MvtStoBA(new MvtStoBAPK());
//        detailf1.getPk().setCodart(1);
//        detailf1.setQuantite(new BigDecimal("18.00"));
//        detailf1.setPriuni(new BigDecimal("0.85"));
//
//        List<MvtStoBA> listMvtStoBA = Arrays.asList(detailf1);
//        f.setDetailFactureBACollection(listMvtStoBA);
//
//        given(stockService.findByCodartInAndQteGreaterThanZero(anyList())).willReturn(Collections.singletonList(new Depsto(1, 1, new BigDecimal("0"), new BigDecimal("3.33")))); // ==> retour total
//        given(prixMoyPondereArticleRepository.findByArticleIn(anyObject())).willReturn(oldPmps);
//        pricingService.updatePricesAfterReturn(f, CategorieDepotEnum.EC);
//        assertThat(oldPmps).contains(new PrixMoyPondereArticle(1, BigDecimal.ONE));
//
//    }
//
//    @Test
//    public void updatePMPOnAddReturnShouldUpdatePMP() {
//        List<PrixMoyPondereArticle> oldPmps = Arrays.asList(new PrixMoyPondereArticle(1, new BigDecimal("15")));
//        FactureBA f = new FactureBA();
//        f.setTypbon(TypeBonEnum.RT);
//        f.setCategDepot(CategorieDepotEnum.PH);
//
//        MvtStoBA detailf1 = new MvtStoBA(new MvtStoBAPK(), new BigDecimal("17.00"), new BigDecimal("9.00"), new BigDecimal("105.00"));
//        detailf1.getPk().setCodart(1);
//
//        List<MvtStoBA> listMvtStoBA = Arrays.asList(detailf1);
//        f.setDetailFactureBACollection(listMvtStoBA);
//        given(paramAchatServiceClient.articlePHFindbyListCode(anyList())).willReturn(articlePHDTO);
//        given(stockService.findByCodartInAndQteGreaterThanZero(anyList())).willReturn(listeDepstos);
//        given(prixMoyPondereArticleRepository.findByArticleIn(anyObject())).willReturn(oldPmps);
//        pricingService.updatePricesAfterReturn(f, CategorieDepotEnum.EC);
//        assertThat(oldPmps).contains(new PrixMoyPondereArticle(1, new BigDecimal("14.18")));
//
//    }
//
//    @Test
//    public void updatePMPOnRedressemenetShouldUpdatePMP() {
//        List<PrixMoyPondereArticle> oldPmps = Arrays.asList(new PrixMoyPondereArticle(1, new BigDecimal("9.00")));
//        FactureBE f = new FactureBE();
//        f.setCategDepot(CategorieDepotEnum.PH);
//        f.setNumbon("rc2");
////        f.setTypbon(TypeBonEnum.BA);
//
//        MvtStoBE detailf1 = new MvtStoBE(new BigDecimal("3.00"), 1, new BigDecimal("9.00"), 1);
//        detailf1.setCategDepot(CategorieDepotEnum.PH);
//        detailf1.setNumbon("rc2");
//        MvtStoBE detailf2 = new MvtStoBE(new BigDecimal("12.00"), 1, new BigDecimal("4.00"), 2);
//        detailf2.setCategDepot(CategorieDepotEnum.PH);
//        detailf2.setNumbon("rc2");
//
////        MvtStoBA detailf3 = new MvtStoBA(new MvtStoBAPK(), new BigDecimal("3.00"), new BigDecimal("3"), new BigDecimal("9.00"));
////        detailf3.getPk().setCodart(3);
//        List<MvtStoBE> listMvtStoBe = Arrays.asList(detailf1, detailf2);
//
//        ArgumentCaptor<List> listPrixMoyPond = ArgumentCaptor.forClass(List.class);
//        given(paramAchatServiceClient.articlePHFindbyListCode(anyList())).willReturn(articlePHDTO);
//
//        given(stockService.findByCodartInAndQteGreaterThanZero(anyList())).willReturn(listeDepstos);
//        given(prixMoyPondereArticleRepository.findByArticleIn(anyObject())).willReturn(oldPmps);
//        given(prixMoyPondereArticleRepository.save(listPrixMoyPond.capture())).willReturn(anyList());
//        pricingService.updatePMPOnRedressement(f);
//        assertThat(listPrixMoyPond.getValue()).containsExactlyInAnyOrder(new PrixMoyPondereArticle(3, new BigDecimal("3.00")));
//        assertThat(oldPmps).contains(new PrixMoyPondereArticle(1, new BigDecimal("10.05")));
//
//    }
//
//}
