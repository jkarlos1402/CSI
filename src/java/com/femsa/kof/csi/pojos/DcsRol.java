/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.pojos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author TMXIDSJPINAM
 */
@Entity
@Table(name = "DCS_ROL")
@NamedQueries({
    @NamedQuery(name = "DcsRol.findAll", query = "SELECT d FROM DcsRol d")})
public class DcsRol implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PK_ROL")
    private BigDecimal pkRol;
    @Column(name = "ROL")
    private String rol;
    @OneToMany(mappedBy = "fkIdRol")
    private List<DcsUsuario> dcsUsuarioList;

    public DcsRol() {
    }

    public DcsRol(BigDecimal pkRol) {
        this.pkRol = pkRol;
    }

    public BigDecimal getPkRol() {
        return pkRol;
    }

    public void setPkRol(BigDecimal pkRol) {
        this.pkRol = pkRol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<DcsUsuario> getDcsUsuarioList() {
        return dcsUsuarioList;
    }

    public void setDcsUsuarioList(List<DcsUsuario> dcsUsuarioList) {
        this.dcsUsuarioList = dcsUsuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pkRol != null ? pkRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DcsRol)) {
            return false;
        }
        DcsRol other = (DcsRol) object;
        if ((this.pkRol == null && other.pkRol != null) || (this.pkRol != null && !this.pkRol.equals(other.pkRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.femsa.kof.csi.pojos.DcsRol[ pkRol=" + pkRol + " ]";
    }
    
}
