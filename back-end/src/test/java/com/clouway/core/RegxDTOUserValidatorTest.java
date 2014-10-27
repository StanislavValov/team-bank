package com.clouway.core;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by clouway on 14-10-2.
 */
public class RegxDTOUserValidatorTest {

    RegxUserValidator validator = new RegxUserValidator();

    @Test
    public void userValidation() {
        User user = new User("Brahmaputra", "111111");
        assertThat(validator.isValid(user), is(true));
    }

    @Test
    public void usernameIsTooShort() {
        User user = new User("Bra", "123456");
        assertThat(validator.isValid(user), is(false));
    }

    @Test
    public void usernameIsTooLong() {
        User user = new User("BrahmaputrafromMountain", "123456");
        assertThat(validator.isValid(user), is(false));
    }

    @Test
    public void passwordIsTooShort() {
        User user = new User("Brahmaputra", "123");
        assertThat(validator.isValid(user), is(false));
    }

    @Test
    public void passwordIsTooLong() {
        User user = new User("Brahmaputra", "123456789123456789123");
        assertThat(validator.isValid(user), is(false));
    }
}