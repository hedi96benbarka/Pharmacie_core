package com.csys.pharmacie.prelevement.factory;

import com.csys.pharmacie.prelevement.domain.TraceDetailRetourPr;
import com.csys.pharmacie.prelevement.dto.TraceDetailRetourPrDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TraceDetailRetourPrFactory {
  public static TraceDetailRetourPrDTO tracedetailretourprToTraceDetailRetourPrDTO(TraceDetailRetourPr tracedetailretourpr) {
    TraceDetailRetourPrDTO tracedetailretourprDTO=new TraceDetailRetourPrDTO();
    tracedetailretourprDTO.setTraceDetailRetourPrPK(tracedetailretourpr.getTraceDetailRetourPrPK());
    tracedetailretourprDTO.setCodeMvtstopr(tracedetailretourpr.getCodeMvtstopr());
    tracedetailretourprDTO.setQuantite(tracedetailretourpr.getQuantite());
    return tracedetailretourprDTO;
  }

  public static TraceDetailRetourPr tracedetailretourprDTOToTraceDetailRetourPr(TraceDetailRetourPrDTO tracedetailretourprDTO) {
    TraceDetailRetourPr tracedetailretourpr=new TraceDetailRetourPr();
    tracedetailretourpr.setTraceDetailRetourPrPK(tracedetailretourprDTO.getTraceDetailRetourPrPK());
    tracedetailretourpr.setCodeMvtstopr(tracedetailretourprDTO.getCodeMvtstopr());
    tracedetailretourpr.setQuantite(tracedetailretourprDTO.getQuantite());
    return tracedetailretourpr;
  }

  public static Collection<TraceDetailRetourPrDTO> tracedetailretourprToTraceDetailRetourPrDTOs(Collection<TraceDetailRetourPr> tracedetailretourprs) {
    List<TraceDetailRetourPrDTO> tracedetailretourprsDTO=new ArrayList<>();
    tracedetailretourprs.forEach(x -> {
      tracedetailretourprsDTO.add(tracedetailretourprToTraceDetailRetourPrDTO(x));
    } );
    return tracedetailretourprsDTO;
  }
}

