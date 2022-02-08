package com.sdn.bd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.sdn.bd.local.BDLocal;
import com.sdn.bd.modelo.Destino;

import java.util.ArrayList;

public class TblDestino {
    private static final String SQLClearTable = "DELETE from Destino";
    private static final String SQLObtenerRegistro = "SELECT id,nombre FROM Destino WHERE id=?";

    public static void vaciarTabla(Context contexto) {
        BDLocal.abrir(contexto);
        BDLocal.BD_SQLITE.execSQL(SQLClearTable);
        BDLocal.cerrar();
    }

    public static boolean guardar(Context contexto, Destino objeto) {
        long resultado = -1;
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("id", objeto.getId());
        nuevoRegistro.put("nombre", objeto.getNombre());

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.insert("Destino", null, nuevoRegistro);
        BDLocal.cerrar();
        return resultado > 0;
    }

    //METODO CREADO DE MANERA INADECUADO PERO FUNCIONAL PARA IMPORTACION DE PAISES
    public static Destino obtenerRegistro(Context contexto, Integer iddestino) {
        Destino obj = new Destino();
        obj.setId(iddestino);

        String[] args = new String[]{"" + iddestino};
        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLObtenerRegistro, args);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    obj.setId(result.getInt(0));
                    obj.setNombre(result.getString(1));
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return obj;
    }

    public static ArrayList<Destino> convertStringToPais(Context pantalla, String paises) {
        ArrayList<Destino> destino = new ArrayList<Destino>();
        String[] Lista = paises.split(",");


        for (Integer it = 0; it < Lista.length; it++) {
            try {
                destino.add(TblDestino.obtenerRegistro(pantalla, Integer.parseInt(Lista[it])));
            } catch (NumberFormatException e) {
                System.out.println("Error al tranformar" + Lista[it]);
            }
        }
        return destino;
    }

    public static String convertPaisToString(Context pantalla, ArrayList<Destino> paises) {
        String Lista = "";

        for (Integer it = 0; it < paises.size(); it++)
            Lista += paises.get(it).getId() + ",";

        Lista = Lista.length() > 0 ? Lista.substring(0, Lista.length() - 1) : "";

        return Lista;
    }
}
