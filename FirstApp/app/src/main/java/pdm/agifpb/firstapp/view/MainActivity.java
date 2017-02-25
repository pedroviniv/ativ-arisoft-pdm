package pdm.agifpb.firstapp.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pdm.agifpb.firstapp.R;
import pdm.agifpb.firstapp.domain.Chat;
import pdm.agifpb.firstapp.domain.User;
import pdm.agifpb.firstapp.services.ChatSession;
import pdm.agifpb.firstapp.services.ContactsService;
import pdm.agifpb.firstapp.services.UserSession;
import pdm.agifpb.firstapp.view.adapter.ChatListAdapter;
import pdm.agifpb.firstapp.view.util.LogUser;
import pdm.agifpb.firstapp.view.util.Print;

public class MainActivity extends AppCompatActivity {

    //Buttons
    private Button tabChat;
    private Button tabMissions;
    //Mission Buttons
    private Button btNewMission;
    private Button btOldMissions;
    private Button btConfig;
    private Button btGallery;
    //Layout
    private LinearLayout frameChat;
    private LinearLayout frameMissions;
    //TextView
    private TextView txName;
    //ListView
    private ListView lvChats;
    //Chats adapter
    private ChatListAdapter chatAdapter;
    //Chats
    private List<Chat> chats;


    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        initTextViews();
        initFrames();
        initButtons();
        initListView();

        this.chats = ChatSession.getChats();

        if(ChatSession.isEmpty()) {
            loadContacts();
        }
        //
        prepareChatAdapter();
    }

    /**
     * instancia o ChatAdapter
     */
    private void prepareChatAdapter() {
        this.chatAdapter = new ChatListAdapter(chats, this);
        lvChats.setAdapter(chatAdapter);
    }

    /**
     * Inicializa ListView contendo os chats
     */
    private void initListView() {
        this.lvChats = (ListView) findViewById(R.id.lv_chats);
        this.lvChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chat chat = (Chat) parent.getItemAtPosition(position);
                openChatActivity(chat);
            }
        });
    }

    /**
     * Abre ChatActivity passando o chat selecionado
     * @param chat
     */
    private void openChatActivity(Chat chat) {
        Intent intent = new Intent(this, ChatActivity.class);

        intent.putExtra("chat", chat);

        startActivity(intent);
    }

    private void initTextViews() {
        this.txName = (TextView) findViewById(R.id.tx_name);
        txName.setText(UserSession.getLoggedUser() != null ? UserSession.getLoggedUser().getName() : "Sem usuário");
    }

    private void initFrames() {
        this.frameChat = (LinearLayout) findViewById(R.id.frame_chat);
        this.frameMissions = (LinearLayout) findViewById(R.id.frame_missions);
    }

    private void initButtons() {
        this.tabChat = (Button) findViewById(R.id.tab_chat);
        this.tabMissions = (Button) findViewById(R.id.tab_missions);
        this.btNewMission = (Button) findViewById(R.id.bt_new_mission);
        this.btOldMissions = (Button) findViewById(R.id.bt_old_mission);
        this.btConfig = (Button) findViewById(R.id.bt_config);
        this.btGallery = (Button) findViewById(R.id.bt_gallery);

        tabChat.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                tabChat.setBackgroundColor(Color.parseColor("#55888888"));
                tabMissions.setBackgroundColor(Color.parseColor("#00000000"));
                frameMissions.setVisibility(View.GONE);
                frameChat.setVisibility(View.VISIBLE);
            }
        });

        tabMissions.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                tabChat.setBackgroundColor(Color.parseColor("#00000000"));
                tabMissions.setBackgroundColor(Color.parseColor("#55888888"));
                frameChat.setVisibility(View.GONE);
                frameMissions.setVisibility(View.VISIBLE);
            }
        });

        this.btNewMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Nova Missão!");
            }
        });

        this.btOldMissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Missões Anteriores!");
            }
        });

        this.btConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Configuração!");
            }
        });

        this.btGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("Galeria!");
            }
        });
    }

    private void toast(String message) {

        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.show();
    }

    /**
     * Atualiza o Adapter da ListView dos chats na thread principal
     */
    private void updateListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                prepareChatAdapter();
            }
        });
    }

    /**
     * Inicia thread para solicitar chats do usuário logado ao servidor.
     */
    private void loadContacts() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d(LogUser.NAME, "Carregando Chats do servidor... ");
                chats = loadChats();
                //
                String string = new Print<Chat>().print(chats);
                Log.d(LogUser.NAME, "Chats carregados: "+string);
                //
                Log.d(LogUser.NAME, "Salvando Chats na Sessão... ");
                ChatSession.setChats(chats);
                //
                updateListView();
            }
        });

        thread.start();
    }

    /**
     * Solicita chats do usuário logado ao servidor
     * @return Lista com Chats do Usuário logado
     */
    private List<Chat> loadChats() {
        User loggedUser = UserSession.getLoggedUser();
        ContactsService service = new ContactsService();
        return service.getChats(loggedUser);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            UserSession.logout();
            ChatSession.clean();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.chatAdapter.notifyDataSetChanged();
    }
}
