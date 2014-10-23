package com.clouway.http;

import com.clouway.core.Session;
import com.clouway.core.SiteMap;
import com.google.inject.util.Providers;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
    private HttpServletRequest request;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletResponse response;

    @Mock
    private SiteMap siteMap;

    @Before
    public void setUp() {
        securityFilter = new SecurityFilter(Providers.of(session), siteMap);
    }


    @Test
    public void whenRequestPageIsLogin() throws Exception {

        context.checking(new Expectations() {{

            oneOf(request).getRequestURI();
            will(returnValue("/login"));

            oneOf(siteMap).loginPage();
            will(returnValue("/login"));

            oneOf(filterChain).doFilter(request, response);
        }
        });

        securityFilter.doFilter(request, response, filterChain);

    }

    @Test
    public void whenRequestPageIsRegistration() throws Exception {

        context.checking(new Expectations() {{

            oneOf(request).getRequestURI();
            will(returnValue("/registration"));

            oneOf(siteMap).loginPage();
            will(returnValue("/login"));

            oneOf(siteMap).registrationPage();
            will(returnValue("/registration"));

            oneOf(filterChain).doFilter(request, response);
        }
        });

        securityFilter.doFilter(request, response, filterChain);

    }

    @Test
    public void whenRequestPageIsLogout() throws Exception {

        context.checking(new Expectations() {{

            oneOf(request).getRequestURI();
            will(returnValue("/logout"));

            oneOf(siteMap).loginPage();
            will(returnValue("/login"));

            oneOf(siteMap).registrationPage();
            will(returnValue("/registration"));

            oneOf(siteMap).logout();
            will(returnValue("/logout"));

            oneOf(filterChain).doFilter(request, response);
        }
        });

        securityFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void sessionIsNotExpired() throws IOException, ServletException {

        session = new Session("username", "sessionid", new Date(System.currentTimeMillis()));

        securityFilter = new SecurityFilter(Providers.of(session), siteMap);

        context.checking(new Expectations() {
            {
                oneOf(siteMap).loginPage();
                will(returnValue("/login"));

                oneOf(siteMap).registrationPage();
                will(returnValue("/registration"));

                oneOf(siteMap).logout();
                will(returnValue("logout"));

                oneOf(request).getRequestURI();
                will(returnValue("/"));

                oneOf(filterChain).doFilter(request, response);
            }
        });

        securityFilter.doFilter(request, response, filterChain);

    }


    @Test
    public void sessionIsExpired() throws IOException, ServletException {

        session = new Session("username", "sessionid", new Date(System.currentTimeMillis()-1000));

        securityFilter = new SecurityFilter(Providers.of(session), siteMap);

        context.checking(new Expectations() {
            {
                oneOf(siteMap).loginPage();
                will(returnValue("/login"));

                oneOf(siteMap).registrationPage();
                will(returnValue("/registration"));

                oneOf(siteMap).logout();
                will(returnValue("logout"));

                oneOf(request).getRequestURI();
                will(returnValue("/"));

                oneOf(response).sendRedirect("/login");
            }
        });

        securityFilter.doFilter(request, response, filterChain);

    }
}