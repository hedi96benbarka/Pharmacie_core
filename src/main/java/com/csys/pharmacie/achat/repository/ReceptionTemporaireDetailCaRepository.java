package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCa;
import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCaPK;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionTemporaireDetailCaRepository extends JpaRepository<ReceptionTemporaireDetailCa, ReceptionTemporaireDetailCaPK> {

    public List<ReceptionTemporaireDetailCa> findByReceptionTemporaireDetailCaPK_CommandeAchatIn(List<Integer> listCodeCAs);

    public List<ReceptionTemporaireDetailCa> findByReceptionTemporaireDetailCaPK_CommandeAchatInAndReceptionTemporaire_FactureBAIsNull(List<Integer> listCodeCAs);

    public List< ReceptionTemporaireDetailCa> findByReceptionTemporaireDetailCaPK_CommandeAchatAndReceptionTemporaire_FactureBAIsNull(Integer codeCommande);
}
