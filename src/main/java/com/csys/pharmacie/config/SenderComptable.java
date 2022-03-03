/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csys.pharmacie.config;

import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.FactureDirecteDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 *
 * @author USER
 */
@Service
public class SenderComptable {
    
    private final Environment env;

    public SenderComptable(Environment env) {
        this.env = env;
    }
    private static final Logger log = LoggerFactory.getLogger(SenderComptable.class);
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String, FactureDirecteDTO> kafkaTemplateFactureDirecte;
//    @Autowired
//    private KafkaTemplate<String, BonRecepDTO> kafkaTemplateBonReceptionOnShelf;
//    
//    @Autowired
//    private KafkaTemplate<String,TransfertCompanyBranchDTO > kafkaTemplateTransferBranchToCompanyManagement;
//    
    @Value("${environnement}")
    private String environnement;

    @Async
    public void send(String topic, String key, FactureDirecteDTO message) {//topic key message
        if (Arrays.stream(env.getActiveProfiles()).anyMatch("kafka"::equals)) {
            log.debug("***************send message FactureDirecteDTO ******************************");
         
            kafkaTemplateFactureDirecte.send(topic, key, message).addCallback(new ListenableFutureCallback<SendResult<String, FactureDirecteDTO>>() {

                        @Override
                        public void onSuccess(SendResult<String, FactureDirecteDTO> result) {
                            log.info("sent FactureDirecteDTO message  ='{}' with offset={}", message,
                                    result.getRecordMetadata().offset());
                        }

                        @Override
                        public void onFailure(Throwable ex) {
                            log.error("unable to send message FactureDirecteDTO  ='{}'", message, ex);
                            prepareAndSendMail(message, ex);
                        }
                    });
        }
    }

    public void prepareAndSendMail(Object message, Throwable ex) {
        log.debug("Request to prepareAndSendMail");
        String subject = environnement + ": pharmacie sending message exception";
        String body = "Unable to send message:  ";
        body += message.toString() + " \n";
        body += "Exception: " + ex;

        log.debug("subject = {}", subject);
        log.debug("body = {}", body);
//        String result = mailSender.(mailFrom, mailsRecipientProducerFailure, subject, body);
    }
    
}
