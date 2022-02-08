package com.sdn.bd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.sdn.bd.local.BDLocal;
import com.sdn.bd.modelo.BorrarLectura;

import java.util.ArrayList;

public class TblBorrarLectura {

    public static void vaciarTabla(Context contexto) {//int id, String usuario, String clave, String nombre, int tipo
        BDLocal.abrir(contexto);
        BDLocal.BD_SQLITE.execSQL("DELETE from BorraLectura");
        BDLocal.cerrar();
    }

    public static boolean guardar(Context contexto, BorrarLectura objeto) {
        long resultado = -1;
        ContentValues nuevoRegistro = new ContentValues();

        nuevoRegistro.put("idservidor", objeto.getIdservidor());
        nuevoRegistro.put("barra", objeto.getBarra());
        nuevoRegistro.put("idorden", objeto.getIdorden());

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.insert("BorrarLectura", null, nuevoRegistro);
        BDLocal.cerrar();

        return resultado > 0;
    }

    public static boolean borrarXbarra(Context contexto, String barra) {
        String[] args = new String[]{barra};
        long resultado = -1;

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.delete("BorrarLectura", "barra=?", args);
        BDLocal.cerrar();

        return resultado > 0;
    }

    public static boolean borrarXidservidor(Context contexto, int idservidor) {
        String[] args = new String[]{""+idservidor};
        long resultado = -1;

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.delete("BorrarLectura", "idservidor=?", args);
        BDLocal.cerrar();

        return resultado > 0;
    }

    public static boolean borrarXidOrden(Context contexto, int idorden) {
        String[] args = new String[]{""+idorden};
        long resultado = -1;

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.delete("BorrarLectura", "idorden=?", args);
        BDLocal.cerrar();

        return resultado > 0;
    }

    public static Boolean existenDatos(Context contexto) {
        Boolean resultado = false;

        BDLocal.abrir(contexto);
        Cursor result = BDLocal.BD_SQLITE.rawQuery("SELECT * FROM BorrarLectura", null);
        if (result.getCount() != 0) {
            resultado = true;
        }
        result.close();
        BDLocal.cerrar();
        return resultado;
    }

    public static Boolean existenDatosDeLaOrden(Context contexto, int idorden) {
        String[] args = new String[]{""+idorden};
        Boolean resultado = false;

        BDLocal.abrir(contexto);
        Cursor result = BDLocal.BD_SQLITE.rawQuery("SELECT * FROM BorrarLectura WHERE idorden=?", args);
        if (result.getCount() != 0) {
            resultado = true;
        }
        result.close();
        BDLocal.cerrar();
        return resultado;
    }

    public static ArrayList<BorrarLectura> obtenerRegistros(Context contexto) {
        String SQLTEMP = "SELECT idservidor,barra,idorden FROM Borrarlectura";
        ArrayList<BorrarLectura> registros = new ArrayList<BorrarLectura>();

        BDLocal.abrir(contexto);
        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLTEMP, null);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    BorrarLectura obj = new BorrarLectura();
                    obj.setIdservidor(Integer.parseInt(result.getString(0)));
                    obj.setBarra(result.getString(1));
                    obj.setIdorden(Integer.parseInt(result.getString(2)));
                    registros.add(obj);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();

        return registros;
    }

    public static ArrayList<BorrarLectura> obtenerRegistrosXOrden(Context contexto,int idorden) {
        String SQLTEMP = "SELECT idservidor,barra,idorden FROM Borrarlectura WHERE idorden=?";
        ArrayList<BorrarLectura> registros = new ArrayList<BorrarLectura>();
        String[] args = new String[]{""+idorden};

        BDLocal.abrir(contexto);
        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLTEMP, args);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    BorrarLectura obj = new BorrarLectura();
                    obj.setIdservidor(Integer.parseInt(result.getString(0)));
                    obj.setBarra(result.getString(1));
                    obj.setIdorden(Integer.parseInt(result.getString(2)));
                    registros.add(obj);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();

        return registros;
    }

    public static ArrayList<ArrayList<Object>> convertArray1DTo2D(ArrayList<BorrarLectura> arreglo1D,Integer columna) {
        ArrayList<ArrayList<Object>> arreglo2D = new ArrayList<ArrayList<Object>>();
        int POSInicial, POSFinal, columnas, iteraciones;

        columnas =columna;

        iteraciones = arreglo1D.size() >= columnas ? ((int) (Math.ceil((double)arreglo1D.size() / (double)columnas))) : 1;

        for (int i = 1; i <= iteraciones; i++) {
            POSInicial = (i * columnas) - columnas;
            POSFinal = (i * columnas) > arreglo1D.size() ? arreglo1D.size() : (i * columnas);

            ArrayList<Object> NuewRegistro = new ArrayList<Object>();
            NuewRegistro.addAll(arreglo1D.subList(POSInicial, POSFinal));

            arreglo2D.add(NuewRegistro);
        }

        return arreglo2D;
    }

}
