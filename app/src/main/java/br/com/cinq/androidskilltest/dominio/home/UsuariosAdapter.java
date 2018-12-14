package br.com.cinq.androidskilltest.dominio.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import br.com.cinq.androidskilltest.R;
import br.com.cinq.androidskilltest.persistencia.Usuario;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Usuario> items;
    private List<Usuario> itemsFiltered;
    private UsuariosAdapterListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvName);

            view.findViewById(R.id.ib_edit).setOnClickListener(v ->
                    listener.onEditarClicked(itemsFiltered.get(getAdapterPosition())));

            view.findViewById(R.id.ib_delete).setOnClickListener(v ->
                    listener.onExcluirClicked(itemsFiltered.get(getAdapterPosition()))
            );
        }
    }

    public UsuariosAdapter(Context context, List<Usuario> items, UsuariosAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.items = items;
        this.itemsFiltered = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_item_usuario, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final String nome = itemsFiltered.get(position).getNome();
        holder.name.setText(nome);

    }

    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();

                List<Usuario> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered = items;
                } else {

                    for (Usuario usuario : items) {

                        if (usuario.getNome().toLowerCase().contains(query.toLowerCase())) {
                            filtered.add(usuario);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                itemsFiltered = (ArrayList<Usuario>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface UsuariosAdapterListener {

        void onEditarClicked(Usuario usuario);

        void onExcluirClicked(Usuario usuario);
    }
}