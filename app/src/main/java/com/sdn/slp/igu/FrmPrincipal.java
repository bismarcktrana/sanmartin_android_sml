package com.sdn.slp.igu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdn.utils.ConfApp;

public class FrmPrincipal extends AppCompatActivity {

    private TextView txtestado;
    private ImageView img_coneccion;
    private LinearLayout panelPresentacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_principal);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ((TextView) findViewById(R.id.p02_registro)).setText(ConfApp.UUID_FROM_DEVICE);
        ((TextView) findViewById(R.id.p02_lblInfoEquipo)).setText(ConfApp.USER_DTS ?"dts": (ConfApp.OPERADORLOGEADO.getTipo()==1?"Administrador":"Operador")  );
        ((TextView) findViewById(R.id.p02_lblinfooperador)).setText(ConfApp.USER_DTS ?"dts":ConfApp.OPERADORLOGEADO.getNombre());

        txtestado = (TextView) findViewById(R.id.p02_txt_conectado);
        img_coneccion = (ImageView) findViewById(R.id.p02_img_coneccion);

        ScrollView a =  findViewById(R.id.PnlMenuItem);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate( R.layout.pnl_modulos, null);
        a.addView (  dialogView);

        ((LinearLayout) dialogView.findViewById(R.id.optorden)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nuevaPantalla = new Intent(FrmPrincipal.this, FrmEscaneoLibre.class);
                startActivity(nuevaPantalla);
            }
        });

        ((LinearLayout) dialogView.findViewById(R.id.optdts)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nuevaPantalla = new Intent(FrmPrincipal.this, FrmBaseDeDatos.class);
                startActivity(nuevaPantalla);
            }
        });

        if(!ConfApp.USER_DTS){
            RemoverVista(dialogView,R.id.optdts);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.m02_action_salir) {
            cerrarSesion();
        }
        return super.onOptionsItemSelected(item);
    }

    public void RemoverVista(View contenedor, int vista){
        View namebar = contenedor.findViewById(vista);
        ViewGroup parent = (ViewGroup) namebar.getParent();
        if (parent != null) {
            parent.removeView(namebar);
        }
    }


/*
    @Override
    protected void onPostResume() {//SE EJECUTA DESPUES DE ONCREATE
        super.onPostResume();
        ConfApp.loadParameters(FrmPrincipal.this);
    }*/

    private void cerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("<small>Desea cerrar session?</small>"))
                .setIcon(R.drawable.img_logo)
                .setCancelable(false)
                .setTitle(Html.fromHtml("<font color='#FF7F27'><small>Confirmar cierre de sesion</small></font>"))
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("SI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        onBackPressed();
                                    }
                                });
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}