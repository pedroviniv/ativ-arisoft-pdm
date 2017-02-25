package pdm.agifpb.firstapp.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pdm.agifpb.firstapp.R;
import pdm.agifpb.firstapp.domain.Chat;
import pdm.agifpb.firstapp.domain.Contact;
import pdm.agifpb.firstapp.domain.Message;
import pdm.agifpb.firstapp.domain.User;
import pdm.agifpb.firstapp.services.UserSession;

/**
 * Created by Pedro Arthur on 20/02/2017.
 */

public class ChatMessageListAdapter extends BaseAdapter {

    private Context context;
    private Chat chat;

    public ChatMessageListAdapter(Chat chat, Context context) {
        this.chat = chat;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.chat.getMessages().size();
    }

    @Override
    public Object getItem(int position) {
        return this.chat.getMessages().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 1L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message current = chat.getMessages().get(position);

        User loggedUser = UserSession.getLoggedUser();
        Contact contact = chat.getContact();

        int balloonId = 0;
        String actor = "";

        if(current.getPublisherId().equals(loggedUser.getEmail())) {
            balloonId = R.layout.box_right_balloon;
            actor = loggedUser.getName();
        } else {
            balloonId = R.layout.box_left_balloon;
            actor = contact.getName();
        }
        convertView = LayoutInflater.from(context).inflate(balloonId, null);

        TextView tvMessageActor = (TextView) convertView.findViewById(R.id.tv_message_actor);
        tvMessageActor.setText(actor);
        String msgActor = tvMessageActor.getText().toString();
        //
        TextView tvMessageContent = (TextView) convertView.findViewById(R.id.tv_message_content);
        String msgContent = tvMessageContent.getText().toString();
        tvMessageContent.setText(current.getText());


        return convertView;
    }
}
