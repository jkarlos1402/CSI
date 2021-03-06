package com.femsa.kof.csi.dao;

import com.femsa.kof.csi.exception.DAOException;
import com.femsa.kof.csi.exception.DataBaseException;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.ObjectDeletedException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TypeMismatchException;
import org.hibernate.exception.ConstraintViolationException;

public class GenericDAO implements GenericDaoInterface {

    private final HibernateUtil hibernateUtil;
    private final SessionFactory sessionFactory;
    private final Session session;
    private Transaction tx;

    public GenericDAO() throws DataBaseException {
        this.hibernateUtil = new HibernateUtil();
        sessionFactory = this.hibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
    }

    public Session getSession() {
        return session;
    }

    public void closeDAO() {
        if (session != null && sessionFactory != null) {
            session.clear();
            session.close();
            sessionFactory.close();
        }
    }

    @Override
    public <T, O extends Serializable> T findById(final Class clase, O id) throws DAOException {
        T elemento = null;
        try {
            elemento = (T) session.get(clase, id);
        } catch (TypeMismatchException e) {
            throw new DAOException("Error, parámetros incompatibles: " + e.getMessage());
        } finally {
            session.flush();
        }
        return elemento;
    }

    @Override
    public <T extends Serializable> List<T> findAll(final Class clase) throws DAOException {
        List<T> elementos = null;
        Query query;
        try {
            query = session.createQuery("FROM " + clase.getName() + " c");
            elementos = query.list();            
        } catch (HibernateException e) {
            throw new DAOException("Error no identificado: " + e.getMessage());
        } finally {
            session.flush();
        }
        return elementos;
    }

    @Override
    public <T extends Serializable> void delete(final T persistentInstance) throws DAOException {
        try {
            tx = session.beginTransaction();
            session.delete(persistentInstance);
            tx.commit();
        } catch (HibernateException e) {
            throw new DAOException("Error: entidad no conocida o no válida, " + e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw new DAOException("Error: Argumentos no válidos, " + e2.getMessage());
        } finally {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
                session.flush();
            } catch (ObjectDeletedException ex) {
                throw new DAOException("Error: al eliminar registro, " + ex.getMessage());
            } catch (ConstraintViolationException ex2) {
                throw new DAOException("Error: al eliminar registro, " + ex2.getMessage());
            }
        }
    }

    @Override
    public <T extends Serializable> void delete(final List<T> instances) throws DAOException {
        try {
            tx = session.beginTransaction();
            if (instances != null && !instances.isEmpty()) {
                for (T instance : instances) {
                    session.delete(instance);
                }
            }
            tx.commit();
        } catch (HibernateException e) {
            throw new DAOException("Error: entidad no conocida o no válida, " + e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw new DAOException("Error: entidad no conocida o no válida, " + e2.getMessage());
        } finally {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
                session.flush();
            } catch (ObjectDeletedException ex) {
                throw new DAOException("Error: al eliminar registro, " + ex.getMessage());
            } catch (ConstraintViolationException ex2) {
                throw new DAOException("Error: al eliminar registro, " + ex2.getMessage());
            }
        }
    }

    @Override
    public <T extends Serializable> void saveOrUpdate(final T instance) throws DAOException {
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(instance);
            tx.commit();
        } catch (HibernateException e) {           
            String message;
            message = e.getMessage();
            throw new DAOException("Error al guardar la entidad: entidad no conocida o no válida, " + message);
        } catch (IllegalArgumentException e2) {
            String message;
            message = e2.getMessage();
            throw new DAOException("Error al guardar la entidad: entidad no conocida o no válida, " + message);
        } finally {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                String message;
                if (ex instanceof Throwable) {
                    message = ex.getCause().getMessage();
                } else {
                    message = ex.getMessage();
                }
                throw new DAOException("Error: No se puede guardar el registro, " + message);
            }
        }
    }

    @Override
    public <T extends Serializable> void saveOrUpdateAll(final List<T> instances) throws DAOException {
        try {
            tx = session.beginTransaction();
            if (instances != null) {
                for (T instance : instances) {
                    session.saveOrUpdate(instance);
                }
            }
            tx.commit();
        } catch (HibernateException e) {
            throw new DAOException("Error: entidad no conocida o no válida, " + e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw new DAOException("Error: entidad no conocida o no válida, " + e2.getMessage());
        } finally {
            if (tx.wasCommitted()) {
                session.flush();
            }
            try {
                if (tx.isActive()) {
                    tx.rollback();
                    session.clear();
                }
            } catch (Exception ex) {
                throw new DAOException("Error: No se puede guardar el registro, " + ex.getMessage());
            }
        }
    }

    @Override
    public <T extends Serializable> List< T> findByQuery(
            final Class clase,
            final String query) throws DAOException {
        List<T> elementos = null;
        Query queryHql;
        try {
            queryHql = session.createQuery(query);
            elementos = queryHql.list();
            if (elementos != null && !elementos.isEmpty()) {
                T muestra = elementos.get(0);
                if (muestra.getClass() == clase) {
                    return elementos;
                } else {
                    throw new DAOException("Error: Clases no compatibles");
                }
            }
        } catch (HibernateException e) {
            throw new DAOException("Error no identificado: " + e.getMessage());
        } catch (IllegalArgumentException e2) {
            throw new DAOException("Error no identificado: " + e2.getMessage());
        } finally {
            session.flush();
        }
        return elementos;
    }

    @Override
    public <T extends Serializable> List<T> findByComponent(
            final Class clase,
            final String columna,
            final Object valor) throws DAOException {
        List<T> elementos = null;
        Query queryHql;
        try {
            queryHql = session.createQuery("SELECT c FROM " + clase.getName() + " c WHERE c." + columna + " = :valor");
            queryHql.setParameter("valor", valor);
            elementos = queryHql.list();
            if (elementos != null && !elementos.isEmpty()) {
                T muestra = elementos.get(0);
                if (muestra.getClass() == clase) {
                    return elementos;
                } else {
                    throw new DAOException("Error: Clases no compatibles");
                }
            }
        } catch (HibernateException e) {
            throw new DAOException("Error no identificado: " + e.getMessage());
        } finally {
            session.flush();
        }
        return elementos;
    }

    @Override
    public boolean excecuteNativeDDLSQL(final String sqlNative) throws DAOException {
        Query sql;
        try {
            if (sqlNative != null) {
                sql = session.createSQLQuery(sqlNative);
                sql.executeUpdate();
            } else {
                throw new DAOException("Error, can not excecute sentence: " + sqlNative);
            }
        } catch (HibernateException e) {
            sql = session.createSQLQuery("ROLLBACK");
            sql.executeUpdate();
            throw new DAOException("Error, can not excecute sentence: " + e.getMessage());
        } finally {
            session.flush();
        }
        return true;
    }
}
