package br.com.cinq.androidskilltest.login;

import android.app.Application;
import android.content.Intent;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.EmptyResultSetException;
import br.com.cinq.androidskilltest.cadastro.CadastroActivity;
import br.com.cinq.androidskilltest.home.HomeActivity;
import br.com.cinq.androidskilltest.persistencia.Usuario;
import br.com.cinq.androidskilltest.repositorio.UsuarioRepository;
import br.com.cinq.androidskilltest.util.SessaoSharedPreferences;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {

    private Disposable disposableConsultaUsuario;
    private UsuarioRepository usuarioRepositorio;
    private MutableLiveData<String> mensagemAviso = new MutableLiveData<>();


    public LoginViewModel(Application application) {
        super(application);
        usuarioRepositorio = new UsuarioRepository(getApplication());
    }

    public void onClickLogin(String emailInformado, final String senhaInformada) {

        resetaDisposableConsulta();
        mensagemAviso.postValue("Logando...");

        disposableConsultaUsuario = usuarioRepositorio.getUsuario(emailInformado)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(usuario -> {

                    if (validarCredenciais(usuario, senhaInformada)) {
                        executarLogin(usuario);
                    }

                }, falha -> {
                    falha.printStackTrace();

                    if (falha instanceof EmptyResultSetException) {
                        mensagemAviso.postValue("Email inexistente! ");
                    } else {
                        mensagemAviso.postValue("Falha desconhecida ao validar [" + falha.getMessage() + "]");
                    }

                });
    }


    private void executarLogin(Usuario usuario) {

        SessaoSharedPreferences.setUsuarioLogado(getApplication(), usuario);
        iniciarTelaPrincipal();

    }


    private void resetaDisposableConsulta() {
        if (disposableConsultaUsuario != null && !disposableConsultaUsuario.isDisposed()) {
            disposableConsultaUsuario.dispose();
        }
    }

    private boolean validarCredenciais(Usuario usuario, String senhaInformada) {

        if (usuario == null) {
            mensagemAviso.postValue("Email inexistente!");
            return false;
        }

        if (!comparaSenhaIgual(usuario, senhaInformada)) {
            mensagemAviso.postValue("Senha inválida!");
            return false;
        }

        return true;

    }

    private boolean comparaSenhaIgual(Usuario usuario, String senhaInformada) {
        return usuario.getSenha().trim().equals(senhaInformada.trim());
    }

    @Override
    protected void onCleared() {
        resetaDisposableConsulta();
        super.onCleared();
    }

    public MutableLiveData<String> getMensagemAviso() {
        return mensagemAviso;
    }

    public void iniciarTelaPrincipal() {

        Intent intent = new Intent(getApplication(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplication().startActivity(intent);

    }

    public void iniciarTelaCadastro() {

        Intent intent = new Intent(getApplication(), CadastroActivity.class);
        getApplication().startActivity(intent);

    }
}
