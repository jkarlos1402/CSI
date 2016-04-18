package com.femsa.kof.csi.listener;

import com.femsa.kof.csi.util.LoadCatalogs;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author TMXIDSJPINAM
 */
@WebListener()
public class ContextListenerKOF implements ServletContextListener {

    /**
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        Boolean flagLoadIndi = false;
        Boolean flagLoadFlota = false;

        context.setAttribute("flag_load_indi", flagLoadIndi);
        context.setAttribute("flag_load_flota", flagLoadFlota);

        LoadCatalogs.load();
    }

    /**
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //comentario
    }
}
