package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.ResultDAO;
import com.onlineexam.model.Student;

/** Shows a student their full quiz attempt history. */
@WebServlet("/student/results")
public class StudentResultServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Student student = (Student) request.getSession().getAttribute("student");
        request.setAttribute("results", resultDAO.findByStudent(student.getId()));
        request.getRequestDispatcher("/WEB-INF/views/student/history.jsp").forward(request, response);
    }
}
