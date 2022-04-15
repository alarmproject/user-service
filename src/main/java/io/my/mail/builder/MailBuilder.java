package io.my.mail.builder;

import io.my.mail.MailProperties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

// 아래 순서 지켜야함!
public class MailBuilder {
    private Message message;
    private Session session;
    private Properties properties;

    private MailBuilder() { }

    public static MailBuilder builder() {
        MailBuilder mailBuilder = new MailBuilder();
        mailBuilder.properties = new Properties();
        return new MailBuilder();
    }

    public MailBuilder properties(MailProperties mailProperties) {
        this.properties.put("mail.smtp.host", mailProperties.getSmtpHost());
        this.properties.put("mail.smtp.port", mailProperties.getSmtpPort());
        this.properties.put("mail.smtp.auth", mailProperties.getSmtpAuth());
        this.properties.put("mail.smtp.socketFactory.port", mailProperties.getSmtpSocketFactoryPort());
        this.properties.put("mail.smtp.socketFactory.class", mailProperties.getSmtpSocketFactoryClass());


        String username = mailProperties.getUsername();
        String password = mailProperties.getPassword();

        this.session = Session.getInstance(this.properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        return this;
    }

    public MailBuilder from(String address) throws MessagingException {
        this.message = new MimeMessage(this.session);
        this.message.setFrom(new InternetAddress(address));
        return this;
    }

    public MailBuilder recipients(InternetAddress[] toAddr) throws MessagingException {
        this.message.setRecipients(
                Message.RecipientType.TO,
                toAddr
        );
        return this;
    }

    public MailBuilder subject(String subject) throws MessagingException {
        this.message.setSubject(subject);
        return this;
    }

    public MailBuilder content(String content) throws MessagingException {
        this.message.setContent(content, "text/html; charset=utf-8");
        return this;
    }

    public Message build() {
        return this.message;
    }







}
