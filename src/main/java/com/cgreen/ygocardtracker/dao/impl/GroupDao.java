package com.cgreen.ygocardtracker.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import com.cgreen.ygocardtracker.card.Group;
import com.cgreen.ygocardtracker.dao.Dao;
import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.db.Queries;

import javafx.collections.ObservableList;

public class GroupDao implements Dao<Group> {
    private ObservableList<Group> allGroups;
    
    @Override
    public ObservableList<Group> getAll() throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
            Connection conn = dbm.connectToDatabase();
            PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("select_group_table_query"));    
        ) {            
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {                
                Group grp = new Group();
                grp.setName(rs.getString("name"));
                grp.setId(rs.getInt("ID"));
                allGroups.add(grp);
            }
        }
        return allGroups;
    }

    @Override
    public void save(Group grp) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
            Connection conn = dbm.connectToDatabase();
            PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("insert_into_group_table_statement"), Statement.RETURN_GENERATED_KEYS);    
        ) {            
            stmt.setObject(1, Objects.requireNonNull(grp.getName(), "Group name must have a value."));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                grp.setId((int) rs.getLong(1));
            }
            allGroups.add(grp);
        }
    }

    @Override
    public void delete(Group grp) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        try (
            Connection conn = dbm.connectToDatabase();
            PreparedStatement stmt = conn.prepareStatement(Queries.getQuery("delete_from_group_table_statement"));    
        ) {
            stmt.setInt(1, grp.getId());
            stmt.executeUpdate();
            allGroups.remove(grp);
        }
    }

}
