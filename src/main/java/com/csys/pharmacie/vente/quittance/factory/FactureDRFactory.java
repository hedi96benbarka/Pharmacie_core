/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.factory;

import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.vente.quittance.domain.FactureDR;
import com.csys.pharmacie.vente.quittance.domain.MvtstoDR;
import com.csys.pharmacie.vente.quittance.domain.MvtstoDRPK;
import com.csys.pharmacie.vente.quittance.dto.FactureDRDetailDTO;
import com.csys.pharmacie.vente.quittance.dto.MvtstoDRDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class FactureDRFactory {
    
    public static FactureDR FactureDRDTOTOFactureDR(FactureDRDetailDTO factureDRDTO) {
        
        FactureDR factureDR = new FactureDR();
        factureDR.setCategDepot(factureDRDTO.getCategDepot());
        factureDR.setTypbon(TypeBonEnum.DR);
        factureDR.setNumpiece(factureDRDTO.getNumpiece());
        factureDR.setDatepiece(factureDRDTO.getDatepiece());
        factureDR.setSatisf(factureDRDTO.getSatisf());
        factureDR.setMntbon(factureDRDTO.getMntbon());
        factureDR.setMemop(factureDRDTO.getMemop());
        factureDR.setNumdoss(factureDRDTO.getNumdoss());
        factureDR.setNumfacbl(factureDRDTO.getNumfacbl());
        factureDR.setEtatbon(factureDRDTO.getEtatbon());
        factureDR.setRaisoc(factureDRDTO.getRaisoc());
        factureDR.setCoddep(factureDRDTO.getCoddep());
        factureDR.setImprimer(factureDRDTO.isImprimer());
        factureDR.setReffrs(factureDRDTO.getReffrs());
        factureDR.setSexe(factureDRDTO.getSexe());
        factureDR.setNumCha(factureDRDTO.getNumCha());
        factureDR.setReffrs(factureDRDTO.getReffrs());
        factureDR.setDateNaissance(factureDRDTO.getDateNaissance());
        factureDR.setDatreffrs(factureDRDTO.getDatreffrs());
        factureDR.setNumbon(factureDRDTO.getNumbon());
        List<MvtstoDR> detailFactureCollection = new ArrayList();
        for (MvtstoDRDTO mvtstoDRDTO : factureDRDTO.getDetailFactureDTOCollection()) {
            MvtstoDR mvtstoDR = new MvtstoDR();
            MvtstoDRPK mvtstoDRPK = new MvtstoDRPK();
            mvtstoDRPK.setNumbon(factureDRDTO.getNumbon());
            mvtstoDRPK.setCodart(mvtstoDRDTO.getCodart());
            mvtstoDRPK.setNumordre(mvtstoDRDTO.getNumordre());
            mvtstoDR.setMvtstoDRPK(mvtstoDRPK);
            mvtstoDR.setCoddep(mvtstoDRDTO.getCoddep());
            mvtstoDR.setLot(mvtstoDRDTO.getLot());
            mvtstoDR.setTypbon("DR");
            mvtstoDR.setDatbon(mvtstoDRDTO.getDatbon());
            mvtstoDR.setNumfacbl(mvtstoDRDTO.getNumfacbl());
            mvtstoDR.setDatefacbl(mvtstoDRDTO.getDatefacbl());
            mvtstoDR.setDesart(mvtstoDRDTO.getDesart());
            mvtstoDR.setEtatDep(mvtstoDRDTO.getEtatDep());
            mvtstoDR.setPriAchApresProrata(mvtstoDRDTO.getPriAchApresProrata());
            mvtstoDR.setPrixMajore(mvtstoDRDTO.getPrixMajore());
            mvtstoDR.setModifier(mvtstoDRDTO.getModifier());
            mvtstoDR.setHeureSysteme(new Date());
            mvtstoDR.setTxMajoration(mvtstoDRDTO.getTxMajoration());
            mvtstoDR.setNumBonDepot(mvtstoDRDTO.getNumBonDepot());
            mvtstoDR.setReference(mvtstoDRDTO.getReference());
            mvtstoDR.setaRemplacer(mvtstoDRDTO.isaRemplacer());
            mvtstoDR.setModifier(Character.MIN_VALUE);
            mvtstoDR.setDatbon(new Date());
            mvtstoDR.setQuantite(mvtstoDRDTO.getQuantite());
            mvtstoDR.setPriuni(mvtstoDRDTO.getPriuni());
            mvtstoDR.setRemise(mvtstoDRDTO.getRemise());
            mvtstoDR.setMontht(mvtstoDRDTO.getMontht());
            mvtstoDR.setTautva(mvtstoDRDTO.getTautva());
            mvtstoDR.setFodart(mvtstoDRDTO.getFodart());
            mvtstoDR.setHomologue(mvtstoDRDTO.getHomologue());
            mvtstoDR.setDeptr(mvtstoDRDTO.getDeptr());
            mvtstoDR.setPriach(mvtstoDRDTO.getPriach());
            mvtstoDR.setMarge(mvtstoDRDTO.getMarge());
            mvtstoDR.setNbrpce(mvtstoDRDTO.getNbrpce());
            mvtstoDR.setMemoart(mvtstoDRDTO.getMemoart());
            mvtstoDR.setAncienQte(mvtstoDRDTO.getAncienQte());
            mvtstoDR.setAncienValrem(mvtstoDRDTO.getAncienValrem());
            mvtstoDR.setAncienValfod(mvtstoDRDTO.getAncienValfod());
            mvtstoDR.setNumaffiche(factureDRDTO.getNumbon());
            mvtstoDR.setSolde(mvtstoDRDTO.getSolde());
            mvtstoDR.setCodtva(mvtstoDRDTO.getCodtva());
            mvtstoDR.setTypmvt(mvtstoDRDTO.getTypmvt());
            mvtstoDR.setQteben(mvtstoDRDTO.getQteben());
            mvtstoDR.setPrixben(mvtstoDRDTO.getPrixben());
            mvtstoDR.setPvp(mvtstoDRDTO.getPvp());
            mvtstoDR.setMajoration(mvtstoDRDTO.getMajoration());
            mvtstoDR.setPrixben(mvtstoDRDTO.getPrixben());
            mvtstoDR.setQuantite(mvtstoDRDTO.getQuantite());
            mvtstoDR.setGest(mvtstoDRDTO.getGest());
            mvtstoDR.setDatArr(mvtstoDRDTO.getDatArr());
            mvtstoDR.setQteArr(mvtstoDRDTO.getQteArr());
            mvtstoDR.setNumCha(mvtstoDRDTO.getNumCha());
            mvtstoDR.setUtilis(mvtstoDRDTO.getUtilis());
            mvtstoDR.setSatisfait(mvtstoDRDTO.getSatisfait());
            mvtstoDR.setQtecom(mvtstoDRDTO.getQtecom());
            mvtstoDR.setNumdoss(mvtstoDRDTO.getNumdoss());
            mvtstoDR.setMedtrait(mvtstoDRDTO.getMedtrait());
            mvtstoDR.setEtat(mvtstoDRDTO.getEtat());
            mvtstoDR.setDatinv(mvtstoDRDTO.getDatinv());
            mvtstoDR.setNumVir(mvtstoDRDTO.getNumVir());
            mvtstoDR.setEtatDep(mvtstoDRDTO.getEtatDep());
            mvtstoDR.setDatinv(mvtstoDRDTO.getDatinvdep());
            mvtstoDR.setDatPer(mvtstoDRDTO.getDatPer());
            mvtstoDR.setPoids(mvtstoDRDTO.getPoids());
            mvtstoDR.setQtePans(mvtstoDRDTO.getQtePans());
            mvtstoDR.setQteAnest(mvtstoDRDTO.getQteAnest());
            mvtstoDR.setSatisfait(mvtstoDRDTO.getSatisfait());
            mvtstoDR.setQteComSauv(mvtstoDRDTO.getQteComSauv());
            mvtstoDR.setCodTvaAch(mvtstoDRDTO.getCodTvaAch());
            mvtstoDR.setTautva(mvtstoDRDTO.getTauTvaAch());
            mvtstoDR.setUnite(mvtstoDRDTO.getUnite());
            mvtstoDR.setDesart(mvtstoDRDTO.getDesart());
            mvtstoDR.setDesartSec(mvtstoDRDTO.getDesartSec());
            mvtstoDR.setCodeSaisi(mvtstoDRDTO.getCodeSaisi());
            mvtstoDR.setDateSysteme(new Date());
            mvtstoDR.setCodeUnite(mvtstoDRDTO.getCodeUnite());
            mvtstoDR.setPerissable(mvtstoDRDTO.getPerissable());
            detailFactureCollection.add(mvtstoDR);
        }
        factureDR.setDetailFactureCollection(detailFactureCollection);
        return factureDR;
        
    }
}
