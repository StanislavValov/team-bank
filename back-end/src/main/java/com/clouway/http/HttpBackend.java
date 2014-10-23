package com.clouway.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * @author Emil Georgiev <emogeorgiev88@gmail.com>.
 */
public class HttpBackend {
    private final Integer jettyPort;

    @Inject
    public HttpBackend(@Named("jetty.port") Integer jettyPort) {

        this.jettyPort = jettyPort;
    }

    public void startServer() {
        Server server = new Server(jettyPort);

        WebAppContext webapp = new WebAppContext();
        webapp.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        webapp.setContextPath("/");
        webapp.setWar("frontend/bin");

        String[] welcomeFiles = new String[]{"/index.html"};

        webapp.setWelcomeFiles(welcomeFiles);
        server.setHandler(webapp);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
