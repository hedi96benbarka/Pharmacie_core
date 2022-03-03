/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.service;

import com.csys.pharmacie.client.dto.DeviseDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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
 * @author admin
 */
@RefreshScope
@Service("CaisseServiceClient")
public class CaisseServiceClient {

    private final Logger log = LoggerFactory.getLogger(ParamAchatServiceClient.class);

    private final RestTemplate restTemplate;

    @Value("${caisseService.base-uri}")
    private String baseUri;

    @Value("${caisseService.devises}")
    private String uriDevises;

    public CaisseServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "findDeviseByIdFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")})

    public DeviseDTO findDeviseById(String code) {
        log.debug("Sending request to find  Devise By id: {}", code);
        String language = LocaleContextHolder.getLocale().getLanguage();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Language", language);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + uriDevises + "/" + code)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, entity, new ParameterizedTypeReference<DeviseDTO>() {
        }).getBody();

    }

    private DeviseDTO findDeviseByIdFallback(String code) {
        log.error("falling back find devise ");
        return null;
    }
}
