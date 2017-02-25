/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador de Mensagens
 * @author Pedro Arthur
 */
public class MessageManager {
    
    private final List<Message> messages;
    
    public MessageManager() {
        this.messages = new ArrayList<>();
    }
    
    /**
     * Pursiste a mensagem publicada 
     * @param message - mensagem a ser persistida
     */
    public void publish(Message message) {
        this.messages.add(message);
    }
    
    /**
     * Remove a mensagem publicada
     * @param message - mensagem a ser removida
     */
    public void unpublish(Message message) {
        System.out.println("BEFORE REMOVING");
        for(Message message1 : messages) {
            System.out.println(message1);
        }
        this.messages.remove(message);
        System.out.println("AFTER REMOVING");
        for(Message message1 : messages) {
            System.out.println(message1);
        }
        
    }
    
    /**
     * Localiza um conjunto de mensagens par aum determinado subscriber
     * @param subscriberId - identificador do subscriber
     * @return 
     */
    public List<Message> find(String subscriberId) {
        
        List<Message> result = new ArrayList<>();
        
        for(Message message : messages) {
            if(message.getSubscriberId().equals(subscriberId)) {
                result.add(message);
            }
        }
        return result;
    }
}
