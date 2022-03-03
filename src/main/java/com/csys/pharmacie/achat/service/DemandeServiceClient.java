/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
import com.csys.pharmacie.achat.dto.CommandeAchatLazyDTO;
import com.csys.pharmacie.achat.dto.CommandeAchatModeReglementDTO;
import com.csys.pharmacie.achat.dto.DemandeRedressementDTO;
import com.csys.pharmacie.achat.dto.DemandeTrDTO;
import com.csys.pharmacie.prelevement.dto.DemandePrDTO;
import static com.csys.util.Preconditions.checkBusinessLogique;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Farouk
 */
@Service("DemandeServiceClient")
public class DemandeServiceClient {

    private final Logger log = LoggerFactory.getLogger(DemandeServiceClient.class);

    @Value("${demandeService.base-uri}")
    private String baseUri;
    @Value("${demandeService.commandeachats}")
    private String uricommandeachats;
    @Value("${demandeService.commandeachatsbycode}")
    private String commandeachatsbycode;
    @Value("${demandeService.demandetrs}")
    private String uridemandetrs;
    @Value("${demandeService.demandeprs}")
    private String uridemandeprs;
    @Value("${demandeService.commandeachatmodereglements}")
    private String uricommandeachatmodereglements;
    @Value("${demandeService.demanderedressement}")
    private String uridemanderedressement;

    private final RestTemplate restTemplate;

    public DemandeServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "findListCommandeAchatFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    })
    public Set<CommandeAchatDTO> findListCommandeAchat(List<Integer> codes, String language) {
        log.debug("Sending request findListCommandeAchat");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<List<Integer>> entity = new HttpEntity<>(codes, headers);
        return restTemplate.exchange(baseUri + uricommandeachats, HttpMethod.POST, entity, new ParameterizedTypeReference<Set<CommandeAchatDTO>>() {
        }).getBody();
    }

    Set<CommandeAchatDTO> findListCommandeAchatFallback(List<Integer> code, String language) {
        log.error("falling back findListCommandeAchatFallback");
        return new HashSet<CommandeAchatDTO>();
    }

    @HystrixCommand(fallbackMethod = "findListDemandeTrFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    })
    public List<DemandeTrDTO> findListDemandeTr(Collection<Integer> codes, String language, Boolean lazy) {
        log.debug("Sending request findListDemandeTr");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uridemandetrs)
                .path("/searches")
                .queryParam("lazy", lazy)
                .build()
                .encode();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<List<Integer>> entity = new HttpEntity(codes, headers);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<DemandeTrDTO>>() {
        }).getBody();
    }

    List<DemandeTrDTO> findListDemandeTrFallback(Collection<Integer> code, String language, Boolean lazy) {
        log.error("falling back findListDemandeTrFallback");
        return new ArrayList();
    }

    @HystrixCommand(fallbackMethod = "createCommandeAchatFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
        ,@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public CommandeAchatDTO createCommandeAchat(CommandeAchatDTO commandeAchatDTO) {
        log.debug("Sending request createCommandeAchat");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + commandeachatsbycode)
                .path("/manuel")
                .queryParam("on-shelf", true)
                .build()
                .encode();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<List<Integer>> entity = new HttpEntity(commandeAchatDTO, headers);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<CommandeAchatDTO>() {
        }).getBody();
    }

    CommandeAchatDTO createCommandeAchatFallback(CommandeAchatDTO commandeAchatDTO) {
        log.error("falling back createCommandeAchatFallback");
        checkBusinessLogique(false, "error.demande.fallback");
        return null;
    }

    @HystrixCommand(fallbackMethod = "deleteCommandeAchatFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
        ,@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public Void deleteCommandeAchat(String codeCommandeAchats, Boolean permanent) {
        log.debug("Sending request createDemandeAchat");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + commandeachatsbycode)
                .path(codeCommandeAchats)
                .queryParam("permanent", permanent)
                .build()
                .encode();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<List<Integer>> entity = new HttpEntity(headers);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.DELETE, entity, new ParameterizedTypeReference<Void>() {
        }).getBody();
    }

    Void deleteCommandeAchatFallback(CommandeAchatDTO commandeAchatDTO) {
        log.error("falling back deleteCommandeAchatFallback");
        checkBusinessLogique(false, "error.demande.fallback");
        return null;
    }

//--
    @HystrixCommand(fallbackMethod = "findListDemandePrFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    })
    public Set<DemandePrDTO> findListDemandePr(List<Integer> codes, String language) {
        log.debug("Sending request findListDemandePr");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uridemandeprs)
                .path("/searches")
                .queryParam("eager", true)
                .build()
                .encode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<List<Integer>> entity = new HttpEntity<>(codes, headers);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<Set<DemandePrDTO>>() {
        }).getBody();
    }

    Set<DemandePrDTO> findListDemandePrFallback(List<Integer> code, String language) {
        log.error("falling back findListDemandeTrFallback");
        return new HashSet<DemandePrDTO>();
    }

    @HystrixCommand(fallbackMethod = "findListCommandeAchatLazyFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    })
    public Set<CommandeAchatLazyDTO> findListCommandeAchatLazy(List<Integer> codes, String language) {
        log.debug("Sending request findListCommandeAchat");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<List<Integer>> entity = new HttpEntity<>(codes, headers);
        return restTemplate.exchange(baseUri + uricommandeachats, HttpMethod.POST, entity, new ParameterizedTypeReference<Set<CommandeAchatLazyDTO>>() {
        }).getBody();
    }

    Set<CommandeAchatLazyDTO> findListCommandeAchatLazyFallback(List<Integer> code, String language) {
        log.error("falling back findListCommandeAchatFallback");
        return new HashSet<CommandeAchatLazyDTO>();
    }

    @HystrixCommand(fallbackMethod = "findCommandeAchatFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    })
    public CommandeAchatDTO findCommandeAchat(Integer code, String language) {
        log.debug("Sending request findCommandeAchat");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<List<Integer>> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(baseUri + commandeachatsbycode + "/" + code, HttpMethod.GET, entity, new ParameterizedTypeReference<CommandeAchatDTO>() {
        }).getBody();
    }

    CommandeAchatDTO findCommandeAchatFallback(Integer code, String language) {
        log.error("falling back findCommandeAchat");
        return null;
    }



    @HystrixCommand(fallbackMethod = "findListCommandeAchatModeReglementDTOByCodeCaInFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    })
    public List<CommandeAchatModeReglementDTO> findListCommandeAchatModeReglementDTOByCodeCaIn(List<Integer> codesCa) {
        log.debug("Sending request findListCommandeAchatModeReglementDTOByCodeCaIn");
        if (codesCa.isEmpty()) {
            return new ArrayList();
        } else {
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uricommandeachatmodereglements)
                    .path("/searches")
                    .build()
                    .encode();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
            HttpEntity<List<Integer>> entity = new HttpEntity<>(codesCa, headers);
            return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<CommandeAchatModeReglementDTO>>() {
            }).getBody();
        }
    }
    
    List<CommandeAchatModeReglementDTO> findListCommandeAchatModeReglementDTOByCodeCaInFallback(List<Integer> codesCa) {
        log.error("falling back findListCommandeAchatModeReglementDTOByCodeCaInFallback");
        return new ArrayList<CommandeAchatModeReglementDTO>();
    }
    //--
    @HystrixCommand(fallbackMethod = "updateSatisfiedDemandeRedressementFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    })
    public DemandeRedressementDTO UpdateSatisfiedDemande(Integer numeroDemande, String numbonRedressement) {
        log.debug("Sending request to update satisied Demande redressement ");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uridemanderedressement)
                .path("/satisfied")
                .queryParam("numeroDemandeRedressement", numeroDemande)
                .queryParam("numBonRedressement", numbonRedressement)
                .build()
                .encode();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<Integer> entity = new HttpEntity<>(numeroDemande, headers);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, new ParameterizedTypeReference<DemandeRedressementDTO>() {
        }).getBody();
    }

    DemandeRedressementDTO updateSatisfiedDemandeRedressementFallback(Integer numeroDemande, String numbonRedressement) {
        log.error("falling back updateSatisfiedDemandeRedressementFallback ");
        return null;
    }

    @HystrixCommand(fallbackMethod = "findDemandeRedressementFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    })
    public DemandeRedressementDTO findDemandeRedressement(Integer code, String language) {
        log.debug("Sending request findDemandeRedressement");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<List<Integer>> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(baseUri + uridemanderedressement + "/" + code, HttpMethod.GET, entity, new ParameterizedTypeReference<DemandeRedressementDTO>() {
        }).getBody();
    }

    DemandeRedressementDTO findDemandeRedressementFallback(Integer code, String language) {
        log.error("falling back findDemandeRedressement");
        return null;
    }
    
    
    
   /*    @HystrixCommand(fallbackMethod = "updateCommandesAchatByCodesInFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    })*/
  public Set<String> updateCommandesAchatByCodesIn(List<Integer> codes,String codeFrs)
  {   
     log.debug("Sending request updateCommandesAchatByCodesIn");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + "/api/commandeachats")
                .path("/update")
                .queryParam("codeFrs", codeFrs)
                .build()
                .encode();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<Integer>> entity = new HttpEntity<>(codes, headers);
           return restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, new ParameterizedTypeReference<Set<String>>() {
        }).getBody();
  }
  
    public List<Integer> updateCommandesByCodesIn(List<Integer> codes,String numbon)
  {   
     log.debug("Sending request updateCommandesAchatByCodesIn");
     log.debug("numbon {}",numbon);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + "/api/commandeachats")
                .path("/update-facture")
                .queryParam("numbon", numbon)
                .build()
                .encode();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<Integer>> entity = new HttpEntity<>(codes, headers);
           return restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, new ParameterizedTypeReference<List<Integer>>() {
        }).getBody();
  }


}
