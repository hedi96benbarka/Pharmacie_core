package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.Receiving;
import com.csys.pharmacie.achat.domain.ReceivingCommande;
import com.csys.pharmacie.achat.domain.ReceivingCommandePK;
import com.csys.pharmacie.achat.domain.ReceivingDetails;
import com.csys.pharmacie.achat.domain.ReceivingDetailsPK;
import com.csys.pharmacie.achat.dto.ReceivingDTO;
import com.csys.pharmacie.achat.dto.ReceivingDetailsDTO;
import com.csys.pharmacie.achat.dto.ReceivingEditionDTO;
import static com.csys.pharmacie.achat.factory.ReceivingDetailsFactory.receivingdetailsDTOToReceivingDetails;
import static com.csys.pharmacie.achat.factory.ReceivingDetailsFactory.receivingdetailsToReceivingDetailsDTOs;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReceivingFactory {

//    @Autowired
//    @Lazy
//    FactureBAFactory factureBAFactory;
//    private final Logger log = LoggerFactory.getLogger(ReceivingService.class);
//    public static ReceivingDTO receivingToReceivingDTO(Receiving receiving) {
//        ReceivingDTO receivingDTO = new ReceivingDTO();
//        receivingDTO.setNumaffiche(receiving.getNumaffiche());
//
//        receivingDTO.setCode(receiving.getCode());
//        receivingDTO.setNumbon(receiving.getNumbon());
//        receivingDTO.setDateCreate(receiving.getDateCreate());
//        receivingDTO.setUserCreate(receiving.getUserCreate());
//        receivingDTO.setDateValidate(receiving.getDateValidate());
//        receivingDTO.setUserValidate(receiving.getUserValidate());
//        receivingDTO.setDateAnnule(receiving.getDateAnnule());
//        receivingDTO.setUserAnnule(receiving.getUserAnnule());
//        receivingDTO.setCategDepot(receiving.getCategDepot());
//        receivingDTO.setFournisseur(receiving.getFournisseur());
//        receivingDTO.setImprime(receiving.getImprime());
//        receivingDTO.setDateImprime(receiving.getDateImprime());
//        receivingDTO.setUserImprime(receiving.getUserImprime());
//        receivingDTO.setMemo(receiving.getMemo());
//        receivingDTO.setDateMemo(receiving.getDateMemo());
//        receivingDTO.setUserMemo(receiving.getUserMemo());
//        List<Integer> codesCommandeAchat = receiving.getReceivingCommandeList().stream().map(x -> x.getReceivingCommandePK().getCommandeParamAchat()).collect(Collectors.toList());
//
//        receivingDTO.setCodesCommandeAchat(codesCommandeAchat);
////        List<ReceivingDetailsDTO> receivingDetailsDTO = receivingdetailsToReceivingDetailsDTOs(receiving.getReceivingDetailsList());
////        if (receivingDTO.getBonRecep() != null) {
////            receivingDetailsDTO.forEach(x -> {
////                BigDecimal quantite = receivingDTO.getBonRecep().getDetails().stream().filter(
////                        y -> y.getRefArt().equals(x.getCodart())
////                ).map(z -> z.getQuantite())
////                        .reduce(BigDecimal.ZERO, BigDecimal::add);
////                x.setQuantiteReceptionne(quantite);
////            });
////        } else {
////            receivingDetailsDTO.forEach(x -> {
////                x.setQuantiteReceptionne(BigDecimal.ZERO);
////            });
////        }
////        receivingDTO.setReceivingDetailsList(receivingDetailsDTO);
////        if (receivingDTO.getBonRecep() != null) {
////            receivingDTO.setNumAfficheBonRecep(receivingDTO.getBonRecep().getNumbon());
////        }
//        return receivingDTO;
//    }
    public static Receiving receivingDTOToReceiving(ReceivingDTO receivingDTO) {
        Receiving receiving = new Receiving();
        receiving.setNumbon(receivingDTO.getNumbon());
        receiving.setDateCreate(receivingDTO.getDateCreate());
        receiving.setUserCreate(receivingDTO.getUserCreate());
        receiving.setDateValidate(receivingDTO.getDateValidate());
        receiving.setUserValidate(receivingDTO.getUserValidate());
        receiving.setUserValidateReceiving(receivingDTO.getUserValidateReceiving());
        receiving.setDateValidateReceiving(receivingDTO.getDateValidateReceiving());
        receiving.setDateAnnule(receivingDTO.getDateAnnule());
        receiving.setUserAnnule(receivingDTO.getUserAnnule());
        receiving.setFournisseur(receivingDTO.getFournisseurDTO().getCode());
        receiving.setCategDepot(receivingDTO.getCategDepot());
        receiving.setImprime(Boolean.FALSE);
        List<ReceivingCommande> receivingCommandeList = new ArrayList<>();
        receivingDTO.getCommandeAchatList().forEach(x -> {
            ReceivingCommande receivingCommande = new ReceivingCommande();
            ReceivingCommandePK receivingCommandePK = new ReceivingCommandePK();
            receivingCommandePK.setCommandeParamAchat(x.getCode());
            receivingCommandePK.setReciveing(receiving.getCode());
            receivingCommande.setReceivingCommandePK(receivingCommandePK);
            receivingCommande.setReceiving(receiving);
            receivingCommandeList.add(receivingCommande);
        });
        receiving.setReceivingCommandeList(receivingCommandeList);
        List<ReceivingDetails> receivingDetailsList = new ArrayList<>();
        receivingDTO.getReceivingDetailsList().forEach(x -> {
            ReceivingDetails receivingDetails = receivingdetailsDTOToReceivingDetails(x);
//            ReceivingDetailsPK receivingDetailsPK = new ReceivingDetailsPK();
//            receivingDetailsPK.setCodart(x.getCodart());
            receivingDetails.setCodart(x.getCodart());
//            receivingDetailsPK.setReceiving(receiving.getCode());
//            receivingDetails.setReceivingDetailsPK(receivingDetailsPK);
            receivingDetails.setReceiving1(receiving);
            receivingDetailsList.add(receivingDetails);
        });
        receiving.setReceivingDetailsList(receivingDetailsList);

        return receiving;
    }


    public static ReceivingDTO receivingToReceivingDTO(Receiving receiving) {
        ReceivingDTO receivingDTO = new ReceivingDTO();
        receivingDTO.setNumaffiche(receiving.getNumaffiche());

        receivingDTO.setCode(receiving.getCode());
        receivingDTO.setNumbon(receiving.getNumbon());
        receivingDTO.setDateCreate(receiving.getDateCreate());
        receivingDTO.setUserCreate(receiving.getUserCreate());
        receivingDTO.setDateValidate(receiving.getDateValidate());
        receivingDTO.setUserValidate(receiving.getUserValidate());
        receivingDTO.setDateAnnule(receiving.getDateAnnule());
        receivingDTO.setUserAnnule(receiving.getUserAnnule());
        receivingDTO.setCategDepot(receiving.getCategDepot());
        receivingDTO.setFournisseur(receiving.getFournisseur());
        receivingDTO.setImprime(receiving.getImprime());
        receivingDTO.setDateImprime(receiving.getDateImprime());
        receivingDTO.setUserImprime(receiving.getUserImprime());
        receivingDTO.setMemo(receiving.getMemo());
        receivingDTO.setDateMemo(receiving.getDateMemo());
        receivingDTO.setUserMemo(receiving.getUserMemo());
        receivingDTO.setUserValidateReceiving(receiving.getUserValidateReceiving());
        receivingDTO.setDateValidateReceiving(receiving.getDateValidateReceiving());
        receivingDTO.setCodeSite(receiving.getCodeSite());
        List<Integer> codesCommandeAchat = receiving.getReceivingCommandeList().stream().map(x -> x.getReceivingCommandePK().getCommandeParamAchat()).collect(Collectors.toList());

        receivingDTO.setCodesCommandeAchat(codesCommandeAchat);
        if (receivingDTO.getBonRecep() != null) {
            receivingDTO.setNumAfficheBonRecep(receivingDTO.getBonRecep().getNumbon());
        }
        return receivingDTO;
    }
//        List<ReceivingDetailsDTO> receivingDetailsDTO = receivingdetailsToReceivingDetailsDTOs(receiving.getReceivingDetailsList());
//        if (receivingDTO.getBonRecep() != null) {
//            receivingDetailsDTO.forEach(x -> {
//                BigDecimal quantite = receivingDTO.getBonRecep().getDetails().stream().filter(
//                        y -> y.getRefArt().equals(x.getCodart())
//                ).map(z -> z.getQuantite())
//                        .reduce(BigDecimal.ZERO, BigDecimal::add);
//                x.setQuantiteReceptionne(quantite);
//            });
//        } else {
//            receivingDetailsDTO.forEach(x -> {
//                x.setQuantiteReceptionne(BigDecimal.ZERO);
//            });
//        }
//        receivingDTO.setReceivingDetailsList(receivingDetailsDTO);
//        if (receivingDTO.getBonRecep() != null) {
//            receivingDTO.setNumAfficheBonRecep(receivingDTO.getBonRecep().getNumbon());
//        }
//    return receivingDTO ;
//}

    public static List<ReceivingDTO> receivingToReceivingDTOs(List<Receiving> receivings) {
        List<ReceivingDTO> receivingsDTO = new ArrayList<>();
        receivings.forEach(x -> {
            receivingsDTO.add(receivingToReceivingDTO(x));
        });
        return receivingsDTO;
    }

    public static ReceivingEditionDTO receivingToReceivingEditionDTO(Receiving receiving) {
        ReceivingEditionDTO receivingEditionDTO = new ReceivingEditionDTO();
        receivingEditionDTO.setCode(receiving.getCode());
        receivingEditionDTO.setNumbon(receiving.getNumbon());
        receivingEditionDTO.setNumaffiche(receiving.getNumaffiche());
        receivingEditionDTO.setDateCreate(Date.from(receiving.getDateCreate().atZone(ZoneId.systemDefault()).toInstant()));
        receivingEditionDTO.setUserCreate(receiving.getUserCreate());
        receivingEditionDTO.setImprime(receiving.getImprime());
        if (receiving.getDateImprime() != null) {
            receivingEditionDTO.setDateImprime(Date.from(receiving.getDateImprime().atZone(ZoneId.systemDefault()).toInstant()));
        }
        receivingEditionDTO.setUserImprime(receiving.getUserImprime());
        receivingEditionDTO.setMemo(receiving.getMemo());
        if (receiving.getDateMemo() != null) {
            receivingEditionDTO.setDateMemo(Date.from(receiving.getDateMemo().atZone(ZoneId.systemDefault()).toInstant()));
        }
        receivingEditionDTO.setUserMemo(receiving.getUserMemo());
        if (receiving.getDateValidate() != null) {
            receivingEditionDTO.setDateValidate(Date.from(receiving.getDateValidate().atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            receivingEditionDTO.setDateValidate(null);
        }
            if (receiving.getDateValidateReceiving()!= null) {
            receivingEditionDTO.setDateValidateReceiving(Date.from(receiving.getDateValidateReceiving().atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            receivingEditionDTO.setDateValidateReceiving(null);
        }
        receivingEditionDTO.setUserValidate(receiving.getUserValidate());
        receivingEditionDTO.setUserValidateReceiving(receiving.getUserValidateReceiving());
        if (receiving.getDateAnnule() != null) {
            receivingEditionDTO.setDateAnnule(Date.from(receiving.getDateAnnule().atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            receivingEditionDTO.setDateAnnule(null);
        }
        receivingEditionDTO.setUserAnnule(receiving.getUserAnnule());
        receivingEditionDTO.setCategDepot(receiving.getCategDepot());
        receivingEditionDTO.setFournisseur(receiving.getFournisseur());
        List<Integer> codesCommandeAchat = new ArrayList<>();
        receiving.getReceivingCommandeList().forEach(x -> {
            codesCommandeAchat.add(x.getReceivingCommandePK().getCommandeParamAchat());
        });
//        receivingEditionDTO.setBonRecep(factureBAFactory.factureBAToBonRecepDTOEager(receiving.getFactureBA()));
        receivingEditionDTO.setCodesCommandeAchat(codesCommandeAchat);
//        List<ReceivingDetailsDTO> receivingDetailsDTO = ReceivingDetailsFactory.receivingdetailsToReceivingDetailsDTOs(receiving.getReceivingDetailsList());
//        if (receivingEditionDTO.getBonRecep() != null) {
//            receivingDetailsDTO.forEach(x -> {
//                BigDecimal quantite = receivingEditionDTO.getBonRecep().getDetails().stream()
//                        .filter(y -> y.getRefArt().equals(x.getCodart()))
//                        .map(z -> z.getQuantite())
//                        .reduce(BigDecimal.ZERO, BigDecimal::add);
//                x.setQuantiteReceptionne(quantite);
//            });
//        } else {
//            receivingDetailsDTO.forEach(x -> {
//                x.setQuantiteReceptionne(BigDecimal.ZERO);
//            });
//        }
//        receivingEditionDTO.setReceivingDetailsList(receivingDetailsDTO);
//        if (receivingEditionDTO.getBonRecep() != null) {
//            receivingEditionDTO.setNumAfficheBonRecep(receivingEditionDTO.getBonRecep().getNumbon());
//        }
        return receivingEditionDTO;
    }
       public static ReceivingDTO receivingToReceivingDTOEager(Receiving receiving) {
        ReceivingDTO receivingDTO = receivingToReceivingDTO(receiving);
        List<ReceivingDetailsDTO> receivingDetailsDTO = receivingdetailsToReceivingDetailsDTOs(receiving.getReceivingDetailsList());
        receivingDTO.setReceivingDetailsList(receivingDetailsDTO);
         receivingDTO.setDateCreateEdition(java.util.Date.from(receiving.getDateCreate().atZone(ZoneId.systemDefault()).toInstant()));
        return receivingDTO;
    }
     
         public static List<ReceivingDTO> receivingToReceivingDTOEager(List<Receiving> receivings) {
        List<ReceivingDTO> receivingsDTO = new ArrayList<>();
        receivings.forEach(x -> {
            receivingsDTO.add(receivingToReceivingDTOEager(x));
        });
        return receivingsDTO;
    }

}
//@Deprecated
//    public CommandeAchatEditionDTO commandeAchatDTOToCommandeAchatEditionDTO(CommandeAchatDTO commandeAchatDTO, Integer codeReceving) {
//        CommandeAchatEditionDTO commandeAchatEditionDTO = new CommandeAchatEditionDTO();
//        commandeAchatEditionDTO.setCategorieDepot(commandeAchatDTO.getCategorieDepot());
//        commandeAchatEditionDTO.setCode(commandeAchatDTO.getCode());
//        commandeAchatEditionDTO.setCodeReceiving(codeReceving);
//        commandeAchatEditionDTO.setDateAnnul(commandeAchatDTO.getDateAnnul());
//        commandeAchatEditionDTO.setDateCreate(commandeAchatDTO.getDateCreate());
//        commandeAchatEditionDTO.setDateDelete(commandeAchatDTO.getDateDelete());
////        commandeAchatEditionDTO.setDelaiPaiement(commandeAchatDTO.getDelaiPaiement());
//        commandeAchatEditionDTO.setFournisseur(commandeAchatDTO.getFournisseur().getCode());
//        commandeAchatEditionDTO.setImprimer(commandeAchatDTO.getImprimer());
//        commandeAchatEditionDTO.setManuel(commandeAchatDTO.getManuel());
//        commandeAchatEditionDTO.setMemo(commandeAchatDTO.getMemo());
//        commandeAchatEditionDTO.setNumbon(commandeAchatDTO.getNumbon());
//        commandeAchatEditionDTO.setSatisfaction(commandeAchatDTO.getSatisfaction());
//        commandeAchatEditionDTO.setStockable(commandeAchatDTO.getStockable());
//        commandeAchatEditionDTO.setUserAnnul(commandeAchatDTO.getUserAnnul());
//        commandeAchatEditionDTO.setUserCreate(commandeAchatDTO.getUserCreate());
//        commandeAchatEditionDTO.setUserDelete(commandeAchatDTO.getUserDelete());
//        commandeAchatEditionDTO.setModeReglementList(commandeAchatDTO.getModeReglementList());
//        return commandeAchatEditionDTO;
//    }

//    public List<DetailCommandeEditionDTO> commandeAchatDTOToDetailCommandeEditionDTOs(CommandeAchatDTO commandeAchatDTO) {
//        List<DetailCommandeEditionDTO> detailCommandeEditionDTOs = new ArrayList<>();
//        Integer codeCommandeAchat = commandeAchatDTO.getCode();
//        commandeAchatDTO.getDetailCommandeAchatCollection().forEach(x -> {
//            DetailCommandeEditionDTO detailCommandeEditionDTO = new DetailCommandeEditionDTO();
//            detailCommandeEditionDTO.setCodart(x.getCodart());
//            detailCommandeEditionDTO.setCodeSaisi(x.getCodeSaisi());
//            detailCommandeEditionDTO.setDesignation(x.getDesignation());
//            detailCommandeEditionDTO.setPrixUnitaire(x.getPrixUnitaire());
//            detailCommandeEditionDTO.setQuantite(x.getQuantite());
//            detailCommandeEditionDTO.setCodeCommandeAchat(codeCommandeAchat);
//            detailCommandeEditionDTOs.add(detailCommandeEditionDTO);
//        });
//        return detailCommandeEditionDTOs;
//    }
//}

