<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    List<Result> results = (List<Result>) request.getAttribute("results");
    request.setAttribute("pageTitle", "All Results");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="page-head"><h1>All Results</h1><p>Every quiz attempt recorded in the system.</p></div>

<% if (results.isEmpty()) { %>
  <div class="card empty-state"><span class="emoji">&#128203;</span> No quiz attempts recorded yet.</div>
<% } else { %>
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>#</th><th>Student</th><th>Subject</th><th>Score</th><th>Percentage</th><th>Date &amp; time</th></tr></thead>
      <tbody>
      <% int i = 1; for (Result r : results) {
           int pct = r.getPercentage();
           String cls = pct >= 75 ? "badge-success" : pct >= 40 ? "badge-warning" : "badge-danger"; %>
        <tr>
          <td class="text-muted"><%= i++ %></td>
          <td><%= WebUtil.escape(r.getStudentName()) %></td>
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
