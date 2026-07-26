<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Student student = (Student) session.getAttribute("student");
    int attemptCount = (Integer) request.getAttribute("attemptCount");
    int bestPercentage = (Integer) request.getAttribute("bestPercentage");
    List<Result> recent = (List<Result>) request.getAttribute("recent");
    request.setAttribute("pageTitle", "Student Dashboard");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="page-head">
  <h1>Welcome, <%= WebUtil.escape(student.getName()) %> &#128075;</h1>
  <p>Ready to test your knowledge? Pick a subject and start a quiz.</p>
</div>

<div class="card-grid mb">
  <div class="stat"><div class="stat-value"><%= attemptCount %></div><div class="stat-label">Quizzes Attempted</div></div>
  <div class="stat"><div class="stat-value"><%= bestPercentage %>%</div><div class="stat-label">Best Score</div></div>
</div>

<div class="card-grid">
  <a class="tile" href="<%= ctx %>/student/quiz">
    <div class="tile-icon">&#9998;</div>
    <h3>Take a Quiz</h3>
    <p>Choose a subject and attempt a timed multiple-choice quiz.</p>
  </a>
  <a class="tile" href="<%= ctx %>/student/results">
    <div class="tile-icon">&#128202;</div>
    <h3>My Results</h3>
    <p>Review every quiz you have attempted and how you scored.</p>
  </a>
  <a class="tile" href="<%= ctx %>/leaderboard">
    <div class="tile-icon">&#127942;</div>
    <h3>Leaderboard</h3>
    <p>See how your best scores compare with everyone else.</p>
  </a>
</div>

<h2 class="section-title">Recent attempts</h2>
<% if (recent == null || recent.isEmpty()) { %>
  <div class="card empty-state">
    <span class="emoji">&#128203;</span>
    You haven't attempted any quizzes yet.
    <div class="mt"><a class="btn btn-primary" href="<%= ctx %>/student/quiz">Start your first quiz</a></div>
  </div>
<% } else { %>
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>Subject</th><th>Score</th><th>Percentage</th><th>Date</th></tr></thead>
      <tbody>
      <% for (Result r : recent) {
           int pct = r.getPercentage();
           String cls = pct >= 75 ? "badge-success" : pct >= 40 ? "badge-warning" : "badge-danger"; %>
        <tr>
          <td><%= WebUtil.escape(r.getSubjectName()) %> <span class="text-muted">(<%= WebUtil.escape(r.getSubjectCode()) %>)</span></td>
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
