package br.univesp.pi7sem2.BDRemota;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.support.v7.app.ActionBarActivity;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.protocol.*;
import android.view.View.*;
import android.view.*;

import br.univesp.pi7sem2.BancoDeDados.TestAdapter;

public class DriveConect extends IntentService
{
    int status;
    //Context context;

    private static final String PATH_TO_SERVER =
    "https://docs.google.com/spreadsheets/d/1Hpur-ezEXlFzjM-XleCyi02K9UuOuBa5ZeiOG_BcQWM/export?format=csv";
    SearchView searchView;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DriveConect(String name, Context context) {
        super(name);
    //    this.context = context;
    }

    public DriveConect(String name) {
        super(name);

    }

    public DriveConect() {
        super("DriveConect");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
/*        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute();*/
 //       Toast.makeText(this,"baixando dados...",Toast.LENGTH_SHORT).show();
        List<String> file = downloadRemoteTextFileContent();
        printCVSContent(file);
    }


    private class DownloadFilesTask extends AsyncTask<URL, Void, List<String>> {
        protected List<String> doInBackground(URL... urls) {
            return downloadRemoteTextFileContent();
        }
        protected void onPostExecute(List<String> result) {
            if(result != null){
                printCVSContent(result);
            //fileContent.setText("download feito com sucesso");
                if(status!=HttpURLConnection.HTTP_OK){
                  //  Toast.makeText(getApplication(),"no connection",Toast.LENGTH_SHORT).show();
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
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
          //  Toast.makeText(this,"print - "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
       // Toast.makeText(this,"arquivo baixado com sucesso",Toast.LENGTH_SHORT).show();

    }
    private List<String> downloadRemoteTextFileContent(){
        URL mUrl = null;
        HttpURLConnection connection=null;
        List<String> csvLine = new ArrayList<>();
        TestAdapter mDbHelper = new TestAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();
        mDbHelper.clearData();
        try {
            mUrl = new URL(PATH_TO_SERVER);
        } catch (MalformedURLException e) {
            e.printStackTrace();
          //  Toast.makeText(this,"url - "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        try {
            assert mUrl != null;
            connection = (HttpURLConnection) mUrl.openConnection();
            connection.setRequestMethod("GET");
            status = connection.getResponseCode();
            if ( status == HttpURLConnection.HTTP_OK) {
                // method body
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                boolean test=false;
                while((line = br.readLine()) != null){
                    csvLine.add(line);
                    String line2 = line.replace(",","','");
                    if(test){
                    mDbHelper.insertData("'"+line2+"'");
                    }
                    test=true;
                }
                br.close();
            mDbHelper.close();
            }
        }catch(IOException e){
            e.printStackTrace();
//            Toast.makeText(this,"conection - "+e.getMessage(),Toast.LENGTH_SHORT).show();

        }catch (Exception e) {
            e.printStackTrace();
  //          Toast.makeText(this,"conection - "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return csvLine;
    }



}
