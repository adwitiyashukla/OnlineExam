<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<% request.setAttribute("pageTitle", "Something went wrong"); %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="card" style="max-width:560px;margin:40px auto;text-align:center;">
  <div style="font-size:3rem;">&#9888;&#65039;</div>
  <h1 style="margin:10px 0;">Oops — something went wrong</h1>
  <p class="text-muted mb">The page you requested could not be processed. Please try again,
     or head back to a familiar place.</p>
  <div class="hero-actions" style="justify-content:center;">
    <a class="btn btn-primary" href="<%= ctx %>/index.jsp">Back to home</a>
    <a class="btn btn-outline" href="<%= ctx %>/leaderboard">Leaderboard</a>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
