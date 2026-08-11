package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.QuestionDAO;
import com.onlineexam.dao.ResultDAO;
import com.onlineexam.dao.StudentDAO;
import com.onlineexam.dao.SubjectDAO;
import com.onlineexam.dao.TeacherDAO;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final StudentDAO studentDAO = new StudentDAO();
    private final TeacherDAO teacherDAO = new TeacherDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("studentCount", studentDAO.count());
        request.setAttribute("teacherCount", teacherDAO.count());
        request.setAttribute("subjectCount", subjectDAO.count());
        request.setAttribute("questionCount", questionDAO.count());
        request.setAttribute("resultCount", resultDAO.count());
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}
