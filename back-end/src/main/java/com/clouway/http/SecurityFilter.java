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
import java.util.Set;

/**
 * @author Emil Georgiev <emogeorgiev88@gmail.com>.
 */
@Singleton
public class SecurityFilter implements Filter {

    private final Provider<Session> sessionProvider;
    private final SiteMap siteMap;
    private final Provider<Set<String>> setProvider;

    @Inject
    public SecurityFilter(Provider<Session> sessionProvider, SiteMap siteMap, Provider<Set<String>> setProvider) {
        this.sessionProvider = sessionProvider;
        this.siteMap = siteMap;
        this.setProvider = setProvider;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();

        for (String resource : setProvider.get()) {
            if (uri.contains(resource)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        Session session = sessionProvider.get();
        Date currentTime = new Timestamp(System.currentTimeMillis());

        if (session == null || session.getExpirationTime().before(currentTime)) {
            if (uri.contains("amount")) {
                response.setStatus(401);
                return;
            }

            response.sendRedirect(siteMap.loginPage());
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}