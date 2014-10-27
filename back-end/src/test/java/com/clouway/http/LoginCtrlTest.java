package com.clouway.http;

import com.clouway.core.*;
import com.google.common.base.Optional;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

/**
 * Created by clouway on 14-9-24.
 */
public class LoginCtrlTest {

    private User user;
    private LoginCtrl loginCtrl;
    private FakeHttpResponse response;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    UserRepository userRepository;

    @Mock
    SessionRepository sessionRepository;

    @Mock
    IdGenerator idGenerator;

    @Mock
    SiteMap siteMap;

    @Before
    public void setUp() {

        user = new User("name","password");
        loginCtrl = new LoginCtrl(userRepository, sessionRepository, idGenerator, siteMap);

        response = new FakeHttpResponse() {
            @Override
            public void addCookie(Cookie cookie) {
                super.addCookie(cookie);
            }
        };
    }


    @Test
    public void authenticate() throws ServletException, IOException {

        context.checking(new Expectations() {
            {
                oneOf(userRepository).find(user);
                will(returnValue(Optional.of(user)));

                oneOf(idGenerator).generateFor(user);
                will(returnValue("sessionId"));

                oneOf(sessionRepository).addUser(user.getName(), "sessionId");

                oneOf(siteMap).sessionCookieName();
                will(returnValue("sId"));

            }
        });
        assertThat(loginCtrl.authenticate(response), is("/"));
    }

    @Test
    public void loginFailed() throws ServletException, IOException {

        context.checking(new Expectations() {
            {
                oneOf(userRepository).find(user);
                will(returnValue(Optional.absent()));

                oneOf(siteMap).loginFailed();
                will(returnValue("Error"));
            }
        });
        assertThat(loginCtrl.authenticate(response), is("/login"));
    }
}