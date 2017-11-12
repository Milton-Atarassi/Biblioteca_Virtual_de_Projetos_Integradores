package br.univesp.pi7sem2.BancoDeDados;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoDados {

    class novoBD extends SQLiteOpenHelper{

        private static final String name = "banco_de_dados.db";
        private static final String tabela="projetos";

        private static final String data="data";
        private static final String email="email";
        private static final String curso="curso";
        private static final String polo="polo";
        private static final String aut="aut";
        private static final String arquivos="arquivos";
        private static final String titulo="titulo";
        private static final String descricao_r="descricao_r";
        private static final String descricao_d="descricao_d";
        private static final String assuntos="assuntos";
        private static final String link="link";
        private static final String autores="autores";
        private static final String contatos="contatos";
        private static final String id="id";


        public novoBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+ tabela + "("+
               data +"text, "+
               email +"text, "+
               curso +"text, "+
               polo +"text, "+
               aut +"text, "+
               arquivos +"text, "+
               titulo +"text, "+
               descricao_r +"text, "+
               descricao_d +"text, "+
               assuntos +"text, "+
               link +"text, "+
               autores +"text, "+
               contatos +"text, "+
               id +"text, "
                +")";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + tabela);
            onCreate(db);
        }
    }
}
