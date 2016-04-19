/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.managedbeans;

import com.femsa.kof.csi.dao.GenericDAO;
import com.femsa.kof.csi.exception.DAOException;
import com.femsa.kof.csi.exception.DCSException;
import com.femsa.kof.csi.exception.DataBaseException;
import com.femsa.kof.csi.pojos.DcsCatIndicadores;
import com.femsa.kof.csi.pojos.DcsCatPais;
import com.femsa.kof.csi.pojos.DcsUsuario;
import com.femsa.kof.csi.pojos.Xtmpinddl;
import com.femsa.kof.csi.pojos.XtmpinddlFlota;
import com.femsa.kof.csi.util.LoadCatalogs;
import com.femsa.kof.csi.util.ScriptAnalizer;
import com.femsa.kof.csi.util.XlsAnalizer;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author TMXIDSJPINAM
 */
@ManagedBean
@ViewScoped
public class LoadBean implements Serializable{

    private List<Xtmpinddl> listInfoCargaIndi;

    private List<XtmpinddlFlota> listInfoCargaFlota;

    private List<String> omittedSheets;
    private List<String> loadedSheets;
    private List<String> errors;

    private List<String> omittedSheetsFlota;
    private List<String> loadedSheetsFlota;
    private List<String> errorsFlota;

    private final DcsUsuario usuario;

    public LoadBean() {
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        usuario = (DcsUsuario) httpSession.getAttribute("session_user");
        omittedSheets = new ArrayList<String>();
        loadedSheets = new ArrayList<String>();
        errors = new ArrayList<String>();
        listInfoCargaIndi = new ArrayList<Xtmpinddl>();

        omittedSheetsFlota = new ArrayList<String>();
        loadedSheetsFlota = new ArrayList<String>();
        errorsFlota = new ArrayList<String>();
        listInfoCargaFlota = new ArrayList<XtmpinddlFlota>();
    }

    public List<XtmpinddlFlota> getListInfoCargaFlota() {
        return listInfoCargaFlota;
    }

    public void setListInfoCargaFlota(List<XtmpinddlFlota> listInfoCargaFlota) {
        this.listInfoCargaFlota = listInfoCargaFlota;
    }

    public List<String> getOmittedSheetsFlota() {
        return omittedSheetsFlota;
    }

    public void setOmittedSheetsFlota(List<String> omittedSheetsFlota) {
        this.omittedSheetsFlota = omittedSheetsFlota;
    }

    public List<String> getLoadedSheetsFlota() {
        return loadedSheetsFlota;
    }

    public void setLoadedSheetsFlota(List<String> loadedSheetsFlota) {
        this.loadedSheetsFlota = loadedSheetsFlota;
    }

    public List<String> getErrorsFlota() {
        return errorsFlota;
    }

    public void setErrorsFlota(List<String> errorsFlota) {
        this.errorsFlota = errorsFlota;
    }

    public List<Xtmpinddl> getListInfoCargaIndi() {
        return listInfoCargaIndi;
    }

    public void setListInfoCargaIndi(List<Xtmpinddl> listInfoCargaIndi) {
        this.listInfoCargaIndi = listInfoCargaIndi;
    }

    public List<String> getOmittedSheets() {
        return omittedSheets;
    }

    public void setOmittedSheets(List<String> omittedSheets) {
        this.omittedSheets = omittedSheets;
    }

    public List<String> getLoadedSheets() {
        return loadedSheets;
    }

    public void setLoadedSheets(List<String> loadedSheets) {
        this.loadedSheets = loadedSheets;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message;
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        Object objeto = context.getAttribute("catalogo_paises");
        List<DcsCatPais> paises;
        if (objeto != null) {            
            paises = (List<DcsCatPais>) objeto;
        } else {            
            LoadCatalogs.load();
            paises = (List<DcsCatPais>) context.getAttribute("catalogo_paises");
        }
        Object objetoIndi = context.getAttribute("catalogo_indicadores");
        List<DcsCatIndicadores> indicadores;
        if (objetoIndi != null) {            
            indicadores = (List<DcsCatIndicadores>) objetoIndi;
        } else {            
            LoadCatalogs.load();
            indicadores = (List<DcsCatIndicadores>) context.getAttribute("catalogo_indicadores");
        }
        try {
            XlsAnalizer analizer = new XlsAnalizer();
            listInfoCargaIndi = analizer.analizeXlsIndi(event.getFile(), usuario, paises != null ? paises : new ArrayList<DcsCatPais>(),indicadores != null ? indicadores : new ArrayList<DcsCatIndicadores>());
            omittedSheets = analizer.getOmittedSheets();
            loadedSheets = analizer.getLoadedSheets();
            errors = analizer.getErrors();
            if (!errors.isEmpty()) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alert", event.getFile().getFileName() + " is corrupt.");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", event.getFile().getFileName() + " is uploaded.");
            }
        } catch (DCSException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage());
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void saveInfoIndi() throws InterruptedException {
        FacesMessage message;
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        Boolean flagLoadIndi = (Boolean) context.getAttribute("flag_load_indi");
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            if (flagLoadIndi != null && !flagLoadIndi) {
                flagLoadIndi = true;
                context.setAttribute("flag_load_indi", flagLoadIndi);
                genericDAO.excecuteNativeDDLSQL("DELETE FROM XTMPINDDL");
                genericDAO.excecuteNativeDDLSQL("COMMIT");
                genericDAO.saveOrUpdateAll(listInfoCargaIndi);
                genericDAO.excecuteNativeDDLSQL("DROP SEQUENCE DCS_SEQ_XTMPINDDL");
                genericDAO.excecuteNativeDDLSQL("CREATE SEQUENCE DCS_SEQ_XTMPINDDL INCREMENT BY 1 START WITH 1");
                //Se ejecutan scripts sql
                ScriptAnalizer.excecuteInstructionsSQL("indicador", genericDAO);
                flagLoadIndi = false;
                context.setAttribute("flag_load_indi", flagLoadIndi);
                listInfoCargaIndi.clear();
                errors.clear();
                loadedSheets.clear();
                omittedSheets.clear();
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "Records saved.");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Sorry", "Process load, try more later!");
            }
        } catch (DAOException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Sorry", e.getMessage());
            flagLoadIndi = false;
            context.setAttribute("flag_load_indi", flagLoadIndi);
        } catch (DataBaseException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Sorry", e.getMessage());
            flagLoadIndi = false;
            context.setAttribute("flag_load_indi", flagLoadIndi);
        } catch (DCSException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Sorry", e.getMessage());
            flagLoadIndi = false;
            context.setAttribute("flag_load_indi", flagLoadIndi);
        } catch (IOException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Sorry", e.getMessage());
            flagLoadIndi = false;
            context.setAttribute("flag_load_indi", flagLoadIndi);
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void handleFileUploadFlota(FileUploadEvent event) {
        FacesMessage message;
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        Object objeto = context.getAttribute("catalogo_paises");
        List<DcsCatPais> paises;
        if (objeto != null) {            
            paises = (List<DcsCatPais>) objeto;
            System.out.println(paises.size());
        } else {            
            LoadCatalogs.load();
            paises = (List<DcsCatPais>) context.getAttribute("catalogo_paises");
        }
        try {
            XlsAnalizer analizer = new XlsAnalizer();
            listInfoCargaFlota = analizer.analizeXlsFlota(event.getFile(), usuario,paises != null ? paises : new ArrayList<DcsCatPais>());
            omittedSheetsFlota = analizer.getOmittedSheets();
            loadedSheetsFlota = analizer.getLoadedSheets();
            errorsFlota = analizer.getErrors();
            if (!errorsFlota.isEmpty()) {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Alert", event.getFile().getFileName() + " is corrupt.");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", event.getFile().getFileName() + " is uploaded.");
            }
        } catch (DCSException ex) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage());
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void saveInfoFlota() throws InterruptedException {
        FacesMessage message;
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        Boolean flagLoadFlota = (Boolean) context.getAttribute("flag_load_flota");
        GenericDAO genericDAO = null;
        try {
            genericDAO = new GenericDAO();
            if (flagLoadFlota != null && !flagLoadFlota) {
                flagLoadFlota = true;
                context.setAttribute("flag_load_flota", flagLoadFlota);
                genericDAO.excecuteNativeDDLSQL("DELETE FROM XTMPINDDL_FLOTA");
                genericDAO.excecuteNativeDDLSQL("COMMIT");
                genericDAO.saveOrUpdateAll(listInfoCargaFlota);
                genericDAO.excecuteNativeDDLSQL("DROP SEQUENCE DCS_SEQ_XTMPINDDL_FLOTA");
                genericDAO.excecuteNativeDDLSQL("CREATE SEQUENCE DCS_SEQ_XTMPINDDL_FLOTA INCREMENT BY 1 START WITH 1");
                //Se ejecutan scripts sql
                ScriptAnalizer.excecuteInstructionsSQL("flota", genericDAO);
                flagLoadFlota = false;
                context.setAttribute("flag_load_flota", flagLoadFlota);
                listInfoCargaFlota.clear();
                errorsFlota.clear();
                loadedSheetsFlota.clear();
                omittedSheetsFlota.clear();
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "Records saved.");
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Sorry", "Process load, try more later!");
            }
        } catch (DAOException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Sorry", e.getMessage());
            flagLoadFlota = false;
            context.setAttribute("flag_load_flota", flagLoadFlota);
        } catch (DataBaseException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Sorry", e.getMessage());
            flagLoadFlota = false;
            context.setAttribute("flag_load_flota", flagLoadFlota);
        } catch (DCSException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Sorry", e.getMessage());
            flagLoadFlota = false;
            context.setAttribute("flag_load_flota", flagLoadFlota);
        } catch (IOException e) {
            message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Sorry", e.getMessage());
            flagLoadFlota = false;
            context.setAttribute("flag_load_flota", flagLoadFlota);
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
