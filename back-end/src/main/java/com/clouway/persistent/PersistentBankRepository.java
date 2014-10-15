package com.clouway.persistent;

import com.clouway.core.*;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

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
    public TransactionStatus deposit(double amount) {

        DBObject query = new BasicDBObject("name", currentUser.get().getName());

        DBObject update = new BasicDBObject("$inc", new BasicDBObject("amount", amount));

        return new TransactionStatus(transactionMessages.success(),
                (Double)bankAccounts().findAndModify(query,null,null,false,update,true,false).get("amount"));
    }

    /**
     * Withdraw amount from client account.If amount who withdraw is greater than current amount
     * transaction is failed.
     *
     * @param amount amount who withdraw from account
     * @return info object for transaction and new current amount on the client.
     */
    @Override
    public TransactionStatus withdraw(double amount) {

        Double currentAmount = getBalance();

        if (currentAmount < amount) {
            return new TransactionStatus(transactionMessages.failed(), currentAmount);
        }

        DBObject query = new BasicDBObject("name", currentUser.get().getName());

        DBObject update = new BasicDBObject("$inc", new BasicDBObject("amount", -amount));

        return new TransactionStatus(transactionMessages.success(),
                (Double) bankAccounts().findAndModify(query, null,null,false,update,true,false).get("amount"));
    }

    @Override
    public double getBalance() {

        DBObject criteria = new BasicDBObject("name", currentUser.get().getName());

        DBObject projection = new BasicDBObject("amount", 1)
                .append("_id", 0);

        BasicDBObject dbObject = (BasicDBObject) bankAccounts().findOne(criteria, projection);

        return dbObject.getDouble("amount");
    }

    private DBCollection bankAccounts() {
        return dbProvider.get().getCollection("bank_accounts");
    }


}