package com.example.mercandoyaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private List<Producto> productos = new ArrayList<>();
    private OnProductoClickListener listener;

    public interface OnProductoClickListener {
        void onEditarClick(Producto producto);
        void onEliminarClick(Producto producto);
    }

    public ProductoAdapter(OnProductoClickListener listener) {
        this.listener = listener;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvMarca.setText(producto.getMarca());
        holder.tvPrecio.setText("$ " + producto.getPrecio());
        holder.tvCodigo.setText("Código: " + producto.getCodigo());
        holder.tvCantidad.setText("Cantidad: " + producto.getCantidad());
        holder.tvInventario.setText("Inventario: " + producto.getInventario());
        holder.btnEditar.setOnClickListener(v -> listener.onEditarClick(producto));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminarClick(producto));
    }

    @Override
    public int getItemCount() { return productos.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvMarca, tvPrecio, tvCodigo, tvCantidad, tvInventario;
        Button btnEditar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvMarca = itemView.findViewById(R.id.tvMarca);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvInventario = itemView.findViewById(R.id.tvInventario);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}