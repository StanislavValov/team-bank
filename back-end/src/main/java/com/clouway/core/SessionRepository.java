package com.clouway.core;

import com.google.common.base.Optional;

import java.util.Date;

/**
 * Created by emil on 14-9-27.
 */
public interface SessionRepository {

    CurrentUser findUserBy(String sessionId);

    void addUser(String username, String sessionId);

    void remove(String sessionId);

    Optional<Session> find(String sessionId);
}