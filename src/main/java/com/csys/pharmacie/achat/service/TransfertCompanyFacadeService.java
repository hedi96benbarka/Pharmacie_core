/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.DetailTransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.dto.KafkaConsumerErrorDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.factory.TransfertCompanyBranchFactory;
import com.csys.pharmacie.achat.repository.TransfertCompanyBranchRepository;
import com.csys.pharmacie.helper.EnumCrudMethod;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.csys.pharmacie.achat.service.NativeKafkaTransferBranchToCompany.topicTransferBranchToCompany;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.config.Sender;
import com.csys.pharmacie.parametrage.repository.ParamService;
import java.time.LocalDate;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

/**
 *
 * @author Administrateur
 */
@Service
@Transactional
public class TransfertCompanyFacadeService extends TransfertCompanyBranchService {

    private final FactureBAService factureBAService;
    private final TransfertCompanyService transfertCompanyService;
//    private final FactureBACompanyService factureBACompanyService;

    public TransfertCompanyFacadeService(@Lazy FactureBAService factureBAService, TransfertCompanyService transfertCompanyService, Sender sender, TransfertCompanyBranchRepository transfertcompanybranchRepository, TopicPartitionOffsetService topicPartitionOffsetService, KafkaConsumerErrorService kafkaConsumerErrorService, ParamService paramService, ParamAchatServiceClient paramAchatServiceClient, Environment env, ParamServiceClient paramServiceClient) {
        super(sender, transfertcompanybranchRepository, topicPartitionOffsetService, kafkaConsumerErrorService, paramService, paramAchatServiceClient, env, paramServiceClient);
        this.factureBAService = factureBAService;
        this.transfertCompanyService = transfertCompanyService;
    }

    //to improve parameter result can be not necessary ==>check if all inforamtion exists in dto and remove it
    public TransfertCompanyBranchDTO generateTransfertCompanySuiteReceptionInCompany(FactureBA reception) {
        List<DetailTransfertCompanyBranchDTO> listDetailTransfertCompanyBranchDTOs = new ArrayList<>();
        //we generate dto  from recept not entity to send it to broker 
        String numBonTrCB = paramService.getcompteur(reception.getCategDepot(), TypeBonEnum.TCB);

        log.debug("numBonTrCB est {}", numBonTrCB);
        TransfertCompanyBranchDTO transfertCompanyBranchDTO = new TransfertCompanyBranchDTO();
        transfertCompanyBranchDTO.setNumBon(numBonTrCB);

        transfertCompanyBranchDTO.setCodeFournisseur(reception.getCodfrs());
        transfertCompanyBranchDTO.setCategDepot(reception.getCategDepot());
        transfertCompanyBranchDTO.setDateCreate(LocalDateTime.now());
        transfertCompanyBranchDTO.setDateCreateReception(reception.getDatbon());
        transfertCompanyBranchDTO.setMontantTTC(reception.getMntbon());
        transfertCompanyBranchDTO.setNumAffiche(numBonTrCB.substring(2));
        transfertCompanyBranchDTO.setNumbonReception(reception.getNumbon());
        transfertCompanyBranchDTO.setTypeBon(TypeBonEnum.TCB);
        transfertCompanyBranchDTO.setUserCreate(SecurityContextHolder.getContext().getAuthentication().getName());
        transfertCompanyBranchDTO.setCodeDepot(reception.getCoddep());
        transfertCompanyBranchDTO.setCodeSite(reception.getCodeSite());
        transfertCompanyBranchDTO.setOnShelf(Boolean.TRUE.equals(reception.getAutomatique()));
        reception.getDetailFactureBACollection().forEach(mvtStoBA -> {
            DetailTransfertCompanyBranchDTO detailTransfertCompanyBranchDTO = new DetailTransfertCompanyBranchDTO();
            detailTransfertCompanyBranchDTO.setCodeArticle(mvtStoBA.getCodart());
            detailTransfertCompanyBranchDTO.setCodeSaisi(mvtStoBA.getCodeSaisi());
            detailTransfertCompanyBranchDTO.setCategDepot(mvtStoBA.getCategDepot().categ());
            detailTransfertCompanyBranchDTO.setCodeTva(mvtStoBA.getCodtva());
            detailTransfertCompanyBranchDTO.setCodeUnite(mvtStoBA.getCodeUnite());
            if (reception.getAutomatique()) {
                detailTransfertCompanyBranchDTO.setDatePeremption(LocalDate.now());
            } else {
                detailTransfertCompanyBranchDTO.setDatePeremption(mvtStoBA.getDatPer());
            }
            detailTransfertCompanyBranchDTO.setDesignation(mvtStoBA.getDesart());
            detailTransfertCompanyBranchDTO.setDesignationSec(mvtStoBA.getDesart());
            detailTransfertCompanyBranchDTO.setLotInter(mvtStoBA.getLotInter());
            detailTransfertCompanyBranchDTO.setMontantHt(mvtStoBA.getMontht());
            detailTransfertCompanyBranchDTO.setNumBon(numBonTrCB);
            detailTransfertCompanyBranchDTO.setPrixUnitaire(mvtStoBA.getPriuni());
            detailTransfertCompanyBranchDTO.setQuantite(mvtStoBA.getQuantite());
            detailTransfertCompanyBranchDTO.setTauxTva(mvtStoBA.getTautva());
            detailTransfertCompanyBranchDTO.setBaseTva(mvtStoBA.getBaseTva());
            detailTransfertCompanyBranchDTO.setIsCapitalize(mvtStoBA.getIsCapitalize());
            detailTransfertCompanyBranchDTO.setPrixUnitaire(mvtStoBA.getPriuni());
            detailTransfertCompanyBranchDTO.setQuantite(mvtStoBA.getQuantite());
            detailTransfertCompanyBranchDTO.setRemise(mvtStoBA.getRemise());
            detailTransfertCompanyBranchDTO.setPrixVente(mvtStoBA.getPrixVente());
            detailTransfertCompanyBranchDTO.setQuantiteRestante(mvtStoBA.getQuantite());
//            detailTransfertCompanyBranchDTO.setPmpPrecedent(mvtStoBA.getPmpPrecedent());
//            detailTransfertCompanyBranchDTO.setQuantitePrecedante(mvtStoBA.getQuantitePrecedante());
            detailTransfertCompanyBranchDTO.setIsReferencePrice(mvtStoBA.getIsPrixReference());
            detailTransfertCompanyBranchDTO.setCodeArticleFournisseur(mvtStoBA.getCodeArticleFournisseur());
            listDetailTransfertCompanyBranchDTOs.add(detailTransfertCompanyBranchDTO);
        });
        transfertCompanyBranchDTO.setDetailTransfertCompanyBranchDTOs(listDetailTransfertCompanyBranchDTOs);

        transfertCompanyBranchDTO.setAction(EnumCrudMethod.CREATE);
        TransfertCompanyBranchDTO resultedDTO = save(transfertCompanyBranchDTO);
        return resultedDTO;
    }

    public TransfertCompanyBranchDTO save(TransfertCompanyBranchDTO transfertCompanyBranchDTO) {
        transfertCompanyService.saveTransfertCompany(transfertCompanyBranchDTO);
        if (!Boolean.TRUE.equals(transfertCompanyBranchDTO.getOnShelf())) {
            sender.send("transfer-company-to-branch-management", transfertCompanyBranchDTO.getNumBon(), transfertCompanyBranchDTO);
        }
        return transfertCompanyBranchDTO;
    }

    @Override
    public TransfertCompanyBranchDTO rereplicateTransfertWhenErrorReplication(String numbon) {
        FactureBA bonRecep = factureBAService.findOne(numbon);
        TransfertCompanyBranchDTO transfertCompanyBranchDTO = generateTransfertCompanySuiteReceptionInCompany(bonRecep);
        TransfertCompanyBranchDTO resultedDTO = save(transfertCompanyBranchDTO);
        return resultedDTO;
    }

    @Retryable(
            maxAttempts = 1,
            backoff = @Backoff(delay = 5000)
    )
    public TransfertCompanyBranchDTO saveRecordAfterReturnTransfer(ConsumerRecord<String, TransfertCompanyBranchDTO> record, String groupId) {
        log.debug("*********************************debut process  save TransfertCompanyBranch in company with offset **************************: {}", record.offset());
//
        TransfertCompanyBranchDTO transfertcompanybranchDTO = record.value();

        TransfertCompanyBranch transfertBranchToCompanyForReturn = TransfertCompanyBranchFactory.transfertCompanyBranchDTOToTransfertCompanyBranch(transfertcompanybranchDTO);
        transfertBranchToCompanyForReturn.setReturnedToSupplier(Boolean.FALSE);
        transfertBranchToCompanyForReturn.setIntegrer(Boolean.FALSE);
        transfertBranchToCompanyForReturn.setCodeIntegration("");

        log.debug("*********************************resulted transfert**************************: {}", transfertBranchToCompanyForReturn);
        transfertBranchToCompanyForReturn = transfertcompanybranchRepository.save(transfertBranchToCompanyForReturn);
        TransfertCompanyBranchDTO resultDTO = TransfertCompanyBranchFactory.transfertCompanyBranchToTransfertCompanyBranchDTO(transfertBranchToCompanyForReturn, Boolean.FALSE);
        log.debug("*********************************resulted dto**************************: {}", resultDTO);
        processTransferOnReturnTransfert(transfertBranchToCompanyForReturn);
        log.debug("*********************************tranfer is proeccessed**************************: {}", resultDTO);
        topicPartitionOffsetService.commitOffsetByGroupIdToDB(record, groupId);
        return resultDTO;
    }

    @Recover
    public TransfertCompanyBranchDTO recover(Exception exc, ConsumerRecord<String, TransfertCompanyBranchDTO> record, String groupId) {
        log.error("****************RECOVERING: I will handle my exception  while insering transfer to company occured with object {} ********", record.value().toString());
//        log.error("exception est {},{},{},{}", exc.getCause(), exc.getMessage(), exc.getStackTrace());
        KafkaConsumerErrorDTO kafkaConsumerErrorDTO = new KafkaConsumerErrorDTO();
        kafkaConsumerErrorDTO.setTopic(topicTransferBranchToCompany);
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

        log.debug("******* RECOVERING:  just saved  kafkaConsumerError TransfertCompanyBranchDTO  topicTransferToCompany********** ");
        kafkaConsumerErrorService.save(kafkaConsumerErrorDTO);
        topicPartitionOffsetService.commitOffsetByGroupIdToDB(record, groupId);
        return record.value();
    }

    @Override
    public TransfertCompanyBranchDTO ajoutTransfertBranchToCompanyPourRetour(TransfertCompanyBranchDTO transfertcompanybranchDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void saveTransfertCompany(TransfertCompanyBranch transfertCompanyBranch) {

        transfertcompanybranchRepository.save(transfertCompanyBranch);

    }
}
