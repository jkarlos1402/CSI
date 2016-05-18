/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.off.pojos;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "OFF_NOTAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OffNotas.findAll", query = "SELECT o FROM OffNotas o")})
public class OffNotas implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OFF_SEQ_NOTAS")
    @SequenceGenerator(name = "OFF_SEQ_NOTAS", sequenceName = "OFF_SEQ_NOTAS", allocationSize = 1)
    @Column(name = "IDNOTA")
    private Integer idnota;
    @Column(name = "MES")
    private String mes;
    @Column(name = "PAIS")
    private String pais;
    @Column(name = "NOTA")
    private String nota;

    public OffNotas() {
    }

    public OffNotas(Integer idnota) {
        this.idnota = idnota;
    }

    public Integer getIdnota() {
        return idnota;
    }

    public void setIdnota(Integer idnota) {
        this.idnota = idnota;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes != null ? mes.toUpperCase() : mes;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais != null ? pais.toUpperCase() : pais;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota != null ? nota.toUpperCase() : nota;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idnota != null ? idnota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OffNotas)) {
            return false;
        }
        OffNotas other = (OffNotas) object;
        if ((this.idnota == null && other.idnota != null) || (this.idnota != null && !this.idnota.equals(other.idnota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nota;
    }

}
