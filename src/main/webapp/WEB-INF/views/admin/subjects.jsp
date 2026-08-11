<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    List<Subject> subjects = (List<Subject>) request.getAttribute("subjects");
    String info = (String) request.getAttribute("info");
    request.setAttribute("pageTitle", "All Subjects");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="page-head"><h1>Subjects</h1><p>Every subject across all teachers.</p></div>
<% if (info != null) { %><div class="alert alert-success"><%= WebUtil.escape(info) %></div><% } %>

<% if (subjects.isEmpty()) { %>
  <div class="card empty-state">No subjects have been created yet.</div>
<% } else { %>
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>Code</th><th>Name</th><th>Teacher</th><th>Questions</th><th class="text-right">Action</th></tr></thead>
      <tbody>
      <% for (Subject s : subjects) { String c = WebUtil.escape(s.getCode()); %>
        <tr>
          <td><span class="badge badge-muted"><%= c %></span></td>
          <td><%= WebUtil.escape(s.getName()) %></td>
          <td><%= WebUtil.escape(s.getTeacherName()) %></td>
          <td><%= s.getQuestionCount() %></td>
          <td class="text-right">
            <form method="post" action="<%= ctx %>/admin/subjects" style="display:inline"
                  onsubmit="return confirm('Delete subject <%= c %> and all its questions?');">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="code" value="<%= c %>">
              <button class="btn btn-danger btn-sm" type="submit">Delete</button>
            </form>
          </td>
        </tr>
      <% } %>
      </tbody>
    </table>
  </div>
<% } %>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
