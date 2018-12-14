package br.com.cinq.androidskilltest.dominio.lista_album;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.cinq.androidskilltest.R;
import br.com.cinq.androidskilltest.util.BundleViewModelFactory;
import br.com.cinq.androidskilltest.util.EmptyRecyclerView;

public class ListaAlbumActivity extends AppCompatActivity {

    private ListaAlbumViewModel viewModel;
    private EmptyRecyclerView rvPhotos;
    private PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_album);

        viewModel = ViewModelProviders.of(this, new BundleViewModelFactory(this.getApplication(), getIntent().getExtras())).get(ListaAlbumViewModel.class);

        inicializarViews();
        inicializarObservers();

    }

    private void inicializarViews() {
        rvPhotos = findViewById(R.id.rv_album);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        rvPhotos.setLayoutManager(lm);
        rvPhotos.setItemAnimator(new DefaultItemAnimator());
        rvPhotos.setEmptyView(findViewById(R.id.empty_view));

    }

    private void inicializarObservers() {

        viewModel.getListaPhotos().observe(this, listaPhoto -> {

            adapter = new PhotoAdapter(listaPhoto, getBaseContext());
            rvPhotos.setAdapter(adapter);
            rvPhotos.setEmptyView(findViewById(R.id.empty_view));

        });
    }
}
