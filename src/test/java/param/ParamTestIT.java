//package param;
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
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
//
//import com.csys.pharmacie.parametrage.entity.Paramph;
//import com.csys.pharmacie.parametrage.repository.ParamService;
//
//import ch.qos.logback.classic.Logger;
//import com.csys.pharmacie.parametrage.repository.TvaRepository;
//
//import static org.junit.Assert.*;
//
//import java.math.BigDecimal;
//
///**
// *
// * @author farouk
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//
//public class ParamTestIT {
//	 @Autowired   
//	     private ParamService ps;
//
//	 @Autowired   
//     		private TvaRepository tva;
//
//    
//    public ParamTestIT() {
//    }
//  
//  
//   @Test()
//   public void findall() {	   
//	   ps.findall();
//	   
//   }
//   
//   @Test()
//   public void updateparam() {	  
//	   Paramph param = new Paramph();
//	   param.setCode("tauxfodec");
//	   param.setValeur("1");
//	   assertTrue (ps.updateparam(param));
//	   
//   }
//   
//   @Test()
//   public void findValeur() {	  
//	   assertNotNull (ps.findValeur());
//	   
//   }
//   
//   @Test()
//   public void getPreemptionParams() {
//	   assertNotNull (ps.getPreemptionParams());
//	   
//   }
//   @Test()  
//   public void findalltva() {
//	   assertNotNull( tva.findAll());	
//   }
//   @Test()  
//   public void findFirstByCodTVA() {
//	   assertNotNull( tva.findFirstByCodTVA("0"));	
//   }
//   
//   @Test()  
//   public void findFirstByTauTVA() {
//	   assertNotNull("null", tva.findFirstByTauTVA(BigDecimal.ZERO));
//   }
//   
//}
