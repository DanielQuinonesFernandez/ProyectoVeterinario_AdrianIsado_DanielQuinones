package com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mascota;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.Mensaje;

import java.util.ArrayList;

public class AdaptadorMensajes extends BaseAdapter {
    private final Context context;
    private final ArrayList<Mensaje> mensajes;

    public AdaptadorMensajes(Context context, ArrayList<Mensaje> mensajes) {
        this.context = context;
        this.mensajes = mensajes;
    }

    @Override
    public int getCount() {
        return mensajes.size();
    }

    @Override
    public Object getItem(int position) {
        return mensajes.get(position);
    }

    public long getItemId(int position) {
        return mensajes.get(position).getId();
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        view = layoutInflater.inflate(R.layout.item_mensaje, null);

        TextView tvTituloMensaje = view.findViewById(R.id.tvTituloMensaje);
        ImageButton btnBorrarMensaje = view.findViewById(R.id.btnBorrarMensaje);
        TextView tvTipoMensaje = view.findViewById(R.id.tvTipoMensaje);
        TextView tvContenidoMensaje = view.findViewById(R.id.tvContenidoMensaje);

        tvTituloMensaje.setText(mensajes.get(i).getAsunto());
        tvTipoMensaje.setText(mensajes.get(i).getTipoMensaje());
        tvContenidoMensaje.setText(mensajes.get(i).getContenido());

        return view;
    }
}
