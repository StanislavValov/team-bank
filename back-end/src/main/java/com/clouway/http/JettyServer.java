package com.clouway.http;

import com.clouway.persistent.PersistentModule;
import com.google.inject.*;
import com.google.inject.name.Names;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Emil Georgiev <emogeorgiev88@gmail.com>.
 */
@SuppressWarnings("deprecation")
public class JettyServer {

    public static void main(String[] args) throws Exception {

        final Map<String, String> configuration = loadConfiguration(args);

        Module[] modules = new Module[]{

                new HttpModule(), new Sitebricks(), new PersistentModule(),

                new AbstractModule() {
                    @Override
                    protected void configure() {
                        Names.bindProperties(binder(), configuration);
                    }
                }
        };
        Injector injector = Guice.createInjector(Stage.PRODUCTION, modules);
        HttpBackend jettyServer = injector.getInstance(HttpBackend.class);
        jettyServer.startServer();
    }

    private static Map<String, String> loadConfiguration(String[] args) {

        Properties configuration = new Properties();
        String configurationFile = "back-end/configuration.properties";

        if(args.length > 0) {
            configurationFile = args[0];
        }


        try {
            InputStream input = new FileInputStream(new File(configurationFile));
            configuration.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> config = new HashMap<>();
        for(String key : configuration.stringPropertyNames()) {
            String value = configuration.getProperty(key);
            config.put(key, value);
        }

        return config;
    }
}
