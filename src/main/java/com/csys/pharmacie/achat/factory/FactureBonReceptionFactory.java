package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.BaseTvaFactureBonReception;
import com.csys.pharmacie.achat.domain.FactureBonReception;
import com.csys.pharmacie.achat.dto.BaseTvaFactureBonReceptionDTO;

import com.csys.pharmacie.achat.dto.FactureBonReceptionDTO;
import java.time.Instant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.time.ZoneId;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactureBonReceptionFactory {

    private final Logger log = LoggerFactory.getLogger(FactureBonReceptionFactory.class);

    public static FactureBonReceptionDTO facturebonreceptionToFactureBonReceptionDTO(FactureBonReception factureBonReception) {
        if (factureBonReception == null) {
            return null;
        } else {
            FactureBonReceptionDTO facturebonreceptionDTO = new FactureBonReceptionDTO();
            facturebonreceptionDTO.setCodeFournisseur(factureBonReception.getCodeFournisseur());
            facturebonreceptionDTO.setDateFournisseur(factureBonReception.getDateFournisseur());
            facturebonreceptionDTO.setReferenceFournisseur(factureBonReception.getReferenceFournisseur());
            facturebonreceptionDTO.setCodvend(factureBonReception.getCodvend());
            facturebonreceptionDTO.setDatesys(factureBonReception.getDatesys());
            facturebonreceptionDTO.setNumaffiche(factureBonReception.getNumaffiche());
            facturebonreceptionDTO.setNumbon(factureBonReception.getNumbon());
            facturebonreceptionDTO.setMontant(factureBonReception.getMontant());
            facturebonreceptionDTO.setHeuresys(factureBonReception.getHeuresys());
            facturebonreceptionDTO.setDatBon(factureBonReception.getDatbon());
            facturebonreceptionDTO.setTypbon(factureBonReception.getTypbon());
            facturebonreceptionDTO.setCategDepot(factureBonReception.getCategDepot());
            facturebonreceptionDTO.setRemiseExep(factureBonReception.getRemiseExep());
            facturebonreceptionDTO.setUserAnnule(factureBonReception.getUserAnnule());
            facturebonreceptionDTO.setDateAnnule(factureBonReception.getDateAnnule());
            facturebonreceptionDTO.setCodeDevise(factureBonReception.getCodeDevise());
            facturebonreceptionDTO.setTauxDevise(factureBonReception.getTauxDevise());
            facturebonreceptionDTO.setMontantDevise(factureBonReception.getMontantDevise());
            facturebonreceptionDTO.setMaxDelaiPaiement(factureBonReception.getMaxDelaiPaiement());
            Instant instant = factureBonReception.getDateFournisseur().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            facturebonreceptionDTO.setDateFournisseurEdition(Date.from(instant));
            facturebonreceptionDTO.setDatebonEdition(java.util.Date.from(factureBonReception.getDatbon().atZone(ZoneId.systemDefault())
                    .toInstant()));

            List<BaseTvaFactureBonReceptionDTO> baseTvaFactureBonReceptionCollectionDtos = new ArrayList();
            factureBonReception.getBaseTvaFactureBonReceptionCollection().forEach(x -> {
                BaseTvaFactureBonReceptionDTO basetvafacturebonreceptionDto = BaseTvaFactureBonReceptionFactory.basetvafacturebonreceptionToBaseTvaFactureBonReceptionDTO(x);
                baseTvaFactureBonReceptionCollectionDtos.add(basetvafacturebonreceptionDto);
            });
            facturebonreceptionDTO.setBaseTvaFactureBonReceptionCollection(baseTvaFactureBonReceptionCollectionDtos);

            return facturebonreceptionDTO;
        }
    }

    public static FactureBonReception facturebonreceptionDTOToFactureBonReception(FactureBonReceptionDTO facturebonreceptionDTO) {
        FactureBonReception facturebonreception = new FactureBonReception();
        facturebonreception.setCodeFournisseur(facturebonreceptionDTO.getCodeFournisseur());
        facturebonreception.setDateFournisseur(facturebonreceptionDTO.getDateFournisseur());
        facturebonreception.setReferenceFournisseur(facturebonreceptionDTO.getReferenceFournisseur());
        facturebonreception.setCodvend(facturebonreceptionDTO.getCodvend());
        facturebonreception.setDatesys(facturebonreceptionDTO.getDatesys());
        facturebonreception.setNumaffiche(facturebonreceptionDTO.getNumaffiche());
//        facturebonreception.setNumbon(facturebonreceptionDTO.getNumbon());
        facturebonreception.setMontant(facturebonreceptionDTO.getMontant());
        facturebonreception.setHeuresys(facturebonreceptionDTO.getHeuresys());
        facturebonreception.setDatbon(facturebonreceptionDTO.getDatBon());
//        facturebonreception.setTypbon(facturebonreceptionDTO.getTypbon());
        facturebonreception.setCategDepot(facturebonreceptionDTO.getCategDepot());
        facturebonreception.setRemiseExep(facturebonreceptionDTO.getRemiseExep());
        facturebonreception.setUserAnnule(facturebonreceptionDTO.getUserAnnule());
        facturebonreception.setDateAnnule(facturebonreceptionDTO.getDateAnnule());
        facturebonreception.setCodeDevise(facturebonreceptionDTO.getCodeDevise());
        facturebonreception.setTauxDevise(facturebonreceptionDTO.getTauxDevise());
        facturebonreception.setMontantDevise(facturebonreceptionDTO.getMontantDevise());
        Collection<BaseTvaFactureBonReception> baseTvaFactureBonReceptionCollections = new ArrayList<>();
        facturebonreceptionDTO.getBaseTvaFactureBonReceptionCollection().forEach(x -> {
            BaseTvaFactureBonReception basetvafacturebonreception = BaseTvaFactureBonReceptionFactory.basetvafacturebonreceptionDTOToBaseTvaFactureBonReception(x);
            basetvafacturebonreception.setFactureBonReception(facturebonreception);
            baseTvaFactureBonReceptionCollections.add(basetvafacturebonreception);
        });

        facturebonreception.setBaseTvaFactureBonReceptionCollection((List<BaseTvaFactureBonReception>) baseTvaFactureBonReceptionCollections);

        return facturebonreception;
    }

    public static Collection<FactureBonReceptionDTO> facturebonreceptionToFactureBonReceptionDTOs(Collection<FactureBonReception> facturebonreceptions) {
        List<FactureBonReceptionDTO> facturebonreceptionsDTO = new ArrayList<>();
        facturebonreceptions.forEach(x -> {
            facturebonreceptionsDTO.add(facturebonreceptionToFactureBonReceptionDTO(x));
        });
        return facturebonreceptionsDTO;
    }

    public static FactureBonReceptionDTO lazyfacturebonreceptionToFactureBonReceptionDTO(FactureBonReception facturebonreception) {
        FactureBonReceptionDTO facturebonreceptionDTO = new FactureBonReceptionDTO();
        facturebonreceptionDTO.setCodeFournisseur(facturebonreception.getCodeFournisseur());
        facturebonreceptionDTO.setDateFournisseur(facturebonreception.getDateFournisseur());
        facturebonreceptionDTO.setReferenceFournisseur(facturebonreception.getReferenceFournisseur());
        facturebonreceptionDTO.setCodvend(facturebonreception.getCodvend());
        facturebonreceptionDTO.setDatesys(facturebonreception.getDatesys());
        facturebonreceptionDTO.setNumaffiche(facturebonreception.getNumaffiche());
        facturebonreceptionDTO.setNumbon(facturebonreception.getNumbon());
        facturebonreceptionDTO.setMontant(facturebonreception.getMontant());
        facturebonreceptionDTO.setHeuresys(facturebonreception.getHeuresys());
        facturebonreceptionDTO.setDatBon(facturebonreception.getDatbon());
        facturebonreceptionDTO.setTypbon(facturebonreception.getTypbon());
        facturebonreceptionDTO.setCategDepot(facturebonreception.getCategDepot());
        facturebonreceptionDTO.setRemiseExep(facturebonreception.getRemiseExep());
        facturebonreceptionDTO.setUserAnnule(facturebonreception.getUserAnnule());
        facturebonreceptionDTO.setDateAnnule(facturebonreception.getDateAnnule());
        facturebonreceptionDTO.setMaxDelaiPaiement(facturebonreception.getMaxDelaiPaiement());
        facturebonreception.setCodeDevise(facturebonreceptionDTO.getCodeDevise());
        facturebonreception.setTauxDevise(facturebonreceptionDTO.getTauxDevise());
        facturebonreception.setMontantDevise(facturebonreceptionDTO.getMontantDevise());
        return facturebonreceptionDTO;
    }

    public static Collection<FactureBonReceptionDTO> lazyfacturebonreceptionToFactureBonReceptionDTOs(Collection<FactureBonReception> facturebonreceptions) {
        List<FactureBonReceptionDTO> facturebonreceptionsDTO = new ArrayList<>();
        facturebonreceptions.forEach(x -> {
            facturebonreceptionsDTO.add(lazyfacturebonreceptionToFactureBonReceptionDTO(x));
        });
        return facturebonreceptionsDTO;
    }

//    public FactureBonReceptionEditionDTO factureBonRecepetionToFactureBonReceptionEditionDTO(FactureBonReception factureBonReception) {
//        if (factureBonReception == null) {
//            return null;
//        }
//        FactureBonReceptionEditionDTO factureBonReceptionEditionDTO = new FactureBonReceptionEditionDTO();
//
//        factureBonReceptionToFactureBrEditionDTO(factureBonReceptionEditionDTO, factureBonReception);
//
//        factureBonReceptionEditionDTO.setBaseTva((List<BaseTvaFactureBonReceptionDTO>) baseTVAFactureBonReceptionFactory.basetvafacturebonreceptionToBaseTvaFactureBonReceptionDTOs(factureBonReception.getBaseTvaFactureBonReceptionCollection()));
//        factureBonReceptionEditionDTO.setCategDepot(factureBonReception.getCategDepot());
//        factureBonReceptionEditionDTO.setFournisseur(paramAchatServiceClient.findFournisseurByCode(factureBonReception.getCodeFournisseur()));
//
//        List<DetailEditionDTO> detailsReception = new ArrayList();
//        Set<DetailReceptionDTO> details = factureBonReceptionService.findDetailsFactureById(factureBonReception.getNumbon());
//        
//        details.forEach(mvtstoBA -> {
//            detailReceptionFactory.t
//          DetailEditionDTO detailRecep = detailReceptionFactory.toEditionDTO(mvtstoBA);
//            detailRecep.setQteReturned(mvtstoBA.getQuantite().subtract(mvtstoBA.getQtecom()));
//            detailsReception.add(detailRecep);
//        });
//        bonRecepEditionDTO.setDetails((List) detailsReception);
//        if (factureBA.getTypbon() == TypeBonEnum.BE) {
//            List<Integer> listCodesCA = factureBA.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());
//            bonRecepEditionDTO.setPurchaseOrders(demandeServiceClient.findListCommandeAchat(listCodesCA, LocaleContextHolder.getLocale().getLanguage()));
//        }
//
//        return factureBonReceptionEditionDTO;
//    }
//    private void factureBonReceptionToFactureBrEditionDTO(FactureBonReceptionEditionDTO dto, FactureBonReception entity) {
//        dto.setNumaffiche(entity.getNumaffiche());
//        dto.setNumbon(entity.getNumbon());
//        dto.setDatbon(entity.getDatbon());
//        dto.setCodvend(entity.getCodvend());
//        dto.setMontant(entity.getMontant());
//        if (entity.getDateFournisseur() != null) {
//            dto.setDateFournisseur(Date.from(entity.getDateFournisseur().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        }
//        dto.setReferenceFournisseur(entity.getReferenceFournisseur());
//        dto.setCodeFournisseur(entity.getCodeFournisseur());
//        dto.setTypbon(entity.getTypbon());
//        dto.setCategDepot(entity.getCategDepot());
//        dto.setRemiseExep(entity.getRemiseExep());
//    }
}
