package br.com.luan.pedidos.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractEmailService{

    @Autowired
    private MailSender mailSender; //essa classe é parte do framework spring, fazendo essa injeção, o Spring
                                    //vai instanciar um objeto MailSender com todas as informações que estão
                                    // no arquivo aplicattion-dev.properties.

    private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);

    @Override
    public void sendEmail(SimpleMailMessage msg) {
        LOG.info("Simulando envio de email...");
        mailSender.send(msg);
        LOG.info("Email enviado");
    }
}

