package com.javarush.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public abstract class AbstractDAO<T> {
    private final Class<T> clazz;
    private final SessionFactory sessionFactory;

    public AbstractDAO(final Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    public T getById(final int id){
        return (T) sessionFactory.getCurrentSession().get(clazz, id);
    }

    public List<T> getItems(int offset, int count){
        Query query = sessionFactory.getCurrentSession().createQuery("from " + clazz.getName(), clazz);
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return query.getResultList();
    }

    public List<T> getAll(){
        return sessionFactory.getCurrentSession().createQuery("from " + clazz.getName(), clazz).list();
    }

    public T save(final T entity){
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    public T update(final T entity){
        return (T) sessionFactory.getCurrentSession().merge(entity);
    }

    public void deleteById(final int id){
        T entity = getById(id);
        sessionFactory.getCurrentSession().delete(entity);
    }

    public void delete(final T entity){
        sessionFactory.getCurrentSession().delete(entity);
    }
}
