package com.onlineexam.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.ResultDAO;
import com.onlineexam.model.Result;
import com.onlineexam.model.Student;

/** Landing page for a logged-in student with quick stats and recent attempts. */
@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Student student = (Student) request.getSession().getAttribute("student");

        List<Result> history = resultDAO.findByStudent(student.getId());
        int best = 0;
        for (Result r : history) {
            best = Math.max(best, r.getPercentage());
        }

        request.setAttribute("attemptCount", history.size());
        request.setAttribute("bestPercentage", best);
        request.setAttribute("recent", history.size() > 5 ? history.subList(0, 5) : history);
        request.getRequestDispatcher("/WEB-INF/views/student/dashboard.jsp").forward(request, response);
    }
}
