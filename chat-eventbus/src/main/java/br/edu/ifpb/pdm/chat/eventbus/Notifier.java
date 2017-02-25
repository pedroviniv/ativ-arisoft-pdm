
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus;

import br.edu.ifpb.pdm.chat.eventbus.exception.SubscriberNotRegisteredException;
import java.io.IOException;
import java.net.Socket;

/**
 * Notificador do subscriber
 * @author Pedro Arthur
 */
public class Notifier {
    
    private final static String TOKEN = "---123456---";
    private final Register register;
    
    public Notifier(Register register) {
        this.register = register;
    }
    
    /**
     * Notifica um determinado subscriber com uma mensagem específica
     * @param message - mensagem a ser notificada
     * @throws br.edu.ifpb.pdm.chat.eventbus.exception.SubscriberNotRegisteredException
     */
    public void notify(Message message) throws SubscriberNotRegisteredException {
        //
        Socket socket = register.find(message.getSubscriberId());
        
        if(socket != null) {
            //
            try {

                StringBuilder builder = new StringBuilder();
                builder.append(TOKEN)
                       .append(message.toJson())
                       .append(TOKEN);
                //Esses tokens servem para identificarmos o inicio e o fim da mensagem.


                socket.getOutputStream().write(builder.toString().getBytes());
                //
                socket.getOutputStream().flush();

            } catch(IOException ex) {
                ex.printStackTrace();
            } 
        } else {
            throw new SubscriberNotRegisteredException("Subscriber "+message.getSubscriberId()+" not registered!");
        }
    }
}
