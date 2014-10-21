package com.clouway.core;

import com.google.common.base.Optional;

/**
 * @author Emil Georgiev <emogeorgiev88@gmail.com>.
 */
public interface UserRepository {

    Optional<User> find(DTOUser DTOUser);

    Optional<User> findByName(String username);

    void add(DTOUser DTOUser);
}
