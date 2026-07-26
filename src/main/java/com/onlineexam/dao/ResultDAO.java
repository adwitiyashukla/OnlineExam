package com.onlineexam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.onlineexam.model.Result;

/** Data-access object for completed quiz attempts. */
public class ResultDAO {

    private static final Logger LOGGER = Logger.getLogger(ResultDAO.class.getName());

    /** Persist a completed quiz attempt (attempted_at defaults to now). */
    public boolean save(Result r) {
        String sql = "INSERT INTO result (student_id, subject_code, score, total) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getStudentId());
            ps.setString(2, r.getSubjectCode());
            ps.setInt(3, r.getScore());
            ps.setInt(4, r.getTotal());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "save result failed", e);
            throw new DataAccessException("Unable to save result", e);
        }
    }

    /** A student's own attempt history (most recent first). */
    public List<Result> findByStudent(int studentId) {
        String sql = """
                SELECT r.id, r.student_id, r.subject_code, r.score, r.total, r.attempted_at,
                       s.name AS subject_name
                FROM result r
                JOIN subject s ON r.subject_code = s.code
                WHERE r.student_id = ?
                ORDER BY r.attempted_at DESC""";
        List<Result> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Result r = base(rs);
                    r.setSubjectName(rs.getString("subject_name"));
                    list.add(r);
                }
            }
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByStudent failed for " + studentId, e);
            throw new DataAccessException("Unable to load results", e);
        }
    }

    /** All attempts on subjects owned by a given teacher (most recent first). */
    public List<Result> findByTeacher(int teacherId) {
        String sql = """
                SELECT r.id, r.student_id, r.subject_code, r.score, r.total, r.attempted_at,
                       st.name AS student_name, s.name AS subject_name
                FROM result r
                JOIN subject s ON r.subject_code = s.code
                JOIN student st ON r.student_id = st.id
                WHERE s.teacher_id = ?
                ORDER BY r.attempted_at DESC""";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                return mapWithNames(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findByTeacher results failed for " + teacherId, e);
            throw new DataAccessException("Unable to load results", e);
        }
    }

    /** Every attempt in the system (admin view, most recent first). */
    public List<Result> findAll() {
        String sql = """
                SELECT r.id, r.student_id, r.subject_code, r.score, r.total, r.attempted_at,
                       st.name AS student_name, s.name AS subject_name
                FROM result r
                JOIN subject s ON r.subject_code = s.code
                JOIN student st ON r.student_id = st.id
                ORDER BY r.attempted_at DESC""";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return mapWithNames(rs);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findAll results failed", e);
            throw new DataAccessException("Unable to load results", e);
        }
    }

    /** Top attempts ranked by percentage then raw score (for the leaderboard). */
    public List<Result> getLeaderboard(int limit) {
        String sql = """
                SELECT r.id, r.student_id, r.subject_code, r.score, r.total, r.attempted_at,
                       st.name AS student_name, s.name AS subject_name
                FROM result r
                JOIN subject s ON r.subject_code = s.code
                JOIN student st ON r.student_id = st.id
                ORDER BY (r.score / r.total) DESC, r.score DESC, r.attempted_at ASC
                LIMIT ?""";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                return mapWithNames(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getLeaderboard failed", e);
            throw new DataAccessException("Unable to load leaderboard", e);
        }
    }

    /** Total number of attempts (for admin dashboard stats). */
    public int count() {
        String sql = "SELECT COUNT(*) FROM result";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "count results failed", e);
            throw new DataAccessException("Unable to count results", e);
        }
    }

    private List<Result> mapWithNames(ResultSet rs) throws SQLException {
        List<Result> list = new ArrayList<>();
        while (rs.next()) {
            Result r = base(rs);
            r.setStudentName(rs.getString("student_name"));
            r.setSubjectName(rs.getString("subject_name"));
            list.add(r);
        }
        return list;
    }

    private Result base(ResultSet rs) throws SQLException {
        Result r = new Result();
        r.setId(rs.getInt("id"));
        r.setStudentId(rs.getInt("student_id"));
        r.setSubjectCode(rs.getString("subject_code"));
        r.setScore(rs.getInt("score"));
        r.setTotal(rs.getInt("total"));
        r.setAttemptedAt(rs.getTimestamp("attempted_at"));
        return r;
    }
}
