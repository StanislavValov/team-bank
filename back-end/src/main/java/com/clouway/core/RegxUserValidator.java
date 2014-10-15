package com.clouway.core;

/**
 * Created by clouway on 14-10-2.
 */

public class RegxUserValidator implements Validator<User>{

    @Override
    public boolean isValid(User Object) {
        return Object.getUsername() != null && Object.getUsername().matches("^[A-Za-z]{5,12}?$") && Object.getPassword().matches("^[0-9a-zA-Z]{6,18}?$");

    }
}