package com.cgreen.ygocardtracker.dao;

import java.sql.SQLException;

import javafx.collections.ObservableList;

public interface Dao<T> {
    ObservableList<T> getAll() throws SQLException;
    
    void save(T t) throws SQLException;
        
    void delete(T t) throws SQLException;
}
