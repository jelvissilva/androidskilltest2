package br.com.cinq.androidskilltest.cadastro;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import br.com.cinq.androidskilltest.R;
import br.com.cinq.androidskilltest.persistencia.Usuario;
import br.com.cinq.androidskilltest.util.BundleViewModelFactory;


public class CadastroActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etSenha;
    private EditText etNome;
    private Button btCadastrar;
    private TextView tvAviso;
    private Toolbar toolbar;
    private AlertDialog alertDialog;
    public static final String TAG_ID_USUARIO = "TAG_ID_USUARIO";

    private CadastroViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        viewModel = ViewModelProviders.of(this, new BundleViewModelFactory(this.getApplication(), getIntent().getExtras())).get(CadastroViewModel.class);

        inicializarViews();
        recarregarValoresExibidos();
        inicializarListeners();
        inicializarObservers();

        if (viewModel.isEdicao()) {
            etEmail.setEnabled(false);
            btCadastrar.setText(R.string.confirmar_edicao);
            toolbar.setTitle(R.string.titulo_edicao);
        }

    }

    private void inicializarViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        etEmail = (EditText) findViewById(R.id.et_email);
        etNome = (EditText) findViewById(R.id.et_nome);
        etSenha = (EditText) findViewById(R.id.et_senha);
        btCadastrar = (Button) findViewById(R.id.bt_salvar);
        tvAviso = (TextView) findViewById(R.id.tv_aviso);

    }

    private void recarregarValoresExibidos() {

        Usuario usuario = viewModel.getUsuarioEditado().getValue();
        if (usuario != null) {
            etEmail.setText(usuario.getEmail());
            etNome.setText(usuario.getNome());
            etSenha.setText(usuario.getSenha());
        }


    }

    private void carregarValoresEdicao(Usuario usuario) {

        etEmail.setText(usuario.getEmail());
        etNome.setText(usuario.getNome());
        etSenha.setText(usuario.getSenha());


    }

    private void inicializarListeners() {

        btCadastrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                onClickCadastrar();
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable valor) {
                viewModel.onEmailChanged(valor.toString());
            }
        });

        etSenha.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable valor) {
                viewModel.onSenhaChanged(valor.toString());
            }
        });

        etNome.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable valor) {
                viewModel.onNomeChanged(valor.toString());
            }
        });

    }

    private void inicializarObservers() {

        viewModel.getMensagemAviso().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String novoAviso) {
                tvAviso.setText(novoAviso);
            }

        });

        viewModel.getEstadoMsgConfirmacao().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean exibir) {

                onEstadoMsgConfirmacaoChanged(exibir);

            }

        });

        viewModel.getUsuarioEditado().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                carregarValoresEdicao(usuario);
            }
        });

    }

    private void onEstadoMsgConfirmacaoChanged(boolean exibir) {
        if (exibir) {
            exibirDialogConfirmacao();
        } else {
            esconderDialogConfirmacao();
        }
    }

    private void esconderDialogConfirmacao() {

        if (isExibindoDialogConfirmacao()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    private boolean isExibindoDialogConfirmacao() {
        return alertDialog != null;
    }

    private void exibirDialogConfirmacao() {

        esconderDialogConfirmacao();

        String mensagemSucesso = viewModel.isEdicao() ? "Edição realizada" : "Cadastro realizado";
        mensagemSucesso += " com sucesso! ";

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Atenção!")
                .setMessage(mensagemSucesso)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        setResult(RESULT_OK);
                        finish();

                    }
                })
                .create();

        alertDialog.show();


    }

    private void onClickCadastrar() {

        etEmail.setError(null);
        etNome.setError(null);
        etSenha.setError(null);


        if (isCamposObrigatoriosValidos()) {

            viewModel.onClickGravar();

        }

    }

    @Override
    protected void onDestroy() {
        if (isExibindoDialogConfirmacao()) {
            esconderDialogConfirmacao();
        }
        super.onDestroy();

    }

    private boolean isCamposObrigatoriosValidos() {

        boolean camposValidos = true;

        if (TextUtils.isEmpty(etSenha.getText())) {
            etSenha.setError("Informe uma senha!");
            etSenha.requestFocus();
            camposValidos = false;
        }

        if (TextUtils.isEmpty(etNome.getText())) {
            etNome.setError("Informe o nome!");
            etNome.requestFocus();
            camposValidos = false;
        }

        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("Informe um email!");
            etEmail.requestFocus();
            camposValidos = false;
        }


        return camposValidos;
    }


}

