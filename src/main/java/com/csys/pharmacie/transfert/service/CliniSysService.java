package com.csys.pharmacie.transfert.service;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.helper.AccessControlEmailDTO;
import com.csys.pharmacie.helper.EmailDTO;
import com.google.common.base.Preconditions;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrateur
 */
@RefreshScope
@Service("clinisys")
public class CliniSysService {

    private final Logger log = LoggerFactory.getLogger(CliniSysService.class);

    private final RestTemplate restTemplate;

    @Value("${cliniSys.base-uri}")
    private String baseUri;

    @Value("${cliniSys.sendEmails}")
    private String sendEmailsUri;

    @Value("${cliniSys.searchEmails}")
    private String searchEmailsUri;

    @Value("${cliniSys.senderAdress}")
    private String senderAdress;

    public CliniSysService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EmailDTO prepareEmail(List<String> users, EmailDTO emailDTO) {

        List<AccessControlEmailDTO> accessControlEmailDTOs = this.searchEmails(users);
        log.debug("accessControlEmailDTOs {}", accessControlEmailDTOs.toString());

        List<String> emails = accessControlEmailDTOs.stream().map(itm -> itm.getEmail())
                .filter(x -> x != null).collect(Collectors.toList());
        Preconditions.checkArgument(!emails.isEmpty(), "pas-d-emails");
        if (!emails.isEmpty()) {
            emailDTO.setTo(emails);
            emailDTO.setFrom(senderAdress);
            emailDTO.setUseHtmlInBody(Boolean.FALSE);
            this.sendMail(emailDTO);
        }
        return emailDTO;
    }

    @HystrixCommand(fallbackMethod = "sendMailFallBack", commandProperties = @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000"))
    public void sendMail(EmailDTO email) {
        log.debug("Sending request to send emails ");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + sendEmailsUri)
                .build();
        restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, new HttpEntity(email), Boolean.class).getBody();
    }

    private void sendMailFallBack(EmailDTO email) {
        throw new IllegalBusinessLogiqueException("mailing-error", new Throwable(email.getTo().toString()));

    }

    @HystrixCommand(fallbackMethod = "searchEmailsFallBack", commandProperties = @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000"))
    public List<AccessControlEmailDTO> searchEmails(List<String> users) {
        log.debug("Sending request to find emails");
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(baseUri + searchEmailsUri)
                .build();

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().getLanguage());
        HttpEntity<List<String>> entity = new HttpEntity<>(users, header);
        return restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST, entity, new ParameterizedTypeReference<List<AccessControlEmailDTO>>() {
        }).getBody();
    }

    private List<AccessControlEmailDTO> searchEmailsFallBack(List<String> users) {
        return new ArrayList<>();

    }

}
