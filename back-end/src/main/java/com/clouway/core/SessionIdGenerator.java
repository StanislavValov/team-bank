package com.clouway.core;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * Created by clouway on 14-9-26.
 */
public class SessionIdGenerator implements IdGenerator {
    @Override
    public String generateFor(DTOUser DTOUser) {
        HashFunction hashFunction = Hashing.sha1();
        HashCode hashCode = hashFunction.hashString(DTOUser.getUsername() + DTOUser.getPassword() + System.currentTimeMillis());
        return hashCode.toString();
    }
}