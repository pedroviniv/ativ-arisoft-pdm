/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.repository;

import br.edu.ifpb.pdm.chat.eventbus.domain.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Pedro Arthur
 */
public class UserRepository {
    
    private final List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }
    
    public User signIn(String email, String password) {
        System.out.println("vamos verificar! email: "+email+" | password: "+password);
        for(User user : users) {
            if(user.authenticate(email, password))
                return user;
        }
        
        return null;
    }
    
    public List<User> listAll() {
        return Collections.unmodifiableList(users);
    }
    
    public void register(User user) {
        this.users.add(user);
    }
    
    public void unregister(User user) {
        this.users.remove(user);
    }
    
    public User find(String email) {
        for(User user : users) {
            if(user.getEmail().equals(email)) {
                return user;
            }
        } return null;
    }

    @Override
    public String toString() {
        return "UserRepository{" + "users=" + users + '}';
    }
}
