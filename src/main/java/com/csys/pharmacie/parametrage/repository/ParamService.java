package com.csys.pharmacie.parametrage.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csys.util.RestPreconditions;
import com.csys.pharmacie.parametrage.entity.CompteurPharmacie;
import com.csys.pharmacie.parametrage.entity.CompteurPharmaciePK;
import com.csys.pharmacie.parametrage.entity.OptionVersionPharmacie;
import com.csys.pharmacie.parametrage.entity.Paramph;

import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.util.Preconditions;
import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("ParamService")
public class ParamService {

    private final Logger log = LoggerFactory.getLogger(ParamService.class);
 
    @Autowired
    private ParamRepository paramRepository;

    @Autowired
    private CompteurPharmacieRepository compteurPharmacieRepository;


    @Autowired
    private OptionVersionPharmacieRepository optionVersionPharmacieRepository;

//    @Autowired
//    private ModRegRepository modRegRepository;

    
//    @Autowired
//    private CliniqueRepository cliniqueRepository;

    public List<Paramph> findall() {
        return paramRepository.findAll();
    }

    public Paramph findparambycode(String code) {
        Paramph param = paramRepository.findFirstByCode(code);
        RestPreconditions.checkFound(param);
        return param;
    }

    public Boolean updateparam(Paramph param) {

        paramRepository.save(param);

        return true;
    }
    @Transactional
    public String getcompteur(CategorieDepotEnum codeDepot, TypeBonEnum typeBon) {
        CompteurPharmacie c = findcompteurbycode(codeDepot, typeBon);
        String numBon = codeDepot.equals(CategorieDepotEnum.IMMO) ? "FA"+c.getP1() + c.getP2() : c.getCompteurPharmaciePK().getCodeDepot().categ() + c.getP1() + c.getP2();
        return numBon;

    }



    public CompteurPharmacie findcompteurbycode(CategorieDepotEnum codeDepot, TypeBonEnum typeBon) {
        CompteurPharmaciePK id = new CompteurPharmaciePK();
        id.setCodeDepot(codeDepot);
        id.setTypeBon(typeBon);
        CompteurPharmacie c = compteurPharmacieRepository.findFirstByCompteurPharmaciePK(id);
        RestPreconditions.checkFound(c);
        return c;

    }

    @Transactional
    public boolean updatecompteurpharmacie(CategorieDepotEnum codeDepot, TypeBonEnum typeBon, String valeur) {
        CompteurPharmacie p = findcompteurbycode(codeDepot, typeBon);
        p.setP2(valeur);
        compteurPharmacieRepository.save(p);
        return true;
    }

    public OptionVersionPharmacie findOptionVersionPharmacieByID(String ID) {
        OptionVersionPharmacie opt = optionVersionPharmacieRepository.findFirstById(ID);
        RestPreconditions.checkFound(opt);
        return opt;
    }

    public String findValeurOptionVersionPharmacieByID(String ID) {
        OptionVersionPharmacie opt = findOptionVersionPharmacieByID(ID);
        RestPreconditions.checkFound(opt);
        return opt.getValeur();
    }

    public List<Integer> getPreemptionParams() {
        List<Integer> result = new ArrayList<Integer>();
        OptionVersionPharmacie opt = findOptionVersionPharmacieByID("DatPerDefautBA");
        if (opt.getValeur().equalsIgnoreCase("O")) {
            result.add(Integer.parseInt(findValeurOptionVersionPharmacieByID("Interv_Date_Per_Mois")));
            result.add(Integer.parseInt(findValeurOptionVersionPharmacieByID("Insterv_DatPer_CTR")));
        } else {
            result.add(0);
        }
        return result;

    }

//    public List<ModReg> findAllModeReg() {
//        return modRegRepository.findAll();
//    }

    public String findSuffixRptBC() {
        String SuffixeBonCommande = "";
        OptionVersionPharmacie opt = optionVersionPharmacieRepository.findFirstById("EtatSpCA");
        if (opt.getValeur().equalsIgnoreCase("O")) {
            SuffixeBonCommande = optionVersionPharmacieRepository.findFirstById("SuffixeBonCommande").getValeur();
        }
        return SuffixeBonCommande;

    }

//    public List<Tva> findAllTva() {
//        return tvaRepository.findAll();
//    }
//
//    public Tva findFirstByCodTVA(Integer codtva) {
//        return tvaRepository.findFirstByCode(codtva);
//    }
//
//    public Tva findFirstByTauTVA(BigDecimal tautva) {
//        return tvaRepository.findFirstByValeur(tautva);
//    }

    @Transactional
    public void updateCompteurPharmacie(CategorieDepotEnum codeDepot, TypeBonEnum typeBon) {
        CompteurPharmacie c = findcompteurbycode(codeDepot, typeBon);
        String suffixe = c.getP2();
        c.setP2(Helper.IncrementString(suffixe, suffixe.length()));
        compteurPharmacieRepository.save(c);
    }

    @Transactional
    public void updateCompteurPharmacie(CategorieDepotEnum codeDepot, TypeBonEnum typeBon, String suffixe) {
        CompteurPharmacie c = findcompteurbycode(codeDepot, typeBon);
        c.setP2(Helper.IncrementString(suffixe.substring((c.getCompteurPharmaciePK().getCodeDepot().categ() + c.getP1()).length() + 1), c.getP2().length()));
        compteurPharmacieRepository.save(c);
    }

    @Transactional
    public void savePharmacie(CompteurPharmacie c) {

        compteurPharmacieRepository.save(c);
    }

//    public Clinique findCliniqueConfig() {
//        return cliniqueRepository.findFirst1By();
//    }

    public List<OptionVersionPharmacie> findConfigTransfRecup() {
        List<String> s = new ArrayList<>();
        s.add("Autor_Qte_Supp_Recup");
        s.add("Cond_Doss_Avoir");
        s.add("QteDu_BT_Av_Ap");
        s.add("Inclur_DateADD_Au_BT");
        s.add("Lie_DatInv_Du_BT");
        s.add("IntervDateBT");
        return optionVersionPharmacieRepository.findById(s);
    }

    public List<OptionVersionPharmacie> findConfigPrmArticle() {
        List<String> s = new ArrayList<>();
        s.add("Panier_Depot_Frs");
        s.add("CodArt_Auto");
        s.add("Tva_VntDefaut");
        s.add("Modif_TvaVnt");
        s.add("Oblig_MinMax_art");
        s.add("Marge_Automatique");

        return optionVersionPharmacieRepository.findById(s);
    }

    public OffsetDateTime getServerDateTime() {
        return paramRepository.getServerDateTime();
    }

    public List<Paramph> getParamsByCodeIn(List<String> listCodes) {
        return paramRepository.getParamsByCodeIn(listCodes);
    }


    @Transactional
    public boolean checkMdpByCode(String code, String mdp) {
        Paramph p = findparambycode(code);

        Preconditions.checkBusinessLogique(mdp.equalsIgnoreCase(p.getValeur()), "inventaire.err-password");

        return true;
    }
}
