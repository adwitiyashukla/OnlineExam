package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.onlineexam.dao.AdminDAO;
import com.onlineexam.dao.StudentDAO;
import com.onlineexam.dao.TeacherDAO;
import com.onlineexam.model.Admin;
import com.onlineexam.model.Student;
import com.onlineexam.model.Teacher;
import com.onlineexam.util.PasswordUtil;
import com.onlineexam.util.WebUtil;

/**
 * Handles login for all three roles (student, teacher, admin). The {@code role}
 * request parameter selects which account table to authenticate against. On
 * success a session is created carrying the role and the user object.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final StudentDAO studentDAO = new StudentDAO();
    private final TeacherDAO teacherDAO = new TeacherDAO();
    private final AdminDAO adminDAO = new AdminDAO();

    /** Show the login form. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String role = normaliseRole(request.getParameter("role"));
        request.setAttribute("role", role);
        if ("1".equals(request.getParameter("denied"))) {
            request.setAttribute("error", "Please log in to continue.");
        }
        if ("1".equals(request.getParameter("registered"))) {
            request.setAttribute("info", "Registration successful — please log in.");
        }
        forwardToForm(request, response);
    }

    /** Process a login attempt. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String role = normaliseRole(request.getParameter("role"));
        String email = WebUtil.clean(request.getParameter("email"));
        String password = WebUtil.clean(request.getParameter("password"));

        request.setAttribute("role", role);
        request.setAttribute("email", email); // preserve entered email on error

        if (WebUtil.isBlank(email) || WebUtil.isBlank(password)) {
            request.setAttribute("error", "Email and password are required.");
            forwardToForm(request, response);
            return;
        }

        boolean success = false;
        HttpSession session = request.getSession();

        switch (role) {
            case "teacher": {
                Teacher t = teacherDAO.findByEmail(email);
                if (t != null && PasswordUtil.verify(password, t.getPassword())) {
                    session.setAttribute("role", "TEACHER");
                    session.setAttribute("teacher", t);
                    session.setAttribute("userName", t.getName());
                    success = true;
                }
                break;
            }
            case "admin": {
                Admin a = adminDAO.findByEmail(email);
                if (a != null && PasswordUtil.verify(password, a.getPassword())) {
                    session.setAttribute("role", "ADMIN");
                    session.setAttribute("admin", a);
                    session.setAttribute("userName", "Administrator");
                    success = true;
                }
                break;
            }
            default: { // student
                Student s = studentDAO.findByEmail(email);
                if (s != null && PasswordUtil.verify(password, s.getPassword())) {
                    session.setAttribute("role", "STUDENT");
                    session.setAttribute("student", s);
                    session.setAttribute("userName", s.getName());
                    success = true;
                }
                break;
            }
        }

        if (success) {
            response.sendRedirect(request.getContextPath() + "/" + role + "/dashboard");
        } else {
            request.setAttribute("error", "Invalid email or password.");
            forwardToForm(request, response);
        }
    }

    private void forwardToForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    private String normaliseRole(String role) {
        role = WebUtil.clean(role).toLowerCase();
        if (role.equals("teacher") || role.equals("admin")) {
            return role;
        }
        return "student";
    }
}
