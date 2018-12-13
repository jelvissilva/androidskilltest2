package br.com.cinq.androidskilltest.lista_album;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import br.com.cinq.androidskilltest.R;
import br.com.cinq.androidskilltest.network.PhotosResponse;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    List<PhotosResponse> photoList;
    Context context;

    public PhotoAdapter(List<PhotosResponse> photoList, Context context) {
        this.photoList = photoList;
        this.context = context;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_item_photos, parent, false);
        PhotoHolder ph = new PhotoHolder(v);
        return ph;
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {

        holder.tvTitulo.setText(photoList.get(position).getTitulo());
        Glide.with(context).load(photoList.get(position).getThumbnailUrl()).into(holder.ivThumbnail);

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo;
        ImageView ivThumbnail;

        public PhotoHolder(View v) {
            super(v);
            tvTitulo = (TextView) v.findViewById(R.id.tv_titulo);
            ivThumbnail = (ImageView) v.findViewById(R.id.iv_thumbnail);
        }
    }
}