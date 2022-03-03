/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticleIMMODTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUuDTO;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.CategorieArticleDTO;
import com.csys.pharmacie.achat.dto.CategorieDepotDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.EmplacementDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;

import com.csys.pharmacie.client.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.transfert.dto.MotifDemandeRedressementDTO;
import com.csys.pharmacie.vente.quittance.dto.CategorieArticleWithRoot;
import com.csys.util.Preconditions;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Farouk
 */
@RefreshScope
@Service("ParamAchatServiceClient")
public class ParamAchatServiceClient {

    private final Logger log = LoggerFactory.getLogger(ParamAchatServiceClient.class);

    private final RestTemplate restTemplate;

    @Value("${paramAchatService.base-uri}")
    private String baseUri;
    @Value("${paramAchatService.path}")
    private String path;

    @Value("${paramAchatService.fournisseurs}")
    private String uriFournisseur;

    @Value("${paramAchatService.articles}")
    private String uriArticles;

    @Value("${paramAchatService.medical}")
    private String uriMedical;

    @Value("${paramAchatService.medication}")
    private String uriMedication;

    @Value("${paramAchatService.non-medical}")
    private String uriNonMedical;

    @Value("${paramAchatService.immo}")
    private String uriImmo;
    @Value("${paramAchatService.depot}")
    private String uriDepot;

    @Value("${paramAchatService.tvas}")
    private String uritvas;

    @Value("${paramAchatService.unite}")
    private String uriUnite;

    @Value("${paramAchatService.categorie-article}")
    private String uricategorie_articles;

    @Value("${paramAchatService.departement}")
    private String uridepartements;

    @Value("${paramAchatService.categorie-depots}")
    private String uricategorie_depots;
    @Value("${paramAchatService.emplacement}")
    private String uriEmplacement;

    @Value("${paramAchatService.article-depot}")
    private String uriArticleDepot;

    @Value("${paramAchatService.motif-paiement}")
    private String uriMotifPaiement;

    @Value("${paramAchatService.article-non-mouvemente}")
    private String uriArticleNonMouvemente;

    @Value("${paramAchatService.motif-demande-redressement}")
    private String uriMotifDemandeRedressement;

    @Value("${paramAchatService.palier-marge-categorie-article}")
    private String uriPalierMargeCategorieArticle;

    @Value("${paramAchatService.injection}")
    private String uriInjection;

    @Value("${paramAchatService.classification-articles}")
    private String uriClassificationArticles;

    @Value("${paramAchatService.palier-classification-articles}")
    private String uriPalierClassificationArticles;

    public ParamAchatServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Article service client +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @HystrixCommand(fallbackMethod = "articleFindByCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000")})
    public ArticleDTO articleFindByCode(Integer id) {
        log.debug("Sending request to find articlePH with code: {}", id);
        if (id == null) {
            return null;
        } else {
            return restTemplate.exchange(baseUri + uriArticles + "/" + id, HttpMethod.GET, null, ArticleDTO.class).getBody();
        }
    }

    private ArticleDTO articleFindByCodeFallback(Integer id) {
        log.error("falling back articleFindByCodeFallback: {}", id);
        return null;
    }

    @HystrixCommand(fallbackMethod = "articleFindbyListCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000")}
    )
    public List<ArticleDTO> articleFindbyListCode(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find list of article with codes: {}", ids);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticles)
                    .path("/searches")
                    .build()
                    .encode();
            List<Integer> intList = new ArrayList<>();
            intList.addAll(ids);
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity<>(intList, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
            }).getBody();
        }
    }

    private List<ArticleDTO> articleFindbyListCodeFallback(Collection<Integer> ids) {
        log.error("falling back from article FindbyListCode");
        List<ArticleDTO> listArt = new ArrayList<>();
        for (Integer id : ids) {
            ArticleDTO art = new ArticleDTO();
            art.setCode(id);
            art.setDesignation("Not Available");
            listArt.add(art);
        }
        return listArt;
    }

    @HystrixCommand(fallbackMethod = "articleFindbyListCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "360000")}
    )
    public List<ArticleDTO> articleFindbyListCode(Collection<Integer> ids, Boolean onlyMyArticles) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find list of article with codes: {}", ids);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticles)
                    .path("/searches")
                    .queryParam("only-my-articles", onlyMyArticles)
                    .build()
                    .encode();
            List<Integer> intList = new ArrayList<>();
            intList.addAll(ids);
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity<>(intList, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
            }).getBody();
        }
    }

    private List<ArticleDTO> articleFindbyListCodeFallback(Collection<Integer> ids, Boolean onlyMyArticles) {
        log.error("falling back from article FindbyListCode");
        List<ArticleDTO> listArt = new ArrayList<>();
        for (Integer id : ids) {
            ArticleDTO art = new ArticleDTO();
            art.setCode(id);
            art.setDesignation("Not Available");
            listArt.add(art);
        }
        return listArt;
    }

    //************************************************************
    @HystrixCommand(fallbackMethod = "articleFindbyListCodeWithLanguageFRFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<ArticleDTO> articleFindbyListCodeWithLanguageFR(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find list of article with codes: {}", ids);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticles)
                    .path("/searches")
                    .build()
                    .encode();
            List<Integer> intList = new ArrayList<>();
            intList.addAll(ids);
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, "FR");
            HttpEntity<List<Integer>> entity = new HttpEntity<>(intList, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
            }).getBody();
        }
    }

    private List<ArticleDTO> articleFindbyListCodeWithLanguageFRFallback(Collection<Integer> ids) {
        log.error("falling back from article FindbyListCode fr");
        List<ArticleDTO> listArt = new ArrayList<>();
        for (Integer id : ids) {
            ArticleDTO art = new ArticleDTO();
            art.setCode(id);
            art.setDesignation("Not Available");
            listArt.add(art);
        }
        return listArt;
    }

    //************************************************************
    @HystrixCommand(fallbackMethod = "ArticlePriceUpdateAndArticleFrsPriceUpdateFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public void ArticlePriceUpdateAndArticleFrsPriceUpdate(BonRecepDTO bonrecep) {

        log.debug("Sending request find list of article with codes: {}", bonrecep);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticles)
                .path("/pricing")
                .build()
                .encode();

        HttpEntity<BonRecepDTO> entity = new HttpEntity<>(bonrecep, null);

        restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
        });

    }

    public void ArticlePriceUpdateAndArticleFrsPriceUpdateFallback(BonRecepDTO bonrecep) throws Exception {
        throw new Exception("problem of updating");
    }

    ////////////////////////////////////
    @HystrixCommand(fallbackMethod = "articlePriceRevertAndArticleFrsPriceRevertFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public void articlePriceRevertAndArticleFrsPriceRevert(BonRecepDTO bonrecep) {

        log.debug("Sending request find list of article with codes: {}", bonrecep);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticles)
                .path("/pricing-revert")
                .build()
                .encode();

        HttpEntity<BonRecepDTO> entity = new HttpEntity<>(bonrecep, null);
        restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
        });

    }

    public void articlePriceRevertAndArticleFrsPriceRevertFallback(BonRecepDTO bonrecep) throws Exception {
        throw new Exception("problem of reverting");

    }

    public ArticleDTO findArticlebyCategorieDepotAndCodeArticle(CategorieDepotEnum categorieDepot, Integer codeArticle) {
        return (ArticleDTO) findArticlebyCategorieDepotAndListCodeArticle(categorieDepot, codeArticle).get(0);
    }

    /**
     *
     * @param categorieDepot categorieDepot that we are looking in for the
     * articles
     * @param listCodeArticle list of article's codes
     * @return List of articles with requested codes in the requested Categorie
     * of depots
     */
    public List<?> findArticlebyCategorieDepotAndListCodeArticle(CategorieDepotEnum categorieDepot, Integer... listCodeArticle) {
        List<?> listarticle = new ArrayList<>();
        switch (categorieDepot) {
            case EC:
                log.debug("EC");
                listarticle = articleECFindbyListCode(Arrays.asList(listCodeArticle));
                break;
            case PH:
                log.debug("PH");
                listarticle = articlePHFindbyListCode(Arrays.asList(listCodeArticle));
                break;
            case UU:
                log.debug("UU");
                listarticle = articleUUFindbyListCode(Arrays.asList(listCodeArticle));
                break;
            case IMMO:
                log.debug("IMMO");
                listarticle = articleIMMOFindbyListCode(Arrays.asList(listCodeArticle));
                break;
            default:
                break;
        }
        return listarticle;
    }
    /////////**********IMMO************/////////// 

    @HystrixCommand(fallbackMethod = "articleIMMOFindbyListCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public List<ArticleIMMODTO> articleIMMOFindbyListCode(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find list of articleIMMO with codes: {}", ids);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriImmo)
                    .path("/searches")
                    .build()
                    .encode();
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleIMMODTO>>() {
            }).getBody();
        }
    }

    public List<ArticleIMMODTO> articleIMMOFindbyListCodeFallback(Collection<Integer> ids) {
        log.error("falling back from articleIMMOFindbyListCode");
        List<ArticleIMMODTO> listArt = new ArrayList<>();
        return listArt;
    }

    /////////**********end IMMO************/////////// 
    @HystrixCommand(fallbackMethod = "articleECFindbyListCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public List<ArticleDTO> articleECFindbyListCode(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find list of articleEC with codes: {}", ids);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriNonMedical)
                    .path("/searches")
                    .build()
                    .encode();
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
            }).getBody();
        }
    }

    public List<ArticleDTO> articleECFindbyListCodeFallback(Collection<Integer> ids) {
        log.error("falling back from articleECFindbyListCode");
        List<ArticleDTO> listArt = new ArrayList<>();
        for (Integer id : ids) {
            ArticleDTO art = new ArticleDTO();
            art.setCode(id);
            art.setDesignation("Not Available");
            listArt.add(art);
        }
        return listArt;
    }

    @HystrixCommand(fallbackMethod = "articlePHFindByCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000")})
    public ArticlePHDTO articlePHFindByCode(Integer id) {
        log.debug("Sending request to find articlePH with code: {}", id);
        if (id == null) {
            return null;
        } else {
            return restTemplate.exchange(baseUri + uriMedication + "/" + id, HttpMethod.GET, null, ArticlePHDTO.class).getBody();
        }
    }

    private ArticlePHDTO articlePHFindByCodeFallback(Integer id) {
        log.error("falling back articlePHFindByCodeFallback: {}", id);
        return null;
    }

    /**
     * @param ids
     * @return
     */
    @HystrixCommand(fallbackMethod = "articlePHFindbyListCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000")})
    public List<ArticlePHDTO> articlePHFindbyListCode(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request to find list of articlePH with codes: {}", ids);

            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);
            return restTemplate.exchange(baseUri + uriMedication + "/searches", HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticlePHDTO>>() {
            }).getBody();
        }

    }

    private List<ArticlePHDTO> articlePHFindbyListCodeFallback(Collection<Integer> ids) {

        log.error("falling back articlePHFindbyListCodeFallback");
        List<ArticlePHDTO> listArt = new ArrayList<>();
        for (Integer id : ids) {
            ArticlePHDTO art = new ArticlePHDTO();
            art.setCode(id);
            art.setDesignation("Not Available");
            listArt.add(art);
        }
        return listArt;
    }

    @HystrixCommand(fallbackMethod = "findListeArticlePHByIdsAndSiteFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000")})
    public List<ArticlePHDTO> findListeArticlePHByIdsAndSite(Collection<Integer> ids, String ipAdressSite) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find list of ph with codes: {}", ids);
            String baseUriUpdated = "http://" + ipAdressSite + path;
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUriUpdated + uriMedication)
                    .path("/searches")
                    .build()
                    .encode();
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticlePHDTO>>() {
            }).getBody();
        }

    }

    private List<ArticlePHDTO> findListeArticlePHByIdsAndSiteFallBack(Collection<Integer> ids, String ipAdressSite) {

        log.error("falling back articlePHFindbyListCodeFallback and site with codes: {} **********  and code site {}", ids, ipAdressSite);
        return null;
    }

    //**********************************************
    @HystrixCommand(fallbackMethod = "articlePHFindbyListCodeFrFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<ArticlePHDTO> articlePHFindbyListCodeFr(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request to find list of articlePH with codes: {}", ids);

            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, "fr");
            HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);
            return restTemplate.exchange(baseUri + uriMedication + "/searches", HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticlePHDTO>>() {
            }).getBody();
        }

    }

    private List<ArticlePHDTO> articlePHFindbyListCodeFrFallback(Collection<Integer> ids) {

        log.error("falling back articlePHFindbyListCodeFrFallback");
        List<ArticlePHDTO> listArt = new ArrayList<>();
        for (Integer id : ids) {
            ArticlePHDTO art = new ArticlePHDTO();
            art.setCode(id);
            art.setDesignation("Not Available");
            listArt.add(art);
        }
        return listArt;
    }

    //**********************************************
    @HystrixCommand(fallbackMethod = "articleUUFindbyListCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<ArticleUuDTO> articleUUFindbyListCode(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request to find list of articleUU: {}", ids);
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);
            return restTemplate.exchange(baseUri + uriMedical + "/searches", HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleUuDTO>>() {
            }).getBody();
        }
    }

    private List<ArticleUuDTO> articleUUFindbyListCodeFallback(Collection<Integer> ids) {
        log.error("falling back articleUUFindbyListCodeFallback");
        List<ArticleUuDTO> listArt = new ArrayList<>();
        for (Integer id : ids) {
            ArticleUuDTO art = new ArticleUuDTO();
            art.setCode(id);
            art.setDesignation("Not Available");
            listArt.add(art);
        }
        return listArt;
    }

    @HystrixCommand(fallbackMethod = "articleUUFindbyListCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<ArticleUuDTO> articleUUFindbyListCode(Collection<Integer> ids, Integer coddep, boolean withSellingPrice, boolean withArticleDepot) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request to find list of articleUU: {}", ids);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriMedical)
                    .path("/searches")
                    .queryParam("with-selling-price", withSellingPrice)
                    .queryParam("depot-id", coddep)
                    .queryParam("with-article-depot", withArticleDepot)
                    .build()
                    .encode();

            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleUuDTO>>() {
            }).getBody();
        }
    }

    private List<ArticleUuDTO> articleUUFindbyListCodeFallback(Collection<Integer> ids, Integer coddep, boolean withSellingPrice, boolean withArticleDepot) {
        log.error("falling back articleUUFindbyListCodeFallback BY ids, coddep, withSellingPrice, withArticleDepot");
        List<ArticleUuDTO> listArt = new ArrayList<>();
        for (Integer id : ids) {
            ArticleUuDTO art = new ArticleUuDTO();
            art.setCode(id);
            art.setDesignation("Not Available");
            listArt.add(art);
        }
        return listArt;
    }

    //********************************************
    @HystrixCommand(fallbackMethod = "articleUUFindbyListCodeFrFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<ArticleUuDTO> articleUUFindbyListCodeFr(Collection<Integer> ids, Integer coddep, boolean withSellingPrice) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request to find list of articleUU: {}", ids);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriMedical)
                    .path("/searches")
                    .queryParam("with-selling-price", withSellingPrice)
                    .queryParam("depot-id", coddep)
                    .build()
                    .encode();

            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, "fr");
            HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleUuDTO>>() {
            }).getBody();
        }
    }

    private List<ArticleUuDTO> articleUUFindbyListCodeFrFallback(Collection<Integer> ids, Integer coddep, boolean withSellingPrice) {
        log.error("falling back articleUUFindbyListCodeFallback");
        List<ArticleUuDTO> listArt = new ArrayList<>();
        for (Integer id : ids) {
            ArticleUuDTO art = new ArticleUuDTO();
            art.setCode(id);
            art.setDesignation("Not Available");
            listArt.add(art);
        }
        return listArt;
    }

    //********************************************
    @HystrixCommand(fallbackMethod = "articleParFournisseurFindbyListCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<ArticleDTO> articleParFournisseurFindbyListCode(List<Integer> ids, String codfrs) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request to find list of article: {} by frs: {}", ids, codfrs);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticles)
                    .path("/prixAchatParFournisseur")
                    .queryParam("codfrs", codfrs)
                    .build()
                    .encode();

            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
            }).getBody();
        }
    }

    private List<ArticleDTO> articleParFournisseurFindbyListCodeFallback(List<Integer> ids, String codfrs) {
        log.error("falling back articleParFournisseurFindbyListCodeFallback");
        return new ArrayList();
    }

    //////////////////////////////PH///////////////////////
    /**
     *
     * @param q query criteria ( codesaisi, designation, categorieArticle)
     * @param onlyMyArticles
     * @return
     */
    @HystrixCommand(fallbackMethod = "articlePHFindAllFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})

    public List<ArticlePHDTO> articlePHFindAll(String q, Boolean onlyMyArticles) {
        log.debug("Sending request to find list of articlePH with query: {}", q);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriMedication)
                .queryParam("q", q)
                .queryParam("only-my-articles", onlyMyArticles)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ArticlePHDTO>>() {
        }).getBody();
    }

    private List<ArticlePHDTO> articlePHFindAllFallback(String q, Boolean onlyMyArticles) {
        throw new IllegalBusinessLogiqueException("error-loading-articlesPH");
    }
    //////////////////////////////UU///////////////////////

    /**
     *
     * @param q query criteria ( codesaisi, designation, categorieArticle)
     * @param withSellingPrice
     * @param onlyMyArticles
     * @return
     */
    @HystrixCommand(fallbackMethod = "articleUUFindAllFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<ArticleUuDTO> articleUUFindAll(String q, boolean withSellingPrice, Boolean onlyMyArticles, Integer coddep) {

        log.debug("Sending request to find list of articleUU with query: {}", q);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriMedical)
                .queryParam("q", q)
                .queryParam("sellingPrice", withSellingPrice)
                .queryParam("only-my-articles", onlyMyArticles)
                .queryParam("coddep", coddep)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ArticleUuDTO>>() {
        }).getBody();
    }

    private List<ArticleUuDTO> articleUUFindAllFallback(String q, boolean withSellingPrice, Boolean onlyMyArticles, Integer coddep) {
        throw new IllegalBusinessLogiqueException("error-loading-articlesUU");
    }

    //////////////////////////////EC///////////////////////
    /**
     *
     * @param q query criteria ( codesaisi, designation, categorieArticle)
     * @param onlyMyArticles
     * @return
     */
    @HystrixCommand(fallbackMethod = "articleECFindAllFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<ArticleDTO> articleECFindAll(String q, Boolean onlyMyArticles) {

        log.debug("Sending request to find list of articleEC with query: {}", q);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriNonMedical)
                .queryParam("q", q)
                .queryParam("only-my-articles", onlyMyArticles)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ArticleDTO>>() {
        }).getBody();
    }

    private List<ArticleDTO> articleECFindAllFallback(String q, Boolean onlyMyArticles) {
        throw new IllegalBusinessLogiqueException("error-loading-articlesEC");
    }

    ///////////////////////////////////IMMO///////////////////////
    @HystrixCommand(fallbackMethod = "articleIMMOFindAllFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public List<ArticleIMMODTO> articleIMMOFindAll(String q) {

        log.debug("Sending request to find list of articleIMMO with query: {}", q);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriImmo)
                .queryParam("q", q)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ArticleIMMODTO>>() {
        }).getBody();
    }

    private List<ArticleIMMODTO> articleIMMOFindAllFallback(String q) {
        throw new IllegalBusinessLogiqueException("error-loading-articlesIMMO");
    }
//------------------------------------------------------- End of Article service client --------------------------------------------------------
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++ FournisseurServiceClient ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @HystrixCommand(fallbackMethod = "findFournisseurByListCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public List<FournisseurDTO> findFournisseurByListCode(List<String> ids) {
        log.debug("Sending request to find FournisseurByListCode {}", ids);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);
        return restTemplate.exchange(baseUri + uriFournisseur + "/searches", HttpMethod.POST, entity, new ParameterizedTypeReference<List<FournisseurDTO>>() {
        }).getBody();
    }

    private List<FournisseurDTO> findFournisseurByListCodeFallback(List<String> ids) {

        log.error("falling back FournisseurByListCode");
        List<FournisseurDTO> listeFournisseurs = new ArrayList<>();
        for (String id : ids) {
            FournisseurDTO art = new FournisseurDTO();
            art.setCode(id);
            art.setDesignation("fournisseur.not-available");
            listeFournisseurs.add(art);
        }
        return listeFournisseurs;
    }

    @HystrixCommand(fallbackMethod = "findFournisseurByCodeFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public FournisseurDTO findFournisseurByCode(String id) {
        log.debug("Sending request to find Fournisseur by code: {}", baseUri + uriFournisseur + "/" + id);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<Integer>> entity = new HttpEntity(header);
        return restTemplate.exchange(baseUri + uriFournisseur + "/" + id, HttpMethod.GET, entity, new ParameterizedTypeReference<FournisseurDTO>() {
        }).getBody();
    }

    private FournisseurDTO findFournisseurByCodeFallBack(String id) {
        log.error("falling back findFournisseurByCode");
        FournisseurDTO frs = new FournisseurDTO();
        frs.setCode(id);
        frs.setDesignation("fournisseur.deleted");

        return frs;
    }
//----------------------------------------------------- End of FournisseurServiceClient ---------------------------------------------

//+++++++++++++++++++++++++++++++++++++++++++++++++++++ DepotServiceClient +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @HystrixCommand(fallbackMethod = "findDepotByCodeFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")})
    public DepotDTO findDepotByCode(Integer id) {
        log.debug("Sending request to find depot by code : {}", baseUri + uriDepot + "/" + id);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<Integer>> entity = new HttpEntity(header);
        return restTemplate.exchange(baseUri + uriDepot + "/" + id, HttpMethod.GET, entity, new ParameterizedTypeReference<DepotDTO>() {
        }).getBody();
    }

    private DepotDTO findDepotByCodeFallBack(Integer id) {
        log.error("falling back findDepotByCodeFallBack");
        DepotDTO dep = new DepotDTO(id);
        dep.setDesignation("depot.deleted");
        return dep;
    }

    @HystrixCommand(fallbackMethod = "findDepotPrincipalByCategorieDepotFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")})
    public DepotDTO findDepotPrincipalByCategorieDepot(CategorieDepotEnum categorieDepot) {
        log.debug("Sending request to find depot by code : {}", baseUri + uriDepot + "/principal?categorie-depot=" + categorieDepot);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<Integer>> entity = new HttpEntity(header);
        return restTemplate.exchange(baseUri + uriDepot + "/principal?categorie-depot=" + categorieDepot, HttpMethod.GET, entity, new ParameterizedTypeReference<DepotDTO>() {
        }).getBody();
    }

    private DepotDTO findDepotPrincipalByCategorieDepotFallBack(CategorieDepotEnum categorieDepot) {
        log.error("falling back findDepotPrincipalByCategorieDepotFallBack");
        DepotDTO dep = new DepotDTO();
        dep.setDesignation("depot.deleted");
        return dep;
    }

    @HystrixCommand(fallbackMethod = "findDepotsByCodesFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public List<DepotDTO> findDepotsByCodes(Collection<Integer> ids) {
        log.debug("Sending request find list of depots with codes: {}", ids);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriDepot)
                .path("/searches")
                .build()
                .encode();
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<DepotDTO>>() {
        }).getBody();
    }

    private List<DepotDTO> findDepotsByCodesFallback(Collection<Integer> ids) {
        log.error("falling back from findDepotsByCodes");
        List<DepotDTO> listArt = new ArrayList<>();
        for (Integer id : ids) {
            DepotDTO unt = new DepotDTO();
            unt.setCode(id);
            unt.setDesignation("Not Available");
            listArt.add(unt);
        }
        return listArt;
    }
// ----------------------------------------------------End Depot Service Client-------------------------------------------------------

//+++++++++++++++++++++++++++++++++++++++++++++++++++++Unite service client++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @HystrixCommand(fallbackMethod = "findUniteByCodeFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})

    public UniteDTO findUniteByCode(Integer id) {
        log.debug("Sending request findUniteByCode");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriUnite)
                .path("/{id}")
                .buildAndExpand(id)
                .encode();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Integer> entity = new HttpEntity<>(id, headers);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, entity, new ParameterizedTypeReference<UniteDTO>() {
        }).getBody();

    }

    private UniteDTO findUniteByCodeFallBack(Integer id) {
        log.error("falling back from findUniteByCode");
        return null;
    }

    @HystrixCommand(fallbackMethod = "findUnitsByCodesFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<UniteDTO> findUnitsByCodes(Collection<Integer> ids) {
        log.debug("Sending request find list of unities with codes: {}", ids);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriUnite)
                .path("/searches")
                .build()
                .encode();
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<UniteDTO>>() {
        }).getBody();
    }

    private List<UniteDTO> findUnitsByCodesFallback(Collection<Integer> ids) {
        log.error("falling back from findUnitiesByCodes");
        List<UniteDTO> listArt = new ArrayList<>();
        for (Integer id : ids) {
            UniteDTO unt = new UniteDTO();
            unt.setCode(id);
            unt.setDesignation("Not Available");
            listArt.add(unt);
        }
        return listArt;
    }
//------------------------------------------------- End of unit service client ------------------------------------------------

//++++++++++++++++++++++++++++++++++++++++++++++++++DepartmentServiceClient ++++++++++++++++++++++++++++++++++++++++++++
    @HystrixCommand(fallbackMethod = "findListDepartmentsFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    })
    public List<DepartementDTO> findListDepartments(Collection<Integer> codes) {
        log.debug("Sending request findAllDepartement");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uridepartements)
                .path("/searches")
                .build()
                .encode();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Collection<Integer>> entity = new HttpEntity<>(codes, headers);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<DepartementDTO>>() {
        }).getBody();
    }

    private List<DepartementDTO> findListDepartmentsFallBack(Collection<Integer> codes) {
        log.error("falling back from findListDepartments");
        return new ArrayList<DepartementDTO>();

    }

    @HystrixCommand(fallbackMethod = "findDepartmentsFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})

    public DepartementDTO findDepartment(Integer id) {
        log.debug("Sending request findDepartement");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uridepartements)
                .path("/{id}")
                .buildAndExpand(id)
                .encode();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Integer> entity = new HttpEntity<>(id, headers);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, entity, new ParameterizedTypeReference<DepartementDTO>() {
        }).getBody();

    }

    private DepartementDTO findDepartmentsFallBack(Integer id) {
        log.error("falling back from findListDepartments");
        DepartementDTO dep = new DepartementDTO();
        dep.setCode(id);

        dep.setDesignation("department.deleted");
        return dep;

    }
// --------------------------------------end Of Departement Service--------------------------------------------------------------- 
//+++++++++++++++++++++++++++++++++++++++++++++++++++++ EmplacementServiceClient +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @HystrixCommand(fallbackMethod = "findEmplacementByCodeFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")})
    public EmplacementDTO findEmplacementByCode(Integer id) {
        log.debug("Sending request to find emplacement by code : {}", baseUri + uriEmplacement + "/" + id);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<Integer>> entity = new HttpEntity(header);
        return restTemplate.exchange(baseUri + uriEmplacement + "/" + id, HttpMethod.GET, entity, new ParameterizedTypeReference<EmplacementDTO>() {
        }).getBody();
    }

    private EmplacementDTO findEmplacementByCodeFallBack(Integer id) {
        log.error("falling back findEmplacementByCode {}", id);
        EmplacementDTO emplacement = new EmplacementDTO();

        return emplacement;
    }

    @HystrixCommand(fallbackMethod = "findEmplacementsByCodesFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")})
    public List<EmplacementDTO> findEmplacementsByCodes(Set<Integer> ids, String ipAdressSite) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find list of emplacements with codes: {}", ids);
            String baseUriUpdated = ipAdressSite != null ? "http://" + ipAdressSite + path : baseUri;
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUriUpdated + uriEmplacement)
                    .path("/searches")
                    //                .queryParam("actif", actif)
                    //                .queryParam("code-service", codeService)
                    .build()
                    .encode();
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<EmplacementDTO>>() {
            }).getBody();
        }

    }

    private List<EmplacementDTO> findEmplacementsByCodesFallback(Set<Integer> ids, String ipAdressSite) {
        log.error("falling back from find list of emplacements with codes: {}", ids);

        return null;
    }

// ----------------------------------------------------EmplacementServiceClient -------------------------------------------------------
    @HystrixCommand(fallbackMethod = "findTreeCategoriesArticlesByCategorieDepotFallback")
    public List<CategorieArticleDTO> findTreeCategoriesArticlesByCategorieDepot(String categ_depot, Boolean actif) {
        log.debug("Sending request find list of categorie article with categorie depot: {}", categ_depot);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uricategorie_articles)
                //                                .path("/searches")
                .queryParam("categorieDepot", categ_depot)
                .queryParam("actif", actif)
                .build()
                .encode();

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<CategorieArticleDTO>>() {
        }).getBody();
    }

    public List<CategorieArticleDTO> findTreeCategoriesArticlesByCategorieDepotFallback(String categ_depot, Boolean actif) {
        log.error("falling back from categorie article with categorie depot");
        List<CategorieArticleDTO> listArt = new ArrayList<>();
        CategorieArticleDTO art = new CategorieArticleDTO();
        listArt.add(art);

        return listArt;
    }
//
//    @HystrixCommand(fallbackMethod = "findNodesByCategorieArticleParentFallback")

    //TODO : Verify method name
    @HystrixCommand(fallbackMethod = "findNodesByCategorieArticleParentFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public List<CategorieArticleDTO> findNodesByCategorieArticleParent(Integer id) {
        log.debug("Sending request find list of categorie article with id: {}", id);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uricategorie_articles)
                .path("/" + id + "/nodes")
                .build()
                .encode();

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<CategorieArticleDTO>>() {
        }).getBody();
    }

    public List<CategorieArticleDTO> findNodesByCategorieArticleParentFallback(Integer id) {
        log.error("falling back from findNodesByCategorieArticleParent");
        List<CategorieArticleDTO> listArt = new ArrayList<>();
        CategorieArticleDTO art = new CategorieArticleDTO();
        listArt.add(art);

        return listArt;
    }

    @HystrixCommand(fallbackMethod = "articleByCategArtFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public List<ArticleDTO> articleByCategArt(Integer categorieArticleID) {
        log.debug("Sending request find list of categorie article by cathegorie article: {}", categorieArticleID);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticles)
                .path("/categorieArticle")
                .queryParam("categorie-article-id", categorieArticleID)
                .build()
                .encode();

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ArticleDTO>>() {
        }).getBody();
    }

    public List<ArticleDTO> articleByCategArtFallback(Integer categorieArticleID) {
        log.error("falling back find articles by categorie article");
        List<ArticleDTO> listArt = new ArrayList<>();
        return listArt;
    }

    @HystrixCommand(fallbackMethod = "articleByCategArtFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public List<ArticleIMMODTO> articleImmoByCategArt(Integer categorieArticleID) {
        log.debug("Sending request find list of categorie article by cathegorie article: {}", categorieArticleID);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriImmo)
                .path("/categorieArticle")
                .queryParam("categorie-article-id", categorieArticleID)
                .build()
                .encode();

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ArticleIMMODTO>>() {
        }).getBody();
    }

    public List<ArticleIMMODTO> articleImmoByCategArtArtFallback(Integer categorieArticleID) {
        log.error("falling back find articles immo by categorie article");
        List<ArticleIMMODTO> listArt = new ArrayList<>();
        return listArt;
    }

    @HystrixCommand(fallbackMethod = "categorieArticleECFindbyListIDFallback")
    public List<CategorieArticleDTO> categorieArticleECFindbyListId(List<Integer> id) {
        log.debug("Sending request find list of categorie article with list id: {}", id);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uricategorie_articles)
                .path("/searches")
                .build()
                .encode();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<List<Integer>> entity = new HttpEntity<>(id, headers);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<CategorieArticleDTO>>() {
        }).getBody();
    }

    public List<CategorieArticleDTO> categorieArticleECFindbyListIDFallback(List<Integer> id) {
        log.error("falling back from categorie article with categorie depot");
        List<CategorieArticleDTO> listArt = new ArrayList<>();
        CategorieArticleDTO art = new CategorieArticleDTO();
        listArt.add(art);

        return listArt;
    }

    @HystrixCommand(fallbackMethod = "findCategorieArticlesWithParentByCategorieDepotFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public Set<CategorieArticleDTO> findCategorieArticlesWithParentByCategorieDepot(CategorieDepotEnum categorieDepot, Boolean actif) {
        log.debug("find Categorie Articles With Parents By Categorie Depot");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uricategorie_articles)
                .path("/parentsByCategorieDepot")
                .queryParam("categorieDepot", categorieDepot)
                .queryParam("actif", actif)
                .build()
                .encode();

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Set<CategorieArticleDTO>>() {
        }).getBody();
    }

    public Set<CategorieArticleDTO> findCategorieArticlesWithParentByCategorieDepotFallback(CategorieDepotEnum categorieDepot, Boolean actif) {
        log.error("falling back from findCategorieArticlesWithParentByCategorieDepotFallback");
        return new HashSet();

    }

    @HystrixCommand(fallbackMethod = "findCategorieDepotFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})

    public CategorieDepotDTO findCategorieDepot(String id) {
        log.debug("Sending request findCategorieDepot");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uricategorie_depots)
                .path("/{id}")
                .buildAndExpand(id)
                .encode();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(id, headers);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, entity, new ParameterizedTypeReference<CategorieDepotDTO>() {
        }).getBody();

    }

    private CategorieDepotDTO findCategorieDepotFallBack(String id) {
        log.error("falling back from findCategorieDepot");
        CategorieDepotDTO dep = new CategorieDepotDTO();
        dep.setCode(id);

        dep.setDesignation("Not.Available");
        return dep;

    }

    @HystrixCommand(fallbackMethod = "findAllCategorieDepotFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})

    public List<CategorieDepotDTO> findAllCategorieDepot() {
        log.debug("Sending request findCategorieDepot");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uricategorie_depots)
                .build()
                .encode();

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<CategorieDepotDTO>>() {
        }).getBody();

    }

    private List<CategorieDepotDTO> findAllCategorieDepotFallBack() {
        log.error("falling back from findCategorieDepot");

        return new ArrayList();

    }

    @HystrixCommand(fallbackMethod = "findTvasFallBack")
    public List<TvaDTO> findTvas() {
        log.debug("Sending request to find Fournisseur by code: {}", baseUri + uritvas);
        return restTemplate.exchange(baseUri + uritvas, HttpMethod.GET, null, new ParameterizedTypeReference<List<TvaDTO>>() {
        }).getBody();
    }

    private List<TvaDTO> findTvasFallBack() {
        log.error("falling back findTvas");
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "findRootbyListCodeFallback")

    public List<CategorieArticleWithRoot> findRootbyListCode(List<Integer> codes) {
        if (codes.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find price liste by categorie article : {}", codes);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uricategorie_articles)
                    .path("/root/searches")
                    .build()
                    .encode();
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity<>(codes, header);
            log.debug("uriComponents {}", uriComponents.toString());
            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<CategorieArticleWithRoot>>() {
            }).getBody();
        }
    }

    private List<CategorieArticleWithRoot> findRootbyListCodeFallback(List<Integer> codes) {
        log.error("falling back from find price liste by categorie article ");
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "findArticleDepotbyCodeDepotAndCategDepotFallback")
    public List<ArticleDepotFixeDTO> findArticleDepotbyCodeDepotAndCategDepot(Integer codeDepot, CategorieDepotEnum categDepot) {

        log.debug("Sending request find ArticleDepot by depot Id : {}", codeDepot);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticleDepot)
                .path("/codeDepot")
                .queryParam("code-depot", codeDepot)
                .queryParam("categ-depot", categDepot)
                .build()
                .encode();
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<Integer> entity = new HttpEntity<>(codeDepot, header);
        log.debug("uriComponents {}", uriComponents.toString());
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<ArticleDepotFixeDTO>>() {
        }).getBody();
    }

    private List<ArticleDepotFixeDTO> findArticleDepotbyCodeDepotAndCategDepotFallback(Integer codeDepot, CategorieDepotEnum categDepot) {
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "findArticleDepotbyCodeDepotAndCodeArticlesInFallback")
    public List<ArticleDepotFixeDTO> findArticleDepotbyCodeDepotAndCodeArticlesIn(List<ArticleDepotFixeDTO> listeArticleDepot) {

        log.debug("Sending request find ArticleDepot ");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticleDepot)
                .path("/search")
                .build()
                .encode();
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<ArticleDepotFixeDTO>> entity = new HttpEntity<>(listeArticleDepot, header);
        log.debug("uriComponents {}", uriComponents.toString());
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleDepotFixeDTO>>() {
        }).getBody();
    }

    private List<ArticleDepotFixeDTO> findArticleDepotbyCodeDepotAndCodeArticlesInFallback(List<ArticleDepotFixeDTO> listeArticleDepot) {
        log.error("Sending request find ArticleDepot by codes  : {}", listeArticleDepot);
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "findMotifPaiementByCodesFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public Collection<MotifPaiementDTO> findMotifPaiementByCodes(Integer[] codes) {
        log.debug("Sending request to search for motif paiement");

        List<Integer> list = new ArrayList<>();
        list.addAll(Arrays.asList(codes));
        String language = LocaleContextHolder.getLocale().getLanguage();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<List<Integer>> entity = new HttpEntity<>(list, headers);

        return restTemplate.exchange(baseUri + uriMotifPaiement + "/searches", HttpMethod.POST, entity, new ParameterizedTypeReference<Collection<MotifPaiementDTO>>() {
        }).getBody();

    }

    public Collection<MotifPaiementDTO> findMotifPaiementByCodesFallBack(Integer[] codes) {
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "articleNonMvtByNonMovedFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public List<ArticleNonMvtDTO> articleNonMvtByNonMoved(Boolean nonMoved, String categDepot, Boolean actif) {
        log.debug("Sending request find list of article non mvt by nonMoved and categorie depot: {}", nonMoved, categDepot, actif);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticleNonMouvemente)
                .path("/nonMoved")
                .queryParam("non-moved", nonMoved)
                .queryParam("categorie-depot", categDepot)
                .queryParam("actif", actif)
                .build()
                .encode();

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ArticleNonMvtDTO>>() {
        }).getBody();
    }

    public List<ArticleNonMvtDTO> articleNonMvtByNonMovedFallback(Boolean nonMoved, String categDepot, Boolean actif) {
        log.error("falling back from article non mvt");
        List<ArticleNonMvtDTO> listArt = new ArrayList<>();
        return listArt;
    }

    /**
     * ** update article to mvt or not mvt
     *
     *******
     * @param listeArticleNonMvtsDTO
     * @return
     */
    @HystrixCommand(fallbackMethod = "updateListArticleNonMvtFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public ListeArticleNonMvtDTOWrapper updateListArticleNonMvt(ListeArticleNonMvtDTOWrapper listeArticleNonMvtsDTO) {

        log.debug("Sending request update list of article non mvts: {}", listeArticleNonMvtsDTO);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticleNonMouvemente)
                .build()
                .encode();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<ListeArticleNonMvtDTOWrapper> entity = new HttpEntity<>(listeArticleNonMvtsDTO, headers);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, new ParameterizedTypeReference<ListeArticleNonMvtDTOWrapper>() {
        }).getBody();

    }

    ListeArticleNonMvtDTOWrapper updateListArticleNonMvtFallback(ListeArticleNonMvtDTOWrapper listeArticleNonMvtsDTO) {
        log.error("Fallback update list of article non mvts");
        return new ListeArticleNonMvtDTOWrapper();
    }

    @HystrixCommand(fallbackMethod = "findMotifDemandeRedressementByIdFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000")
    })
    public MotifDemandeRedressementDTO findMotifDemandeRedressementById(Integer id) {
        log.debug("Sending request find Motif Demande Redressement By Id: {}", id);
        String language = LocaleContextHolder.getLocale().getLanguage();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<List<Integer>> entity = new HttpEntity<>(headers);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriMotifDemandeRedressement + "/" + id)
                .build()
                .encode();

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, entity, new ParameterizedTypeReference<MotifDemandeRedressementDTO>() {
        }).getBody();

    }

    private MotifDemandeRedressementDTO findMotifDemandeRedressementByIdFallback(Integer id) {
        log.error("falling back find Motif Demande Redressemen tBy Id Fallback");
        return new MotifDemandeRedressementDTO();

    }

    @HystrixCommand(fallbackMethod = "findMotifDemandeRedressementByCodesFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000")})
    public List<MotifDemandeRedressementDTO> findMotifDemandeRedressementByCodes(Set<Integer> ids) {
        log.debug("Sending request find Motif Demande Redressement with codes: {}", ids);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriMotifDemandeRedressement)
                .path("/searches")
                .build()
                .encode();
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<Integer>> entity = new HttpEntity(ids, header);

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<MotifDemandeRedressementDTO>>() {
        }).getBody();
    }

    private List<MotifDemandeRedressementDTO> findMotifDemandeRedressementByCodesFallback(Set<Integer> ids) {
        log.error("falling back from findDepotsByCodes");
        List<MotifDemandeRedressementDTO> listeMotifs = new ArrayList<>();
        for (Integer id : ids) {
            MotifDemandeRedressementDTO motif = new MotifDemandeRedressementDTO();
            motif.setId(id);
            motif.setDescription("Not Available");
            listeMotifs.add(motif);
        }
        return listeMotifs;
    }

    @HystrixCommand(fallbackMethod = "categorieArticleByIdFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public CategorieArticleDTO categorieArticleById(Integer categorieArticleID) {
        String language = LocaleContextHolder.getLocale().getLanguage();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<List<Integer>> entity = new HttpEntity<>(headers);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uricategorie_articles + "/" + categorieArticleID)
                .build()
                .encode();

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, entity, new ParameterizedTypeReference<CategorieArticleDTO>() {
        }).getBody();
    }

    public CategorieArticleDTO categorieArticleByIdFallback(Integer categorieArticleID) {
        log.error("falling back from categorie article by id");
        CategorieArticleDTO categorieArticleDTO = new CategorieArticleDTO();
        return categorieArticleDTO;
    }

    @HystrixCommand(fallbackMethod = "articleFindByListCategorieArticlesIdsFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<ArticleDTO> articleFindByListCategorieArticlesIds(List<Integer> categorieArticlesIds) {
        if (categorieArticlesIds.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find list of article with Categorie Articles Ids : {}", categorieArticlesIds);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticles)
                    .path("/categories-articles")
                    .build()
                    .encode();
            List<Integer> intList = new ArrayList<>();
            intList.addAll(categorieArticlesIds);
            HttpHeaders header = new HttpHeaders();
            String language = LocaleContextHolder.getLocale().getLanguage();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, language);
            HttpEntity<List<Integer>> entity = new HttpEntity<>(intList, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
            }).getBody();
        }
    }

    private List<ArticleDTO> articleFindByListCategorieArticlesIdsFallback(List<Integer> ids) {
        log.error("falling back from param achat articleFindByListCategorieArticlesIds");
        return null;
    }

    @HystrixCommand(fallbackMethod = "articleIMMOFindByListCategorieArticlesIdsFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<ArticleIMMODTO> articleIMMOFindByListCategorieArticlesIds(List<Integer> categorieArticlesIds) {
        if (categorieArticlesIds.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find list of article with Categorie Articles Ids : {}", categorieArticlesIds);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriImmo)
                    .path("/categories-articles")
                    .build()
                    .encode();
            List<Integer> intList = new ArrayList<>();
            intList.addAll(categorieArticlesIds);
            HttpHeaders header = new HttpHeaders();
            String language = LocaleContextHolder.getLocale().getLanguage();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, language);
            HttpEntity<List<Integer>> entity = new HttpEntity<>(intList, header);

            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleIMMODTO>>() {
            }).getBody();
        }
    }

    private List<ArticleIMMODTO> articleIMMOFindByListCategorieArticlesIdsFallback(List<Integer> ids) {
        log.error("falling back from param achat articleIMMOFindByListCategorieArticlesIdsFallback");
        return null;
    }

    @HystrixCommand(fallbackMethod = "palierMargeCategorieArticleByIdFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public List<PalierMargeCategorieArticleDTO> palierMargeCategorieArticleById(Integer codeMaquetteCategorieArticle) {
        String language = LocaleContextHolder.getLocale().getLanguage();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<List<Integer>> entity = new HttpEntity<>(headers);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriPalierMargeCategorieArticle)
                .path("/codeMaquette")
                .queryParam("codeMaquette", codeMaquetteCategorieArticle)
                .build()
                .encode();

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<PalierMargeCategorieArticleDTO>>() {
        }).getBody();
    }

    public List<PalierMargeCategorieArticleDTO> palierMargeCategorieArticleByIdFallback(Integer codeMaquetteCategorieArticle) {
        log.error("falling back from palier marge categorie article by id");
        return null;
    }

    @HystrixCommand(fallbackMethod = "searchesArticlesWithCategoriesParentsByListCodeArticlesFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "360000")}
    )
    public List<ArticleDTO> articlesWithCategoriesParentsByListCodeArticles(CategorieDepotEnum categorieDepot, Integer[] listCodeArticle) {

        log.debug("Sending request find list of article WithCategoriesParents by: {}", listCodeArticle.toString());
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticles)
                .path("/searches/findItemsWithCategoriesParents")
                .queryParam("categDepot", categorieDepot)
                .build()
                .encode();

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<Integer[]> entity = new HttpEntity<>(listCodeArticle, header);

        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
        }).getBody();

    }

    private List<ArticleDTO> searchesArticlesWithCategoriesParentsByListCodeArticlesFallback(CategorieDepotEnum categorieDepot, Integer[] listCodeArticle) {
        log.error("falling back from searches ArticlesWithCategoriesParentsByListCodeArticles ");
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "ArticleMinimumAndMaximumStockUpdateFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000")}
    )
    public void articleMinimumAndMaximumStockUpdate(CategorieDepotEnum categDepot, Integer codeRop, List<ArticleDTO> articleDTOs) {

        log.debug("Sending update the last minimun (ROP) and maximum : {}", articleDTOs);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticles)
                .path("/std-minimum-maximum-stock")
                .queryParam("categorieDepot", categDepot)
                .queryParam("codeRop", codeRop)
                .build()
                .encode();

        HttpEntity<List<ArticleDTO>> entity = new HttpEntity<>(articleDTOs, null);
        restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
        });
    }

    public void ArticleMinimumAndMaximumStockUpdateFallback(CategorieDepotEnum categDepot, Integer codeRop, List<ArticleDTO> articleDTOs) throws Exception {
        throw new Exception("problem from articleMinimumAndMaximumStockUpdate");

    }

    @HystrixCommand(fallbackMethod = "updateArticleFournisseurFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public void updateArticleFournisseur(BonRecepDTO bonrecep) {

        log.debug("Sending request find list of article with codes: {}", bonrecep);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriFournisseur)
                .path("/articlefrs/Update")
                .build()
                .encode();

        HttpEntity<BonRecepDTO> entity = new HttpEntity<>(bonrecep, null);

        restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
        });

    }

    public void updateArticleFournisseurFallback(BonRecepDTO bonrecep) throws Exception {
        throw new Exception("problem of updating");
    }

    @HystrixCommand(fallbackMethod = "ArticleFrsPriceReverttFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public void ArticleFrsPriceRevert(BonRecepDTO bonrecep) {

        log.debug("Sending request find list of article with codes: {}", bonrecep);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriArticles)
                .path("/pricing-article-revert")
                .build()
                .encode();

        HttpEntity<BonRecepDTO> entity = new HttpEntity<>(bonrecep, null);
        restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, new ParameterizedTypeReference<List<ArticleDTO>>() {
        });

    }

    public void ArticleFrsPriceReverttFallback(BonRecepDTO bonrecep) throws Exception {
        throw new Exception("problem of reverting");

    }

    @HystrixCommand(fallbackMethod = "purchasePricesIsInjectedFallBack")
    public Boolean purchasePricesIsInjected(CategorieDepotEnum categorieDepot) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriInjection)
                .path("/purchase-prices-is-injected")
                .queryParam("categDepot", categorieDepot)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Boolean>() {
        }).getBody();
    }

    private Boolean purchasePricesIsInjectedFallBack(CategorieDepotEnum categorieDepot) {
        log.error("falling back purchasePricesIsInjected");
        return null;
    }

    /**
     *
     * @return classification Article
     */
    @HystrixCommand(fallbackMethod = "classificationArticleFindAllFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})

    public List<ClassificationArticleDTO> classificationArticleFindAll() {
        log.debug("Sending request to find all classification articles");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriClassificationArticles)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ClassificationArticleDTO>>() {
        }).getBody();
    }

    private List<ClassificationArticleDTO> classificationArticleFindAllFallback() {
        throw new IllegalBusinessLogiqueException("error-loading-classificationArticle");
    }

    /**
     *
     * @return palier classification Article
     */
    @HystrixCommand(fallbackMethod = "palierClassificationArticleFindAllFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})

    public List<PalierClassificationArticleDTO> palierClassificationArticleFindAll() {
        log.debug("Sending request to find all classification articles");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriPalierClassificationArticles)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<PalierClassificationArticleDTO>>() {
        }).getBody();
    }

    private List<PalierClassificationArticleDTO> palierClassificationArticleFindAllFallback() {
        throw new IllegalBusinessLogiqueException("error-loading-palierclassificationArticle");
    }
}
