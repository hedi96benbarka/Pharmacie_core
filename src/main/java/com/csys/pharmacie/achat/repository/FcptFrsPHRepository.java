package com.csys.pharmacie.achat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csys.pharmacie.achat.domain.FcptfrsPH;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository("FcptFrsPHRepository")
public interface FcptFrsPHRepository extends JpaRepository<FcptfrsPH, Long> {

    public FcptfrsPH findFirstByNumBon(String numbon);
    
    public List<FcptfrsPH> findByNumBonIn(List<String> numbon) ; 
    
    @Query("Select f from FcptfrsPH f Where f.sens=?1 and f.mntOP=?2 and f.numBon like ?3% and f.reste=f.credit ")
    public List<FcptfrsPH> findBySensAndMntOPAndNumBonStartingWith(String string, BigDecimal ZERO, String fl);

    public void deleteByNumOprIn(Long[] numOprs);
}
