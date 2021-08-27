package com.cgreen.ygocardtracker;

import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MyTableItemDao implements Dao<MyTableItem> {
    
    private ObservableList<MyTableItem> collectionItems;
    
    public MyTableItemDao() {
        collectionItems = FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<MyTableItem> getAll() {
        return collectionItems;
    }

    @Override
    public void save(MyTableItem c) {
        collectionItems.add(c);
    }

    @Override
    public void update(MyTableItem c, String[] params) {
        c.setCol1(Objects.requireNonNull(params[0], "Column 1 must not be null."));
        c.setCol2(Objects.requireNonNull(Integer.parseInt(params[1]), "Column 2 must not be null."));
        c.setCol3(Objects.requireNonNull(params[2], "Column 3 must not be null."));
        c.setCol4(Objects.requireNonNull(params[3], "Column 4 must not be null."));
        collectionItems.add(c);
    }

    @Override
    public void delete(MyTableItem c) {
        collectionItems.remove(c);
    }

}
