package com.javarush.dao;

import com.javarush.domain.Rental;
import com.javarush.domain.Staff;
import com.javarush.domain.Store;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class StaffDAO extends AbstractDAO<Staff> {
    public StaffDAO(SessionFactory sessionFactory) {
        super(Staff.class, sessionFactory);
    }

    public Staff getStaffByStore(Store store){
        if (store != null) {
            Query<Staff> query = getCurrentSession().createQuery("select staff from Staff staff where staff.store = :store", Staff.class);
            query.setParameter("store", store);
            query.setMaxResults(1);
            return query.getSingleResult();
        }
        return null;
    }
}
