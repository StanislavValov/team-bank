package com.clouway.core;

/**
 * @author Emil Georgiev <emogeorgiev88@gmail.com>.
 */
public class Amount {

    private String amount;

    public Amount(String amount) {
        this.amount = amount;
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
        Amount amount1 = (Amount) o;
        if (amount != null ? !amount.equals(amount1.amount) : amount1.amount != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return amount != null ? amount.hashCode() : 0;
    }
}