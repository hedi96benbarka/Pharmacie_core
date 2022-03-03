/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.service;

import com.csys.pharmacie.transfert.dto.DemandeTransfRecupereeDTO;
import com.csys.pharmacie.transfert.repository.DemandeTransfRecupereeRepository;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.springframework.stereotype.Service;

/**
 *
 * @author Administrateur
 */
@Service
public class DemandeTransfRecupereeService {

    private final DemandeTransfRecupereeRepository demandeTransfRecupereeRepository;

    public DemandeTransfRecupereeService(DemandeTransfRecupereeRepository demandeTransfRecupereeRepository) {
        this.demandeTransfRecupereeRepository = demandeTransfRecupereeRepository;
    }

    public List<DemandeTransfRecupereeDTO> findByDemandeTransfIn(List<Integer> demandeTRIDs) {
        demandeTransfRecupereeRepository.findByCodedemandeTrIn(demandeTRIDs);
        List<DemandeTransfRecupereeDTO> result = demandeTransfRecupereeRepository.findByCodedemandeTrIn(demandeTRIDs).stream().map(item -> new DemandeTransfRecupereeDTO(item.getCodedemandeTr())).collect(toList());
        return result;

    }
}
