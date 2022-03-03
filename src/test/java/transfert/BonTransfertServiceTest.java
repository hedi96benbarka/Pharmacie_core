/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transfert;

import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.prelevement.service.RetourPrelevementService;
import com.csys.pharmacie.transfert.domain.DetailMvtStoBT;
import com.csys.pharmacie.transfert.domain.FactureBT;
import com.csys.pharmacie.transfert.domain.MvtStoBT;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;
import com.csys.pharmacie.transfert.dto.MvtStoBEDTO;
import com.csys.pharmacie.transfert.dto.MvtstoBTDTO;
import com.csys.pharmacie.transfert.factory.MvtstoBTFactory;
import com.csys.pharmacie.transfert.repository.FactureBTRepository;
import com.csys.pharmacie.transfert.repository.MvtstoBTRepository;
import com.csys.pharmacie.transfert.service.BonTransfertAnnulationService;
import com.csys.pharmacie.transfert.service.BonTransfertService;
import com.csys.pharmacie.transfert.service.CliniSysService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Collections.list;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import static org.mockito.BDDMockito.given;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author DELL
 */
public class BonTransfertServiceTest {

    @Mock
    FactureBTRepository factureBTRepository;
    @Mock
    MvtstoBTRepository mvtstoBTRepository;
    @Mock
    BonTransfertAnnulationService bonTransfertAnnulationService;
    @Mock
    private ParamAchatServiceClient paramAchatServiceClient;
    @Mock
    MvtstoBTFactory mvtstoBTFactory;
    @Mock
    CliniSysService cliniSysService;
    @Autowired
    private BonTransfertService bonTransfertService;
////////////////////////////////////////////////////////////////    
    private DetailMvtStoBT detailMvtStoBT1;
    private DetailMvtStoBT detailMvtStoBT2;
    private DetailMvtStoBT detailMvtStoBT3;
    private DetailMvtStoBT detailMvtStoBT4;

    FactureBT factureBT;
    MvtStoBT mvtstoBT1;
    MvtStoBT mvtstoBT2;
    List<DetailMvtStoBT> list11;
    List<DetailMvtStoBT> list12;
    FactureBTDTO factureBTDTO;

    List<MvtStoBT> list1;

    private Date date;
    private LocalDate localDate;

    MvtstoBTDTO mvtstoDTO;
    List<MvtstoBTDTO> listeMvtstoBTDTO;
    MvtStoBEDTO expectedMvtstoBE1;
    MvtStoBEDTO expectedMvtstoBE2;

    @Before
    public void startup() {
        MockitoAnnotations.initMocks(this);
        this.bonTransfertService = new BonTransfertService(factureBTRepository, bonTransfertAnnulationService, cliniSysService);

        date = new Date(2019, 01, 06);
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        //liste des details
        detailMvtStoBT1 = new DetailMvtStoBT();//pu 1,taux10 
        detailMvtStoBT2 = new DetailMvtStoBT();//pu 1,taux 0
        detailMvtStoBT3 = new DetailMvtStoBT();//pu 5,taux 0
        detailMvtStoBT4 = new DetailMvtStoBT();//pu 7,taux10

        detailMvtStoBT1.setDatPer(localDate);
        detailMvtStoBT1.setLotinter("lot3");
        detailMvtStoBT1.setQuantitePrelevee(new BigDecimal(1));

        detailMvtStoBT2.setDatPer(localDate);
        detailMvtStoBT2.setLotinter("lot2");
        detailMvtStoBT2.setQuantitePrelevee(new BigDecimal(1));

        detailMvtStoBT3.setDatPer(localDate);
        detailMvtStoBT3.setLotinter("lot1");
        detailMvtStoBT3.setQuantitePrelevee(new BigDecimal(7));

        detailMvtStoBT4.setDatPer(localDate);
        detailMvtStoBT4.setLotinter("lot4");
        detailMvtStoBT4.setQuantitePrelevee(new BigDecimal(1));

        detailMvtStoBT1.setPriuni(BigDecimal.ONE);
        detailMvtStoBT1.setTauxTva(BigDecimal.TEN);

        detailMvtStoBT2.setPriuni(BigDecimal.ONE);
        detailMvtStoBT2.setTauxTva(BigDecimal.ZERO);

        detailMvtStoBT3.setPriuni(new BigDecimal(5));
        detailMvtStoBT3.setTauxTva(BigDecimal.ZERO);

        detailMvtStoBT4.setPriuni(new BigDecimal(7));
        detailMvtStoBT4.setTauxTva(BigDecimal.ZERO);
        list11 = Arrays.asList(detailMvtStoBT1, detailMvtStoBT2, detailMvtStoBT3, detailMvtStoBT4);
        list12 = new ArrayList();
        //// 

        mvtstoBT1 = new MvtStoBT(list11, factureBT, BigDecimal.TEN, 1200, "lot1", localDate, 138);
        mvtstoBT1.setDatPer(localDate);
//        mvtstoBT2 = new MvtStoBT(list12, factureBT, BigDecimal.ONE, 1200, "lot2", localDate, 138);
        list1 = Arrays.asList(mvtstoBT1);

        factureBT = new FactureBT(true);
        factureBT.setDetailFactureBTCollection(list1);
        factureBT.setCategDepot(CategorieDepotEnum.PH);

        date = new Date(2019, 01, 06);
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        factureBTDTO = new FactureBTDTO();
        factureBTDTO.setCode("code");
        mvtstoDTO = new MvtstoBTDTO(1200, "lot1", localDate, 138, BigDecimal.TEN, new BigDecimal(8), BigDecimal.ONE);
        listeMvtstoBTDTO = Arrays.asList(mvtstoDTO);
        factureBTDTO.setDetails(listeMvtstoBTDTO);

    }

//    @Test
//    public void testSortingPrice() {
//
//        List<DetailMvtStoBT> listExpected = Arrays.asList(detailMvtStoBT4, detailMvtStoBT3, detailMvtStoBT1, detailMvtStoBT2);
//
//        List<DetailMvtStoBT> liste = Arrays.asList(detailMvtStoBT1, detailMvtStoBT2, detailMvtStoBT3, detailMvtStoBT4);
//        List<DetailMvtStoBT> resultTest = bonTransfertService.testSorting(liste);
//        assertThat(resultTest).containsExactly(detailMvtStoBT4, detailMvtStoBT3, detailMvtStoBT1, detailMvtStoBT2);
//    }

//    @Test
//    public void ValidateTransfertShouldReturnFactureBE() throws ParseException {
//        factureBT.setValide(Boolean.FALSE);
//        factureBT.setCodAnnul(null);
//        expectedMvtstoBE1 = new MvtStoBEDTO(1200, "lot4", localDate, CategorieDepotEnum.PH, 138);
//        expectedMvtstoBE1.setQuantite(BigDecimal.ONE.negate());
//        expectedMvtstoBE1.setPriuni(new BigDecimal(7));
//        expectedMvtstoBE1.setTautva(BigDecimal.ZERO);
//        expectedMvtstoBE2 = new MvtStoBEDTO(1200, "lot3", localDate, CategorieDepotEnum.PH, 138);
//        expectedMvtstoBE2.setQuantite(BigDecimal.ONE.negate());
//        expectedMvtstoBE2.setPriuni(BigDecimal.ONE);
//        expectedMvtstoBE2.setTautva(BigDecimal.TEN);
//
//        given(factureBTRepository.findOne(Matchers.anyString())).willReturn(factureBT);
//        List<MvtStoBEDTO> resultTest = bonTransfertService.validateTransferTest(factureBTDTO);
//        assertThat(resultTest).containsExactly(expectedMvtstoBE1, expectedMvtstoBE2);
//
//    }

}

// ce sont les methodes dans BOnTransfert sur les quels on a effectué les test unitaires
//  public List<DetailMvtStoBT> testSorting(List<DetailMvtStoBT> listeMvtstoBT) {
//
//        List<DetailMvtStoBT> candidMvtstoBTToRedress = listeMvtstoBT
//                .stream()
//                //                        .filter(t -> !(t.getDatPer().equals(mvtStoBT.getDatPer()) && t.getLotinter().equals(mvtStoBT.getLotinter())))
//                .sorted(Comparator.comparing(DetailMvtStoBT::getPriuni).thenComparing(DetailMvtStoBT::getTauxTva).reversed())
//                .collect(Collectors.toList());
//
//        return candidMvtstoBTToRedress;
//
//    }
//
//    @Transactional
//    public List<MvtStoBEDTO> validateTransferTest(FactureBTDTO factureBTDTO) throws ParseException {
//
//        log.debug("Request to validate  Transfert: {}", factureBTDTO);
//
//        FactureBT inBase = factureBTRepository.findOne(factureBTDTO.getCode());
//        //verification existence de la facture a modifier et si elle est annulé 
//        Preconditions.checkBusinessLogique(inBase != null, "transfert.NotFound");
//        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(inBase.getValide()), "transfert.valide");
//        Preconditions.checkBusinessLogique(inBase.getCodAnnul() == null, "transfert.Annule");
//
//        Boolean size = inBase.getDetailFactureBTCollection().size() < factureBTDTO.getDetails().size();
//        log.debug(" ctrl size est {}", size);
//        Preconditions.checkBusinessLogique(!(inBase.getDetailFactureBTCollection().size() < factureBTDTO.getDetails().size()), "transfert-errone");
//
//        Boolean conforme = Boolean.TRUE;
//        List<MvtStoBEDTO> listdto = new ArrayList();
//
//        for (MvtStoBT mvtStoBT : inBase.getDetailFactureBTCollection()) {
//            MvtstoBTDTO matchingMvstoBT = factureBTDTO.getDetails()
//                    .stream()
//                    .filter(item -> item.getArticleID().equals(mvtStoBT.getCodart()) && item.getUnityID().equals(mvtStoBT.getUnite()) && item.getLotInter().equals(mvtStoBT.getLotinter()) && item.getPreemptionDate().equals(mvtStoBT.getDatPer()))
//                    .findFirst().orElse(null);
//            //si on trouve le meme mvtstoBT avec meme lot et meme DatPer et unite et codart , on traite les quantites 
//            if (matchingMvstoBT != null) {
//                mvtStoBT.setQuantiteDefectueuse(matchingMvstoBT.getQuantiteDefectueuse());
//                mvtStoBT.setQuantiteRecue(matchingMvstoBT.getQuantiteRecue());
//                Preconditions.checkBusinessLogique(mvtStoBT.getQuantiteRecue().compareTo(mvtStoBT.getQuantite()) <= 0, "transfert-errone");
//                Preconditions.checkBusinessLogique(mvtStoBT.getQuantiteDefectueuse().compareTo(mvtStoBT.getQuantiteRecue()) <= 0, "transfert-errone");
//            } else // le mvtstoBT est manquant cad la quantite recue est egale a 0 
//            {
//                mvtStoBT.setQuantiteRecue(BigDecimal.ZERO);
//                mvtStoBT.setQuantiteDefectueuse(BigDecimal.ZERO);
//            }
//
//            if (mvtStoBT.getQuantiteDefectueuse().compareTo(BigDecimal.ZERO) == 1) {
//                conforme = Boolean.FALSE;
//            }
//
//            if (mvtStoBT.getQuantite().compareTo(mvtStoBT.getQuantiteRecue()) > 0) {//////!! a corriger Integer motif
//                conforme = Boolean.FALSE;
//                log.debug("mvtStoBT {}", mvtStoBT);
//                log.debug("liste des detail sont{}", mvtStoBT.getDetailMvtStoBTList());
//                List<DetailMvtStoBT> candidMvtstoBTToRedress = mvtStoBT.getDetailMvtStoBTList()
//                        .stream()
//                        .filter(t -> !(t.getDatPer().equals(mvtStoBT.getDatPer()) && t.getLotinter().equals(mvtStoBT.getLotinter())))
//                        .sorted(Comparator.comparing(DetailMvtStoBT::getPriuni).thenComparing(DetailMvtStoBT::getTauxTva).reversed())
//                        .collect(Collectors.toList());
//                Iterator<DetailMvtStoBT> it = candidMvtstoBTToRedress.iterator();
//
//                BigDecimal qteToRedress = mvtStoBT.getQuantite().subtract(mvtStoBT.getQuantiteRecue());
//                log.debug("qteToRedress");
//                while (it.hasNext() && qteToRedress.compareTo(BigDecimal.ZERO) > 0) {
//
//                    DetailMvtStoBT actualDetailMvtstoBT = it.next();
//                    BigDecimal qteRedresseParDetail = actualDetailMvtstoBT.getQuantitePrelevee().min(qteToRedress);
//
//                    MvtStoBEDTO mvtstoBEdto = new MvtStoBEDTO(mvtStoBT.getCodart(), actualDetailMvtstoBT.getLotinter(), actualDetailMvtstoBT.getDatPer(), inBase.getCategDepot(), mvtStoBT.getUnite());
//                    mvtstoBEdto.setQuantite(qteRedresseParDetail.negate());
//                    log.debug(" quantite  a redresser est {} ", mvtstoBEdto.getQuantite());
//
//                    mvtstoBEdto.setPriuni(actualDetailMvtstoBT.getPriuni());
//
//                    mvtstoBEdto.setCodtva(actualDetailMvtstoBT.getCodeTva());
//                    mvtstoBEdto.setTautva(actualDetailMvtstoBT.getTauxTva());
//                    mvtStoBT.setFactureBT(inBase);
//                    listdto.add(mvtstoBEdto);
//                    qteToRedress = qteToRedress.subtract(qteRedresseParDetail);
//                }
//            }
//        };
//
//        //non conforme retourne vrai s'il ya une quantite defectueuse ou une quantite manquante
//        inBase.setConforme(conforme);
//        inBase.setValide(true);
//
////        List<String> usersToNotify = new ArrayList();
////        usersToNotify.add(inBase.getCodvend());
////
////        Object[] obj = new Object[1];
////        obj[0] = inBase.getNumbon();
////        String subject = messages.getMessage("transfert-subjectEmailValidation", null, LocaleContextHolder.getLocale());
////        String body = messages.getMessage("transfert-bodyEmailValidation", obj, LocaleContextHolder.getLocale());
////        EmailDTO emailDTO = new EmailDTO();
////        emailDTO.setSubject(subject);
////        emailDTO.setBody(body);
////        cliniSysService.prepareEmail(usersToNotify, emailDTO);
//        return listdto;
//    }