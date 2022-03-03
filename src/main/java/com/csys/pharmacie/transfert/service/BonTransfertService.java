/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DemandeTrDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.service.DemandeServiceClient;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.EmailDTO;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.transfert.domain.FactureBT;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.transfert.domain.Btbt;
import com.csys.pharmacie.transfert.domain.DetailMvtStoBT;
import com.csys.pharmacie.transfert.domain.MvtStoBT;
import com.csys.pharmacie.transfert.domain.QFactureBT;
import com.csys.pharmacie.transfert.domain.QMvtStoBT;
import com.csys.pharmacie.transfert.dto.BonTransferAnnulationDTO;
import com.csys.pharmacie.transfert.dto.DetailsTransfertDTO;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;
import com.csys.pharmacie.transfert.dto.FactureBTEditionDTO;
import com.csys.pharmacie.transfert.dto.MvtstoBTDTO;
import com.csys.pharmacie.transfert.dto.MvtstoBTEditionDTO;
import com.csys.pharmacie.transfert.factory.BonTransfertRecupFactory;
import com.csys.pharmacie.transfert.factory.BtfeFactory;
import com.csys.pharmacie.transfert.factory.FactureBTFactory;
import com.csys.pharmacie.transfert.factory.MvtstoBTFactory;
import com.csys.pharmacie.transfert.repository.BtbtRepository;
import com.csys.pharmacie.transfert.repository.FactureBTRepository;
import com.csys.pharmacie.transfert.repository.MvtstoBTRepository;
import com.csys.pharmacie.vente.quittance.service.FactureService;
import com.csys.util.Preconditions;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;

/**
 *
 * @author Farouk
 */
@Service("BonTransfertService")
public class BonTransfertService {

    private final Logger log = LoggerFactory.getLogger(BonTransfertService.class);

    private final FactureBTRepository factureBTRepository;

    private final BonTransfertAnnulationService bonTransfertAnnulationService;
    @Autowired
    CliniSysService cliniSysService;

    @Autowired
    MvtstoBTRepository mvtstoBTRepository;

    @Autowired
    private ParamAchatServiceClient paramAchatServiceClient;

    @Autowired
    BtfeFactory btfeFactory;

    @Autowired
    @Lazy
    FactureService factureService;

    @Autowired
    ParamServiceClient parametrageService;

    @Autowired
    DemandeServiceClient demandeServiceClient;

    @Autowired
    MvtstoBTFactory mvtstoBTFactory;

    @Autowired
    MessageSource messages;

    @Autowired
    @Lazy
    FactureBEService factureBEService;

    @Autowired
    BtbtRepository btbtRepository;

//    private static List<String> DefaultUsersToNotify;
    private String DefaultUserToNotify;

    @Value("${default-list-users-to-notify-for-validation-transfert}")
    public void setDefaultUserToNotify(String DefaultUserToNotify) {
        this.DefaultUserToNotify = DefaultUserToNotify;
    }

    public String getDefaultUserToNotify() {
        return DefaultUserToNotify;
    }

    private static String limitOfTransferts;

    @Value("${default-number-of-transferts}")
    public void setdefaultLimitOfPrelevements(String defaultLimitOfTransferts) {
        limitOfTransferts = defaultLimitOfTransferts;
    }
//    @Value("${default-list-users-to-notify-for-validation-transfert}.split(',')}")

//    @Value("#{'${default-list-users-to-notify-for-validation-transfert}'.split(',')}")
//    public static List<String> getDefaultUsersToNotify() {
//        return DefaultUsersToNotify;
//    }
//
//
//
//    public static void setDefaultUsersToNotify(List<String> DefaultUsersToNotify) {
//        BonTransfertService.DefaultUsersToNotify = DefaultUsersToNotify;
//    }
    public BonTransfertService(FactureBTRepository factureBTRepository, @Lazy BonTransfertAnnulationService bonTransfertAnnulationService, CliniSysService cliniSysService) {
        this.factureBTRepository = factureBTRepository;
        this.bonTransfertAnnulationService = bonTransfertAnnulationService;
        this.cliniSysService = cliniSysService;
    }

    @Transactional(readOnly = true)
    public List<FactureBTDTO> findAll(CategorieDepotEnum categ, LocalDateTime fromDate, LocalDateTime toDate, Boolean interdepot,
            Boolean avoirTransfert, Boolean deleted, Boolean validated, Boolean conforme, Integer depotDest, Integer depotSrc) {
        log.debug("Request to get All FactureBTs");
        QFactureBT _FactureBT = QFactureBT.factureBT;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_FactureBT.typbon.eq(TypeBonEnum.BT))
                .optionalAnd(categ, () -> _FactureBT.categDepot.eq(categ))
                .optionalAnd(fromDate, () -> _FactureBT.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _FactureBT.datbon.loe(toDate))
                .optionalAnd(interdepot, () -> _FactureBT.interdepot.eq(interdepot))
                .optionalAnd(avoirTransfert, () -> _FactureBT.avoirTransfert.eq(avoirTransfert))
                .optionalAnd(validated, () -> _FactureBT.valide.eq(validated))
                .optionalAnd(conforme, () -> _FactureBT.conforme.eq(conforme))
                .optionalAnd(depotDest, () -> _FactureBT.deptr.eq(depotDest))
                .optionalAnd(depotSrc, () -> _FactureBT.coddep.eq(depotSrc))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _FactureBT.datAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _FactureBT.datAnnul.isNull());
        Set<FactureBT> listFactureBTs = new HashSet((Collection) factureBTRepository.findAll(builder));

        Set<Integer> codeDepots = new HashSet();
        listFactureBTs.forEach(x -> {
            codeDepots.add(x.getCoddep());
            codeDepots.add(x.getDeptr());
        });
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots);

        List<FactureBTDTO> listFactureBTDTO = listFactureBTs.stream().map(transfert -> {
            FactureBTDTO factureBTDTO = new FactureBTDTO();
            FactureBTDTO dto = FactureBTFactory.factureBTToFactureBTDTO(transfert, factureBTDTO);
            listDepot.stream().filter(x -> x.getCode().equals(transfert.getCoddep()) || x.getCode().equals(transfert.getDeptr())).forEach(filtredItem -> {
                if (filtredItem.getCode().equals(transfert.getCoddep())) {
                    dto.setSourceDesignation(filtredItem.getDesignation());
                    dto.setSourceCodeSaisi(filtredItem.getCodeSaisi());
                } else {
                    dto.setDestinationDesignation(filtredItem.getDesignation());
                    dto.setDestinationCodeSaisi(filtredItem.getCodeSaisi());
                }
            });

            return dto;
        }
        ).collect(toList());
        return listFactureBTDTO;
    }

    @Transactional(readOnly = true)
    public List<MvtstoBTDTO> findDetails(String numBon) {
        List<MvtstoBTDTO> result = MvtstoBTFactory.mvtstoBTToMvtstoBTDTOs(mvtstoBTRepository.findByNumbon(numBon));
        List<Integer> codeUnites = new ArrayList<>();
        result.forEach(x -> {
            codeUnites.add(x.getUnityID());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        result.forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityID())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });
        return result;
    }

    @Transactional(readOnly = true)
    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddeps, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
        QMvtStoBT _mvtsto = QMvtStoBT.mvtStoBT;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.factureBT().categDepot.eq(categ))
                .and(_mvtsto.factureBT().codAnnul.isNull())
                .optionalAnd(codart, () -> _mvtsto.codart.eq(codart))
                .optionalAnd(coddeps, () -> _mvtsto.factureBT().deptr.eq(coddeps).or(_mvtsto.factureBT().coddep.eq(coddeps)))
                .optionalAnd(fromDate, () -> _mvtsto.factureBT().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.factureBT().datbon.loe(toDate));
        Set<MvtStoBT> list = new HashSet((List<MvtStoBT>) mvtstoBTRepository.findAll(builder));
        List<Integer> codeDepots = new ArrayList<>();
        List<Integer> codeUnites = new ArrayList<>();
        list.forEach(x -> {
            codeDepots.add(x.getFactureBT().getCoddep());
            codeDepots.add(x.getFactureBT().getDeptr());
            codeUnites.add(x.getUnite());
        });
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots);
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        List<Mouvement> mouvements = new ArrayList<>();
        list.forEach(y -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(y.getUnite())).findFirst().orElse(null);
            DepotDTO codedep = listDepot.stream().filter(x -> x.getCode().equals(y.getFactureBT().getCoddep())).findFirst().orElse(null);
            DepotDTO deptr = listDepot.stream().filter(x -> x.getCode().equals(y.getFactureBT().getDeptr())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            com.csys.util.Preconditions.checkBusinessLogique(codedep != null, "missing-depot");
            com.csys.util.Preconditions.checkBusinessLogique(deptr != null, "missing-depot");
            mouvements.addAll(mvtstoBTFactory.toMouvement(y, unite, codedep, deptr, coddeps, typeDate));
        });
        log.debug("mouvements BT {}", mouvements);
        return mouvements;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementSortie(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return mvtstoBTRepository.findTotalMouvementSortie(coddep, codart, toDate);
        } else {
            return mvtstoBTRepository.findTotalMouvementSortie(coddep, codart, fromDate, toDate);
        }
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementEntree(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return mvtstoBTRepository.findTotalMouvementEntree(coddep, codart, toDate);
        } else {
            return mvtstoBTRepository.findTotalMouvementEntree(coddep, codart, fromDate, toDate);
        }
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementEntree(List<Integer> codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        List<TotalMouvement> lists = new ArrayList<>();
        if (codart != null && codart.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codart.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {

                List<Integer> codesChunk = codart.subList(i * 2000, Math.min(i * 2000 + 2000, codart.size()));
                if (fromDate == null) {
                    completableFuture[i] = mvtstoBTRepository.findTotalMouvementEntree(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = mvtstoBTRepository.findTotalMouvementEntree(coddep, codesChunk, fromDate, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                }
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementSortie(List<Integer> codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        List<TotalMouvement> lists = new ArrayList<>();
        if (codart != null && codart.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codart.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {

                List<Integer> codesChunk = codart.subList(i * 2000, Math.min(i * 2000 + 2000, codart.size()));
                if (fromDate == null) {
                    completableFuture[i] = mvtstoBTRepository.findTotalMouvementSortie(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = mvtstoBTRepository.findTotalMouvementSortie(coddep, codesChunk, fromDate, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                }
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (coddep != null) {
            if (fromDate == null) {
                return mvtstoBTRepository.findTotalMouvementSortie(coddep, toDate);
            } else {
                return mvtstoBTRepository.findTotalMouvementSortie(coddep, fromDate, toDate);
            }

        } else {
            if (fromDate == null) {
                return mvtstoBTRepository.findTotalMouvementSortie(toDate);
            } else {
                return mvtstoBTRepository.findTotalMouvementSortie(fromDate, toDate);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (coddep != null) {
            if (fromDate == null) {
                return mvtstoBTRepository.findTotalMouvementEntree(coddep, toDate);
            } else {
                return mvtstoBTRepository.findTotalMouvementEntree(coddep, fromDate, toDate);
            }

        } else {
            if (fromDate == null) {
                return mvtstoBTRepository.findTotalMouvementEntree(toDate);
            } else {
                return mvtstoBTRepository.findTotalMouvementEntree(fromDate, toDate);
            }

        }
    }

    /**
     * Edition transfert by numbon.
     *
     * @param numBon the numBon of the entity
     * @param simpleHeader
     * @return
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Transactional(readOnly = true)
    public byte[] edition(String numBon, Boolean simpleHeader, Boolean detailed) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print facture BTtttt : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        FactureBT facture = factureBTRepository.findOne(numBon);
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();

        Preconditions.checkFound(facture, "transfert.NotFound");
        FactureBTEditionDTO factureBTEditionDTO = FactureBTFactory.factureBTToFactureBTEditionDTO(facture);

        if (Boolean.TRUE.equals(simpleHeader)) {
            if (Boolean.TRUE.equals(detailed)) {
                if (facture.getValide()) {
                    reportClientDoc.open("Reports/BonRecupSansListeBonDetailedValide" + local + ".rpt", 0);
                } else if (!facture.getValide()) {
                    reportClientDoc.open("Reports/BonRecupSansListeBonDetailed" + local + ".rpt", 0);
                }
            } else {

                if (facture.getValide()) {
                    reportClientDoc.open("Reports/BonRecupSansListeBonValide" + local + ".rpt", 0);
                } else if (!facture.getValide()) {
                    reportClientDoc.open("Reports/BonRecupSansListeBon" + local + ".rpt", 0);
                }
            }
        } else {
            if (Boolean.TRUE.equals(facture.getPerime())) {
                if (Boolean.TRUE.equals(detailed)) {
                    reportClientDoc.open("Reports/BonTransfertPerimeDetailed" + local + ".rpt", 0);
                } else {
                    reportClientDoc.open("Reports/BonTransfertPerime" + local + ".rpt", 0);
                }
            } else {
                if (Boolean.TRUE.equals(detailed)) {
                    if (facture.getValide()) {
                        reportClientDoc.open("Reports/BonTransfertDetailedValide" + local + ".rpt", 0);
                    } else if (!facture.getValide()) {
                        reportClientDoc.open("Reports/BonTransfertDetailed" + local + ".rpt", 0);
                    }
                } else {

                    if (facture.getValide()) {
                        reportClientDoc.open("Reports/BonTransfertValide" + local + ".rpt", 0);
                    } else if (!facture.getValide()) {
                        reportClientDoc.open("Reports/BonTransfert" + local + ".rpt", 0);
                    }

                }
            }
        }

        List<Integer> codeDepots = new ArrayList<>();
        codeDepots.add(factureBTEditionDTO.getDestinationID());
        codeDepots.add(factureBTEditionDTO.getSourceID());
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots);
        listDepot.stream().forEach(item -> {
            if (item.getCode().equals(factureBTEditionDTO.getSourceID())) {
                factureBTEditionDTO.setSourceDesignation(item.getDesignation());
            } else {
                factureBTEditionDTO.setDestinationDesignation(item.getDesignation());
            }
        });
        factureBTEditionDTO.setListMvtsto(MvtstoBTFactory.mvtstoBTToMvtstoBTEditionDTOs(facture.getDetailFactureBTCollection()));

        List<Integer> codeUnites = new ArrayList<>();
        facture.getDetailFactureBTCollection().forEach(x -> {
            codeUnites.add(x.getUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        factureBTEditionDTO.getListMvtsto().forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityID())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });

        if (facture.getInterdepot()) {
            if (facture.isAvoirTransfert()) {
                List<String> codeNumBTs = new ArrayList<>();
                facture.getNumBTReturn().forEach(x -> {
                    codeNumBTs.add(x.getNumBTReturn());
                });
                factureBTEditionDTO.setDetails(String.join(", ", codeNumBTs));
            } else {
                List<Integer> codeDemandeTrs = new ArrayList<>();
                facture.getDetailTransfertDTR().forEach(x -> {
                    codeDemandeTrs.add(x.getCodeDTR());
                });
                List<DemandeTrDTO> listDemandeTrs = demandeServiceClient.findListDemandeTr(codeDemandeTrs, language, false);
                List<String> numeroDemandeTrs = new ArrayList<>();
                listDemandeTrs.forEach(x -> {
                    numeroDemandeTrs.add(x.getNumeroDemande());
                });
                factureBTEditionDTO.setDetails(String.join(", ", numeroDemandeTrs));
            }
        } else {
            List<String> codeQuittances = new ArrayList<>();
            facture.getBTFE().forEach(x -> {
                codeQuittances.add(x.getNumFE());
            });
            factureBTEditionDTO.setDetails(String.join(", ", codeQuittances));
        }

        log.debug("factureBTEditionDTOoo : {}", factureBTEditionDTO.toString());
        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(factureBTEditionDTO), FactureBTEditionDTO.class,
                "Entete", "Entete");
        reportClientDoc.getDatabaseController().setDataSource(factureBTEditionDTO.getListMvtsto(), MvtstoBTEditionDTO.class,
                "Detaille", "Detaille");
        reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                "clinique", "clinique");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return Helper.read(byteArrayInputStream);
    }

    /**
     * Get all the FactureBTDTO by code in.
     *
     * @param codes
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<FactureBTDTO> findByNumbonIn(List<String> codes) {
        log.debug("Request to get All factureBTs by code in");
        List<FactureBT> listFactureBTs = (List<FactureBT>) factureBTRepository.findByNumbonIn(codes);
        List<Integer> codeDepots = new ArrayList<>();
        listFactureBTs.forEach(x -> {
            codeDepots.add(x.getCoddep());
            codeDepots.add(x.getDeptr());
        });
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        List<FactureBTDTO> listFactureBTDTO = listFactureBTs.stream().map(item -> {
            FactureBTDTO dto = BonTransfertRecupFactory.factureBTToFactureBTDTO(item);
            listDepot.stream().filter(x -> x.getCode().equals(item.getCoddep()) || x.getCode().equals(item.getDeptr())).forEach(filtredItem -> {
                if (filtredItem.getCode().equals(item.getCoddep())) {
                    dto.setSourceDesignation(filtredItem.getDesignation());
                    dto.setSourceCodeSaisi(filtredItem.getCodeSaisi());
                } else {
                    dto.setDestinationDesignation(filtredItem.getDesignation());
                    dto.setDestinationCodeSaisi(filtredItem.getCodeSaisi());
                }
            });

            return dto;
        }).collect(toList());
        return listFactureBTDTO;
    }
/////////////////////////////////////

    @Transactional(readOnly = true)
    public byte[] editionDetailsTransfert(CategorieDepotEnum categ, Integer depotSrcId,
            List<Integer> depotDestIds, LocalDateTime fromDate,
            LocalDateTime toDate, String type,
            Boolean groupByArticle, String filter, Boolean conforme, Boolean detailed) throws ReportSDKException, IOException, SQLException {

        log.debug("REST request to print details Transfert ");
        List<DetailsTransfertDTO> listeDetailsTransfertDTO = findDetailTransfert(categ, depotSrcId, depotDestIds, fromDate, toDate, groupByArticle, filter, conforme, detailed);
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        if (type.equalsIgnoreCase("P")) {

            if (filter.equalsIgnoreCase("all")) {
                reportClientDoc.open("Reports/ListeTransfertsTous" + local + ".rpt", 0);
            } else if (filter.equalsIgnoreCase("validated") && Boolean.TRUE.equals(conforme)) {
                reportClientDoc.open("Reports/ListeTransfertsConformes" + local + ".rpt", 0);
            } else if (filter.equalsIgnoreCase("validated") && Boolean.FALSE.equals(conforme)) {
                reportClientDoc.open("Reports/ListeTransfertsNonConformes" + local + ".rpt", 0);

            } else if (filter.equalsIgnoreCase("nonvalidated")) {
                if (!Boolean.TRUE.equals(groupByArticle) && Boolean.TRUE.equals(detailed)) {
                    reportClientDoc.open("Reports/DetailsTransfert_par_depot_detaille" + local + ".rpt", 0);
                } else if (!Boolean.TRUE.equals(groupByArticle) && !Boolean.TRUE.equals(detailed)) {
                    reportClientDoc.open("Reports/DetailsTransfert_par_depot_nonDetaille" + local + ".rpt", 0);
                } else if (Boolean.TRUE.equals(groupByArticle) && Boolean.TRUE.equals(detailed)) {
                    reportClientDoc.open("Reports/DetailsTransfert_par_article_detaille" + local + ".rpt", 0);
                } else if (Boolean.TRUE.equals(groupByArticle) && !Boolean.TRUE.equals(detailed)) {
                    reportClientDoc.open("Reports/DetailsTransfert_par_article_nonDetaille" + local + ".rpt", 0);
                }

            }
        } else { ///////////// else excel

            if (filter.equalsIgnoreCase("all")) {
                reportClientDoc.open("Reports/ListeTransfertsTous_excel" + local + ".rpt", 0);
            } else if (filter.equalsIgnoreCase("validated") && Boolean.TRUE.equals(conforme)) {
                reportClientDoc.open("Reports/ListeTransfertsConformes_excel" + local + ".rpt", 0);
            } else if (filter.equalsIgnoreCase("validated") && Boolean.FALSE.equals(conforme)) {
                reportClientDoc.open("Reports/ListeTransfertsNonConformes_excel" + local + ".rpt", 0);

            } else if (filter.equalsIgnoreCase("nonvalidated")) {

                if (!Boolean.TRUE.equals(groupByArticle) && Boolean.TRUE.equals(detailed)) {
                    reportClientDoc.open("Reports/DetailsTransfert_par_depot_detaille_excel" + local + ".rpt", 0);
                } else if (!Boolean.TRUE.equals(groupByArticle) && !Boolean.TRUE.equals(detailed)) {
                    reportClientDoc.open("Reports/DetailsTransfert_par_depot_nonDetaille_excel" + local + ".rpt", 0);
                } else if (Boolean.TRUE.equals(groupByArticle) && Boolean.TRUE.equals(detailed)) {
                    reportClientDoc.open("Reports/DetailsTransfert_par_article_detaille_excel" + local + ".rpt", 0);
                } else if (Boolean.TRUE.equals(groupByArticle) && !Boolean.TRUE.equals(detailed)) {
                    reportClientDoc.open("Reports/DetailsTransfert_par_article_nonDetaille_excel" + local + ".rpt", 0);
                }
            }
        }

        if (type.equalsIgnoreCase("P")) {

            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        reportClientDoc.getDatabaseController().setDataSource(listeDetailsTransfertDTO, DetailsTransfertDTO.class, "Detail", "Detail");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "debut", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "fin", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));

        if (!listeDetailsTransfertDTO.isEmpty()) {

            paramController.setCurrentValue("", "depotSource", listeDetailsTransfertDTO.get(0).getDesignationDepotSource());
        } else {
            DepotDTO depotDest = paramAchatServiceClient.findDepotByCode(depotSrcId);
            paramController.setCurrentValue("", "depotSource", depotDest.getDesignation());

        }

        if (type.equalsIgnoreCase("P")) {

            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 

    @Transactional(readOnly = true)
    public List<DetailsTransfertDTO> findDetailTransfert(CategorieDepotEnum categ, Integer depotSrcId,
            List<Integer> depotDestIds, LocalDateTime fromDate,
            LocalDateTime toDate, Boolean grouByArticle,
            String filter, Boolean conforme,
            Boolean detailed
    ) {
        QMvtStoBT _mvtsto = QMvtStoBT.mvtStoBT;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(categ, () -> _mvtsto.factureBT().categDepot.eq(categ))
                .optionalAnd(depotSrcId, () -> _mvtsto.factureBT().coddep.eq(depotSrcId))
                //                .optionalAnd(depotDestIds, () -> _mvtsto.factureBT().deptr.in(depotDestIds))
                .booleanAnd(depotDestIds != null && depotDestIds.size() > 0, () -> _mvtsto.factureBT().deptr.in(depotDestIds))
                .optionalAnd(fromDate, () -> _mvtsto.factureBT().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.factureBT().datbon.loe(toDate));
//                .booleanAnd(Objects.equals(conforme, Boolean.TRUE), () -> _FactureBT.datAnnul.isNotNull())
//                .booleanAnd(Objects.equals(conforme, Boolean.FALSE), () -> _FactureBT.datAnnul.isNull());;
        Set<MvtStoBT> listMvt = new HashSet((List<MvtStoBT>) mvtstoBTRepository.findAll(builder));
        Set<Integer> listDepotCodes = new HashSet();
        listDepotCodes.add(depotSrcId);
        if (depotDestIds != null && !depotDestIds.isEmpty()) {
            listDepotCodes.addAll(depotDestIds);
        } else {
            Set<Integer> listDepotCodeDestination = listMvt.stream().map(mvt -> mvt.getFactureBT().getDeptr()).distinct().collect(Collectors.toSet());
            listDepotCodes.addAll(listDepotCodeDestination);
        }

        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(listDepotCodes);
        log.debug("la liste des depots est {}:", listDepot);

        /////////recuperer liste des articles et la liste des codes unités
        Set<Integer> codesArticles = new HashSet();
        Set<Integer> codesUnites = new HashSet();
        Set<String> numbon = new HashSet();

        listMvt.forEach(mvt -> {
            codesArticles.add(mvt.getCodart());
            codesUnites.add(mvt.getUnite());
            numbon.add(mvt.getNumbon());
        });
        List<String> listenumbon = new ArrayList(numbon);
        List<ArticleDTO> listeArticles = paramAchatServiceClient.articleFindbyListCode(codesArticles);
        List<UniteDTO> listUnites = paramAchatServiceClient.findUnitsByCodes(codesUnites);

        List<Btbt> listBTBT = new ArrayList();
        if (filter.equalsIgnoreCase("validated") && Boolean.FALSE.equals(conforme)) {
            listBTBT = btbtRepository.findByNumBTReturnIn(listenumbon);
        }

//////////////////creation du dto/////////////////
        List<DetailsTransfertDTO> listeDetailsTransfertDTO = new ArrayList();

        for (MvtStoBT mvt : listMvt) {
            DetailsTransfertDTO detailTransfertDTO = MvtstoBTFactory.mvtstoBTToDetailsTransfertDTO(mvt);

            DepotDTO matchedDepotSource = listDepot.stream().filter(depot -> depot.getCode().equals(mvt.getFactureBT().getCoddep())).findFirst().orElse(null);
            DepotDTO matchedDepotDest = listDepot.stream().filter(depot -> depot.getCode().equals(mvt.getFactureBT().getDeptr())).findFirst().orElse(null);
            ArticleDTO matchedArticle = listeArticles.stream().filter(article -> article.getCode().equals(detailTransfertDTO.getCodeArticle())).findFirst().orElse(null);
            UniteDTO matchedUnite = listUnites.stream().filter(unite -> unite.getCode().equals(detailTransfertDTO.getCodeUnite())).findFirst().orElse(null);
            detailTransfertDTO.setDesignationUnite(matchedUnite.getDesignation());
            detailTransfertDTO.setSourceDesignation(matchedDepotSource.getDesignation());
            detailTransfertDTO.setDesignationDepotDestination(matchedDepotDest.getDesignation());
            detailTransfertDTO.setCodeCategorieArticle(matchedArticle.getCategorieArticle().getCode());
            detailTransfertDTO.setDesignationCategorieArticle(matchedArticle.getCategorieArticle().getDesignation());
            if (Boolean.TRUE.equals(mvt.getFactureBT().getValide())) {
                detailTransfertDTO.setDateValidate(java.util.Date.from(mvt.getFactureBT().getDateValidate().atZone(ZoneId.systemDefault()).toInstant()));
            }
            detailTransfertDTO.setValeur(detailTransfertDTO.getQuantite().multiply(detailTransfertDTO.getPrixUnitaire()));
            Locale loc = LocaleContextHolder.getLocale();
            if (detailTransfertDTO.isInterdepot() && !detailTransfertDTO.isAvoirTransfert()) {
                String translatedmessage = messages.getMessage("transfert", null, loc);
                detailTransfertDTO.setTypeMvt(translatedmessage);
            } else if (detailTransfertDTO.isAvoirTransfert()) {
                String translatedmessage = messages.getMessage("avoir_transfert", null, loc);
                detailTransfertDTO.setTypeMvt(translatedmessage);

            } else {
                String translatedmessage = messages.getMessage("transfert_indemnisation", null, loc);
                detailTransfertDTO.setTypeMvt(translatedmessage);
            }

            if (filter.equalsIgnoreCase("validated") && Boolean.FALSE.equals(conforme)) {
                Optional<Btbt> retour = listBTBT.stream().filter(item -> Boolean.TRUE.equals(item.getNumBT().getAvoirSuiteValidation()) && mvt.getNumbon().equals(item.getNumBTReturn())).findFirst();
                if (retour.isPresent()) {
                    detailTransfertDTO.setNumAfficheRetourTransfert(retour.get().getNumBT().getNumaffiche());
                }
            }

//            if (filter.equalsIgnoreCase("nonvalidated")&& detailTransfertDTO.getValide()) {
            List<DetailMvtStoBT> listeDetailsMvtstoBT = mvt.getDetailMvtStoBTList();
            //pour chaque detail//montantTTCTotal somme des mntTTc des details du mvtstoBT  
            BigDecimal montantTTCTotal = BigDecimal.ZERO;
            montantTTCTotal = listeDetailsMvtstoBT.stream()
                    .map(detailMvtstoBT -> {
                        BigDecimal montantTva = BigDecimal.ZERO;
                        montantTva = detailMvtstoBT.getPriuni().multiply(detailMvtstoBT.getTauxTva()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP);
                        BigDecimal MontantTTCDetail = BigDecimal.ZERO;
                        MontantTTCDetail = detailMvtstoBT.getPriuni().add(montantTva).multiply(detailMvtstoBT.getQuantitePrelevee());
                        return MontantTTCDetail;
                    })
                    .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

            if (montantTTCTotal.compareTo(detailTransfertDTO.getValeur()) < 0) {
                detailTransfertDTO.setMntTTC(detailTransfertDTO.getValeur());
            } else {
                detailTransfertDTO.setMntTTC(montantTTCTotal);
            }

//            }
            listeDetailsTransfertDTO.add(detailTransfertDTO);

        }
        checkBusinessLogique(listeDetailsTransfertDTO.size() > 0, "No-data-to-print");

/////////////////////////////////////////
        if (filter.equalsIgnoreCase("validated") && Boolean.TRUE.equals(conforme)) {
            listeDetailsTransfertDTO = listeDetailsTransfertDTO.stream().filter(x -> x.getValide() && x.getConforme()).collect(Collectors.toList());
            checkBusinessLogique(listeDetailsTransfertDTO.size() > 0, "No-data-to-print");
            return listeDetailsTransfertDTO;
        } else if (filter.equalsIgnoreCase("validated") && Boolean.FALSE.equals(conforme)) {
            listeDetailsTransfertDTO = listeDetailsTransfertDTO.stream().filter(x -> x.getValide() && Boolean.FALSE.equals(x.getConforme())).collect(Collectors.toList());
            checkBusinessLogique(listeDetailsTransfertDTO.size() > 0, "No-data-to-print");
            return listeDetailsTransfertDTO;
        } else if (filter.equalsIgnoreCase("nonvalidated") && Boolean.TRUE.equals(detailed)) {
            listeDetailsTransfertDTO = listeDetailsTransfertDTO.stream().filter(x -> !x.getValide()).collect(Collectors.toList());
            checkBusinessLogique(listeDetailsTransfertDTO.size() > 0, "No-data-to-print");
            //groupt par numbon ,codArt,codUnite
            List<DetailsTransfertDTO> listeResultat = groupDetailsByNumBonAndAritcleAndUnity(listeDetailsTransfertDTO);
            return listeResultat;
        } else if (filter.equalsIgnoreCase("nonvalidated") && !Boolean.TRUE.equals(detailed)) {
            listeDetailsTransfertDTO = listeDetailsTransfertDTO.stream().filter(x -> !x.getValide()).collect(Collectors.toList());
            checkBusinessLogique(listeDetailsTransfertDTO.size() > 0, "No-data-to-print");
//grpt par Coddep,codArt,codUnite
            List<DetailsTransfertDTO> listeResultat = groupDetailsByAritcleAndUnityAndCodedep(listeDetailsTransfertDTO);
            return listeResultat;
        } else {
            List<DetailsTransfertDTO> listeResultat = listeDetailsTransfertDTO;
            return listeResultat;
        }
//        return listeResultat;
    }

//////////////////////////////////////////////////////////////////////////
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private List<DetailsTransfertDTO> groupDetailsByNumBonAndAritcleAndUnity(List<DetailsTransfertDTO> listeDetailsTransfertDTO
    ) {
        DetailsTransfertDTO detailTransfert = new DetailsTransfertDTO();
        detailTransfert.setValeur(BigDecimal.ZERO);
        detailTransfert.setQuantite(BigDecimal.ZERO);
        detailTransfert.setMntTTC(BigDecimal.ZERO);

        List<DetailsTransfertDTO> simplifiedResult = new ArrayList();
        if (!listeDetailsTransfertDTO.isEmpty()) {
            listeDetailsTransfertDTO.stream()
                    .collect(groupingBy(DetailsTransfertDTO::getNumbon, (groupingBy(DetailsTransfertDTO::getCodeArticle,
                            groupingBy(DetailsTransfertDTO::getCodeUnite,
                                    Collectors.reducing(detailTransfert, (a, b) -> {
                                        b.setQuantite(b.getQuantite().add(a.getQuantite()));
//                                        b.setValeur(b.getQuantite().multiply(b.getPrixUnitaire()));
                                        b.setValeur(b.getValeur().add(a.getValeur()));
                                        b.setMntTTC(b.getMntTTC().add(a.getMntTTC()));
                                        b.setCodeTvaA(null);
                                        b.setTauxTvaA(null);

                                        return b;
                                    }))
                    ))))
                    .forEach((k, v) -> {
                        Collection<   Map<Integer, DetailsTransfertDTO>> map = v.values();
//                        Collection<  Map<Integer, DetailsTransfertDTO>> map = secondLevelMap.stream().flatMap(elt -> elt.values().stream()).collect(toList());
                        simplifiedResult.addAll(map.stream().flatMap(elt -> elt.values().stream()).collect(toList()));

                    });
        }
        return simplifiedResult;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private List<DetailsTransfertDTO> groupDetailsByAritcleAndUnityAndCodedep(List<DetailsTransfertDTO> listeDetailsTransfertDTO
    ) {
        List<DetailsTransfertDTO> simplifiedResult = new ArrayList();
        DetailsTransfertDTO detailTransfert = new DetailsTransfertDTO();
        detailTransfert.setValeur(java.math.BigDecimal.ZERO);
        detailTransfert.setQuantite(java.math.BigDecimal.ZERO);
        detailTransfert.setMntTTC(BigDecimal.ZERO);
        if (!listeDetailsTransfertDTO.isEmpty()) {
            listeDetailsTransfertDTO.stream()
                    .collect(groupingBy(DetailsTransfertDTO::getCodeArticle,
                            (groupingBy(DetailsTransfertDTO::getCodeUnite,
                                    groupingBy(DetailsTransfertDTO::getCodeDepotDest,
                                            Collectors.reducing(detailTransfert, (a, b) -> {
                                                b.setQuantite(b.getQuantite().add(a.getQuantite()));
//                                                b.setValeur(b.getQuantite().multiply(b.getPrixUnitaire()));
                                                b.setValeur(b.getValeur().add(a.getValeur()));
                                                b.setMntTTC(b.getMntTTC().add(a.getMntTTC()));
                                                b.setNumbon(null);
                                                b.setNumordre(null);
                                                b.setTauxTvaA(null);
                                                b.setCodeTvaA(null);
                                                b.setPrixUnitaire(null);

                                                return b;
                                            }))
                            ))))
                    .forEach((k, v) -> {
                        Collection<  Map<Integer, DetailsTransfertDTO>> map = v.values();
//                        Collection<  Map<Integer, DetailsTransfertDTO>> map = secondLevelMap.stream().flatMap(elt -> elt.values().stream()).collect(toList());
                        simplifiedResult.addAll(map.stream().flatMap(elt -> elt.values().stream()).collect(toList()));

                    });

        }
        return simplifiedResult;
    }

    @Transactional
    public Boolean checkExistanceNotValidatedTransferts(Integer codeDepot, CategorieDepotEnum categorieDepotEnum) {
        Boolean exist = factureBTRepository.existsByCoddepAndValideFalseAndCategDepotAndCodAnnulIsNull(codeDepot, categorieDepotEnum);
        Boolean exist2 = factureBTRepository.existsByDeptrAndValideFalseAndCategDepotAndCodAnnulIsNull(codeDepot, categorieDepotEnum);
        Boolean result = exist || exist2;
        log.debug("existance: {} existance2 :{},result: {}", exist, exist2, result);
        return result;

    }

    @Transactional
    public FactureBTDTO validateTransfer(FactureBTDTO factureBTDTO) throws ParseException {

        log.debug("Request to validate  Transfert: {}", factureBTDTO);

        FactureBT inBase = factureBTRepository.findOne(factureBTDTO.getCode());
        //verification existence de la facture a modifier et si elle est annulé 
        Preconditions.checkBusinessLogique(inBase != null, "transfert.NotFound");
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(inBase.getValide()), "transfert.valide");
        Preconditions.checkBusinessLogique(inBase.getCodAnnul() == null, "transfert.Annule");

        Boolean size = inBase.getDetailFactureBTCollection().size() < factureBTDTO.getDetails().size();
        log.debug(" ctrl size est {}", size);
        Preconditions.checkBusinessLogique(!(inBase.getDetailFactureBTCollection().size() < factureBTDTO.getDetails().size()), "transfert-errone");

        Boolean conforme = Boolean.TRUE;
        Boolean quantiteManquante = Boolean.FALSE;
        List<MvtstoBTDTO> listdto = new ArrayList();
        List<String> listeRelativesFactureBTs = new ArrayList();
        for (MvtStoBT mvtStoBT : inBase.getDetailFactureBTCollection()) {
            MvtstoBTDTO matchingMvstoBT = factureBTDTO.getDetails()
                    .stream()
                    .filter(item -> item.getCode().equals(mvtStoBT.getCode()))
                    .findFirst().orElse(null);

            if (matchingMvstoBT != null) {
                mvtStoBT.setQuantiteDefectueuse(matchingMvstoBT.getQuantiteDefectueuse());
                mvtStoBT.setQuantiteRecue(matchingMvstoBT.getQuantiteRecue());
                Preconditions.checkBusinessLogique(!(mvtStoBT.getQuantiteRecue().compareTo(mvtStoBT.getQuantite()) > 0), "transfert-errone");
                Preconditions.checkBusinessLogique(!(mvtStoBT.getQuantiteDefectueuse().compareTo(mvtStoBT.getQuantiteRecue()) > 0), "transfert-errone");
//                if (inBase.isInterdepot()) {
//                    mvtStoBT.setQteben(mvtStoBT.getQuantiteRecue());
//                }
            } else // le mvtstoBT est manquant cad la quantite recue est egale a 0 
            {
                mvtStoBT.setQuantiteRecue(BigDecimal.ZERO);
                mvtStoBT.setQuantiteDefectueuse(BigDecimal.ZERO);
//                if (inBase.isInterdepot()) {
//                    mvtStoBT.setQteben(BigDecimal.ZERO);
//                }
            }

            if (mvtStoBT.getQuantiteDefectueuse().compareTo(BigDecimal.ZERO) == 1) {
                conforme = Boolean.FALSE;
            }

            if (mvtStoBT.getQuantite().compareTo(mvtStoBT.getQuantiteRecue()) > 0) {
                conforme = Boolean.FALSE;
                quantiteManquante = Boolean.TRUE;
                BigDecimal qteToRedress = mvtStoBT.getQuantite().subtract(mvtStoBT.getQuantiteRecue());
//                MvtstoBTDTO mvtStoBTDTO = new MvtstoBTDTO(mvtStoBT.getCodart(), mvtStoBT.getLotinter(), mvtStoBT.getDatPer(), mvtStoBT.getUnite(), qteToRedress, qteToRedress, BigDecimal.ZERO);
                MvtstoBTDTO mvtStoBTDTO = new MvtstoBTDTO(mvtStoBT.getCodart(), mvtStoBT.getLotinter(), mvtStoBT.getDatPer(), mvtStoBT.getUnite(), mvtStoBT.getCategDepot(), qteToRedress, qteToRedress, BigDecimal.ZERO);

                mvtStoBTDTO.setUnityPrice(mvtStoBT.getPriuni());
                mvtStoBTDTO.setDesignation(mvtStoBT.getDesart());
                mvtStoBTDTO.setSecondDesignation(mvtStoBT.getDesArtSec());
                mvtStoBTDTO.setCodeSaisi(mvtStoBT.getCodeSaisi());
                log.debug("mvtStoBTDTO est {}", mvtStoBTDTO);
                mvtStoBT.setFactureBT(inBase);
                listdto.add(mvtStoBTDTO);

            }

        }

        //non conforme retourne vrai s'il ya une quantite defectueuse ou une quantite manquante
        inBase.setConforme(conforme);
        inBase.setValide(true);
        inBase.setDateValidate(LocalDateTime.now());
        inBase.setUserValidate(SecurityContextHolder.getContext().getAuthentication().getName());
        inBase.setMemoValidate(factureBTDTO.getMemoValidate());
        log.debug("Conforme {}:", conforme);
        if (quantiteManquante) {
            BonTransferAnnulationDTO bonTransferAnnulationDTO = new BonTransferAnnulationDTO(inBase.getDeptr(), inBase.getCoddep());
            bonTransferAnnulationDTO.setAvoirSuiteValidation(Boolean.TRUE);
            bonTransferAnnulationDTO.setUserValidate(inBase.getUserValidate());
            bonTransferAnnulationDTO.setDateValidate(LocalDateTime.now());
            bonTransferAnnulationDTO.setCategDepot(inBase.getCategDepot());
            bonTransferAnnulationDTO.setAvoirTransfert(true);
            bonTransferAnnulationDTO.setPerime(Boolean.FALSE);
            bonTransferAnnulationDTO.setDetails(listdto);
            bonTransferAnnulationDTO.setInterdpot(true);
            listeRelativesFactureBTs.add(inBase.getNumbon());
            bonTransferAnnulationDTO.setRelativesFactureBTs(listeRelativesFactureBTs);
            bonTransferAnnulationDTO.setMemop("إرجاع تحويل ناتج عن تأكيد تحويل رقم" + inBase.getNumaffiche());
            bonTransfertAnnulationService.addBonTransferAnnulation(bonTransferAnnulationDTO);
        }

        factureBTRepository.save(inBase);
        FactureBTDTO resultDTO = new FactureBTDTO();
        return FactureBTFactory.factureBTToFactureBTDTO(inBase, resultDTO);
    }

    public EmailDTO notify(String transfertCode) {

        FactureBT inBase = factureBTRepository.findOne(transfertCode);
        Preconditions.checkBusinessLogique(inBase != null, "transfert.NotFound");
        List<String> usersToNotify = new ArrayList();
//        usersToNotify.add(inBase.getCodvend());

        List<Integer> codesDepartements = new ArrayList();
        codesDepartements.add(inBase.getCoddep());
        codesDepartements.add(inBase.getDeptr());
        List<DepartementDTO> departementSrcEtDest = paramAchatServiceClient.findListDepartments(codesDepartements);
        List<String> departementAdministrateurs = new ArrayList();
        departementSrcEtDest.stream().forEach(depart
                -> {
            Collection<String> UsersAdmins = depart.getAdministrateurs();
            departementAdministrateurs.addAll(UsersAdmins);
        }
        );

        log.debug(" DefaultUserToNotify {}", DefaultUserToNotify);
        String[] listeUsers = DefaultUserToNotify.split(",");
        if (DefaultUserToNotify != null) {
            usersToNotify.addAll(Arrays.asList(listeUsers));
        }
        usersToNotify.addAll(departementAdministrateurs);
        log.debug("list of usres to Notify est {}", usersToNotify);

        Object[] obj = new Object[1];
        obj[0] = inBase.getNumaffiche();
        String subject = messages.getMessage("transfert-subjectEmailValidation", null, LocaleContextHolder.getLocale());
        String body = messages.getMessage("transfert-bodyEmailValidation", obj, LocaleContextHolder.getLocale());
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setSubject(subject);
        emailDTO.setBody(body);
        return cliniSysService.prepareEmail(usersToNotify, emailDTO);
    }

    @Transactional(readOnly = true)
    public List<FactureBTDTO> findNotValidatedTransfertsForInventory(Integer depot, CategorieDepotEnum categ) {
        log.debug("Request to findNotValidatedTransfertsForInventory");
        List<FactureBT> listFactureBTs;
        if (categ != null) {
            listFactureBTs = factureBTRepository.findByCoddepAndValideFalseAndCodAnnulIsNullAndCategDepot(depot, categ);
            listFactureBTs.addAll(factureBTRepository.findByDeptrAndValideFalseAndCodAnnulIsNullAndCategDepot(depot, categ));
        } else {
            listFactureBTs = factureBTRepository.findByCoddepAndValideFalseAndCodAnnulIsNull(depot);
            listFactureBTs.addAll(factureBTRepository.findByDeptrAndValideFalseAndCodAnnulIsNull(depot));
        }

        Set<Integer> codeDepots = new HashSet();
        listFactureBTs.forEach(x -> {
            codeDepots.add(x.getCoddep());
            codeDepots.add(x.getDeptr());
        });
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots);

        List<FactureBTDTO> listFactureBTDTO = listFactureBTs.stream().map(transfert -> {
            FactureBTDTO factureBTDTO = new FactureBTDTO();
            FactureBTDTO dto = FactureBTFactory.factureBTToFactureBTDTO(transfert, factureBTDTO);
            listDepot.stream().filter(x -> x.getCode().equals(transfert.getCoddep()) || x.getCode().equals(transfert.getDeptr())).forEach(filtredItem -> {
                if (filtredItem.getCode().equals(transfert.getCoddep())) {
                    dto.setSourceDesignation(filtredItem.getDesignation());
                    dto.setSourceCodeSaisi(filtredItem.getCodeSaisi());
                } else {
                    dto.setDestinationDesignation(filtredItem.getDesignation());
                    dto.setDestinationCodeSaisi(filtredItem.getCodeSaisi());
                }
            });

            return dto;
        }
        ).collect(toList());
        return listFactureBTDTO;
    }

    @Transactional(readOnly = true)
    public List<FactureBTDTO> findLastFactureBTByCodart(CategorieDepotEnum categ, Integer codeDepot, Integer codeArticle) {

        int limit = Integer.parseInt(limitOfTransferts);
//Page<MvtStoBT> page = mvtstoBTRepository.findAll(PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "coddep")));
//        List<MvtStoBT> results = mvtstoBTRepository.findTopMvtstoBTByCodart(codeArticle, categ, codeDepot, new PageRequest(0, limit, Sort.by(Sort.Direction.DESC "coddep")));
        List<MvtStoBT> results = mvtstoBTRepository.findTop50ByCodartAndCodeDepotAndCodAnnulIsNullOrderByDatBonDesc(codeArticle, categ.categ(), codeDepot);
//        log.debug("results.getContent() sont {}", results.getContent());
        log.debug("codeArticle est {} et categ est {}, et codeDepot est {}", codeArticle, categ, codeDepot);
//        List<MvtStoBT> results = mvtstoBTRepository.findTopNByCodartAndCodeDepotAndCodAnnulIsNullOrderByDatBonDesc(codeArticle, categ.categ(), codeDepot , Integer limit);
        log.debug("results sont {}", results);
        List<FactureBTDTO> listeFactureBT = results.stream()
                .map(elt -> {
                    FactureBTDTO factureBTDTO = new FactureBTDTO();
                    FactureBTDTO dto = FactureBTFactory.factureBTToFactureBTDTO(elt.getFactureBT(), factureBTDTO);
                    dto.setDetails(MvtstoBTFactory.mvtstoBTToMvtstoBTDTOs(elt.getFactureBT().getDetailFactureBTCollection()));
                    return dto;
                })
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
        return listeFactureBT;

    }
}
