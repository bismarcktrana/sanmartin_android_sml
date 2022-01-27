package com.sdn.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.sdn.slp.controlador.Tbl_Parametro;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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

    public static String getWorkDirectory(Context pantalla) {
       String PATH_DIRECTORYWORK= Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"sml"+File.separator;
        try {
            if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
                createandnotifyToAndroid(pantalla,new File(PATH_DIRECTORYWORK));
            }
        }catch (Exception e){
            Toast.makeText(pantalla, "Direccion:["+ PATH_DIRECTORYWORK +"], Message:["+e.getMessage()+"]", Toast.LENGTH_LONG).show();
        }
        return PATH_DIRECTORYWORK;
    }

    private static void createandnotifyToAndroid(Context referencia, File carpeta) {
        if(!carpeta.exists()){
            carpeta.mkdirs();

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

    public static Bitmap cargarBitImageFromWorkDirectory(Context pantalla, String NombreImagen) {
        Bitmap image = null;

        File archivo = new File(Utils.getWorkDirectory(pantalla)+NombreImagen);
        if(archivo.exists()){
            image = BitmapFactory.decodeFile(Utils.getWorkDirectory(pantalla)+NombreImagen);
            //System.out.println("Si existe.");
        }
        return image;
    }

}
