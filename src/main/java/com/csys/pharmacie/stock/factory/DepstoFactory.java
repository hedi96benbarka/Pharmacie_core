/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.dto.DepstoDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class DepstoFactory {

    public static Depsto depstoDTOToDepsto(DepstoDTO depstoDTO) {
        Depsto depSto = new Depsto();
        depSto.setCategDepot(depstoDTO.getCategorieDepot());
        depSto.setDatesys(LocalDateTime.now());
        depSto.setCoddep(depstoDTO.getCodeDepot());
        depSto.setCodart(depstoDTO.getArticleID());
        depSto.setNumBon(depstoDTO.getNumBon());
        depSto.setLotInter(depstoDTO.getLotInter());
//        depSto.setUnite(depstoDTO.getUnityCode());
        /*set juste 3 chars pour etre sur que unite est non concatiné avec code article afin de resoudre BUG on ne sais pas sa cause*/
//        System.out.println("depstoDTO.getUnityCode()"+depstoDTO.getUnityCode());
        depSto.setUnite(Integer.valueOf(depstoDTO.getUnityCode().toString().substring(0, 3)));
        depSto.setDatPer(depstoDTO.getPreemptionDate());
        depSto.setStkrel(depstoDTO.getQuantiteReel());
        depSto.setQte(depstoDTO.getQuantity());
        depSto.setPu(depstoDTO.getPrixAchat());
        depSto.setTauxTva(depstoDTO.getTauxTva());
        depSto.setCodeTva(depstoDTO.getCodeTva());
        return depSto;

    }

    public static DepstoDTO DepstoTodepstoDTO(Depsto depsto) {
        DepstoDTO depstoDTO = new DepstoDTO();
        depstoDTO.setCategorieDepot(depsto.getCategDepot());
        depstoDTO.setCodeDepot(depsto.getCoddep());
        depstoDTO.setArticleID(depsto.getCodart());
        depstoDTO.setNumBon(depsto.getNumBon());
        depstoDTO.setLotInter(depsto.getLotInter());
        depstoDTO.setUnityCode(depsto.getUnite());
        depstoDTO.setPreemptionDate(depsto.getDatPer());
        depstoDTO.setQuantiteReel(depsto.getStkrel());
        depstoDTO.setQuantity(depsto.getQte());
        depstoDTO.setPrixAchat(depsto.getPu());
        depstoDTO.setTauxTva(depsto.getTauxTva());
        depstoDTO.setCodeTva(depsto.getCodeTva());
        return depstoDTO;

    }

    public static List<DepstoDTO> DepstoTodepstoDTOs(List<Depsto> listeDepstos) {
        List<DepstoDTO> depstoDTOs = new ArrayList<>();
        listeDepstos.forEach(x -> {
            depstoDTOs.add(DepstoTodepstoDTO(x));
        });
        return depstoDTOs;
    }
}
