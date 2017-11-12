package br.univesp.pi7sem2;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
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
    List lables;
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
            int colcount = testdata.getColumnCount();

            while (!testdata.isAfterLast()) {
                for (int i = 0; i < colcount; i++) {
                    lables.add(testdata.getString(i));
                }
                testdata.moveToNext();
            }
            mDbHelper.close();

        } catch (Exception e) {
        }


    }
}
