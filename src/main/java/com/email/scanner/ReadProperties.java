package com.email.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadProperties {
    public static Logger logger = LoggerFactory.getLogger(ReceiveEmailWithAttachment.class);
    public static Properties properties = new Properties();

    protected static void readPropertiesFromConfigFile() {
        try (InputStream input = new FileInputStream("config.properties")) {
            java.util.Properties prop = new java.util.Properties();
            InputStreamReader reader = null;
            reader = new InputStreamReader(input, "UTF-8");
            prop.load(reader);
            logger.debug("get the property values");
            properties.setUserMail(prop.getProperty("user_mail"));
            properties.setUserMailPassword(prop.getProperty("user_mail_password"));
            input.close();
            printProperties();
        } catch (IOException ex) {
            logger.error(ex.toString());
        }
    }

    protected static void printProperties() {
        logger.info("Running under: \r\n LOGIN: {} PASSWORD: {} ", properties.getUserMail(), properties.getUserMailPassword());
    }
}
