package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.DetailTransfertCompanyBranch;
import java.lang.Boolean;
import java.lang.Long;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailTransfertCompanyBranchRepository extends JpaRepository<DetailTransfertCompanyBranch, Integer> {
}

