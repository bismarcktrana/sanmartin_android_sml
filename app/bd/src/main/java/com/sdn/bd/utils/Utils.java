package com.sdn.bd.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.sdn.bd.dao.TblParametro;

import java.io.File;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    public static String C_DateToFormat(Date fecha, String format){
        String valor="";
        try {
            valor= fecha == null ? null : new SimpleDateFormat(format).format(new java.sql.Date(fecha.getTime()));
        }catch (Exception e){
            valor ="";
        }
        return  valor;
    }

    public static Date C_StringToDate(String cadena, Calendar c) {
        cadena = cadena.replace(" ", "");
        if (cadena.length() == 10 && !cadena.equals("01/01/0001")) {
            try {
                c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(cadena.substring(0, 2)));
                c.set(Calendar.MONTH, Integer.parseInt(cadena.substring(3, 5)) - 1);
                c.set(Calendar.YEAR, Integer.parseInt(cadena.substring(6, 10)));
                return c.getTime();
            } catch (Exception e) {
                System.out.println("Error en C_StringToDate:" + cadena + " Info:" + e.getMessage());
                return null;
            }
        }
        return null;
    }

    public static boolean isNumber(Object obj) {
        String valor = obj.toString();
        System.out.println("Finalizar:" + valor);
        try {
            Integer.parseInt(valor);
            return true;
        } catch (Exception e) {
            System.out.println("Error en isNumber:" + obj.toString() + " Info:" + e.getMessage());
            return false;
        }
    }

    public static boolean isDate(Object obj) {
        return convertFromDateShortToDate(obj) != null;
    }

    public static Double convertToWeight(Object obj) {
        String valor = obj.toString();//160831

        try {
            return Double.parseDouble(valor.substring(0, 2) + "." + valor.substring(2, 4));
        } catch (Exception e) {
            System.out.println("Error en convertToWeight:" + obj.toString() + " Info:" + e.getMessage());
            return null;
        }
    }

    /**
     * Convierte una fecha en formato yyMMDD a un objeto Date
     *
     * @param obj objeto fecha
     * @return el objeto Date o Null
     */
    public static Date convertFromDateShortToDate(Object obj) {
        String valor = obj.toString();//160831
        String fecha = valor.substring(4, 6) + "/" + valor.substring(2, 4) + "/" + "20" + valor.substring(0, 2);
        try {
            return C_StringToDate(fecha, Calendar.getInstance());
        } catch (Exception e) {
            System.out.println("Error Fecha: [" + obj.toString() + "]" + e.getMessage());
            return null;
        }
    }

    /**
     * ESTE METODO CONVIERTE UNA FECHA DE LA LIBRERIA java.util.Date a un formato a un formato permitido por sqlite y devuelto en forma de cadena
     *
     * @param fecha
     * @return
     */
    public static String C_DateToDBFORMAT(Date fecha) {
        ConfApp.ISO8601_FORMATTER = new SimpleDateFormat(ConfApp.ISO8601_FORMATDATE_BD);
        return fecha == null ? null : ConfApp.ISO8601_FORMATTER.format(new java.sql.Date(fecha.getTime()));
    }

    /**
     * ESTE METODO CONVIERTE UNA FECHA DE LA LIBRERIA java.util.Date a un formato a un formato permitido por sqlite y devuelto en forma de cadena
     *
     * @param fecha
     * @return
     */
    public static String C_TimeStampToDBFORMAT(Date fecha) {
        ConfApp.ISO8601_FORMATTER = new SimpleDateFormat(ConfApp.ISO8601_FORMATTIMESTAMP_BD);
        return fecha == null ? null : ConfApp.ISO8601_FORMATTER.format(new java.sql.Date(fecha.getTime()));
    }

    /**
     * ESTE METODO CONVIERTE UNA FECHA DE LA LIBRERIA java.util.Date a un formato a un formato permitido por sqlite y devuelto en forma de cadena
     *
     * @param fecha
     * @return
     */
    /*public static String C_TimeStampToDBWCFFORMAT(Date fecha) {
        ConfApp.ISO8601_FORMATTER = new SimpleDateFormat(ConfApp.ISO8601_FORMATTIMESTAMP_BDWCF);
        return fecha == null ? null : ConfApp.ISO8601_FORMATTER.format(new java.sql.Date(fecha.getTime()));
    }*/

    public static String C_TimeStampToCustom(Date fecha, String format) {
        ConfApp.ISO8601_FORMATTER = new SimpleDateFormat(format);
        return fecha == null ? null : ConfApp.ISO8601_FORMATTER.format(new java.sql.Date(fecha.getTime()));
    }

    /**
     * FUNCION QUE CONVIERTE UNA CADENA yyyy-MM-dd a Date
     *
     * @param cadena
     * @return
     */
    public static Date C_StringToObjetTimeStamp(String cadena) {
        ConfApp.ISO8601_FORMATTER = new SimpleDateFormat(ConfApp.ISO8601_FORMATTIMESTAMP_BD);
        try {
            return cadena == null ? null : ConfApp.ISO8601_FORMATTER.parse(cadena);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * FUNCION QUE CONVIERTE UNA CADENA yyyy-MM-dd a Date
     *
     * @param
     * @return
     */
    public static Date C_StringToObjetDate(String cadena) {
        ConfApp.ISO8601_FORMATTER = new SimpleDateFormat(ConfApp.ISO8601_FORMATDATE_BD);
        if (cadena != null) {
            if (cadena.length() == 10) {
                try {
                    return ConfApp.ISO8601_FORMATTER.parse(cadena);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String C_DateToAppFormat(Date fecha) {
        ConfApp.ISO8601_FORMATTER = new SimpleDateFormat(ConfApp.ISO8601_FORMATDATE_APP);
        return fecha == null ? null : ConfApp.ISO8601_FORMATTER.format(fecha);
    }

    public static String ConvertToNameDate(Date fecha) {
        String MES;
        String valor;
        String dt;
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);

        try {
            int D = calendario.get(Calendar.DAY_OF_MONTH);//Enero =0,.....,Diciembre
            int M = calendario.get(Calendar.MONTH);//lote
            int A = calendario.get(Calendar.YEAR);//consecutivo
            int H = calendario.get(Calendar.HOUR_OF_DAY);//consecutivo
            int MN = calendario.get(Calendar.MINUTE);//consecutivo
            int S = calendario.get(Calendar.SECOND);//consecutivo

            switch (M) {
                case 0:
                    MES = "ENERO";
                    break;
                case 1:
                    MES = "FEBRERO";
                    break;
                case 2:
                    MES = "MARZO";
                    break;
                case 3:
                    MES = "ABRIL";
                    break;
                case 4:
                    MES = "MAYO";
                    break;
                case 5:
                    MES = "JUNIO";
                    break;
                case 6:
                    MES = "JULIO";
                    break;
                case 7:
                    MES = "AGOSTO";
                    break;
                case 8:
                    MES = "SEPTIEMBRE";
                    break;
                case 9:
                    MES = "OCTUBRE";
                    break;
                case 10:
                    MES = "NOVIEMBRE";
                    break;
                case 11:
                    MES = "DICIEMBRE";
                    break;
                default:
                    MES = "INVALIDO";
                    break;
            }
            valor = (H > 12) ? "PM" : "AM";
            H = (H > 12) ? H - 12 : H;
            dt = String.format("%02d", D) + MES + A + " " + String.format("%02d", H) + String.format("%02d", MN) + String.format("%02d", S) + valor;

        } catch (NumberFormatException excepcion) {
            return "";
        }
        return dt;
    }

    public static float C_FloatToCustomFloat(Float number, int decimalPlace) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
