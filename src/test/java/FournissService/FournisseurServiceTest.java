//package FournissService;
//
//
//import achat.*;
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
//import com.csys.pharmacie.achat.repository.FournisseurService;
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
//public class FournisseurServiceTest {
//
//   
//    @Autowired
//    private FournisseurService fs;
// 
//
//    @Test
//    public void getPropose() throws ParseException {
//        
//         fs.findByProposePK_Codfrs("0049");
//    }
//
//}
