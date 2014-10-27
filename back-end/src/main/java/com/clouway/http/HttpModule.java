package com.clouway.http;
import com.clouway.core.*;
import com.clouway.persistent.PersistentBankRepository;
import com.google.common.base.Optional;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by emil on 14-9-24.
 */
public class HttpModule extends ServletModule {
    @Override
    protected void configureServlets() {

        filterRegex("^(?!.*(.)*login|(.)*registration|(.)*logout).*").through(SecurityFilter.class);
        bind(BankRepository.class).to(PersistentBankRepository.class);
        bind(IdGenerator.class).to(SessionIdGenerator.class);
        bind(SiteMap.class).to(LabelMap.class);
        bind(Clock.class).to(CalendarUtil.class);
        bind(Validator.class).annotatedWith(Names.named("UserValidator")).to(RegxUserValidator.class);
        bind(Validator.class).annotatedWith(Names.named("AmountValidator")).to(RegxAmountValidator.class);
    }

    @Provides
    public Set<String> unchekedresources() {
        Set<String> unsecureResources = new HashSet<>();
        unsecureResources.add("/login");
        unsecureResources.add("/logout");
        unsecureResources.add("/registration");
        return unsecureResources;
    }

    @Provides
    public TransactionMessages getTransactionMessages() {
        return new TransactionMessages() {
            @Override
            public String onSuccess() {
                return "Successful transaction!";
            }
            @Override
            public String onFailuer() {
                return "Amount is more then Balance";
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