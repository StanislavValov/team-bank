package com.clouway.core;

/**
 * Created by clouway on 14-10-9.
 */
public interface Validator<T> {

    boolean isValid(T Object);
}