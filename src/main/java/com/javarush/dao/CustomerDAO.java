package com.javarush.dao;

import com.javarush.domain.Customer;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class CustomerDAO extends AbstractDAO<Customer> {
    public CustomerDAO(SessionFactory sessionFactory) {
        super(Customer.class, sessionFactory);
    }

    @Override
    public Customer getById(int id) {
        return getCurrentSession().get(Customer.class, (short) id);
    }

    public Customer getRandomCustomer(){
        Query<Long> query = getCurrentSession().createQuery("select COUNT(*) from Customer c");
        Long count = query.getSingleResult();
        int randomId = (int)(Math.random()*count.intValue() + 1);
        return getById(randomId);
    }
}
