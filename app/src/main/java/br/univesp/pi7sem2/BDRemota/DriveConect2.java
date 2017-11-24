package br.univesp.pi7sem2.BDRemota;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.univesp.pi7sem2.BancoDeDados.BancoDados;
import br.univesp.pi7sem2.BancoDeDados.TestAdapter;
import br.univesp.pi7sem2.R;

public class DriveConect2
{
    int status;
    Context context;

    private String PATH_TO_SERVER ;

    public DriveConect2(Context context) {
        this.context = context;
        String main = "https://docs.google.com/";
        String type = "spreadsheets/d/";
        String format = "/export?format=tsv";
        String id = context.getString(R.string.id2);
        PATH_TO_SERVER =main+type+id+format;
    }

    public void conect() {
        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute();
    }


    private class DownloadFilesTask extends AsyncTask<URL, Void, List<String>> {
        ProgressDialog progressDialog;
        protected List<String> doInBackground(URL... urls) {
            return downloadRemoteTextFileContent();
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
 //           Toast.makeText(context,"Atualizando base de dados",Toast.LENGTH_SHORT).show();

            progressDialog = ProgressDialog.show(context, "Atualizando base de dados", "Aguarde por favor", true, false);
        }

        protected void onPostExecute(List<String> result) {
            if(result != null){
           //     printCVSContent(result);
                if(status!=HttpURLConnection.HTTP_OK){
                   Toast.makeText(context,"sem conexão",Toast.LENGTH_SHORT).show();
                }
            }
            progressDialog.dismiss();

        }
    }
    private void printCVSContent(List<String> result){
        try{
            String csvLine = "";


                for(String row:result) {
                    csvLine += row + " ";
                    csvLine += "\n";
                }

                        try {
                File externalStorageDir = Environment.getExternalStorageDirectory();
                File file = new File(externalStorageDir + "/bd.tsv");
                FileWriter f = new FileWriter(file);
                f.write(csvLine);
                f.close();

                Toast.makeText(context,"arquivo baixado com sucesso",Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                e.printStackTrace();

                        }

        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context,"erro - "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    private List<String> downloadRemoteTextFileContent(){
        Handler handler =  new Handler(context.getMainLooper());
        URL mUrl = null;
        HttpURLConnection connection=null;
        List<String> csvLine = null;

 /*       TestAdapter mDbHelper = new TestAdapter(context);
        mDbHelper.createDatabase();
        mDbHelper.open();
        mDbHelper.clearData();*/

            try {
                BancoDados mDbHelper= new BancoDados(context);
                mDbHelper.open();
                //           mDbHelper.deleteAll();
                Cursor cursor = mDbHelper.all();

                mUrl = new URL(PATH_TO_SERVER);

                assert mUrl != null;
                connection = (HttpURLConnection) mUrl.openConnection();
                //connection.setRequestMethod("GET");
                status = connection.getResponseCode();
                if ( status == HttpURLConnection.HTTP_OK) {
                    csvLine = new ArrayList<>();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    boolean test=false;
                    while((line = br.readLine()) != null){
                        csvLine.add(line);
                        if (test) {
    /*                    String line3 = line.replace("\t","','");
                        line3 = "'"+line3+"'";*/

                        String[] line2 = line.split("\t");
                        if(!cursor.isAfterLast()){
                            if(!cursor.getString(0).equals(line2[0])){
                                mDbHelper.setDado(line2);
                            }
                        }else {
                            String line3 = dado(line2);


                                mDbHelper.insertData(line3);
                            }
                            cursor.moveToNext();

                        }
                        test = true;

                    }
                    br.close();
                mDbHelper.close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "base de dados atualizado com sucesso", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            } catch (final IOException e1) {
                e1.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "erro - " + e1.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            } catch (final SQLException e1) {
                e1.printStackTrace();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "erro - " + e1.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }

finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return csvLine;
    }

public String dado(String[]line2){
    String line3 = "'";
    for (int i = 0; i < line2.length; i++)
        if (i != 13) {
            if (i != (line2.length - 1)) {
                line3 += line2[i] + "','";
            } else {
                line3 += line2[i] + "'";
            }
        }
    return line3;
}

}
