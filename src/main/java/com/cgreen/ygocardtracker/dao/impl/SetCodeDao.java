package com.cgreen.ygocardtracker.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import com.cgreen.ygocardtracker.db.DatabaseManager;
import com.cgreen.ygocardtracker.db.Queries;
import com.cgreen.ygocardtracker.util.AlertHelper;

public class SetCodeDao {

    public SetCodeDao() { }
    
    public String get(Integer id) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        String out = "";
        try {            
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("select_set_code_by_id_query"));
            stmt.setInt(1, id);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
                out = rs.getString("set_codes");
            }
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return out;
    }

    public int save(String setCodes) throws SQLException {
        if (setCodes.isBlank()) {
            throw new IllegalArgumentException("Set codes cannot be blank.");
        }
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        int generatedId = -1;
        try {
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("insert_into_set_code_table_statement"), Statement.RETURN_GENERATED_KEYS);           
            stmt.setObject(1, Objects.requireNonNull(setCodes, "Set codes must have a value."));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException sqle) {
            throw sqle;
        } catch (IllegalArgumentException iae) {
            AlertHelper.raiseAlert(iae.getMessage());
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return generatedId;
    }
    
    public void update(int id, String setCodes) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getDatabaseManager();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dbm.connectToDatabase();
            stmt = conn.prepareStatement(Queries.getQuery("update_set_code_table_statement"));           
            stmt.setString(1, setCodes);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void delete(String setCode) throws SQLException {
        // TODO Auto-generated method stub
        
    }

}
