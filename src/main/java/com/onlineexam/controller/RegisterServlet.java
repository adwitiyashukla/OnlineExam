package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.StudentDAO;
import com.onlineexam.dao.TeacherDAO;
import com.onlineexam.model.Student;
import com.onlineexam.model.Teacher;
import com.onlineexam.util.PasswordUtil;
import com.onlineexam.util.WebUtil;

/**
 * Handles self-registration for students and teachers. Passwords are hashed
 * before storage and duplicate emails are rejected. (Admin accounts are not
 * self-registerable and are seeded directly in the database.)
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int MIN_PASSWORD = 6;

    private final StudentDAO studentDAO = new StudentDAO();
    private final TeacherDAO teacherDAO = new TeacherDAO();

    /** Show the registration form. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String role = normaliseRole(request.getParameter("role"));
        request.setAttribute("role", role);
        forwardToForm(request, response);
    }

    /** Process a registration attempt. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String role = normaliseRole(request.getParameter("role"));
        String name = WebUtil.clean(request.getParameter("name"));
        String email = WebUtil.clean(request.getParameter("email"));
        String password = WebUtil.clean(request.getParameter("password"));

        request.setAttribute("role", role);
        request.setAttribute("name", name);
        request.setAttribute("email", email);

        // ---- server-side validation ----
        if (WebUtil.isBlank(name) || WebUtil.isBlank(email) || WebUtil.isBlank(password)) {
            fail(request, response, "All fields are required.");
            return;
        }
        if (!WebUtil.isValidEmail(email)) {
            fail(request, response, "Please enter a valid email address.");
            return;
        }
        if (password.length() < MIN_PASSWORD) {
            fail(request, response, "Password must be at least " + MIN_PASSWORD + " characters.");
            return;
        }

        String hashed = PasswordUtil.hash(password);

        if ("teacher".equals(role)) {
            if (teacherDAO.emailExists(email)) {
                fail(request, response, "An account with that email already exists.");
                return;
            }
            teacherDAO.register(new Teacher(0, name, email, hashed));
        } else {
            if (studentDAO.emailExists(email)) {
                fail(request, response, "An account with that email already exists.");
                return;
            }
            studentDAO.register(new Student(0, name, email, hashed));
        }

        response.sendRedirect(request.getContextPath() + "/login?role=" + role + "&registered=1");
    }

    /** Set an error message and re-show the registration form. */
    private void fail(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        forwardToForm(request, response);
    }

    private void forwardToForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    private String normaliseRole(String role) {
        role = WebUtil.clean(role).toLowerCase();
        return role.equals("teacher") ? "teacher" : "student";
    }
}
