/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.KafkaProducerErrorGec;
import com.csys.pharmacie.achat.dto.KafkaProducerErrorGecDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author USER
 */
public class KafkaProducerErrorGecFactory {
    
    public static KafkaProducerErrorGecDTO kafkaProducerErrorGecToKafkaProducerErrorGecDTO(KafkaProducerErrorGec kafkaProducerErrorGec) {
    KafkaProducerErrorGecDTO kafkaProducerErrorGecDTO=new KafkaProducerErrorGecDTO();
    kafkaProducerErrorGecDTO.setCode(kafkaProducerErrorGec.getCode());
    kafkaProducerErrorGecDTO.setTopic(kafkaProducerErrorGec.getTopic());
    kafkaProducerErrorGecDTO.setTryCount(kafkaProducerErrorGec.getTryCount());
    kafkaProducerErrorGecDTO.setHandled(kafkaProducerErrorGec.getHandled());
    kafkaProducerErrorGecDTO.setCreateTime(kafkaProducerErrorGec.getCreateTime());
    kafkaProducerErrorGecDTO.setExceptionDetails(kafkaProducerErrorGec.getExceptionDetails());
    kafkaProducerErrorGecDTO.setCodeSite(kafkaProducerErrorGec.getCodeSite());
    kafkaProducerErrorGecDTO.setRecordKey(kafkaProducerErrorGec.getRecordKey());
    kafkaProducerErrorGecDTO.setRecord(kafkaProducerErrorGec.getRecord());
    return kafkaProducerErrorGecDTO;
  }

  public static KafkaProducerErrorGec kafkaProducerErrorGecDTOToKafkaProducerErrorGec(KafkaProducerErrorGecDTO kafkaProducerErrorGecDTO) {
    KafkaProducerErrorGec kafkaProducerErrorGec=new KafkaProducerErrorGec();
    kafkaProducerErrorGec.setTopic(kafkaProducerErrorGecDTO.getTopic());
    kafkaProducerErrorGec.setTryCount(kafkaProducerErrorGecDTO.getTryCount());
    kafkaProducerErrorGec.setHandled(kafkaProducerErrorGecDTO.getHandled());
    kafkaProducerErrorGec.setCreateTime(kafkaProducerErrorGecDTO.getCreateTime());
    kafkaProducerErrorGec.setExceptionDetails(kafkaProducerErrorGecDTO.getExceptionDetails());
    kafkaProducerErrorGec.setCodeSite(kafkaProducerErrorGecDTO.getCodeSite());
    kafkaProducerErrorGec.setRecordKey(kafkaProducerErrorGecDTO.getRecordKey());
    kafkaProducerErrorGec.setRecord(kafkaProducerErrorGecDTO.getRecord());
    return kafkaProducerErrorGec;
  }

  public static Collection<KafkaProducerErrorGecDTO> KafkaProducerErrorGecToKafkaProducerErrorGecDTOs(Collection<KafkaProducerErrorGec> kafkaProducerErrorGecs) {
    List<KafkaProducerErrorGecDTO> kafkaProducerErrorGecDTOs=new ArrayList<>();
    kafkaProducerErrorGecs.forEach(x -> {
      kafkaProducerErrorGecDTOs.add(kafkaProducerErrorGecToKafkaProducerErrorGecDTO(x));
    } );
    return kafkaProducerErrorGecDTOs;
  }
    
}
