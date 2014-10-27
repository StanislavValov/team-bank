package com.clouway.http;

import com.clouway.core.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.Show;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;

/**
 * Created by clouway on 14-10-2.
 */
@At("/registration")
@Show("registration.html")
@Singleton
public class RegistrationCtrl {

    private Validator validator;
    private UserRepository repository;
    private SiteMap siteMap;
    private String error;
    private DTOUser dtoUser = new DTOUser();

    @Inject
    public RegistrationCtrl(@Named("UserValidator") Validator validator, UserRepository repository, SiteMap siteMap) {

        this.validator = validator;
        this.repository = repository;
        this.siteMap = siteMap;
    }

    @Post
    public String register() {

        User user = new User(dtoUser.getUsername(),dtoUser.getPassword());

        if (!validator.isValid(user)) {
            error = siteMap.dataMissmatch();
            return null;
        }
        if (repository.findByName(dtoUser.getUsername()).isPresent()) {
            error = siteMap.occupiedUsername();
            return null;
        }
        repository.add(user);
        return siteMap.loginPage();
    }

    @Get
    public void clear() {
        error = null;
    }

    public void setDtoUser(DTOUser dtoUser) {
        this.dtoUser = dtoUser;
    }

    public DTOUser getDtoUser() {
        return dtoUser;
    }

    public String getError() {
        return error;
    }
}