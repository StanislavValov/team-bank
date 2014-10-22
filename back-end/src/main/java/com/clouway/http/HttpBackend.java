package com.clouway.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

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
