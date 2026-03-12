from django.contrib import admin
from .models import Producto


@admin.register(Producto)
class ProductoAdmin(admin.ModelAdmin):
    list_display = ['codigo', 'nombre', 'precio', 'marca', 'cantidad', 'inventario', 'fecha']
    search_fields = ['nombre', 'marca']