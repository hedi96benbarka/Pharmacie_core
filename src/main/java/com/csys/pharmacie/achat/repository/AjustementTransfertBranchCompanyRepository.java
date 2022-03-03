package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.AjustementTransfertBranchCompany;
import com.csys.pharmacie.achat.domain.AjustementTransfertBranchCompanyPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AjustementTransfertBranchCompanyRepository extends JpaRepository<AjustementTransfertBranchCompany, AjustementTransfertBranchCompanyPK> {
 
}

