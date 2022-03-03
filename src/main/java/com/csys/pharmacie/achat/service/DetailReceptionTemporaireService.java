package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.DetailReceptionTemporaire;
import com.csys.pharmacie.achat.dto.DetailReceptionTemporaireDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.factory.DetailReceptionTemporaireFactory;
import com.csys.pharmacie.achat.repository.DetailReceptionTemporaireRepository;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DetailReceptionTemporaireService {

    private final Logger log = LoggerFactory.getLogger(DetailReceptionTemporaireService.class);

    private final DetailReceptionTemporaireRepository detailreceptiontemporaireRepository;
      private final ParamAchatServiceClient paramAchatServiceClient;

    public DetailReceptionTemporaireService(DetailReceptionTemporaireRepository detailreceptiontemporaireRepository, ParamAchatServiceClient paramAchatServiceClient) {
        this.detailreceptiontemporaireRepository = detailreceptiontemporaireRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
    }


    public DetailReceptionTemporaireDTO save(DetailReceptionTemporaireDTO detailreceptiontemporaireDTO) {
        log.debug("Request to save DetailReceptionTemporaire: {}", detailreceptiontemporaireDTO);
        DetailReceptionTemporaire detailreceptiontemporaire = DetailReceptionTemporaireFactory.detailReceptionTemporaireDTOToDetailReceptionTemporaire(detailreceptiontemporaireDTO);
        detailreceptiontemporaire = detailreceptiontemporaireRepository.save(detailreceptiontemporaire);
        DetailReceptionTemporaireDTO resultDTO = DetailReceptionTemporaireFactory.detailreceptiontemporaireToDetailReceptionTemporaireDTO(detailreceptiontemporaire);
        return resultDTO;
    }

    public DetailReceptionTemporaireDTO update(DetailReceptionTemporaireDTO detailreceptiontemporaireDTO) {
        log.debug("Request to update DetailReceptionTemporaire: {}", detailreceptiontemporaireDTO);
        DetailReceptionTemporaire inBase = detailreceptiontemporaireRepository.findOne(detailreceptiontemporaireDTO.getCode());
        Preconditions.checkArgument(inBase != null, "DetailReceptionTemporaire does not exist");
        DetailReceptionTemporaireDTO result = save(detailreceptiontemporaireDTO);
        return result;
    }

    @Transactional(
            readOnly = true
    )
    public DetailReceptionTemporaireDTO findOne(Integer id) {
        log.debug("Request to get DetailReceptionTemporaire: {}", id);
        DetailReceptionTemporaire detailreceptiontemporaire = detailreceptiontemporaireRepository.findOne(id);
        Preconditions.checkArgument(detailreceptiontemporaire != null, "DetailReceptionTemporaire does not exist");
        DetailReceptionTemporaireDTO dto = DetailReceptionTemporaireFactory.detailreceptiontemporaireToDetailReceptionTemporaireDTO(detailreceptiontemporaire);
        return dto;
    }

    @Transactional(
            readOnly = true
    )
    public DetailReceptionTemporaire findDetailReceptionTemporaire(Integer id) {
        log.debug("Request to get DetailReceptionTemporaire: {}", id);
        DetailReceptionTemporaire detailreceptiontemporaire = detailreceptiontemporaireRepository.findOne(id);
        Preconditions.checkArgument(detailreceptiontemporaire != null, "DetailReceptionTemporaire does not exist");
        return detailreceptiontemporaire;
    }

    @Transactional(
            readOnly = true
    )
    public Collection<DetailReceptionTemporaireDTO> findAll() {
        log.debug("Request to get All DetailReceptionTemporaires");
        Collection<DetailReceptionTemporaire> result = detailreceptiontemporaireRepository.findAll();
        return DetailReceptionTemporaireFactory.detailreceptiontemporaireToDetailReceptionTemporaireDTOs(result);
    }

    public void delete(Integer id) {
        log.debug("Request to delete DetailReceptionTemporaire: {}", id);
        detailreceptiontemporaireRepository.delete(id);
    }
    
    
       
           public List <DetailReceptionTemporaireDTO> findByNumBon(String numBon ) {
        log.debug("Request to get DetailReceptionTemporaire: {}", numBon);
         List<DetailReceptionTemporaireDTO> detailreceptiontemporaireDTO=new ArrayList<DetailReceptionTemporaireDTO>();
      List<DetailReceptionTemporaire> detailreceptiontemporaire = detailreceptiontemporaireRepository.findByReception_numbon(numBon);
       Preconditions.checkArgument(detailreceptiontemporaire != null, "DetailReceptionTemporaire does not exist");

        for(DetailReceptionTemporaire detail:detailreceptiontemporaire){
        DetailReceptionTemporaireDTO dto = DetailReceptionTemporaireFactory.detailreceptiontemporaireToDetailReceptionTemporaireDTO(detail);
        detailreceptiontemporaireDTO.add(dto);
     
    }
          List<Integer> codeUnites = new ArrayList();
          
          for(DetailReceptionTemporaireDTO detailDto :detailreceptiontemporaireDTO){
            
                     log.debug("detail.getUnite(): {}", detailDto.getCodeUnite());
                  codeUnites.add(detailDto.getCodeUnite());
           
          /*  UniteDTO unite=paramAchatServiceClient.findUniteByCode(detailDto.getCodeUnite());
                log.debug("detail.getUnite(): {}", unite);
                    log.debug("unite get DetailReceptionTemporaire: {}", unite.toString());
                      log.debug("unite get DetailReceptionTemporaire: {}", unite.getDesignation());
               detailDto.setUnitDesignation(unite.getDesignation());*/
          }
            List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
            
                 for(DetailReceptionTemporaireDTO detailDto :detailreceptiontemporaireDTO){
                UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(detailDto.getCodeUnite())).findFirst().orElse(null);
                      Preconditions.checkArgument(unite != null, "missing-unity");
    
            detailDto.setUnitDesignation(unite.getDesignation());
            }
           
          
          
           return detailreceptiontemporaireDTO;

}
}
