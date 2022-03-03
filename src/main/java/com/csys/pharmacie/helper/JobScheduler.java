/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

/**
 *
 * @author bassatine
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.csys.pharmacie.stock.service.ArticleReorderPointPlanningService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author Administrateur
 */
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@Service
public class JobScheduler {

    private final Logger log = LoggerFactory.getLogger(JobScheduler.class);

    private final JdbcTemplate jdbcTemplate;
    private final cron_errorRepository cron_errorRepository;
    private final ArticleReorderPointPlanningService articleReorderPointPlanningService;

    public JobScheduler(JdbcTemplate jdbcTemplate, cron_errorRepository cron_errorRepository, @Lazy ArticleReorderPointPlanningService articleReorderPointPlanningService) {
        this.jdbcTemplate = jdbcTemplate;
        this.cron_errorRepository = cron_errorRepository;
        this.articleReorderPointPlanningService = articleReorderPointPlanningService;
    }

    @Scheduled(cron = "${time-of-cron-valeur-stock}")
    public void myScheduler() {
        try {
            System.out.println("copie de depsto");
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));

            String sql = " Begin tran "
                    + "INSERT INTO param_achat.valeur_stock( code_depsto,datesys,date_job,coddep,codart,Lot_Inter,qte,PU,categ_depot,PMP,unite,DatPer,taux_tva_achat,taux_tva_vente) SELECT D.code,'" + now + "','" + now + "', D.coddep,D.codart,D.Lot_Inter,D.qte,D.PU,D.categ_depot,S.prix_moy_pondere ,D.unite,D.DatPer,D.taux_tva,TV.valeur FROM param_achat.depsto D left outer join  \n"
                    + "param_achat.prix_moy_pondere_article S ON D.codart = S.article  left outer join param_achat.article A on D.codart=A.code left outer join param_achat.tva TV on TV.code=a.fk_tva_codeV where D.qte >0"
                    + "UNION ALL SELECT D.code,'" + now + "','" + now + "', D.coddep,D.codart,D.Lot_Inter,D.qte0,D.PU,D.categ_depot,S.prix_moy_pondere ,D.unite,D.DatPer,D.taux_tva,TV.valeur FROM param_achat.depsto D left outer join  \n"
                    + "param_achat.prix_moy_pondere_article S ON D.codart = S.article  left outer join param_achat.article A on D.codart=A.code left outer join param_achat.tva TV on TV.code=a.fk_tva_codeV where D.qte =0 and D.qte0 IS NOT NULL and D.qte0 > 0 and D.A_Inventorier = 1 and D.numbon_origin <> 'nbo_inj_inv'"
                    + "commit tran";
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            if (!e.getMessage().contains("PRIMARY KEY")) {
                log.error(e.getMessage());
                cron_error val = new cron_error(e.getMessage(), LocalDateTime.now(), e.getCause().toString());
                cron_errorRepository.save(val);
            }

        }

    }

    /**
     * planingRop pour categ PH
     */
    @Scheduled(cron = "${time-of-cron-prepare-data-rop-ph}")
    @SchedulerLock(name = "TaskScheduler_planingRopPHScheduler", lockAtLeastForString = "PT20M", lockAtMostForString = "PT25M")
    public void planingRopPHScheduler() {
        try {
            articleReorderPointPlanningService.excuteJobWithOrder1ForPHGroupped(CategorieDepotEnum.PH);
        } catch (DataAccessException e) {
            cron_error val = new cron_error(e.getMessage(), LocalDateTime.now(), e.getCause().toString());
            cron_errorRepository.save(val);

        }
    }

    @Scheduled(cron = "${time-of-cron-excuted-rop-ph}")
    @SchedulerLock(name = "TaskScheduler_saveArticleReorderPointPHScheduler", lockAtLeastForString = "PT20M", lockAtMostForString = "PT25M")
    public void saveArticleReorderPointPHScheduler() {

        try {
            articleReorderPointPlanningService.excuteJobWithOrder2(CategorieDepotEnum.PH);
        } catch (DataAccessException e) {
            cron_error val = new cron_error(e.getMessage(), LocalDateTime.now(), e.getCause().toString());
            cron_errorRepository.save(val);

        }
    }

    /**
     * planingRop pour categ UU
     */
    @Scheduled(cron = "${time-of-cron-prepare-data-rop-uu}")
    @SchedulerLock(name = "TaskScheduler_planingRopUUScheduler", lockAtLeastForString = "PT20M", lockAtMostForString = "PT25M")
    public void planingRopUUScheduler() {
        try {
            articleReorderPointPlanningService.excuteJobWithOrder1Groupped(CategorieDepotEnum.UU);
        } catch (DataAccessException e) {
            cron_error val = new cron_error(e.getMessage(), LocalDateTime.now(), e.getCause().toString());
            cron_errorRepository.save(val);

        }
    }

    @Scheduled(cron = "${time-of-cron-excuted-rop-uu}")
    @SchedulerLock(name = "TaskScheduler_saveArticleReorderPointUUScheduler", lockAtLeastForString = "PT20M", lockAtMostForString = "PT25M")
    public void saveArticleReorderPointUUScheduler() {

        try {
            articleReorderPointPlanningService.excuteJobWithOrder2(CategorieDepotEnum.UU);
        } catch (DataAccessException e) {
            cron_error val = new cron_error(e.getMessage(), LocalDateTime.now(), e.getCause().toString());
            cron_errorRepository.save(val);

        }
    }

    /**
     * planingRop pour categ EC
     */
    @Scheduled(cron = "${time-of-cron-prepare-data-rop-ec}")
    @SchedulerLock(name = "TaskScheduler_planingRopECScheduler", lockAtLeastForString = "PT20M", lockAtMostForString = "PT25M")
    public void planingRopECScheduler() {
        try {
            articleReorderPointPlanningService.excuteJobWithOrder1Groupped(CategorieDepotEnum.EC);
        } catch (DataAccessException e) {
            cron_error val = new cron_error(e.getMessage(), LocalDateTime.now(), e.getCause().toString());
            cron_errorRepository.save(val);

        }
    }

    @Scheduled(cron = "${time-of-cron-excuted-rop-ec}")
    @SchedulerLock(name = "TaskScheduler_saveArticleReorderPointECScheduler", lockAtLeastForString = "PT20M", lockAtMostForString = "PT25M")
    public void saveArticleReorderPointECScheduler() {

        try {
            articleReorderPointPlanningService.excuteJobWithOrder2(CategorieDepotEnum.EC);
        } catch (DataAccessException e) {
            cron_error val = new cron_error(e.getMessage(), LocalDateTime.now(), e.getCause().toString());
            cron_errorRepository.save(val);

        }
    }

    /**
     * planingRop pour categ IMMO
     */
    @Scheduled(cron = "${time-of-cron-prepare-data-rop-immo}")
    @SchedulerLock(name = "TaskScheduler_planingRopIMMOScheduler", lockAtLeastForString = "PT20M", lockAtMostForString = "PT25M")
    public void planingRopIMMOScheduler() {
        try {
            articleReorderPointPlanningService.excuteJobWithOrder1Groupped(CategorieDepotEnum.IMMO);
        } catch (DataAccessException e) {
            cron_error val = new cron_error(e.getMessage(), LocalDateTime.now(), e.getCause().toString());
            cron_errorRepository.save(val);

        }
    }

    @Scheduled(cron = "${time-of-cron-excuted-rop-immo}")
    @SchedulerLock(name = "TaskScheduler_saveArticleReorderPointIMMOScheduler", lockAtLeastForString = "PT20M", lockAtMostForString = "PT25M")
    public void saveArticleReorderPointIMMOScheduler() {

        try {
            articleReorderPointPlanningService.excuteJobWithOrder2(CategorieDepotEnum.IMMO);
        } catch (DataAccessException e) {
            cron_error val = new cron_error(e.getMessage(), LocalDateTime.now(), e.getCause().toString());
            cron_errorRepository.save(val);

        }
    }

}
