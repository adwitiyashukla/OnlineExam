package com.onlineexam.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.onlineexam.dao.ResultDAO;

@WebServlet("/admin/results")
public class AdminResultServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("results", resultDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/admin/results.jsp").forward(request, response);
    }
}
