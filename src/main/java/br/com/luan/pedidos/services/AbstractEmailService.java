package br.com.luan.pedidos.services;

import br.com.luan.pedidos.domain.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public abstract class AbstractEmailService implements EmailService {

    @Value("${default.sender}")
    private String sender;

    //injetamos essa classe que é do Thymeleag para fazer o processamento do template HTML com os dados do pedido
    @Autowired
    private TemplateEngine templateEngine;
    //usado para instanciar um MimeMessage
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendOrderConfirmationEmail(Pedido obj) {
        SimpleMailMessage sm = prepareSimpleMailMessageFromPredido(obj);
        sendEmail(sm); //método da interface, isso faz parte do padrao Template Method. Posso chamar um
                        // método da interface que nem foi implementado ainda. Ele será implementado
                        // somente depois, depois implementacoes da interface.
    }

    @Override
    public void sendOrderConfirmationHtmlEmail(Pedido obj) {
        try {
            MimeMessage mm = prepareMimeMessageFromPedido(obj);
            sendHtmlEmail(mm);
        } catch (MessagingException e) {
            sendOrderConfirmationEmail(obj);
        }
    }

    //metodo auxiliar para preparar o email a ser enviado em texto plano
    protected SimpleMailMessage prepareSimpleMailMessageFromPredido(Pedido obj) {
        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setTo(obj.getCliente().getEmail());
        sm.setFrom(sender);
        sm.setSubject("Pedido confirmado! Código " + obj.getId());
        sm.setSentDate(new Date(System.currentTimeMillis()));
        sm.setText(obj.toString());
        return sm;
    }

    //metodo auxiliar para preparar o email a ser enviado em HTML
    protected MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
        mmh.setTo(obj.getCliente().getEmail());
        mmh.setFrom(sender);
        mmh.setSubject("Pedido confirmado! Código " + obj.getId());
        mmh.setSentDate(new Date(System.currentTimeMillis()));
        mmh.setText(htmlFromTemplatePedido(obj), true);
        return mimeMessage;
    }
    //OBS: o método acima obriga a lancar a exception MessagingException, por causa do MimeMessageHelper.
    //Então, lançamos a exception, e no métodoqque chama esse, que é o sendOrderConfirmationHtmlEmail
    //tratamos a exception com o try catch, e dizemos o seguinte: tenta enviar com o html, se der zebra,
    //manda o texto plano mesmo, sem html.

    //metodo para retornar um HTML preenchido com os dados do pedido, ele retorna uma String que
    //corresponde ao HTML preenchido com os dados do pedido.
    protected String htmlFromTemplatePedido(Pedido obj){
        Context context = new Context(); //esse Context é do Thymeleaf e serve para popular o HTML
        context.setVariable("pedido", obj);
        return templateEngine.process("email/confirmacaoPedido", context); //por padrao o Thymeleaf busca dentro da pasta resources/templates
    }


}
