package br.com.cinq.androidskilltest.repositorio;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface NetworkInterface {

    @GET("/photos")
    Observable<List<PhotosResponse>> getPhotos();
}
