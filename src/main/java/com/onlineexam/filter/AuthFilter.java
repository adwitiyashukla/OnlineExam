package com.onlineexam.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/student/*", "/teacher/*", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        String path = request.getRequestURI().substring(request.getContextPath().length());

        boolean allowed =
                (path.startsWith("/student/") && "STUDENT".equals(role))
             || (path.startsWith("/teacher/") && "TEACHER".equals(role))
             || (path.startsWith("/admin/")   && "ADMIN".equals(role));

        if (allowed) {

            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            chain.doFilter(req, res);
        } else {
            String wanted = path.startsWith("/teacher/") ? "teacher"
                          : path.startsWith("/admin/")   ? "admin"
                          : "student";
            response.sendRedirect(request.getContextPath()
                    + "/login?role=" + wanted + "&denied=1");
        }
    }
}
