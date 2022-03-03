package com.csys.pharmacie.stock.repository;
import com.csys.pharmacie.stock.repository.ArticleReorderPointPlanningRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(
    replace = Replace.ANY
)
public class ArticleReorderPointPlanningTest {
  @Autowired
  private ArticleReorderPointPlanningRepository articlereorderpointplanningRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @Before
  public void setup() {
  }

  @Test
  public void test() {
  }
}

