/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.repository;

import br.edu.ifpb.pdm.chat.eventbus.domain.Chat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Pedro Arthur
 */
public class ChatRepository {
    
    private final List<Chat> chats;
    
    public ChatRepository() {
        this.chats = new ArrayList<>();
    }

    public List<Chat> getChats() {
        return Collections.unmodifiableList(chats);
    }
    
    public void addChat(Chat chat) {
        this.chats.add(chat);
    }
    
    public void removeChat(Chat chat) {
        this.chats.remove(chat);
    }
    
    public Chat find(String email) {
        for(Chat chat : chats) {
            if(chat.getContact().getEmail().equals(email)) {
                return chat;
            }
        } return null;
    }

    @Override
    public String toString() {
        return "ChatRepository{" + "chats=" + chats + '}';
    }
}
