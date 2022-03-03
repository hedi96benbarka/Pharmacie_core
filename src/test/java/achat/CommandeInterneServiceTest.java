//package achat;
//
//
//import com.csys.pharmacie.achat.repository.CommandeInterneService;
//import com.csys.pharmacie.article.repository.ArticleService;
//import java.util.ArrayList;
//import java.util.List;
//import static org.junit.Assert.assertTrue;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
///**
// *
// * @author Farouk
// */
//
//@RunWith(SpringJUnit4ClassRunner.class)
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//public class CommandeInterneServiceTest {
//    @Autowired
//	CommandeInterneService cs;
//
//	@Test
//        public void testUpdateListeCommandeInterne(){
//            List<String> s= new ArrayList<>();
//            String numBonTransf="test";
//            s.add("16000000");
//            s.add("16000001");
//            assertTrue(cs.save(s, numBonTransf));
//            
//        } 
//
//     
//}
