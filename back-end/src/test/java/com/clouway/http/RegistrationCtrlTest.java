package com.clouway.http;

import com.clouway.core.*;
import com.google.common.base.Optional;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by clouway on 14-10-2.
 */
public class RegistrationCtrlTest {
    private RegistrationCtrl registrationCtrl;
    private User user = new User("name", "pass");

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    UserRepository repository;

    @Mock
    Validator validator;

    @Mock
    SiteMap siteMap;

    @Before
    public void setUp() {
        registrationCtrl = new RegistrationCtrl(validator, repository, siteMap);
    }

    @Test
    public void createAccount() {

        final Optional<User> optional = Optional.absent();

        context.checking(new Expectations() {
            {
                oneOf(validator).isValid(user);
                will(returnValue(true));

                oneOf(repository).findByName(null);
                will(returnValue(optional));

                oneOf(repository).add(user);

                oneOf(siteMap).loginPage();
                will(returnValue("/login"));
            }
        });
        assertThat(registrationCtrl.register(), is("/login"));
    }

    @Test
    public void userAlreadyExists() {

        final Optional<User> optional = Optional.fromNullable(new User("name", "pass"));

        context.checking(new Expectations() {
            {
                oneOf(validator).isValid(user);
                will(returnValue(true));
                oneOf(repository).findByName(null);
                will(returnValue(optional));
                oneOf(siteMap).occupiedUsername();
            }
        });
        assertThat(registrationCtrl.register(), nullValue());
    }

    @Test
    public void userDataAreNotCorrect() {

        final Optional<User> optional = Optional.absent();

        context.checking(new Expectations() {
            {
                oneOf(validator).isValid(user);
                will(returnValue(false));
                
                oneOf(siteMap).dataMissmatch();
            }
        });
        assertThat(registrationCtrl.register(), nullValue());
    }
}