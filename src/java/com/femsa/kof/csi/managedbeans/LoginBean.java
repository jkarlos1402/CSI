package com.femsa.kof.csi.managedbeans;

import com.femsa.kof.csi.dao.GenericDAO;
import com.femsa.kof.csi.exception.DAOException;
import com.femsa.kof.csi.exception.DataBaseException;
import com.femsa.kof.csi.pojos.DcsCatPais;
import com.femsa.kof.csi.pojos.DcsCatProyecto;
import com.femsa.kof.csi.pojos.DcsRol;
import com.femsa.kof.csi.pojos.DcsUsuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author TMXIDSJPINAM
 */
@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean implements Serializable {

    private String user;
    private String password;  

    private String error;
    private static final String MSG_ERROR_TITULO = "Mensaje de error...";    

    /**
     *
     * @return
     */
    public String getError() {
        return error;
    }

    /**
     *
     * @param error
     */
    public void setError(String error) {
        this.error = error;
    }
   

    /**
     *
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
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
    public String logIn() {
        System.out.println("entro login");
        FacesContext context = FacesContext.getCurrentInstance();
        GenericDAO genericDAO = null;
        DcsUsuario usuario;
        String respuesta = "index";
        try {
            genericDAO = new GenericDAO();
            List<DcsUsuario> usuarios = genericDAO.findByComponent(DcsUsuario.class, "usuario", user);
            if (usuarios != null && !usuarios.isEmpty() && usuarios.get(0).getPassword().equals(password)) {
                usuario = usuarios.get(0);
                HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                if (usuario.getLastlogin() == null) {
                    httpSession.setAttribute("first_session_user", true);
                } else {
                    httpSession.setAttribute("first_session_user", false);
                }
                usuario.setIntentos(0);
                usuario.setLastlogin(new Date());
                genericDAO.saveOrUpdate(usuario);
                httpSession.setAttribute("session_user", usuario);
                respuesta = "correct";
            } else if (usuarios != null && !usuarios.isEmpty()) {
                usuario = usuarios.get(0);
                usuario.setIntentos(usuario.getIntentos() + 1);
//                if (usuario.getIntentos() == 3) {
//                    usuario.setEstatus(false);
//                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Attention: The user has been blocked, number of attempts exceeded, Contact the administrator", ""));
//                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: User or password incorrect", ""));
//                }
                genericDAO.saveOrUpdate(usuario);
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: User or password incorrect", ""));
            }
        } catch (DAOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, MSG_ERROR_TITULO, e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), ""));
        } catch (DataBaseException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, MSG_ERROR_TITULO, e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), ""));
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
        return respuesta;
    }

    /**
     *
     * @return
     */
    public String logout() {
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (httpSession != null) {
            httpSession.invalidate();
        }
        return "index";
    }    
}
