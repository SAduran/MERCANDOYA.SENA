package com.example.mercandoyaapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormProductoActivity extends AppCompatActivity {

    private TextInputEditText etCodigo, etNombre, etPrecio, etMarca, etCantidad, etInventario;
    private ApiService apiService;
    private int productoId = -1;
    private boolean esEdicion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_producto);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        TextView tvTitulo = findViewById(R.id.tvTituloForm);
        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        etMarca = findViewById(R.id.etMarca);
        etCantidad = findViewById(R.id.etCantidad);
        etInventario = findViewById(R.id.etInventario);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        apiService = RetrofitClient.getApiService();

        if (getIntent().hasExtra("producto_id")) {
            esEdicion = true;
            productoId = getIntent().getIntExtra("producto_id", -1);
            tvTitulo.setText("Editar Producto");
            btnGuardar.setText("ACTUALIZAR PRODUCTO");
            etCodigo.setText(String.valueOf(getIntent().getIntExtra("codigo", 0)));
            etNombre.setText(getIntent().getStringExtra("nombre"));
            etPrecio.setText(getIntent().getStringExtra("precio"));
            etMarca.setText(getIntent().getStringExtra("marca"));
            etCantidad.setText(String.valueOf(getIntent().getIntExtra("cantidad", 0)));
            etInventario.setText(String.valueOf(getIntent().getIntExtra("inventario", 0)));
        }

        btnGuardar.setOnClickListener(v -> guardarProducto());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void guardarProducto() {
        String codigoStr = etCodigo.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();
        String marca = etMarca.getText().toString().trim();
        String cantidadStr = etCantidad.getText().toString().trim();
        String inventarioStr = etInventario.getText().toString().trim();

        if (codigoStr.isEmpty() || nombre.isEmpty() || precio.isEmpty() ||
                marca.isEmpty() || cantidadStr.isEmpty() || inventarioStr.isEmpty()) {
            Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Producto producto = new Producto();
        producto.setCodigo(Integer.parseInt(codigoStr));
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setMarca(marca);
        producto.setCantidad(Integer.parseInt(cantidadStr));
        producto.setInventario(Integer.parseInt(inventarioStr));

        if (esEdicion) {
            apiService.actualizarProducto(productoId, producto).enqueue(new Callback<Producto>() {
                @Override
                public void onResponse(Call<Producto> call, Response<Producto> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(FormProductoActivity.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(FormProductoActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Producto> call, Throwable t) {
                    Toast.makeText(FormProductoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            apiService.crearProducto(producto).enqueue(new Callback<Producto>() {
                @Override
                public void onResponse(Call<Producto> call, Response<Producto> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(FormProductoActivity.this, "Producto creado exitosamente", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(FormProductoActivity.this, "Error al crear producto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Producto> call, Throwable t) {
                    Toast.makeText(FormProductoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}