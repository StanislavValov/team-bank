package com.clouway.http;

import com.clouway.core.SiteMap;
import com.clouway.core.DTOUser;
import com.clouway.core.UserRepository;
import com.clouway.core.Validator;
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

        if (validator.isValid(dtoUser) &&
                !repository.findByName(dtoUser.getUsername()).isPresent()) {

            repository.add(dtoUser);
            return siteMap.loginPage();
        }
        error = siteMap.registrationError();
        return null;
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