import tkinter as tk
from tkinter import ttk, messagebox
from database import Database

class AppProductos:
    def __init__(self, root):
        self.root = root
        self.root.title("Mercando Ya - CRUD Productos")
        self.root.geometry("900x600")
        self.root.configure(bg="#1a2332")

        self.db = Database()
        self.codigo_seleccionado = None

        self.crear_widgets()
        self.refrescar_tabla()

    def crear_widgets(self):
        # Titulo
        titulo = tk.Label(
            self.root, text="MERCANDO-YA",
            font=("Arial", 20, "bold"), fg="#f0c040", bg="#1a2332"
        )
        titulo.pack(pady=10)

        # Frame del formulario
        frame_form = tk.Frame(self.root, bg="#1e3a5f", padx=20, pady=15)
        frame_form.pack(fill="x", padx=20)

        # Campos del formulario
        campos = [
            ("Código:", "entry_codigo"),
            ("Nombre:", "entry_nombre"),
            ("Precio:", "entry_precio"),
            ("Marca:", "entry_marca"),
            ("Cantidad:", "entry_cantidad"),
            ("Inventario:", "entry_inventario"),
        ]

        for i, (label_text, attr_name) in enumerate(campos):
            col = (i % 3) * 2
            fila = i // 3

            label = tk.Label(
                frame_form, text=label_text,
                font=("Arial", 10, "bold"), fg="white", bg="#1e3a5f"
            )
            label.grid(row=fila, column=col, sticky="w", padx=5, pady=5)

            entry = tk.Entry(frame_form, font=("Arial", 10), width=20)
            entry.grid(row=fila, column=col + 1, padx=5, pady=5)
            setattr(self, attr_name, entry)

        Frame de botones
        frame_botones = tk.Frame(self.root, bg="#1a2332")
        frame_botones.pack(pady=10)

        botones = [
            ("Guardar", "#28a745", self.guardar),
            ("Actualizar", "#ffc107", self.actualizar),
            ("Eliminar", "#dc3545", self.eliminar),
            ("Limpiar", "#17a2b8", self.limpiar),
        ]

        for texto, color, comando in botones:
            btn = tk.Button(
                frame_botones, text=texto, bg=color, fg="white",
                font=("Arial", 10, "bold"), width=12, command=comando
            )
            btn.pack(side="left", padx=5)

        # Tabla de productos
        frame_tabla = tk.Frame(self.root, bg="#1a2332")
        frame_tabla.pack(fill="both", expand=True, padx=20, pady=10)

        columnas = ("Código", "Nombre", "Precio", "Marca", "Cantidad", "Fecha", "Inventario")
        self.tabla = ttk.Treeview(frame_tabla, columns=columnas, show="headings", height=10)

        for col in columnas:
            self.tabla.heading(col, text=col)
            self.tabla.column(col, width=120, anchor="center")

        scrollbar = ttk.Scrollbar(frame_tabla, orient="vertical", command=self.tabla.yview)
        self.tabla.configure(yscrollcommand=scrollbar.set)

        self.tabla.pack(side="left", fill="both", expand=True)
        scrollbar.pack(side="right", fill="y")

        self.tabla.bind("<<TreeviewSelect>>", self.seleccionar)

    def validar_campos(self):
        if not self.entry_codigo.get().strip():
            messagebox.showerror("Error", "El código es obligatorio.")
            return False
        if not self.entry_nombre.get().strip():
            messagebox.showerror("Error", "El nombre es obligatorio.")
            return False
        if not self.entry_precio.get().strip():
            messagebox.showerror("Error", "El precio es obligatorio.")
            return False
        if not self.entry_marca.get().strip():
            messagebox.showerror("Error", "La marca es obligatoria.")
            return False
        if not self.entry_cantidad.get().strip():
            messagebox.showerror("Error", "La cantidad es obligatoria.")
            return False
        if not self.entry_inventario.get().strip():
            messagebox.showerror("Error", "El inventario es obligatorio.")
            return False

        try:
            int(self.entry_codigo.get())
        except ValueError:
            messagebox.showerror("Error", "El código debe ser un número entero.")
            return False

        try:
            float(self.entry_precio.get())
        except ValueError:
            messagebox.showerror("Error", "El precio debe ser un número válido.")
            return False

        try:
            int(self.entry_cantidad.get())
        except ValueError:
            messagebox.showerror("Error", "La cantidad debe ser un número entero.")
            return False

        try:
            int(self.entry_inventario.get())
        except ValueError:
            messagebox.showerror("Error", "El inventario debe ser un número entero.")
            return False

        return True

    def guardar(self):
        if not self.validar_campos():
            return

        resultado = self.db.insertar(
            int(self.entry_codigo.get()),
            self.entry_nombre.get().strip(),
            float(self.entry_precio.get()),
            self.entry_marca.get().strip(),
            int(self.entry_cantidad.get()),
            int(self.entry_inventario.get())
        )

        if resultado:
            messagebox.showinfo("Éxito", "Producto registrado correctamente.")
            self.limpiar()
            self.refrescar_tabla()
        else:
            messagebox.showerror("Error", "Ya existe un producto con ese código.")

    def actualizar(self):
        if self.codigo_seleccionado is None:
            messagebox.showwarning("Advertencia", "Seleccione un producto de la tabla.")
            return

        if not self.validar_campos():
            return

        resultado = self.db.actualizar(
            int(self.entry_codigo.get()),
            self.entry_nombre.get().strip(),
            float(self.entry_precio.get()),
            self.entry_marca.get().strip(),
            int(self.entry_cantidad.get()),
            int(self.entry_inventario.get())
        )

        if resultado:
            messagebox.showinfo("Éxito", "Producto actualizado correctamente.")
            self.limpiar()
            self.refrescar_tabla()
        else:
            messagebox.showerror("Error", "No se pudo actualizar el producto.")

    def eliminar(self):
        if self.codigo_seleccionado is None:
            messagebox.showwarning("Advertencia", "Seleccione un producto de la tabla.")
            return

        confirmar = messagebox.askyesno(
            "Confirmar Eliminación",
            f"¿Está seguro de eliminar el producto con código {self.codigo_seleccionado}?"
        )

        if confirmar:
            resultado = self.db.eliminar(self.codigo_seleccionado)
            if resultado:
                messagebox.showinfo("Éxito", "Producto eliminado correctamente.")
                self.limpiar()
                self.refrescar_tabla()
            else:
                messagebox.showerror("Error", "No se pudo eliminar el producto.")

    def limpiar(self):
        self.entry_codigo.delete(0, tk.END)
        self.entry_nombre.delete(0, tk.END)
        self.entry_precio.delete(0, tk.END)
        self.entry_marca.delete(0, tk.END)
        self.entry_cantidad.delete(0, tk.END)
        self.entry_inventario.delete(0, tk.END)
        self.codigo_seleccionado = None
        self.entry_codigo.config(state="normal")

    def refrescar_tabla(self):
        for item in self.tabla.get_children():
            self.tabla.delete(item)

        productos = self.db.listar()
        for producto in productos:
            self.tabla.insert("", "end", values=producto)

    def seleccionar(self, event):
        seleccion = self.tabla.selection()
        if seleccion:
            valores = self.tabla.item(seleccion[0], "values")
            self.limpiar()
            self.entry_codigo.insert(0, valores[0])
            self.entry_codigo.config(state="disabled")
            self.entry_nombre.insert(0, valores[1])
            self.entry_precio.insert(0, valores[2])
            self.entry_marca.insert(0, valores[3])
            self.entry_cantidad.insert(0, valores[4])
            self.entry_inventario.insert(0, valores[6])
            self.codigo_seleccionado = int(valores[0])