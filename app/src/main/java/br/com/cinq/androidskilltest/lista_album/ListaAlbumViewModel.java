package br.com.cinq.androidskilltest.lista_album;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import br.com.cinq.androidskilltest.persistencia.Usuario;
import br.com.cinq.androidskilltest.persistencia.UsuarioRepository;
import br.com.cinq.androidskilltest.repositorio.NetworkClient;
import br.com.cinq.androidskilltest.repositorio.NetworkInterface;
import br.com.cinq.androidskilltest.repositorio.PhotosResponse;
import br.com.cinq.androidskilltest.util.BundleAndroidViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ListaAlbumViewModel extends BundleAndroidViewModel {

    private Disposable disposableConsultaPhotos;

    private UsuarioRepository usuarioRepositorio;
    private MutableLiveData<String> mensagemAviso = new MutableLiveData<>();
    private MutableLiveData<Usuario> usuarioLogado = new MutableLiveData<>();
    private MutableLiveData<List<PhotosResponse>> listaPhotos = new MutableLiveData<>();
    private MutableLiveData<Usuario> usuarioSolicitadoExclusao = new MutableLiveData<>();


    public ListaAlbumViewModel(Application application, Bundle bundle) {
        super(application, bundle);

        getListaPhotos().setValue(new ArrayList<>());
        usuarioSolicitadoExclusao.setValue(null);

        usuarioRepositorio = new UsuarioRepository(getApplication());

        carregarPhotos();

    }

    public void carregarPhotos() {

        getListaPhotos().getValue().clear();
        getObservable().subscribeWith(getObserver());
    }

    public Observable<List<PhotosResponse>> getObservable() {
        return NetworkClient.getRetrofit().create(NetworkInterface.class)
                .getPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public DisposableObserver<List<PhotosResponse>> getObserver() {
        return new DisposableObserver<List<PhotosResponse>>() {

            @Override
            public void onNext(@NonNull List<PhotosResponse> photosResponse) {

                Log.d("error", "on next photos "+photosResponse.size());
                getListaPhotos().setValue(photosResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("error", "Falha " + e);
                e.printStackTrace();
                //mvi.displayError("Error fetching Movie Data");
            }

            @Override
            public void onComplete() {
                Log.d("error", "Completou lista");

            }
        };
    }

    public MutableLiveData<List<PhotosResponse>> getListaPhotos() {
        return listaPhotos;
    }


//    private void carregarListaUsuarios() {
//
//        resetaDisposable(disposableConsultaListaUsuario);
//        resetaDisposable(disposableExcluir);
//
//        disposableConsultaListaUsuario = usuarioRepositorio.getTodosUsuarios()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(lista -> {
//
//                    getListaUsuarios().setValue(lista);
//
//                }, falha -> {
//                    falha.printStackTrace();
//
//                    if (falha instanceof EmptyResultSetException) {
//                        mensagemAviso.postValue("Nenhum usuario encontrado! ");
//                    } else {
//                        mensagemAviso.postValue("Falha desconhecida [" + falha.getMessage() + "]");
//                    }
//
//                });
//    }
//
//
//    private void carregarUsuarioLogado() {
//
//        int idUsuario = SessaoSharedPreferences.getIDUsuarioLogado(getApplication());
//
//        resetaDisposable(disposableConsultaUsuario);
//        resetaDisposable(disposableExcluir);
//
//        disposableConsultaUsuario = usuarioRepositorio.getUsuarioPorID(idUsuario)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(usuario -> {
//
//                    getUsuarioLogado().setValue(usuario);
//
//                }, falha -> {
//                    falha.printStackTrace();
//
//                    if (falha instanceof EmptyResultSetException) {
//                        mensagemAviso.postValue("Usuario logado não encontrado! ");
//                    } else {
//                        mensagemAviso.postValue("Falha desconhecida [" + falha.getMessage() + "]");
//                    }
//
//                });
//    }
//
//    private void resetaDisposable(Disposable disposable) {
//        if (disposable != null && !disposable.isDisposed()) {
//            disposable.dispose();
//        }
//    }
//
//    private void resetaTodosDisposables() {
//
//        resetaDisposable(disposableConsultaUsuario);
//        resetaDisposable(disposableConsultaListaUsuario);
//        resetaDisposable(disposableExcluir);
//
//    }
//
//
//    @Override
//    protected void onCleared() {
//        resetaTodosDisposables();
//        super.onCleared();
//    }
//
//    public MutableLiveData<String> getMensagemAviso() {
//        return mensagemAviso;
//    }
//
//
//    public MutableLiveData<Usuario> getUsuarioSolicitadoExclusao() {
//        return usuarioSolicitadoExclusao;
//    }
//
//    public MutableLiveData<Usuario> getUsuarioLogado() {
//        return usuarioLogado;
//    }
//
//    public MutableLiveData<List<Usuario>> getListaUsuarios() {
//        return listaUsuarios;
//    }
//
//    private boolean isUsuarioLogado(Usuario usuarioComparado) {
//        return usuarioLogado.getValue().getId() == usuarioComparado.getId();
//    }
//
//    public void onSolicitarExcluirUsuario(Usuario usuario) {
//
//        if (isUsuarioLogado(usuario)) {
//            mensagemAviso.postValue("Não é possivel excluir o usuario logado!");
//            return;
//        }
//
//        usuarioSolicitadoExclusao.postValue(usuario);
//
//    }
//
//    public void onAvisoExibido() {
//        mensagemAviso.setValue("");
//    }
//
//    public void onCancelarExclusaoUsuario() {
//        usuarioSolicitadoExclusao.postValue(null);
//    }
//
//    public void onConfirmarExclusaoUsuario(Usuario usuario) {
//
//        resetaTodosDisposables();
//
//        disposableExcluir = Completable.fromAction(() -> usuarioRepositorio.excluirUsuario(usuario))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(() -> {
//
//                            carregarListaUsuarios();
//                            mensagemAviso.postValue("Usuario excluido com sucesso!");
//                        },
//                        throwable -> {
//                            mensagemAviso.postValue("Falha ao excluir [" + throwable + "]");
//                        }
//                );
//
//    }
//
//
//    public void onCadastradoAlterado() {
//
//        carregarUsuarioLogado();
//        carregarListaUsuarios();
//
//
//    }


}
