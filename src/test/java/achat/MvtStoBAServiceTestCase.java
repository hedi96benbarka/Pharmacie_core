//package achat;
//
//
//
//import com.csys.pharmacie.achat.repository.MvtStoBAService;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// *
// * @author Farouk
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//public class MvtStoBAServiceTestCase {
//    	@Autowired
//	private MvtStoBAService mvtStoBAService;
//    public MvtStoBAServiceTestCase() {
//    }
//    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }
//
//    // TODO add test methods here.
//    // The methods must be annotated with annotation @Test. For example:
//    //
//    // @Test
//    // public void hello() {}
//    @Test
//	public void testGetDetailsListeAchatBrphByNumBon() {
//		assertNotNull(mvtStoBAService.getDetailsListeAchatBrphByNumBon("BAB1500004"));
//	}
//}
