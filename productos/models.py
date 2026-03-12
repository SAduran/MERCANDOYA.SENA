from django.db import models


class Producto(models.Model):
    codigo = models.IntegerField(unique=True)
    nombre = models.CharField(max_length=100)
    precio = models.DecimalField(max_digits=10, decimal_places=2)
    marca = models.CharField(max_length=100)
    cantidad = models.IntegerField()
    fecha = models.DateField(auto_now_add=True)
    inventario = models.IntegerField()

    def __str__(self):
        return f"{self.codigo} - {self.nombre}"

    class Meta:
        ordering = ['codigo']