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
@Table(name = "XTMPINDDL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Xtmpinddl.findAll", query = "SELECT x FROM Xtmpinddl x")})
public class Xtmpinddl implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCS_SEQ_XTMPINDDL")
    @SequenceGenerator(name = "DCS_SEQ_XTMPINDDL", sequenceName = "DCS_SEQ_XTMPINDDL", allocationSize = 1)    
    @Column(name = "ID_XTMPINDDL")
    private Integer idXtmpinddl;
    @Column(name = "FECHA")
    private String fecha;
    @Column(name = "GRUPO_IND")
    private String grupoInd;
    @Column(name = "INDICADOR")
    private String indicador;
    @Column(name = "PAIS")
    private String pais;
    @Column(name = "CENTRO")
    private String centro;
    @Column(name = "RUTA")
    private String ruta;
    @Column(name = "VALOR_MENSUAL")
    private Float valorMensual;
    @Column(name = "VALOR_ACUMULADO")
    private Float valorAcumulado;
    @Column(name = "VALOR_UNIDAD")
    private String valorUnidad;    

    public Xtmpinddl() {
    }

    public Xtmpinddl(Integer idXtmpinddl) {
        this.idXtmpinddl = idXtmpinddl;
    }

    public Integer getIdXtmpinddl() {
        return idXtmpinddl;
    }

    public void setIdXtmpinddl(Integer idXtmpinddl) {
        this.idXtmpinddl = idXtmpinddl;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getGrupoInd() {
        return grupoInd;
    }

    public void setGrupoInd(String grupoInd) {
        this.grupoInd = grupoInd;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Float getValorMensual() {
        return valorMensual;
    }

    public void setValorMensual(Float valorMensual) {
        this.valorMensual = valorMensual;
    }

    public Float getValorAcumulado() {
        return valorAcumulado;
    }

    public void setValorAcumulado(Float valorAcumulado) {
        this.valorAcumulado = valorAcumulado;
    }

    public String getValorUnidad() {
        return valorUnidad;
    }

    public void setValorUnidad(String valorUnidad) {
        this.valorUnidad = valorUnidad;
    }   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idXtmpinddl != null ? idXtmpinddl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Xtmpinddl)) {
            return false;
        }
        Xtmpinddl other = (Xtmpinddl) object;
        if ((this.idXtmpinddl == null && other.idXtmpinddl != null) || (this.idXtmpinddl != null && !this.idXtmpinddl.equals(other.idXtmpinddl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.femsa.kof.csi.pojos.XtmpinddlTmp[ idXtmpinddl=" + idXtmpinddl + " ]";
    }
    
}
