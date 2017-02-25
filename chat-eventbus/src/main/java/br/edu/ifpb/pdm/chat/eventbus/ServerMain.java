/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.pdm.chat.eventbus;

import br.edu.ifpb.pdm.chat.eventbus.repository.UserRepository;
import br.edu.ifpb.pdm.chat.eventbus.repository.ChatRepository;
import br.edu.ifpb.pdm.chat.eventbus.domain.Chat;
import br.edu.ifpb.pdm.chat.eventbus.domain.Contact;
import br.edu.ifpb.pdm.chat.eventbus.domain.User;
import br.edu.ifpb.pdm.chat.eventbus.endpoints.ContactServer;
import br.edu.ifpb.pdm.chat.eventbus.endpoints.LoginServer;
import br.edu.ifpb.pdm.chat.eventbus.endpoints.SubscriberServer;
import br.edu.ifpb.pdm.chat.eventbus.endpoints.PublisherServer;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Pedro Arthur
 */
public class ServerMain {

    public static void main(String[] args) throws IOException {

        //Instanciar elementos principais 
        UserRepository userRepo = new UserRepository();
        ChatRepository chatRepo = new ChatRepository();
        
        Register register = new Register();
        registerUsers(userRepo, register, chatRepo);
        
        Notifier notifier = new Notifier(register);
        MessageManager manager = new MessageManager();
        MessageManager messageRepository = new MessageManager();
        TaskManager taskManager = new TaskManager(register, manager, notifier);

        //Programar background
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        //executar uma thread depois dos primeiros 2s e daí por diante a cada 5s
        executor.scheduleAtFixedRate(taskManager, 2000, 5000, TimeUnit.MILLISECONDS);
        
        PublisherServer pubServer = new PublisherServer(manager, userRepo);
        pubServer.turnOn();
        SubscriberServer subServer = new SubscriberServer(register);
        subServer.turnOn();
        
        executor.scheduleAtFixedRate(pubServer, 2000, 2000, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(subServer, 2000, 2000, TimeUnit.MILLISECONDS);
        
        LoginServer loginServer = new LoginServer(userRepo, register);   
        loginServer.turnOn();
        
        ContactServer contactServer = new ContactServer(chatRepo, userRepo);
        contactServer.turnOn();
        
        executor.scheduleAtFixedRate(loginServer, 2000, 2000, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(contactServer, 2000, 2000, TimeUnit.MILLISECONDS);

    }
    
    private static void registerUsers(UserRepository userRepo, Register register, ChatRepository chatRepository) {
        
        userRepo.register(new User("Fulano", "fulano@gmail.com","123456"));
        register.register("fulano@gmail.com", null);
        Contact contact1 = new Contact("fulano@gmail.com", "Fulano");
        chatRepository.addChat(new Chat(contact1));
       
        userRepo.register(new User("Sicrano", "sicrano@gmail.com","123456"));
        register.register("sicrano@gmail.com", null);
        Contact contact2 = new Contact("sicrano@gmail.com", "Sicrano");
        chatRepository.addChat(new Chat(contact2));
        
        userRepo.register(new User("Beltrano", "beltrano@gmail.com","123456"));
        register.register("beltrano@gmail.com", null);
        Contact contact3 = new Contact("beltrano@gmail.com", "Beltrano");
        chatRepository.addChat(new Chat(contact3));
        
        userRepo.register(new User("Deltrano", "deltrano@gmail.com","123456")); 
        register.register("deltrano@gmail.com", null);
        Contact contact4 = new Contact("deltrano@gmail.com", "Deltrano");
        chatRepository.addChat(new Chat(contact4));
    }

}
