package com.clouway.persistent;

import com.clouway.core.DTOUser;
import com.clouway.core.User;
import com.clouway.core.UserRepository;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Created by emil on 14-9-27.
 */
@Singleton
public class PersistentUserRepository implements UserRepository {

    private final DB db;

    @Inject
    public PersistentUserRepository(Provider<DB> dbProvider) {

        this.db = dbProvider.get();
    }

    @Override
    public Optional<User> find(DTOUser DTOUser) {

        BasicDBObject query = new BasicDBObject("username",DTOUser.getUsername());

        query.append("password", DTOUser.getPassword());

        BasicDBObject result = (BasicDBObject) users().findOne(query,query);

        if (!Optional.fromNullable(result).isPresent()){
            return Optional.absent();
        }

        return Optional.fromNullable(new User(result.getString("username")));
    }

    @Override
    public Optional<User> findByName(String username){
        DBObject query = new BasicDBObject("username",username);

        BasicDBObject result = (BasicDBObject) users().findOne(query,query);

        if (!Optional.fromNullable(result).isPresent()){
            return Optional.absent();
        }

        return Optional.fromNullable(new User(result.getString("username")));
    }

    @Override
    public void add(DTOUser DTOUser) {

        DBObject query = new BasicDBObject("username", DTOUser.getUsername()).
                append("password", DTOUser.getPassword());

        createAccount(DTOUser.getUsername());
        users().insert(query);
    }

    private DBCollection users() {
        return db.getCollection("users");
    }

    private void createAccount(String name){

        BasicDBObject query = new BasicDBObject();

        query.append("name", name);
        query.append("amount", "0");

        db.getCollection("bank_accounts").insert(query);
    }
}