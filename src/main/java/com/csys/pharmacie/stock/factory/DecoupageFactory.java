package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.stock.domain.Decoupage;
import com.csys.pharmacie.stock.dto.DecoupageDTO;
import com.csys.pharmacie.stock.dto.DecoupageEditionDTO;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DecoupageFactory {

    public static DecoupageDTO decoupageToDecoupageDTO(Decoupage decoupage) {
        DecoupageDTO decoupageDTO = new DecoupageDTO();
        decoupageDTO.setNumbon(decoupage.getNumbon());
        decoupageDTO.setDatbon(decoupage.getDatbon());
        decoupageDTO.setCategDepot(decoupage.getCategDepot());
        decoupageDTO.setCodvend(decoupage.getCodvend());
        decoupageDTO.setTypbon(decoupage.getTypbon());
        decoupageDTO.setCoddep(decoupage.getCoddep());
        decoupageDTO.setMemop(decoupage.getMemo());
        decoupageDTO.setNumaffiche(decoupage.getNumaffiche());
        decoupageDTO.setAuto(decoupage.isAuto());
        return decoupageDTO;
    }

    public static DecoupageEditionDTO decoupageToDecoupageEditionDTO(Decoupage decoupage) {
        DecoupageEditionDTO decoupageDTO = new DecoupageEditionDTO();
        decoupageDTO.setNumbon(decoupage.getNumbon());
        decoupageDTO.setDatbon(Date.from(decoupage.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        decoupageDTO.setCategDepot(decoupage.getCategDepot());
        decoupageDTO.setCodvend(decoupage.getCodvend());
        decoupageDTO.setTypbon(decoupage.getTypbon());
        decoupageDTO.setCoddep(decoupage.getCoddep());
        decoupageDTO.setNumaffiche(decoupage.getNumaffiche());
        decoupageDTO.setAuto(decoupage.isAuto());
        return decoupageDTO;
    }

    public static Decoupage decoupageDTOToDecoupage(DecoupageDTO decoupageDTO) {
        Decoupage decoupage = new Decoupage();
        decoupage.setNumbon(decoupageDTO.getNumbon());
        decoupage.setCategDepot(decoupageDTO.getCategDepot());
        decoupage.setCoddep(decoupageDTO.getCoddep());
        decoupage.setTypbon(decoupageDTO.getTypbon());
        decoupage.setMemo(decoupageDTO.getMemop());
        return decoupage;
    }

    public static Collection<DecoupageDTO> decoupageToDecoupageDTOs(Collection<Decoupage> decoupages) {
        List<DecoupageDTO> decoupagesDTO = new ArrayList<>();
        decoupages.forEach(x -> {
            decoupagesDTO.add(decoupageToDecoupageDTO(x));
        });
        return decoupagesDTO;
    }
}
