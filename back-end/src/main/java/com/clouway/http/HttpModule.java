package com.clouway.http;

import com.clouway.core.*;
import com.clouway.persistent.PersistentBankRepository;
import com.google.common.base.Optional;
import com.google.inject.*;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by emil on 14-9-24.
 */
public class HttpModule extends ServletModule {

    @Override
    protected void configureServlets() {

        filter("/amount/*").through(SecurityFilter.class);
        filter("/amount").through(SecurityFilter.class);

        bind(BankRepository.class).to(PersistentBankRepository.class);
        bind(IdGenerator.class).to(SessionIdGenerator.class);
        bind(SiteMap.class).to(LabelMap.class);
        bind(Clock.class).to(CalendarUtil.class);
        bind(Validator.class).annotatedWith(Names.named("UserValidator")).to(RegxUserValidator.class);
        bind(Validator.class).annotatedWith(Names.named("AmountValidator")).to(RegxAmountValidator.class);

    }

    @Provides
    public TransactionMessages getTransactionMessages() {
        return new TransactionMessages() {
            @Override
            public String success() {
                return "Transaction success";
            }

            @Override
            public String failed() {
                return "Transaction failed";
            }
        };
    }

    @Provides
    @RequestScoped
    public Session getCurrentSession(Provider<HttpServletRequest> requestProvider, SessionRepository sessionRepository, SiteMap siteMap) {

        Cookie[] cookies = requestProvider.get().getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {

                Optional<Session> session = sessionRepository.find(cookie.getValue());

                if (cookie.getName().equals(siteMap.sessionCookieName()) && session.isPresent()) {
                    return session.get();
                }
            }
        }
        return null;
    }
}