/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import static com.csys.pharmacie.config.ServicesConfig.contextReception;
import com.csys.pharmacie.stock.dto.MouvementStockEditionDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Administrateur
 */
public class MouvementStockFactory {

    public static List<MouvementStockEditionDTO> buildMouvementStockEditionDTOs(List<MouvementStockEditionDTO> mouvementStock, List<DepotDTO> depotDTOs) {
        List<MouvementStockEditionDTO> mouvementStockEditionDTOs = new ArrayList();
        mouvementStock.forEach(x -> {
            DepotDTO depotDTO = depotDTOs.stream().filter(d -> d.getCode().equals(x.getCoddep())).findFirst().get();
            mouvementStockEditionDTOs.add(buildMouvementStockEditionDTO(x, depotDTO));
        });
        List<MouvementStockEditionDTO> mouvementStockEditionDTOsfinal = mouvementStockEditionDTOs.stream()
                .collect(Collectors.groupingBy(item -> item.getCoddep(),
                        Collectors.reducing(new MouvementStockEditionDTO(), (a, b) -> {
                            b.setStkDep(b.getStkDep().add(a.getStkDep()));
                            b.setRecep(b.getRecep().add(a.getRecep()));
                            b.setReceptionAnnulee(b.getReceptionAnnulee().add(a.getReceptionAnnulee()));
                            b.setRetourFournisseur(b.getRetourFournisseur().add(a.getRetourFournisseur()));
                            b.setAjustementRetourFournisseurE(b.getAjustementRetourFournisseurE().add(a.getAjustementRetourFournisseurE()));
                            b.setAjustementRetourFournisseurS(b.getAjustementRetourFournisseurS().add(a.getAjustementRetourFournisseurS()));
                            b.setAvoirFournisseur(b.getAvoirFournisseur().add(a.getAvoirFournisseur()));
                            b.setAjustementAvoirFournisseurE(b.getAjustementAvoirFournisseurE().add(a.getAjustementAvoirFournisseurE()));
                            b.setAjustementAvoirFournisseurS(b.getAjustementAvoirFournisseurS().add(a.getAjustementAvoirFournisseurS()));
                            b.setTransent(b.getTransent().add(a.getTransent()));
                            b.setTransf(b.getTransf().add(a.getTransf()));
                            b.setTransfertEA(b.getTransfertEA().add(a.getTransfertEA()));
                            b.setTransfertSA(b.getTransfertSA().add(a.getTransfertSA()));
                            b.setRetourPerime(b.getRetourPerime().add(a.getRetourPerime()));
                            b.setAjustementRetourPerimeE(b.getAjustementRetourPerimeE().add(a.getAjustementRetourPerimeE()));
                            b.setAjustementRetourPerimeS(b.getAjustementRetourPerimeS().add(a.getAjustementRetourPerimeS()));
                            b.setRedressE(b.getRedressE().add(a.getRedressE()));
                            b.setRedressS(b.getRedressS().add(a.getRedressS()));
                            b.setBonPrelevement(b.getBonPrelevement().add(a.getBonPrelevement()));
                            b.setBonPrelevement1(b.getBonPrelevement1().add(a.getBonPrelevement1()));
                            b.setBonPrelevementAnnulee(b.getBonPrelevementAnnulee().add(a.getBonPrelevementAnnulee()));
                            b.setBonRetourPrelevement(b.getBonRetourPrelevement().add(a.getBonRetourPrelevement()));
                            b.setDecoupE(b.getDecoupE().add(a.getDecoupE()));
                            b.setDecoupS(b.getDecoupS().add(a.getDecoupS()));
                            b.setQuittance(b.getQuittance().add(a.getQuittance()));
                            b.setQuittanceAnnulee(b.getQuittanceAnnulee().add(a.getQuittanceAnnulee()));
                            b.setAvoir(b.getAvoir().add(a.getAvoir()));
                            b.setInvenE(b.getInvenE().add(a.getInvenE()));
                            b.setInvenS(b.getInvenS().add(a.getInvenS()));
                            if (contextReception.contains("company")) {
                                b.setTransfertCB(b.getTransfertBC().add(a.getTransfertBC()));
                                b.setTransfertBC(b.getTransfertCB().add(a.getTransfertCB()));
                            } else {
                                b.setTransfertCB(b.getTransfertCB().add(a.getTransfertCB()));
                                b.setTransfertBC(b.getTransfertBC().add(a.getTransfertBC()));
                            }
                            return b;
                        }))).values().stream().collect(Collectors.toList());
        return mouvementStockEditionDTOsfinal;
    }

    private static MouvementStockEditionDTO buildMouvementStockEditionDTO(MouvementStockEditionDTO mouvementStock, DepotDTO depotDTO) {
        MouvementStockEditionDTO mouvementStockEditionDTO = new MouvementStockEditionDTO();

        mouvementStockEditionDTO.setValeur(mouvementStock.getValeur());
        mouvementStockEditionDTO.setTypbon(mouvementStock.getTypbon());

        mouvementStockEditionDTO.setCoddep(mouvementStock.getCoddep());
        mouvementStockEditionDTO.setCodeSaisiDepot(depotDTO.getCodeSaisi());
        mouvementStockEditionDTO.setDesignationDepot(depotDTO.getDesignation());
        switch (mouvementStock.getTypbon()) {
            case "VS":
                mouvementStockEditionDTO.setStkDep(mouvementStock.getValeur());
                break;
            case "BA":
                mouvementStockEditionDTO.setRecep(mouvementStock.getValeur());
                break;
            case "BAA":
                mouvementStockEditionDTO.setReceptionAnnulee(mouvementStock.getValeur());
                break;
            case "RT":
                mouvementStockEditionDTO.setRetourFournisseur(mouvementStock.getValeur());
                break;
            case "AJRFP":
                mouvementStockEditionDTO.setAjustementRetourFournisseurE(mouvementStock.getValeur());
                break;
            case "AJRFM":
                mouvementStockEditionDTO.setAjustementRetourFournisseurS(mouvementStock.getValeur().abs());
                break;
            case "AF":
                mouvementStockEditionDTO.setAvoirFournisseur(mouvementStock.getValeur());
                break;
            case "AJAFP":
                mouvementStockEditionDTO.setAjustementAvoirFournisseurE(mouvementStock.getValeur());
                break;
            case "AJAFM":
                mouvementStockEditionDTO.setAjustementAvoirFournisseurS(mouvementStock.getValeur().abs());
                break;
            case "BTP":
                mouvementStockEditionDTO.setTransent(mouvementStock.getValeur());
                break;
            case "BTM":
                mouvementStockEditionDTO.setTransf(mouvementStock.getValeur());
                break;
            case "BTPA":
                mouvementStockEditionDTO.setTransfertEA(mouvementStock.getValeur());
                break;
            case "BTMA":
                mouvementStockEditionDTO.setTransfertSA(mouvementStock.getValeur());
                break;
            case "RP":
                mouvementStockEditionDTO.setRetourPerime(mouvementStock.getValeur());
                break;
            case "AJRPP":
                mouvementStockEditionDTO.setAjustementRetourPerimeE(mouvementStock.getValeur());
                break;
            case "AJRPM":
                mouvementStockEditionDTO.setAjustementRetourPerimeS(mouvementStock.getValeur().abs());
                break;
            case "BEP":
                mouvementStockEditionDTO.setRedressE(mouvementStock.getValeur());
                break;
            case "BEM":
                mouvementStockEditionDTO.setRedressS(mouvementStock.getValeur());
                break;
            case "PR":
                mouvementStockEditionDTO.setBonPrelevement(mouvementStock.getValeur());
                break;
            case "PRA":
                mouvementStockEditionDTO.setBonPrelevement1(mouvementStock.getValeur());
                break;
            case "PRAA":
                mouvementStockEditionDTO.setBonPrelevementAnnulee(mouvementStock.getValeur());
                break;
            case "RPR":
                mouvementStockEditionDTO.setBonRetourPrelevement(mouvementStock.getValeur());
                break;
            case "DCP":
                mouvementStockEditionDTO.setDecoupE(mouvementStock.getValeur());
                break;
            case "DCM":
                mouvementStockEditionDTO.setDecoupS(mouvementStock.getValeur());
                break;
            case "FE":
                mouvementStockEditionDTO.setQuittance(mouvementStock.getValeur());
                break;
            case "FEA":
                mouvementStockEditionDTO.setQuittanceAnnulee(mouvementStock.getValeur());
                break;
            case "AV":
                mouvementStockEditionDTO.setAvoir(mouvementStock.getValeur());
                break;
            case "INP":
                mouvementStockEditionDTO.setInvenE(mouvementStock.getValeur());
                break;
            case "INM":
                mouvementStockEditionDTO.setInvenS(mouvementStock.getValeur());
                break;
            case "TCB":
                mouvementStockEditionDTO.setTransfertCB(mouvementStock.getValeur());
                break;
            case "TBC":
                mouvementStockEditionDTO.setTransfertBC(mouvementStock.getValeur());
                break;
        }
        return mouvementStockEditionDTO;
    }

    public static List<MouvementStockEditionDTO> buildMouvementStockGrouppedByArticleEditionDTOs(List<MouvementStockEditionDTO> mouvementStock, List<ArticleDTO> articleDTOs) {
        List<MouvementStockEditionDTO> mouvementStockEditionDTOs = new ArrayList();
        mouvementStock.forEach(x -> {
            ArticleDTO articleDTO = articleDTOs.stream().filter(d -> d.getCode().equals(x.getCodeArticle())).findFirst().get();
            mouvementStockEditionDTOs.add(buildMouvementStockGrouppedByArticleEditionDTO(x, articleDTO));
        });
        List<MouvementStockEditionDTO> mouvementStockEditionDTOsfinal = mouvementStockEditionDTOs.stream()
                .collect(Collectors.groupingBy(item -> item.getCodeArticle(),
                        Collectors.reducing(new MouvementStockEditionDTO(), (a, b) -> {
                            b.setStkDep(b.getStkDep().add(a.getStkDep()));
                            b.setQuantiteStkDep(b.getQuantiteStkDep().add(a.getQuantiteStkDep()));
                            b.setRecep(b.getRecep().add(a.getRecep()));
                            b.setQuantiteRecep(b.getQuantiteRecep().add(a.getQuantiteRecep()));
                            b.setReceptionAnnulee(b.getReceptionAnnulee().add(a.getReceptionAnnulee()));
                            b.setQuantiteReceptionAnnulee(b.getQuantiteReceptionAnnulee().add(a.getQuantiteReceptionAnnulee()));
                            b.setRetourFournisseur(b.getRetourFournisseur().add(a.getRetourFournisseur()));
                            b.setQuantiteRetourFournisseur(b.getQuantiteRetourFournisseur().add(a.getQuantiteRetourFournisseur()));
                            b.setAjustementRetourFournisseurE(b.getAjustementRetourFournisseurE().add(a.getAjustementRetourFournisseurE()));
                            b.setQuantiteAjustementRetourFournisseurE(b.getQuantiteAjustementRetourFournisseurE().add(a.getQuantiteAjustementRetourFournisseurE()));
                            b.setAjustementRetourFournisseurS(b.getAjustementRetourFournisseurS().add(a.getAjustementRetourFournisseurS()));
                            b.setQuantiteAjustementRetourFournisseurS(b.getQuantiteAjustementRetourFournisseurS().add(a.getQuantiteAjustementRetourFournisseurS()));
                            b.setAvoirFournisseur(b.getAvoirFournisseur().add(a.getAvoirFournisseur()));
                            b.setQuantiteAvoirFournisseur(b.getQuantiteAvoirFournisseur().add(a.getQuantiteAvoirFournisseur()));
                            b.setAjustementAvoirFournisseurE(b.getAjustementAvoirFournisseurE().add(a.getAjustementAvoirFournisseurE()));
                            b.setQuantiteAjustementAvoirFournisseurE(b.getQuantiteAjustementAvoirFournisseurE().add(a.getQuantiteAjustementAvoirFournisseurE()));
                            b.setAjustementAvoirFournisseurS(b.getAjustementAvoirFournisseurS().add(a.getAjustementAvoirFournisseurS()));
                            b.setQuantiteAjustementAvoirFournisseurS(b.getQuantiteAjustementAvoirFournisseurS().add(a.getQuantiteAjustementAvoirFournisseurS()));
                            b.setTransent(b.getTransent().add(a.getTransent()));
                            b.setQuantiteTransent(b.getQuantiteTransent().add(a.getQuantiteTransent()));
                            b.setTransfertEA(b.getTransfertEA().add(a.getTransfertEA()));
                            b.setQuantiteTransfertSA(b.getQuantiteTransfertSA().add(a.getQuantiteTransfertSA()));
                            b.setTransf(b.getTransf().add(a.getTransf()));
                            b.setQuantiteTransf(b.getQuantiteTransf().add(a.getQuantiteTransf()));
                            b.setRetourPerime(b.getRetourPerime().add(a.getRetourPerime()));
                            b.setQuantiteRetourPerime(b.getQuantiteRetourPerime().add(a.getQuantiteRetourPerime()));
                            b.setAjustementRetourPerimeE(b.getAjustementRetourPerimeE().add(a.getAjustementRetourPerimeE()));
                            b.setQuantiteAjustementRetourPerimeE(b.getQuantiteAjustementRetourPerimeE().add(a.getQuantiteAjustementRetourPerimeE()));
                            b.setAjustementRetourPerimeS(b.getAjustementRetourPerimeS().add(a.getAjustementRetourPerimeS()));
                            b.setQuantiteAjustementRetourPerimeS(b.getQuantiteAjustementRetourPerimeS().add(a.getQuantiteAjustementRetourPerimeS()));
                            b.setRedressE(b.getRedressE().add(a.getRedressE()));
                            b.setQuantiteRedressE(b.getQuantiteRedressE().add(a.getQuantiteRedressE()));
                            b.setRedressS(b.getRedressS().add(a.getRedressS()));
                            b.setQuantiteRedressS(b.getQuantiteRedressS().add(a.getQuantiteRedressS()));
                            b.setBonPrelevement(b.getBonPrelevement().add(a.getBonPrelevement()));
                            b.setQuantiteBonPrelevement(b.getQuantiteBonPrelevement().add(a.getQuantiteBonPrelevement()));
                            b.setBonPrelevement1(b.getBonPrelevement1().add(a.getBonPrelevement1()));
                            b.setQuantiteBonPrelevement1(b.getQuantiteBonPrelevement1().add(a.getQuantiteBonPrelevement1()));
                            b.setBonPrelevementAnnulee(b.getBonPrelevementAnnulee().add(a.getBonPrelevementAnnulee()));
                            b.setQuantiteBonPrelevementAnnulee(b.getQuantiteBonPrelevementAnnulee().add(a.getQuantiteBonPrelevementAnnulee()));
                            b.setBonRetourPrelevement(b.getBonRetourPrelevement().add(a.getBonRetourPrelevement()));
                            b.setQuantiteBonRetourPrelevement(b.getQuantiteBonRetourPrelevement().add(a.getQuantiteBonRetourPrelevement()));
                            b.setDecoupE(b.getDecoupE().add(a.getDecoupE()));
                            b.setQuantiteDecoupE(b.getQuantiteDecoupE().add(a.getQuantiteDecoupE()));
                            b.setDecoupS(b.getDecoupS().add(a.getDecoupS()));
                            b.setQuantiteDecoupS(b.getQuantiteDecoupS().add(a.getQuantiteDecoupS()));
                            b.setQuittance(b.getQuittance().add(a.getQuittance()));
                            b.setQuantiteQuittance(b.getQuantiteQuittance().add(a.getQuantiteQuittance()));
                            b.setQuittanceAnnulee(b.getQuittanceAnnulee().add(a.getQuittanceAnnulee()));
                            b.setQuantiteQuittanceAnnulee(b.getQuantiteQuittanceAnnulee().add(a.getQuantiteQuittanceAnnulee()));
                            b.setAvoir(b.getAvoir().add(a.getAvoir()));
                            b.setQuantiteAvoir(b.getQuantiteAvoir().add(a.getQuantiteAvoir()));
                            b.setInvenE(b.getInvenE().add(a.getInvenE()));
                            b.setQuantiteInvenE(b.getQuantiteInvenE().add(a.getQuantiteInvenE()));
                            b.setInvenS(b.getInvenS().add(a.getInvenS()));
                            b.setQuantiteInvenS(b.getQuantiteInvenS().add(a.getQuantiteInvenS()));
                            return b;
                        }))).values().stream().collect(Collectors.toList());
        return mouvementStockEditionDTOsfinal;
    }

    private static MouvementStockEditionDTO buildMouvementStockGrouppedByArticleEditionDTO(MouvementStockEditionDTO mouvementStock, ArticleDTO articleDTO) {
        MouvementStockEditionDTO mouvementStockEditionDTO = new MouvementStockEditionDTO();

        mouvementStockEditionDTO.setCodeArticle(articleDTO.getCode());
        mouvementStockEditionDTO.setCodeSaisiArticle(articleDTO.getCodeSaisi());
        mouvementStockEditionDTO.setDesignationArticle(articleDTO.getDesignation());
        mouvementStockEditionDTO.setDesignationCategorieArticle(articleDTO.getCategorieArticle().getDesignationSec());

        mouvementStockEditionDTO.setValeur(mouvementStock.getValeur());
        mouvementStockEditionDTO.setTypbon(mouvementStock.getTypbon());
        mouvementStockEditionDTO.setQuantite(mouvementStock.getQuantite());

        mouvementStockEditionDTO.setCoddep(mouvementStock.getCoddep());

        switch (mouvementStock.getTypbon()) {
            case "VS":
                mouvementStockEditionDTO.setStkDep(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteStkDep(mouvementStock.getQuantite());
                break;
            case "BA":
                mouvementStockEditionDTO.setRecep(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteRecep(mouvementStock.getQuantite().abs());
                break;
            case "BAA":
                mouvementStockEditionDTO.setReceptionAnnulee(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteReceptionAnnulee(mouvementStock.getQuantite().abs());
                break;
            case "RT":
                mouvementStockEditionDTO.setRetourFournisseur(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteRetourFournisseur(mouvementStock.getQuantite().abs());
                break;
            case "AJRFP":
                mouvementStockEditionDTO.setAjustementRetourFournisseurE(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteAjustementRetourFournisseurE(BigDecimal.ZERO);
                break;
            case "AJRFM":
                mouvementStockEditionDTO.setAjustementRetourFournisseurS(mouvementStock.getValeur().abs());
                mouvementStockEditionDTO.setQuantiteAjustementRetourFournisseurS(BigDecimal.ZERO);
                break;
            case "AF":
                mouvementStockEditionDTO.setAvoirFournisseur(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteAvoirFournisseur(mouvementStock.getQuantite().abs());
                break;
            case "AJAFP":
                mouvementStockEditionDTO.setAjustementAvoirFournisseurE(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteAjustementAvoirFournisseurE(BigDecimal.ZERO);
                break;
            case "AJAFM":
                mouvementStockEditionDTO.setAjustementAvoirFournisseurS(mouvementStock.getValeur().abs());
                mouvementStockEditionDTO.setQuantiteAjustementAvoirFournisseurS(BigDecimal.ZERO);
                break;
            case "BTP":
                mouvementStockEditionDTO.setTransent(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteTransent(mouvementStock.getQuantite().abs());
                break;
            case "BTM":
                mouvementStockEditionDTO.setTransf(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteTransf(mouvementStock.getQuantite().abs());
                break;
            case "BTPA":
                mouvementStockEditionDTO.setTransfertEA(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteTransfertEA(mouvementStock.getQuantite().abs());
                break;
            case "BTMA":
                mouvementStockEditionDTO.setTransfertSA(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteTransfertSA(mouvementStock.getQuantite().abs());
                break;
            case "RP":
                mouvementStockEditionDTO.setRetourPerime(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteRetourPerime(mouvementStock.getQuantite().abs());
                break;
            case "AJRPP":
                mouvementStockEditionDTO.setAjustementRetourPerimeE(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteAjustementRetourPerimeE(BigDecimal.ZERO);
                break;
            case "AJRPM":
                mouvementStockEditionDTO.setAjustementRetourPerimeS(mouvementStock.getValeur().abs());
                mouvementStockEditionDTO.setQuantiteAjustementRetourPerimeS(BigDecimal.ZERO);
                break;
            case "BEP":
                mouvementStockEditionDTO.setRedressE(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteRedressE(mouvementStock.getQuantite().abs());
                break;
            case "BEM":
                mouvementStockEditionDTO.setRedressS(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteRedressS(mouvementStock.getQuantite().abs());
                break;
            case "PR":
                mouvementStockEditionDTO.setBonPrelevement(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteBonPrelevement(mouvementStock.getQuantite().abs());
                break;
            case "PRA":
                mouvementStockEditionDTO.setBonPrelevement1(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteBonPrelevement1(mouvementStock.getQuantite().abs());
                break;
            case "PRAA":
                mouvementStockEditionDTO.setBonPrelevementAnnulee(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteBonPrelevementAnnulee(mouvementStock.getQuantite().abs());
                break;
            case "RPR":
                mouvementStockEditionDTO.setBonRetourPrelevement(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteBonRetourPrelevement(mouvementStock.getQuantite().abs());
                break;
            case "DCP":
                mouvementStockEditionDTO.setDecoupE(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteDecoupE(mouvementStock.getQuantite().abs());
                break;
            case "DCM":
                mouvementStockEditionDTO.setDecoupS(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteDecoupS(mouvementStock.getQuantite().abs());
                break;
            case "FE":
                mouvementStockEditionDTO.setQuittance(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantite(mouvementStock.getQuantite().abs());
                break;
            case "FEA":
                mouvementStockEditionDTO.setQuittanceAnnulee(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteQuittanceAnnulee(mouvementStock.getQuantite().abs());
                break;
            case "AV":
                mouvementStockEditionDTO.setAvoir(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteAvoir(mouvementStock.getQuantite().abs());
                break;
            case "INP":
                mouvementStockEditionDTO.setInvenE(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteInvenE(mouvementStock.getQuantite().abs());
                break;
            case "INM":
                mouvementStockEditionDTO.setInvenS(mouvementStock.getValeur());
                mouvementStockEditionDTO.setQuantiteInvenS(mouvementStock.getQuantite().abs());
                break;
        }
        return mouvementStockEditionDTO;
    }

}
