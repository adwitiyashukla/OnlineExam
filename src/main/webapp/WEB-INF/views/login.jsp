<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String role = (String) request.getAttribute("role");
    if (role == null) role = "student";
    String error = (String) request.getAttribute("error");
    String info  = (String) request.getAttribute("info");
    String email = (String) request.getAttribute("email");
    request.setAttribute("pageTitle", "Login");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="auth-shell">
  <div class="auth-card">
    <h1>Welcome back</h1>
    <p class="auth-sub">Sign in as <span class="role-pill"><%= role %></span></p>

    <% if (info != null) { %><div class="alert alert-info"><%= WebUtil.escape(info) %></div><% } %>
    <% if (error != null) { %><div class="alert alert-error"><%= WebUtil.escape(error) %></div><% } %>

    <form method="post" action="<%= ctx %>/login">
      <input type="hidden" name="role" value="<%= role %>">
      <div class="form-group">
        <label for="email">Email</label>
        <input class="form-control" type="email" id="email" name="email"
               value="<%= email != null ? WebUtil.escape(email) : "" %>" required autofocus>
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <input class="form-control" type="password" id="password" name="password" required>
      </div>
      <button class="btn btn-primary btn-block" type="submit">Login</button>
    </form>

    <div class="auth-switch">
      <% if (!"admin".equals(role)) { %>
        New here? <a href="<%= ctx %>/register?role=<%= role %>">Create a <%= role %> account</a><br>
      <% } %>
      <a href="<%= ctx %>/index.jsp">Back to home</a>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
