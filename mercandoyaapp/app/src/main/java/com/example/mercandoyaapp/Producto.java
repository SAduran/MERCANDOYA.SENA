package com.example.mercandoyaapp;

import com.google.gson.annotations.SerializedName;

public class Producto {

    @SerializedName("id")
    private int id;

    @SerializedName("codigo")
    private int codigo;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("precio")
    private String precio;

    @SerializedName("marca")
    private String marca;

    @SerializedName("cantidad")
    private int cantidad;

    @SerializedName("fecha")
    private String fecha;

    @SerializedName("inventario")
    private int inventario;

    public Producto() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPrecio() { return precio; }
    public void setPrecio(String precio) { this.precio = precio; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public int getInventario() { return inventario; }
    public void setInventario(int inventario) { this.inventario = inventario; }
}