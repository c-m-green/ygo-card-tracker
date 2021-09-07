package com.cgreen.ygocardtracker.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class Queries {
    private static final String PROPERTIES_FILENAME = "queries.properties";
    private static Properties props;
    
    private static Properties getQueries() throws SQLException {
        InputStream is = Queries.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
        if (is == null) {
            throw new SQLException("Unable to load property file: " + PROPERTIES_FILENAME);
        }
        if (props == null) {
            props = new Properties();
            try {
                props.load(is);
            } catch (IOException e) {
                throw new SQLException("Unable to load property file:" + PROPERTIES_FILENAME + "\n" + e.getMessage());
            }
        }
        return props;
    }
    
    public static String getQuery(String query) throws SQLException {
        return getQueries().getProperty(query);
    }
}
