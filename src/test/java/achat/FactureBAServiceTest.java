//package achat;
//
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.csys.pharmacie.achat.repository.BonCommandedto;
//import com.csys.pharmacie.achat.repository.FactureBARepository;
//import com.csys.pharmacie.achat.repository.FactureBAService;
//import com.csys.pharmacie.achat.repository.FactureCAService;
//import com.csys.pharmacie.achat.repository.FactureCAdto;
//import com.csys.pharmacie.achat.repository.MvtstoCAdto;
//
//import com.csys.pharmacie.parametrage.entity.Paramph;
//import com.csys.pharmacie.parametrage.repository.ParamService;
//
//import ch.qos.logback.classic.Logger;
//import com.csys.pharmacie.achat.repository.BonRecepDTO;
//import com.csys.pharmacie.achat.repository.MvtstoBADTO;
//
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
//
///**
// *
// * @author farouk
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//
//public class FactureBAServiceTest {
//
//    @Autowired
//    private FactureBAService factureBAService;
//    @Autowired
//    private FactureBARepository factureBArepo;
//
////    @Test
////    public void countNombreBonRetoursInListBonReceptionTest() {
////        Collection<String> listebonreception = new ArrayList<String>();
////        listebonreception.add("BAB1601648");
////
////        assertEquals(new Long(1), factureBAService.countNombreBonRetoursInListBonReception(listebonreception));
////    }
////
////    @Test
////    public void findByNumpieceInAndTypbon() {
////        Collection<String> listebonreception = new ArrayList<String>();
////        listebonreception.add("BAB1601648");
////
////        assertEquals(1, factureBArepo.findByNumpieceInAndTypbon(listebonreception, "rt").size());
////    }
////
////    @Test
////    public void isFactureTest() {
////        List<String> listebonreception = new ArrayList<String>();
////        listebonreception.add("BAB1601645");
////        listebonreception.add("BAB1502358");
////        assertTrue(factureBAService.isFacture(listebonreception));
////    }
//
//    @Test
//    public void modifierFactureBA() throws ParseException {
//        BonRecepDTO b = new BonRecepDTO();
//        b.setBase0Tva00(BigDecimal.ZERO);
//        b.setBase1Tva06(BigDecimal.ZERO);
//        b.setBase1Tva10(BigDecimal.ZERO);
//        b.setBase1Tva18(BigDecimal.ZERO);
//        b.setCoddep("99");
//        b.setCodfrs("0001");
//        b.setCodvend("test");
//        b.setNumBon("BAB1601697");
////	   b.setDatRefFrs(new Date());
//        b.setFodcli(true);
//        b.setMemop("");
//        b.setMntbon(BigDecimal.ZERO);
//        b.setValrem(BigDecimal.ZERO);
//        b.setSmfodec(BigDecimal.ZERO);
//        b.setTva10(BigDecimal.ZERO);
//        b.setTva18(BigDecimal.ZERO);
//        b.setTva6(BigDecimal.ZERO);
//        b.setValrem(BigDecimal.ZERO);
//        List<MvtstoBADTO> details = new ArrayList<MvtstoBADTO>();
//        MvtstoBADTO mvtstoBAdto = new MvtstoBADTO();
//        mvtstoBAdto.setRefArt("00146");
//        mvtstoBAdto.setLot_inter("");
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date datedeb = formatter.parse("06/12/2018");
//        mvtstoBAdto.setDatPer(datedeb);
//        mvtstoBAdto.setMontht(BigDecimal.TEN);
//        mvtstoBAdto.setPriuni(BigDecimal.TEN);
//        mvtstoBAdto.setRemise(BigDecimal.TEN);
//        mvtstoBAdto.setTauTVA(BigDecimal.ZERO);
//        mvtstoBAdto.setNumOrdre("BA001");
//        mvtstoBAdto.setAmc("amctest");
//        details.add(mvtstoBAdto);
//        b.setDetails(details);
//        factureBAService.updateBonReception(b);
//    }
//
//}
