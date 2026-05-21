package com.myaplicacion.mapano.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myaplicacion.mapano.R;
import com.myaplicacion.mapano.model.FotoLugar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter para el RecyclerView de la galería de fotos.
 */
public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.FotoViewHolder> {

    private List<FotoLugar> fotos = new ArrayList<>();
    private OnFotoClickListener listener;

    // ========================
    // INTERFACE PARA CLICKS
    // ========================

    public interface OnFotoClickListener {
        void onFotoClick(FotoLugar foto);          // Ver foto en grande
        void onEliminarClick(FotoLugar foto);      // Eliminar foto
    }

    public void setOnFotoClickListener(OnFotoClickListener listener) {
        this.listener = listener;
    }

    // ========================
    // ACTUALIZAR DATOS
    // ========================

    public void setFotos(List<FotoLugar> nuevasFotos) {
        this.fotos = nuevasFotos;
        notifyDataSetChanged();
    }

    // ========================
    // ADAPTER METHODS
    // ========================

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_foto, parent, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        FotoLugar foto = fotos.get(position);
        holder.bind(foto);
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    // ========================
    // VIEW HOLDER
    // ========================

    class FotoViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivFoto;
        private final TextView tvComentario;
        private final TextView tvFecha;
        private final ImageButton btnEliminar;

        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            tvComentario = itemView.findViewById(R.id.tvComentario);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);

            // Click en la foto → ver en grande
            ivFoto.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onFotoClick(fotos.get(getAdapterPosition()));
                }
            });

            // Click en eliminar
            btnEliminar.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onEliminarClick(fotos.get(getAdapterPosition()));
                }
            });
        }

        public void bind(FotoLugar foto) {
            // Cargar imagen desde la ruta del archivo
            File archivoFoto = new File(foto.getRutaArchivo());
            if (archivoFoto.exists()) {
                ivFoto.setImageURI(Uri.fromFile(archivoFoto));
            } else {
                ivFoto.setImageResource(android.R.drawable.ic_menu_gallery);
            }

            // Comentario
            if (foto.getComentario() != null && !foto.getComentario().isEmpty()) {
                tvComentario.setText(foto.getComentario());
                tvComentario.setVisibility(View.VISIBLE);
            } else {
                tvComentario.setVisibility(View.GONE);
            }

            // Fecha
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String fecha = sdf.format(new Date(foto.getFechaCaptura()));
            tvFecha.setText(fecha);
        }
    }
}