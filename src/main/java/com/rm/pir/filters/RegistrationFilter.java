package com.rm.pir.filters;

import com.rm.pir.model.Constants;
import com.rm.pir.model.Settings;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class RegistrationFilter implements Filter {

    @Inject
    private Constants constants;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String contextPath = ((HttpServletRequest)request).getContextPath();
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        Settings settings = constants.getSettings();
        if (settings != null && !settings.isRegistrationOpen())
            toClosed(req, resp);
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
    
    private void toClosed(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/register-closed.xhtml");
    }
}
