package br.univesp.pi7sem2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
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
    String shareContent;
    private ShareActionProvider mShareActionProvider;

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
            shareContent = "Título:\n" + dado.getString(6) + "\n";

            TextView desRes = (TextView) findViewById(R.id.desRes);
            desRes.setText(dado.getString(7));
            shareContent += "Descrição:\n" + dado.getString(7) + "\n";

            TextView descDet = (TextView) findViewById(R.id.obs);
            descDet.setText(dado.getString(8));
            shareContent += "Observações:\n" + dado.getString(8) + "\n";

            TextView assuntos = (TextView) findViewById(R.id.assuntos);
            assuntos.setText(dado.getString(9));
            shareContent += "Assuntos relacionados:\n" + dado.getString(9) + "\n";

            TextView link = (TextView) findViewById(R.id.link);
            link.setText(dado.getString(10));
            shareContent += "Link do projeto:\n" + dado.getString(10) + "\n";

            TextView linkvideo = (TextView) findViewById(R.id.video);
            linkvideo.setText(dado.getString(14));
            shareContent += "Link do vídeo:\n" + dado.getString(14) + "\n";

            TextView arquivo = (TextView) findViewById(R.id.arquivo);
            arquivo.setText(dado.getString(5));
            shareContent += "Arquivos:\n" + dado.getString(5) + "\n";

            TextView periodo = (TextView) findViewById(R.id.periodo);
            periodo.setText(dado.getString(13));
            shareContent += "Periodo:\n" + dado.getString(13) + "\n";

            TextView curso = (TextView) findViewById(R.id.curso);
            curso.setText(dado.getString(2));
            shareContent += "Curso:\n" + dado.getString(2) + "\n";

            TextView polo = (TextView) findViewById(R.id.polo);
            polo.setText(dado.getString(3));
            shareContent += "Polo:\n" + dado.getString(3) + "\n";

            TextView grupo = (TextView) findViewById(R.id.grupo);
            grupo.setText(dado.getString(15));
            shareContent += "Grupo:\n" + dado.getString(15) + "\n";

            TextView autores = (TextView) findViewById(R.id.autores);
            autores.setText(dado.getString(11));
            shareContent += "Autores:\n" + dado.getString(11) + "\n";

            TextView contatos = (TextView) findViewById(R.id.contatos);
            contatos.setText(dado.getString(1) + " (cadastrador), " + dado.getString(12));
            shareContent += "Contatos:\n" + dado.getString(1) + " (cadastrador), " + dado.getString(12) + "\n";

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

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
            case R.id.menu_item_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Biblioteca Virtual de Projetos Integradores");
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                sendIntent.setType("text/plain");
                setShareIntent(sendIntent);
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
