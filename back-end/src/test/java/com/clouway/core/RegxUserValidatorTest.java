package com.clouway.core;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by clouway on 14-10-2.
 */
public class RegxUserValidatorTest {

    RegxUserValidator validator = new RegxUserValidator();
    User user = new User();

    @Test
    public void userValidation() {
        user.setUsername("Brahmaputra");
        user.setPassword("111111");
        assertThat(validator.isValid(user), is(true));
    }

    @Test
    public void usernameIsTooShort() {
        user.setUsername("Bra");
        user.setPassword("123456");
        assertThat(validator.isValid(user), is(false));
    }

    @Test
    public void usernameIsTooLong() {
        user.setUsername("BrahmaputrafromMountain");
        user.setPassword("123456");
        assertThat(validator.isValid(user), is(false));
    }

    @Test
    public void passwordIsTooShort() {
        user.setUsername("Brahmaputra");
        user.setPassword("123");
        assertThat(validator.isValid(user), is(false));
    }

    @Test
    public void passwordIsTooLong() {
        user.setUsername("Brahmaputra");
        user.setPassword("123456789123456789123");
        assertThat(validator.isValid(user), is(false));
    }
}