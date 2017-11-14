package br.univesp.pi7sem2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.univesp.pi7sem2.BancoDeDados.TestAdapter;


public class Busca extends Fragment {
    SearchView searchView;
    View myView;
    Button myButton;
    boolean isUp;
    ArrayList<String> lables;
    static ArrayList<String> dado;
    static ArrayList<ArrayList<String>> dados;
    ListView listView;
    WebView wv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.busca, null);

        searchView=(SearchView) view.findViewById(R.id.searchView);
        searchView.setQueryHint("Busca");
        searchView.setIconifiedByDefault(true);

        myView = view.findViewById(R.id.my_view);
        myButton = (Button) view.findViewById(R.id.my_button);

        myView.setVisibility(View.GONE);
        isUp = false;

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUp) {

                    myView.setVisibility(View.GONE);

                } else {
                    myView.setVisibility(View.VISIBLE);

                }
                isUp = !isUp;


            }
        });

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),Resultado.class);
                intent.putExtra("posicao",position);
                startActivity(intent);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                wv.setVisibility(View.GONE);
                dados();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, lables);
                listView.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        wv = (WebView) view.findViewById(R.id.webView);
        wv.loadUrl("file:///android_asset/htmls/file.html");

    return view;
    }

    public void dados() {

        try {
            TestAdapter mDbHelper = new TestAdapter(getActivity());
            mDbHelper.createDatabase();
            mDbHelper.open();

            Cursor testdata = mDbHelper.getTestData();
            lables = new ArrayList<String>();
            dados = new ArrayList<>();
            ArrayList<String> temp=null;
            int colcount = testdata.getColumnCount();
            int j=1;
            while (!testdata.isAfterLast()) {
                temp = new ArrayList<>();
                for (int i = 0; i < colcount; i++) {
                    temp.add(testdata.getString(i));
                }
                dados.add(temp);
                lables.add(j + " - "+temp.get(6));
                testdata.moveToNext();
                j++;
            }
            mDbHelper.close();

        } catch (Exception e) {
        }
    }

    public static ArrayList<String> getDado(int position){
        dado = dados.get(position);
        return dado;
    }
}
