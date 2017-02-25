package pdm.agifpb.firstapp.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import pdm.agifpb.firstapp.domain.Credentials;
import pdm.agifpb.firstapp.domain.User;

/**
 * Created by Pedro Arthur on 20/02/2017.
 */

public class LoginService {

    private final static String TOKEN = "---123456---";
    private final static String HOST = "192.168.43.152";
    private final static int PORT = 10996;

    private static final String SUCCESS_RESPONSE = "#SUCCESS#",
            ERROR_RESPONSE = "#ERROR#";


    public LoginService() {

    }

    public User signIn(Credentials credentials) {

        try {

            System.out.print("Conectando... ");
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Conectou!");
            OutputStream out = socket.getOutputStream();

            out.write(getRequest(credentials).getBytes());

            InputStream in = socket.getInputStream();

            User user = treatResponse(in);

            return user;

        } catch (IOException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private User treatResponse(InputStream in) throws IOException {

        byte[] bytes = new byte[1024];
        in.read(bytes);

        String response = new String(bytes).trim();

        if(verifyResponse(response, SUCCESS_RESPONSE)) {
            String data = getResponseData(response, SUCCESS_RESPONSE);
            return new User().fromJson(data);
        } else if(verifyResponse(response, ERROR_RESPONSE)) {
            String data = getResponseData(response, ERROR_RESPONSE);
            System.out.println("ERROR: "+data);
        }

        return null;
    }


    private String getResponseData(String response, String responseType) {
        return response.replaceAll(responseType, "");
    }

    private boolean verifyResponse(String response, String responseType) {
        return response.startsWith(responseType) && response.endsWith(responseType);
    }

    private String getRequest(Credentials credentials) {

        StringBuilder builder = new StringBuilder();

        builder.append(TOKEN)
                .append(credentials.toJson())
                .append(TOKEN);

        return builder.toString();
    }
}
