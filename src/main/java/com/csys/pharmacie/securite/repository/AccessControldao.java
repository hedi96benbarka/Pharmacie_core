/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.securite.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.csys.pharmacie.securite.entity.AccessControl;
import com.csys.pharmacie.securite.entity.AccessWeb;
import com.csys.pharmacie.securite.entity.AccessWebPK;
import com.csys.pharmacie.securite.entity.UserAction;

import javax.persistence.PersistenceContext;

/**
 *
 * @author ERRAYHAN
 */
public class AccessControldao {

     @PersistenceContext
    EntityManager em1;
    EntityTransaction tx1 = em1.getTransaction();
        public AccessControl authentification(String user, String pass) {
        try {
            Query query2 = em1.createNamedQuery("AccessControl.authentification", AccessControl.class);
            query2.setParameter("userName", user);
            query2.setParameter("passWord", pass);
            List<AccessControl> list = (List<AccessControl>) query2.getResultList();
            if (!list.isEmpty()) {

                return (AccessControl) list.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.getLogger(AccessControldao.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<AccessControl> findallaccess() {
        try {
            Query query = em1.createNamedQuery("AccessControl.findAll", AccessControl.class);
            return query.getResultList();
        } catch (Exception e) {
            Logger.getLogger(AccessControldao.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<String> findallgroupe() {
        try {
            List<String> l = new ArrayList<String>();
            List<String> l1 = new ArrayList<String>();

            Query q = em1.createNativeQuery("select distinct grp from [Access Control]");
            l = q.getResultList();
            for (String l2 : l) {
                String[] grp = l2.split(",");
                l1.addAll(Arrays.asList(grp));
            }
            Set set = new HashSet();
            set.addAll(l1);
            ArrayList distinctList = new ArrayList(set);
            return distinctList;
        } catch (Exception e) {
            Logger.getLogger(AccessControldao.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public boolean ajoutaccesscontrolweb(String l) {
        try {
            tx1.begin();
            JSONArray json = new JSONArray();
            try {
                json = (JSONArray) new JSONParser().parse(l);
            } catch (ParseException ex) {
            }

            for (Object json1 : json) {
                JSONObject object = (JSONObject) json1;
                AccessWeb access = new AccessWeb();
                AccessWebPK pk = new AccessWebPK();
                access.setUtil(Integer.parseInt(String.valueOf(object.get("util"))));
                access.setDroit(Boolean.parseBoolean(String.valueOf(object.get("droit"))));
                pk.setAction(String.valueOf(object.get("action")));
                pk.setUserName(String.valueOf(object.get("userName")));
                access.setAccessWebPK(pk);

                Query q = em1.createNamedQuery("AccessWeb.findByUserNameByaction", AccessWeb.class).setParameter("userName", access.getAccessWebPK().getUserName()).
                        setParameter("action", access.getAccessWebPK().getAction());
                if (q.getResultList().isEmpty() == false) {
                    AccessWeb acc = new AccessWeb();
                    acc = (AccessWeb) q.getResultList().get(0);
                    acc.setDroit(access.getDroit());
                    em1.merge(acc);
                } else {
                    em1.persist(access);
                }

            }

            tx1.commit();
            return true;
        } catch (Exception e) {
            tx1.rollback();
            Logger.getLogger(AccessControldao.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

//    public List<UserAction> getuseraction(String user) {
//        try {
//            String sql = " select a.action ,a.designation_action,w.droit from Access_Web w inner join action1 a on w.action =a.action where username='" + user + "'\n"
//                    + "union all \n"
//                    + "select a.action,a.designation_action ,0 from action1  a where a.action not in (select action  from Access_Web where username='" + user + "')\n"
//                    + "union all\n"
//                    + " select a.action ,a.designation_action,w.droit from Access_Web w inner join action1 a on w.action =a.action where UserName = (select grp from [Access Control] where USERNAME='" + user + "') and w.Action\n"
//                    + " not in (select action  from Access_Web where username='" + user + "' )";
////            String sql = "select a.action ,a.designation_action,w.droit from Access_Web w inner join action a on w.action =a.action where username='" + user + "'\n"
////                    + "union all \n"
////                    + "select a.action,a.designation_action ,0 from action  a where a.action not in (select action  from Access_Web where username='" + user + "')";
//
//            Query q = em1.createNativeQuery(sql, UserAction.class);
//            return q.getResultList();
//        } catch (Exception e) {
//            Logger.getLogger(Otherfunctiondao.class.getName()).log(Level.SEVERE, null, e);
//            return null;
//        }
//    }
//
//    public List<String> getusermodule(String user) {
//        try {
//            String sql = "SELECT distinct Autre \n"
//                    + " FROM Access_Web acc inner join Action1 action on acc.Action=action.Action where acc.UserName='" + user + "' \n"
//                    + " and acc.Droit=1 and Autre is not null";
//            Query q = em1.createNativeQuery(sql);
//            return q.getResultList();
//        } catch (Exception e) {
//            Logger.getLogger(Otherfunctiondao.class.getName()).log(Level.SEVERE, null, e);
//            return null;
//        }
//    }
}
