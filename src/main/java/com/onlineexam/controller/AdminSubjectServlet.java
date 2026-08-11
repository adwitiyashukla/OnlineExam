package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.SubjectDAO;
import com.onlineexam.util.WebUtil;

@WebServlet("/admin/subjects")
public class AdminSubjectServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final SubjectDAO subjectDAO = new SubjectDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("deleted".equals(WebUtil.clean(request.getParameter("msg")))) {
            request.setAttribute("info", "Subject deleted.");
        }
        request.setAttribute("subjects", subjectDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/subjects.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("delete".equals(WebUtil.clean(request.getParameter("action")))) {
            String code = WebUtil.clean(request.getParameter("code"));
            if (!WebUtil.isBlank(code)) {
                subjectDAO.delete(code);
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/subjects?msg=deleted");
    }
}
