package com.javarush.dao;

import com.javarush.domain.Film;
import org.hibernate.SessionFactory;

public class FilmDAO extends AbstractDAO<Film> {
    public FilmDAO(SessionFactory sessionFactory) {
        super(Film.class, sessionFactory);
    }
}
