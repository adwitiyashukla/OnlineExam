<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    Subject subject = (Subject) request.getAttribute("subject");
    List<Question> questions = (List<Question>) request.getAttribute("questions");
    int seconds = questions.size() * 40;   // 40 seconds per question
    request.setAttribute("pageTitle", "Quiz - " + subject.getName());
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<form method="post" action="<%= ctx %>/student/quiz" id="quizForm">
  <input type="hidden" name="action" value="submit">
  <input type="hidden" name="subject" value="<%= WebUtil.escape(subject.getCode()) %>">

  <div class="quiz-header">
    <div>
      <strong><%= WebUtil.escape(subject.getName()) %></strong>
      <span class="text-muted">(<%= WebUtil.escape(subject.getCode()) %>) - <%= questions.size() %> questions</span>
    </div>
    <div class="timer" id="timer">--:--</div>
  </div>

  <% int n = 1;
     for (Question q : questions) {
        String[] opts = { q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4() };
        String field = "q_" + q.getId(); %>
    <div class="question-card">
      <div class="q-num">Question <%= n %> of <%= questions.size() %></div>
      <div class="q-text"><%= WebUtil.escape(q.getQuestionText()) %></div>
      <div class="options">
        <% for (String opt : opts) { %>
          <label class="option">
            <input type="radio" name="<%= field %>" value="<%= WebUtil.escape(opt) %>">
            <span><%= WebUtil.escape(opt) %></span>
          </label>
        <% } %>
      </div>
    </div>
  <% n++; } %>

  <div class="flex between items-center wrap gap mt">
    <a class="btn btn-ghost" href="<%= ctx %>/student/quiz" onclick="return confirm('Leave this quiz? Your answers will be lost.');">Cancel</a>
    <button class="btn btn-primary btn-lg" type="submit" id="submitBtn">Submit Quiz</button>
  </div>
</form>

<script>
(function () {
  var total = <%= seconds %>;
  var el = document.getElementById('timer');
  var form = document.getElementById('quizForm');
  var done = false;

  function render() {
    var m = Math.floor(total / 60), s = total % 60;
    el.textContent = (m < 10 ? '0' : '') + m + ':' + (s < 10 ? '0' : '') + s;
    el.classList.toggle('warning', total <= 30 && total > 10);
    el.classList.toggle('danger', total <= 10);
  }
  function finish() { if (!done) { done = true; form.submit(); } }

  render();
  var iv = setInterval(function () {
    total--;
    render();
    if (total <= 0) { clearInterval(iv); finish(); }
  }, 1000);

  form.addEventListener('submit', function () { done = true; });
})();
</script>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
