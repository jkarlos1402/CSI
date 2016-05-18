package com.femsa.kof.off.managedbeans;

import com.femsa.kof.csi.dao.GenericDAO;
import com.femsa.kof.csi.exception.DAOException;
import com.femsa.kof.csi.exception.DataBaseException;
import com.femsa.kof.csi.pojos.DcsCatPais;
import com.femsa.kof.off.pojos.OffNotas;
import com.femsa.kof.off.pojos.OffPais;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author TMXIDSJPINAM
 */
@ManagedBean
@ViewScoped
public class NotasBean {

    private OffNotas notaNueva;
    private OffNotas notaSeleccionada;
    private List<OffNotas> listNotas;
    private List<OffPais> listPaises;
    private OffPais paisSelected;

    /**
     * Creates a new instance of AlertasBean
     */
    public NotasBean() {
        refreshNotas();
    }

    public void openDialogNota(String nombreDialog, int tipoOperacion) {
        FacesMessage message = null;
        switch (tipoOperacion) {
            case 1://editar
                if (notaSeleccionada != null) {
                    RequestContext.getCurrentInstance().execute("PF('" + nombreDialog + "').show()");
                } else {
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sorry", "Not element selected");
                }
                if (message != null) {
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                break;
            case 2://nuevo
                notaSeleccionada = null;
                paisSelected = null;
                notaNueva = new OffNotas();
                break;
            case 3://ver
                if (notaSeleccionada != null) {
                    RequestContext.getCurrentInstance().execute("PF('" + nombreDialog + "').show()");
                } else {
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sorry", "Not element selected");
                }
                if (message != null) {
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                break;
        }
    }

    public void refreshNotas() {
        FacesMessage message = null;
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            listNotas = genericDAO.findAll(OffNotas.class);
            listPaises = genericDAO.findAll(OffPais.class);
        } catch (DataBaseException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage());
            Logger.getLogger(NotasBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DAOException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage());
            Logger.getLogger(NotasBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
        if (message != null) {
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public Serializable copiaNotaNuevaANotaSeleccionada() {
        Field[] campos = notaNueva.getClass().getDeclaredFields();
        for (Field campo : campos) {
            try {
                if ((Modifier.PRIVATE + Modifier.STATIC + Modifier.FINAL) != campo.getModifiers()) {
                    campo.setAccessible(true);
                    campo.set(notaSeleccionada, campo.get(notaNueva));
                }
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(NotasBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(NotasBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return notaSeleccionada;
    }

    public void guardaNota() {
        FacesMessage message;
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            notaNueva.setPais(paisSelected.getClave());
            genericDAO.saveOrUpdate(notaSeleccionada != null ? copiaNotaNuevaANotaSeleccionada() : notaNueva);
            RequestContext.getCurrentInstance().execute("PF('dialogFormOrderNotas').hide()");
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "The record has been saved");
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(NotasBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage());
        } catch (DataBaseException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage());
            Logger.getLogger(NotasBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DAOException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage());
            Logger.getLogger(NotasBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            refreshNotas();
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void selectNota() {        
        if (notaNueva == null) {
            notaNueva = new OffNotas();
        }
        Field[] campos = notaNueva.getClass().getDeclaredFields();        
        for (Field campo : campos) {           
            try {
                if ((Modifier.PRIVATE + Modifier.STATIC + Modifier.FINAL) != campo.getModifiers()) {
                    campo.setAccessible(true);
                    campo.set(notaNueva, campo.get(notaSeleccionada));                    
                }
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(NotasBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(NotasBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        paisSelected = listPaises.get(listPaises.indexOf(new OffPais(notaSeleccionada.getPais())));
    }

    public void cerroDialog() {
        RequestContext.getCurrentInstance().reset("formOrderNotas:panelFormOrderNotas");
    }

    public void eliminaNota() {
        FacesMessage message;
        GenericDAO genericDAO = null;
        if (notaSeleccionada != null) {
            try {
                genericDAO = new GenericDAO();
                genericDAO.delete(notaSeleccionada);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "The record has been deleted");
            } catch (DAOException ex) {
                Logger.getLogger(NotasBean.class.getName()).log(Level.SEVERE, null, ex);
                message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage());
            } catch (DataBaseException ex) {
                Logger.getLogger(NotasBean.class.getName()).log(Level.SEVERE, null, ex);
                message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage());
            } finally {
                refreshNotas();
                if (genericDAO != null) {
                    genericDAO.closeDAO();
                }
            }
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sorry", "Not element selected");
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public List<OffPais> getListPaises() {
        return listPaises;
    }

    public OffPais getPaisSelected() {
        return paisSelected;
    }

    public void setPaisSelected(OffPais paisSelected) {
        this.paisSelected = paisSelected;
    }

    public void setListPaises(List<OffPais> listPaises) {
        this.listPaises = listPaises;
    }

    public OffNotas getNotaNueva() {
        return notaNueva;
    }

    public void setNotaNueva(OffNotas notaNueva) {
        this.notaNueva = notaNueva;
    }

    public OffNotas getNotaSeleccionada() {
        return notaSeleccionada;
    }

    public void setNotaSeleccionada(OffNotas notaSeleccionada) {
        this.notaSeleccionada = notaSeleccionada;
    }

    public List<OffNotas> getListNotas() {
        return listNotas;
    }

    public void setListNotas(List<OffNotas> listNotas) {
        this.listNotas = listNotas;
    }

}
