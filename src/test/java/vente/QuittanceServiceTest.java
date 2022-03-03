//package vente;
//
//import com.csys.pharmacie.transfert.entity.MvtStoBT;
//import com.csys.pharmacie.transfert.entity.MvtStoBTPK;
//import com.csys.pharmacie.vente.quittance.dto.MvtstoAvoirVO;
//import com.csys.pharmacie.vente.quittance.entity.Mvtsto;
//import com.csys.pharmacie.vente.quittance.repository.MvtstoRepository;
//import com.csys.pharmacie.vente.quittance.repository.QuittanceService;
//import static org.junit.Assert.*;
//
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
///**
// *
// * @author Farouk
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//public class QuittanceServiceTest {
//
//    @Autowired
//    QuittanceService quittanceService;
//
//    @Autowired
//    private MvtstoRepository mvtstoRepository;
//
//    @Test
//    public void testAjoutArticle() {
//        List<String> s = new ArrayList<>();
//        s.add("FCF9694244");
//        s.add("FCF9694254");
//        List<MvtStoBT> l = new ArrayList<>();
//        MvtStoBT m1 = new MvtStoBT();
//        MvtStoBTPK m2 = new MvtStoBTPK();
//        m2.setCodart("U0196");
//        m1.setMvtStoBTPK(m2);
//        m1.setQuantite(BigDecimal.valueOf(5));
//        l.add(m1);
//        System.out.println("*-*-*-*---*-*-*-*-*" + quittanceService.updateQteFromMvtstoBT(l, s) + "+*+*+*+*+*+*+*+*+*+*++*+*+*");
//    }
//
//    @Test
//    public void findByMvtstoPK_codartAndNumdossAndTypbonINTEST() {
//
//        List<Mvtsto> mvts = mvtstoRepository.findByMvtstoPK_codartAndNumdossAndTypbonIn("16009672", "AA", BigDecimal.ZERO);
//        System.out.println("mvts" + mvts.size());
//    }
//
//    @Test
//    public void findquittanceTEST() {
//
//        List<MvtstoAvoirVO> mvts = mvtstoRepository.findquittanceByNumdossAndCoddep("16009672", "99");
//        System.out.println("qt" + mvts.size());
//    }
//
////    
////    @Test
////    public void findDetailsOfMultiplesQuittances () {
////        List<String> s = new ArrayList<>();
////        s.add("FCF9694244");
////        s.add("FCF9694254");
////        List<MvtStoBT> l = new ArrayList<>();
////        MvtStoBT m1 = new MvtStoBT();
////        MvtStoBTPK m2 = new MvtStoBTPK();
////        m2.setCodart("U0196");
////        m1.setMvtStoBTPK(m2);
////        m1.setQuantite(BigDecimal.valueOf(5));
////        l.add(m1);
////         Gson gson=new Gson();
////        System.out.println("*-*-*-*---*-*-*-*-*"+gson.toJson(qS.findDetailsOfMultiplesQuittances()+"+*+*+*+*+*+*+*+*+*+*++*+*+*"));
////    }
//    @Test
//    public void findByCoddepAndDatbonBetween() throws ParseException {
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date datefin = formatter.parse("15/12/2016");
//        Date datedeb = formatter.parse("01/12/2016");
//
////        assertNotEquals(0, quittanceService.findByCoddepAndDatbonBetween("99", datedeb, datefin));
//    }
//
//    @Test
//    public void findByCoddepAndEtatCliAndDatbonBetweenTest() throws ParseException {
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date datefin = formatter.parse("15/12/2016");
//        Date datedeb = formatter.parse("01/12/2016");
// 
////        assertNotEquals(0, quittanceService.findByCoddepAndEtatCliAndDatbonBetween("99", "0", datedeb, datefin));
//    }
//
//    @Test
//    public void findByMvtsto_CoddepAndDatbonBetweenOrderByDatbonDescTest() throws ParseException {
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date datefin = formatter.parse("15/12/2016");
//        Date datedeb = formatter.parse("01/12/2016");
//
//        assertNotEquals(0, quittanceService.findByMvtsto_CoddepAndDatbonBetweenOrderByDatbonDesc(datedeb, datefin, "99"));
//    }
//
//    @Test
//    public void findByNumbonTest() {
//        assertNotNull(quittanceService.findByNumbon("FCF9695689"));
//    }
//
//    @Test
//    public void findDetailFactureTest() {
//        assertNotNull(quittanceService.findDetailFacture("FCF9695689"));
//    }
//
//    @Test
//    public void findquittanceByNumdossAndCoddepTest() {
//        assertNotNull(quittanceService.findquittanceByNumdossAndCoddep("16009672", "99"));
//    }
//
//    @Test
//    public void findquittanceByNumdossAndCodartTest() {
//        BigDecimal pri = new BigDecimal("6.6");
//        assertNotNull(quittanceService.findquittanceByNumdossAndCodart("16009672", "AA", pri));
//    }
//
//}
