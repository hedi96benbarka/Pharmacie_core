package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.DetailReceptionTemporaire;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.ReceptionTemporaire;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.DetailReceptionTemporaireDTO;
import com.csys.pharmacie.achat.dto.FactureBADTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import com.csys.pharmacie.achat.dto.ReceptionTemporaireDTO;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.stock.domain.Depsto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

public class ReceptionTemporaireFactory {

    private final Logger log = LoggerFactory.getLogger(ReceptionTemporaireFactory.class);

    public static ReceptionTemporaireDTO receptiontemporaireToReceptionTemporaireDTOLazy(ReceptionTemporaire receptiontemporaire) {
        ReceptionTemporaireDTO receptiontemporaireDTO = new ReceptionTemporaireDTO();

        receptiontemporaireDTO.setCodfrs(receptiontemporaire.getCodfrs());
        receptiontemporaireDTO.setMntbon(receptiontemporaire.getMntbon());
        receptiontemporaireDTO.setMntBon(receptiontemporaire.getMntbon());
        receptiontemporaireDTO.setMemop(receptiontemporaire.getMemop());
        receptiontemporaireDTO.setReffrs(receptiontemporaire.getReffrs());
        receptiontemporaireDTO.setDateFrs(receptiontemporaire.getDatRefFrs().toLocalDate());
        receptiontemporaireDTO.setCoddep(receptiontemporaire.getCoddep());
        receptiontemporaireDTO.setCodAnnul(receptiontemporaire.getCodAnnul());
        receptiontemporaireDTO.setDatAnnul(receptiontemporaire.getDatAnnul());
        receptiontemporaireDTO.setCodePieceJointe(receptiontemporaire.getCodePieceJointe());
        receptiontemporaireDTO.setFournisseurExonere(receptiontemporaire.getFrsExonere());
        receptiontemporaireDTO.setIsTemporaire(receptiontemporaire.getIsTemporaire());
        // receptiontemporaireDTO.setDateDebutExoneration(receptiontemporaire.getDateDebutExoneration().toLocalDate());
        // receptiontemporaireDTO.setDateFinExoneration(receptiontemporaire.getDateFinExoneration().toLocalDate());
        receptiontemporaireDTO.setNumbon(receptiontemporaire.getNumbon());
        receptiontemporaireDTO.setCodvend(receptiontemporaire.getCodvend());
        receptiontemporaireDTO.setDatbon(receptiontemporaire.getDatbon());
        receptiontemporaireDTO.setDatesys(receptiontemporaire.getDatesys());
        receptiontemporaireDTO.setHeuresys(receptiontemporaire.getHeuresys());
        receptiontemporaireDTO.setTypbon("BAT");
        receptiontemporaireDTO.setNumaffiche(receptiontemporaire.getNumaffiche());
        receptiontemporaireDTO.setCategDepot(receptiontemporaire.getCategDepot());
        receptiontemporaireDTO.setReceivingCode(receptiontemporaire.getcodeReceiving());
               if(receptiontemporaire.getReceivingNumaffiche()!=null)
            receptiontemporaireDTO.setReceivingNumaffiche(receptiontemporaire.getReceivingNumaffiche());
               if(receptiontemporaire.getFactureBA()!=null)
                   receptiontemporaireDTO.setNumBonFactureBa(receptiontemporaire.getFactureBA().getNumbon());
               if(receptiontemporaire.isIsValidated())
               receptiontemporaireDTO.setIsValidated(receptiontemporaire.isIsValidated());
        /* if (receptiontemporaireDTO.getDetailReceptionTempraireDTO()!=null){
      List<DetailReceptionTemporaireDTO> details = new ArrayList<>();
        for (  DetailReceptionTemporaireDTO detailReceptionTemporaireDTO : receptiontemporaireDTO.getDetailReceptionTempraireDTO()) {
              details.add(detailReceptionTemporaireDTO);
                  }
        receptiontemporaireDTO.setDetailReceptionTempraireDTO(details);
     receptiontemporaireDTO.setDetailReceptionTempraireDTO(details);
    }
         */

        return receptiontemporaireDTO;
    }

    public static ReceptionTemporaire receptiontemporaireDTOToReceptionTemporaire(ReceptionTemporaireDTO receptiontemporaireDTO) {
        ReceptionTemporaire receptiontemporaire = new ReceptionTemporaire();
        receptiontemporaire.setCodfrs(receptiontemporaireDTO.getCodfrs());
        receptiontemporaire.setMntbon(receptiontemporaireDTO.getMntbon());
        receptiontemporaire.setMemop(receptiontemporaireDTO.getMemop());
        receptiontemporaire.setReffrs(receptiontemporaireDTO.getReffrs());
        receptiontemporaire.setDatRefFrs(receptiontemporaireDTO.getDateFrs().atStartOfDay());
        receptiontemporaire.setCoddep(receptiontemporaireDTO.getCoddep());
        receptiontemporaire.setCodAnnul(receptiontemporaireDTO.getCodAnnul());

        receptiontemporaire.setDatAnnul(receptiontemporaireDTO.getDatAnnul());
        receptiontemporaire.setCodePieceJointe(receptiontemporaireDTO.getCodePieceJointe());
        receptiontemporaire.setFrsExonere(receptiontemporaireDTO.getFournisseurExonere());
        //  receptiontemporaire.setDateDebutExoneration(receptiontemporaireDTO.getDateDebutExoneration().atStartOfDay());
        //   receptiontemporaire.setDateFinExoneration(receptiontemporaireDTO.getDateFinExoneration().atTime(23,59,59));
        receptiontemporaire.setCategDepot(receptiontemporaireDTO.getCategDepot());
        //receptiontemporaire.setcodeReceiving(receptiontemporaireDTO.getReceivingCode());
        // receptiontemporaire.setMaxDelaiPaiement(receptiontemporaireDTO.getMaxDelaiPaiement());
        receptiontemporaire.setTypbon(TypeBonEnum.BAT);
        /*   if(receptiontemporaireDTO.getDetailReceptionTempraireDTO()!=null){
      List<DetailReceptionTemporaire> details = new ArrayList<>();
        for (  DetailReceptionTemporaireDTO detailReceptionTemporaireDTO : receptiontemporaireDTO.getDetailReceptionTempraireDTO()) {
              details.add(DetailReceptionTemporaireFactory.detailreceptiontemporaireDTOToDetailReceptionTemporaire(detailReceptionTemporaireDTO));
                  }
       
  
    
    
     receptiontemporaire.setDetailReceptionTempraire(details);}*/

        return receptiontemporaire;
    }

    public static Collection<ReceptionTemporaireDTO> receptiontemporaireToReceptionTemporaireDTOs(Collection<ReceptionTemporaire> receptiontemporaires) {
        List<ReceptionTemporaireDTO> receptiontemporairesDTO = new ArrayList<>();
        receptiontemporaires.forEach(x -> {
            receptiontemporairesDTO.add(receptiontemporaireToReceptionTemporaireDTOLazy(x));
        });
        return receptiontemporairesDTO;
    }

    public static ReceptionTemporaireDTO receptiontemporaireToReceptionTemporaireDTONotLazy(ReceptionTemporaire receptiontemporaire) {
        ReceptionTemporaireDTO receptiontemporaireDTO = new ReceptionTemporaireDTO();

        receptiontemporaireDTO.setCodfrs(receptiontemporaire.getCodfrs());
        receptiontemporaireDTO.setMntbon(receptiontemporaire.getMntbon());
        receptiontemporaireDTO.setMemop(receptiontemporaire.getMemop());
        receptiontemporaireDTO.setReffrs(receptiontemporaire.getReffrs());
        receptiontemporaireDTO.setDateFrs(receptiontemporaire.getDatRefFrs().toLocalDate());
        receptiontemporaireDTO.setCoddep(receptiontemporaire.getCoddep());
        receptiontemporaireDTO.setCodAnnul(receptiontemporaire.getCodAnnul());
        receptiontemporaireDTO.setDatAnnul(receptiontemporaire.getDatAnnul());
        receptiontemporaireDTO.setCodePieceJointe(receptiontemporaire.getCodePieceJointe());
        receptiontemporaireDTO.setFournisseurExonere(receptiontemporaire.getFrsExonere());
        //receptiontemporaireDTO.setDateDebutExoneration(receptiontemporaire.getDateDebutExoneration().toLocalDate());
        //receptiontemporaireDTO.setDateFinExoneration(receptiontemporaire.getDateFinExoneration().toLocalDate());
        receptiontemporaireDTO.setNumbon(receptiontemporaire.getNumbon());
        receptiontemporaireDTO.setCodvend(receptiontemporaire.getCodvend());
        receptiontemporaireDTO.setDatbon(receptiontemporaire.getDatbon());
        receptiontemporaireDTO.setDatesys(receptiontemporaire.getDatesys());
        receptiontemporaireDTO.setHeuresys(receptiontemporaire.getHeuresys());
        receptiontemporaireDTO.setTypbon("BAT");
        receptiontemporaireDTO.setNumaffiche(receptiontemporaire.getNumaffiche());
        receptiontemporaireDTO.setCategDepot(receptiontemporaire.getCategDepot());
        receptiontemporaireDTO.setReceivingCode(receptiontemporaire.getcodeReceiving());
        if(receptiontemporaire.getReceivingNumaffiche()!=null)
            receptiontemporaireDTO.setReceivingNumaffiche(receptiontemporaire.getReceivingNumaffiche());
        receptiontemporaireDTO.setMaxDelaiPaiement(receptiontemporaire.getMaxDelaiPaiement());
        if (receptiontemporaire.getDetailReceptionTempraire() != null) {
            List<DetailReceptionTemporaireDTO> details = new ArrayList<>();
            for (DetailReceptionTemporaire detailReceptionTemporaire : receptiontemporaire.getDetailReceptionTempraire()) {
                details.add(DetailReceptionTemporaireFactory.detailreceptiontemporaireToDetailReceptionTemporaireDTO(detailReceptionTemporaire));
            }

            receptiontemporaireDTO.setDetailReceptionTempraireDTO(details);

        }

        return receptiontemporaireDTO;
    }

    public static FactureBA receptiontemporaireDTOToReception(ReceptionTemporaireDTO receptiontemporaireDTO) {
        FactureBA f = new FactureBA();

        // baseBonFactory.toEntity(f, receptiontemporaireDTO);
        f.setRefFrs(receptiontemporaireDTO.getRefFrs());
        f.setFournisseurExonere(receptiontemporaireDTO.getFournisseurExonere());
        f.setDatRefFrs(receptiontemporaireDTO.getDateFrs());
        f.setMntbon(receptiontemporaireDTO.getMntbon());
        f.setNumbon(receptiontemporaireDTO.getNumbon());
        f.setMemop(receptiontemporaireDTO.getMemop());
        f.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());
        f.setTypbon(TypeBonEnum.valueOf(receptiontemporaireDTO.getTypbon()));
        f.setCategDepot(receptiontemporaireDTO.getCategDepot());
        f.setMaxDelaiPaiement(receptiontemporaireDTO.getMaxDelaiPaiement());
        //    f.setListbc(receptiontemporaireDTO.getPurchaseOrdersCodes());
        //    f.setValrem(receptiontemporaireDTO.getValrem());
        //  f.setAutomatique(receptiontemporaireDTO.getAutomatique());
        //  f.setCodeSaisi(receptiontemporaireDTO.getCodeSaisi());

        return f;
    }

    public static FactureBADTO receptiontemporaireDTOTofactureDto(ReceptionTemporaireDTO receptiontemporaireDTO) {
        FactureBADTO f = new FactureBADTO();

        // baseBonFactory.toEntity(f, receptiontemporaireDTO);
        f.setRefFrs(receptiontemporaireDTO.getRefFrs());
        f.setFournisseurExonere(receptiontemporaireDTO.getFournisseurExonere());
        f.setDateFrs(receptiontemporaireDTO.getDateFrs());
        f.setMntBon(receptiontemporaireDTO.getMntbon());
        f.setNumbon(receptiontemporaireDTO.getNumbon());
        f.setMemop(receptiontemporaireDTO.getMemop());
        f.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());
        f.setTypbon("BA");
        f.setCategDepot(receptiontemporaireDTO.getCategDepot());
        f.setMaxDelaiPaiement(receptiontemporaireDTO.getMaxDelaiPaiement());
        //    f.setListbc(receptiontemporaireDTO.getPurchaseOrdersCodes());
        //    f.setValrem(receptiontemporaireDTO.getValrem());
        //  f.setAutomatique(receptiontemporaireDTO.getAutomatique());
        //  f.setCodeSaisi(receptiontemporaireDTO.getCodeSaisi());

        return f;
    }

    /*    
   public static ReceptionTemporaireDTO BonReceptionDTOToReceptiontTemporaire(BonRecepDTO bonRecepDTO) {
    ReceptionTemporaireDTO receptionTemporaireDTO=new ReceptionTemporaireDTO();
    receptionTemporaireDTO.setCodfrs(bonRecepDTO.getCodeFournisseu());
    receptionTemporaireDTO.setMntbon(bonRecepDTO.getMntBon());
    receptionTemporaireDTO.setMemop(bonRecepDTO.getMemop());
    receptionTemporaireDTO.setReffrs(bonRecepDTO.getRefFrs());
    receptionTemporaireDTO.setDatRefFrs(bonRecepDTO.getDateFrs());
    receptionTemporaireDTO.setCoddep(bonRecepDTO.getCoddep());
//    receptiontemporaire.setCodAnnul(bonRecepDTO.getCodAnnul());
    
    receptionTemporaireDTO.setDatAnnul(bonRecepDTO.getDateAnnule());
//    receptiontemporaire.setCodePieceJointe(bonRecepDTO.getCodePieceJointe());
    receptionTemporaireDTO.setFrsExonere(bonRecepDTO.getFournisseurExonere());
  //  receptiontemporaire.setDateDebutExoneration(receptiontemporaireDTO.getDateDebutExoneration().atStartOfDay());
 //   receptiontemporaire.setDateFinExoneration(receptiontemporaireDTO.getDateFinExoneration().atTime(23,59,59));
    receptionTemporaireDTO.setCategDepot(bonRecepDTO.getCategDepot());
    receptionTemporaireDTO.setReceivingCode(bonRecepDTO.getReceivingCode());
     receptionTemporaireDTO.setMaxDelaiPaiement(bonRecepDTO.getMaxDelaiPaiement());
    receptionTemporaireDTO.setTypbon(TypeBonEnum.BAT);
 
   if(receptiontemporaireDTO.getDetailReceptionTempraireDTO()!=null){
      List<DetailReceptionTemporaire> details = new ArrayList<>();
        for (  DetailReceptionTemporaireDTO detailReceptionTemporaireDTO : receptiontemporaireDTO.getDetailReceptionTempraireDTO()) {
              details.add(DetailReceptionTemporaireFactory.detailreceptiontemporaireDTOToDetailReceptionTemporaire(detailReceptionTemporaireDTO));
                  }
       
  
    
    
     receptiontemporaire.setDetailReceptionTempraire(details);}
  
    return receptionTemporaireDTO;
  }*/
    public static ReceptionTemporaireDTO BonReceptionDTOToReceptiontTemporaire(BonRecepDTO dto) {

        ReceptionTemporaireDTO receptionTemporaireDTO = new ReceptionTemporaireDTO();
        receptionTemporaireDTO.setRefFrs(dto.getRefFrs());
        receptionTemporaireDTO.setFournisseurExonere(dto.getFournisseurExonere());
        receptionTemporaireDTO.setDateFrs(dto.getDateFrs());
        receptionTemporaireDTO.setMntbon(dto.getMntBon());
        receptionTemporaireDTO.setNumbon(dto.getNumbon());
        receptionTemporaireDTO.setMemop(dto.getMemop());
        receptionTemporaireDTO.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());
        receptionTemporaireDTO.setCategDepot(dto.getCategDepot());
        receptionTemporaireDTO.setPurchaseOrdersCodes(dto.getPurchaseOrdersCodes());
        receptionTemporaireDTO.setMaxDelaiPaiement(dto.getMaxDelaiPaiement());
        receptionTemporaireDTO.setCategDepot(dto.getCategDepot());
        receptionTemporaireDTO.setCodfrs(dto.getFournisseur().getCode());
        receptionTemporaireDTO.setMntbon(dto.getMntBon());
        receptionTemporaireDTO.setMemop(dto.getMemop());
        receptionTemporaireDTO.setReceivingCode(dto.getReceivingCode());
        receptionTemporaireDTO.setCoddep(dto.getCoddep());
        receptionTemporaireDTO.setTypbon("BAT");
        receptionTemporaireDTO.setMaxDelaiPaiement(dto.getMaxDelaiPaiement());

        //receptionTemporaireDTO.setValrem(dto.getValrem());
        // receptionTemporaireDTO.setAutomatique(dto.getAutomatique());
        List<DetailReceptionTemporaireDTO> details = new ArrayList<>();
        List<Depsto> depstos = new ArrayList<>();

        for (MvtstoBADTO detailsRecep : dto.getDetails()) {
            if (detailsRecep.getQuantite().compareTo(BigDecimal.ZERO) != 0) {

                DetailReceptionTemporaireDTO detailsReceptionTemporaireDTO = new DetailReceptionTemporaireDTO();

                detailsReceptionTemporaireDTO.setTypeBon("BAT");
                detailsReceptionTemporaireDTO.setQuantite(detailsRecep.getQuantite());
                //   detailsReceptionTemporaireDTO.setMemoart(dto.getMemop());
                detailsReceptionTemporaireDTO.setLotInter(detailsRecep.getLotInter());
                detailsReceptionTemporaireDTO.setPriuni(detailsRecep.getPriuni());
                detailsReceptionTemporaireDTO.setRemise(detailsRecep.getRemise());
                detailsReceptionTemporaireDTO.setMontht(detailsRecep.getMontht());
                detailsReceptionTemporaireDTO.setTauTVA(detailsRecep.getTauTVA());
                detailsReceptionTemporaireDTO.setCodeTva(detailsRecep.getCodTVA());;
                if (!receptionTemporaireDTO.getFournisseurExonere()) {
                    detailsReceptionTemporaireDTO.setOldCodtva(5);
                    detailsReceptionTemporaireDTO.setOldCodtva(0);
                } else {
                    detailsReceptionTemporaireDTO.setOldCodtva(detailsRecep.getOldCodTva());
                    detailsReceptionTemporaireDTO.setOldCodtva(detailsRecep.getOldCodTva());
                }
                detailsReceptionTemporaireDTO.setCategDepot(dto.getCategDepot());
                detailsReceptionTemporaireDTO.setCode(dto.getCoddep());
                detailsReceptionTemporaireDTO.setBaseTva(detailsRecep.getBaseTva());

                detailsReceptionTemporaireDTO.setCodeSaisi(detailsRecep.getCodeSaisi());
                detailsReceptionTemporaireDTO.setQteCom(detailsRecep.getQuantite());
                detailsReceptionTemporaireDTO.setIsPrixRef(detailsRecep.getIsPrixRef());
                detailsReceptionTemporaireDTO.setDatPer(detailsRecep.getDatPer());
                detailsReceptionTemporaireDTO.setRefArt((detailsRecep.getRefArt()));
                detailsReceptionTemporaireDTO.setSellingPrice(detailsRecep.getSellingPrice());
                detailsReceptionTemporaireDTO.setDesart(detailsRecep.getDesart());
                detailsReceptionTemporaireDTO.setDesartSec(detailsRecep.getDesartSec());
                detailsReceptionTemporaireDTO.setCodeUnite(0);

                details.add(detailsReceptionTemporaireDTO);

            }

        }
        receptionTemporaireDTO.setDetailReceptionTempraireDTO(details);
        return receptionTemporaireDTO;
    }

    //   
}
