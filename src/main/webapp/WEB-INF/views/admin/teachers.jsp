<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    List<Teacher> teachers = (List<Teacher>) request.getAttribute("teachers");
    String info = (String) request.getAttribute("info");
    request.setAttribute("pageTitle", "Manage Teachers");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="page-head"><h1>Teachers</h1><p><%= teachers.size() %> registered teacher(s).</p></div>
<% if (info != null) { %><div class="alert alert-success"><%= WebUtil.escape(info) %></div><% } %>

<% if (teachers.isEmpty()) { %>
  <div class="card empty-state"><span class="emoji">&#128101;</span> No teachers registered yet.</div>
<% } else { %>
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>ID</th><th>Name</th><th>Email</th><th class="text-right">Action</th></tr></thead>
      <tbody>
      <% for (Teacher t : teachers) { %>
        <tr>
          <td class="text-muted"><%= t.getId() %></td>
          <td><%= WebUtil.escape(t.getName()) %></td>
          <td><%= WebUtil.escape(t.getEmail()) %></td>
          <td class="text-right">
            <form method="post" action="<%= ctx %>/admin/teachers" style="display:inline"
                  onsubmit="return confirm('Delete <%= WebUtil.escape(t.getName()) %>? Their subjects, questions and related results will be removed too.');">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="id" value="<%= t.getId() %>">
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
