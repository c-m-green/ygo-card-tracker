package com.cgreen.ygocardtracker;

import javafx.collections.ObservableList;

public interface Dao<T> {
    ObservableList<T> getAll();
    
    void save(T t);
    
    void update(T t, String[] params);
    
    void delete(T t);
}
