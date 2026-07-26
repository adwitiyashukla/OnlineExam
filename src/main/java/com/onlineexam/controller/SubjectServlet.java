package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.SubjectDAO;
import com.onlineexam.model.Subject;
import com.onlineexam.model.Teacher;
import com.onlineexam.util.WebUtil;

/**
 * Lets a teacher manage their own subjects (create, rename, delete). All write
 * operations verify that the target subject actually belongs to the logged-in
 * teacher before making a change.
 */
@WebServlet("/teacher/subjects")
public class SubjectServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final SubjectDAO subjectDAO = new SubjectDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
        String action = WebUtil.clean(request.getParameter("action"));

        if ("edit".equals(action)) {
            Subject s = subjectDAO.findByCode(WebUtil.clean(request.getParameter("code")));
            if (s != null && s.getTeacherId() == teacher.getId()) {
                request.setAttribute("editSubject", s);
            }
        }
        translateMessage(request);
        request.setAttribute("subjects", subjectDAO.findByTeacher(teacher.getId()));
        request.getRequestDispatcher("/WEB-INF/views/teacher/subjects.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
        String action = WebUtil.clean(request.getParameter("action"));
        String code = WebUtil.clean(request.getParameter("code"));
        String name = WebUtil.clean(request.getParameter("name"));

        String base = request.getContextPath() + "/teacher/subjects";

        switch (action) {
            case "add":
                if (WebUtil.isBlank(code) || WebUtil.isBlank(name)) {
                    response.sendRedirect(base + "?msg=invalid");
                } else if (subjectDAO.exists(code)) {
                    response.sendRedirect(base + "?msg=duplicate");
                } else {
                    subjectDAO.add(new Subject(code, name, teacher.getId()));
                    response.sendRedirect(base + "?msg=added");
                }
                break;

            case "update": {
                Subject existing = subjectDAO.findByCode(code);
                if (existing == null || existing.getTeacherId() != teacher.getId() || WebUtil.isBlank(name)) {
                    response.sendRedirect(base + "?msg=denied");
                } else {
                    existing.setName(name);
                    subjectDAO.update(existing);
                    response.sendRedirect(base + "?msg=updated");
                }
                break;
            }

            case "delete": {
                Subject existing = subjectDAO.findByCode(code);
                if (existing == null || existing.getTeacherId() != teacher.getId()) {
                    response.sendRedirect(base + "?msg=denied");
                } else {
                    subjectDAO.delete(code);
                    response.sendRedirect(base + "?msg=deleted");
                }
                break;
            }

            default:
                response.sendRedirect(base);
        }
    }

    private void translateMessage(HttpServletRequest request) {
        switch (WebUtil.clean(request.getParameter("msg"))) {
            case "added":     request.setAttribute("info", "Subject added successfully."); break;
            case "updated":   request.setAttribute("info", "Subject updated successfully."); break;
            case "deleted":   request.setAttribute("info", "Subject deleted."); break;
            case "duplicate": request.setAttribute("error", "A subject with that code already exists."); break;
            case "invalid":   request.setAttribute("error", "Subject code and name are required."); break;
            case "denied":    request.setAttribute("error", "You can only modify your own subjects."); break;
            default: /* no message */ break;
        }
    }
}
