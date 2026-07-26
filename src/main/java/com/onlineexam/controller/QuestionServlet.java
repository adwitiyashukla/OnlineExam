package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.QuestionDAO;
import com.onlineexam.dao.SubjectDAO;
import com.onlineexam.model.Question;
import com.onlineexam.model.Subject;
import com.onlineexam.model.Teacher;
import com.onlineexam.util.WebUtil;

/**
 * Lets a teacher manage the questions inside one of their subjects. Every
 * request is scoped to a subject the teacher owns; the correct answer is chosen
 * by option number (1-4) so it always matches one of the stored options.
 */
@WebServlet("/teacher/questions")
public class QuestionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
        String code = WebUtil.clean(request.getParameter("subject"));
        Subject subject = ownedSubject(teacher, code);
        if (subject == null) {
            response.sendRedirect(request.getContextPath() + "/teacher/subjects?msg=denied");
            return;
        }

        if ("edit".equals(WebUtil.clean(request.getParameter("action")))) {
            Question q = questionDAO.findById(parseInt(request.getParameter("id")));
            if (q != null && q.getSubjectCode().equals(code)) {
                request.setAttribute("editQuestion", q);
            }
        }

        translateMessage(request);
        request.setAttribute("subject", subject);
        request.setAttribute("questions", questionDAO.findBySubject(code));
        request.getRequestDispatcher("/WEB-INF/views/teacher/questions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
        String code = WebUtil.clean(request.getParameter("subject"));
        Subject subject = ownedSubject(teacher, code);
        String listUrl = request.getContextPath() + "/teacher/questions?subject=" + code;
        if (subject == null) {
            response.sendRedirect(request.getContextPath() + "/teacher/subjects?msg=denied");
            return;
        }

        String action = WebUtil.clean(request.getParameter("action"));

        if ("delete".equals(action)) {
            Question q = questionDAO.findById(parseInt(request.getParameter("id")));
            if (q != null && q.getSubjectCode().equals(code)) {
                questionDAO.delete(q.getId());
                response.sendRedirect(listUrl + "&msg=deleted");
            } else {
                response.sendRedirect(listUrl + "&msg=denied");
            }
            return;
        }

        // ---- add / update share the same field parsing + validation ----
        String text = WebUtil.clean(request.getParameter("question"));
        String op1 = WebUtil.clean(request.getParameter("op1"));
        String op2 = WebUtil.clean(request.getParameter("op2"));
        String op3 = WebUtil.clean(request.getParameter("op3"));
        String op4 = WebUtil.clean(request.getParameter("op4"));
        int answerIndex = parseInt(request.getParameter("answerIndex"));

        boolean invalid = WebUtil.isBlank(text) || WebUtil.isBlank(op1) || WebUtil.isBlank(op2)
                || WebUtil.isBlank(op3) || WebUtil.isBlank(op4) || answerIndex < 1 || answerIndex > 4;
        if (invalid) {
            response.sendRedirect(listUrl + "&msg=invalid");
            return;
        }

        String correct = optionByIndex(answerIndex, op1, op2, op3, op4);

        if ("update".equals(action)) {
            Question q = questionDAO.findById(parseInt(request.getParameter("id")));
            if (q == null || !q.getSubjectCode().equals(code)) {
                response.sendRedirect(listUrl + "&msg=denied");
                return;
            }
            q.setQuestionText(text);
            q.setOption1(op1);
            q.setOption2(op2);
            q.setOption3(op3);
            q.setOption4(op4);
            q.setCorrectAnswer(correct);
            questionDAO.update(q);
            response.sendRedirect(listUrl + "&msg=updated");
        } else {
            Question q = new Question(0, code, text, op1, op2, op3, op4, correct);
            questionDAO.add(q);
            response.sendRedirect(listUrl + "&msg=added");
        }
    }

    /** @return the subject if it exists and belongs to this teacher, else null. */
    private Subject ownedSubject(Teacher teacher, String code) {
        if (WebUtil.isBlank(code)) {
            return null;
        }
        Subject s = subjectDAO.findByCode(code);
        return (s != null && s.getTeacherId() == teacher.getId()) ? s : null;
    }

    private String optionByIndex(int idx, String o1, String o2, String o3, String o4) {
        switch (idx) {
            case 1:  return o1;
            case 2:  return o2;
            case 3:  return o3;
            default: return o4;
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(WebUtil.clean(value));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void translateMessage(HttpServletRequest request) {
        switch (WebUtil.clean(request.getParameter("msg"))) {
            case "added":   request.setAttribute("info", "Question added successfully."); break;
            case "updated": request.setAttribute("info", "Question updated successfully."); break;
            case "deleted": request.setAttribute("info", "Question deleted."); break;
            case "invalid": request.setAttribute("error", "All fields are required and a correct option must be chosen."); break;
            case "denied":  request.setAttribute("error", "That question does not belong to this subject."); break;
            default: break;
        }
    }
}
