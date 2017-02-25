package pdm.agifpb.firstapp.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pdm.agifpb.firstapp.R;
import pdm.agifpb.firstapp.domain.Chat;
import pdm.agifpb.firstapp.domain.Message;

/**
 * Created by Pedro Arthur on 23/02/2017.
 */

public class ChatListAdapter extends ArrayAdapter<Chat> {

    public ChatListAdapter(List<Chat> chats, Context context) {
        super(context, 0, chats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Chat current = getItem(position);
        List<Message> msgs = current.getMessages();
        //
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.box_listitem, null);
        //
        TextView tvInitialName = (TextView) convertView.findViewById(R.id.tv_initial_name);
        tvInitialName.setText(current.getContact().getName().substring(0, 2));

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_list_item_name);
        tvName.setText(current.getContact().getName());

        TextView tvLastMsg = (TextView) convertView.findViewById(R.id.tv_last_message);
        tvLastMsg.setText(msgs.isEmpty() ? "" : msgs.get(msgs.size()-1).getText());

        return convertView;

    }



}
