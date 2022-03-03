/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticleIMMODTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.achat.dto.ArticleUuDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.stock.dto.DepstoDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import static com.csys.pharmacie.helper.CategorieDepotEnum.EC;
import static com.csys.pharmacie.helper.CategorieDepotEnum.IMMO;
import static com.csys.pharmacie.helper.CategorieDepotEnum.PH;
import static com.csys.pharmacie.helper.CategorieDepotEnum.UU;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.dto.ArticleStockProjection;
import com.csys.pharmacie.stock.dto.DepstoEditionDTO;
import com.csys.pharmacie.vente.dto.PMPArticleDTO;
import com.csys.util.Preconditions;
import com.csys.pharmacie.stock.dto.DepstoEditionValeurStockDTO;
import static com.csys.pharmacie.stock.factory.DetailDecoupageFactory.LANGUAGE_SEC;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author Administrateur
 */
public class DepstoDTOAssembler {

    private final static Logger log = LoggerFactory.getLogger(DepstoDTOAssembler.class);

    public static DepstoDTO assemble(Depsto depsto, ArticleDTO article) {
        DepstoDTO depstoDTO = new DepstoDTO();
        if (depsto != null) {
            depstoDTO.setCode(depsto.getCode());
            depstoDTO.setArticleID(depsto.getCodart());
            depstoDTO.setUnityCode(depsto.getUnite());

           
            depstoDTO.setPreemptionDate(depsto.getDatPer());
            depstoDTO.setQuantity(depsto.getQte());
            depstoDTO.setPrixAchat(depsto.getPu());
            depstoDTO.setLotInter(depsto.getLotInter());
            depstoDTO.setNumBon(depsto.getNumBon());
            depstoDTO.setQuantiteReel(depsto.getStkrel());
            depstoDTO.setQuantiteTheorique(depsto.getQte0());
            depstoDTO.setCategorieDepot(depsto.getCategDepot());
            depstoDTO.setCodeDepot(depsto.getCoddep());

            //set tva
            depstoDTO.setCodeTva(depsto.getCodeTva());
            depstoDTO.setTauxTva(depsto.getTauxTva());

            if (depsto.getaInventorier() != null) {
                depstoDTO.setIsInInventory(depsto.getaInventorier());
            } else {
                depstoDTO.setIsInInventory(false);
            }
            if (depsto.getDatPer() != null && depsto.getLotInter() != null) {

                depstoDTO.setQrCode(new StringBuilder()
                        .append(depsto.getCodart())
                        .append(";")
                        .append(depsto.getDatPer().format(DateTimeFormatter.BASIC_ISO_DATE))
                        .append(";")
                        .append(depsto.getLotInter())
                        .append(";")
                        .append(depsto.getUnite()).toString());
            }
        }

        if (article != null) {

            depstoDTO.setCodeSaisiArticle(article.getCodeSaisi());
            depstoDTO.setDesignation(article.getDesignation());
            depstoDTO.setDesignationSec(article.getDesignationSec());
            depstoDTO.setCodeTvaVente(article.getCodeTvaVente());
            depstoDTO.setValeurTvaVente(article.getValeurTvaVente());
            depstoDTO.setPrixVente(article.getPrixVente());
            depstoDTO.setDernierPrixAchat(article.getPrixAchat());

            depstoDTO.setUnityDesignation(article.getDesignationUnite());
            depstoDTO.setGenericCode(article.getGenericCode());
            depstoDTO.setGenericDesignation(article.getGenericDesignation());

            if (article.getCategorieDepot() != null) {
                depstoDTO.setCategorieDepot(article.getCategorieDepot());
            }
            if (article.getPerissable() != null) {
                depstoDTO.setPerissable(article.getPerissable());
            }
            if (article.getCategorieArticle() != null) {
                depstoDTO.setCategorieArticle(article.getCategorieArticle().getCode().toString());
            }
        }
        return depstoDTO;
    }

    public static DepstoDTO assemblePH(Depsto depsto, ArticlePHDTO article) {

        ArticleUniteDTO matchingItemUnitDTO = article.getArticleUnites().stream()
                .filter(unit -> unit.getCodeUnite().equals(depsto.getUnite()))
                .findFirst()
                .orElse(null);
        Preconditions.checkBusinessLogique(matchingItemUnitDTO != null, "article-ph.invalid-unity", depsto.getUnite().toString(), depsto.getCodart().toString());
        article.setCodeUnite(matchingItemUnitDTO.getCodeUnite());
        article.setDesignationUnite(matchingItemUnitDTO.getUnityDesignation());
        DepstoDTO depsoDTO = assemble(depsto, (ArticleDTO) article);
        depsoDTO.setPrixVente(matchingItemUnitDTO.getPrixVente());
        return depsoDTO;
    }

    public static List<DepstoDTO> assembleList(List<Depsto> depstos, Collection<ArticleDTO> articles) {
        if (articles != null) {
            return depstos.stream().map(depsto -> {
                ArticleDTO article = articles.stream()
                        .filter(art -> art.getCode().equals(depsto.getCodart()))
                        .findFirst()
                        .orElse(null);
                Preconditions.checkBusinessLogique(article != null, "stock.find-all.missing-article", depsto.getCodart().toString());

                DepstoDTO depsoDTO = DepstoDTOAssembler.assemble(depsto, article);
                return depsoDTO;
            }).collect(toList());
        } else {
            return depstos.stream().map(depsto -> assemble(depsto, null)).collect(toList());
        }
    }

    public static List<DepstoDTO> assembleListPH(List<Depsto> depstos, Collection<ArticlePHDTO> articles) {
        return depstos.stream().filter(item -> item.getCategDepot().equals(PH)).map(depsto -> {
            ArticlePHDTO article = articles.stream()
                    .filter(art -> art.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            Preconditions.checkBusinessLogique(article != null, "stock.find-all.missing-article", depsto.getCodart().toString());
            DepstoDTO depsoDTO = assemblePH(depsto, article);
            return depsoDTO;
        }).collect(toList());
    }

    /**
     *
     * @param depstos
     * @param articles
     * @return list depsto dto having a selling price for matching unit not fix
     * for public but equal to purchase price applying marginal
     */
    public static List<DepstoDTO> assembleListPHhenApplyMarginal(List<Depsto> depstos, Collection<ArticlePHDTO> articles) {
        return depstos.stream().filter(item -> item.getCategDepot().equals(PH)).map(depsto -> {
            ArticlePHDTO articlePHDTO = articles.stream()
                    .filter(art -> art.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            Preconditions.checkBusinessLogique(articlePHDTO != null, "stock.find-all.missing-article", depsto.getCodart().toString());
            DepstoDTO depsoDTO = assemblePHhenApplyMarginal(depsto, articlePHDTO);
            return depsoDTO;
        }).collect(toList());
    }

    public static DepstoDTO assemblePHhenApplyMarginal(Depsto depsto, ArticlePHDTO articlePHDTO) {

        ArticleUniteDTO matchingItemUnitDTO = articlePHDTO.getArticleUnites().stream()
                .filter(unit -> unit.getCodeUnite().equals(depsto.getUnite()))
                .findFirst()
                .orElse(null);
        Preconditions.checkBusinessLogique(matchingItemUnitDTO != null, "article-ph.invalid-unity", depsto.getUnite().toString(), depsto.getCodart().toString());
        Preconditions.checkBusinessLogique(matchingItemUnitDTO.getPrixVenteAvecMarge() != null, "error-item-without-marge", articlePHDTO.getCodeSaisi());
        articlePHDTO.setCodeUnite(matchingItemUnitDTO.getCodeUnite());
        articlePHDTO.setDesignationUnite(matchingItemUnitDTO.getUnityDesignation());
        DepstoDTO depsoDTO = assemble(depsto, (ArticleDTO) articlePHDTO);
        depsoDTO.setPrixVente(matchingItemUnitDTO.getPrixVenteAvecMarge());
        return depsoDTO;
    }

    public static List<DepstoDTO> assembleListUU(List<Depsto> depstos, Collection<ArticleUuDTO> articles) {
        return depstos.stream().filter(item -> item.getCategDepot().equals(UU)).map(depsto -> {
            ArticleUuDTO article = articles.stream()
                    .filter(art -> art.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            Preconditions.checkBusinessLogique(article != null, "stock.find-all.missing-article", depsto.getCodart().toString());

            DepstoDTO depsoDTO = assemble(depsto, article);
            return depsoDTO;
        }).collect(toList());
    }

    public static List<DepstoDTO> assembleListIMMO(List<Depsto> depstos, Collection<ArticleIMMODTO> articles) {
        return depstos.stream().filter(item -> item.getCategDepot().equals(IMMO)).map(depsto -> {
            ArticleIMMODTO article = articles.stream()
                    .filter(art -> art.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            Preconditions.checkBusinessLogique(article != null, "stock.find-all.missing-article", depsto.getCodart().toString());

            DepstoDTO depsoDTO = assemble(depsto, article);
            depsoDTO.setAvecNumeroSerie(article.getAvecNumeroSerie());
            depsoDTO.setDetaille(article.getDetaille());
            return depsoDTO;
        }).collect(toList());
    }

    public static List<DepstoDTO> assembleListEC(List<Depsto> depstos, Collection<ArticleDTO> articles, Collection<PMPArticleDTO> pmps) {
        return depstos.stream().filter(item -> item.getCategDepot().equals(EC)).map(depsto -> {
            ArticleDTO article = articles.stream()
                    .filter(art -> art.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            Preconditions.checkBusinessLogique(article != null, "stock.find-all.missing-article", depsto.getCodart().toString());
            PMPArticleDTO pmp = pmps.stream().filter(item -> item.getArticleID().equals(depsto.getCodart()))
                    .findFirst()
                    .get();
            DepstoDTO depsoDTO = DepstoDTOAssembler.assemble(depsto, article);
            depsoDTO.setPrixVente(pmp.getPMP());
            return depsoDTO;
        }).collect(toList());
    }

    public static List<DepstoDTO> assembleListEC(List<Depsto> depstos, Collection<ArticleDTO> articles) {
        return depstos.stream().filter(item -> item.getCategDepot().equals(EC)).map(depsto -> {
            ArticleDTO article = articles.stream()
                    .filter(art -> art.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            Preconditions.checkBusinessLogique(article != null, "stock.find-all.missing-article", depsto.getCodart().toString());
            DepstoDTO depsoDTO = DepstoDTOAssembler.assemble(depsto, article);
            return depsoDTO;
        }).collect(toList());
    }

    public static DepstoEditionDTO assembleEditionDTO(Depsto depsto, UniteDTO unite) {
        DepstoEditionDTO depstoDTO = new DepstoEditionDTO();
        if (depsto != null) {
           
            depstoDTO.setLotInter(depsto.getLotInter());
            depstoDTO.setDatePer(Date.from(depsto.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            depstoDTO.setQte(depsto.getQte());
            depstoDTO.setPU(depsto.getPu());
            depstoDTO.setCoddep(depsto.getCoddep());
            depstoDTO.setCodart(depsto.getCodart());
        }
        if (unite != null) {
            depstoDTO.setCodunite(unite.getCode());
            depstoDTO.setDesunite(unite.getDesignation());
        }
        return depstoDTO;
    }

    public static DepstoEditionValeurStockDTO assembleDepstoEditionValeurStockDTO(Depsto depsto, ArticleDTO article, UniteDTO unite, DepotDTO depot) {
        DepstoEditionValeurStockDTO depstoDTO = new DepstoEditionValeurStockDTO();
        if (depsto != null) {
            depstoDTO.setDatPer(Date.from(depsto.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            depstoDTO.setQte(depsto.getQte());
            depstoDTO.setPu(depsto.getPu());
            depstoDTO.setLot(depsto.getLotInter());
            depstoDTO.setCoddep(depsto.getCoddep());
            depstoDTO.setCodart(depsto.getCodart());
            depstoDTO.setCodeunite(depsto.getUnite());
            depstoDTO.setTautva(depsto.getTauxTva());
            depstoDTO.setCodtva(depsto.getCodeTva());

            if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
                depstoDTO.setDesignationCategorieArticle(article.getCategorieArticle().getDesignation());
            } else {
                depstoDTO.setDesignationCategorieArticle(article.getCategorieArticle().getDesignationSec());
            }
        }

        if (article != null) {
            depstoDTO.setCodeSaisi(article.getCodeSaisi());
            depstoDTO.setDesart(article.getDesignation());
        }
        if (unite != null) {
            depstoDTO.setDesignationunite(unite.getDesignation());
        }
        if (depot != null) {
            depstoDTO.setCodeSaisiDepot(depot.getCodeSaisi());
            depstoDTO.setDesdep(depot.getDesignation());
        }

        return depstoDTO;
    }

    public static List<DepstoDTO> assembleDepstosWithActualStock(List<Depsto> depstos, List<DepstoDTO> injectDespstoDTOs) {

        List<DepstoDTO> resultAssembleur = new ArrayList<>();

        injectDespstoDTOs.forEach(injectDespstoDTO -> {
            List<Depsto> depstosFiltred = depstos.stream()
                    .filter(d -> d.getCodart().equals(injectDespstoDTO.getArticleID()) && d.getUnite().equals(injectDespstoDTO.getUnityCode()))
                    .collect(toList());

//            if (injectDespstoDTO != null) {
            List<DepstoDTO> dTOs = new ArrayList<>();
            depstosFiltred.forEach(depsto -> {
                dTOs.add(assembleDepstoWithActualStock(depsto, (DepstoDTO) injectDespstoDTO));
            }
            );
            resultAssembleur.addAll(dTOs);
//            } else {
//                return null;
//            }
        });
        return resultAssembleur;
    }

    public static DepstoDTO assembleDepstoWithActualStock(Depsto depsto, DepstoDTO injectDespstoDTO) {
        DepstoDTO depstoDTO = new DepstoDTO();

        if (depsto != null) {
            depstoDTO.setCode(depsto.getCode());
            depstoDTO.setArticleID(depsto.getCodart());
            depstoDTO.setUnityCode(depsto.getUnite());
            depstoDTO.setPreemptionDate(depsto.getDatPer());
            depstoDTO.setLotInter(depsto.getLotInter());
            depstoDTO.setNumBon(depsto.getNumBon());
            depstoDTO.setCodeDepot(depsto.getCoddep());
            depstoDTO.setQuantity(depsto.getQte());
        } else {
            depstoDTO.setArticleID(injectDespstoDTO.getArticleID());
            depstoDTO.setUnityCode(injectDespstoDTO.getUnityCode());
            depstoDTO.setCodeDepot(injectDespstoDTO.getCodeDepot());
            depstoDTO.setQuantity(BigDecimal.ZERO);
        }

        if (injectDespstoDTO != null) {
            depstoDTO.setCodeSaisiArticle(injectDespstoDTO.getCodeSaisiArticle());
            depstoDTO.setDesignation(injectDespstoDTO.getDesignation());
            depstoDTO.setUnityDesignation(injectDespstoDTO.getUnityDesignation());
            depstoDTO.setQuantityOfOldStock(injectDespstoDTO.getQuantityOfOldStock());
        }

        return depstoDTO;
    }

    public static ArticleStockProjection despoToArticleStockProjection(Depsto depsto) {
        ArticleStockProjection articleStockProjection = new ArticleStockProjection();
        if (depsto != null) {
            articleStockProjection.setCodart(depsto.getCodart());
            articleStockProjection.setUnite(depsto.getUnite());
            articleStockProjection.setAvailableQte(depsto.getQte());
        }
        return articleStockProjection;
    }

    public static List<ArticleStockProjection> desposToArticleStockProjectionList(List<Depsto> depstos) {
        List<ArticleStockProjection> articleStockProjectionList = new ArrayList<>();
        depstos.forEach(depsto -> {
            articleStockProjectionList.add(despoToArticleStockProjection(depsto));
        });
        return articleStockProjectionList;
    }
}
