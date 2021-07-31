package com.desmonddavid.TGLastFMBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class ApplicationProperties {
    public static final String TELEGRAM_BOT_TOKEN;
    public static final String TELEGRAM_BOT_NAME;
    public static final long TELEGRAM_CREATOR_ID;
    public static final String LAST_FM_API_KEY;
    public static final String LAST_FM_API_SECRET;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);

    static {
        // Read properties
        Properties applicationProperties = new Properties();
        String path;
        try {
            // First look for application.properties file in the same path as the running jar.
            path = new File("").getAbsolutePath();
            logger.info("Looking for the file application.properties in the path: "+path);
            InputStream inStream;
            try {
                inStream = new FileInputStream(path + File.separator + "application.properties");
            } catch(FileNotFoundException e) {
                logger.info("The file application.properties was not found in path: "+path+". Falling back to internal property file.");
                inStream = ApplicationProperties.class.getClassLoader().getResourceAsStream("application.properties");
                if(inStream == null)
                    throw new RuntimeException("Unable to load internal properties file. Please check your build.");
            }
            applicationProperties.load(inStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load properties due to an IO error.");
        }
        logger.info("Reading properties from application.properties file.");

        // Store properties to variables
        TELEGRAM_BOT_TOKEN = (String) applicationProperties.get("TELEGRAM_BOT_TOKEN");
        TELEGRAM_BOT_NAME = (String) applicationProperties.get("TELEGRAM_BOT_NAME");
        TELEGRAM_CREATOR_ID = Long.parseLong((String) applicationProperties.get("TELEGRAM_CREATOR_ID"));
        LAST_FM_API_KEY = (String) applicationProperties.get("LAST_FM_API_KEY");
        LAST_FM_API_SECRET = (String) applicationProperties.get("LAST_FM_API_SECRET");
    }

    private ApplicationProperties(){}

}
