package br.com.cinq.androidskilltest.home;

import android.app.Application;
import android.os.Bundle;

import java.util.List;

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

public class HomeViewModel extends BundleAndroidViewModel {

    private Disposable disposableConsultaListaUsuario;
    private Disposable disposableConsultaUsuario;
    private Disposable disposableExcluir;
    private UsuarioRepository usuarioRepositorio;
    private MutableLiveData<String> mensagemAviso = new MutableLiveData<>();
    private MutableLiveData<Usuario> usuarioLogado = new MutableLiveData<>();
    private MutableLiveData<List<Usuario>> listaUsuarios = new MutableLiveData<>();
    private MutableLiveData<Usuario> usuarioSolicitadoExclusao = new MutableLiveData<>();

    public HomeViewModel(Application application, Bundle bundle) {
        super(application, bundle);

        usuarioSolicitadoExclusao.setValue(null);

        usuarioRepositorio = new UsuarioRepository(getApplication());

        carregarUsuarioLogado();
        carregarListaUsuarios();

    }

    private void carregarListaUsuarios() {

        resetaDisposable(disposableConsultaListaUsuario);
        resetaDisposable(disposableExcluir);

        disposableConsultaListaUsuario = usuarioRepositorio.getTodosUsuarios()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lista -> getListaUsuarios().setValue(lista)
                        , falha -> {
                            falha.printStackTrace();

                            if (falha instanceof EmptyResultSetException) {
                                mensagemAviso.postValue("Nenhum usuario encontrado! ");
                            } else {
                                mensagemAviso.postValue("Falha desconhecida [" + falha.getMessage() + "]");
                            }

                        });
    }


    private void carregarUsuarioLogado() {

        int idUsuario = SessaoSharedPreferences.getIDUsuarioLogado(getApplication());

        resetaDisposable(disposableConsultaUsuario);
        resetaDisposable(disposableExcluir);

        disposableConsultaUsuario = usuarioRepositorio.getUsuarioPorID(idUsuario)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(usuario -> getUsuarioLogado().setValue(usuario)
                        , falha -> {
                            falha.printStackTrace();

                            if (falha instanceof EmptyResultSetException) {
                                mensagemAviso.postValue("Usuario logado não encontrado! ");
                            } else {
                                mensagemAviso.postValue("Falha desconhecida [" + falha.getMessage() + "]");
                            }

                        });
    }

    private void resetaDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void resetaTodosDisposables() {

        resetaDisposable(disposableConsultaUsuario);
        resetaDisposable(disposableConsultaListaUsuario);
        resetaDisposable(disposableExcluir);

    }


    @Override
    protected void onCleared() {
        resetaTodosDisposables();
        super.onCleared();
    }

    public MutableLiveData<String> getMensagemAviso() {
        return mensagemAviso;
    }


    public MutableLiveData<Usuario> getUsuarioSolicitadoExclusao() {
        return usuarioSolicitadoExclusao;
    }

    public MutableLiveData<Usuario> getUsuarioLogado() {
        return usuarioLogado;
    }

    public MutableLiveData<List<Usuario>> getListaUsuarios() {
        return listaUsuarios;
    }

    private boolean isUsuarioLogado(Usuario usuarioComparado) {
        return usuarioLogado.getValue().getId() == usuarioComparado.getId();
    }

    public void onSolicitarExcluirUsuario(Usuario usuario) {

        if (isUsuarioLogado(usuario)) {
            mensagemAviso.postValue("Não é possivel excluir o usuario logado!");
            return;
        }

        usuarioSolicitadoExclusao.postValue(usuario);

    }

    public void onAvisoExibido() {
        mensagemAviso.setValue("");
    }

    public void onCancelarExclusaoUsuario() {
        usuarioSolicitadoExclusao.postValue(null);
    }

    public void onConfirmarExclusaoUsuario(Usuario usuario) {

        resetaTodosDisposables();

        disposableExcluir = Completable.fromAction(() -> usuarioRepositorio.excluirUsuario(usuario))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            carregarListaUsuarios();
                            mensagemAviso.postValue("Usuario excluido com sucesso!");
                        },
                        throwable -> mensagemAviso.postValue("Falha ao excluir [" + throwable + "]")

                );

    }

    public void onCadastradoAlterado() {

        carregarUsuarioLogado();
        carregarListaUsuarios();

    }

}
