package br.univesp.pi7sem2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import br.univesp.pi7sem2.BancoDeDados.BancoDados;


public class Favoritos extends AppCompatActivity {

    ArrayList<String> dado;
    ArrayAdapter<String> adapter;
    ListView favoritos;

    @Override
    public void onCreate(Bundle Saved){
        super.onCreate(Saved);
        setContentView(R.layout.favoritos);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        //toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        favoritos = (ListView) findViewById(R.id.favoritos);
        populate();


        favoritos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Resultado.class);
                intent.putExtra("id", dado.get(position));
                startActivity(intent);
            }
        });

        favoritos.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
    }

    void populate(){
        ArrayList<String> lables = new ArrayList<>();
        dado = new ArrayList<>();

        try {
            BancoDados fav = new BancoDados(this);
            fav.open();
            Cursor test = fav.findFav();

            int i=1;
            while (!test.isAfterLast()) {
                lables.add(i + " - " + test.getString(6));
                dado.add(test.getString(16));
                test.moveToNext();
                i++;
            }


            fav.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lables);
        favoritos.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        populate();
    }
}
