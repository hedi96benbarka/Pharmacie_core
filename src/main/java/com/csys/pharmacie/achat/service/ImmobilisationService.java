/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;


import com.csys.pharmacie.client.dto.ImmobilisationDTO;
import com.csys.pharmacie.client.dto.ListeImmobilisationDTOWrapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author DELL
 */
@RefreshScope
@Service("ImmobilisationService")
public class ImmobilisationService {

    private final Logger log = LoggerFactory.getLogger(ImmobilisationService.class);
    private final RestTemplate restTemplate;

    public ImmobilisationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${immobilisation.base-uri}")
    private String baseUri;

    @Value("${immobilisation.immobilisations}")
    private String uriImmobilisations;

    @HystrixCommand(fallbackMethod = "saveImmoFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
    public ListeImmobilisationDTOWrapper saveImmo(ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper) {

        log.debug("Sending request to save Immo:{} ");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriImmobilisations)
                .build()
                .encode();

        HttpEntity<ListeImmobilisationDTOWrapper> entity = new HttpEntity<>(listeImmobilisationDTOWrapper, null);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<ListeImmobilisationDTOWrapper>() {
        }).getBody();

    }

    public ListeImmobilisationDTOWrapper saveImmoFallback(ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper) throws Exception {
        log.error("save Immobilisation Fallback");

        return null;

    }

    @HystrixCommand(fallbackMethod = "updateImmoFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")}
    )
     public ListeImmobilisationDTOWrapper  updateImmo(ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper) {

        log.debug("Sending request to save Immo: ");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriImmobilisations)
                .build()
                .encode();

        HttpEntity<ListeImmobilisationDTOWrapper> entity = new HttpEntity<>(listeImmobilisationDTOWrapper, null);
      return restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, entity, new ParameterizedTypeReference<ListeImmobilisationDTOWrapper>() {
        }).getBody();

    }

    public ListeImmobilisationDTOWrapper updateImmoFallback(ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper) throws Exception {
        log.error("update Immobilisation Fallback");
        return null;
    }

    @HystrixCommand(fallbackMethod = "findImmobilisationsBylisteCodeArticleFallBack", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")})
    public List<ImmobilisationDTO> findImmobilisationsBylisteCodeArticle(List<String> codeArticles, Boolean isNotUsed) {
        log.debug("Sending request to find Immobilisations");
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());

        HttpEntity<List<Integer>> entity = new HttpEntity(codeArticles, header);

        URI uri = UriComponentsBuilder.fromUriString(baseUri + uriImmobilisations)
                .path("/article/searches")
                .queryParam("isNotUsed", isNotUsed)
                .build()
                .encode()
                .toUri();

        return restTemplate.exchange(uri, HttpMethod.POST, entity, new ParameterizedTypeReference<List<ImmobilisationDTO>>() {
        }).getBody();

    }

    public List<ImmobilisationDTO> findImmobilisationsBylisteCodeArticleFallBack(List<String> codeArticles, Boolean isNotUsed) {
        log.error("falling back find liste immobilisations");
        return null;
    }

}
