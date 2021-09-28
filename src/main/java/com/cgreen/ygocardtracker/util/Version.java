package com.cgreen.ygocardtracker.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.cgreen.ygocardtracker.db.Queries;

public final class Version {
    private static final String VERSION_FILENAME = "version.properties";
    private static Properties props;
    
    public static String getVersion() throws IOException {
        InputStream is = Queries.class.getClassLoader().getResourceAsStream(VERSION_FILENAME);
        if (is == null) {
            throw new IOException("Unable to load property file: " + VERSION_FILENAME);
        }
        if (props == null) {
            props = new Properties();
            props.load(is);
        }
        return props.getProperty("version");
    }
}
