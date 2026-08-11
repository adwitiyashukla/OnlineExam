<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Subject subject = (Subject) request.getAttribute("subject");
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    Question editQuestion = (Question) request.getAttribute("editQuestion");
    boolean editing = editQuestion != null;
    String code = subject.getCode();
    String info  = (String) request.getAttribute("info");
    String error = (String) request.getAttribute("error");

    String[] eo = editing
        ? new String[]{ editQuestion.getOption1(), editQuestion.getOption2(),
                        editQuestion.getOption3(), editQuestion.getOption4() }
        : new String[]{ "", "", "", "" };
    String corr = editing ? editQuestion.getCorrectAnswer() : null;
    request.setAttribute("pageTitle", "Questions - " + subject.getName());
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="flex between items-center wrap gap">
  <div class="page-head" style="margin-bottom:0;">
    <h1>Questions</h1>
    <p><%= WebUtil.escape(subject.getName()) %> <span class="badge badge-muted"><%= WebUtil.escape(code) %></span></p>
  </div>
  <a class="btn btn-outline btn-sm" href="<%= ctx %>/teacher/subjects">Back to subjects</a>
</div>

<% if (info != null)  { %><div class="alert alert-success mt"><%= WebUtil.escape(info) %></div><% } %>
<% if (error != null) { %><div class="alert alert-error mt"><%= WebUtil.escape(error) %></div><% } %>

<div class="card mt">
  <h3 class="mb"><%= editing ? "Edit question" : "Add a question" %></h3>
  <form method="post" action="<%= ctx %>/teacher/questions">
    <input type="hidden" name="action" value="<%= editing ? "update" : "add" %>">
    <input type="hidden" name="subject" value="<%= WebUtil.escape(code) %>">
    <% if (editing) { %><input type="hidden" name="id" value="<%= editQuestion.getId() %>"><% } %>

    <div class="form-group">
      <label for="question">Question</label>
      <input class="form-control" id="question" name="question" placeholder="Type the question"
             value="<%= editing ? WebUtil.escape(editQuestion.getQuestionText()) : "" %>" required autofocus>
    </div>

    <div class="form-group">
      <label>Options - select the radio next to the correct answer</label>
      <div class="options">
        <% for (int k = 1; k <= 4; k++) {
             String val = eo[k - 1];
             boolean chk = editing && val != null && val.equals(corr); %>
          <div class="option" style="cursor:default;">
            <input type="radio" name="answerIndex" value="<%= k %>" <%= chk ? "checked" : "" %> required title="Mark option <%= k %> as correct">
            <input class="form-control" name="op<%= k %>" placeholder="Option <%= k %>"
                   value="<%= WebUtil.escape(val) %>" required>
          </div>
        <% } %>
      </div>
    </div>

    <button class="btn btn-primary" type="submit"><%= editing ? "Update question" : "Add question" %></button>
    <% if (editing) { %><a class="btn btn-ghost" href="<%= ctx %>/teacher/questions?subject=<%= WebUtil.escape(code) %>">Cancel</a><% } %>
  </form>
</div>

<h2 class="section-title">Questions in this subject (<%= questions.size() %>)</h2>
<% if (questions.isEmpty()) { %>
  <div class="card empty-state">No questions yet - add your first one above.</div>
<% } else {
     int n = 1;
     for (Question q : questions) {
        String[] opts = { q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4() }; %>
  <div class="question-card">
    <div class="flex between items-center wrap gap">
      <div class="q-num">Question <%= n %></div>
      <div class="nowrap">
        <a class="btn btn-outline btn-sm" href="<%= ctx %>/teacher/questions?subject=<%= WebUtil.escape(code) %>&amp;action=edit&amp;id=<%= q.getId() %>">Edit</a>
        <form method="post" action="<%= ctx %>/teacher/questions" style="display:inline"
              onsubmit="return confirm('Delete this question?');">
          <input type="hidden" name="action" value="delete">
          <input type="hidden" name="subject" value="<%= WebUtil.escape(code) %>">
          <input type="hidden" name="id" value="<%= q.getId() %>">
          <button class="btn btn-danger btn-sm" type="submit">Delete</button>
        </form>
      </div>
    </div>
    <div class="q-text"><%= WebUtil.escape(q.getQuestionText()) %></div>
    <div class="options">
      <% for (String opt : opts) {
           boolean isCorrect = opt.equals(q.getCorrectAnswer()); %>
        <div class="option <%= isCorrect ? "correct" : "" %>" style="cursor:default;">
          <span><%= WebUtil.escape(opt) %></span>
          <% if (isCorrect) { %><span class="tick badge badge-success">Correct</span><% } %>
        </div>
      <% } %>
    </div>
  </div>
  <% n++; } } %>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
