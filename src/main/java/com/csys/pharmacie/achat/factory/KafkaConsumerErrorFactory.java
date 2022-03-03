/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.KafkaConsumerError;
import com.csys.pharmacie.achat.dto.KafkaConsumerErrorDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class KafkaConsumerErrorFactory {
  public static KafkaConsumerErrorDTO kafkaConsumerErrorToKafkaConsumerErrorDTO(KafkaConsumerError kafkaConsumerError) {
    KafkaConsumerErrorDTO kafkaConsumerErrorDTO=new KafkaConsumerErrorDTO();
    kafkaConsumerErrorDTO.setCode(kafkaConsumerError.getCode());
    kafkaConsumerErrorDTO.setTopic(kafkaConsumerError.getTopic());
    kafkaConsumerErrorDTO.setPartition(kafkaConsumerError.getPartition());
    kafkaConsumerErrorDTO.setOffset(kafkaConsumerError.getOffset());
    kafkaConsumerErrorDTO.setGroupId(kafkaConsumerError.getGroupId());
    kafkaConsumerErrorDTO.setTryCount(kafkaConsumerError.getTryCount());
    kafkaConsumerErrorDTO.setHandled(kafkaConsumerError.getHandled());
    kafkaConsumerErrorDTO.setCreateTime(kafkaConsumerError.getCreateTime());
    kafkaConsumerErrorDTO.setExceptionDetails(kafkaConsumerError.getExceptionDetails());
    kafkaConsumerErrorDTO.setCodeSite(kafkaConsumerError.getCodeSite());
    kafkaConsumerErrorDTO.setRecordKey(kafkaConsumerError.getRecordKey());
    kafkaConsumerErrorDTO.setRecord(kafkaConsumerError.getRecord());
    return kafkaConsumerErrorDTO;
  }

  public static KafkaConsumerError kafkaConsumerErrorDTOToKafkaConsumerError(KafkaConsumerErrorDTO kafkaConsumerErrorDTO) {
    KafkaConsumerError kafkaConsumerError=new KafkaConsumerError();
    kafkaConsumerError.setTopic(kafkaConsumerErrorDTO.getTopic());
    kafkaConsumerError.setPartition(kafkaConsumerErrorDTO.getPartition());
    kafkaConsumerError.setOffset(kafkaConsumerErrorDTO.getOffset());
    kafkaConsumerError.setGroupId(kafkaConsumerErrorDTO.getGroupId());
    kafkaConsumerError.setTryCount(kafkaConsumerErrorDTO.getTryCount());
    kafkaConsumerError.setHandled(kafkaConsumerErrorDTO.getHandled());
    kafkaConsumerError.setCreateTime(kafkaConsumerErrorDTO.getCreateTime());
    kafkaConsumerError.setExceptionDetails(kafkaConsumerErrorDTO.getExceptionDetails());
    kafkaConsumerError.setCodeSite(kafkaConsumerErrorDTO.getCodeSite());
    kafkaConsumerError.setRecordKey(kafkaConsumerErrorDTO.getRecordKey());
    kafkaConsumerError.setRecord(kafkaConsumerErrorDTO.getRecord());
    kafkaConsumerError.setAction(kafkaConsumerErrorDTO.getAction());
    return kafkaConsumerError;
  }

  public static Collection<KafkaConsumerErrorDTO> KafkaConsumerErrorToKafkaConsumerErrorDTOs(Collection<KafkaConsumerError> kafkaConsumerErrors) {
    List<KafkaConsumerErrorDTO> kafkaConsumerErrorDTOs=new ArrayList<>();
    kafkaConsumerErrors.forEach(x -> {
      kafkaConsumerErrorDTOs.add(kafkaConsumerErrorToKafkaConsumerErrorDTO(x));
    } );
    return kafkaConsumerErrorDTOs;
  }
}

