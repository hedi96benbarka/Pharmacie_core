/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.config;

import static com.csys.pharmacie.config.ServicesConfig.contextReception;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author Administrateur
 */
@Component
public class StartUpCommands {

    private Logger log = LoggerFactory.getLogger(StartUpCommands.class);
//    @Value("${context}")
//    private String contextReception;
    @Autowired
    List<AbstractNativeConsumer> nativeConsumers;


    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        
        
       List <String> listTopicToConsumeInCompany= new ArrayList<>();
       listTopicToConsumeInCompany.add("transfert-bon-reception-onShelf-management");
       listTopicToConsumeInCompany.add("transfer-branch-to-company-management");
       
       if (Arrays.stream(event.getApplicationContext().getEnvironment().getActiveProfiles()).anyMatch("kafka"::equals)//consumer start only if profile klafka and we are on context branch (to prevent producing and cosnuming on central)
                && contextReception.contains("branch")) {
    log.info("*****************context branch ********** srarting consumer not equals to transfert-bon-reception-onShelf-management");
            nativeConsumers.stream().filter(elt-> !( listTopicToConsumeInCompany.contains(elt.getTopic())  ))
                    .forEach(AbstractNativeConsumer::start); 
         }
        if (Arrays.stream(event.getApplicationContext().getEnvironment().getActiveProfiles()).anyMatch("kafka"::equals)//consumer start only if profile klafka and we are on context branch (to prevent producing and cosnuming on central)
                && contextReception.contains("company")) {
    log.info("*****************context company ********** srarting consumer  equals to transfert-bon-reception-onShelf-management");
            nativeConsumers.stream().filter(elt-> !( listTopicToConsumeInCompany.contains(elt.getTopic())  ))
                    .forEach(AbstractNativeConsumer::start);
        }
        
     
    }
    
}