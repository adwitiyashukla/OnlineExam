<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    List<Subject> subjects = (List<Subject>) request.getAttribute("subjects");
    Subject editSubject = (Subject) request.getAttribute("editSubject");
    boolean editing = editSubject != null;
    String info  = (String) request.getAttribute("info");
    String error = (String) request.getAttribute("error");
    request.setAttribute("pageTitle", "My Subjects");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="page-head">
  <h1>My Subjects</h1>
  <p>Create a subject, then add its questions.</p>
</div>

<% if (info != null)  { %><div class="alert alert-success"><%= WebUtil.escape(info) %></div><% } %>
<% if (error != null) { %><div class="alert alert-error"><%= WebUtil.escape(error) %></div><% } %>

<div class="card form-wrap">
  <h3 class="mb"><%= editing ? "Edit subject" : "Add a subject" %></h3>
  <form method="post" action="<%= ctx %>/teacher/subjects">
    <input type="hidden" name="action" value="<%= editing ? "update" : "add" %>">
    <div class="form-group">
      <label for="code">Subject code</label>
      <input class="form-control" id="code" name="code" placeholder="e.g. CS101"
             value="<%= editing ? WebUtil.escape(editSubject.getCode()) : "" %>"
             <%= editing ? "readonly" : "" %> required>
      <% if (editing) { %><div class="form-hint">The code cannot be changed.</div><% } %>
    </div>
    <div class="form-group">
      <label for="name">Subject name</label>
      <input class="form-control" id="name" name="name" placeholder="e.g. Programming Fundamentals"
             value="<%= editing ? WebUtil.escape(editSubject.getName()) : "" %>" required>
    </div>
    <button class="btn btn-primary" type="submit"><%= editing ? "Update subject" : "Add subject" %></button>
    <% if (editing) { %><a class="btn btn-ghost" href="<%= ctx %>/teacher/subjects">Cancel</a><% } %>
  </form>
</div>

<h2 class="section-title">All my subjects</h2>
<% if (subjects == null || subjects.isEmpty()) { %>
  <div class="card empty-state"><span class="emoji">&#128193;</span> No subjects yet — add your first one above.</div>
<% } else { %>
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>Code</th><th>Name</th><th>Questions</th><th class="text-right">Actions</th></tr></thead>
      <tbody>
      <% for (Subject s : subjects) { String c = WebUtil.escape(s.getCode()); %>
        <tr>
          <td><span class="badge badge-muted"><%= c %></span></td>
          <td><%= WebUtil.escape(s.getName()) %></td>
          <td><%= s.getQuestionCount() %></td>
          <td class="text-right nowrap">
            <a class="btn btn-primary btn-sm" href="<%= ctx %>/teacher/questions?subject=<%= c %>">Questions</a>
            <a class="btn btn-outline btn-sm" href="<%= ctx %>/teacher/subjects?action=edit&amp;code=<%= c %>">Edit</a>
            <form method="post" action="<%= ctx %>/teacher/subjects" style="display:inline"
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
