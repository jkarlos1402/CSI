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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author TMXIDSJPINAM
 */
@ManagedBean(name = "userBean")
@ViewScoped
public class UserBean implements Serializable {

    private String user;
    private String password;

    private DcsUsuario usuarioNuevo = new DcsUsuario();
    private DcsUsuario usuarioSelected;

    private List<DcsUsuario> usuariosAll;

    private List<DcsCatPais> catPaises;
    private DcsCatPais paisSelected;
    private List<DcsRol> catRoles = new ArrayList<DcsRol>();
    private DcsRol rolSelected;

    private List<DcsCatProyecto> catProyectos = new ArrayList<DcsCatProyecto>();
    private DcsCatProyecto proyectoSelected;

    private String error;
    private static final String MSG_ERROR_TITULO = "Mensaje de error...";

    /**
     *
     */
    public UserBean() {
        startBean();
    }

    private void startBean() {
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            catRoles = genericDAO.findAll(DcsRol.class);
            catProyectos = genericDAO.findAll(DcsCatProyecto.class);
            catPaises = genericDAO.findAll(DcsCatPais.class);
            usuariosAll = genericDAO.findAll(DcsUsuario.class);
        } catch (DAOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataBaseException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }

    }

    public DcsCatPais getPaisSelected() {
        return paisSelected;
    }

    public void setPaisSelected(DcsCatPais paisSelected) {
        this.paisSelected = paisSelected;
    }

    public List<DcsCatPais> getCatPaises() {
        return catPaises;
    }

    public void setCatPaises(List<DcsCatPais> catPaises) {
        this.catPaises = catPaises;
    }

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
    public List<DcsCatProyecto> getCatProyectos() {
        return catProyectos;
    }

    /**
     *
     * @param catProyectos
     */
    public void setCatProyectos(List<DcsCatProyecto> catProyectos) {
        this.catProyectos = catProyectos;
    }

    /**
     *
     * @return
     */
    public DcsCatProyecto getProyectoSelected() {
        return proyectoSelected;
    }

    /**
     *
     * @param proyectoSelected
     */
    public void setProyectoSelected(DcsCatProyecto proyectoSelected) {
        this.proyectoSelected = proyectoSelected;
    }

    /**
     *
     * @return
     */
    public List<DcsRol> getCatRoles() {
        return catRoles;
    }

    /**
     *
     * @param catRoles
     */
    public void setCatRoles(List<DcsRol> catRoles) {
        this.catRoles = catRoles;
    }

    /**
     *
     * @return
     */
    public DcsRol getRolSelected() {
        return rolSelected;
    }

    /**
     *
     * @param rolSelected
     */
    public void setRolSelected(DcsRol rolSelected) {
        this.rolSelected = rolSelected;
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
    public DcsUsuario getUsuarioNuevo() {
        return usuarioNuevo;
    }

    /**
     *
     * @param usuarioNuevo
     */
    public void setUsuarioNuevo(DcsUsuario usuarioNuevo) {
        this.usuarioNuevo = usuarioNuevo;
    }

    /**
     *
     * @return
     */
    public DcsUsuario getUsuarioSelected() {
        return usuarioSelected;
    }

    /**
     *
     * @param usuarioSelected
     */
    public void setUsuarioSelected(DcsUsuario usuarioSelected) {
        this.usuarioSelected = usuarioSelected;
    }

    /**
     *
     * @return
     */
    public List<DcsUsuario> getUsuariosAll() {
        return usuariosAll;
    }

    /**
     *
     * @param usuariosAll
     */
    public void setUsuariosAll(List<DcsUsuario> usuariosAll) {
        this.usuariosAll = usuariosAll;
    }

    /**
     *
     * @return
     */
    public String logIn() {
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
                if (usuario.getIntentos() == 3) {
                    usuario.setEstatus(false);
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Attention: The user has been blocked, number of attempts exceeded, Contact the administrator", ""));
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: User or password incorrect", ""));
                }
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

    /**
     *
     */
    public void saveUser() {
        FacesMessage message = null;
        GenericDAO genericDAO = null;
        List<DcsUsuario> usuarios;
        DcsUsuario usuarioOld = null;
        boolean bndSave = true;
        try {
            genericDAO = new GenericDAO();
            usuarios = genericDAO.findByComponent(DcsUsuario.class, "usuario", usuarioNuevo.getUsuario());
            if (usuarios != null && !usuarios.isEmpty()) {
                usuarioOld = usuarios.get(0);
            }
            if (usuarioOld != null && !usuarioOld.getPkUsuario().equals(usuarioNuevo.getPkUsuario())) {
                message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "User already exists");
                bndSave = false;
            }
            if (bndSave) {
                usuarioNuevo.setPassword(usuarioNuevo.getPassword());
                usuarioNuevo.setPais(paisSelected.getPais());
                usuarioNuevo.setEstatus(true);
                if (usuarioNuevo.getIntentos() != null && usuarioNuevo.getIntentos() == 3) {
                    usuarioNuevo.setPassreset(true);
                }
                usuarioNuevo.setIntentos(0);
                if (usuarioOld == null) {
                    usuarioOld = new DcsUsuario();
                }
                usuarioOld.setDcsCatProyectoList(usuarioNuevo.getDcsCatProyectoList());
                usuarioOld.setEstatus(usuarioNuevo.getEstatus());
                usuarioOld.setFkIdRol(usuarioNuevo.getFkIdRol());
                usuarioOld.setIntentos(usuarioNuevo.getIntentos());
                usuarioOld.setLastlogin(usuarioNuevo.getLastlogin());
                usuarioOld.setMail(usuarioNuevo.getMail());
                usuarioOld.setNombre(usuarioNuevo.getNombre());
                usuarioOld.setPais(usuarioNuevo.getPais());
                usuarioOld.setPassreset(usuarioNuevo.getPassreset());
                usuarioOld.setPassword(usuarioNuevo.getPassword());
                usuarioOld.setPkUsuario(usuarioNuevo.getPkUsuario());
                usuarioOld.setUsuario(usuarioNuevo.getUsuario());
                genericDAO.saveOrUpdate(usuarioOld);
                refreshUsers();
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "User saved");
            }
        } catch (DataBaseException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "There was a error while saving the user, " + ex.getMessage());
            usuarioNuevo.setPkUsuario(null);
        } catch (DAOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "There was a error while saving the user, " + ex.getMessage());
            usuarioNuevo.setPkUsuario(null);
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
    public void newUser() {
        usuarioNuevo = new DcsUsuario();
        usuarioSelected = null;
        paisSelected = null;
    }

    /**
     *
     */
    public void selectUser() {
        usuarioNuevo.setPkUsuario(usuarioSelected.getPkUsuario());
        usuarioNuevo.setEstatus(usuarioSelected.getEstatus());
        usuarioNuevo.setMail(usuarioSelected.getMail());
        usuarioNuevo.setNombre(usuarioSelected.getNombre());
        usuarioNuevo.setPassword(usuarioSelected.getPassword());
        usuarioNuevo.setFkIdRol(usuarioSelected.getFkIdRol());
        usuarioNuevo.setUsuario(usuarioSelected.getUsuario());
        usuarioNuevo.setPais(usuarioSelected.getPais());
        usuarioNuevo.setIntentos(usuarioSelected.getIntentos());
        usuarioNuevo.setLastlogin(usuarioSelected.getLastlogin());
        paisSelected = catPaises.get(catPaises.indexOf(new DcsCatPais(usuarioSelected.getPais())));
    }

    /**
     *
     */
    public void refreshUsers() {
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            usuariosAll = genericDAO.findAll(DcsUsuario.class);
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

    /**
     *
     */
    public void deleteUser() {
        FacesMessage message;
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            genericDAO.delete(usuarioNuevo);
            refreshUsers();
            newUser();
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "User deleted");
        } catch (DataBaseException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "There was a error while deleting the user, " + ex.getMessage());
        } catch (DAOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "There was a error while deleting the user, " + ex.getMessage());
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
