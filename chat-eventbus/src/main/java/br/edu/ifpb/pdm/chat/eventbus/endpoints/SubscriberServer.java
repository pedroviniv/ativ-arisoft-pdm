/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.endpoints;

import br.edu.ifpb.pdm.chat.eventbus.Register;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Arthur
 */
public class SubscriberServer implements Runnable {
    
    private ServerSocket serverSocket;
    private static final int PORT = 10998;
    private static final String TOKEN = "---123456---";
    private final Register register;

    public SubscriberServer(Register register) {
        this.register = register;
    }
    
    public void turnOn() {
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        
        try {
            System.out.println("\n============== SUBSCRIBER SERVER ==============\n");
            
            System.out.print("1. Aguardando conexão do subscriber...");
            
            Socket socket = serverSocket.accept();
            
            System.out.println("Conexão estabelecida com o subscriber!");
            System.out.println("2. Fazendo leitura dos dados do subscriber... ");
            //Recebe mensagem
            String subscriberId = extractSubscribId(socket.getInputStream());
            //
            System.out.println("4. Registrando o subscriber... ");
            register.register(subscriberId, socket);
            
        } catch (IOException ex) {
            Logger.getLogger(SubscriberServer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    private String extractSubscribId(InputStream in) throws IOException {
        byte[] bytes = new byte[1024];
        in.read(bytes);
        String requestMsg = new String(bytes).trim();
        return requestMsg.replaceAll(TOKEN, "");
    }
    
}
