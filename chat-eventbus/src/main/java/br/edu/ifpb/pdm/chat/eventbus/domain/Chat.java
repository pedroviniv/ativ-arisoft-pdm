/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.domain;

import br.edu.ifpb.pdm.chat.eventbus.Message;
import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Pedro Arthur
 */
public class Chat implements JsonObject<Chat>, Serializable {

    private Contact contact;
    private List<Message> messages;

    public Chat(Contact contact) {
        this.contact = contact;
        this.messages = new ArrayList<>();
    }

    public Contact getContact() {
        return contact;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void addAll(Collection<Message> messages) {
        this.messages.addAll(messages);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    @Override
    public String toString() {
        return "Chat{" + "contact=" + contact + ", messages=" + messages + '}';
    }

    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, Chat.class);
    }

    @Override
    public Chat fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, this.getClass());
    }
}