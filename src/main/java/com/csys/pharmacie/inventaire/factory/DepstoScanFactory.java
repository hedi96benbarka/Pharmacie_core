package com.csys.pharmacie.inventaire.factory;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.inventaire.domain.DepstoScan;
import com.csys.pharmacie.inventaire.dto.DepstoScanDTO;
import com.csys.pharmacie.stock.domain.Depsto;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DepstoScanFactory {

    public static DepstoScanDTO depstoscanToDepstoScanDTO(DepstoScan depstoscan) {
        DepstoScanDTO depstoscanDTO = new DepstoScanDTO();
        depstoscanDTO.setNum(depstoscan.getNum());
        depstoscanDTO.setCodart(depstoscan.getCodart());
        depstoscanDTO.setUnite(depstoscan.getUnite());
        depstoscanDTO.setCoddep(depstoscan.getCoddep());
        depstoscanDTO.setCategDepot(depstoscan.getCategDepot());
        depstoscanDTO.setDesart(depstoscan.getDesart());
        depstoscanDTO.setDesartSec(depstoscan.getDesartSec());
        depstoscanDTO.setQuantite(depstoscan.getQuantite());
        depstoscanDTO.setDatPer(depstoscan.getDatPer());
        depstoscanDTO.setDatPerEdition(java.util.Date.from(depstoscan.getDatPer().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()));
        depstoscanDTO.setLotInter(depstoscan.getLotInter());
        depstoscanDTO.setCodeSaisi(depstoscan.getCodeSaisi());
        depstoscanDTO.setDefectueux(depstoscan.getDefectueux());
        depstoscanDTO.setImporter(depstoscan.getImporter());
        depstoscanDTO.setInventerier(depstoscan.getInventerier());
        depstoscanDTO.setHeureSysteme(depstoscan.getHeureSysteme());
        depstoscanDTO.setAdresseMac(depstoscan.getAdresseMac());
        depstoscanDTO.setUserName(depstoscan.getUserName());
        depstoscanDTO.setUniteDesignation(depstoscan.getUniteDesignation());
        depstoscanDTO.setCategArt(depstoscan.getCategArt());
        depstoscanDTO.setInventaire(depstoscan.getCodInv());
        return depstoscanDTO;
    }

    public static DepstoScan depstoscanDTOToDepstoScan(DepstoScanDTO depstoscanDTO) {
        DepstoScan depstoscan = new DepstoScan();
        depstoscan.setNum(depstoscanDTO.getNum());
        depstoscan.setCodart(depstoscanDTO.getCodart());
        /*set juste 3 chars pour etre sur que unite est non concatiné avec code article afin de resoudre BUG on ne sais pas sa cause*/
        depstoscan.setUnite(Integer.valueOf(depstoscanDTO.getUnite().toString().substring(0, 3)));
        depstoscan.setCoddep(depstoscanDTO.getCoddep());
        depstoscan.setCategDepot(depstoscanDTO.getCategDepot());
        depstoscan.setDesart(depstoscanDTO.getDesart());
        depstoscan.setDesartSec(depstoscanDTO.getDesartSec());
        depstoscan.setQuantite(depstoscanDTO.getQuantite());
        depstoscan.setDatPer(depstoscanDTO.getDatPer());
        depstoscan.setLotInter(depstoscanDTO.getLotInter());
        depstoscan.setCodeSaisi(depstoscanDTO.getCodeSaisi());
        depstoscan.setDefectueux(depstoscanDTO.getDefectueux());
        depstoscan.setImporter(depstoscanDTO.getImporter());
        depstoscan.setInventerier(depstoscanDTO.getInventerier());
        depstoscan.setHeureSysteme(LocalDateTime.now());
        depstoscan.setAdresseMac(depstoscanDTO.getAdresseMac());
        depstoscan.setUserName(depstoscanDTO.getUserName());
        depstoscan.setUniteDesignation(depstoscanDTO.getUniteDesignation());
        depstoscan.setCategArt(depstoscanDTO.getCategArt());
        depstoscan.setCodInv(depstoscanDTO.getInventaire());
        return depstoscan;
    }

    public static List<DepstoScanDTO> depstoscanToDepstoScanDTOs(List<DepstoScan> depstoscans) {
        List<DepstoScanDTO> depstoscansDTO = new ArrayList<>();
        depstoscans.forEach(x -> {
            depstoscansDTO.add(depstoscanToDepstoScanDTO(x));
        });
        return depstoscansDTO;
    }

    public static DepstoScanDTO depstoToDepstoScanDTO(Depsto depsto, ArticleDTO articleDTO) {
        DepstoScanDTO depstoscanDTO = new DepstoScanDTO();
//        depstoscanDTO.setNum(depsto.getNum());
        depstoscanDTO.setCodart(depsto.getCodart());
        depstoscanDTO.setUnite(depsto.getUnite());
        depstoscanDTO.setCoddep(depsto.getCoddep());
        depstoscanDTO.setCategDepot(depsto.getCategDepot());
        depstoscanDTO.setQuantite(depsto.getStkrel());
        depstoscanDTO.setDatPer(depsto.getDatPer());
        depstoscanDTO.setLotInter(depsto.getLotInter());
        depstoscanDTO.setDatPerEdition(java.util.Date.from(depsto.getDatPer().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()));
        if (articleDTO != null) {
            depstoscanDTO.setCodeSaisi(articleDTO.getCodeSaisi());
            depstoscanDTO.setDesart(articleDTO.getDesignation());
            depstoscanDTO.setDesartSec(articleDTO.getDesignationSec());
            depstoscanDTO.setUniteDesignation(articleDTO.getDesignationUnite());
        }
        return depstoscanDTO;
    }

    public static List<DepstoScanDTO> depstosToDepstoScanDTOs(List<Depsto> depstos, Collection<ArticleDTO> articleDTOs) {
        List<DepstoScanDTO> depstoscansDTO = new ArrayList<>();
        depstos.forEach(x -> {
            ArticleDTO articleDTO = articleDTOs.stream().filter(art -> x.getCodart().equals(art.getCode())).findFirst().orElse(null);
            depstoscansDTO.add(depstoToDepstoScanDTO(x, articleDTO));
        });
        return depstoscansDTO;
    }
}
