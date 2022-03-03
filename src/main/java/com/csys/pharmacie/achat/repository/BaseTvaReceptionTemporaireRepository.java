package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.BaseTvaReceptionTemporaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseTvaReceptionTemporaireRepository extends JpaRepository<BaseTvaReceptionTemporaire, Integer> {

}

