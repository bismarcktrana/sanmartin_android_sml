package com.sdn.slp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.sdn.bd.dao.TblParametro;
import com.sdn.bd.modelo.Lectura2;
import com.sdn.slp.controlador.Tbl_Parametro;
import com.sdn.slp.igu.R;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class Utils {

    public static String Encriptar(Context pantalla, String texto) {

        String secretKey = Tbl_Parametro.getClave(pantalla,"SUPPLIER"); //llave para encriptar datos
        String base64EncryptedString = "";

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            base64EncryptedString = new String(base64Bytes);

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }

    public static String Desencriptar(Context pantalla,String textoEncriptado)  {

        String secretKey = Tbl_Parametro.getClave(pantalla,"SUPPLIER");  //llave para desenciptar datos
        String base64EncryptedString = "";

        try {
            byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");

            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = decipher.doFinal(message);

            base64EncryptedString = new String(plainText, "UTF-8");

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }

    public static String C_DateToFormat(Date fecha, String format){
        String valor="";
        try {
            valor= fecha == null ? null : new SimpleDateFormat(format).format(new java.sql.Date(fecha.getTime()));
        }catch (Exception e){
            valor ="";
        }
        return  valor;
    }

    public static String createWorkDirectory(Context pantalla,String DirectoryName) {
        String PATH_DIRECTORYWORK= Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+DirectoryName+File.separator;

        try {
            if(!new File(PATH_DIRECTORYWORK).exists())
                createandnotifyToAndroid(pantalla,new File(PATH_DIRECTORYWORK));
            TblParametro.modificar(pantalla,"HOST_LOCATION",PATH_DIRECTORYWORK);//notifica al sistema la nueva ruta de almacenamiento
        }catch (Exception e){
            Toast.makeText(pantalla, "Direccion:["+ PATH_DIRECTORYWORK +"], Message:["+e.getMessage()+"]", Toast.LENGTH_LONG).show();
        }
        return PATH_DIRECTORYWORK;
    }

    /*public static String createWorkDirectory(Context pantalla,String DirectoryName) {
        File PATH_DIRECTORYWORK = new File(Environment.getExternalStorageDirectory(), DirectoryName);
        if (!PATH_DIRECTORYWORK.exists()) {
            PATH_DIRECTORYWORK.mkdirs();
            notifyToAndroidCreation(pantalla,PATH_DIRECTORYWORK);
        }
        return PATH_DIRECTORYWORK.getAbsolutePath();
    }*/

    public static String getWorkDirectory(Context pantalla) {
        String PATH_DIRECTORYWORK= TblParametro.getClave(pantalla,"HOST_LOCATION");

        try {
            if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
                if(new File(PATH_DIRECTORYWORK).exists())
                    return  PATH_DIRECTORYWORK;
            }
        }catch (Exception e){
            Toast.makeText(pantalla, "Direccion:["+ PATH_DIRECTORYWORK +"], Message:["+e.getMessage()+"]", Toast.LENGTH_LONG).show();
        }
        return PATH_DIRECTORYWORK;
    }

    private static void createandnotifyToAndroid(Context referencia, File carpeta) {
        if(!carpeta.exists()){
            carpeta.mkdirs();
            carpeta.setReadable(true);
            carpeta.setWritable(true);
            MediaScannerConnection.scanFile(referencia,new String[]{carpeta.getAbsolutePath().toString()},null,null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(carpeta);
                mediaScanIntent.setData(contentUri);
                referencia.sendBroadcast(mediaScanIntent);
            } else {
                referencia.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(carpeta.getAbsolutePath())));
            }
        }
    }

    public static void sendSamba(Context referencia){
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                "",
                "smb",
                "2212"
        );
        String DirectorioRemoto = "smb:"+TblParametro.getClave(referencia, "SERVER_SMB"); //Utils.isUserValidPath(referencia, "SERVER_SMB") ? TblParametro.getClave(referencia, "SERVER_SMB") : Environment.getExternalStorageDirectory().getAbsolutePath();


        try {
            if (new SmbFile(DirectorioRemoto,auth).exists()) {
                System.out.println("Existe el archivo remoto");
            }
        } catch (SmbException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

      /*  try {
            SmbFile source = new SmbFile("smb://smb:2212@a.b.c.d/sandbox/sambatosdcard.txt");

            File destination =
                    new File(Environment.DIRECTORY_DOWNLOADS, "SambaCopy.txt");

            InputStream in = source.getInputStream();
            OutputStream out = new FileOutputStream(destination);

            // Copy the bits from Instream to Outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Maybe in.close();
            out.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }


    public static Bitmap cargarBitImageFromWorkDirectory(Context pantalla, String NombreImagen) {
        Bitmap image = null;

        File archivo = new File(Utils.getWorkDirectory(pantalla)+NombreImagen);
        if(archivo.exists()){
            image = BitmapFactory.decodeFile(Utils.getWorkDirectory(pantalla)+NombreImagen);
            //System.out.println("Si existe.");
        }
        return image;
    }

    public static java.util.Date C_StringToDate(String cadena, Calendar c) {
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

    public static Double convertToLbs(Object obj) {
        String valor = obj.toString();//160831

        try {
            return Double.parseDouble(valor.substring(0, 2) + "." + valor.substring(2, 4));
        } catch (Exception e) {
            System.out.println("Error en convertToWeight:" + obj.toString() + " Info:" + e.getMessage());
            return null;
        }
    }

    public static Double convertToKgs(Object obj) {
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

    public static boolean isValidDirectory(Context context, File RutaDeCarpeta) {
        if(!RutaDeCarpeta.exists() ){
            if(RutaDeCarpeta.isDirectory()){
                RutaDeCarpeta.mkdirs();
                notifyToAndroidCreation(context, RutaDeCarpeta);
                File root = new File(RutaDeCarpeta.getAbsolutePath(), "prueba-crea-destruye");
                if (!root.exists()) {
                    root.mkdirs();
                    if (root.exists()) {
                        root.delete();
                        return true;
                    }
                }
            }
        }else{
            if(RutaDeCarpeta.isDirectory()){
                return true;
            }
        }
        return false;
    }

    public static boolean isValidDirectory(Context context, String variable) {
        File DirectorioLocal = new File(TblParametro.getClave(context, variable));

        if (DirectorioLocal.isDirectory()) {
            File root = new File(DirectorioLocal, "prueba-crea-destruye");
            if (!root.exists()) {
                root.mkdirs();
                notifyToAndroidCreation(context, root);
                if (root.exists()) {
                    root.delete();
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean esCodidoValido(String codigo) {
        boolean respuesta = true;
        try{
            codigo = codigo.replace(" ", "");//REEMPLAZAR CUALQUIER ESPACIO.
            respuesta &= Utils.isNumber(codigo.substring(0, 11));     //NO PRODUCTO
            respuesta &= Utils.isDate(codigo.substring(18, 24));     //FECHA EMPAQUE
            respuesta &= Utils.isNumber(codigo.substring(28, 34));   //PESO
            respuesta &= ( Integer.parseInt(codigo.substring(24, 26))==31 || Integer.parseInt(codigo.substring(24, 26))==32)?true:false; //VALIDAR QUE EL PESO SEA LIBRAS O KILOS
            respuesta &= Utils.isNumber(codigo.substring(26, 28));   //CANTIDAD DE POSICION DECIMALS
            respuesta &= Utils.isNumber(codigo.substring(36, 44));   //LOTE
            respuesta &= Utils.isNumber(codigo.substring(46, 52));   //SERIE
            respuesta &= Utils.isNumber(codigo.substring(52, 54));  //CANTIDAD DE PIEZAS
        }catch (Exception e){
            System.out.println("Error en el metodo esCodigoValido: "+e.getMessage());
            respuesta = false;
        }
        return respuesta;
    }


    public static Lectura2 DividirCodigo(String codigo) {
        boolean respuesta =false;
        Lectura2 Lectura =new  Lectura2();
        if (esCodidoValido(codigo)) {//DEJO PRECARGADOS LOS DATOS PARA LAS SIGUIENTES CONSULTAS
            Lectura = new Lectura2();
            Lectura.getProducto().setCodigo(codigo.substring(10, 15));
            Lectura.setBarra(codigo);
            Lectura.setFecha_empaque(Utils.convertFromDateShortToDate(codigo.substring(18, 24)));
            Lectura.setFecha_proceso(new Date());
            Lectura.setLote(codigo.substring(36, 44));
            Lectura.setSerie(codigo.substring(46, 52));
            Lectura.setCant_pieza(Integer.parseInt( codigo.substring(52, 54)));

            Integer posicion = Integer.parseInt(codigo.substring(26, 28));              //POSICION DONDE SE INSERTARA EL PUNTO DEL PESO EJEMPLO POS=1    236.5
            StringBuilder stringBuilder= new StringBuilder(codigo.substring(28, 34));   //OBTENER SUBCADENA DE PESO PARA PROCESAR
            stringBuilder.insert(6-posicion,".");                             //AGREGANDO EL PUNTO A LA CADENA
            Double PESO =  Double.parseDouble(stringBuilder.toString());

            switch (Integer.parseInt(codigo.substring(24, 26))){
                case 31: //KILOS
                    Lectura.setPeso_libra(PESO*2.20462);
                    Lectura.setPeso_kilogramo(PESO);
                    Lectura.setUm(1);
                    break;
                case 32: //LIBRAS
                    Lectura.setPeso_libra(PESO);
                    Lectura.setPeso_kilogramo(PESO/2.20462);
                    Lectura.setUm(0);
                    break;
            }
            Lectura.set_idoperador(ConfApp.OPERADORLOGEADO.getId());
            Lectura.setEnviado(0);
            respuesta = true;
        } else {
            Lectura = new Lectura2();
        }
        return  Lectura;
    }

    public static Integer getTipoSonido(Context pantalla, String voz) {
        Integer valor;
        try {
            valor = Integer.parseInt(TblParametro.getClave(pantalla, voz));
            if (valor == 1 || valor == 2 || valor == 3)
                return valor;
            else
                return voz.equals("estibador") ? 3 : 1;
        } catch (Exception e) {
            return voz.equals("estibador") ? 3 : 1;
        }
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

    public static void setTabColor(TabHost tabhost, Context pantalla) {
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.colorWhite); // unselected
            TextView tv = tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(ContextCompat.getColor(pantalla, R.color.colorPrimaryDark));
        }

        tabhost.getTabWidget().setCurrentTab(0);
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundResource(R.color.zebra_blue); // selected
        TextView tv = tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).findViewById(android.R.id.title); //Unselected Tabs
        tv.setTextColor(ContextCompat.getColor(pantalla, R.color.colorWhite));
    }

    public static String convertArrayToCSV(Object[] array,String caracter) {
        String result = "";

        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();

            for (Object s : array) {
                sb.append(s.toString()).append(caracter);
            }

            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }

    public static void notifyToAndroidCreation(Context referencia, File archivo) {
        if(archivo.exists()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(archivo);
                mediaScanIntent.setData(contentUri);
                referencia.sendBroadcast(mediaScanIntent);
            } else {
                referencia.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(archivo.getAbsolutePath())));
            }
        }
    }
}
