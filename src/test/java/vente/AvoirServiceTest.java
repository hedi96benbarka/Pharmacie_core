//package vente;
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.csys.pharmacie.vente.avoir.dto.AvoirDTO;
//import com.csys.pharmacie.vente.avoir.dto.MvtstoAvoirDTO;
//import com.csys.pharmacie.vente.avoir.repository.AvoirService;
//import static org.junit.Assert.*;
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//
///**
// *
// * @author farouk
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//
//public class AvoirServiceTest {
//	 @Autowired   
//     private AvoirService avoirService;
//	
//    
//    public AvoirServiceTest() {
//    }
//
//    
//    @Test
//    public void addavoirTEST() throws ParseException
//    {
//    	AvoirDTO facture = new AvoirDTO();
//    	List<MvtstoAvoirDTO> list = new ArrayList<MvtstoAvoirDTO>();
//
//    	
//    	facture.setBase0Tva00(BigDecimal.ZERO);
//  	   facture.setBase1Tva06(BigDecimal.ZERO);
//  	   facture.setBase1Tva10(BigDecimal.ZERO);
//  	   facture.setBase1Tva18(BigDecimal.ZERO);
//  	   facture.setCodvend("test");
//  	   facture.setMemop("ASD mem");
//  	   facture.setMntbon(BigDecimal.ZERO);
//  	   facture.setValrem(BigDecimal.ZERO);
//  	   facture.setSmfodec(BigDecimal.ZERO);
//  	   facture.setStup(false);
//  	   facture.setTva10(BigDecimal.ZERO);
//  	   facture.setTva18(BigDecimal.ZERO);
//  	   facture.setTva6(BigDecimal.ZERO);
//  	   facture.setValrem(BigDecimal.ZERO);
//  	   facture.setCoddep("99");
//  	   facture.setNumCha("123");
//   	MvtstoAvoirDTO mvtsto = new MvtstoAvoirDTO();
//  	   
//  		mvtsto.setDesart("aaaa");
//  		mvtsto.setFodart(false);
//  		mvtsto.setMontht(BigDecimal.ZERO);
//  		mvtsto.setPriuni(BigDecimal.ZERO);
//  		mvtsto.setQuantite(BigDecimal.ONE);
//  		mvtsto.setRefArt("00146");
//  		mvtsto.setRemise(BigDecimal.ZERO);
//  		mvtsto.setTauTVA(BigDecimal.ZERO);
//  		mvtsto.setDatPer(new Date());
//   		mvtsto.setCodTVA("1");
//   	
//  	   
//  	   mvtsto.setRefArt("AA");
//    	mvtsto.setQuantite(new BigDecimal(1));
//    	list.add(mvtsto);   
//    	
//    	facture.setNumDoss("16009672");
//    	facture.setDetails(list);
//   	avoirService.AddAvoir(facture);
//    }
//    
//    @Test
//    public void testFindAll() throws ParseException {
//        List<String> numdoss = new ArrayList<>();
//        List<String> codart = new ArrayList<>();
//        numdoss.add("16009672");
//        numdoss.add("16009458");
//        codart.add("U0189");
//        codart.add("U0169");
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date datefin = formatter.parse("15/12/2016");
//        Date datedeb = formatter.parse("01/06/2016");
//         
//    }
//    
//    @Test
//    public void findByCoddepAndDatbonBetweenTest() throws ParseException {
//    	  DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//          Date datefin = formatter.parse("15/12/2016");
//          Date datedeb = formatter.parse("01/06/2016");
//          List<String> s=new ArrayList<>();
//          s.add("99");
//          s.add("01");
//          
//    	assertNotEquals(0,avoirService.findByCoddepAndDatbonBetween(s, datedeb, datefin));
//    }
//    
//    @Test
//    public void findByCoddepAndEtatCliAndDatbonBetweenTest() throws ParseException {
//    	  DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//          Date datefin = formatter.parse("15/12/2016");
//          Date datedeb = formatter.parse("01/06/2016");
//           List<String> s=new ArrayList<>();
//          s.add("99");
//          s.add("01");
//    	assertNotEquals(0,avoirService.findByCoddepAndEtatCliAndDatbonBetween(s,"0", datedeb, datefin));
//    }
//   
//    @Test
//    public void findByNumbonTest() throws ParseException {
//    	            
//    	assertNotNull(avoirService.findByNumbon("AVA1600064"));
//    }
//    
//    @Test
//    public void findClientHospitaliseTest() throws ParseException {
//    	            
//    	assertNotEquals(0,avoirService.findClientHospitalise());
//    }
//    
//    
//    @Test
// 	public void findFirst10ClientLikeTest() {
//	 assertNotEquals(0, avoirService.findFirst10ClientLike("a").size());
//    	}
// 
//    @Test
// 	public void findFirst10ClientTest() {
//	 assertNotEquals(0, avoirService.findFirst10Client().size());
//    	}
//    
//    
//  }
