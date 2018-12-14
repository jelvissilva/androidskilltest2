package br.com.cinq.androidskilltest.network;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotosResponse {

    @SerializedName("albumId")
    @Expose
    private Integer albumId;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose
    private String titulo;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("thumbnailUrl")
    @Expose
    private String thumbnailUrl;

    /**
     * No args constructor for use in serialization
     */
    public PhotosResponse() {
    }


    public PhotosResponse(Integer albumId, Integer id, String title, String url, String thumbnailUrl) {
        super();
        this.setAlbumId(albumId);
        this.setId(id);
        this.setTitulo(title);
        this.setUrl(url);
        this.setThumbnailUrl(thumbnailUrl);
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}