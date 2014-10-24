package com.clouway.http;

import com.clouway.persistent.PersistentModule;
import com.google.inject.*;
import com.google.inject.name.Names;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by emil on 14-9-26.
 */
public class JettyServer {

    public static void main(String[] args) {
        final Map<String, String> configuration = loadConfituration(args);

        Module[] modules = new Module[]{new HttpModule(), new Sitebricks(), new PersistentModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        Names.bindProperties(binder(), configuration);
                    }
                }
        };
        Injector injector = Guice.createInjector(Stage.PRODUCTION, modules);
        HttpBackend server = injector.getInstance(HttpBackend.class);
        server.startServer();
    }

    private static Map<String, String> loadConfituration(String[] args) {
        Properties configuration = new Properties();
        String confFile = "configuration.properties";
        if (args.length != 0) {
            confFile = args[0];
        }

        try {
            InputStream inputStream = new FileInputStream(new File(confFile));
            configuration.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> config = new HashMap<>();
        for (String key : configuration.stringPropertyNames()) {
            String value = configuration.getProperty(key);
            config.put(key, value);
        }
        return config;
    }
}