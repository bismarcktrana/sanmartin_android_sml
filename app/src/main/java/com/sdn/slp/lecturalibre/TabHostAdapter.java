package com.sdn.slp.lecturalibre;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabHostAdapter extends FragmentStateAdapter {

    public TabHostAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragmento = new Fragment();
        if(position ==0){
            fragmento =   new LabLectura();
            Bundle args = new Bundle();
        // Our object is just an integer :-P
            args.putInt(LabLectura.ARG_PARAM1, position + 1);
            fragmento.setArguments(args);
        }
        if(position ==1){
            fragmento =  new TabConsolidado();
        }
        if(position ==2){
            fragmento =  new TabDetalle();
        }

        // Return a NEW fragment instance in createFragment(int)
//        Fragment fragment = new LecturaFragment();
//        Bundle args = new Bundle();
//        // Our object is just an integer :-P
//        args.putInt(LecturaFragment.ARG_OBJECT, position + 1);
//        fragment.setArguments(args);
        return fragmento;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
