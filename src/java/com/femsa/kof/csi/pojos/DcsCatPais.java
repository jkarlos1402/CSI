/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.pojos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author TMXIDSJPINAM
 */
@Entity
@Table(name = "DCS_CAT_PAIS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DcsCatPais.findAll", query = "SELECT d FROM DcsCatPais d")})
public class DcsCatPais implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PAIS")
    private Integer idPais;
    @Column(name = "PAIS")
    private String pais;

    public DcsCatPais() {
        // TO DO
    }

    public DcsCatPais(String pais) {
        this.pais = pais;
    }

    public Integer getIdPais() {
        return idPais;
    }

    public void setIdPais(Integer idPais) {
        this.idPais = idPais;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPais != null ? idPais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DcsCatPais)) {
            return false;
        }
        DcsCatPais other = (DcsCatPais) object;
        if ((this.pais == null && other.pais != null) || (this.pais != null && !this.pais.equalsIgnoreCase(other.pais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return pais;
    }

}
