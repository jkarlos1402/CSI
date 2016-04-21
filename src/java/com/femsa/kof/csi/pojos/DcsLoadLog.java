/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.pojos;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author TMXIDSJPINAM
 */
@Entity
@Table(name = "DCS_LOAD_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DcsLoadLog.findAll", query = "SELECT d FROM DcsLoadLog d")})
public class DcsLoadLog implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCS_SEQ_LOAD_LOG")
    @SequenceGenerator(name = "DCS_SEQ_LOAD_LOG", sequenceName = "DCS_SEQ_LOAD_LOG", allocationSize = 1)
    @Column(name = "ID_LOG")
    private Integer idLog;
    @Column(name = "NOMBRE_PROCESO")
    private String nombreProceso;
    @Column(name = "NOMBRE_ARCHIVO")
    private String nombreArchivo;
    @Column(name = "NOMBRE_PROYECTO")
    private String nombreProyecto;
    @Column(name = "FECHA_EJECUCION")
    @Temporal(TemporalType.DATE)
    private Date fechaEjecucion;
    @Column(name = "INICIO_EJECUCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicioEjecucion;
    @Column(name = "FIN_EJECUCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finEjecucion;
    @Column(name = "REGISTROS_PROCESADOS")
    private Integer registrosProcesados;
    @Column(name = "PAIS")
    private String pais;
    @Column(name = "ESTATUS")
    private String estatus;    
    @JoinColumn(name = "ID_USUARIO")
    @ManyToOne(fetch = FetchType.EAGER)
    private DcsUsuario idUsuario;

    public DcsLoadLog() {
    }

    public DcsLoadLog(Integer idLog) {
        this.idLog = idLog;
    }

    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }

    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public Date getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(Date fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }

    public Date getInicioEjecucion() {
        return inicioEjecucion;
    }

    public void setInicioEjecucion(Date inicioEjecucion) {
        this.inicioEjecucion = inicioEjecucion;
    }

    public Date getFinEjecucion() {
        return finEjecucion;
    }

    public void setFinEjecucion(Date finEjecucion) {
        this.finEjecucion = finEjecucion;
    }

    public Integer getRegistrosProcesados() {
        return registrosProcesados;
    }

    public void setRegistrosProcesados(Integer registrosProcesados) {
        this.registrosProcesados = registrosProcesados;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public DcsUsuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(DcsUsuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLog != null ? idLog.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DcsLoadLog)) {
            return false;
        }
        DcsLoadLog other = (DcsLoadLog) object;
        if ((this.idLog == null && other.idLog != null) || (this.idLog != null && !this.idLog.equals(other.idLog))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.femsa.kof.csi.pojos.DcsLoadLog[ idLog=" + idLog + " ]";
    }
    
}
