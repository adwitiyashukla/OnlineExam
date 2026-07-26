package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.TeacherDAO;
import com.onlineexam.util.WebUtil;

/** Admin management of teacher accounts (list and delete). */
@WebServlet("/admin/teachers")
public class AdminTeacherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final TeacherDAO teacherDAO = new TeacherDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("deleted".equals(WebUtil.clean(request.getParameter("msg")))) {
            request.setAttribute("info", "Teacher deleted (their subjects and questions were removed too).");
        }
        request.setAttribute("teachers", teacherDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/teachers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("delete".equals(WebUtil.clean(request.getParameter("action")))) {
            try {
                teacherDAO.delete(Integer.parseInt(WebUtil.clean(request.getParameter("id"))));
            } catch (NumberFormatException ignored) {
                // fall through
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/teachers?msg=deleted");
    }
}
