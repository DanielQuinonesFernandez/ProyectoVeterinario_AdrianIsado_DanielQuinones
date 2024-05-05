package com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.HistorialMedico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdaptadorHistorialMedico extends BaseAdapter {
    private final Context context;
    private final ArrayList<HistorialMedico> historialesMedicos;

    public AdaptadorHistorialMedico(Context context, ArrayList<HistorialMedico> historialMedico) {
        this.context = context;
        this.historialesMedicos = historialMedico;
    }

    @Override
    public int getCount() {
        return historialesMedicos.size();
    }

    @Override
    public Object getItem(int position) {
        return historialesMedicos.get(position);
    }

    public long getItemId(int position) {
        return historialesMedicos.get(position).getId();
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        view = layoutInflater.inflate(R.layout.item_historial_medico, null);

        TextView tvFechaCita = view.findViewById(R.id.tvFechaCita);
        TextView tvTituloCita = view.findViewById(R.id.tvTituloCita);
        TextView tvDescripcionCita = view.findViewById(R.id.tvDescripcionCita);
        TextView tvPrecioCita = view.findViewById(R.id.tvPrecioCita);

        tvFechaCita.setText(historialesMedicos.get(i).getFechaCita());
        String[] partesTitulo = historialesMedicos.get(i).getTituloCita().split("-");
        tvTituloCita.setText(partesTitulo[0]);
        tvDescripcionCita.setText(historialesMedicos.get(i).getDescripcionCita());

        if (historialesMedicos.get(i).getPrecioCita() != 0.0) {
            tvPrecioCita.setText(String.format("%s €", historialesMedicos.get(i).getPrecioCita()));
        } else {
            tvPrecioCita.setText(String.format("%s € %s", "???", "(CONSULTA CON EL VETERINARIO)"));
        }

        return view;

    }
}