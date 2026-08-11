<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    int studentCount = (Integer) request.getAttribute("studentCount");
    int teacherCount = (Integer) request.getAttribute("teacherCount");
    int subjectCount = (Integer) request.getAttribute("subjectCount");
    int questionCount = (Integer) request.getAttribute("questionCount");
    int resultCount = (Integer) request.getAttribute("resultCount");
    request.setAttribute("pageTitle", "Admin Dashboard");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="page-head">
  <h1>Admin Dashboard</h1>
  <p>A bird's-eye view of the entire platform.</p>
</div>

<div class="card-grid mb">
  <div class="stat"><div class="stat-value"><%= studentCount %></div><div class="stat-label">Students</div></div>
  <div class="stat"><div class="stat-value"><%= teacherCount %></div><div class="stat-label">Teachers</div></div>
  <div class="stat"><div class="stat-value"><%= subjectCount %></div><div class="stat-label">Subjects</div></div>
  <div class="stat"><div class="stat-value"><%= questionCount %></div><div class="stat-label">Questions</div></div>
  <div class="stat"><div class="stat-value"><%= resultCount %></div><div class="stat-label">Attempts</div></div>
</div>

<h2 class="section-title">Manage</h2>
<div class="card-grid">
  <a class="tile" href="<%= ctx %>/admin/students"><h3>Students</h3><p>View and remove student accounts.</p>
  </a>
  <a class="tile" href="<%= ctx %>/admin/teachers"><h3>Teachers</h3><p>View and remove teacher accounts.</p>
  </a>
  <a class="tile" href="<%= ctx %>/admin/subjects"><h3>Subjects</h3><p>Browse and delete subjects across all teachers.</p>
  </a>
  <a class="tile" href="<%= ctx %>/admin/results"><h3>Results</h3><p>Inspect every quiz attempt in the system.</p>
  </a>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
