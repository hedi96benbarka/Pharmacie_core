/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.repository;

import com.csys.pharmacie.prelevement.domain.TracePrelevementImmobilisation;
import com.csys.pharmacie.prelevement.domain.TracePrelevementImmobilisationPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Administrateur
 */
public interface TracePrelevementImmobilisationRepository extends JpaRepository<TracePrelevementImmobilisation, TracePrelevementImmobilisationPK> {
    
}
