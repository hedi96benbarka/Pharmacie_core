package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.MvtstoAF;
import com.csys.pharmacie.achat.domain.MvtstoBACodartFrs;
import com.csys.pharmacie.achat.domain.QMvtStoBA;
import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.CategorieArticleDTO;
import com.csys.pharmacie.achat.dto.DetailReceptionDTO;
import com.csys.pharmacie.achat.dto.EmplacementDTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import com.csys.pharmacie.achat.dto.PriceVarianceDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.factory.MvtstoBAFactory;
import com.csys.pharmacie.achat.repository.MvtstoBARepository;
import com.csys.pharmacie.achat.repository.ReceptionDetailCARepository;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MvtStoBAService {

    private final Logger log = LoggerFactory.getLogger(MvtStoBAService.class);
    private final MvtstoBARepository mvtstoBARepository;
//    private final DetailReceptionFactory detailReceptionFactory;
    private final ParamAchatServiceClient paramAchatServiceClient;
//    private final MvtstoBAFactory mvtstoBAFactory;
    private final ReceptionDetailCARepository receptiondetailcaRepository;

    public MvtStoBAService(MvtstoBARepository mvtstoBARepository, ParamAchatServiceClient paramAchatServiceClient, ReceptionDetailCARepository receptiondetailcaRepository) {
        this.mvtstoBARepository = mvtstoBARepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.receptiondetailcaRepository = receptiondetailcaRepository;
    }

    public List<MvtstoBADTO> getDetailsReception(String numBon) {
        List<MvtStoBA> l = mvtstoBARepository.findByPkNumbon(numBon);

        List<Integer> codesUnites = new ArrayList();
        List<Integer> codesEmplacements = new ArrayList();
        l.stream().forEach(item -> {
            codesUnites.add(item.getCodeUnite());
            if (item.getCodeEmplacement() != null) {
                codesEmplacements.add(item.getCodeEmplacement());
            }
        }
        );
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codesUnites);
        Set<Integer> setEmplacement = new HashSet(codesEmplacements);

        List<EmplacementDTO> emplacements = l.get(0).getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(setEmplacement,null) : new ArrayList();

        List<MvtstoBADTO> result = l.stream().map(x -> {
            MvtstoBADTO dto = new MvtstoBADTO();
            MvtstoBAFactory.toDTO(x, dto);
            dto.setQteReturned(x.getQuantite().subtract(x.getQtecom()));
            log.debug("qté retourné du detail est {}", dto.getQteReturned());
//                    log.debug("qté Qcom du detail est {}",dto.getQuantite());
            UniteDTO unite = listUnite.stream().filter(item -> item.getCode().equals(x.getCodeUnite())).findFirst().orElse(null);

            if (l.get(0).getCategDepot().equals(CategorieDepotEnum.IMMO)&&(emplacements!=null)) {
                EmplacementDTO emplacement = emplacements.stream()
                        .filter(item -> item.getCode().equals(x.getCodeEmplacement()))
                        .findFirst().orElse(null);
                if (x.getCodeEmplacement() != null) {
                    dto.setDesignationEmplacement(emplacement.getDesignation());
                }
            }
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            dto.setUnitDesignation(unite.getDesignation());
            return dto;
        }).collect(Collectors.toList());

        return result;
    }

    public List<MvtstoBADTO> getDetailsReceptions(List<String> numBons) {
        List<MvtStoBA> l = mvtstoBARepository.findByPkNumbonIn(numBons);
        List<MvtstoBADTO> result = l.stream()
                .map(x -> {
                    MvtstoBADTO dto = new MvtstoBADTO();
                    MvtstoBAFactory.toDTO(x, dto);
                    dto.setQteReturned(x.getQuantite().subtract(x.getQtecom()));
                    log.debug("qté retourné du detail est {}", dto.getQteReturned());
//                    log.debug("qté Qcom du detail est {}",dto.getQuantite());
                    return dto;
                }).collect(Collectors.toList());

        return result;
    }

//    /*must be read only so quantity of MvtstoBa wont be changed in database(working on entities))
//*//read only is not working 
//    @Transactional(readOnly = true)
    public List<DetailReceptionDTO> returnListeArticlesRetournesTotalmenet(List<MvtStoBA> listeDetailsReception) {

        log.debug("listeDetailsReception est {}", listeDetailsReception);
        List<DetailReceptionDTO> listeMvtstoBasRetournesTotalement = new ArrayList();
        List<DetailReceptionDTO> listeDetailsReceptionDtos = new ArrayList();
        listeDetailsReception
                .stream()
                .forEach(elt -> {
                    DetailReceptionDTO dto = new DetailReceptionDTO();
                    dto.setQteCom(elt.getQtecom());
                    dto.setRefArt(elt.getCodart());
                    dto.setAncienPrixAchat(elt.getAncienPrixAchat());
                    listeDetailsReceptionDtos.add(dto);
                }); 
  log.debug("listeDetailsReceptionDtos est {}", listeDetailsReceptionDtos);
        listeDetailsReceptionDtos.stream().collect(Collectors.groupingBy(DetailReceptionDTO::getRefArt,
                Collectors.reducing(new DetailReceptionDTO(BigDecimal.ZERO), (a, b) -> {
                   log.debug(" a.getqtecom {} "+a.getQteCom(),"b.getQteCom(){} " +b.getQteCom() );
                    b.setQteCom(b.getQteCom().add(a.getQteCom()));

                    return b;
                }))).forEach((k, v) -> {
            if (BigDecimal.ZERO.compareTo(v.getQteCom()) == 0) {
                listeMvtstoBasRetournesTotalement.add(v);
            }
        });
        return listeMvtstoBasRetournesTotalement;
    }

    public List<MvtstoBACodartFrs> findByCodArtIn(List<Integer> codarts) {

        return mvtstoBARepository.findByCodArtIn(codarts);
    }

    public List<MvtStoBA> findByNumBonInAndCodartIn(List<String> numBons, List<Integer> codArts) {
        return mvtstoBARepository.findByNumBonInAndCodartIn(numBons, codArts);
    }

    @Transactional
    public void updateQteComOnAvoirFournisseur(AvoirFournisseur avoirFournisseur) {

        List<String> numBonsReceptions = new ArrayList();
        List<Integer> codArts = new ArrayList();
        avoirFournisseur.getMvtstoAFList().forEach(item -> {
            numBonsReceptions.add(item.getNumbonReception());
            codArts.add(item.getCodart());
        });

        List<MvtStoBA> listeMvtstoBAs = findByNumBonInAndCodartIn(numBonsReceptions, codArts);
        List<MvtStoBA> listeMvtstoBAsUpdated = new ArrayList();
        for (MvtstoAF mvtstoAF : avoirFournisseur.getMvtstoAFList()) {
            List<MvtStoBA> matchingMvtstoBAs = listeMvtstoBAs.stream()
                    .filter(item -> mvtstoAF.getCodart().equals(item.getPk().getCodart())
                    && mvtstoAF.getLotinter().equalsIgnoreCase(item.getLotInter())
                    && mvtstoAF.getDatPer().equals(item.getDatPer())
                    && mvtstoAF.getNumbonReception().equals(item.getPk().getNumbon())
                    && mvtstoAF.getPriuni().setScale(7).equals(item.getPriuni().setScale(7)))
                    .collect(Collectors.toList());
            log.debug("matchingMvtstoBAs sont {}", matchingMvtstoBAs);
            log.debug("lotInter du detail recep {} ,code article{} et le priuni {} ", mvtstoAF.getLotinter(), mvtstoAF.getCodart(), mvtstoAF.getPriuni());
            BigDecimal qteComTotale = matchingMvtstoBAs.stream().map(MvtStoBA::getQtecom).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            log.debug("qteComTotale est {}", qteComTotale);
            Preconditions.checkBusinessLogique(qteComTotale.compareTo(mvtstoAF.getQuantite()) >= 0, "quantite-insuffisante");
            BigDecimal qteARetourner = mvtstoAF.getQuantite();
            for (MvtStoBA mvtstoBAToUpdateQteCom : matchingMvtstoBAs) {
                BigDecimal qteMin = qteARetourner.min(mvtstoBAToUpdateQteCom.getQtecom());
                qteARetourner = qteARetourner.subtract(qteMin);
                mvtstoBAToUpdateQteCom.setQtecom(mvtstoBAToUpdateQteCom.getQtecom().subtract(qteMin));
                log.debug("qteARetourner restante est {}", qteARetourner);
                if (qteARetourner.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            listeMvtstoBAsUpdated.addAll(matchingMvtstoBAs);
        }
        mvtstoBARepository.save(listeMvtstoBAsUpdated);
    }

    public BigDecimal getDernierPrixAchat(String codart) {
        List<BigDecimal> prixachats = mvtstoBARepository.getDernierPrixAchat(codart);
        BigDecimal res = BigDecimal.ZERO;
        if (prixachats.isEmpty()) {
            res = prixachats.get(0);
        }
        return res;
    }

    public BigDecimal findMaxPriuniByCodeArticleAndIsPrixRefAndNotReturnedAndNumBonNot(Integer codeArticle, Boolean isPrixRef, String numbon) {

//        builder.and(_MvtStoBA.isPrixReference.eq(Boolean.TRUE)).and(_MvtStoBA.pk().codart.eq(codeArticle)).and(_MvtStoBA.priuni.eq(_MvtStoBA.priuni.max())); 
        return mvtstoBARepository.findMaxPriuniByCodeArticleAndIsPrixRefAndNotReturnedAndNumBonNot(codeArticle, isPrixRef, numbon);
    }

    public BigDecimal findMaxPriuniByCodeArticleAndIsPrixRefAndNotReturned(Integer codeArticle, Boolean isPrixRef) {

//        builder.and(_MvtStoBA.isPrixReference.eq(Boolean.TRUE)).and(_MvtStoBA.pk().codart.eq(codeArticle)).and(_MvtStoBA.priuni.eq(_MvtStoBA.priuni.max())); 
        return mvtstoBARepository.findMaxPriuniByCodeArticleAndIsPrixRefAndNotReturned(codeArticle, isPrixRef);
    }

    public List<MvtStoBA> findByNumBon(String numpiece) {
        return mvtstoBARepository.findByPkNumbon(numpiece);
    }

    public void save(List<MvtStoBA> result) {
        mvtstoBARepository.save(result);
    }

    @Transactional(readOnly = true)
    public List<MvtStoBA> findByPk_CodartInAndFactureBA_DatbonAfterAndTypeBon(Set<Integer> codeArticles, LocalDateTime datbon, TypeBonEnum typeBonEnum) {
        log.debug("find derniers MVtstoBa avec les codarts suivants {}", codeArticles);
        return mvtstoBARepository.findByPk_CodartInAndFactureBA_DatbonAfterAndTypbon(codeArticles, datbon, typeBonEnum.toString());

    }

    @Transactional(readOnly = true)
    public List<MvtstoBADTO> findReceptionsByCodeCaAndCodeArticle(Integer codeCa, Integer codeArticle) {
        log.debug("Request findReceptionsByCodeCaAndCodeArticle: {},{}", codeCa, codeArticle);
        Set<ReceptionDetailCA> listeRecepCA = receptiondetailcaRepository.findByPk_CommandeAchatAndPk_Article(codeCa, codeArticle);

        Set<String> listeNumBons = listeRecepCA.stream().map(RecepCa -> RecepCa.getReception().getNumbon()).collect(Collectors.toSet());
        List<MvtStoBA> listeMvtStoBA = mvtstoBARepository.findByPk_CodartAndFactureBA_NumbonIn(codeArticle, listeNumBons);

        List<MvtstoBADTO> resultDTOs = new ArrayList();
        if (listeMvtStoBA != null && listeMvtStoBA.size() > 0) {
            Set<Integer> setEmplacement = listeMvtStoBA.stream()
                    .filter(elt -> elt.getCodeEmplacement() != null)
                    .map(MvtStoBA::getCodeEmplacement).collect(Collectors.toSet());
//        List<MvtStoBA> listeMvtStoBA = listeRecepCA.stream().map(recepCa -> recepCa.getReception().getDetailFactureBACollection()).flatMap(x -> x.stream()).collect(Collectors.toList());
            List<EmplacementDTO> emplacements = listeMvtStoBA.get(0).getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(setEmplacement,null) : new ArrayList();
            resultDTOs = MvtstoBAFactory.toDTOs(listeMvtStoBA);

            if (listeMvtStoBA.get(0).getCategDepot().equals(CategorieDepotEnum.IMMO)) {
                resultDTOs.forEach(detailRecep -> {
                    EmplacementDTO emplacement = emplacements.stream()
                            .filter(item -> item.getCode().equals(detailRecep.getCodeEmplacement()))
                            .findFirst().orElse(null);
                    if (detailRecep.getCodeEmplacement() != null) {
                        detailRecep.setDesignationEmplacement(emplacement.getDesignation());
                        detailRecep.setCodeEmplacement(emplacement.getCode());
                    }
                });
            }
        }
        return resultDTOs;
    }

    @Transactional(readOnly = true)
    public MvtStoBA findDernierMvtstoBa(LocalDateTime date, Integer codart) {
//    return  mvtstoBARepository.findTopByPk_CodartAndFactureBA_DatbonLessThanAndFactureBA_CodAnnulIsNullAndFactureBA_TypbonAndPriuniIsNotNullOrderByFactureBA_DatbonDesc( codart,factureBA.getDatbon(),TypeBonEnum.BA);
        List<MvtStoBA> mvtStoBAs = mvtstoBARepository.findByPk_CodartAndFactureBA_DatbonLessThanAndFactureBA_CodAnnulIsNullAndFactureBA_TypbonAndPriuniIsNotNull(codart, date, TypeBonEnum.BA);
        return mvtStoBAs.stream().sorted(Comparator.comparing((MvtStoBA x) -> {
            return x.getFactureBA().getDatbon();
        }).reversed()).findFirst().get();
    }

    @Transactional(readOnly = true)
    public List<PriceVarianceDTO> calculPriceVariance(LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categDepot, Integer categorieArticle) {

        Preconditions.checkBusinessLogique(Integer.compare(toDate.getYear(), fromDate.getYear()) == 0, "wrong-period");
//        List<Integer> listeCodeArticle= new ArrayList();
        List<ArticleDTO> listeArticleAffectedTOCategorieArticle = categorieArticle != null ? paramAchatServiceClient.articleByCategArt(categorieArticle) : new ArrayList();
        List<Integer> listeCodeArticleAffectedTOCategorieArticle = categorieArticle != null ? listeArticleAffectedTOCategorieArticle.stream().map(elt -> elt.getCode()).collect(Collectors.toList()) : new ArrayList();
        List<MvtStoBA> listeMvtstoBa = new ArrayList<>();
        List<MvtStoBA> listeMvtstoBaPrecedants = new ArrayList<>();
        if (listeCodeArticleAffectedTOCategorieArticle != null && listeCodeArticleAffectedTOCategorieArticle.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) listeCodeArticleAffectedTOCategorieArticle.size() / 2000);

            for (int i = 0; i < numberOfChunks; i++) {

                List<Integer> codesChunk = listeCodeArticleAffectedTOCategorieArticle.subList(i * 2000, Math.min(i * 2000 + 2000, listeCodeArticleAffectedTOCategorieArticle.size()));

                QMvtStoBA _mvtsto = QMvtStoBA.mvtStoBA;
                WhereClauseBuilder builder = new WhereClauseBuilder()
                        .and(_mvtsto.factureBA().categDepot.eq(categDepot))
                        .optionalAnd(fromDate, () -> _mvtsto.factureBA().datbon.goe(fromDate))
                        .optionalAnd(toDate, () -> _mvtsto.factureBA().datbon.loe(toDate))
                        .optionalAnd(categorieArticle, () -> _mvtsto.pk().codart.in(codesChunk))
                        .and(_mvtsto.factureBA().codAnnul.isNull())
                        .and(_mvtsto.priuni.gt(BigDecimal.ZERO));
                List<MvtStoBA> listeMvtstoBaChunk = (List<MvtStoBA>) mvtstoBARepository.findAll(builder);
                listeMvtstoBa.addAll(listeMvtstoBaChunk);

                WhereClauseBuilder builder2 = new WhereClauseBuilder()
                        .and(_mvtsto.factureBA().categDepot.eq(categDepot))
                        .optionalAnd(fromDate, () -> _mvtsto.factureBA().datbon.goe(fromDate.minusYears(1)))
                        .optionalAnd(toDate, () -> _mvtsto.factureBA().datbon.loe(toDate.minusYears(1)))
                        //                .optionalAnd(categorieArticle, () -> listeCodeArticle.contains(_mvtsto.pk().codart))
                        .optionalAnd(categorieArticle, () -> _mvtsto.pk().codart.in(codesChunk))
                        .and(_mvtsto.factureBA().codAnnul.isNull())
                        .and(_mvtsto.priuni.gt(BigDecimal.ZERO));
                List<MvtStoBA> listeMvtstoBaPrecedantsChunk = (List<MvtStoBA>) mvtstoBARepository.findAll(builder2);
                listeMvtstoBaPrecedants.addAll(listeMvtstoBaPrecedantsChunk);

            }
        }

        Set<Integer> codeArticles = new HashSet();
        Set<Integer> codeUnites = new HashSet();

        listeMvtstoBa.addAll(listeMvtstoBaPrecedants);

        listeMvtstoBa.forEach(mvtsto
                -> {
            codeArticles.add(mvtsto.getCodart());
            codeUnites.add(mvtsto.getCodeUnite());
        }
        );

        List<ArticleDTO> ArticleDTOs = paramAchatServiceClient.articleFindbyListCode(codeArticles);
        List<UniteDTO> units = paramAchatServiceClient.findUnitsByCodes(codeUnites);

        Map<Integer, List<MvtStoBA>> listeMvtstoBaActuelsGroupes = listeMvtstoBa
                .stream()
                .collect(Collectors.groupingBy(mvtsto -> mvtsto.getCodart()));

        List<PriceVarianceDTO> listPriceVariance = new ArrayList();
        Set<CategorieArticleDTO> listeCategorieArticleDTO = paramAchatServiceClient.findCategorieArticlesWithParentByCategorieDepot(categDepot, null);
        for (Map.Entry<Integer, List<MvtStoBA>> entry : listeMvtstoBaActuelsGroupes.entrySet()) {

            log.debug("*****************************************************************codart est {}*******************************************************", entry.getKey());
//            log.debug("entry.getValue() est {}", entry.getValue());
            BigDecimal quantiteActuelle = BigDecimal.ZERO;
            BigDecimal prixTotalActuel = BigDecimal.ZERO;
            BigDecimal quantitePrecedante = BigDecimal.ZERO;
            BigDecimal prixTotalPrecedant = BigDecimal.ZERO;
            BigDecimal prixTotalTTCActuel = BigDecimal.ZERO;
            BigDecimal prixTotalTTCPrecedant = BigDecimal.ZERO;

//             calculate the average price
            for (MvtStoBA mvtstoBA : entry.getValue()) {
//                log.debug("mvtstoBA est {}", mvtstoBA);
                BigDecimal montantHTTotat = mvtstoBA.getPriuni()//PU*REMISE*QUANTITE
                        .multiply((BigDecimal.valueOf(100).subtract(mvtstoBA.getRemise())).divide(BigDecimal.valueOf(100)))//remise
                        .multiply(mvtstoBA.getQuantite());
//                if ((mvtstoBA.getPriuni().compareTo(BigDecimal.ZERO) == 0) && (mvtstoBA.getBaseTva().compareTo(BigDecimal.ZERO) > 0)) {
//                    //mnt Tva Gratuite   
//                    BigDecimal divisor = BigDecimal.valueOf(100).add(mvtstoBA.getTautva()).divide(BigDecimal.valueOf(100));//100+tva/100
//                    prixTotal.add(mvtstoBA.getBaseTva()
//                            .multiply(mvtstoBA.getTautva())
//                            .divide(new BigDecimal(100))
//                            .divide(divisor, 5, RoundingMode.CEILING)
//                            .multiply(mvtstoBA.getQuantite()));
//
//                }
                //case the actual year 
                if (mvtstoBA.getFactureBA().getTypbon().equals(TypeBonEnum.RT) && (Integer.compare(mvtstoBA.getFactureBA().getDatbon().getYear(), fromDate.getYear()) == 0)) {
                    quantiteActuelle = quantiteActuelle.subtract(mvtstoBA.getQuantite());
                    prixTotalActuel = prixTotalActuel.subtract(montantHTTotat);
                    prixTotalTTCActuel = prixTotalTTCActuel.subtract(montantHTTotat.multiply((BigDecimal.valueOf(100).add(mvtstoBA.getTautva())).divide(BigDecimal.valueOf(100))));//prixTotal * 1,Tva
//                    log.debug("la quantite du retour num {} est {} et son prix est {}", mvtstoBA.getPk().getNumbon(), mvtstoBA.getQuantite(), mvtstoBA.getMontht());
//                    log.debug("prixTotalTTCActuel est {} ", prixTotalTTCActuel.longValue());

                } else if (mvtstoBA.getFactureBA().getTypbon().equals(TypeBonEnum.BA) && (Integer.compare(mvtstoBA.getFactureBA().getDatbon().getYear(), fromDate.getYear()) == 0)) {
                    quantiteActuelle = quantiteActuelle.add(mvtstoBA.getQuantite());
                    prixTotalActuel = prixTotalActuel.add(montantHTTotat);
                    prixTotalTTCActuel = prixTotalTTCActuel.add(montantHTTotat.multiply((BigDecimal.valueOf(100).add(mvtstoBA.getTautva())).divide(BigDecimal.valueOf(100))));
//                    log.debug("la quantite du bon reception num {} est {} et son prix est {}", mvtstoBA.getPk().getNumbon(), mvtstoBA.getQuantite(), mvtstoBA.getMontht());
//                    log.debug("prixTotalActuel est {} et prixTotalTTCActuel est {}  ", prixTotalActuel.toString(), prixTotalTTCActuel.toString());

                } // case the year before
                else if (mvtstoBA.getFactureBA().getTypbon().equals(TypeBonEnum.RT) && (Integer.compare(mvtstoBA.getFactureBA().getDatbon().getYear(), fromDate.getYear()) < 0)) {
                    quantitePrecedante = quantitePrecedante.subtract(mvtstoBA.getQuantite());
                    prixTotalPrecedant = prixTotalPrecedant.subtract(montantHTTotat);
                    prixTotalTTCPrecedant = prixTotalTTCPrecedant.subtract(montantHTTotat.multiply((BigDecimal.valueOf(100).add(mvtstoBA.getTautva())).divide(BigDecimal.valueOf(100))));

//                    log.debug("la quantite du retour -1 num {} est {} et son prix est {}", mvtstoBA.getPk().getNumbon(), mvtstoBA.getQuantite(), mvtstoBA.getMontht());
//                    log.debug("prixTotalTTCPrecedant est {} ", prixTotalTTCPrecedant.longValue());
                } else if (mvtstoBA.getFactureBA().getTypbon().equals(TypeBonEnum.BA) && (Integer.compare(mvtstoBA.getFactureBA().getDatbon().getYear(), fromDate.getYear()) < 0)) {
                    quantitePrecedante = quantitePrecedante.add(mvtstoBA.getQuantite());
                    prixTotalPrecedant = prixTotalPrecedant.add(montantHTTotat);
                    prixTotalTTCPrecedant = prixTotalTTCPrecedant.add(montantHTTotat.multiply((BigDecimal.valueOf(100).add(mvtstoBA.getTautva())).divide(BigDecimal.valueOf(100))));
//                    log.debug("la quantite du bon reception -1 num {} est {} et son prix est {}", mvtstoBA.getPk().getNumbon(), mvtstoBA.getQuantite(), mvtstoBA.getMontht());
//                    log.debug("prixTotalTTCPrecedant est {} ", prixTotalTTCPrecedant.longValue());
                }
            }
// create priceVarianceDTO
            PriceVarianceDTO priceVariance = new PriceVarianceDTO();
            priceVariance.setCodeArticle(entry.getKey());
//            log.debug("quantiteActuelle du codart {} est {}", entry.getKey(), quantiteActuelle);
            if (quantiteActuelle.compareTo(BigDecimal.ZERO) == 1) {
                priceVariance.setPrixMoyenActuel(prixTotalActuel.divide(quantiteActuelle, 2, RoundingMode.CEILING));
                priceVariance.setPrixMoyenActuelTTC(prixTotalTTCActuel.divide(quantiteActuelle, 2, RoundingMode.CEILING));
            } else {
                priceVariance.setPrixMoyenActuel(BigDecimal.ZERO);
                priceVariance.setPrixMoyenActuelTTC(BigDecimal.ZERO);
            }
            if (quantitePrecedante.compareTo(BigDecimal.ZERO) == 1) {
                priceVariance.setPrixMoyenPrecedant(prixTotalPrecedant.divide(quantitePrecedante, 2, RoundingMode.CEILING));
                priceVariance.setPrixMoyenPrecedantTTC(prixTotalTTCPrecedant.divide(quantitePrecedante, 2, RoundingMode.CEILING));
            } else {
                priceVariance.setPrixMoyenPrecedant(BigDecimal.ZERO);
                priceVariance.setPrixMoyenPrecedantTTC(BigDecimal.ZERO);
            }
            log.debug("priceVariance du codart {} :****** le prix moyen est  {} et le dernier prix achat est {}", priceVariance.getCodeArticle(), priceVariance.getPrixMoyenActuel(), priceVariance.getPrixDernierAchat());

            ArticleDTO matchedItem = ArticleDTOs.stream().filter(item -> item.getCode().equals(entry.getKey())).findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(entry.getKey().toString())));

            priceVariance.setCodeSaisi(matchedItem.getCodeSaisi());
            priceVariance.setDesArticle(matchedItem.getDesignation());
            priceVariance.setDesSecArticle(matchedItem.getDesignationSec());
            MvtStoBA dernierMvtstoBa = findDernierMvtstoBa(LocalDateTime.now(), entry.getKey());
            log.debug("dernierMvtstoBa est {}", dernierMvtstoBa);
            priceVariance.setPrixDernierAchat(matchedItem.getPrixAchat().multiply((BigDecimal.valueOf(100).add(dernierMvtstoBa.getTautva())).divide(BigDecimal.valueOf(100))));
            priceVariance.setTauxTvaActuel(dernierMvtstoBa.getTautva());
            priceVariance.setQuantiteTotale(quantiteActuelle);
            UniteDTO matchedUnit = units.stream()
                    .filter(unit -> unit.getCode().equals(matchedItem.getCodeUnite()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit", new Throwable(matchedItem.getCodeUnite().toString())));
            priceVariance.setDesignationUnite(matchedUnit.getDesignation());

            CategorieArticleDTO matchedCategorieArticle = listeCategorieArticleDTO.stream()
                    .filter(elt -> elt.getCode().equals(matchedItem.getCategorieArticle().getCode()))
                    .findFirst().orElse(null);
            if (matchedCategorieArticle != null) {
                priceVariance.setListeCategorieArticlesParents(matchedCategorieArticle.getListeCategorieArticle());
            }

            listPriceVariance.add(priceVariance);
        }

        return listPriceVariance;
    }

    public Boolean exitsByCodartAndIsPrixReference(Integer codart, Boolean isPrixReference) {
        if (isPrixReference != null) {
            return mvtstoBARepository.existsByPk_CodartAndIsPrixReference(codart, isPrixReference);
        } else {
            return mvtstoBARepository.existsByPk_Codart(codart);
        }

    }

    @Transactional(readOnly = true)
    public List<MvtStoBA> findByPk_CodartInAndFactureBA_DatbonAfterAndCodeFournisseursInAndTypeBon(Set<Integer> codeArticles, LocalDateTime datbon, Set<String> codeFournisseurs, TypeBonEnum typeBon) {
        log.debug("find derniers MVtstoBa avec les codarts suivants et les fournisseurs suivants {}", codeArticles, codeFournisseurs);
        return mvtstoBARepository.findByPk_CodartInAndFactureBA_DatbonAfterAndFactureBA_CodfrsInAndTypbon(codeArticles, datbon, codeFournisseurs, typeBon.toString());
    }

    @Transactional(readOnly = true)
    public List<MvtStoBA> findByPk_CodartInAndFactureBA_DatbonAfterAndCodeFournisseursAndTypeBon(Set<Integer> codeArticles, LocalDateTime datbon, String codeFournisseur, TypeBonEnum typeBon) {
        log.debug("find derniers MVtstoBa avec les codarts suivants et les fournisseurs suivants {}", codeArticles, codeFournisseur);
        return mvtstoBARepository.findByPk_CodartInAndFactureBA_DatbonAfterAndFactureBA_CodfrsAndTypbon(codeArticles, datbon, codeFournisseur, typeBon.toString());

    }

    public List<Integer> findReceivedItemsByCategorieDepot(CategorieDepotEnum categDep) {

        List<String> listItems = mvtstoBARepository.findReceivedItemsByCategorieDepot(categDep.categ());
        List<Integer> listReceivedItems = new ArrayList<>();
        listReceivedItems.addAll(listItems.stream().map(Integer::parseInt).collect(Collectors.toList()));

        return listReceivedItems;

    }

    public List<Integer> findReceivedItemsByCategorieDepotAndCodesArticleIn(CategorieDepotEnum categDep, List<Integer> articleIDs) {
        List<Integer> listReceivedItems = new ArrayList<>();
        if (articleIDs.isEmpty()) {
            return listReceivedItems;
        }
        List<String> listItems = mvtstoBARepository.findReceivedItemsByCategorieDepotAndCodesArticleIn(categDep.categ(), articleIDs);

        listReceivedItems.addAll(listItems.stream().map(Integer::parseInt).collect(Collectors.toList()));

        return listReceivedItems;

    }

    public List<MvtstoBADTO> findArticleGratuit(CategorieDepotEnum categDep, LocalDateTime fromDate,
            LocalDateTime toDate) {
        List<MvtStoBA> articlesGratuit = mvtstoBARepository.findByPriuniAndByCategDepot(BigDecimal.ZERO, categDep, fromDate, toDate);

        return MvtstoBAFactory.toDTOs(articlesGratuit);
    }
}

//    @Transactional
//    public void deleteMvtStoBAByNumBon(String numBon) {
//        List<DetailReceptionDTO> l = getDetailsReception(numBon);
//        mvtstoBARepository.deleteByNumbon(numBon);
//    }
//    public MvtStoBA findMvtStoBAByCodartAndNumBon(String numBon, String codArt) {
//
//        return mvtstoBARepository.findByNumbonAndCodArt(numBon, codArt);
//    }
//    @Transactional
//    public boolean updatenumfacblmvtstoBA(List<String> listebr, String numbon, Date d) {
//
//        for (String l : listebr) {
//            List<MvtStoBA> mvtsto = mvtstoBARepository.findByPkNumbon(l);
//            for (MvtStoBA mvt : mvtsto) {
//                mvt.setNumfacbl(numbon);
//                mvt.setDatefacbl(d);
//                mvtstoBARepository.save(mvt);
//            }
//        }
//        return true;
//
//    }
//    public void updateQteComBA(List<String> listebr, Fournisseur f) {
//        List<ListArticle> lA = f.getListearticle();
//        List<MvtStoBA> mvtsto = mvtstoBARepository.findByPkNumbon(listebr.get(0));
//        for (MvtStoBA mvt : mvtsto) {
//            for (ListArticle l : lA) {
//                if (l.getCodart().equals(mvt.getPk().getCodart())) {
//                    mvt.setQtecom(mvt.getQtecom().subtract(l.getQte()));
//                    mvtstoBARepository.save(mvt);
//                    break;
//                }
//            }
//        }
//
//    }
//    @Transactional
//    public MvtStoBA findMvtStoBAByCodartAndNumBonAndLot(String numBon, String codArt, String lot) {
//        MvtStoBA m = mvtstoBARepository.findByPk_CodartAndPk_NumbonAndLotInter(numBon, codArt, lot);
//        return m;
//    }
//    @Deprecated
//    @Transactional(readOnly = true)
//    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
//        QMvtStoBA _mvtsto = QMvtStoBA.mvtStoBA;
//        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.factureBA().categDepot.eq(categ))
//                .optionalAnd(codart, () -> _mvtsto.pk().codart.eq(codart))
//                .optionalAnd(coddep, () -> _mvtsto.factureBA().coddep.eq(coddep))
//                .optionalAnd(fromDate, () -> _mvtsto.factureBA().datbon.goe(fromDate))
//                .optionalAnd(toDate, () -> _mvtsto.factureBA().datbon.loe(toDate));
//        List<MvtStoBA> list = (List<MvtStoBA>) mvtstoBARepository.findAll(builder);
//        List<Mouvement> mouvements = new ArrayList<>();
//        List<String> codesFrs = list.stream().map(item -> item.getFactureBA().getCodfrs()).distinct().collect(Collectors.toList());
//        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
//        List<Integer> codeUnites = list.stream().map(item -> item.getCodeUnite()).distinct().collect(Collectors.toList());
//        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
//        list.forEach((mouvement) -> {
//            FournisseurDTO fournisseur = fournisseurs.stream().filter(x -> x.getCode().equals(mouvement.getFactureBA().getCodfrs())).findFirst().orElse(null);
//            com.csys.util.Preconditions.checkBusinessLogique(fournisseur != null, "missing-frs");
//            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mouvement.getCodeUnite())).findFirst().orElse(null);
//            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
//            log.debug("mvtsto ba mouvement {}", mouvement);
//            mouvements.addAll(mvtstoBAFactory.toMouvement(mouvement, fournisseur, unite, typeDate));
//        });
//        return mouvements;
//    }
//    @Deprecated
//    @Transactional(readOnly = true)
//    public List<Mouvement> findListMouvementAnnule(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
//        QMvtStoBA _mvtsto = QMvtStoBA.mvtStoBA;
//        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.factureBA().categDepot.eq(categ))
//                .optionalAnd(codart, () -> _mvtsto.pk().codart.eq(codart))
//                .optionalAnd(coddep, () -> _mvtsto.factureBA().coddep.eq(coddep))
//                .optionalAnd(fromDate, () -> _mvtsto.factureBA().datbon.goe(fromDate))
//                .optionalAnd(toDate, () -> _mvtsto.factureBA().datbon.loe(toDate))
//                .and(_mvtsto.factureBA().codAnnul.isNotNull());
//        List<MvtStoBA> list = (List<MvtStoBA>) mvtstoBARepository.findAll(builder);
//        List<Mouvement> mouvements = new ArrayList<>();
//        List<String> codesFrs = list.stream().map(item -> item.getFactureBA().getCodfrs()).distinct().collect(Collectors.toList());
//        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
//        List<Integer> codeUnites = list.stream().map(item -> item.getCodeUnite()).distinct().collect(Collectors.toList());
//        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
//        list.forEach((mouvement) -> {
//            FournisseurDTO fournisseur = fournisseurs.stream().filter(x -> x.getCode().equals(mouvement.getFactureBA().getCodfrs())).findFirst().orElse(null);
//            com.csys.util.Preconditions.checkBusinessLogique(fournisseur != null, "missing-frs");
//            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mouvement.getCodeUnite())).findFirst().orElse(null);
//            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
//            log.debug("mvtsto ba mouvement {}", mouvement);
//            mouvements.addAll(mvtstoBAFactory.toMouvementAnnule(mouvement, fournisseur, unite, typeDate));
//        });
//        return mouvements;
//    }
//    public List<QteMouvement> findQuantiteMouvement(String famart, String coddep, Date datedeb, Date datefin) {
//    	if(famart.equalsIgnoreCase("tous"))
//    		return mvtstoBARepository.findQuantiteMouvementTous(coddep, datedeb, datefin);    		
//    	else
//        return mvtstoBARepository.findQuantiteMouvement(famart, coddep, datedeb, datefin);
//    }TODO qte mouvementée par famille
//    public List<QteMouvement> findQuantiteMouvementRetour(String famart, String coddep, Date datedeb, Date datefin) {
//       
//    	if(famart.equalsIgnoreCase("tous"))
//            return mvtstoBARepository.findQuantiteMouvementRetourTous(coddep, datedeb, datefin);
//    	else
//        return mvtstoBARepository.findQuantiteMouvementRetour(famart, coddep, datedeb, datefin);
//    }TODO qte retournée par famille
//    @Deprecated
//    @Transactional(readOnly = true)
//    public List<TotalMouvement> findTotalMouvement(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeBonEnum typbon) {
//        if (fromDate == null) {
//            return mvtstoBARepository.findTotalMouvement(coddep, codart, toDate, typbon);
//        } else {
//            return mvtstoBARepository.findTotalMouvement(coddep, codart, fromDate, toDate, typbon);
//        }
//    }
// @Deprecated
//    @Transactional(readOnly = true)
//    public List<TotalMouvement> findTotalMouvement(List<Integer> codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeBonEnum typbon) {
//        List<TotalMouvement> lists = new ArrayList<>();
//        if (codart != null && codart.size() > 0) {
//            Integer numberOfChunks = (int) Math.ceil((double) codart.size() / 2000);
//            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
//            for (int i = 0; i < numberOfChunks; i++) {
//
//                List<Integer> codesChunk = codart.subList(i * 2000, Math.min(i * 2000 + 2000, codart.size()));
//                if (fromDate == null) {
//                    completableFuture[i] = mvtstoBARepository.findTotalMouvement(coddep, codesChunk, toDate, typbon).whenComplete((list, exception) -> {
//                        lists.addAll(list);
//                    });
//                } else {
//                    completableFuture[i] = mvtstoBARepository.findTotalMouvement(coddep, codesChunk, fromDate, toDate, typbon).whenComplete((list, exception) -> {
//                        lists.addAll(list);
//                    });
//                }
//            }
//            CompletableFuture.allOf(completableFuture).join();
//        }
//        return lists;
//    }
//@Deprecated
//    @Transactional(readOnly = true)
//    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeBonEnum typbon) {
//        if (coddep != null) {
//            if (fromDate == null) {
//                return mvtstoBARepository.findTotalMouvement(coddep, toDate, typbon);
//            } else {
//                return mvtstoBARepository.findTotalMouvement(coddep, fromDate, toDate, typbon);
//            }
//
//        } else {
//            if (fromDate == null) {
//                return mvtstoBARepository.findTotalMouvement(toDate, typbon);
//            } else {
//                return mvtstoBARepository.findTotalMouvement(fromDate, toDate, typbon);
//            }
//        }
//    }
