package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.DetailReceptionTemporaire;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.MvtStoBAPK;
import com.csys.pharmacie.achat.dto.DetailReceptionTemporaireDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DetailReceptionTemporaireFactory {

    public static DetailReceptionTemporaireDTO detailreceptiontemporaireToDetailReceptionTemporaireDTO(DetailReceptionTemporaire detailreceptiontemporaire) {
        DetailReceptionTemporaireDTO detailreceptiontemporaireDTO = new DetailReceptionTemporaireDTO();
        detailreceptiontemporaireDTO.setQteCom(detailreceptiontemporaire.getQuantiteCommande());
        detailreceptiontemporaireDTO.setRemise(detailreceptiontemporaire.getRemise());
        detailreceptiontemporaireDTO.setMontht(detailreceptiontemporaire.getMontht());
        detailreceptiontemporaireDTO.setTauTVA(detailreceptiontemporaire.getTautva());
        detailreceptiontemporaireDTO.setCodeTva(detailreceptiontemporaire.getCodtva());
        detailreceptiontemporaireDTO.setIsPrixRef(detailreceptiontemporaire.getIsPrixReference());
        detailreceptiontemporaireDTO.setSellingPrice(detailreceptiontemporaire.getSellingPrice());
        detailreceptiontemporaireDTO.setBaseTva(detailreceptiontemporaire.getBaseTva());
        detailreceptiontemporaireDTO.setPmpPrecedant(detailreceptiontemporaire.getPmpPrecedant());
        detailreceptiontemporaireDTO.setQuantitePrecedante(detailreceptiontemporaire.getQuantitePrecedante());
        detailreceptiontemporaireDTO.setOldCodtva(detailreceptiontemporaire.getOldCodtva());
        detailreceptiontemporaireDTO.setOldTautva(detailreceptiontemporaire.getOldTautva());
        detailreceptiontemporaireDTO.setCodeEmplacement(detailreceptiontemporaire.getCodeEmplacement());
        detailreceptiontemporaireDTO.setCategDepot(detailreceptiontemporaire.getCategDepot());
        detailreceptiontemporaireDTO.setDesart(detailreceptiontemporaire.getDesart());
        detailreceptiontemporaireDTO.setDesartSec(detailreceptiontemporaire.getDesArtSec());
        detailreceptiontemporaireDTO.setCodeSaisi(detailreceptiontemporaire.getCodeSaisi());
        detailreceptiontemporaireDTO.setQuantite(detailreceptiontemporaire.getQuantite());
        detailreceptiontemporaireDTO.setCode(detailreceptiontemporaire.getCode());
        detailreceptiontemporaireDTO.setRefArt(detailreceptiontemporaire.getCodart());
        detailreceptiontemporaireDTO.setNumbon(detailreceptiontemporaire.getNumbon());
        detailreceptiontemporaireDTO.setLotInter(detailreceptiontemporaire.getLotinter());
        detailreceptiontemporaireDTO.setDatPer(detailreceptiontemporaire.getDatPer());
        detailreceptiontemporaireDTO.setPriuni(detailreceptiontemporaire.getPriuni());
        detailreceptiontemporaireDTO.setUnite(detailreceptiontemporaire.getUnite());

        
        if (detailreceptiontemporaireDTO.getPriuni().compareTo(BigDecimal.ZERO) == 0) {
            detailreceptiontemporaireDTO.setFree(true);
        } else if (detailreceptiontemporaireDTO.getPriuni().compareTo(BigDecimal.ZERO) == 1) {
            detailreceptiontemporaireDTO.setFree(false);
        }
        return detailreceptiontemporaireDTO;
    }

    public static DetailReceptionTemporaire detailReceptionTemporaireDTOToDetailReceptionTemporaire(DetailReceptionTemporaireDTO detailreceptiontemporaireDTO) {
        DetailReceptionTemporaire detailReceptionTemporaire = new DetailReceptionTemporaire();
        detailReceptionTemporaire.setQtecom(detailreceptiontemporaireDTO.getQteCom());
        detailReceptionTemporaire.setRemise(detailreceptiontemporaireDTO.getRemise());
        detailReceptionTemporaire.setMontht(detailreceptiontemporaireDTO.getMontht());
        detailReceptionTemporaire.setTautva(detailreceptiontemporaireDTO.getTauTVA());
        detailReceptionTemporaire.setCodtva(detailreceptiontemporaireDTO.getCodeTva());
        detailReceptionTemporaire.setIsPrixReference(detailreceptiontemporaireDTO.getIsPrixRef());
        detailReceptionTemporaire.setSellingPrice(detailreceptiontemporaireDTO.getSellingPrice());
        detailReceptionTemporaire.setBaseTva(detailreceptiontemporaireDTO.getBaseTva());
        detailReceptionTemporaire.setPmpPrecedant(detailreceptiontemporaireDTO.getPmpPrecedant());
        detailReceptionTemporaire.setQuantitePrecedante(detailreceptiontemporaireDTO.getQuantitePrecedante());
        detailReceptionTemporaire.setOldCodtva(detailreceptiontemporaireDTO.getOldCodtva());
        detailReceptionTemporaire.setOldTautva(detailreceptiontemporaireDTO.getOldTautva());
        detailReceptionTemporaire.setCodeEmplacement(detailreceptiontemporaireDTO.getCodeEmplacement());
        detailReceptionTemporaire.setCategDepot(detailreceptiontemporaireDTO.getCategDepot());
        detailReceptionTemporaire.setDesart(detailreceptiontemporaireDTO.getDesart());
        detailReceptionTemporaire.setDesArtSec(detailreceptiontemporaireDTO.getDesartSec());
        detailReceptionTemporaire.setCodeSaisi(detailreceptiontemporaireDTO.getCodeSaisi());
        detailReceptionTemporaire.setQuantite(detailreceptiontemporaireDTO.getQuantite());
        detailReceptionTemporaire.setCode(detailreceptiontemporaireDTO.getCode());
        detailReceptionTemporaire.setCodart(detailreceptiontemporaireDTO.getRefArt());
        detailReceptionTemporaire.setNumbon(detailreceptiontemporaireDTO.getNumbon());
        detailReceptionTemporaire.setLotinter(detailreceptiontemporaireDTO.getLotInter());
        detailReceptionTemporaire.setDatPer(detailreceptiontemporaireDTO.getDatPer());
        detailReceptionTemporaire.setPriuni(detailreceptiontemporaireDTO.getPriuni());
        detailReceptionTemporaire.setUnite(detailreceptiontemporaireDTO.getUnite());
        detailReceptionTemporaire.setLotinter(detailreceptiontemporaireDTO.getLotInter());

        return detailReceptionTemporaire;
    }

    public static Collection<DetailReceptionTemporaireDTO> detailreceptiontemporaireToDetailReceptionTemporaireDTOs(Collection<DetailReceptionTemporaire> detailreceptiontemporaires) {
        List<DetailReceptionTemporaireDTO> detailreceptiontemporairesDTO = new ArrayList<>();
        detailreceptiontemporaires.forEach(x -> {
            detailreceptiontemporairesDTO.add(detailreceptiontemporaireToDetailReceptionTemporaireDTO(x));
        });
        return detailreceptiontemporairesDTO;
    }

    public static MvtStoBA detailReceptionTemporaireDTOToMvtstoBa(DetailReceptionTemporaireDTO detailReceptionTemporaireDTO ,String ordre) {
        MvtStoBA mvtstoBA = new MvtStoBA();

        MvtStoBAPK pk = new MvtStoBAPK();
        pk.setCodart(detailReceptionTemporaireDTO.getRefArt());
        pk.setNumbon(detailReceptionTemporaireDTO.getNumbon());
        pk.setNumordre(ordre);
        mvtstoBA.setPk(pk);
        mvtstoBA.setTypbon("BA");
        mvtstoBA.setQuantite(detailReceptionTemporaireDTO.getQuantite());
        //      mvtstoBA.setMemoart(detailreceptiontemporaireDTO.getMemop());
        mvtstoBA.setLotInter(detailReceptionTemporaireDTO.getLotInter());
        mvtstoBA.setPriuni(detailReceptionTemporaireDTO.getPriuni());
        mvtstoBA.setRemise(detailReceptionTemporaireDTO.getRemise());
        mvtstoBA.setMontht(detailReceptionTemporaireDTO.getMontht());
        mvtstoBA.setTautva(detailReceptionTemporaireDTO.getTauTVA());
        mvtstoBA.setCodtva(detailReceptionTemporaireDTO.getCodeTva());
        mvtstoBA.setCategDepot(detailReceptionTemporaireDTO.getCategDepot());
        //  mvtstoBA.setCoddep(detailreceptiontemporaireDTO.getCoddep());
        mvtstoBA.setBaseTva(detailReceptionTemporaireDTO.getBaseTva());

        return mvtstoBA;
    }

}
