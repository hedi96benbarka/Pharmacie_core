package com.csys.pharmacie.prelevement.factory;

import com.csys.pharmacie.client.dto.ArticleNonMvtDTO;
import com.csys.pharmacie.client.dto.ListeArticleNonMvtDTOWrapper;
import com.csys.pharmacie.client.dto.TypeMvtEnum;
import com.csys.pharmacie.helper.TypeBonEnum;

import com.csys.pharmacie.prelevement.domain.FacturePR;
import com.csys.pharmacie.prelevement.domain.MvtStoPR;
import com.csys.pharmacie.prelevement.dto.FacturePRDTO;
import com.csys.pharmacie.prelevement.dto.FacturePREditionDTO;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FacturePRFactory {

    private final Logger log = LoggerFactory.getLogger(FacturePRFactory.class);

    public static FacturePREditionDTO factureprToFacturePREditionDTO(FacturePR facturepr) {
        FacturePREditionDTO factureprDTO = new FacturePREditionDTO();
        factureprDTO.setRemarque(facturepr.getRemarque());
        factureprDTO.setMntFac(facturepr.getMntFac());
        factureprDTO.setCoddepotSrc(facturepr.getCoddepotSrc());
        factureprDTO.setCoddepartDesti(facturepr.getCoddepartDest());
        factureprDTO.setDetails(MvtStoPRFactory.mvtstoprToMvtStoPREditionDTOs(facturepr.getDetailFacturePRCollection()));
        factureprDTO.setNumaffiche(facturepr.getNumaffiche());
        factureprDTO.setNumbon(facturepr.getNumbon());
        factureprDTO.setCodvend(facturepr.getCodvend());
        factureprDTO.setDatbon(Date.from(facturepr.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        factureprDTO.setTypbon(facturepr.getTypbon());
        factureprDTO.setMotif(MotifFactory.motifToMotifDTO(facturepr.getMotif()));
        factureprDTO.setCategDepot(facturepr.getCategDepot());
        return factureprDTO;
    }

    public static FacturePRDTO factureprToFacturePRDTO(FacturePR facturepr) {

        FacturePRDTO factureprDTO = factureprToFacturePRDTOLazy(facturepr);

        factureprDTO.setDetails(MvtStoPRFactory.mvtstoprToMvtStoPRDTOs(facturepr.getDetailFacturePRCollection()));

        return factureprDTO;
    }

    public static FacturePRDTO factureprToFacturePRDTOLazy(FacturePR facturepr) {

        FacturePRDTO factureprDTO = new FacturePRDTO();

        factureprDTO.setRemarque(facturepr.getRemarque());
        factureprDTO.setMntFac(facturepr.getMntFac());
        factureprDTO.setCoddepotSrc(facturepr.getCoddepotSrc());
        factureprDTO.setCoddepartDesti(facturepr.getCoddepartDest());
        factureprDTO.setNumaffiche(facturepr.getNumaffiche());
        factureprDTO.setCodAnnul(facturepr.getCodAnnul());
        factureprDTO.setDatAnnul(facturepr.getDatAnnul());
        factureprDTO.setNumbon(facturepr.getNumbon());
        factureprDTO.setCodvend(facturepr.getCodvend());
        factureprDTO.setDatbon(facturepr.getDatbon());
        factureprDTO.setDatesys(facturepr.getDatesys());
        factureprDTO.setHeuresys(facturepr.getHeuresys());
        factureprDTO.setTypbon(facturepr.getTypbon());
        factureprDTO.setMotif(MotifFactory.motifToMotifDTO(facturepr.getMotif()));
        factureprDTO.setCategDepot(facturepr.getCategDepot());

        return factureprDTO;
    }

    public static FacturePR factureprDTOToFacturePR(FacturePRDTO factureprDTO) {
        FacturePR facturepr = new FacturePR();

        facturepr.setRemarque(factureprDTO.getRemarque());
        facturepr.setMntFac(factureprDTO.getMntFac());
        facturepr.setCoddepotSrc(factureprDTO.getCoddepotSrc());
        facturepr.setCoddepartDest(factureprDTO.getCoddepartDesti());

        facturepr.setNumaffiche(factureprDTO.getNumaffiche());

        facturepr.setNumbon(factureprDTO.getNumbon());

        facturepr.setTypbon(TypeBonEnum.PR);

        facturepr.setIntegrer(Boolean.FALSE);

        facturepr.setCategDepot(factureprDTO.getCategDepot());

        return facturepr;
    }

    public static FacturePR factureprDTOToFacturePRforUpdate(FacturePRDTO factureprDTO) {

        FacturePR facturepr = new FacturePR();

        facturepr.setRemarque(factureprDTO.getRemarque());
        facturepr.setMntFac(factureprDTO.getMntFac());
        facturepr.setCoddepotSrc(factureprDTO.getCoddepotSrc());
        facturepr.setCoddepartDest(factureprDTO.getCoddepartDesti());
        facturepr.setDetailFacturePRCollection((List<MvtStoPR>) MvtStoPRFactory.MvtStoPRDTOsTomvtstopr(factureprDTO.getDetails()));

        facturepr.setNumbon(factureprDTO.getNumbon());
        facturepr.setCodvend(factureprDTO.getCodvend());
        facturepr.setDatbon(factureprDTO.getDatbon());

        facturepr.setTypbon(TypeBonEnum.PR);

        facturepr.setCategDepot(factureprDTO.getCategDepot());
        return facturepr;

    }

    public static Collection<FacturePRDTO> factureprToFacturePRDTOs(Collection<FacturePR> factureprs) {
        List<FacturePRDTO> factureprsDTO = new ArrayList<>();
        factureprs.forEach(x -> {
            factureprsDTO.add(factureprToFacturePRDTO(x));
        });
        return factureprsDTO;
    }

    public static ListeArticleNonMvtDTOWrapper facturePrDTOToListeArticleNonMvtDTOWrapper(FacturePRDTO factureprDTO) {
         ListeArticleNonMvtDTOWrapper result = new ListeArticleNonMvtDTOWrapper();
        List<ArticleNonMvtDTO> listeArticleNonMvtDTO = new ArrayList();
        factureprDTO.getDetails().forEach(art -> {
            ArticleNonMvtDTO articleNonMvtDTO = new ArticleNonMvtDTO();
            articleNonMvtDTO.setCategDepot(art.getCategDepot());
            articleNonMvtDTO.setCodart(art.getArticleID());
            articleNonMvtDTO.setLastDateMvt(java.util.Date.from(factureprDTO.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
            articleNonMvtDTO.setTypeMvt(TypeMvtEnum.Prelevement);
            articleNonMvtDTO.setNonMoved(Boolean.FALSE);
            listeArticleNonMvtDTO.add(articleNonMvtDTO);
        });
        result.setListeArticleNonMvtDTO(listeArticleNonMvtDTO);
        return result;
    }
}
