package com.onlineexam.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.QuestionDAO;
import com.onlineexam.dao.ResultDAO;
import com.onlineexam.dao.SubjectDAO;
import com.onlineexam.model.Question;
import com.onlineexam.model.Result;
import com.onlineexam.model.Student;
import com.onlineexam.model.Subject;
import com.onlineexam.util.WebUtil;

@WebServlet("/student/quiz")
public class QuizServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showPicker(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = WebUtil.clean(request.getParameter("action"));
        if ("submit".equals(action)) {
            submit(request, response);
        } else {
            start(request, response);
        }
    }

    private void start(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = WebUtil.clean(request.getParameter("subject"));
        Subject subject = WebUtil.isBlank(code) ? null : subjectDAO.findByCode(code);
        if (subject == null) {
            showPicker(request, response, "Please choose a subject to begin.");
            return;
        }

        List<Question> questions = questionDAO.findBySubject(code);
        if (questions.isEmpty()) {
            showPicker(request, response, "That subject has no questions yet.");
            return;
        }

        request.setAttribute("subject", subject);
        request.setAttribute("questions", questions);
        request.getRequestDispatcher("/WEB-INF/views/student/quiz.jsp").forward(request, response);
    }

    private void submit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Student student = (Student) request.getSession().getAttribute("student");
        String code = WebUtil.clean(request.getParameter("subject"));
        Subject subject = WebUtil.isBlank(code) ? null : subjectDAO.findByCode(code);
        if (subject == null) {
            showPicker(request, response, "Something went wrong, please try again.");
            return;
        }

        List<Question> questions = questionDAO.findBySubject(code);
        Map<Integer, String> selected = new HashMap<>();
        int score = 0;

        for (Question q : questions) {
            String answer = WebUtil.clean(request.getParameter("q_" + q.getId()));
            selected.put(q.getId(), answer);
            if (!answer.isEmpty() && answer.equals(q.getCorrectAnswer())) {
                score++;
            }
        }
        int total = questions.size();

        resultDAO.save(new Result(student.getId(), code, score, total));

        request.setAttribute("subject", subject);
        request.setAttribute("questions", questions);
        request.setAttribute("selected", selected);
        request.setAttribute("score", score);
        request.setAttribute("total", total);
        request.setAttribute("percentage", total == 0 ? 0 : Math.round((score * 100.0f) / total));
        request.getRequestDispatcher("/WEB-INF/views/student/result.jsp").forward(request, response);
    }

    private void showPicker(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {

        List<Subject> subjects = subjectDAO.findPlayable();
        request.setAttribute("subjects", subjects);
        if (error != null) {
            request.setAttribute("error", error);
        }
        request.getRequestDispatcher("/WEB-INF/views/student/quiz-select.jsp").forward(request, response);
    }
}
