package br.com.cinq.androidskilltest.dominio.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import br.com.cinq.androidskilltest.R;
import br.com.cinq.androidskilltest.dominio.cadastro.CadastroActivity;
import br.com.cinq.androidskilltest.util.SessaoSharedPreferences;


public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOGIN = 5;
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

        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_senha);
        btLogar = findViewById(R.id.bt_logar);
        tvAviso = findViewById(R.id.tv_aviso);
        tvCadastrar = findViewById(R.id.tv_cadastrar);
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

        Intent intent = new Intent(getApplication(), CadastroActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        viewModel.zerarAviso();

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

