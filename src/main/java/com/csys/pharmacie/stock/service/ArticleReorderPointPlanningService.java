package com.csys.pharmacie.stock.service;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.stock.domain.ArticleReorderPointPlanning;
import com.csys.pharmacie.stock.domain.QArticleReorderPoint;
import com.csys.pharmacie.stock.domain.QArticleReorderPointPlanning;
import com.csys.pharmacie.stock.dto.ArticleReorderPointPlanningDTO;
import com.csys.pharmacie.stock.factory.ArticleReorderPointPlanningFactory;
import com.csys.pharmacie.stock.repository.ArticleReorderPointPlanningRepository;
import com.csys.util.Preconditions;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArticleReorderPointPlanningService {

    private final Logger log = LoggerFactory.getLogger(ArticleReorderPointPlanningService.class);

    private final ArticleReorderPointPlanningRepository articleReorderPointPlanningRepository;
    private final ArticleReorderPointService articleReorderPointService;
    private final JdbcTemplate jdbcTemplate;

    public ArticleReorderPointPlanningService(ArticleReorderPointPlanningRepository articleReorderPointPlanningRepository, @Lazy ArticleReorderPointService articleReorderPointService, JdbcTemplate jdbcTemplate) {
        this.articleReorderPointPlanningRepository = articleReorderPointPlanningRepository;
        this.articleReorderPointService = articleReorderPointService;
        this.jdbcTemplate = jdbcTemplate;
    }

    public ArticleReorderPointPlanningDTO save(ArticleReorderPointPlanningDTO articlereorderpointplanningDTO) {
        log.debug("Request to save ArticleReorderPointPlanning: {}", articlereorderpointplanningDTO);
        Preconditions.checkBusinessLogique(articleReorderPointPlanningRepository.findFirstByCategDepotAndPlannedTrue(articlereorderpointplanningDTO.getCategDepot()) == null, "rop.planned.exist");
        articlereorderpointplanningDTO.setDateAuReference(articlereorderpointplanningDTO.getDateAuReference().withHour(23).withMinute(59).withSecond(59));

        ArticleReorderPointPlanning articlereorderpointplanning = ArticleReorderPointPlanningFactory.articlereorderpointplanningDTOToArticleReorderPointPlanning(articlereorderpointplanningDTO);
        articlereorderpointplanning = articleReorderPointPlanningRepository.save(articlereorderpointplanning);
        ArticleReorderPointPlanningDTO resultDTO = ArticleReorderPointPlanningFactory.articlereorderpointplanningToArticleReorderPointPlanningDTO(articlereorderpointplanning);
        return resultDTO;
    }

    public ArticleReorderPointPlanningDTO getPlanningWithDataNotPreparedAndStdExecuted(CategorieDepotEnum categDepot) {
        ArticleReorderPointPlanning articlereorderpointplanning = articleReorderPointPlanningRepository.findFirstByCategDepotAndPlannedTrueAndLtpExecutedTrueAndDataPreparedFalseAndExecutedFalse(categDepot);
        return ArticleReorderPointPlanningFactory.articlereorderpointplanningToArticleReorderPointPlanningDTO(articlereorderpointplanning);
    }

    public ArticleReorderPointPlanningDTO getPlanningWithDataPreparedAndLtpExecuted(CategorieDepotEnum categDepot) {
        ArticleReorderPointPlanning articlereorderpointplanning = articleReorderPointPlanningRepository.findFirstByCategDepotAndPlannedTrueAndLtpExecutedTrueAndDataPreparedTrueAndExecutedFalse(categDepot);
        return ArticleReorderPointPlanningFactory.articlereorderpointplanningToArticleReorderPointPlanningDTO(articlereorderpointplanning);
    }

    public void updateDataPrepared(Integer id) {
        ArticleReorderPointPlanning articlereorderpointplanning = articleReorderPointPlanningRepository.findOne(id);

        //normalement on n'a pas besoin des precondition puisque date selectes from DB filtrer
        Preconditions.checkBusinessLogique(articlereorderpointplanning != null, "articlereorderpointplanning does not exist");
        Preconditions.checkBusinessLogique(Boolean.TRUE.equals(articlereorderpointplanning.isLtpExecuted()), "ltd not excuted");
        Preconditions.checkBusinessLogique(Boolean.FALSE.equals(articlereorderpointplanning.isDataPrepared()), "data is already prepared");
        articlereorderpointplanning.setDataPrepared(Boolean.TRUE);
    }

    public void updateExcuted(Integer id) {
        ArticleReorderPointPlanning articlereorderpointplanning = articleReorderPointPlanningRepository.findOne(id);
        Preconditions.checkBusinessLogique(articlereorderpointplanning != null, "articlereorderpointplanning does not exist");
        Preconditions.checkBusinessLogique(Boolean.FALSE.equals(articlereorderpointplanning.isExecuted()), "data is already excuted");
        Preconditions.checkBusinessLogique(Boolean.TRUE.equals(articlereorderpointplanning.isDataPrepared()), "data not prepared");
        articlereorderpointplanning.setStdExecuted(Boolean.TRUE);
        articlereorderpointplanning.setExecuted(Boolean.TRUE);
        articlereorderpointplanning.setPlanned(Boolean.FALSE);
    }

    public Boolean updateLtpExecuted(Integer id) {
        ArticleReorderPointPlanning articleReorderPointPlanning = articleReorderPointPlanningRepository.findOne(id);
        Preconditions.checkBusinessLogique(articleReorderPointPlanning != null, "articlereorderpointplanning does not exist");
        Preconditions.checkBusinessLogique(Boolean.FALSE.equals(articleReorderPointPlanning.isLtpExecuted()), "ltd is already excuted");
        articleReorderPointPlanning.setLtpExecuted(Boolean.TRUE);
        return Boolean.TRUE;
    }

    @Transactional(readOnly = true)
    public ArticleReorderPointPlanningDTO findOne(Integer id) {
        log.debug("Request to get ArticleReorderPointPlanningDTO: {}", id);
        ArticleReorderPointPlanning articlereorderpoint = articleReorderPointPlanningRepository.findOne(id);
        Preconditions.checkBusinessLogique(articlereorderpoint != null, "ArticleReorderPoint does not exist");
        ArticleReorderPointPlanningDTO dto = ArticleReorderPointPlanningFactory.articlereorderpointplanningToArticleReorderPointPlanningDTO(articlereorderpoint);
        return dto;
    }

    public void saveArticleReorderPointIfJobNotWorking(Integer id) {
        ArticleReorderPointPlanningDTO articleReorderPointPlanningDTO = this.findOne(id);
        articleReorderPointService.saveByPlaning(articleReorderPointPlanningDTO);
    }

//    public void excuteJobWithOrder1(CategorieDepotEnum categDepot) {
//        ArticleReorderPointPlanningDTO planingRop = this.getPlanningWithDataNotPreparedAndStdExecuted(categDepot);
//        log.debug("planingRopPh is", planingRop);
//        if (planingRop != null) {
//            System.out.println("planingRopPh is:" + planingRop.toString());
//            String sql = " Begin tran "
//                    + "INSERT INTO [param_achat].[consommation_reelle_for_rop] \n"
//                    + "select * from param_achat.ConsomationReels vueCR\n"
//                    + "  where vueCR.categ_depot='" + planingRop.getCategDepot().categ() + "' and vueCR.date>= '" + planingRop.getDateDuReference().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")) + "' and vueCR.date<='" + planingRop.getDateAuReference().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")) + "' "
//                    + "commit tran";
//            System.out.println("log requete sql is:" + sql);
//            int rowsInserted = jdbcTemplate.update(sql);
//            if (rowsInserted > 0) {
//                this.updateDataPrepared(planingRop.getCode());
//            }
//        }
//    }
    public void excuteJobWithOrder1ForPHGroupped(CategorieDepotEnum categDepot) {
        ArticleReorderPointPlanningDTO planingRop = this.getPlanningWithDataNotPreparedAndStdExecuted(categDepot);
        log.debug("planingRopPh is", categDepot.categ());
        if (planingRop != null) {
            System.out.println("planingRopPh is:" + categDepot.categ());
            String sql = " Begin tran "
                    + "INSERT INTO [param_achat].[consommation_reelle_for_rop] "
                    + "([categ_depot]\n"
                    + ",[codart]\n"
                    + ",[codeUnite]\n"
                    + ",[quantite]\n"
                    + ",[valeur])\n "
                    + "select "
                    + "vueCR.categ_depot,\n"
                    + "vueCR.codart,\n "
                    + "a.fk_unite_code,"
                    + "sum(vueCR.quantite/au.nbre_piece) as quantite, \n"
                    + "sum((vueCR.quantite/au.nbre_piece)*(1+vueCR.tauTvaAch/100)*vueCR.priach) as valeur\n"
                    + "from param_achat.ConsomationReels vueCR\n"
                    + "inner join param_achat.article a on a.code = codart\n"
                    + "inner join param_achat.article_unite au on au.fk_article_code = codart and au.fk_unite_code = codeUnite "
                    + "where vueCR.categ_depot='" + planingRop.getCategDepot().categ() + "' and vueCR.date>= '" + planingRop.getDateDuReference().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")) + "' and vueCR.date<='" + planingRop.getDateAuReference().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")) + "' "
                    + "and a.regeneration =1 "
                    + "and vueCR.coddep not in (select code from param_achat.depot where DepotFrs =1)"
                    + "group by vueCR.codart ,a.fk_unite_code, vueCR.categ_depot "
                    + "commit tran";
            System.out.println("log requete sql is:" + sql);
            int rowsInserted = jdbcTemplate.update(sql);
            if (rowsInserted > 0) {
                this.updateDataPrepared(planingRop.getCode());
            }
        }
    }

    //categorie depot not PH
    public void excuteJobWithOrder1Groupped(CategorieDepotEnum categDepot) {
        ArticleReorderPointPlanningDTO planingRop = this.getPlanningWithDataNotPreparedAndStdExecuted(categDepot);
        log.debug("planingRop is : {}", categDepot.categ());
        if (planingRop != null) {
            System.out.println("planingRop is : {}" + categDepot.categ());
            String sql = " Begin tran "
                    + "INSERT INTO [param_achat].[consommation_reelle_for_rop] "
                    + "([categ_depot]\n"
                    + ",[codart]\n"
                    + ",[codeUnite]\n"
                    + ",[quantite]\n"
                    + ",[valeur])\n "
                    + "select "
                    + "vueCR.categ_depot,\n"
                    + "vueCR.codart,\n "
                    + "a.fk_unite_code,"
                    + "sum(vueCR.quantite) as quantite, \n"
                    + "sum((vueCR.quantite)*(1+vueCR.tauTvaAch/100)*vueCR.priach) as valeur\n"
                    + "from param_achat.ConsomationReels vueCR\n"
                    + "inner join param_achat.article a on a.code = codart\n"
                    + "where vueCR.categ_depot='" + planingRop.getCategDepot().categ() + "' and vueCR.date>= '" + planingRop.getDateDuReference().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")) + "' and vueCR.date<='" + planingRop.getDateAuReference().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")) + "' "
                    + "and a.regeneration =1 "
                    + "and vueCR.coddep not in (select code from param_achat.depot where DepotFrs =1)"
                    + "group by vueCR.codart ,a.fk_unite_code, vueCR.categ_depot "
                    + "commit tran";
            System.out.println("log requete sql is:" + sql);
            int rowsInserted = jdbcTemplate.update(sql);
            if (rowsInserted > 0) {
                this.updateDataPrepared(planingRop.getCode());
            }
        }
    }

    public void excuteJobWithOrder2(CategorieDepotEnum categDepot) {
        ArticleReorderPointPlanningDTO planingRopPh = this.getPlanningWithDataPreparedAndLtpExecuted(categDepot);
        if (planingRopPh != null) {
            System.out.println("planingRopPh is:" + planingRopPh.toString());
            articleReorderPointService.saveByPlaning(planingRopPh);
        }
    }

    @Transactional(readOnly = true)
    public Collection<ArticleReorderPointPlanningDTO> findAll(CategorieDepotEnum categorieDepot, Boolean planned, Boolean ltpExecuted, Boolean executed) {
        log.debug("Request to get all ArticleReorderPointPlanning");
        QArticleReorderPointPlanning articleAOP = QArticleReorderPointPlanning.articleReorderPointPlanning;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .and(articleAOP.categDepot.eq(categorieDepot))
                .optionalAnd(planned, () -> articleAOP.planned.eq(planned))
                .optionalAnd(ltpExecuted, () -> articleAOP.ltpExecuted.eq(ltpExecuted))
                .optionalAnd(executed, () -> articleAOP.executed.eq(executed));
        List<ArticleReorderPointPlanning> articleReorderPointPlannings = articleReorderPointPlanningRepository.findAll(builder);
        Collection<ArticleReorderPointPlanningDTO> articleReorderPointDTOs = ArticleReorderPointPlanningFactory.articleReorderPointPlanningsToArticleReorderPointPlanningDTOs((Collection<ArticleReorderPointPlanning>) articleReorderPointPlannings);
        return articleReorderPointDTOs;
    }

}
