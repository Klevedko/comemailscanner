package com.email.scanner;

import java.io.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.email.scanner.ReadProperties.properties;

public class ReceiveEmailWithAttachment {
    public static Logger logger = LoggerFactory.getLogger(ReceiveEmailWithAttachment.class);

    final static String pop3Host = "pop.gmail.com";
    final static String userName = properties.getUserMail();
    final static String password = properties.getUserMailPassword();
    final static String imaps_store_protocol = "imaps";
    final static String imaps_host = "imap.gmail.com";
    final static String imaps_port = "993";
    final static Flags flags = new Flags(Flags.Flag.SEEN);
    public static String messageSubject = App.simpleGUI.getMessageSubjectText();
    public static Store store;

    public static void main(String[] args) {
        connect_imap(pop3Host, userName, password);
        search_in_folder();
        logger.info("END \r\n");
        System.exit(0);
    }

    public static void connect_imap(String pop3Host, String userName, String password) {
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", imaps_store_protocol);
            properties.put("mail.imaps.host", imaps_host);
            properties.put("mail.imaps.port", imaps_port);
            Session session = Session.getInstance(properties);
            logger.info("Connecting to imap...");
            store = session.getStore(imaps_store_protocol);
            store.connect(pop3Host, userName, password);
            logger.info("Connect succeeded!");
        } catch (MessagingException e) {
            logger.error(e.toString());
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public static void search_in_folder() {
        try {
            Boolean notFound = true;
            logger.info("Looking for our email...");
            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_ONLY);
            Message messages[] = inbox.search(new FlagTerm(flags, false));
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                if (message.getSubject() != null) {
                    if (message.getSubject().contains(messageSubject)) {
                        logger.info("FOUND A MESSAGE : \r\n {} \r\n WITH SUBJECT: {} \r\n...seeking for an attachments"
                                , message.getReceivedDate(), message.getSubject());
                        Address[] toAddress = message.getRecipients(Message.RecipientType.TO);
                        if (message.getContent() instanceof Multipart) {
                            Multipart multipart = (Multipart) message.getContent();
                            for (int k = 0; k < multipart.getCount(); k++) {
                                BodyPart bodyPart = multipart.getBodyPart(k);
                                if (bodyPart.getFileName() != null) {
                                    String filename = MimeUtility.decodeText(bodyPart.getFileName());
                                    File file = new File(filename);
                                    InputStream stream = (InputStream) bodyPart.getInputStream();
                                    OutputStream outputStream = new FileOutputStream(file);
                                    IOUtils.copy(stream, outputStream);
                                    outputStream.close();
                                    logger.info("Attachment saved as {}", filename);
                                }
                            }
                        }
                    }
                }
            }
            if (notFound)
                logger.warn("\r\n COULD NOT FIND A MESSAGE WITH SUBJECT: {}", App.simpleGUI.getMessageSubjectText());
            inbox.close(false);
            store.close();
        } catch (Exception x) {
            logger.error(x.toString());
        }
    }
}

