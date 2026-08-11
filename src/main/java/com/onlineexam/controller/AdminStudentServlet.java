package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.StudentDAO;
import com.onlineexam.util.WebUtil;

@WebServlet("/admin/students")
public class AdminStudentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("deleted".equals(WebUtil.clean(request.getParameter("msg")))) {
            request.setAttribute("info", "Student deleted.");
        }
        request.setAttribute("students", studentDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/students.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("delete".equals(WebUtil.clean(request.getParameter("action")))) {
            try {
                studentDAO.delete(Integer.parseInt(WebUtil.clean(request.getParameter("id"))));
            } catch (NumberFormatException ignored) {

            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/students?msg=deleted");
    }
}
