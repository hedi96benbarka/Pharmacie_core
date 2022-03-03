package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.DetailTransfertCompanyBranch;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.DetailTransfertCompanyBranchDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DetailTransfertCompanyBranchFactory {
    
    public static DetailTransfertCompanyBranchDTO detailtransfertcompanybranchToDetailTransfertCompanyBranchDTO(DetailTransfertCompanyBranch detailtransfertcompanybranch) {
        DetailTransfertCompanyBranchDTO detailtransfertcompanybranchDTO = new DetailTransfertCompanyBranchDTO();
        detailtransfertcompanybranchDTO.setCode(detailtransfertcompanybranch.getCode());
        detailtransfertcompanybranchDTO.setCodeArticle(detailtransfertcompanybranch.getCodeArticle());
        detailtransfertcompanybranchDTO.setLotInter(detailtransfertcompanybranch.getLotInter());
        detailtransfertcompanybranchDTO.setDatePeremption(detailtransfertcompanybranch.getDatePeremption());
        detailtransfertcompanybranchDTO.setCategDepot(detailtransfertcompanybranch.getCategDepot());
        detailtransfertcompanybranchDTO.setCodeSaisi(detailtransfertcompanybranch.getCodeSaisi());
        detailtransfertcompanybranchDTO.setCodeUnite(detailtransfertcompanybranch.getCodeUnite());
        detailtransfertcompanybranchDTO.setDesignationSec(detailtransfertcompanybranch.getDesignationSec());
        detailtransfertcompanybranchDTO.setDesignation(detailtransfertcompanybranch.getDesignation());
        detailtransfertcompanybranchDTO.setQuantite(detailtransfertcompanybranch.getQuantite());
        detailtransfertcompanybranchDTO.setPrixUnitaire(detailtransfertcompanybranch.getPrixUnitaire());
        detailtransfertcompanybranchDTO.setMontantHt(detailtransfertcompanybranch.getMontantHt());
        detailtransfertcompanybranchDTO.setCodeTva(detailtransfertcompanybranch.getCodeTva());
        detailtransfertcompanybranchDTO.setTauxTva(detailtransfertcompanybranch.getTauxTva());
//        detailtransfertcompanybranchDTO.setNumBon(detailtransfertcompanybranch.getNumBon());
        detailtransfertcompanybranchDTO.setRemise(detailtransfertcompanybranch.getRemise());
        detailtransfertcompanybranchDTO.setBaseTva(detailtransfertcompanybranch.getBaseTva());
        detailtransfertcompanybranchDTO.setIsCapitalize(detailtransfertcompanybranch.getIsCapitalize());
        detailtransfertcompanybranchDTO.setPrixVente(detailtransfertcompanybranch.getPrixVente());
        detailtransfertcompanybranchDTO.setQuantiteRestante(detailtransfertcompanybranch.getQuantiteRestante());
        detailtransfertcompanybranchDTO.setIsReferencePrice(detailtransfertcompanybranch.getIsReferencePrice());
        detailtransfertcompanybranchDTO.setCodeEmplacement(detailtransfertcompanybranch.getCodeEmplacement());
        
        if (detailtransfertcompanybranch.getPrixUnitaire().compareTo(BigDecimal.ZERO) == 0) {
            detailtransfertcompanybranchDTO.setFree(true);
        } else if (detailtransfertcompanybranch.getPrixUnitaire().compareTo(BigDecimal.ZERO) == 1) {
            detailtransfertcompanybranchDTO.setFree(false);
        }
        return detailtransfertcompanybranchDTO;
    }
    
    public static DetailTransfertCompanyBranch detailtransfertcompanybranchDTOToDetailTransfertCompanyBranch(DetailTransfertCompanyBranchDTO detailTransfertCompanyBranchDTO) {
        DetailTransfertCompanyBranch detailTransfertCompanyBranch = new DetailTransfertCompanyBranch();
        detailTransfertCompanyBranch.setCode(detailTransfertCompanyBranchDTO.getCode());
        detailTransfertCompanyBranch.setCodeArticle(detailTransfertCompanyBranchDTO.getCodeArticle());
        detailTransfertCompanyBranch.setLotInter(detailTransfertCompanyBranchDTO.getLotInter());
        detailTransfertCompanyBranch.setDatePeremption(detailTransfertCompanyBranchDTO.getDatePeremption());
        detailTransfertCompanyBranch.setCategDepot(detailTransfertCompanyBranchDTO.getCategDepot());
        detailTransfertCompanyBranch.setCodeSaisi(detailTransfertCompanyBranchDTO.getCodeSaisi());
        detailTransfertCompanyBranch.setCodeUnite(detailTransfertCompanyBranchDTO.getCodeUnite());
        detailTransfertCompanyBranch.setDesignationSec(detailTransfertCompanyBranchDTO.getDesignationSec());
        detailTransfertCompanyBranch.setDesignation(detailTransfertCompanyBranchDTO.getDesignation());
        detailTransfertCompanyBranch.setQuantite(detailTransfertCompanyBranchDTO.getQuantite());
        detailTransfertCompanyBranch.setPrixUnitaire(detailTransfertCompanyBranchDTO.getPrixUnitaire());
        detailTransfertCompanyBranch.setMontantHt(detailTransfertCompanyBranchDTO.getMontantHt());
        detailTransfertCompanyBranch.setCodeTva(detailTransfertCompanyBranchDTO.getCodeTva());
        detailTransfertCompanyBranch.setTauxTva(detailTransfertCompanyBranchDTO.getTauxTva());
//        detailTransfertCompanyBranch.setNumBon(detailTransfertCompanyBranchDTO.getNumBon());
        detailTransfertCompanyBranch.setRemise(detailTransfertCompanyBranchDTO.getRemise());
        detailTransfertCompanyBranch.setBaseTva(detailTransfertCompanyBranchDTO.getBaseTva());
        detailTransfertCompanyBranch.setIsCapitalize(detailTransfertCompanyBranchDTO.getIsCapitalize());
        detailTransfertCompanyBranch.setPrixVente(detailTransfertCompanyBranchDTO.getPrixVente());
        detailTransfertCompanyBranch.setPmpPrecedent(detailTransfertCompanyBranchDTO.getPmpPrecedent());
        detailTransfertCompanyBranch.setQuantitePrecedante(detailTransfertCompanyBranchDTO.getQuantitePrecedante());
        detailTransfertCompanyBranch.setQuantiteRestante(detailTransfertCompanyBranchDTO.getQuantiteRestante());
        detailTransfertCompanyBranch.setIsReferencePrice(detailTransfertCompanyBranchDTO.getIsReferencePrice());
        detailTransfertCompanyBranch.setCodeEmplacement(detailTransfertCompanyBranchDTO.getCodeEmplacement());
        return detailTransfertCompanyBranch;
    }
    
    public static Collection<DetailTransfertCompanyBranchDTO> detailtransfertcompanybranchToDetailTransfertCompanyBranchDTOs(Collection<DetailTransfertCompanyBranch> detailtransfertcompanybranchs) {
        List<DetailTransfertCompanyBranchDTO> detailtransfertcompanybranchsDTO = new ArrayList<>();
        detailtransfertcompanybranchs.forEach(x -> {
            detailtransfertcompanybranchsDTO.add(detailtransfertcompanybranchToDetailTransfertCompanyBranchDTO(x));
        });
        return detailtransfertcompanybranchsDTO;
    }
    
    public static Collection<DetailTransfertCompanyBranch> detailTransfertCompanyBranchDTOsToDetailTransfertCompanyBranch(Collection<DetailTransfertCompanyBranchDTO> detailtransfertcompanybranchsDTO, TransfertCompanyBranch transfertCompanyBranch) {
        List<DetailTransfertCompanyBranch> detailtransfertcompanybranchs = new ArrayList<>();
        detailtransfertcompanybranchsDTO.forEach(x -> {
            DetailTransfertCompanyBranch detailtransfertcompanybranch = detailtransfertcompanybranchDTOToDetailTransfertCompanyBranch(x);
            detailtransfertcompanybranch.setTransfertCompanyBranch(transfertCompanyBranch);
            detailtransfertcompanybranchs.add(detailtransfertcompanybranch);
        });
        return detailtransfertcompanybranchs;
    }
}
