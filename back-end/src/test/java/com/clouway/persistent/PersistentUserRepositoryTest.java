package com.clouway.persistent;

import com.clouway.core.User;
import com.clouway.persistent.util.UserUtil;
import com.google.common.base.Optional;
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
    private UserUtil userUtil;

    @Before
    public void setUp() throws UnknownHostException {

        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDB("team-bank-test");
        persistentUserRepository = new PersistentUserRepository(Providers.of(db));
        userUtil = new UserUtil(db);
        users().drop();
        accounts().drop();
    }

    @Test
    public void findUserByName() throws Exception {

        pretendThatHasUser(name("username"), password("password"));
        Optional<User> optional = persistentUserRepository.findByName("username");
        User user = optional.get();
        assertThat(user.getName(), is("username"));
        assertThat(user.getPassword(), is("password"));
    }

    @Test
    public void userBankAccountWasCreatedAfterRegistration() {

        User user = new User("Ivan", "123456");
        persistentUserRepository.add(user);
        assertThat(accounts().findOne(), notNullValue());
    }

    private void pretendThatHasUser(String username, String password) {
        userUtil.registerClient(username, password);
    }

    private String password(String password) {
        return password;
    }

    private String name(String username) {
        return username;
    }

    private DBCollection users() {
        return db.getCollection("users");
    }

    private DBCollection accounts() {
        return db.getCollection("bank_accounts");
    }
}