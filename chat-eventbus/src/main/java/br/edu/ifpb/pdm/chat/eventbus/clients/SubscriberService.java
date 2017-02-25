/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.clients;

import br.edu.ifpb.pdm.chat.eventbus.Message;
import br.edu.ifpb.pdm.chat.eventbus.domain.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Arthur
 */
public class SubscriberService {
    
    private final static String TOKEN = "---123456---";
    private final static String HOST = "localhost";
    private final static int PORT = 10998;
    
    private String wait(Socket socket) throws IOException {
        
        InputStream is = socket.getInputStream();
        byte[] bytes = new byte[1024];
        is.read(bytes); //blocking method
        return new String(bytes).trim();
        
    }
    
    public void subscribe(User user) {
        
        try {
            
            System.out.print("Conectando... ");
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Conectou!");
            OutputStream out = socket.getOutputStream();
            
            System.out.println("subscribing...");
            out.write(getRequest(user).getBytes());
            
            boolean waiting = true;
            while(waiting) {
                
                String msg = wait(socket);
                Message message = parse(msg);
                
                System.out.println("Mensagem recebida: "+message);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Message parse(String msg) {
        String json = msg.replaceAll(TOKEN, "");
        return new Message().fromJson(json);
    }
 
    private String getRequest(User user) {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append(TOKEN)
                .append(user.getEmail())
                .append(TOKEN);
        
        return builder.toString();
    }
    
}
