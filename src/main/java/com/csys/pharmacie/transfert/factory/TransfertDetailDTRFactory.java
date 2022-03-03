package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.transfert.domain.TransfertDetailDTR;
import com.csys.pharmacie.transfert.domain.TransfertDetailDTRPK;
import com.csys.pharmacie.transfert.dto.TransfertDetailDTRDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransfertDetailDTRFactory {
  public static TransfertDetailDTRDTO transfertdetaildtrToTransfertDetailDTRDTO(TransfertDetailDTR transfertdetaildtr) {
    TransfertDetailDTRDTO transfertdetaildtrDTO=new TransfertDetailDTRDTO();
    
     transfertdetaildtrDTO.setCodedetailDTR(transfertdetaildtr.getPk().getCodedetailDTR());
transfertdetaildtrDTO.setCodeTransfert(transfertdetaildtr.getPk().getCodeTransfert());

    transfertdetaildtrDTO.setCodeDTR(transfertdetaildtr.getCodeDTR());
    transfertdetaildtrDTO.setQteTransferred(transfertdetaildtr.getQuantiteTransferred());
    transfertdetaildtrDTO.setReception(transfertdetaildtr.getFacturebt());
    return transfertdetaildtrDTO;
  }

  public static TransfertDetailDTR transfertdetaildtrDTOToTransfertDetailDTR(TransfertDetailDTRDTO transfertdetaildtrDTO) {
    TransfertDetailDTR transfertdetaildtr=new TransfertDetailDTR();
    TransfertDetailDTRPK pk= new  TransfertDetailDTRPK(transfertdetaildtrDTO.getCodedetailDTR(),transfertdetaildtrDTO.getCodeTransfert());
      transfertdetaildtr.setPk(pk);
    transfertdetaildtr.setCodeDTR(transfertdetaildtrDTO.getCodeDTR());
    transfertdetaildtr.setQuantiteTransferred(transfertdetaildtrDTO.getQteTransferred());
    transfertdetaildtr.setFacturebt(transfertdetaildtrDTO.getReception());
   
    return transfertdetaildtr;
  }

  public static Collection<TransfertDetailDTRDTO> transfertdetaildtrToTransfertDetailDTRDTOs(Collection<TransfertDetailDTR> transfertdetaildtrs) {
    List<TransfertDetailDTRDTO> transfertdetaildtrsDTO=new ArrayList<>();
    transfertdetaildtrs.forEach(x -> {
      transfertdetaildtrsDTO.add(transfertdetaildtrToTransfertDetailDTRDTO(x));
    } );
    return transfertdetaildtrsDTO;
  }
}
       
