//package achat;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.csys.pharmacie.achat.repository.MvtStoBAService;
//import com.csys.pharmacie.achat.repository.MvtstoBAdao;
//
//import helper.FactoriesRepository;
//import javax.persistence.PersistenceContext;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//
//
//public class MvtStoBAServiceTest {
//	
//	@Autowired
//	private MvtStoBAService mvtStoBAService;
//	  @PersistenceContext(unitName = "Gclinique")
//    private EntityManager em1;
//	  
//	 
//
//
//
////	@Test
////	public void testDeleteMvtStoBAByNumBon() {
////
////	assertNotNull(mvtStoBAService.deleteMvtStoBAByNumBon("BAB1500004"));
////	}
//	
//	@Test
//	public void updatenumfacblmvtstoBA() {
//		
//		List <String> lst = new ArrayList<String>();
//		
//		lst.add("BAB1500004");
//		lst.add("BAB1500001");
//		
//
//	assertTrue(mvtStoBAService.updatenumfacblmvtstoBA(lst, "numbon", new Date()));
//	}
//	
//	@Test
//	public void findMvtStoBAByCodartAndNumBon() {
//		
//		assertNotNull(mvtStoBAService.findMvtStoBAByCodartAndNumBon("BAB1500001","MEDS00014"));	}
//	
//	
//	
//
//}
