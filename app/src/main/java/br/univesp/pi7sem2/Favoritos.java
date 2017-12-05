package br.univesp.pi7sem2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    Context contexto;

    @Override
    public void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        contexto = this;
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

    void populate() {
        ArrayList<String> lables = new ArrayList<>();
        dado = new ArrayList<>();

        try {
            BancoDados fav = new BancoDados(this);
            fav.open();
            Cursor test = fav.findFav();

            int i = 1;
            if (!test.isAfterLast()) {
                while (!test.isAfterLast()) {
                    lables.add(i + " - " + test.getString(6) + "\n" + test.getString(2));
                    dado.add(test.getString(16));
                    test.moveToNext();
                    i++;
                }
            } else {
                lables.add("SEM FAVORITOS");
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
    public void onResume() {
        super.onResume();
        populate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.limpar_fav:
                alert2();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void alert2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Deseja apagar todos os favoritos?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    BancoDados fav = new BancoDados(contexto);
                    fav.open();
                    fav.removeAllFav();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                populate();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
