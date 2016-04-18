package com.femsa.kof.csi.util;

import com.femsa.kof.csi.dao.GenericDAO;
import com.femsa.kof.csi.exception.DAOException;
import com.femsa.kof.csi.exception.DCSException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 *
 * @author TMXIDSJPINAM
 */
public class ScriptAnalizer {

    public static void excecuteInstructionsSQL(String proceso, GenericDAO genericDAO) throws DCSException, IOException {
        ServletContext sc = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String contextPathResources = sc.getRealPath("");
        File directorioBase = new File(contextPathResources + File.separator + "WEB-INF" + File.separator + "scripts" + File.separator + proceso + File.separator);
        File[] ficheros = directorioBase.listFiles();
        StringBuilder stringBuilder;
        FileReader f = null;
        BufferedReader b = null;
        String cadena;
        String nombre;
        int indexComentario;
        List<String> statements = new ArrayList<String>();
        for (File fichero : ficheros) {
            statements.clear();
            stringBuilder = new StringBuilder();
            nombre = fichero.getAbsoluteFile().getName().trim();
            if (getExtension(nombre).equalsIgnoreCase("sql")) {
                try {
                    f = new FileReader(fichero);
                    b = new BufferedReader(f);
                    while ((cadena = b.readLine()) != null) {
                        cadena = cadena.trim();
                        if (!cadena.startsWith("/*", 0) && !cadena.startsWith("*", 0) && !cadena.startsWith("--", 0) && !cadena.equals("") && !cadena.equalsIgnoreCase("exit;")) {
                            if (cadena.contains("/*")) {
                                indexComentario = cadena.indexOf("/*");
                                cadena = cadena.substring(0, indexComentario);
                            }
                            stringBuilder.append(cadena);
                            stringBuilder.append(" ");
                            if (cadena.endsWith(";")) {
                                statements.add(stringBuilder.toString().replaceAll(";", "").trim());
                                stringBuilder = new StringBuilder();
                            }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ScriptAnalizer.class.getName()).log(Level.SEVERE, null, ex);
                    throw new DCSException("Error: " + ex.getMessage());
                } finally {
                    if (b != null) {
                        b.close();
                    }
                    if (f != null) {
                        f.close();
                    }
                }
                for (String statement : statements) {
                    try {
                        genericDAO.excecuteNativeDDLSQL(statement);
                    } catch (DAOException ex) {
                        System.out.println(statement);
                        Logger.getLogger(ScriptAnalizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private static String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
}
