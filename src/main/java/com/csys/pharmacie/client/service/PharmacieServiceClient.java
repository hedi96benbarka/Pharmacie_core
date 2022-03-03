///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.csys.pharmacie.client.service;
//
//import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponents;
//import org.springframework.web.util.UriComponentsBuilder;
//
//@RefreshScope
//@Service
//public class PharmacieServiceClient {
//
//    private final Logger log = LoggerFactory.getLogger(PharmacieServiceClient.class);

//    private final RestTemplate restTemplate;

//    @Value("${pharmacieService.uri}")
//    private String uri;

    
//     @Value("${pharmacieService.transfert-company-banch}")
//    private String uriTransfertCompany;
//     
//    public PharmacieServiceClient(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }



//    @HystrixCommand(fallbackMethod = "UpdateReplicatedTransfertFallback", commandProperties = {
//        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
//    })
//    public TransfertCompanyBranchDTO updateReplicatedTransfert(String numBonTransfert, String ipSite) {
//        log.debug("find Fournisseur By Code From Branche with code {}, ip Site {}", numBonTransfert, ipSite);
//        UriComponents uriComponents = UriComponentsBuilder.fromUriString("http://" + ipSite + uri + uriTransfertCompany)
//                .path("/{numBonTransfert}")
//                .buildAndExpand(numBonTransfert)
//                .encode();
//
//        ResponseEntity<TransfertCompanyBranchDTO> service = restTemplate.exchange(uriComponents.toUri(), HttpMethod.PUT, new HttpEntity(null, new HttpHeaders()), new ParameterizedTypeReference<TransfertCompanyBranchDTO>() {
//        });
//        return service.getBody();
//    }
//
//    TransfertCompanyBranchDTO UpdateReplicatedTransfertFallback(String numBonTransfert, String ipSite) {
//        log.error("UpdateReplicatedTransfertFallback falling back");
//        return null;
//    }

//}
