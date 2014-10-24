package com.clouway.core;

/**
 * Created by clouway on 14-9-25.
 */
public interface SiteMap {

    String sessionCookieName();

    String loginPage();

    String index();

    String loginFailed();

    String transactionError();

    String dataMissmatch();

    String occupiedUsername();
}
