package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.ReceivingDetails;
import com.csys.pharmacie.achat.domain.ReceivingDetailsPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReceivingDetails entity.
 */
@Repository
public interface ReceivingDetailsRepository extends JpaRepository<ReceivingDetails, Integer> {
 List<ReceivingDetails>findByReceiving_Code(Integer code);
}

