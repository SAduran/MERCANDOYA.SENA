from django.contrib import admin
from django.urls import path, include
from productos.urls import router

urlpatterns = [
    path('admin/', admin.site.urls),
    path('', include('productos.urls')),
    path('api/', include(router.urls)),
]