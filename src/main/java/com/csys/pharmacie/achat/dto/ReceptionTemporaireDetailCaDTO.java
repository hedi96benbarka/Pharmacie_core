package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.achat.domain.ReceptionTemporaire;
import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCaPK;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class ReceptionTemporaireDetailCaDTO {
  private ReceptionTemporaireDetailCaPK receptionTemporaireDetailCaPK;

  @NotNull
  private BigDecimal quantiteReceptione;

  private BigDecimal quantiteGratuite;

  private ReceptionTemporaire receptionTemporaire1;

  public ReceptionTemporaireDetailCaPK getReceptionTemporaireDetailCaPK() {
    return receptionTemporaireDetailCaPK;
  }

  public void setReceptionTemporaireDetailCaPK(ReceptionTemporaireDetailCaPK receptionTemporaireDetailCaPK) {
    this.receptionTemporaireDetailCaPK = receptionTemporaireDetailCaPK;
  }

  public BigDecimal getQuantiteReceptione() {
    return quantiteReceptione;
  }

  public void setQuantiteReceptione(BigDecimal quantiteReceptione) {
    this.quantiteReceptione = quantiteReceptione;
  }

  public BigDecimal getQuantiteGratuite() {
    return quantiteGratuite;
  }

  public void setQuantiteGratuite(BigDecimal quantiteGratuite) {
    this.quantiteGratuite = quantiteGratuite;
  }

  public ReceptionTemporaire getReceptionTemporaire1() {
    return receptionTemporaire1;
  }

  public void setReceptionTemporaire1(ReceptionTemporaire receptionTemporaire1) {
    this.receptionTemporaire1 = receptionTemporaire1;
  }
}

