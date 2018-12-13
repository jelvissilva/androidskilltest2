package br.com.cinq.androidskilltest.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.cinq.androidskilltest.R;
import br.com.cinq.androidskilltest.cadastro.CadastroActivity;
import br.com.cinq.androidskilltest.lista_album.ListaAlbumActivity;
import br.com.cinq.androidskilltest.persistencia.Usuario;
import br.com.cinq.androidskilltest.util.BundleViewModelFactory;
import br.com.cinq.androidskilltest.util.SessaoSharedPreferences;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UsuariosAdapter.UsuariosAdapterListener {

    private RecyclerView rvUsuarios;
    private UsuariosAdapter adapter;
    private final static int TAMANHO_MAX_FILTRO = 50;
    private HomeViewModel viewModel;
    private TextView tvNomeUsuarioLogado;
    private TextView tvEmailUsuarioLogado;
    public static final int REQUEST_CODE_HOME = 3;
    private AlertDialog alertaMensagemExclusao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        viewModel = ViewModelProviders.of(this, new BundleViewModelFactory(this.getApplication(), getIntent().getExtras())).get(HomeViewModel.class);

        inicializarViews();

        viewModel.getUsuarioLogado().observe(this, usuario -> {

            tvNomeUsuarioLogado.setText(usuario.getNome());
            tvEmailUsuarioLogado.setText(usuario.getEmail());


        });

        viewModel.getListaUsuarios().observe(this, listaUsuarios -> {

            adapter = new UsuariosAdapter(this, listaUsuarios, this);
            rvUsuarios.swapAdapter(adapter,false);

        });

        viewModel.getMensagemAviso().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String mensagem) {

                if (!TextUtils.isEmpty(mensagem)) {

                    Toast.makeText(getBaseContext(), mensagem, Toast.LENGTH_LONG).show();
                    viewModel.onAvisoExibido();
                }


            }
        });
        viewModel.getUsuarioSolicitadoExclusao().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {

                onUsuarioSolicitadoExclusaoChanged(usuario);
            }

        });

    }

    private void onUsuarioSolicitadoExclusaoChanged(Usuario usuario) {
        if (usuario != null) {
            exibirDialogConfirmacaoExclusao(usuario);
        } else {
            esconderDialogConfirmacaoExclusao();
        }
    }

    private void esconderDialogConfirmacaoExclusao() {

        if (isExibindoDialogConfirmacao()) {
            alertaMensagemExclusao.dismiss();
            alertaMensagemExclusao = null;
        }
    }

    private boolean isExibindoDialogConfirmacao() {
        return alertaMensagemExclusao != null;
    }

    private void exibirDialogConfirmacaoExclusao(Usuario usuario) {

        esconderDialogConfirmacaoExclusao();

        String mensagem = "Tem certeza que deseja excluir o usuario " + usuario.getNome() + "? ";

        alertaMensagemExclusao = new AlertDialog.Builder(this)
                .setTitle("Atenção!")
                .setMessage(mensagem)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        viewModel.onConfirmarExclusaoUsuario(usuario);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.onCancelarExclusaoUsuario();
                        dialog.dismiss();
                    }
                })
                .create();

        alertaMensagemExclusao.show();

    }

    private void inicializarViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        tvNomeUsuarioLogado = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_nome_usuario_logado);
        tvEmailUsuarioLogado = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_email_usuario_logado);

        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);

        rvUsuarios = findViewById(R.id.rv_usuarios);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        rvUsuarios.setLayoutManager(lm);
        rvUsuarios.setItemAnimator(new DefaultItemAnimator());


        FloatingActionButton fabCadastrar = (FloatingActionButton) findViewById(R.id.fab_adicionar);
        fabCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplication(), CadastroActivity.class);
                startActivityForResult(intent, REQUEST_CODE_HOME);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        inicializarSearchView(menu);

        return true;

    }

    private void inicializarSearchView(Menu menu) {

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });


        TextView et = (TextView) searchView.findViewById(R.id.search_src_text);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(TAMANHO_MAX_FILTRO)});

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //do nothing
        } else if (id == R.id.nav_lista_album) {

            Intent intent = new Intent(getApplication(), ListaAlbumActivity.class);
            getApplication().startActivity(intent);

        } else if (id == R.id.nav_deslogar) {

            SessaoSharedPreferences.setDeslogado(getApplication());
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSelected(Usuario usuario) {
        Toast.makeText(this, "Selecionado: " + usuario.getNome(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditarClicked(Usuario usuario) {

        Intent intent = new Intent(getApplication(), CadastroActivity.class);
        intent.putExtra(CadastroActivity.TAG_ID_USUARIO, usuario.getId());

        startActivityForResult(intent, REQUEST_CODE_HOME);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            viewModel.onCadastradoAlterado();
        }

    }

    @Override
    public void onExcluirClicked(Usuario usuario) {

        viewModel.onSolicitarExcluirUsuario(usuario);

    }
}
