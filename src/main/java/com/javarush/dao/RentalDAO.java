package com.javarush.dao;

import com.javarush.domain.Rental;
import org.hibernate.SessionFactory;

public class RentalDAO extends AbstractDAO<Rental> {
    public RentalDAO(SessionFactory sessionFactory) {
        super(Rental.class, sessionFactory);
    }
}
