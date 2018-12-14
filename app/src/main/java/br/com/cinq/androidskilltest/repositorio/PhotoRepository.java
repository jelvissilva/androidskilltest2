package br.com.cinq.androidskilltest.repositorio;

import android.app.Application;

import java.util.List;

import br.com.cinq.androidskilltest.network.NetworkClient;
import br.com.cinq.androidskilltest.network.NetworkInterface;
import br.com.cinq.androidskilltest.network.PhotosResponse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PhotoRepository {

    public PhotoRepository(Application application) {
    }

    public Observable<List<PhotosResponse>> getPhotos() {
        return NetworkClient.getRetrofit().create(NetworkInterface.class)
                .getPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
