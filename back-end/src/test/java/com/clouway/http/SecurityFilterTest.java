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
import java.util.HashSet;
import java.util.Set;

public class SecurityFilterTest {

    private SecurityFilter securityFilter;
    private Session session;
    private Set<String> uncheckedResources;

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
        securityFilter = new SecurityFilter(Providers.of(session), siteMap, Providers.of(uncheckedResources));
    }

    @Test
    public void resourceMustNotBeSecured() throws IOException, ServletException {

        uncheckedResources = new HashSet<>();
        uncheckedResources.add("resource");

        securityFilter = new SecurityFilter(Providers.of(session), siteMap, Providers.of(uncheckedResources));

        context.checking(new Expectations(){
            {
                oneOf(request).getRequestURI();
                will(returnValue("resource"));

                oneOf(filterChain).doFilter(request,response);
            }
        });

        securityFilter.doFilter(request,response,filterChain);
    }

    @Test
    public void sessionIsNotExpired() throws IOException, ServletException {

        uncheckedResources = new HashSet<>();

        session = new Session("username", "sessionid", new Date(System.currentTimeMillis() + 1000));

        securityFilter = new SecurityFilter(Providers.of(session), siteMap, Providers.of(uncheckedResources));

        context.checking(new Expectations() {
            {
                oneOf(request).getRequestURI();

                oneOf(filterChain).doFilter(request, response);
            }
        });

        securityFilter.doFilter(request, response, filterChain);

    }


    @Test
    public void sessionIsExpired() throws IOException, ServletException {

        uncheckedResources = new HashSet<>();

        session = new Session("username", "sessionid", new Date(System.currentTimeMillis() - 1000));

        securityFilter = new SecurityFilter(Providers.of(session), siteMap, Providers.of(uncheckedResources));

        context.checking(new Expectations() {
            {
                oneOf(request).getRequestURI();
                will(returnValue("/"));

                oneOf(siteMap).loginPage();
                will(returnValue("/login"));

                oneOf(response).sendRedirect("/login");

                oneOf(filterChain).doFilter(request, response);
            }
        });

        securityFilter.doFilter(request, response, filterChain);

    }

    @Test
    public void tryToOpenAccountPageWithoutPermission() throws IOException, ServletException {

        uncheckedResources = new HashSet<>();

        session = null;

        securityFilter = new SecurityFilter(Providers.of(session), siteMap, Providers.of(uncheckedResources));

        context.checking(new Expectations() {
            {
                oneOf(request).getRequestURI();
                will(returnValue("/amount"));

                oneOf(response).setStatus(401);
            }
        });
        securityFilter.doFilter(request, response, filterChain);
    }

    @Test
    public void sessionIsNull() throws Exception {

        uncheckedResources = new HashSet<>();

        session = null;

        securityFilter = new SecurityFilter(Providers.of(session), siteMap, Providers.of(uncheckedResources));

        context.checking(new Expectations() {
            {
                oneOf(request).getRequestURI();

                oneOf(siteMap).loginPage();

                oneOf(response).sendRedirect("");

                oneOf(filterChain).doFilter(request, response);
            }
        });
        securityFilter.doFilter(request, response, filterChain);
    }
}