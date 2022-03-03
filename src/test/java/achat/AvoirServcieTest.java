//package achat;
//
//
//import com.csys.pharmacie.achat.repository.AvoirFinancierService;
//import com.csys.pharmacie.vente.avoir.repository.AvoirService;
//
//import java.lang.reflect.Array;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import static org.junit.Assert.assertNotEquals;
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
///**
// *
// * @author Farouk
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//public class AvoirServcieTest {
//
//    @Autowired
//    AvoirService as;
//
//    @Test
//    public void findSumByCodArtAndDatbonAndCodarts() throws ParseException {
//        List<String> numdoss = new ArrayList<>();
//        List<String> codart = new ArrayList<>();
//        numdoss.add("16009672");
//        numdoss.add("16009458");
//        codart.add("U0189");
//        codart.add("U0169");
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date datefin = formatter.parse("15/12/2016");
//        Date datedeb = formatter.parse("01/06/2016");
//    }
//}
