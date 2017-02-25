/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.endpoints;

import br.edu.ifpb.pdm.chat.eventbus.Register;
import br.edu.ifpb.pdm.chat.eventbus.domain.Credentials;
import br.edu.ifpb.pdm.chat.eventbus.domain.User;
import br.edu.ifpb.pdm.chat.eventbus.repository.UserRepository;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Pedro Arthur
 */
public class LoginServer implements Runnable {
    
    private final static String TOKEN = "---123456---";
    private final static int PORT = 10996;
    private ServerSocket server;
    private UserRepository userRepo;
    private Register register;
    
    public LoginServer(UserRepository userRepo, Register register) {
        this.userRepo = userRepo;
        this.register = register;
    }
    
    public void turnOn() {
        try {
            this.server = new ServerSocket(PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        
        Socket socket = null;
        try {
            System.out.println("Aguardando cliente se conectar!");
            socket = server.accept();
            System.out.println("Cliente se conectou!");
            
            StringBuilder builder = new StringBuilder();
            
            try {
                System.out.println("Extraindo credentiais");
                InputStream in = socket.getInputStream();
                Credentials cred = extractCredentials(in);
                System.out.println("Credentiais extraídas: "+cred.toJson());
                System.out.println("Logando...");
                User user = userRepo.signIn(cred.getEmail(), cred.getPassword());
                System.out.println("resultado: "+user);
                OutputStream out = socket.getOutputStream();
                if(user != null) {
                    System.out.println("Usuário logado com sucesso: "+user.toJson());
                    builder.append("#SUCCESS#")
                            .append(user.toJson())
                            .append("#SUCCESS#");
                    out.write(builder.toString().getBytes());
                    
                } else {
                    System.out.println("Usuário/Senha incorretos!");
                    builder.append("#ERROR#")
                            .append("Usuário e/ou Senha incorreto(as).")
                            .append("#ERROR#");
                    out.write(builder.toString().getBytes());
                }
                
            } catch (JsonSyntaxException | IllegalArgumentException ex) {
                OutputStream out = socket.getOutputStream();
                System.out.println("Requisição ilegal!");
                builder.append("#ERROR#")
                            .append(ex.getMessage())
                            .append("#ERROR#");
                out.write(builder.toString().getBytes());
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Protocolo
     * ---123456---{email: '', password: ''}---123456---
     * @param in
     * @return
     * @throws IOException 
     */
    public Credentials extractCredentials(InputStream in) throws IOException {
        
        byte[] bytes = new byte[1024];
        in.read(bytes);
        
        String request = new String(bytes).trim();
        if(request.startsWith(TOKEN) && request.endsWith(TOKEN)) {
            String data = request.replaceAll(TOKEN, "");
            Credentials cred = new Credentials().fromJson(data);
            return cred;
        }
        throw new IllegalArgumentException("Requisição não autorizada!");
    }
    
}
