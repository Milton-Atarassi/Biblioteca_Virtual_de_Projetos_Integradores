package br.univesp.pi7sem2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import br.univesp.pi7sem2.BDRemota.DriveConect;
import br.univesp.pi7sem2.BDRemota.DriveConect2;


public class Inicio extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.inicio, null);

        setHasOptionsMenu(true);


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });


        return view;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.atualizar) {
            try {
/*                Intent intent = new Intent(getActivity(), DriveConect.class);
                getActivity().startService(intent);*/
                DriveConect2 novo = new DriveConect2();
                novo.conect(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
           return true;
        }

        if (item.getItemId() == R.id.sugestoes) {
            try {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "milton.atarassi@aluno.univesp.br", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sugestoes para aplicativo");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return true;
    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Busca();
                case 1:
                    return new Insercao();

            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }


        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Busca";
                case 1:
                    return "Inserção";

            }
            return null;
        }
    }

}
