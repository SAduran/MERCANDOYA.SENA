package com.example.mercandoyaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ProductoAdapter.OnProductoClickListener {

    private RecyclerView recyclerProductos;
    private TextView tvVacio;
    private ProductoAdapter adapter;
    private ApiService apiService;

    private final ActivityResultLauncher<Intent> formLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    cargarProductos();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerProductos = findViewById(R.id.recyclerProductos);
        tvVacio = findViewById(R.id.tvVacio);
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregar);

        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductoAdapter(this);
        recyclerProductos.setAdapter(adapter);

        apiService = RetrofitClient.getApiService();

        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormProductoActivity.class);
            formLauncher.launch(intent);
        });

        cargarProductos();
    }

    private void cargarProductos() {
        apiService.getProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> lista = response.body();
                    adapter.setProductos(lista);
                    if (lista.isEmpty()) {
                        recyclerProductos.setVisibility(View.GONE);
                        tvVacio.setVisibility(View.VISIBLE);
                    } else {
                        recyclerProductos.setVisibility(View.VISIBLE);
                        tvVacio.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onEditarClick(Producto producto) {
        Intent intent = new Intent(this, FormProductoActivity.class);
        intent.putExtra("producto_id", producto.getId());
        intent.putExtra("codigo", producto.getCodigo());
        intent.putExtra("nombre", producto.getNombre());
        intent.putExtra("precio", producto.getPrecio());
        intent.putExtra("marca", producto.getMarca());
        intent.putExtra("cantidad", producto.getCantidad());
        intent.putExtra("inventario", producto.getInventario());
        formLauncher.launch(intent);
    }

    @Override
    public void onEliminarClick(Producto producto) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar producto")
                .setMessage("¿Seguro que quieres eliminar " + producto.getNombre() + "?")
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    apiService.eliminarProducto(producto.getId()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(MainActivity.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                            cargarProductos();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}