//package achat;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.csys.pharmacie.achat.repository.CablPHService;
//import com.csys.pharmacie.parametrage.repository.ParamService;
//import java.util.Set;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//
//public class CablPHServiceTest {
//	
//
//	 @Autowired   
//	     private CablPHService as;
//	 
//
//	 @Test
//		public void delete() {
//			as.deleteCablByNumBon("12test");
//		}
//	 
//	 @Test
//	public void addCABLPH() {
//		
//		String numbon ="test";
//		List<String> bc = new ArrayList <String> ();
//		bc.add("bc1");
//		bc.add("bc2");
//		bc.add("bc1");
//		as.addCABLPH(numbon, (Set<String>) bc);
//	}
//	 
//	@Test
//	public void getListBonCommandeByNumBl() {
//		
//		assertTrue(as.getListBonCommandeByNumBl("test").size() == 3);
//	}
//	
//	@Test
//	public void getListBlByNumBC()
//	{
//		
//		assertEquals(1,as.getListBlByNumBC("C0600136").size());//(4,CP150003)
//	}
//	
//	@Test
//	public void deleteCablByNumBon() {
//		as.deleteCablByNumBon("12test");
//	}
//	
//	
//}
