<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    List<Subject> subjects = (List<Subject>) request.getAttribute("subjects");
    String error = (String) request.getAttribute("error");
    request.setAttribute("pageTitle", "Take a Quiz");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="page-head">
  <h1>Take a Quiz</h1>
  <p>Select a subject to begin. Each quiz is timed, so stay sharp!</p>
</div>

<% if (error != null) { %><div class="alert alert-error"><%= WebUtil.escape(error) %></div><% } %>

<% if (subjects == null || subjects.isEmpty()) { %>
  <div class="card empty-state">
    No quizzes are available yet. Please check back once a teacher has added questions.
  </div>
<% } else { %>
  <div class="card form-wrap">
    <form method="post" action="<%= ctx %>/student/quiz">
      <input type="hidden" name="action" value="start">
      <div class="form-group">
        <label for="subject">Subject</label>
        <select class="form-control" id="subject" name="subject" required>
          <option value="">Choose a subject</option>
          <% for (Subject s : subjects) { %>
            <option value="<%= WebUtil.escape(s.getCode()) %>">
              <%= WebUtil.escape(s.getCode()) %> - <%= WebUtil.escape(s.getName()) %>
              (<%= s.getQuestionCount() %> questions)
            </option>
          <% } %>
        </select>
      </div>
      <button class="btn btn-primary btn-block btn-lg" type="submit">Start Quiz</button>
    </form>
  </div>
<% } %>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
