package com.csys.pharmacie.client.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ArticleNonMvtDTO {

    @NotNull
    private Integer codart;

    private TypeMvtEnum typeMvt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastDateMvt;

    @NotNull
    private Boolean nonMoved;

    @Size(min = 0, max = 4)
    private CategorieDepotEnum categDepot;

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public TypeMvtEnum getTypeMvt() {
        return typeMvt;
    }

    public void setTypeMvt(TypeMvtEnum typeMvt) {
        this.typeMvt = typeMvt;
    }

    public Date getLastDateMvt() {
        return lastDateMvt;
    }

    public void setLastDateMvt(Date lastDateMvt) {
        this.lastDateMvt = lastDateMvt;
    }

    public boolean isNonMoved() {
        return nonMoved;
    }

    public void setNonMoved(Boolean nonMoved) {
        this.nonMoved = nonMoved;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    @Override
    public String toString() {
        return "{" + "codart=" + codart + ", typeMvt=" + typeMvt + ", lastDateMvt=" + lastDateMvt + ", nonMoved=" + nonMoved + ", categDepot=" + categDepot + '}';
    }



}
