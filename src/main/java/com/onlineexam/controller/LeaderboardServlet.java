package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.ResultDAO;

/** Public leaderboard showing the highest-scoring quiz attempts. */
@WebServlet("/leaderboard")
public class LeaderboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int TOP_N = 20;

    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("leaders", resultDAO.getLeaderboard(TOP_N));
        request.getRequestDispatcher("/WEB-INF/views/leaderboard.jsp").forward(request, response);
    }
}
