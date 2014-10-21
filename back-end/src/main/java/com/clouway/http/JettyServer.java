package com.clouway.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author Emil Georgiev <emogeorgiev88@gmail.com>.
 */
public class JettyServer {

    public static void main(String[] args) throws Exception {

        Server server = new Server(5555);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("frontend");
        server.setHandler(webapp);

        server.start();
        server.join();
    }
}
