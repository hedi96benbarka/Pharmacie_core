package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.DetailReceptionTemporaire;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailReceptionTemporaireRepository extends JpaRepository<DetailReceptionTemporaire, Integer> {
    
    public List<DetailReceptionTemporaire> findByReception_numbon(String numbon);

}

