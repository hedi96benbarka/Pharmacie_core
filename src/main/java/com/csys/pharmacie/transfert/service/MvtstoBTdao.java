///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.csys.pharmacie.transfert.repository;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Query;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//
//import com.csys.pharmacie.achat.domain.MvtStoBA;
//import com.csys.pharmacie.parametrage.repository.Otherfunctiondao;
//import com.csys.pharmacie.transfert.entity.MvtStoBT;
//import com.csys.pharmacie.transfert.entity.MvtStoBTPK;
//
//import helper.FactoriesRepository;
//import helper.Helper;
//
///**
// *
// * @author farouk
// */
//public class MvtstoBTdao {
//
//    EntityManager em1 = FactoriesRepository.GetEntityManager("Gclinique");
//    //EntityTransaction tx = em1.getTransaction();
//    Helper util = com.csys.pharmacie.helper();
//
//    public List<MvtStoBT> getDetailsListeAchatBrphByNumBon(String numBon) {
//        try {
//            List<MvtStoBT> l = new ArrayList<>();
//            Query q1;
//            q1 = em1.createNamedQuery("MvtStoBT.findByNumbon", MvtStoBA.class).setParameter("numbon", numBon);
//            l = q1.getResultList();
//            return l;
//        } catch (Exception e) {
//            Logger.getLogger(MvtstoBTdao.class.getName()).log(Level.SEVERE, null, e);
//            return null;
//        }
//    }
//
//    Boolean ajoutMvtstoBT(JSONArray Mvt, String numBon, String lot,String codDep,String deptr, EntityManager em1) {
//        try {
//
//            List<String> Result;
//            String numOrder = "001";
//          Otherfunctiondao o = new Otherfunctiondao();
//
//            for (Object obj : Mvt) {
//                JSONObject art = (JSONObject) obj;
//                MvtStoBT m = new MvtStoBT();
//                MvtStoBTPK mPK = new MvtStoBTPK();
//                BigDecimal qte=new BigDecimal(String.valueOf(art.get("demande")));
//                mPK.setCodart(String.valueOf(art.get("codArt")));
////                mPK.setCoddep(codDep);
//                mPK.setNumbon(numBon);
//                mPK.setNumordre("BT" + util.IncrementString(numOrder, 3));
////                mPK.setLot(String.valueOf(art.get("lot")));
//                m.setDeptr(deptr);
//                m.setMvtStoBTPK(mPK);
//                m.setQuantite(qte);
//                m.setPriuni(new BigDecimal(String.valueOf(art.get("priUni"))));
//                m.setMontht(BigDecimal.ONE);
//                m.setDatbon(o.getServerDateTime());
//                m.setHeureSysteme(o.getServerDateTime());
//                m.setDesart(String.valueOf(art.get("desArt")));
//                m.setTypbon("BT");
//                m.setQuantite(qte);
//                m.setQtecom(qte);
//                m.setQteben(qte);
//                m.setAncienQte(qte);
//                m.setNbrpce(Short.valueOf("1"));
////                m.setSatisfait("NST");
//                m.setEtatDep('0');
//                m.setNumVir("");
//                m.setNumCha("");
//                m.setNumdoss("");
//                m.setPriAchApresProrata(BigDecimal.ZERO);
//               
//                m.setMajoration(BigDecimal.ZERO);
//                m.setModifier('0');
//                m.setEtat("0");
//                m.setMemoart("");
//                m.setQteArr(BigDecimal.ZERO);
//                m.setQteAvoir(BigDecimal.ZERO);
//                m.setQteRecup(BigDecimal.ZERO);
//                m.setQteDisponible(BigDecimal.ZERO);
//                em1.persist(m);
//                
//            }
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
