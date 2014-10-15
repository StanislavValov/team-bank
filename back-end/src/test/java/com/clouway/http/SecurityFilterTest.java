package com.clouway.http;

import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.google.inject.util.Providers;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class SecurityFilterTest {

    private SecurityFilter securityFilter;
    private Session session;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    private HttpServletRequest request = null;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletResponse response = null;


    @Test
    public void sessionIsNotExpired() throws IOException, ServletException {

        session = new Session("username", "sessionid", new Date(System.currentTimeMillis()));

        securityFilter = new SecurityFilter(Providers.of(session));

        context.checking(new Expectations() {
            {
                oneOf(filterChain).doFilter(request, response);
            }
        });

        securityFilter.doFilter(request, response, filterChain);

    }


    @Test
    public void sessionIsExpired() throws IOException, ServletException {

        session = new Session("username", "sessionid", new Date(System.currentTimeMillis()-1000));

        securityFilter = new SecurityFilter(Providers.of(session));

        context.checking(new Expectations() {
            {
                oneOf(response).setStatus(401);
            }
        });

        securityFilter.doFilter(request, response, filterChain);

    }
}