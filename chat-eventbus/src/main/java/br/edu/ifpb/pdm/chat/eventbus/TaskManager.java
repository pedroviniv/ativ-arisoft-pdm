/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus;

import br.edu.ifpb.pdm.chat.eventbus.exception.SubscriberNotRegisteredException;
import java.util.Collections;
import java.util.List;

/**
 * Gerenciador de Tarefas
 * @author Pedro Arthur
 */
public class TaskManager implements Runnable {
    
    private final Register register;
    private final MessageManager messageManager;
    private final Notifier notifier;

    public TaskManager(Register register, MessageManager messageManager, Notifier notifier) {
        this.register = register;
        this.messageManager = messageManager;
        this.notifier = notifier;
    }
    
    /**
     * Lista as Identificação dos subscribers
     * @return 
     */
    private List<String> listSubscriber() {
        return register.listIds();
    }
    
    /**
     * Lista as mensagens relacionadas a um subscriber
     * @param subscriberId
     * @return 
     */
    private List<Message> listMessages(String subscriberId) {
        return Collections.unmodifiableList(messageManager.find(subscriberId));
    }
    
    /**
     * Notifica e remove uma determinada mensagem a um determinado subscriber
     * @param subscriberId
     * @param message 
     */
    private void notifyAndRemoveMessage(Message message){
        
        try {
            System.out.println("Notificando");
            notifier.notify(message);
            System.out.println("Removendo...");
            messageManager.unpublish(message);
        } catch (SubscriberNotRegisteredException ex) {
            System.out.println(ex.getMessage());
        }
        
    }

    /**
     * Inicia-se o ciclo de vida do gerenciador de tarefas.
     * 1. Lista subscritores
     * 2. Para cada subscritor, verifica se existem mensagens
     * 3. Notica as mensagens 
     */
    @Override
    public void run() {
        //log
        System.out.println("\n============== TASK MANAGER ==============\n");
        
        System.out.println("Listar subscritores");
        //-- listar subscritores
        List<String> subscribers = listSubscriber();
        System.out.println("Quantidade de subscritores: "+subscribers.size());
        //-- para cada subscritor, verificar se existem mensagens
        for(String subscriber : subscribers) {
            //log
            System.out.print("Mensagens para "+subscriber+": ");
            //-- verificar se existem mensagens
            List<Message> messages = listMessages(subscriber);
            //log
            System.out.println(messages.size());
            //-- caso exista, notifica e em seguida remove
            for(Message message : messages) {
                //log
                System.out.println("Enviando mensagem "+message.getIdentify()+" para "+subscriber);
                //
                notifyAndRemoveMessage(message);
            }
        }
    }
    
}
