package com.clouway.core;

/**
 * Created by emil on 14-9-25.
 */
public class TransactionStatus {
    public final String message;
    public final double amount;

    public TransactionStatus(String message, double amount) {

        this.message = message;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionStatus that = (TransactionStatus) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = message != null ? message.hashCode() : 0;
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
