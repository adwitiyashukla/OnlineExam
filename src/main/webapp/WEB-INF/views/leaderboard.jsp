<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    List<Result> leaders = (List<Result>) request.getAttribute("leaders");
    request.setAttribute("pageTitle", "Leaderboard");
%>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="page-head">
  <h1>&#127942; Leaderboard</h1>
  <p>Top quiz attempts across the platform, ranked by percentage then raw score.</p>
</div>

<% if (leaders == null || leaders.isEmpty()) { %>
  <div class="card empty-state"><span class="emoji">&#127937;</span> No attempts yet — be the first to top the board!</div>
<% } else { %>
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>Rank</th><th>Student</th><th>Subject</th><th>Score</th><th>Percentage</th><th>Date</th></tr></thead>
      <tbody>
      <% int rank = 1; for (Result r : leaders) {
           int pct = r.getPercentage();
           String cls = pct >= 75 ? "badge-success" : pct >= 40 ? "badge-warning" : "badge-danger";
           String medal = rank == 1 ? "&#129351;" : rank == 2 ? "&#129352;" : rank == 3 ? "&#129353;" : "";
           String rankCls = rank <= 3 ? "rank rank-" + rank : "rank"; %>
        <tr>
          <td class="<%= rankCls %>"><%= medal %> <%= rank %></td>
          <td><%= WebUtil.escape(r.getStudentName()) %></td>
          <td><%= WebUtil.escape(r.getSubjectName()) %> <span class="text-muted">(<%= WebUtil.escape(r.getSubjectCode()) %>)</span></td>
          <td><%= r.getScore() %> / <%= r.getTotal() %></td>
          <td><span class="badge <%= cls %>"><%= pct %>%</span></td>
          <td class="nowrap text-muted"><%= r.getAttemptedAtDisplay() %></td>
        </tr>
      <% rank++; } %>
      </tbody>
    </table>
  </div>
<% } %>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
