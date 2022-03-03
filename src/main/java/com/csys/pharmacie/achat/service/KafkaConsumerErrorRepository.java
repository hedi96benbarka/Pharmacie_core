/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.KafkaConsumerError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Administrateur
 */
@Repository
public interface KafkaConsumerErrorRepository extends JpaRepository<KafkaConsumerError, Integer> {
    
}
