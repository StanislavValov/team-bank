package com.clouway.http;

import com.clouway.core.Session;
import com.clouway.core.SiteMap;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Emil Georgiev <emogeorgiev88@gmail.com>.
 */
@Singleton
public class SecurityFilter implements Filter {

    private final Provider<Session> sessionProvider;
    private final SiteMap siteMap;

    @Inject
    public SecurityFilter(Provider<Session> sessionProvider, SiteMap siteMap) {

        this.sessionProvider = sessionProvider;
        this.siteMap = siteMap;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String uri = request.getRequestURI();

        Session session = sessionProvider.get();

        Date currentTime = new Timestamp(System.currentTimeMillis());

        if (session == null || session.getExpirationTime().before(currentTime)) {
            if (uri.contains("amount")) {

                response.setStatus(401);
                return;
            }
            response.sendRedirect(siteMap.loginPage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}