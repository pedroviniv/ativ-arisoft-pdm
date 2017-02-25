package pdm.agifpb.firstapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import pdm.agifpb.firstapp.R;
import pdm.agifpb.firstapp.domain.Chat;
import pdm.agifpb.firstapp.domain.Message;
import pdm.agifpb.firstapp.services.ChatService;
import pdm.agifpb.firstapp.services.ChatSession;
import pdm.agifpb.firstapp.services.SubscriberService;
import pdm.agifpb.firstapp.services.UserSession;
import pdm.agifpb.firstapp.view.adapter.ChatMessageListAdapter;
import pdm.agifpb.firstapp.view.util.LogUser;

public class ChatActivity extends AppCompatActivity {

    private Button btnSendMessage;
    private EditText etMessageContent;
    private Chat chat;
    private ListView lvChatMessages;
    private BaseAdapter chatAdapter;
    private TextView tvChatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //instancia componentes
        initButton();
        initEditText();
        initTextView();
        //recebe a intenção e recupera instância de chat passada
        Intent intent = getIntent();
        Chat chat = (Chat) intent.getSerializableExtra("chat");
        this.chat = chat;
        //
        //Uma vez recuperado o chat, atualiza o nome do chat
        tvChatName.setText(chat.getContact().getName());

        //inicializa a ListView
        initList();
        //Se inscreve no servidor e começa a ser notificado para receber mensagens
        startSubscribe();
    }

    private void initTextView() {
        this.tvChatName = (TextView) findViewById(R.id.tv_chat_name);
    }

    private void initEditText() {
        this.etMessageContent = (EditText) findViewById(R.id.et_msg_content);
    }

    private void initList() {
        this.lvChatMessages = (ListView) findViewById(R.id.lv_chat);
        this.chatAdapter = new ChatMessageListAdapter(chat, this);
        this.lvChatMessages.setAdapter(chatAdapter);
    }

    private void initButton() {
        this.btnSendMessage = (Button) findViewById(R.id.send_message_btn);
        btnSendMessage.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                String msg = etMessageContent.getText().toString();
                //Instancia objeto da mensagem. ps: Id da mensagem é gerada no servidor!
                Message message = new Message("", UserSession.getLoggedUser().getEmail(), chat.getContact().getEmail(), msg);
                Log.d(LogUser.NAME, "Publishing message "+message);
                publish(message);
            }
        });
    }

    /**
     * Inicia thread com procedimento de subscribe
     */
    private void startSubscribe() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                subcribe();
            }
        });
        thread.start();
    }

    /**
     * Se inscreve no servidor para receber notificações de mensagens
     */
    private void subcribe() {
        SubscriberService subscriberService = new SubscriberService();
        subscriberService.subscribe(UserSession.getLoggedUser(), chat, this,chatAdapter);
    }

    /**
     * Envia uma mensagem ao servidor
     * @param message mensagem a ser enviada para o servidor
     */
    private void publish(final Message message) {
        //Instancia a classe responsável por realizar o envio da mensagem para o servidor
        final ChatService service = new ChatService();
        //
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(LogUser.NAME, "New thread! Enviando mensagem...");
                if(service.enviarMensagem(message)) {
                    Log.d(LogUser.NAME, "Mensagem enviada!");
                    chat.addMessage(message);
                    Log.d(LogUser.NAME, "Adiconou na lista de mensagens. Atualizando ListView...");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatAdapter.notifyDataSetChanged();
                            ChatSession.addMessage(message);
                            etMessageContent.setText("");
                        }
                    });
                }
            }
        });

        thread.start();
    }


}
