package com.example.mercandoyaapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("productos/")
    Call<List<Producto>> getProductos();

    @POST("productos/")
    Call<Producto> crearProducto(@Body Producto producto);

    @PUT("productos/{id}/")
    Call<Producto> actualizarProducto(@Path("id") int id, @Body Producto producto);

    @DELETE("productos/{id}/")
    Call<Void> eliminarProducto(@Path("id") int id);
}