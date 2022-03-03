/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.parametrage.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.csys.pharmacie.parametrage.entity.OptionVersionPharmacie;

import javax.persistence.PersistenceContext;

/**
 *
 * @author farouk
 */
public class OptionVersionPharmaciedao {

     @PersistenceContext
    EntityManager em1;

    public List<Integer> getPreemptionParams() {
        try {
            List<Integer> result=new ArrayList<Integer>();
            OptionVersionPharmacie opt = new OptionVersionPharmacie();
            Query q = em1.createNamedQuery("OptionVersionPharmacie.findById", OptionVersionPharmaciedao.class).setParameter("id", "DatPerDefautBA");
            opt = (OptionVersionPharmacie) q.getSingleResult();
            if (opt.getValeur().equalsIgnoreCase("O")) {
                q = em1.createNamedQuery("OptionVersionPharmacie.findById", OptionVersionPharmaciedao.class).setParameter("id", "Interv_Date_Per_Mois");
                Query q1 = em1.createNamedQuery("OptionVersionPharmacie.findById", OptionVersionPharmaciedao.class).setParameter("id", "Insterv_DatPer_CTR");
                opt = (OptionVersionPharmacie) q.getSingleResult();
                result.add(Integer.parseInt(opt.getValeur()));
                opt = (OptionVersionPharmacie) q1.getSingleResult();
                result.add(Integer.parseInt(opt.getValeur()));
            } else {
                result.add(0);
            }
            return result;
        }
         catch(Exception e){
           Logger.getLogger(OptionVersionPharmaciedao.class.getName()).log(Level.SEVERE, null, e);
           return null;
         }
         
         
     }
     
     public String findValeurOptionVersionPharmacieByID(String ID){
         try {             
             return em1.find(OptionVersionPharmacie.class, ID).getValeur();
         }catch (Exception e){           
            Logger.getLogger(OptionVersionPharmaciedao.class.getName()).log(Level.SEVERE, null, e);  
            return null ;
         }
     }
}
