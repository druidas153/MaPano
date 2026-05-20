package com.myaplicacion.mapano.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myaplicacion.mapano.R;
import com.myaplicacion.mapano.model.DeseoLugar;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter para el RecyclerView de la Lista de Deseos.
 */
public class DeseoAdapter extends RecyclerView.Adapter<DeseoAdapter.DeseoViewHolder> {

    private List<DeseoLugar> deseos = new ArrayList<>();
    private OnDeseoClickListener listener;

    // ========================
    // INTERFACE PARA CLICKS
    // ========================

    public interface OnDeseoClickListener {
        void onDeseoClick(DeseoLugar deseo);       // Click normal → editar
        void onDeseoLongClick(DeseoLugar deseo);   // Click largo → eliminar
    }

    public void setOnDeseoClickListener(OnDeseoClickListener listener) {
        this.listener = listener;
    }

    // ========================
    // ACTUALIZAR DATOS
    // ========================

    public void setDeseos(List<DeseoLugar> nuevosDeseos) {
        this.deseos = nuevosDeseos;
        notifyDataSetChanged();
    }

    // ========================
    // ADAPTER METHODS
    // ========================

    @NonNull
    @Override
    public DeseoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deseo, parent, false);
        return new DeseoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeseoViewHolder holder, int position) {
        DeseoLugar deseo = deseos.get(position);
        holder.bind(deseo);
    }

    @Override
    public int getItemCount() {
        return deseos.size();
    }

    // ========================
    // VIEW HOLDER
    // ========================

    class DeseoViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvIconoCategoria;
        private final TextView tvNombreLugar;
        private final TextView tvCategoria;
        private final TextView tvNotaPersonal;
        private final TextView tvEstado;
        private final TextView tvPrioridad;

        public DeseoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIconoCategoria = itemView.findViewById(R.id.tvIconoCategoria);
            tvNombreLugar = itemView.findViewById(R.id.tvNombreLugar);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvNotaPersonal = itemView.findViewById(R.id.tvNotaPersonal);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvPrioridad = itemView.findViewById(R.id.tvPrioridad);

            // Click normal → editar
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onDeseoClick(deseos.get(getAdapterPosition()));
                }
            });

            // Click largo → eliminar
            itemView.setOnLongClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onDeseoLongClick(deseos.get(getAdapterPosition()));
                }
                return true;
            });
        }

        public void bind(DeseoLugar deseo) {
            tvNombreLugar.setText(deseo.getNombreLugar());
            tvCategoria.setText(capitalizarCategoria(deseo.getCategoria()));
            tvEstado.setText(deseo.getEstadoTexto());
            tvPrioridad.setText(deseo.getPrioridadTexto());

            // Icono según categoría
            tvIconoCategoria.setText(getIconoCategoria(deseo.getCategoria()));
            tvIconoCategoria.setBackgroundResource(0); // Quitar background drawable

            // Nota personal
            if (deseo.getNotaPersonal() != null && !deseo.getNotaPersonal().isEmpty()) {
                tvNotaPersonal.setText("\"" + deseo.getNotaPersonal() + "\"");
                tvNotaPersonal.setVisibility(View.VISIBLE);
            } else {
                tvNotaPersonal.setVisibility(View.GONE);
            }
        }

        private String getIconoCategoria(String categoria) {
            if (categoria == null) return "📍";
            switch (categoria) {
                case "restaurante": return "🍴";
                case "evento": return "🎭";
                case "farmacia": return "💊";
                case "taxi": return "🚕";
                default: return "📍";
            }
        }

        private String capitalizarCategoria(String categoria) {
            if (categoria == null || categoria.isEmpty()) return "";
            return categoria.substring(0, 1).toUpperCase() + categoria.substring(1);
        }
    }
}
