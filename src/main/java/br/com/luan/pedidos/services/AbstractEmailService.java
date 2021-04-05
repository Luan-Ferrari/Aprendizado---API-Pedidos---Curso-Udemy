package br.com.luan.pedidos.services;

import br.com.luan.pedidos.domain.Pedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

public abstract class AbstractEmailService implements EmailService {

    @Value("${default.sender}")
    private String sender;

    @Override
    public void sendOrderConfirmationEmail(Pedido obj) {
        SimpleMailMessage sm = prepareSimpleMailMessageFromPredido(obj);
        sendEmail(sm); //método da interface, isso faz parte do padrao Template Method. Posso chamar um
                        // método da interface que nem foi implementado ainda. Ele será implementado
                        // somente depois, depois implementacoes da interface.
    }

    //metodo auxiliar para preparar o email a ser enviado
    protected SimpleMailMessage prepareSimpleMailMessageFromPredido(Pedido obj) {
        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setTo(obj.getCliente().getEmail());
        sm.setFrom(sender);
        sm.setSubject("Pedido confirmado! Código " + obj.getId());
        sm.setSentDate(new Date(System.currentTimeMillis()));
        sm.setText(obj.toString());
        return sm;
    }
}
