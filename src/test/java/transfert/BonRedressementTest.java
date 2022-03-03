//package transfert;
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
//import com.csys.pharmacie.achat.repository.BonCommandedto;
//import com.csys.pharmacie.achat.repository.FactureCAService;
//import com.csys.pharmacie.achat.repository.FactureCAdto;
//import com.csys.pharmacie.achat.repository.MvtstoCAdto;
//
//import com.csys.pharmacie.parametrage.entity.Paramph;
//import com.csys.pharmacie.parametrage.repository.ParamService;
//import com.csys.pharmacie.transfert.dto.BonRedressementDTO;
//import com.csys.pharmacie.transfert.dto.MvtstoRedressementDTO;
//import com.csys.pharmacie.transfert.repository.BonRedressementService;
//
//import ch.qos.logback.classic.Logger;
//import helper.MvtstoDTO;
//
//import static org.junit.Assert.*;
//
//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// *
// * @author farouk
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//
//public class BonRedressementTest {
//	 @Autowired   
//     private BonRedressementService bonRedressementService;
//	
//    
//    public BonRedressementTest() {
//    }
//
//   @Test()
//   public void addfacturetest() throws ParseException {
//	   BonRedressementDTO facture = new BonRedressementDTO();
//	   facture.setBase0Tva00(BigDecimal.ZERO);
//	   facture.setBase1Tva06(BigDecimal.ZERO);
//	   facture.setBase1Tva10(BigDecimal.ZERO);
//	   facture.setBase1Tva18(BigDecimal.ZERO);
//	   facture.setCodvend("test");
//	   facture.setMemop("ASD mem");
//	   facture.setMntbon(BigDecimal.ZERO);
//	   facture.setValrem(BigDecimal.ZERO);
//	   facture.setSmfodec(BigDecimal.ZERO);
//	   facture.setStup(false);
//	   facture.setTva10(BigDecimal.ZERO);
//	   facture.setTva18(BigDecimal.ZERO);
//	   facture.setTva6(BigDecimal.ZERO);
//	   facture.setValrem(BigDecimal.ZERO);
//	   facture.setCoddep("99");
//	   facture.setDesdepd("des dep");
//	
//	List <MvtstoRedressementDTO> details = new ArrayList<MvtstoRedressementDTO> ();
//	 
//	MvtstoRedressementDTO mvtstoCAdto = new MvtstoRedressementDTO();
//	mvtstoCAdto.setDesart("aaaa");
//	mvtstoCAdto.setFodart(false);
//	mvtstoCAdto.setMontht(BigDecimal.ZERO);
//	mvtstoCAdto.setPriuni(BigDecimal.ZERO);
//	mvtstoCAdto.setQuantite(BigDecimal.ONE);
//	mvtstoCAdto.setRefArt("00146");
//	mvtstoCAdto.setRemise(BigDecimal.ZERO);
//	mvtstoCAdto.setTauTVA(BigDecimal.ZERO);
//	mvtstoCAdto.setDatPer(new Date());
//	
//
//	details.add(mvtstoCAdto);
//	
//	facture.setDetails(details);
//	bonRedressementService.addBonRedressement(facture);
//   }
//}
