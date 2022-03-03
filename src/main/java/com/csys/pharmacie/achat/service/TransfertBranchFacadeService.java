/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.factory.TransfertCompanyBranchFactory;
import com.csys.pharmacie.achat.repository.TransfertCompanyBranchRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.csys.pharmacie.achat.dto.KafkaConsumerErrorDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.config.Sender;
import com.csys.pharmacie.helper.EnumCrudMethod;
import org.springframework.retry.annotation.EnableRetry;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.util.Preconditions;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.env.Environment;

/**
 *
 * @author Administrateur
 */
@EnableRetry
@Service
@Transactional
public class TransfertBranchFacadeService extends TransfertCompanyBranchService {

    @Value("${kafka.topic.transfer-company-to-branch-management}")
    private String topicTransfertManagement;
//    public final TopicPartitionOffsetService topicPartitionOffsetService;
    private final TransfertBranchService transfertBranchService;

    private final FactureBABranchFacadeService factureBABranchService;

    public TransfertBranchFacadeService(TransfertBranchService transfertBranchService, FactureBABranchFacadeService factureBABranchService, Sender sender, TransfertCompanyBranchRepository transfertcompanybranchRepository, TopicPartitionOffsetService topicPartitionOffsetService, KafkaConsumerErrorService kafkaConsumerErrorService, ParamService paramService, ParamAchatServiceClient paramAchatServiceClient, Environment env, ParamServiceClient paramServiceClient) {
        super(sender, transfertcompanybranchRepository, topicPartitionOffsetService, kafkaConsumerErrorService, paramService, paramAchatServiceClient, env, paramServiceClient);
        this.transfertBranchService = transfertBranchService;
        this.factureBABranchService = factureBABranchService;
    }

//
//    public TransfertBranchFacadeService(FactureBABranchFacadeService factureBABranchService, StockService stockService, @Lazy PricingService pricingService, AjustementTransfertBranchCompanyService ajustementTransfertBranchCompanyService, Sender sender, TransfertCompanyBranchRepository transfertcompanybranchRepository, TopicPartitionOffsetService topicPartitionOffsetService, KafkaConsumerErrorService kafkaConsumerErrorService, ParamService paramService, ParamAchatServiceClient paramAchatServiceClient, Environment env, ParamServiceClient paramServiceClient) {
//        super(sender, transfertcompanybranchRepository, topicPartitionOffsetService, kafkaConsumerErrorService, paramService, paramAchatServiceClient, env, paramServiceClient);
//        this.factureBABranchService = factureBABranchService;
////        this.stockService = stockService;
////        this.pricingService = pricingService;
////        this.ajustementTransfertBranchCompanyService = ajustementTransfertBranchCompanyService;
//    }
    @Retryable(
            maxAttempts = 2,
            backoff = @Backoff(delay = 5000)
    )

    public void saveRecord(ConsumerRecord<String, TransfertCompanyBranchDTO> record, String groupId) {

        log.debug("*********************************debut process  save TransfertCompanyBranch in branch with offset***********************: {}  and code site****{}", record.offset(), record.value().getCodeSite());

        TransfertCompanyBranchDTO transfertCompanyBranchDTO = record.value();

        Integer actualCodeSite = resolveCodeSite();
        log.info("actualCodeSite est {}  **** codesite from record {}", actualCodeSite, transfertCompanyBranchDTO.getCodeSite());
        Boolean matchingCodeSite = actualCodeSite.equals(transfertCompanyBranchDTO.getCodeSite());
        if (matchingCodeSite) {
            log.info("*** is its matchingCodeSite **************");
            TransfertCompanyBranch transfertWithSameId = transfertcompanybranchRepository.findOne(record.value().getNumBon());

            Preconditions.checkBusinessLogique(transfertWithSameId == null, "transfer-company-branch-already-replicated {}", record.value().getNumAffiche());
            TransfertCompanyBranch transfertCompanyBranch = TransfertCompanyBranchFactory.transfertCompanyBranchDTOToTransfertCompanyBranch(transfertCompanyBranchDTO);
            //-------  depot--------/
            DepotDTO depotPrincipalDTO = paramAchatServiceClient.findDepotPrincipalByCategorieDepot(transfertCompanyBranchDTO.getCategDepot());
            Preconditions.checkBusinessLogique(!depotPrincipalDTO.getDesignation().equals("depot.deleted"), "missing main deposit");
            transfertCompanyBranch.setCodeDepot(depotPrincipalDTO.getCode());

            transfertCompanyBranch.setDateCreate(LocalDateTime.now());
            transfertCompanyBranch.setIntegrer(Boolean.FALSE);
            transfertCompanyBranch.setCodeIntegration("");

            transfertCompanyBranch.setCodeSite(transfertCompanyBranchDTO.getCodeSite());

//          bonRecep.setDateDebutExoneration(fournisseurDTO.getDateDebutExenoration());
//        bonRecep.setDateFinExenoration(fournisseurDTO.getDateFinExenoration());????????ask bilel or ahmed 


      Set<Integer> codeArticles = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()
              .stream()
              .map(elt-> elt.getCodeArticle())
              .collect(Collectors.toSet());
                
            log.debug("codeArticles sont {}", codeArticles);
            List<ArticleDTO> articles = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(transfertCompanyBranch.getCategDepot(), codeArticles.toArray(new Integer[codeArticles.size()]));

            transfertCompanyBranch.getDetailTransfertCompanyBranchCollection().forEach(det -> {
                ArticleDTO matchingItem = articles.stream()
                        .filter(article -> article.getCode().equals(det.getCodeArticle()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("stock.find-all.missing-article", new Throwable(det.getCodeSaisi())));
                det.setAncienPrixAchat(matchingItem.getPrixAchat());
                det.setQuantiteRestante(det.getQuantite());
            });

            transfertCompanyBranch = transfertcompanybranchRepository.save(transfertCompanyBranch);
//        unecessary 
//TransfertCompanyBranchDTO resultDTO = TransfertCompanyBranchFactory.transfertCompanyBranchToTransfertCompanyBranchDTO(transfertCompanyBranch, Boolean.FALSE);
            factureBABranchService.processStockAndPricesAfterTransefrtCompanyToBranch(transfertCompanyBranch, record.value(), articles);
            log.debug("************after process stock and prices **********  ");

        }
        topicPartitionOffsetService.commitOffsetByGroupIdToDB(record, groupId);

    }

    @Recover
    public void recover(Exception exc, ConsumerRecord<String, TransfertCompanyBranchDTO> record,  String groupId) {
        log.error("****************RECOVERING: I will handle my exception  occured with object {} ********", record.value().toString());
//        log.error("exception est {},{},{},{}", exc.getCause(), exc.getMessage(), exc.getStackTrace());
        KafkaConsumerErrorDTO kafkaConsumerErrorDTO = new KafkaConsumerErrorDTO();
        kafkaConsumerErrorDTO.setTopic(topicTransfertManagement);
        kafkaConsumerErrorDTO.setPartition(record.partition());
        kafkaConsumerErrorDTO.setOffset((int) record.offset());
        kafkaConsumerErrorDTO.setGroupId(groupId);
        kafkaConsumerErrorDTO.setTryCount(1);
        kafkaConsumerErrorDTO.setCreateTime(LocalDateTime.now());
        kafkaConsumerErrorDTO.setHandled(Boolean.FALSE);
        String errorMessage = String.valueOf(" ********** my  exception  ********** :");
        if (exc.getCause() != null) {
            errorMessage = errorMessage.concat(" **********exception cuase*********").concat(exc.getCause().toString());
            if (exc.getCause().getCause() != null) {
                errorMessage = errorMessage.concat("************** exception cause  cause  est: ").concat(exc.getCause().getCause().toString());
            }

        }

        if (exc.getMessage() != null) {
            errorMessage = errorMessage.concat("************** message exception est ********* : ")
                    .concat(exc.getMessage());
        } else if (exc.getStackTrace() != null) {
            errorMessage = errorMessage.concat("*************** exception stack  est : ").concat(Arrays.toString(exc.getStackTrace()));
        }
        log.error("************ {}***************", errorMessage);
        kafkaConsumerErrorDTO.setExceptionDetails(errorMessage);
        kafkaConsumerErrorDTO.setRecordKey(record.key());
        kafkaConsumerErrorDTO.setRecord(record.value().toString());
        kafkaConsumerErrorDTO.setAction(EnumCrudMethod.CREATE);

        log.debug("******* RECOVERING:  just saved  kafkaConsumerError TransfertCompanyBranchDTO ********** ");
        kafkaConsumerErrorService.save(kafkaConsumerErrorDTO);
        topicPartitionOffsetService.commitOffsetByGroupIdToDB(record, groupId);
//        return record.value();
    }

    @Override
    public TransfertCompanyBranchDTO rereplicateTransfertWhenErrorReplication(String numbon
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TransfertCompanyBranchDTO ajoutTransfertBranchToCompanyPourRetour(TransfertCompanyBranchDTO transfertcompanybranchDTO) {

        log.debug("*********************************ajoutTransfertBranchToCompanyPourRetour *************in branch*************: ");
        TransfertCompanyBranchDTO resultDTO = transfertBranchService.ajoutTransfertBranchToCompanyPourRetour(transfertcompanybranchDTO);

        sender.send("transfer-branch-to-company-management", resultDTO.getNumBon(), resultDTO);

        return resultDTO;
    }

    @Override
    public TransfertCompanyBranchDTO save(TransfertCompanyBranchDTO transfertcompanybranchDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
