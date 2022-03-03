package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.stock.domain.Depsto;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Collections;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class DepstoRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private DepstoRepository depstoRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    Depsto depsto;

    @Before
    public void setup() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        "admin",
                        "password",
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    }

    @Test
    public void findByActifShouldReturnOnlyActif() {

        depstoRepository.findAllGrouped(null, null);

    }

    @Test(expected = ObjectOptimisticLockingFailureException.class)
//    @Test
    public void optimisticLockingTest() {
        try {
            depsto = new Depsto();
            depsto.setCoddep(510);
            depsto.setCodart(1);
            depsto.setQte(BigDecimal.ONE);
            depsto.setPu(BigDecimal.TEN);
            depsto.setNumBon("UU10");
            depsto.setUnite(157);
            depsto.setCategDepot(CategorieDepotEnum.PH);
            depsto.setLotInter("Lot");
            depsto.setMemo("saluttttt");
//            depsto.setNum(new Long(1444));

            System.out.println("lot 1:" + depsto.getLotInter());
            System.out.println("version1:" + depsto.getVersion());
            testEntityManager.persist(depsto);
            testEntityManager.flush();
            testEntityManager.detach(depsto);

            final Depsto newEntity = testEntityManager.find(Depsto.class, depsto.getCode());
            newEntity.setLotInter("optimisticLot");
            testEntityManager.persist(newEntity);
            testEntityManager.flush();
            System.out.println("lot 2:" + depsto.getLotInter());
            System.out.println("version2:" + depsto.getVersion());
            depsto.setLotInter("new optimisticLot");
            System.out.println("lot 3" + depsto.getLotInter());
            System.out.println("version3:" + depsto.getVersion());
            depsto = testEntityManager.merge(depsto);
            testEntityManager.persist(depsto);
        } catch (Exception e) {
            System.out.println("exception:" + e.getMessage());
        }
    }

}
