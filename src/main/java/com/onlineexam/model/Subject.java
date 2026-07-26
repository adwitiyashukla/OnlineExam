package com.onlineexam.model;

/**
 * Represents a subject / course that groups a set of quiz questions.
 * {@code teacherName} is a display-only field populated by table joins.
 */
public class Subject {

    private String code;
    private String name;
    private int teacherId;
    private String teacherName;   // display only (from JOIN)
    private int questionCount;    // display only (from COUNT)

    public Subject() {
    }

    public Subject(String code, String name, int teacherId) {
        this.code = code;
        this.name = name;
        this.teacherId = teacherId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
