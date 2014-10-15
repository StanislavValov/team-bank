package com.clouway.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by emil on 14-9-26.
 */
public class JettyServer {
    public static void main(String[] args) throws Exception {

        Server server = new Server(8090);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("back-end/src/main/webapp");
        server.setHandler(webapp);

        server.start();
        server.join();
    }
}