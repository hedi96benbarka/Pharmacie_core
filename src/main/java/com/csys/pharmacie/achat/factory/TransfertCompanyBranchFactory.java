package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransfertCompanyBranchFactory {

    public static TransfertCompanyBranchDTO transfertCompanyBranchToTransfertCompanyBranchDTO(TransfertCompanyBranch transfertcompanybranch, Boolean withDetails) {
        TransfertCompanyBranchDTO transfertCompanyBranchDTO = new TransfertCompanyBranchDTO();
        transfertCompanyBranchDTO.setNumBon(transfertcompanybranch.getNumBon());
        transfertCompanyBranchDTO.setTypeBon(transfertcompanybranch.getTypeBon());
        transfertCompanyBranchDTO.setCategDepot(transfertcompanybranch.getCategDepot());
        transfertCompanyBranchDTO.setNumAffiche(transfertcompanybranch.getNumAffiche());
        transfertCompanyBranchDTO.setUserCreate(transfertcompanybranch.getUserCreate());
        transfertCompanyBranchDTO.setDateCreate(transfertcompanybranch.getDateCreate());
        transfertCompanyBranchDTO.setCodeFournisseur(transfertcompanybranch.getCodeFournisseur());
        transfertCompanyBranchDTO.setMontantTTC(transfertcompanybranch.getMontantTtc());
        transfertCompanyBranchDTO.setNumbonReception(transfertcompanybranch.getNumbonReception());
        transfertCompanyBranchDTO.setReplicated(transfertcompanybranch.getReplicated());
        transfertCompanyBranchDTO.setCodeSite(transfertcompanybranch.getCodeSite());
        transfertCompanyBranchDTO.setCodeDepot(transfertcompanybranch.getCodeDepot());
        transfertCompanyBranchDTO.setNumBonTransfertRelatif(transfertcompanybranch.getNumBonTransfertRelatif());
        transfertCompanyBranchDTO.setReturnedToSupplier(transfertcompanybranch.getReturnedToSupplier());
        transfertCompanyBranchDTO.setDateCreateReception(transfertcompanybranch.getDateCreateReception());
//        if (contextReception.contains("company") && transfertcompanybranch.getReceptionRelative() != null) {
            transfertCompanyBranchDTO.setOnShelf(transfertcompanybranch.getOnShelf());
//        }
        if (Boolean.TRUE.equals(withDetails)) {
            transfertCompanyBranchDTO.setDetailTransfertCompanyBranchDTOs(DetailTransfertCompanyBranchFactory.detailtransfertcompanybranchToDetailTransfertCompanyBranchDTOs(transfertcompanybranch.getDetailTransfertCompanyBranchCollection()));
        }
        return transfertCompanyBranchDTO;
    }

    public static TransfertCompanyBranch transfertCompanyBranchDTOToTransfertCompanyBranch(TransfertCompanyBranchDTO transfertcompanybranchDTO) {
        TransfertCompanyBranch transfertCompanyBranch = new TransfertCompanyBranch();
        transfertCompanyBranch.setNumBon(transfertcompanybranchDTO.getNumBon());
        transfertCompanyBranch.setTypeBon(transfertcompanybranchDTO.getTypeBon());
        transfertCompanyBranch.setCategDepot(transfertcompanybranchDTO.getCategDepot());
        transfertCompanyBranch.setNumAffiche(transfertcompanybranchDTO.getNumAffiche());
        transfertCompanyBranch.setUserCreate(transfertcompanybranchDTO.getUserCreate());
        transfertCompanyBranch.setDateCreate(transfertcompanybranchDTO.getDateCreate());
        transfertCompanyBranch.setDateCreateReception(transfertcompanybranchDTO.getDateCreateReception());
        transfertCompanyBranch.setCodeFournisseur(transfertcompanybranchDTO.getCodeFournisseur());
        transfertCompanyBranch.setMontantTtc(transfertcompanybranchDTO.getMontantTTC());
        transfertCompanyBranch.setNumbonReception(transfertcompanybranchDTO.getNumbonReception());
        transfertCompanyBranch.setCodeDepot(transfertcompanybranchDTO.getCodeDepot());
        transfertCompanyBranch.setCodeSite(transfertcompanybranchDTO.getCodeSite());
        transfertCompanyBranch.setNumBonTransfertRelatif(transfertcompanybranchDTO.getNumBonTransfertRelatif());
        transfertCompanyBranch.setOnShelf(transfertcompanybranchDTO.getOnShelf());
        transfertCompanyBranch.setDetailTransfertCompanyBranchCollection(
                DetailTransfertCompanyBranchFactory.detailTransfertCompanyBranchDTOsToDetailTransfertCompanyBranch(transfertcompanybranchDTO.getDetailTransfertCompanyBranchDTOs(), transfertCompanyBranch));
        return transfertCompanyBranch;
    }

    public static Collection<TransfertCompanyBranchDTO> transfertcompanybranchToTransfertCompanyBranchDTOs(Collection<TransfertCompanyBranch> transfertcompanybranchs) {
        List<TransfertCompanyBranchDTO> transfertcompanybranchsDTO = new ArrayList<>();
        transfertcompanybranchs.forEach(x -> {
            transfertcompanybranchsDTO.add(transfertCompanyBranchToTransfertCompanyBranchDTO(x, Boolean.FALSE));
        });
        return transfertcompanybranchsDTO;
    }
}
