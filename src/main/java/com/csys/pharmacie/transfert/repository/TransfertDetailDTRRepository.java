package com.csys.pharmacie.transfert.repository;

import com.csys.pharmacie.transfert.domain.TransfertDetailDTR;
import com.csys.pharmacie.transfert.domain.TransfertDetailDTRPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransfertDetailDTR entity.
 */
@Repository
public interface TransfertDetailDTRRepository extends JpaRepository<TransfertDetailDTR, TransfertDetailDTRPK> {
     List<TransfertDetailDTR> findByCodeDTRIn(List<Integer>listCodetr);
      List<TransfertDetailDTR> findByCodeDTR(Integer id);
      List<TransfertDetailDTR> findByPk_CodeTransfert(String numbon);
      List<TransfertDetailDTR> findByPk_CodeTransfertIn(List<String> listnumbon);
}

