<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String role = (String) request.getAttribute("role");
    if (role == null) role = "student";
    String error = (String) request.getAttribute("error");
    String name  = (String) request.getAttribute("name");
    String email = (String) request.getAttribute("email");
    request.setAttribute("pageTitle", "Register");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="auth-shell">
  <div class="auth-card">
    <h1>Create account</h1>
    <p class="auth-sub">Register as <span class="role-pill"><%= role %></span></p>

    <% if (error != null) { %><div class="alert alert-error"><%= WebUtil.escape(error) %></div><% } %>

    <form method="post" action="<%= ctx %>/register">
      <input type="hidden" name="role" value="<%= role %>">
      <div class="form-group">
        <label for="name">Full name</label>
        <input class="form-control" type="text" id="name" name="name"
               value="<%= name != null ? WebUtil.escape(name) : "" %>" required autofocus>
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input class="form-control" type="email" id="email" name="email"
               value="<%= email != null ? WebUtil.escape(email) : "" %>" required>
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <input class="form-control" type="password" id="password" name="password"
               minlength="6" required>
        <div class="form-hint">At least 6 characters.</div>
      </div>
      <button class="btn btn-primary btn-block" type="submit">Create account</button>
    </form>

    <div class="auth-switch">
      Already have an account? <a href="<%= ctx %>/login?role=<%= role %>">Login instead</a><br>
      <a href="<%= ctx %>/index.jsp">&larr; Back to home</a>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
