/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.QtePrixMouvement;
import com.csys.pharmacie.vente.avoir.domain.DetailMvtstoAV;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Farouk
 */
public interface DetailMvtstoAVRepository extends JpaRepository<DetailMvtstoAV, Long> {

    @Query("SELECT NEW com.csys.pharmacie.helper.QtePrixMouvement( d.mvtStoAV.mvtStoAVPK.codart,d.mvtStoAV.codeSaisi,d.mvtStoAV.desart,d.mvtStoAV.desArtSec,d.mvtStoAV.unite,-sum(d.qte),-sum(d.qte * d.mvtStoAV.priuni *(1 + d.mvtStoAV.tautva/100)),-sum(d.qte * d.pu * (1 + d.tauxTva/100)),d.mvtStoAV.tautva,d.tauxTva,d.mvtStoAV.factureAV.coddep,d.depsto.numBonOrigin) FROM DetailMvtstoAV d"
            + " WHERE d.mvtStoAV.factureAV.coddep in ?1 and d.mvtStoAV.factureAV.datbon BETWEEN ?2 and ?3 and  d.mvtStoAV.factureAV.categDepot=?4 and d.mvtStoAV.mvtStoAVPK.codart in ?5"
            + " group by  d.mvtStoAV.mvtStoAVPK.codart,d.mvtStoAV.codeSaisi,d.mvtStoAV.desart,d.mvtStoAV.desArtSec,d.mvtStoAV.unite, d.mvtStoAV.tautva,d.mvtStoAV.factureAV.coddep,d.tauxTva,d.depsto.numBonOrigin")
    public List<QtePrixMouvement> findQuantitePrixMouvementByCodartIn(List<Integer> coddep, LocalDateTime datedeb, LocalDateTime datefin, CategorieDepotEnum categ, List<Integer> articleIds);

    @Query("SELECT NEW com.csys.pharmacie.helper.QtePrixMouvement( d.mvtStoAV.mvtStoAVPK.codart,d.mvtStoAV.codeSaisi,d.mvtStoAV.desart,d.mvtStoAV.desArtSec,d.mvtStoAV.unite,-sum(d.qte),-sum(d.qte * d.mvtStoAV.priuni *(1 + d.mvtStoAV.tautva/100)),-sum(d.qte * d.pu * (1 + d.tauxTva/100)),d.mvtStoAV.tautva,d.tauxTva,d.mvtStoAV.factureAV.coddep,d.depsto.numBonOrigin) FROM DetailMvtstoAV d"
            + " WHERE d.mvtStoAV.factureAV.datbon BETWEEN ?1 and ?2 and  d.mvtStoAV.factureAV.categDepot=?3 and d.mvtStoAV.mvtStoAVPK.codart in ?4"
            + " group by  d.mvtStoAV.mvtStoAVPK.codart,d.mvtStoAV.codeSaisi,d.mvtStoAV.desart,d.mvtStoAV.desArtSec,d.mvtStoAV.unite, d.mvtStoAV.tautva,d.mvtStoAV.factureAV.coddep,d.tauxTva,d.depsto.numBonOrigin")
    public List<QtePrixMouvement> findQuantitePrixMouvementByCodartIn(LocalDateTime datedeb, LocalDateTime datefin, CategorieDepotEnum categ, List<Integer> articleIds);

}
