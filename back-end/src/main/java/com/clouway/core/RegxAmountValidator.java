package com.clouway.core;

/**
 * Created by clouway on 14-10-3.
 */

public class RegxAmountValidator implements Validator<Amount> {

    @Override
    public boolean isValid(Amount Object) {
        return String.valueOf(Object.getAmount()).matches("^[1-9][0-9]*(\\.[0-9]{1,2})?$");
    }
}