package com.sdn.bd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.sdn.bd.modelo.Producto;
import com.sdn.bd.local.BDLocal;

import java.util.Vector;

public class TblProducto {
    private static final String SQLClearTable = "DELETE from Producto";
    private static final String SQLObtenerRegistros = "SELECT codigo, nombre,abreviatura FROM Producto";
    private static final String SQLObtenerRegistro = "SELECT codigo,nombre,abreviatura FROM Producto  WHERE codigo=?";

    public static void vaciarTabla(Context contexto) {
        BDLocal.abrir(contexto);
        BDLocal.BD_SQLITE.execSQL(SQLClearTable);
        BDLocal.cerrar();
    }

    public static boolean guardar(Context contexto, Producto objeto) {
        long resultado = -1;
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("codigo", objeto.getCodigo());
        nuevoRegistro.put("nombre", objeto.getNombre());
        nuevoRegistro.put("abreviatura", objeto.getAbreviatura());

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.insert("producto", null, nuevoRegistro);
        BDLocal.cerrar();

        return resultado > 0;
    }

    public static Vector<Producto> obtenerRegistros(Context contexto) {
        Vector<Producto> productos = new Vector<Producto>();

        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLObtenerRegistros, null);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    Producto obj = new Producto();
                    obj.setCodigo(result.getString(0));
                    obj.setNombre(result.getString(1));
                    obj.setAbreviatura(result.getString(2));
                    productos.add(obj);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return productos;
    }

    public static Producto obtenerRegistroxCodigo(Context contexto, String codigo) {
        Producto producto = new Producto();

        String[] args = new String[]{codigo};
        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLObtenerRegistro, args);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    producto.setCodigo(result.getString(0));
                    producto.setNombre(result.getString(1));
                    producto.setAbreviatura(result.getString(2));
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return producto;
    }
}
