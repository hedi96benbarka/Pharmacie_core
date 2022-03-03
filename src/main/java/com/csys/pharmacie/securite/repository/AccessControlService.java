package com.csys.pharmacie.securite.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AccessControlService")
public class AccessControlService {

   
    @Autowired
    private AccessMenuRepository accessMenuRepository;
    
    @Autowired
    private AccessFormRepository accessFormRepository;


    public List<String> findAccessMenuByUser(String user) {
        return accessMenuRepository.findAccessMenuByUser(user);
    }
    
    
    public List<String> findAccessFormByUser(String user,String form) {
        return accessFormRepository.findAccessFormByUser(user, form);
    }
}
