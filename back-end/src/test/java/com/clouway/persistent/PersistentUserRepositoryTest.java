package com.clouway.persistent;

import com.clouway.persistent.util.UserUtil;
import com.clouway.core.DTOUser;
import com.google.inject.util.Providers;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class PersistentUserRepositoryTest {

    private PersistentUserRepository persistentUserRepository;
    private DB db;
    private DTOUser DTOUser;

    private UserUtil userUtil;

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient();

        DTOUser = new DTOUser();

        db = mongoClient.getDB("team-bank-test");

        persistentUserRepository = new PersistentUserRepository(Providers.of(db));

        userUtil = new UserUtil(db);

        users().drop();
        accounts().drop();
    }

    @Test
    public void userIsNotAuthorised() {
//        assertThat(persistentUserRepository.findByName(DTOUser), is(false));
    }

    @Test
    public void userIsAuthorised() {
        DTOUser.setUsername("Brahmaputra");
        DTOUser.setPassword("123456");
        persistentUserRepository.add(DTOUser);
//        assertThat(persistentUserRepository.isAuthorised(DTOUser), is(true));
    }

    @Test
    public void userBankAccountWasCreatedAfterRegistration() {
        DTOUser.setUsername("Ivan");
        DTOUser.setPassword("123456");
        persistentUserRepository.add(DTOUser);
        assertThat(accounts().findOne(), notNullValue());
    }

    private DBCollection users() {
        return db.getCollection("users");
    }

    private DBCollection accounts() {
        return db.getCollection("bank_accounts");
    }

}