package com.sdn.bd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.sdn.bd.local.BDLocal;
import com.sdn.bd.modelo.Operador;

import java.util.Vector;

public class TblOperador {
    private static final String SQLNuevoid = "Select (ifnull(max(id),0))+1 AS ID  from operador";
    private static final String SQLClearTable = "DELETE from operador WHERE id>0";
    private static final String SQLObtenerTodosRegistros = "SELECT id,usuario,clave,nombre,tipo FROM operador";
    private static final String SQLObtenerTodosMenosAdminsitrador = "SELECT id,usuario,clave,nombre,tipo FROM operador";// WHERE tipo!=1
    private static final String SQLObtenerRegistros = "SELECT id,usuario,clave,nombre,tipo FROM operador WHERE tipo=?";
    private static final String SQLLogin = "SELECT id,usuario,clave,nombre,tipo  FROM operador  WHERE usuario=? AND clave=?";
    private static final String SQLObtenerRegistro = "SELECT id,usuario,clave,nombre,tipo  FROM operador  WHERE usuario=?";
    private static final String SQLObtenerRegistroXID = "SELECT id,usuario,clave,nombre,tipo  FROM operador  WHERE id=?";

    public static int nuevoID(Context contexto) {
        int idgenerado = 0;

        BDLocal.abrir(contexto);
        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLNuevoid, null);
        if (result.moveToFirst()) {
            do {
                idgenerado = Integer.parseInt(result.getString(0));
            } while (result.moveToNext());
        }
        result.close();
        BDLocal.cerrar();
        return idgenerado;
    }

    public static void vaciarTabla(Context contexto) {//int id, String usuario, String clave, String nombre, int tipo
        //Operador operador = new Operador(1,"dts","1","Administrador",1);

        BDLocal.abrir(contexto);
        BDLocal.BD_SQLITE.execSQL(SQLClearTable);
        BDLocal.cerrar();
        // TblOperador.guardar(contexto, operador);
    }

    public static boolean guardar(Context contexto, Operador objeto) {
        long resultado = -1;
        ContentValues nuevoRegistro = new ContentValues();
        objeto.setId(objeto.getId() < 1 ? nuevoID(contexto) : objeto.getId());

        nuevoRegistro.put("id", objeto.getId());
        nuevoRegistro.put("usuario", objeto.getUsuario());
        nuevoRegistro.put("clave", objeto.getClave());
        nuevoRegistro.put("nombre", objeto.getNombre());
        nuevoRegistro.put("tipo", objeto.getTipo());

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.insert("Operador", null, nuevoRegistro);
        BDLocal.cerrar();

        return resultado > 0;
    }

    public static Operador buscarUsuario(Context contexto, String usuario, String clave) {
        Operador operador = new Operador();

        String[] args = new String[]{usuario, clave};
        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLLogin, args);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    operador.setId(Integer.parseInt(result.getString(0)));
                    operador.setUsuario(result.getString(1));
                    operador.setClave(result.getString(2));
                    operador.setNombre(result.getString(3));
                    operador.setTipo(Integer.parseInt(result.getString(4)));
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return operador;
    }

    public static Vector<Operador> obtenerRegistros(Context contexto, int idtipooperador) {
        Vector<Operador> operadores = new Vector<Operador>();

        String[] args = new String[]{"" + idtipooperador};
        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLObtenerRegistros, args);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    Operador obj = new Operador();
                    obj.setId(Integer.parseInt(result.getString(0)));
                    obj.setUsuario(result.getString(1));
                    obj.setClave(result.getString(2));
                    obj.setNombre(result.getString(3));
                    obj.setTipo(Integer.parseInt(result.getString(4)));
                    operadores.add(obj);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return operadores;
    }

    public static Vector<Operador> obtenerRegistros(Context contexto) {
        Vector<Operador> operadores = new Vector<Operador>();

        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLObtenerTodosRegistros, null);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    Operador obj = new Operador();
                    obj.setId(Integer.parseInt(result.getString(0)));
                    obj.setUsuario(result.getString(1));
                    obj.setClave(result.getString(2));
                    obj.setNombre(result.getString(3));
                    obj.setTipo(Integer.parseInt(result.getString(4)));
                    operadores.add(obj);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return operadores;
    }

    public static Vector<Operador> obtenerRegistrosSistema(Context contexto) {
        Vector<Operador> operadores = new Vector<Operador>();

        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLObtenerTodosMenosAdminsitrador, null);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    Operador obj = new Operador();
                    obj.setId(Integer.parseInt(result.getString(0)));
                    obj.setUsuario(result.getString(1));
                    obj.setClave(result.getString(2));
                    obj.setNombre(result.getString(3));
                    obj.setTipo(Integer.parseInt(result.getString(4)));
                    operadores.add(obj);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return operadores;
    }

    public static Operador obtenerRegistro(Context contexto, String usuario) {
        Operador operador = new Operador();

        String[] args = new String[]{usuario};
        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLObtenerRegistro, args);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    operador.setId(Integer.parseInt(result.getString(0)));
                    operador.setUsuario(result.getString(1));
                    operador.setClave(result.getString(2));
                    operador.setNombre(result.getString(3));
                    operador.setTipo(Integer.parseInt(result.getString(4)));

                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return operador;
    }

    public static Operador obtenerRegistroXID(Context contexto, int idusuario) {
        Operador operador = new Operador();

        String[] args = new String[]{"" + idusuario};
        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLObtenerRegistroXID, args);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    operador.setId(Integer.parseInt(result.getString(0)));
                    operador.setUsuario(result.getString(1));
                    operador.setClave(result.getString(2));
                    operador.setNombre(result.getString(3));
                    operador.setTipo(Integer.parseInt(result.getString(4)));

                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return operador;
    }

    public static boolean modificar(Context contexto, Operador operador) {
        long resultado = -1;
        String[] args = new String[]{"" + operador.getId()};

        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("usuario", operador.getUsuario());
        nuevoRegistro.put("clave", operador.getClave());
        nuevoRegistro.put("nombre", operador.getNombre());
        nuevoRegistro.put("tipo", operador.getTipo());

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.update("Operador", nuevoRegistro, "id=?", args);
        BDLocal.cerrar();

        return resultado > 0;
    }

}
