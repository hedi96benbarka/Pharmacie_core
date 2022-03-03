//package article;
//import static org.junit.Assert.*;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.csys.exception.webhandler.RestPreconditions;
//import com.csys.pharmacie.article.repository.ArticleService;
//import com.csys.pharmacie.article.repository.StockRepository;
//import com.csys.pharmacie.stock.entity.Depot;
//import com.csys.pharmacie.stock.repository.DepstoRepository;
//import com.csys.pharmacie.stock.repository.DepotRepository;
//import com.csys.pharmacie.stock.repository.StockService;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//
//@ContextConfiguration(locations = {"classpath:application-context-test.xml"})
//
//public class ArticleServiceTest {
//	
//	@Autowired
//	private ArticleService articleService;
//
//	 @Test
//	    public void findAllFamArtMereTest() {
//		 
//		 assertNotEquals(0, articleService.findAllFamArtMere().size());
//	 }
//	 
//	 
//	 @Test
//	    public void findarticlephbycodeTest() {
//		 
//		 assertNotNull(articleService.findarticlephbycode("CA0269"));
//	 }
//	 
//	 @Test
//	 	public void findAllFamartTest() {
//		 assertNotEquals(0, articleService.findAllFamart().size());
//	 }
//	
//	
//}
