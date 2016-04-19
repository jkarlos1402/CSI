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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author TMXIDSJPINAM
 */
@Entity
@Table(name = "DCS_ALERTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DcsAlerta.findAll", query = "SELECT d FROM DcsAlerta d")})
public class DcsAlerta implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ALERTA")
    @SequenceGenerator(name = "SEQ_ALERTA", sequenceName = "SEQ_ALERTA", allocationSize = 1)
    @Column(name = "PK_ALERTA")
    private Integer pkAlerta;
    @JoinColumn(name = "ID_USUARIO")
    @ManyToOne(fetch = FetchType.EAGER)
    private DcsUsuario idUsuario;
    @Column(name = "JEFE1_NOMBRE")
    private String jefe1Nombre;
    @Column(name = "JEFE1_CORREO")
    private String jefe1Correo;
    @Column(name = "JEFE2_NOMBRE")
    private String jefe2Nombre;
    @Column(name = "JEFE2_CORREO")
    private String jefe2Correo;
    @Column(name = "JEFE3_NOMBRE")
    private String jefe3Nombre;
    @Column(name = "JEFE3_CORREO")
    private String jefe3Correo;
    @Column(name = "PERIODO")
    private String periodo;
    @Column(name = "MENSAJE")
    private String mensaje;

    public Integer getPkAlerta() {
        return pkAlerta;
    }

    public void setPkAlerta(Integer pkAlerta) {
        this.pkAlerta = pkAlerta;
    }

    public DcsUsuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(DcsUsuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getJefe1Nombre() {
        return jefe1Nombre;
    }

    public void setJefe1Nombre(String jefe1Nombre) {
        this.jefe1Nombre = jefe1Nombre;
    }

    public String getJefe1Correo() {
        return jefe1Correo;
    }

    public void setJefe1Correo(String jefe1Correo) {
        this.jefe1Correo = jefe1Correo;
    }

    public String getJefe2Nombre() {
        return jefe2Nombre;
    }

    public void setJefe2Nombre(String jefe2Nombre) {
        this.jefe2Nombre = jefe2Nombre;
    }

    public String getJefe2Correo() {
        return jefe2Correo;
    }

    public void setJefe2Correo(String jefe2Correo) {
        this.jefe2Correo = jefe2Correo;
    }

    public String getJefe3Nombre() {
        return jefe3Nombre;
    }

    public void setJefe3Nombre(String jefe3Nombre) {
        this.jefe3Nombre = jefe3Nombre;
    }

    public String getJefe3Correo() {
        return jefe3Correo;
    }

    public void setJefe3Correo(String jefe3Correo) {
        this.jefe3Correo = jefe3Correo;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pkAlerta != null ? pkAlerta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DcsAlerta)) {
            return false;
        }
        DcsAlerta other = (DcsAlerta) object;
        if ((this.pkAlerta == null && other.pkAlerta != null) || (this.pkAlerta != null && !this.pkAlerta.equals(other.pkAlerta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.femsa.kof.csi.pojos.DcsAlerta[ pkAlerta=" + pkAlerta + " ]";
    }

}
