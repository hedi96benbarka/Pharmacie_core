package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.FactureBonReception;
import com.csys.pharmacie.achat.domain.FcptfrsPH;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.QFactureBonReception;
import com.csys.pharmacie.achat.dto.BaseTvaFactureBonReceptionDTO;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
import com.csys.pharmacie.achat.dto.FactureBonReceptionDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import com.csys.pharmacie.achat.factory.FactureBAFactory;
import com.csys.pharmacie.achat.factory.FactureBonReceptionFactory;
import com.csys.pharmacie.achat.factory.MvtstoBAFactory;
import com.csys.pharmacie.achat.repository.FactureBonReceptionRepository;
import com.csys.pharmacie.achat.repository.FcptFrsPHRepository;
import com.csys.pharmacie.client.dto.DeviseDTO;
import com.csys.pharmacie.client.service.CaisseServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Convert;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.util.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing FactureBonReception.
 */
@Service
@Transactional
public class FactureBonReceptionService {

    private final Logger log = LoggerFactory.getLogger(FactureBonReceptionService.class);

    private final FactureBonReceptionRepository facturebonreceptionRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final ParamService paramService;
    private final FactureBAService factureBAService;
    private final FcptFrsPHService fcptFrsPHService;
//    private final FactureBAFactory factureBAFactory;
    private final DemandeServiceClient demandeServiceClient;
    private final MvtStoBAService mvtStoBAService;
    private final ParamServiceClient parametrageService;
    private final FcptFrsPHRepository fcptFrsPHRepository;
    private final CaisseServiceClient caisseServiceClient;

    public FactureBonReceptionService(FactureBonReceptionRepository facturebonreceptionRepository, ParamAchatServiceClient paramAchatServiceClient, ParamService paramService, @Lazy FactureBAService factureBAService, FcptFrsPHService fcptFrsPHService, DemandeServiceClient demandeServiceClient, MvtStoBAService mvtStoBAService, ParamServiceClient parametrageService, FcptFrsPHRepository fcptFrsPHRepository, CaisseServiceClient caisseServiceClient) {
        this.facturebonreceptionRepository = facturebonreceptionRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.paramService = paramService;
        this.factureBAService = factureBAService;
        this.fcptFrsPHService = fcptFrsPHService;
        this.demandeServiceClient = demandeServiceClient;
        this.mvtStoBAService = mvtStoBAService;
        this.parametrageService = parametrageService;
        this.fcptFrsPHRepository = fcptFrsPHRepository;
        this.caisseServiceClient = caisseServiceClient;
    }

    /**
     * Save a facturebonreceptionDTO
     *
     * @param factureBonReceptionDTO
     * @return the persisted entity
     */
    public FactureBonReceptionDTO save(FactureBonReceptionDTO factureBonReceptionDTO) {
        log.debug("Request to save FactureBonReception: {}", factureBonReceptionDTO);

        FactureBonReception factureBonReception = FactureBonReceptionFactory.facturebonreceptionDTOToFactureBonReception(factureBonReceptionDTO);
        FactureBonReception factureWithSameRef = facturebonreceptionRepository.findByReferenceFournisseur(factureBonReception.getReferenceFournisseur());
        com.csys.util.Preconditions.checkBusinessLogique(factureWithSameRef == null, "reffrs-fournisseur-exists");

//        FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(factureBonReceptionDTO.getCodeFournisseur());
//        Preconditions.checkBusinessLogique(fournisseur != null, "missing-supplier", factureBonReceptionDTO.getCodeFournisseur());
//        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(fournisseur.getAnnule()) && !(Boolean.TRUE.equals(fournisseur.getStopped())), "fournisseur.stopped", fournisseur.getCode());
        DeviseDTO deviseDTO = caisseServiceClient.findDeviseById(factureBonReceptionDTO.getCodeDevise());
        Preconditions.checkBusinessLogique(deviseDTO != null, "missing-devise", factureBonReceptionDTO.getCodeDevise());
        factureBonReception.setTypbon(TypeBonEnum.FBR);
        String numbon = paramService.getcompteur(factureBonReceptionDTO.getCategDepot(), TypeBonEnum.FBR);
        factureBonReception.setNumbon(numbon);
        factureBonReception.setIntegrer(false);
        paramService.updateCompteurPharmacie(factureBonReceptionDTO.getCategDepot(), TypeBonEnum.FBR);
        factureBonReception = facturebonreceptionRepository.saveAndFlush(factureBonReception);
        Set<FactureBA> listeBonsReception = (Set<FactureBA>) factureBAService.findFactureBAByNumBonIn(factureBonReceptionDTO.getBonReceptionCollection().stream().map(BonRecepDTO::getNumbon).collect(toList()));
        List<Integer> listeDesDelai = new ArrayList();
        List<LocalDateTime> listeDatBons = new ArrayList();
        for (FactureBA bonReception : listeBonsReception) {
            if (bonReception.getFactureBonReception() != null) {
                log.debug("le bon de reception {} et sa facture BonReception {} est : ", bonReception.getNumbon(), bonReception.getFactureBonReception().getNumbon());
            }
            Preconditions.checkBusinessLogique(bonReception.getFactureBonReception() == null, "facture-bon-reception.invoiced-reception", bonReception.getNumbon());
            bonReception.setFactureBonReception(factureBonReception);
            listeDesDelai.add(bonReception.getMaxDelaiPaiement());
            listeDatBons.add(bonReception.getDatbon());
        }
        Integer maxDelaiPaiementBons = listeDesDelai.stream().max(Integer::compare).get();
        log.debug("maxDelaiPaiementBons est {}", maxDelaiPaiementBons);
        LocalDateTime maxDate = listeDatBons.stream().max(LocalDateTime::compareTo).get();
        log.debug("maxDate est {}", maxDate);
        Period diff = Period.between(maxDate.toLocalDate(), factureBonReceptionDTO.getDatBon().toLocalDate());
        log.debug("max delai facture bon reception est {}", diff.getDays());
        factureBonReception.setMaxDelaiPaiement(maxDelaiPaiementBons - diff.getDays());
        factureBonReception.setBonReceptionCollection(listeBonsReception);
        factureBonReception.calcul(factureBonReception);

        fcptFrsPHService.addFcptFrsOnFactureBonReception(factureBonReception);
        FactureBonReceptionDTO resultDTO = FactureBonReceptionFactory.facturebonreceptionToFactureBonReceptionDTO(factureBonReception);
        return resultDTO;
    }

    /**
     * Update a facturebonreceptionDTO.
     *
     * @param factureBonReceptionDTO
     * @return the updated entity
     */
    public FactureBonReceptionDTO update(FactureBonReceptionDTO factureBonReceptionDTO) {
        log.debug("Request to update FactureBonReception: {}", factureBonReceptionDTO);
        FactureBonReception inBase = facturebonreceptionRepository.findOne(factureBonReceptionDTO.getNumbon());
        Preconditions.checkBusinessLogique(inBase != null, "facturebonreception.NotFound");

        FactureBonReception factureBonReception = FactureBonReceptionFactory.facturebonreceptionDTOToFactureBonReception(factureBonReceptionDTO);
        FactureBonReception factureWithSameRef = facturebonreceptionRepository.findByReferenceFournisseur(factureBonReception.getReferenceFournisseur());
        com.csys.util.Preconditions.checkBusinessLogique(factureWithSameRef == inBase || factureWithSameRef == null, "reffrs-fournisseur-exists");
        factureBonReception = facturebonreceptionRepository.saveAndFlush(factureBonReception);

        Set<FactureBA> listeBonsReception = (Set<FactureBA>) factureBAService.findFactureBAByNumBonIn(factureBonReceptionDTO.getBonReceptionCollection().stream().map(BonRecepDTO::getNumbon).collect(toList()));
        List<Integer> listeDesDelai = new ArrayList();
        List<LocalDateTime> listeDatBons = new ArrayList();
        for (FactureBA bonReception : listeBonsReception) {
            if (bonReception.getFactureBonReception() != null) {
                log.debug("le bon de reception {} et sa facture BonReception {} est : ", bonReception.getNumbon(), bonReception.getFactureBonReception().getNumbon());
            }
            //verification ctrl bonReceptions non facturés
            Preconditions.checkBusinessLogique(bonReception.getFactureBonReception() == null, "facture-bon-reception.invoiced-reception", bonReception.getNumbon());
            bonReception.setFactureBonReception(factureBonReception);
            listeDesDelai.add(bonReception.getMaxDelaiPaiement());
            listeDatBons.add(bonReception.getDatbon());
        }
        Integer maxDelaiPaiementBons = listeDesDelai.stream().max(Integer::compare).get();
        LocalDateTime maxDate = listeDatBons.stream().max(LocalDateTime::compareTo).get();
        Period diff = Period.between(maxDate.toLocalDate(), LocalDate.now());
        factureBonReception.setMaxDelaiPaiement(maxDelaiPaiementBons - diff.getDays());

        factureBonReception.setBonReceptionCollection(listeBonsReception);
        log.debug("la liste des bons est : ", listeBonsReception);
        factureBonReception.calcul(inBase);

        inBase.getBonReceptionCollection().forEach(bonReception -> {
            bonReception.setFactureBonReception(null);
        });
        factureBonReception = facturebonreceptionRepository.save(inBase);

        FactureBonReceptionDTO resultDTO = FactureBonReceptionFactory.facturebonreceptionToFactureBonReceptionDTO(factureBonReception);
//        resultDTO.setBonReceptionCollection(factureBonReception.getBonReceptionCollection().stream().map(item -> factureBAFactory.factureBAToBonRecepDTOEagerAvecDetailsetCommandes(item)).distinct().collect(Collectors.toList()));
//        factureBAService.isReturned(resultDTO.getBonReceptionCollection());
        return resultDTO;
    }

    /**
     * Get one facturebonreceptionDTO by id
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(readOnly = true)
    public FactureBonReceptionDTO findOne(String id) {
        log.debug("Request to get FactureBonReception: {}", id);
        FactureBonReception facturebonreception = facturebonreceptionRepository.findOne(id);
        FactureBonReceptionDTO factureBonReceptionDTO = FactureBonReceptionFactory.facturebonreceptionToFactureBonReceptionDTO(facturebonreception);
        if (factureBonReceptionDTO != null) {
            FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(factureBonReceptionDTO.getCodeFournisseur());
            Preconditions.checkBusinessLogique(fournisseur != null, "missing-supplier", factureBonReceptionDTO.getCodeFournisseur());
            factureBonReceptionDTO.setFournisseur(fournisseur);
            DeviseDTO deviseDTO = caisseServiceClient.findDeviseById(facturebonreception.getCodeDevise());
            Preconditions.checkBusinessLogique(deviseDTO != null, "missing-devise", facturebonreception.getCodeDevise());
            factureBonReceptionDTO.setDevise(deviseDTO);
             Set<BonRecepDTO> listeBonRecep= facturebonreception.getBonReceptionCollection()
                            .stream()
                            .map(item -> {

                                BonRecepDTO bonReceptionDTO = FactureBAFactory.factureBAToBonRecepDTO(item);
                               
                              
                                List<MvtstoBADTO> detailsReception = new ArrayList();
                                List<Integer> listCodesCA = item.getRecivedDetailCA()
                                        .stream()
                                        .map(receivedDetailCa -> receivedDetailCa.getPk().getCommandeAchat())
                                        .distinct()
                                        .collect(Collectors.toList());
                                bonReceptionDTO.setPurchaseOrders(demandeServiceClient.findListCommandeAchat(listCodesCA, LocaleContextHolder.getLocale().getLanguage()));
                                item.getDetailFactureBACollection().forEach(mvtstoBA -> {
                           
                                    MvtstoBADTO dto = new MvtstoBADTO();
                                    MvtstoBAFactory.toDTO(mvtstoBA, dto);
                                    dto.setQteReturned(mvtstoBA.getQuantite().subtract(mvtstoBA.getQtecom()));
                                    detailsReception.add(dto);
                                });
                                bonReceptionDTO.setDetails(new ArrayList<> (detailsReception));
                                return bonReceptionDTO;
                            })
                         
                            .collect(Collectors.toSet());
            factureBonReceptionDTO.setBonReceptionCollection( new ArrayList<>(listeBonRecep));

            factureBAService.isReturned(factureBonReceptionDTO.getBonReceptionCollection());
        }
        return factureBonReceptionDTO;
    }

    /**
     * Get one facturebonreception by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FactureBonReception findFactureBonReception(String id) {
        log.debug("Request to get FactureBonReception: {}", id);
        FactureBonReception facturebonreception = facturebonreceptionRepository.findOne(id);
        return facturebonreception;
    }

    /**
     * Get all the facturebonreceptions.
     *
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public Collection<FactureBonReceptionDTO> findAll(LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categDepot, Boolean deleted, String codeFournisseur) {
        log.debug("Request to get All FactureBonReceptions");

        QFactureBonReception _FactureBonReception = QFactureBonReception.factureBonReception;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(fromDate, () -> _FactureBonReception.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _FactureBonReception.datbon.loe(toDate))
                .optionalAnd(categDepot, () -> _FactureBonReception.categDepot.eq(categDepot))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _FactureBonReception.dateAnnule.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _FactureBonReception.dateAnnule.isNull())
                .optionalAnd(codeFournisseur, () -> _FactureBonReception.codeFournisseur.eq(codeFournisseur));

        List<FactureBonReception> listFactureBonReception = (List<FactureBonReception>) facturebonreceptionRepository.findAll(builder);

        List<String> fournisseursID = listFactureBonReception.stream().map(FactureBonReception::getCodeFournisseur).collect(toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(fournisseursID);

        return listFactureBonReception.stream().map(factureBR -> {
            FactureBonReceptionDTO factureBonReceptiondto = FactureBonReceptionFactory.lazyfacturebonreceptionToFactureBonReceptionDTO(factureBR);
            FournisseurDTO matchedFournisseur = fournisseurs.stream()
                    .filter(fournisseur -> fournisseur.getCode().equals(factureBR.getCodeFournisseur()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-supplier", new Throwable(factureBR.getCodeFournisseur())));

            factureBonReceptiondto.setFournisseur(matchedFournisseur);

            return factureBonReceptiondto;
        }).collect(toList());
    }

    @Transactional(readOnly = true)
    public Set<MvtstoBADTO> findDetailFactureById(String id) {
        log.debug("Request to get FactureBonReception details: {}", id);

        FactureBonReception facturebonreception = facturebonreceptionRepository.findOne(id);
        Set<MvtStoBA> listeMvstoBA = facturebonreception.getBonReceptionCollection()
                .stream()
                .map(item -> item.getDetailFactureBACollection())
                .flatMap(x -> x.stream())
                .collect(Collectors.toSet());
        return listeMvstoBA
                .stream()
                .map(item -> {
            MvtstoBADTO dto = new MvtstoBADTO();
            MvtstoBAFactory.toDTO(item, dto);

            return dto;
        })
         .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<MvtstoBADTO> findDetailsFactureById(String id) {
        log.debug("Request to get FactureBonReception details: {}", id);

        FactureBonReception factureBonReception = facturebonreceptionRepository.findOne(id);

        List<String> NumBons = factureBonReception.getBonReceptionCollection().stream().map(x -> x.getNumbon()).collect(Collectors.toList());

        Set<MvtstoBADTO> listeAllDetails = new HashSet<>();

        List<MvtstoBADTO> listeDetails = mvtStoBAService.getDetailsReceptions(NumBons);

        Set set = new HashSet(listeDetails);
//            set.stream().map(item->item.set)
        listeAllDetails.addAll(set);

        return listeAllDetails;
    }

    @Transactional(readOnly = true)
    public byte[] editionFactureBonReception(String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureBonReception : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        FactureBonReceptionDTO factureBonReceptionDTO = findOne(id);

        List<String> listeBonRecep = new ArrayList();
        factureBonReceptionDTO.getBonReceptionCollection().forEach(x -> {
            Set<String> listeCommande = x.getPurchaseOrders().stream().map(CommandeAchatDTO::getNumbon).collect(Collectors.toSet());
            String listeCommandeAchat = "(" + String.join(",", listeCommande) + ")";
            String bonRecep = x.getNumaffiche() + listeCommandeAchat;
            listeBonRecep.add(bonRecep);
        });

        factureBonReceptionDTO.setListeDesBons(String.join(",", listeBonRecep));

        reportClientDoc.open("Reports/Facture_Bon_Reception" + local + ".rpt", 0);

        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(factureBonReceptionDTO), FactureBonReceptionDTO.class, "Entete", "Entete");
        Set<MvtstoBADTO> details = factureBonReceptionDTO.getBonReceptionCollection().stream().map(t -> t.getDetails()).flatMap(x -> x.stream()).map(elt ->  elt).distinct().collect(Collectors.toSet());

//        Set<BonRecepDTO> details=factureBonReceptionDTO.getBonReceptionCollection().stream().map(item -> 
//        {factureBAFactory.factureBAToBonRecepDTOEagerAvecDetailsetCommandes(item);
//          item.ge     
//                }).distinct().collect(Collectors.toSet()));
//        details.forEach(x -> {
//            Instant instant = x.getDatPer().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
//            x.setDatePerEdition(Date.from(instant));
//        });
        reportClientDoc.getDatabaseController().setDataSource(details, MvtstoBADTO.class, "Detail", "Detail");
        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(factureBonReceptionDTO.getFournisseur()), FournisseurDTO.class, "Fournisseur", "Fournisseur");
        reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class, "clinique", "clinique");
        reportClientDoc.getSubreportController().getSubreport("BaseTVA").getDatabaseController().setDataSource(factureBonReceptionDTO.getBaseTvaFactureBonReceptionCollection(), BaseTvaFactureBonReceptionDTO.class, "Commande", "Commande");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        BigDecimal baseTVA = BigDecimal.ZERO;
        BigDecimal mntTVASansGratuite = BigDecimal.ZERO;
        BigDecimal mntTVAGratuite = BigDecimal.ZERO;

        for (BaseTvaFactureBonReceptionDTO x : factureBonReceptionDTO.getBaseTvaFactureBonReceptionCollection()) {
            baseTVA = baseTVA.add(x.getBaseTva());
            mntTVASansGratuite = mntTVASansGratuite.add(x.getMontantTva());
            mntTVAGratuite = mntTVAGratuite.add(x.getMontantTvaGratuite());
        }

        BigDecimal mntTVA = mntTVAGratuite.add(mntTVASansGratuite);
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "baseTVA", baseTVA);
        paramController.setCurrentValue("", "mntTVA", mntTVA);
        if ("ar".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.AR(factureBonReceptionDTO.getMontant().toString(), "جنيه", "قرش"));
        } else if ("fr".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.FR(factureBonReceptionDTO.getMontant().toString(), "livres", "pence"));
        } else {
            paramController.setCurrentValue("", "montantLettre", Convert.EN(factureBonReceptionDTO.getMontant().toString(), "pounds", "penny"));
        }
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return Helper.read(byteArrayInputStream);
    }

    /**
     * Delete facturebonreception by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete FactureBonReception: {}", id);
        FactureBonReception factureBonReception = facturebonreceptionRepository.findOne(id);
        Preconditions.checkFound(factureBonReception, "factureBonReception.NotFound");
        Preconditions.checkBusinessLogique(factureBonReception.getUserAnnule() == null, "factureBonReception.Annule");
        FcptfrsPH fcptFrs = fcptFrsPHRepository.findFirstByNumBon(id);
        BigDecimal numOpr = new BigDecimal(fcptFrs.getNumOpr());
        //verification facture reglée
        Preconditions.checkBusinessLogique(facturebonreceptionRepository.findFactureReglee(numOpr).compareTo(BigDecimal.ZERO) == 0, "factureBonReception-deja-reglee");
        // verification ctrl facture integrée
        Preconditions.checkBusinessLogique(!factureBonReception.getIntegrer(), "facture-deja-integre");
        fcptFrsPHService.deleteFcptfrsByNumBonDao(id, factureBonReception.getTypbon());
        LocalDateTime date = LocalDateTime.now();
        factureBonReception.setDateAnnule(date);
        factureBonReception.setUserAnnule(SecurityContextHolder.getContext().getAuthentication().getName());
        Set<FactureBA> listeBonRecep = factureBonReception.getBonReceptionCollection()
                .stream()
                .map(t -> {
                    t.setFactureBonReception(null);
                    return t;
                })
                .collect(Collectors.toSet());
        facturebonreceptionRepository.save(factureBonReception);

    }
}
