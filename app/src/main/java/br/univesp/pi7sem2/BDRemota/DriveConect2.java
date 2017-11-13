package br.univesp.pi7sem2.BDRemota;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
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
import java.util.ArrayList;
import java.util.List;

import br.univesp.pi7sem2.BancoDeDados.TestAdapter;

public class DriveConect2
{
    int status;
    Context context;

    private static final String PATH_TO_SERVER =
    "https://docs.google.com/spreadsheets/d/1Hpur-ezEXlFzjM-XleCyi02K9UuOuBa5ZeiOG_BcQWM/export?format=csv";
    SearchView searchView;



    public void conect(Context context) {
        this.context=context;
        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute();
              Toast.makeText(context, "atualizando dados...", Toast.LENGTH_SHORT).show();
    }


    private class DownloadFilesTask extends AsyncTask<URL, Void, List<String>> {
        protected List<String> doInBackground(URL... urls) {
            return downloadRemoteTextFileContent();
        }
        protected void onPostExecute(List<String> result) {
            if(result != null){
                printCVSContent(result);
                if(status!=HttpURLConnection.HTTP_OK){
                   Toast.makeText(context,"sem conexão",Toast.LENGTH_SHORT).show();
                }
            }
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
                File file = new File(externalStorageDir + "/bd_csv.csv");
                FileWriter f = new FileWriter(file);
                f.write(csvLine);
                f.close();

                Toast.makeText(context,"arquivo baixado com sucesso",Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                e.printStackTrace();

                        }

        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(context,"print - "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    private List<String> downloadRemoteTextFileContent(){
        Handler handler =  new Handler(context.getMainLooper());
        URL mUrl = null;
        HttpURLConnection connection=null;
        List<String> csvLine = new ArrayList<>();
        TestAdapter mDbHelper = new TestAdapter(context);
        mDbHelper.createDatabase();
        mDbHelper.open();
        mDbHelper.clearData();
        try {
            mUrl = new URL(PATH_TO_SERVER);
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "url - " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        try {
            assert mUrl != null;
            connection = (HttpURLConnection) mUrl.openConnection();
            //connection.setRequestMethod("GET");
            status = connection.getResponseCode();
            if ( status == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                boolean test=false;
                while((line = br.readLine()) != null){
                    csvLine.add(line);
                    String line2 = line.replace(",","','");
                    if(test){
  /*                      char[] a = line.toCharArray();
                        for(int i=0;i<a.length;i++){
                            char[] b=''+a+'';

                        }*/
                    mDbHelper.insertData("'"+line2+"'");
                    }
                    test=true;
                }
                br.close();
            mDbHelper.close();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "base de dados atualizada com sucesso", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }catch(final IOException e){
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "erro - " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }catch (final Exception e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "erro - " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return csvLine;
    }



}
