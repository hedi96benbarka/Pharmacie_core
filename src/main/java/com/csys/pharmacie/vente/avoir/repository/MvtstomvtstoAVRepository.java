/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.csys.pharmacie.vente.avoir.domain.MvtstoMvtstoAV;
import com.csys.pharmacie.vente.avoir.dto.FactureFactureAV;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Farouk
 */
public interface MvtstomvtstoAVRepository extends JpaRepository<MvtstoMvtstoAV, Long> {

    List<MvtstoMvtstoAV> findByNumBonMvtstoAVAndNumordreMvtstoAVAndCodart(String numBonMvtsto, String numordreMvtstoAV, Integer codart);

    @Query("select distinct  new com.csys.pharmacie.vente.avoir.dto.FactureFactureAV(m.numBonMvtstoAV,m.numBonMvtsto) from MvtstoMvtstoAV m where m.numBonMvtsto in ?1")
    public List<FactureFactureAV> findFactureFactureAVByNumBonMvtsto(List<String> numBonMvtstos);

    @Query("select distinct  new com.csys.pharmacie.vente.avoir.dto.FactureFactureAV(m.numBonMvtstoAV,m.numBonMvtsto) from MvtstoMvtstoAV m where m.numBonMvtstoAV in ?1")
    public List<FactureFactureAV> findFactureFactureAVByNumBonMvtstoAV(List<String> numBonMvtstoAVs);
    
   List<MvtstoMvtstoAV> findByNumBonMvtstoAVIn(List<String> numBonMvtstoAV);
}
