package com.sdn.slp.igu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.filter.Filter;
import com.evrencoskun.tableview.filter.FilterChangedListener;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sdn.bd.dao.TblLectura2;
import com.sdn.bd.dao.TblParametro;
import com.sdn.bd.dao.TblProducto;
import com.sdn.bd.modelo.Lectura2;
import com.sdn.bd.modelo.Producto;

import com.sdn.slp.controlador.Tbl_Parametro;
import com.sdn.sound.SoundManager;
import com.sdn.tableview.TableViewAdapter;
import com.sdn.tableview.TableViewModel;
import com.sdn.tableview.model.Cell;
import com.sdn.utils.ConfApp;
import com.sdn.utils.ExportarExcelAndSamba;
import com.sdn.utils.Utils;

import java.util.Date;
import java.util.List;

public class FrmEscaneoLibre extends AppCompatActivity {
    private TextView txtLectura, txtcodigo, txtdescripcion, txtcaja, txtpesolbs,txtpesokgs, txtlote, txtserie, txtfechaempaque, txtfechaproceso, txtCantidadPiezas, txtpesototal, txtcantidadcajas, lblNoOrden, lblCantidadCajasTotales, lblLbsEscaneadas,lblkgsEscaneadas;
    private TableViewAdapter TblAdapterConsolidado, TblAdapterDetalle;
    private TableViewModel TblModelConsolidado, TblModelDetalle;
    private TableView TblVisorConsolidado, TblVisorDetalle;
    private boolean estaLeyendo = false;
    Integer msg_ok, msg_warning, msg_error, duracion;
    SoundManager sound;

    private Filter tableViewFilter;
    TextInputEditText inputSearch;
    TextInputLayout LayoutinputSearch;

    String codigoAEliminar = "";

    TabHost tabs;
    Toolbar toolbar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnu_escaneolibre, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void NombredeArchivo() {
        LayoutInflater inflater = this.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(FrmEscaneoLibre.this);
        builder.setTitle(R.string.app_descripcion);
        View viewInflated = inflater.inflate(R.layout.pnlarchivo, null);

        final EditText input = (EditText) viewInflated.findViewById(R.id.pnl1_txtNombreArchivo);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre =input.getText().toString().trim();
                new ExportarExcelAndSamba(FrmEscaneoLibre.this, nombre).execute("Exportando Datos", "Preparando datos");
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.p12_item_exportarlocal) {
            TableViewModel tbltempo = new TableViewModel(FrmEscaneoLibre.this, getSQLQUERY());

            if (tbltempo.getRowCount() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Desea exportar los datos?")
                        .setCancelable(false)
                        .setTitle("Exportar Archivo")
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
                                            NombredeArchivo();


                                            }
                                        });
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(FrmEscaneoLibre.this, "No hay registros para exportar", Toast.LENGTH_SHORT);
            }
        }

        if (id == R.id.p12_item_borrar) {
            if (!codigoAEliminar.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Desea borrar la caja " + codigoAEliminar + "?")
                        .setCancelable(false)
                        .setTitle("Borrar caja")
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
                                                TblLectura2.borrarCaja(FrmEscaneoLibre.this, codigoAEliminar);
                                                codigoAEliminar = "";
                                                inputSearch.setText("");
                                                cargarDetalle();
                                                toolbar.getMenu().findItem(R.id.p12_item_borrar).setVisible(TblModelDetalle.getRowCount() > 0);
                                                toolbar.getMenu().findItem(R.id.p12_item_borrartodo).setVisible(TblModelDetalle.getRowCount() > 1);
                                            }
                                        });
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                codigoAEliminar = "";
                inputSearch.setText("");
            }


        }

        if (id == R.id.p12_item_borrartodo) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Desea borrar todas las cjas escaneadas?")
                    .setCancelable(false)
                    .setTitle("Borrar todo")
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
                                            TblLectura2.borrarTodo(FrmEscaneoLibre.this, ConfApp.OPERADORLOGEADO.getId());
                                            cargarDetalle();
                                            toolbar.getMenu().findItem(R.id.p12_item_borrar).setVisible(TblModelDetalle.getRowCount() > 0);
                                            toolbar.getMenu().findItem(R.id.p12_item_borrartodo).setVisible(TblModelDetalle.getRowCount() > 1);
                                        }
                                    });
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_escaneo_libre);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.frmescaneolibre);

        setItemsOnTab();

        //TblLectura2.borrarEnviados(FrmEscaneoLibre.this);

        //PESTANA LECTURA
        txtcodigo =findViewById(R.id.p12_txtCodigo);
        txtLectura = findViewById(R.id.p12_txtCodigoBarra);
        txtdescripcion = findViewById(R.id.p12_txtdescripcion);
        txtcaja = findViewById(R.id.p12_txtcaja);
        txtpesolbs = findViewById(R.id.p12_txtpesolbs);
        txtpesokgs = findViewById(R.id.p12_txtpesokgs);
        txtlote = findViewById(R.id.p12_txtlote);
        txtserie = findViewById(R.id.p12_txtserie);
        txtfechaempaque = findViewById(R.id.p12_txtfechaempaque);
        txtfechaproceso = findViewById(R.id.p12_txtfechaproceso);
        txtCantidadPiezas = findViewById(R.id.p12_txtCantidadPiezas);


        txtcantidadcajas = findViewById(R.id.p12_txtcantcajas);
        txtpesototal = findViewById(R.id.p12_txtpesototal);

        //PRESTANA CONSOLIDADO
        lblCantidadCajasTotales = findViewById(R.id.p12_lblLbsSolicitadas);
        lblLbsEscaneadas = findViewById(R.id.p12_txtLbsEscaneadas);
        lblkgsEscaneadas = findViewById(R.id.p12_txtkbsEscaneadas);

        //PRESTANA DETALLE
        LayoutinputSearch = findViewById(R.id.p12_txtBuscarDetalle);
        inputSearch = findViewById(R.id.p12_txtSearchDetalle);

        try {
            duracion = Integer.parseInt(TblParametro.getClave(FrmEscaneoLibre.this, "Timer"));
        } catch (Exception e) {
            duracion = 3000;
        }

        txtLectura.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                        String codigo = txtLectura.getText().toString().replaceAll("\\s", "");
                        txtLectura.setText("");
                        ocultarTeclado();

                        if ((codigo.length() == 54) && !estaLeyendo && Utils.esCodidoValido(codigo)) {
                            estaLeyendo = true;
                            continuar_Comprobacion(codigo);
                            return true;
                        } else {
                            sound.play(msg_error);
                            //tabs.setBackgroundColor(getResources().getColor(R.color.colorRed));
                            mostrarTimerInmediatamente();
                            return false;
                        }
                    }
                    return false;
                }
                return false;
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tableViewFilter.set(4, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        configurarSonido();
        cargarConsolidado();
        cargarDetalle();
        limpiarFormulario();
    }

    private void setItemsOnTab() {
        Resources res = getResources();
        tabs = findViewById(R.id.p12_layout);
        tabs.setup();
        TabHost.TabSpec spec;

        spec = tabs.newTabSpec("LECTURA");
        spec.setContent(R.id.p12_pnlLectura);
        spec.setIndicator("LECTURA", res.getDrawable(R.drawable.ic_action_buscar));

        tabs.addTab(spec);

        spec = tabs.newTabSpec("RESUMEN");
        spec.setContent(R.id.p12_pnlConsolidado);
        spec.setIndicator("RESUMEN", res.getDrawable(R.drawable.ic_action_buscar));

        tabs.addTab(spec);

        spec = tabs.newTabSpec("DETALLE");
        spec.setContent(R.id.p12_pnlDetalle);
        spec.setIndicator("DETALLE", res.getDrawable(android.R.drawable.ic_delete));
        tabs.addTab(spec);

        // perform set on tab changed listener on tab host
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                toolbar.getMenu().findItem(R.id.p12_item_exportarlocal).setVisible(false);
                toolbar.getMenu().findItem(R.id.p12_item_borrar).setVisible(false);
                toolbar.getMenu().findItem(R.id.p12_item_borrartodo).setVisible(false);

                if (tabId.equals("DETALLE")) {
                    ocultarTeclado();
                    cargarDetalle();
                    toolbar.getMenu().findItem(R.id.p12_item_exportarlocal).setVisible(TblModelDetalle.getRowCount() > 0);
                    toolbar.getMenu().findItem(R.id.p12_item_borrar).setVisible(TblModelDetalle.getRowCount() > 0);
                    toolbar.getMenu().findItem(R.id.p12_item_borrartodo).setVisible(TblModelDetalle.getRowCount() > 1);
                } else if (tabId.equals("RESUMEN")) {
                    ocultarTeclado();
                    cargarConsolidado();
                    toolbar.getMenu().findItem(R.id.p12_item_exportarlocal).setVisible(TblModelDetalle.getRowCount() > 0);
                } else {
                    //mostrarTimerInmediatamente();
                    limpiarFormulario();
                }
                Utils.setTabColor(tabs, FrmEscaneoLibre.this);

                // Add code Here
            }
        });

        tabs.getTabWidget().setCurrentTab(0);
        tabs.getTabWidget().getChildAt(tabs.getCurrentTab()).setBackgroundResource(R.color.zebra_blue);
        TextView tv = tabs.getTabWidget().getChildAt(tabs.getCurrentTab()).findViewById(android.R.id.title); //Unselected Tabs
       tv.setTextColor(ContextCompat.getColor(FrmEscaneoLibre.this, R.color.colorWhite));

    }

    private void mostrarTimerInmediatamente() {
        CountDownTimer countDownTimer = new CountDownTimer(200, 100) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                limpiarFormulario();
            }
        };
        countDownTimer.start();
    }

    private void limpiarFormulario() {
        tabs.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        txtcodigo.setText("");
        txtdescripcion.setText("");
        txtcaja.setText("");
        txtpesolbs.setText("");
        txtpesokgs.setText("");

        txtlote.setText("");
        txtserie.setText("");
        txtfechaempaque.setText("");
        txtfechaproceso.setText("");
        txtCantidadPiezas.setText("");
        txtpesototal.setText("");
        txtcantidadcajas.setText("");
        txtLectura.setText("");
        txtLectura.requestFocus();
        estaLeyendo = false;
    }

    private void configurarSonido() {
        sound = new SoundManager(FrmEscaneoLibre.this);
        Integer tiposonido = (ConfApp.OPERADORLOGEADO.getTipo() == 1 || ConfApp.OPERADORLOGEADO.getTipo() == 2) ? Utils.getTipoSonido(FrmEscaneoLibre.this, "estibador") : Utils.getTipoSonido(FrmEscaneoLibre.this, "verificador");
        msg_warning = sound.load(R.raw.doblebeep_msg_error);
        msg_ok = sound.load(tiposonido == 1 ? R.raw.woman_msg_ok : (tiposonido == 2 ? R.raw.man_msg_ok : R.raw.beep_msg_ok));
        msg_error = sound.load(tiposonido == 1 ? R.raw.woman_msg_error : (tiposonido == 2 ? R.raw.man_msg_error : R.raw.beep_msg_error));
    }

    public void ocultarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public String getSQLQUERY() {
        return "SELECT L.idproducto AS CODIGO,P.nombre,count(L.id) AS CAJAS,  SUM(L.peso_libra) AS  PESO_LIBRA,SUM(L.peso_kilogramo) AS  PESO_KILOGRAMO \n" +
                "FROM Lectura2 AS L LEFT JOIN Producto AS P ON L.idproducto=P.codigo\n" +
                "WHERE L._idoperador="+ ConfApp.OPERADORLOGEADO.getId() +" \n" +
                "GROUP BY L.idproducto";
    }

    private void continuar_Comprobacion(final String codigo) {
        Lectura2 Lectura = Utils.DividirCodigo(codigo);
        Producto producto = TblProducto.obtenerRegistroxCodigo(FrmEscaneoLibre.this, Lectura.getProducto().getCodigo());//ConfApp.CODEBAR_IDPRODUCTO);

        txtcodigo.setText(Lectura.getProducto().getCodigo());
        txtdescripcion.setText(producto.getNombre());
        txtcaja.setText(Lectura.getBarra());
        txtpesolbs.setText("" + String.format("%,.2f", Lectura.getPeso_libra()));
        txtpesokgs.setText("" + String.format("%,.2f", Lectura.getPeso_kilogramo()));
        txtlote.setText("" + Lectura.getLote());
        txtserie.setText("" + Lectura.getSerie());
        txtfechaempaque.setText(Utils.C_DateToAppFormat(Lectura.getFecha_empaque()));//Utils.C_DateToAppFormat(inventario.getFecha_Sacrificio()));
        txtfechaproceso.setText(Utils.C_DateToAppFormat(Lectura.getFecha_proceso()));//Utils.C_DateToAppFormat(inventario.getFecha_Proceso()));
        txtCantidadPiezas.setText(""+Lectura.getCant_pieza());//Utils.C_DateToAppFormat(inventario.getFecha_Vencimiento()));

        String consultas =  "SELECT L.idproducto AS CODIGO,P.nombre,count(L.id) AS CAJAS,  SUM(L.peso_libra) AS  PESO_LIBRA,SUM(L.peso_kilogramo) AS  PESO_KILOGRAMO \n" +
                            " FROM Lectura2 AS L LEFT JOIN Producto AS P ON L.idproducto=P.codigo\n" +
                            " WHERE L._idoperador="+ ConfApp.OPERADORLOGEADO.getId() +" \n";

        TableViewModel tbltempo = new TableViewModel(FrmEscaneoLibre.this, consultas+ " AND L.idproducto='" + Lectura.getProducto().getCodigo() + "'");// + " WHERE T.CODIGO='" + ConfApp.CODEBAR_IDPRODUCTO + "'"
        txtcantidadcajas.setText(tbltempo.getRowCount() > 0 ? tbltempo.getCellList().get(0).get(2).getData().toString() : "0");
        txtpesototal.setText(tbltempo.getRowCount() > 0 ? tbltempo.getCellList().get(0).get(3).getData().toString() : "0.00");

        if (!ConfApp.USER_DTS) {
            if (!TblLectura2.estaRegistrado(FrmEscaneoLibre.this, codigo)) {
                if (TblLectura2.guardar(FrmEscaneoLibre.this, Lectura)) {
                    sound.play(msg_ok);
                    //tabs.setBackgroundColor(getResources().getColor(R.color.colorGreed));
                    mostrarTimer();
                    // mostrarDialogo("REGISTRO GUARDADO", "SE GUARDO CORRECTAMENTE", msg_ok, R.color.colorGreed, true);
                } else {
                    mostrarDialogo("ERROR AL GUARDAR", "NO SE PUDO GUARDAR LA LECTURA", msg_error, R.color.colorRed, true);
                }
            } else {
                sound.play(msg_warning);
               // tabs.setBackgroundColor(getResources().getColor(R.color.colorRed));
                mostrarTimer();
                // mostrarDialogo("CAJA REPETIDA", "CAJA " + codigo + ", YA ESTA REGISTRADA", msg_warning, R.color.colorRed, true);
            }
        } else {
            mostrarDialogo("PERMISO ADMINISTRADOR", "ESTE USUARIO NO PUEDE REGISTRAR OPERACIONES", msg_error, R.color.colorRed, true);
        }
        estaLeyendo = false;
    }

    private void mostrarTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(duracion, 1000) {
            public void onTick(long millisUntilFinished) {
                // mTextField.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                limpiarFormulario();
            }
        };
        countDownTimer.start();
    }

    private void mostrarDialogo(String Titulo, String cuerpoMensaje, Integer sonido, Integer color, boolean MostrarMensaje) {
        sound.play(sonido);
        //tabs.setBackgroundColor(getResources().getColor(color));
        mostrarTimer();

        if (MostrarMensaje) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(Titulo);
            builder.setMessage(Html.fromHtml(cuerpoMensaje))
                    .setPositiveButton("CONTINUAR",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //RegistrarProducto(codigo);
                                }
                            });

            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    private void cargarConsolidado() {
        double PESO_TOTAL_LIBRAS = 0.0;
        double PESO_TOTAL_KILOGRAMOS = 0.0;
        Integer CAJA_TOTAL = 0;

        TblVisorConsolidado = findViewById(R.id.p12_tblconsolidado);
        TblModelConsolidado = new TableViewModel(FrmEscaneoLibre.this, getSQLQUERY());
        TblAdapterConsolidado = new TableViewAdapter(FrmEscaneoLibre.this, TblModelConsolidado);       //
        TableViewAdapter.ON_CORNER_LISTENER = true;

        if (TblModelConsolidado.getRowHeaderList().size() > 0) {
            TblVisorConsolidado.setVisibility(View.VISIBLE);
            TblVisorConsolidado.setAdapter(TblAdapterConsolidado);
            TblAdapterConsolidado.setAllItems(TblModelConsolidado.getColumnHeaderList(), TblModelConsolidado.getRowHeaderList(), TblModelConsolidado.getCellList());

            for (int it = 0; it < TblModelConsolidado.getCellList().size(); it++) {
                CAJA_TOTAL += Integer.parseInt(TblModelConsolidado.getCellList().get(it).get(2).getData().toString());
                PESO_TOTAL_LIBRAS += Double.parseDouble(TblModelConsolidado.getCellList().get(it).get(3).getData().toString());
                PESO_TOTAL_KILOGRAMOS+= Double.parseDouble(TblModelConsolidado.getCellList().get(it).get(4).getData().toString());
            }
        } else {
            TblVisorConsolidado.setVisibility(View.INVISIBLE);
        }

        lblCantidadCajasTotales.setText(CAJA_TOTAL + " CAJAS ESCANEADAS");
        lblLbsEscaneadas.setText(String.format("%.2f", PESO_TOTAL_LIBRAS) + " LBS ESCANEADAS");
        lblkgsEscaneadas.setText(String.format("%.2f", PESO_TOTAL_KILOGRAMOS) + " LBS ESCANEADAS");
    }

    private void cargarDetalle() {
        String SQLQUERY="SELECT L.idproducto,P.nombre,L.fecha_proceso,(CASE WHEN (L.um==0) THEN 'KILOGRAMO' ELSE 'LIBRAS' END) AS UNIDAD_MEDIDA,L.peso_libra AS  PESO_LIBRA,L.peso_kilogramo AS  PESO_KILOGRAMO,L.barra\n" +
        "FROM Lectura2 AS L LEFT JOIN Producto AS P ON L.idproducto=P.codigo\n" +
        "WHERE L._idoperador=" + ConfApp.OPERADORLOGEADO.getId();

        /*String SQLQUERY = "SELECT Lectura2.idproducto AS CODIGO,producto.nombre AS DESCRIPCION, lectura2.fecha_hora AS FECHA_HORA,printf(\"%.2f\", Lectura2.peso) AS PESO, Lectura2.barra AS BARRA " +
                " FROM Lectura2 LEFT JOIN Producto ON Lectura2.idproducto=Producto.codigo" +
                " WHERE Lectura2._enviado=0 AND Lectura2._idoperador =" + ConfApp.OPERADORLOGEADO.getId() +
                " ORDER BY Lectura2.fecha_hora ASC";*/

        TblVisorDetalle = findViewById(R.id.p12_tbldetalle);
        TblModelDetalle = new TableViewModel(FrmEscaneoLibre.this, SQLQUERY);
        TblAdapterDetalle = new TableViewAdapter(FrmEscaneoLibre.this, TblModelDetalle);       //
        TableViewAdapter.ON_CORNER_LISTENER = true;

        if (TblModelDetalle.getRowHeaderList().size() > 0) {
            TblVisorDetalle.setVisibility(View.VISIBLE);
            TblVisorDetalle.setAdapter(TblAdapterDetalle);
            TblAdapterDetalle.setAllItems(TblModelDetalle.getColumnHeaderList(), TblModelDetalle.getRowHeaderList(), TblModelDetalle.getCellList());
            tableViewFilter = new Filter(TblVisorDetalle);
            TblVisorDetalle.setTableViewListener(new ITableViewListener() {

                @Override
                public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
                    try {

                        if (inputSearch.getText().length() < 1) {
                            TblVisorDetalle.setSelectedRow(row);
                            codigoAEliminar = TblModelDetalle.getCellList().get(TblVisorDetalle.getSelectedRow()).get(6).getData().toString();
                        } else {
                        }
                    } catch (Exception e) {
                    }
                }

                /*@Override
                public void onCellDoubleClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

                }*/

                @Override
                public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

                }

                @Override
                public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {

                }

                /*@Override
                public void onColumnHeaderDoubleClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {

                }*/

                @Override
                public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {

                }

                @Override
                public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

                }

                /*@Override
                public void onRowHeaderDoubleClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

                }*/

                @Override
                public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

                }
            });

            TblVisorDetalle.getFilterHandler().addFilterChangedListener(new FilterChangedListener() {
                @Override
                public void onFilterChanged(List filteredCellItems, List filteredRowHeaderItems) {
                    super.onFilterChanged(filteredCellItems, filteredRowHeaderItems);

                    List<List<Cell>> temp = filteredCellItems;

                    if (temp.size() > 0) {
                        codigoAEliminar = temp.get(0).get(4).getData().toString();
                    } else {
                        codigoAEliminar = "";
                    }

                }

            });
            LayoutinputSearch.setVisibility(View.VISIBLE);
        } else {
            TblVisorDetalle.setVisibility(View.INVISIBLE);
            LayoutinputSearch.setVisibility(View.INVISIBLE);
        }
    }


    /****
     * METODO DE NOTIFICAICON DEL HILO EXPORTAR
     */
    public void eliminarEnviados() {
    }
}