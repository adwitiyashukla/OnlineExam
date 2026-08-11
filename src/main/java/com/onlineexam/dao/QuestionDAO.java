package com.onlineexam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.onlineexam.model.Question;

public class QuestionDAO {

    private static final Logger LOGGER = Logger.getLogger(QuestionDAO.class.getName());

    public boolean add(Question q) {
        String sql = """
                INSERT INTO question
                    (subject_code, question_text, option1, option2, option3, option4, correct_answer)
                VALUES (?, ?, ?, ?, ?, ?, ?)""";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            bind(ps, q);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "add question failed", e);
            throw new DataAccessException("Unable to add question", e);
        }
    }

    public List<Question> findBySubject(String subjectCode) {
        String sql = """
                SELECT id, subject_code, question_text, option1, option2, option3, option4, correct_answer
                FROM question WHERE subject_code = ? ORDER BY id""";
        List<Question> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, subjectCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findBySubject failed for " + subjectCode, e);
            throw new DataAccessException("Unable to load questions", e);
        }
    }

    public Question findById(int id) {
        String sql = """
                SELECT id, subject_code, question_text, option1, option2, option3, option4, correct_answer
                FROM question WHERE id = ?""";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "findById question failed for " + id, e);
            throw new DataAccessException("Unable to load question", e);
        }
    }

    public boolean update(Question q) {
        String sql = """
                UPDATE question SET
                    question_text = ?, option1 = ?, option2 = ?, option3 = ?, option4 = ?, correct_answer = ?
                WHERE id = ?""";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, q.getQuestionText());
            ps.setString(2, q.getOption1());
            ps.setString(3, q.getOption2());
            ps.setString(4, q.getOption3());
            ps.setString(5, q.getOption4());
            ps.setString(6, q.getCorrectAnswer());
            ps.setInt(7, q.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "update question failed for " + q.getId(), e);
            throw new DataAccessException("Unable to update question", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM question WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "delete question failed for " + id, e);
            throw new DataAccessException("Unable to delete question", e);
        }
    }

    public int countBySubject(String subjectCode) {
        String sql = "SELECT COUNT(*) FROM question WHERE subject_code = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, subjectCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "countBySubject failed for " + subjectCode, e);
            throw new DataAccessException("Unable to count questions", e);
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM question";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "count questions failed", e);
            throw new DataAccessException("Unable to count questions", e);
        }
    }

    private void bind(PreparedStatement ps, Question q) throws SQLException {
        ps.setString(1, q.getSubjectCode());
        ps.setString(2, q.getQuestionText());
        ps.setString(3, q.getOption1());
        ps.setString(4, q.getOption2());
        ps.setString(5, q.getOption3());
        ps.setString(6, q.getOption4());
        ps.setString(7, q.getCorrectAnswer());
    }

    private Question map(ResultSet rs) throws SQLException {
        return new Question(
                rs.getInt("id"),
                rs.getString("subject_code"),
                rs.getString("question_text"),
                rs.getString("option1"),
                rs.getString("option2"),
                rs.getString("option3"),
                rs.getString("option4"),
                rs.getString("correct_answer"));
    }
}
