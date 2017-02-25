package pdm.agifpb.firstapp.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pedro Arthur on 20/02/2017.
 */

public class Contact implements JsonObject<Contact>, Serializable{

    private String email;
    private String name;

    private List<Message> messages;

    public Contact(String email, String name) {
        this.email = email;
        this.name = name;
        this.messages = new ArrayList<>();
    }

    public Contact() {
        this.messages = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(this.messages);
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void addMessages(Collection<Message> messages) {
        this.messages.addAll(messages);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
    }

    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, Contact.class);
    }

    @Override
    public Contact fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, this.getClass());
    }

    @Override
    public String toString() {
        return "Contact{" + "email=" + email + ", name=" + name + ", messages=" + messages + '}';
    }
}
