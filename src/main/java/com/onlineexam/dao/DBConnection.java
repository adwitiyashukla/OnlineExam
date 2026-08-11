package com.onlineexam.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {

    private static final String url;
    private static final String user;
    private static final String password;

    static {
        try (InputStream in = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (in == null) {
                throw new IllegalStateException(
                        "db.properties not found on the classpath (expected in WEB-INF/classes)");
            }
            Properties props = new Properties();
            props.load(in);

            Class.forName(props.getProperty("db.driver"));

            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");

        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "Failed to initialise database configuration: " + e.getMessage());
        }
    }

    private DBConnection() {

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
