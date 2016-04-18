/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.converter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.Id;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 * Convertidor genérico para clases de tipo entidad y que contengan un campo con
 * anotación @Id, el componente debe contener un f:selectItems, de lo contrario
 * siempre regresara nulo
 *
 * @author TMXIDSJPINAM
 */
@FacesConverter("genericConverter")
public class GenericConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Field[] properties;        
        for (UIComponent object : component.getChildren()) {
            if ("javax.faces.component.UISelectItems".equalsIgnoreCase(object.getClass().getName())) {
                UISelectItems items = (UISelectItems) object;
                List<Object> lista = (List<Object>) items.getValue();
                if (lista != null && !lista.isEmpty()) {
                    try {
                        Class clase = lista.get(0).getClass();
                        properties = clase.getDeclaredFields();
                        for (Object elemento : lista) {
                            for (Field campo : properties) {
                                campo.setAccessible(true);
                                if (campo.isAnnotationPresent(Id.class) && campo.get(elemento).toString().equals(value)) {
                                    return elemento;
                                }
                            }
                        }
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(GenericConverter.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Field[] properties = value.getClass().getDeclaredFields();
        for (Field property : properties) {
            if (property.isAnnotationPresent(Id.class)) {
                try {
                    property.setAccessible(true);
                    return property.get(value).toString();
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(GenericConverter.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(GenericConverter.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }
        }
        return null;
    }

}
