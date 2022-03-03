///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package achat;
//
//import com.csys.pharmacie.depotFRS.repository.BonPrelevementService;
//import com.csys.pharmacie.depotFRS.dto.FactureBDPRDTO;
//import com.csys.pharmacie.depotFRS.dto.MvtstoBDPRDTO;
//import java.lang.reflect.Array;
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import org.assertj.core.api.Assertions;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// *
// * @author Farouk
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//public class BonPrelevementTest {
//
//    @Autowired
//    BonPrelevementService bs;
//
//    @Test
//    public void findByDateBetween() throws ParseException {
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date datefin = formatter.parse("15/12/2016");
//        Date datedeb = formatter.parse("01/06/2016");
//        List<FactureBDPRDTO> l = bs.findByDatbonBetween(datedeb, datefin);
//
//        Assert.assertTrue(l.size() > 0);
//
//    }
//
//       @Test
//    public void addPrelevement() throws ParseException {
//        FactureBDPRDTO b = new FactureBDPRDTO();
//        b.setBase0Tva00(BigDecimal.ZERO);
//        b.setBase1Tva06(BigDecimal.ZERO);
//        b.setBase1Tva10(BigDecimal.ZERO);
//        b.setBase1Tva18(BigDecimal.ZERO);
//        b.setCodDep("BF");
//        b.setCodfrs("0001");
//        b.setCodvend("test");
//        b.setNumBon("BAB1601697");
//        b.setFodcli(true);
//        b.setMemop("");
//        b.setMntbon(BigDecimal.ZERO);
//        b.setValrem(BigDecimal.ZERO);
//        b.setSmfodec(BigDecimal.ZERO);
//        b.setTva10(BigDecimal.ZERO);
//        b.setTva18(BigDecimal.ZERO);
//        b.setTva6(BigDecimal.ZERO);
//        b.setValrem(BigDecimal.ZERO);
//        MvtstoBDPRDTO m = new MvtstoBDPRDTO();
//        m.setBarcode("010871764815778317180825105082041915447");
//        m.setRefArt("C1136");
//        m.setReference("1070275-15");
//        m.setIdentifiant("08717648157783");
//        m.setLotFrs("5082041");
//        m.setNumBD("BDD1600038");
//        m.setNumSerie("");
//        m.setQuantite(BigDecimal.ONE);
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date datedeb = formatter.parse("25/08/2018");
//        m.setDatPer(datedeb);
//        m.setMontht(BigDecimal.TEN);
//        m.setPriuni(BigDecimal.TEN);
//        m.setRemise(BigDecimal.TEN);
//        m.setTauTVA(BigDecimal.ZERO);
//        List<MvtstoBDPRDTO> lm=new ArrayList<>();
//        lm.add(m);
//        b.setDetails(lm);
//        Assertions.assertThat(bs.addBonPrelevement(b));
////        Assertions.assertThatThrownBy(b::getNumBon)
////                .hasMessageContaining("exception");
//
//    }
//}
