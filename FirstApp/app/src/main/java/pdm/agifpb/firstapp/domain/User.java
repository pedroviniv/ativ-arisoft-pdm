package pdm.agifpb.firstapp.domain;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Pedro Arthur on 20/02/2017.
 */

public class User implements JsonObject<User>, Serializable {

        private String name;
        private String email;
        private String password;

        public User(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        public User() {

        }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean authenticate(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, User.class);
    }

    @Override
    public User fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", email=" + email + ", password=" + password + '}';
    }
}
