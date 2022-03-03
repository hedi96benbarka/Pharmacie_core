///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package achat;
//
//import com.csys.pharmacie.depotFRS.dto.MvtStoBDPRProjection;
//import com.csys.pharmacie.depotFRS.entity.MvtStoBDPR;
//import com.csys.pharmacie.depotFRS.repository.BonDepotFrsService;
//import java.util.List;
//import static java.util.stream.Collectors.toList;
//import static org.junit.Assert.assertTrue;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// *
// * @author Farouk
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//public class BonDepotFrsServiceTest {
//
//    @Autowired
//    BonDepotFrsService bs;
//
////    @Test
////    public void findByCodFrs() {
////        assertTrue(bs.findByCodFrs("0002").size() > 0);
////    }
////
////    @Test
////    public void findByBarCode() {
////        assertTrue("erreur find mvtstoBDPR by barCode", bs.findByBarCode("010871764815778317180825105082041915447", "BF").size() > 0);
////    }
//    @Test
//    public void checkUniqueBarCodePerArt() {
//        String barCode = "010871764815778317180825105082041915447";
//        List<String> listMvtsto = bs.checkUniqueBarCodePerArt(barCode);
//        assertTrue("le code à barre " + barCode + " correspond aux articles : " + listMvtsto.toString(), listMvtsto.size() <= 1);
//    }
//
//    @Test
//    public void checkUniqueBarCodePerFrs() {
//        String barCode = "010871764815778317180825105082041915447";
//        List<String> listMvtsto = bs.checkUniqueBarCodePerFrs(barCode);
//        assertTrue("le code à barre " + barCode + " correspond aux articles : " + listMvtsto.toString(), listMvtsto.size() <= 1);
//    }
//}
