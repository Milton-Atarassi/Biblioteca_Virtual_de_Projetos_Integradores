package br.univesp.pi7sem2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.univesp.pi7sem2.BancoDeDados.BancoDados;


public class Config extends AppCompatActivity {
    Context context;
    BancoDados dados = null;
    TextView inf_DB;


    @Override
    public void onCreate(Bundle Saved){
        super.onCreate(Saved);
        context = this;

        setContentView(R.layout.config);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        //toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String number = "";
        try {
            dados = new BancoDados(context);
            dados.open();
            Cursor count = dados.searchSQL("SELECT COUNT(*) FROM projetos");
            number = count.getString(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences bd_version = PreferenceManager.getDefaultSharedPreferences(context);
        String data = bd_version.getString("date", "falha");


        inf_DB = (TextView) findViewById(R.id.inf_DB);
        inf_DB.setText(number + " registros no banco de dados\nData de atualização: " + data);

        Button clearDB = (Button) findViewById(R.id.clearDB);
        clearDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert2();
            }
        });

    }

    private void alert2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Deseja apagar todos os registros do banco de dados?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dados.deleteAll();
                    inf_DB.setText("sem registros no banco de dados");
                    Toast.makeText(context, "Banco de dados apagado", Toast.LENGTH_SHORT).show();
                    Busca.atualizar();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
