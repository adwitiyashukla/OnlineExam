package com.onlineexam.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.ResultDAO;
import com.onlineexam.dao.SubjectDAO;
import com.onlineexam.model.Result;
import com.onlineexam.model.Subject;
import com.onlineexam.model.Teacher;

/** Landing page for a teacher: their subjects, question totals and recent attempts. */
@WebServlet("/teacher/dashboard")
public class TeacherDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");

        List<Subject> subjects = subjectDAO.findByTeacher(teacher.getId());
        int questionTotal = 0;
        for (Subject s : subjects) {
            questionTotal += s.getQuestionCount();
        }
        List<Result> attempts = resultDAO.findByTeacher(teacher.getId());

        request.setAttribute("subjects", subjects);
        request.setAttribute("subjectCount", subjects.size());
        request.setAttribute("questionTotal", questionTotal);
        request.setAttribute("attemptCount", attempts.size());
        request.setAttribute("recentAttempts", attempts.size() > 5 ? attempts.subList(0, 5) : attempts);
        request.getRequestDispatcher("/WEB-INF/views/teacher/dashboard.jsp").forward(request, response);
    }
}
