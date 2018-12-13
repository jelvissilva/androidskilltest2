package br.com.cinq.androidskilltest.lista_album;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.cinq.androidskilltest.R;
import br.com.cinq.androidskilltest.util.BundleViewModelFactory;

public class ListaAlbumActivity extends AppCompatActivity {

    private ListaAlbumViewModel viewModel;
    private RecyclerView rvPhotos;
    private PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_album);

        viewModel = ViewModelProviders.of(this, new BundleViewModelFactory(this.getApplication(), getIntent().getExtras())).get(ListaAlbumViewModel.class);

        rvPhotos = findViewById(R.id.rv_album);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        rvPhotos.setLayoutManager(lm);
        rvPhotos.setItemAnimator(new DefaultItemAnimator());

        viewModel.getListaPhotos().observe(this, listaPhoto -> {

            adapter = new PhotoAdapter(listaPhoto, getBaseContext());
            rvPhotos.swapAdapter(adapter, false);

        });


    }
}
