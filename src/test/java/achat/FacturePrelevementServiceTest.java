///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package achat;
//
//import com.csys.pharmacie.achat.Facturation.DTO.FacturePrelevementDTO;
//import java.util.Date;
//import java.util.List;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import com.csys.pharmacie.achat.Facturation.DTO.FacturationProjection;
//import com.csys.pharmacie.achat.Facturation.repository.FacturePrelevementService;
//import com.csys.pharmacie.parametrage.entity.CompteurPharmacie;
//import com.csys.pharmacie.parametrage.repository.ParamService;
//import java.lang.reflect.Array;
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
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
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//public class FacturePrelevementServiceTest {
//
//    @Autowired
//    FacturePrelevementService fps;
//    @Autowired
//    ParamService paramService;
//
//    /**
//     * Test of findByByDateBonBetween method, of class
//     * FacturePrelevementService.
//     */
//    @Test
//    public void testFindByByDateBonBetween() {
//        System.out.println("findByByDateBonBetween");
//        long a = new Long("1451602800000"), b = new Long("1480636512000");
//        Date du = new Date(a);
//        Date au = new Date(b);
//
//        List<FacturationProjection> result = fps.findByByDateBonBetween(du, au,true);
//        assertTrue("Erreur methode findByByDateBonBetween ", result.size() > 0);
//
//    }
//
//    @Test
//    public void addFacturePrelevement() throws ParseException {
//        System.out.println("findByByDateBonBetween");
//        FacturePrelevementDTO b = new FacturePrelevementDTO();
//        b.setBase0Tva00(BigDecimal.ZERO);
//        b.setBase1Tva06(BigDecimal.ZERO);
//        b.setBase1Tva10(BigDecimal.ZERO);
//        b.setBase1Tva18(BigDecimal.ZERO);
//        b.setCodfrs("0049");
//        b.setCodvend("test");
//        b.setFodcli(true);
//        b.setMntbon(BigDecimal.ZERO);
//        b.setValrem(BigDecimal.ZERO);
//        b.setSmfodec(BigDecimal.ZERO);
//        b.setTva10(BigDecimal.ZERO);
//        b.setTva18(BigDecimal.ZERO);
//        b.setTva6(BigDecimal.ZERO);
//        b.setValrem(BigDecimal.ZERO);
//        List<String> ls = new ArrayList<>();
//        ls.add("FEFBF00020");
//        ls.add("FEFBF00019");
//        ls.add("FEFBF00018");
//        ls.add("FEFBF00017");
//        b.setBonRelatif(ls);
//        CompteurPharmacie c = paramService.findcompteurbycode("FL");
//        assertTrue(fps.addFacturePrelevementClient(b).equalsIgnoreCase("FL" + c.getP1() + c.getP2()));
//    }
//
//}
