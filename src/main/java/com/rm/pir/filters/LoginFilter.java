package com.rm.pir.filters;

import com.rm.pir.model.User;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter
public class LoginFilter implements Filter{

    @Inject
    private User user;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String contextPath = ((HttpServletRequest)request).getContextPath();
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (user == null || user.getEmail() == null) {
            toLogin(req, resp);
            return;
        }
        else {
            String uri = req.getRequestURI();
            String type = user.getAcct_type();
            if (uri.contains("/admin") && !type.equals("a")) {
                toNotAuth(req, resp);
                return;
            }
            else if (uri.contains("/student") && !type.equals("s")) {
                toNotAuth(req, resp);
                return;
            }
            else if (uri.contains("/child") && !type.equals("c")) {
                toNotAuth(req, resp);
                return;
            }
            else if (uri.contains("/frontDesk") && !type.equals("f")) {
                toNotAuth(req, resp);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
    
    private void toNotAuth(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(403);
        resp.sendRedirect(req.getContextPath() + "/403.xhtml");
    }
    
    private void toLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/login.xhtml");
    }
}
