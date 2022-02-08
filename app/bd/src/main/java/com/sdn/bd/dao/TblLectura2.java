package com.sdn.bd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.sdn.bd.local.BDLocal;
import com.sdn.bd.modelo.Lectura2;
import com.sdn.bd.utils.Utils;

import java.util.ArrayList;

public class TblLectura2 {
    private static final String SQLNuevoid = "Select (ifnull(max(id),0))+1 AS ID  from lectura2";
    private static final String SQLClearTable = "DELETE from lectura2 WHERE id>0";
    private static final String SQLClearSended = "DELETE from lectura2 WHERE _enviado>0";
    private static final String SQLObtenerRegistros = "SELECT id,idproducto,fecha_hora,peso,barra,_idoperador,_enviado FROM lectura2 WHERE idoperador=?";

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
        BDLocal.abrir(contexto);
        BDLocal.BD_SQLITE.execSQL(SQLClearTable);
        BDLocal.cerrar();
    }

    public static void borrarEnviados(Context contexto) {//int id, String usuario, String clave, String nombre, int tipo
        BDLocal.abrir(contexto);
        BDLocal.BD_SQLITE.execSQL(SQLClearSended);
        BDLocal.cerrar();
    }

    public static boolean guardar(Context contexto, Lectura2 objeto) {
        boolean resultado = false;
        ContentValues nuevoRegistro = new ContentValues();
        objeto.setId(objeto.getId() < 1 ? nuevoID(contexto) : objeto.getId());

        nuevoRegistro.put("id", objeto.getId());
        nuevoRegistro.put("idproducto", objeto.getProducto().getCodigo());
        nuevoRegistro.put("barra", objeto.getBarra());
        nuevoRegistro.put("fecha_empaque", Utils.C_DateToDBFORMAT(objeto.getFecha_empaque()));
        nuevoRegistro.put("fecha_proceso", Utils.C_DateToDBFORMAT(objeto.getFecha_proceso()));
        nuevoRegistro.put("peso_libra",String.format("%.2f", objeto.getPeso_libra()) );
        nuevoRegistro.put("peso_kilogramo",String.format("%.2f", objeto.getPeso_kilogramo()) );
        nuevoRegistro.put("lote", objeto.getLote());
        nuevoRegistro.put("serie", objeto.getSerie());
        nuevoRegistro.put("cant_pieza", objeto.getCant_pieza());
        nuevoRegistro.put("um", objeto.getUm());
        nuevoRegistro.put("_idoperador",objeto.get_idoperador());
        nuevoRegistro.put("_enviado",objeto.getEnviado());


        try {
            BDLocal.abrir(contexto);
            BDLocal.BD_SQLITE.insert("Lectura2",null,nuevoRegistro);
            BDLocal.cerrar();
            resultado = true;
        } catch (Exception e) {
            resultado = false;
        }

        return resultado;
    }

    public static boolean modificar(Context contexto, Integer id) {
        long resultado = -1;
        String[] args = new String[]{"" + id};

        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("_enviado", 1);

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.update("Lectura2", nuevoRegistro, "id=?", args);
        BDLocal.cerrar();

        return resultado > 0;
    }

    public static ArrayList<Lectura2> obtenerRegistros(Context contexto, int idtipooperador) {
        String SQLTEMP = "SELECT id,idproducto,barra,fecha_empaque,fecha_proceso,peso_libra,peso_kilogramo,lote,serie,cant_pieza,um,_idoperador,_enviado FROM lectura2";
        ArrayList<Lectura2> registros = new ArrayList<Lectura2>();
        String[] args = null;

        if (idtipooperador != 0) {
            SQLTEMP += " WHERE idoperador=?";
            args = new String[]{"" + idtipooperador};
        }

        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLObtenerRegistros, args);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    Lectura2 obj = new Lectura2();
                    obj.setId(Integer.parseInt(result.getString(0)));
                    obj.getProducto().setCodigo(result.getString(1));
                    obj.setBarra(result.getString(2));
                    obj.setFecha_empaque(Utils.C_StringToObjetTimeStamp(result.getString(3)));
                    obj.setFecha_proceso(Utils.C_StringToObjetTimeStamp(result.getString(4)));
                    obj.setPeso_libra(result.getDouble(5));
                    obj.setPeso_kilogramo(result.getDouble(6));
                    obj.setLote(result.getString(7));
                    obj.setSerie(result.getString(8));
                    obj.setCant_pieza(result.getInt(9));
                    obj.setUm(result.getInt(10));
                    obj.set_idoperador(result.getInt(11));
                    obj.setEnviado(result.getInt(12));
                    registros.add(obj);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();

        return registros;
    }

    public static Boolean estaRegistrado(Context contexto, String barra) {
        String[] args = new String[]{"" + barra};
        Boolean resultado = false;

        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery("SELECT id FROM Lectura2 WHERE barra =?", args);

        if (result.getCount() != 0) {
            resultado = true;
        }
        result.close();
        BDLocal.cerrar();
        return resultado;
    }

    public static boolean borrarCaja(Context contexto, String codigo) {
        long resultado = -1;
        String[] args = new String[]{codigo};

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.delete("Lectura2", "barra=?", args);
        BDLocal.cerrar();

        return resultado > 0;
    }

    public static boolean borrarTodo(Context contexto, int idoperador) {
        long resultado = -1;
        String[] args = new String[]{"" + idoperador};

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.delete("Lectura2", "_idoperador=?", args);
        BDLocal.cerrar();

        return resultado > 0;
    }

}
