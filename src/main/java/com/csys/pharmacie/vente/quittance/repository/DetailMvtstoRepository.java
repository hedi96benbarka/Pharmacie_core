package com.csys.pharmacie.vente.quittance.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.QtePrixMouvement;
import com.csys.pharmacie.vente.quittance.domain.DetailMvtsto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface DetailMvtstoRepository extends JpaRepository< DetailMvtsto, Integer>, QueryDslPredicateExecutor<DetailMvtsto> {

    @Query("SELECT NEW com.csys.pharmacie.helper.QtePrixMouvement( d.mvtsto.mvtstoPK.codart,d.mvtsto.codeSaisi,d.mvtsto.desart,d.mvtsto.desArtSec,d.mvtsto.unite,sum(d.qte),sum(d.qte * d.mvtsto.priuni *(1 + d.mvtsto.tautva/100)),sum(d.qte * d.pu * (1 + d.tauxTva/100)),d.mvtsto.tautva,d.tauxTva,d.mvtsto.facture.coddep,d.depsto.numBonOrigin) FROM DetailMvtsto d"
            + " WHERE d.mvtsto.facture.coddep in ?1 and d.mvtsto.facture.datbon BETWEEN ?2 and ?3 and  d.mvtsto.facture.categDepot=?4  and d.mvtsto.mvtstoPK.codart in ?5 and d.mvtsto.facture.codAnnul is null and d.mvtsto.facture.numbonRecept is  null"
            + " group by  d.mvtsto.mvtstoPK.codart,d.mvtsto.codeSaisi,d.mvtsto.desart,d.mvtsto.desArtSec,d.mvtsto.unite, d.mvtsto.tautva,d.mvtsto.facture.coddep,d.tauxTva,d.depsto.numBonOrigin")
    public List<QtePrixMouvement> findQuantitePrixMouvementAndCodartIn(List<Integer> coddep, LocalDateTime datedeb, LocalDateTime datefin, CategorieDepotEnum categ, List<Integer> articleIds);

    @Query("SELECT NEW com.csys.pharmacie.helper.QtePrixMouvement( d.mvtsto.mvtstoPK.codart,d.mvtsto.codeSaisi,d.mvtsto.desart,d.mvtsto.desArtSec,d.mvtsto.unite,sum(d.qte),sum(d.qte * d.mvtsto.priuni *(1 + d.mvtsto.tautva/100)),sum(d.qte * d.pu * (1 + d.tauxTva/100)),d.mvtsto.tautva,d.tauxTva,d.mvtsto.facture.coddep,d.depsto.numBonOrigin) FROM DetailMvtsto d"
            + " WHERE d.mvtsto.facture.datbon BETWEEN ?1 and ?2 and  d.mvtsto.facture.categDepot=?3 and d.mvtsto.mvtstoPK.codart in ?4 and d.mvtsto.facture.codAnnul is null and d.mvtsto.facture.numbonRecept is null"
            + " group by  d.mvtsto.mvtstoPK.codart,d.mvtsto.codeSaisi,d.mvtsto.desart,d.mvtsto.desArtSec,d.mvtsto.unite, d.mvtsto.tautva,d.mvtsto.facture.coddep,d.tauxTva,d.depsto.numBonOrigin")
    public List<QtePrixMouvement> findQuantitePrixMouvementAndCodartIn(LocalDateTime datedeb, LocalDateTime datefin, CategorieDepotEnum categ, List<Integer> articleIds);

}
