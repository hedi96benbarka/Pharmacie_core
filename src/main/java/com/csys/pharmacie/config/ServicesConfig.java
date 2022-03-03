/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.config;

import com.csys.pharmacie.achat.service.AvoirFournisseurService;
import com.csys.pharmacie.achat.service.FactureBAService;
import com.csys.pharmacie.achat.service.TransfertCompanyBranchService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ServicesConfig {

    private final Logger log = LoggerFactory.getLogger(ServicesConfig.class);

    private enum serviceFactureBAImplementations {
        factureBAStandardService, factureBACompanyService, factureBABranchFacadeService
    }

    private enum serviceTransfertCompanyBranchImplementations {
        transfertBranchFacadeService, transfertCompanyFacadeService
    }

    private enum serviceAvoirFournisseurImplementations {
        avoirFournisseurStandardService, avoirFournisseurCompanyService
    }
    public static String contextReception;

    @Value("${context.reception}")
    public void setContext(String contextValue) {
        contextReception = contextValue;
    }

    @Autowired
    Map<String, FactureBAService> myServicesFactureBA;
    @Autowired
    Map<String, TransfertCompanyBranchService> myServicesTransfertCompanyBranch;
    @Autowired
    Map<String, AvoirFournisseurService> myServicesAvoirFournisseur;

    @Primary
    @Bean
    public FactureBAService factureBAService() {

        if (contextReception.contains("company")) {
            log.info("FactureBA context company");
            return myServicesFactureBA.get(serviceFactureBAImplementations.factureBACompanyService.toString());
        } else if (contextReception.contains("branch")) {
            log.info("FactureBA context branch");
            return myServicesFactureBA.get(serviceFactureBAImplementations.factureBABranchFacadeService.toString());
        } else {
            log.info(" factureBA  context standard");
            return myServicesFactureBA.get(serviceFactureBAImplementations.factureBAStandardService.toString());
        }
    }

    @Primary
    @Bean
    public TransfertCompanyBranchService transfertCompanyBranchService() {

        if (contextReception.contains("company")) {
            log.info("TransfertCompanyBranch context company");
            return myServicesTransfertCompanyBranch.get(serviceTransfertCompanyBranchImplementations.transfertCompanyFacadeService.toString());
        } else {
            log.info(" TransfertCompanyBranch  context branch");
            return myServicesTransfertCompanyBranch.get(serviceTransfertCompanyBranchImplementations.transfertBranchFacadeService.toString());
        }
    }

    @Primary
    @Bean
    public AvoirFournisseurService avoirFournisseurService() {

        if (contextReception.contains("company")) {
            log.info("avoir fournisseur context company");
            return myServicesAvoirFournisseur.get(serviceAvoirFournisseurImplementations.avoirFournisseurCompanyService.toString());
        } else {
            log.info("avoir fournisseur  context standard");
            return myServicesAvoirFournisseur.get(serviceAvoirFournisseurImplementations.avoirFournisseurStandardService.toString());

        }
    }
}