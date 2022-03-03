package com.csys.pharmacie.vente.avoir.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.vente.quittance.dto.AdmissionDemandePECDTO;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.NatureDepotDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.dto.AdmissionFacturationDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.BaseAvoirQuittance;
import com.csys.pharmacie.vente.quittance.service.ReceptionServiceClient;
import com.csys.pharmacie.helper.BaseTVADTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.EnumEtatPatient;
import com.csys.pharmacie.helper.EnumNatureAdmission;
import com.csys.pharmacie.helper.Helper;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.vente.avoir.domain.FactureAV;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.MouvementDuJour;
import com.csys.pharmacie.helper.QtePrixMouvement;
import com.csys.pharmacie.helper.ReceptionConstants;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.parametrage.entity.Paramph;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.vente.quittance.domain.DetailMvtsto;
import com.csys.pharmacie.vente.avoir.domain.DetailMvtstoAV;
import com.csys.pharmacie.vente.avoir.domain.MvtStoAV;
import com.csys.pharmacie.vente.avoir.domain.MvtStoAVPK;
import com.csys.pharmacie.vente.avoir.domain.MvtstoMvtstoAV;
import com.csys.pharmacie.vente.avoir.domain.QFactureAV;
import com.csys.pharmacie.vente.avoir.domain.QMvtStoAV;
import com.csys.pharmacie.vente.avoir.dto.Avoir;
import com.csys.pharmacie.vente.avoir.dto.AvoirWithReglement;
import com.csys.pharmacie.vente.avoir.dto.FactureAVDTO;
import com.csys.pharmacie.vente.avoir.dto.FactureAVEditionDTO;
import com.csys.pharmacie.vente.avoir.dto.FactureFactureAV;
import com.csys.pharmacie.vente.avoir.dto.MvtAvoir;
import com.csys.pharmacie.vente.avoir.dto.MvtStoAVDTO;
import com.csys.pharmacie.vente.avoir.dto.MvtstomvtstoAVDTO;
import com.csys.pharmacie.vente.avoir.factory.AvoirFactory;
import com.csys.pharmacie.vente.avoir.factory.FactureAVFactory;
import com.csys.pharmacie.vente.avoir.factory.MvtStoAVFactory;
import com.csys.pharmacie.vente.avoir.factory.MvtstomvtstomvtstoAVFactory;
import com.csys.pharmacie.vente.avoir.repository.FactureAVRepository;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import com.csys.pharmacie.vente.quittance.repository.MvtstoRepository;
import com.csys.pharmacie.vente.quittance.service.FactureService;
import java.util.Collections;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import com.csys.pharmacie.vente.avoir.repository.MvtstoAVRepository;
import com.csys.pharmacie.vente.avoir.repository.MvtstomvtstoAVRepository;
import com.csys.pharmacie.vente.quittance.dto.MvtstoDTO;
import com.csys.pharmacie.vente.quittance.dto.QuittanceDTO;
import com.csys.pharmacie.vente.quittance.dto.RemiseConventionnelleDTO;
import com.csys.pharmacie.vente.quittance.dto.SocieteDTO;
import com.csys.pharmacie.vente.quittance.repository.FactureRepository;
import com.csys.util.Preconditions;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import com.csys.pharmacie.vente.quittance.repository.DetailMvtstoRepository;
import com.csys.pharmacie.vente.avoir.repository.DetailMvtstoAVRepository;
import com.csys.pharmacie.vente.quittance.dto.FactureDTO;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;

@Service
@Transactional
public class AvoirService {
    
    private final Logger log = LoggerFactory.getLogger(AvoirService.class);
    static String LANGUAGE_SEC;
    static long BLOCKING_DELAY_BETWEEN_QUITTANCES;
    static long MAX_DELAY_RETURN_ITEM_THERMOSENSITIVE;
    
    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }
    
    @Value("${blocking-delay-between-quittances}")
    public void setBlockingDelayBetweenQuittances(long db) {
        BLOCKING_DELAY_BETWEEN_QUITTANCES = db;
    }
    
    @Value("${max-delay-return-item-thermosensitive}")
    public void setMaxDelayReturnItemThermosensitive(long db) {
        MAX_DELAY_RETURN_ITEM_THERMOSENSITIVE = db;
    }
    
    private final ParamService paramService;
    private final FactureAVRepository factureavRepository;
    private final FactureRepository factureRepository;
    private final ReceptionServiceClient receptionServiceClient;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final AvoirFactory avoirFactory;
    private final DetailMvtstoAVRepository detailMvtstoAVRepository;
    private final MvtstomvtstoAVRepository mvtstomvtstoAVRepository;
    private final DetailMvtstoRepository detailMvtstoRepository;
    private final MvtstoRepository mvtstoRepository;
    private final MvtstoAVRepository mvtstoAVRepository;
    private final FactureAVFactory factureAVFactory;
    private final ParamServiceClient parametrageService;
    private final InventaireService inventaireService;
    private final MvtStoAVFactory mvtStoAVFactory;
    @Autowired
    private FactureService factureService;
    
    public AvoirService(ParamService paramService, FactureAVRepository factureavRepository, FactureRepository factureRepository, ReceptionServiceClient receptionServiceClient, ParamAchatServiceClient paramAchatServiceClient, AvoirFactory avoirFactory, DetailMvtstoAVRepository detailMvtstoAVRepository, MvtstomvtstoAVRepository mvtstomvtstoAVRepository, DetailMvtstoRepository detailMvtstoRepository, MvtstoRepository mvtstoRepository, MvtstoAVRepository mvtstoAVRepository, FactureAVFactory factureAVFactory, ParamServiceClient parametrageService, InventaireService inventaireService, MvtStoAVFactory mvtStoAVFactory) {
        this.paramService = paramService;
        this.factureavRepository = factureavRepository;
        this.factureRepository = factureRepository;
        this.receptionServiceClient = receptionServiceClient;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.avoirFactory = avoirFactory;
        this.detailMvtstoAVRepository = detailMvtstoAVRepository;
        this.mvtstomvtstoAVRepository = mvtstomvtstoAVRepository;
        this.detailMvtstoRepository = detailMvtstoRepository;
        this.mvtstoRepository = mvtstoRepository;
        this.mvtstoAVRepository = mvtstoAVRepository;
        this.factureAVFactory = factureAVFactory;
        this.parametrageService = parametrageService;
        this.inventaireService = inventaireService;
        this.mvtStoAVFactory = mvtStoAVFactory;
    }

    /**
     * Save a avoir.
     *
     * @param avoir
     * @param listMvtstoToUpdate
     * @param listFactureToUpdate
     * @param listDetailMvtstoToUpdate
     * @param listMvtstomvtstoAV
     * @param listDetailMvtstoAV
     * @param pharmacieExterne
     * @return the persisted entity
     * @throws java.security.NoSuchAlgorithmException
     */
    public FactureAV saveAvoir(Avoir avoir, List<Mvtsto> listMvtstoToUpdate, List<Facture> listFactureToUpdate, List<DetailMvtsto> listDetailMvtstoToUpdate, List<MvtstoMvtstoAV> listMvtstomvtstoAV, List<DetailMvtstoAV> listDetailMvtstoAV, Boolean pharmacieExterne) throws NoSuchAlgorithmException {
        DepotDTO depotDTO = paramAchatServiceClient.findDepotByCode(avoir.getCoddep());
        Preconditions.checkBusinessLogique(!depotDTO.getDesignation().equals("depot.deleted"), "Depot [" + depotDTO.getDesignation() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotDTO.getDepotFrs(), "Dépot [" + depotDTO.getDesignation() + "] est un dépot fournisseur");
        String numBon = paramService.getcompteur(avoir.getCategDepot(), TypeBonEnum.AV);
        AdmissionFacturationDTO admissionFacturationDTO = receptionServiceClient.findAdmission(avoir.getNumdoss());
        Preconditions.checkBusinessLogique(admissionFacturationDTO != null , "error.facturation.fallback");
        
        Boolean isNotInpatientOrInpatientAndNotCloture = !EnumNatureAdmission.CODE_NATURE_ADMISSION_INPATIENT.intValue().equals(admissionFacturationDTO.getNatureAdmission().getCode())
              || (EnumNatureAdmission.CODE_NATURE_ADMISSION_INPATIENT.intValue().equals(admissionFacturationDTO.getNatureAdmission().getCode())
                && EnumEtatPatient.CODE_ETAT_PATIENT_RESIDENT.intValue().equals(admissionFacturationDTO.getEtatPatient().getCode()));
        Preconditions.checkBusinessLogique(isNotInpatientOrInpatientAndNotCloture  ,"dossier-inpatient-and-not-resident", avoir.getNumdoss());
                 
        List<Facture> listFacture = factureService.findByNumdossAndCoddepAndCategDepotAndNotDeleted(avoir.getCategDepot(), avoir.getNumdoss(), avoir.getCoddep(), avoir.getNumFacture());
        List<Mvtsto> listeMvtsto = new ArrayList<>();
        listFacture.forEach(x -> {
            listeMvtsto.addAll(x.getMvtstoCollection());
        });
        List<Integer> codarts = avoir.getMvtAvoir().stream().map(x -> x.getCodart()).collect(Collectors.toList());
        List<ArticleDTO> listArticleDTOs = paramAchatServiceClient.articleFindbyListCode(codarts);

        // check for inventory 
        List<Integer> categArticleIDs = listArticleDTOs.stream().map(item -> item.getCategorieArticle().getCode()).collect(toList());
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(categArticleIDs, avoir.getCoddep());
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = listArticleDTOs.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }
        List<MvtAvoir> mvtAvoirs = new ArrayList<>();
        avoir.getMvtAvoir().forEach(x -> {
            MvtAvoir mvtAvoir = x;
            List<ArticleDTO> articleDTOs = listArticleDTOs.stream().filter(y -> y.getCode().equals(x.getCodart())).collect(Collectors.toList());
            checkBusinessLogique(!articleDTOs.isEmpty(), "return.add.article-inexistant", mvtAvoir.getCodart().toString());
            mvtAvoir.setArticle(articleDTOs.get(0));
            mvtAvoirs.add(mvtAvoir);
        });
        FactureAV factureAV = avoirFactory.avoirToFactureAV(avoir);
        factureAV.setNumbon(numBon);
        List<MvtStoAV> details = new ArrayList<>();
        String numordre = "0001";
        for (MvtAvoir detailAvoir : mvtAvoirs) {
            BigDecimal qte = detailAvoir.getQuantite();
            List<Mvtsto> listeMvtstoByCodArt = listeMvtsto.stream()
                    .filter(item -> {
                        Boolean VerifCodart = item.getMvtstoPK().getCodart().equals(detailAvoir.getCodart());
                        Boolean VerifUnite = item.getUnite().equals(detailAvoir.getUnite());
                        Boolean VerifQte = item.getQteben().compareTo(BigDecimal.ZERO) > 0;
                        Boolean VerifLotInte = detailAvoir.getLot() != null && item.getLotInter() != null ? item.getLotInter().equals(detailAvoir.getLot()) : Boolean.TRUE;
                        Boolean VerifDatPer = detailAvoir.getDatPer() != null && item.getDatPer() != null ? item.getDatPer().equals(detailAvoir.getDatPer()) : Boolean.TRUE;
                        Boolean VerifCodTva = detailAvoir.getCodeTva() != null ? item.getCodtva().equals(detailAvoir.getCodeTva()) : Boolean.TRUE;
                        Boolean VerifTauTva = detailAvoir.getTauxTva() != null ? item.getTautva().equals(detailAvoir.getTauxTva()) : Boolean.TRUE;
                        return VerifCodart && VerifUnite && VerifQte && VerifCodTva && VerifTauTva && VerifLotInte && VerifDatPer;
                    }).collect(Collectors.toList());
            
            Integer availableQteMvtSto = listeMvtstoByCodArt.stream()
                    .map(filtredItem -> filtredItem.getQteben().intValue())
                    .collect(Collectors.summingInt(Integer::new));
            //filter depsto by codart and unite  qte available < qte demande
            if (availableQteMvtSto < qte.intValue()) {
                List<Mvtsto> listeMvtstoByCodArtWithoutDatperLot = listeMvtsto.stream()
                        .filter(item -> {
                            Boolean VerifCodart = item.getMvtstoPK().getCodart().equals(detailAvoir.getCodart());
                            Boolean VerifUnite = item.getUnite().equals(detailAvoir.getUnite());
                            Boolean VerifQte = item.getQteben().compareTo(BigDecimal.ZERO) > 0;
                            return VerifCodart && VerifUnite && VerifQte;
                        }).filter(x -> !listeMvtstoByCodArt.contains(x)).collect(Collectors.toList());
                //sort depstoByArticle by methode traitement 
                listeMvtstoByCodArt.addAll(listeMvtstoByCodArtWithoutDatperLot);
            }
            
            availableQteMvtSto = listeMvtstoByCodArt.stream()
                    .map(filtredItem -> filtredItem.getQteben().intValue())
                    .collect(Collectors.summingInt(Integer::new));
            checkBusinessLogique(availableQteMvtSto >= qte.intValue(), "return.add.insuffisant-qte-avoir", detailAvoir.getCodart().toString());
            Collections.sort(listeMvtstoByCodArt, (p1, p2) -> p1.getPriuni().compareTo(p2.getPriuni()));
            MvtStoAV mvtstoAV = new MvtStoAV();
            String ordre = TypeBonEnum.AV.name() + numordre;
            MvtStoAVPK pk = new MvtStoAVPK();
            pk.setCodart(detailAvoir.getCodart());
            pk.setNumbon(numBon);
            pk.setNumordre(ordre);
            mvtstoAV.setMvtStoAVPK(pk);
            mvtstoAV.setUnite(detailAvoir.getUnite());
            mvtstoAV.setTypbon(TypeBonEnum.AV);
            mvtstoAV.setCodtva(listeMvtstoByCodArt.get(0).getCodtva());
            mvtstoAV.setMemoart(detailAvoir.getMemoart());
            mvtstoAV.setTautva(listeMvtstoByCodArt.get(0).getTautva());
            mvtstoAV.setCategDepot(avoir.getCategDepot());
            if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
                mvtstoAV.setDesArtSec(detailAvoir.getArticle().getDesignation());
                mvtstoAV.setDesart(detailAvoir.getArticle().getDesignationSec());
            } else {
                mvtstoAV.setDesart(detailAvoir.getArticle().getDesignation());
                mvtstoAV.setDesArtSec(detailAvoir.getArticle().getDesignationSec());
            }
            mvtstoAV.setLotInter(detailAvoir.getLot());
            mvtstoAV.setDatPer(detailAvoir.getDatPer());
            mvtstoAV.setCodeSaisi(detailAvoir.getArticle().getCodeSaisi());
            mvtstoAV.setQuantite(detailAvoir.getQuantite());
            numordre = Helper.IncrementString(numordre, 4);
            BigDecimal priuni = BigDecimal.ZERO;
            BigDecimal prixBrute = BigDecimal.ZERO;
            BigDecimal priAch = BigDecimal.ZERO;
            BigDecimal remise = BigDecimal.ZERO;
            BigDecimal tauxCouverture = BigDecimal.ZERO;
            BigDecimal majoration = BigDecimal.ZERO;
            BigDecimal ajustement = BigDecimal.ZERO;
            for (Mvtsto mvtsto : listeMvtstoByCodArt) {
                if (pharmacieExterne) {
                    checkBusinessLogique((Boolean.TRUE.equals(detailAvoir.getArticle().getThermosensitive()) && mvtsto.getFacture().getDatbon().plusMinutes(MAX_DELAY_RETURN_ITEM_THERMOSENSITIVE).compareTo(LocalDateTime.now()) > 0)
                            || (!Boolean.TRUE.equals(detailAvoir.getArticle().getThermosensitive())),
                            "return.add.item.thermosensitive", detailAvoir.getArticle().getDesignation());
                }
                BigDecimal qteToRmvFromMvtsto = qte.min(mvtsto.getQteben());
                if (qteToRmvFromMvtsto.compareTo(BigDecimal.ZERO) > 0) {
                    mvtsto.setQteben(mvtsto.getQteben().subtract(qteToRmvFromMvtsto));
                    Facture facture = mvtsto.getFacture();
                    facture.setEtatbon(true);
                    listFactureToUpdate.add(facture);
                    listMvtstoToUpdate.add(mvtsto);
                    MvtstoMvtstoAV mvtstomvtstoAV = new MvtstoMvtstoAV();
                    mvtstomvtstoAV.setCodart(detailAvoir.getCodart());
                    mvtstomvtstoAV.setNumBonMvtsto(mvtsto.getMvtstoPK().getNumbon());
                    mvtstomvtstoAV.setNumordreMvtsto(mvtsto.getMvtstoPK().getNumordre());
                    mvtstomvtstoAV.setNumBonMvtstoAV(mvtstoAV.getMvtStoAVPK().getNumbon());
                    mvtstomvtstoAV.setNumordreMvtstoAV(mvtstoAV.getMvtStoAVPK().getNumordre());
                    mvtstomvtstoAV.setQte(qteToRmvFromMvtsto);
                    listMvtstomvtstoAV.add(mvtstomvtstoAV);
                    qte = qte.subtract(qteToRmvFromMvtsto);
                    priuni = priuni.add(qteToRmvFromMvtsto.multiply(mvtsto.getPriuni()));
                    priAch = priAch.add(qteToRmvFromMvtsto.multiply(mvtsto.getPriach()));
                    prixBrute = prixBrute.add(qteToRmvFromMvtsto.multiply(mvtsto.getPrixBrute()));
                    remise = remise.add(qteToRmvFromMvtsto.multiply(mvtsto.getRemise()));
                    tauxCouverture = tauxCouverture.add(qteToRmvFromMvtsto.multiply(mvtsto.getTauxCouverture()));
                    majoration = majoration.add(qteToRmvFromMvtsto.multiply(mvtsto.getMajoration()));
                    ajustement = ajustement.add(qteToRmvFromMvtsto.multiply(mvtsto.getAjustement()));
                    List<DetailMvtsto> listDetailMvtsto = mvtsto.getDetailMvtstoCollection().stream()
                            .filter(item -> item.getQte().subtract(item.getQteAvoir()).compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
                    Collections.sort(listDetailMvtsto, (p1, p2) -> p1.getPu().compareTo(p2.getPu()));//TODO avec sofien

                    for (DetailMvtsto detailMvtsto : listDetailMvtsto) {
                        BigDecimal qteToAddInDepsto = qteToRmvFromMvtsto.min(detailMvtsto.getQte().subtract(detailMvtsto.getQteAvoir()));
                        if (qteToAddInDepsto.compareTo(BigDecimal.ZERO) > 0) {
                            detailMvtsto.setQteAvoir(detailMvtsto.getQteAvoir().add(qteToAddInDepsto));
                            Depsto depsto = detailMvtsto.getDepsto();
                            depsto.setQte(depsto.getQte().add(qteToAddInDepsto));
                            detailMvtsto.setDepsto(depsto);
                            listDetailMvtstoToUpdate.add(detailMvtsto);
                            DetailMvtstoAV detailMvtstoAV = new DetailMvtstoAV();
                            detailMvtstoAV.setCodeDetailMvtsto(detailMvtsto.getCode());
                            detailMvtstoAV.setCodart(detailAvoir.getCodart());
                            detailMvtstoAV.setDepsto(depsto);
                            detailMvtstoAV.setNumbonMvtstoAV(mvtstoAV.getMvtStoAVPK().getNumbon());
                            detailMvtstoAV.setNumordreMvtstoAV(mvtstoAV.getMvtStoAVPK().getNumordre());
                            detailMvtstoAV.setCodeTva(depsto.getCodeTva());
                            detailMvtstoAV.setTauxTva(depsto.getTauxTva());
                            detailMvtstoAV.setPu(depsto.getPu());
                            detailMvtstoAV.setQte(qteToAddInDepsto);
                            listDetailMvtstoAV.add(detailMvtstoAV);
                            qteToRmvFromMvtsto = qteToRmvFromMvtsto.subtract(qteToAddInDepsto);
                        }
                        if (qteToAddInDepsto.compareTo(BigDecimal.ZERO) == 0) {
                            continue;
                        }
                    }
                }
                if (qteToRmvFromMvtsto.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                
            }
            mvtstoAV.setPriach(priAch.divide(mvtstoAV.getQuantite(), 7, RoundingMode.HALF_UP));
            mvtstoAV.setPriuni(priuni.divide(mvtstoAV.getQuantite(), 7, RoundingMode.HALF_UP));
            mvtstoAV.setPrixBrute(prixBrute.divide(mvtstoAV.getQuantite(), 7, RoundingMode.HALF_UP));
            mvtstoAV.setRemise(remise.divide(mvtstoAV.getQuantite(), 7, RoundingMode.HALF_UP));
            mvtstoAV.setTauxCouverture(tauxCouverture.divide(mvtstoAV.getQuantite(), 7, RoundingMode.HALF_UP));
            mvtstoAV.setMajoration(majoration.divide(mvtstoAV.getQuantite(), 7, RoundingMode.HALF_UP));
            mvtstoAV.setAjustement(ajustement.divide(mvtstoAV.getQuantite(), 7, RoundingMode.HALF_UP));
            mvtstoAV.setMontht(mvtstoAV.getPriuni().multiply(mvtstoAV.getQuantite()));
            mvtstoAV.setFactureAV(factureAV);
            details.add(mvtstoAV);
        }
        List<String> listRemiseConventionnellePharmacie = listFactureToUpdate.stream().map(Facture::getRemiseConventionnellePharmacie).distinct().collect(Collectors.toList());
        checkBusinessLogique(listRemiseConventionnellePharmacie.size() == 1, "remise-conventionnelle-pharmacie-unique");
        factureAV.setRemiseConventionnellePharmacie(listRemiseConventionnellePharmacie.get(0));
        factureAV.setCodeSociete(listFactureToUpdate.get(0).getCodeSociete());
        
        factureAV.setMvtStoAVCollection(details);
        String code = factureAV.getNumdoss().concat(factureAV.getCoddep().toString());
        for (MvtStoAV mvtStoAV : factureAV.getMvtStoAVCollection()) {
            code = code.concat(mvtStoAV.getMvtStoAVPK().getCodart().toString());
        }
        byte[] hashCode = Helper.hashing(code);
        if (!pharmacieExterne) {
            List<FactureAV> listFactureAVByHashCodeAndDatbonGreaterThan = factureavRepository.findByHashCodeAndDatbonGreaterThan(hashCode, LocalDateTime.now().minusMinutes(BLOCKING_DELAY_BETWEEN_QUITTANCES));
            checkBusinessLogique(listFactureAVByHashCodeAndDatbonGreaterThan.isEmpty(), "blockage.avoir.double");
        }
        factureAV.setHashCode(hashCode);
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        log.debug("listTvas {}", listTvas);
        log.debug("factureAV.getMvtStoAVCollection() {}", factureAV.getMvtStoAVCollection());
        factureAV.calcul(listTvas, pharmacieExterne);
        
         factureAV.setCodeCostCenter(depotDTO.getCodeCostCenterDeparement());
            
        paramService.updateCompteurPharmacie(factureAV.getCategDepot(), TypeBonEnum.AV);
        if (avoir.getMntbon() != null) {
            log.debug("********  mnt bon calculé  {} ***********", factureAV.getMntbon().setScale(2, RoundingMode.HALF_UP));
            log.debug("**********     mnt bon     {}   ************", avoir.getMntbon());
            checkBusinessLogique(avoir.getMntbon().compareTo(factureAV.getMntbon().setScale(2, RoundingMode.HALF_UP)) == 0, "error-montant-bon");
        }
        return factureAV;
    }

    /**
     * Save a avoir.
     *
     * @param avoirWithReglement
     * @param pharmacieExterne
     * @return the persisted entity
     */
    public List<FactureAVDTO> saves(AvoirWithReglement avoirWithReglement, Boolean pharmacieExterne) throws NoSuchAlgorithmException {
        String numbonComplementaire = null;
        List<Mvtsto> listMvtstoToUpdate = new ArrayList<>();
        List<Facture> listFactureToUpdate = new ArrayList<>();
        List<DetailMvtsto> listDetailMvtstoToUpdate = new ArrayList<>();
        List<MvtstoMvtstoAV> listMvtstomvtstoAV = new ArrayList<>();
        List<DetailMvtstoAV> listDetailMvtstoAV = new ArrayList<>();
        List<FactureAV> factureAVs = new ArrayList<>();
        List<FactureAVDTO> factureAVDTOs = new ArrayList<>();
        List<Avoir> avoirs = avoirWithReglement.getAvoirs();
        int compteNumdoss = avoirs.stream().map(Avoir::getNumdoss).distinct().collect(Collectors.toList()).size();
        checkBusinessLogique(compteNumdoss == 1, "avoir.unique.dossier");
        
        for (Avoir avoir : avoirs) {
            factureAVs.add(saveAvoir(avoir, listMvtstoToUpdate, listFactureToUpdate, listDetailMvtstoToUpdate, listMvtstomvtstoAV, listDetailMvtstoAV, pharmacieExterne));
        }
        
        if (pharmacieExterne) {
            Paramph param = paramService.findparambycode("numbonComplementaireAvoir");
            numbonComplementaire = "RS" + param.getValeur();
            param.setValeur(Helper.IncrementString(param.getValeur(), 7));
            paramService.updateparam(param);
        }
        for (FactureAV factureAV : factureAVs) {
            DepotDTO depotDTO = paramAchatServiceClient.findDepotByCode(factureAV.getCoddep());
            factureAV.setMntbon(factureAV.getMntbon().setScale(2, RoundingMode.HALF_UP));
            factureAV.setPartiePEC(factureAV.getPartiePEC().setScale(2, RoundingMode.HALF_UP));
            factureAV.setPartiePatient(factureAV.getPartiePatient().setScale(2, RoundingMode.HALF_UP));
            if (numbonComplementaire != null) {
                factureAV.setNumbonComplementaire(numbonComplementaire);
            }
            FactureAVDTO factureDTO = factureAVFactory.factureavToFactureAVDTO(factureAV);
            NatureDepotDTO natureDepot = depotDTO.getNatureDepot().stream().filter(item -> item.getCategorieDepot().equals(factureDTO.getCategDepot())).findFirst().orElse(null);
            checkBusinessLogique(natureDepot != null, "missing-prestation-depot");
            factureDTO.setCodePrestation(natureDepot.getPrestationID());
            factureDTO.setMntmpl(BigDecimal.valueOf(factureDTO.getMvtstoCollection().stream().map(x -> (x.getPrixBrute().multiply(x.getQuantite())).multiply(BigDecimal.valueOf(100).add(x.getTautva())).divide(BigDecimal.valueOf(100))).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
            factureAVDTOs.add(factureDTO);
        }
        
        factureRepository.save(listFactureToUpdate);
        detailMvtstoAVRepository.save(listDetailMvtstoAV);
        mvtstomvtstoAVRepository.save(listMvtstomvtstoAV);
        mvtstoRepository.save(listMvtstoToUpdate);
        detailMvtstoRepository.save(listDetailMvtstoToUpdate);
        factureavRepository.save(factureAVs);
        return factureAVDTOs;
        
    }

    /**
     * Save a avoir.
     *
     * @param numQuittances
     * @param codeAdmission
     * @param pharmacieExterne
     * @return the persisted entity
     */
    public List<FactureAVDTO> saveAvoirTotals(List<String> numQuittances, String codeAdmission, Boolean pharmacieExterne) throws NoSuchAlgorithmException {
        List<Mvtsto> listMvtstoToUpdate = new ArrayList<>();
        List<Facture> listFactureToUpdate = new ArrayList<>();
        List<DetailMvtsto> listDetailMvtstoToUpdate = new ArrayList<>();
        List<MvtstoMvtstoAV> listMvtstomvtstoAV = new ArrayList<>();
        List<DetailMvtstoAV> listDetailMvtstoAV = new ArrayList<>();
        List<FactureAV> factureAVs = new ArrayList<>();
        List<FactureAVDTO> factureAVDTOs = new ArrayList<>();
        SocieteDTO societe = receptionServiceClient.findSocieteByCodeAdmission(codeAdmission);
        for (String numQuittance : numQuittances) {
            factureAVs.add(saveAvoirTotal(numQuittance, codeAdmission, listMvtstoToUpdate, listFactureToUpdate, listDetailMvtstoToUpdate, listMvtstomvtstoAV, listDetailMvtstoAV, societe, pharmacieExterne));
        }
        for (FactureAV factureAV : factureAVs) {
            DepotDTO depotDTO = paramAchatServiceClient.findDepotByCode(factureAV.getCoddep());
            factureAV.setMntbon(factureAV.getMntbon().setScale(2, RoundingMode.HALF_UP));
            factureAV.setPartiePEC(factureAV.getPartiePEC().setScale(2, RoundingMode.HALF_UP));
            factureAV.setPartiePatient(factureAV.getPartiePatient().setScale(2, RoundingMode.HALF_UP));
            FactureAVDTO factureDTO = factureAVFactory.factureavToFactureAVDTO(factureAV);
            NatureDepotDTO natureDepot = depotDTO.getNatureDepot().stream().filter(item -> item.getCategorieDepot().equals(factureDTO.getCategDepot())).findFirst().orElse(null);
            checkBusinessLogique(natureDepot != null, "missing-prestation-depot");
            factureDTO.setCodePrestation(natureDepot.getPrestationID());
            factureDTO.setMntmpl(BigDecimal.valueOf(factureDTO.getMvtstoCollection().stream().map(x -> (x.getPrixBrute().multiply(x.getQuantite())).multiply(BigDecimal.valueOf(100).add(x.getTautva())).divide(BigDecimal.valueOf(100))).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
            factureAVDTOs.add(factureDTO);
        }
        if (pharmacieExterne) {
            Paramph param = paramService.findparambycode("numbonComplementaireAvoir");
            String numbonComplementaire = "RS" + param.getValeur();
            param.setValeur(Helper.IncrementString(param.getValeur(), 7));
            paramService.updateparam(param);
            for (FactureAV factureAV : factureAVs) {
                factureAV.setNumbonComplementaire(numbonComplementaire);
            }
        }
        factureRepository.save(listFactureToUpdate);
        detailMvtstoAVRepository.save(listDetailMvtstoAV);
        mvtstomvtstoAVRepository.save(listMvtstomvtstoAV);
        mvtstoRepository.save(listMvtstoToUpdate);
        detailMvtstoRepository.save(listDetailMvtstoToUpdate);
        factureavRepository.save(factureAVs);
        return factureAVDTOs;
        
    }

    /**
     * Save a avoir.
     *
     * @param numQuittance
     * @param listMvtstoToUpdate
     * @param codeAdmission
     * @param listFactureToUpdate
     * @param listDetailMvtstoToUpdate
     * @param listMvtstomvtstoAV
     * @param listDetailMvtstoAV
     * @param societe
     * @param pharmacieExterne
     * @return the persisted entity
     */
    public FactureAV saveAvoirTotal(String numQuittance, String codeAdmission, List<Mvtsto> listMvtstoToUpdate, List<Facture> listFactureToUpdate, List<DetailMvtsto> listDetailMvtstoToUpdate, List<MvtstoMvtstoAV> listMvtstomvtstoAV, List<DetailMvtstoAV> listDetailMvtstoAV, SocieteDTO societe, Boolean pharmacieExterne) throws NoSuchAlgorithmException {
        Facture facture = factureService.findByNumdossAndNumbon(codeAdmission, numQuittance);
        String numBon = paramService.getcompteur(facture.getCategDepot(), TypeBonEnum.AV);
        facture = factureService.findByNumdossAndNumbon(codeAdmission, numQuittance);
        checkBusinessLogique(facture != null, "facture.NotFound");
        checkBusinessLogique(facture.getCodAnnul() == null, "facture.Annule");
        checkBusinessLogique(!facture.isEtatbon(), "facture.Avoir");
        DepotDTO depotDTO = paramAchatServiceClient.findDepotByCode(facture.getCoddep());
        Preconditions.checkBusinessLogique(!depotDTO.getDesignation().equals("depot.deleted"), "Depot [" + depotDTO.getDesignation() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotDTO.getDepotFrs(), "Dépot [" + depotDTO.getDesignation() + "] est un dépot fournisseur");
        
        List<Mvtsto> listeMvtsto = facture.getMvtstoCollection();
        List<Integer> codarts = listeMvtsto.stream().map(x -> x.getMvtstoPK().getCodart()).collect(Collectors.toList());
        List<ArticleDTO> listArticleDTOs = paramAchatServiceClient.articleFindbyListCode(codarts);

        // check for inventory 
        List<Integer> categArticleIDs = listArticleDTOs.stream().map(item -> item.getCategorieArticle().getCode()).collect(toList());
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(categArticleIDs, facture.getCoddep());
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = listArticleDTOs.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }
        
        FactureAV factureAV = avoirFactory.factureToFactureAV(facture);
        factureAV.setNumbon(numBon);
        factureAV.setRemiseConventionnellePharmacie(facture.getRemiseConventionnellePharmacie());
        factureAV.setCodeSociete(facture.getCodeSociete());
        factureAV.setNumQuittanceCorrespondant(numQuittance);
        List<MvtStoAV> details = new ArrayList<>();
        String numordre = "0001";
        for (Mvtsto mvtsto : listeMvtsto) {
            if (pharmacieExterne) {
                ArticleDTO articleDTO = listArticleDTOs.stream()
                        .filter(art -> art.getCode().equals(mvtsto.getMvtstoPK().getCodart())
                        && art.getThermosensitive()
                        && mvtsto.getFacture().getDatbon().plusMinutes(MAX_DELAY_RETURN_ITEM_THERMOSENSITIVE).compareTo(LocalDateTime.now()) < 0).findFirst()
                        .orElse(null);
                checkBusinessLogique(articleDTO == null,
                        "return.add.item.thermosensitive", articleDTO == null ? "" : articleDTO.getDesignation());
            }
            MvtStoAV mvtstoAV = new MvtStoAV();
            String ordre = TypeBonEnum.AV.name() + numordre;
            MvtStoAVPK pk = new MvtStoAVPK();
            pk.setCodart(mvtsto.getMvtstoPK().getCodart());
            pk.setNumbon(numBon);
            pk.setNumordre(ordre);
            mvtstoAV.setMvtStoAVPK(pk);
            mvtstoAV.setUnite(mvtsto.getUnite());
            mvtstoAV.setTypbon(TypeBonEnum.AV);
            mvtstoAV.setCodtva(mvtsto.getCodtva());
            mvtstoAV.setMemoart(mvtsto.getMemoart());
            mvtstoAV.setTautva(mvtsto.getTautva());
            mvtstoAV.setCategDepot(facture.getCategDepot());
            mvtstoAV.setDesart(mvtsto.getDesart());
            mvtstoAV.setDesArtSec(mvtsto.getDesArtSec());
            mvtstoAV.setLotInter(mvtsto.getLotInter());
            mvtstoAV.setDatPer(mvtsto.getDatPer());
            mvtstoAV.setCodeSaisi(mvtsto.getCodeSaisi());
            mvtstoAV.setQuantite(mvtsto.getQteben());
            numordre = Helper.IncrementString(numordre, 4);
            mvtsto.setQteben(BigDecimal.ZERO);
            facture.setEtatbon(true);
            listFactureToUpdate.add(facture);
            listMvtstoToUpdate.add(mvtsto);
            MvtstoMvtstoAV mvtstomvtstoAV = new MvtstoMvtstoAV();
            mvtstomvtstoAV.setCodart(mvtsto.getMvtstoPK().getCodart());
            mvtstomvtstoAV.setNumBonMvtsto(mvtsto.getMvtstoPK().getNumbon());
            mvtstomvtstoAV.setNumordreMvtsto(mvtsto.getMvtstoPK().getNumordre());
            mvtstomvtstoAV.setNumBonMvtstoAV(mvtstoAV.getMvtStoAVPK().getNumbon());
            mvtstomvtstoAV.setNumordreMvtstoAV(mvtstoAV.getMvtStoAVPK().getNumordre());
            mvtstomvtstoAV.setQte(mvtstoAV.getQuantite());
            listMvtstomvtstoAV.add(mvtstomvtstoAV);
            for (DetailMvtsto detailMvtsto : mvtsto.getDetailMvtstoCollection()) {
                BigDecimal qteToAddInDepsto = mvtstoAV.getQuantite().min(detailMvtsto.getQte().subtract(detailMvtsto.getQteAvoir()));
                if (qteToAddInDepsto.compareTo(BigDecimal.ZERO) > 0) {
                    detailMvtsto.setQteAvoir(detailMvtsto.getQteAvoir().add(qteToAddInDepsto));
                    Depsto depsto = detailMvtsto.getDepsto();
                    depsto.setQte(depsto.getQte().add(qteToAddInDepsto));
                    detailMvtsto.setDepsto(depsto);
                    listDetailMvtstoToUpdate.add(detailMvtsto);
                    DetailMvtstoAV detailMvtstoAV = new DetailMvtstoAV();
                    detailMvtstoAV.setCodeDetailMvtsto(detailMvtsto.getCode());
                    detailMvtstoAV.setCodart(mvtsto.getMvtstoPK().getCodart());
                    detailMvtstoAV.setDepsto(depsto);
                    detailMvtstoAV.setNumbonMvtstoAV(mvtstoAV.getMvtStoAVPK().getNumbon());
                    detailMvtstoAV.setNumordreMvtstoAV(mvtstoAV.getMvtStoAVPK().getNumordre());
                    detailMvtstoAV.setCodeTva(depsto.getCodeTva());
                    detailMvtstoAV.setTauxTva(depsto.getTauxTva());
                    detailMvtstoAV.setPu(depsto.getPu());
                    detailMvtstoAV.setQte(qteToAddInDepsto);
                    listDetailMvtstoAV.add(detailMvtstoAV);
                }
                if (qteToAddInDepsto.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
            }
            mvtstoAV.setPriach(mvtsto.getPriach());
            mvtstoAV.setPriuni(mvtsto.getPriuni());
            mvtstoAV.setPrixBrute(mvtsto.getPrixBrute());
            mvtstoAV.setRemise(mvtsto.getRemise());
            mvtstoAV.setTauxCouverture(mvtsto.getTauxCouverture());
            mvtstoAV.setMajoration(mvtsto.getMajoration());
            mvtstoAV.setAjustement(mvtsto.getAjustement());
            mvtstoAV.setMontht(mvtstoAV.getPriuni().multiply(mvtstoAV.getQuantite()));
            mvtstoAV.setFactureAV(factureAV);
            details.add(mvtstoAV);
        }
        factureAV.setMvtStoAVCollection(details);
        String code = factureAV.getNumdoss().concat(factureAV.getCoddep().toString());
        for (MvtStoAV mvtStoAV : factureAV.getMvtStoAVCollection()) {
            code = code.concat(mvtStoAV.getMvtStoAVPK().getCodart().toString());
        }
        byte[] hashCode = Helper.hashing(code);
        if (!pharmacieExterne) {
            List<FactureAV> listFactureAVByHashCodeAndDatbonGreaterThan = factureavRepository.findByHashCodeAndDatbonGreaterThan(hashCode, LocalDateTime.now().minusMinutes(BLOCKING_DELAY_BETWEEN_QUITTANCES));
            checkBusinessLogique(listFactureAVByHashCodeAndDatbonGreaterThan.isEmpty(), "blockage.avoir.double");
        }
        factureAV.setHashCode(hashCode);
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        log.debug("listTvas {}", listTvas);
        log.debug("factureAV.getMvtStoAVCollection() {}", factureAV.getMvtStoAVCollection());
        factureAV.calcul(listTvas, pharmacieExterne);
        
        factureAV.setCodeCostCenter(depotDTO.getCodeCostCenterDeparement());
        paramService.updateCompteurPharmacie(factureAV.getCategDepot(), TypeBonEnum.AV);
        return factureAV;
    }

    /**
     * Save a avoir.
     *
     * @param avoir
     * @param pharmacieExterne
     * @return the persisted entity
     * @throws java.security.NoSuchAlgorithmException
     */
    public List<FactureAVDTO> save(Avoir avoir, Boolean pharmacieExterne) throws NoSuchAlgorithmException {
        List<Mvtsto> listMvtstoToUpdate = new ArrayList<>();
        List<Facture> listFactureToUpdate = new ArrayList<>();
        List<DetailMvtsto> listDetailMvtstoToUpdate = new ArrayList<>();
        List<MvtstoMvtstoAV> listMvtstomvtstoAV = new ArrayList<>();
        List<DetailMvtstoAV> listDetailMvtstoAV = new ArrayList<>();
        List<FactureAV> factureAVs = new ArrayList<>();
        List<FactureAVDTO> factureAVDTOs = new ArrayList<>();
        
        factureAVs.add(saveAvoir(avoir, listMvtstoToUpdate, listFactureToUpdate, listDetailMvtstoToUpdate, listMvtstomvtstoAV, listDetailMvtstoAV, pharmacieExterne));
        for (FactureAV factureAV : factureAVs) {
            DepotDTO depotDTO = paramAchatServiceClient.findDepotByCode(factureAV.getCoddep());
            factureAV.setMntbon(factureAV.getMntbon().setScale(2, RoundingMode.HALF_UP));
            factureAV.setPartiePEC(factureAV.getPartiePEC().setScale(2, RoundingMode.HALF_UP));
            factureAV.setPartiePatient(factureAV.getPartiePatient().setScale(2, RoundingMode.HALF_UP));
            FactureAVDTO factureDTO = factureAVFactory.factureavToFactureAVDTO(factureAV);
            NatureDepotDTO natureDepot = depotDTO.getNatureDepot().stream().filter(item -> item.getCategorieDepot().equals(factureDTO.getCategDepot())).findFirst().orElse(null);
            checkBusinessLogique(natureDepot != null, "missing-prestation-depot");
            factureDTO.setCodePrestation(natureDepot.getPrestationID());
            factureDTO.setMntmpl(BigDecimal.valueOf(factureDTO.getMvtstoCollection().stream().map(x -> (x.getPrixBrute().multiply(x.getQuantite())).multiply(BigDecimal.valueOf(100).add(x.getTautva())).divide(BigDecimal.valueOf(100))).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
            factureAVDTOs.add(factureDTO);
        }
        if (pharmacieExterne) {
            Paramph param = paramService.findparambycode("numbonComplementaireAvoir");
            String numbonComplementaire = "RS" + param.getValeur();
            param.setValeur(Helper.IncrementString(param.getValeur(), 7));
            paramService.updateparam(param);
            for (FactureAV factureAV : factureAVs) {
                factureAV.setNumbonComplementaire(numbonComplementaire);
            }
        }
        detailMvtstoAVRepository.save(listDetailMvtstoAV);
        mvtstomvtstoAVRepository.save(listMvtstomvtstoAV);
        mvtstoRepository.save(listMvtstoToUpdate);
        detailMvtstoRepository.save(listDetailMvtstoToUpdate);
        factureavRepository.save(factureAVs);
        factureRepository.save(listFactureToUpdate);
        return factureAVDTOs;
        
    }

    /**
     * Delete facture by liste id.
     *
     * @param avoirIDs
     * @return
     */
    public List<FactureAV> deletesPermanent(List<String> avoirIDs) {
        log.debug("Request to delete quittances: {}", avoirIDs.toString());
        List<FactureAV> factureAVs = new ArrayList<>();
        for (String id : avoirIDs) {
            factureAVs.add(deleteFactureAVPermanent(id));
        }
        factureavRepository.delete(factureAVs);
        
        return factureAVs;
    }

    /**
     * Delete factureAV by liste id.
     *
     * @param avoirIDs
     * @return
     */
    public List<FactureAVDTO> deletes(List<String> avoirIDs) {
        log.debug("Request to delete quittances: {}", avoirIDs.toString());
        List<FactureAV> factureAVs = deletesPermanent(avoirIDs);
        int compteNumdoss = factureAVs.stream().map(FactureAV::getNumdoss).distinct().collect(Collectors.toList()).size();
        checkBusinessLogique(compteNumdoss == 1, "facture.unique.dossier");
        return factureAVFactory.factureavToFactureAVDTOs(factureAVs);
    }

    /**
     * Delete facture by id.
     *
     * @param id the id of the entity
     * @param codeMotifSuppression
     * @param withFacturation annulation facturation quittance
     * @return Facture
     */
    private FactureAV deleteFactureAVPermanent(String id) {
        log.debug("Request to delete quittance: {}", id);
        FactureAV factureAV = factureavRepository.findOne(id);
        checkBusinessLogique(factureAV != null, "factureAV.NotFound");
        List<Facture> factures = new ArrayList<>();
        for (MvtStoAV mvtStoAV : factureAV.getMvtStoAVCollection()) {
            List<DetailMvtstoAV> detailMvtstoAVs = mvtStoAV.getDetailMvtstoAVCollection();
            List<MvtstoMvtstoAV> mvtstoMvtstoAVs = mvtStoAV.getMvtstoMvtstoAVCollection();
            List<DetailMvtsto> detailMvtstos = mvtStoAV.getMvtstoMvtstoAVCollection().stream().map(x -> x.getMvtsto())
                    .flatMap(e -> e.getDetailMvtstoCollection().stream()).collect(Collectors.toList());
            for (DetailMvtstoAV detailMvtstoAV : detailMvtstoAVs) {
                DetailMvtsto detailMvtsto = detailMvtstos.stream().filter(item -> {
                    return item.getCode().equals(detailMvtstoAV.getCodeDetailMvtsto());
                }).findFirst()
                        .orElse(null);
                if (detailMvtsto != null) {
                    detailMvtsto.setQteAvoir(detailMvtsto.getQteAvoir().subtract(detailMvtstoAV.getQte()));
                }
                Depsto depsto = detailMvtstoAV.getDepsto();
                depsto.setQte(depsto.getQte().subtract(detailMvtstoAV.getQte()));
            }
            for (MvtstoMvtstoAV mvtstoMvtstoAV : mvtstoMvtstoAVs) {
                Mvtsto mvtsto = mvtstoMvtstoAV.getMvtsto();
                mvtsto.setQteben(mvtsto.getQteben().add(mvtstoMvtstoAV.getQte()));
                factures.add(mvtsto.getFacture());
            }
        }
        for (Facture facture : factures) {
            facture.setEtatbon(false);
            for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                if (!mvtsto.getQteben().equals(mvtsto.getQuantite())) {
                    facture.setEtatbon(true);
                }
            }
        }
        return factureAV;
    }

    /**
     * Get one factureavDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public FactureAVDTO findOne(String id) {
        log.debug("Request to get FactureAV: {}", id);
        FactureAV factureAV = factureavRepository.findOne(id);
        FactureAVDTO dto = factureAVFactory.factureavToFactureAVDTO(factureAV);
        List<String> codeAdmissions = new ArrayList<>();
        codeAdmissions.add(factureAV.getNumdoss());
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        if (!clients.isEmpty()) {
            dto.setClient(clients.get(0));
        }
        List<Integer> codeUnites = new ArrayList<>();
        factureAV.getMvtStoAVCollection().forEach(x -> {
            codeUnites.add(x.getUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        dto.getMvtstoCollection().forEach((mvtstoDTO) -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            mvtstoDTO.setUnityCode(unite.getCode());
            mvtstoDTO.setUnityDesignation(unite.getDesignation());
        });
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(dto.getCodeDepot());
        dto.setDesignationDepot(depot.getDesignation());
        return dto;
    }

    /**
     * Get one factureav by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public FactureAV findFactureAV(String id) {
        log.debug("Request to get FactureAV: {}", id);
        FactureAV factureav = factureavRepository.findOne(id);
        return factureav;
    }

    /**
     * Get all the factureavs.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @param coddep
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<FactureAV> findAllAvoirs(List<CategorieDepotEnum> categ, LocalDateTime fromDate,
            LocalDateTime toDate, Integer coddep ) {
        log.debug("Request to get All FactureAVs");
        QFactureAV _facture = QFactureAV.factureAV;
        WhereClauseBuilder builder = new WhereClauseBuilder().
                optionalAnd(categ, () -> _facture.categDepot.in(categ))
                .optionalAnd(fromDate, () -> _facture.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _facture.datbon.loe(toDate))
                .optionalAnd(coddep, () -> _facture.coddep.eq(coddep));
        List<FactureAV> result = (List<FactureAV>) factureavRepository.findAll(builder);
        log.debug("result size is {}", result);

        return result;
    }
    
  @Transactional(readOnly = true)
    public List<FactureAVDTO> findAll(List<CategorieDepotEnum> categ, LocalDateTime fromDate,
            LocalDateTime toDate, String codeAdmission, Integer coddep, List<ReceptionConstants> etatPatient
    ) {
        log.debug("Request to get All FactureAVs");
        QFactureAV _facture = QFactureAV.factureAV;
        WhereClauseBuilder builder = new WhereClauseBuilder().
                optionalAnd(categ, () -> _facture.categDepot.in(categ))
                .optionalAnd(fromDate, () -> _facture.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _facture.datbon.loe(toDate))
                .optionalAnd(codeAdmission, () -> _facture.numdoss.eq(codeAdmission))
                .optionalAnd(coddep, () -> _facture.coddep.eq(coddep));
        List<FactureAV> result = (List<FactureAV>) factureavRepository.findAll(builder);
        log.debug("result size is {}", result);
        List<FactureAVDTO> resultDTO = factureAVFactory.factureavToFactureAVDTOLazys(result);
        List<String> codeAdmissions = new ArrayList<>();
        result.forEach(facture -> {
            codeAdmissions.add(facture.getNumdoss());
        });
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        resultDTO.forEach(factureAVDTO -> {
            AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(factureAVDTO.getNumdoss())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(client != null, "missing-client");
            factureAVDTO.setClient(client);
        });
        if (etatPatient != null) {
            List<Integer> codeEtatAdmission = new ArrayList<>();
            etatPatient.forEach(x -> {
                codeEtatAdmission.add(x.code());
            });
            resultDTO = resultDTO.stream().filter(item -> codeEtatAdmission.contains(item.getClient().getCodeEtatPatient())).collect(toList());
        }
        
        List<Integer> codeDepots = new ArrayList<>();
        resultDTO.forEach(x -> {
            codeDepots.add(x.getCodeDepot());
        });
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        resultDTO.forEach(factureDTO -> {
            DepotDTO depot = listDepot.stream().filter(x -> x.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(depot != null, "missing-depot");
            factureDTO.setDesignationDepot(depot.getDesignation());
        });
        return resultDTO;
    }
    /**
     * Get details factureAV.
     *
     * @param numBon
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<MvtStoAVDTO> findDetails(String numBon) {
        List<MvtStoAVDTO> result = MvtStoAVFactory.mvtstoavToMvtStoAVDTOs(mvtstoAVRepository.findByMvtStoAVPK_Numbon(numBon));
        List<Integer> codeUnites = new ArrayList<>();
        result.forEach(x -> {
            codeUnites.add(x.getUnityCode());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        result.forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<MvtstoDTO> searchMouvements(String[] quittancesIDs, List<CategorieDepotEnum> categDepot, String codeAdmission) {
        if (categDepot != null) {
            checkBusinessLogique(!categDepot.contains(CategorieDepotEnum.EC), "article-economat");
        }
        QMvtStoAV _mvtsto = QMvtStoAV.mvtStoAV;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(categDepot, () -> _mvtsto.factureAV().categDepot.in(categDepot))
                .optionalAnd(quittancesIDs, () -> _mvtsto.factureAV().numbon.in(quittancesIDs))
                .optionalAnd(codeAdmission, () -> _mvtsto.factureAV().numdoss.eq(codeAdmission));
        
        List<MvtstoDTO> result = MvtStoAVFactory.mvtstoAVToMvtstoDTOs((List<MvtStoAV>) mvtstoAVRepository.findAll(builder));
        List<Integer> codeUnites = new ArrayList<>();
        result.forEach(x -> {
            codeUnites.add(x.getUnityCode());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        result.forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });
        
        return result;
    }

    /**
     * Get details MvtSto.
     *
     * @param numBonMvtsto
     * @param numordreMvtstoAV
     * @param codart
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<MvtstomvtstoAVDTO> findDetailsMvtStoAV(String numBonMvtsto, String numordreMvtstoAV, Integer codart) {
        List<MvtstoMvtstoAV> mvtstoMvtstoAVs = mvtstomvtstoAVRepository.findByNumBonMvtstoAVAndNumordreMvtstoAVAndCodart(numBonMvtsto, numordreMvtstoAV, codart);
        List<MvtstomvtstoAVDTO> result = MvtstomvtstomvtstoAVFactory.mvtstomvtstoAVToMvtstomvtstomvtstoAVAVDTOs(mvtstoMvtstoAVs);
        List<Integer> codeUnites = new ArrayList<>();
        result.forEach(x -> {
            codeUnites.add(x.getUnityCode());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        result.forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });
        return result;
    }

    /**
     * Edition FactureAV by numBon.
     *
     * @param numBon the numBon of the entity
     * @return
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public byte[] edition(String numBon) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        FactureAV factureAV = factureavRepository.findOne(numBon);
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        reportClientDoc.open("Reports/Avoir" + local + ".rpt", 0);
        
        Preconditions.checkFound(factureAV, "factureAV.NotFound");
        FactureAVEditionDTO FactureAVDTO = factureAVFactory.factureavToFactureAVEditionDTO(factureAV);
        List<String> codeAdmissions = new ArrayList<>();
        codeAdmissions.add(factureAV.getNumdoss());
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        if (!clients.isEmpty()) {
            FactureAVDTO.setClient(clients.get(0));
        }
        List<Integer> codeUnites = new ArrayList<>();
        factureAV.getMvtStoAVCollection().forEach(x -> {
            codeUnites.add(x.getUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        FactureAVDTO.getMvtStoAVCollection().forEach((mvtstoDTO) -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            mvtstoDTO.setUnityCode(unite.getCode());
            mvtstoDTO.setUnityDesignation(unite.getDesignation());
        });
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(FactureAVDTO.getCodeDepot());
        FactureAVDTO.setDesignationDepot(depot.getDesignation());
        reportClientDoc
                .getDatabaseController().setDataSource(java.util.Arrays.asList(FactureAVDTO), FactureAVEditionDTO.class,
                        "entete", "entete");
        reportClientDoc
                .getDatabaseController().setDataSource(java.util.Arrays.asList(FactureAVDTO.getClient()), AdmissionDemandePECDTO.class,
                        "client", "client");
        reportClientDoc
                .getDatabaseController().setDataSource(FactureAVDTO.getMvtStoAVCollection(), MvtStoAVDTO.class,
                        "mvtStoAVCollection", "mvtStoAVCollection");
        reportClientDoc
                .getSubreportController().getSubreport("tva").getDatabaseController().setDataSource(FactureAVDTO.getBasetvaFactureAVCollection(), BaseTVADTO.class,
                "Commande", "Commande");
        reportClientDoc
                .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
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
     * Get details facture.
     *
     * @param categ
     * @param Numdoss
     * @param coddep
     * @param fromDate
     * @param toDate
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<MvtStoAVDTO> findDetailsAvoir(List<CategorieDepotEnum> categ, String Numdoss, Integer coddep, LocalDateTime fromDate,
            LocalDateTime toDate) {
        log.debug("Request to get Facture: {}", Numdoss);
        checkBusinessLogique(!categ.contains(CategorieDepotEnum.EC), "article-economat");
        QMvtStoAV _mvtsto = QMvtStoAV.mvtStoAV;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.factureAV().categDepot.in(categ))
                .optionalAnd(Numdoss, () -> _mvtsto.factureAV().numdoss.eq(Numdoss))
                .optionalAnd(coddep, () -> _mvtsto.factureAV().coddep.eq(coddep))
                .optionalAnd(fromDate, () -> _mvtsto.factureAV().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.factureAV().datbon.loe(toDate));
        
        List<MvtStoAVDTO> result = MvtStoAVFactory.mvtstoavToMvtStoAVDTOs((List<MvtStoAV>) mvtstoAVRepository.findAll(builder));
        List<Integer> codeUnites = new ArrayList<>();
        result.forEach(x -> {
            codeUnites.add(x.getUnityCode());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        result.forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });
        
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
        QMvtStoAV _mvtsto = QMvtStoAV.mvtStoAV;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.factureAV().categDepot.eq(categ))
                .optionalAnd(codart, () -> _mvtsto.mvtStoAVPK().codart.eq(codart))
                .optionalAnd(coddep, () -> _mvtsto.factureAV().coddep.eq(coddep))
                .optionalAnd(fromDate, () -> _mvtsto.factureAV().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.factureAV().datbon.loe(toDate));
        List<MvtStoAV> list = (List<MvtStoAV>) mvtstoAVRepository.findAll(builder);
        List<String> codeAdmissions = new ArrayList<>();
        List<Integer> codeUnites = new ArrayList<>();
        list.forEach(mvtsto -> {
            codeAdmissions.add(mvtsto.getFactureAV().getNumdoss());
            codeUnites.add(mvtsto.getUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        List<Mouvement> mouvements = new ArrayList<>();
        list.forEach(mvtsto -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mvtsto.getUnite())).findFirst().orElse(null);
            AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(mvtsto.getFactureAV().getNumdoss())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            com.csys.util.Preconditions.checkBusinessLogique(client != null, "missing-client");
            mouvements.addAll(mvtStoAVFactory.toMouvement(mvtsto, unite, client, typeDate));
        });
        return mouvements;
    }
    
    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return mvtstoAVRepository.findTotalMouvement(coddep, codart, toDate);
        } else {
            return mvtstoAVRepository.findTotalMouvement(coddep, codart, fromDate, toDate);
        }
    }
    
    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(List<Integer> codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        List<TotalMouvement> lists = new ArrayList<>();
        if (codart != null && codart.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codart.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {
                
                List<Integer> codesChunk = codart.subList(i * 2000, Math.min(i * 2000 + 2000, codart.size()));
                if (fromDate == null) {
                    completableFuture[i] = mvtstoAVRepository.findTotalMouvement(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = mvtstoAVRepository.findTotalMouvement(coddep, codesChunk, fromDate, toDate).whenComplete((articles, exception) -> {
                        lists.addAll(articles);
                    });
                }
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
    }
    
    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (coddep != null) {
            if (fromDate == null) {
                return mvtstoAVRepository.findTotalMouvement(coddep, toDate);
            } else {
                return mvtstoAVRepository.findTotalMouvement(coddep, fromDate, toDate);
            }
            
        } else {
            if (fromDate == null) {
                return mvtstoAVRepository.findTotalMouvement(toDate);
            } else {
                return mvtstoAVRepository.findTotalMouvement(fromDate, toDate);
            }
        }
    }

    /**
     * Get all the factures.
     *
     * @param codeAdmission
     * @param codes
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<FactureAV> findAll(String codeAdmission, List<String> codes
    ) {
        QFactureAV _factureAV = QFactureAV.factureAV;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_factureAV.numbon.in(codes))
                .and(_factureAV.numdoss.eq(codeAdmission));
        log.debug("Request to get All Factures");
        List<FactureAV> result = (List<FactureAV>) factureavRepository.findAll(builder);
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<QtePrixMouvement> findQuantitePrixMouvementByCodartIn(List<Integer> coddep, LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categ, List<Integer> articleIds) {
        List<QtePrixMouvement> result = new ArrayList();
        Integer numberOfChunks = (int) Math.ceil((double) articleIds.size() / 2000);
        for (int i = 0; i < numberOfChunks; i++) {
            List<Integer> articleIdChunk = articleIds.subList(i * 2000, Math.min(i * 2000 + 2000, articleIds.size()));
            if (coddep != null) {
                result.addAll(detailMvtstoAVRepository.findQuantitePrixMouvementByCodartIn(coddep, fromDate, toDate, categ, articleIdChunk));
            } else {
                result.addAll(detailMvtstoAVRepository.findQuantitePrixMouvementByCodartIn(fromDate, toDate, categ, articleIdChunk));
            }
        }
        return result;
    }
    
    public MouvementDuJour findMouvementDuJour() throws ParseException {
        Date date = new Date();
        return factureavRepository.findMouvementDuJour(date);
    }
    
    public List<BaseAvoirQuittance> updateOrganismePEC(List<String> codes, Integer codePriceList, Integer codeListCouverture,
            Integer codeNatureAdmission, Integer codeConvention, String numdoss, Boolean pharmacieExterne, SocieteDTO societe) {
        List<BaseAvoirQuittance> resultat = new ArrayList<>();
        List<FactureAV> listAvoirs = findAll(numdoss, codes);
        List<Integer> codarts = listAvoirs.stream().flatMap(x -> x.getMvtStoAVCollection().stream()).map(x -> x.getMvtStoAVPK().getCodart()).collect(Collectors.toList());
        List<RemiseConventionnelleDTO> remiseConventionnelles = parametrageService.pricelisteParArticle(codarts, codePriceList, codeListCouverture, codeNatureAdmission, codeConvention, pharmacieExterne);
        Collection<Integer> coddeps = listAvoirs.stream().map(x -> x.getCoddep()).collect(Collectors.toList());
        List<DepotDTO> depotDTOs = paramAchatServiceClient.findDepotsByCodes(coddeps);
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        
        for (FactureAV factureAV : listAvoirs) {
            for (MvtStoAV mvtstoAV : factureAV.getMvtStoAVCollection()) {
                RemiseConventionnelleDTO remiseConventionnelle = remiseConventionnelles.stream().filter(
                        item -> item.getCodeArticle().equals(mvtstoAV.getMvtStoAVPK().getCodart())).findFirst().orElse(null);
                checkBusinessLogique(remiseConventionnelle != null, "error-remise-article");
                if ("MAJ".equals(remiseConventionnelle.getNatureException())) {
                    mvtstoAV.setMajoration(remiseConventionnelle.getTaux());
                    mvtstoAV.setRemise(BigDecimal.ZERO);
                } else {
                    mvtstoAV.setRemise(remiseConventionnelle.getTaux());
                    mvtstoAV.setMajoration(BigDecimal.ZERO);
                }
                mvtstoAV.setTauxCouverture(remiseConventionnelle.getTauxCouverture());
                BigDecimal ajustement = BigDecimal.ZERO;
                for (MvtstoMvtstoAV mvtstoMvtstoAV : mvtstoAV.getMvtstoMvtstoAVCollection()) {
                    ajustement = ajustement.add(mvtstoMvtstoAV.getQte().multiply(mvtstoMvtstoAV.getMvtsto().getAjustement()));
                }
                log.debug("ajustement {}", ajustement);
                mvtstoAV.setAjustement(ajustement.divide(mvtstoAV.getQuantite()));
                mvtstoAV.setPriuni(mvtstoAV.getPrixBrute().multiply((BigDecimal.valueOf(100).subtract(mvtstoAV.getRemise()).add(mvtstoAV.getMajoration())).divide(BigDecimal.valueOf(100)).setScale(7, RoundingMode.HALF_UP)));
                mvtstoAV.setPriuni(mvtstoAV.getPriuni().multiply((BigDecimal.valueOf(100).subtract(mvtstoAV.getAjustement())).divide(BigDecimal.valueOf(100)).setScale(7, RoundingMode.HALF_UP)));
                mvtstoAV.setMontht(mvtstoAV.getPriuni().multiply(mvtstoAV.getQuantite()));
            }
            if (societe != null) {
                factureAV.setCodeSociete(societe.getCode());
                factureAV.setRemiseConventionnellePharmacie(societe.getRemiseConventionnellePharmacie());
            } else {
                factureAV.setCodeSociete(null);
                factureAV.setRemiseConventionnellePharmacie(null);
            }
            factureAV.calcul(listTvas, pharmacieExterne);
        }
        for (FactureAV factureAV : listAvoirs) {
            factureAV.setMntbon(factureAV.getMntbon().setScale(2, RoundingMode.HALF_UP));
            factureAV.setPartiePEC(factureAV.getPartiePEC().setScale(2, RoundingMode.HALF_UP));
            factureAV.setPartiePatient(factureAV.getPartiePatient().setScale(2, RoundingMode.HALF_UP));
        }
        listAvoirs = factureavRepository.save(listAvoirs);
        listAvoirs.forEach(factureAV -> {
            FactureAVDTO factureDTO = factureAVFactory.factureavToFactureAVDTO(factureAV);
            DepotDTO depotDTO = depotDTOs.stream().filter(
                    item -> item.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
            checkBusinessLogique(!depotDTO.getDesignation().equals("depot.deleted"), "Depot [" + depotDTO.getDesignation() + "] introuvable");
            NatureDepotDTO natureDepot = depotDTO.getNatureDepot().stream().filter(item -> item.getCategorieDepot().equals(factureDTO.getCategDepot())).findFirst().orElse(null);
            checkBusinessLogique(natureDepot != null, "missing-prestation-depot");
            factureDTO.setMntmpl(BigDecimal.valueOf(factureDTO.getMvtstoCollection().stream().map(x -> (x.getPrixBrute().multiply(x.getQuantite())).multiply(BigDecimal.valueOf(100).add(x.getTautva())).divide(BigDecimal.valueOf(100))).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
            factureDTO.setCodePrestation(natureDepot.getPrestationID());
            resultat.add(factureDTO);
        });
        return resultat;
    }

    /**
     * recalcul taux couverture pour list des quittances and avoirs.
     *
     * @param codes list des ids pour des quittances
     * @param codePriceList code price list
     * @param codeListCouverture code list couverture
     * @param codeNatureAdmission code nature admisssion
     * @param codeConvention code convention
     * @param societe
     * @param numdoss code admission
     * @param pharmacieExterne test pharmacie externe
     * @return Boolean
     */
    @Transactional(
            readOnly = true
    )
    public List<BaseAvoirQuittance> recalculTauxCouverture(List<String> codes, Integer codePriceList, Integer codeListCouverture,
            Integer codeNatureAdmission, Integer codeConvention, String numdoss, SocieteDTO societe, Boolean pharmacieExterne) {
        List<BaseAvoirQuittance> resultat = new ArrayList<>();
        List<FactureAV> listAvoirs = findAll(numdoss, codes);
        List<Integer> codarts = listAvoirs.stream().flatMap(x -> x.getMvtStoAVCollection().stream()).map(x -> x.getMvtStoAVPK().getCodart()).collect(Collectors.toList());
        List<RemiseConventionnelleDTO> remiseConventionnelles = parametrageService.pricelisteParArticle(codarts, codePriceList, codeListCouverture, codeNatureAdmission, codeConvention, pharmacieExterne);
        Collection<Integer> coddeps = listAvoirs.stream().map(x -> x.getCoddep()).collect(Collectors.toList());
        List<DepotDTO> depotDTOs = paramAchatServiceClient.findDepotsByCodes(coddeps);
        
        for (FactureAV factureAV : listAvoirs) {
            for (MvtStoAV mvtstoAV : factureAV.getMvtStoAVCollection()) {
                RemiseConventionnelleDTO remiseConventionnelle = remiseConventionnelles.stream().filter(
                        item -> item.getCodeArticle().equals(mvtstoAV.getMvtStoAVPK().getCodart())).findFirst().orElse(null);
                checkBusinessLogique(remiseConventionnelle != null, "error-remise-article");
                mvtstoAV.setTauxCouverture(remiseConventionnelle.getTauxCouverture());
            }
            factureAV.calculPartiePatientPartiePEC(societe, pharmacieExterne);
        }
        for (FactureAV factureAV : listAvoirs) {
            factureAV.setPartiePEC(factureAV.getPartiePEC().setScale(2, RoundingMode.HALF_UP));
            factureAV.setPartiePatient(factureAV.getPartiePatient().setScale(2, RoundingMode.HALF_UP));
        }
        listAvoirs.forEach(factureAV -> {
            FactureAVDTO factureDTO = factureAVFactory.factureavToFactureAVDTO(factureAV);
            DepotDTO depotDTO = depotDTOs.stream().filter(
                    item -> item.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
            checkBusinessLogique(!depotDTO.getDesignation().equals("depot.deleted"), "Depot [" + depotDTO.getDesignation() + "] introuvable");
            NatureDepotDTO natureDepot = depotDTO.getNatureDepot().stream().filter(item -> item.getCategorieDepot().equals(factureDTO.getCategDepot())).findFirst().orElse(null);
            checkBusinessLogique(natureDepot != null, "missing-prestation-depot");
            factureDTO.setMntmpl(BigDecimal.valueOf(factureDTO.getMvtstoCollection().stream().map(x -> (x.getPrixBrute().multiply(x.getQuantite())).multiply(BigDecimal.valueOf(100).add(x.getTautva())).divide(BigDecimal.valueOf(100))).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
            factureDTO.setCodePrestation(natureDepot.getPrestationID());
            resultat.add(factureDTO);
        });
        return resultat;
    }
    
    public Boolean updatePartiePatient(List<QuittanceDTO> quittanceDTOs) {
        List<String> listNumdoss = quittanceDTOs.stream().map(quittanceDTO -> quittanceDTO.getNumdoss()).distinct().collect(Collectors.toList());
        checkBusinessLogique(listNumdoss.size() == 1, "quittance.unique.dossier");
        String numdoss = listNumdoss.get(0);
        List<String> codes = quittanceDTOs.stream().map(quittanceDTO -> quittanceDTO.getNumbon()).distinct().collect(Collectors.toList());
        List<FactureAV> listAvoirs = findAll(numdoss, codes);
        for (FactureAV factureAV : listAvoirs) {
            QuittanceDTO quittanceDTO = quittanceDTOs.stream().filter(item -> {
                return item.getNumbon().equals(factureAV.getNumbon());
            }).findFirst()
                    .orElse(null);
            factureAV.setPartiePEC(quittanceDTO.getPartiePEC().abs());
            factureAV.setPartiePatient(quittanceDTO.getPartiePatient().abs());
            BigDecimal tauxCouverture = (factureAV.getPartiePEC().multiply(BigDecimal.valueOf(100))).divide(factureAV.getMntbon(), 7, RoundingMode.HALF_UP);
            for (MvtStoAV mvtStoAV : factureAV.getMvtStoAVCollection()) {
                mvtStoAV.setTauxCouverture(tauxCouverture);
            }
        }
        factureavRepository.save(listAvoirs);
        return Boolean.TRUE;
    }
    
    public List<FactureDTO> findAvoirs(LocalDateTime fromDate,
            LocalDateTime toDate, Integer coddep, List<ReceptionConstants> etatPatient, String search, Boolean withClient
    ) {
        log.debug("Request to get All FactureAVs");
        QFactureAV _facture = QFactureAV.factureAV;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(fromDate, () -> _facture.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _facture.datbon.loe(toDate))
                .optionalAnd(search, () -> _facture.numaffiche.like("%" + search + "%").or(_facture.numdoss.like("%" + search + "%")))
                .optionalAnd(coddep, () -> _facture.coddep.eq(coddep));
        List<FactureAV> result = (List<FactureAV>) factureavRepository.findAll(builder);
        List<FactureDTO> resultDTO = factureAVFactory.factureAVToFactureDTOs(result);
        if (withClient) {
            List<String> codeAdmissions = result.stream().map(x -> {
                return x.getNumdoss();
            }).distinct().collect(Collectors.toList());
            List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
            resultDTO.forEach(factureAVDTO -> {
                AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(factureAVDTO.getNumdoss())).findFirst().orElse(null);
                Preconditions.checkBusinessLogique(client != null, "missing-client");
                factureAVDTO.setClient(client);
            });
            if (etatPatient != null) {
                List<Integer> codeEtatAdmission = etatPatient.stream().map(x -> {
                    return x.code();
                }).distinct().collect(Collectors.toList());
                resultDTO = resultDTO.stream().filter(item -> codeEtatAdmission.contains(item.getClient().getCodeEtatPatient())).collect(toList());
            }
            
        }
        List<Integer> codeDepots = resultDTO.stream().map(x -> {
            return x.getCodeDepot();
        }).distinct().collect(Collectors.toList());
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        resultDTO.forEach(factureDTO -> {
            DepotDTO depot = listDepot.stream().filter(x -> x.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(depot != null, "missing-depot");
            factureDTO.setDesignationDepot(depot.getDesignation());
        });
        List<String> numbons = resultDTO.stream().map(FactureDTO::getNumbon).distinct().collect(Collectors.toList());
        List<FactureFactureAV> factureFactureAVs = factureService.findFactureFactureAV(numbons);
        resultDTO.forEach(factureDTO -> {
            List<FactureDTO> factures = factureFactureAVs.stream().filter(x -> x.getNumBonFactureAV().equals(factureDTO.getNumbon())).map(FactureFactureAV::getFacture)
                    .collect(Collectors.toList());
            factureDTO.setFactureCorespondantes(factures);
        });
        return resultDTO;
    }
    
    public List<FactureFactureAV> findFactureFactureAV(List<String> codeQuittances, Boolean withClient
    ) {
        List<FactureFactureAV> factureFactureAVs = new ArrayList<>();
        Integer numberOfChunks = (int) Math.ceil((double) codeQuittances.size() / 2000);
        for (int i = 0; i < numberOfChunks; i++) {
            List<String> codesChunk = codeQuittances.subList(i * 2000, Math.min(i * 2000 + 2000, codeQuittances.size()));
            factureFactureAVs.addAll(mvtstomvtstoAVRepository.findFactureFactureAVByNumBonMvtsto(codesChunk));
        }
        List<String> codes = factureFactureAVs.stream().map(x -> {
            return x.getNumBonFactureAV();
        }).distinct().collect(Collectors.toList());
        QFactureAV _factureAV = QFactureAV.factureAV;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_factureAV.numbon.in(codes));
        List<FactureAV> result = (List<FactureAV>) factureavRepository.findAll(builder);
        List<FactureDTO> resultDTO = factureAVFactory.factureAVToFactureDTOs(result);
        if (withClient) {
            List<String> codeAdmissions = result.stream().map(x -> {
                return x.getNumdoss();
            }).distinct().collect(Collectors.toList());
            List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
            resultDTO.forEach(factureAVDTO -> {
                AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(factureAVDTO.getNumdoss())).findFirst().orElse(null);
                Preconditions.checkBusinessLogique(client != null, "missing-client");
                factureAVDTO.setClient(client);
            });
            
        }
        
        List<Integer> codeDepots = resultDTO.stream().map(x -> {
            return x.getCodeDepot();
        }).distinct().collect(Collectors.toList());
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        resultDTO.forEach(factureDTO -> {
            DepotDTO depot = listDepot.stream().filter(x -> x.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(depot != null, "missing-depot");
            factureDTO.setDesignationDepot(depot.getDesignation());
        });
        factureFactureAVs.forEach(factureFactureAV -> {
            FactureDTO factureDTO = resultDTO.stream().filter(x -> x.getNumbon().equals(factureFactureAV.getNumBonFactureAV())).findFirst().orElse(null);
            factureFactureAV.setFactureAV(factureDTO);
        });
        return factureFactureAVs;
    }
    
    public List<FactureDTO> findByNumbonComplementaire(String numbonComplementaire) {
        log.debug("Request to find By numbonComplementaire");
        List<FactureDTO> resultDTO = new ArrayList<>();
        List<FactureAV> result = factureavRepository.findByNumbonComplementaire(numbonComplementaire);
        if (!result.isEmpty()) {
            resultDTO = factureAVFactory.factureAVToFactureDTOs(result);
            List<String> numbons = resultDTO.stream().map(FactureDTO::getNumbon).distinct().collect(Collectors.toList());
            List<FactureFactureAV> factureFactureAVs = factureService.findFactureFactureAV(numbons);
            resultDTO.forEach(factureDTO -> {
                List<FactureDTO> factures = factureFactureAVs.stream().filter(x -> x.getNumBonFactureAV().equals(factureDTO.getNumbon())).map(FactureFactureAV::getFacture)
                        .collect(Collectors.toList());
                factureDTO.setFactureCorespondantes(factures);
            });
        }
        return resultDTO;
    }

    /**
     * Get all the factures.
     *
     * @param quittancesIDs
     * @param categ
     * @param lazy
     * @return the the list of entities
     * @author randa
     */
    @Transactional(
            readOnly = true
    )
    public List<FactureDTO> findAvoirsByNumbonIn(String[] quittancesIDs, List<CategorieDepotEnum> categ) {
        log.debug("Request to find Avoirs ByNumbonIn");
        if (categ != null) {
            checkBusinessLogique(!categ.contains(CategorieDepotEnum.EC), "article-economat");
        }
        
        QFactureAV _factureAV = QFactureAV.factureAV;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(categ, () -> _factureAV.categDepot.in(categ))
                .and(_factureAV.numbon.in(quittancesIDs));
        
        List<FactureAV> result = (List<FactureAV>) factureavRepository.findAll(builder);
        List<FactureDTO> resultDTO = factureAVFactory.factureAVToFactureDTOs(result);
        List<Integer> codeDepots = resultDTO.stream().map(FactureDTO::getCodeDepot).distinct().collect(Collectors.toList());
        List<DepotDTO> depotDTOs = paramAchatServiceClient.findDepotsByCodes(codeDepots);
        resultDTO.forEach(factureDTO -> {
            NatureDepotDTO natureDepot = depotDTOs.stream().filter(item -> item.getCode().equals(factureDTO.getCodeDepot())).findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("Depot introuvable"))
                    .getNatureDepot().stream().filter(item -> item.getCategorieDepot().equals(factureDTO.getCategDepot())).findFirst().orElse(null);
            checkBusinessLogique(natureDepot != null, "missing-prestation-depot");
            factureDTO.setMntmpl(BigDecimal.valueOf(factureDTO.getMvtstoCollection().stream().map(x -> (x.getPrixBrute().multiply(x.getQuantite())).multiply(BigDecimal.valueOf(100).add(x.getTautva())).divide(BigDecimal.valueOf(100))).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
            factureDTO.setCodePrestation(natureDepot.getPrestationID());
        });
        return resultDTO;
    }
    
    @Transactional(readOnly = true)
    public List<String> findListNumBonsByNumDoss(String numDoss) {
        log.debug("Request to find num bons avoir By numDoss");
        return factureavRepository.findListNumBonsByNumDoss(numDoss);
    }
}
