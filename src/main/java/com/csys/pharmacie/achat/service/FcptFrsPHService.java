package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import com.csys.pharmacie.achat.domain.BaseTvaAvoirFournisseur;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.FactureBonReception;
import com.csys.pharmacie.achat.domain.FactureDirecte;
import com.csys.pharmacie.achat.domain.FactureRetourPerime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csys.pharmacie.achat.domain.FcptfrsPH;
import com.csys.pharmacie.achat.dto.FcptfrsPHdto;
import com.csys.pharmacie.achat.dto.ListeFcptFrsPHDTOWrapper;
import com.csys.pharmacie.achat.factory.FcptfrsPHFactory;
import com.csys.pharmacie.achat.repository.FcptFrsPHRepository;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service("FcptFrsPHService")
public class FcptFrsPHService {

    private final Logger log = LoggerFactory.getLogger(FcptFrsPHService.class);
    @Autowired
    FcptFrsPHRepository fcptFrsPHRepository;

    @Transactional
    public boolean addFcptFrsOnReception(FactureBA reception) {
        FcptfrsPH fcptFrs = new FcptfrsPH();

        fcptFrs.setCodFrs(reception.getCodfrs());
        fcptFrs.setLibOpr("Facture BL N°" + reception.getNumaffiche());
        fcptFrs.setReste(reception.getMntbon());
        fcptFrs.setNumBon(reception.getNumbon());
        fcptFrs.setNumBonAff(reception.getNumaffiche());
        fcptFrs.setNumFac("");
        fcptFrs.setNumfacture("");
        fcptFrs.setSens("2");
        fcptFrs.setTypBon(TypeBonEnum.BA.toString());
        fcptFrs.setCodsup("0");
        fcptFrs.setDebit(BigDecimal.ZERO);
        fcptFrs.setCredit(reception.getMntbon());
        fcptFrs.setSolde(BigDecimal.ZERO);
        fcptFrs.setEtat("0");
        fcptFrs.setRetenu("0");
        fcptFrs.setMntOP(BigDecimal.ZERO);
        fcptFrs.setRetenuOP(BigDecimal.ZERO);
        fcptFrs.setDeclar(false);
        fcptFrs.setNumReg("");
        fcptFrs.setNumRegAff("");
        BigDecimal montantHT = reception.getBaseTvaReceptionList().stream().map(item -> item.getBaseTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantHT(montantHT);
        BigDecimal montantTVASansGratuite = reception.getBaseTvaReceptionList().stream().map(item -> item.getMontantTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        BigDecimal montantTvaGratuite = reception.getBaseTvaReceptionList().stream().map(item -> item.getMontantTvaGratuite()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantTVA(montantTVASansGratuite.add(montantTvaGratuite));
        fcptFrs.setDateOpr(reception.getDatbon());

        fcptFrsPHRepository.save(fcptFrs);

        return true;

    }

    @Transactional
    public FcptfrsPH updateFcptFrsOnReception(FcptfrsPH fcptFrs, FactureBA reception) {

        fcptFrs.setReste(reception.getMntbon());
        fcptFrs.setCredit(reception.getMntbon());
        BigDecimal montantHT = reception.getBaseTvaReceptionList().stream().map(item -> item.getBaseTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantHT(montantHT);
        BigDecimal montantTVASansGratuite = reception.getBaseTvaReceptionList().stream().map(item -> item.getMontantTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        BigDecimal montantTvaGratuite = reception.getBaseTvaReceptionList().stream().map(item -> item.getMontantTvaGratuite()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantTVA(montantTVASansGratuite.add(montantTvaGratuite));
        fcptFrsPHRepository.save(fcptFrs);
        return fcptFrs;
    }

    @Transactional
    public boolean addFcptFrsOnReturn(FactureBA retour) {
        FcptfrsPH fcptFrs = new FcptfrsPH();

        fcptFrs.setCodFrs(retour.getCodfrs());
        fcptFrs.setLibOpr("Retour N°" + retour.getNumaffiche());
        fcptFrs.setReste(retour.getMntbon());
        fcptFrs.setNumBon(retour.getNumbon());
        fcptFrs.setNumBonAff(retour.getNumaffiche());
        fcptFrs.setNumFac("");
        fcptFrs.setNumfacture("");
        fcptFrs.setSens("2");
        fcptFrs.setTypBon(TypeBonEnum.RT.toString());
        fcptFrs.setCodsup("0");
        fcptFrs.setDebit(BigDecimal.ZERO);
        fcptFrs.setCredit(retour.getMntbon());
        fcptFrs.setSolde(BigDecimal.ZERO);
        fcptFrs.setEtat("0");
        fcptFrs.setRetenu("0");
        fcptFrs.setMntOP(BigDecimal.ZERO);
        fcptFrs.setRetenuOP(BigDecimal.ZERO);
        fcptFrs.setDeclar(false);
        fcptFrs.setNumReg("");
        fcptFrs.setNumRegAff("");
        BigDecimal montantHT = retour.getBaseTvaReceptionList().stream().map(item -> item.getBaseTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantHT(montantHT);
        BigDecimal montantTVASansGratuite = retour.getBaseTvaReceptionList().stream().map(item -> item.getMontantTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        BigDecimal montantTvaGratuite = retour.getBaseTvaReceptionList().stream().map(item -> item.getMontantTvaGratuite()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantTVA(montantTVASansGratuite.add(montantTvaGratuite));
        fcptFrs.setDateOpr(retour.getDatbon());
        fcptFrsPHRepository.save(fcptFrs);

        return true;

    }

    @Transactional
    public boolean addFcptFrsOnFactureDirecte(FactureDirecte factureDirecte) {
        FcptfrsPH fcptFrs = new FcptfrsPH();

        fcptFrs.setCodFrs(factureDirecte.getCodeFournisseur());
        fcptFrs.setLibOpr("Facture directe N° " + factureDirecte.getNumaffiche());
        BigDecimal montantHT = factureDirecte.getBaseTvaFactureDirecteCollection().stream().map(item -> item.getBaseTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantHT(montantHT);
        BigDecimal montantTVA = factureDirecte.getBaseTvaFactureDirecteCollection().stream().map(item -> item.getMontantTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantTVA(montantTVA);
        fcptFrs.setReste(factureDirecte.getMontant());
        fcptFrs.setNumBon(factureDirecte.getNumbon());
        fcptFrs.setNumBonAff(factureDirecte.getNumaffiche());
        fcptFrs.setNumFac("");
        fcptFrs.setNumfacture("");
        fcptFrs.setSens("2");
        fcptFrs.setTypBon(TypeBonEnum.DIR.toString());
        fcptFrs.setCodsup("0");
        fcptFrs.setDebit(BigDecimal.ZERO);
        fcptFrs.setCredit(factureDirecte.getMontant());
        fcptFrs.setSolde(BigDecimal.ZERO);
        fcptFrs.setEtat("0");
        fcptFrs.setRetenu("0");
        fcptFrs.setMntOP(BigDecimal.ZERO);
        fcptFrs.setRetenuOP(BigDecimal.ZERO);
        fcptFrs.setDeclar(false);
        fcptFrs.setNumReg("");
        fcptFrs.setNumRegAff("");
        fcptFrs.setDateOpr(factureDirecte.getDatbon());
        fcptFrs.setCodeDevise(factureDirecte.getCodeDevise());
        fcptFrs.setTauxDevise(factureDirecte.getTauxDevise());
        fcptFrs.setMontantDevise(factureDirecte.getMontantDevise());
        fcptFrsPHRepository.save(fcptFrs);

        return true;

    }

    @Transactional
    public FcptfrsPH deleteFcptfrsByNumBonDao(String numBon, TypeBonEnum typeBon) {
        FcptfrsPH fcptFrs = fcptFrsPHRepository.findFirstByNumBon(numBon);
        Preconditions.checkBusinessLogique(
                (typeBon.equals(TypeBonEnum.FRP) && fcptFrs.getDebit().setScale(2, RoundingMode.HALF_UP).equals(fcptFrs.getReste()))
                || (!typeBon.equals(TypeBonEnum.FRP) && fcptFrs.getCredit().equals(fcptFrs.getReste())),
                "reception-is-paid");//TODOA
//        Preconditions.checkBusinessLogique(fcptFrs.getCredit().equals(fcptFrs.getReste()), "reception-is-paid");//TODOA
        fcptFrsPHRepository.delete(fcptFrs);
        return fcptFrs;
    }

    @Transactional
    public FcptfrsPH updateFcptFrsOnFactureDirecte(FcptfrsPH fcptFrs, FactureDirecte factureDirecte) {

        BigDecimal montantHT = factureDirecte.getBaseTvaFactureDirecteCollection().stream().map(item -> item.getBaseTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantHT(montantHT);
        BigDecimal montantTVA = factureDirecte.getBaseTvaFactureDirecteCollection().stream().map(item -> item.getMontantTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantTVA(montantTVA);
        fcptFrs.setReste(factureDirecte.getMontant());
        fcptFrs.setDebit(BigDecimal.ZERO);
        fcptFrs.setCredit(factureDirecte.getMontant());
        fcptFrsPHRepository.save(fcptFrs);
        return fcptFrs;
    }

    @Transactional
    public void modifierfichefournisseurPH(List<String> listebr) {
        for (String l : listebr) {
            FcptfrsPH frs = fcptFrsPHRepository.findFirstByNumBon(l);
            frs.setCodsup("1");
        }

    }

    @Transactional(readOnly = true)
    public FcptfrsPH findFirstByNumBon(String numBon) {
        FcptfrsPH frs = fcptFrsPHRepository.findFirstByNumBon(numBon);
        return frs;
    }

    @Transactional
    public boolean addFcptFrsOnFactureBonReception(FactureBonReception factureBonReception) {
        FcptfrsPH fcptFrs = new FcptfrsPH();
        fcptFrs.setCodFrs(factureBonReception.getCodeFournisseur());
        fcptFrs.setLibOpr("facture reception N° " + factureBonReception.getNumaffiche());
        BigDecimal montantHT = factureBonReception.getBaseTvaFactureBonReceptionCollection().stream().map(item -> item.getBaseTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantHT(montantHT);
//         BigDecimal montantTVASansGratuite = retour.getBaseTvaReceptionList().stream().map(item -> item.getMontantTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
//         BigDecimal montantTvaGratuite = retour.getBaseTvaReceptionList().stream().map(item -> item.getMontantTvaGratuite()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
//        fcptFrs.setMontantTVA(montantTVASansGratuite.add(montantTvaGratuite));
        BigDecimal montantTVASansGratuite = factureBonReception.getBaseTvaFactureBonReceptionCollection().stream().map(item -> item.getMontantTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        BigDecimal montantTvaGratuite = factureBonReception.getBaseTvaFactureBonReceptionCollection().stream().map(item -> item.getMontantTvaGratuite()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantTVA(montantTVASansGratuite.add(montantTvaGratuite));
        fcptFrs.setReste(factureBonReception.getMontant());
        fcptFrs.setNumBon(factureBonReception.getNumbon());
        fcptFrs.setNumBonAff(factureBonReception.getNumaffiche());
        fcptFrs.setNumFac("");
        fcptFrs.setNumfacture("");
        fcptFrs.setSens("2");
        fcptFrs.setTypBon(TypeBonEnum.FBR.toString());
        fcptFrs.setCodsup("0");
        fcptFrs.setDebit(BigDecimal.ZERO);
        fcptFrs.setCredit(factureBonReception.getMontant());
        fcptFrs.setSolde(BigDecimal.ZERO);
        fcptFrs.setEtat("0");
        fcptFrs.setRetenu("0");
        fcptFrs.setMntOP(BigDecimal.ZERO);
        fcptFrs.setRetenuOP(BigDecimal.ZERO);
        fcptFrs.setDeclar(false);
        fcptFrs.setNumReg("");
        fcptFrs.setNumRegAff("");
        fcptFrs.setDateOpr(factureBonReception.getDatbon());
        fcptFrs.setCodeDevise(factureBonReception.getCodeDevise());
        fcptFrs.setTauxDevise(factureBonReception.getTauxDevise());
        fcptFrs.setMontantDevise(factureBonReception.getMontantDevise());

        List<String> ListeNumBons = factureBonReception.getBonReceptionCollection().stream().map(ba -> ba.getNumbon()).collect(Collectors.toList());
        log.debug("les liste des ids des factures ba de cette facture bon receptions sont {} ", ListeNumBons);
        List<FcptfrsPH> listeFcptBA = fcptFrsPHRepository.findByNumBonIn(ListeNumBons);
        listeFcptBA.stream().forEach(x -> {
            x.setCodsup("1");
        });

        fcptFrsPHRepository.save(fcptFrs);
        return true;
    }

    @Transactional
    public void addFcptFrsOnAvoirFournisseur(AvoirFournisseur avoirFournisseur) {
        FcptfrsPH fcptFrs = new FcptfrsPH();

        fcptFrs.setCodFrs(avoirFournisseur.getCodeFournisseur());
        fcptFrs.setLibOpr("Retour N°" + avoirFournisseur.getNumaffiche());
        fcptFrs.setReste(avoirFournisseur.getMontantTTC());
        fcptFrs.setNumBon(avoirFournisseur.getNumbon());
        fcptFrs.setNumBonAff(avoirFournisseur.getNumaffiche());
        fcptFrs.setNumFac("");
        fcptFrs.setNumfacture("");
        fcptFrs.setSens("1");
        fcptFrs.setTypBon(TypeBonEnum.AF.toString());
        fcptFrs.setCodsup("0");
        fcptFrs.setDebit(avoirFournisseur.getMontantTTC());
        fcptFrs.setCredit(BigDecimal.ZERO);
        fcptFrs.setSolde(BigDecimal.ZERO);
        fcptFrs.setEtat("0");
        fcptFrs.setRetenu("0");
        fcptFrs.setMntOP(BigDecimal.ZERO);
        fcptFrs.setRetenuOP(BigDecimal.ZERO);
//        fcptFrs.setDeclar(false);
        fcptFrs.setNumRegAff("");

        BigDecimal montantHT = BigDecimal.ZERO;
        BigDecimal mntTVASansGratuite = BigDecimal.ZERO;
        BigDecimal mntTVAGratuite = BigDecimal.ZERO;

        for (BaseTvaAvoirFournisseur x : avoirFournisseur.getBaseTvaAvoirFournisseurList()) {
            montantHT = montantHT.add(x.getBaseTva());
            mntTVASansGratuite = mntTVASansGratuite.add(x.getMontantTva());
            mntTVAGratuite = mntTVAGratuite.add(x.getMntTvaGrtauite());
        }
        BigDecimal mntTVA = mntTVAGratuite.add(mntTVASansGratuite);

        fcptFrs.setMontantHT(montantHT);
        fcptFrs.setMontantTVA(mntTVA);
        fcptFrs.setDateOpr(avoirFournisseur.getDatbon());
        fcptFrsPHRepository.save(fcptFrs);

    }

    @Transactional
    public boolean addFcptFrsOnFactureRetourPerime(FactureRetourPerime factureRetourPerime) {
        FcptfrsPH fcptFrs = new FcptfrsPH();
        fcptFrs.setCodFrs(factureRetourPerime.getCodeFournisseur());
        fcptFrs.setLibOpr("facture retour perime N° " + factureRetourPerime.getNumaffiche());

        BigDecimal montantHT = factureRetourPerime.getBaseTvaFactureRetourPerime().stream()
                .map(item -> item.getBaseTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantHT(montantHT);

        BigDecimal montantTTC = factureRetourPerime.getBaseTvaFactureRetourPerime().stream()
                .map(item -> item.getMontantTva()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        fcptFrs.setMontantTVA(montantTTC);

        fcptFrs.setDebit(factureRetourPerime.getMontantTTC());
        fcptFrs.setReste(factureRetourPerime.getMontantTTC());

        fcptFrs.setNumBon(factureRetourPerime.getNumbon());
        fcptFrs.setNumBonAff(factureRetourPerime.getNumaffiche());
        fcptFrs.setNumFac("");
        fcptFrs.setNumfacture("");
        fcptFrs.setSens("1");
        fcptFrs.setTypBon(TypeBonEnum.FRP.toString());
        fcptFrs.setCodsup("0");
        fcptFrs.setCredit(BigDecimal.ZERO);
        fcptFrs.setSolde(BigDecimal.ZERO);
        fcptFrs.setEtat("0");
        fcptFrs.setRetenu("0");
        fcptFrs.setMntOP(BigDecimal.ZERO);
        fcptFrs.setRetenuOP(BigDecimal.ZERO);
        fcptFrs.setDeclar(false);
        fcptFrs.setNumReg("");
        fcptFrs.setNumRegAff("");

        Calendar cal = new GregorianCalendar();
        cal.setTime(Date.from(factureRetourPerime.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        cal.set(Calendar.HOUR_OF_DAY, factureRetourPerime.getHeuresys().getHour());
        cal.set(Calendar.MINUTE, factureRetourPerime.getHeuresys().getMinute());
        cal.set(Calendar.SECOND, factureRetourPerime.getHeuresys().getSecond());

        fcptFrs.setDateOpr(cal.getTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());

        List<String> ListeNumBons = factureRetourPerime.getRetourPerime().stream().map(rp -> rp.getNumbon()).collect(Collectors.toList());
        log.debug("les liste des nums bons des retours perimes de cette facture retour perime sont {} ", ListeNumBons);
        List<FcptfrsPH> listeFcptBA = fcptFrsPHRepository.findByNumBonIn(ListeNumBons);
        listeFcptBA.stream().forEach(x -> {
            x.setCodsup("1");
        });

        fcptFrsPHRepository.save(fcptFrs);
        return true;
    }

    @Transactional
    public FcptfrsPHdto save(FcptfrsPHdto fcptfrsPHdto) {
        log.debug("Request to save FcptfrsPHdto: {}", fcptfrsPHdto);
        FcptfrsPH fcptfrsPH = FcptfrsPHFactory.FcptfrsPHDTOTOFcptfrsPH(fcptfrsPHdto);
        FcptfrsPH result = fcptFrsPHRepository.save(fcptfrsPH);
        FcptfrsPHdto resultDTO = FcptfrsPHFactory.FcptfrsPHTOFcptfrsPHDTO(result);
        return resultDTO;

    }

    @Transactional
    public FcptfrsPHdto saveOnAvanceFrournisseur(FcptfrsPHdto fcptfrsPHdto) {
        log.debug("Request to save FcptfrsPHdto: {}", fcptfrsPHdto);
        FcptfrsPH fcptfrsPH = FcptfrsPHFactory.FcptfrsPHDTOTOFcptfrsPH(fcptfrsPHdto);
        fcptfrsPH.setSens("2");
        fcptfrsPH.setNumBon("AVANCE");
        fcptfrsPH.setNumBonAff("AVANCE");
        fcptfrsPH.setTypBon("RF");
        fcptfrsPH.setNumFac("");
        fcptfrsPH.setNumfacture("");
        fcptfrsPH.setEtat("0");
        fcptfrsPH.setRetenu("0");
        fcptfrsPH.setCodsup("0");
        fcptfrsPH.setMntOP(BigDecimal.ZERO);
        fcptfrsPH.setRetenuOP(BigDecimal.ZERO);
        fcptfrsPH.setDeclar(false);
        fcptfrsPH.setNumReg("");
        fcptfrsPH.setNumRegAff("");
        fcptfrsPH.setSolde(BigDecimal.ZERO);
        fcptfrsPH.setDebit(fcptfrsPHdto.getMntttc());
        fcptfrsPH.setReste(fcptfrsPHdto.getMntttc());
        fcptfrsPH.setCredit(BigDecimal.ZERO);
        fcptfrsPH.setMontantHT(BigDecimal.ZERO);
        fcptfrsPH.setMontantTVA(BigDecimal.ZERO);
        FcptfrsPH result = fcptFrsPHRepository.save(fcptfrsPH);
        FcptfrsPHdto resultDTO = FcptfrsPHFactory.FcptfrsPHTOFcptfrsPHDTO(result);
        return resultDTO;

    }

    @Transactional
    public ListeFcptFrsPHDTOWrapper saveListeListeFcptFrsPHOnAvanceFournisseur(ListeFcptFrsPHDTOWrapper liste) {
        log.debug("Request to save Liste FcptFrs on Avance Fournisseur {}", liste);
        ListeFcptFrsPHDTOWrapper result = new ListeFcptFrsPHDTOWrapper();
        Collection<FcptfrsPHdto> listeResulted = new ArrayList();
        liste.getListeFcptfrsDTO().forEach(fcptFrs -> {
            FcptfrsPHdto resultedPR = saveOnAvanceFrournisseur(fcptFrs);
            listeResulted.add(resultedPR);

        });
        result.setListeFcptfrsDTO(listeResulted);
        return result;

    }

    @Transactional
    public Boolean deleteByNumOprIn(Long[] numOprs) {
        fcptFrsPHRepository.deleteByNumOprIn(numOprs);
        return Boolean.TRUE;
    }
}
