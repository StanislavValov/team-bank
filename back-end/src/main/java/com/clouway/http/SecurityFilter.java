package com.clouway.http;

import com.clouway.core.Clock;
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
    private final Clock clock;

    @Inject
    public SecurityFilter(Provider<Session> sessionProvider, SiteMap siteMap, Clock clock) {

        this.sessionProvider = sessionProvider;
        this.siteMap = siteMap;
        this.clock = clock;
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

        if (session == null || session.getExpirationTime().before(clock.now())) {

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