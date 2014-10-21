package com.clouway.persistent;

import com.clouway.core.Clock;
import com.clouway.core.CurrentUser;
import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import java.util.Calendar;

/**
 * Created by emil on 14-9-26.
 */
@Singleton
public class PersistentSessionRepository implements SessionRepository {

    private DB db;
    private Clock clock;

    @Inject
    public PersistentSessionRepository(Provider<DB> dbProvider, Clock clock) {

        this.clock = clock;
        this.db = dbProvider.get();
    }

    @Override
    public CurrentUser findUserBy(String sessionId) {

        DBObject criteria = new BasicDBObject("sessionId", sessionId);

        DBObject projection = new BasicDBObject("username", 1);

        BasicDBObject username = (BasicDBObject) sessions().findOne(criteria, projection);

        return new CurrentUser(username.getString("username"));
    }

    @Override
    public void addUser(String username, String sessionId) {

        DBObject query = new BasicDBObject("username", username)

                .append("sessionId", sessionId)
                .append("expirationTime", clock.sessionExpirationTime(Calendar.getInstance()));

        sessions().createIndex(new BasicDBObject("expirationTime", 1), new BasicDBObject("expireAfterSeconds", 0));

        sessions().insert(query);
    }

    @Override
    public void remove(String sessionId) {

        BasicDBObject query = new BasicDBObject();

        query.append("sessionId", sessionId);

        sessions().remove(query);
    }

    @Override
    public Optional<Session> find(String sessionId) {

        BasicDBObject query = new BasicDBObject("sessionId", sessionId);

        BasicDBObject fields = new BasicDBObject();

        fields.put("username", 1);
        fields.put("sessionId", 2);
        fields.put("expirationTime", 3);

        BasicDBObject dbObject = (BasicDBObject) sessions().findOne(query, fields);

        if (!Optional.fromNullable(dbObject).isPresent()){
            return Optional.absent();
        }

        return Optional.fromNullable(new Session(dbObject.getString("username"),
                dbObject.getString("sessionId"), dbObject.getDate("expirationTime")));
    }

    private DBCollection sessions() {
        return db.getCollection("sessions");
    }
}