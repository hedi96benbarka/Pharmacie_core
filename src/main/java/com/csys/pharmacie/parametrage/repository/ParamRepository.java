package com.csys.pharmacie.parametrage.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csys.pharmacie.parametrage.entity.Paramph;
import java.time.OffsetDateTime;
import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

@Repository("ParamRepository")
public interface ParamRepository extends JpaRepository<Paramph, Long> {

    
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Paramph findFirstByCode(String code);

    @Query(value = "SELECT SYSDATETIMEOFFSET() AS SYSDATETIMEOFFSET", nativeQuery = true)
    public OffsetDateTime getServerDateTime();

    @Query("Select p from Paramph p where p.code in ?1")
    public List<Paramph> getParamsByCodeIn(List<String> lostCodarts);
}
