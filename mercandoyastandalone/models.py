class Producto:
    def __init__(self, codigo, nombre, precio, marca, cantidad, fecha, inventario):
        self.codigo = codigo
        self.nombre = nombre
        self.precio = precio
        self.marca = marca
        self.cantidad = cantidad
        self.fecha = fecha
        self.inventario = inventario

    def __str__(self):
        return f"Producto({self.codigo}, {self.nombre}, ${self.precio}, {self.marca})"