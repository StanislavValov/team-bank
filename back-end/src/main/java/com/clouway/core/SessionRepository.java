package com.clouway.core;

import java.util.Date;

/**
 * Created by emil on 14-9-27.
 */
public interface SessionRepository {

    CurrentUser findUserBy(String sessionId);

    void addUser(String username, String sessionId);

    void remove(String sessionId);

    com.google.common.base.Optional<Session> find(String sessionId);
}