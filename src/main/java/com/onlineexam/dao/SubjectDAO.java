package com.onlineexam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.onlineexam.model.Subject;

public class SubjectDAO {

    private static final Logger LOGGER = Logger.getLogger(SubjectDAO.class.getName());

    public boolean add(Subject s) {
        String sql = "INSERT INTO subject (code, name, teacher_id) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getCode());
            ps.setString(2, s.getName());
            ps.setInt(3, s.getTeacherId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "add subject failed for " + s.getCode(), e);
            throw new DataAccessException("Unable to add subject", e);
        }
    }

    public boolean exists(String code) {
        String sql = "SELECT 1 FROM subject WHERE code = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "exists subject failed for " + code, e);
            throw new DataAccessException("Unable to check subject", e);
        }
    }

    public Subject findByCode(String code) {
        String sql = """
                SELECT s.code, s.name, s.teacher_id, t.name AS teacher_name
                FROM subject s
                JOIN teacher t ON s.teacher_id = t.id
                WHERE s.code = ?""";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Subject s = new Subject(rs.getString("code"), rs.getString("name"),
                            rs.getInt("teacher_id"));
                    s.setTeacherName(rs.getString("teacher_name"));
                    return s;
                }
            }
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByCode failed for " + code, e);
            throw new DataAccessException("Unable to load subject", e);
        }
    }

    public List<Subject> findByTeacher(int teacherId) {
        String sql = """
                SELECT s.code, s.name, s.teacher_id, t.name AS teacher_name,
                       (SELECT COUNT(*) FROM question q WHERE q.subject_code = s.code) AS qcount
                FROM subject s
                JOIN teacher t ON s.teacher_id = t.id
                WHERE s.teacher_id = ?
                ORDER BY s.code""";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                return mapList(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByTeacher failed for " + teacherId, e);
            throw new DataAccessException("Unable to list subjects", e);
        }
    }

    public List<Subject> findAll() {
        String sql = """
                SELECT s.code, s.name, s.teacher_id, t.name AS teacher_name,
                       (SELECT COUNT(*) FROM question q WHERE q.subject_code = s.code) AS qcount
                FROM subject s
                JOIN teacher t ON s.teacher_id = t.id
                ORDER BY s.code""";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return mapList(rs);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findAll subjects failed", e);
            throw new DataAccessException("Unable to list subjects", e);
        }
    }

    public List<Subject> findPlayable() {
        String sql = """
                SELECT s.code, s.name, s.teacher_id, t.name AS teacher_name,
                       COUNT(q.id) AS qcount
                FROM subject s
                JOIN teacher t ON s.teacher_id = t.id
                JOIN question q ON q.subject_code = s.code
                GROUP BY s.code, s.name, s.teacher_id, t.name
                ORDER BY s.code""";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return mapList(rs);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findPlayable subjects failed", e);
            throw new DataAccessException("Unable to list subjects", e);
        }
    }

    public boolean update(Subject s) {
        String sql = "UPDATE subject SET name = ? WHERE code = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getCode());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "update subject failed for " + s.getCode(), e);
            throw new DataAccessException("Unable to update subject", e);
        }
    }

    public boolean delete(String code) {
        String sql = "DELETE FROM subject WHERE code = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, code);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "delete subject failed for " + code, e);
            throw new DataAccessException("Unable to delete subject", e);
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM subject";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "count subjects failed", e);
            throw new DataAccessException("Unable to count subjects", e);
        }
    }

    private List<Subject> mapList(ResultSet rs) throws SQLException {
        List<Subject> list = new ArrayList<>();
        while (rs.next()) {
            Subject s = new Subject(rs.getString("code"), rs.getString("name"),
                    rs.getInt("teacher_id"));
            s.setTeacherName(rs.getString("teacher_name"));
            s.setQuestionCount(rs.getInt("qcount"));
            list.add(s);
        }
        return list;
    }
}
