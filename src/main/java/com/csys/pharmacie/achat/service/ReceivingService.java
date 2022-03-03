package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.QReceiving;
import com.csys.pharmacie.achat.domain.Receiving;
import com.csys.pharmacie.achat.domain.ReceivingDetails;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
import com.csys.pharmacie.achat.dto.DetailCommandeAchatDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.ReceivingDTO;
import com.csys.pharmacie.achat.dto.ReceivingDetailsDTO;
import com.csys.pharmacie.achat.dto.ReceivingEditionDTO;
import com.csys.pharmacie.achat.factory.ReceivingDetailsFactory;
import static com.csys.pharmacie.achat.factory.ReceivingDetailsFactory.receivingdetailsToReceivingDetailsDTO;
import com.csys.pharmacie.achat.factory.ReceivingFactory;
import com.csys.pharmacie.achat.repository.ReceivingRepository;
import com.csys.pharmacie.client.dto.SiteDTO;
import com.csys.pharmacie.client.service.ParamServiceClient;
import static com.csys.pharmacie.config.ServicesConfig.contextReception;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.util.Preconditions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Receiving.
 */
@Service
@Transactional
public class ReceivingService {

    private static String automaticReceiving;

    @Value("${automatic-receiving}")
    public void setAutomaticReceiving(String typeReceiving) {
        automaticReceiving = typeReceiving;
    }

    private final Logger log = LoggerFactory.getLogger(ReceivingService.class);

    private final ReceivingRepository receivingRepository;
//    private final ReceivingFactory receivingFactory;
    private final DemandeServiceClient demandeServiceClient;
    private final ParamService paramService;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final ParamServiceClient parametrageService;
    private final FactureBAService factureBAService;

    public ReceivingService(ReceivingRepository receivingRepository, DemandeServiceClient demandeServiceClient, ParamService paramService, ParamAchatServiceClient paramAchatServiceClient, ParamServiceClient parametrageService, @Lazy FactureBAService factureBAService) {
        this.receivingRepository = receivingRepository;
        this.demandeServiceClient = demandeServiceClient;
        this.paramService = paramService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.parametrageService = parametrageService;
        this.factureBAService = factureBAService;
    }

    /**
     * Save a receivingDTO.
     *
     * @param receivingDTO
     * @return the persisted entity
     */
    public ReceivingDTO save(ReceivingDTO receivingDTO) {
        log.debug("Request to save Receiving: {}", receivingDTO);
        if (receivingDTO.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            Preconditions.checkBusinessLogique(receivingDTO.getCommandeAchatList().size() == 1, "receiving-IMMO-commande-achat-list-not-valid");
        }
        //-------  fournisseur --------/
        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(receivingDTO.getFournisseur());
        Preconditions.checkBusinessLogique(!fournisseurDTO.getDesignation().equals("fournisseur.deleted"), "Fournisseur avec code : " + receivingDTO.getFournisseur() + " est introuvable");
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(fournisseurDTO.getAnnule()) && !(Boolean.TRUE.equals(fournisseurDTO.getStopped())), "fournisseur.stopped", fournisseurDTO.getCode());

        List<Integer> codeCommandeAchat = receivingDTO.getCommandeAchatList().stream().map(item -> item.getCode()).distinct().collect(Collectors.toList());
        String language = LocaleContextHolder.getLocale().getLanguage();
        Set<CommandeAchatDTO> listeCommandeAchat = demandeServiceClient.findListCommandeAchat(codeCommandeAchat, language);
        CommandeAchatDTO commandeAchat = listeCommandeAchat.stream().filter(item -> item.getUserArchive() != null).findFirst().orElse(null);
        Preconditions.checkBusinessLogique(commandeAchat == null, "receiving-with-archeived-purchase-order");
        if (("true").equalsIgnoreCase(automaticReceiving)) {
            log.debug("aaaaaaaaaaaaaaaaaaaaaaaaaa");
            receivingDTO.setNumbon(paramService.getcompteur(receivingDTO.getCategDepot(), TypeBonEnum.RC));
            receivingDTO.setDateValidateReceiving(LocalDateTime.now());
            receivingDTO.setUserValidateReceiving(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        List<DetailCommandeAchatDTO> detailCADto = listeCommandeAchat.stream().map(detail -> detail.getDetailCommandeAchatCollection()).flatMap(x -> x.stream()).collect(Collectors.toList());
//              Map<Integer, List<DetailCommandeAchatDTO>> listeDetailsCaGroupedBycodart = detailCADto
//                .stream()
//                .collect(Collectors.groupingBy(item -> item.getCodart()));

//        List<ReceivingDetailsDTO>receivingDetaislDto=new ArrayList<>();
        Set<Integer> listeCodeArticles = receivingDTO.getReceivingDetailsList().stream().map(elt -> elt.getCodart()).collect(Collectors.toSet());
        List<ArticleDTO> articles = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(receivingDTO.getCategDepot(), listeCodeArticles.toArray(new Integer[listeCodeArticles.size()]));
        articles.forEach(item -> {
            Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(item.getAnnule()) && !(Boolean.TRUE.equals(item.getStopped())), "item.stopped", item.getCodeSaisi());
        });
        for (ReceivingDetailsDTO detailReceivibgDto : receivingDTO.getReceivingDetailsList()) {
            log.debug("detail receiving *** {} *** {}", detailReceivibgDto.getQuantite(), detailReceivibgDto.getFree());
            if (detailReceivibgDto.getQuantite().compareTo(BigDecimal.ZERO) == 0 && Boolean.TRUE.equals(detailReceivibgDto.getFree())) {// bouton quantite gratuits  not from commande, quantite commande ==0
                //   detailReceivibgDto.setFree(Boolean.TRUE);
                detailReceivibgDto.setQuantityFromComannde(Boolean.FALSE);
                detailReceivibgDto.setRemise(BigDecimal.ZERO);
                detailReceivibgDto.setPrixUnitaire(BigDecimal.ZERO);
                //tva article gratuit selon commande si il ya meme article non gratuit ou tva de l article a linstant
                Optional<ReceivingDetailsDTO> matchingNotFreeItem = receivingDTO.getReceivingDetailsList()
                        .stream()
                        .filter(elt -> elt.getCodart().equals(detailReceivibgDto.getCodart()) && elt.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0)
                        .findFirst();
                if (matchingNotFreeItem.isPresent()) {
                    detailReceivibgDto.setTautva(matchingNotFreeItem.get().getTautva());
                    detailReceivibgDto.setCodtva(matchingNotFreeItem.get().getCodtva());
                } else {
                    ArticleDTO matchingItem = articles
                            .stream()
                            .filter(elt -> elt.getCode().equals(detailReceivibgDto.getCodart()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("stock.find-all.missing-article", new Throwable(detailReceivibgDto.getCodeSaisi())));
                    detailReceivibgDto.setTautva(matchingItem.getValeurTvaAch());
                    detailReceivibgDto.setCodtva(matchingItem.getCodeTvaAch());
                }
            } else {
                detailReceivibgDto.setQuantityFromComannde(Boolean.TRUE);
                DetailCommandeAchatDTO matchingDetailCommandeDTO = detailCADto.stream().filter(detail -> detail.getCodart().equals(detailReceivibgDto.getCodart()))
                        //                   .sorted(Comparator.comparing((DetailCommandeAchatDTO x) -> {
                        //                        return x.getCommandeAchat().getDateValidate();
                        //                    }).reversed())
                        .findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing.main.detail.receiving", new Throwable(detailReceivibgDto.getCodeSaisi())));
                if (Boolean.TRUE.equals(detailReceivibgDto.getFree())) {
                    detailReceivibgDto.setQuantiteGratuite(Integer.valueOf(detailReceivibgDto.getQuantiteReceiving().toString()));
                    detailReceivibgDto.setQuantiteRestante(detailReceivibgDto.getQuantiteReceiving());

                }
                detailReceivibgDto.setCodtva(matchingDetailCommandeDTO.getCodtva());
                detailReceivibgDto.setDelaiLivraison(matchingDetailCommandeDTO.getDelaiLivraison());
                detailReceivibgDto.setPrixUnitaire(matchingDetailCommandeDTO.getPrixUnitaire());
                detailReceivibgDto.setUnitDesignation(matchingDetailCommandeDTO.getUnitDesignation());
                detailReceivibgDto.setCodeUnite(matchingDetailCommandeDTO.getCodeUnite());
                detailReceivibgDto.setCodtva(matchingDetailCommandeDTO.getCodtva());
                detailReceivibgDto.setRemise(matchingDetailCommandeDTO.getRemise());
                detailReceivibgDto.setTautva(matchingDetailCommandeDTO.getTautva());
                detailReceivibgDto.setMemo(matchingDetailCommandeDTO.getMemo());
                detailReceivibgDto.setIsCapitalize(matchingDetailCommandeDTO.getIsCapitalize());
                detailReceivibgDto.setIsAppelOffre(matchingDetailCommandeDTO.getAppelOffre());
            }
        }

        Receiving receiving = ReceivingFactory.receivingDTOToReceiving(receivingDTO);
        if (receivingDTO.getCodeSite() != null) {
            SiteDTO codeSiteBranch = parametrageService.findSiteByCode(receivingDTO.getCodeSite());
            Preconditions.checkBusinessLogique(codeSiteBranch != null, "parametrage.clinique.error");
            receiving.setCodeSite(codeSiteBranch.getCode());
        }
        //in case automatic receiving , la validation du receiving est automatique et la quantite gratuite est provenante du PO
        if (automaticReceiving.equalsIgnoreCase("true")) {
            for (ReceivingDetails details : receiving.getReceivingDetailsList()) {
                details.setQuantiteValide(details.getQuantiteReceiving());
                details.setQuantityFromComannde(Boolean.TRUE);
            }
        }

        receiving.setNumbon(paramService.getcompteur(receiving.getCategDepot(), TypeBonEnum.RC));
        paramService.updateCompteurPharmacie(receiving.getCategDepot(), TypeBonEnum.RC);
        receiving = receivingRepository.save(receiving);
        ReceivingDTO resultDTO = ReceivingFactory.receivingToReceivingDTO(receiving);
        return resultDTO;
    }

    /**
     * Update a receivingDTO.
     *
     * @param receivingDTO
     * @return the updated entity
     */
    public ReceivingDTO update(ReceivingDTO receivingDTO) {
        log.debug("Request to update Receiving: {}", receivingDTO);
        Receiving inBase = receivingRepository.findOne(receivingDTO.getCode());
        Preconditions.checkFound(inBase, "receiving.NotFound");
        Receiving receiving = ReceivingFactory.receivingDTOToReceiving(receivingDTO);
        receivingDTO.setDateCreate(inBase.getDateCreate());
        receivingDTO.setUserCreate(inBase.getUserCreate());
        receiving = receivingRepository.save(receiving);
        ReceivingDTO resultDTO = ReceivingFactory.receivingToReceivingDTO(receiving);
        return resultDTO;
    }

    public ReceivingDTO validate(Integer id) {
        log.debug("Request to delete Receiving: {}", id);
        Receiving receiving = receivingRepository.findOne(id);
        Preconditions.checkFound(receiving, "receiving.NotFound");
        Preconditions.checkBusinessLogique(receiving.getUserValidate() == null, "receiving.Valide");
        Preconditions.checkBusinessLogique(receiving.getUserAnnule() == null, "receiving.Annule");
        LocalDateTime date = LocalDateTime.now();
        receiving.setDateValidate(date);
        receiving.setUserValidate(SecurityContextHolder.getContext().getAuthentication().getName());
        receiving = receivingRepository.save(receiving);
        return ReceivingFactory.receivingToReceivingDTO(receiving);
    }

    @Transactional(readOnly = true)
    public ReceivingDTO findOne(Integer id) {
        log.debug("Request to get Receiving: {}", id);
        Receiving receiving = receivingRepository.findOne(id);
        Preconditions.checkFound(receiving, "receiving.NotFound");

        ReceivingDTO receivingDTO = ReceivingFactory.receivingToReceivingDTO(receiving);
        if (receiving.getDateValidate() != null) {
            receivingDTO.setBonRecep(factureBAService.findBonReceptionByNumReceiving(id));
            receivingDTO.setNumAfficheBonRecep(receivingDTO.getBonRecep().getNumbon());
        }
        List<ReceivingDetailsDTO> receivingDetailsDTO = new ArrayList();
        receiving.getReceivingDetailsList().forEach(x -> {
            ReceivingDetailsDTO detail = receivingdetailsToReceivingDetailsDTO(x);
            if (receiving.getDateValidate() != null) {

                BigDecimal quantite = receivingDTO.getBonRecep().getDetails().stream()
                        .filter(y -> y.getRefArt().equals(detail.getCodart()) && y.getLotInter().equals(detail.getLotInter()) && y.getDatPer().toString().equals(detail.getDatePréemption().toString()))
                        .map(z -> z.getQuantite())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                detail.setQuantiteReceptionne(quantite);
            } else {
                detail.setQuantiteReceptionne(BigDecimal.ZERO);
            }
            receivingDetailsDTO.add(detail);
        });
        receivingDTO.setReceivingDetailsList(receivingDetailsDTO);

        FournisseurDTO fourniss = paramAchatServiceClient.findFournisseurByCode(receiving.getFournisseur());
        receivingDTO.setDesignationFournisseur(fourniss.getDesignation());
        receivingDTO.setFournisseurDTO(fourniss);
        if (receiving.getCodeSite() != null) {
            SiteDTO siteDTO = parametrageService.findSiteByCode(receiving.getCodeSite());
            Preconditions.checkBusinessLogique(siteDTO != null, "parametrage.clinique.error");
            receivingDTO.setDesignationSite(siteDTO.getDesignationAr());
        }
        List<Integer> codesCommande = (receiving.getReceivingCommandeList().stream().map(e -> e.getReceivingCommandePK().getCommandeParamAchat()).distinct()).collect(Collectors.toList());
        String language = LocaleContextHolder.getLocale().getLanguage();
        Set<CommandeAchatDTO> commandeAchatDTO = demandeServiceClient.findListCommandeAchat(codesCommande, language);
        List<String> numbonCommandeDTO = commandeAchatDTO.stream().map(z -> z.getNumbon()).collect(Collectors.toList());
        receivingDTO.setNumBonCommandes(numbonCommandeDTO);
        receivingDTO.setCommandeAchatList(commandeAchatDTO);
        return receivingDTO;
    }

    public List<ReceivingDetailsDTO> findDetailsBycodeReceiving(Integer code) {
        log.debug("Request to get All details receivings");
        Receiving receiving = receivingRepository.findOne(code);
        Preconditions.checkBusinessLogique(receiving!=null, "receiving.NotFound");
        log.debug("receiving {}",receiving);
       List<ReceivingDetails>  receivingDetails = receiving.getReceivingDetailsList();
        Preconditions.checkBusinessLogique(!receivingDetails.isEmpty(), "receiving.NotFound");
        Set<Integer> listeCodeArticles = receivingDetails.stream().map(elt -> elt.getCodart()).collect(Collectors.toSet());
        List<ArticlePHDTO> articlePHDTOSFromBranch = null;
        List<ArticleDTO> articlesDtos = null;
        if (contextReception.contains("company") && receivingDetails.get(0).getReceiving1().getCategDepot().equals(CategorieDepotEnum.PH)) {
            log.debug("context company and categ PH");
            Preconditions.checkBusinessLogique(receiving.getCodeSite() != null, "missing code site");
            String ipAdressSite = parametrageService.resolveIpAdresseSite(receiving.getCodeSite());
              log.debug("context company and categ PH");
            articlePHDTOSFromBranch = paramAchatServiceClient.findListeArticlePHByIdsAndSite(listeCodeArticles, ipAdressSite);
            Preconditions.checkBusinessLogique(articlePHDTOSFromBranch != null, "probleme-parametrage-achat-liste-article");
        } else {
            articlesDtos = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(receivingDetails.get(0).getReceiving1().getCategDepot(), listeCodeArticles.toArray(new Integer[listeCodeArticles.size()]));
        }
        List<ReceivingDetailsDTO> receivingDetailsDTO = ReceivingDetailsFactory.receivingdetailsToReceivingDetailsDTOs(receivingDetails);

            for(ReceivingDetailsDTO dto : receivingDetailsDTO  ){
            ArticleDTO matchingItemDTO = contextReception.contains("company") && receivingDetails.get(0).getReceiving1().getCategDepot().equals(CategorieDepotEnum.PH)
                    ? articlePHDTOSFromBranch.stream()
                            .filter(item -> item.getCode().equals(dto.getCodart())).findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(dto.getCodart().toString())))
                    : articlesDtos.stream()
                            .filter(item -> item.getCode().equals(dto.getCodart())).findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(dto.getCodart().toString())));

            dto.setCodeSaisi(matchingItemDTO.getCodeSaisi());
            dto.setPerissable(matchingItemDTO.getPerissable());
            dto.setUnitDesignation(matchingItemDTO.getDesignationUnite());
            dto.setCodeUnite(matchingItemDTO.getCodeUnite());
            if (matchingItemDTO.getCorporel() != null) {
                dto.setCorporel(matchingItemDTO.getCorporel());
            }
            dto.setDernierPrixAchat(matchingItemDTO.getPrixAchat());
            if (receivingDetails.get(0).getReceiving1().getCategDepot().equals(CategorieDepotEnum.PH)) {
                List<ArticleUniteDTO> articlePHunits = ((ArticlePHDTO) matchingItemDTO).getArticleUnites();

                BigDecimal prixVente = ((ArticlePHDTO) matchingItemDTO).getArticleUnites()
                        .stream()
                        .filter(au -> au.getNbPiece().compareTo(BigDecimal.ONE) == 0)
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(dto.getCodart().toString())))
                        .getPrixVente();

                dto.setPrixVente(prixVente);
            }
        }
       

        return receivingDetailsDTO;
    }

    /**
     * Get one receiving by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public Receiving findReceiving(Integer id
    ) {
        log.debug("Request to get Receiving: {}", id);
        Receiving receiving = receivingRepository.findOne(id);
        return receiving;
    }

    @Transactional(readOnly = true)
    public List<ReceivingDTO> findAll(CategorieDepotEnum categ, LocalDateTime fromDate,
            LocalDateTime toDate, Boolean imprime,
            Boolean hasMemo, Boolean deleted,
            Boolean valid, String codeFrs, Boolean validated, Integer codeSite
    ) {
        log.debug("Request to get All Receivings");
        QReceiving qReceiving = QReceiving.receiving;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(categ, () -> qReceiving.categDepot.eq(categ))
                .optionalAnd(fromDate, () -> qReceiving.dateCreate.goe(fromDate))
                .optionalAnd(toDate, () -> qReceiving.dateCreate.loe(toDate))
                .optionalAnd(codeFrs, () -> qReceiving.fournisseur.eq(codeFrs))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> qReceiving.userAnnule.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> qReceiving.userAnnule.isNull())
                .booleanAnd(Objects.equals(valid, Boolean.TRUE), () -> qReceiving.userValidate.isNotNull())
                .booleanAnd(Objects.equals(valid, Boolean.FALSE), () -> qReceiving.userValidate.isNull())
                .optionalAnd(imprime, () -> qReceiving.imprime.eq(imprime))
                .booleanAnd(Objects.equals(hasMemo, Boolean.TRUE), () -> qReceiving.memo.isNotNull())
                .booleanAnd(Objects.equals(hasMemo, Boolean.FALSE), () -> qReceiving.memo.isNull())
                .booleanAnd(Objects.equals(validated, Boolean.TRUE), () -> qReceiving.userValidateReceiving.isNotNull())
                .booleanAnd(Objects.equals(validated, Boolean.FALSE), () -> qReceiving.userValidateReceiving.isNull())
                .optionalAnd(codeSite, () -> qReceiving.codeSite.eq(codeSite));

        List<Receiving> listeReceivings = (List<Receiving>) new ArrayList(receivingRepository.findAll(builder));
        List<Integer> codesCommandes = new ArrayList<>();
        List<String> codesfournisseur = new ArrayList<>();
        List<Integer> ListeNumReceiving = new ArrayList();
        listeReceivings.forEach(x -> {

            /*extract numreceving */
            ListeNumReceiving.add(x.getCode());
            codesfournisseur.add(x.getFournisseur());
            x.getReceivingCommandeList().forEach(y -> {
                codesCommandes.add(y.getReceivingCommandePK().getCommandeParamAchat());
            });
        });
        String language = LocaleContextHolder.getLocale().getLanguage();
        Set<CommandeAchatDTO> commandeAchatDTO = demandeServiceClient.findListCommandeAchat(codesCommandes, language);
        List<FournisseurDTO> fournisseurDTOs = paramAchatServiceClient.findFournisseurByListCode(codesfournisseur);
        /*find receptions by numbReceiving ( dont use any entity graph and it must return a list of BonRecepDTO) */
        List<BonRecepDTO> BonRecptionDtos = factureBAService.findBonReceptionByNumReceivingIn(ListeNumReceiving);
        List<ReceivingDTO> resultDTO = ReceivingFactory.receivingToReceivingDTOs(listeReceivings);

        Collection<SiteDTO> listeSiteDTOs = contextReception.contains("company") ? parametrageService.findAllSites(Boolean.TRUE, "B") : new ArrayList();
        log.debug("listeSiteDTOs est {}", listeSiteDTOs);
        Preconditions.checkBusinessLogique(listeSiteDTOs != null, "parametrage.clinique.error");
        resultDTO.forEach(
                (receivingDTO) -> {
                    Optional<FournisseurDTO> fourniss = fournisseurDTOs.stream().filter(x -> x.getCode().equalsIgnoreCase(receivingDTO.getFournisseur())).findFirst();
                    if (fourniss.isPresent()) {
                        receivingDTO.setDesignationFournisseur(fourniss.get().getDesignation());
                        receivingDTO.setFournisseurDTO(fourniss.get());
//                        log.debug("fournisseur est{}", receivingDTO.getFournisseurDTO());
                    }
                    List<String> numbonCommandeDTO = commandeAchatDTO.stream().filter(x -> receivingDTO.getCodesCommandeAchat().contains(x.getCode())).map(z -> z.getNumbon()).collect(Collectors.toList());
                    receivingDTO.setNumBonCommandes(numbonCommandeDTO);
                    List<CommandeAchatDTO> commandeDTO = commandeAchatDTO.stream().filter(x -> receivingDTO.getCodesCommandeAchat().contains(x.getCode())).collect(Collectors.toList());
                    receivingDTO.setCommandeAchatList(new HashSet(commandeDTO));
                    /*find the matched reception and set it to the list */
//                    log.debug(" receivingDTO {}", receivingDTO.getCode(matchedBonReceptionDto));
                    if (receivingDTO.getDateValidate() != null) {
                        BonRecepDTO matchedBonReceptionDto = BonRecptionDtos.stream().filter(x -> x.getReceivingCode().equals(receivingDTO.getCode()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-reception", new Throwable(receivingDTO.getNumbon())));

                        receivingDTO.setBonRecep(matchedBonReceptionDto);
                        receivingDTO.setNumAfficheBonRecep(receivingDTO.getBonRecep().getNumaffiche());
                    }
                    if (receivingDTO.getCodeSite() != null) {
                        SiteDTO matchingSiteDTO = listeSiteDTOs
                                .stream()
                                .filter(elt -> elt.getCode().equals(receivingDTO.getCodeSite())).findFirst()
                                .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-site", new Throwable(receivingDTO.getCodeSite().toString())));
                        receivingDTO.setDesignationSite(matchingSiteDTO.getDesignationAr());
                    }
                }
        );

        return resultDTO;
    }

    /**
     * Get all the receivings.
     *
     * @param codes
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<ReceivingDTO> findByCodeIn(List<Integer> codes) {
        log.debug("Request to get All Receivings");

        List<Receiving> result = (List<Receiving>) receivingRepository.findByCodeIn(codes);
        List<Integer> codesCommandes = new ArrayList<>();
        result.forEach(x -> {
            x.getReceivingCommandeList().forEach(y -> {
                codesCommandes.add(y.getReceivingCommandePK().getCommandeParamAchat());
            });
        });
        List<String> codesfournisseur = new ArrayList<>();
        result.forEach(x -> {
            codesfournisseur.add(x.getFournisseur());
        });
        String language = LocaleContextHolder.getLocale().getLanguage();
        Set<CommandeAchatDTO> commandeAchatDTO = demandeServiceClient.findListCommandeAchat(codesCommandes, language);
        List<FournisseurDTO> fournisseurDTOs = paramAchatServiceClient.findFournisseurByListCode(codesfournisseur);
        List<ReceivingDTO> resultDTO = ReceivingFactory.receivingToReceivingDTOs(result);
        resultDTO.forEach((receivingDTO) -> {
            Optional<FournisseurDTO> fourniss = fournisseurDTOs.stream().filter(x -> x.getCode().equals(receivingDTO.getFournisseur())).findFirst();
            if (fourniss.isPresent()) {
                receivingDTO.setDesignationFournisseur(fourniss.get().getDesignation());
                receivingDTO.setFournisseurDTO(fourniss.get());
            }
            List<String> numbonCommandeDTO = commandeAchatDTO.stream().filter(x -> receivingDTO.getCodesCommandeAchat().contains(x.getCode())).map(z -> z.getNumbon()).collect(Collectors.toList());
            receivingDTO.setNumBonCommandes(numbonCommandeDTO);
            List<CommandeAchatDTO> commandeDTO = commandeAchatDTO.stream().filter(x -> receivingDTO.getCodesCommandeAchat().contains(x.getCode())).collect(Collectors.toList());
            receivingDTO.setCommandeAchatList(new HashSet(commandeDTO));
        });
        return resultDTO;
    }

    /**
     * Delete receiving by id.
     *
     * @param id the id of the entity
     * @return
     */
    public ReceivingDTO delete(Integer id) {
        log.debug("Request to delete Receiving: {}", id);
        Receiving receiving = receivingRepository.findOne(id);
        Preconditions.checkFound(receiving, "receiving.NotFound");
        Preconditions.checkBusinessLogique(receiving.getUserValidate() == null, "receiving.Valide");
        Preconditions.checkBusinessLogique(receiving.getUserAnnule() == null, "receiving.Annule");
        LocalDateTime date = LocalDateTime.now();
        receiving.setDateAnnule(date);
        receiving.setUserAnnule(SecurityContextHolder.getContext().getAuthentication().getName());
        receiving = receivingRepository.save(receiving);
        return ReceivingFactory.receivingToReceivingDTO(receiving);
    }

    /**
     * Delete receiving by id.
     *
     * @param id the id of the entity
     * @param memo
     * @return
     */
    public ReceivingDTO addMemo(Integer id, String memo) {
        log.debug("Request to add memo to Receiving: {}", id);
        Receiving receiving = receivingRepository.findOne(id);
        Preconditions.checkFound(receiving, "receiving.NotFound");
        Preconditions.checkBusinessLogique(receiving.getUserValidate() == null, "receiving.Valide");
        Preconditions.checkBusinessLogique(receiving.getUserAnnule() == null, "receiving.Annule");
        receiving.setMemo(memo);
        LocalDateTime date = LocalDateTime.now();
        receiving.setDateMemo(date);
        receiving.setUserMemo(SecurityContextHolder.getContext().getAuthentication().getName());
        receiving = receivingRepository.save(receiving);
        return ReceivingFactory.receivingToReceivingDTO(receiving);
    }

    /**
     * Edition receiving by id.
     *
     * @param id the id of the entity
     * @return
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public byte[] edition(Integer id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print Fournisseurs : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        Receiving receiving = receivingRepository.findOne(id);
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (receiving.getDateCreate().isBefore(LocalDateTime.parse("2021-12-15T16:32:56.000"))) {
            return runOldReceivingEdition(id);
        } else {
            if (receiving.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
//            reportClientDoc.open("Reports/Bon_Receiving_IMMO" + local + ".rpt", 0);
//        } else if (receiving.getCategDepot().equals(CategorieDepotEnum.IMMO) && receiving.getDateValidate() != null) {
                reportClientDoc.open("Reports/Bon_Receiving_Valide_IMMO" + local + ".rpt", 0);
            } //        else if (receiving.getDateValidate() != null) {
            //            reportClientDoc.open("Reports/Bon_Receiving_Valide" + local + ".rpt", 0);
            //        } 
            else {
                reportClientDoc.open("Reports/Bon_Receiving_Valide" + local + ".rpt", 0);
            }

            Preconditions.checkFound(receiving, "receiving.NotFound");
            ReceivingEditionDTO receivingDTO = ReceivingFactory.receivingToReceivingEditionDTO(receiving);
            receivingDTO.setDesignationSite("");
            if (receiving.getCodeSite() != null) {
                SiteDTO codeSiteBranch = parametrageService.findSiteByCode(receiving.getCodeSite());
                Preconditions.checkBusinessLogique(codeSiteBranch != null, "parametrage.clinique.error");
                receivingDTO.setDesignationSite(codeSiteBranch.getDesignation());
            }
            if (receiving.getDateValidate() != null) {
                receivingDTO.setBonRecep(factureBAService.findBonReceptionByNumReceiving(id));
                receivingDTO.setNumAfficheBonRecep(receivingDTO.getBonRecep().getNumbon());
            }

            List<ReceivingDetailsDTO> receivingDetailsDTO = new ArrayList();

            for (ReceivingDetails x : receiving.getReceivingDetailsList()) {
                List<ReceivingDetailsDTO> listeDetailsReceptionnes = new ArrayList();
                if (receiving.getDateValidate() != null) {
                    if (!receivingDTO.getCategDepot().categ().equals("IMMO")) {
                        listeDetailsReceptionnes = receivingDTO.getBonRecep().getDetails().stream()
                                .filter(y -> y.getRefArt().equals(x.getCodart()) && y.getLotInter().equals(x.getLotInter()) && y.getFree().equals(x.getIsFree()) && y.getDatPer().toString().equals(x.getDatePer().toString()))
                                .map(d -> {
                                    ReceivingDetailsDTO detail = receivingdetailsToReceivingDetailsDTO(x);
                                    detail.setDatPer(java.util.Date.from(d.getDatPer().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                                    detail.setLotInter(d.getLotInter());
                                    detail.setQuantiteReceptionne(d.getQuantite());
                                    detail.setCodeEmplacement(d.getCodeEmplacement());
                                    detail.setDesignationEmplacement(d.getDesignationEmplacement());

                                    return detail;
                                }
                                )
                                .collect(Collectors.toList());
                    } else {

                        listeDetailsReceptionnes = receivingDTO.getBonRecep().getDetails().stream()
                                .filter(y -> y.getRefArt().equals(x.getCodart()) && y.getFree().equals(x.getIsFree()))
                                .map(d -> {
                                    ReceivingDetailsDTO detail = receivingdetailsToReceivingDetailsDTO(x);
                                    detail.setDatPer(java.util.Date.from(d.getDatPer().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                                    detail.setLotInter(d.getLotInter());
                                    detail.setQuantiteReceptionne(d.getQuantite());
                                    detail.setCodeEmplacement(d.getCodeEmplacement());
                                    detail.setDesignationEmplacement(d.getDesignationEmplacement());
                                    detail.setFree(d.getFree());

                                    return detail;
                                }
                                ).collect(Collectors.toList());
                    }
                }
                if (listeDetailsReceptionnes.isEmpty() || receiving.getDateValidate() == null) {
                    ReceivingDetailsDTO detail = receivingdetailsToReceivingDetailsDTO(x);
                    if (!receiving.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
                        detail.setDatPer(java.util.Date.from(detail.getDatePréemption().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                    }
                    detail.setQuantiteReceptionne(BigDecimal.ZERO);
//                if (receiving.getUserValidateReceiving() == null) {
//                    detail.setQuantiteReceivingValide(BigDecimal.ZERO);
//                }
                    receivingDetailsDTO.add(detail);
                } else {
                    receivingDetailsDTO.addAll(listeDetailsReceptionnes);
                }

            }

            receivingDTO.setReceivingDetailsList(receivingDetailsDTO);

            FournisseurDTO fourniss = paramAchatServiceClient.findFournisseurByCode(receiving.getFournisseur());
            receivingDTO.setDesignationFournisseur(fourniss.getDesignation());
            receivingDTO.setFournisseurDTO(fourniss);
            List<Integer> codesCommande = (receiving.getReceivingCommandeList().stream().map(e -> e.getReceivingCommandePK().getCommandeParamAchat()).distinct()).collect(Collectors.toList());
            Set<CommandeAchatDTO> commandeAchatDTO = demandeServiceClient.findListCommandeAchat(codesCommande, language);
            List<String> numbonCommandeDTO = commandeAchatDTO.stream().map(z -> z.getNumbon()).collect(Collectors.toList());
            receivingDTO.setNumBonCommandes(numbonCommandeDTO);
            receivingDTO.setCommandeAchatList(commandeAchatDTO);
            reportClientDoc
                    .getDatabaseController().setDataSource(java.util.Arrays.asList(receivingDTO), ReceivingEditionDTO.class,
                            "BonReceiving", "BonReceiving");
            reportClientDoc
                    .getDatabaseController().setDataSource(java.util.Arrays.asList(receivingDTO.getFournisseurDTO()), FournisseurDTO.class,
                            "Fournisseur", "Fournisseur");
            reportClientDoc
                    .getDatabaseController().setDataSource(receivingDTO.getReceivingDetailsList(), ReceivingDetailsDTO.class,
                            "receivingDetailsList", "receivingDetailsList");
            reportClientDoc
                    .getSubreportController().getSubreport("ListeBonCommandes").getDatabaseController().setDataSource(receivingDTO.getCommandeAchatList(), CommandeAchatDTO.class,
                    "ListeBonCommandes", "ListeBonCommandes");
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
            ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            paramController.setCurrentValue("", "user", user);
            CommandeAchatDTO lastPurchaseOrder = receivingDTO.getCommandeAchatList().stream().findFirst().get();
            paramController.setCurrentValue("", "dateDebutExenoration", lastPurchaseOrder.getDateDebutExenoration());
            paramController.setCurrentValue("", "dateFinExenoration", lastPurchaseOrder.getDateFinExenoration());
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            LocalDateTime date = LocalDateTime.now();
            receiving.setDateImprime(date);
            receiving.setUserImprime(SecurityContextHolder.getContext().getAuthentication().getName());
            receivingRepository.save(receiving);
            return Helper.read(byteArrayInputStream);
        }
    }

    @Transactional(readOnly = true)//to optimize and merge with methode findall
    public List<ReceivingDTO> findValidatedReceiving(CategorieDepotEnum categ, LocalDateTime fromDate,
            LocalDateTime toDate, Boolean deleted, Boolean valid
    ) {
        log.debug("Request to get All Receivings");
        QReceiving qReceiving = QReceiving.receiving;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(categ, () -> qReceiving.categDepot.eq(categ))
                .optionalAnd(fromDate, () -> qReceiving.dateCreate.goe(fromDate))
                .optionalAnd(toDate, () -> qReceiving.dateCreate.loe(toDate))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> qReceiving.userAnnule.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> qReceiving.userAnnule.isNull())
                .booleanAnd(Objects.equals(valid, Boolean.TRUE), () -> qReceiving.userValidate.isNotNull())
                .booleanAnd(Objects.equals(valid, Boolean.FALSE), () -> qReceiving.userValidate.isNull());
        List<Receiving> result = (List<Receiving>) new ArrayList(receivingRepository.findAll(builder));
        List<ReceivingDTO> loisteReceivingDTOs = ReceivingFactory.receivingToReceivingDTOEager(result);
        List<Integer> codesReceivings = loisteReceivingDTOs.stream().map(x -> x.getCode()).collect(Collectors.toList());

        List<BonRecepDTO> matchingReceptions = factureBAService.findBonReceptionByNumReceivingIn(codesReceivings);
        for (ReceivingDTO receivingDto : loisteReceivingDTOs) {
            receivingDto.setBonRecep(matchingReceptions.stream().filter(x -> x.getReceivingCode().equals(receivingDto.getCode())).findFirst().orElse(null));
            List<ReceivingDetailsDTO> detailsReceiving = receivingDto.getReceivingDetailsList();
            if (receivingDto.getBonRecep() != null) {
                detailsReceiving.forEach(x -> {
                    BigDecimal quantiterReceiving = detailsReceiving.stream()
                            .filter(y -> y.getCodart().equals(x.getCodart()))
                            .map(z -> z.getQuantiteReceiving())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal quantite = receivingDto.getBonRecep().getDetails().stream()
                            .filter(y -> y.getRefArt().equals(x.getCodart()))
                            .map(z -> z.getQuantite())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    x.setQuantiteReceptionne(quantite);
                    x.setQuantite(quantite);

                });
            } else {
                detailsReceiving.forEach(x -> {
                    BigDecimal quantiterReceiving = detailsReceiving.stream()
                            .filter(y -> y.getCodart().equals(x.getCodart()))
                            .map(z -> z.getQuantiteReceiving())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    x.setQuantiteReceptionne(BigDecimal.ZERO);
                    x.setQuantite(quantiterReceiving);

                });
            }
            List<ReceivingDetailsDTO> allFiltredDetails = detailsReceiving.stream().collect(Collectors.groupingBy(x -> x.getCodart())).values().stream().map(l -> (l.get(0))).collect(Collectors.toList());
            receivingDto.setReceivingDetailsList(allFiltredDetails);

        }

        List<Integer> codesCommandes = new ArrayList<>();
        result.forEach(x -> {

            x.getReceivingCommandeList().forEach(y -> {
                codesCommandes.add(y.getReceivingCommandePK().getCommandeParamAchat());
            });
        });
        String language = LocaleContextHolder.getLocale().getLanguage();
        Set<CommandeAchatDTO> commandeAchatDTO = demandeServiceClient.findListCommandeAchat(codesCommandes, language);
        loisteReceivingDTOs.forEach(
                (receivingDTO) -> {
                    List<String> numbonCommandeDTO = commandeAchatDTO.stream().filter(x -> receivingDTO.getCodesCommandeAchat().contains(x.getCode())).map(z -> z.getNumbon()).collect(Collectors.toList());
                    //attribut numcommandes is added for edition report 
                    String numCommandes = String.join(";", numbonCommandeDTO);
                    receivingDTO.setNumBonCommandes(numbonCommandeDTO);
                    receivingDTO.setNumeroBonCommandes(numCommandes);
                }
        );

        return loisteReceivingDTOs;
    }

    public List<ReceivingDetails> validateReceiving(ReceivingDTO receivingDTO) {
        log.debug("Request to save Receiving: {}", receivingDTO);
        Receiving receivingInBase = receivingRepository.findByNumaffiche(receivingDTO.getNumaffiche());
        Preconditions.checkBusinessLogique(receivingInBase != null, "receiving-not-valid");
        Preconditions.checkBusinessLogique(receivingInBase.getDateAnnule() == null, "reception.add.canceld-receiving");
        Preconditions.checkBusinessLogique(receivingInBase.getDateValidateReceiving() == null, "reception.add.validated-receiving");

        //-------  fournisseur --------/
        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(receivingInBase.getFournisseur());
        Preconditions.checkBusinessLogique(!fournisseurDTO.getDesignation().equals("fournisseur.deleted"), "Fournisseur avec code : " + receivingDTO.getFournisseur() + " est introuvable");
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(fournisseurDTO.getAnnule()) && !(Boolean.TRUE.equals(fournisseurDTO.getStopped())), "fournisseur.stopped", fournisseurDTO.getCode());

        List<ReceivingDetails> detailReceivingvalidated = receivingInBase.getReceivingDetailsList().stream().map(detailReceiving -> {
            log.debug("************ {}", detailReceiving);
            log.debug("*********code*** {}", detailReceiving.getCodart());
            log.debug("*********code*** {}", detailReceiving.getLotInter());
            ReceivingDetailsDTO matchingdetailDto;
            if (receivingDTO.getCategDepot().categ().equals("IMMO")) {
                matchingdetailDto = receivingDTO.getReceivingDetailsList().stream()
                        //                        .peek(x -> log.debug("detailsreceiving  {}**** {}", x.getLotInter()))
                        //                        .peek(x -> log.debug("detailsreceiving2  {}**** {}", x.getCodart().equals(detail.getCodart()) == true))
                        .filter(detailReceivingDTO -> detailReceivingDTO.getCodart().equals(detailReceiving.getCodart()) && detailReceivingDTO.getFree().equals(detailReceiving.getIsFree()) ).findFirst().orElse(null);
                log.debug("************ matchinhdetail {}", matchingdetailDto.getQuantiteReceivingValide());
            } else {
                matchingdetailDto = receivingDTO.getReceivingDetailsList().stream()
                        //                        .peek(x -> log.debug("detailsreceiving  {}**** {}", x.getLotInter()))
                        //                        .peek(x -> log.debug("detailsreceiving2  {}**** {}", x.getCodart().equals(detail.getCodart())))
                        //                        .peek(x -> log.debug("detailsreceiving2  {}**** {}", x.isFree() == detail.getIsFree()))
                        //                        .peek(x -> log.debug("detailsreceiving2  {}**** {}", detail.getDatePer().toString()))
                        .filter(x -> x.getCodart().equals(detailReceiving.getCodart()) && x.getLotInter().equals(detailReceiving.getLotInter()) && x.getFree().equals(detailReceiving.getIsFree())&& x.getDatePréemption().toString().equals(detailReceiving.getDatePer().toString())).findFirst().orElse(null);
            }
            log.debug("************ matchinhdetail {}", matchingdetailDto.getQuantiteReceivingValide());
            Preconditions.checkBusinessLogique(matchingdetailDto.getQuantiteReceivingValide() != null, "receiving-not-valid");
            detailReceiving.setQuantiteValide(matchingdetailDto.getQuantiteReceivingValide());
            log.debug("************ matchinhdetail {}", detailReceiving.getQuantiteValide());
            return detailReceiving;
        }).collect(Collectors.toList());
        receivingInBase.setDateValidateReceiving(LocalDateTime.now());
        receivingInBase.setUserValidateReceiving(SecurityContextHolder.getContext().getAuthentication().getName());
        return detailReceivingvalidated;
    }

    public byte[] runOldReceivingEdition(Integer id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print Fournisseurs : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        Receiving receiving = receivingRepository.findOne(id);
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();

        if (receiving.getUserValidate() == null && receiving.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            reportClientDoc.open("Reports/Bon_Receiving_IMMO" + local + ".rpt", 0);
        } else if (receiving.getCategDepot().equals(CategorieDepotEnum.IMMO) && receiving.getDateValidate() != null) {
            reportClientDoc.open("Reports/Old_Bon_Receiving_Valide_IMMO" + local + ".rpt", 0);
        } else if (receiving.getDateValidate() != null) {
            reportClientDoc.open("Reports/Old_Bon_Receiving_Valide" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/Bon_Receiving" + local + ".rpt", 0);
        }

        Preconditions.checkFound(receiving, "receiving.NotFound");
        ReceivingEditionDTO receivingDTO = ReceivingFactory.receivingToReceivingEditionDTO(receiving);

        if (receiving.getDateValidate() != null) {
            receivingDTO.setBonRecep(factureBAService.findBonReceptionByNumReceiving(id));
            receivingDTO.setNumAfficheBonRecep(receivingDTO.getBonRecep().getNumbon());
        }

        List<ReceivingDetailsDTO> receivingDetailsDTO = new ArrayList();

        for (ReceivingDetails x : receiving.getReceivingDetailsList()) {
            List<ReceivingDetailsDTO> listeDetailsReceptionnes = new ArrayList();
            if (receiving.getDateValidate() != null) {

                listeDetailsReceptionnes = receivingDTO.getBonRecep().getDetails().stream()
                        .filter(y -> y.getRefArt().equals(x.getCodart()))
                        .map(d -> {
                            ReceivingDetailsDTO detail = receivingdetailsToReceivingDetailsDTO(x);
                            detail.setDatPer(java.util.Date.from(d.getDatPer().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                            detail.setLotInter(d.getLotInter());
                            detail.setQuantiteReceptionne(d.getQuantite());
                            detail.setCodeEmplacement(d.getCodeEmplacement());
                            detail.setDesignationEmplacement(d.getDesignationEmplacement());

                            return detail;
                        }
                        )
                        .collect(Collectors.toList());
            }
            if (listeDetailsReceptionnes.isEmpty() || receiving.getDateValidate() == null) {
                ReceivingDetailsDTO detail = receivingdetailsToReceivingDetailsDTO(x);
                detail.setQuantiteReceptionne(BigDecimal.ZERO);
                receivingDetailsDTO.add(detail);
            } else {
                receivingDetailsDTO.addAll(listeDetailsReceptionnes);
            }

        }

        receivingDTO.setReceivingDetailsList(receivingDetailsDTO);

        FournisseurDTO fourniss = paramAchatServiceClient.findFournisseurByCode(receiving.getFournisseur());
        receivingDTO.setDesignationFournisseur(fourniss.getDesignation());
        receivingDTO.setFournisseurDTO(fourniss);
        List<Integer> codesCommande = (receiving.getReceivingCommandeList().stream().map(e -> e.getReceivingCommandePK().getCommandeParamAchat()).distinct()).collect(Collectors.toList());
        Set<CommandeAchatDTO> commandeAchatDTO = demandeServiceClient.findListCommandeAchat(codesCommande, language);
        List<String> numbonCommandeDTO = commandeAchatDTO.stream().map(z -> z.getNumbon()).collect(Collectors.toList());
        receivingDTO.setNumBonCommandes(numbonCommandeDTO);
        receivingDTO.setCommandeAchatList(commandeAchatDTO);
        reportClientDoc
                .getDatabaseController().setDataSource(java.util.Arrays.asList(receivingDTO), ReceivingEditionDTO.class,
                        "BonReceiving", "BonReceiving");
        reportClientDoc
                .getDatabaseController().setDataSource(java.util.Arrays.asList(receivingDTO.getFournisseurDTO()), FournisseurDTO.class,
                        "Fournisseur", "Fournisseur");
        reportClientDoc
                .getDatabaseController().setDataSource(receivingDTO.getReceivingDetailsList(), ReceivingDetailsDTO.class,
                        "receivingDetailsList", "receivingDetailsList");
        reportClientDoc
                .getSubreportController().getSubreport("ListeBonCommandes").getDatabaseController().setDataSource(receivingDTO.getCommandeAchatList(), CommandeAchatDTO.class,
                "ListeBonCommandes", "ListeBonCommandes");
        reportClientDoc
                .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                "clinique", "clinique");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        CommandeAchatDTO lastPurchaseOrder = receivingDTO.getCommandeAchatList().stream().findFirst().get();
        paramController.setCurrentValue("", "dateDebutExenoration", lastPurchaseOrder.getDateDebutExenoration());
        paramController.setCurrentValue("", "dateFinExenoration", lastPurchaseOrder.getDateFinExenoration());

        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        LocalDateTime date = LocalDateTime.now();
        receiving.setDateImprime(date);
        receiving.setUserImprime(SecurityContextHolder.getContext().getAuthentication().getName());
        receivingRepository.save(receiving);
        return Helper.read(byteArrayInputStream);
    }

}
