package com.onlineexam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.onlineexam.model.Student;

public class StudentDAO {

    private static final Logger LOGGER = Logger.getLogger(StudentDAO.class.getName());

    public boolean register(Student s) {
        String sql = "INSERT INTO student (name, email, password) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPassword());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "register failed for " + s.getEmail(), e);
            throw new DataAccessException("Unable to register student", e);
        }
    }

    public Student findByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM student WHERE email = ?";
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
            throw new DataAccessException("Unable to load student", e);
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM student WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "emailExists failed for " + email, e);
            throw new DataAccessException("Unable to check student email", e);
        }
    }

    public List<Student> findAll() {
        String sql = "SELECT id, name, email FROM student ORDER BY id";
        List<Student> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                list.add(s);
            }
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findAll students failed", e);
            throw new DataAccessException("Unable to list students", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "delete student failed for id " + id, e);
            throw new DataAccessException("Unable to delete student", e);
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM student";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "count students failed", e);
            throw new DataAccessException("Unable to count students", e);
        }
    }

    private Student map(ResultSet rs) throws SQLException {
        return new Student(rs.getInt("id"), rs.getString("name"),
                rs.getString("email"), rs.getString("password"));
    }
}
