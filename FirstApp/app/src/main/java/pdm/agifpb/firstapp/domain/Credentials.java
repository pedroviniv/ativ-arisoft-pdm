package pdm.agifpb.firstapp.domain;

import com.google.gson.Gson;

/**
 * Created by Pedro Arthur on 20/02/2017.
 */

public class Credentials implements JsonObject<Credentials> {

    private String email;
    private String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, Credentials.class);
    }

    @Override
    public Credentials fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Credentials.class);
    }
}
