/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femsa.kof.csi.dao;

import com.femsa.kof.csi.exception.DAOException;
import java.io.Serializable;
import java.util.List;

public interface GenericDaoInterface {

   public <T, O extends Serializable> T findById(Class clase, O id) throws DAOException;

    public <T extends Serializable> List<T> findAll(Class clase) throws DAOException;

    public <T extends Serializable> void delete(T persistentInstance) throws DAOException;

    public <T extends Serializable> void delete(List<T> instances) throws DAOException;

    public <T extends Serializable> void saveOrUpdate(T instance) throws DAOException;

    public <T extends Serializable> void saveOrUpdateAll(List<T> instances) throws DAOException;

    public <T extends Serializable> List<T> findByQuery(Class clase, String query) throws DAOException;

    public <T extends Serializable> List<T> findByComponent(Class clase, String columna, String valor) throws DAOException;
    
    public boolean excecuteNativeDDLSQL(String sqlNative) throws DAOException;
}
