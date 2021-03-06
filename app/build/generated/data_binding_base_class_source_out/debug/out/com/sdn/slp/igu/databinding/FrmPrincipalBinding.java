// Generated by view binder compiler. Do not edit!
package com.sdn.slp.igu.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.appbar.AppBarLayout;
import com.sdn.slp.igu.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FrmPrincipalBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ScrollView PnlMenuItem;

  @NonNull
  public final TextView Registro;

  @NonNull
  public final AppBarLayout appbar;

  @NonNull
  public final ImageView imageView2;

  @NonNull
  public final ImageView p02ImgConeccion;

  @NonNull
  public final TextView p02LblInfoEquipo;

  @NonNull
  public final TextView p02Lblinfooperador;

  @NonNull
  public final TextView p02Lbloperador;

  @NonNull
  public final TextView p02Lbltipousuario;

  @NonNull
  public final TextView p02Registro;

  @NonNull
  public final TextView p02TxtConectado;

  @NonNull
  public final LinearLayout pnlTitulo;

  @NonNull
  public final Toolbar toolbar;

  private FrmPrincipalBinding(@NonNull LinearLayout rootView, @NonNull ScrollView PnlMenuItem,
      @NonNull TextView Registro, @NonNull AppBarLayout appbar, @NonNull ImageView imageView2,
      @NonNull ImageView p02ImgConeccion, @NonNull TextView p02LblInfoEquipo,
      @NonNull TextView p02Lblinfooperador, @NonNull TextView p02Lbloperador,
      @NonNull TextView p02Lbltipousuario, @NonNull TextView p02Registro,
      @NonNull TextView p02TxtConectado, @NonNull LinearLayout pnlTitulo,
      @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.PnlMenuItem = PnlMenuItem;
    this.Registro = Registro;
    this.appbar = appbar;
    this.imageView2 = imageView2;
    this.p02ImgConeccion = p02ImgConeccion;
    this.p02LblInfoEquipo = p02LblInfoEquipo;
    this.p02Lblinfooperador = p02Lblinfooperador;
    this.p02Lbloperador = p02Lbloperador;
    this.p02Lbltipousuario = p02Lbltipousuario;
    this.p02Registro = p02Registro;
    this.p02TxtConectado = p02TxtConectado;
    this.pnlTitulo = pnlTitulo;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FrmPrincipalBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FrmPrincipalBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.frm_principal, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FrmPrincipalBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.PnlMenuItem;
      ScrollView PnlMenuItem = ViewBindings.findChildViewById(rootView, id);
      if (PnlMenuItem == null) {
        break missingId;
      }

      id = R.id.Registro;
      TextView Registro = ViewBindings.findChildViewById(rootView, id);
      if (Registro == null) {
        break missingId;
      }

      id = R.id.appbar;
      AppBarLayout appbar = ViewBindings.findChildViewById(rootView, id);
      if (appbar == null) {
        break missingId;
      }

      id = R.id.imageView2;
      ImageView imageView2 = ViewBindings.findChildViewById(rootView, id);
      if (imageView2 == null) {
        break missingId;
      }

      id = R.id.p02_img_coneccion;
      ImageView p02ImgConeccion = ViewBindings.findChildViewById(rootView, id);
      if (p02ImgConeccion == null) {
        break missingId;
      }

      id = R.id.p02_lblInfoEquipo;
      TextView p02LblInfoEquipo = ViewBindings.findChildViewById(rootView, id);
      if (p02LblInfoEquipo == null) {
        break missingId;
      }

      id = R.id.p02_lblinfooperador;
      TextView p02Lblinfooperador = ViewBindings.findChildViewById(rootView, id);
      if (p02Lblinfooperador == null) {
        break missingId;
      }

      id = R.id.p02_lbloperador;
      TextView p02Lbloperador = ViewBindings.findChildViewById(rootView, id);
      if (p02Lbloperador == null) {
        break missingId;
      }

      id = R.id.p02_lbltipousuario;
      TextView p02Lbltipousuario = ViewBindings.findChildViewById(rootView, id);
      if (p02Lbltipousuario == null) {
        break missingId;
      }

      id = R.id.p02_registro;
      TextView p02Registro = ViewBindings.findChildViewById(rootView, id);
      if (p02Registro == null) {
        break missingId;
      }

      id = R.id.p02_txt_conectado;
      TextView p02TxtConectado = ViewBindings.findChildViewById(rootView, id);
      if (p02TxtConectado == null) {
        break missingId;
      }

      id = R.id.pnlTitulo;
      LinearLayout pnlTitulo = ViewBindings.findChildViewById(rootView, id);
      if (pnlTitulo == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new FrmPrincipalBinding((LinearLayout) rootView, PnlMenuItem, Registro, appbar,
          imageView2, p02ImgConeccion, p02LblInfoEquipo, p02Lblinfooperador, p02Lbloperador,
          p02Lbltipousuario, p02Registro, p02TxtConectado, pnlTitulo, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
