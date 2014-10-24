package com.clouway.persistent;

import com.clouway.core.SessionRepository;
import com.clouway.core.UserRepository;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

/**
 * Created by emil on 14-9-24.
 */
public class PersistentModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(UserRepository.class).to(PersistentUserRepository.class);
        bind(SessionRepository.class).to(PersistentSessionRepository.class);
    }

    @Provides
    public DB getDBProvider(@Named("dbHost")String host, @Named("dbPort")Integer port, @Named("dbName")String name) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host,port);

        return mongoClient.getDB(name);
    }
}
