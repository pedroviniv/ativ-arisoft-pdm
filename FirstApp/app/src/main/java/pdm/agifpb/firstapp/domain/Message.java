package pdm.agifpb.firstapp.domain;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Pedro Arthur on 20/02/2017.
 */

public class Message implements JsonObject<Message>, Serializable {

    private String identify;
    private String publisherId;
    private String subscriberId;
    private String text;

    public Message(String identify, String publisherId, String subscriberId, String text) {
        this.identify = identify;
        this.publisherId = publisherId;
        this.subscriberId = subscriberId;
        this.text = text;
    }

    public Message() {

    }

    public String getIdentify() {
        return identify;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public String getText() {
        return text;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public void setText(String text) {
        this.text = text;
    }

    //Num mundo real deveria ser implementado um equals e hashcode

    @Override
    public String toString() {
        return "Message{" + "identify=" + identify + ", publisherId=" + publisherId + ", subscriberId=" + subscriberId + ", text=" + text + '}';
    }

    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, Message.class);
    }

    @Override
    public Message fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, this.getClass());
    }
}
