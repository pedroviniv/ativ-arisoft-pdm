/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus.exception;

/**
 *
 * @author Pedro Arthur
 */
public class SubscriberNotRegisteredException extends Exception {
    
    public SubscriberNotRegisteredException(String msg) {
        super(msg);
    }
}
