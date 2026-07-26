<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Subject subject = (Subject) request.getAttribute("subject");
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    Map<Integer, String> selected = (Map<Integer, String>) request.getAttribute("selected");
    int score = (Integer) request.getAttribute("score");
    int total = (Integer) request.getAttribute("total");
    int percentage = (Integer) request.getAttribute("percentage");

    String ring = percentage >= 75 ? "linear-gradient(135deg,#16a34a,#22c55e)"
                : percentage >= 40 ? "linear-gradient(135deg,#d97706,#f59e0b)"
                :                    "linear-gradient(135deg,#dc2626,#ef4444)";
    String verdict = percentage >= 75 ? "Excellent work!"
                   : percentage >= 40 ? "Good effort!"
                   :                    "Keep practising!";
    request.setAttribute("pageTitle", "Quiz Result");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="card result-hero">
  <div class="score-ring" style="background: <%= ring %>;">
    <span class="score-pct"><%= percentage %>%</span>
    <span class="score-frac"><%= score %> / <%= total %></span>
  </div>
  <h1><%= verdict %></h1>
  <p class="text-muted"><%= WebUtil.escape(subject.getName()) %> (<%= WebUtil.escape(subject.getCode()) %>)</p>
  <div class="hero-actions" style="justify-content:center;">
    <a class="btn btn-primary" href="<%= ctx %>/student/quiz">Take another quiz</a>
    <a class="btn btn-outline" href="<%= ctx %>/student/results">View my results</a>
  </div>
</div>

<h2 class="section-title">Answer review</h2>
<% for (Question q : questions) {
     String sel = selected.get(q.getId());
     if (sel == null) sel = "";
     String correct = q.getCorrectAnswer();
     String[] opts = { q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4() };
     boolean gotIt = sel.equals(correct); %>
  <div class="question-card">
    <div class="q-num" style="color:<%= gotIt ? "#16a34a" : "#dc2626" %>;">
      <%= gotIt ? "&#10003; Correct" : (sel.isEmpty() ? "&#8212; Not answered" : "&#10007; Incorrect") %>
    </div>
    <div class="q-text"><%= WebUtil.escape(q.getQuestionText()) %></div>
    <div class="options">
      <% for (String opt : opts) {
           String cls = "option";
           String mark = "";
           if (opt.equals(correct)) { cls += " correct"; mark = "&#10003;"; }
           else if (opt.equals(sel)) { cls += " incorrect"; mark = "&#10007;"; } %>
        <div class="<%= cls %>">
          <span><%= WebUtil.escape(opt) %></span>
          <% if (!mark.isEmpty()) { %><span class="tick"><%= mark %></span><% } %>
        </div>
      <% } %>
    </div>
  </div>
<% } %>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
