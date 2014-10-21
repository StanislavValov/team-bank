package com.clouway.core;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by clouway on 14-10-2.
 */
public class RegxDTOUserValidatorTest {

    RegxUserValidator validator = new RegxUserValidator();
    DTOUser DTOUser = new DTOUser();

    @Test
    public void userValidation() {
        DTOUser.setUsername("Brahmaputra");
        DTOUser.setPassword("111111");
        assertThat(validator.isValid(DTOUser), is(true));
    }

    @Test
    public void usernameIsTooShort() {
        DTOUser.setUsername("Bra");
        DTOUser.setPassword("123456");
        assertThat(validator.isValid(DTOUser), is(false));
    }

    @Test
    public void usernameIsTooLong() {
        DTOUser.setUsername("BrahmaputrafromMountain");
        DTOUser.setPassword("123456");
        assertThat(validator.isValid(DTOUser), is(false));
    }

    @Test
    public void passwordIsTooShort() {
        DTOUser.setUsername("Brahmaputra");
        DTOUser.setPassword("123");
        assertThat(validator.isValid(DTOUser), is(false));
    }

    @Test
    public void passwordIsTooLong() {
        DTOUser.setUsername("Brahmaputra");
        DTOUser.setPassword("123456789123456789123");
        assertThat(validator.isValid(DTOUser), is(false));
    }
}