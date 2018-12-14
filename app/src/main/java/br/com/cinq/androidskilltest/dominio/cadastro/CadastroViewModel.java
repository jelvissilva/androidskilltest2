package br.com.cinq.androidskilltest.dominio.cadastro;

import android.app.Application;
import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.room.EmptyResultSetException;
import br.com.cinq.androidskilltest.persistencia.Usuario;
import br.com.cinq.androidskilltest.repositorio.UsuarioRepository;
import br.com.cinq.androidskilltest.util.BundleAndroidViewModel;
import br.com.cinq.androidskilltest.util.SessaoSharedPreferences;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CadastroViewModel extends BundleAndroidViewModel {

    private Disposable disposableConsultaUsuario;
    private Disposable disposableGravar;
    private UsuarioRepository usuarioRepositorio;
    private MutableLiveData<String> mensagemAviso = new MutableLiveData<>();
    private Boolean edicao = false;
    private MutableLiveData<Usuario> usuarioEditado = new MutableLiveData<>();
    private MutableLiveData<Boolean> estadoMsgConfirmacao = new MutableLiveData<>();

    public CadastroViewModel(Application application, Bundle bundle) {
        super(application, bundle);
        estadoMsgConfirmacao.setValue(false);
        usuarioRepositorio = new UsuarioRepository(getApplication());

        int idUsuario = bundle.getInt(CadastroActivity.TAG_ID_USUARIO, -1);

        if (isEdicao(idUsuario)) {
            edicao = true;
            carregarUsuarioEditado(idUsuario);
        } else {
            usuarioEditado.setValue(new Usuario());
        }

    }

    private void carregarUsuarioEditado(int idUsuario) {

        resetaDisposables();

        disposableConsultaUsuario = usuarioRepositorio.getUsuarioPorID(idUsuario)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(usuario -> {

                    getUsuarioEditado().setValue(usuario);

                }, falha -> {
                    falha.printStackTrace();

                    if (falha instanceof EmptyResultSetException) {
                        mensagemAviso.postValue("ID editado não encontrado! ");
                    } else {
                        mensagemAviso.postValue("Falha desconhecida [" + falha.getMessage() + "]");
                    }

                });
    }


    private boolean isEdicao(int idUsuario) {
        return idUsuario != -1;
    }

    public void onClickGravar() {

        resetaDisposables();
        mensagemAviso.postValue("Gravando...");
        validarEmailUtilizado();

    }

    private void validarEmailUtilizado() {

        Usuario usuario = usuarioEditado.getValue();
        String emailInformado = usuario.getEmail();
        Integer idUsuario = edicao ? usuario.getId() : 0;

        disposableConsultaUsuario = usuarioRepositorio.countEmailUtilizado(emailInformado, idUsuario)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(qtd -> {

                    if (qtd > 0) {
                        mensagemAviso.postValue("O email informado já esta sendo utilizado ");
                    } else if (isEdicao()) {
                        atualizarUsuario();
                    } else {
                        cadastrarUsuario();
                    }


                }, falha -> {
                    mensagemAviso.postValue("Falha desconhecida [" + falha.getMessage() + "]");
                });

    }

    private void cadastrarUsuario() {

        disposableGravar = Completable.fromAction(() -> usuarioRepositorio.inserirUsuario(usuarioEditado.getValue()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            onGravarComSucesso();
                        },
                        throwable -> {
                            mensagemAviso.postValue("Falha ao inserir [" + throwable + "]");
                        }
                );

    }

    private void atualizarUsuario() {

        disposableGravar = Completable.fromAction(() -> usuarioRepositorio.atualizarUsuario(usuarioEditado.getValue()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {

                            atualizarUsuarioLogado();
                            onGravarComSucesso();
                        },
                        throwable -> {
                            mensagemAviso.postValue("Falha ao atualizar [" + throwable + "]");
                        }
                );


    }

    private void onGravarComSucesso() {
        estadoMsgConfirmacao.postValue(true);
        mensagemAviso.postValue("");
    }

    private void atualizarUsuarioLogado() {

        Usuario usuarioAtualizado = usuarioEditado.getValue();
        if (SessaoSharedPreferences.getIDUsuarioLogado(getApplication()) == usuarioAtualizado.getId()) {
            SessaoSharedPreferences.setUsuarioLogado(getApplication(), usuarioAtualizado);
        }

    }

    private void resetaDisposables() {
        if (disposableConsultaUsuario != null && !disposableConsultaUsuario.isDisposed()) {
            disposableConsultaUsuario.dispose();
        }

        if (disposableGravar != null && !disposableGravar.isDisposed()) {
            disposableGravar.dispose();
        }
    }


    @Override
    protected void onCleared() {
        resetaDisposables();
        super.onCleared();
    }

    public MutableLiveData<String> getMensagemAviso() {
        return mensagemAviso;
    }


    public Boolean isEdicao() {
        return edicao;
    }

    public MutableLiveData<Usuario> getUsuarioEditado() {
        return usuarioEditado;
    }

    public void onEmailChanged(String valor) {

        Usuario usuario = usuarioEditado.getValue();
        usuario.setEmail(valor);
    }

    public void onSenhaChanged(String valor) {

        Usuario usuario = usuarioEditado.getValue();
        usuario.setSenha(valor);
    }

    public void onNomeChanged(String valor) {

        Usuario usuario = usuarioEditado.getValue();
        usuario.setNome(valor);
    }

    public MutableLiveData<Boolean> getEstadoMsgConfirmacao() {
        return estadoMsgConfirmacao;
    }

}
