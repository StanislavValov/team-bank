package com.clouway.core;

/**
 * Created by clouway on 14-10-16.
 */
public class DTOAmount {

    private String amount;

    public DTOAmount() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DTOAmount amount1 = (DTOAmount) o;

        if (amount != null ? !amount.equals(amount1.amount) : amount1.amount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return amount != null ? amount.hashCode() : 0;
    }
}