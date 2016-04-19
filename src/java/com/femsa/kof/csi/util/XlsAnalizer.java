/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.util;

import com.femsa.kof.csi.exception.DCSException;
import com.femsa.kof.csi.pojos.DcsCatIndicadores;
import com.femsa.kof.csi.pojos.DcsCatPais;
import com.femsa.kof.csi.pojos.DcsUsuario;
import com.femsa.kof.csi.pojos.Xtmpinddl;
import com.femsa.kof.csi.pojos.XtmpinddlFlota;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author TMXIDSJPINAM
 */
public class XlsAnalizer {

    private List<String> omittedSheets;
    private List<String> loadedSheets;
    private List<String> errors;

    public XlsAnalizer() {
        omittedSheets = new ArrayList<String>();
        loadedSheets = new ArrayList<String>();
        errors = new ArrayList<String>();
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

    public List<Xtmpinddl> analizeXlsIndi(UploadedFile file, final DcsUsuario usuario, List<DcsCatPais> paises, List<DcsCatIndicadores> indicadores) throws DCSException {
        Workbook excelXLS = null;
        List<Xtmpinddl> listaCarga = null;
        try {
            String extension = getExtension(file.getFileName());
            Iterator<Row> rowIterator;
            if (extension.equalsIgnoreCase("xlsx")) {
                excelXLS = new XSSFWorkbook(file.getInputstream());
            } else if (extension.equalsIgnoreCase("xls")) {
                excelXLS = new HSSFWorkbook(file.getInputstream());
            }
            int numberOfSheets = excelXLS != null ? excelXLS.getNumberOfSheets() : 0;
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = excelXLS != null ? excelXLS.getSheetAt(i) : null;
                rowIterator = sheet != null ? sheet.iterator() : null;
                if (sheet != null && i == 0) {
                    listaCarga = this.analizeSheetIndi(rowIterator, usuario, sheet.getSheetName(), paises, indicadores);
                    if (!listaCarga.isEmpty()) {
                        loadedSheets.add(sheet.getSheetName().trim().toUpperCase());
                    } else {
                        omittedSheets.add(sheet.getSheetName().trim().toUpperCase() + ", Empty");
                    }
                } else {
                    String mensaje = sheet != null ? sheet.getSheetName().trim().toUpperCase() + ", not valid." : "Not valid.";
                    omittedSheets.add(mensaje);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error IO", ex);
            throw new DCSException("An error ocurred while analizing the file: " + ex.getMessage());
        } finally {
            try {
                file.getInputstream().close();
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error IO", ex);
                throw new DCSException("An error ocurred while analizing the file: " + ex.getMessage());
            }
        }
        return listaCarga;
    }

    /**
     * Método encargado de la lectura y análisis de una hoja del archivo excel
     * cargado en la interfaz gráfica correspondiente a Rolling
     *
     *
     * @param rowIterator lista de renglones contenidos en la hoja de excel
     * @param usuario usuario que realiza el análisis
     * @param sheetName nombre de la hoja de excel
     * @return Regresa una lista con los registros a ser almacenados en base de
     * datos
     */
    private List<Xtmpinddl> analizeSheetIndi(Iterator<Row> rowIterator, DcsUsuario usuario, String sheetName, List<DcsCatPais> paises, List<DcsCatIndicadores> indicadores) throws DCSException {
        int numRow = 0;
        List<Xtmpinddl> cargas = new ArrayList<Xtmpinddl>();
        Xtmpinddl indi;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendarioActual = Calendar.getInstance();
        Calendar calendario = Calendar.getInstance();
        end:
        while (rowIterator != null && rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell cell;
            if (numRow == 0) {

            } else {
                indi = new Xtmpinddl();
                cell = row.getCell(0);
                if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    calendario.setTime(cell.getDateCellValue());
                    if ("User".equalsIgnoreCase(usuario.getFkIdRol().getRol()) && (calendarioActual.get(Calendar.YEAR) != calendario.get(Calendar.YEAR) || calendarioActual.get(Calendar.MONTH) != calendario.get(Calendar.MONTH))) {
                        throw new DCSException("Error: You can not load information of a different month of the current");
                    }
                    indi.setFecha(sdf.format(cell.getDateCellValue()));
                } else if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    try {
                        calendario.setTime(sdf.parse(cell.getStringCellValue().trim()));
                        if ("User".equalsIgnoreCase(usuario.getFkIdRol().getRol()) && (calendarioActual.get(Calendar.YEAR) != calendario.get(Calendar.YEAR) || calendarioActual.get(Calendar.MONTH) != calendario.get(Calendar.MONTH))) {
                            throw new DCSException("Error: You can not load information of a different month of the current");
                        }
                        indi.setFecha(cell.getStringCellValue().trim());
                    } catch (ParseException ex) {
                        Logger.getLogger(XlsAnalizer.class.getName()).log(Level.SEVERE, null, ex);
                        errors.add("Approximately " + Character.toString((char) (65 + 0)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                        cargas.clear();
                        break;
                    }
                } else {
                    numRow++;
                    continue;
                }
                cell = row.getCell(1);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    indi.setGrupoInd(cell != null ? cell.getStringCellValue().trim() : null);
                } else {
                    errors.add("Approximately " + Character.toString((char) (65 + 1)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                    cargas.clear();
                    break;
                }
                cell = row.getCell(2);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (indicadores.contains(new DcsCatIndicadores(cell != null ? cell.getStringCellValue().trim() : null))) {
                        indi.setIndicador(cell != null ? cell.getStringCellValue().trim() : null);
                    }else {
                        errors.add("Approximately " + Character.toString((char) (65 + 2)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "], indicator not found.");
                        cargas.clear();
                        break;
                    }
                } else {
                    errors.add("Approximately " + Character.toString((char) (65 + 2)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                    cargas.clear();
                    break;
                }
                cell = row.getCell(3);
                if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if ("KOF".equalsIgnoreCase(cell.getStringCellValue().trim()) || paises.contains(new DcsCatPais(cell.getStringCellValue().trim()))) {
                        indi.setPais(cell.getStringCellValue().trim());
                    } else {
                        errors.add("Approximately " + Character.toString((char) (65 + 3)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                        cargas.clear();
                        break;
                    }
                    if ("User".equalsIgnoreCase(usuario.getFkIdRol().getRol()) && (!usuario.getPais().equalsIgnoreCase(indi.getPais()))) {
                        throw new DCSException("Error: you can not load information from other country");
                    }
                } else {
                    errors.add("Approximately " + Character.toString((char) (65 + 3)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                    cargas.clear();
                    break;
                }
                cell = row.getCell(4);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    indi.setCentro(cell != null ? cell.getStringCellValue().trim() : null);
                } else {
                    errors.add("Approximately " + Character.toString((char) (65 + 4)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                    cargas.clear();
                    break;
                }
                cell = row.getCell(5);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    indi.setRuta(cell != null ? cell.getStringCellValue().trim() : null);
                } else {
                    errors.add("Approximately " + Character.toString((char) (65 + 5)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                    cargas.clear();
                    break;
                }
                cell = row.getCell(6);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    indi.setValorMensual(cell != null ? (float) cell.getNumericCellValue() : 0);
                } else {
                    errors.add("Approximately " + Character.toString((char) (65 + 6)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                    cargas.clear();
                    break;
                }
                cell = row.getCell(7);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    indi.setValorAcumulado(cell != null ? (float) cell.getNumericCellValue() : 0);
                } else {
                    errors.add("Approximately " + Character.toString((char) (65 + 7)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                    cargas.clear();
                    break;
                }
                cargas.add(indi);
            }
            numRow++;
        }
        return cargas;
    }

    public List<XtmpinddlFlota> analizeXlsFlota(UploadedFile file, final DcsUsuario usuario, List<DcsCatPais> paises) throws DCSException {
        Workbook excelXLS = null;
        List<XtmpinddlFlota> listaCarga = null;
        try {
            String extension = getExtension(file.getFileName());
            Iterator<Row> rowIterator;
            if (extension.equalsIgnoreCase("xlsx")) {
                excelXLS = new XSSFWorkbook(file.getInputstream());
            } else if (extension.equalsIgnoreCase("xls")) {
                excelXLS = new HSSFWorkbook(file.getInputstream());
            }
            int numberOfSheets = excelXLS != null ? excelXLS.getNumberOfSheets() : 0;
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = excelXLS != null ? excelXLS.getSheetAt(i) : null;
                rowIterator = sheet != null ? sheet.iterator() : null;
                if (sheet != null && i == 0) {
                    listaCarga = this.analizeSheetFlota(rowIterator, usuario, sheet.getSheetName(), paises);
                    if (!listaCarga.isEmpty()) {
                        loadedSheets.add(sheet.getSheetName().trim().toUpperCase());
                    } else {
                        omittedSheets.add(sheet.getSheetName().trim().toUpperCase() + ", Empty");
                    }
                } else {
                    String mensaje = sheet != null ? sheet.getSheetName().trim().toUpperCase() + ", not valid." : "Not valid.";
                    omittedSheets.add(mensaje);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error IO", ex);
            throw new DCSException("An error ocurred while analizing the file: " + ex.getMessage());
        } finally {
            try {
                file.getInputstream().close();
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error IO", ex);
                throw new DCSException("An error ocurred while analizing the file: " + ex.getMessage());
            }
        }
        return listaCarga;
    }

    /**
     * Método encargado de la lectura y análisis de una hoja del archivo excel
     * cargado en la interfaz gráfica correspondiente a flota
     *
     *
     * @param rowIterator lista de renglones contenidos en la hoja de excel
     * @param usuario usuario que realiza el análisis
     * @param sheetName nombre de la hoja de excel
     * @return Regresa una lista con los registros a ser almacenados en base de
     * datos
     */
    private List<XtmpinddlFlota> analizeSheetFlota(Iterator<Row> rowIterator, DcsUsuario usuario, String sheetName, List<DcsCatPais> paises) throws DCSException {
        int numRow = 0;
        List<XtmpinddlFlota> cargas = new ArrayList<XtmpinddlFlota>();
        XtmpinddlFlota flota;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Calendar calendario = Calendar.getInstance();
        end:
        while (rowIterator != null && rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell cell;
            if (numRow == 0) {

            } else {
                flota = new XtmpinddlFlota();
                cell = row.getCell(0);
                if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    try {
                        calendario.setTime(sdf.parse(((int) cell.getNumericCellValue()) + ""));
                    } catch (ParseException ex) {
                        Logger.getLogger(XlsAnalizer.class.getName()).log(Level.SEVERE, null, ex);
                        errors.add("Approximately " + Character.toString((char) (65 + 0)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                        cargas.clear();
                        break;
                    }
                    flota.setAnio(calendario.get(Calendar.YEAR));
                } else if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    try {
                        calendario.setTime(sdf.parse(cell.getStringCellValue().trim()));
                    } catch (ParseException ex) {
                        Logger.getLogger(XlsAnalizer.class.getName()).log(Level.SEVERE, null, ex);
                        errors.add("Approximately " + Character.toString((char) (65 + 0)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                        cargas.clear();
                        break;
                    }
                    flota.setAnio(calendario.get(Calendar.YEAR));
                } else {
                    numRow++;
                    continue;
                }
                cell = row.getCell(1);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if ("KOF".equalsIgnoreCase(cell != null ? cell.getStringCellValue().trim() : "") || paises.contains(new DcsCatPais(cell != null ? cell.getStringCellValue().trim() : ""))) {
                        flota.setPais(cell != null ? cell.getStringCellValue().trim() : null);
                    } else {
                        errors.add("Approximately " + Character.toString((char) (65 + 1)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                        cargas.clear();
                        break;
                    }
                    if ("User".equalsIgnoreCase(usuario.getFkIdRol().getRol()) && (!usuario.getPais().equalsIgnoreCase(flota.getPais()))) {
                        throw new DCSException("Error: you can not load information from other country");
                    }
                } else {
                    errors.add("Approximately " + Character.toString((char) (65 + 1)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                    cargas.clear();
                    break;
                }
                cell = row.getCell(2);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    flota.setTipo(cell != null ? cell.getStringCellValue().trim() : null);
                } else {
                    errors.add("Approximately " + Character.toString((char) (65 + 2)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                    cargas.clear();
                    break;
                }
                cell = row.getCell(3);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    flota.setEdad(cell != null ? cell.getStringCellValue().trim() : null);
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    flota.setEdad((int) cell.getNumericCellValue() + "");
                } else {
                    errors.add("Approximately " + Character.toString((char) (65 + 3)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                    cargas.clear();
                    break;
                }
                cell = row.getCell(4);
                if (cell == null || cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    flota.setCantidad(cell != null ? Integer.parseInt(cell.getStringCellValue().trim()) : null);
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    flota.setCantidad((int) cell.getNumericCellValue());
                } else {
                    errors.add("Approximately " + Character.toString((char) (65 + 4)) + "" + (numRow + 1) + " cell in " + sheetName + " sheet have a invalid value [" + cell + "].");
                    cargas.clear();
                    break;
                }
                cargas.add(flota);
            }
            numRow++;
        }
        return cargas;
    }

    /**
     * Método estático encargado de obtener la extensión del archivo cargado
     *
     * @param filename nombre del archivo
     * @return Regresa la extensión del archivo
     */
    private static String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
}
