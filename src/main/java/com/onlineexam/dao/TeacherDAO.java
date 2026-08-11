package com.onlineexam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.onlineexam.model.Teacher;

public class TeacherDAO {

    private static final Logger LOGGER = Logger.getLogger(TeacherDAO.class.getName());

    public boolean register(Teacher t) {
        String sql = "INSERT INTO teacher (name, email, password) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getName());
            ps.setString(2, t.getEmail());
            ps.setString(3, t.getPassword());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "register failed for " + t.getEmail(), e);
            throw new DataAccessException("Unable to register teacher", e);
        }
    }

    public Teacher findByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM teacher WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByEmail failed for " + email, e);
            throw new DataAccessException("Unable to load teacher", e);
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM teacher WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "emailExists failed for " + email, e);
            throw new DataAccessException("Unable to check teacher email", e);
        }
    }

    public List<Teacher> findAll() {
        String sql = "SELECT id, name, email FROM teacher ORDER BY id";
        List<Teacher> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Teacher t = new Teacher();
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                t.setEmail(rs.getString("email"));
                list.add(t);
            }
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findAll teachers failed", e);
            throw new DataAccessException("Unable to list teachers", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM teacher WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "delete teacher failed for id " + id, e);
            throw new DataAccessException("Unable to delete teacher", e);
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM teacher";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "count teachers failed", e);
            throw new DataAccessException("Unable to count teachers", e);
        }
    }

    private Teacher map(ResultSet rs) throws SQLException {
        return new Teacher(rs.getInt("id"), rs.getString("name"),
                rs.getString("email"), rs.getString("password"));
    }
}
