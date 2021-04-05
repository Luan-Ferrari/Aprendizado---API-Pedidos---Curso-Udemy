package br.com.luan.pedidos.services;

import br.com.luan.pedidos.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService { //interface para por der o smtp do google e o mock para teste

    void sendOrderConfirmationEmail(Pedido obj);

    void sendEmail(SimpleMailMessage msg); // SimpleMailMessage é do Spring mail
}
