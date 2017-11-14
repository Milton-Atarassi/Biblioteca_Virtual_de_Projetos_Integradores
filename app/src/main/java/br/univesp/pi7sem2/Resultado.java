package br.univesp.pi7sem2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class Resultado extends AppCompatActivity {
    static ArrayList<String>dado;

    @Override
    public void onCreate(Bundle Saved){
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

        int item = getIntent().getIntExtra("posicao", -1);
        dado = Busca.getDado(item);

        TextView titulo = (TextView) findViewById(R.id.titulo);
        titulo.setText(" "+dado.get(6));
        TextView desRes = (TextView) findViewById(R.id.desRes);
        desRes.setText(" "+dado.get(7));
        TextView descDet = (TextView) findViewById(R.id.descDet);
        descDet.setText(" "+dado.get(8));
        TextView assuntos = (TextView) findViewById(R.id.assuntos);
        assuntos.setText(" "+dado.get(9));
        TextView link = (TextView) findViewById(R.id.link);
        link.setText(" "+dado.get(10));
        TextView arquivo = (TextView) findViewById(R.id.arquivo);
        arquivo.setText(" "+dado.get(5));
        TextView curso = (TextView) findViewById(R.id.curso);
        curso.setText(" "+dado.get(2));
        TextView polo = (TextView) findViewById(R.id.polo);
        polo.setText(" "+dado.get(3));
        TextView autores = (TextView) findViewById(R.id.autores);
        autores.setText(" "+dado.get(11));
        TextView contatos = (TextView) findViewById(R.id.contatos);
        contatos.setText(" "+dado.get(1)+","+dado.get(12));

        Button salvar = (Button)findViewById(R.id.salvar);

    }

}
