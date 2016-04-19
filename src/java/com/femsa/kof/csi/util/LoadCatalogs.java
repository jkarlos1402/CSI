/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.util;

import com.femsa.kof.csi.dao.GenericDAO;
import com.femsa.kof.csi.exception.DAOException;
import com.femsa.kof.csi.exception.DataBaseException;
import com.femsa.kof.csi.pojos.DcsCatIndicadores;
import com.femsa.kof.csi.pojos.DcsCatPais;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 *
 * @author TMXIDSJPINAM
 */
public class LoadCatalogs {

    public static void load() {
        GenericDAO genericDAO = null;
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        List<DcsCatPais> paises;
        List<DcsCatIndicadores> indicadores;
        try {
            genericDAO = new GenericDAO();
            paises = genericDAO.findAll(DcsCatPais.class);
            context.setAttribute("catalogo_paises", paises);
            indicadores = genericDAO.findAll(DcsCatIndicadores.class);
            context.setAttribute("catalogo_indicadores", indicadores);
        } catch (DAOException ex) {
            context.setAttribute("catalogo_paises", null);
            context.setAttribute("catalogo_indicadores", null);
            Logger.getLogger(LoadCatalogs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataBaseException ex) {
            context.setAttribute("catalogo_paises", null);
            context.setAttribute("catalogo_indicadores", null);
            Logger.getLogger(LoadCatalogs.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (genericDAO != null) {
                genericDAO.closeDAO();
            }
        }

    }
}
