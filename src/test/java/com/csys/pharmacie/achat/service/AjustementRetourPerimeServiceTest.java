/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.AjustementRetourPerime;
import com.csys.pharmacie.achat.domain.DetailMvtStoRetourPerime;
import com.csys.pharmacie.achat.domain.MvtstoRetourPerime;
import com.csys.pharmacie.achat.domain.RetourPerime;
import com.csys.pharmacie.achat.repository.AjustementRetourPerimeRepository;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.DetailMvtSto;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.parametrage.entity.CompteurPharmacie;
import com.csys.pharmacie.parametrage.entity.CompteurPharmaciePK;
import com.csys.pharmacie.parametrage.repository.CompteurPharmacieRepository;
import com.csys.pharmacie.parametrage.repository.ParamService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import static org.mockito.Matchers.anyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

/**
 *
 * @author Administrateur
 */
public class AjustementRetourPerimeServiceTest {
    
    @Mock
    private AjustementRetourPerimeRepository ajustementRetourPerimeRepository;
    
    @Mock
    private ParamService paramService;
    
    private List<DetailMvtStoRetourPerime> detailMvtStoRetourPerimes;
    
    private RetourPerime factureRP;
    
    private List<MvtstoRetourPerime> mvtstoRetourPerimes;
    
    private AjustementRetourPerimeService ajustementRetourPerimeService;
    
    private Collection<AjustementRetourPerime> result ;
     
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ajustementRetourPerimeService = new AjustementRetourPerimeService(ajustementRetourPerimeRepository, paramService);
    
    }
    
//    @Test
//    public void saveAjustementRetour1(){ 
//        
//        given(paramService.getcompteur(CategorieDepotEnum.PH , TypeBonEnum.AJ)).willReturn("AJ0001");
//      
//        mvtstoRetourPerimes =  new ArrayList<>();
//         detailMvtStoRetourPerimes = new ArrayList<>();
//        detailMvtStoRetourPerimes.add(new DetailMvtStoRetourPerime(1, new BigDecimal(20), new BigDecimal(9.640), BigDecimal.ZERO));
//        detailMvtStoRetourPerimes.add(new DetailMvtStoRetourPerime(2, new BigDecimal(13), new BigDecimal(9.640), BigDecimal.ZERO));
//        
//        MvtstoRetourPerime mrp1 = new MvtstoRetourPerime();
//        mrp1.setCodart(55980);
//        mrp1.setUnite(140);
//        mrp1.setQuantite(new BigDecimal(33));
//        mrp1.setPriuni(new BigDecimal(8.8));
//        mrp1.setTautva(BigDecimal.ZERO);
//        mrp1.setDetailMvtStoList(detailMvtStoRetourPerimes);
//        mvtstoRetourPerimes.add(mrp1);
//        
//         factureRP = new RetourPerime(mvtstoRetourPerimes);
//    Collection<AjustementRetourPerime> result = ajustementRetourPerimeService.saveAjustementRetour(factureRP);
//        assertThat(result).extracting("diffMntTtc").containsExactly(new BigDecimal("27.7200000"));
//        assertThat(result).extracting("diffMntHt").containsExactly(new BigDecimal("27.7200000"));
//    } 
    
//        @Test
//    public void saveAjustementRetour2(){ 
//        
//        mvtstoRetourPerimes =  new ArrayList<>();
//        
//         detailMvtStoRetourPerimes = new ArrayList<>();
//        detailMvtStoRetourPerimes.add(new DetailMvtStoRetourPerime(1, new BigDecimal(20), new BigDecimal(9.640), new BigDecimal(14)));
//        detailMvtStoRetourPerimes.add(new DetailMvtStoRetourPerime(2, new BigDecimal(13), new BigDecimal(9.640), new BigDecimal(14)));
//        
//        MvtstoRetourPerime mrp1 = new MvtstoRetourPerime();
//        mrp1.setCodart(55980);
//        mrp1.setUnite(140);
//        mrp1.setQuantite(new BigDecimal(33));
//        mrp1.setPriuni(new BigDecimal(8.8));
//        mrp1.setTautva(BigDecimal.ZERO);
//        mrp1.setDetailMvtStoList(detailMvtStoRetourPerimes);
//        
//        mvtstoRetourPerimes.add(mrp1);
//    Collection<AjustementRetourPerime> result = ajustementRetourPerimeService.saveAjustementRetour(mvtstoRetourPerimes);
//        assertThat(result).extracting("diffMntTtc").containsExactly(new BigDecimal("72.2568000"));
//        assertThat(result).extracting("diffMntHt").containsExactly(new BigDecimal("27.7200000"));
//    } 
//    
//            @Test
//    public void saveAjustementRetour3(){ 
//        
//        mvtstoRetourPerimes =  new ArrayList<>();
//        
//         detailMvtStoRetourPerimes = new ArrayList<>();
//        detailMvtStoRetourPerimes.add(new DetailMvtStoRetourPerime(1, new BigDecimal(20), new BigDecimal(9.640), new BigDecimal(14)));
//        detailMvtStoRetourPerimes.add(new DetailMvtStoRetourPerime(2, new BigDecimal(13), new BigDecimal(9.640), new BigDecimal(14)));
//        
//        MvtstoRetourPerime mrp1 = new MvtstoRetourPerime();
//        mrp1.setCodart(55980);
//        mrp1.setUnite(140);
//        mrp1.setQuantite(new BigDecimal(33));
//        mrp1.setPriuni(new BigDecimal(8.8));
//        mrp1.setTautva(new BigDecimal(14));
//        mrp1.setDetailMvtStoList(detailMvtStoRetourPerimes);
//        
//        mvtstoRetourPerimes.add(mrp1);
//    Collection<AjustementRetourPerime> result = ajustementRetourPerimeService.saveAjustementRetour(mvtstoRetourPerimes);
//        assertThat(result).extracting("diffMntTtc").containsExactly(new BigDecimal("31.6008000"));
//        assertThat(result).extracting("diffMntHt").containsExactly(new BigDecimal("27.7200000"));
//    } 
}
