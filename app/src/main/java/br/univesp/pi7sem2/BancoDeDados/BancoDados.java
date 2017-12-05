package br.univesp.pi7sem2.BancoDeDados;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class BancoDados {
    private static final String name = "banco_de_dados.db";
    private static final String tabela="projetos";
    private static final String data="data";
    private static final String email="email";
    private static final String curso="curso";
    private static final String polo="polo";
    private static final String fav="fav";
    private static final String arquivos="arquivos";
    private static final String titulo="titulo";
    private static final String observacao="observacao";
    private static final String descricao="descricao";
    private static final String assuntos="assuntos";
    private static final String link="link";
    private static final String autores="autores";
    private static final String contatos="contatos";
    private static final String periodo="periodo";
    private static final String linkvideo="linkvideo";
    private static final String grupo="grupo";
    private static final String id="id";
    private static final int databaseversion=1;
    Context context;
    //   private NovoBD mDbHelper;
    private DataBaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    public BancoDados(Context ctx) {
        this.context = ctx;
    }

    public BancoDados open() throws SQLException {
        try {
            mDbHelper = new DataBaseHelper(context);
            mDbHelper.createDataBase();
            mDbHelper.openDataBase();
 //           mDbHelper = new NovoBD(context,name,null,databaseversion);
            mDb = mDbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void insertData(String dado)throws android.database.SQLException {

        mDb.execSQL("insert into "+ tabela +"(" +
                data +", "+
                email +", "+
                titulo +", "+
                descricao +", "+
                assuntos +", "+
                link +", "+
                arquivos +", "+
                periodo +", "+
                observacao +", "+
                curso +", "+
                polo +", "+
                autores +", "+
                contatos +", "+
                linkvideo +", "+
                grupo +", "+
                id +")"
                +" values("+dado+");");

    }

    public void setDado(String[] dado)throws SQLException{
        mDb.execSQL("update "+ tabela +" set "+
                        data +"='"+dado[0]+"', "+
                        email +"='"+dado[1]+"', "+
                titulo +"='"+dado[2]+"', "+
                descricao +"='"+dado[3]+"', "+
                assuntos +"='"+dado[4]+"', "+
                link +"='"+dado[5]+"', "+
                arquivos +"='"+dado[6]+"', "+
                periodo +"='"+dado[7]+"', "+
                observacao +"='"+dado[8]+"', "+
                curso +"='"+dado[9]+"', "+
                        polo +"='"+dado[10]+"', "+
                        autores +"='"+dado[11]+"', "+
                        contatos +"='"+dado[12]+"', "+
                linkvideo +"='"+dado[14]+"', "+
                grupo +"='"+dado[15]+"', "+
                        id +"='"+dado[16]+"' "+
                " where "+id+"='"+dado[16]+"';");
    }

    public void insertFav(String data, int valor) throws SQLException {
        mDb.execSQL("update " + tabela + " set " + fav + "='" + valor + "' where " + id + "='" + data + "';");
    }

    public void removeFav(String data)throws SQLException{
        mDb.execSQL("update " + tabela + " set " + fav + " = NULL" + " where " + id + "='" + data + "';");
    }

    public void removeAllFav() throws SQLException {
        mDb.execSQL("update " + tabela + " set " + fav + " = NULL;");
    }

    public Cursor findFav()throws SQLException{
        Cursor mCursor = mDb.rawQuery("select * from " + tabela + " where " + fav + " IS NOT NULL ORDER BY " + fav + " DESC;", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor search(String inputText) throws SQLException {
        String query = "SELECT * "+
                " FROM " + tabela+
            " WHERE "
                 +tabela +
                " MATCH "+
                inputText
/*                +  titulo +":"+ inputText + " OR "
                +  assuntos +":"+ inputText + " OR "
                +  descricao_r +":"+ inputText + " OR "
                +  descricao_d +":"+ inputText*/

/*                +  titulo +" LIKE '%"+ inputText + "%' OR "
                +  assuntos +" LIKE '%"+ inputText + "%' OR "
                +  descricao_r +" LIKE '%"+ inputText + "%' OR "
                +  descricao_d +" LIKE '%"+ inputText*/

//                +  arquivos +" LIKE '"+ inputText

                +" ORDER BY "+periodo+" DESC;";
        Cursor mCursor = mDb.rawQuery(query,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor searchSQL(String inputText) throws SQLException {

        Cursor mCursor = mDb.rawQuery(inputText,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor where(String _id) throws SQLException {
        String query = "SELECT *"+
                " FROM " + tabela+
                " WHERE " +  id + " MATCH '"+id+":" + _id + "';";
        Cursor mCursor = mDb.rawQuery(query,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public void deleteDado(String _id) throws SQLException {
        String query = "DELETE FROM " + tabela +
                " WHERE " + id + "='" + _id + "';";
        mDb.execSQL(query);
    }

    public Cursor all() throws SQLException {
        String query = "SELECT * "+
                " from " + tabela+";";
        Cursor mCursor = mDb.rawQuery(query,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public boolean deleteAll() {

        int doneDelete = 0;
        doneDelete = mDb.delete(tabela, null , null);
        return doneDelete > 0;

    }



    class NovoBD extends SQLiteOpenHelper{



         public NovoBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE VIRTUAL TABLE "+ tabela + " USING fts3("+
//                String sql = "CREATE TABLE "+ tabela + " ("+

                    data +" TEXT, "+
               email +" TEXT, "+
               curso +" TEXT, "+
               polo +" TEXT, "+
                fav +" TEXT, "+
               arquivos +" TEXT, "+
               titulo +" TEXT, "+
               descricao +" TEXT, "+
               observacao +" TEXT, "+
               assuntos +" TEXT, "+
               link +" TEXT, "+
               autores +" TEXT, "+
               contatos +" TEXT, "+
                periodo +" TEXT, "+
                linkvideo +" TEXT, "+
                grupo +" TEXT, "+
                id +" TEXT);";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + tabela);
            onCreate(db);
        }
    }
}
