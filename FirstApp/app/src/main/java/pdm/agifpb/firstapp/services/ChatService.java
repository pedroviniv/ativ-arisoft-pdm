package pdm.agifpb.firstapp.services;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pdm.agifpb.firstapp.domain.Contact;
import pdm.agifpb.firstapp.domain.Message;
import pdm.agifpb.firstapp.domain.User;

/**
 * Created by Pedro Arthur on 20/02/2017.
 */

public class ChatService {

    private static final String TOKEN = "---123456---";
    private static final String HOST = "192.168.43.152";
    private static final int PORT = 10999;

    public boolean enviarMensagem(Message message) {

        try {

            Socket socket = new Socket(HOST,PORT);

            OutputStream os = socket.getOutputStream();

            Log.d("Kieckegard Log", "Writing "+getRequest(message));

            os.write(getRequest(message).getBytes());

            InputStream in = socket.getInputStream();

            String response = getResponse(in);

            Log.d("[Kieckegard Log]", "Server response: "+response);

            return response.equals("#OK#");

        } catch (IOException ex) {
            Logger.getLogger(ChatService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    private String getResponse(InputStream in) throws IOException {
        byte[] bytes = new byte[4];
        in.read(bytes);
        return new String(bytes).trim();
    }

    private String getRequest(Message message) {

        StringBuilder builder = new StringBuilder();
        builder.append(TOKEN)
                .append(message.toJson())
                .append(TOKEN);

        return builder.toString();
    }
}
