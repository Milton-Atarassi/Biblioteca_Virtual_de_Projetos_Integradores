package br.univesp.pi7sem2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

import br.univesp.pi7sem2.BancoDeDados.BancoDados;


public class Resultado extends AppCompatActivity {
    Cursor dado;
    Menu menu;

    @Override
    public void onCreate(Bundle Saved) {
        super.onCreate(Saved);
        setContentView(R.layout.projeto);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        //toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        PopupMenu p = new PopupMenu(this, null);
        menu = p.getMenu();


        String id = getIntent().getStringExtra("id");

        try {
            BancoDados bd = new BancoDados(this);
            bd.open();
            dado = bd.where(id);

            String a="";
            for(int i=0;i<dado.getColumnCount();i++)
                a=a+dado.getString(i)+";";



            TextView titulo = (TextView) findViewById(R.id.titulo);
            titulo.setText(dado.getString(6));
            TextView desRes = (TextView) findViewById(R.id.desRes);
            desRes.setText(dado.getString(7));
            TextView descDet = (TextView) findViewById(R.id.descDet);
            descDet.setText(dado.getString(8));
            TextView assuntos = (TextView) findViewById(R.id.assuntos);
            assuntos.setText(dado.getString(9));
            TextView link = (TextView) findViewById(R.id.link);
            link.setText(dado.getString(10));
            TextView linkvideo = (TextView) findViewById(R.id.video);
            linkvideo.setText(dado.getString(14));
            TextView arquivo = (TextView) findViewById(R.id.arquivo);
            arquivo.setText(dado.getString(5));
            TextView periodo = (TextView) findViewById(R.id.periodo);
            periodo.setText(dado.getString(13));
            TextView curso = (TextView) findViewById(R.id.curso);
            curso.setText(dado.getString(2));
            TextView polo = (TextView) findViewById(R.id.polo);
            polo.setText(dado.getString(3));
            TextView grupo = (TextView) findViewById(R.id.grupo);
            grupo.setText(dado.getString(15));
            TextView autores = (TextView) findViewById(R.id.autores);
            autores.setText(dado.getString(11));
            TextView contatos = (TextView) findViewById(R.id.contatos);
            contatos.setText(dado.getString(1) + " (cadastrador), " + dado.getString(12));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav_menu, menu);
        if (dado.getString(4) != null && dado.getString(4).equals("1")) {
            menu.getItem(0).setVisible(false);
        } else {
            menu.getItem(1).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.marcar:
                try {
                    BancoDados fav = new BancoDados(this);
                    fav.open();
                    fav.insertFav(dado.getString(16));
                    Toast.makeText(this, "Salvo em Favoritos", Toast.LENGTH_SHORT).show();
                    menu.getItem(0).setVisible(false);
                    menu.getItem(1).setVisible(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.desmarcar:
                try {
                    BancoDados fav = new BancoDados(this);
                    fav.open();
                    fav.removeFav(dado.getString(16));
                    Toast.makeText(this, "Removido de Favoritos", Toast.LENGTH_SHORT).show();
                    menu.getItem(1).setVisible(false);
                    menu.getItem(0).setVisible(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
