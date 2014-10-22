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
    public DB getDBProvider(@Named("db.host") String dbHost,
                            @Named("db.port") Integer dbPort,
                            @Named("db.name") String dbName) throws UnknownHostException {

        MongoClient mongoClient = new MongoClient(dbHost, dbPort);

        return mongoClient.getDB(dbName);
    }
}
