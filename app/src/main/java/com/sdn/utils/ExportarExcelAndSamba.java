package com.sdn.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.sdn.bd.dao.TblParametro;
import com.sdn.bd.local.BDUtil;
import com.sdn.slp.igu.FrmEscaneoLibre;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExportarExcelAndSamba extends AsyncTask<String, String, String> {
    String[] toppings = new String[2];
    Context referencia;
    ProgressDialog progressDialog;
    String NombreArchivo;
    String SQLQUERYDETALLE="";
    String SQLQUERYCONSOLIDADO="";

    public ExportarExcelAndSamba(Context formulario, String NombreArchivo) {
        referencia = formulario;
        this.NombreArchivo = NombreArchivo;
        SQLQUERYDETALLE="SELECT L.idproducto,P.nombre,L.fecha_proceso,(CASE WHEN (L.um==0) THEN 'KILOGRAMO' ELSE 'LIBRAS' END) AS UNIDAD_MEDIDA,L.peso_libra AS  PESO_LIBRA,L.peso_kilogramo AS  PESO_KILOGRAMO,L.barra\n" +
                "FROM Lectura2 AS L LEFT JOIN Producto AS P ON L.idproducto=P.codigo\n" +
                "WHERE L._idoperador=" + ConfApp.OPERADORLOGEADO.getId();

        SQLQUERYCONSOLIDADO = "SELECT L.idproducto AS CODIGO,P.nombre,count(L.id) AS CAJAS,  SUM(L.peso_libra) AS  PESO_LIBRA,SUM(L.peso_kilogramo) AS  PESO_KILOGRAMO \n" +
                "FROM Lectura2 AS L LEFT JOIN Producto AS P ON L.idproducto=P.codigo\n" +
                "WHERE L._idoperador="+ ConfApp.OPERADORLOGEADO.getId() +" \n" +
                "GROUP BY L.idproducto";
    }
/*
    public boolean PrepararArchivo(HSSFWorkbook LibroExcel,String Consulta){
        return true;
    }
*/
    @Override
    protected String doInBackground(String... params) { //TblParametro.getClave(context,"ExcelFile")     ,TblParametro.getClave(getApplicationContext(),"ExcelFile")
        String sFileName = this.NombreArchivo;// NombreArchivo;//Utils.ConvertToNameDate(new Date());  //TblParametro.getClave(FrmExportar.this  ,"ExcelFile");
        //String sFileName2 = sFileName;
        sFileName += ".xls";
        File DirectorioLocalAutorizado = (Utils.isUserValidPath(referencia, "HostLocal") ? new File(TblParametro.getClave(referencia, "HostLocal")) : Environment.getExternalStorageDirectory());
        File gpxfile = new File(DirectorioLocalAutorizado, sFileName);

        publishProgress("Verificando ubicacion de archivo: " + gpxfile.getAbsolutePath(), "Localizando Archivo"); // Calls onProgressUpdate()

        if (!gpxfile.exists()) {
            try { // Create a workbook using the File System
                /*HSSFWorkbook myWorkBook = new HSSFWorkbook();
                crearHojaConsolidado(myWorkBook);
                crearHojaDetalle(myWorkBook);
                FileOutputStream fileOut = new FileOutputStream(gpxfile.getAbsolutePath());
                myWorkBook.write(fileOut);
                fileOut.flush();
                fileOut.close();
                myWorkBook.close();*/

                ExportarFicheroLocal(this.NombreArchivo);
               // Utils.notifyToAndroidCreation(referencia,gpxfile);
                ExportarFicheroRemoto(this.NombreArchivo + ".txt");
                //ExportarFicheroRemoto(sFileName2 + ".xls");
                //TblLectura2.vaciarTabla(referencia);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("No se encuentra" + gpxfile.getAbsolutePath());

            //Toast.makeText(getApplicationContext(),"No se encuentra"+gpxfile.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        }

        return "";
    }

    public void guardarHojaDeCalculo(String path) {

    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(referencia,
                "Operacion en Progreso",
                "Por favor  espere....");
    }

    @Override
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        progressDialog.dismiss();
        ((FrmEscaneoLibre)(referencia)).eliminarEnviados();
    }

    @Override
    protected void onProgressUpdate(String... params) {
        progressDialog.setMessage(params[0]);
        progressDialog.setTitle(params[1]);
    }

    /*
     *EXPORTAR HOJA DE DETALLE
     */
    private void crearHojaDetalle(HSSFWorkbook myWorkBook) {

        HSSFSheet HOJADETALLE = myWorkBook.createSheet("DETALLE");
        ArrayList<ArrayList> Registro =  BDUtil.executeQuery(referencia, SQLQUERYDETALLE);

        for (int i = 0; i < Registro.size(); i++) {
            HSSFRow row = HOJADETALLE.createRow(i);
            for (int it = 0; it < Registro.get(i).size(); it++) {
                HSSFCell cell = row.createCell(it);
                cell.setCellValue(Registro.get(i).get(it)==null?"":Registro.get(i).get(it).toString());
            }

            //TblLectura.modificar(getApplicationContext(),Registro.get(i).get(0).toString()); //Actualiza el registro guardado
            toppings[0] = "FILA # " + (i + 1);
            toppings[1] = "EXCEL HOJA: DETALLE";
            publishProgress(toppings);
        }

    }

    /*
     *EXPORTAR HOJA DE CONSOLIDADO
     */
    private void crearHojaConsolidado(HSSFWorkbook myWorkBook) {
        HSSFSheet HOJACONSOLIDADO = myWorkBook.createSheet("CONSOLIDADO");
        ArrayList<ArrayList> Registro = BDUtil.executeQuery(referencia, SQLQUERYCONSOLIDADO);

        for (int i = 0; i < Registro.size(); i++) {
            HSSFRow row = HOJACONSOLIDADO.createRow(i);
            for (int it = 0; it < Registro.get(i).size(); it++) {
                HSSFCell cell = row.createCell(it);
                cell.setCellValue(Registro.get(i).get(it)==null?"":Registro.get(i).get(it).toString());
            }
            toppings[0] = "FILA # " + (i + 1);
            toppings[1] = "EXCEL HOJA: CONSOLIDADO";
            publishProgress(toppings);
        }
    }

    /**
     * EXPORTAR A ARCHIVOS DE TEXTO LOCAL
     */
    public boolean ExportarFicheroLocal(String sFileNamex) {
        String sFileName = sFileNamex+".txt";
        File DirectorioLocalAutorizado = (Utils.isUserValidPath(referencia, "HOST_LOCATION") ? new File(TblParametro.getClave(referencia, "HOST_LOCATION")) : Environment.getExternalStorageDirectory());
        File gpxfile = new File(DirectorioLocalAutorizado, sFileName);

        toppings[0] = "Creado Archivo";
        toppings[1] = gpxfile.getAbsolutePath();
        publishProgress(toppings);

        try {
            if (gpxfile.exists())
                gpxfile.createNewFile();

            OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(gpxfile));

            ArrayList<ArrayList> codigos = BDUtil.executeQuery(referencia, SQLQUERYDETALLE);

            if (codigos.size() > 1) {
                for (int i = 1; i < codigos.size(); i++) {
                    fout.write(codigos.get(i).get(6).toString()+"\r\n");
                    toppings[0] = "Creado Archivo";
                    toppings[1] = gpxfile.getAbsolutePath() + " Registro" + (i);
                    publishProgress(toppings);
                }
            }
            fout.flush();
            fout.close();
            Utils.notifyToAndroidCreation(referencia,gpxfile);
            return true;
        } catch (IOException e) {
            Log.d("debug", e.getMessage());
        }
        return false;
    }

    /**
     * EXPORTAR A ARCHIVOS DE TEXTO A SERVIDOR SAMBA
     */
    public boolean ExportarFicheroRemoto(String sFileNamex) {
        //VM-DESARROLLO/Users/Public

      //  String DirectorioLocal = Utils.isUserValidPath(referencia, "HOST_LOCATION") ? TblParametro.getClave(referencia, "HOST_LOCATION") : Environment.getExternalStorageDirectory().getAbsolutePath();
        String DirectorioRemoto = "smb:"+TblParametro.getClave(referencia, "SERVER_SMB"); //Utils.isUserValidPath(referencia, "SERVER_SMB") ? TblParametro.getClave(referencia, "SERVER_SMB") : Environment.getExternalStorageDirectory().getAbsolutePath();
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                null,
                "smb",
                "2212"
        );

        File archivoOrigen = null;
        SmbFile archivoDestino = null;
        FileInputStream fis = null;
        SmbFileOutputStream fos = null;

        SmbFile ArchivoDestino;
        SmbFile DirectorioDestino ;

      //  mostarMensaje("Crear Fichero en red", DirectorioRemoto + File.separator + sFileNamex);

        try {
            //String Ubicacion_Servidor = "smb://192.168.1.8/server2";
            archivoOrigen = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "1234.txt");//sFileNamex
         //   archivoDestino = new SmbFile(DirectorioRemoto + File.separatorChar + sFileNamex, auth);

            DirectorioDestino = new SmbFile(Ubicacion_Servidor);
            ArchivoDestino = new SmbFile(Ubicacion_Servidor + "/1234.txt",auth);

            System.out.println("Existe el archivo de origin " +archivoOrigen.exists());

            if (DirectorioDestino.exists()) {
                if (!ArchivoDestino.exists())
                    ArchivoDestino.createNewFile();
                ArchivoDestino.connect();
                copiarFicheroAUnidad(archivoOrigen,ArchivoDestino);
            } else {
                //resp = "Red Local desconetada o no cuenta con permisos";
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("MiAppArchivos", "Excepción capturada", e);
        } catch (SmbException e) {
            e.printStackTrace();//Error en exits de Directory remoto
            Log.e("MiAppArchivos", "Excepción capturada", e);
        } catch (IOException e) {
            e.printStackTrace();//error en connect
            Log.e("MiAppArchivos", "Excepción capturada", e);
        }
        return false;
    }


    public void copiarFicheroDesdeUnidad (SmbFile in, File out){
        SmbFileInputStream fis = null;
        FileOutputStream fos = null;

        try
        {
            fis = new SmbFileInputStream(in);
            fos = new FileOutputStream(out);
            byte[] buf = new byte[1024];

            int i ;
            while ((i=fis.read(buf)) != -1)
            {
                fos.write(buf, 0, i);
            }
            fis.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void copiarFicheroAUnidad (File in, SmbFile out){
        FileInputStream fis = null;
        SmbFileOutputStream fos = null;

        try
        {
            fis = new FileInputStream(in);
            fos = new SmbFileOutputStream(out);
            byte[] buf = new byte[1024];

            int i ;
            while ((i=fis.read(buf)) != -1)
            {
                fos.write(buf, 0, i);
            }
            fis.close();
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void mostarMensaje(String msg1, String msg2) {
        toppings[0] = msg1;
        toppings[1] = msg2;
        publishProgress(toppings);
    }
}
