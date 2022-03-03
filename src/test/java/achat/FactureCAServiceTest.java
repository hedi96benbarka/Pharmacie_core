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
//import com.csys.pharmacie.achat.repository.FactureCAService;
//import com.csys.pharmacie.achat.repository.FactureCAdto;
//import com.csys.pharmacie.achat.repository.MvtstoCAdto;
//import com.csys.pharmacie.achat.repository.ProposeRepository;
//import com.csys.pharmacie.achat.repository.ProposeService;
//import com.csys.pharmacie.achat.repository.VCommandedeJourRepository;
//import com.csys.pharmacie.parametrage.entity.Paramph;
//import com.csys.pharmacie.parametrage.repository.ParamService;
//
//import ch.qos.logback.classic.Logger;
//
//import static org.junit.Assert.*;
//
//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.util.ArrayList;
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
//public class FactureCAServiceTest {
//	 @Autowired   
//	     private FactureCAService fca;
//	 @Autowired   
//     	private VCommandedeJourRepository vCommandedeJourRepository;
//
//	 @Autowired   
//  	private ProposeRepository proposeRepository;
//
// 
//    public FactureCAServiceTest() {
//    }
//  
//
//   @Test()
//   public void findfacturebynumbontest() {
//	   
//	   assertNotNull(fca.findfacturebynumbon("CAC0501332").getFacture());
////	   assertEquals(5, fca.findfacturebynumbon("CAC0501332").getDetails().size());
//	 
//	   
//   }
//   
//   
//   @Test()
//   public void findAlltest() {
//	   
//	   assertNotEquals(0,vCommandedeJourRepository.findAll().size());
//	   
//   }
//   @Test()
//   public void findAllProposetest() {
//	   
//	   assertNotEquals(0,proposeRepository.findAll().size());
//   }
//
//   
//   @Test()
//   public void addfacturetest() throws ParseException {
//	   BonCommandedto bondto = new BonCommandedto();
//	   FactureCAdto facture = new FactureCAdto();
//	   facture.setBase0Tva00(BigDecimal.ZERO);
//	   facture.setBase1Tva06(BigDecimal.ZERO);
//	   facture.setBase1Tva10(BigDecimal.ZERO);
//	   facture.setBase1Tva18(BigDecimal.ZERO);
//	   facture.setCoddep("99");
//	   facture.setCodfrs("0001");
//	   facture.setCodvend("test");
////	   facture.setDatRefFrs(new Date());
//	   facture.setFodcli(true);
//	   facture.setMemop("");
//	   facture.setMntbon(BigDecimal.ZERO);
//	   facture.setModreg("");
////	   facture.setNumaffiche("num");
////	   facture.setNumbon("test");
////	   facture.setRaisoc("test");
//	   facture.setValrem(BigDecimal.ZERO);
////	   facture.setSatisf("");
////	   facture.setSatisfait("");
//	   facture.setSmfodec(BigDecimal.ZERO);
//	   facture.setStup(false);
//	   facture.setTypbon("CA");
//	   facture.setTva10(BigDecimal.ZERO);
//	   facture.setTva18(BigDecimal.ZERO);
//	   facture.setTva6(BigDecimal.ZERO);
//	   facture.setValrem(BigDecimal.ZERO);
//
//	   
//	bondto.setFacture(facture);
//	
//	List <MvtstoCAdto> details = new ArrayList<MvtstoCAdto> ();
//	 
//	MvtstoCAdto mvtstoCAdto = new MvtstoCAdto();
//	mvtstoCAdto.setDesart("aaaa");
//	mvtstoCAdto.setFodecArt(false);
//	mvtstoCAdto.setMontht(BigDecimal.ZERO);
//	mvtstoCAdto.setPriuni(BigDecimal.ZERO);
//	mvtstoCAdto.setQuantite(BigDecimal.ONE);
//	mvtstoCAdto.setRefArt("00146");
//	mvtstoCAdto.setRemise(BigDecimal.ZERO);
//	mvtstoCAdto.setTauTVA(BigDecimal.ZERO);
//	
//	
//	details.add(mvtstoCAdto);
//	
//	bondto.setDetails(details);
//	
//	fca.addfacture(bondto);
//	   
//   }
//   
//   @Test()
//   public void updatefacturetest() throws ParseException {
//	   BonCommandedto bondto = new BonCommandedto();
//	   FactureCAdto facture = new FactureCAdto();
//	   facture.setNumBon("CAC0501339");
//	   facture.setBase0Tva00(BigDecimal.ZERO);
//	   facture.setBase1Tva06(BigDecimal.ZERO);
//	   facture.setBase1Tva10(BigDecimal.ZERO);
//	   facture.setBase1Tva18(BigDecimal.ZERO);
//	   facture.setCoddep("22");
//	   facture.setCodfrs("0003");
//	   facture.setCodvend("test");
//	   facture.setFodcli(true);
//	   facture.setMemop("");
//	   facture.setMntbon(BigDecimal.ONE);
//	   facture.setModreg("");
//	   facture.setSmfodec(BigDecimal.ZERO);
//	   facture.setStup(false);
//	   facture.setTypbon("CA");
//	   facture.setTva10(BigDecimal.ZERO);
//	   facture.setTva18(BigDecimal.ZERO);
//	   facture.setTva6(BigDecimal.ZERO);
//	   facture.setValrem(BigDecimal.ZERO);
//	   facture.setDelai(5);
//	   
//	bondto.setFacture(facture);
//	
//	List <MvtstoCAdto> details = new ArrayList<MvtstoCAdto> ();
//	 
//	MvtstoCAdto mvtstoCAdto = new MvtstoCAdto();
//	mvtstoCAdto.setDesart("qqqq");
//	mvtstoCAdto.setFodecArt(false);
//	mvtstoCAdto.setMontht(BigDecimal.ZERO);
//	mvtstoCAdto.setPriuni(BigDecimal.ZERO);
//	mvtstoCAdto.setQuantite(BigDecimal.ONE);
//	mvtstoCAdto.setRefArt("00146");
//	mvtstoCAdto.setRemise(BigDecimal.ZERO);
//	mvtstoCAdto.setTauTVA(BigDecimal.ZERO);
//	
//	MvtstoCAdto mvtstoCAdto1 = new MvtstoCAdto();
//	mvtstoCAdto1.setDesart("test");
//	mvtstoCAdto1.setFodecArt(false);
//	mvtstoCAdto1.setMontht(BigDecimal.ZERO);
//	mvtstoCAdto1.setPriuni(BigDecimal.ZERO);
//	mvtstoCAdto1.setQuantite(BigDecimal.ONE);
//	mvtstoCAdto1.setRefArt("Mo148");
//	mvtstoCAdto1.setRemise(BigDecimal.ZERO);
//	mvtstoCAdto1.setTauTVA(BigDecimal.ZERO);
//
//	details.add(mvtstoCAdto);
//	details.add(mvtstoCAdto1);
//	
//	bondto.setDetails(details);
//	
//	fca.updatefacture(bondto);
//	   
//   }
//   
//   
//   @Test()
//   public void annulerbcph() throws ParseException {
//	   
//	   fca.annulerfacture("CAC0501332", "test");
//   }
//   @Test()
//   public void findListBonCommandeByNumBonReceptest()
//   {
//	  assertEquals(2, fca.findListBonCommandeByNumBonRecep("BAB1601653").size());
//   }
//   
//   
//}
