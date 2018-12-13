package br.com.cinq.androidskilltest.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import br.com.cinq.androidskilltest.R;
import br.com.cinq.androidskilltest.util.SessaoSharedPreferences;


public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etSenha;
    private Button btLogar;
    private TextView tvAviso;
    private TextView tvCadastrar;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        if (SessaoSharedPreferences.isUsuarioLogado(getApplicationContext())) {
            viewModel.iniciarTelaPrincipal();
        }

        inicializarViews();
        inicializarListeners();
        inicializarObservers();
    }

    private void inicializarViews() {

        etEmail = (EditText) findViewById(R.id.et_email);
        etSenha = (EditText) findViewById(R.id.et_senha);
        btLogar = (Button) findViewById(R.id.bt_logar);
        tvAviso = (TextView) findViewById(R.id.tv_aviso);
        tvCadastrar = (TextView) findViewById(R.id.tv_cadastrar);
    }

    private void inicializarListeners() {

        btLogar.setOnClickListener(v -> {
            onClickLogar();
        });

        tvCadastrar.setOnClickListener(v -> {
            onClickCadastrar();
        });
    }

    private void inicializarObservers() {

        viewModel.getMensagemAviso().observe(this, novoAviso -> {
            tvAviso.setText(novoAviso);
        });

    }

    private void onClickCadastrar() {

        viewModel.iniciarTelaCadastro();

    }

    private void onClickLogar() {

        etSenha.setError(null);
        etEmail.setError(null);

        if (isCamposObrigatoriosValidos()) {

            String emailInformado = etEmail.getText().toString();
            String senhaInformada = etSenha.getText().toString();
            viewModel.onClickLogin(emailInformado, senhaInformada);


        }

    }

    private boolean isCamposObrigatoriosValidos() {

        boolean camposValidos = true;

        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("Informe um email!");
            etEmail.requestFocus();
            camposValidos = false;
        }

        if (TextUtils.isEmpty(etSenha.getText())) {
            etSenha.setError("Informe uma senha!");
            etSenha.requestFocus();
            camposValidos = false;
        }

        return camposValidos;
    }


}

