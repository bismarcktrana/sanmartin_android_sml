package com.sdn.slp.utils;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;

import com.sdn.bd.dao.TblParametro;
import com.sdn.slp.controlador.Tbl_Parametro;
import com.sdn.bd.modelo.Operador;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by bismarck on 15/8/2016.
 */
public class ConfApp {

    public static String SCREEN_NAME="";

    /**
     * VARIABLES GLOBAL DE SERVIDOR REMOTO SQL SERVER
     */
    public static String SERVER;
    public static String BDNAME_CATALOG;
    public static String BDNAME_LICENSE;
    public static String BDUSER;
    public static String BDPASS;

    /**
     * VARIABLES GLOBAL DEL SISTEMA
     */
    public static Boolean USER_DTS = false;
    public static Boolean USER_SUPERVISOR = false;
    public static Boolean USER_ESTIBADOR = false;

    /***
     * VARIBLES DEL APP
     */
  /*  public static Date CODEBAR_FECHA_EMPAQUE = null;
    public static Double CODEBAR_PESOLBS = 0.00;
    public static Double CODEBAR_PESOKGS = 0.00;
    public static String CODEBAR_IDPRODUCTO = "";
    public static String CODEBAR_LOTE = "";
    public static String CODEBAR_SERIE = "";
    public static Integer CODEBAR_CANTIDADPIEZAS =0;
    public static byte CODEBAR_UNIDADMEDIDA=-0; //0=lbs  , 1=kgs
*/
    //public static Lectura2 Lectura = new Lectura2();


    /***
     *VARIABLES GLOBAL DE AUTENTICACION
     */
    public static String UUID_FROM_DEVICE;
    public static String UUID_ENCRYPTED;
    public static String UUID_DESENCRYPTED;
    public static boolean DEVICEAUTORIZED = false;

    public static String SYSTEM_USER;
    public static String SYSTEM_PASS;

    public static String HOST_LOCATION;
    public static String SERVER_SMB;

    public static Operador OPERADORLOGEADO = new Operador();

    public static SimpleDateFormat ISO8601_FORMATTER;

    public static String ISO8601_DATE;

    public static final String ISO8601_DATE_FORMAT_1="dd/MM/yyyy";
    public static final String ISO8601_DATE_FORMAT_2="yyyy-MM-dd";

    public static String ISO8601_TIME;
    public static final String ISO8601_TIME_FORMAT_1="HH:MM:SS";
    public static final String ISO8601_TIME_FORMAT_2="hh:mm:ss aaa";

    public static String ISO8601_DATETIME;
    public static final String ISO8601_DATE_TIME_FORMAT_1="dd/MM/yyyy hh:mm:ss aaa";
    public static final String ISO8601_DATE_TIME_FORMAT_2="YYYY-MM-dd HH:MM:SS";
    public static final String ISO8601_FORMATTIMESTAMP_BD = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO8601_FORMATTIMESTAMP_BDWCF = "YYYY-MM-dd HH:MM:SS";

    public static final String ISO8601_FORMATDATE_APP = "dd/MM/yyyy";
    public static final String ISO8601_FORMATDATE_BD = "yyyy-MM-dd";

    public static final  DecimalFormat ISO8601_DECIMAL_FORMAT_1 = new DecimalFormat("#,###.00");

    public static boolean  DEBUG =false;

    public static void loadParameters(Context pantalla) {

        /*******************************CARGAR CONFIG SERVIDOR*****************************/
        ConfApp.SERVER = Utils.Desencriptar(pantalla, Tbl_Parametro.getClave(pantalla, "SERVER"));
        ConfApp.BDNAME_CATALOG = Utils.Desencriptar(pantalla,Tbl_Parametro.getClave(pantalla, "BDNAMECATALOG"));
        ConfApp.BDNAME_LICENSE = Utils.Desencriptar(pantalla,Tbl_Parametro.getClave(pantalla, "BDNAMELICENSE"));
        ConfApp.BDUSER =Utils.Desencriptar(pantalla, Tbl_Parametro.getClave(pantalla, "BDUSER"));
        ConfApp.BDPASS = Utils.Desencriptar(pantalla,Tbl_Parametro.getClave(pantalla, "BDPASS"));


        /*******************************CARGAR CONFIG LOCAL*****************************/

        ConfApp.SYSTEM_USER = Utils.Desencriptar(pantalla, Tbl_Parametro.getClave(pantalla, "SYSTEM_USER"));
        ConfApp.SYSTEM_PASS = Utils.Desencriptar(pantalla, Tbl_Parametro.getClave(pantalla, "SYSTEM_PASS"));

        ConfApp.UUID_FROM_DEVICE = Settings.Secure.getString(pantalla.getContentResolver(), Settings.Secure.ANDROID_ID);
        ConfApp.UUID_ENCRYPTED =Tbl_Parametro.getClave(pantalla, "UUID");
        ConfApp.UUID_DESENCRYPTED =Utils.Desencriptar(pantalla, UUID_ENCRYPTED);
        ConfApp.DEVICEAUTORIZED = ConfApp.DEBUG || ConfApp.UUID_FROM_DEVICE.trim().toString().equals(ConfApp.UUID_DESENCRYPTED.trim().toString());


        /*******************************CARGAR CONFIG SERVIDOR SAMBA*****************************/
        try{
            ConfApp.SERVER_SMB ="smb:";
            ConfApp.SERVER_SMB +=TblParametro.getClave(pantalla, "SERVER_SMB");
        }catch (Exception e){
            ConfApp.SERVER_SMB ="smb:";
        }

        try{
            ConfApp.HOST_LOCATION =TblParametro.getClave(pantalla, "HOST_LOCATION");
            ConfApp.HOST_LOCATION= Utils.isValidDirectory(pantalla, new File(ConfApp.HOST_LOCATION))?ConfApp.HOST_LOCATION:Environment.getExternalStorageDirectory().getAbsolutePath();
            System.out.println("Directory" +ConfApp.HOST_LOCATION);
        }catch (Exception e){
            ConfApp.HOST_LOCATION ="";
        }

    }

    public static String print() {
        return "ConfApp{"+
                "URL='" + ConfApp.SERVER + '\'' +
                ", BD=" + ConfApp.BDNAME_CATALOG +
                ", USER ='" + ConfApp.BDUSER + '\'' +
                ", PASS='" + ConfApp.BDPASS + '\'' +
                ", UUID_FROM_DEVICE ='" + ConfApp.UUID_FROM_DEVICE  + '\'' +
                ", UUID_CHECK_DESENCRYPTED='" + ConfApp.UUID_DESENCRYPTED + '\'' +
                ", DEVICEAUTORIZED ='" + ConfApp.DEVICEAUTORIZED  + '\'' +
               /* ", BD_NAME='" + ConfApp.BD_NAME + '\'' +
                ", PATH_BDSAMBOX='" + ConfApp.PATH_BDSAMBOX + '\'' +
                ", PATH_BDDESTINO='" + ConfApp.PATH_BDDESTINO + '\'' +*/
                "'}'";
    }
}
