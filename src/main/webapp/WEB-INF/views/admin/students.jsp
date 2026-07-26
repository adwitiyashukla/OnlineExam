<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    List<Student> students = (List<Student>) request.getAttribute("students");
    String info = (String) request.getAttribute("info");
    request.setAttribute("pageTitle", "Manage Students");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="page-head"><h1>Students</h1><p><%= students.size() %> registered student(s).</p></div>
<% if (info != null) { %><div class="alert alert-success"><%= WebUtil.escape(info) %></div><% } %>

<% if (students.isEmpty()) { %>
  <div class="card empty-state"><span class="emoji">&#128101;</span> No students registered yet.</div>
<% } else { %>
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>ID</th><th>Name</th><th>Email</th><th class="text-right">Action</th></tr></thead>
      <tbody>
      <% for (Student s : students) { %>
        <tr>
          <td class="text-muted"><%= s.getId() %></td>
          <td><%= WebUtil.escape(s.getName()) %></td>
          <td><%= WebUtil.escape(s.getEmail()) %></td>
          <td class="text-right">
            <form method="post" action="<%= ctx %>/admin/students" style="display:inline"
                  onsubmit="return confirm('Delete <%= WebUtil.escape(s.getName()) %>? Their results will be removed too.');">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="id" value="<%= s.getId() %>">
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
