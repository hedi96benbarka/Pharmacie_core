/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author Farouk
 */
@Configuration
public class MessageConfig {

    @Value("${spring.cloud.config.uri}")
    String CONFIG_SERVER_IP;

    @RefreshScope
    @Bean
    @Profile("prod")
    public ExposedResourceMessageBundleSource messageSource() {
        ExposedResourceMessageBundleSource messageSource = new ExposedResourceMessageBundleSource();
        messageSource.setBasename(CONFIG_SERVER_IP + "/confRepository/default/master/messages");
        messageSource.addBasenames("classpath:/i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}
