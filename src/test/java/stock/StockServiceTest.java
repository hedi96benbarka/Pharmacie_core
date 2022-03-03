package stock;

import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.MvtStoBAPK;
import com.csys.pharmacie.stock.domain.Depsto;

import com.csys.pharmacie.stock.repository.DepstoRepository;
import com.csys.pharmacie.stock.service.StockService;
import java.math.BigDecimal;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = {"classpath:application-context-test.xml"})

public class StockServiceTest {



    @Autowired
    private StockService stockservice;
    @Autowired
    private DepstoRepository DepstoRepository;
    
     @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stockservice = new StockService();

    }

    

//    @Test
//    public void getDesDepotByCodDep() {
//        assertNotNull(stockservice.getDesDepotByCodDep("01"));
//    }
//
//    @Test
//    public void findalldepotph() {
//        assertNotNull(stockservice.findalldepotph(true));
//    }

    @Test
    public void findDepstoByNumBon() {
        assertTrue(stockservice.findDepstoByNumBon("F060208BAB0600129").size() == 9);
    }

//    @Test
//    public void updateQteDepstoToZero() {
//        stockservice.updateQteDepstoToZero("F060209BAB0600149");
//    }

//    @Test
//    public void getArtsInDep() {
//        int size = stockservice.getArtsInDep("99").size();
//    }

//    @Test
//    public void checkArtNotUsed() {
//        assertTrue(stockservice.checkArtNotUsed("BAB1500898").equalsIgnoreCase("false"));
//    }

//    @Test
//    public void findalldepotfrs() {
//        assertTrue(depotrepository.findByDepotFrs(true).size() > 0);
//    }

//    @Test
//    public void findQuantiteOfArticlesInDepV1() {
//        List<String> l = new ArrayList<>();
//        l.add("00146");
//        l.add("0035");
//        assertNotEquals(0, stockservice.findQuantiteOfArticlesInDepV1("99", l).size());
//    }

    @Test
    public void  checkThatProcessStockOnReturnWillcheckQuantity(){
       List <Depsto> listeDepstos= new ArrayList();
       Depsto depsto1= new Depsto(1, 1, BigDecimal.ONE, new BigDecimal("0.24"));
       Depsto depsto2= new Depsto(2, 1, BigDecimal.TEN, new BigDecimal("0.241"));
       
        listeDepstos.add(depsto1);
        listeDepstos.add(depsto2);
        MvtStoBAPK pk = new MvtStoBAPK(1);
        MvtStoBA detailFacture= new MvtStoBA(pk, new BigDecimal("0.26"), BigDecimal.ONE, BigDecimal.ONE);
        Integer availableQteInStock = listeDepstos.stream()
                    .filter(item-> 
                       item.getCodart().equals(detailFacture.getPk().getCodart())
                    && item.getUnite().equals(detailFacture.getCodeUnite())
                    && //m codart et m pu 
                    item.getPu()
                    .subtract(detailFacture.getPriuni().multiply((BigDecimal.valueOf(100).subtract(detailFacture.getRemise())).divide(BigDecimal.valueOf(100))))
                    .abs().compareTo(new BigDecimal("0.1"))<=0)
                    .map(filtredItem -> filtredItem.getQte().intValue())
                    .collect(Collectors.summingInt(Integer::new));
        
       assertThat(availableQteInStock=0); 
         
    }

    private void assertThat(Integer integer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
