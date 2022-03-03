package com.csys.pharmacie.parametrage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csys.pharmacie.parametrage.entity.OptionVersionPharmacie;

@Repository("OptionVersionPharmacieRepository")
public interface OptionVersionPharmacieRepository extends JpaRepository<OptionVersionPharmacie, String> {

    public OptionVersionPharmacie findFirstById(String id);

    @Query("SELECT o FROM OptionVersionPharmacie o WHERE o.id IN ?1")
    public List<OptionVersionPharmacie> findById(List<String> ids);
}
