package br.com.luan.pedidos.services;

import br.com.luan.pedidos.domain.Cliente;
import br.com.luan.pedidos.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;

public interface EmailService { //interface para por der o smtp do google e o mock para teste

    void sendOrderConfirmationEmail(Pedido obj);

    void sendEmail(SimpleMailMessage msg); // SimpleMailMessage é do Spring , serve para enviar texto plano(simples)

    //metodos abaixo para envio de email com html
    void sendOrderConfirmationHtmlEmail(Pedido obj);

    void sendHtmlEmail(MimeMessage msg); // MimeMessage é do Spring, serve para enviar Email com HTML

    void sendNewPasswordEmail(Cliente cliente, String newPass);
}
