package com.example.proyectoveterinario_adrianisado_danielquinones.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoveterinario_adrianisado_danielquinones.R;
import com.example.proyectoveterinario_adrianisado_danielquinones.objetos.HistorialMedico;

import java.util.ArrayList;

public class AdaptadorHistorialMedicoRecyclerView extends RecyclerView.Adapter<AdaptadorHistorialMedicoRecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<HistorialMedico> historialesMedicos;

    public AdaptadorHistorialMedicoRecyclerView(Context context, ArrayList<HistorialMedico> historialesMedicos) {
        this.context = context;
        this.historialesMedicos = historialesMedicos;
    }

    // Método que infla el diseño de cada elemento de la lista y devuelve una instancia de ViewHolder.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_historial_medico, parent, false);
        return new ViewHolder(view);
    }

    // Método que establece los datos en cada elemento de la lista.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistorialMedico historialMedico = historialesMedicos.get(position);

        holder.tvFechaCita.setText(historialMedico.getFechaCita());
        String[] partesTitulo = historialMedico.getTituloCita().split("-");
        holder.tvTituloCita.setText(partesTitulo[0]);
        holder.tvDescripcionCita.setText(historialMedico.getDescripcionCita());

        if (historialMedico.getPrecioCita() != 0.0) {
            holder.tvPrecioCita.setText(String.format("%s €", historialMedico.getPrecioCita()));
        } else {
            holder.tvPrecioCita.setText(String.format("%s € %s", "???", "(CONSULTA CON EL VETERINARIO)"));
        }
    }

    // Método que devuelve la cantidad de elementos en la lista.
    @Override
    public int getItemCount() {
        return historialesMedicos.size();
    }

    // Clase ViewHolder que representa cada elemento de la lista y contiene las vistas a modificar.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFechaCita, tvTituloCita, tvDescripcionCita, tvPrecioCita;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFechaCita = itemView.findViewById(R.id.tvFechaCita);
            tvTituloCita = itemView.findViewById(R.id.tvTituloCita);
            tvDescripcionCita = itemView.findViewById(R.id.tvDescripcionCita);
            tvPrecioCita = itemView.findViewById(R.id.tvPrecioCita);
        }
    }
}
