package com.clouway.persistent;

import com.clouway.core.*;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.math.BigDecimal;

/**
 * Created by emil on 14-9-25.
 */
@Singleton
public class PersistentBankRepository implements BankRepository {
    private final Provider<DB> dbProvider;
    private final Provider<CurrentUser> currentUser;
    private final TransactionMessages transactionMessages;

    @Inject
    public PersistentBankRepository(Provider<DB> dbProvider,
                                    Provider<CurrentUser> currentUser,
                                    TransactionMessages transactionMessages) {

        this.dbProvider = dbProvider;
        this.currentUser = currentUser;
        this.transactionMessages = transactionMessages;
    }

    /**
     * Deposit amount in client account.
     *
     * @param amount amount who add in account
     * @return info for transaction and new current amount on the client
     */
    @Override
    public TransactionStatus deposit(BigDecimal amount) {

        DBObject query = new BasicDBObject("name", currentUser.get().name);

        BigDecimal newAmount = new BigDecimal(getBalance()).add(amount);

        BasicDBObject update = new BasicDBObject("$set", new BasicDBObject("amount", newAmount.toString()));

        bankAccounts().update(query, update);

        return new TransactionStatus(transactionMessages.onSuccess(), newAmount.toString());
    }

    /**
     * Withdraw amount from client account.If amount who withdraw is greater than current amount
     * transaction is onFailuer.
     *
     * @param amount amount who withdraw from account
     * @return info object for transaction and new current amount on the client.
     */
    @Override
    public TransactionStatus withdraw(BigDecimal amount) {

        BigDecimal currentAmount = new BigDecimal(getBalance());

        if (amount.compareTo(currentAmount) > 0) {
            return new TransactionStatus(transactionMessages.onFailuer(), currentAmount.toString());
        }

        DBObject query = new BasicDBObject("name", currentUser.get().name);

        BigDecimal newAmount = new BigDecimal(getBalance()).subtract(amount);

        DBObject update = new BasicDBObject("$set", new BasicDBObject("amount", newAmount.toString()));

        bankAccounts().update(query, update);

        return new TransactionStatus(transactionMessages.onSuccess(), newAmount.toString());
    }

    @Override
    public String getBalance() {

        DBObject criteria = new BasicDBObject("name", currentUser.get().name);

        DBObject projection = new BasicDBObject("amount", 1)
                .append("_id", 0);

        BasicDBObject dbObject = (BasicDBObject) bankAccounts().findOne(criteria, projection);

        return dbObject.getString("amount");
    }

    private DBCollection bankAccounts() {
        return dbProvider.get().getCollection("bank_accounts");
    }
}