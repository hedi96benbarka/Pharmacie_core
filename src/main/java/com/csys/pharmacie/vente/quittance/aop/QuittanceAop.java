/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.aop;

import com.csys.pharmacie.vente.quittance.domain.Facture;
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
public class QuittanceAop {

    private final Logger log = LoggerFactory.getLogger(QuittanceAop.class);

    private final ReceptionServiceClient receptionServiceClient;

    public QuittanceAop(ReceptionServiceClient receptionServiceClient) {
        this.receptionServiceClient = receptionServiceClient;
    }

    @Async
    @AfterReturning(value = "execution(* com.csys.pharmacie.vente.quittance.service.FactureService.deletesPermanent(..))", returning = "factures")
    public void saveDetailsAdmissionMultiple(List<Facture> factures) {
        log.info("after execution");
        log.info("factures count :  {}", factures);
//        SecurityContextHolder.getContext()
//                .setAuthentication(new UsernamePasswordAuthenticationToken(
//                        "AUTO",
//                        "AUTO",
//                        Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        String codeAdmission = factures.stream().findFirst().get().getNumdoss();
        List<String> quittancesIDs = factures.stream().map(Facture::getNumbon).collect(Collectors.toList());
        log.info("codeAdmission: {}", codeAdmission);
        receptionServiceClient.deleteFacturationByNumQuittance(null, quittancesIDs, codeAdmission);

    }

}
