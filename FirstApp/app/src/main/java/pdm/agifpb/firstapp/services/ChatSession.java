package pdm.agifpb.firstapp.services;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pdm.agifpb.firstapp.domain.Chat;
import pdm.agifpb.firstapp.domain.Contact;
import pdm.agifpb.firstapp.domain.Message;
import pdm.agifpb.firstapp.domain.User;
import pdm.agifpb.firstapp.view.util.LogUser;

/**
 * Created by Pedro Arthur on 23/02/2017.
 */

public class ChatSession {

    private static List<Chat> chats = new ArrayList<>();

    public static List<Chat> getChats() {
        return chats;
    }

    public static boolean isEmpty() {
        return chats.isEmpty();
    }

    public static void setChats(List<Chat> auxChats) {
        chats = auxChats;
    }

    public static Message addMessage(Message message) {

        String origem = message.getPublisherId();
        String destino = message.getSubscriberId();

        Log.d(LogUser.NAME, "msg[origem]: "+origem);

        for(Chat chat : chats) {
            Contact contact = chat.getContact();
            if(contact.getEmail().equals(origem) || contact.getEmail().equals(destino)) {
                chat.addMessage(message);
                return message;
            }
        } return null;
    }

    public static void clean() {
        chats = new ArrayList<>();
    }
}
