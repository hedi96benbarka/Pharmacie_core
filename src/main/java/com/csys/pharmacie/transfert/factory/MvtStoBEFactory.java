package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.achat.domain.DetailMvtStoRetourPerime;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.transfert.domain.DetailMvtStoBE;
import com.csys.pharmacie.transfert.domain.MvtStoBE;
import com.csys.pharmacie.transfert.dto.MvtStoBEDTO;
import com.csys.pharmacie.transfert.dto.MvtStoBEEditionDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MvtStoBEFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }


    @Autowired
    MessageSource messages;
    
    public static MvtStoBEDTO mvtstobeToMvtStoBEDTO(MvtStoBE mvtstobe) {
        BigDecimal sum=BigDecimal.ZERO;
        MvtStoBEDTO mvtstobeDTO = new MvtStoBEDTO();
        mvtstobeDTO.setCodArt(mvtstobe.getCodart());
        mvtstobeDTO.setNumBon(mvtstobe.getNumbon());
        mvtstobeDTO.setLotInter(mvtstobe.getLotinter());
        mvtstobeDTO.setPriuni(mvtstobe.getPriuni());
        mvtstobeDTO.setDatPer(mvtstobe.getDatPer());
        mvtstobeDTO.setCategDepot(mvtstobe.getCategDepot());
        mvtstobeDTO.setCodtva(mvtstobe.getCodtva());
        mvtstobeDTO.setTautva(mvtstobe.getTautva());

        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstobeDTO.setDesart(mvtstobe.getDesArtSec());
            mvtstobeDTO.setDesArtSec(mvtstobe.getDesart());

        } else {
            mvtstobeDTO.setDesart(mvtstobe.getDesart());
            mvtstobeDTO.setDesArtSec(mvtstobe.getDesArtSec());

        }

        mvtstobeDTO.setDesart(mvtstobe.getDesart());
        mvtstobeDTO.setDesArtSec(mvtstobe.getDesArtSec());
        mvtstobeDTO.setCodeSaisi(mvtstobe.getCodeSaisi());
        mvtstobeDTO.setQuantite(mvtstobe.getQuantite());
        mvtstobeDTO.setCodeUnite(mvtstobe.getUnite());
        //mvtstobe.getDetailMvtStoBEList().stream().forEach(System.out::println);
//        mvtstobe.getDetailMvtStoBEList().stream().forEach(detail->{
           // s=s.add(detail.getPriuni().multiply(detail.getTauxTva()));
            //to test
        if(mvtstobe.getQuantite().compareTo(BigDecimal.ZERO)<0){
            for(DetailMvtStoBE detail:mvtstobe.getDetailMvtStoBEList()){
                if(detail.getTauxTva().compareTo(BigDecimal.ZERO)>0){
                    sum=sum.add(detail.getPriuni().multiply(detail.getQuantitePrelevee()).multiply(BigDecimal.valueOf(1).add(detail.getTauxTva().divide(BigDecimal.valueOf(100)))));
                }
                else{
                    sum=sum.add(detail.getPriuni().multiply(detail.getQuantitePrelevee()));
                }


            }
            System.out.println("somme=" + sum);

//            sum = mvtstobe.getDetailMvtStoBEList().stream().filter(x->x.getTauxTva().compareTo(BigDecimal.ZERO)>0)
//                    .map(x ->x.getPriuni().multiply(x.getQuantitePrelevee()).multiply(BigDecimal.valueOf(1).add(x.getTauxTva().divide(BigDecimal.valueOf(100)))))    // map
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);      // reduce

            //to test
            // BigDecimal sum = invoices.stream()
            //                .map(x -> x.getQty().multiply(x.getPrice()))    // map
            //                .reduce(BigDecimal.ZERO, BigDecimal::add);      // reduce
            //System.out.println("somme=" + sum);
            mvtstobeDTO.setPrixMoyenTtc(sum.divide(mvtstobeDTO.getQuantite()).multiply(BigDecimal.valueOf(-1)));
            System.out.println(mvtstobeDTO.getPrixMoyenTtc());

        }
        if(mvtstobe.getQuantite().compareTo(BigDecimal.ZERO)>0){

                if(mvtstobe.getTautva().compareTo(BigDecimal.ZERO)>0){
                    mvtstobeDTO.setPrixMoyenTtc(mvtstobe.getPriuni().multiply(mvtstobe.getQuantite()).multiply(BigDecimal.valueOf(1).add(mvtstobe.getTautva().divide(BigDecimal.valueOf(100)))));
                }
                else{
                    mvtstobeDTO.setPrixMoyenTtc(mvtstobe.getPriuni().multiply(mvtstobe.getQuantite()));
                }

            System.out.println("montant pos"+mvtstobeDTO.getPrixMoyenTtc());
            }


//            sum = mvtstobe.getDetailMvtStoBEList().stream().filter(x->x.getTauxTva().compareTo(BigDecimal.ZERO)>0)
//                    .map(x ->x.getPriuni().multiply(x.getQuantitePrelevee()).multiply(BigDecimal.valueOf(1).add(x.getTauxTva().divide(BigDecimal.valueOf(100)))))    // map
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);      // reduce

            //to test
            // BigDecimal sum = invoices.stream()
            //                .map(x -> x.getQty().multiply(x.getPrice()))    // map
            //                .reduce(BigDecimal.ZERO, BigDecimal::add);      // reduce
            //System.out.println("somme=" + sum);





      /* if(mvtstobeDTO.getQuantite().compareTo(BigDecimal.ZERO) < 0) {
           //selen slayma all detailmvsto on le meme prix a verifier avec sirine
           mvtstobeDTO.setPriuni_depsto(mvtstobe.getDetailMvtStoBEList().get(0).getPriuni());
       }else {
           mvtstobeDTO.setPriuni_depsto(mvtstobe.getPriuni());
       }*/
        return mvtstobeDTO;
    }
    
    public static MvtStoBEEditionDTO mvtstobeToMvtStoBEEditionDTO(MvtStoBE mvtstobe) {
        MvtStoBEEditionDTO mvtstobeDTO = new MvtStoBEEditionDTO();
        mvtstobeDTO.setCodArt(mvtstobe.getCodart());
        mvtstobeDTO.setNumBon(mvtstobe.getNumbon());
        mvtstobeDTO.setLotInter(mvtstobe.getLotinter());
        mvtstobeDTO.setPriuni(mvtstobe.getPriuni());
        mvtstobeDTO.setDatPer(Date.from(mvtstobe.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        mvtstobeDTO.setCategDepot(mvtstobe.getCategDepot());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstobeDTO.setDesart(mvtstobe.getDesArtSec());
            mvtstobeDTO.setDesArtSec(mvtstobe.getDesart());
        } else {
            mvtstobeDTO.setDesart(mvtstobe.getDesart());
            mvtstobeDTO.setDesArtSec(mvtstobe.getDesArtSec());
        }
        mvtstobeDTO.setCodeSaisi(mvtstobe.getCodeSaisi());
        mvtstobeDTO.setQuantite(mvtstobe.getQuantite());
        mvtstobeDTO.setCodeUnite(mvtstobe.getUnite());
        return mvtstobeDTO;
    }
    
    public static MvtStoBE mvtstobeDTOToMvtStoBE(MvtStoBEDTO mvtstobeDTO) {
        MvtStoBE mvtstobe = new MvtStoBE();
        mvtstobe.setCodart(mvtstobeDTO.getCodArt());
        mvtstobe.setLotinter(mvtstobeDTO.getLotInter());
        mvtstobe.setPriuni(mvtstobeDTO.getPriuni());
        mvtstobe.setDatPer(mvtstobeDTO.getDatPer());
        mvtstobe.setCategDepot(mvtstobeDTO.getCategDepot());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstobe.setDesart(mvtstobeDTO.getDesArtSec());
            mvtstobe.setDesArtSec(mvtstobeDTO.getDesart());
            
        } else {
            mvtstobe.setDesart(mvtstobeDTO.getDesart());
            mvtstobe.setDesArtSec(mvtstobeDTO.getDesArtSec());
        }
        mvtstobe.setCodtva(mvtstobeDTO.getCodtva());
        mvtstobe.setTautva(mvtstobeDTO.getTautva());
        
        mvtstobe.setCodeSaisi(mvtstobeDTO.getCodeSaisi());
        mvtstobe.setQuantite(mvtstobeDTO.getQuantite());
        mvtstobe.setUnite(mvtstobeDTO.getCodeUnite());
        return mvtstobe;
    }
    
    public static List<MvtStoBEDTO> mvtstobeToMvtStoBEDTOs(List<MvtStoBE> mvtstobes) {
        List<MvtStoBEDTO> mvtstobesDTO = new ArrayList<>();
        mvtstobes.forEach(x -> {
            System.out.println("affichage  "+ x);
            mvtstobesDTO.add(mvtstobeToMvtStoBEDTO(x));
        });
        return mvtstobesDTO;
    }
    
    public List<Mouvement> toMouvement(MvtStoBE entity, UniteDTO unite, DepotDTO codedep, TypeDateEnum typeDate) {
        if (entity == null) {
            return null;
        }
        Locale loc = LocaleContextHolder.getLocale();
        List<Mouvement> listMouvement = new ArrayList<>();
        Mouvement dto = new Mouvement();
        dto.setDate(Date.from(entity.getFactureBE().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesignation(entity.getDesArtSec());
        } else {
            dto.setDesignation(entity.getDesart());
        }
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setOperation(messages.getMessage("ficheStock.redressement", null, loc));
        dto.setNumaffiche(entity.getFactureBE().getNumaffiche());
        dto.setNumbon(entity.getFactureBE().getNumbon());
        dto.setLibelle(codedep.getDesignation() + " " + codedep.getCodeSaisi());
        dto.setCoddep(entity.getFactureBE().getCoddep());
        
        if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO) || (entity.getQuantite().compareTo(BigDecimal.ZERO) > 0 && typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL))) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            if (entity.getDatPer() != null) {
                dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            dtoEntreSortie.setLotinter(entity.getLotinter());
            dtoEntreSortie.setNumbon(entity.getFactureBE().getNumbon());
            if (entity.getQuantite().compareTo(BigDecimal.ZERO) > 0) {
                dtoEntreSortie.setEntree(entity.getQuantite());
                dtoEntreSortie.setSortie(BigDecimal.ZERO);
            } else {
                dtoEntreSortie.setEntree(BigDecimal.ZERO);
                dtoEntreSortie.setSortie(entity.getQuantite().negate());
            }
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            dtoEntreSortie.setNumbon(entity.getFactureBE().getNumbon());
            if (entity.getQuantite().compareTo(BigDecimal.ZERO) > 0) {
                dtoEntreSortie.setEntree(entity.getQuantite());
                dtoEntreSortie.setSortie(BigDecimal.ZERO);
            } else {
                dtoEntreSortie.setEntree(BigDecimal.ZERO);
                dtoEntreSortie.setSortie(entity.getQuantite().negate());
            }
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            for (DetailMvtStoBE detailMvtStoBE : entity.getDetailMvtStoBEList()) {
                EntreSortie dtoEntreSortie = new EntreSortie();
                if (detailMvtStoBE.getDepsto().getDatPer() != null) {
                    dtoEntreSortie.setDatPer(Date.from(detailMvtStoBE.getDepsto().getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                dtoEntreSortie.setLotinter(detailMvtStoBE.getDepsto().getLotInter());
                dtoEntreSortie.setNumbon(entity.getFactureBE().getNumbon());
                if (entity.getQuantite().compareTo(BigDecimal.ZERO) > 0) {
                    dtoEntreSortie.setEntree(detailMvtStoBE.getQuantitePrelevee());
                    dtoEntreSortie.setSortie(BigDecimal.ZERO);
                } else {
                    dtoEntreSortie.setEntree(BigDecimal.ZERO);
                    dtoEntreSortie.setSortie(detailMvtStoBE.getQuantitePrelevee());
                }
                dtoEntreSortie.setPrix(entity.getPriuni());
                dtoEntreSortie.setCodeUnite(entity.getUnite());
                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                dtoEntreSorties.add(dtoEntreSortie);
            }
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        }
        return listMouvement;
        
    }
}
