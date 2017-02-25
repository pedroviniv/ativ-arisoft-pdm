/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.clients;

import br.edu.ifpb.pdm.chat.eventbus.domain.Chat;
import br.edu.ifpb.pdm.chat.eventbus.domain.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Arthur
 */
public class ContactService {
    
    private static final String HOST = "localhost";
    private static final int PORT = 10997;
    private static final String TOKEN = "---123456---";
    private User loggedUser;
    
    private static final String SUCCESS_RESPONSE = "#SUCCESS#", 
            ERROR_RESPONSE = "#ERROR#",
            NONE_CHATS = "#NONE_CHATS#";
    
    public ContactService(User loggedUser) {
        this.loggedUser = loggedUser;
    }
    
    public List<Chat> getChats() {
        List<Chat> chats = new ArrayList<>();
        try {
            Socket socket = new Socket(HOST,PORT);
            
            OutputStream out = socket.getOutputStream();
            out.write(getRequest().getBytes());
            
            InputStream in = socket.getInputStream();
            
            chats = treatResponse(in);
            
            socket.close();
            
            return chats;
            
            
        } catch (IOException ex) {
            Logger.getLogger(ContactService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return chats;
    }
    
    private List<Chat> treatResponse(InputStream in) throws IOException {
        byte[] bytes = new byte[1024];
        in.read(bytes);
        
        String response = new String(bytes).trim();
        
        if(verifyResponse(response, SUCCESS_RESPONSE)) {
            
            return getChats(response, SUCCESS_RESPONSE);
            
        } else if(verifyResponse(response, ERROR_RESPONSE)) { 
            System.out.println(getResponseData(response, ERROR_RESPONSE));
        } else if(verifyResponse(response, NONE_CHATS)) {
            System.out.println(getResponseData(response, NONE_CHATS));
        } else {
            System.out.println("erro desconhecido!");
        }
        return null;
    }
    
    private List<Chat> getChats(String response, String responseType) {
        String json = response.replaceAll(responseType, "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Chat>>(){}.getType();
        return gson.fromJson(json, type);
    }
    
    private String getResponseData(String response, String responseType) {
        return response.replaceAll(responseType, "");
    }
    
    private boolean verifyResponse(String response, String responseType) {
        return response.startsWith(responseType) && response.endsWith(responseType);
    }
    
    private String getRequest() {
        
        StringBuilder builder = new StringBuilder();
        builder.append(TOKEN)
                .append(this.loggedUser.toJson())
                .append(TOKEN);
        
        return builder.toString();
    }
}
