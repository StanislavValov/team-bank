package com.clouway.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by clouway on 14-10-22.
 */
public class HttpBackend {

    private final Integer port;

    @Inject
    public HttpBackend(@Named("jetty.port") Integer port) {
        this.port = port;
    }

    public void startServer() {

        Server server = new Server(port);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("frontend");
        server.setHandler(webapp);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}