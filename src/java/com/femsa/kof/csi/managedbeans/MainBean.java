package com.femsa.kof.csi.managedbeans;

import com.femsa.kof.csi.dao.GenericDAO;
import com.femsa.kof.csi.exception.DAOException;
import com.femsa.kof.csi.exception.DataBaseException;
import com.femsa.kof.csi.pojos.DcsUsuario;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author TMXIDSJPINAM
 */
@ManagedBean(name = "mainBean")
@SessionScoped
public class MainBean implements Serializable {

    private String page = "/WEB-INF/pages/blank.xhtml";
    private String catalog = "";
    private DcsUsuario usuario;
    private List<String> notifications;
    private String password;
    private boolean firstSession;
    private Integer porcentajeAvance = 0;
    private Long numRegistrosProcesados = 0L;
    private Long numRegistrosTotales = 0L;

    public Long getNumRegistrosTotales() {
        return numRegistrosTotales;
    }

    public void setNumRegistrosTotales(Long numRegistrosTotales) {
        this.numRegistrosTotales = numRegistrosTotales;
    }

    public Integer getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(Integer porcentajeAvance) {
        if (porcentajeAvance != null && porcentajeAvance > 100) {
            porcentajeAvance = 100;
        }
        this.porcentajeAvance = porcentajeAvance;
    }

    public Long getNumRegistrosProcesados() {
        return numRegistrosProcesados;
    }

    public void setNumRegistrosProcesados(Long numRegistrosProcesados) {
        this.numRegistrosProcesados = numRegistrosProcesados;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public boolean isFirstSession() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        firstSession = (Boolean) session.getAttribute("first_session_user");
        return firstSession;
    }

    /**
     *
     * @param firstSession
     */
    public void setFirstSession(boolean firstSession) {
        this.firstSession = firstSession;
    }

    /**
     *
     * @return
     */
    public List<String> getNotifications() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        notifications = (List<String>) session.getAttribute("notifications_user");
        return notifications;
    }

    /**
     *
     * @param notifications
     */
    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    /**
     *
     * @return
     */
    public DcsUsuario getUsuario() {
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        usuario = (DcsUsuario) httpSession.getAttribute("session_user");
        return usuario;
    }

    /**
     *
     * @param usuario
     */
    public void setUsuario(DcsUsuario usuario) {
        this.usuario = usuario;
    }

    /**
     *
     * @return
     */
    public String getPage() {
        return page;
    }

    /**
     *
     * @param page
     * @param catalog
     * @param proyecto
     */
    public void setPage(String page, String catalog, String proyecto) {
        if (!"".equalsIgnoreCase(proyecto)) {
            this.page = "/WEB-INF/pages/" + proyecto + "/" + page + ".xhtml";
            this.catalog = catalog;
        } else {
            this.page = "/WEB-INF/pages/" + page + ".xhtml";
        }
    }

    /**
     *
     * @return
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     *
     * @param catalog
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     *
     */
    public void saveUser() {
        FacesMessage message = null;
        GenericDAO genericDAO = null;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.setAttribute("first_session_user", false);
        try {
            genericDAO = new GenericDAO();
            firstSession = false;
            usuario.setPassreset(false);
            genericDAO.saveOrUpdate(usuario);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "Information saved");
        } catch (DataBaseException e) {
            Logger.getLogger(MainBean.class.getName()).log(Level.SEVERE, null, e);
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "There was a error while saving the information, " + e.getMessage());
        } catch (DAOException ex) {
            Logger.getLogger(MainBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "There was a error while saving the information, " + ex.getMessage());
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }        
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     *
     */
    public void changeUserPassword() {
        usuario.setPassword(password);
        saveUser();
    }   
}
