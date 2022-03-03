package com.csys.pharmacie.vente.quittance.factory;

import com.csys.pharmacie.client.dto.ArticleNonMvtDTO;
import com.csys.pharmacie.client.dto.ListeArticleNonMvtDTOWrapper;
import com.csys.pharmacie.client.dto.TypeMvtEnum;
import com.csys.pharmacie.helper.BaseTVAFactory;
import com.csys.pharmacie.vente.quittance.domain.BaseTvaFacture;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import com.csys.pharmacie.vente.quittance.dto.FactureDTO;
import com.csys.pharmacie.vente.quittance.dto.FactureEditionDTO;
import com.csys.pharmacie.vente.quittance.dto.MvtstoDTO;
import static com.csys.pharmacie.vente.quittance.factory.MvtstoFactory.LANGUAGE_SEC;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FactureFactory {

    @Autowired
    BaseTVAFactory baseTVAFactory;
    @Autowired
    MvtstoFactory mvtstoFactory;
    
    private static Logger log = LoggerFactory.getLogger(FactureFactory.class);
    
    

    public FactureDTO factureToFactureDTO(Facture facture) {
        FactureDTO factureDTO = new FactureDTO();
        factureDTO.setMntbon(facture.getMntbon());
        factureDTO.setMemop(facture.getMemop());
        factureDTO.setNumdoss(facture.getNumdoss());
        factureDTO.setCodAnnul(facture.getCodAnnul());
        factureDTO.setDatAnnul(facture.getDatAnnul());
        factureDTO.setCodeDepot(facture.getCoddep());
        factureDTO.setNumbon(facture.getNumbon());
        factureDTO.setCodvend(facture.getCodvend());
        factureDTO.setDatbon(facture.getDatbon());
        factureDTO.setDatesys(facture.getDatesys());
        factureDTO.setHeuresys(facture.getHeuresys());
        factureDTO.setTypbon(facture.getTypbon());
        factureDTO.setNumaffiche(facture.getNumaffiche());
        factureDTO.setCategDepot(facture.getCategDepot());

        factureDTO.setCodfrs(facture.getCodfrs());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            factureDTO.setReffrs(facture.getReffrsAr());
            factureDTO.setReffrsAr(facture.getReffrs());
        } else {
            factureDTO.setReffrs(facture.getReffrs());
            factureDTO.setReffrsAr(facture.getReffrsAr());
        }
        factureDTO.setNumbonRecept(facture.getNumbonRecept());
        factureDTO.setNumbonTransfert(facture.getNumbonTransfert());
        factureDTO.setPanier(facture.getPanier());
        factureDTO.setCodePrestationPanier(facture.getCodePrestation());
        factureDTO.setCodeOperation(facture.getCodeOperation());
        factureDTO.setPartiePEC(facture.getPartiePEC());
        factureDTO.setPartiePatient(facture.getPartiePatient());
        factureDTO.setNumbonComplementaire(facture.getNumbonComplementaire());
        factureDTO.setIdOrdonnance(facture.getIdOrdonnance());
        return factureDTO;
    }
  public FactureEditionDTO factureToFactureEditionDTO(Facture facture) {
        FactureEditionDTO factureavDTO = new FactureEditionDTO();
        factureavDTO.setMntbon(facture.getMntbon());
        factureavDTO.setMemop(facture.getMemop());
        factureavDTO.setNumdoss(facture.getNumdoss());
        factureavDTO.setCodeDepot(facture.getCoddep());
        factureavDTO.setBasetvaFactureCollection(baseTVAFactory.listeEntitiesToListDTos(facture.getBaseTvaFactureList()));
        factureavDTO.setMvtStoCollection(mvtstoFactory.mvtstoToMvtstoDTOs(facture.getMvtstoCollection()));
        factureavDTO.setNumbon(facture.getNumbon());
        factureavDTO.setCodvend(facture.getCodvend());
        factureavDTO.setDatbon(Date.from(facture.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        factureavDTO.setDatesys(facture.getDatesys());
        factureavDTO.setHeuresys(facture.getHeuresys());
        factureavDTO.setTypbon(facture.getTypbon());
        factureavDTO.setNumaffiche(facture.getNumaffiche());
        factureavDTO.setCategDepot(facture.getCategDepot());
        factureavDTO.setImprimer(facture.isImprimer());
        factureavDTO.setPartiePEC(facture.getPartiePEC());
        factureavDTO.setPartiePatient(facture.getPartiePatient());
        return factureavDTO;
    }
    public FactureDTO factureToFactureDTOLazy(Facture facture) {
        FactureDTO factureDTO = new FactureDTO();
        factureDTO.setMntbon(facture.getMntbon());
        factureDTO.setMemop(facture.getMemop());
        factureDTO.setNumdoss(facture.getNumdoss());
        factureDTO.setCodAnnul(facture.getCodAnnul());
        factureDTO.setDatAnnul(facture.getDatAnnul());
        factureDTO.setCodeDepot(facture.getCoddep());
        factureDTO.setBaseTvaFactureList(baseTVAFactory.listeEntitiesToListDTos(facture.getBaseTvaFactureList()));
        factureDTO.setMvtstoCollection(mvtstoFactory.mvtstoToMvtstoDTOs(facture.getMvtstoCollection()));
        factureDTO.setNumbon(facture.getNumbon());
        factureDTO.setCodvend(facture.getCodvend());
        factureDTO.setDatbon(facture.getDatbon());
        factureDTO.setDatesys(facture.getDatesys());
        factureDTO.setHeuresys(facture.getHeuresys());
        factureDTO.setTypbon(facture.getTypbon());
        factureDTO.setNumaffiche(facture.getNumaffiche());
        factureDTO.setCategDepot(facture.getCategDepot());

        factureDTO.setCodfrs(facture.getCodfrs());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            factureDTO.setReffrs(facture.getReffrsAr());
            factureDTO.setReffrsAr(facture.getReffrs());
        } else {
            factureDTO.setReffrs(facture.getReffrs());
            factureDTO.setReffrsAr(facture.getReffrsAr());
        }
        factureDTO.setNumbonRecept(facture.getNumbonRecept());
        factureDTO.setNumbonTransfert(facture.getNumbonTransfert());
        factureDTO.setPanier(facture.getPanier());
        factureDTO.setCodePrestationPanier(facture.getCodePrestation());
        factureDTO.setCodeOperation(facture.getCodeOperation());
        factureDTO.setPartiePEC(facture.getPartiePEC());
        factureDTO.setPartiePatient(facture.getPartiePatient());
        factureDTO.setNumbonComplementaire(facture.getNumbonComplementaire());
        factureDTO.setIdOrdonnance(facture.getIdOrdonnance());
        factureDTO.setCodeCostCenterAnalytique(facture.getCodeCostCenterAnalytique());
        return factureDTO;
    }

    public Facture factureDTOToFacture(FactureDTO factureDTO) {
        Facture facture = new Facture();
        facture.setMntbon(factureDTO.getMntbon());
        facture.setMemop(factureDTO.getMemop());
        facture.setNumdoss(factureDTO.getNumdoss());
        facture.setCodAnnul(factureDTO.getCodAnnul());
        facture.setDatAnnul(factureDTO.getDatAnnul());
        facture.setCoddep(factureDTO.getCodeDepot());
        List<BaseTvaFacture> listeBasesTVA = new ArrayList();
        factureDTO.getBaseTvaFactureList().forEach(baseTVADTO -> {
            BaseTvaFacture baseTVAFacture = new BaseTvaFacture();
            baseTVAFactory.toEntity(baseTVAFacture, baseTVADTO);
            baseTVAFacture.setFacture(facture);
            listeBasesTVA.add(baseTVAFacture);
        });
        facture.setBaseTvaFactureList(listeBasesTVA);
        facture.setMvtstoCollection(mvtstoFactory.mvtstoDTOToMvtstos(factureDTO.getMvtstoCollection()));
        facture.setNumbon(factureDTO.getNumbon());
        facture.setCodvend(factureDTO.getCodvend());
        facture.setDatbon(factureDTO.getDatbon());
        facture.setDatesys(factureDTO.getDatesys());
        facture.setHeuresys(factureDTO.getHeuresys());
        facture.setTypbon(factureDTO.getTypbon());
        facture.setNumaffiche(factureDTO.getNumaffiche());
        facture.setCategDepot(factureDTO.getCategDepot());
        facture.setPartiePEC(factureDTO.getPartiePEC());
        facture.setPartiePatient(factureDTO.getPartiePatient());
        return facture;
    }

    public List<FactureDTO> factureToFactureDTOs(List<Facture> factures) {
        List<FactureDTO> facturesDTO = new ArrayList<>();
        factures.forEach(x -> {
            facturesDTO.add(factureToFactureDTO(x));
        });
        return facturesDTO;
    }

    public List<FactureDTO> factureToFactureDTOLazys(List<Facture> factures) {
        List<FactureDTO> facturesDTO = new ArrayList<>();
        factures.forEach(x -> {
            facturesDTO.add(factureToFactureDTOLazy(x));
        });
        return facturesDTO;
    }

        public static ListeArticleNonMvtDTOWrapper factureDTOToListeArticleNonMvtDTOWrapper(List<FactureDTO> factureDTOs) {
         ListeArticleNonMvtDTOWrapper result = new ListeArticleNonMvtDTOWrapper();
        List<ArticleNonMvtDTO> listeArticleNonMvtDTO = new ArrayList();
        List<MvtstoDTO> mvtstoDTOs = factureDTOs.stream().flatMap(x -> x.getMvtstoCollection().stream()).collect(Collectors.toList());
        mvtstoDTOs.forEach(art -> {
            ArticleNonMvtDTO articleNonMvtDTO = new ArticleNonMvtDTO();
            articleNonMvtDTO.setCategDepot(art.getCategDepot());
            articleNonMvtDTO.setCodart(art.getCodart());
            articleNonMvtDTO.setLastDateMvt(java.util.Date.from(art.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
            articleNonMvtDTO.setTypeMvt(TypeMvtEnum.Vente);
            articleNonMvtDTO.setNonMoved(Boolean.FALSE);
            listeArticleNonMvtDTO.add(articleNonMvtDTO);
        });
        result.setListeArticleNonMvtDTO(listeArticleNonMvtDTO);
        return result;
    }
}
