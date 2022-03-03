/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.aop;

import com.csys.pharmacie.vente.avoir.domain.FactureAV;
import com.csys.pharmacie.vente.quittance.service.ReceptionServiceClient;
import java.util.List;
import java.util.stream.Collectors;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Configuration
@EnableAsync
public class AvoirAop {

    private final Logger log = LoggerFactory.getLogger(AvoirAop.class);

    private final ReceptionServiceClient receptionServiceClient;

    public AvoirAop(ReceptionServiceClient receptionServiceClient) {
        this.receptionServiceClient = receptionServiceClient;
    }

    @Async
    @AfterReturning(value = "execution(* com.csys.pharmacie.vente.avoir.service.AvoirService.deletesPermanent(..))", returning = "factureAVs")
    public void saveDetailsAdmissionMultiple(List<FactureAV> factureAVs) {
       log.info ("after execution");
        log.info("factureAVs count :  {}", factureAVs);
//        SecurityContextHolder.getContext()
//                .setAuthentication(new UsernamePasswordAuthenticationToken(
//                        "AUTO",
//                        "AUTO",
//                        Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        String codeAdmission = factureAVs.stream().findFirst().get().getNumdoss();
        List<String> avoirIDs = factureAVs.stream().map(FactureAV::getNumbon).collect(Collectors.toList());
        log.info("codeAdmission: {}", codeAdmission);
        receptionServiceClient.deleteFacturationByNumQuittance(null, avoirIDs, codeAdmission);

    }

}
