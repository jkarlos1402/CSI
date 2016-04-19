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
@Table(name = "DCS_CAT_INDICADORES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DcsCatIndicadores.findAll", query = "SELECT d FROM DcsCatIndicadores d")})
public class DcsCatIndicadores implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_INDICADOR")
    private Integer idIndicador;
    @Column(name = "NOMBRE_INDICADOR")
    private String nombreIndicador;

    public DcsCatIndicadores() {
    }

    public DcsCatIndicadores(String nombreIndicador) {
        this.nombreIndicador = nombreIndicador;
    }

    public Integer getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(Integer idIndicador) {
        this.idIndicador = idIndicador;
    }

    public String getNombreIndicador() {
        return nombreIndicador;
    }

    public void setNombreIndicador(String nombreIndicador) {
        this.nombreIndicador = nombreIndicador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIndicador != null ? idIndicador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DcsCatIndicadores)) {
            return false;
        }
        DcsCatIndicadores other = (DcsCatIndicadores) object;
        if ((this.nombreIndicador == null && other.nombreIndicador != null) || (this.nombreIndicador != null && !this.nombreIndicador.equals(other.nombreIndicador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.femsa.kof.csi.pojos.DcsCatIndicadores[ idIndicador=" + idIndicador + " ]";
    }

}
