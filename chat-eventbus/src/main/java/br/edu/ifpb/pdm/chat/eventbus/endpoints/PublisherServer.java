/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.endpoints;

import br.edu.ifpb.pdm.chat.eventbus.domain.Chat;
import br.edu.ifpb.pdm.chat.eventbus.repository.ChatRepository;
import br.edu.ifpb.pdm.chat.eventbus.domain.Contact;
import br.edu.ifpb.pdm.chat.eventbus.Message;
import br.edu.ifpb.pdm.chat.eventbus.MessageManager;
import br.edu.ifpb.pdm.chat.eventbus.domain.User;
import br.edu.ifpb.pdm.chat.eventbus.repository.UserRepository;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 *
 * @author Pedro Arthur
 */
public class PublisherServer implements Runnable {

    private final MessageManager messageManager;
    private final UserRepository userRepo;
    
    private static final String TOKEN = "---123456---";

    private ServerSocket serverSocket;
    private static final int PORT = 10999;

    public PublisherServer(MessageManager messageManager, UserRepository userRepo) {
        this.messageManager = messageManager;
        this.userRepo = userRepo;              
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
            System.out.println("\n============== PUBLISHER SERVER ==============\n");
            System.out.println("1. Aguardando conexão do Publisher... ");
            Socket socket = this.serverSocket.accept();
            InputStream in = socket.getInputStream();
            
            try {
                System.out.print("2. Tratando requisição do publisher...");
                Message message = treat(in);
                System.out.println(" Tratada com sucesso!");
                
                System.out.print("3. Publicando mensagem...");
                messageManager.publish(message);
                System.out.println(" Done!");
                System.out.print("4. Salvando mensagem no chat...");
                System.out.println(" Done!");
                socket.getOutputStream().write("#OK#".getBytes());
            
            } catch (RuntimeException ex) {
                socket.getOutputStream().write("Mensagem não segue protocolo. ".getBytes());
            }
            
            socket.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Protocol ---123456---{identify: '', publisherId: '', subscriberId: '', text: ''}---123456----
     * @param in
     * @return
     * @throws IOException 
     */
    private Message treat(InputStream in) throws IOException {
        byte[] bytes = new byte[1024];
        in.read(bytes);
        String request = new String(bytes).trim();
        if (request.startsWith(TOKEN) && request.endsWith(TOKEN)) {
            String json = request.replaceAll(TOKEN, "");
            Message message = new Message().fromJson(json);
            message.setIdentify(UUID.randomUUID().toString());
            return message;
        }
        throw new RuntimeException("Mensagem estruturada incorretamente!");
    }

}
