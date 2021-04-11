package br.com.luan.pedidos.services;

import br.com.luan.pedidos.domain.Cliente;
import br.com.luan.pedidos.repositories.ClienteRepository;
import br.com.luan.pedidos.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder pe;

    @Autowired
    private EmailService emailService;

    private Random rand = new Random();

    public void sendNewPassword(String email) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if(cliente == null) {
            throw new ObjectNotFoundException("Email não encontrado");
        }
        String newPass = newPassword();
        cliente.setSenha(pe.encode(newPass));

        clienteRepository.save(cliente);
        emailService.sendNewPasswordEmail(cliente, newPass);
    }

    private String newPassword() {
        char[] vet = new char[10];
        for (int i=0; i<10; i++) {
            vet[i] = randomChar();
        }
        return new String(vet);
    }

    private char randomChar() {
        int opt = rand.nextInt(3);
        if (opt == 0) { //gera digito numero
            return (char) (rand.nextInt(10) + 48); //48 é onde começa os caracteres unicode para numeros, 10 é quantidade de numeros que existem
        }
        else if (opt == 1) { //gera letra maiuscula
            return (char) (rand.nextInt(26) + 65); //65 é onde começa os caracteres unicode para letras maiusculas, 26 é quantidade de letras Maiusculas que existem
        }
        else {
            return (char) (rand.nextInt(26) + 97);
        }
    }


}
