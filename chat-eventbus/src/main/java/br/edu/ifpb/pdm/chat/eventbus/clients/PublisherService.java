/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.clients;

import br.edu.ifpb.pdm.chat.eventbus.Message;
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
public class PublisherService {
    
    private static final String TOKEN = "---123456---";
    private static final String HOST = "localhost";
    private static final int PORT = 10999;
    
    public boolean enviarMensagem(Message message) {
        
        try {
            
            Socket socket = new Socket(HOST,PORT);
            
            OutputStream os = socket.getOutputStream();
            
            os.write(getRequest(message).getBytes());
            
            InputStream in = socket.getInputStream();
            
            String response = getResponse(in);
            
            return response.equals("#OK#");
            
        } catch (IOException ex) {
            Logger.getLogger(PublisherService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    private String getResponse(InputStream in) throws IOException {
        byte[] bytes = new byte[4];
        in.read(bytes);
        return new String(bytes).trim();
    }
    
    private String getRequest(Message message) {
        
        StringBuilder builder = new StringBuilder();
        builder.append(TOKEN)
                .append(message.toJson())
                .append(TOKEN);
        
        return builder.toString();
    }
}
