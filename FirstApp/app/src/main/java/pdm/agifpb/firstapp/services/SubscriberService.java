package pdm.agifpb.firstapp.services;

import android.app.Activity;
import android.content.Context;
import android.widget.BaseAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import pdm.agifpb.firstapp.domain.Chat;
import pdm.agifpb.firstapp.domain.Message;
import pdm.agifpb.firstapp.domain.User;

/**
 * Created by Pedro Arthur on 23/02/2017.
 */

public class SubscriberService {

    private final static String TOKEN = "---123456---";
    private final static String HOST = "192.168.43.152";
    private final static int PORT = 10998;

    private String wait(Socket socket) throws IOException {

        InputStream is = socket.getInputStream();
        byte[] bytes = new byte[1024];
        is.read(bytes); //blocking method
        return new String(bytes).trim();

    }

    public void subscribe(User user, Chat chat, Context context, final BaseAdapter adapter) {

        try {

            System.out.print("Conectando... ");
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Conectou!");
            OutputStream out = socket.getOutputStream();

            System.out.println("subscribing...");
            out.write(getRequest(user).getBytes());

            boolean waiting = true;
            while(waiting) {

                String msg = wait(socket);
                Message message = parse(msg);

                Message newMessage = ChatSession.addMessage(message);
                if(newMessage != null && newMessage.getPublisherId().equals(chat.getContact().getEmail())) {
                    chat.addMessage(newMessage);
                }
                Activity a = (Activity) context;
                a.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

        } catch (IOException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Message parse(String msg) {
        String json = msg.replaceAll(TOKEN, "");
        return new Message().fromJson(json);
    }

    private String getRequest(User user) {

        StringBuilder builder = new StringBuilder();

        builder.append(TOKEN)
                .append(user.getEmail())
                .append(TOKEN);

        return builder.toString();
    }
}
