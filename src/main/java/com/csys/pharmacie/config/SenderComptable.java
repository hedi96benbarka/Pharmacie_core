/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csys.pharmacie.config;

import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.FactureDirecteDTO;
import com.csys.pharmacie.achat.dto.KafkaProducerErrorGecDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.service.KafkaProducerErrorGecService;
import java.time.LocalDateTime;
import java.util.Arrays;

import com.csys.pharmacie.transfert.dto.FactureBEDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 *
 * @author USER
 */
@Component
public class SenderComptable {

    private final Environment env;

    public SenderComptable(Environment env) {
        this.env = env;
    }
    private static final Logger log = LoggerFactory.getLogger(SenderComptable.class);

    @Autowired
    private KafkaTemplate<String, FactureDirecteDTO> kafkaTemplateFactureDirecte;

    @Autowired
    private KafkaProducerErrorGecService kafkaProducerErrorGecService;

    @Autowired
    private KafkaTemplate<String, FactureBEDTO> kafkaTemplateFactureBE;

//    @Autowired
////    @Qualifier("MailSender")
//    public MailSender mailSender;

    @Value("${mails-recipient-producer-Gec-failure}")
    private String[] mailsRecipientProducerGecFailure;

    @Value("${mailFrom}")
    private String mailFrom;
//    @Autowired
//    private KafkaTemplate<String, BonRecepDTO> kafkaTemplateBonReceptionOnShelf;
//    
//    @Autowired
//    private KafkaTemplate<String,TransfertCompanyBranchDTO > kafkaTemplateTransferBranchToCompanyManagement;
//    
    @Value("${environnement}")
    private String environnement;

    @Async
    public void sendDirectBill(String topic, String key, FactureDirecteDTO message) {//topic key message
        if (Arrays.stream(env.getActiveProfiles()).anyMatch("kafka"::equals)) {
            log.debug("***************send message FactureDirecteDTO ******************************");
           
            try{
                kafkaTemplateFactureDirecte.send(topic, key, message)
                    .addCallback(new ListenableFutureCallback<SendResult<String, FactureDirecteDTO>>() {

                @Override
                public void onSuccess(SendResult<String, FactureDirecteDTO> result) {
                    log.info("sent FactureDirecteDTO message  ='{}' with offset={}", message,
                            result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(Throwable ex) {
                            log.error("unable to send message='{}'", message, ex);

                    recoverErrorAndSendMail(topic, key, message, ex);

                }
            });
                
            }
            catch (Exception ex){
                 log.error("unable to send message='{}'", message, ex);

                    recoverErrorAndSendMail(topic, key, message, ex);
            }
            
        }
    }

    @Async
    public void sendRedressementBill(String topic, String key, FactureBEDTO message) {//topic key message
        if (Arrays.stream(env.getActiveProfiles()).anyMatch("kafka"::equals)) {
            log.debug("***************send message FactureBEDTO ******************************");

            try{
                kafkaTemplateFactureBE.send(topic, key, message)
                        .addCallback(new ListenableFutureCallback<SendResult<String, FactureBEDTO>>() {

                            @Override
                            public void onSuccess(SendResult<String, FactureBEDTO> result) {
                                log.info("sent redessement  bill message  ='{}' with offset={}", message,
                                        result.getRecordMetadata().offset());
                            }

                            @Override
                            public void onFailure(Throwable ex) {
                                log.error("unable to send message='{}'", message, ex);

                                recoverErrorAndSendMail(topic, key, message, ex);

                            }
                        });

            }
            catch (Exception ex){
                log.error("unable to send message='{}'", message, ex);

                recoverErrorAndSendMail(topic, key, message, ex);
            }

        }
    }
//@Async
//public void send(String topic, String key, FactureDirecteDTO message) {//topic key message
//if (Arrays.stream(env.getActiveProfiles()).anyMatch("kafka"::equals)) {
//log.debug("***************send message ArticleECDTO {} ******************************", message);
//
//kafkaTemplateArticleEC.send(topic, key, message)
//.addCallback(new ListenableFutureCallback<SendResult<String, ArticleECDTO>>() {
//
//@Override
//public void onSuccess(SendResult<String, ArticleECDTO> result) {
//log.info("sent message='{}' with offset={}", message,
//result.getRecordMetadata().offset());
//}
//
//@Override
//public void onFailure(Throwable ex) {
//log.error("unable to send message='{}'", message, ex);
//recoverErrorAndSendMail(topic, key, message, ex);
//}
//});
//}
//}
    public void recoverErrorAndSendMail(String topic, String key, Object message, Throwable exc) {
        log.debug("Request to prepareAndSendMail");

        /**
         * save kafkaProducerErrorDTO
         */
        KafkaProducerErrorGecDTO kafkaProducerErrorGecDTO = new KafkaProducerErrorGecDTO();
        kafkaProducerErrorGecDTO.setTopic(topic);
        kafkaProducerErrorGecDTO.setRecord(message.toString());
        kafkaProducerErrorGecDTO.setRecordKey(key);
        kafkaProducerErrorGecDTO.setTryCount(1);
        kafkaProducerErrorGecDTO.setCreateTime(LocalDateTime.now());
        kafkaProducerErrorGecDTO.setHandled(Boolean.FALSE);
        String errorMessage;
        if (exc.getCause() != null && exc.getMessage() != null) {
            errorMessage = String.valueOf("exception cause est :").concat(exc.getCause().toString()).concat(" message exception est : ").concat(exc.getMessage()).concat(" message stack  est : ").concat(Arrays.toString(exc.getStackTrace()));
        } else {
            errorMessage = String.valueOf(" message stack  est : ").concat(Arrays.toString(exc.getStackTrace()));
        }
        kafkaProducerErrorGecDTO.setExceptionDetails(errorMessage);
        log.debug("******* RECOVERING: save kafkaProducerGecError ********** {}", kafkaProducerErrorGecDTO.toString());
        kafkaProducerErrorGecService.save(kafkaProducerErrorGecDTO);

        // send Email
        String subject = environnement + ": pharmacie core sending message exception";
        String body = "Unable to send message:  ";
        body += message.toString() + " \n";
        body += "Exception: " + exc;

        log.debug("subject = {}", subject);
        log.debug("body = {}", body);
//        String result = mailSender.sendMail(mailFrom, mailsRecipientProducerGecFailure, subject, body);
    }

//    public void prepareAndSendMail(Object message, Throwable ex) {
//        log.debug("Request to prepareAndSendMail");
//        String subject = environnement + ": pharmacie sending message exception";
//        String body = "Unable to send message:  ";
//        body += message.toString() + " \n";
//        body += "Exception: " + ex;
//
//        log.debug("subject = {}", subject);
//        log.debug("body = {}", body);
////        String result = mailSender.(mailFrom, mailsRecipientProducerFailure, subject, body);
//    }
}
