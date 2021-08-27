package com.cgreen.ygocardtracker;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MyTableItem {
    private SimpleStringProperty col1, col3, col4;
    private SimpleIntegerProperty col2;
    private int id;
    
    public MyTableItem() { 
        col1 = new SimpleStringProperty();
        col3 = new SimpleStringProperty();
        col4 = new SimpleStringProperty();
        
        col2 = new SimpleIntegerProperty();
    }
    
    public String getCol1() {
        return col1.get();
    }
    public void setCol1(String str) {
        this.col1.set(str);
    }
    public String getCol3() {
        return col3.get();
    }
    public void setCol3(String str) {
        this.col3.set(str);
    }
    public String getCol4() {
        return col4.get();
    }
    public void setCol4(String str) {
        this.col4.set(str);;
    }
    public int getCol2() {
        return col2.get();
    }
    public void setCol2(int i) {
        this.col2.set(i);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
