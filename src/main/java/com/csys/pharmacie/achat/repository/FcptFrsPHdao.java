//package com.csys.pharmacie.achat.repository;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//
//import com.csys.pharmacie.achat.domain.FcptfrsPH;
//import javax.persistence.PersistenceContext;
//
///**
// *
// * @author farouk
// */
//public class FcptFrsPHdao {
//    //  EntityManager em = FactoriesRepository.GetEntityManager("achaPu");
//
//    @PersistenceContext
//    EntityManager em1;
//
//    public boolean ajoutfichefournisseurPH(BigDecimal mntttc, String libopr, String CodFrs, String numbon, String numaffiche, String typbon, Date dateopr, EntityManager em, String sens) {
//        String param = "Methode: ajoutfichefournisseur \n Param : mntttc = " + mntttc + " libopr= " + libopr + " CodFrs= " + CodFrs + " numbon"
//                + numbon + " numaffiche= " + numaffiche + " typbon= " + typbon + " dateopr= " + dateopr + " sens= " + sens;
//        try {
//
//            FcptfrsPH frs = new FcptfrsPH();
//            frs.setCodFrs(CodFrs);
//            frs.setLibOpr(libopr);
//            frs.setReste(mntttc);
//            frs.setNumBon(numbon);
//            frs.setNumBonAff(numaffiche);
//            frs.setDateOpr(dateopr);
//            frs.setNumFac("");
//            frs.setNumfacture("");
//            frs.setSens("2");
//            frs.setTypBon(typbon);
//            frs.setCodsup("0");
//            frs.setDebit(BigDecimal.ZERO);
//            frs.setSolde(BigDecimal.ZERO);
//            frs.setEtat("0");
//            frs.setRetenu("0");
//
//            frs.setMntOP(BigDecimal.ZERO);
//            frs.setRetenuOP(BigDecimal.ZERO);
//            frs.setDeclar(false);
//            frs.setNumReg("");
//            frs.setNumRegAff("");
//
//            if (sens.equalsIgnoreCase("2")) {
//                frs.setDebit(BigDecimal.ZERO);
//                frs.setCredit(mntttc);
//            } else if (sens.equalsIgnoreCase("1")) {
//                frs.setDebit(mntttc);
//                frs.setCredit(BigDecimal.ZERO);
//            }
//            em.merge(frs);
//
//            return true;
//        } catch (Exception e) {
////              EnvoiMail email = new EnvoiMail();
////            Paramdao p = new Paramdao();
////            String nom_clinique = p.findparamphbycode("NomCli").getValeur();
////            email.sendEmail(nom_clinique + ": Erreur module achat ", param + "\n Erreur : \n" + e.getMessage() + e.toString());
////            Logger.getLogger(FcptFrsPHdao.class.getName()).log(Level.SEVERE, null, e);
//            return false;
//        }
//    }
//
//    public FcptfrsPH deleteFcptfrsByNumBonDao(String numBon, EntityManager em) {
//        try {
//            Query q = em.createNamedQuery("FcptfrsPH.findByNumBon", FcptfrsPH.class).setParameter("numBon", numBon);
//            FcptfrsPH fcptFrs = (FcptfrsPH) q.getSingleResult();
//            fcptFrs = em.merge(fcptFrs);
//            em.remove(fcptFrs);
//            return fcptFrs;
//        } catch (Exception e) {
//            Logger.getLogger(FactureCAdao.class.getName()).log(Level.SEVERE, null, e);
//            return null;
//        }
//    }
//
//    public void modifierfichefournisseurPH(List<String> listebr, EntityManager em) {
//        try {
//            for (String l : listebr) {
//                Query q = em.createNamedQuery("FcptfrsPH.findByNumBon", FcptfrsPH.class).setParameter("numBon", l);
//                FcptfrsPH frs = (FcptfrsPH) q.getResultList().get(0);
//                frs.setCodsup("1");
//            }
//        } catch (Exception e) {
//            Logger.getLogger(FcptFrsPHdao.class.getName()).log(Level.SEVERE, null, e);
//        }
//    }
//}
