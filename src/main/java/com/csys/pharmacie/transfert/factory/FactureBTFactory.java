package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.helper.SatisfactionEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.transfert.domain.FactureBT;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;
import com.csys.pharmacie.transfert.dto.FactureBTEditionDTO;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class FactureBTFactory {

    public static FactureBTDTO factureBTToFactureBTDTO(FactureBT factureBT, FactureBTDTO factureBTDTO) {

        factureBTDTO.setCode(factureBT.getNumbon());
        factureBTDTO.setSourceID(factureBT.getCoddep());
        factureBTDTO.setDestinationID(factureBT.getDeptr());
        factureBTDTO.setUserCreate(factureBT.getCodvend());
        factureBTDTO.setNumaffiche(factureBT.getNumaffiche());
        factureBTDTO.setDateCreate(factureBT.getDatbon());
        factureBTDTO.setCategDepot(factureBT.getCategDepot());
        factureBTDTO.setInterdpot(factureBT.getInterdepot());
        factureBTDTO.setAvoirTransfert(factureBT.isAvoirTransfert());
        factureBTDTO.setSatisf(factureBT.getSatisf());
        factureBTDTO.setMemop(factureBT.getMemop());
        factureBTDTO.setMntbon(factureBT.getMntbon());
        factureBTDTO.setAutomatique(factureBT.getAutomatique());
        factureBTDTO.setUserAnnule(factureBT.getCodAnnul());
        factureBTDTO.setDateAnnule(factureBT.getDatAnnul());
        factureBTDTO.setMemoValidate(factureBT.getMemoValidate());
        factureBTDTO.setDateValidate(factureBT.getDateValidate());
        factureBTDTO.setUserValidate(factureBT.getUserValidate());
        factureBTDTO.setValide(factureBT.getValide());
        factureBTDTO.setConforme(factureBT.getConforme());
        return factureBTDTO;
    }

    public static FactureBTEditionDTO factureBTToFactureBTEditionDTO(FactureBT factureBT) {
        FactureBTEditionDTO factureBTEditionDTO = new FactureBTEditionDTO();
        factureBTEditionDTO.setCode(factureBT.getNumbon());
        factureBTEditionDTO.setSourceID(factureBT.getCoddep());
        factureBTEditionDTO.setDestinationID(factureBT.getDeptr());
        factureBTEditionDTO.setInterdpot(factureBT.getInterdepot());
        factureBTEditionDTO.setUserCreate(factureBT.getCodvend());
        factureBTEditionDTO.setNumaffiche(factureBT.getNumaffiche());
        LocalDateTime dateCreate = factureBT.getDatbon();
        factureBTEditionDTO.setDateCreate(Date.from(dateCreate.atZone(ZoneId.systemDefault()).toInstant()));
        factureBTEditionDTO.setCategDepot(factureBT.getCategDepot());
        factureBTEditionDTO.setAvoirTransfert(factureBT.isAvoirTransfert());
        factureBTEditionDTO.setSatisf(factureBT.getSatisf());
        factureBTEditionDTO.setMemop(factureBT.getMemop());
        factureBTEditionDTO.setMntbon(factureBT.getMntbon());
        factureBTEditionDTO.setPerime(factureBT.getPerime());
        factureBTEditionDTO.setValide(factureBT.getValide());
        if(factureBTEditionDTO.getValide())
        { factureBTEditionDTO.setDateValidate(Date.from(factureBT.getDateValidate().atZone(ZoneId.systemDefault()).toInstant()));
        factureBTEditionDTO.setUserValidate(factureBT.getUserValidate());
        }

        return factureBTEditionDTO;
    }

    public static FactureBTDTO factureBTToFactureBTDTOLazy(FactureBT factureBT) {
        FactureBTDTO factureBTDTO = new FactureBTDTO();
        factureBTDTO.setCode(factureBT.getNumbon());
        factureBTDTO.setSourceID(factureBT.getCoddep());
        factureBTDTO.setDestinationID(factureBT.getDeptr());
        factureBTDTO.setNumaffiche(factureBT.getNumaffiche());
        factureBTDTO.setUserCreate(factureBT.getCodvend());
        factureBTDTO.setDateCreate(factureBT.getDatbon());
        factureBTDTO.setCategDepot(factureBT.getCategDepot());
        factureBTDTO.setInterdpot(factureBT.getInterdepot());
        factureBTDTO.setAvoirTransfert(factureBT.isAvoirTransfert());
        factureBTDTO.setSatisf(factureBT.getSatisf());
        factureBTDTO.setMemop(factureBT.getMemop());
        factureBTDTO.setAutomatique(factureBT.getAutomatique());
        factureBTDTO.setUserAnnule(factureBT.getCodAnnul());
        factureBTDTO.setDateAnnule(factureBT.getDatAnnul());
        factureBTDTO.setMntbon(factureBT.getMntbon());
        return factureBTDTO;
    }

    public static FactureBT factureBTDTOToFactureBT(FactureBTDTO factureBTDTO) {
        FactureBT factureBT = new FactureBT();
        factureBT.setMntbon(factureBTDTO.getMntbon());
        factureBT.setMemop(factureBTDTO.getMemop());
        factureBT.setCoddep(factureBTDTO.getSourceID());
        factureBT.setDeptr(factureBTDTO.getDestinationID());
        factureBT.setNumbon(factureBTDTO.getCode());
        factureBT.setCategDepot(factureBTDTO.getCategDepot());
        factureBT.setInterdepot(factureBTDTO.isInterdpot());
        factureBT.setSatisf(SatisfactionEnum.NOT_RECOVRED);
        factureBT.setAvoirTransfert(factureBTDTO.isAvoirTransfert());
        factureBT.setDateValidate(factureBTDTO.getDateValidate());
        factureBT.setUserValidate(factureBTDTO.getUserValidate());
        factureBT.setAutomatique(Boolean.FALSE);
        factureBT.setTypbon(TypeBonEnum.BT);
        factureBT.setPerime(factureBTDTO.getPerime());
        return factureBT;
    }

    public static FactureBT factureToFactureBT(FactureBA factureBA, DepotDTO depots, DepotDTO depotd) {
        FactureBT factureBT = new FactureBT();
        factureBT.setMemop(factureBA.getMemop());
        factureBT.setCoddep(depots.getCode());
        factureBT.setDeptr(depotd.getCode());
        factureBT.setCategDepot(factureBA.getCategDepot());
        factureBT.setInterdepot(Boolean.TRUE);
        factureBT.setSatisf(SatisfactionEnum.RECOVRED);
        factureBT.setAvoirTransfert(Boolean.FALSE);
        factureBT.setPerime(Boolean.FALSE);
        factureBT.setAutomatique(Boolean.TRUE);
        factureBT.setTypbon(TypeBonEnum.BT);
        factureBT.setConforme(Boolean.TRUE);
        factureBT.setValide(Boolean.TRUE);
        factureBT.setUserValidate(factureBA.getCodvend());
        factureBT.setDateValidate(LocalDateTime.now());
        return factureBT;
    }

}
