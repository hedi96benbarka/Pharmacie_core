///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.csys.pharmacie.achat.service;
//
//import com.csys.pharmacie.achat.domain.FactureBA;
//import com.csys.pharmacie.achat.domain.MvtStoBA;
//import com.csys.pharmacie.achat.domain.MvtStoBAPK;
//import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
//import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
//import com.csys.pharmacie.achat.dto.DetailCommandeAchatDTO;
//import com.csys.pharmacie.achat.factory.FactureBAFactory;
//import com.csys.pharmacie.achat.repository.FactureBARepository;
//import com.csys.pharmacie.achat.repository.MvtstoBARepository;
//import com.csys.pharmacie.helper.PurchaseOrderReceptionState;
//import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.PARTIALLY_RECIVED;
//import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.RECEIVED;
//import com.csys.pharmacie.parametrage.repository.ParamService;
//import com.csys.pharmacie.stock.service.StockService;
//import com.csys.pharmacie.vente.service.PricingService;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import static org.assertj.core.api.Assertions.assertThat;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import static org.mockito.BDDMockito.given;
//import org.mockito.Matchers;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
///**
// *
// * @author Farouk
// */
//public class FactureBAServiceTest {
//
//    @Mock
//    DemandeServiceClient demandeServiceClient;
//    @Mock
//    ReceptionDetailCAService receptionDetailCAService;
//    @Mock
//    ParamService paramService;
//    @Mock
//    FcptFrsPHService fcptFrsPHService;
//    @Mock
//    FactureBAFactory factureBAFactory;
//    @Mock
//    StockService stockService;
//    @Mock
//    FactureBARepository factureBARepository;
//    @Mock
//    EtatReceptionCAService etatReceptionCAService;
//
//    @Mock
//    PricingService pricingService;
//
//
//    
//    
//    @Mock
//    ParamServiceClient parametrageService;
//
//    FactureBAService factureBAService;
//    ParamAchatServiceClient paramAchatServiceClient;
//    ReceivingService receivingService;
//    @Mock
//    MvtstoBARepository mvtstoBARepository;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        factureBAService = new FactureBAService(factureBARepository, paramService, fcptFrsPHService, factureBAFactory, stockService, demandeServiceClient, receptionDetailCAService, pricingService, paramAchatServiceClient, receivingService, etatReceptionCAService,parametrageService,mvtstoBARepository);
//    }
//
//    @Test
//    public void getDetailOfPurchaseOrdersShouldReturnDetailsOfPurchaseOrders() {
//        // creating first purchaseOrder
//        CommandeAchatDTO ca1 = new CommandeAchatDTO(1, "CA1");
//
//        DetailCommandeAchatDTO detailca11 = new DetailCommandeAchatDTO(1, 1, new BigDecimal("10"));
//        DetailCommandeAchatDTO detailca12 = new DetailCommandeAchatDTO(2, 2, new BigDecimal("10"));
//
//        List<DetailCommandeAchatDTO> listDetailsca1 = new ArrayList();
//        listDetailsca1.add(detailca11);
//        listDetailsca1.add(detailca12);
//
//        ca1.setDetailCommandeAchatCollection(listDetailsca1);
//
//        //creating second purchaseOrder
//        CommandeAchatDTO ca2 = new CommandeAchatDTO(2, "CA2");
//        DetailCommandeAchatDTO detailca21 = new DetailCommandeAchatDTO(3, 3, new BigDecimal("10"));
//
//        List<DetailCommandeAchatDTO> listeDetilsca2 = new ArrayList();
//        listeDetilsca2.add(detailca21);
//
//        ca2.setDetailCommandeAchatCollection(listeDetilsca2);
//
//        // creating the liste of the purchase orders
//        Set<CommandeAchatDTO> purOrdsList = new HashSet();
//        purOrdsList.add(ca2);
//        purOrdsList.add(ca1);
//
////         creating the liste of the recived details of purcheased order
//        ReceptionDetailCA recepDetailCA1 = new ReceptionDetailCA("", 1, 1, new BigDecimal("3"));
//        ReceptionDetailCA recepDetailCA2 = new ReceptionDetailCA("", 2, 2, new BigDecimal("5"));
//        ReceptionDetailCA recepDetailCA3 = new ReceptionDetailCA("", 3, 3, new BigDecimal("5"));
//
//        List<ReceptionDetailCA> receptionDetailCAs = new ArrayList();
//        receptionDetailCAs.add(recepDetailCA1);
//        receptionDetailCAs.add(recepDetailCA2);
//        receptionDetailCAs.add(recepDetailCA3);
//
//        // the argument of the tested method
//        List<Integer> listePurchOrdCodes = new ArrayList();
//        listePurchOrdCodes.add(Integer.SIZE);
//        listePurchOrdCodes.add(Integer.SIZE);
//
//        given(demandeServiceClient.findListCommandeAchat(Matchers.anyList(), Matchers.anyString())).willReturn(purOrdsList);
//        given(receptionDetailCAService.findByCodesCAIn(Matchers.anyList())).willReturn(receptionDetailCAs);
//
//        List<DetailCommandeAchatDTO> result = factureBAService.getDetailOfPurchaseOrders(listePurchOrdCodes);
//
//        //the result must contains the details of the second purchase order minus the recived details and then the details of the first purchase order minus the recived details
//    //    assertThat(result).extracting("quantiteRestante").containsExactly(new BigDecimal("5"), new BigDecimal("7"), new BigDecimal("5"));
//
//    }
//
//    @Test
//    public void processPurchaseOrdersShouldReturnProcessedPurchaseOrders() {
//        FactureBA f = new FactureBA();
//
//        MvtStoBA detailf1 = new MvtStoBA(new MvtStoBAPK());
//        detailf1.setCodart(1);
//        detailf1.setQuantite(new BigDecimal("10"));
//
//        MvtStoBA detailf2 = new MvtStoBA(new MvtStoBAPK());
//        detailf2.setCodart(1);
//        detailf2.setQuantite(new BigDecimal("5"));
//
//        MvtStoBA detailf3 = new MvtStoBA(new MvtStoBAPK());
//        detailf3.setCodart(2);
//        detailf3.setQuantite(new BigDecimal("10"));
//
//        List<MvtStoBA> listMvtStoBA = new ArrayList();
//        listMvtStoBA.add(detailf3);
//        listMvtStoBA.add(detailf2);
//        listMvtStoBA.add(detailf1);
//
//        f.setDetailFactureBACollection(listMvtStoBA);
//
//        DetailCommandeAchatDTO detailCA1 = new DetailCommandeAchatDTO(1, 1);
//        detailCA1.setQuantite(new BigDecimal("10"));
//        detailCA1.setQuantiteRestante(new BigDecimal("10"));
//        DetailCommandeAchatDTO detailCA2 = new DetailCommandeAchatDTO(2, 1);
//        detailCA2.setQuantite(new BigDecimal("10"));
//        detailCA2.setQuantiteRestante(new BigDecimal("10"));
//        DetailCommandeAchatDTO detailCA3 = new DetailCommandeAchatDTO(3, 2);
//        detailCA3.setQuantite(new BigDecimal("10"));
//        detailCA3.setQuantiteRestante(new BigDecimal("10"));
//
//        List<DetailCommandeAchatDTO> details = new ArrayList();
//        details.add(detailCA1);
//        details.add(detailCA2);
//        details.add(detailCA3);
//
//        ArgumentCaptor<List> listeReceptionDetailsCA = ArgumentCaptor.forClass(List.class);
//        ArgumentCaptor<List> listeStatePurchaseOrders = ArgumentCaptor.forClass(List.class);
//
//        given(receptionDetailCAService.save(listeReceptionDetailsCA.capture())).willReturn(null);
//        given(etatReceptionCAService.save(listeStatePurchaseOrders.capture())).willReturn(null);
//        factureBAService.processPurchaseOrders(f, details);
//        assertThat(f.getRecivedDetailCA()).extracting("quantiteReceptione").containsExactly(new BigDecimal("10"), new BigDecimal("5"), new BigDecimal("10"));
//        assertThat(listeStatePurchaseOrders.getValue()).extracting("etatReception").containsExactly(RECEIVED, PARTIALLY_RECIVED, RECEIVED);
//
//    }
//
//}
