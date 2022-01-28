package com.sdn.slp.igu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sdn.slp.lecturalibre.TabHostAdapter;

public class FrmEscaneoLibre extends AppCompatActivity {
    TabHostAdapter CollectionPagerAdapter;
    ViewPager2 viewPager;
    TabLayoutMediator mediador;
    TabLayout pestana;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_escaneo_libre);
        CollectionPagerAdapter = new TabHostAdapter(this);
        viewPager = findViewById(R.id.pager);
        pestana = findViewById(R.id.tab_layout);
        viewPager.setAdapter(CollectionPagerAdapter);

        mediador = new TabLayoutMediator(pestana, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull  TabLayout.Tab tab, int position) {
                        switch(position) {
                            case 0:
                                tab.setText("Lectura");
                                tab.setIcon(R.drawable.ic_action_buscar);
                                BadgeDrawable p =  tab.getOrCreateBadge();
                                p.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
                                p.setNumber(100);
                                p.setMaxCharacterCount(200);
                                p.setVisible(true);
                                break;
                            case 1:
                                tab.setText("Consolidado");
                                tab.setIcon(R.drawable.icon_chevron_right_blue);
                                break;
                            case 2:
                                tab.setText("Detalle");
                                break;
                        }
                    }
                }
                /*new TabLayoutMediator.TabConfigurationStrategy() {

            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                System.out.println("posicion actual" +position);

                if(position==0){
                    tab.setText("Lectura");
                    tab.setIcon(R.drawable.ic_action_buscar);
                    BadgeDrawable p =  tab.getOrCreateBadge();
                    p.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
                    p.setNumber(100);
                    p.setMaxCharacterCount(200);
                    p.setVisible(true);
                }

                if(position==1){
                    tab.setText("Consolidado");
                    tab.setIcon(R.drawable.icon_chevron_right_blue);
                }

                if(position==2){
                    tab.setText("Detalle");
                }
            }
        }*/);
        mediador.attach();

    }
}