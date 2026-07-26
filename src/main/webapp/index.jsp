<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% request.setAttribute("pageTitle", "Home"); %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<% if ("1".equals(request.getParameter("loggedout"))) { %>
  <div class="alert alert-success">You have been logged out successfully.</div>
<% } %>

<section class="hero">
  <h1>Online Examination Portal</h1>
  <p>A secure quiz platform for the students and teachers of TIET. Teachers create subjects
     and questions; students take timed quizzes, get instant results, and climb the leaderboard.</p>
  <div class="hero-actions">
    <a class="btn btn-primary btn-lg" href="<%= ctx %>/register?role=student">Get Started</a>
    <a class="btn btn-outline btn-lg" href="<%= ctx %>/leaderboard">View Leaderboard</a>
  </div>
</section>

<h2 class="section-title">Choose your role</h2>
<div class="role-cards">
  <div class="role-card">
    <div class="role-emoji">&#127891;</div>
    <h3>Student</h3>
    <p>Register, attempt timed quizzes by subject, and review your score history.</p>
    <div class="role-links">
      <a class="btn btn-primary btn-sm" href="<%= ctx %>/login?role=student">Login</a>
      <a class="btn btn-outline btn-sm" href="<%= ctx %>/register?role=student">Register</a>
    </div>
  </div>
  <div class="role-card">
    <div class="role-emoji">&#128218;</div>
    <h3>Teacher</h3>
    <p>Create subjects, add multiple-choice questions, and see how students performed.</p>
    <div class="role-links">
      <a class="btn btn-primary btn-sm" href="<%= ctx %>/login?role=teacher">Login</a>
      <a class="btn btn-outline btn-sm" href="<%= ctx %>/register?role=teacher">Register</a>
    </div>
  </div>
  <div class="role-card">
    <div class="role-emoji">&#128272;</div>
    <h3>Administrator</h3>
    <p>Oversee all users, subjects and results across the entire platform.</p>
    <div class="role-links">
      <a class="btn btn-primary btn-sm" href="<%= ctx %>/login?role=admin">Login</a>
    </div>
  </div>
</div>

<h2 class="section-title">Why OnlineExam?</h2>
<div class="card-grid">
  <div class="card">
    <h3>&#9889; Instant results</h3>
    <p class="text-muted">Quizzes are graded the moment you submit, with a per-question review.</p>
  </div>
  <div class="card">
    <h3>&#9203; Timed tests</h3>
    <p class="text-muted">Each quiz runs against a countdown timer that auto-submits when time is up.</p>
  </div>
  <div class="card">
    <h3>&#128202; Progress tracking</h3>
    <p class="text-muted">Every attempt is saved so students and teachers can track performance.</p>
  </div>
  <div class="card">
    <h3>&#128274; Secure by design</h3>
    <p class="text-muted">Salted-hashed passwords, session-based access control and prepared statements.</p>
  </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
