package com.csys.pharmacie.parametrage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csys.pharmacie.parametrage.entity.CompteurPharmacie;
import com.csys.pharmacie.parametrage.entity.CompteurPharmaciePK;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

@Repository("CompteurPharmacieRepository")
public interface CompteurPharmacieRepository extends JpaRepository<CompteurPharmacie, Long> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public CompteurPharmacie findFirstByCompteurPharmaciePK(CompteurPharmaciePK id);
}
