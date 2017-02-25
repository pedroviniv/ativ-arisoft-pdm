/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.domain;

import com.google.gson.Gson;
import java.io.Serializable;

/**
 *
 * @author Pedro Arthur
 */
public class Contact implements JsonObject<Contact>, Serializable {
    
    private String email;
    private String name;
    
    public Contact(String email, String name) {
        this.email = email;
        this.name = name;
    }
    
    public Contact() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Contact fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, this.getClass());
    }

    @Override
    public String toString() {
        return "Contact{" + "email=" + email + ", name=" + name + '}';
    }
}
