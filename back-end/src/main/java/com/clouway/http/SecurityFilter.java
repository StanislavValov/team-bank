package com.clouway.http;

import com.clouway.core.Session;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Emil Georgiev <emogeorgiev88@gmail.com>.
 */
@Singleton
public class SecurityFilter implements Filter {

    private final Provider<Session> sessionProvider;

    @Inject
    public SecurityFilter(Provider<Session> sessionProvider) {

        this.sessionProvider = sessionProvider;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Session session = sessionProvider.get();

        Date currentTime = new Timestamp(System.currentTimeMillis());

        if (session==null || session.getExpirationTime().before(currentTime)) {
            response.setStatus(401);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}