import sqlite3
from models import Producto
from datetime import date


class Database:
    def __init__(self, db_name="db.sqlite3"):
        self.db_name = db_name
        self.crear_tabla()

    def conectar(self):
        return sqlite3.connect(self.db_name)

    def crear_tabla(self):
        conn = self.conectar()
        cursor = conn.cursor()
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS productos (
                codigo INTEGER PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                precio DECIMAL(10,2) NOT NULL,
                marca VARCHAR(100) NOT NULL,
                cantidad INTEGER NOT NULL,
                fecha DATE NOT NULL,
                inventario INTEGER NOT NULL
            )
        """)
        conn.commit()
        conn.close()

    def insertar(self, codigo, nombre, precio, marca, cantidad, inventario):
        conn = self.conectar()
        cursor = conn.cursor()
        try:
            cursor.execute(
                "INSERT INTO productos (codigo, nombre, precio, marca, cantidad, fecha, inventario) "
                "VALUES (?, ?, ?, ?, ?, ?, ?)",
                (codigo, nombre, precio, marca, cantidad, date.today().isoformat(), inventario)
            )
            conn.commit()
            return True
        except sqlite3.IntegrityError:
            return False
        finally:
            conn.close()

    def listar(self):
        conn = self.conectar()
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM productos")
        filas = cursor.fetchall()
        conn.close()
        return filas

    def actualizar(self, codigo, nombre, precio, marca, cantidad, inventario):
        conn = self.conectar()
        cursor = conn.cursor()
        cursor.execute(
            "UPDATE productos SET nombre=?, precio=?, marca=?, cantidad=?, inventario=? "
            "WHERE codigo=?",
            (nombre, precio, marca, cantidad, inventario, codigo)
        )
        conn.commit()
        filas_afectadas = cursor.rowcount
        conn.close()
        return filas_afectadas > 0

    def eliminar(self, codigo):
        conn = self.conectar()
        cursor = conn.cursor()
        cursor.execute("DELETE FROM productos WHERE codigo=?", (codigo,))
        conn.commit()
        filas_afectadas = cursor.rowcount
        conn.close()
        return filas_afectadas > 0