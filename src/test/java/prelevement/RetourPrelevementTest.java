/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prelevement;

import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.prelevement.domain.DetailMvtStoPR;
import com.csys.pharmacie.prelevement.domain.DetailRetourPrelevement;
import com.csys.pharmacie.prelevement.domain.FacturePR;
import com.csys.pharmacie.prelevement.domain.MvtStoPR;
import com.csys.pharmacie.prelevement.domain.RetourPrelevement;
import com.csys.pharmacie.prelevement.dto.DetailRetourPrelevementDTO;
import com.csys.pharmacie.prelevement.dto.RetourPrelevementDTO;
import com.csys.pharmacie.prelevement.repository.DetailRetourPrelevementRepository;
import com.csys.pharmacie.prelevement.repository.RetourPrelevementRepository;
import com.csys.pharmacie.prelevement.repository.TraceDetailRetourPrRepository;
import com.csys.pharmacie.prelevement.service.DetailRetourPrelevementService;

import com.csys.pharmacie.prelevement.service.FacturePRService;
import com.csys.pharmacie.prelevement.service.RetourPrelevementService;
import com.csys.pharmacie.stock.repository.DepstoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.mapping.Any;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author DELL
 */
public class RetourPrelevementTest {

    @Mock
    ParamAchatServiceClient paramAchatServiceClient;
    @Mock
    FacturePRService facturePRService;
    @Mock
    RetourPrelevementRepository retourprelevementRepository;
    @Mock
    DetailRetourPrelevementRepository detailRetourPrelevementRepository;
    @Mock
    ParamService paramService;
    @Mock
    ParamServiceClient parametrageService;
    @Mock
    DetailRetourPrelevementService detailRetourPrelevementService;
    @Mock
    TraceDetailRetourPrRepository traceDetailRetourPrRepository;
    @Mock
    DepstoRepository depstoRepository;
    @Mock
    InventaireService inventaireService;
    @Autowired
    private RetourPrelevementService retourPrelevementService;
    private RetourPrelevementDTO retourPrelevementDTO;
    private List<MvtStoPR> listeDetailsMvtstoPrFound;
    private MvtStoPR mvtStoPR1;
    private MvtStoPR mvtStoPR3;
    private MvtStoPR mvtStoPR2;
    private List<DetailRetourPrelevement> listeDetailRetourPrelevement;
    private DetailRetourPrelevement detailRetourPrelevement;
    private DetailMvtStoPR detailMvtstoPr11;
    private DetailMvtStoPR detailMvtstoPr12;
    private DetailMvtStoPR detailMvtstoPr13;

    private DetailMvtStoPR detailMvtstoPr21;
    private DetailMvtStoPR detailMvtstoPr22;
    private DetailMvtStoPR detailMvtstoPr23;

    private DetailMvtStoPR detailMvtstoPr31;
    private DetailMvtStoPR detailMvtstoPr32;
    private DetailMvtStoPR detailMvtstoPr33;

    private List<DetailMvtStoPR> listeDetailMvtsto1;
    private List<DetailMvtStoPR> listeDetailMvtsto2;
    private List<DetailMvtStoPR> listeDetailMvtsto3;
    private Date date;
    private LocalDate localDate;

    @Before
    public void startup() {
        MockitoAnnotations.initMocks(this);
        this.retourPrelevementService = new RetourPrelevementService(retourprelevementRepository, detailRetourPrelevementRepository, paramAchatServiceClient, paramService, facturePRService, parametrageService, detailRetourPrelevementService, traceDetailRetourPrRepository, depstoRepository, inventaireService);
        detailRetourPrelevement = new DetailRetourPrelevement();
        detailRetourPrelevement.setCategDepot(CategorieDepotEnum.PH);
        detailRetourPrelevement.setCodart(1000);
        detailRetourPrelevement.setLotinter("lot1");
        detailRetourPrelevement.setUnite(140);
        detailRetourPrelevement.setQuantite(new BigDecimal(15));
        date = new Date(2019, 01, 06);
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        detailRetourPrelevement.setDatPer(localDate);

        listeDetailRetourPrelevement = new ArrayList();
        listeDetailRetourPrelevement.add(detailRetourPrelevement);

//mvtstopr1
        mvtStoPR1 = new MvtStoPR();
        mvtStoPR1.setCode(1);
        mvtStoPR1.setQtecom(new BigDecimal(4));
        mvtStoPR1.setCodart(1000);
        mvtStoPR1.setUnite(140);
        mvtStoPR1.setLotinter("lot1");
        mvtStoPR1.setDatPer(localDate);

        //liste detail mvtstoPR1
        detailMvtstoPr11 = new DetailMvtStoPR();
        detailMvtstoPr11.setCode(1);
        detailMvtstoPr11.setQtecom(new BigDecimal(1));
        detailMvtstoPr12 = new DetailMvtStoPR();
        detailMvtstoPr11.setCode(2);
        detailMvtstoPr12.setQtecom(new BigDecimal(1));
        detailMvtstoPr13 = new DetailMvtStoPR();
        detailMvtstoPr13.setCode(3);
        detailMvtstoPr13.setQtecom(new BigDecimal(2));
        listeDetailMvtsto1 = Arrays.asList(detailMvtstoPr11, detailMvtstoPr12, detailMvtstoPr13);
        mvtStoPR1.setDetailMvtStoPRList(listeDetailMvtsto1);
//mvtstopr2         
        mvtStoPR2 = new MvtStoPR();
        mvtStoPR2.setCode(2);
        mvtStoPR2.setQtecom(new BigDecimal(5));
        mvtStoPR2.setCodart(1000);
        mvtStoPR2.setLotinter("lot1");
        mvtStoPR2.setDatPer(localDate);
        mvtStoPR2.setUnite(140);
        //liste detail mvtstoPR1

        detailMvtstoPr21 = new DetailMvtStoPR();
        detailMvtstoPr21.setQtecom(new BigDecimal(1));
        detailMvtstoPr22 = new DetailMvtStoPR();
        detailMvtstoPr22.setQtecom(new BigDecimal(1));
        detailMvtstoPr23 = new DetailMvtStoPR();
        detailMvtstoPr23.setQtecom(new BigDecimal(2));
        listeDetailMvtsto2 = Arrays.asList(detailMvtstoPr21, detailMvtstoPr22, detailMvtstoPr23);
        mvtStoPR2.setDetailMvtStoPRList(listeDetailMvtsto2);

        //mvtstopr3         
        mvtStoPR3 = new MvtStoPR();
        mvtStoPR3.setQtecom(new BigDecimal(5));
        mvtStoPR3.setCodart(1000);
        mvtStoPR3.setLotinter("lot1");
        mvtStoPR3.setCode(3);
//        Date date3 = new Date(2019, 01, 06);
//        LocalDate localDate3 = date3.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        mvtStoPR3.setDatPer(localDate3);
        mvtStoPR3.setUnite(140);
        //liste detail mvtstoPR1

        detailMvtstoPr31 = new DetailMvtStoPR();
        detailMvtstoPr31.setQtecom(new BigDecimal(1));
        detailMvtstoPr32 = new DetailMvtStoPR();
        detailMvtstoPr32.setQtecom(new BigDecimal(1));
        detailMvtstoPr33 = new DetailMvtStoPR();
        detailMvtstoPr33.setQtecom(new BigDecimal(2));
        listeDetailMvtsto3 = Arrays.asList(detailMvtstoPr31, detailMvtstoPr32, detailMvtstoPr33);
        mvtStoPR3.setDetailMvtStoPRList(listeDetailMvtsto3);

        listeDetailsMvtstoPrFound = Arrays.asList(mvtStoPR1, mvtStoPR2, mvtStoPR3);

    }

    @Test
    public void processQuantity() {

        //mvtstopr1
        MvtStoPR expectedmvtStoPR1 = new MvtStoPR();
        expectedmvtStoPR1.setCode(1);
        expectedmvtStoPR1.setQtecom(new BigDecimal(0));
        expectedmvtStoPR1.setCodart(1000);
        expectedmvtStoPR1.setLotinter("lot1");
        expectedmvtStoPR1.setDatPer(localDate);
        //liste detail mvtstoPR1

        DetailMvtStoPR expectedDetailMvtstoPr11 = new DetailMvtStoPR();
        expectedDetailMvtstoPr11.setCode(1);
        expectedDetailMvtstoPr11.setQtecom(new BigDecimal(0));
        DetailMvtStoPR expectedDetailMvtstoPr12 = new DetailMvtStoPR();
        expectedDetailMvtstoPr11.setCode(2);
        expectedDetailMvtstoPr12.setQtecom(new BigDecimal(0));
        DetailMvtStoPR expectedDetailMvtstoPr13 = new DetailMvtStoPR();
        expectedDetailMvtstoPr11.setCode(3);
        expectedDetailMvtstoPr13.setQtecom(new BigDecimal(0));
        List<DetailMvtStoPR> expectedDetailMvtsto1List1 = Arrays.asList(expectedDetailMvtstoPr11, expectedDetailMvtstoPr12, expectedDetailMvtstoPr13);
        expectedmvtStoPR1.setDetailMvtStoPRList(expectedDetailMvtsto1List1);

//
        List<MvtStoPR> listeExpctedDMvtsto = Arrays.asList(expectedmvtStoPR1);
        List<MvtStoPR> listeFound = new ArrayList();
        listeFound.add(mvtStoPR1);
        listeFound.add(mvtStoPR2);
//        List<MvtStoPR> result = retourPrelevementService.ProcessSave(listeFound, listeDetailRetourPrelevement);
//         assertThat(result.get(0)).isEqualToComparingOnlyGivenFields(expectedmvtStoPR1,"qtecom");
//          assertThat(result).containsExactlyInAnyOrder(11, 111, 112, 12);
//        System.out.println(result);

    }

    @Test
    public void processQuantitySave() {

        //mvtstopr1
        MvtStoPR expectedmvtStoPR1 = new MvtStoPR();
        expectedmvtStoPR1.setQtecom(new BigDecimal(0));
        expectedmvtStoPR1.setCodart(1000);
        expectedmvtStoPR1.setLotinter("lot1");
        //liste detail mvtstoPR1

        DetailMvtStoPR expectedDetailMvtstoPr11 = new DetailMvtStoPR();
        expectedDetailMvtstoPr11.setQtecom(new BigDecimal(0));
        DetailMvtStoPR expectedDetailMvtstoPr12 = new DetailMvtStoPR();
        expectedDetailMvtstoPr12.setQtecom(new BigDecimal(0));
        DetailMvtStoPR expectedDetailMvtstoPr13 = new DetailMvtStoPR();
        expectedDetailMvtstoPr13.setQtecom(new BigDecimal(0));
        List<DetailMvtStoPR> expectedDetailMvtsto1List1 = Arrays.asList(expectedDetailMvtstoPr11, expectedDetailMvtstoPr12, expectedDetailMvtstoPr13);
        expectedmvtStoPR1.setDetailMvtStoPRList(expectedDetailMvtsto1List1);
//mvtstopr2         
        MvtStoPR expectedmvtStoPR2 = new MvtStoPR();
        expectedmvtStoPR2.setQtecom(new BigDecimal(5));
        expectedmvtStoPR2.setCodart(1000);
        expectedmvtStoPR2.setLotinter("lot1");
        //liste detail mvtstoPR1

        DetailMvtStoPR expecteddetailMvtstoPr21 = new DetailMvtStoPR();
        expecteddetailMvtstoPr21.setQtecom(new BigDecimal(1));
        DetailMvtStoPR expeceteddetailMvtstoPr22 = new DetailMvtStoPR();
        expeceteddetailMvtstoPr22.setQtecom(new BigDecimal(1));
        DetailMvtStoPR expecteddetailMvtstoPr23 = new DetailMvtStoPR();
        expecteddetailMvtstoPr23.setQtecom(new BigDecimal(2));
        List<DetailMvtStoPR> expectedDetailMvtsto1List2 = Arrays.asList(expecteddetailMvtstoPr21, expeceteddetailMvtstoPr22, expecteddetailMvtstoPr23);
        expectedmvtStoPR2.setDetailMvtStoPRList(expectedDetailMvtsto1List2);

        //mvtstopr3         
        MvtStoPR expectedmvtStoPR3 = new MvtStoPR();
        expectedmvtStoPR3.setQtecom(new BigDecimal(5));
        expectedmvtStoPR3.setCodart(1000);
        expectedmvtStoPR3.setLotinter("lot1");
        //liste detail mvtstoPR1

        DetailMvtStoPR expecteddetailMvtstoPr31 = new DetailMvtStoPR();
        expecteddetailMvtstoPr31.setQtecom(new BigDecimal(1));
        DetailMvtStoPR expecteddetailMvtstoPr32 = new DetailMvtStoPR();
        expecteddetailMvtstoPr32.setQtecom(new BigDecimal(1));
        DetailMvtStoPR expecteddetailMvtstoPr33 = new DetailMvtStoPR();
        expecteddetailMvtstoPr33.setQtecom(new BigDecimal(2));
        List<DetailMvtStoPR> expectedDetailMvtsto1List3 = Arrays.asList(expecteddetailMvtstoPr31, expecteddetailMvtstoPr32, expecteddetailMvtstoPr33);
        expectedmvtStoPR3.setDetailMvtStoPRList(expectedDetailMvtsto1List3);
//
        List<MvtStoPR> listeExpctedDMvtsto = Arrays.asList(expectedmvtStoPR1, expectedmvtStoPR2, expectedmvtStoPR3);

//        given(facturePRService.findListDetails(retourPrelevementDTO.getCategDepot(), retourPrelevementDTO.getDateDebut(), retourPrelevementDTO.getDateFin(), retourPrelevementDTO.getCoddepDesti(), retourPrelevementDTO.getCoddepartSrc())).willReturn(listeDetailsMvtstoPrFound);
//
//        
//        List<MvtStoPR> result = retourPrelevementService.ProcessSave(listeDetailsMvtstoPrFound, listeDetailRetourPrelevement);
//        assertThat(result).isEqualTo(listeExpctedDMvtsto);
    }

    @Test
    public void testSortingDate() {
        FacturePR facturePRd1 = new FacturePR();
        facturePRd1.setDatbon((new Date(2019, 01, 05)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        mvtStoPR1.setFacturePR(facturePRd1);

        FacturePR facturePRd2 = new FacturePR();
        facturePRd2.setDatbon((new Date(2019, 01, 06)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        mvtStoPR2.setFacturePR(facturePRd2);

        FacturePR facturePRd3 = new FacturePR();
        facturePRd3.setDatbon((new Date(2019, 01, 04)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        mvtStoPR3.setFacturePR(facturePRd3);

        List<MvtStoPR> list = Arrays.asList(mvtStoPR2, mvtStoPR1, mvtStoPR3);
        List<MvtStoPR> listExpected = Arrays.asList(mvtStoPR1, mvtStoPR2);

        List<MvtStoPR> resultTest = retourPrelevementService.test(list);
        assertThat(resultTest).containsExactly(mvtStoPR3, mvtStoPR1, mvtStoPR2);
    }

    @Test
    public void testSortingPrice() {

        detailMvtstoPr11.setPriuni(BigDecimal.ONE);
        detailMvtstoPr12.setPriuni(BigDecimal.ZERO);
        detailMvtstoPr13.setPriuni(BigDecimal.TEN);

        List<DetailMvtStoPR> list = Arrays.asList(detailMvtstoPr11, detailMvtstoPr12, detailMvtstoPr13);
        List<DetailMvtStoPR> listExpected = Arrays.asList(detailMvtstoPr12, detailMvtstoPr11,detailMvtstoPr13);

        List<DetailMvtStoPR> resultTest = retourPrelevementService.testPrice(list);
        assertThat(resultTest).containsExactly(detailMvtstoPr12, detailMvtstoPr11, detailMvtstoPr13);
    }
}
