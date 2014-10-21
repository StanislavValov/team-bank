package com.clouway.core;

/**
 * Created by clouway on 14-10-3.
 */

public class RegxAmountValidator implements Validator<DTOAmount> {

    @Override
    public boolean isValid(DTOAmount dtoAmount) {
        return dtoAmount.getAmount() != null && dtoAmount.getAmount().matches("^[1-9][0-9]*(\\.[0-9]{1,2})?$");
    }
}