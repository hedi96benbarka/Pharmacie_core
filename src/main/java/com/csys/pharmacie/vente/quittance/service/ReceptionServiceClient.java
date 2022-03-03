/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.vente.quittance.dto.*;
import com.csys.pharmacie.achat.service.ReceivingService;
import com.csys.pharmacie.client.dto.AdmissionFacturationDTO;

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
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Farouk
 */
@RefreshScope
@Service("ReceptionServiceClient")
public class ReceptionServiceClient {

    private final Logger log = LoggerFactory.getLogger(ReceivingService.class);

    private final RestTemplate restTemplate;

    @Value("${receptionService.base-uri}")
    private String receptionServiceBaseUri;

    @Value("${receptionService.admission-facturations}")
    private String admissionFacturations;

    @Value("${receptionService.findSocieteByCodeAdmission}")
    private String findSocieteByCodeAdmission;

    @Value("${receptionService.findAdmissionByCodeInForDemandePEC}")
    private String findAdmissionByCodeInForDemandePEC;

    @Value("${receptionService.facturationQuittanceAvoir}")
    private String facturationQuittanceAvoir;

    @Value("${receptionService.deleteFacturationByNumQuittance}")
    private String deleteFacturationByNumQuittance;

    @Value("${receptionService.admissions}")
    private String admissions;

    public ReceptionServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "CheckEtatPatientInPatientFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100000"),
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public Boolean CheckEtatPatientInPatient(String codeAdmission) {
        log.debug("Sending request CheckEtatPatientInPatient: " + receptionServiceBaseUri + admissionFacturations + "/isEtatPatientResident" + "?codeAdmission=" + codeAdmission);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(receptionServiceBaseUri + admissionFacturations)
                .path("/isEtatPatientResident")
                .queryParam("codeAdmission", codeAdmission)
                .build()
                .encode();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<List<Integer>> entity = new HttpEntity<>(headers);
        ResponseEntity<Boolean> CheckEtatPatientInPatient = restTemplate.exchange(uriComponents.toUri(),
                HttpMethod.GET, entity, new ParameterizedTypeReference<Boolean>() {
        });
        return CheckEtatPatientInPatient.getBody();
    }

    public Boolean CheckEtatPatientInPatientFallback(String codeAdmission) {
        log.error("Sending request CheckEtatPatientInPatientFallback");
        checkBusinessLogique(false, "error.facturation.fallback");
        return false;
    }

    @HystrixCommand(fallbackMethod = "findAdmissionByCodeInForDemandePECFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100000"),
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public List<AdmissionDemandePECDTO> findAdmissionByCodeInForDemandePEC(List<String> codeAdmissions, Integer codeEtatPatient) {
        log.debug("Sending request findAdmissionByCodeInForDemandePEC: " + receptionServiceBaseUri + findAdmissionByCodeInForDemandePEC);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(receptionServiceBaseUri + findAdmissionByCodeInForDemandePEC)
                .queryParam("codeEtatPatient", codeEtatPatient)
                .build()
                .encode();
        HttpEntity<List<String>> entity = new HttpEntity<>(codeAdmissions);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<AdmissionDemandePECDTO>>() {
        }).getBody();
    }

    List<AdmissionDemandePECDTO> findAdmissionByCodeInForDemandePECFallback(List<String> codeAdmissions, Integer codeEtatPatient) {
        log.debug("Sending request findAdmissionByCodeInForDemandePEC");
        List<AdmissionDemandePECDTO> listAdmissionDemandePECDTO = new ArrayList<>();
        for (String id : codeAdmissions) {
            AdmissionDemandePECDTO admissionDemandePECDTO = new AdmissionDemandePECDTO();
            admissionDemandePECDTO.setCode(id);
            admissionDemandePECDTO.setNomCompletAr("Not Available");
            admissionDemandePECDTO.setNomCompletEn("Not Available");
            listAdmissionDemandePECDTO.add(admissionDemandePECDTO);
        }
        return listAdmissionDemandePECDTO;
    }

    @HystrixCommand(fallbackMethod = "findAdmissionNonClotureFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100000"),
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public List<AdmissionDemandePECDTO> findAdmissionNonCloture() {
        log.debug("Sending request admission non cloture: " + receptionServiceBaseUri + admissionFacturations);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(receptionServiceBaseUri + admissionFacturations)
                .path("/admission-non-cloture")
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<List<AdmissionDemandePECDTO>>() {
        }).getBody();
    }

    List<AdmissionDemandePECDTO> findAdmissionNonClotureFallback() {
        log.error("Sending request admission non cloture Fallback");
        checkBusinessLogique(false, "error.facturation.fallback");
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "findAdmissionFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100000"),
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public AdmissionFacturationDTO findAdmission(String numDossier) {
        log.debug("Sending request admission non cloture: " + receptionServiceBaseUri + admissionFacturations + "/" + numDossier);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(receptionServiceBaseUri + admissionFacturations + "/" + numDossier)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<AdmissionFacturationDTO>() {
        }).getBody();
    }

    AdmissionFacturationDTO findAdmissionFallback(String numDossier) {
        log.error("findAdmissionFallback");
        return null;
    }

    @HystrixCommand(fallbackMethod = "facturationQuittanceAvoirFallback", ignoreExceptions = IllegalBusinessLogiqueException.class, commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100000"),
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public FacturationPayementPharmacieDTO facturationQuittanceAvoir(QuittancePharmacieReglementDTO quittancePharmacieReglementDTO, String codeAdmission) {
        log.debug("Sending request facturationQuittanceAvoir: " + receptionServiceBaseUri + facturationQuittanceAvoir + "/" + codeAdmission);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(receptionServiceBaseUri + facturationQuittanceAvoir + "/" + codeAdmission)
                .build()
                .encode();
        HttpEntity<QuittancePharmacieReglementDTO> entity = new HttpEntity<>(quittancePharmacieReglementDTO);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<FacturationPayementPharmacieDTO>() {
        }).getBody();
    }

    FacturationPayementPharmacieDTO facturationQuittanceAvoirFallback(QuittancePharmacieReglementDTO quittancePharmacieReglementDTO, String codeAdmission/*, Throwable t*/) /*throws IOException*/ {
        log.error("Sending request facturationQuittanceAvoirFallback");
        checkBusinessLogique(false, "error.facturation.fallback");
        return null;
    }

    @HystrixCommand(fallbackMethod = "deleteFacturationByNumQuittanceFallback", ignoreExceptions = IllegalBusinessLogiqueException.class, commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100000"),
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public Boolean deleteFacturationByNumQuittance(Integer codeMotifSuppression, List<String> numquittances, String codeAdmission) {
        log.debug("Sending request deleteFacturationByNumQuittance: " + receptionServiceBaseUri + deleteFacturationByNumQuittance + "?codeMotifSuppression=" + codeMotifSuppression + "&codeAdmission=" + codeAdmission);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(receptionServiceBaseUri + deleteFacturationByNumQuittance)
                .queryParam("codeMotifSuppression", codeMotifSuppression)
                .queryParam("codeAdmission", codeAdmission)
                .build()
                .encode();
        HttpEntity<List<String>> entity = new HttpEntity<>(numquittances);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.DELETE, entity, new ParameterizedTypeReference<Boolean>() {
        }).getBody();
    }

    Boolean deleteFacturationByNumQuittanceFallback(Integer codeMotifSuppression, List<String> numquittances, String codeAdmission) {
        log.error("Sending request deleteFacturationByNumQuittanceFallback");
        checkBusinessLogique(false, "error.facturation.fallback");
        return null;
    }

    @HystrixCommand(fallbackMethod = "findSocieteByCodeAdmissionFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100000"),
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public SocieteDTO findSocieteByCodeAdmission(String codeAdmission) {
        log.debug("Sending request findSocieteByCodeAdmission: " + receptionServiceBaseUri + findSocieteByCodeAdmission + codeAdmission);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(receptionServiceBaseUri + findSocieteByCodeAdmission + codeAdmission)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<SocieteDTO>() {
        }).getBody();
    }

    SocieteDTO findSocieteByCodeAdmissionFallback(String codeAdmission) {
        log.error("Sending request findSocieteByCodeAdmissionFallback");
        checkBusinessLogique(false, "error.facturation.fallback");
        return null;
    }

    @HystrixCommand(fallbackMethod = "findAdmissionDTOByCodeAdmissionFallback", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100000"),
        @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public AdmissionDTO findAdmissionDTOByCodeAdmission(String codeAdmission) {
        log.debug("Sending request findAdmissionDTOByCodeAdmission: " + receptionServiceBaseUri + admissions + codeAdmission);
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(receptionServiceBaseUri + admissions + codeAdmission)
                .build()
                .encode();
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.GET, null, new ParameterizedTypeReference<AdmissionDTO>() {
        }).getBody();
    }

    AdmissionDTO findAdmissionDTOByCodeAdmissionFallback(String codeAdmission) {
        log.error("Sending request findAdmissionDTOByCodeAdmissionFallback");
        checkBusinessLogique(false, "error.facturation.fallback");
        return null;
    }

}
