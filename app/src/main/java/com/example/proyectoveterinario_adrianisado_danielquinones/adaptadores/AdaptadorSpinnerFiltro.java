package com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mensaje;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorSpinnerFiltro extends BaseAdapter {

    private final Context context;
    private final List<Mensaje> listaMensajes;

    public AdaptadorSpinnerFiltro(Context context, List<Mensaje> listaMensajes) {
        this.context = context;
        this.listaMensajes = listaMensajes;
    }

    @Override
    public int getCount() {
        return listaMensajes != null ? listaMensajes.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_filtrar_por, parent, false);

        TextView textoFiltro = rootView.findViewById(R.id.descripcionFiltro);

        textoFiltro.setText(listaMensajes.get(position).getTipoMensaje());

        return rootView;
    }
}
