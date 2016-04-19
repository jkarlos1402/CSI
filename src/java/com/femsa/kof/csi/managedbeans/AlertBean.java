/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.managedbeans;

import com.femsa.kof.csi.dao.GenericDAO;
import com.femsa.kof.csi.exception.DAOException;
import com.femsa.kof.csi.exception.DataBaseException;
import com.femsa.kof.csi.pojos.DcsAlerta;
import com.femsa.kof.csi.pojos.DcsUsuario;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author TMXIDSJPINAM
 */
@ManagedBean
@ViewScoped
public class AlertBean implements Serializable {

    private List<DcsUsuario> usuariosAll;
    private List<DcsAlerta> alertasAll;
    private DcsAlerta alertaNueva = new DcsAlerta();
    private DcsAlerta alertaSeleccionada;

    private String[] numDiasMes;

    public AlertBean() {
        GenericDAO genericDAO = null;
        DecimalFormat formateador = new DecimalFormat("00");
        try {
            genericDAO = new GenericDAO();
            usuariosAll = genericDAO.findAll(DcsUsuario.class);            
            numDiasMes = new String[Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)];
            for (int i = 1; i <= numDiasMes.length; i++) {
                numDiasMes[i-1]= formateador.format(i);
            }
            refreshAlertas();
        } catch (DataBaseException ex) {
            Logger.getLogger(AlertBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DAOException ex) {
            Logger.getLogger(AlertBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
    }

    public String[] getNumDiasMes() {
        return numDiasMes;
    }

    public void setNumDiasMes(String[] numDiasMes) {
        this.numDiasMes = numDiasMes;
    }

    public List<DcsUsuario> getUsuariosAll() {
        return usuariosAll;
    }

    public void setUsuariosAll(List<DcsUsuario> usuariosAll) {
        this.usuariosAll = usuariosAll;
    }

    public List<DcsAlerta> getAlertasAll() {
        return alertasAll;
    }

    public void setAlertasAll(List<DcsAlerta> alertasAll) {
        this.alertasAll = alertasAll;
    }

    public DcsAlerta getAlertaNueva() {
        return alertaNueva;
    }

    public void setAlertaNueva(DcsAlerta alertaNueva) {
        this.alertaNueva = alertaNueva;
    }

    public DcsAlerta getAlertaSeleccionada() {
        return alertaSeleccionada;
    }

    public void setAlertaSeleccionada(DcsAlerta alertaSeleccionada) {
        this.alertaSeleccionada = alertaSeleccionada;
    }

    public void saveAlerta() {
        FacesMessage message = null;
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            genericDAO.saveOrUpdate(alertaNueva);
            refreshAlertas();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "Alert saved");

        } catch (DataBaseException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "There was a error while saving the alert, " + ex.getMessage());
            alertaNueva.setPkAlerta(null);
        } catch (DAOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "There was a error while saving the alert, " + ex.getMessage());
            alertaNueva.setPkAlerta(null);
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void refreshAlertas() {
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            alertasAll = genericDAO.findAll(DcsAlerta.class);
        } catch (DataBaseException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DAOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
    }

    public void newAlerta() {
        alertaNueva = new DcsAlerta();
        alertaSeleccionada = null;
    }

    public void selectAlerta() {
        alertaNueva.setIdUsuario(alertaSeleccionada.getIdUsuario());
        alertaNueva.setJefe1Correo(alertaSeleccionada.getJefe1Correo());
        alertaNueva.setJefe1Nombre(alertaSeleccionada.getJefe1Nombre());
        alertaNueva.setJefe2Correo(alertaSeleccionada.getJefe2Correo());
        alertaNueva.setJefe2Nombre(alertaSeleccionada.getJefe2Nombre());
        alertaNueva.setJefe3Correo(alertaSeleccionada.getJefe3Correo());
        alertaNueva.setJefe3Nombre(alertaSeleccionada.getJefe3Nombre());
        alertaNueva.setMensaje(alertaSeleccionada.getMensaje());
        alertaNueva.setPeriodo(alertaSeleccionada.getPeriodo());
        alertaNueva.setPkAlerta(alertaSeleccionada.getPkAlerta());
    }

    public void deleteAlerta() {
        FacesMessage message;
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            genericDAO.delete(alertaNueva);
            refreshAlertas();
            newAlerta();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "Alert deleted");
        } catch (DataBaseException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "There was a error while deleting the alert, " + ex.getMessage());
        } catch (DAOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "There was a error while deleting the alert, " + ex.getMessage());
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
