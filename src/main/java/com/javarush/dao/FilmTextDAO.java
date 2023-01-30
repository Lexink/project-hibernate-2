package com.javarush.dao;

import com.javarush.domain.FilmText;
import org.hibernate.SessionFactory;

public class FilmTextDAO extends AbstractDAO<FilmText> {
    public FilmTextDAO(SessionFactory sessionFactory) {
        super(FilmText.class, sessionFactory);
    }
}
