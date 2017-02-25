/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Registrador de Subscribers (interessados)
 * 
 * @author Pedro Arthur
 */
public class Register {
    
    private final Map<String, Socket> subscribers;
    
    public Register() {
        this.subscribers = new HashMap<>();
    }
    
    /**
     * Registro de um subscriber em memória
     * @param subscriberId - identificador do subscriber
     * @param subscriberSocket - socket da conexão com o subscriber
     */
    public void register(String subscriberId, Socket subscriberSocket) {
        System.out.println("Registering subscriber...");
        subscribers.put(subscriberId, subscriberSocket);
    }
    
    /**
     * Remove o subscriber da memória
     * @param subscriberId - identificador do subscriber
     */
    public void unregister(String subscriberId) {
        this.subscribers.remove(subscriberId);
    }

    /**
     * Recupera conexão o subscriber
     * @param subscriberId - identificador do subscriber
     * @return 
     */
    public Socket find(String subscriberId) {
        return this.subscribers.get(subscriberId);
    }
    
    /**
     * Lista todos os identificadores dos subscribers
     * 
     * @return 
     */
    public List<String> listIds() {
        
        List<String> result = new ArrayList<>();
        Set<String> keySet = this.subscribers.keySet();
        
        for(String key : keySet) {
            result.add(key);
        }
        
        return result;
    }   
    
}
