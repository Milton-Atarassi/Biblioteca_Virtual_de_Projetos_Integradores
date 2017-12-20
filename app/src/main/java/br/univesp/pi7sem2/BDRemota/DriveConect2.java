package br.univesp.pi7sem2.BDRemota;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.univesp.pi7sem2.BancoDeDados.BancoDados;
import br.univesp.pi7sem2.Busca;
import br.univesp.pi7sem2.R;

public class DriveConect2 {

    int status;
    Context context;
    private String PATH_TO_SERVER;

    public DriveConect2(Context context) {
        this.context = context;
        String main = "https://docs.google.com/";
        String type = "spreadsheets/d/";
        String format = "/export?format=tsv";
        String id = context.getString(R.string.id);
        PATH_TO_SERVER = main + type + id + format;

    }

    public void conect() {
        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute();
    }

    private void printCVSContent(List<String> result) {
        try {
            String csvLine = "";


            for (String row : result) {
                csvLine += row + " ";
                csvLine += "\n";
            }

            try {
                File externalStorageDir = Environment.getExternalStorageDirectory();
                File file = new File(externalStorageDir + "/bd.tsv");
                FileWriter f = new FileWriter(file);
                f.write(csvLine);
                f.close();

                Toast.makeText(context, "arquivo baixado com sucesso", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();

            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "erro - " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private List<String> downloadRemoteTextFileContent() {
        Handler handler = new Handler(context.getMainLooper());
        URL mUrl = null;
        HttpURLConnection connection = null;
        List<String> csvLine = null;
        int ins = 0;
        int upd = 0;
        int delet = 0;
        ArrayList<String> del = new ArrayList<>();

 /*       TestAdapter mDbHelper = new TestAdapter(context);
        mDbHelper.createDatabase();
        mDbHelper.open();
        mDbHelper.clearData();*/

        try {
            BancoDados mDbHelper = new BancoDados(context);
            mDbHelper.open();
            //           mDbHelper.deleteAll();
            Cursor cursor = mDbHelper.all();

            mUrl = new URL(PATH_TO_SERVER);

            assert mUrl != null;
            connection = (HttpURLConnection) mUrl.openConnection();
            //connection.setRequestMethod("GET");
            status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                csvLine = new ArrayList<>();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                boolean test = false;
                while ((line = br.readLine()) != null) {
                    //  csvLine.add(line);

                    if (test) {
    /*                    String line3 = line.replace("\t","','");
                        line3 = "'"+line3+"'";*/

                        String[] line2 = line.split("\t");
                        if (!cursor.isAfterLast()) {
                            boolean teste = !cursor.isAfterLast();
                            while (teste) {
                                String id = cursor.getString(16);
                                String date = cursor.getString(0);
                                if (!id.equals(line2[16])) {
                                    del.add(cursor.getString(16));
                                    delet++;
                                    cursor.moveToNext();
                                    teste = !cursor.isAfterLast();
                                    Log.v("banco de dados", "deletado");
                                } else {
                                    if (!date.equals(line2[0])) {
                                        mDbHelper.setDado(line2);
                                        cursor.moveToNext();
                                        upd++;
                                        teste = false;
                                        Log.v("banco de dados", "updated");
                                    } else {
                                        cursor.moveToNext();
                                        teste = false;
                                        Log.v("banco de dados", "verificado");
                                    }
                                }
                            }
                        } else {
                            String line3 = dado(line2);
                            mDbHelper.insertData(line3);
                            ins++;
                            cursor.moveToNext();
                            Log.v("banco de dados", "inserido");
                        }

                    }
                    test = true;

                }
                br.close();
                while (!cursor.isAfterLast()) {
                    mDbHelper.deleteDado(cursor.getString(16));
                    delet++;
                    cursor.moveToNext();
                }

                for (int k = 0; k < del.size(); k++) {
                    mDbHelper.deleteDado(del.get(k).toString());
                }

                mDbHelper.close();

                String message = "";
                if (ins == 0 && upd == 0 && delet == 0) {
                    message = "Sem modificações";
                } else {
                    if (ins != 0 && upd == 0 && delet == 0)
                        message = ins + " inserções";
                    else
                        message = ins + " inserções, " + upd + " atualizações, " + delet + " deleções";

                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                dateFormat.setLenient(false);
                String data_hora = dateFormat.format(new Date());

                SharedPreferences bd_version = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = bd_version.edit();
                editor.putString("date", data_hora);
                editor.apply();

                final String finalMessage = message;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Base de dados atualizado com sucesso\n" + finalMessage, Toast.LENGTH_SHORT).show();

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
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return csvLine;
    }

    public String dado(String[] line2) {
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

    private class DownloadFilesTask extends AsyncTask<URL, Void, List<String>> {
        ProgressDialog progressDialog;
        Handler handler = new Handler(context.getMainLooper());

        protected List<String> doInBackground(URL... urls) {
            return downloadRemoteTextFileContent();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //           Toast.makeText(context,"Atualizando base de dados",Toast.LENGTH_SHORT).show();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = ProgressDialog.show(context, "Atualizando base de dados", "Aguarde por favor", true, false);
                }
            });
        }

        protected void onPostExecute(List<String> result) {
            if (result != null) {
                //     printCVSContent(result);
                if (status != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(context, "sem conexão", Toast.LENGTH_SHORT).show();
                }
            }
            progressDialog.dismiss();

            Busca.init_query();
        }
    }

}
