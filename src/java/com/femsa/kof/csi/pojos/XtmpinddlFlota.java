/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.pojos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author TMXIDSJPINAM
 */
@Entity
@Table(name = "XTMPINDDL_FLOTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XtmpinddlFlota.findAll", query = "SELECT x FROM XtmpinddlFlota x")})
public class XtmpinddlFlota implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCS_SEQ_XTMPINDDL_FLOTA")
    @SequenceGenerator(name = "DCS_SEQ_XTMPINDDL_FLOTA", sequenceName = "DCS_SEQ_XTMPINDDL_FLOTA", allocationSize = 1)
    @Column(name = "ID_XTMPINDDL_FLOTA")
    private Integer idXtmpinddlFlota;
    @Column(name = "ANIO")
    private Integer anio;
    @Column(name = "PAIS")
    private String pais;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "EDAD")
    private String edad;
    @Column(name = "CANTIDAD")
    private Integer cantidad;

    public Integer getIdXtmpinddlFlota() {
        return idXtmpinddlFlota;
    }

    public void setIdXtmpinddlFlota(Integer idXtmpinddlFlota) {
        this.idXtmpinddlFlota = idXtmpinddlFlota;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idXtmpinddlFlota != null ? idXtmpinddlFlota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XtmpinddlFlota)) {
            return false;
        }
        XtmpinddlFlota other = (XtmpinddlFlota) object;
        if ((this.idXtmpinddlFlota == null && other.idXtmpinddlFlota != null) || (this.idXtmpinddlFlota != null && !this.idXtmpinddlFlota.equals(other.idXtmpinddlFlota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.femsa.kof.csi.pojos.XtmpinddlFlota[ idXtmpinddlFlota=" + idXtmpinddlFlota + " ]";
    }
    
}
