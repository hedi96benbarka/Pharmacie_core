//package transfert;
//
//import com.csys.pharmacie.achat.repository.AvoirFinancierService;
//import com.csys.pharmacie.transfert.dto.BonTransferInterDepotDTO;
//import com.csys.pharmacie.transfert.dto.MvtstoInterDepotDTO;
//import com.csys.pharmacie.transfert.repository.BonTransfertInterDepotService;
//
//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.Assert;
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
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//public class BonTransfertInterDepotServiceTest {
//
//    @Autowired
//    BonTransfertInterDepotService bonTIDService;
//
//    @Test
//    public void testFindAll() throws ParseException {
//        BonTransferInterDepotDTO bonDto = new BonTransferInterDepotDTO();
//        MvtstoInterDepotDTO art = new MvtstoInterDepotDTO();
//        List<MvtstoInterDepotDTO> details = new ArrayList<>();
//        bonDto.setBase0Tva00(BigDecimal.ZERO);
//        bonDto.setBase1Tva06(BigDecimal.ZERO);
//        bonDto.setBase1Tva10(BigDecimal.ZERO);
//        bonDto.setBase1Tva18(BigDecimal.ZERO);
//        bonDto.setCodvend("farouk");
//        bonDto.setDepotDestination("01");
//        bonDto.setDepotSource("99");
//        bonDto.setMemop("test");
//        bonDto.setMntbon(BigDecimal.TEN);
//        bonDto.setSmfodec(BigDecimal.ONE);
//        bonDto.setStup(false);
//        bonDto.setTva10(BigDecimal.TEN);
//        bonDto.setTva18(BigDecimal.TEN);
//        bonDto.setTva6(BigDecimal.TEN);
//        bonDto.setValrem(BigDecimal.ZERO);
//
//        art.setCodTVA("18");
//        art.setDesart("ferouk");
//        art.setFodart(false);
//        art.setMontht(BigDecimal.TEN);
//        art.setPriuni(BigDecimal.TEN);
//        art.setQuantite(BigDecimal.TEN);
//        art.setRefArt("Bp31");
//        art.setRemise(BigDecimal.TEN);
//        art.setTauTVA(BigDecimal.ZERO);
//        details.add(art);
//        
//        bonDto.setDetails(details);
//        boolean result=bonTIDService.addBonTransferInterDepot(bonDto);
//        Assert.assertEquals(true, result);
//    }
//
//}
