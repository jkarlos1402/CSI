/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.pojos;

import com.femsa.kof.csi.util.EncrypterKOF;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author TMXIDSJPINAM
 */
@Entity
@Table(name = "DCS_USUARIO")
@NamedQueries({
    @NamedQuery(name = "DcsUsuario.findAll", query = "SELECT d FROM DcsUsuario d")})
public class DcsUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "PK_USUARIO")
    private Integer pkUsuario;
    @Column(name = "USUARIO")
    private String usuario;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "PAIS")
    private String pais;
    @Column(name = "MAIL")
    private String mail;
    @Column(name = "ESTATUS")
    private boolean estatus;
    @Column(name = "INTENTOS")
    private Integer intentos;
    @Column(name = "LASTLOGIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastlogin;
    @Column(name = "PASSRESET")
    private boolean passreset;
    @JoinTable(name = "DCS_USUARIO_PROYECTO", joinColumns = {
        @JoinColumn(name = "PK_USUARIO", referencedColumnName = "PK_USUARIO")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_PROYECTO", referencedColumnName = "ID_PROYECTO")})
    @ManyToMany
    private List<DcsCatProyecto> dcsCatProyectoList;
    @JoinColumn(name = "FK_ID_ROL", referencedColumnName = "PK_ROL")
    @ManyToOne
    private DcsRol fkIdRol;    

    public Integer getPkUsuario() {
        return pkUsuario;
    }

    public void setPkUsuario(Integer pkUsuario) {
        this.pkUsuario = pkUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        EncrypterKOF encrypterKOF = new EncrypterKOF();
        return encrypterKOF.decrypt(password);
    }

    public void setPassword(String password) {
        EncrypterKOF encrypterKOF = new EncrypterKOF();
        this.password = encrypterKOF.encrypt(password);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public Date getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }

    public boolean getPassreset() {
        return passreset;
    }

    public void setPassreset(boolean passreset) {
        this.passreset = passreset;
    }

    public List<DcsCatProyecto> getDcsCatProyectoList() {
        return dcsCatProyectoList;
    }

    public void setDcsCatProyectoList(List<DcsCatProyecto> dcsCatProyectoList) {
        this.dcsCatProyectoList = dcsCatProyectoList;
    }

    public DcsRol getFkIdRol() {
        return fkIdRol;
    }

    public void setFkIdRol(DcsRol fkIdRol) {
        this.fkIdRol = fkIdRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pkUsuario != null ? pkUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DcsUsuario)) {
            return false;
        }
        DcsUsuario other = (DcsUsuario) object;
        if ((this.pkUsuario == null && other.pkUsuario != null) || (this.pkUsuario != null && !this.pkUsuario.equals(other.pkUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
