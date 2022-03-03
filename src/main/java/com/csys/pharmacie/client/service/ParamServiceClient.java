/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.service;

import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.CostProfitCentreDTO;
import com.csys.pharmacie.achat.dto.DetailsPanierPrestDTO;
import static com.csys.pharmacie.achat.service.TransfertCompanyBranchService.actualCodeSiteTest;
import static com.csys.pharmacie.achat.service.TransfertCompanyBranchService.codeCompanyForBranchConfig;
import com.csys.pharmacie.client.dto.ModeReglementDTO;
import com.csys.pharmacie.client.dto.OperationDTO;
import com.csys.pharmacie.client.dto.SiteDTO;
import com.csys.pharmacie.vente.quittance.dto.ChambreDTO;
import com.csys.pharmacie.vente.quittance.dto.LitDTO;
import com.csys.pharmacie.vente.quittance.dto.PricelisteParCategorieArticleDTO;
import com.csys.pharmacie.vente.quittance.dto.RemiseConventionnelleDTO;
import com.csys.pharmacie.vente.quittance.dto.SocieteDTO;
import com.csys.util.Preconditions;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Farouk
 */
@RefreshScope
@Service("ParamServiceClient")
public class ParamServiceClient {

    private final Logger log = LoggerFactory.getLogger(ParamServiceClient.class);

    private final RestTemplate restTemplate;

    public static List<CliniqueDto> cliniqueDto = new ArrayList<>();
    
    protected final Environment env;

    @Value("${paramService.base-uri}")
    private String paramServiceBaseUri;

    @Value("${paramService.cliniques}")
    private String cliniques;

    @Value("${paramService.lits}")
    private String lits;

    @Value("${paramService.remise-price-list-categorie-article}")
    private String remisePriceListCategorieArticle;

    @Value("${paramService.medecins}")
    private String medecins;

    @Value("${paramService.chambres}")
    private String chambres;

    @Value("${paramService.societes}")
    private String societes;

    @Value("${paramService.details-panier-prestation}")
    private String detailsPanierBycodePrestation;
    
   @Value("${paramService.costCenter}")
   private String uriCostCenter;

       @Value("${paramService.modeReglement}")
    private String uriModeReglement;
       
    @Value("${paramService.operations}")
    private String uriOperations;

    @Value("${paramService.site}")
    private String uriSite;
    
   public static String ipAdressSiteCSHParamAchatConfig;

    @Value("${ipAdressSite-csh-ParamAchat}")
    public void setIpAdressSiteCSHParamAchatConfig(String ipAdressSiteParamAchat) {

        ipAdressSiteCSHParamAchatConfig = ipAdressSiteParamAchat;
    }
    
    public static String ipAdressSiteCLEOParamAchatConfig;

    @Value("${ipAdressSite-cleo-ParamAchat}")
    public void setIpAdressSiteCLEOParamAchatConfig(String ipAdressSiteParamAchat) {
        ipAdressSiteCLEOParamAchatConfig = ipAdressSiteParamAchat;
    }
    
    public ParamServiceClient(RestTemplate restTemplate, Environment env) {
        this.restTemplate = restTemplate;
        this.env = env;
    }

    @HystrixCommand(fallbackMethod = "CliniqueFindOneFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
        ,

        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<CliniqueDto> findClinique() {
        log.debug("Sending request findClinique: " + paramServiceBaseUri + cliniques);
        ResponseEntity<List<CliniqueDto>> liste = restTemplate.exchange(paramServiceBaseUri + cliniques,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<CliniqueDto>>() {
        });

        cliniqueDto = liste.getBody();
        return liste.getBody();
    }

    List<CliniqueDto> CliniqueFindOneFallback() {
        log.error("CliniqueFindOneFallback");
        checkBusinessLogique(false, "error.parametrage.fallback");
        return cliniqueDto;
    }

    // request for lit
    @HystrixCommand(fallbackMethod = "litsFindbyListCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")}
    )
    public List<LitDTO> litsFindbyListCode(List<String> ids) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find list of lits with codes: {}", ids);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(paramServiceBaseUri + lits)
                    .path("/findByCodeIn")
                    .build()
                    .encode();
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<String>> entity = new HttpEntity<>(ids, header);
            log.debug("uriComponents {}", uriComponents.toString());
            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<LitDTO>>() {
            }).getBody();
        }
    }

    @HystrixCommand(fallbackMethod = "litFindOneFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    })
    public LitDTO litFindOne(String id) {
        log.debug("Sending request to lit to findOne");
        ResponseEntity<LitDTO> lit = restTemplate.getForEntity(paramServiceBaseUri + lits + "/" + id, LitDTO.class);
        return lit.getBody();
    }

    private LitDTO litFindOneFallback(String id) {
        log.error("falling back litFindOneFallback");

        return null;
    }

    private List<LitDTO> litsFindbyListCodeFallback(List<String> ids) {
        log.error("falling back from lits FindbyListCode");
        checkBusinessLogique(false, "error.parametrage.fallback");
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "pricelisteParCategorieArticleFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
        ,

        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<PricelisteParCategorieArticleDTO> pricelisteParCategorieArticle(List<Integer> ids, String numdoss) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find price liste by categorie article : {}", ids);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(paramServiceBaseUri + remisePriceListCategorieArticle + "/findByCodesCategorieAndNumAdmission" + "?numAdmission=" + numdoss).build()
                    .encode();
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity<>(ids, header);
            log.debug("uriComponents {}", uriComponents.toString());
            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<PricelisteParCategorieArticleDTO>>() {
            }).getBody();
        }
    }

    private List<PricelisteParCategorieArticleDTO> pricelisteParCategorieArticleFallback(List<Integer> ids, String numdoss) {
        log.error("falling back from find price liste by categorie article ");
        checkBusinessLogique(false, "error.parametrage.fallback");
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "pricelisteParArticleFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
        ,

        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<RemiseConventionnelleDTO> pricelisteParArticle(List<Integer> ids, String numdoss, Boolean pharmacieExterne) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find price liste by categorie article : {}", ids);
            UriComponents uriComponents = UriComponentsBuilder
                    .fromUriString(paramServiceBaseUri + remisePriceListCategorieArticle + "/findByCodeAdmissionAndCodesArticle" + "?codeAdmission=" + numdoss + "&pharmacieExterne=" + pharmacieExterne)
                    .build()
                    .encode();
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity<>(ids, header);
            log.debug("uriComponents {}", uriComponents.toString());
            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<RemiseConventionnelleDTO>>() {
            }).getBody();
        }
    }

    private List<RemiseConventionnelleDTO> pricelisteParArticleFallback(List<Integer> ids, String numdoss, Boolean pharmacieExterne) {
        log.error("falling back from find price liste by categorie article ");
        checkBusinessLogique(false, "error.parametrage.fallback");
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "pricelisteParArticleFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
        ,

        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public List<RemiseConventionnelleDTO> pricelisteParArticle(List<Integer> ids, Integer codePriceList, Integer codeListCouverture,
            Integer codeNatureAdmission, Integer codeConvention, Boolean pharmacieExterne) {
        if (ids.isEmpty()) {
            return new ArrayList();
        } else {
            log.debug("Sending request find price liste by categorie article : {}", ids);

            UriComponents uriComponents = UriComponentsBuilder.fromUriString(paramServiceBaseUri + remisePriceListCategorieArticle + "/findRemiseConventionnelle")
                    .queryParam("codePriceList", codePriceList)
                    .queryParam("codeListCouverture", codeListCouverture)
                    .queryParam("codeConvention", codeConvention)
                    .queryParam("codeNatureAdmission", codeNatureAdmission)
                    .queryParam("pharmacieExterne", pharmacieExterne)
                    .build().encode();
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity<>(ids, header);
            log.debug("uriComponents {}", uriComponents.toString());
            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<RemiseConventionnelleDTO>>() {
            }).getBody();
        }
    }

    private List<RemiseConventionnelleDTO> pricelisteParArticleFallback(List<Integer> ids, Integer codePriceList, Integer codeListCouverture,
            Integer codeNatureAdmission, Integer codeConvention, Boolean pharmacieExterne) {
        log.error("falling back from find price liste by categorie article ");
        checkBusinessLogique(false, "error.parametrage.fallback");
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "detailsPanierBycodePrestationFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
        ,

        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public DetailsPanierPrestDTO detailsPanierBycodePrestation(Integer codePrestation) {
        log.debug("Sending request find detailsPanierBycodePrestation");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(paramServiceBaseUri + detailsPanierBycodePrestation + "?codePrestation=" + codePrestation)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<DetailsPanierPrestDTO>() {
        }).getBody();
    }

    private DetailsPanierPrestDTO detailsPanierBycodePrestationFallback(Integer codePrestation) {
        log.error("Sending request detailsPanierBycodePrestationFallback");
        checkBusinessLogique(false, "error.parametrage.fallback");
        return null;
    }

    @HystrixCommand(fallbackMethod = "findSocietebyCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
        ,
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public SocieteDTO findSocietebyCode(Integer codeSociete) {
        log.debug("Sending request find SocietebyCodeFallback");
        if (codeSociete == null) {
            return null;
        } else {
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(paramServiceBaseUri + societes + "/" + codeSociete + "/min")
                    .build()
                    .encode();
            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<SocieteDTO>() {
            }).getBody();
        }
    }

    private SocieteDTO findSocietebyCodeFallback(Integer codeSociete) {
        log.error("Sending request SocietebyCodeFallback");
        checkBusinessLogique(false, "error.parametrage.fallback");
        return null;
    }

    @HystrixCommand(fallbackMethod = "findCostProfitCentreByCodeInFallBack", commandProperties = {
       @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
   })
   public Collection<CostProfitCentreDTO> findCostProfitCentreByCodeIn(Integer[] codes) {
       log.debug("Sending request to findCostProfitCentreByCodeIn : {} ", (Object) codes);
       HttpEntity<Integer[]> httpEntity = new HttpEntity<>(codes);
       ResponseEntity<Collection<CostProfitCentreDTO>> result = restTemplate.exchange(paramServiceBaseUri + uriCostCenter + "/findByCodesIn", HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Collection<CostProfitCentreDTO>>() {
       });
       return result.getBody();
   }

   private Collection<CostProfitCentreDTO> findCostProfitCentreByCodeInFallBack(Integer[] codes) {
       log.error("falling back findCostProfitCentreByCodeInFallBack",(Object) codes);
       return null;
   }
    
   
    @HystrixCommand(fallbackMethod = "modeReglementFindOneFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public ModeReglementDTO modeReglementFindOne(Integer code) {
        log.debug("Sending request to modreg to findOne");

        String language = LocaleContextHolder.getLocale().getLanguage();
        log.debug("Language  : {}", language);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<ModeReglementDTO> modreg = restTemplate.exchange(paramServiceBaseUri + uriModeReglement + "/" + code, HttpMethod.GET, entity, ModeReglementDTO.class);
        return modreg.getBody();
    }

    public ModeReglementDTO modeReglementFindOneFallBack(Integer code) {
        ModeReglementDTO dto = new ModeReglementDTO();
        dto.setCode(0);
        dto.setDesignation("NA");
        return dto;
    }

        @HystrixCommand(fallbackMethod = "findModeReglementByCodesFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public Collection<ModeReglementDTO> findModeReglementByCodes(Integer[] codes) {
        log.debug("Sending request to search for mode reglements");

        List<Integer> list = new ArrayList<>();
        list.addAll(Arrays.asList(codes));
        String language = LocaleContextHolder.getLocale().getLanguage();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<List<Integer>> entity = new HttpEntity<>(list, headers);

        return restTemplate.exchange(paramServiceBaseUri + uriModeReglement + "/findByCodeIn", HttpMethod.POST, entity, new ParameterizedTypeReference<Collection<ModeReglementDTO>>() {
        }).getBody();

    }

    public Collection<ModeReglementDTO> findModeReglementByCodesFallBack(Integer[] codes) {
        return new ArrayList<>();
    }
    
    
    @HystrixCommand(fallbackMethod = "findOperationbyCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
        ,
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public OperationDTO findOperationbyCode(Integer codeOperation) {
        log.debug("Sending request find OperationbyCodeFallback");
        if (codeOperation == null) {
            return null;
        } else {
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(paramServiceBaseUri + uriOperations + "/" + codeOperation)
                    .build()
                    .encode();
            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<OperationDTO>() {
            }).getBody();
        }
    }

    private OperationDTO findOperationbyCodeFallback(Integer codeOperation) {
        log.error("Sending request OperationbyCodeFallback");
        checkBusinessLogique(false, "error.parametrage.fallback");
        return null;
    }

    
        private ChambreDTO chambreCodeFallback(Integer id) {
        log.error("falling back from chambre FindbyChambreCode");
        checkBusinessLogique(false, "error.parametrage.fallback");
        return new ChambreDTO();
    }
    @HystrixCommand(fallbackMethod = "chambreCodeFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")}
    )
    public ChambreDTO findChambreById(Integer id) {
   
            log.debug("Sending request find chambre: {}", id);
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(paramServiceBaseUri + chambres)
                    .path("/{id}")
                    .buildAndExpand(id)
                    .encode();
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<Integer> entity = new HttpEntity<>(id, header);
            log.debug("uriComponents {}", uriComponents.toString());
            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, entity, new ParameterizedTypeReference<ChambreDTO>() {
            }).getBody();
        }
    //+++++++++++++++++++++++++++++++++++++site++++++++++++++++++++++++++++++++++++++++++
    @HystrixCommand(fallbackMethod = "findAllSitesFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")})
    public Collection<SiteDTO> findAllSites(Boolean actif, String typeSite) {
        log.debug("Sending request to find all site by actif {} and type site {}", actif, typeSite);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(paramServiceBaseUri + uriSite)
                .queryParam("actif", actif)
                .queryParam("type", typeSite)
                .build()
                .encode();
        ResponseEntity<Collection<SiteDTO>> liste = restTemplate.exchange(uriComponents.toUri(),
                HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<Collection<SiteDTO>>() {
        });
        return liste.getBody();

    }

    Collection<SiteDTO> findAllSitesFallBack(Boolean actif, String typeSite) {
        log.error("falling back find all sites");
        return null;
    }

    @HystrixCommand(fallbackMethod = "findSiteByCodeFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")})
    public SiteDTO findSiteByCode(Integer code) {
        log.debug("Sending request to find  site by code {} ", code);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(paramServiceBaseUri + uriSite)
                .path("/{code}")
                .buildAndExpand(code)
                .encode();
        ResponseEntity<SiteDTO> site = restTemplate.exchange(uriComponents.toUri(),
                HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<SiteDTO>() {
        });
        return site.getBody();

    }

    SiteDTO findSiteByCodeFallBack(Integer code) {
        log.error("falling back find site by code {}", code);
        return null;
    }
    
    
    
    /************resolve site and company*************************************/
   protected Boolean isItsComapny(Integer codeBranch) {
        log.debug("resolve isItsComapny : {}", codeBranch);
        Integer actualCodeSite;
        if (env.acceptsProfiles("testCentral")) {
            actualCodeSite = actualCodeSiteTest;
            return codeCompanyForBranchConfig.equals(actualCodeSiteTest);
        } else {
            List<CliniqueDto> cliniqueDtos = findClinique();
            Preconditions.checkBusinessLogique(cliniqueDtos != null, "parametrage.clinique.error");
            actualCodeSite = cliniqueDtos.get(0).getCodeSite();
            SiteDTO codeSiteBranch = findSiteByCode(codeBranch);
            Preconditions.checkBusinessLogique(codeSiteBranch != null, "parametrage.clinique.error");
            return codeSiteBranch.getCodeCompany().equals(actualCodeSite);
        }
    }

    protected Integer resolveCodeSite() {
        log.debug("resolve codeSite from table clinique : {}");
        Integer actualCodeSite;
        if (env.acceptsProfiles("testCentral")) {
            actualCodeSite = actualCodeSiteTest;

        } else {
            List<CliniqueDto> cliniqueDtos = findClinique();
            Preconditions.checkBusinessLogique(cliniqueDtos != null, "parametrage.clinique.error");
            actualCodeSite = cliniqueDtos.get(0).getCodeSite();
        }
        return actualCodeSite;
    }
    
    
    public String resolveIpAdresseSite(Integer codeSite) {
        String ipAdressSite = null;
        if (codeSite == null) {
            return null;
        }


        if ( !env.acceptsProfiles("testCentral")) {
            SiteDTO siteDTO = findSiteByCode(codeSite);
            Preconditions.checkBusinessLogique(siteDTO != null, "parametrage.site.error");
            ipAdressSite = siteDTO.getIpAdress();
        } else if ( env.acceptsProfiles("testCentral")) {
            if (codeSite.equals(9)) {
                ipAdressSite = ipAdressSiteCSHParamAchatConfig;
            } else if (codeSite.equals(10)) {
                ipAdressSite = ipAdressSiteCLEOParamAchatConfig;
            }
        }
        return ipAdressSite;
    } 
    
//    //+++++++++++++++++++++++++++++++++++++company++++++++++++++++++++++++++++++++++++++++++
//    @HystrixCommand(fallbackMethod = "findAllCompaniesFallBack", commandProperties = {
//        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")})
//    public Collection<CompanyDTO> findAllCompanies(Boolean actif) {
//        log.debug("Sending request to find all companies by actif {} ", actif);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
//        UriComponents uriComponents = UriComponentsBuilder.fromUriString(paramCentralServiceBaseUri + uriCompany)
//                .queryParam("actif", actif)
//                .build()
//                .encode();
//        ResponseEntity<Collection<CompanyDTO>> liste = restTemplate.exchange(uriComponents.toUri(),
//                HttpMethod.GET, new HttpEntity<>(null, headers), new ParameterizedTypeReference<Collection<CompanyDTO>>() {
//        });
//        return liste.getBody();
//
//    }
//
//    Collection<CompanyDTO> findAllCompaniesFallBack(Boolean actif) {
//        log.error("falling back find all companies");
//        return null;
//    }
}

