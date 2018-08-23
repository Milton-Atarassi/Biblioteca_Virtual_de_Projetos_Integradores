package br.univesp.pi7sem2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.univesp.pi7sem2.BDRemota.DriveConect2;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
   public static FragmentManager mFragmentManager;
    public static FragmentTransaction mFragmentTransaction;
    Toolbar toolbar;
    CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences leia = PreferenceManager.getDefaultSharedPreferences(this);

        if(leia.getBoolean("valor",true)){
            Intent intent = new Intent(getApplicationContext(), Leia.class);
            startActivity(intent);

        }

             mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
             mNavigationView = (NavigationView) findViewById(R.id.menu_drawer) ;


             mFragmentManager = getSupportFragmentManager();
             mFragmentTransaction = mFragmentManager.beginTransaction();
             mFragmentTransaction.replace(R.id.containerView,new Inicio()).commit();



        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();



             mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();



                 if (menuItem.getItemId() == R.id.inicio) {
                     FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.containerView,new Inicio()).commit();
                   //  toolbar.setTitle( R.string.app_name);
                 }


                 if (menuItem.getItemId() == R.id.config) {
    /*                 FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                     xfragmentTransaction.replace(R.id.containerView,new Config()).addToBackStack(null).commit();*/
                     Intent intent = new Intent(getApplicationContext(), Config.class);
                     startActivity(intent);
                  //   toolbar.setTitle( menuItem.getTitle());
                 }

                 if (menuItem.getItemId() == R.id.favoritos) {
   /*                  FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                     xfragmentTransaction.replace(R.id.containerView,new Ajuda()).addToBackStack(null).commit();*/
                     Intent intent = new Intent(getApplicationContext(), Favoritos.class);
                     startActivity(intent);
                   //  toolbar.setTitle( menuItem.getTitle());
                 }

                 if (menuItem.getItemId() == R.id.ajuda) {
   /*                  FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                     xfragmentTransaction.replace(R.id.containerView,new Ajuda()).addToBackStack(null).commit();*/
                     Intent intent = new Intent(getApplicationContext(), Ajuda.class);
                     startActivity(intent);
                   //  toolbar.setTitle( menuItem.getTitle());
                 }

                 if (menuItem.getItemId() == R.id.sobre) {
 /*                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                     xfragmentTransaction.replace(R.id.containerView,new Sobre()).addToBackStack(null).commit();*/
                     Intent intent = new Intent(getApplicationContext(), Sobre.class);
                     startActivity(intent);
                   //  toolbar.setTitle( menuItem.getTitle());
                 }

                 return false;
            }

        });

            try {
                DriveConect2 novo = new DriveConect2(MainActivity.this);
                novo.conect();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

}