/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.config;

import com.csys.pharmacie.achat.dto.BonRecepDTO;
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
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 *
 * @author ASUS
 */
@Component
public class Sender {

    private final Environment env;

    public Sender(Environment env) {
        this.env = env;
    }
    private static final Logger log = LoggerFactory.getLogger(Sender.class);
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String, TransfertCompanyBranchDTO> kafkaTemplateTransferCompanyToBranch;
    @Autowired
    private KafkaTemplate<String, BonRecepDTO> kafkaTemplateBonReceptionOnShelf;
    
    @Autowired
    private KafkaTemplate<String,TransfertCompanyBranchDTO > kafkaTemplateTransferBranchToCompanyManagement;
    
    @Value("${environnement}")
    private String environnement;
//        @Autowired
//    @Qualifier("MailSender")
//    public MailSender mailSender;

//    @Value("${mails-recipient_producer_failure}")
//    private String[] mailsRecipientProducerFailure;
//
//    @Value("${mailFrom}")
//    private String mailFrom;
//    public void send(String topic, String message) {
//        log.debug("request to send messsage to broker");
//        // the KafkaTemplate provides asynchronous send methods returning a
//        // Future
//        ListenableFuture<SendResult<String, String>> future = kafkaTemplateTransfert.send(topic, message);
//
//        // you can register a callback with the listener to receive the result
//        // of the send asynchronously
//        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
//
//            @Override
//            public void onSuccess(SendResult<String, String> result) {
//                log.info("sent message='{}' with offset={}", message,
//                        result.getRecordMetadata().offset());
//            }
//
//            @Override
//            public void onFailure(Throwable ex) {
//                log.debug("unable to send message='{}'", message, ex);
//            }
//        });
//
//        // alternatively, to block the sending thread, to await the result,
//        // invoke the future's get() method
//    }
    @Async
    public void send(String topic, String key, TransfertCompanyBranchDTO message) {//topic key message
        if (Arrays.stream(env.getActiveProfiles()).anyMatch("kafka"::equals)) {
            log.debug("***************send message TransfertCompanyBranchDTO from company to branch ******************************");

            kafkaTemplateTransferCompanyToBranch.send(topic, key, message)
                    .addCallback(new ListenableFutureCallback<SendResult<String, TransfertCompanyBranchDTO>>() {

                        @Override
                        public void onSuccess(SendResult<String, TransfertCompanyBranchDTO> result) {
                            log.info("sent TransfertCompanyBranchDTO message from company to branch ='{}' with offset={}", message,
                                    result.getRecordMetadata().offset());
                        }

                        @Override
                        public void onFailure(Throwable ex) {
                            log.error("unable to send message TransfertCompanyBranchDTO from company to branch ='{}'", message, ex);
                            prepareAndSendMail(message, ex);
                        }
                    });
        }
    }
    @Async
    public void send(String topic, String key, BonRecepDTO message) {//topic key message
        if (Arrays.stream(env.getActiveProfiles()).anyMatch("kafka"::equals)) {
            log.debug("***************try to send message reception onshelf to company  ******************************", message);

            kafkaTemplateBonReceptionOnShelf.send(topic, key, message)
                    .addCallback(new ListenableFutureCallback<SendResult<String, BonRecepDTO>>() {

                        @Override
                        public void onSuccess(SendResult<String, BonRecepDTO> result) {
                            log.info("sent message reception onshelf to company = '{}' with offset={}", message,
                                    result.getRecordMetadata().offset());
                        }

                        @Override
                        public void onFailure(Throwable ex) {
                            log.error("unable to send message reception onshelf to company ='{}'", message, ex);
                            prepareAndSendMail(message, ex);
                        }
                    });
        }
    }
 @Async
    public void sendTransferBranchToCompany(String topic, String key, TransfertCompanyBranchDTO message) {//topic key message
        if (Arrays.stream(env.getActiveProfiles()).anyMatch("kafka"::equals)) {
            log.debug("***************send message transfer To Company {} ******************************");

            kafkaTemplateTransferBranchToCompanyManagement.send(topic, key, message)
                    .addCallback(new ListenableFutureCallback<SendResult<String, TransfertCompanyBranchDTO>>() {

                        @Override
                        public void onSuccess(SendResult<String, TransfertCompanyBranchDTO> result) {
                            log.info("sent TransfertCompanyBranchDTO ransferToCompany message='{}' with offset={}", message,
                                    result.getRecordMetadata().offset());
                        }

                        @Override
                        public void onFailure(Throwable ex) {
                            log.error("unable to send message ransferToCompany TransfertCompanyBranchDTO ='{}'", message, ex);
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
