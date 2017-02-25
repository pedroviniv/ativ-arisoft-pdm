/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.domain;

import com.google.gson.Gson;

/**
 *
 * @author Pedro Arthur
 */
public class Credentials implements JsonObject<Credentials> {
    
    private String email;
    private String password;
    
    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public Credentials() {
        
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public Credentials fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Credentials.class);
    }

    @Override
    public String toString() {
        return "Credentials{" + "email=" + email + ", password=" + password + '}';
    }

    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, Credentials.class);
    }
    
    
}
