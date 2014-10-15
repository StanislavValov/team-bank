package com.clouway.core;

/**
 * Created by emil on 14-9-25.
 */
public interface BankRepository {
    TransactionStatus deposit(double amount);

    TransactionStatus withdraw(double amount);

    double getBalance();
}
