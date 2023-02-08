package com.javarush.dao;

import com.javarush.domain.Inventory;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class InventoryDAO extends AbstractDAO<Inventory> {
    public InventoryDAO(SessionFactory sessionFactory) {
        super(Inventory.class, sessionFactory);
    }

    public Inventory getNotRentedInventory(){
        String hql = "select i From Inventory i where i.id not in (select r.inventory.id from Rental r where r.returnDate is null )";
        Query<Inventory> query = getCurrentSession().createQuery(hql, Inventory.class);
        query.setMaxResults(1);
        return query.getSingleResult();
    }
}
