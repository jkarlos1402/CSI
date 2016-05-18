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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author TMXIDSJPINAM
 */
@ManagedBean(name = "userBean")
@ViewScoped
public class UserBean implements Serializable {

    private DcsUsuario usuarioNuevo = new DcsUsuario();
    private DcsUsuario usuarioSelected;

    private List<DcsUsuario> usuariosAll;

    private List<DcsCatPais> catPaises;
    private DcsCatPais paisSelected;
    private List<DcsRol> catRoles = new ArrayList<DcsRol>();
    private DcsRol rolSelected;

    private DualListModel<DcsCatProyecto> catProyectos;

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
            List<DcsCatProyecto> sourceProyecto = genericDAO.findAll(DcsCatProyecto.class);
            List<DcsCatProyecto> targetProyecto = new ArrayList<DcsCatProyecto>();
            catProyectos = new DualListModel(sourceProyecto, targetProyecto);
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

    public DualListModel<DcsCatProyecto> getCatProyectos() {
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            List<DcsCatProyecto> sourceProyecto = genericDAO.findAll(DcsCatProyecto.class);
            List<DcsCatProyecto> targetProyecto = catProyectos.getTarget();
            for (DcsCatProyecto proyecto : targetProyecto) {
                sourceProyecto.remove(proyecto);
            }
            catProyectos.setSource(sourceProyecto);
        } catch (DataBaseException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DAOException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
        return catProyectos;
    }

    public void setCatProyectos(DualListModel<DcsCatProyecto> catProyectos) {
        this.catProyectos = catProyectos;
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
                if (usuarioNuevo.getDcsCatProyectoList() == null) {
                    usuarioNuevo.setDcsCatProyectoList(new ArrayList<DcsCatProyecto>());
                } else {
                    usuarioNuevo.getDcsCatProyectoList().clear();
                }
                for (int i = 0; i < catProyectos.getTarget().size(); i++) {
                    usuarioNuevo.getDcsCatProyectoList().add(catProyectos.getTarget().get(i));
                }
                usuarioOld.setDcsCatProyectoList(usuarioNuevo.getDcsCatProyectoList());
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
        for (int i = 0; i < catProyectos.getTarget().size(); i++) {
            catProyectos.getSource().add(catProyectos.getTarget().get(i));
        }
        catProyectos.getTarget().clear();
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
        catProyectos.getTarget().clear();
        if (usuarioSelected.getDcsCatProyectoList() != null && !usuarioSelected.getDcsCatProyectoList().isEmpty()) {
            Object[] proyectosT = usuarioSelected.getDcsCatProyectoList().toArray();
            for (int i = 0; i < usuarioSelected.getDcsCatProyectoList().size(); i++) {
                catProyectos.getTarget().add((DcsCatProyecto) proyectosT[i]);
                catProyectos.getSource().remove((DcsCatProyecto) proyectosT[i]);
            }
        }
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
