package pdm.agifpb.firstapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pdm.agifpb.firstapp.R;
import pdm.agifpb.firstapp.domain.Credentials;
import pdm.agifpb.firstapp.domain.User;
import pdm.agifpb.firstapp.services.LoginService;
import pdm.agifpb.firstapp.services.UserSession;
import pdm.agifpb.firstapp.view.util.LogUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etSenha;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //inicializa componentes
        init();
    }

    private void init() {
        this.etEmail = (EditText) findViewById(R.id.et_email);
        this.etSenha = (EditText) findViewById(R.id.et_senha);
        this.btLogin = (Button) findViewById(R.id.bt_login);

        this.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(etEmail, etSenha);
            }
        });
    }

    /**
     * Realiza o procedimento de login e as credenciais estejam certas e o servidor
     * responda com o objeto de Usuário logado, o método openMainActivity() é chamado e
     * a Activity Main é aberta
     * @param etEmail componente visual de campo de texto de email
     * @param etSenha componente visual de campo de texto de senha
     */
    private void login(final EditText etEmail, final EditText etSenha) {

        //recupera credenciais inseridas nos campos
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        //Instancia objeto Credentials com as credenciais recuperadas
        final Credentials cred = new Credentials(email,senha);

        //Thread que usa o LoginService para solicitar ao servidor o procedimento de Login
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(LogUser.NAME, "Solicitando Login no Servidor...");
                //executa requisição
                final User user = new LoginService().signIn(cred);
                Log.d(LogUser.NAME, "Resposta User: "+user);

                if(user != null) {
                    //Login deu certo
                    UserSession.login(user);
                    //Após logado abre a ActivityMain
                    openMainActivity();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast("Usuário/Senha incorreto(s)!");
                        }
                    });
                }
            }
        };

        Thread thread = new Thread(runnable);
        //Inicia a thread acima
        thread.start();
    }

    private void toast(String message) {

        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.show();
    }

    /**
     * Abre a MainActivity
     */
    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
