package com.javarush.dao;

import com.javarush.domain.Customer;
import com.javarush.domain.Rental;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class RentalDAO extends AbstractDAO<Rental> {
    public RentalDAO(SessionFactory sessionFactory) {
        super(Rental.class, sessionFactory);
    }

    public List<Rental> getRentalByCustomer(Customer customer){
        if (customer != null) {
            Query<Rental> query = getCurrentSession().createQuery("select r from Rental r where r.customer = :customer");
            query.setParameter("customer", customer);
            return query.getResultList();
        }
        return null;
    }
}
