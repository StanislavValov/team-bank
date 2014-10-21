package com.clouway.core;

import com.google.common.base.Optional;

/**
 * Created by emil on 14-9-27.
 */
public interface UserRepository {

    Optional<User> find(DTOUser DTOUser);

    Optional<User> findByName(String username);

    void add(DTOUser DTOUser);
}