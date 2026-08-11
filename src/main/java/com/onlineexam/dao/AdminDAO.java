package com.onlineexam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.onlineexam.model.Admin;

public class AdminDAO {

    private static final Logger LOGGER = Logger.getLogger(AdminDAO.class.getName());

    public Admin findByEmail(String email) {
        String sql = "SELECT id, email, password FROM admin WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(rs.getInt("id"), rs.getString("email"),
                            rs.getString("password"));
                }
            }
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByEmail failed for " + email, e);
            throw new DataAccessException("Unable to load admin", e);
        }
    }
}
