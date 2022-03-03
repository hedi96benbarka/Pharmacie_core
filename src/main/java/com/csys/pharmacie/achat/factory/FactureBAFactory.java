package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.dto.FactureBADTO;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.MvtStoBAPK;
import com.csys.pharmacie.achat.dto.BaseTvaReceptionDTO;
import com.csys.pharmacie.achat.dto.BonRetourDTO;
import com.csys.pharmacie.achat.dto.ReceptionEditionDTO;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import org.springframework.security.core.context.SecurityContextHolder;

//@Component
public class FactureBAFactory {

//    @Autowired
//    ParamAchatServiceClient paramAchatServiceClient;
//    @Autowired
//    private DetailReceptionFactory detailReceptionFactory;
//    @Autowired
//    private DetailRetourFactory detailRetourFactory;
//    @Autowired
//    MvtstoBAFactory mvtstoBAFactory;
//    @Autowired
//    BaseTVAFactory baseTVAFactory;
//    @Autowired
//    ReceivingService receivingService;
//    @Autowired
//    DemandeServiceClient demandeServiceClient;
    /**
     * ******************** edition****************************
     */
    public static ReceptionEditionDTO factureBAToReceptionEditionDTO(FactureBA factureBA) {
        
        ReceptionEditionDTO bonRecepDTO = new ReceptionEditionDTO();
        bonRecepDTO.setCodvend(factureBA.getCodvend());
        bonRecepDTO.setNumbon(factureBA.getNumbon());
        bonRecepDTO.setDatbon(Date.from(factureBA.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        bonRecepDTO.setNumaffiche(factureBA.getNumaffiche());
        bonRecepDTO.setMntBon(factureBA.getMntbon());
        bonRecepDTO.setDateFrs(Date.from(factureBA.getDatRefFrs().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        bonRecepDTO.setCodeFournisseu(factureBA.getCodfrs());
        bonRecepDTO.setRefFrs(factureBA.getRefFrs());
        bonRecepDTO.setTypbon(factureBA.getTypbon().type());
        if (factureBA.getCodAnnul() == null) {
            bonRecepDTO.setAnnule(Boolean.FALSE);
        } else {
            bonRecepDTO.setAnnule(Boolean.TRUE);
        }
        if (factureBA.getAutomatique() == null) {
            bonRecepDTO.setAutomatique(Boolean.FALSE);
        } else {
            bonRecepDTO.setAutomatique(factureBA.getAutomatique());
        }
        return bonRecepDTO;
    }

    /**
     * ******************** ENTITY TO DTO****************************
     */
    private static void factureBAToFactureBADTOLazy(FactureBADTO dto, FactureBA entity) {
        dto.setNumaffiche(entity.getNumaffiche());
        dto.setNumbon(entity.getNumbon());
        dto.setDatbon(entity.getDatbon());
        dto.setCodvend(entity.getCodvend());
        dto.setMntBon(entity.getMntbon());
        dto.setDateFrs(entity.getDatRefFrs());
        dto.setRefFrs(entity.getRefFrs());
        dto.setCoddep(entity.getCoddep());
        dto.setMemop(entity.getMemop());
        dto.setCategDepot(entity.getCategDepot());
        dto.setTypbon(entity.getTypbon().type());
        //   dto.setMaxDelaiPaiement(entity.getMaxDelaiPaiement());
        dto.setCodeFournisseu(entity.getCodfrs());
        dto.setFournisseurExonere(entity.getFournisseurExonere());
        if (entity.getDatRefFrs() != null) {
            dto.setDateFournisseurEdition(Date.from(entity.getDatRefFrs().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (entity.getDatbon() != null) {
            dto.setDatebonEdition(Date.from(entity.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        }
        
    }
    
    public static BonRecepDTO receptionTemporaireToBonRecepDTOEager(FactureBA factureBA) {
        if (factureBA == null) {
            return null;
        }
        BonRecepDTO bonRecepDTO = new BonRecepDTO();
        factureBAToFactureBADTOLazy(bonRecepDTO, factureBA);
        
        bonRecepDTO.setBasesTVA(BaseTvaReceptionFactory.baseTvaReceptionsToBaseTvaReception(factureBA.getBaseTvaReceptionList()));
//        dto.setDesignationDepot(paramAchatServiceClient.findDepotByCode(entity.getCoddep()).getDesdep());
//        dto.setFournisseur(paramAchatServiceClient.findFournisseurByCode(entity.getCodfrs()));
        bonRecepDTO.setCodeFournisseu(factureBA.getCodfrs());
        bonRecepDTO.setFournisseurExonere(factureBA.getFournisseurExonere());
        bonRecepDTO.setUserAnnule(factureBA.getCodAnnul());
        bonRecepDTO.setDateAnnule(factureBA.getDatAnnul());
        if (factureBA.getReceiving() != null) {
            bonRecepDTO.setReceivingCode(factureBA.getReceiving().getCode());
            bonRecepDTO.setReceivingNumBon(factureBA.getReceiving().getNumbon());
            bonRecepDTO.setReceivingNumaffiche(factureBA.getReceiving().getNumaffiche());
            bonRecepDTO.setReceivingMemo(factureBA.getReceiving().getMemo());
        }
        //    bonRecepDTO.setAttachedFilesIDs(factureBA.getPiecesJointes().stream().map(elt -> UUID.fromString(elt.getCodeDocument())).collect(toList()));
        bonRecepDTO.setBasesTVA(BaseTvaReceptionFactory.baseTvaReceptionsToBaseTvaReception(factureBA.getBaseTvaReceptionList()));
        List<MvtstoBADTO> detailsReception = new ArrayList();
        factureBA.getDetailFactureBACollection().forEach(mvtstoBA -> {
            MvtstoBADTO detailRecep = new MvtstoBADTO();
            DetailReceptionFactory.toDTO(mvtstoBA, detailRecep);
            detailRecep.setQteReturned(mvtstoBA.getQuantite().subtract(mvtstoBA.getQtecom()));
            detailsReception.add(detailRecep);
        });
        bonRecepDTO.setDetails((List) detailsReception);
        //List<Integer> listCodesCA = factureBA.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());
        //bonRecepDTO.setPurchaseOrders(demandeServiceClient.findListCommandeAchat(listCodesCA, LocaleContextHolder.getLocale().getLanguage()));
        return bonRecepDTO;
    }
    
    public static BonRecepDTO factureBAToBonRecepDTO(FactureBA factureBA) {
        
        BonRecepDTO bonRecepDTO = new BonRecepDTO();
        factureBAToFactureBADTOLazy(bonRecepDTO, factureBA);
        bonRecepDTO.setUserAnnule(factureBA.getCodAnnul());
        bonRecepDTO.setDateAnnule(factureBA.getDatAnnul());
//        bonRecepDTO.setCodeFournisseu(factureBA.getCodfrs());
//        bonRecepDTO.setFournisseurExonere(factureBA.getFournisseurExonere());
        if (factureBA.getReceiving() != null) {
            bonRecepDTO.setReceivingCode(factureBA.getReceiving().getCode());
            bonRecepDTO.setReceivingNumBon(factureBA.getReceiving().getNumbon());
            bonRecepDTO.setReceivingNumaffiche(factureBA.getReceiving().getNumaffiche());
            bonRecepDTO.setReceivingMemo(factureBA.getReceiving().getMemo());
        }
        
        if (factureBA.getFactureBonReception() != null) {
            bonRecepDTO.setNumAfficheFactureBonReception(factureBA.getFactureBonReception().getNumaffiche());
        }
        bonRecepDTO.setAutomatique(factureBA.getAutomatique());
        
        List<BaseTvaReceptionDTO> BaseTvas = BaseTvaReceptionFactory.baseTvaReceptionsToBaseTvaReception(factureBA.getBaseTvaReceptionList());
        bonRecepDTO.setListeBaseTvaReceptionDTO(BaseTvas);
        bonRecepDTO.setBasesTVA(BaseTvas);
        bonRecepDTO.setAttachedFilesIDs(factureBA.getPiecesJointes().stream().map(elt -> UUID.fromString(elt.getCodeDocument())).collect(toList()));
        bonRecepDTO.setCodeSite(factureBA.getCodeSite());
//        List<MvtstoBADTO> mvtstosDTO = new ArrayList();
//        factureBA.getDetailFactureBACollection().forEach(item-> {
//        
//        MvtstoBADTO   mvt = mvtstoBAFactory.toDTOforupdate(item);
//        mvtstosDTO.add(mvt);
//        }
//        );
//        bonRecepDTO.setDetails(mvtstosDTO);
        return bonRecepDTO;
    }
    
    public static BonRetourDTO factureBAToBonRetourDTOLazy(FactureBA factureBA) {
        
        BonRetourDTO bonRetourDTO = new BonRetourDTO();
        factureBAToFactureBADTOLazy(bonRetourDTO, factureBA);
        bonRetourDTO.setReceptionID(factureBA.getNumpiece());
        
        return bonRetourDTO;
    }
    
    public static BonRetourDTO factureBAToBonRetourDTOEager(FactureBA factureBA) {
        if (factureBA == null) {
            return null;
        }
        BonRetourDTO bonRetourDTO = new BonRetourDTO();
        factureBAToFactureBADTOLazy(bonRetourDTO, factureBA);
        bonRetourDTO.setReceptionID(factureBA.getNumpiece());
        bonRetourDTO.setNumbonRetourTransfertCompanyBranch(factureBA.getNumbonRetourCompanyBranch());
        bonRetourDTO.setBasesTVA(BaseTvaReceptionFactory.baseTvaReceptionsToBaseTvaReception(factureBA.getBaseTvaReceptionList()));
//        bonRetourDTO.setBasesTVA(BaseTvaReceptionFactory.baseTvaReceptionsToBaseTvaReception(factureBA.getBaseTvaReceptionList()));

        List<MvtstoBADTO> detailsRetours = new ArrayList();
        factureBA.getDetailFactureBACollection().forEach(mvtstoBA -> {
            MvtstoBADTO detailRet = new MvtstoBADTO();
            detailsRetours.add(DetailRetourFactory.toDTO(mvtstoBA, detailRet));
        });
        bonRetourDTO.setDetails((List) detailsRetours);
        
        return bonRetourDTO;
    }
//    public List<FactureBADTO> factureBAToFactureBADTOs(List<FactureBA> factureBAs) {
//        List<FactureBADTO> factureBADTOs = new ArrayList();
//        factureBAs.forEach(x -> {
//            factureBADTOs.add(factureBAToFactureBADTO(x));
//        });
//        return factureBADTOs;
//    }

    /**
     * ******************** DTO TO ENTITY****************************
     */
    public static FactureBA factureBADTOTOFactureBA(FactureBADTO dto) {
        FactureBA factureBA = new FactureBA();
        
        factureBA.setCategDepot(dto.getCategDepot());
        factureBA.setRefFrs(dto.getRefFrs());
        factureBA.setFournisseurExonere(dto.getFournisseurExonere());
        factureBA.setDatRefFrs(dto.getDateFrs());
        factureBA.setMntbon(dto.getMntBon());
        
        factureBA.setMemop(dto.getMemop());
        factureBA.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());
        factureBA.setTypbon(dto.getTypeBon());
        
        factureBA.setListbc(dto.getPurchaseOrdersCodes());
        factureBA.setValrem(dto.getValrem());
//        factureBA.setAutomatique(dto.getAutomatique());

        return factureBA;
    }

    //** this function takes a dto and returns a map containing FactureBA entity and a TraceModifBL Entity
    public static FactureBA FactureBADTOTOFactureBAForUpdate(BonRecepDTO dto, FactureBA f) {
//        baseBonFactory.toEntity(f, dto);
//        f.setCategDepot(dto.getCategDepot());
        f.setFournisseurExonere(dto.getFournisseurExonere());
        f.setRefFrs(dto.getRefFrs());
        f.setDatRefFrs(dto.getDateFrs());
        f.setMntbon(dto.getMntBon());
        f.setMemop(dto.getMemop());
//        f.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());

        f.setValrem(dto.getValrem());
        f.setFournisseurExonere(dto.getFournisseurExonere());
        // documents 

        return f;
    }

    /**
     * ********************generate dto to UPDATE
     * price****************************
     */
    public static BonRecepDTO dtoToBonRecepDTOforUpdatePrices(BonRecepDTO bonReceptionDTO, String codfrs) {
        
        BonRecepDTO resultedBonRecepDTO = new BonRecepDTO();
        resultedBonRecepDTO.setCodeFournisseu(codfrs);
        resultedBonRecepDTO.setTypbon(bonReceptionDTO.getTypbon());
        
        List<MvtstoBADTO> results = bonReceptionDTO.getDetails()
                .stream()
                .filter(elt -> elt.getPriuni().compareTo(BigDecimal.ZERO) > 0)
                .map(mvtsto -> {
                    MvtstoBADTO dto = new MvtstoBADTO();
                    dto.setPriuni(mvtsto.getPriuni());
                    dto.setRefArt(mvtsto.getRefArt());
                    dto.setSellingPrice(mvtsto.getSellingPrice());
                    dto.setCodeArtFrs(mvtsto.getCodeArtFrs());
                    return dto;
                }).collect(Collectors.toList());
        
        List<MvtstoBADTO> mvtstosDTOs = new ArrayList(results);
        resultedBonRecepDTO.setDetails(mvtstosDTOs);
        return resultedBonRecepDTO;
    }
    
    public static BonRecepDTO factureBAToBonRecepDTOForRevertPrices(String codeFournisseur, List<MvtStoBA> listeToUpdate) {
        BonRecepDTO bonRecepDTO = new BonRecepDTO();
        bonRecepDTO.setCodeFournisseu(codeFournisseur);
        
        List<MvtstoBADTO> mvtstosDTOs
                = listeToUpdate
                        .stream()
                        .map(mvtsto -> {
                            MvtstoBADTO dto = new MvtstoBADTO();
                            dto.setPriuni(mvtsto.getPriuni());
                            dto.setRefArt(mvtsto.getCodart());
//                            log.debug("priuni du mvtsto article {} est {}", mvtsto.getCodart(), mvtsto.getPriuni());
//                            log.debug("priuni du cod article {} est {}", dto.getRefArt(), dto.getPriuni());
                            return dto;
                        })
                        .collect(Collectors.toList());

//        List<MvtstoBADTO> mvtstos = new ArrayList();
        Collection<MvtstoBADTO> mvt = mvtstosDTOs
                .stream()
                .filter(elt -> elt.getPriuni().compareTo(BigDecimal.ZERO) > 0)
                .collect(groupingBy(MvtstoBADTO::getRefArt,
                        Collectors.reducing(new MvtstoBADTO(BigDecimal.ZERO), (a, b) -> {

//                            log.debug("priuni du cod article b {} est {}", b.getRefArt(), b.getPriuni());
                            return b;
                        }))).values();

//        log.debug("details are {}", mvt);
        bonRecepDTO.setDetails(new ArrayList(mvt));
        
        return bonRecepDTO;
    }

    public static FactureBA factureBADTOTOFactureBANotLzy(BonRecepDTO bonRecepDTO, String numBon) {
        FactureBA bonRecep = new FactureBA();
        bonRecep.setNumbon(numBon);
        bonRecep.setRefFrs(bonRecepDTO.getRefFrs());
        bonRecep.setCodfrs(bonRecepDTO.getCodeFournisseu());
        bonRecep.setDatRefFrs(bonRecepDTO.getDateFrs());
        bonRecep.setCategDepot(bonRecepDTO.getCategDepot());
        bonRecep.setCodeSite(bonRecepDTO.getCodeSite());
        bonRecep.setMemop(bonRecepDTO.getMemop());
        bonRecep.setTypbon(bonRecepDTO.getTypeBon());
        bonRecep.setCoddep(bonRecepDTO.getCoddep());
//        f.setSatisf("NST");
        bonRecep.setAutomatique(bonRecepDTO.getAutomatique());
        bonRecep.setValrem(bonRecepDTO.getValrem());
        bonRecep.setFournisseurExonere(bonRecepDTO.getFournisseurExonere());
        bonRecep.setCodeSite(bonRecepDTO.getCodeSite());
        
        List<MvtStoBA> details = new ArrayList<>();
        for (MvtstoBADTO mvtstoBaDto : bonRecepDTO.getDetails()) {
            if (mvtstoBaDto.getQuantite().compareTo(BigDecimal.ZERO) != 0) {
                MvtStoBA mvtstoBA = new MvtStoBA();
                MvtStoBAPK pk = new MvtStoBAPK();
                pk.setCodart(Integer.valueOf(mvtstoBaDto.getCodeArtFrs()));
                pk.setNumbon(numBon);
                pk.setNumordre(mvtstoBaDto.getNumOrdre());
                mvtstoBA.setPk(pk);
                
                mvtstoBA.setTypbon(mvtstoBaDto.getTypbon());
                mvtstoBA.setQuantite(mvtstoBaDto.getQuantite());

                //Prix
                mvtstoBA.setPriuni(mvtstoBaDto.getPriuni());
                mvtstoBA.setRemise(mvtstoBaDto.getRemise());
                mvtstoBA.setMontht(mvtstoBaDto.getMontht());
                
                mvtstoBA.setCodtva(mvtstoBaDto.getCodTVA());
                mvtstoBA.setTautva(mvtstoBaDto.getTauTVA());
                mvtstoBA.setOldTauTva(mvtstoBaDto.getOldTauTva());
                mvtstoBA.setOldCodTva(mvtstoBaDto.getOldCodTva());
                mvtstoBA.setCategDepot(bonRecep.getCategDepot());
                mvtstoBA.setCoddep(bonRecep.getCoddep());
                mvtstoBA.setDesart(mvtstoBaDto.getDesart());
                mvtstoBA.setDesArtSec(mvtstoBaDto.getDesartSec());
                mvtstoBA.setCodeSaisi(mvtstoBaDto.getCodeSaisi());
                mvtstoBA.setQtecom(mvtstoBaDto.getQuantite());
                mvtstoBA.setLotInter(mvtstoBaDto.getLotInter());
                mvtstoBA.setIsPrixReference(mvtstoBaDto.getIsPrixRef());
                mvtstoBA.setDatPer(mvtstoBaDto.getDatPer());
                mvtstoBA.setCodeUnite(mvtstoBaDto.getCodeUnite());
                mvtstoBA.setBaseTva(BigDecimal.ZERO);
                mvtstoBA.setFactureBA(bonRecep);
                details.add(mvtstoBA);
            }
        }
        bonRecep.setDetailFactureBACollection(details);
        
        return bonRecep;
    }
    public static BonRecepDTO factureBAToBonRecepDTONotLazy(FactureBA factureBA) {
        
        BonRecepDTO bonRecepDTO = new BonRecepDTO();
        factureBAToFactureBADTOLazy(bonRecepDTO, factureBA);
        bonRecepDTO.setUserAnnule(factureBA.getCodAnnul());
        bonRecepDTO.setDateAnnule(factureBA.getDatAnnul());
//        bonRecepDTO.setDateFrs(factureBA.getDatRefFrs());
        bonRecepDTO.setCodeFournisseu(factureBA.getCodfrs());
        bonRecepDTO.setFournisseurExonere(factureBA.getFournisseurExonere());
        if (factureBA.getReceiving() != null) {
            bonRecepDTO.setReceivingCode(factureBA.getReceiving().getCode());
            bonRecepDTO.setReceivingNumBon(factureBA.getReceiving().getNumbon());
            bonRecepDTO.setReceivingNumaffiche(factureBA.getReceiving().getNumaffiche());
            bonRecepDTO.setReceivingMemo(factureBA.getReceiving().getMemo());
        }
        
        if (factureBA.getFactureBonReception() != null) {
            bonRecepDTO.setNumAfficheFactureBonReception(factureBA.getFactureBonReception().getNumaffiche());
        }
        bonRecepDTO.setAutomatique(factureBA.getAutomatique());
        
        List<BaseTvaReceptionDTO> BaseTvas = BaseTvaReceptionFactory.baseTvaReceptionsToBaseTvaReception(factureBA.getBaseTvaReceptionList());
        bonRecepDTO.setListeBaseTvaReceptionDTO(BaseTvas);
        bonRecepDTO.setBasesTVA(BaseTvas);
        if (factureBA.getPiecesJointes() != null) {
            bonRecepDTO.setAttachedFilesIDs(factureBA.getPiecesJointes().stream().map(elt -> UUID.fromString(elt.getCodeDocument())).collect(toList()));
        }
        List<MvtstoBADTO> mvtstosDTO = new ArrayList();
        // *** factory pour les details factureba (mvtstoba) (quittance avec on shelf) ***
        factureBA.getDetailFactureBACollection().forEach(mvtstoBA -> {
            MvtstoBADTO mvtstoBADTO = new MvtstoBADTO();
            
            mvtstoBADTO.setCodeArtFrs(mvtstoBA.getPk().getCodart().toString());
            mvtstoBADTO.setNumbon(factureBA.getNumbon());
            mvtstoBADTO.setNumOrdre(mvtstoBA.getPk().getNumordre());
            mvtstoBADTO.setTypbon(mvtstoBA.getTypbon());
            mvtstoBADTO.setQuantite(mvtstoBA.getQuantite());
            //Prix
            mvtstoBADTO.setPriuni(mvtstoBA.getPriuni());
            mvtstoBADTO.setRemise(mvtstoBA.getRemise());
            mvtstoBADTO.setMontht(mvtstoBA.getMontht());
            mvtstoBADTO.setCodTVA(mvtstoBA.getCodtva());
            mvtstoBADTO.setTauTVA(mvtstoBA.getTautva());
            mvtstoBADTO.setOldTauTva(mvtstoBA.getOldTauTva());
            mvtstoBADTO.setOldCodTva(mvtstoBA.getOldCodTva());
            mvtstoBADTO.setCategDepot(factureBA.getCategDepot());
            mvtstoBADTO.setCodeDepartementEmplacement(factureBA.getCoddep());
            mvtstoBADTO.setDesart(mvtstoBA.getDesart());
            mvtstoBADTO.setDesartSec(mvtstoBA.getDesArtSec());
            mvtstoBADTO.setCodeSaisi(mvtstoBA.getCodeSaisi());
            mvtstoBADTO.setQteCom(mvtstoBA.getQuantite());
            mvtstoBADTO.setLotInter(mvtstoBA.getLotInter());
            mvtstoBADTO.setIsPrixRef(mvtstoBA.getIsPrixReference());
            mvtstoBADTO.setDatPer(mvtstoBA.getDatPer());
            mvtstoBADTO.setCodeUnite(mvtstoBA.getCodeUnite());
            mvtstoBADTO.setBaseTva(BigDecimal.ZERO);
            mvtstosDTO.add(mvtstoBADTO);
        }
        );
        bonRecepDTO.setDetails(mvtstosDTO);
        return bonRecepDTO;
    }
//    public FactureBA ReceptionDTOToReceptionSuiteValidationTemp(BonRecepDTO dto) {
//        FactureBA reception = new FactureBA();
//
//       
//        reception.setCategDepot(dto.getCategDepot());
//        reception.setRefFrs(dto.getRefFrs());
//        reception.setFournisseurExonere(dto.getFournisseurExonere());
//        reception.setDatRefFrs(dto.getDateFrs());
//        reception.setMntbon(dto.getMntBon());
//        // reception.setNumbon(dto.getNumbon());
//        reception.setMemop(dto.getMemop());
//        reception.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());
//        reception.setTypbon(TypeBonEnum.BA);
//       
//        reception.setListbc(dto.getPurchaseOrdersCodes());
//        reception.setValrem(dto.getValrem());
//        reception.setAutomatique(dto.getAutomatique());
//
//        //reception.setBaseTvaReceptionList(BaseTvaReceptionFactory.baseTvaReceptionsToBaseTvaReceptionDto(dto.getListeBaseTvaReceptionDTO()));
//        return reception;
//    }
//    @Deprecated
//public FactureBA bonRetourFromDTO(BonRetourDTO dto) throws ParseException {
////        HashMap<String, Object> map = new HashMap<>();
//        FactureBA f = new FactureBA();
//        baseBonFactory.toEntity(f, dto);
//        f.setRefFrs(dto.getRefFrs());
//        f.setDatRefFrs(dto.getDateFrs());
//        f.setMntbon(dto.getMntBon());
//        f.setNumpiece(dto.getReceptionID());
//        f.setNumbon(dto.getNumbon());
//        f.setMemop(dto.getMemop());
//        f.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());
//        f.setTypbon(dto.getTypeBon());
//        f.setCategDepot(dto.getCategDepot());
//        f.setValrem(dto.getValrem());
//        // depot
//        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(dto.getCoddep());
//        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + dto.getCoddep() + "] introuvable");
////        Preconditions.checkBusinessLogique(!depotd.getOuvrenouvex(), "Le dépôt " + depotd.getDesdep() + " est  fermé");
//        f.setCoddep(depotd.getCode());
//
//// fournisseur
//        FournisseurDTO fourniss = paramAchatServiceClient.findFournisseurByCode(dto.getFournisseur().getCode());
//        Preconditions.checkBusinessLogique(!fourniss.getDesignation().equals("fournisseur.deleted"), "Fournisseur avec code : " + dto.getFournisseur().getCode() + " est introuvable");
//        f.setCodfrs(fourniss.getCode());
//
//        //details
//        List<MvtStoBA> details = new ArrayList<>();
//        String numordre1 = "0001";
//        for (MvtstoBADTO mvtstoDTO : dto.getDetails()) {
//
//            MvtStoBA mvtsto = new MvtStoBA();
//            // update qtecom dans demande achat
//            String ordre = "RT" + numordre1;
//
//            MvtStoBAPK pk = new MvtStoBAPK();
//            pk.setCodart(mvtstoDTO.getRefArt());
//            pk.setNumbon(dto.getNumbon());
//            pk.setNumordre(ordre);
//            mvtsto.setPk(pk);
//
//            mvtsto.setTypbon("RT");
//            mvtsto.setDatPer(mvtstoDTO.getDatPer());
//            mvtsto.setQuantite(mvtstoDTO.getQuantite());
////            mvtsto.setMemoart(dto.getMemop());
//            mvtsto.setPriuni(mvtstoDTO.getPriuni());
//            mvtsto.setRemise(mvtstoDTO.getRemise());
//            mvtsto.setMontht(mvtstoDTO.getMontht());
//            mvtsto.setTautva(mvtstoDTO.getTauTVA());
//            mvtsto.setCodtva(mvtstoDTO.getCodTVA());
//            mvtsto.setCategDepot(dto.getCategDepot());
//            mvtsto.setPrixVente(mvtstoDTO.getSellingPrice());
//            mvtsto.setBaseTva(mvtstoDTO.getBaseTva());
//            if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
//
//                mvtsto.setDesart(mvtstoDTO.getDesartSec());
//                mvtsto.setDesArtSec(mvtstoDTO.getDesart());
//
//            } else {
//                mvtsto.setDesart(mvtstoDTO.getDesart());
//                mvtsto.setDesArtSec(mvtstoDTO.getDesartSec());
//            }
//            mvtsto.setCodeSaisi(mvtstoDTO.getCodeSaisi());
//            mvtsto.setQtecom(mvtstoDTO.getQuantite());
//            mvtsto.setLotInter(mvtstoDTO.getLotInter());
//            mvtsto.setIsPrixReference(mvtstoDTO.getIsPrixRef());
//            mvtsto.setCoddep(dto.getCoddep());
//            mvtsto.setCodeUnite(mvtstoDTO.getCodeUnite());
//
//            mvtsto.setFactureBA(f);
//            details.add(mvtsto);
//            numordre1 = Helper.IncrementString(numordre1, 4);
//        }
//        f.setDetailFactureBACollection(details);
////        List<BaseTvaReception> listeBasesTVA = new ArrayList();
////        dto.getBasesTVA().forEach(baseTVADTO -> {
////            BaseTvaReception baseTVARecep = new BaseTvaReception();
////            baseTVAFactory.toEntity(baseTVARecep, baseTVADTO);
////            baseTVARecep.setFactureBA(f);
////            listeBasesTVA.add(baseTVARecep);
////        });
////        f.setBaseTvaReceptionList(listeBasesTVA);
//        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
//        f.calcul(listTvas);
//        return f;
//
//    }
//      public FactureBA factureBADTOTOFactureBA(BonRecepDTO dto) {
////        HashMap<String, Object> map = new HashMap<>();
//        FactureBA factureBA = new FactureBA();
//        baseBonFactory.toEntity(factureBA, dto);
//        factureBA.setRefFrs(dto.getRefFrs());
//        factureBA.setFournisseurExonere(dto.getFournisseurExonere());
//        factureBA.setDatRefFrs(dto.getDateFrs());
//        factureBA.setMntbon(dto.getMntBon());
////        factureBA.setNumbon(dto.getNumbon());
//        factureBA.setMemop(dto.getMemop());
//        factureBA.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());
//        factureBA.setTypbon(dto.getTypeBon());
//        factureBA.setCategDepot(dto.getCategDepot());
//        factureBA.setListbc(dto.getPurchaseOrdersCodes());
//        factureBA.setValrem(dto.getValrem());
//        factureBA.setAutomatique(dto.getAutomatique());
//        return factureBA;
//    }
////        List<MvtStoBA> details = new ArrayList<>();
////        List<Depsto> depstos = new ArrayList<>();
////        List<Propose> props = new ArrayList<>();
////        String numordre1 = "0001";
////        Date date = new Date(3000, 01, 01);
////        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
////        ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper = new ListeImmobilisationDTOWrapper();
////        List<ImmobilisationDTO> Immobilisations = new ArrayList();
////        for (MvtstoBADTO mvtstoDTO : dto.getDetails()) {
////            if (mvtstoDTO.getQuantite().compareTo(BigDecimal.ZERO) != 0) {
////                Optional<ArticleDTO> filtredArt = articles.stream().filter(article -> article.getCode().equals(mvtstoDTO.getRefArt())).findFirst();
////                Preconditions.checkBusinessLogique(filtredArt.isPresent(), "reception.add.missing-article", mvtstoDTO.getRefArt().toString());
////                log.debug(" le priuni du mvtstoBA {} est {}et sa baseTva est{} :", mvtsto.getCodeSaisi(),mvtsto.getPriuni().toString(),mvtsto.getBaseTva() );
////                Preconditions.checkBusinessLogique((mvtsto.getPriuni().compareTo(BigDecimal.ZERO) == 1 && mvtsto.getBaseTva().compareTo(BigDecimal.ZERO) == 0) || (mvtsto.getPriuni().compareTo(BigDecimal.ZERO) == 0), "reception.add.error-baseTva", mvtsto.getCodeSaisi());
////MvtStoBADTOTOMvtstoBA
////                MvtStoBA mvtsto = new MvtStoBA();
////                // update qtecom dans demande achat
////                String ordre = "BA" + numordre1;
////
////                MvtStoBAPK pk = new MvtStoBAPK();
////                pk.setCodart(mvtstoDTO.getRefArt());
////                pk.setNumbon(dto.getNumbon());
////                pk.setNumordre(ordre);
////                mvtsto.setPk(pk);
////
////                mvtsto.setTypbon("BA");
////                mvtsto.setQuantite(mvtstoDTO.getQuantite());
////                mvtsto.setLotInter(mvtstoDTO.getLotInter());
////                mvtsto.setPriuni(mvtstoDTO.getPriuni());
////                mvtsto.setRemise(mvtstoDTO.getRemise());
////                mvtsto.setMontht(mvtstoDTO.getMontht());
////                mvtsto.setTautva(mvtstoDTO.getTauTVA());
////                mvtsto.setCodtva(mvtstoDTO.getCodTVA());
////                mvtsto.setCodeSaisi(mvtstoDTO.getCodeSaisi());
////                mvtsto.setQtecom(mvtstoDTO.getQuantite());
////                mvtsto.setLotInter(mvtstoDTO.getLotInter());
////                mvtsto.setIsPrixReference(mvtstoDTO.getIsPrixRef());
////                mvtsto.setDatPer(mvtstoDTO.getDatPer());
////                if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
////                    mvtsto.setDesart(mvtstoDTO.getDesartSec());
////                    mvtsto.setDesArtSec(mvtstoDTO.getDesart());
////                } else {
////                    mvtsto.setDesart(mvtstoDTO.getDesart());
////                    mvtsto.setDesArtSec(mvtstoDTO.getDesartSec());
////                }
////                mvtsto.setCategDepot(dto.getCategDepot());
////                mvtsto.setCoddep(dto.getCoddep());
////                mvtsto.setAncienPrixAchat(filtredArt.get().getPrixAchat());
////                log.debug("le fournisseur est exonere :{}",f.getFournisseurExonere());
////                if (!factureBA.getFournisseurExonere()) {
////                    mvtsto.setOldCodTva(5);
////                    mvtsto.setOldTauTva(BigDecimal.ZERO);
////                } else {
////                    mvtsto.setOldCodTva(mvtstoDTO.getOldCodTva());
////                    mvtsto.setOldTauTva(mvtstoDTO.getOldTauTva());
////                }
////                mvtsto.setBaseTva(mvtstoDTO.getBaseTva());
////                if (dto.getCategDepot().equals(PH)) {
////                    Preconditions.checkBusinessLogique(mvtstoDTO.getSellingPrice() != null, "reception.add.missing-selling-price", mvtstoDTO.getCodeSaisi());
////                    mvtsto.setPrixVente(mvtstoDTO.getSellingPrice());
////                }
////
////                if (dto.getCategDepot().equals(IMMO)) {
////                    mvtsto.setIsCapitalize(mvtstoDTO.getIsCapitalize());
////                } else {
////                    mvtsto.setIsCapitalize(Boolean.FALSE);
////                }
////                mvtsto.setCodeSaisi(mvtstoDTO.getCodeSaisi());
////                mvtsto.setQtecom(mvtstoDTO.getQuantite());
////                mvtsto.setLotInter(mvtstoDTO.getLotInter());
////                mvtsto.setIsPrixReference(mvtstoDTO.getIsPrixRef());
////                mvtsto.setDatPer(mvtstoDTO.getDatPer());
////                mvtsto.setCodeUnite(filtredArt.get().getCodeUnite());
////                mvtsto.setFactureBA(factureBA);
////                details.add(mvtsto);
////                Depsto depst = new Depsto();
////
////                depst.setCodart(mvtstoDTO.getRefArt());
//////                depst.setUnite(filtredArt.get().getCodeUnite());
////                depst.setLotInter(mvtstoDTO.getLotInter());
////                depst.setDatPer(mvtstoDTO.getDatPer());
////                depst.setQte(mvtstoDTO.getQuantite());
////                depst.setIsCapitalize(mvtstoDTO.getIsCapitalize());
////
////                depst.setPu(mvtstoDTO.getPriuni().multiply((BigDecimal.valueOf(100).subtract(mvtstoDTO.getRemise())).divide(BigDecimal.valueOf(100))));
////                depst.setCoddep(factureBA.getCoddep());
////                depst.setCategDepot(factureBA.getCategDepot());
////                depst.setNumBon(factureBA.getNumbon());
////                depst.setCodeTva(mvtstoDTO.getCodTVA());
////                depst.setTauxTva(mvtstoDTO.getTauTVA());
////                depst.setNumBonOrigin(factureBA.getNumbon());
////                if (dto.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
////                    mvtsto.setDatPer(localDate);
////                    depst.setDatPer(localDate);
////                    if (mvtstoDTO.getLotInter() == null) {
////                        mvtsto.setLotInter("-");
////                        depst.setLotInter("-");
////                    }
////                    mvtsto.setCodeEmplacement(mvtstoDTO.getCodeEmplacement());
////                    depst.setCodeEmplacement(mvtstoDTO.getCodeEmplacement());
////
////                    ArticleIMMODTO articleIMMO = (ArticleIMMODTO) filtredArt.get();
////                    if (articleIMMO.getGenererImmobilisation()) {
////                        Immobilisations.add(genererImmoDTO(mvtsto, articleIMMO, dto.getFournisseur().getCode(), emplacements, LocalDate.now(), factureBA.getNumbon().substring(2)));
////                    }
////                }
////                if (mvtstoDTO.getPriuni().compareTo(BigDecimal.ZERO) == 0) {
////                    depst.setMemo("FREE" + factureBA.getNumbon());
////                    if (mvtstoDTO.getBaseTva().compareTo(BigDecimal.ZERO) > 0) {
////
////                        BigDecimal divisor = BigDecimal.valueOf(100).add(mvtstoDTO.getTauTVA()).divide(BigDecimal.valueOf(100));//100+tva/100
////                        depst.setPu(mvtstoDTO.getBaseTva()
////                                .multiply(mvtstoDTO.getTauTVA())
////                                .divide(new BigDecimal(100)).//baseTva   *0.14-> mntTva gratuite
////                                divide(divisor, 5, RoundingMode.CEILING));
////                    }
////
//////                }
////                depstos.add(depst);
//////                numordre1 = Helper.IncrementString(numordre1, 4);
////            }
////        }
//    ////////////////IMMO/////////
////        factureBA.setDetailFactureBACollection(details);
//    // log.debug("Immobilisations sont {}",Immobilisations);
////        if (dto.getCategDepot().equals(CategorieDepotEnum.IMMO) && !Immobilisations.isEmpty()) {
////            listeImmobilisationDTOWrapper.setImmobilisation(Immobilisations);
////            ListeImmobilisationDTOWrapper listeImmo = immobilisationService.saveImmo(listeImmobilisationDTOWrapper);
////            Preconditions.checkBusinessLogique(listeImmo != null, "error-saving-immo");
////        }
////
////        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
////        factureBA.calcul(listTvas);
////
////        map.put("depstos", depstos);
////        map.put("Listpropose", props);
////        map.put("bonRecep", factureBA);
////
////        depstoRepository.save(depstos);
////        depstoRepository.flush();
////        codesEmplacements.forEach(codeEmp -> {
////
////            Set<MvtstoBADTO> listDetailsMatchingEmplacement = dto.getDetails().stream().filter(x -> codeEmp.equals(x.getCodeEmplacement())).collect(Collectors.toSet());
////            EmplacementDTO matchingEmplacement = emplacements.stream().filter(x -> x.getCode().equals(codeEmp))
////                    .findFirst()
////                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-emplacement", new Throwable(codeEmp.toString())));
////
////            FacturePRDTO facturePrDTO = new FacturePRDTO();
////            facturePrDTO.setCategDepot(CategorieDepotEnum.IMMO);
////
////            facturePrDTO.setMotifID(1);
////            List<MvtStoPRDTO> detailsPrelevements = new ArrayList();
////            facturePrDTO.setCoddepotSrc(dto.getCoddep());
////            facturePrDTO.setCoddepartDesti(matchingEmplacement.getCodeDepartement().getCode());
////            listDetailsMatchingEmplacement.forEach(mvtstoBa -> {
////                MvtStoPRDTO mvtStoPRDTO = new MvtStoPRDTO(mvtstoBa.getRefArt(), mvtstoBa.getLotInter(), mvtstoBa.getCodeUnite(), CategorieDepotEnum.IMMO, mvtstoBa.getQuantite());
////                mvtStoPRDTO.setCodeEmplacement(mvtstoBa.getCodeEmplacement());
////                mvtStoPRDTO.setDatPer(localDate);
////                detailsPrelevements.add(mvtStoPRDTO);
////            });
////            facturePrDTO.setDetails(detailsPrelevements);
////            facturePRService.save(facturePrDTO);
////        });
////        return factureBA;
////    }
    //    public static FactureBADTO factureBAToBonEditionDTOEager(FactureBA factureBA) {
//
//        FactureBADTO bonRecepEditionDTO = new FactureBADTO();
//        factureBAToFactureBADTOLazy(bonRecepEditionDTO, factureBA);
//
//        bonRecepEditionDTO.setBasesTVA(BaseTvaReceptionFactory.baseTvaReceptionsToBaseTvaReception(factureBA.getBaseTvaReceptionList()));
//        List<DetailEditionDTO> detailsReception = new ArrayList();
//        List<Integer> codeUnites = new ArrayList<>();
//        Set<Integer> setEmplacement = new HashSet();
//
//        factureBA.getDetailFactureBACollection().forEach(mvt -> {
//            codeUnites.add(mvt.getCodeUnite());
//            if (mvt.getCodeEmplacement() != null) {
//                setEmplacement.add(mvt.getCodeEmplacement());
//            }
//        });
//        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
//        List<EmplacementDTO> emplacements = factureBA.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(setEmplacement) : new ArrayList();
//
//        factureBA.getDetailFactureBACollection().forEach(mvtstoBA -> {
//            DetailEditionDTO detailRecep = DetailReceptionFactory.toEditionDTO(mvtstoBA);
//            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoBA.getCodeUnite())).findFirst().orElse(null);
//            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
//            detailRecep.setDesignationunite(unite.getDesignation());
//
//            if (factureBA.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
//                EmplacementDTO emplacement = emplacements.stream()
//                        .filter(item -> item.getCode().equals(mvtstoBA.getCodeEmplacement()))
//                        .findFirst().orElse(null);
//                if (mvtstoBA.getCodeEmplacement() != null) {
//                    detailRecep.setDesignationEmplacement(emplacement.getDesignation());
//                    detailRecep.setCodeEmplacement(emplacement.getCode());
//                }
//            }
//
//            detailRecep.setQteReturned(mvtstoBA.getQuantite().subtract(mvtstoBA.getQtecom()));
//            detailsReception.add(detailRecep);
//        });
//        bonRecepEditionDTO.setDetails((List) detailsReception);
//        if (factureBA.getTypbon() == TypeBonEnum.BE) {
//            List<Integer> listCodesCA = factureBA.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());
//            bonRecepEditionDTO.setPurchaseOrders(demandeServiceClient.findListCommandeAchat(listCodesCA, LocaleContextHolder.getLocale().getLanguage()));
//        }
//        return bonRecepEditionDTO;
//    }
}
