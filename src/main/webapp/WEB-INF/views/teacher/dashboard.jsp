<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Teacher teacher = (Teacher) session.getAttribute("teacher");
    List<Subject> subjects = (List<Subject>) request.getAttribute("subjects");
    int subjectCount = (Integer) request.getAttribute("subjectCount");
    int questionTotal = (Integer) request.getAttribute("questionTotal");
    int attemptCount = (Integer) request.getAttribute("attemptCount");
    List<Result> recentAttempts = (List<Result>) request.getAttribute("recentAttempts");
    request.setAttribute("pageTitle", "Teacher Dashboard");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="page-head">
  <h1>Welcome, <%= WebUtil.escape(teacher.getName()) %></h1>
  <p>Manage your subjects and questions, and track how students are performing.</p>
</div>

<div class="card-grid mb">
  <div class="stat"><div class="stat-value"><%= subjectCount %></div><div class="stat-label">Subjects</div></div>
  <div class="stat"><div class="stat-value"><%= questionTotal %></div><div class="stat-label">Questions</div></div>
  <div class="stat"><div class="stat-value"><%= attemptCount %></div><div class="stat-label">Student Attempts</div></div>
</div>

<div class="card-grid">
  <a class="tile" href="<%= ctx %>/teacher/subjects">
    <h3>Manage Subjects</h3>
    <p>Create subjects and add or edit their multiple-choice questions.</p>
  </a>
  <a class="tile" href="<%= ctx %>/teacher/results">
    <h3>Student Attempts</h3>
    <p>See every quiz attempt made on the subjects you own.</p>
  </a>
  <a class="tile" href="<%= ctx %>/leaderboard">
    <h3>Leaderboard</h3>
    <p>View the top-scoring attempts across the platform.</p>
  </a>
</div>

<div class="flex between items-center mt">
  <h2 class="section-title" style="margin:0;">Your subjects</h2>
  <a class="btn btn-primary btn-sm" href="<%= ctx %>/teacher/subjects">+ New subject</a>
</div>
<% if (subjects == null || subjects.isEmpty()) { %>
  <div class="card empty-state" style="margin-top:12px;">
    You have not created any subjects yet.
    <div class="mt"><a class="btn btn-primary" href="<%= ctx %>/teacher/subjects">Create a subject</a></div>
  </div>
<% } else { %>
  <div class="table-wrap" style="margin-top:12px;">
    <table class="table">
      <thead><tr><th>Code</th><th>Name</th><th>Questions</th><th class="text-right">Manage</th></tr></thead>
      <tbody>
      <% for (Subject s : subjects) { %>
        <tr>
          <td><span class="badge badge-muted"><%= WebUtil.escape(s.getCode()) %></span></td>
          <td><%= WebUtil.escape(s.getName()) %></td>
          <td><%= s.getQuestionCount() %></td>
          <td class="text-right"><a class="btn btn-outline btn-sm" href="<%= ctx %>/teacher/questions?subject=<%= WebUtil.escape(s.getCode()) %>">Questions</a></td>
        </tr>
      <% } %>
      </tbody>
    </table>
  </div>
<% } %>

<h2 class="section-title">Recent attempts</h2>
<% if (recentAttempts == null || recentAttempts.isEmpty()) { %>
  <div class="card empty-state">No attempts on your subjects yet.</div>
<% } else { %>
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>Student</th><th>Subject</th><th>Score</th><th>%</th><th>Date</th></tr></thead>
      <tbody>
      <% for (Result r : recentAttempts) {
           int pct = r.getPercentage();
           String cls = pct >= 75 ? "badge-success" : pct >= 40 ? "badge-warning" : "badge-danger"; %>
        <tr>
          <td><%= WebUtil.escape(r.getStudentName()) %></td>
          <td><%= WebUtil.escape(r.getSubjectName()) %></td>
          <td><%= r.getScore() %> / <%= r.getTotal() %></td>
          <td><span class="badge <%= cls %>"><%= pct %>%</span></td>
          <td class="nowrap text-muted"><%= r.getAttemptedAtDisplay() %></td>
        </tr>
      <% } %>
      </tbody>
    </table>
  </div>
<% } %>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
