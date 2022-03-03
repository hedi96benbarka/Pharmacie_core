//package com.csys.pharmacie.achat.factory;
//
//import com.csys.pharmacie.achat.dto.MvtstoAFDTO;
//import com.csys.pharmacie.achat.dto.AvoirFrsDTO;
//import com.csys.pharmacie.achat.repository.*;
//import com.csys.pharmacie.achat.service.MvtStoBAService;
//import com.csys.pharmacie.achat.service.FactureBAService;
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.Format;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.csys.util.RestPreconditions;
//import com.csys.pharmacie.achat.dto.ArticleDTO;
//import com.csys.pharmacie.achat.dto.DepotDTO;
//import com.csys.pharmacie.achat.domain.FactureAA;
//import com.csys.pharmacie.achat.domain.Fournissph;
//import com.csys.pharmacie.achat.domain.MvtStoAA;
//import com.csys.pharmacie.achat.domain.MvtStoAAPK;
//import com.csys.pharmacie.achat.domain.MvtStoBA;
//import com.csys.pharmacie.client.service.ParamAchatServiceClient;
//import com.csys.pharmacie.article.entity.Stockph;
//import com.csys.pharmacie.parametrage.entity.CompteurPharmacie;
//import com.csys.pharmacie.parametrage.entity.Tva;
//import com.csys.pharmacie.parametrage.repository.ParamService;
//import com.csys.pharmacie.parametrage.repository.TvaRepository;
//import com.csys.pharmacie.stock.domain.Depot;
//import com.csys.pharmacie.stock.service.StockService;
//import com.google.common.base.Preconditions;
//import com.csys.pharmacie.helper.CategorieDepotEnum;
//import com.csys.pharmacie.helper.Helper;
//import org.springframework.context.annotation.Lazy;
//
//@Component
//public class AvoirFrsFactory {
//
//    @Autowired
//    private FournissPHRepository fournissPHRepository;
//
//    @Autowired
//    private ParamAchatServiceClient paramAchatServiceClient;
//
//    @Autowired
//    private ParamService paramService;
//
//    @Autowired
//    private TvaRepository tvaRepository;
//
//    @Autowired
//    private StockService stockService;
////    @Autowired
////    private FactureBAService factureBAService;
//    @Autowired
//    MvtStoBAService mvtStoBAService;
//
//    public static final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//    public static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//    public static final Format formatter1 = new SimpleDateFormat("dd/MM/yyyy");
//    public static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

//    public FactureAA AvoirFrsFromDto(AvoirFrsDTO dto) throws ParseException {
//        FactureAA avoirFrs = new FactureAA();
//        CompteurPharmacie c = paramService.findcompteurbycode(dto.getCategDepot(),dto.getTypeBon());
//        String prefix = c.getP1();
//        String suffixe = c.getP2();
//        String numaffiche = prefix + suffixe;
//        String numbon = "AA" + numaffiche;
//        avoirFrs.setNumaffiche(numaffiche);
//        avoirFrs.setNumbon(numbon);
////        mapperAvoirFrs(dto, avoirFrs);
//        return avoirFrs;
//    }

//    private void mapperAvoirFrs(AvoirFrsDTO dto, FactureAA avoirFrs) throws ParseException {
////        avoirFrs.setBase0tva00(dto.getBase0Tva00());
////        avoirFrs.setBase1tva06(dto.getBase1Tva06());
////        avoirFrs.setBase1tva10(dto.getBase1Tva10());
////        avoirFrs.setBase1tva18(dto.getBase1Tva18());
//        avoirFrs.setMemop(dto.getMemop());
//        avoirFrs.setCodvend(dto.getCodvend());
////        avoirFrs.setSmfodec(dto.getSmfodec());
////        avoirFrs.setTva10(dto.getTva10());
////        avoirFrs.setTva18(dto.getTva18());
////        avoirFrs.setTva6(dto.getTva6());
////        avoirFrs.setMntbon(dto.getMntbon());
////        avoirFrs.setFodcli(dto.isFodcli());
//        avoirFrs.setNumpiece(dto.getNumAfficheFacRecep());
////       TODO avoirFrs.setDatepiece(factureBAService.findFactureBABynumBon("FL" + dto.getNumAfficheFacRecep()).getDatbon());
//        avoirFrs.setValrem(dto.getValrem());
////        avoirFrs.setStup(dto.isStup());
//        avoirFrs.setTaufodec(paramService.findValeur().getTauxFodec());
//        ///Frs
//        Fournissph fournisseur = fournissPHRepository.findFirstByCodfrs(dto.getCodfrs());
//        RestPreconditions.checkFound(fournisseur);
//        avoirFrs.setCodfrs(fournisseur.getCodfrs());
//        avoirFrs.setAdr(fournisseur.getAdrliv());
//        avoirFrs.setFodcli(fournisseur.getFodec());
//        avoirFrs.setCodtva(fournisseur.getCodtva());
//        avoirFrs.setAdr(fournisseur.getAdrliv());
//        avoirFrs.setRaisoc(fournisseur.getRaiSoc());
//        avoirFrs.setTimbre(fournisseur.getTimbre());
//
//        //depot
//        DepotDTO depot = stockService.findFirstByCoddep(dto.getCodDep());
//        RestPreconditions.checkFound(fournisseur);
//        avoirFrs.setCoddep(depot.getCode());
//
//        //champs statique
//        avoirFrs.setBase1tva29(BigDecimal.ZERO);
//        avoirFrs.setTypbon("AA");
//        avoirFrs.setRemise(BigDecimal.ZERO);
//        avoirFrs.setCodAnnul("");
//        avoirFrs.setSatisf("NST");
//        avoirFrs.setImprimer(false);
//        avoirFrs.setAutomatique(false);
//        avoirFrs.setEtatbon(false);
//        avoirFrs.setMntRemise2(BigDecimal.ZERO);
//        avoirFrs.setTauxRemise2(BigDecimal.ZERO);
//        avoirFrs.setNumVir("");
//        avoirFrs.setMaintenance("");
//        avoirFrs.setBase2tva06(BigDecimal.ZERO);
//        avoirFrs.setBase2tva10(BigDecimal.ZERO);
//        avoirFrs.setBase2tva18(BigDecimal.ZERO);
//        avoirFrs.setBase2tva29(BigDecimal.ZERO);
//        avoirFrs.setCodAnes("");
//        avoirFrs.setTimbre(false);
//        avoirFrs.setMajoration(BigDecimal.ZERO);
//        avoirFrs.setTypexon(Short.parseShort("1"));
//        avoirFrs.setAssujetti(true);
//        avoirFrs.setCodrem("0");
//        avoirFrs.setValtimbre(BigDecimal.ZERO);
//        avoirFrs.setCodPan("");
//        avoirFrs.setCodPans("");
//        avoirFrs.setContr("");
//        avoirFrs.setEmis("");
//        avoirFrs.setFrais(BigDecimal.ZERO);
//        avoirFrs.setMp("");
//        avoirFrs.setNature("");
//        avoirFrs.setOrdon("");
//        avoirFrs.setProrata(BigDecimal.ZERO);
//        avoirFrs.setTva29(BigDecimal.ZERO);
//        avoirFrs.setTypCB("");
//
//        avoirFrs.setCodrep("");
//        avoirFrs.setNumdoss("");
//        avoirFrs.setNumCha("");
//        avoirFrs.setReffrs("");
//        avoirFrs.setNatadm("");
//        avoirFrs.setMedecin("");
//        avoirFrs.setDiagnost("");
//        avoirFrs.setDeptr("");
//        avoirFrs.setDesdepa("");
//        avoirFrs.setDesdepd("");
//
//        Date d2 = new Date();
//        String heuresy = sdf.format(d2);
//        Date heuresys = df.parse("30/12/1899 " + heuresy);
//        Date datesys = formatter.parse(formatter1.format(d2));
//
////        avoirFrs.setDatesys(datesys);
////        avoirFrs.setHeuresys(heuresys);
////        avoirFrs.setDatbon(datesys);
//
//        List<MvtstoAFDTO> listdto = dto.getDetails();
//
//        List<MvtStoAA> details = new ArrayList<MvtStoAA>();
//        List<MvtStoBA> lmvtstoBA = new ArrayList<MvtStoBA>();
//        String numordre = "001";
//
//        for (MvtstoAFDTO art : listdto) {
//
//            Preconditions.checkArgument(stockService.checkArtExistInDep(art.getRefArt(), dto.getCodDep(), art.getQuantite()), "Quantité dans le stock est insuffisante");
//
//            if (art.getQuantite().floatValue() > 0) {
//                Tva t1 = tvaRepository.findFirstByValeur(art.getTauTVA());
//                RestPreconditions.checkFound(t1);
//                String ordre = "AA" + numordre;
//                MvtStoAA mvtsto = new MvtStoAA();
//                MvtStoAAPK pk = new MvtStoAAPK();
//                pk.setCodart(art.getRefArt());
//                pk.setNumbon(avoirFrs.getNumbon());
//                pk.setNumordre(ordre);
//                mvtsto.setMvtStoAAPK(pk);
//
//                String d = formatter1.format(new Date());
//                String lot = d.substring(8, 10) + Helper.SerialiserNumero(d.substring(3, 5), 2) + Helper.SerialiserNumero(d.substring(0, 2), 2) + avoirFrs.getNumbon();
//
//                mvtsto.setDesart(art.getDesart());
//                mvtsto.setLot(lot);
//                mvtsto.setTypbon("AA");
//
//                ArticleDTO articleDTO = paramAchatServiceClient.findArticlebyCategorieDepotAndCodeArticle(CategorieDepotEnum.PH, art.getRefArt());
//
//                BigDecimal prixachat = articleDTO.getPrixAchat();
//
//                mvtsto.setDatbon(formatter.parse(d));
//                mvtsto.setQuantite(art.getQuantite());
//                mvtsto.setQtecom(art.getQuantite());
//                mvtsto.setQteben(art.getQuantite());
//                mvtsto.setAncienQte(art.getQuantite());
//                mvtsto.setPriuni(art.getPriuni());
////                mvtsto.setRemise(art.getRemise());
//                mvtsto.setMontht(art.getMontht());
//                mvtsto.setTautva(art.getTauTVA()); 
//                mvtsto.setPriach(prixachat);
//                mvtsto.setPrixben(prixachat);
//                mvtsto.setCodvend(dto.getCodvend());
//
//                mvtsto.setNbrpce(Short.valueOf("1"));
//                mvtsto.setNumaffiche(avoirFrs.getNumaffiche());
//                mvtsto.setCodtva(art.getCodTVA());
//                mvtsto.setTypmvt("S");
//                mvtsto.setUtilis("");
//                mvtsto.setEtatDep(Character.MIN_VALUE);
//                mvtsto.setHeureSysteme(d2);
//                mvtsto.setModifier(Character.MIN_VALUE);
//                mvtsto.setNumVir("");
//                mvtsto.setNumCha("");
//                mvtsto.setNumdoss("");
//
//                mvtsto.setPriAchApresProrata(BigDecimal.ZERO);
//                mvtsto.setGest("");
//                mvtsto.setMarge(BigDecimal.ZERO);//TODO
//                mvtsto.setMajoration(BigDecimal.ZERO);
//                mvtsto.setCoddep(dto.getCodDep());
//                mvtsto.setFactureAA(avoirFrs);
//                mvtsto.setNumBL(art.getNumBL());
//                numordre = Helper.IncrementString(numordre, 3);
//
//                details.add(mvtsto);
//
//            }
//
//        }
//        avoirFrs.setDetailFactureAACollection(details);
//
//    }

//}
