//package achat;
//
//import static org.junit.Assert.*;
//
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.csys.pharmacie.achat.domain.MvtStoAA;
//import com.csys.pharmacie.achat.repository.AvoirFinancierDTO;
//import com.csys.pharmacie.achat.repository.AvoirFinancierService;
//import com.csys.pharmacie.achat.repository.MvtstoAADTO;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//
//
//public class AvoirFinancierTest {
//	@Autowired
//	AvoirFinancierService afs;
//
//	@Test
//	public void testFindAll() {
//		assertNotEquals(0, afs.findAll().size());
//	}
////	@Test
////	public void testAddAvoirFinancier() throws ParseException {
////			
////		AvoirFinancierDTO dto = new AvoirFinancierDTO();
////		dto.setBase0Tva00(BigDecimal.TEN);
////		dto.setBase1Tva06(BigDecimal.TEN);
////		dto.setBase1Tva10(BigDecimal.TEN);
////		dto.setBase1Tva18(BigDecimal.TEN);
////		dto.setCodfrs("0181");
////		dto.setCodvend("testAF");
////		dto.setFodcli(true);
////		dto.setMemop("test");
////		dto.setMntbon(BigDecimal.TEN);
////		dto.setSmfodec(BigDecimal.TEN);
////		dto.setTva10(BigDecimal.TEN);
////		dto.setTva18(BigDecimal.TEN);
////		dto.setTva6(BigDecimal.TEN);
////	
////
////		  List<MvtstoAADTO> details = new ArrayList<MvtstoAADTO>();
////		  MvtstoAADTO  art = new MvtstoAADTO();
////		  art.setCodTVA("18");
////		  art.setDesart("qqqq");
////		  art.setFodart(false);
////		  art.setMontht(BigDecimal.TEN);
////		  art.setPriuni(BigDecimal.TEN);
////		  art.setQuantite(BigDecimal.TEN);
////		  art.setRefArt("AF0002");
////		  art.setRemise(BigDecimal.TEN);
////		  art.setTauTVA(BigDecimal.ZERO);
////		  details.add(art);
////		  
////		  
////		dto.setDetails(details);
////		
////	
////	afs.addAvoirFinancier(dto);
////	
////	}
////	
//	@Test
//	public void findByDatbonBetween() throws ParseException {
//		   DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//	       Date datedeb = formatter.parse("01/01/2015");
//	       Date datefin = formatter.parse("01/01/2018");
//		assertNotEquals(0, afs.findByDatbonBetween(datedeb,datefin,true).size());
//	}
//	
//}
