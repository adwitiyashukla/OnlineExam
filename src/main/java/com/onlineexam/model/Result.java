package com.onlineexam.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Represents one completed quiz attempt by a student.
 * {@code studentName} and {@code subjectName} are display-only fields
 * populated by table joins.
 */
public class Result {

    private int id;
    private int studentId;
    private String subjectCode;
    private int score;
    private int total;
    private Timestamp attemptedAt;

    private String studentName;   // display only (from JOIN)
    private String subjectName;   // display only (from JOIN)

    public Result() {
    }

    public Result(int studentId, String subjectCode, int score, int total) {
        this.studentId = studentId;
        this.subjectCode = subjectCode;
        this.score = score;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Timestamp getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(Timestamp attemptedAt) {
        this.attemptedAt = attemptedAt;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /** Percentage score rounded to a whole number (0-100). */
    public int getPercentage() {
        if (total == 0) {
            return 0;
        }
        return Math.round((score * 100.0f) / total);
    }

    /** Human-friendly attempt date, e.g. "20 Jul 2026, 10:15". */
    public String getAttemptedAtDisplay() {
        if (attemptedAt == null) {
            return "-";
        }
        return new SimpleDateFormat("dd MMM yyyy, HH:mm").format(attemptedAt);
    }
}
