package com.sdn.slp.igu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdn.bd.dao.TblOperador;
import com.sdn.bd.dao.TblParametro;
import com.sdn.bd.modelo.Lectura2;
import com.sdn.slp.controlador.Tbl_Parametro;
import com.sdn.bd.modelo.Operador;
import com.sdn.utils.ConfApp;
import com.sdn.utils.Utils;

import java.io.File;
import java.util.Date;

public class Frm_IniciarSesion extends AppCompatActivity {

    private TextView lblCliente,lblMensaje;
    private EditText txtusuario,txtClave;
    private Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.createWorkDirectory(Frm_IniciarSesion.this,"sml");
        //ConfApp.createDirectoryWork(Frm_IniciarSesion.this);
        ConfApp.loadParameters(Frm_IniciarSesion.this);
/*
        String appDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SML";
        File dir_temp = new File(appDirPath);
        dir_temp.setExecutable(true);
        dir_temp.setWritable(true);
        dir_temp.setReadable(true);
        dir_temp.setLastModified(new Date().getTime());

        if(dir_temp.isDirectory()){
            System.out.println("Si es un directorio");
            if(!dir_temp.exists()){
                dir_temp.mkdirs();
            }
        }else{
            System.out.println("No es un directorio");
        }*/



       // Utils.sendSamba(Frm_IniciarSesion.this);
       // ConfApp.loadConection();
        //Lectura2  lectura= Utils.DividirCodigo("010000000000598211151112320304255610000209032194998905");
        //System.out.println("Lectura "+lectura.toString2());
       // ConfApp.print2();


        setContentView(R.layout.frm_iniciar_sesion);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        lblCliente = (TextView) findViewById(R.id.p1_lblNombreCliente);
        lblMensaje =(TextView) findViewById(R.id.p1_lblMensaje);
        txtusuario = (EditText) findViewById(R.id.p1_txtUsuario);
        txtClave = (EditText) findViewById(R.id.p1_txtClave);

        btnIngresar = (Button) findViewById(R.id.p1_btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConfApp.DEVICEAUTORIZED) {
                    continuarComprobacion();
                }else{
                    Toast.makeText(Frm_IniciarSesion.this, String.format(getResources().getString(R.string.p1_lblDispositivoNoAutorizado),ConfApp.UUID_FROM_DEVICE), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void continuarComprobacion() {
        String Usuario =txtusuario.getText().toString().trim();
        String Clave = txtClave.getText().toString().trim();

        if(!Usuario.isEmpty() && !Clave.isEmpty()){

            if(Usuario.equals(ConfApp.SYSTEM_USER) && Clave.equals(ConfApp.SYSTEM_PASS)){
                ConfApp.USER_DTS=true;
                ConfApp.USER_SUPERVISOR =true;
                ConfApp.OPERADORLOGEADO = new Operador(Usuario,Clave,Usuario,1);
                Intent nuevaPantalla = new Intent(Frm_IniciarSesion.this, FrmPrincipal.class);
                startActivity(nuevaPantalla);
            }else{
                ConfApp.OPERADORLOGEADO = TblOperador.buscarUsuario(Frm_IniciarSesion.this, Usuario, Clave);
                txtusuario.setText("");
                txtClave.setText("");

                if (ConfApp.OPERADORLOGEADO.getId() != -1) {
                    ConfApp.USER_DTS=false;
                    ConfApp.USER_SUPERVISOR = ConfApp.OPERADORLOGEADO.getTipo() == 1;
                    ConfApp.USER_ESTIBADOR = ConfApp.OPERADORLOGEADO.getTipo() == 2;

                    Intent nuevaPantalla = new Intent(Frm_IniciarSesion.this, FrmPrincipal.class);
                    startActivity(nuevaPantalla);
                }
            }


            /*ConfApp.OPERADORLOGEADO = validarUsuario(Frm_IniciarSesion.this,Usuario,Clave);

            if(ConfApp.OPERADORLOGEADO.getId()!=-1){

            }*/

        }
    }

    @Override
    protected void onPostResume() {//SE EJECUTA DESPUES DE ONCREATE
        super.onPostResume();
        ConfApp.loadParameters(Frm_IniciarSesion.this);
        lblCliente.setText(Html.fromHtml("<CENTER><B>INDUSTRIAL COMERCIAL<BR>SAN MARTIN</B></CENTER>"));
        actualizarMensajeLicencia();
        limpiarFormulario();
    }
    private void limpiarFormulario(){
        txtusuario.setText("");
        txtClave.setText("");
        txtusuario.requestFocus();
    }

    private void actualizarMensajeLicencia() {
        lblMensaje.setText(ConfApp.DEVICEAUTORIZED ? String.format(getResources().getString(R.string.p1_lblDispositoAutorizado),ConfApp.UUID_FROM_DEVICE)  :  String.format(getResources().getString(R.string.p1_lblDispositivoNoAutorizado),ConfApp.UUID_FROM_DEVICE));
        lblMensaje.setTextColor(ConfApp.DEVICEAUTORIZED ? getResources().getColor(R.color.colorGreed ) : getResources().getColor(R.color.colorRed ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnu_ingreso, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m01_action_salir:
                cerrarApliacion();
                return true;
            case R.id.m01_action_help:
                Leerparametro();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cerrarApliacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("<small>Desea cerrar la Aplicacion..?</small>"))
                .setIcon(R.drawable.img_logo)
                .setCancelable(false)
                .setTitle(Html.fromHtml("<font color='#FF7F27'><small>Confirmar cierre de programa</small></font>"))
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("SI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void Leerparametro() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Frm_IniciarSesion.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.pnl_parametro, null);
        dialogBuilder.setView(dialogView);

        final EditText txtparametro = (EditText) dialogView.findViewById(R.id.pnl1_txtParametro);
        final EditText txtclave = (EditText) dialogView.findViewById(R.id.pnl1_txtValor);
        txtparametro.setOnClickListener(null);
        txtclave.setOnClickListener(null);

        dialogBuilder.setTitle(getResources().getString(R.string.p1_lblConfigurar).toUpperCase());
        //((TextView) findViewById(R.id.p1_lblMensaje)).setText(ConfApp.DEVICEAUTORIZED ? "" :  );

        if(!ConfApp.DEVICEAUTORIZED)
            dialogBuilder.setMessage(String.format(getResources().getString(R.string.p1_lblDispositivoNoAutorizado),ConfApp.UUID_FROM_DEVICE));

        dialogBuilder.setIcon(R.drawable.img_logo);

        dialogBuilder.setPositiveButton(getResources().getString(R.string.lblGuardar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String TEMP_PARAMETRO = txtparametro.getText().toString().trim();
                String TEMP_VALOR = txtclave.getText().toString().trim();

                if(Tbl_Parametro.getClave(getApplication(),TEMP_PARAMETRO).isEmpty()){
                    Toast.makeText(Frm_IniciarSesion.this ,String.format(getResources().getString(R.string.lblParametroNoExiste),TEMP_PARAMETRO),Toast.LENGTH_LONG).show();
                }else{
                    if(TEMP_PARAMETRO.equals("UUID") || TEMP_PARAMETRO.equals("SERVER") || TEMP_PARAMETRO.equals("BDNAMECATALOG") ||TEMP_PARAMETRO.equals("BDUSER") ||TEMP_PARAMETRO.equals("BDPASS") ||TEMP_PARAMETRO.equals("system_user")||TEMP_PARAMETRO.equals("system_pass")||TEMP_PARAMETRO.equals("user_policies")||TEMP_PARAMETRO.equals("register_policies")||TEMP_PARAMETRO.equals("export_policies")||TEMP_PARAMETRO.equals("import_policies")||TEMP_PARAMETRO.equals("partial_import_policies"))
                        TEMP_VALOR = Utils.Encriptar(Frm_IniciarSesion.this,TEMP_VALOR);

                    if(Tbl_Parametro.modificar(getApplicationContext(),TEMP_PARAMETRO,TEMP_VALOR)){
                        Toast.makeText(Frm_IniciarSesion.this ,String.format( getResources().getString(R.string.lblParametroModificad),TEMP_PARAMETRO),Toast.LENGTH_LONG).show();
                        ConfApp.loadParameters(Frm_IniciarSesion.this);
                    }

                    actualizarMensajeLicencia();
                    limpiarFormulario();
                }
            }
        });
        dialogBuilder.setNegativeButton(getResources().getString(R.string.lblCancelar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
        txtparametro.requestFocus();
    }
}

