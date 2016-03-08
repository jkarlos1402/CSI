package com.femsa.kof.csi.managedbeans;

import com.femsa.kof.csi.dao.GenericDAO;
import com.femsa.kof.csi.dao.GenericDaoInterface;
import com.femsa.kof.csi.exception.DAOException;
import com.femsa.kof.csi.exception.DataBaseException;
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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DualListModel;

/**
 *
 * @author TMXIDSJPINAM
 */
@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean implements Serializable {

    private String user;
    private String password;

    private DcsUsuario usuarioNuevo = new DcsUsuario();
    private DcsUsuario usuarioSelected;

    private List<DcsUsuario> usuariosAll;

//    private DualListModel<ShareCatPais> paisesAll;
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
//            List<ShareCatPais> sourcePais = paisDAO.getCatPais();
//            List<ShareCatPais> targetPais = new ArrayList<ShareCatPais>();
//            ShareCatProyectoDAO proyectoDAO = new ShareCatProyectoDAO();
            catProyectos = genericDAO.findAll(DcsCatProyecto.class);
//            paisesAll = new DualListModel<ShareCatPais>(sourcePais, targetPais);

//            ShareUsuarioDAO usuarioDAO = new ShareUsuarioDAO();
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

//    /**
//     *
//     * @return
//     */
//    public DualListModel<ShareCatPais> getPaisesAll() {
//        ShareCatPaisDAO paisDAO = new ShareCatPaisDAO();
//        List<ShareCatPais> sourcePais = paisDAO.getCatPais();
//        List<ShareCatPais> targetPais = paisesAll.getTarget();
//        for (ShareCatPais pais : targetPais) {
//            sourcePais.remove(pais);
//        }
//        paisesAll.setSource(sourcePais);
//        return paisesAll;
//    }
//    /**
//     *
//     * @param paisesAll
//     */
//    public void setPaisesAll(DualListModel<ShareCatPais> paisesAll) {
//        this.paisesAll = paisesAll;
//    }
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

            if (usuarios != null && !usuarios.isEmpty() && usuarios.get(0).getPassword().equalsIgnoreCase(password)) {
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
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, MSG_ERROR_TITULO, e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), ""));
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
        System.out.println("respuesta:" + respuesta);
        return respuesta;
    }

    /**
     *
     * @return
     */
    public String logout() {
        System.out.println("salir");
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (httpSession != null) {
            httpSession.invalidate();
        }
        return "index";
    }

//    /**
//     *
//     */
//    public void saveUser() {
//        FacesMessage message;
//        ShareUsuarioDAO usuarioDAO = new ShareUsuarioDAO();
//        if (usuarioNuevo.getPaises() == null) {
//            usuarioNuevo.setPaises(new ArrayList<ShareCatPais>());
//        } else {
//            usuarioNuevo.getPaises().clear();
//        }
//        if (usuarioNuevo.getProyectos() == null) {
//            usuarioNuevo.setProyectos(new ArrayList<ShareCatProyecto>());
//        } else {
//            usuarioNuevo.getProyectos().clear();
//        }
//        for (int i = 0; i < paisesAll.getTarget().size(); i++) {
//            usuarioNuevo.getPaises().add(paisesAll.getTarget().get(i));
//        }
//        usuarioNuevo.setPassword(usuarioNuevo.getPassword());
//        usuarioNuevo.getProyectos().add(proyectoSelected);
//        if (usuarioNuevo.getIntentos() != null && usuarioNuevo.getIntentos() == 3) {
//            usuarioNuevo.setPassReset(true);
//        }
//        usuarioNuevo.setIntentos(0);
//        if (usuarioDAO.saveUser(usuarioNuevo)) {
//            refreshUsers();
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "User saved");
//        } else {
//            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "There was a error while saving the user, " + usuarioDAO.getError());
//            usuarioNuevo.setPkUsuario(null);
//        }
//        FacesContext.getCurrentInstance().addMessage(null, message);
//    }
//
//    /**
//     *
//     */
//    public void newUser() {
//        usuarioNuevo = new ShareUsuario();
//        usuarioSelected = null;
//        for (int i = 0; i < paisesAll.getTarget().size(); i++) {
//            paisesAll.getSource().add(paisesAll.getTarget().get(i));
//        }
//        paisesAll.getTarget().clear();
//        proyectoSelected = null;
//    }
//
//    /**
//     *
//     */
//    public void selectUser() {
//        usuarioNuevo.setPkUsuario(usuarioSelected.getPkUsuario());
//        usuarioNuevo.setEstatus(usuarioSelected.getEstatus());
//        usuarioNuevo.setMail(usuarioSelected.getMail());
//        usuarioNuevo.setNombre(usuarioSelected.getNombre());
//        usuarioNuevo.setPassword(usuarioSelected.getPassword());
//        usuarioNuevo.setRol(usuarioSelected.getRol());
//        usuarioNuevo.setUsuario(usuarioSelected.getUsuario());
//        usuarioNuevo.setPaises(usuarioSelected.getPaises());
//        usuarioNuevo.setPais(usuarioSelected.getPais());
//        usuarioNuevo.setProyectos(usuarioSelected.getProyectos());
//        usuarioNuevo.setIntentos(usuarioSelected.getIntentos());
//        usuarioNuevo.setLastLogin(usuarioSelected.getLastLogin());
//        paisesAll.getTarget().clear();
//        if (usuarioSelected.getPaises() != null && !usuarioSelected.getPaises().isEmpty()) {
//            Object[] paisesT = usuarioSelected.getPaises().toArray();
//            for (int i = 0; i < usuarioSelected.getPaises().size(); i++) {
//                paisesAll.getTarget().add((ShareCatPais) paisesT[i]);
//                paisesAll.getSource().remove((ShareCatPais) paisesT[i]);
//            }
//        }
//        if (usuarioSelected.getProyectos() != null && !usuarioSelected.getProyectos().isEmpty()) {
//            proyectoSelected = usuarioSelected.getProyectos().get(0);
//        }
//    }
//
//    /**
//     *
//     */
//    public void refreshUsers() {
//        ShareUsuarioDAO usuarioDAO = new ShareUsuarioDAO();
//        usuariosAll = usuarioDAO.getAllUsers();
//    }
}
