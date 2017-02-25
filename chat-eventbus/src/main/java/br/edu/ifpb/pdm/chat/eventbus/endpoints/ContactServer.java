/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.endpoints;

import br.edu.ifpb.pdm.chat.eventbus.domain.Chat;
import br.edu.ifpb.pdm.chat.eventbus.repository.ChatRepository;
import br.edu.ifpb.pdm.chat.eventbus.domain.User;
import br.edu.ifpb.pdm.chat.eventbus.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Arthur
 */
public class ContactServer implements Runnable {

    private final ChatRepository chatRepository;
    private final UserRepository userRepo;
    private final static String TOKEN = "---123456---";
    private final static int PORT = 10997;
    private ServerSocket server;

    public ContactServer(ChatRepository chatRepository, UserRepository userRepo) {
        this.chatRepository = chatRepository;
        this.userRepo = userRepo;
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
        System.out.println("============= CONTACT SERVICE =============");

        Socket socket = null;
        try {
            socket = this.server.accept();
            System.out.println("Cliente se conectou para buscar chats!");

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            StringBuilder builder = new StringBuilder();
            
            try {
                System.out.println("Tratando requisição!");
                User loggedUser = treatRequest(in);
                System.out.println("usuário logado: "+loggedUser);
                
                if(loggedUser != null) {
                    System.out.println("recuperando chats");
                    List<Chat> chats = getChats(loggedUser);
                    
                    if(chats != null && !chats.isEmpty()) {
                        System.out.println("Existem chats!");
                        Gson gson = new Gson();
                        String json = gson.toJson(chats, List.class);
                        builder.append("#SUCCESS#")
                                .append(json)
                                .append("#SUCCESS#");
                    }
                    
                    else {
                        System.out.println("Não existem chats");
                        builder.append("#NONE_CHATS#")
                                .append("#NONE_CHATS#");
                    }
                } else {
                    System.out.println("Usuário logado não existe!");
                    builder.append("#ERROR#")
                            .append("Usuário não existe!")
                            .append("#ERROR#");
                }
                
            } catch (JsonSyntaxException ex) {
                builder.append("#ERROR#")
                        .append("Erro de sintaxe de Json!!")
                        .append("#ERROR#");
            } finally {
                out.write(builder.toString().getBytes());
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(ContactServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(socket != null) {
                try {
                    System.out.println("Finalizando conexão!");
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ContactServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private User treatRequest(InputStream in) throws IOException {
        System.out.println("============= TREATING REQUEST | CONTACT SERVICE =============");
        byte[] bytes = new byte[1024];
        in.read(bytes);

        String request = new String(bytes).trim();
        System.out.println("Requisição enviada por cliente: "+request);
        
        System.out.println("validando requisição");
        if(request.startsWith(TOKEN) && request.endsWith(TOKEN)) {
            System.out.println("Requisição válida!");
            String json = request.replaceAll(TOKEN, "");
            System.out.println("Json: "+json);
            User user = new User().fromJson(json);

            return user;
            
        }
        System.out.println("Requisição inválida!");
        return null;
    }
    
    private List<Chat> getChats(User user) {     
        List<Chat> chats = new ArrayList<>();
        for(User userItem : userRepo.listAll()) {
            if(!userItem.authenticate(user.getEmail(), user.getPassword())) {
                Chat chat = chatRepository.find(userItem.getEmail());
                System.out.println(chat);
                chats.add(chat);
            }
        }
        
        return chats;
    }

}
