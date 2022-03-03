/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import com.csys.pharmacie.achat.domain.FactureBonReception;
import com.csys.pharmacie.achat.domain.MvtstoAF;
import com.csys.pharmacie.achat.dto.AvoirFournisseurDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoAFDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.achat.factory.AvoirFournisseurFactory;
import com.csys.pharmacie.achat.factory.MvtstoAFFactory;
import com.csys.pharmacie.achat.repository.AvoirFournisseurRepository;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.service.StockService;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrateur
 */
@Service
@Transactional
public class AvoirFournisseurStandardService extends AvoirFournisseurService{
    private final Logger log = LoggerFactory.getLogger(AvoirFournisseurStandardService.class);
   private final StockService stockService;
    private final AjustementAvoirFournisseurService ajustementAvoirFournisseurService;

    public AvoirFournisseurStandardService(StockService stockService, AjustementAvoirFournisseurService ajustementAvoirFournisseurService, AvoirFournisseurRepository avoirfournisseurRepository, FactureBonReceptionService factureBonReceptionService, ParamService paramService, ParamAchatServiceClient paramAchatServiceClient, MvtStoBAService mvtStoBAService, FcptFrsPHService fcptFrsPHService, MvtstoAFService mvtstoAFService, ParamServiceClient parametrageService, MvtstoAFFactory mvtstoAFFactory, AvoirFournisseurFactory avoirFournisseurFactory) {
        super(avoirfournisseurRepository, factureBonReceptionService, paramService, paramAchatServiceClient, mvtStoBAService, fcptFrsPHService, mvtstoAFService, parametrageService, mvtstoAFFactory, avoirFournisseurFactory);
        this.stockService = stockService;
        this.ajustementAvoirFournisseurService = ajustementAvoirFournisseurService;
    }
 


   
     /**
     * Save a avoirfournisseurDTO.
     *
     * @param avoirfournisseurDTO
     * @return the persisted entity
     */
    @Override
    public AvoirFournisseurDTO save(AvoirFournisseurDTO avoirfournisseurDTO) {
        log.debug("Request to save AvoirFournisseur context standard: {}", avoirfournisseurDTO);
        FactureBonReception FacturebonReceptionCorrespondante = factureBonReceptionService.findFactureBonReception(avoirfournisseurDTO.getNumFactureBonRecep());
        checkBusinessLogique(FacturebonReceptionCorrespondante != null, "missing-factureBonReception", avoirfournisseurDTO.getNumFactureBonRecep());
        FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(avoirfournisseurDTO.getCodeFournisseur());
        checkBusinessLogique(fournisseur != null, "missing-supplier", avoirfournisseurDTO.getCodeFournisseur());
        String numBon = paramService.getcompteur(avoirfournisseurDTO.getCategDepot(), avoirfournisseurDTO.getTypbon());

        AvoirFournisseur avoirFournisseur = avoirFournisseurFactory.avoirFournisseurDTOToAvoirFournisseur(avoirfournisseurDTO);
        avoirFournisseur.setNumbon(numBon);

        List<MvtstoAF> listeMvtstoAF = new ArrayList();
        for (MvtstoAFDTO mvtstoAFDTO : avoirfournisseurDTO.getMvtstoAFList()) {
            MvtstoAF mvtstoAF = mvtstoAFFactory.mvtstoAFDTOToMvtstoAF(mvtstoAFDTO);
            mvtstoAF.setAvoirFournisseur(avoirFournisseur);
            listeMvtstoAF.add(mvtstoAF);
        }
        avoirFournisseur.setMvtstoAFList(listeMvtstoAF);
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        avoirFournisseur.calcul(listTvas);
        avoirFournisseur = avoirfournisseurRepository.save(avoirFournisseur);

        mvtStoBAService.updateQteComOnAvoirFournisseur(avoirFournisseur);
        fcptFrsPHService.addFcptFrsOnAvoirFournisseur(avoirFournisseur);
        stockService.processStockOnAvoirFournisseur(avoirFournisseur);
        //logique changé plus de calcul de pmp lors des avoir
//        pricingService.updatePricesAfterAvoirFournisseur(avoirFournisseur, avoirFournisseur.getCategDepot());
        ajustementAvoirFournisseurService.saveAjustementAvoirFournisseur(avoirFournisseur);
        paramService.updateCompteurPharmacie(avoirfournisseurDTO.getCategDepot(), avoirfournisseurDTO.getTypbon());
        AvoirFournisseurDTO resultDTO = avoirFournisseurFactory.avoirFournisseurToAvoirFournisseurDTOLazy(avoirFournisseur);
        return resultDTO;
    }

    /**
     * Update a avoirfournisseurDTO.
     *
     * @param avoirfournisseurDTO
     * @return the updated entity
     */
    @Override
    public AvoirFournisseurDTO update(AvoirFournisseurDTO avoirfournisseurDTO) {
        log.debug("Request to update AvoirFournisseur standard: {}", avoirfournisseurDTO);
        AvoirFournisseur inBase = avoirfournisseurRepository.findOne(avoirfournisseurDTO.getNumbon());
        checkBusinessLogique(inBase != null, "avoirfournisseur.NotFound");
        AvoirFournisseur avoirfournisseur = avoirFournisseurFactory.avoirFournisseurDTOToAvoirFournisseur(avoirfournisseurDTO);
        avoirfournisseur = avoirfournisseurRepository.save(avoirfournisseur);
        AvoirFournisseurDTO resultDTO = avoirFournisseurFactory.avoirFournisseurToAvoirFournisseurDTO(avoirfournisseur);
        return resultDTO;
    }

    /**
     * Get one avoirfournisseurDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
//    @Transactional(readOnly = true)
//    public AvoirFournisseurDTO findOne(String id) {
//        log.debug("Request to get AvoirFournisseur: {}", id);
//        AvoirFournisseur avoirfournisseur = avoirfournisseurRepository.findOne(id);
//        AvoirFournisseurDTO dto = avoirFournisseurFactory.avoirFournisseurToAvoirFournisseurDTO(avoirfournisseur);
//        FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(avoirfournisseur.getCodeFournisseur());
//        checkBusinessLogique(fournisseur != null, "missing-supplier", avoirfournisseur.getCodeFournisseur());
//        dto.setFournisseur(fournisseur);
//        DepotDTO depot = paramAchatServiceClient.findDepotByCode(avoirfournisseur.getCoddep());
//        dto.setDesignationDepot(depot.getDesignation());
//        FactureBonReceptionDTO facture = factureBonReceptionService.findOne(avoirfournisseur.getNumFactureBonRecep());
//        dto.setMntFactureBonRecep(facture.getMontant());
//        dto.setDateFactureBonRecep(facture.getDatBon());
//        dto.setNumFactureBonRecep(facture.getNumbon());
//
//        return dto;
//    }
//
//    /**
//     * Get one avoirfournisseur by id.
//     *
//     * @param id the id of the entity
//     * @return the entity
//     */
//    @Transactional(readOnly = true)
//    public AvoirFournisseur findAvoirFournisseur(String id) {
//        log.debug("Request to get AvoirFournisseur: {}", id);
//        AvoirFournisseur avoirfournisseur = avoirfournisseurRepository.findOne(id);
//        return avoirfournisseur;
//    }
//
//    /**
//     * Get all the avoirfournisseurs.
//     *
//     * @return the the list of entities
//     */
//    @Transactional(
//            readOnly = true
//    )
//    public List<AvoirFournisseurDTO> findAll(LocalDateTime fromDate, LocalDateTime toDate, Boolean deleted, CategorieDepotEnum categDepot) {
//        log.debug("Request to get All AvoirFournisseurs");
//
//        QAvoirFournisseur _AvoirFournisseur = QAvoirFournisseur.avoirFournisseur;
//        WhereClauseBuilder builder = new WhereClauseBuilder()
//                .optionalAnd(fromDate, () -> _AvoirFournisseur.datbon.goe(fromDate))
//                .optionalAnd(toDate, () -> _AvoirFournisseur.datbon.loe(toDate))
//                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _AvoirFournisseur.userAnnule.isNotNull())
//                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _AvoirFournisseur.userAnnule.isNull())
//                .optionalAnd(categDepot, () -> _AvoirFournisseur.categDepot.eq(categDepot));
//
//        List<AvoirFournisseur> listeAvoirs = (List<AvoirFournisseur>) avoirfournisseurRepository.findAll(builder);
//
//        Set<String> fournisseursID = listeAvoirs.stream().map(AvoirFournisseur::getCodeFournisseur).collect(Collectors.toSet());
//        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(new ArrayList(fournisseursID));
//
//        List<AvoirFournisseurDTO> results = listeAvoirs.stream().map(elt -> {
//            AvoirFournisseurDTO avoirFournisseurDTO = avoirFournisseurFactory.avoirFournisseurToAvoirFournisseurDTOLazy(elt);
//            FournisseurDTO matchedFournisseur = fournisseurs.stream()
//                    .filter(fournisseur -> fournisseur.getCode().equals(elt.getCodeFournisseur()))
//                    .findFirst()
//                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-supplier", new Throwable(elt.getCodeFournisseur())));
//
//            avoirFournisseurDTO.setDesignationFournisseur(matchedFournisseur.getDesignation());
//
//            return avoirFournisseurDTO;
//        }).collect(toList());
//        return results;
//    }
//
//    /**
//     * Edition BonEditionDTO by .
//     *
//     * @param codfrs
//     * @param fromDate
//     * @param toDate
//     * @param categ
//     * @return
//     * @throws java.net.URISyntaxException
//     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
//     * @throws java.io.IOException
//     * @throws java.sql.SQLException
//     */
//    @Transactional(
//            readOnly = true
//    )
//    public List<BonEditionDTO> editionEtatAchat(String codfrs, LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categ, List<Integer> codarts) throws URISyntaxException, ReportSDKException, IOException, SQLException {
//
//        QAvoirFournisseur _AvoirFournisseur = QAvoirFournisseur.avoirFournisseur;
//        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(codfrs, () -> _AvoirFournisseur.codeFournisseur.eq(codfrs))
//                .optionalAnd(fromDate, () -> _AvoirFournisseur.datbon.goe(fromDate))
//                .optionalAnd(toDate, () -> _AvoirFournisseur.datbon.loe(toDate))
//                .optionalAnd(categ, () -> _AvoirFournisseur.categDepot.eq(categ));
//        List<AvoirFournisseur> result = (List<AvoirFournisseur>) avoirfournisseurRepository.findAll(builder);
//
//        List<BonEditionDTO> bonEditionDTOs = new ArrayList();
//        if (!result.isEmpty()) {
//            List<String> codesFrs = result.stream().map(item -> item.getCodeFournisseur()).distinct().collect(Collectors.toList());
//            List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
//            List<DepotDTO> depots = paramAchatServiceClient.findDepotsByCodes(result.stream().map(item -> item.getCoddep()).distinct().collect(Collectors.toList()));
//            List<Integer> codeUnites = new ArrayList<>();
//            result.forEach(y -> {
//                y.getMvtstoAFList().forEach(mvt -> {
//                    codeUnites.add(mvt.getUnite());
//                });
//            });
//            List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
//            result.forEach((avoirFournisseur) -> {
//                BonEditionDTO bonEditionDTO = avoirFournisseurFactory.lazyAvoirfournisseurToBonEditionDTO(avoirFournisseur);
//                Optional<FournisseurDTO> optFrs = fournisseurs.stream().filter(item -> item.getCode().equals(avoirFournisseur.getCodeFournisseur())).findFirst();
//                checkBusinessLogique(optFrs.isPresent(), "returns.get-all.missing-frs", avoirFournisseur.getCodeFournisseur());
//                bonEditionDTO.setFournisseur(optFrs.get());
//                Optional<DepotDTO> depot = depots.stream().filter(item -> item.getCode().equals(avoirFournisseur.getCoddep())).findFirst();
//                checkBusinessLogique(depot.isPresent(), "returns.get-all.missing-depot", Integer.toString(avoirFournisseur.getCoddep()));
//                bonEditionDTO.setDesignationDepot(depot.get().getDesignation());
//                bonEditionDTO.getDetails().forEach(x -> {
//                    UniteDTO unite = listUnite.stream().filter(y -> y.getCode().equals(x.getCodeUnite())).findFirst().orElse(null);
//                    checkBusinessLogique(unite != null, "missing-unity");
//                    x.setDesignationunite(unite.getDesignation());
//                });
//                bonEditionDTOs.add(bonEditionDTO);
//            });
//        }
//        return bonEditionDTOs;
//    }
//
//    /**
//     * Delete avoirfournisseur by id.
//     *
//     * @param id the id of the entity
//     */
//    public void delete(String id) {
//        log.debug("Request to delete AvoirFournisseur: {}", id);
//        avoirfournisseurRepository.delete(id);
//    }
//
//    public List<MvtstoAFDTO> findDetailsAvoirById(String numBon) {
//        log.debug("Request to get details by numBon :{},id");
//
//        List<MvtstoAF> mvtstos = mvtstoAFService.findByNumbon(numBon);
//
//        List<MvtstoAFDTO> mvtstoAFDTOs = mvtstoAFFactory.mvtstoAFToMvtstoAFDTOs(mvtstos);
//
//        Set<Integer> listCodeUnites = mvtstos.stream().map(MvtstoAF::getUnite).collect(Collectors.toSet());
//
//        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(listCodeUnites);
//
//        mvtstoAFDTOs.forEach(item -> {
//            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getUnite())).findFirst().orElse(null);
//            checkBusinessLogique(unite != null, "missing-unity", item.getUnite().toString());
//
//            item.setDesignationUnite(unite.getDesignation());
//        });
//        return mvtstoAFDTOs;
//
//    }
//
//    @Transactional(readOnly = true)
//    public byte[] editionAvoirFournisseur(String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
//        log.debug("REST request to print Avoir Fournisseur : {}", id);
//        String language = LocaleContextHolder.getLocale().getLanguage();
//        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
//        cliniqueDto.get(0).setLogoClinique();
//        ReportClientDocument reportClientDoc = new ReportClientDocument();
//        Locale loc = LocaleContextHolder.getLocale();
//        String local = "_" + loc.getLanguage();
//        reportClientDoc.open("Reports/Avoir_Fournisseur" + local + ".rpt", 0);
//        AvoirFournisseur result = avoirfournisseurRepository.findOne(id);
//        AvoirFournisseurDTO avoirFournisseurDTO = avoirFournisseurFactory.avoirFournisseurToAvoirFournisseurDTO(result);
//
//        FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(result.getCodeFournisseur());
//        avoirFournisseurDTO.setDesignationFournisseur(fournisseur.getDesignation());
//        log.debug("fournisseur est {}", avoirFournisseurDTO.getDesignationFournisseur());
//        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(avoirFournisseurDTO), AvoirFournisseurDTO.class, "Entete", "Entete");
//        reportClientDoc.getDatabaseController().setDataSource(avoirFournisseurDTO.getMvtstoAFList(), MvtstoAFDTO.class, "Detaille", "Detaille");
//        reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class, "clinique", "clinique");
//        reportClientDoc.getSubreportController().getSubreport("BaseTVA").getDatabaseController().setDataSource(avoirFournisseurDTO.getBaseTvaAvoirFournisseurList(), BaseTvaAvoirFournisseurDTO.class, "Commande", "Commande");
//        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
//        BigDecimal baseTVA = BigDecimal.ZERO;
//        BigDecimal mntTVASansGratuite = BigDecimal.ZERO;
//        BigDecimal mntTVAGratuite = BigDecimal.ZERO;
//
//        for (BaseTvaAvoirFournisseurDTO x : avoirFournisseurDTO.getBaseTvaAvoirFournisseurList()) {
//            baseTVA = baseTVA.add(x.getBaseTva());
//            mntTVASansGratuite = mntTVASansGratuite.add(x.getMontantTva());
//            mntTVAGratuite = mntTVAGratuite.add(x.getMntTvaGrtauite());
//        }
//        BigDecimal mntTVA = mntTVAGratuite.add(mntTVASansGratuite);
//
//        String user = SecurityContextHolder.getContext().getAuthentication().getName();
//        paramController.setCurrentValue("", "user", user);
//        paramController.setCurrentValue("", "baseTVA", baseTVA);
//        paramController.setCurrentValue("", "mntTVA", mntTVA);
//
//        if ("ar".equals(loc.getLanguage())) {
//            paramController.setCurrentValue("", "montantLettre", Convert.AR(avoirFournisseurDTO.getMontantTTC().toString(), "جنيه", "قرش"));
//        } else if ("fr".equals(loc.getLanguage())) {
//            paramController.setCurrentValue("", "montantLettre", Convert.FR(avoirFournisseurDTO.getMontantTTC().toString(), "livres", "pence"));
//        } else {
//            paramController.setCurrentValue("", "montantLettre", Convert.EN(avoirFournisseurDTO.getMontantTTC().toString(), "pounds", "penny"));
//        }
//        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
//        reportClientDoc.close();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        return Helper.read(byteArrayInputStream);
//    }
//}
   
}
