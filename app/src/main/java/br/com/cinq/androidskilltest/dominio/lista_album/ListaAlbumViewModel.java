package br.com.cinq.androidskilltest.lista_album;

import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import br.com.cinq.androidskilltest.network.PhotosResponse;
import br.com.cinq.androidskilltest.repositorio.PhotoRepository;
import br.com.cinq.androidskilltest.util.BundleAndroidViewModel;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ListaAlbumViewModel extends BundleAndroidViewModel {

    private Disposable disposableConsultaPhotos;

    private PhotoRepository photoRepositorio;
    private MutableLiveData<List<PhotosResponse>> listaPhotos = new MutableLiveData<>();


    public ListaAlbumViewModel(Application application, Bundle bundle) {
        super(application, bundle);

        getListaPhotos().setValue(new ArrayList<>());

        photoRepositorio = new PhotoRepository(getApplication());

        carregarPhotos();

    }

    public void carregarPhotos() {

        getListaPhotos().getValue().clear();
        resetaDisposables();
        disposableConsultaPhotos = photoRepositorio.getPhotos().subscribeWith(getObserver());
    }


    public DisposableObserver<List<PhotosResponse>> getObserver() {
        return new DisposableObserver<List<PhotosResponse>>() {

            @Override
            public void onNext(@NonNull List<PhotosResponse> photosResponse) {
                getListaPhotos().setValue(photosResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
            }
        };
    }

    public MutableLiveData<List<PhotosResponse>> getListaPhotos() {
        return listaPhotos;
    }


    private void resetaDisposables() {

        if (disposableConsultaPhotos != null && !disposableConsultaPhotos.isDisposed()) {
            disposableConsultaPhotos.dispose();
        }

    }

    @Override
    protected void onCleared() {
        resetaDisposables();
        super.onCleared();
    }


}