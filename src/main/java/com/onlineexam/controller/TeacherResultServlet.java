package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.ResultDAO;
import com.onlineexam.model.Teacher;

/** Shows a teacher every attempt made on the subjects they own. */
@WebServlet("/teacher/results")
public class TeacherResultServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
        request.setAttribute("results", resultDAO.findByTeacher(teacher.getId()));
        request.getRequestDispatcher("/WEB-INF/views/teacher/results.jsp").forward(request, response);
    }
}
