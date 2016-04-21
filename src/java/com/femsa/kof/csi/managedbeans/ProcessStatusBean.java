/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.managedbeans;

import com.femsa.kof.csi.dao.GenericDAO;
import com.femsa.kof.csi.exception.DAOException;
import com.femsa.kof.csi.exception.DataBaseException;
import com.femsa.kof.csi.pojos.DcsLoadLog;
import com.femsa.kof.csi.pojos.DcsUsuario;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author TMXIDSJPINAM
 */
@ManagedBean
@ViewScoped
public class ProcessStatusBean {

    private List<DcsLoadLog> listaCargas;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("hh:mm:ss a");
    private final DcsUsuario usuario;

    /**
     * Creates a new instance of ProcessStatusBean
     */
    public ProcessStatusBean() {
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        usuario = (DcsUsuario) httpSession.getAttribute("session_user");
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            if ("Administrator".equalsIgnoreCase(usuario.getFkIdRol().getRol())) {
                listaCargas = genericDAO.findAll(DcsLoadLog.class);
            } else {
                listaCargas = genericDAO.findByComponent(DcsLoadLog.class, "idUsuario", usuario);
            }
        } catch (DataBaseException ex) {
            Logger.getLogger(ProcessStatusBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DAOException ex) {
            Logger.getLogger(ProcessStatusBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
    }

    public SimpleDateFormat getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(SimpleDateFormat dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public DcsUsuario getUsuario() {
        return usuario;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public List<DcsLoadLog> getListaCargas() {
        return listaCargas;
    }

    public void setListaCargas(List<DcsLoadLog> listaCargas) {
        this.listaCargas = listaCargas;
    }

    public void refreshListLog() {
        FacesMessage message = null;
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            if ("Administrator".equalsIgnoreCase(usuario.getFkIdRol().getRol())) {
                listaCargas = genericDAO.findAll(DcsLoadLog.class);
            } else {
                listaCargas = genericDAO.findByComponent(DcsLoadLog.class, "idUsuario", usuario);
            }
        } catch (DataBaseException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alert", ex.getMessage());
            Logger.getLogger(ProcessStatusBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DAOException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alert", ex.getMessage());
            Logger.getLogger(ProcessStatusBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
        if (message != null) {
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}
